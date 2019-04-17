//package schemavalidation;
//
//import common.SSHConnector;
//import cucumber.api.java.en.Given;
//import cucumber.api.java.en.When;
//
//public class SchemaValidationStockByStoreAappSnpsStep {
//    private static SSHConnector ssh = new SSHConnector();
//
//    @Given("^an Avro files$")
//    public void setupAvroFiles() {
//        ssh.connect();
//        ssh.sendCommand("describe table");
//    }
//
//    @When("^I query for schema \"([^\"]*)\"$")
//    public void i_query_for_schema(String schemaFileName) {
//        System.out.println("Implement Me");
//    }
//}
