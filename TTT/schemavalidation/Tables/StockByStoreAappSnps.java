package schemavalidation.Tables;



public class StockByStoreAappSnps {

        private String code;
        private String actualproductpackcode;
        private String locationnumber;
        private String locationtype;
        private String socnumber;
        private String lastupdatedtime;
        private String onshelf;
        private String stockonshelfupdated;
        private String hardallocated;
        private String onorder;
        private String intransit;
        private String blocked;
        private String blockedunsalable;


    public StockByStoreAappSnps(String code, String  actualproductpackcode, String locationnumber, String locationtype,
                                   String socnumber, String lastupdatedtime, String onshelf,
                                   String stockonshelfupdated, String hardallocated, String intransit,
                                   String blocked, String blockedunsalable     ) {
        this.code = code;
        this.actualproductpackcode = actualproductpackcode;
        this.locationnumber = locationnumber;
        this.locationtype = locationtype;
        this.socnumber = socnumber;
        this.lastupdatedtime = lastupdatedtime;
        this.onshelf = onshelf;
        this.stockonshelfupdated = stockonshelfupdated;
        this.hardallocated = hardallocated;
        this.intransit = intransit;
        this.blocked = blocked;
        this.blockedunsalable = blockedunsalable;

    }




    public String getCode() {
        return code;
    }

    public String getActualproductpackcode() {
        return actualproductpackcode;
    }

    public String getLocationnumber() {
        return locationnumber;
    }

    public String getLocationtype() {
        return locationtype;
    }

    public String getSocnumber() {
        return socnumber;
    }

    public String getLastupdatedtime() {
        return lastupdatedtime;
    }

    public String getOnshelf() {
        return onshelf;
    }

    public String getStockonshelfupdated() {
        return stockonshelfupdated;
    }

    public String getHardallocated() {
        return hardallocated;
    }

    public String getOnorder() {
        return onorder;
    }

    public String getIntransit() {
        return intransit;
    }

    public String getBlocked() {
        return blocked;
    }

    public String getBlockedunsalable() {
        return blockedunsalable;
    }





    }





