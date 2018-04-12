////////////////////////////////////////////////////////////////////////////////
//
// Created by BBruhns on 10.03.2018.
//
// Copyright (c) 2006 - 2018 FORCAM GmbH. All rights reserved.
////////////////////////////////////////////////////////////////////////////////

package odataservice.database.dao;

import com.mongodb.WriteResult;
import odataservice.database.collections.Spfli;
import org.mongodb.morphia.Key;

import java.util.List;

/**
 *
 */
public interface SpfliDAO {

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    Spfli getById(String id);

    Key<Spfli> save(Spfli spfli);

    WriteResult delete(Spfli spfli);

    boolean idTaken(String id);

    List<Spfli> getAllSpflis();

    Spfli findConnectionByConnectionIdAndCarrierId(String connectionId, String carrierId);

    List<Spfli> findConnectionsByCarrierId(String carrierId);
}
