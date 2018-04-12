////////////////////////////////////////////////////////////////////////////////
//
// Created by BBruhns on 10.03.2018.
//
// Copyright (c) 2006 - 2018 FORCAM GmbH. All rights reserved.
////////////////////////////////////////////////////////////////////////////////

package odataservice.database.service;

import com.mongodb.WriteResult;
import odataservice.database.collections.Saplane;
import odataservice.database.connection.MorphiaService;
import odataservice.database.dao.SaplaneDAO;
import odataservice.database.dao.SaplaneDAOImpl;
import odataservice.service.entities.definitions.EntityNames;
import org.mongodb.morphia.Key;

import java.util.List;

/**
 *
 */
public class SaplaneService implements IDBService {

    // ------------------------------------------------------------------------
    // members
    // ------------------------------------------------------------------------

    private SaplaneDAO mSaplaneDAO;

    // ------------------------------------------------------------------------
    // constructors
    // ------------------------------------------------------------------------

    public SaplaneService() {
        final MorphiaService morphiaService = new MorphiaService();
        mSaplaneDAO = new SaplaneDAOImpl(Saplane.class, morphiaService.getMongoClient(), morphiaService.getMorphia(), EntityNames.DB_NAME);
    }

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    public List<Saplane> getAllSaplanes() {
        return mSaplaneDAO.getAllSaplanes();
    }

    public Saplane getById(String id) {
        return mSaplaneDAO.getById(id);
    }

    public Key<Saplane> save(Saplane saplane) {
        return mSaplaneDAO.save(saplane);
    }

    public WriteResult delete(Saplane saplane) {
        return mSaplaneDAO.delete(saplane);
    }

    @Override
    public boolean idTaken(String id) {
        return mSaplaneDAO.idTaken(id);
    }

    public Saplane findPlaneByPlaneType(String planeType) {
        return mSaplaneDAO.findPlaneByPlaneType(planeType);
    }

    public Saplane findPlaneByCarrierIdAndConnectionIdAndFlDate(String carrierId, String connectionId, String flDate) {
        return mSaplaneDAO.findPlaneByCarrierIdAndConnectionIdAndFlDate(carrierId, connectionId, flDate);
    }

}
