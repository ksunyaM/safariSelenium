package schemavalidation.support.ReferenceTables;

import java.sql.Timestamp;

public class DateDim {
    private String weekkey;
    private String monthkey;
    private String fulldate;
    private String datename;
    private String datenameus;
    private String datenameeu;
    private String dayofweek;
    private String daynameofweek;
    private String dayofmonth;
    private String dayofyear;
    private String weekdayweekend;
    private String weekofyear;
    private String monthname;
    private String monthofyear;
    private String islastdayofmonth;
    private String calendarquarter;
    private String calendaryear;
    private String calendaryearmonth;
    private String calendaryearqtr;
    private String fiscalmonthofyear;
    private String fiscalquarter;
    private String fiscalyear;
    private String fiscalyearmonth;
    private String fiscalyearqtr;

    public DateDim(String weekkey, String monthkey, String fulldate,
                        String datename, String datenameus, String datenameeu, String dayofweek,
                        String daynameofweek, String dayofmonth, String dayofyear,
                        String weekdayweekend, String weekofyear, String monthname, String monthofyear, String islastdayofmonth,
                        String calendarquarter, String calendaryear, String calendaryearmonth,
                        String calendaryearqtr, String fiscalmonthofyear, String fiscalquarter, String fiscalyear, String fiscalyearmonth, String fiscalyearqtr){
    this.weekkey = weekkey;
    this.monthkey = monthkey;
    this.fulldate = fulldate;
    this.datename = datename;
    this.datenameus = datenameus;
    this.datenameeu = datenameeu;
    this.dayofweek = dayofweek;
    this.daynameofweek = daynameofweek;
    this.dayofmonth = dayofmonth;
    this.dayofyear = dayofyear;
    this.weekdayweekend = weekdayweekend;
    this.weekofyear = weekofyear;
    this.monthname = monthname;
    this.monthofyear = monthofyear;
    this.islastdayofmonth = islastdayofmonth;
    this.calendarquarter = calendarquarter;
    this.calendaryear = calendaryear;
    this.calendaryearmonth = calendaryearmonth;
    this.calendaryearqtr = calendaryearqtr;
    this.fiscalmonthofyear = fiscalmonthofyear;
    this.fiscalquarter = fiscalquarter;
    this.fiscalyear = fiscalyear;
    this.fiscalyearmonth = fiscalyearmonth;
    this.fiscalyearqtr = fiscalyearqtr;





    }

    public String getWeekkey() {
        return weekkey;
    }

    public String getMonthkey() {
        return monthkey;
    }

    public String getFulldate() {
        return fulldate;
    }

    public String getDatename() {
        return datename;
    }

    public String getDatenameus() {
        return datenameus;
    }

    public String getDatenameeu() {
        return datenameeu;
    }

    public String getDayofweek() {
        return dayofweek;
    }

    public String getDaynameofweek() {
        return daynameofweek;
    }

    public String getDayofmonth() {
        return dayofmonth;
    }

    public String getDayofyear() {
        return dayofyear;
    }

    public String getWeekdayweekend() {
        return weekdayweekend;
    }

    public String getWeekofyear() {
        return weekofyear;
    }

    public String getMonthname() {
        return monthname;
    }

    public String getMonthofyear() {
        return monthofyear;
    }

    public String getIslastdayofmonth() {
        return islastdayofmonth;
    }

    public String getCalendarquarter() {
        return calendarquarter;
    }

    public String getCalendaryear() {
        return calendaryear;
    }

    public String getCalendaryearmonth() {
        return calendaryearmonth;
    }

    public String getCalendaryearqtr() {
        return calendaryearqtr;
    }

    public String getFiscalmonthofyear() {
        return fiscalmonthofyear;
    }

    public String getFiscalquarter() {
        return fiscalquarter;
    }

    public String getFiscalyear() {
        return fiscalyear;
    }

    public String getFiscalyearmonth() {
        return fiscalyearmonth;
    }

    public String getFiscalyearqtr() {
        return fiscalyearqtr;
    }

}