////////////////////////////////////////////////////////////////////////////////
//
// Created by bruhn on 15.03.2018.
//
// Copyright (c) 2006 - 2018 FORCAM GmbH. All rights reserved.
////////////////////////////////////////////////////////////////////////////////

package odataservice.database.connection;

import com.mongodb.MongoClient;
import odataservice.service.entities.definitions.EntityNames;
import org.mongodb.morphia.Morphia;

/**
 *
 */
public class MorphiaService {

    // ------------------------------------------------------------------------
    // members
    // ------------------------------------------------------------------------

    protected MongoClient mMongoClient;
    protected Morphia mMorphia;

    // ------------------------------------------------------------------------
    // constructors
    // ------------------------------------------------------------------------

    public MorphiaService() {
        mMongoClient = new MongoClient("localhost", 27017);
        mMorphia = new Morphia();
        mMorphia.mapPackage(EntityNames.PACKAGE_NAME);
    }

    // ------------------------------------------------------------------------
    // getters/setters
    // ------------------------------------------------------------------------

    public MongoClient getMongoClient() {
        return mMongoClient;
    }

    public Morphia getMorphia() {
        return mMorphia;
    }
}
