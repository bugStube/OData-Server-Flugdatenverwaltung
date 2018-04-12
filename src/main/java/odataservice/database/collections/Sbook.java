package odataservice.database.collections;

import odataservice.database.collections.enums.UnitOfMass;
import odataservice.service.entities.definitions.EntityNames;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;

import static odataservice.service.entities.definitions.EntityNames.DB_CARRIER;
import static odataservice.service.entities.definitions.EntityNames.DB_CONNECTION;
import static odataservice.service.entities.definitions.EntityNames.DB_FLIGHT_DATE;
import static odataservice.service.entities.definitions.EntityNames.DB_ID;

/**
 *
 */
@Entity(value = EntityNames.SBOOK, noClassnameStored = true)
@Indexes({ @Index(fields = @Field(DB_ID)), @Index(fields = @Field(DB_CARRIER)), @Index(fields = @Field(DB_CONNECTION)),
           @Index(fields = @Field(DB_FLIGHT_DATE)) })
public class Sbook {

    @Id
    @Property(EntityNames.SBOOK_BOOKID)
    private String bookId;

    @Reference
    private Scarr scarr;

    @Reference
    private Spfli spfli;

    @Reference
    private Sflight sflight;

    @Property(EntityNames.SBOOK_CUSTOMID)
    private String customId;

    @Property(EntityNames.SBOOK_CUSTTYPE)
    private Character custType;

    @Property(EntityNames.SBOOK_SMOKER)
    private Character smoker;

    @Property(EntityNames.SBOOK_LUGGWEIGHT)
    private double luggWeight;

    @Property(EntityNames.SBOOK_WUNIT)
    private UnitOfMass wUnit;

    @Property(EntityNames.SBOOK_INVOICE)
    private boolean invoice;

    @Property(EntityNames.SBOOK_CLASS)
    private Character flightClass;

    @Property(EntityNames.SBOOK_ORDER_DATE)
    private String orderDate;

    @Property(EntityNames.SBOOK_CANCELLED)
    private boolean cancelled;

    @Property(EntityNames.SBOOK_RESERVED)
    private boolean reserved;

    public Sbook() {}

    public Sbook(String bookId, Scarr scarr, Spfli spfli, Sflight sflight, String customId, Character custType, Character smoker, double luggWeight,
                 UnitOfMass wUnit, boolean invoice, Character flightClass, String orderDate, boolean cancelled, boolean reserved) {
        this.bookId = bookId;
        this.scarr = scarr;
        this.spfli = spfli;
        this.sflight = sflight;
        this.customId = customId;
        this.custType = custType;
        this.smoker = smoker;
        this.luggWeight = luggWeight;
        this.wUnit = wUnit;
        this.invoice = invoice;
        this.flightClass = flightClass;
        this.orderDate = orderDate;
        this.cancelled = cancelled;
        this.reserved = reserved;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public Scarr getScarr() {
        return scarr;
    }

    public void setScarr(Scarr scarr) {
        this.scarr = scarr;
    }

    public Spfli getSpfli() {
        return spfli;
    }

    public void setSpfli(Spfli spfli) {
        this.spfli = spfli;
    }

    public Sflight getSflight() {
        return sflight;
    }

    public void setSflight(Sflight sflight) {
        this.sflight = sflight;
    }

    public String getCustomId() {
        return customId;
    }

    public void setCustomId(String customId) {
        this.customId = customId;
    }

    public String getCustType() {
        return custType == 'M' ? "Male" : "Female";
    }

    public void setCustType(Character custType) {
        this.custType = custType;
    }

    public void setCustType(String custType) {
        if ("Male".equals(custType)) {
            setCustType('M');
        } else {
            setCustType('F');
        }
    }

    public boolean isSmoker() {
        return smoker == 'Y';
    }

    public void setSmoker(Character smoker) {
        this.smoker = smoker;
    }

    public void setSmoker(boolean isSmoker) {
        if (isSmoker) {
            this.setSmoker('Y');
        } else {
            this.setSmoker('N');
        }
    }

    public double getLuggWeight() {
        return luggWeight;
    }

    public void setLuggWeight(double luggWeight) {
        this.luggWeight = luggWeight;
    }

    public UnitOfMass getwUnit() {
        return wUnit;
    }

    public void setwUnit(UnitOfMass wUnit) {
        this.wUnit = wUnit;
    }

    public boolean hasInvoice() {
        return invoice;
    }

    public void setInvoice(Character invoice) {
        this.invoice = 'Y' == invoice;
    }

    public void setInvoice(boolean hasInvoice) {
        if (hasInvoice) {
            this.setInvoice('Y');
        } else {
            this.setInvoice('N');
        }
    }

    public String getFlightClass() {
        switch (flightClass) {
            case 'E':
                return "Economy";
            case 'B':
                return "Business";
            case 'F':
                return "First Class";
            default:
                return "N/A";
        }
    }

    public void setFlightClass(Character flightClass) {
        this.flightClass = flightClass;
    }

    public void setFlightClass(String flightClass) {
        switch (flightClass) {
            case "Economy":
                setFlightClass('E');
                return;
            case "Business":
                setFlightClass('B');
                return;
            case "First Class":
                setFlightClass('F');
        }
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(Character cancelled) {
        this.cancelled = 'Y' == cancelled;
    }

    public void setCancelled(boolean isCancelled) {
        if (isCancelled) {
            this.setCancelled('Y');
        } else {
            this.setCancelled('N');
        }
    }

    public boolean isReserved() {
        return reserved;
    }

    public void setReserved(Character reserved) {
        this.reserved = 'Y' == reserved;
    }

    public void setReserved(boolean isReserved) {
        if (isReserved) {
            this.setReserved('Y');
        } else {
            this.setReserved('N');
        }
    }
}
