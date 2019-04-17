package schemavalidation.Tables;


public class StockByStoreAppTxnDelta {

    private String operationType;
    private String rxNumnber;
    private String onshelf;
    private String hardallocated;
    private String onorder;
    private String intransit;
    private String blocked;
    private String blockedunsalable;


    public StockByStoreAppTxnDelta(String operationType, String rxNumnber, String onshelf, String hardallocated,
                                   String onorder, String intransit,
                                   String blocked, String blockedunsalable) {
        this.operationType = operationType;
        this.rxNumnber = rxNumnber;
        this.onshelf = onshelf;
        this.hardallocated = hardallocated;
        this.onorder = onorder;
        this.intransit = intransit;
        this.blocked = blocked;
        this.blockedunsalable = blockedunsalable;

    }

    public String getOperationType() {
        return operationType;
    }

    public String getRxNumnber() {
        return rxNumnber;
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
