package schemavalidation.support.ReferenceTables;



public class SupplierCatalog {
    private String suppliercode;
     private String upc;
     private String cost;

    public SupplierCatalog(String suppliercode, String upc, String cost) {
this.suppliercode = suppliercode;
this.upc = upc;
this.cost = cost;

    }


    public String getSuppliercode() {
        return suppliercode;
    }

    public String getUpc() {
        return upc;
    }

    public String getCost() {
        return cost;
    }








}
