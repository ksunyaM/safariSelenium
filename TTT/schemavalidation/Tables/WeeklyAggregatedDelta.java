package schemavalidation.Tables;


public class WeeklyAggregatedDelta {

    private String wkkey;
    private String code;
    private String actual_product_pack_code;
    private String locationNumber;
    private String locationType;
    private String String;
    private String onshelfeow;
    private String onshelf;
    private String hardallocated;
    private String onorder;
    private String intransit;
    private String blocked;
    private String blockedunsalable;



    public WeeklyAggregatedDelta( String wkkey, String code, String actual_product_pack_code,String locationNumber,String locationType, String String,
                                  String onshelfeow, String onshelf,
                                  String hardallocated,
                                   String onorder, String intransit,
                                   String blocked, String blockedunsalable) {

        this.wkkey = wkkey;
        this.code = code;
        this.actual_product_pack_code = actual_product_pack_code;
        this.locationNumber = locationNumber;
        this.locationType = locationType;
        this.String = String;
        this.onshelfeow = onshelfeow;
        this.onshelf = onshelf;
        this.hardallocated = hardallocated;
        this.onorder = onorder;
        this.intransit = intransit;
        this.blocked = blocked;
        this.blockedunsalable = blockedunsalable;



}

    public String getWkkey() {
        return wkkey;
    }

    public String getCode() {
        return code;
    }

    public String getActual_product_pack_code() {
        return actual_product_pack_code;
    }

    public String getLocationNumber() {
        return locationNumber;
    }

    public String getLocationType() {
        return locationType;
    }

    public String getLastUpdatedTime() {
        return String;
    }

    public String getOnshelfeow() {
        return onshelfeow;
    }

    public String getOnshelf() {
        return onshelf;
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
