/*
 * Copyright 2018 Walgreen Co.
 */
package com.wba.test.utils.tdm.prescription;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.Session;
import com.wba.test.utils.tdm.DBConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class RxHelper {
    private Logger LOGGER = LoggerFactory.getLogger(RxHelper.class);
    private DBConnection connection = DBConnection.CONNECT;
    private Random rand = new Random();

    private String envSetCommand = "export sqlplus=\"/usr/local/oracle/product/12.1/bin/sqlplus\"; export STORE_CODE=\"storeNumber\";" +
            "export PATH=\"/usr/local/p2000/tools:/usr/local/bea/tuxedo/bin:/usr/local/p2000/bin:/usr/local/bin:/usr/bin:/bin:/usr/games:.:/usr/local/p2000:/usr/local/p2000/bin:/usr/local/p2000/data:/usr/local/p2000/tools:/usr/local/p2000/etc:/usr/local/bin:/bin:/etc:/usr/bin:/usr/local/netpbm/bin:/usr/local/oracle/product/11.1/bin/:/usr/local/bea/tuxedo/lib:/usr/local/bea/tuxedo/bin:/usr/local/oracle:/usr/local/oracle/bin:/usr/local/oracle/product/12.1:/usr/local/oracle/product/12.1/bin:/usr/lbin:/usr/local/p2000/sblite/bin:/usr/local/p2000/sblite/etc:/usr/local/p2000/sblite/tools:/usr/local/stage/bin:/usr/local/bin:/usr/local/p2000/bin\"; " +
            "export ORACLE_HOME=\"/usr/local/oracle/product/12.1\"; export DBLOG=\"rx2000\";" +
            "export DBPWD=\"storesrv\"; export RELEASE=\"a\"; export ORACLE_SID=\"storesrv\"; export ORACLE_BASE=\"/usr/local/oracle\";" +
            "export TNS_ADMIN=\"/usr/local/oracle/etc\"; export SVRMGR_RESOURCE=\"/usr/local/oracle/product/12.1/svrmgr/admin/resource/US\";" +
            "export LD_LIBRARY_PATH=\"/usr/local/p2000/jre/lib:/usr/local/p2000/jre/lib/i386:/usr/local/p2000/jre/lib/i386/server:/usr/local/oracle/product/client32/lib:/usr/local/oracle/product/client32/jdbc/lib:/usr/local/bea/tuxedo/lib:/usr/local/lib:/usr/local/bea/tuxedo/lib:/usr/local/lib:/usr/spool/teleph\"; " +
            "export TUXDIR=\"/usr/local/bea/tuxedo\"; export ROOTDIR=\"/usr/local/bea/tuxedo\"; export TUXCONFIG=\"/usr/local/p2000/bin/p2000.storeNumber.tux\";" +
            "export FLDTBLDIR32=\"/usr/local/p2000/etc:/usr/local/bea/tuxedo/udataobj\"; export NLSPATH=\"/usr/local/bea/tuxedo/locale/C\"; " +
            "export FIELDTBLS32=\"db.fml,nondb.fml,Usysfl32\"; export VIEWFILES32=\"viewsTEL2.V,viewsTEL1.V,views9.V,views8.V,views7.V,views6.V,views5.V,views4.V,views32.V,views31.V,views30.V,views3.V,views29.V,views28.V,views27.V,views26.V,views25.V,views24.V,views23.V,views22.V,views21.V,views20.V,views2.V,views19.V,views18.V,views17.V,views16.V,views15.V,views14.V,views13.V,views12.V,views11.V,views10.V,views1.V,\"; " +
            "export VIEWDIR32=\"/usr/local/p2000/etc\"; ";

    //    just helps format/loop info for the actual ud32 call
    public String ud32SimpleCashRXHelper(String storeNum, String patId, String drug_id, String refillCount, String prescNum, String userId) {
        return createUDCashRx(storeNum, patId, drug_id, refillCount, prescNum, userId);
    }

    /**
     * I dont ask to suffer like this
     * <p>
     * The scripts call random functions for status changes
     * <p>
     * To make it SD: RD() -> SD()
     * To make it PR: UF() -> PR()
     * To make it UF: UF()
     * To make it RD: PR() -> FL() -> RD()
     */
    public boolean ud32UpdateRxStatusHelper(String storeNum, String rxNumber, String status, String userId) {
        Connection storeConn = connection.connectToStoreDB(storeNum);
        try {
            //region Get maximumfill number
            //need this to see the latest status in work queue
            ResultSet rs = connection.executeQuery(storeConn, "select max(fill_nbr) from tbf0_work_queue where rx_nbr = " + rxNumber + " and store_nbr = " + storeNum + " and fill_status_cd != 'DL'");//get current max fill number
            if (!rs.next()) throw new RuntimeException("SOMETHINGS GONE WRONG!");
            //endregion
            //region get current status
            rs = connection.executeQuery(storeConn, "select fill_status_cd from tbf0_work_queue where rx_nbr = " + rxNumber + " and " +
                    "store_nbr = " + storeNum + " and fill_nbr = " + rs.getString(1));
            if (!rs.next()) {
                LOGGER.error(rxNumber + " was not found in the store's database.");
            }
            //endregion
            // "'David Offord'? I've heard you're the worst programmer in the company!"
            // "Ah, but you have heard of me?"
            String previousStatus = rs.getString(1);
            switch (previousStatus) {
                case "EN": {
                    switch (status) {
                        case "EN":
                            LOGGER.info("Rx alredy in Entry status");
                            return true;
                        case "UF":
                            return udUpdateStatusUF(storeNum, rxNumber, userId);
                        case "PR":
                            udUpdateStatusUF(storeNum, rxNumber, userId);
                            return udUpdateStatusPR(storeNum, rxNumber, userId);
                        case "RD":
                            udUpdateStatusPR(storeNum, rxNumber, userId);
                            udUpdateStatusFL(storeNum, rxNumber, userId);
                            return udUpdateStatusRD(storeNum, rxNumber, userId);
                        case "SD":
                            udUpdateStatusRD(storeNum, rxNumber, userId);
                            return udUpdateStatusSD(storeNum, rxNumber, userId);
                        default:
                            throw new RuntimeException("Unknown status: " + status);
                    }
                }
                case "PR": {
                    switch (status) {
                        case "RD":
                            udUpdateStatusFL(storeNum, rxNumber, userId);
                            return udUpdateStatusRD(storeNum, rxNumber, userId);
                        case "SD":
                            udUpdateStatusRD(storeNum, rxNumber, userId);
                            return udUpdateStatusSD(storeNum, rxNumber, userId);
                        default:
                            throw new RuntimeException("Unknown status: " + status);
                    }
                }
                case "RD":
                    if (status.equals("SD")) {
                        udUpdateStatusRD(storeNum, rxNumber, userId);
                        return udUpdateStatusSD(storeNum, rxNumber, userId);
                    }
                    return true;
                case "PN":
                    LOGGER.info("Rx is currently in PN (pending) status. Please wait before trying to update again.");
                    return true;
                default:
                    LOGGER.info("Current status: " + previousStatus);
                    return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    //so makes an rx at target store, no scripts needed. like the third rewrite and each time i hate myself even more
    private String createUDCashRx(String storeNum, String patId, String drug_id, String refillCount, String prescNum, String userId) {
        int qty = -1;
        String rxNumber = "";
        String pickupDate = "", dateWithTime = "";
        Connection storeConn = connection.connectToStoreDB(storeNum);
        Session session = connection.getStoreSession(storeNum);
        try {
            LocalDateTime d = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/YYYY");//get current date with this logic
            String currentDate = d.format(formatter);//current date

            //region find fill_nbr_added
            int fill_nbr_added = rand.nextInt() % 5;
            while (fill_nbr_added <= 1) {
                fill_nbr_added = rand.nextInt() % 5;
            }
            //endregion
            //region QTY
            ResultSet rs = connection.executeQuery(storeConn, "select package_size from tbf0_drug where drug_id = " + drug_id);//query to get latest pat_id
            if (rs.next()) qty = rs.getInt(1);//get next pat_id value
            //endregion
            //region find drug class
            rs = connection.executeQuery(storeConn, "select drug_class from tbf0_drug where drug_id = " + drug_id);//query to get latest pat_id
            if (!rs.next()) throw new RuntimeException("Drug with id has not been found in tbf0_drug: " + drug_id);
            String drugClass = rs.getString(1) == null ? "RX" : rs.getString(1);
            //endregion
            //region find drug name
            rs = connection.executeQuery(storeConn, "select product_name_abbr from tbf0_drug where drug_id = " + drug_id);//query to get latest pat_id
            String drugName = rs.next() ? rs.getString(1) : "";
            //endregion
            //region Get pickupdate
            d = d.plusDays(1);
            formatter = DateTimeFormatter.ofPattern("MM/dd/YYYY HH:mm:ss");
            pickupDate = d.format(formatter);
            //endregion
            //region Get Sig
            rs = connection.executeQuery(storeConn, "select dtl.primary_decode from tbf0_drug drug, tbf0_Code_Dtl dtl " +
                    "where drug.default_sig = dtl.code and drug.drug_id =" + drug_id + " and dtl.category_cd = 89");//query to get latest pat_id
            String sig = rs.next() ? rs.getString(1) : "TK UTD";
            //endregion
            //region get date with time
            d = d.minusDays(1);
            dateWithTime = d.format(formatter);
            //endregion
            //region drug sub code
            rs = connection.executeQuery(storeConn, "select drug_type_cd from tbf0_drug where drug_id = " + drug_id);//query for drug sub code
            String drugSubCode = rs.next() ? rs.getString(1) : "";
            //endregion
            //region find rx_nbr
            rs = connection.executeQuery(storeConn, "select sq_rx_nbr.nextval from dual");//query to get latest pat_id
            int rx_nbr = rs.next() ? rs.getInt(1) : -1;
            //endregion
            String command = "ud32 -n <<%\n" +
                    "SRVCNM\tPSCMFillSvc\n" +
                    "WINDOW_ID\t83\n" +
                    "SYS_STATUS\t0\n" +
                    "DB_ROUTING_NUM\t" + ((Long.parseLong(patId) % 4) + 1) + "\n" +
                    "CST_OFFSET\t1\n" +
                    "PREV_FILL_NBR\t-32767\n" +
                    "PREV_FILL_PARTIAL_NBR\t-32767\n" +
                    "DUR_EFFORT_LEVEL_ARR\t0\n" +
                    "DUR_EFFORT_LEVEL_ARR\t0\n" +
                    "DUR_EFFORT_LEVEL_ARR\t0\n" +
                    "TPGM_DEFAULT_DAYS_SUPPLY\t0\n" +
                    "FILL_ADDED_FILLS\t0\n" +
                    "FILL_DAYS_SUPPLY\t30\n" +
                    "FILL_NBR_ADDED\t" + fill_nbr_added + "\n" +
                    "FILL_NBR_PRESCRIBED\t" + refillCount + "\n" +
                    "PBR_LOC_ID\t1\n" +
                    "PICKUP_HOUR\t4\n" +
                    "PICKUP_WEEK_DAY\t5\n" +
                    "REFILLS_REMAINING\t" + (Integer.parseInt(refillCount) - 1) + "\n" +
                    "WS_PIPE_ID\t1\n" +
                    "FILL_NBR_DISP_TRANS\t0\n" +
                    "CUSTOM_AUTO_FILL_COUNT\t-32767\n" +
                    "CUSTOM_AUTO_FILL_DS\t-32767\n" +
                    "DELAY_REASON_CD\t0\n" +
                    "PLACE_OF_SERVICE\t0\n" +
                    "COB_DELAY_REASON_CD\t0\n" +
                    "COB_PLACE_OF_SERVICE\t0\n" +
                    "STORE_CODE\t" + storeNum + "\n" +
                    "RX_NBR\t" + rx_nbr + "\n" +
                    "USER_ID\t" + userId + "\n" +
                    "RPH_USER_ID\t" + userId + "\n" +
                    "EA_WIN32_PID\t7312\n" +
                    "PREF_M_DRUG_ID\t0\n" +
                    "DRUG_ID\t" + drug_id + "\n" +
                    "FILL_VERIFIED_USER_ID\t" + userId + "\n" +
                    "ORDERED_DRUG_ID\t" + drug_id + "\n" +
                    "TP_REPLY_SVC_ID\t14\n" +
                    "UPDATE_USER_ID\t" + userId + "\n" +
                    "SCANNED_USER_ID\t" + userId + "\n" +
                    "FILL_ADDED_QTY\t0.000000\n" +
                    "FILL_DISCOUNT_AMT\t0.000000\n" +
                    "FILL_QTY_DISPENSED\t" + (qty - 1) + "\n" +
                    "RX_TOTAL_DISPENSED_QTY\t" + (qty - 1) + "\t\n" +
                    "FILL_RETAIL_PRICE_AMT\t42.290000\n" +
                    "FILL_SAVINGS_AMT\t1.111112\n" +
                    "PLAN_SUBMTD_COPAY_AMT\t0.000000\n" +
                    "PLAN_SUBMTD_COST_AMT\t50.400000\n" +
                    "PLAN_SUBMTD_FEE_AMT\t7.250000\n" +
                    "RX_ADDED_QTY\t0.000000\n" +
                    "RX_ORIGINAL_QTY\t" + qty + "\n" +
                    "WS_WINDOW_HANDLE\t14026510.000000\n" +
                    "PAT_BRAND_COPAY\t1.111112\n" +
                    "PAT_GENERIC_COPAY\t1.111112\n" +
                    "PLAN_INCENT_AMT_SUBMTD\t0.000000\n" +
                    "PLAN_OTHER_AMT_SUBMTD\t0.000000\n" +
                    "MANUAL_CLAIM_QTY\t0.000000\n" +
                    "CUST_COPAY\t0.000000\n" +
                    "EQUIVALENT_GENERIC_COPAY\t1.111112\n" +
                    "COB_PLN_INCNT_AMT_SUBMTD\t0.000000\n" +
                    "USER_NM\tAAA\n" +
                    "APP_ID\t\n" +
                    "EA_UPDATE_DTTM\t" + dateWithTime + "\n" +
                    "USER_INIT\tQAA\n" +
                    "RPH_INIT\tQAA\n" +
                    "SINGLE_USER_MODE_IND\tN\n" +
                    "USER_FIRST_NAME\tQA\n" +
                    "USER_LAST_NAME\tAUTOMATION\n" +
                    "EXC_IND\tN\n" +
                    "PREV_EXC_EXIST_IND\tN\n" +
                    "FILL_IN_PROGRESS_IND\tN\n" +
                    "PAT_DOWNLOAD_IND\tN\n" +
                    "PBR_DOWNLOAD_IND\tN\n" +
                    "RX_DOWNLOAD_IND\tN\n" +
                    "SEND_FAX_TO_PBR_IND\tN\n" +
                    "SEND_TO_PRESCRIBE_IND\tN\n" +
                    "APP_SVR_CONN\t\n" +
                    "ARCH_VERSION\tb\n" +
                    "PRINT_THRESHOLD\t60\n" +
                    "DUR_INTERVENTION_CD_ARR\t\n" +
                    "DUR_INTERVENTION_CD_ARR\t\n" +
                    "DUR_INTERVENTION_CD_ARR\t\n" +
                    "DUR_CONFLICT_CD_ARR\t\n" +
                    "DUR_CONFLICT_CD_ARR\t\n" +
                    "DUR_CONFLICT_CD_ARR\t\n" +
                    "DUR_OUTCOME_CD_ARR\t\n" +
                    "DUR_OUTCOME_CD_ARR\t\n" +
                    "DUR_OUTCOME_CD_ARR\t\n" +
                    "MAN_CLM_DIAGNOSIS_CD\t\n" +
                    "ROUTING_IND\tN\n" +
                    "SUB_COMPLICATED_SUP_IND\tN\n" +
                    "SUB_SPECIALTY_REV_IND\tN\n" +
                    "SUB_CLINICAL_VALUE_IND\t\n" +
                    "SUB_REQUIRED_SUP_IND\t\n" +
                    "MOCKUP_IND\tN\n" +
                    "TPGR_ACK_IND\tN\n" +
                    "DEST_ADDR\tNA\n" +
                    "ORIG_EXC_REASON_CD\t\n" +
                    "COPY_STORE_NAME\t\n" +
                    "COPY_PHONE_AREA_CD\t\n" +
                    "COPY_PHONE_NBR\t\n" +
                    "AVOID_DATA_REVIEW_IND\tN\n" +
                    "CMD_RESOLVED_IND\t\n" +
                    "AVOID_MEDISPAN_IND\tN\n" +
                    "CREATE_DTTM\t\n" +
                    "DIAGNOSIS_CD\t\n" +
                    "DRUG_CLASS\t" + drugClass + "\n" +
                    "DRUG_NAME\t" + drugName + "\n" + //HERE
                    "DRUG_NON_SYSTEM_CD\t\n" +
                    "DRUG_SUB_CD\t" + drugSubCode + "\n" +
                    "DUR_CONFLICT_CD\t\n" +
                    "DUR_INTERVENTION_CD\t\n" +
                    "DUR_OUTCOME_CD\t\n" +
                    "ELIGIBILITY_OVERRIDE_CD\t\n" +
                    "EXC_CMT\t\n" +
                    "FILL_ASSIGNED_PBR_NBR\t\n" +
                    "FILL_AUTO_IND\t\n" +
                    "FILL_DISCOUNT_CD\tN\n" +
                    "FILL_EST_PICK_UP_DTTM\t" + pickupDate + "\n" +
                    "FILL_PAY_METHOD_CD\tC\n" +
                    "FILL_PICKUP_CD\tF\n" +
                    "FILL_PREPAID_IND\tX\n" +
                    "FILL_PRICE_VARIANCE_CD\tN\n" +
                    "FILL_PRORATED_PRICE_IND\tN\n" +
                    "FILL_TYPE_CD\tN\n" +
                    "FILL_UNLIMITED_IND\tN\n" +
                    "FILL_VERIFIED_USER_INITS\tSAR\n" +
                    "GENERAL_RECIPIENT_NBR\t\n" +
                    "IP_ADDR\t10.237.22.155\n" +
                    "LABEL_COMMENTS\t\n" +
                    "NCPDP_VERSION\t5\n" +
                    "PAT_ID\t" + patId + "\n" +
                    "PAT_PICKUP_ID\t\n" +
                    "PAY_CD\t0\n" +
                    "PBR_ID\t" + prescNum + "\n" +
                    "PBR_LAST_NAME\t\n" +
                    "PLAN_ID\t\n" +
                    "PRICED_AS_NONSYS_IND\t\n" +
                    "PRIOR_AUTH_CD\t\n" +
                    "PRIOR_AUTH_NBR\t\n" +
                    "REFILLS_REMAIN_WHEN_ENT\t19\t\n" +
                    "RX_COPY_COMMENTS\t\n" +
                    "RX_DAW_IND\tN\n" +
                    "RX_DENIAL_OVERRIDE_CD\t\n" +
                    "RX_EXP_DTTM\t07/07/2020 00:00:00\n" +
                    "RX_PRINT_DRUG_CD\tN\n" +
                    "RX_REFILLS_BY_DTTM\t07/07/2020 00:00:00\n" +
                    "RX_SIG\t" + sig + "\n" +
                    "RX_WRITTEN_DTTM\t" + dateWithTime + "\n" +
                    "SOLD_IND\tN\n" +
                    "TRANSACTION_TYPE\tF\n" +
                    "UPDATE_DTTM\t\n" +
                    "WS_TIMESTAMP\t1278538509\n" +
                    "CALLER_NAME\t\n" +
                    "FILL_SEND_TO_CELL_IND\tN\n" +
                    "RX_PRINT_SAVINGS_IND\tY\n" +
                    "STORE_MAILING_STATE\tIL\n" +
                    "STORE_TYPE_CD\tR\n" +
                    "TRIPLICATE_SERIAL_NBR\t\n" +
                    "IMAGING_IND\tY\n" +
                    "FILL_START_DTTM\t" + dateWithTime + "\n" +
                    "GENERIC_SUBS_PREF\tY\n" +
                    "GENERIC_SUBSTITUTED_IND\t\n" +
                    "BASIS_OF_COST_DETERM\t01\n" +
                    "ORIGIN_CD\t\n" +
                    "PARTIAL_FILL_VERSION_CD\t\n" +
                    "PLAN_OTHER_AMT_SUBM_TYPE\t\n" +
                    "FILL_WAITING_IND\tY\n" +
                    "PAT_COB_IND\t\n" +
                    "LVL_OF_SVC_CD\t\n" +
                    "PAT_CODE\t\n" +
                    "BUYOUT_RX_IND\t\n" +
                    "RESOURCE_CD\t\n" +
                    "PREG_IND\t\n" +
                    "PRIOR_AUTH_IND\t\n" +
                    "REFERRED_IND\t\n" +
                    "MEDICARE_STATUS_IND\t\n" +
                    "NURSE_HOME_IND\t\n" +
                    "ITEM_MODIFIER_CD\t\n" +
                    "MANUAL_CLAIM_IND\t\n" +
                    "MANUAL_CLAIM_NDC\t\n" +
                    "MANUAL_CLAIM_REMARKS\t\n" +
                    "HCPCS_CD\t\n" +
                    "EMERGENCY_IND\t\n" +
                    "EPSDT_IND\t\n" +
                    "FAMILY_PLANING_IND\t\n" +
                    "DIAGNOSIS_CD_QUALIFIER\t\n" +
                    "COMPOUND_DRUG_IND\t\n" +
                    "COUNSELING_IND\t\n" +
                    "CUSTOM_AUTO_FILL_FREQ\t\n" +
                    "ADD_INFO1\t\n" +
                    "ADD_INFO2\t\n" +
                    "ATTACH_IND\t\n" +
                    "SEND_PLAN_IND\t\n" +
                    "TPL_CD\t\n" +
                    "TREATING_TYPE_CD\t\n" +
                    "UNIT_DOSE\t\n" +
                    "VISIT_CD\t\n" +
                    "PBR_ID_QUALIFIER\t\n" +
                    "IMAGE_ID\t\n" +
                    "RX_OVERRIDE_IND\tY\n" +
                    "SCANNED_DTTM\t" + currentDate + "\n" +
                    "REMOTE_FACILITY_CD\t\n" +
                    "REMOTE_ROLE_CD\tSS\n" +
                    "COB_DIAGNOSIS_CD\t\n" +
                    "COB_FILL_ASSIGN_PBR_NBR\t\n" +
                    "COB_PBR_ID_QUALIFIER\t\n" +
                    "COB_PRIOR_AUTH_CD\t\n" +
                    "COB_PRIOR_AUTH_NBR\t\n" +
                    "RX_TRNSF_CLOSE_REASON_CD\t\n" +
                    "THRD_PTY_GRP_RULE\t\n" +
                    "COB_DIAGNOSIS_CD_QLFR\t\n" +
                    "COB_ELGBLTY_OVERRIDE_CD\t\n" +
                    "COB_LVL_OF_SVC_CD\t\n" +
                    "COB_ORIGIN_CD\t\n" +
                    "COB_RX_DENIAL_OVRIDE_CD\t\n" +
                    "PBR_ORDER_NBR\t\n" +
                    "DISC_PREF_IND\t\n" +
                    "DISC_PREF_PLAN_ID\t\n" +
                    "DPS_NBR\t\n" +
                    "DIAGNOSIS_CD_2\t\n" +
                    "DIAGNOSIS_CD_3\t\n" +
                    "DIAGNOSIS_CD_4\t\n" +
                    "PAT_GENERIC_RQST_IND\t\n" +
                    "AUTOFILL_INSTR\t\n" +
                    "BRAND_INSTR\t\n" +
                    "HOLD_INSTR\t\n" +
                    "OTHER_INSTR\t\n" +
                    "PLAN_ID_INSTR\t\n" +
                    "PULLBACK_THRESHOLD_DTTM\t\n" +
                    "QTY_INSTR\t\n" +
                    "RX_APDP_CLM_CD\t\n" +
                    "RX_APDP_IND\t\n" +
                    "COB_MAJOR_MED_PLAN_ID\t\n" +
                    "RX_PAD_BARCODE\t\n" +
                    "RX_PAD_PROGRAM_IND\t\n" +
                    "DIAGNOSIS_CD_5\t\n" +
                    "INTERMEDIARY_AUTH_ID\t\n" +
                    "RX_DENIAL_OVERRIDE_CD_2\t\n" +
                    "RX_DENIAL_OVERRIDE_CD_3\t\n" +
                    "DIAGNOSIS_CD_QUALIFIER_2\t\n" +
                    "DIAGNOSIS_CD_QUALIFIER_3\t\n" +
                    "DIAGNOSIS_CD_QUALIFIER_4\t\n" +
                    "DIAGNOSIS_CD_QUALIFIER_5\t\n" +
                    "IMAGE_SOURCE_CD\t\n" +
                    "RX_VACCINE_OVERRIDE_IND\tY\n" +
                    "COB_DIAGNOSIS_CD_4\t\n" +
                    "COB_DIAGNOSIS_CD_QLFR_4\t\n" +
                    "COB_RX_DENIAL_OVRIDE_CD3\t\n" +
                    "COB_INTERMEDIARY_AUTH_ID\t\n" +
                    "COB_DIAGNOSIS_CD_QLFR_5\t\n" +
                    "COB_DIAGNOSIS_CD_3\t\n" +
                    "COB_DIAGNOSIS_CD_5\t\n" +
                    "COB_DIAGNOSIS_CD_QLFR_2\t\n" +
                    "COB_DIAGNOSIS_CD_2\t\n" +
                    "COB_DIAGNOSIS_CD_QLFR_3\t\n" +
                    "PWR_PRIORITY_IND\tL\n" +
                    "RX_LAST_TP_FILL_DTTM\t\n" +
                    "\n" +
                    "%\n";

            String envCommand = envSetCommand.replace("storeNumber", storeNum);
            boolean isSuccessful = execSSH(session, envCommand + " " + command + " sleep 7");
            rs = connection.executeQuery(storeConn, "select * from tbf0_work_queue where rx_nbr = " + rx_nbr);
            if (!isSuccessful && !rs.next()) {
                LOGGER.error("Error creating rx " + rx_nbr + ".");
                rxIssueFinder(storeNum, prescNum, patId, "C", String.valueOf(rx_nbr));
            } else {
                LOGGER.info("Rx " + rx_nbr + " has been created.");
                rxNumber = String.valueOf(rx_nbr);
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        if (!rxNumber.equals("")) return rxNumber;
        else throw new RuntimeException("Rx has not been created");
    }

    //this is for status reviewed. Printed also calls this.
    private boolean udUpdateStatusUF(String storeNum, String rx_nbr, String userId) {
        Connection storeConn = connection.connectToStoreDB(storeNum);
        Session session = connection.getStoreSession(storeNum);
        try {
            //region Get maximumfill number
            ResultSet rs = connection.executeQuery(storeConn, "select max(fill_nbr) from tbf0_work_queue where rx_nbr = " + rx_nbr + " and store_nbr = " + storeNum + " and fill_status_cd != 'DL'");//get current max fill number
            String fill_nbr = rs.next() ? rs.getString(1) : "";
            //region Get fill partial number
            rs = connection.executeQuery(storeConn, "select fill_partial_nbr from tbf0_work_queue where rx_nbr = " + rx_nbr + " and store_nbr = " + storeNum + " and fill_nbr = " + fill_nbr);//get fill partial
            String partialfillNbr = rs.next() ? rs.getString(1) : "";
            //region Get fill qty remaining
            rs = connection.executeQuery(storeConn, "select qty_remaining from tbf0_work_queue where rx_nbr = " + rx_nbr + " and store_nbr = " + storeNum + " and fill_nbr = " + fill_nbr);//get qty remaining
            String qtyRemaining = rs.next() ? rs.getString(1) : "";
            //region Get qty prescribed
            rs = connection.executeQuery(storeConn, "select TOTAL_QTY_PRESCRIBED from tbf0_rx where rx_nbr = " + rx_nbr + " and store_nbr = " + storeNum);//get qty remaining
            String totalQtyPrescribed = rs.next() ? rs.getString(1) : "";
            //region Get Pat_id
            rs = connection.executeQuery(storeConn, "select pat_id from tbf0_work_queue where rx_nbr = " + rx_nbr + " and store_nbr = " + storeNum);//get qty remaining
            String pat_id = rs.next() ? rs.getString(1) : "";
            //region current time
            LocalDateTime d = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/YYYY HH:mm:ss");
            String dateWithTime = d.format(formatter);
            //region COmmand
            String command = "ud32 -n <<%\n" +
                    "SRVCNM\tPSVRREVUpdSvc\n" +
                    "WINDOW_ID\t540\n" +
                    "SYS_STATUS\t0\n" +
                    "CST_OFFSET\t0\n" +
                    "DB_ROUTING_NUM\t" + ((Long.parseLong(pat_id) % 4) + 1) + "\n" +
                    "FILL_NBR\t" + fill_nbr + "\n" +
                    "FILL_PARTIAL_NBR\t" + partialfillNbr + "\n" +
                    "STORE_CODE\t" + storeNum + "\n" +
                    "USER_ID\t" + userId + "\n" +
                    "RX_NBR\t" + rx_nbr + "\n" +
                    "STORE_NBR\t" + storeNum + "\n" +
                    "DATA_REV_SPEC_ID\t" + storeNum + "\n" +
                    "DATA_REV_SPEC_STORE_NBR\t" + storeNum + "\n" +
                    "QTY_REMAINING\t" + qtyRemaining + "\n" +
                    "TOTAL_QTY_PRESCRIBED\t" + totalQtyPrescribed + "\n" +
                    "USER_NM\tQAA\n" +
                    "APP_ID\t\n" +
                    "EA_UPDATE_DTTM\t" + dateWithTime + "\n" +
                    "APP_SVR_CONN\t\n" +
                    "ARCH_VERSION\tb\n" +
                    "DEST_ADDR\t\n" +
                    "IP_ADDR\t10.237.32.231\n" +
                    "LABEL_COMMENTS\t\n" +
                    "PAT_ID\t" + pat_id + "\n" +
                    "RX_COMMENT\t\n" +
                    "TRANSACTION_TYPE\tD\n" +
                    "WS_TIMESTAMP\t1338313530\n" +
                    "STORE_TYPE_CD\tR\n" +
                    "IMAGING_IND\tN\n" +
                    "FILL_DATA_REV_DTTM\t\n" +
                    "FILL_DATA_REV_IND\tA\n" +
                    "REMOTE_FACILITY_CD\t\n" +
                    "REMOTE_ROLE_CD\tXX\n" +
                    "DATA_REV_SPEC_DTTM\t\n" +
                    "CELGENE_MD_AUTH_NBR\t\n" +
                    "\n" +
                    "%\n";
            String envCommand = envSetCommand.replace("storeNumber", storeNum);
            return execSSH(session, envCommand + " " + command + " sleep 3");
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    //this is for status Printed.
    private boolean udUpdateStatusPR(String storeNum, String rx_nbr, String userId) {
        Connection storeConn = connection.connectToStoreDB(storeNum);
        Session session = connection.getStoreSession(storeNum);
        try {
            //region Get maximumfill number
            ResultSet rs = connection.executeQuery(storeConn, "select max(fill_nbr) from tbf0_work_queue where rx_nbr = " + rx_nbr + " and store_nbr = " + storeNum + " and fill_status_cd != 'DL'");//get current max fill number
            String fill_nbr = rs.next() ? rs.getString(1) : "";
            //region Get fill partial number
            rs = connection.executeQuery(storeConn, "select fill_partial_nbr from tbf0_work_queue where rx_nbr = " + rx_nbr + " and store_nbr = " + storeNum + " and fill_nbr = " + fill_nbr);//get fill partial
            String partialfillNbr = rs.next() ? rs.getString(1) : "";
            //region Get fill nbr dispensed
            rs = connection.executeQuery(storeConn, "select fill_nbr_dispensed from tbf0_work_queue where rx_nbr = " + rx_nbr + " and store_nbr = " + storeNum + " and fill_nbr = " + fill_nbr);//get qty remaining
            String fillNbrDispensed = rs.next() ? rs.getString(1) : "";
            //region Get Pat_id
            rs = connection.executeQuery(storeConn, "select pat_id from tbf0_work_queue where rx_nbr = " + rx_nbr + " and store_nbr = " + storeNum);//get qty remaining
            String pat_id = rs.next() ? rs.getString(1) : "";
            //region current time
            rs = connection.executeQuery(storeConn, "select to_char(sysdate,'YYYYMMDDHH24MISS') from dual");//get qty remaining
            String printtime = rs.next() ? rs.getString(1) : "";
            //region COmmand
            String command = "ud32 -n <<%\n" +
                    "SRVCNM\tPRINTUPDSvc\n" +
                    "USER_NM\t\n" +
                    "USER_ID\t" + userId + "\n" +
                    "RPH_OF_REC_USER_ID\t" + userId + "\n" +
                    "UPDATE_USER_ID\t70020\n" +
                    "STORE_CODE\t" + storeNum + "\n" +
                    "WINDOW_ID\t0\n" +
                    "APP_ID\t\n" +
                    "SYS_STATUS\t0\n" +
                    "EA_UPDATE_DTTM\t" + printtime + "\n" +
                    "CST_OFFSET\t\n" +
                    "DB_ROUTING_NUM\t" + ((Long.parseLong(pat_id) % 4) + 1) + "\n" +
                    "IP_ADDR\t10.237.32.231\n" +
                    "APP_SVR_CONN\t\n" +
                    "ARCH_VERSION\tb\n" +
                    "STORE_TYPE_CD\tR\n" +
                    "EA_WIN32_PID\t\n" +
                    "TPA_STATUS\t\n" +
                    "IMAGING_IND\tY\n" +
                    "REMOTE_ROLE_CD\tSS\n" +
                    "REMOTE_FACILITY_CD\tR\n" +
                    "RX_NBR\t" + rx_nbr + "\n" +
                    "FILL_NBR\t" + fill_nbr + "\n" +
                    "FILL_PARTIAL_NBR\t" + partialfillNbr + "\n" +
                    "FILL_STATUS_CD\tPR\n" +
                    "FILL_PRINT_DTTM\t" + printtime + "\n" +
                    "PAT_ID\t" + pat_id + "\n" +
                    "RPH_OF_REC_INITS\tQAA\n" +
                    "UPDATE_DTTM\t" + printtime + "\n" +
                    "FILL_AUTO_IND\t\n" +
                    "FILL_NBR_DISPENSED\t" + fillNbrDispensed + "\n" +
                    "\n" +
                    "%\n";
            //endregion
            String envCommand = envSetCommand.replace("storeNumber", storeNum);
            return execSSH(session, envCommand + " " + command + " sleep 3");
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    //Set to fill status
    private boolean udUpdateStatusFL(String storeNum, String rx_nbr, String userId) {
        Connection storeConn = connection.connectToStoreDB(storeNum);
        Session session = connection.getStoreSession(storeNum);
        try {
            //region Get maximumfill number          
            ResultSet rs = connection.executeQuery(storeConn, "select max(fill_nbr) from tbf0_work_queue where rx_nbr = " + rx_nbr + " and store_nbr = " + storeNum + " and fill_status_cd != 'DL'");//get current max fill number
            String fill_nbr = rs.next() ? rs.getString(1) : "";
            //region Get fill partial number
            rs = connection.executeQuery(storeConn, "select fill_partial_nbr from tbf0_work_queue where rx_nbr = " + rx_nbr + " and store_nbr = " + storeNum + " and fill_nbr = " + fill_nbr);//get fill partial
            String partialfillNbr = rs.next() ? rs.getString(1) : "";
            //region current time
            LocalDateTime d = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/YYYY HH:mm:ss");
            String dateWithTime = d.format(formatter);
            //region Command
            String command = "ud32 -n <<%\n" +
                    "SRVCNM\tPSWQUWQSSvc\n" +
                    "WINDOW_ID\t540\n" +
                    "SYS_STATUS\t0\n" +
                    "CST_OFFSET\t0\n" +
                    "DB_ROUTING_NUM\t0\n" +
                    "FILL_NBR\t" + fill_nbr + "\n" +
                    "FILL_PARTIAL_NBR\t" + partialfillNbr + "\n" +
                    "STORE_CODE\t" + storeNum + "\n" +
                    "USER_ID\t" + userId + "\n" +
                    "RX_NBR\t" + rx_nbr + "\n" +
                    "USER_NM\tQAA\n" +
                    "APP_ID\t\n" +
                    "EA_UPDATE_DTTM\t" + dateWithTime + "\n" +
                    "APP_SVR_CONN\tC1\n" +
                    "ARCH_VERSION\tb\n" +
                    "DEST_ADDR\tNA\n" +
                    "IP_ADDR\t10.237.32.237\n" +
                    "TRANSACTION_TYPE\tO\n" +
                    "WS_TIMESTAMP\t1338313530\n" +
                    "STORE_TYPE_CD\tR\n" +
                    "IMAGING_IND\tY\n" +
                    "OVERRIDE_REASON_CD\tNP\n" +
                    "OVERRIDE_REASON_NDC\t\n" +
                    "REMOTE_FACILITY_CD\t\n" +
                    "REMOTE_ROLE_CD\tXX\n" +
                    "\n" +
                    "%\n";
            String envCommand = envSetCommand.replace("storeNumber", storeNum);
            return execSSH(session, envCommand + " " + command + " sleep 3");
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    //Making RX ready
    private boolean udUpdateStatusRD(String storeNum, String rx_nbr, String userId) {
        Connection storeConn = connection.connectToStoreDB(storeNum);
        Session session = connection.getStoreSession(storeNum);
        try {
            //region Get maximumfill number            
            ResultSet rs = connection.executeQuery(storeConn, "select max(fill_nbr) from tbf0_work_queue where rx_nbr = " + rx_nbr + " and store_nbr = " + storeNum + " and fill_status_cd != 'DL'");//get current max fill number
            String fill_nbr = rs.next() ? rs.getString(1) : "";
            //region Get fill partial number
            rs = connection.executeQuery(storeConn, "select fill_partial_nbr from tbf0_work_queue where rx_nbr = " + rx_nbr + " and store_nbr = " + storeNum + " and fill_nbr = " + fill_nbr);//get fill partial
            String partialfillNbr = rs.next() ? rs.getString(1) : "";
            //region Get fill entered dttm
            rs = connection.executeQuery(storeConn, "select to_char(FILL_ENTERED_DTTM,'MM/DD/YYYY HH24:MI:SS') from tbf0_work_queue where rx_nbr = " + rx_nbr + " and store_nbr = " + storeNum + " and fill_nbr = " + fill_nbr);//get qty remaining
            String fillEntered = rs.next() ? rs.getString(1) : "";
            //region Fill est pick up dttm
            rs = connection.executeQuery(storeConn, "select to_char(FILL_EST_PICK_UP_DTTM,'MM/DD/YYYY HH24:MI:SS') from tbf0_work_queue where rx_nbr = " + rx_nbr + " and store_nbr = " + storeNum + " and fill_nbr = " + fill_nbr);//get qty remaining
            String estPickUpDttm = rs.next() ? rs.getString(1) : "";
            //region Get Pat_id
            rs = connection.executeQuery(storeConn, "select pat_id from tbf0_work_queue where rx_nbr = " + rx_nbr + " and store_nbr = " + storeNum);//get qty remaining
            String pat_id = rs.next() ? rs.getString(1) : "";
            //region current time
            LocalDateTime d = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/YYYY HH:mm:ss");
            String dateWithTime = d.format(formatter);
            //region Command
            String command = "ud32 -n <<%\n" +
                    "SRVCNM\tPSWQUWQSSvc\n" +
                    "WINDOW_ID\t540\n" +
                    "SYS_STATUS\t0\n" +
                    "CST_OFFSET\t0\n" +
                    "DB_ROUTING_NUM\t0\n" +
                    "FILL_NBR\t" + fill_nbr + "\n" +
                    "FILL_PARTIAL_NBR\t" + partialfillNbr + "\n" +
                    "STORE_CODE\t" + storeNum + "\n" +
                    "USER_ID\t" + userId + "\n" +
                    "RX_NBR\t" + rx_nbr + "\n" +
                    "USER_NM\tQAA\n" +
                    "APP_ID\t\n" +
                    "EA_UPDATE_DTTM\t" + dateWithTime + "\n" +
                    "APP_SVR_CONN\tC1\n" +
                    "ARCH_VERSION\tb\n" +
                    "DEST_ADDR\tNA\n" +
                    "IP_ADDR\t10.237.32.237\n" +
                    "WS_TIMESTAMP\t1338313530\n" +
                    "STORE_TYPE_CD\tR\n" +
                    "IMAGING_IND\tY\n" +
                    "REMOTE_FACILITY_CD\t\n" +
                    "REMOTE_ROLE_CD\tXX\n" +
                    "PAT_LARGE_PRINT_IND\tN\n" +
                    "FILL_RPH_OF_RECORD_ID\t" + userId + "\n" +
                    "FILL_STATUS_CD\tRD\n" +
                    "FILL_ENTERED_DTTM\t" + fillEntered + "\n" +
                    "FILL_EST_PICK_UP_DTTM\t" + estPickUpDttm + "\n" +
                    "FILL_VERIFIED_USER_INITS\tQAA\n" +
                    "FILL_PRODUCT_REV_IND\tA\n" +
                    "\n" +
                    "%\n";
            //endregion
            String envCommand = envSetCommand.replace("storeNumber", storeNum);
            return execSSH(session, envCommand + " " + command + " sleep 3");
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    //Set to sold Status
    private boolean udUpdateStatusSD(String storeNum, String rx_nbr, String userId) {
        Connection storeConn = connection.connectToStoreDB(storeNum);
        Session session = connection.getStoreSession(storeNum);
        try {
            //region Get maximumfill number            
            ResultSet rs = connection.executeQuery(storeConn, "select max(fill_nbr) from tbf0_work_queue where rx_nbr = " + rx_nbr + " and store_nbr = " + storeNum + " and fill_status_cd != 'DL'");//get current max fill number
            String fill_nbr = rs.next() ? rs.getString(1) : "";
            //region Get fill dataRevID
            rs = connection.executeQuery(storeConn, "select FILL_DATA_REV_ID from tbf0_work_queue where rx_nbr = " + rx_nbr + " and store_nbr = " + storeNum + " and fill_nbr = " + fill_nbr);//get filldatarevid
            String fillDataRevId = rs.next() ? rs.getString(1) : "";
            //region Get fill Label Price
            rs = connection.executeQuery(storeConn, "select FILL_LABEL_PRICE_AMT from tbf0_work_queue where rx_nbr = " + rx_nbr + " and store_nbr = " + storeNum + " and fill_nbr = " + fill_nbr);//get label price
            String fillLabelPriceAmt = rs.next() ? rs.getString(1) : "";
            //region Get fill qty dispensed
            rs = connection.executeQuery(storeConn, "select FILL_QTY_DISPENSED from tbf0_work_queue where rx_nbr = " + rx_nbr + " and store_nbr = " + storeNum + " and fill_nbr = " + fill_nbr);//get fill qty dispensed
            String fillQtyDispensed = rs.next() ? rs.getString(1) : "";
            //region Get Pat_id
            rs = connection.executeQuery(storeConn, "select pat_id from tbf0_work_queue where rx_nbr = " + rx_nbr + " and store_nbr = " + storeNum);//get qty remaining
            String pat_id = rs.next() ? rs.getString(1) : "";
            //region Get fill_pay_method
            rs = connection.executeQuery(storeConn, "select FILL_PAY_METHOD_CD from tbf0_work_queue where rx_nbr = " + rx_nbr + " and store_nbr = " + storeNum + " and fill_nbr = " + fill_nbr);//get fill pay method
            String fillPayMethod = rs.next() ? rs.getString(1) : "";
            //region Get fill_status_cd
            rs = connection.executeQuery(storeConn, "select FILL_STATUS_CD from tbf0_work_queue where rx_nbr = " + rx_nbr + " and store_nbr = " + storeNum + " and fill_nbr = " + fill_nbr);//get fill pay method
            String fillStatusCd = rs.next() ? rs.getString(1) : "";
            //region Get fill_data_rev_dttm
            rs = connection.executeQuery(storeConn, "select to_char(FILL_DATA_REV_DTTM,'MM/DD/YYYY HH24:MI:SS') from tbf0_work_queue where rx_nbr = " + rx_nbr + " and store_nbr = " + storeNum + " and fill_nbr = " + fill_nbr);//get fill pay method
            String fillDataRevDttm = rs.next() ? rs.getString(1) : "";
            //region current time
            LocalDateTime d = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/YYYY HH:mm:ss");
            String dateWithTime = d.format(formatter);
            //region Get fill nbr dispensed
            rs = connection.executeQuery(storeConn, "select fill_nbr_dispensed from tbf0_work_queue where rx_nbr = " + rx_nbr + " and store_nbr = " + storeNum + " and fill_nbr = " + fill_nbr);//get qty remaining
            String fillNbrDispensed = rs.next() ? rs.getString(1) : "";
            //region RXPOIS_MSG

            // look man i have no idea how this works, this is just how it is in the script
            // it checks the length of the rx_nbr (????) and then based on that gives it a certain value, then adds
            // all the messages up?

            String rxpoisMsg1 = "";
            if (rx_nbr.length() == 5)
                rxpoisMsg1 = "L128000R100";
            else if ((rx_nbr.length() == 6))
                rxpoisMsg1 = "L128000R10";
            else if ((rx_nbr.length() == 7))
                rxpoisMsg1 = "L128000R1";
            String rxpoismsg2 = "0" + fill_nbr + "0" + fillNbrDispensed + "00013992011051309030000000123RNY";
            String rxpoismsg3 = "322325911051320005520801882";
            String rxpoismsg4 = rxpoisMsg1 + rx_nbr + rxpoismsg2 + storeNum + rxpoismsg3;

            //region Get fill partial number
            rs = connection.executeQuery(storeConn, "select fill_partial_nbr from tbf0_work_queue where rx_nbr = " + rx_nbr + " and store_nbr = " + storeNum + " and fill_nbr = " + fill_nbr);//get fill partial
            String partialfillNbr = rs.next() ? rs.getString(1) : "";
            //region Command
            String command = "ud32 -n <<%\n" +
                    "SRVCNM\tPSPOUpSVSvc\n" +
                    "USER_ID\t" + userId + "\n" +
                    "UPDATE_USER_ID\t" + userId + "\n" +
                    "FILL_NBR\t" + fill_nbr + "\n" +
                    "FILL_NBR_DISPENSED\t" + fillNbrDispensed + "\n" +
                    "FILL_PARTIAL_NBR\t" + partialfillNbr + "\n" +//find fill partial nbr
                    "SOLD_CNT\t0\n" +
                    "CREATE_USER_ID\t" + userId + "\n" +
                    "FILL_VERIFIED_USER_ID\t" + userId + "\n" +
                    "RX_NBR\t" + rx_nbr + "\n" +
                    "FILL_DATA_REV_ID\t" + fillDataRevId + "\n" +
                    "FILL_LABEL_PRICE_AMT\t" + fillLabelPriceAmt + "\n" +
                    "FILL_SOLD_AMT\t" + fillLabelPriceAmt + "\n" +
                    "FILL_QTY_DISPENSED\t" + fillQtyDispensed + "\n" +
                    "DB_ROUTING_NUM\t" + ((Long.parseLong(pat_id) % 4) + 1) + "\n" +
                    "STORE_CODE\t" + storeNum + "\n" +
                    "WINDOW_ID\t0\n" +
                    "APP_ID\t\n" +
                    "SYS_STATUS\t0\n" +
                    "FILL_PAY_METHOD_CD\t" + fillPayMethod + "\n" +
                    "FILL_SOLD_DTTM\t" + dateWithTime + "\n" +
                    "FILL_STATUS_CD\t" + fillStatusCd + "\n" +
                    "PAT_ID\t" + pat_id + "\n" +
                    "FILL_DATA_REV_DTTM\t" + fillDataRevDttm + "\n" +
                    "EA_UPDATE_DTTM\t" + dateWithTime + "\n" +
                    "UPDATE_DTTM\t" + dateWithTime + "\n" +
                    "CST_OFFSET\t\n" +
                    "IP_ADDR\t10.237.32.231\n" +
                    "APP_SVR_CONN\t\n" +
                    "ARCH_VERSION\tb\n" +
                    "TPA_STATUS\t\n" +
                    "REMOTE_ROLE_CD\tSS\n" +
                    "REMOTE_FACILITY_CD\tR\n" +
                    "LAST_ROW_KEY_LONG_1\t" + storeNum + "\n" +
                    "POSIN_MSG\t" + rxpoismsg4 + "\n" +
                    "\n" +
                    "%\n";
            return execSSH(session, envSetCommand.replace("storeNumber", storeNum) + " " + command + " sleep 3");
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

    private boolean execSSH(Session session, String command) {
        try {
            Channel commandChannel = session.openChannel("exec");
            ((ChannelExec) commandChannel).setPty(true);
            ((ChannelExec) commandChannel).setCommand(command);//set up command
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
            //region Printing to file if error occurred
            if (errorOnCreation) {
                LOGGER.error("Error executing command " + command);
                return false;
            } else {//everything was successful
                LOGGER.info("Command has been executed");
                return true;
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
    }

//    tries to find an issue with why an rx was not created
    public void rxIssueFinder(String storeNum, String prescId, String patId, String payType, String rx_nbr) {
        LOGGER.info("Finding possible issues for why rx " + rx_nbr + " was not created successfully");
        Connection storeConn = connection.connectToStoreDB(storeNum);
        boolean patientOnStore = true;
        boolean atLeastOneIssue = false;
        try {
            //below is checking if pbr exists in store, and if it is an active prescriber
            ResultSet rs = connection.executeQuery(storeConn, "select pbr_status_cd from tbf0_prescriber where pbr_id = ?", "s::" + prescId);
            if (rs.next()) {
                if (!rs.getString(1).equals("A"))//if the prescriber is not active
                {
                    LOGGER.error("The prescriber given is not active. (Current status: " + rs.getString(1) + ")");
                    atLeastOneIssue = true;
                }
            } else {//prescriber not in this store
                LOGGER.error("The prescriber given is not found in store " + storeNum);
                atLeastOneIssue = true;
            }
            //check if patient is on this store
            rs = connection.executeQuery(storeConn, "select * from tbf0_patient where pat_id = ?", "s::" + patId);
            if (!rs.next()) {//if no rows came
                LOGGER.error("Patient id does not exist in store " + storeNum);
                atLeastOneIssue = true;
                patientOnStore = false;
            }
            //check if giving tp to cash patient
            if (payType.equals("T") && patientOnStore) {
                rs = connection.executeQuery(storeConn, "select primary_plan_ind from tbf0_pat_thrd_pty where pat_id = ?", "s::" + patId);
                if (rs.next()) {
                    boolean primaryExists = false;
                    if (rs.getString(1).equals("Y")) {
                        primaryExists = true;
                    }
                    while (rs.next() && !primaryExists) {
                        if (rs.getString(1).equals("Y")) {
                            primaryExists = true;
                        }
                    }
                    if (!primaryExists)//if no primary exists
                    {
                        LOGGER.error("This patient has a plan, but none of them are marked as their primary");
                        atLeastOneIssue = true;
                    }
                } else {
                    LOGGER.error("You attempted to give a Non-Third party patient a third party rx");
                    atLeastOneIssue = true;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getLocalizedMessage());
        }
        if (!atLeastOneIssue) {
            LOGGER.info("Could not find issue... Please Contact TDM Team ");
        }
    }

}
