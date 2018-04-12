////////////////////////////////////////////////////////////////////////////////
//
// Created by BBruhns on 10.03.2018.
//
// Copyright (c) 2006 - 2018 FORCAM GmbH. All rights reserved.
////////////////////////////////////////////////////////////////////////////////

package odataservice.database.service;

import com.mongodb.WriteResult;
import odataservice.database.collections.Spfli;
import odataservice.database.connection.MorphiaService;
import odataservice.database.dao.SpfliDAO;
import odataservice.database.dao.SpfliDAOImpl;
import odataservice.service.entities.definitions.EntityNames;
import org.mongodb.morphia.Key;

import java.util.List;

/**
 *
 */
public class SpfliService implements IDBService {

    // ------------------------------------------------------------------------
    // members
    // ------------------------------------------------------------------------

    private SpfliDAO mSpfliDAO;

    // ------------------------------------------------------------------------
    // constructors
    // ------------------------------------------------------------------------

    public SpfliService() {
        final MorphiaService morphiaService = new MorphiaService();
        mSpfliDAO = new SpfliDAOImpl(Spfli.class, morphiaService.getMongoClient(), morphiaService.getMorphia(), EntityNames.DB_NAME);
    }

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    public List<Spfli> getAllSpflis() {
        return mSpfliDAO.getAllSpflis();
    }

    public Spfli getById(String id) {
        return mSpfliDAO.getById(id);
    }

    public Key<Spfli> save(Spfli spfli) {
        return mSpfliDAO.save(spfli);
    }

    public WriteResult delete(Spfli spfli) {
        return mSpfliDAO.delete(spfli);
    }

    @Override
    public boolean idTaken(String id) {
        return mSpfliDAO.idTaken(id);
    }

    public Spfli findConnectionByConnectionIdAndCarrierId(String connectionId, String carrierId) {
        return mSpfliDAO.findConnectionByConnectionIdAndCarrierId(connectionId, carrierId);
    }

    public List<Spfli> findConnectionsByCarrierId(String carrierId) {
        return mSpfliDAO.findConnectionsByCarrierId(carrierId);
    }

}
