////////////////////////////////////////////////////////////////////////////////
//
// Created by BBruhns on 10.03.2018.
//
// Copyright (c) 2006 - 2018 FORCAM GmbH. All rights reserved.
////////////////////////////////////////////////////////////////////////////////

package odataservice.database.dao;

import com.mongodb.WriteResult;
import odataservice.database.collections.Scarr;
import org.mongodb.morphia.Key;

import java.util.List;

/**
 *
 */
public interface ScarrDAO {

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    Scarr getById(String id);

    Key<Scarr> save(Scarr scarr);

    WriteResult delete(Scarr scarr);

    boolean idTaken(String id);

    List<Scarr> getAllScarrs();

}
