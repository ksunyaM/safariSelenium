package schemavalidation.support.ReferenceTables;

public class Product{
    private String productcode;
    private String productndc;
    private String productupc;
    private String productwic;
    private String productname;
    private String productdeaclass;
    private String productstoragelocation;
    private String brandgenrictype;
    private String warehousecost;

    public Product(String productcode, String productndc, String productupc, String productwic, String productname, String productdeaclass, String productstoragelocation, String brandgenrictype, String warehousecost) {
        this.productcode = productcode;
        this.productndc = productndc;
        this.productupc = productupc;
        this.productwic = productwic;
        this.productname = productname;
        this.productdeaclass = productdeaclass;
        this.productstoragelocation = productstoragelocation;
        this.brandgenrictype = brandgenrictype;
        this.warehousecost = warehousecost;
    }



    public String getProductcode() {
        return productcode;
    }

    public String getProductndc() {
        return productndc;
    }

    public String getProductupc() {
        return productupc;
    }

    public String getProductwic() {
        return productwic;
    }

    public String getProductname() {
        return productname;
    }

    public String getProductdeaclass() {
        return productdeaclass;
    }

    public String getProductstoragelocation() {
        return productstoragelocation;
    }

    public String getBrandgenrictype() {
        return brandgenrictype;
    }

    public String getWarehousecost() {
        return warehousecost;
    }





}
