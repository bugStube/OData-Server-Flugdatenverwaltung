////////////////////////////////////////////////////////////////////////////////
//
// Created by BBruhns on 10.03.2018.
//
// Copyright (c) 2006 - 2018 FORCAM GmbH. All rights reserved.
////////////////////////////////////////////////////////////////////////////////

package odataservice.database.service;

import com.mongodb.WriteResult;
import odataservice.database.collections.Scarr;
import odataservice.database.connection.MorphiaService;
import odataservice.database.dao.ScarrDAO;
import odataservice.database.dao.ScarrDAOImpl;
import org.mongodb.morphia.Key;

import java.util.List;

import static odataservice.service.entities.definitions.EntityNames.DB_NAME;

/**
 *
 */
public class ScarrService implements IDBService {

    // ------------------------------------------------------------------------
    // members
    // ------------------------------------------------------------------------

    private ScarrDAO mScarrDAO;

    // ------------------------------------------------------------------------
    // constructors
    // ------------------------------------------------------------------------

    public ScarrService() {
        final MorphiaService morphiaService = new MorphiaService();
        mScarrDAO = new ScarrDAOImpl(Scarr.class, morphiaService.getMongoClient(), morphiaService.getMorphia(), DB_NAME);
    }

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    public List<Scarr> getAllScarrs() {
        return mScarrDAO.getAllScarrs();
    }

    public Scarr getById(String id) {
        return mScarrDAO.getById(id);
    }

    public Key<Scarr> save(Scarr scarr) {
        return mScarrDAO.save(scarr);
    }

    public WriteResult delete(Scarr scarr) {
        return mScarrDAO.delete(scarr);
    }

    @Override
    public boolean idTaken(String id) {
        return mScarrDAO.idTaken(id);
    }

}
