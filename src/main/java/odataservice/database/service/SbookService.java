////////////////////////////////////////////////////////////////////////////////
//
// Created by BBruhns on 10.03.2018.
//
// Copyright (c) 2006 - 2018 FORCAM GmbH. All rights reserved.
////////////////////////////////////////////////////////////////////////////////

package odataservice.database.service;

import com.mongodb.WriteResult;
import odataservice.database.collections.Sbook;
import odataservice.database.connection.MorphiaService;
import odataservice.database.dao.SbookDAO;
import odataservice.database.dao.SbookDAOImpl;
import odataservice.service.entities.definitions.EntityNames;
import org.mongodb.morphia.Key;

import java.util.List;

/**
 *
 */
public class SbookService implements IDBService {

    // ------------------------------------------------------------------------
    // members
    // ------------------------------------------------------------------------

    private SbookDAO mSbookDAO;

    // ------------------------------------------------------------------------
    // constructors
    // ------------------------------------------------------------------------

    public SbookService() {
        final MorphiaService morphiaService = new MorphiaService();
        mSbookDAO = new SbookDAOImpl(Sbook.class, morphiaService.getMongoClient(), morphiaService.getMorphia(), EntityNames.DB_NAME);
    }

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    public List<Sbook> getAllSbooks() {
        return mSbookDAO.getAllSbooks();
    }

    public Sbook getById(String id) {
        return mSbookDAO.getById(id);
    }

    public Key<Sbook> save(Sbook sbook) {
        return mSbookDAO.save(sbook);
    }

    public WriteResult delete(Sbook sbook) {
        return mSbookDAO.delete(sbook);
    }

    @Override
    public boolean idTaken(String id) {
        return mSbookDAO.idTaken(id);
    }

    public List<Sbook> findBookingsByCarrierId(String carrierId) {
        return mSbookDAO.findBookingsByCarrierId(carrierId);
    }

    public List<Sbook> findBookingsByCarrierIdAndConnectionIdAndFlDate(String carrierCode, String connId, String fldate) {
        return mSbookDAO.findBookingsByCarrierIdAndConnectionIdAndFlDate(carrierCode, connId, fldate);
    }

    public List<Sbook> findBookingsByConnectionId(String connectionId) {
        return mSbookDAO.findBookingsByConnectionId(connectionId);
    }

}
