////////////////////////////////////////////////////////////////////////////////
//
// Created by BBruhns on 10.03.2018.
//
// Copyright (c) 2006 - 2018 FORCAM GmbH. All rights reserved.
////////////////////////////////////////////////////////////////////////////////

package odataservice.database.dao;

import com.mongodb.MongoClient;
import com.mongodb.WriteResult;
import odataservice.database.collections.Saplane;
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
import static odataservice.service.entities.definitions.EntityNames.PLANE_TYPE;
import static odataservice.service.entities.definitions.EntityNames.SCARR;
import static odataservice.service.entities.definitions.EntityNames.SPFLI;

/**
 *
 */
public class SaplaneDAOImpl extends BasicDAO<Saplane, ObjectId> implements SaplaneDAO {

    // ------------------------------------------------------------------------
    // constructors
    // ------------------------------------------------------------------------

    public SaplaneDAOImpl(Class<Saplane> entityClass, MongoClient mongoClient, Morphia morphia, String dbName) {
        super(entityClass, mongoClient, morphia, dbName);
    }

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    @Override
    public Saplane getById(String id) {
        return super.findOne(DB_ID + " = ", id);
    }

    @Override
    public Key<Saplane> save(Saplane saplane) {
        return super.save(saplane);
    }

    @Override
    public WriteResult delete(Saplane saplane) {
        return super.delete(saplane);
    }

    @Override
    public boolean idTaken(String id) {
        return this.getById(id) != null;
    }

    @Override
    public Saplane findPlaneByPlaneType(String planeType) {
        return createQuery().field(PLANE_TYPE).equal(planeType).get();
    }

    @Override
    public List<Saplane> getAllSaplanes() {
        return super.getDatastore().createQuery(Saplane.class).asList();
    }

    // ========================================================================
    //                           NAVIGATION
    // ========================================================================

    @Override
    public Saplane findPlaneByCarrierIdAndConnectionIdAndFlDate(String carrierId, String connectionId, String flDate) {
        final Query<Sflight> query = getDatastore().find(Sflight.class).field(DB_ID).equal(flDate).field(DB_CARRIER).equal(new Key<>(Scarr.class,
                                                                                                                                        SCARR,
                                                                                                                                        carrierId)).field(
            DB_CONNECTION).equal(new Key<>(Spfli.class, SPFLI, connectionId));

        final Sflight sflight = query.get();

        return this.getById(sflight.getSaplane().getPlaneType());
    }

}
