package odataservice.database.dao;

import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import odataservice.database.collections.Scarr;
import odataservice.database.collections.Sflight;
import odataservice.database.collections.Spfli;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;

import java.util.List;

import static odataservice.service.entities.definitions.EntityNames.DB_CARRIER;
import static odataservice.service.entities.definitions.EntityNames.DB_CONNECTION;
import static odataservice.service.entities.definitions.EntityNames.DB_ID;
import static odataservice.service.entities.definitions.EntityNames.SCARR;
import static odataservice.service.entities.definitions.EntityNames.SPFLI;

/**
 *
 */
public class SflightDAOImpl extends BasicDAO<Sflight, ObjectId> implements SflightDAO {

    // ------------------------------------------------------------------------
    // constructors
    // ------------------------------------------------------------------------

    public SflightDAOImpl(Class<Sflight> entityClass, MongoClient mongoClient, Morphia morphia, String dbName) {
        super(entityClass, mongoClient, morphia, dbName);
    }

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    @Override
    public Sflight getById(String id) {
        return super.findOne(DB_ID + " = ", id);
    }

    @Override
    public Key<Sflight> save(Sflight sflight) {
        return super.save(sflight);
    }

    @Override
    public WriteResult delete(Sflight sflight) {
        return super.delete(sflight);
    }

    @Override
    public boolean idTaken(String id) {
        return this.getById(id) != null;
    }

    @Override
    public List<Sflight> getAllSflights() {
        return super.createQuery().asList();
    }

    // ========================================================================
    //                           NAVIGATION
    // ========================================================================

    @Override
    public Sflight findFlightByCarrierIdAndConnectionIdAndFlDate(String carrierId, String connectionId, String flDate) {
        final Query<Sflight> query = super.createQuery().field(DB_ID).equal(flDate).field(DB_CARRIER).equal(new Key<>(Scarr.class, SCARR, carrierId)).field(
            DB_CONNECTION).equal(new Key<>(Spfli.class, SPFLI, connectionId));

        return query.get();
    }

    @Override
    public List<Sflight> findFlightsByCarrierId(String carrierCode) {
        final Query<Sflight> query = super.createQuery().field(DB_CARRIER).equal(new Key<>(Scarr.class, SCARR, carrierCode));

        return query.asList();
    }

    @Override
    public List<Sflight> findFlightsByCarrierIdAndConnectionId(String carrierId, String connectionId) {
        final Query<Sflight> query = super.createQuery()
                                          .field(DB_CARRIER)
                                          .equal(new Key<>(Scarr.class, SCARR, carrierId))
                                          .field(DB_CONNECTION)
                                          .equal(new Key<>(Spfli.class, SPFLI, connectionId));
        return query.asList();
    }

}
