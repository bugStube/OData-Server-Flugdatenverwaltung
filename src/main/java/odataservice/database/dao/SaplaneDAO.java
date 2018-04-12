////////////////////////////////////////////////////////////////////////////////
//
// Created by BBruhns on 10.03.2018.
//
// Copyright (c) 2006 - 2018 FORCAM GmbH. All rights reserved.
////////////////////////////////////////////////////////////////////////////////

package odataservice.database.dao;

import com.mongodb.WriteResult;
import odataservice.database.collections.Saplane;
import org.mongodb.morphia.Key;

import java.util.List;

/**
 *
 */
public interface SaplaneDAO {

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    Saplane getById(String id);

    Key<Saplane> save(Saplane saplane);

    WriteResult delete(Saplane saplane);

    boolean idTaken(String id);

    List<Saplane> getAllSaplanes();

    Saplane findPlaneByPlaneType(String planeType);

    Saplane findPlaneByCarrierIdAndConnectionIdAndFlDate(String carrierId, String connectionId, String flDate);

}
