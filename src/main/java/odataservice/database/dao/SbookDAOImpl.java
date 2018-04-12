////////////////////////////////////////////////////////////////////////////////
//
// Created by BBruhns on 10.03.2018.
//
// Copyright (c) 2006 - 2018 FORCAM GmbH. All rights reserved.
////////////////////////////////////////////////////////////////////////////////

package odataservice.database.dao;

import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import odataservice.database.collections.Sbook;
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
import static odataservice.service.entities.definitions.EntityNames.DB_FLIGHT_DATE;
import static odataservice.service.entities.definitions.EntityNames.DB_ID;
import static odataservice.service.entities.definitions.EntityNames.SCARR;
import static odataservice.service.entities.definitions.EntityNames.SFLIGHT;
import static odataservice.service.entities.definitions.EntityNames.SPFLI;

/**
 *
 */
public class SbookDAOImpl extends BasicDAO<Sbook, ObjectId> implements SbookDAO {

    // ------------------------------------------------------------------------
    // constructors
    // ------------------------------------------------------------------------

    public SbookDAOImpl(Class<Sbook> entityClass, MongoClient mongoClient, Morphia morphia, String dbName) {
        super(entityClass, mongoClient, morphia, dbName);
    }

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    @Override
    public Sbook getById(String id) {
        return super.findOne(DB_ID + " = ", id);
    }

    @Override
    public Key<Sbook> save(Sbook sbook) {
        return super.save(sbook);
    }

    @Override
    public WriteResult delete(Sbook sbook) {
        return super.delete(sbook);
    }

    @Override
    public boolean idTaken(String id) {
        return this.getById(id) != null;
    }

    @Override
    public List<Sbook> getAllSbooks() {
        return getDatastore().createQuery(Sbook.class).asList();
    }

    // ========================================================================
    //                           NAVIGATION
    // ========================================================================

    @Override
    public List<Sbook> findBookingsByCarrierId(String carrierId) {
        final Query<Sbook> query = getDatastore().find(Sbook.class).field(DB_CARRIER).equal(new Key<>(Scarr.class, SCARR, carrierId));

        return query.asList();
    }

    @Override
    public List<Sbook> findBookingsByCarrierIdAndConnectionIdAndFlDate(String carrierCode, String connId, String fldate) {
        final String equals = " = ";
        final Query<Sbook> query = getDatastore().createQuery(Sbook.class).filter(DB_CARRIER + equals, new Key<>(Scarr.class, SCARR, carrierCode)).filter(
            DB_CONNECTION + equals,
            new Key<>(Spfli.class, SPFLI, connId)).filter(DB_FLIGHT_DATE + equals, new Key<>(Sflight.class, SFLIGHT, fldate));
        return query.asList();
    }

    @Override
    public List<Sbook> findBookingsByConnectionId(String connectionId) {
        final Query<Sbook> query = getDatastore().find(Sbook.class).field(DB_CONNECTION).equal(new Key<>(Spfli.class, SPFLI, connectionId));

        return query.asList();
    }

}
