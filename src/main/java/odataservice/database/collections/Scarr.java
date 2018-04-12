package odataservice.database.collections;

import odataservice.database.collections.enums.UnitOfCurrency;
import odataservice.service.entities.definitions.EntityNames;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Property;

import static odataservice.service.entities.definitions.EntityNames.DB_ID;

/**
 *
 */
@Entity(value = EntityNames.SCARR, noClassnameStored = true)
@Indexes({ @Index(fields = @Field(DB_ID)) })
public class Scarr {

    @Id
    @Property(EntityNames.SCARR_CARRID)
    private String carrId;

    @Property(EntityNames.SCARR_CARRNAME)
    private String carrName;

    @Property(EntityNames.SCARR_CURRCODE)
    private UnitOfCurrency currCode;

    @Property(EntityNames.SCARR_URL)
    private String url;

    public Scarr() {}

    public Scarr(String carrId, String carrName, UnitOfCurrency currCode, String url) {
        this.carrId = carrId;
        this.carrName = carrName;
        this.currCode = currCode;
        this.url = url;
    }

    public String getCarrId() {
        return carrId;
    }

    public void setCarrId(String carrId) {
        this.carrId = carrId;
    }

    public String getCarrName() {
        return carrName;
    }

    public void setCarrName(String carrName) {
        this.carrName = carrName;
    }

    public UnitOfCurrency getCurrCode() {
        return currCode;
    }

    public void setCurrCode(UnitOfCurrency currCode) {
        this.currCode = currCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
