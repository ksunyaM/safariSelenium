package schemavalidation.support.ReferenceTables;

public class LocationGroup {
    private String code;
    private String creationDate;
    private String description;
    private String locationitemKeys;
    private String startdate;

    public LocationGroup(String code, String creationDate, String descriptiom, String locationitemKeys, String startdate) {

        this.code = code;
        this.creationDate = creationDate;
        this.description = descriptiom;
        this.locationitemKeys = locationitemKeys;
        this.startdate = startdate;

    }



    public String getCode() {
        return code;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public String getDescription() {
        return description;
    }

    public String getlocationitemKeys() {
        return locationitemKeys;
    }

    public String getStartdate() {
        return startdate;
    }




}
