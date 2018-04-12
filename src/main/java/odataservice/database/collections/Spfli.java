package odataservice.database.collections;

import odataservice.database.collections.enums.UnitOfLength;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;

import java.time.LocalTime;

import static odataservice.service.entities.definitions.EntityNames.DB_CARRIER;
import static odataservice.service.entities.definitions.EntityNames.DB_ID;
import static odataservice.service.entities.definitions.EntityNames.SPFLI;
import static odataservice.service.entities.definitions.EntityNames.SPFLI_AIRPFROM;
import static odataservice.service.entities.definitions.EntityNames.SPFLI_AIRPTO;
import static odataservice.service.entities.definitions.EntityNames.SPFLI_ARRTIME;
import static odataservice.service.entities.definitions.EntityNames.SPFLI_CITYFROM;
import static odataservice.service.entities.definitions.EntityNames.SPFLI_CITYTO;
import static odataservice.service.entities.definitions.EntityNames.SPFLI_CONNID;
import static odataservice.service.entities.definitions.EntityNames.SPFLI_COUNTRYFR;
import static odataservice.service.entities.definitions.EntityNames.SPFLI_COUNTRYTO;
import static odataservice.service.entities.definitions.EntityNames.SPFLI_DEPTIME;
import static odataservice.service.entities.definitions.EntityNames.SPFLI_DISTANCE;
import static odataservice.service.entities.definitions.EntityNames.SPFLI_DISTID;
import static odataservice.service.entities.definitions.EntityNames.SPFLI_FLTIME;
import static odataservice.service.entities.definitions.EntityNames.SPFLI_FLTYPE;
import static odataservice.service.entities.definitions.EntityNames.SPFLI_PERIOD;

/**
 *
 */
@Entity(value = SPFLI, noClassnameStored = true)
@Indexes({ @Index(fields = @Field(DB_ID)), @Index(fields = @Field(DB_CARRIER)) })
public class Spfli {

    // ------------------------------------------------------------------------
    // members
    // ------------------------------------------------------------------------

    @Id
    @Property(SPFLI_CONNID)
    private String connId;

    @Reference
    private Scarr scarr;

    @Property(SPFLI_AIRPFROM)
    private String airpFrom;

    @Property(SPFLI_AIRPTO)
    private String airpTo;

    @Property(SPFLI_CITYFROM)
    private String cityFrom;

    @Property(SPFLI_CITYTO)
    private String cityTo;

    @Property(SPFLI_COUNTRYFR)
    private String countryFrom;

    @Property(SPFLI_COUNTRYTO)
    private String countryTo;

    @Property(SPFLI_FLTIME)
    private int flTime;

    /** Save in SAP's TIMS-format (hhmmss) */
    @Property(SPFLI_DEPTIME)
    private String depTime;

    /** Save in SAP's TIMS-format (hhmmss) */
    @Property(SPFLI_ARRTIME)
    private String arrTime;

    @Property(SPFLI_DISTANCE)
    private double distance;

    @Property(SPFLI_DISTID)
    private UnitOfLength distId;

    @Property(SPFLI_FLTYPE)
    private String flType;

    @Property(SPFLI_PERIOD)
    private int period;

    // ------------------------------------------------------------------------
    // constructors
    // ------------------------------------------------------------------------

    /**
     * Default constructor.
     */
    public Spfli() {}

    public Spfli(String connId, Scarr scarr, String airpFrom, String airpTo, String cityFrom, String cityTo, String countryFrom, String countryTo, int flTime,
                 String depTime, String arrTime, double distance, UnitOfLength distId, String flType, int period) {
        this.connId = connId;
        this.scarr = scarr;
        this.airpFrom = airpFrom;
        this.airpTo = airpTo;
        this.cityFrom = cityFrom;
        this.cityTo = cityTo;
        this.countryFrom = countryFrom;
        this.countryTo = countryTo;
        this.flTime = flTime;
        this.depTime = depTime;
        this.arrTime = arrTime;
        this.distance = distance;
        this.distId = distId;
        this.flType = flType;
        this.period = period;
    }

    // ------------------------------------------------------------------------
    // getters/setters
    // ------------------------------------------------------------------------

    public String getConnId() {
        return connId;
    }

    public void setConnId(String connId) {
        this.connId = connId;
    }

    public Scarr getScarr() {
        return scarr;
    }

    public void setScarr(Scarr scarr) {
        this.scarr = scarr;
    }

    public String getAirpFrom() {
        return airpFrom;
    }

    public void setAirpFrom(String airpFrom) {
        this.airpFrom = airpFrom;
    }

    public String getAirpTo() {
        return airpTo;
    }

    public void setAirpTo(String airpTo) {
        this.airpTo = airpTo;
    }

    public String getCityFrom() {
        return cityFrom;
    }

    public void setCityFrom(String cityFrom) {
        this.cityFrom = cityFrom;
    }

    public String getCityTo() {
        return cityTo;
    }

    public void setCityTo(String cityTo) {
        this.cityTo = cityTo;
    }

    public String getCountryFrom() {
        return countryFrom;
    }

    public void setCountryFrom(String countryFrom) {
        this.countryFrom = countryFrom;
    }

    public String getCountryTo() {
        return countryTo;
    }

    public void setCountryTo(String countryTo) {
        this.countryTo = countryTo;
    }

    public int getFlTime() {
        return flTime;
    }

    public void setFlTime(int flTime) {
        this.flTime = flTime;
    }

    public LocalTime getDepTime() {
        return LocalTime.parse(depTime);
    }

    public void setDepTime(String depTime) {
        this.depTime = depTime;
    }

    public LocalTime getArrTime() {
        return LocalTime.parse(arrTime);
    }

    public void setArrTime(String arrTime) {
        this.arrTime = arrTime;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public UnitOfLength getDistId() {
        return distId;
    }

    public void setDistId(UnitOfLength distId) {
        this.distId = distId;
    }

    public String getFlType() {
        return flType;
    }

    public void setFlType(String flType) {
        this.flType = flType;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}
