////////////////////////////////////////////////////////////////////////////////
//
// Created by BBruhns on 10.03.2018.
//
// Copyright (c) 2006 - 2018 FORCAM GmbH. All rights reserved.
////////////////////////////////////////////////////////////////////////////////

package odataservice.database.service;

import com.mongodb.WriteResult;
import odataservice.database.collections.Sflight;
import odataservice.database.connection.MorphiaService;
import odataservice.database.dao.SflightDAO;
import odataservice.database.dao.SflightDAOImpl;
import odataservice.service.entities.definitions.EntityNames;
import org.mongodb.morphia.Key;

import java.util.List;

/**
 *
 */
public class SflightService implements IDBService {

    // ------------------------------------------------------------------------
    // members
    // ------------------------------------------------------------------------

    private SflightDAO mSflightDAO;

    // ------------------------------------------------------------------------
    // constructors
    // ------------------------------------------------------------------------

    public SflightService() {
        final MorphiaService morphiaService = new MorphiaService();
        mSflightDAO = new SflightDAOImpl(Sflight.class, morphiaService.getMongoClient(), morphiaService.getMorphia(), EntityNames.DB_NAME);
    }

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    public List<Sflight> getAllSflights() {
        return mSflightDAO.getAllSflights();
    }

    public Sflight getById(String id) {
        return mSflightDAO.getById(id);
    }

    public Key<Sflight> save(Sflight sflight) {
        return mSflightDAO.save(sflight);
    }

    public WriteResult delete(Sflight sflight) {
        return mSflightDAO.delete(sflight);
    }

    @Override
    public boolean idTaken(String id) {
        return mSflightDAO.idTaken(id);
    }

    public Sflight findFlightByCarrierIdAndConnectionIdAndFlDate(String carrierId, String connectionId, String flDate) {
        return mSflightDAO.findFlightByCarrierIdAndConnectionIdAndFlDate(carrierId, connectionId, flDate);
    }

    public List<Sflight> findFlightsByCarrierId(String carrierCode) {
        return mSflightDAO.findFlightsByCarrierId(carrierCode);
    }

    public List<Sflight> findFlightsByCarrierIdAndConnectionId(String carrierId, String connectionId) {
        return mSflightDAO.findFlightsByCarrierIdAndConnectionId(carrierId, connectionId);
    }

}
