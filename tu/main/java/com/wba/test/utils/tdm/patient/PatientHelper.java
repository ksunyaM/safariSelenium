/*
 * Copyright 2018 Walgreen Co.
 */
package com.wba.test.utils.tdm.patient;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;
import com.wba.test.utils.tdm.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PatientHelper {
    private Logger LOGGER = LoggerFactory.getLogger(PatientHelper.class);
    private DBConnection connection = DBConnection.CONNECT;


    //will parse all relevant info and call createUDPatient for each patient being created
    //"You have no idea the physical toll, that three rewrites of a function have on a person!" -Michael Scott (paraphrased)
    public String ud32PatientHelperFunction(String storeNum, String fName, String lName, String gender, String address,
                                            String areaCode, String phone, LocalDate dpv, String payType, String pid,
                                            String recipient, String group, String prescriber, boolean cobPlan, boolean petInd,
                                            String petType, String phoneType, String suffix, String middleInitial, String userId,
                                            String residenceCode, boolean vip_patient) {

        String pidEdit = payType.equals("TP") ? pid : "";
        String groupEdit = payType.equals("TP") ? group : "";
        String recipEdit = payType.equals("TP") ? recipient : "";//the way ud32 calls work is that if it's blank it wont set a value
        //so im basically setting all these values blank until i realize they are third party patients
        String cob_ind = payType.equals("TP") ? cobPlan ? "Y" : "" : "";
        if (residenceCode.equals("0")) residenceCode = "";//this means the option is set on none/null
        String isPet = petInd ? "Y" : "N";
        String petTypeEdit = petInd ? petType : "";
        String phoneTypeParam = phoneType.equals("Cell") ? "C" : phoneType.equals("Work") ? "W" : "H";
        String prescEdit = prescriber.equals("") ? "" : prescriber;

        LOGGER.info("Going to create patient");
        String id = createUDPatient(storeNum, fName, lName, gender, address, areaCode, phone, dpv, payType, pidEdit, recipEdit,
                groupEdit, prescEdit, cob_ind, isPet, petTypeEdit, phoneTypeParam, suffix, middleInitial, userId, residenceCode, vip_patient);
        LOGGER.info("Patient {} successfully created in the store {}", id, storeNum);
        return id;
    }

    /********************************************************************************
     *                                                                              *
     * BELOW IS THE UD32 CALLS                                                      *
     *                                                                              *
     * Before looking at these functions remember that Nietzsche quote:             *
     * "And if thou gaze long into an abyss, the abyss will also gaze into thee..." *
     *                                                                              *
     ********************************************************************************/

    //takes in a whoooooole bunch of arguments, calls ud32 directly, creates patients
    private String createUDPatient(String storeNum, String fName, String lName, String gender, String address,
                                   String areaCode, String phone, LocalDate dpv, String payType, String pid,
                                   String recipient, String group, String presc, String cobPlan, String patientPet,
                                   String petType, String phoneType, String suffix, String middleInitial, String userId,
                                   String residenceCode, boolean vip_pat) {
        String primaryPlan = payType.equals("TP") ? "Y" : "";//these should be empty unless thirdparty is true
        String numRows = payType.equals("TP") ? "1" : "";
        String trtypecdb = payType.equals("TP") ? "I" : "";
        int patStart = 0;
        long pat_id = -1;//will fail if this is not a unique id
        Connection storeConn = connection.connectToStoreDB(storeNum);
        Session session = connection.getStoreSession(storeNum);
        try {
            ResultSet rs = connection.executeQuery(storeConn, "select sq_patient_id.nextval from dual");//query to get latest pat_id

            if (rs.next()) patStart = rs.getInt(1);//get next pat_id value

            long tempInLong = (Integer.parseInt(storeNum) * 1000000L) + patStart;
            String temp = String.valueOf(tempInLong);//convert previous logic into id number
            pat_id = Long.parseLong(temp);//parse it into a long format

            //envrionment variables
            String envsetCommand = "export sqlplus=\"/usr/local/oracle/product/12.1/bin/sqlplus\"; export STORE_CODE=\"" + storeNum + "\";" +
                    "export PATH=\"/usr/local/p2000/tools:/usr/local/bea/tuxedo/bin:/usr/local/p2000/bin:/usr/local/bin:/usr/bin:/bin:/usr/games:.:/usr/local/p2000:/usr/local/p2000/bin:/usr/local/p2000/data:/usr/local/p2000/tools:/usr/local/p2000/etc:/usr/local/bin:/bin:/etc:/usr/bin:/usr/local/netpbm/bin:/usr/local/oracle/product/11.1/bin/:/usr/local/bea/tuxedo/lib:/usr/local/bea/tuxedo/bin:/usr/local/oracle:/usr/local/oracle/bin:/usr/local/oracle/product/12.1:/usr/local/oracle/product/12.1/bin:/usr/lbin:/usr/local/p2000/sblite/bin:/usr/local/p2000/sblite/etc:/usr/local/p2000/sblite/tools:/usr/local/stage/bin:/usr/local/bin:/usr/local/p2000/bin\"; " +
                    "export ORACLE_HOME=\"/usr/local/oracle/product/12.1\"; export DBLOG=\"rx2000\";" +
                    "export DBPWD=\"storesrv\"; export RELEASE=\"a\"; export ORACLE_SID=\"storesrv\"; export ORACLE_BASE=\"/usr/local/oracle\";" +
                    "export TNS_ADMIN=\"/usr/local/oracle/etc\"; export SVRMGR_RESOURCE=\"/usr/local/oracle/product/12.1/svrmgr/admin/resource/US\";" +
                    "export LD_LIBRARY_PATH=\"/usr/local/p2000/jre/lib:/usr/local/p2000/jre/lib/i386:/usr/local/p2000/jre/lib/i386/server:/usr/local/oracle/product/client32/lib:/usr/local/oracle/product/client32/jdbc/lib:/usr/local/bea/tuxedo/lib:/usr/local/lib:/usr/local/bea/tuxedo/lib:/usr/local/lib:/usr/spool/teleph\"; " +
                    "export TUXDIR=\"/usr/local/bea/tuxedo\"; export ROOTDIR=\"/usr/local/bea/tuxedo\"; export TUXCONFIG=\"/usr/local/p2000/bin/p2000." + storeNum + ".tux\";" +
                    "export FLDTBLDIR32=\"/usr/local/p2000/etc:/usr/local/bea/tuxedo/udataobj\"; export NLSPATH=\"/usr/local/bea/tuxedo/locale/C\"; " +
                    "export FIELDTBLS32=\"db.fml,nondb.fml,Usysfl32\"; export VIEWFILES32=\"viewsTEL2.V,viewsTEL1.V,views9.V,views8.V,views7.V,views6.V,views5.V,views4.V,views32.V,views31.V,views30.V,views3.V,views29.V,views28.V,views27.V,views26.V,views25.V,views24.V,views23.V,views22.V,views21.V,views20.V,views2.V,views19.V,views18.V,views17.V,views16.V,views15.V,views14.V,views13.V,views12.V,views11.V,views10.V,views1.V,\"; " +
                    "export VIEWDIR32=\"/usr/local/p2000/etc\"; ";


            LocalDate d = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/YYYY");//get current date with this logic
            String currentDate = d.format(formatter);//current date


            String command = "ud32 -n <<%\n" +
                    "WINDOW_ID\t31\n" +
                    "SYS_STATUS\t0\n" +
                    "CST_OFFSET\t0\n" +
                    "DB_ROUTING_NUM\t" + ((pat_id % 4) + 1) + "\n" +
                    "PAT_CC_EXP_DATE\tNot Viewable\n" + //not longer used since we do not store cc info
                    "PAT_PRIM_CARE_PBR_LOC_ID\t\n" +//primary location of prescriber (almost always 1)
                    "STORE_CODE\t" + storeNum + "\n" +//store number. Should be same as current store you are logged in on
                    "USER_ID\t" + userId + "\n" +//user id of person performing operation (tbf0_store_pharm_staff). This can actually be any value, as the system doesnt check to make sure its a valid id
                    "NUM_ROWS_5\t1\n" +
                    "AUTO_REFILL_PREF_STR_NBR\t\n" +
                    "USER_NM\tSUB\n" + //name of user_id 3 initials. This doesnt have to sync with userid, so they can be totally wrong
                    "APP_ID\t\n" +
                    "SRVCNM\tPSPTUDtlSvc\n" +//name of service
                    "TRAN_TYPE\tI\n" +
                    "EA_UPDATE_DTTM\t" + currentDate + "\n" +
                    "APP_SVR_CONN\t\n" +
                    "ARCH_VERSION\tb\n" +//this can be found on your store by doing 'echo $VERSION'
                    "IP_ADDR\t10.237.32.231\n" +//doesn't matter to my knowledge
                    "PAT_ALGY_HLTH_CD_INQ_IND\tY\n" +//if the patient has been asked regarding health conditions/allergies
                    "PAT_ALGY_HLTH_TYPE_CD\t0\n" +//should always be zero in this context, but can be a different value if given hc/allergies
                    "PAT_BIRTH_DTTM\t" + dpv.format(formatter) + "\n" +
                    "PAT_CITY\tDEERFIELD\n" +
                    "PAT_CLINIC_MEDICAL_ID\t\n" +
                    "PAT_CMTS\t\n" +
                    "PAT_DECEASED_IND\tN\n" +//why would you ever make a patient and immediately want them to die? No idea, but heres the indicator
                    "PAT_DISCOUNT_CD\tN\n" +
                    "PAT_FIRST_NAME\t" + fName.toUpperCase() + "\n" +
                    "PAT_HOH_RELATION_CD\t1\n" +//head of household relation. 1 = owner
                    "PAT_ID\t" + Long.toString(pat_id) + "\n" +
                    "PAT_LANG_PREF_CD\tE\n" +
                    "PAT_LAST_NAME\t" + lName.toUpperCase() + "\n" +
                    "PAT_MAIL_LIST_PREF\tY\n" +
                    "PAT_MAIL_SERVICE_ID\t\n" +
                    "PAT_MID_INIT\t" + middleInitial + "\n" +
                    "PAT_PET_IND\t" + patientPet + "\n" +//if the patient is an animal
                    "PAT_PET_TYPE\t" + petType + "\n" +//what type of animal? (obviously previous indicator should be Y)
                    "PAT_PICKUP_ID\t\n" +
                    "PAT_PREAPPR_PAY_IND\tX\n" +
                    "PAT_PRIM_PHONE_CD\tY\n" +//always marked as yes, or else no phone is considered primary
                    "PAT_SEX_CD\t" + gender.toUpperCase() + "\n" +
                    "PAT_SNAP_CAP_PREF\tY\n" +
                    "PAT_STATE\tIL\n" +
                    "PAT_STREET_ADDR\t" + address + "\n" +
                    "PAT_RESIDENCE_CD\t" + residenceCode + "\n" +
                    "PAT_SURNAME_SUFFIX\t" + suffix + "\n" +
                    "PAT_ZIP\t60015\n" +
                    "PAT_AREA_CD\t" + areaCode + "\n" +
                    "PAT_PHONE\t" + phone + "\n" +
                    "STORE_TYPE_CD\tR\n" +
                    "PAT_SIGNATURE_IND\tN\n" +
                    "PAT_INTERNET_IND\tN\n" +
                    "IMAGING_IND\tY\n" +
                    "PAT_REGISTRATION_DTTM\t" + currentDate + "\n";
            if (vip_pat)
                command = command + "PAT_VIP_DTTM\t" + currentDate + "\n";
            command = command + "EMAIL_ADDRESS\t\n" +
                    "PAT_CC_HOLDER_ZIP\t\n" +
                    "PAT_SR_DIV_REC_NBR\t\n" +
                    "PAT_HIPAA_IND\t\n" +
                    "PAT_CODE\t\n" +//i have no idea? it cna be a 20 char though
                    "BUYOUT_PAT_IND\t\n" +
                    "SEND_PHRM_NABP\t\n" +
                    "PAT_BROCHURE_IND\tY\n" +
                    "PAT_AUTOREFILL_IND\tY\n" +//if patient has autorefill enabled
                    "PAT_LARGE_PRINT_IND\tN\n" +
                    "REMOTE_FACILITY_CD\t\n" +
                    "REMOTE_ROLE_CD\tSS\n" +
                    "CUST_BALANCE_HOLD_IND\t\n" +
                    "PAT_HIPAA_AUTH_CD\t1\n" +
                    "PAT_HIPAA_AUTH_EXP\t\n" +
                    "PAT_HIPAA_AUTH_NAME\t\n" +
                    "PAT_HIPAA_AUTH_OTHER\t\n" +
                    "PAT_TEXT_MSG_IND\tN\n" +
                    "PAT_CALL_TIME_CODES\tDEW\n" +//when you can call patient (d = day, e = evening, w = weekend. you can combine any together to get both of them)
                    "PAT_PHONE_TYPE_CD\t" + phoneType + "\n" +//cellphone (C) home phone (H) work phone (W)
                    "PAT_PRIM_CARE_PBR_ID\t" + presc + "\n" +//primary presciber id. this can be null
                    "PAT_PICKUP_REL_CD\t\n" +
                    "PAT_PICKUP_ID_QLFR\t\n" +
                    "PAT_UNMERGE_VERIFY_IND\t\n" +
                    "PAT_PICKUP_GOV_AUTH_ID\t\n" +
                    "PAT_90DAY_PREF_IND\t\n" +
                    "PLAN_ID\t" + pid + "\n" +//third party plan
                    "GENERAL_RECIPIENT_NBR\t" + recipient + "\n" +//recipient of the third party plan (a number )
                    "PLAN_GROUP_NBR\t" + group + "\n" +//group for third party plan (D0PAYABLE, D0REJECT, etc)
                    "PAT_PLAN_PLCY_HLDR_ID\t" + pat_id + "\n" +//who holds the plan (doesn't always have to be patient itself)
                    "PAT_PLAN_PLCY_HLDR_REL\t1\n" +
                    "PAT_HOH_RELATION_CD\t1\n" +
                    "UPI\t\n" +
                    "PRIMARY_PLAN_IND\t" + primaryPlan + "\n" +//should always be yes if its the first one created
                    "WS_TIMESTAMP\t1297981992\n" +
                    "PAT_COB_IND\t" + cobPlan + "\n" + //indicates if a plan is a cob plan
                    "SUPLMTL_PLAN_AREA_CD\t\n" +
                    "SUPLMTL_PLAN_NAME\t\n" +
                    "SUPLMTL_PLAN_PHONE\t\n" +
                    "SUPLMTL_PLAN_STATUS_CD\t\n" +
                    "PAT_TEXT_MSG_IND\tN\n" +
                    //"PAT_LOCK_IND\t\n" +
                    "NUM_ROWS\t" + numRows + "\n" +//should be 1 if you are adding a third party plan
                    "TRAN_TYPE_CD_2\t" + trtypecdb + "\n\n" +//always I if third party plan
                    "%\n";

            Channel commandChannel = session.openChannel("exec");
            ((ChannelExec) commandChannel).setPty(true);

            ((ChannelExec) commandChannel).setCommand(envsetCommand + " " + command + " sleep 2");//set up command
            commandChannel.connect();

            InputStream in = commandChannel.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            StringBuilder errLog = new StringBuilder("Log:\n");
            boolean errorOnCreation = false;

            while ((line = reader.readLine()) != null) {
                errLog.append(line + "\n");
                if (line.contains("ERR_FILE_NAME") && line.length() > 17) {
                    errorOnCreation = true;
                }
            }
            if (errorOnCreation) {
                LOGGER.error("Error creating patient " + pat_id + ".");
                LOGGER.error(errLog.toString());
            } else
                LOGGER.info("Patient " + pat_id + " has been created.");
            commandChannel.disconnect();
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        return String.valueOf(pat_id);
    }
}




