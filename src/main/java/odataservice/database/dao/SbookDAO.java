////////////////////////////////////////////////////////////////////////////////
//
// Created by BBruhns on 10.03.2018.
//
// Copyright (c) 2006 - 2018 FORCAM GmbH. All rights reserved.
////////////////////////////////////////////////////////////////////////////////

package odataservice.database.dao;

import com.mongodb.WriteResult;
import odataservice.database.collections.Sbook;
import org.mongodb.morphia.Key;

import java.util.List;

/**
 *
 */
public interface SbookDAO {

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    Sbook getById(String id);

    Key<Sbook> save(Sbook sbook);

    WriteResult delete(Sbook sbook);

    boolean idTaken(String id);

    List<Sbook> getAllSbooks();

    List<Sbook> findBookingsByCarrierId(String carrierId);

    List<Sbook> findBookingsByCarrierIdAndConnectionIdAndFlDate(String carrierCode, String connId, String fldate);

    List<Sbook> findBookingsByConnectionId(String connectionId);

}
