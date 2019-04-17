package com.wba.rxrintegration.api.test.Pi_eRxSendMessage;

import com.wba.rxrintegration.api.test.Pi_eRxInboundSS.EncryptionUtility;
import com.wba.rxrintegration.api.test.common.XMLUtility;
import com.wba.rxrintegration.api.test.common._VerificationStep;
import com.wba.test.utils.BaseStep;
import com.wba.test.utils.DataBaseUtils;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang.StringUtils;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;


public class Pi_eRxSendMessage_Step extends BaseStep {
    private String query_pbrSPI,query_locNum,query_msgTransType,query_relatesToMsgId,query_pbrOrderNbr;
    private String pbrSPI,locNum,msgTransType,relatesToMsgId,locNumber,pbrNbr;
    private String correlationId,uniqueMsgId;
    private String transType,presSPI,pharmNcpdpId,relatesMessageId,encryptedRawMsg,decryptedRawMsg,encryptedUpdatedMsg,transTypeText;
    private String jsonFilePath=System.getProperty("user.dir") + File.separator + "src/test/resources/com/wba/rxrintegration/api/test/data/";


   @And("user gets unique data for json to publish \"([^\"]*)\"$")
   public void getUniqueValues(String jsonFile) throws Throwable{

//       Object obj = new JSONParser().parse(new FileReader(jsonFilePath+jsonFile));
//       JSONObject jo = (JSONObject) obj;
//
//       presSPI = jo.get("prescriberSPI").toString();
//       pharmNcpdpId=jo.get("pharmacyNcpdpId").toString();
//       transType=jo.get("transactionType").toString();
//       transTypeText = StringUtils.substringsBetween(transType,"\"","\"")[1];
//       relatesMessageId=(jo.get("relatesToMessageId") != null ? ((HashMap<String,String>)(jo.get("relatesToMessageId"))).get("string"): null) ;
//       locNumber=(jo.get("locationNumber") != null ? ((HashMap<String,String>)(jo.get("locationNumber"))).get("string"): null) ;
//       correlationId=UUID.randomUUID().toString().replace("-", "") ;
//       dataStorage.add("unique_corrId",correlationId);
//       uniqueMsgId=UUID.randomUUID().toString().replace("-", "") ;
//       dataStorage.add("unique_msgId",uniqueMsgId);
//       encryptedRawMsg=((HashMap<String,String>)(jo.get("rawMessage"))).get("value");
       System.out.println("Encrypted Msg: "+encryptedRawMsg);
       EncryptionUtility encryptionUtility = new com.wba.rxrintegration.api.test.Pi_eRxInboundSS.EncryptionUtility();
       decryptedRawMsg = encryptionUtility.decrypt(encryptedRawMsg, "2", EncryptionUtility.EncryptionType.AES).replaceAll("(\\r|\\n|\\t)", "");
       System.out.println("Decrypted Msg: "+decryptedRawMsg);
         if(decryptedRawMsg.contains("<MessageID>"))
         {
             decryptedRawMsg=decryptedRawMsg.replaceAll("(?<=<MessageID>).*?(?=</MessageID>)", uniqueMsgId);
             System.out.println("Updated Decrypted Msg: "+decryptedRawMsg);
         }
        encryptedUpdatedMsg=encryptionUtility.encrypt(decryptedRawMsg,"2",EncryptionUtility.EncryptionType.AES);
       System.out.println(encryptedUpdatedMsg);
         dataStorage.add("encrypted_response",encryptedUpdatedMsg);
   }


    @Then("^the user validates record in erx_outbound_msg table for messageId \"([^\"]*)\" and presSPI \"([^\"]*)\"$")
    public void the_user_validates_record_in_erx_oubound_msg_table(String msgId,String pSPI) throws Throwable {

           msgId = String.valueOf(dataStorage.unmask("unique_msgId"));
           pSPI = presSPI;
           query_pbrSPI = "select msg_pbr_spi from erx_outbound_msg where msg_id = '" + msgId + "' and msg_pbr_spi='" + pSPI + "'";
           query_locNum = "select msg_location_number from erx_outbound_msg where msg_id = '" + msgId + "' and msg_pbr_spi='" + pSPI + "'";
           query_msgTransType = "select msg_trans_type from erx_outbound_msg where msg_id = '" + msgId + "' and msg_pbr_spi='" + pSPI + "'";
           query_relatesToMsgId = "select relates_to_msg_id from erx_outbound_msg where msg_id = '" + msgId + "' and msg_pbr_spi='" + pSPI + "'";
           query_pbrOrderNbr = "select pbr_order_number from erx_outbound_msg where msg_id = '" + msgId + "' and msg_pbr_spi='" + pSPI + "'";

           pbrSPI = (String) ((HashMap) (fetchRecordFromCassandra(query_pbrSPI)).get(0)).get("msg_pbr_spi");
           locNum = (String) ((HashMap) (fetchRecordFromCassandra(query_locNum)).get(0)).get("msg_location_number");
           msgTransType = (String) ((HashMap) (fetchRecordFromCassandra(query_msgTransType)).get(0)).get("msg_trans_type");
           relatesToMsgId = (String) ((HashMap) (fetchRecordFromCassandra(query_relatesToMsgId)).get(0)).get("relates_to_msg_id");
           pbrNbr = (String) ((HashMap) (fetchRecordFromCassandra(query_pbrOrderNbr)).get(0)).get("pbr_order_nbr");

           new _VerificationStep().verify_that(pbrSPI, "equal", presSPI);
           new _VerificationStep().verify_that(locNum, "equal", locNumber);
           new _VerificationStep().verify_that(msgTransType, "equal", transTypeText);
           new _VerificationStep().verify_that(relatesToMsgId, "equal", relatesMessageId);
           new _VerificationStep().verify_that(pbrNbr, "equal", null);
       }


    public List fetchRecordFromCassandra(String query) {
        DataBaseUtils dbutil = new DataBaseUtils("erx");
        List<Map<String, Object>> casResult = dbutil.runCassandraQuery(query);
        int size=casResult.size();
        if(size>1) {
            System.out.println("Duplicate added");
        }
        return casResult;
   }

    @Then("^user validates response event in topic \"([^\"]*)\"$")
    public void user_validates_response_event_in_topic(String topicName) {
        new _VerificationStep().verify_that(eventStorage.getLastConsumed().getBodyAttribute("relatesToMessageId").toString().replace("\"", ""), "equal", uniqueMsgId);
        new _VerificationStep().verify_that(eventStorage.getLastConsumed().getBodyAttribute("prescriberSPI").toString().replace("\"", ""), "equal",presSPI );
        new _VerificationStep().verify_that(eventStorage.getLastConsumed().getBodyAttribute("pharmacyNcpdpId").toString().replace("\"", ""), "equal", pharmNcpdpId);
        new _VerificationStep().verify_that(eventStorage.getLastConsumed().getBodyAttribute("transactionType").toString().replace("\"", ""), "equal", transTypeText);
        new _VerificationStep().verify_that(eventStorage.getLastConsumed().getBodyAttribute("responseTransactionType").toString().replace("\"", ""),"equal", "STATUS");
   }


}

