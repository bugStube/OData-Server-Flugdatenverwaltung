////////////////////////////////////////////////////////////////////////////////
//
// Created by BBruhns on 10.03.2018.
//
// Copyright (c) 2006 - 2018 FORCAM GmbH. All rights reserved.
////////////////////////////////////////////////////////////////////////////////

package odataservice.service.request_handling.handler.services;

import odataservice.database.collections.Scarr;
import odataservice.database.util.DataTransformator;
import odataservice.database.service.ScarrService;
import odataservice.service.entities.definitions.EntityNames;
import odataservice.service.util.Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.http.HttpMethod;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 *
 */
public class EntityCarrierService {

    // ------------------------------------------------------------------------
    // constants
    // ------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(EntityCarrierService.class);

    // ------------------------------------------------------------------------
    // members
    // ------------------------------------------------------------------------

    private ScarrService mScarrService;

    // ------------------------------------------------------------------------
    // constructors
    // ------------------------------------------------------------------------

    public EntityCarrierService() {
        mScarrService = new ScarrService();
    }

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    public EntityCollection getCarriers() {
        final List<Scarr> scarrs = mScarrService.getAllScarrs();
        final List<Entity> carriers = scarrs.stream().map(DataTransformator::transformScarrToEntity).collect(Collectors.toList());
        final EntityCollection entitySet = new EntityCollection();
        entitySet.getEntities().addAll(carriers);

        return entitySet;
    }

    public Entity getCarrier(EdmEntityType edmEntityType, List<UriParameter> keyParams) throws ODataApplicationException {
        final Scarr scarr = mScarrService.getById(Util.getCarrierIdKey(keyParams));
        return DataTransformator.transformScarrToEntity(scarr);
    }

    public Entity createCarrier(EdmEntityType edmEntityType, Entity entity) {
        final Property idProperty = entity.getProperty(EntityNames.CARRIER_ID);
        final String carrierName = (String) entity.getProperty(EntityNames.CARRIER_NAME).getValue();
        final String carrierId;

        if (idProperty != null) {
            final String carrierCode = (String) idProperty.getValue();

            if (Util.idTaken(carrierCode, mScarrService)) {
                LOGGER.info("The carrier code {} is already taken. A new random carrier code will be generated.", carrierCode);
                carrierId = this.generateScarrId(carrierName);
            } else {
                carrierId = carrierCode;
            }
            idProperty.setValue(ValueType.PRIMITIVE, carrierId);
        } else {
            //as of OData v4 spec, the key property can be omitted from the POST request body
            carrierId = this.generateScarrId(carrierName);
            entity.getProperties().add(new Property(null, EntityNames.CARRIER_ID, ValueType.PRIMITIVE, carrierId));
        }
        entity.setId(Util.createId(EntityNames.ES_SCARR_NAME, carrierId));
        mScarrService.save(DataTransformator.transformEntityToScarr(entity));

        return entity;
    }

    public void updateCarrier(EdmEntityType edmEntityType, List<UriParameter> keyParams, Entity updatedEntityFromRequest, HttpMethod httpMethod)
        throws ODataApplicationException {

        final Entity entityFromDB = getCarrier(edmEntityType, keyParams);
        if (entityFromDB == null) {
            throw new ODataApplicationException("Entity not found", HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ENGLISH);
        }

        final List<Property> existingProperties = entityFromDB.getProperties();
        for (Property existingProperty : existingProperties) {
            final String propName = existingProperty.getName();

            if (Util.isKey(edmEntityType, propName)) {
                continue;
            }

            final Property updateProperty = updatedEntityFromRequest.getProperty(propName);

            if (updateProperty == null) {
                //if a property has not been added to the request payload
                //depending on the HttpMethod the behaviour should be different
                if (httpMethod.equals(HttpMethod.PUT)) {
                    // in case of PUT, the existing property is set to null --> deleted
                    existingProperty.setValue(existingProperty.getValueType(), null);
                    // in case of PATCH, the existing property is not touched
                }
            } else {
                if (updateProperty.getValue() != null) {
                    existingProperty.setValue(existingProperty.getValueType(), updateProperty.getValue());
                }//  property (key) is found in payload, but no value is set, then the value will not be touched.
            }
        }
        //this is actually an update - morphias/mongo uses upsert
        mScarrService.save(DataTransformator.transformEntityToScarr(entityFromDB));
    }

    public void deleteCarrier(EdmEntityType edmEntityType, List<UriParameter> keyParams) throws ODataApplicationException {
        final Entity entity = this.getCarrier(edmEntityType, keyParams);

        if (entity == null) {
            throw new ODataApplicationException("Entity not found", HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ENGLISH);
        }

        mScarrService.delete(DataTransformator.transformEntityToScarr(entity));
    }

    // ========================================================================
    //                           NAVIGATION
    // ========================================================================

    public EntityCollection getCarrierforFlight(Entity sourceEntity, EntityCollection navigationTargetEntityCollection) {
        final String carrierCode = (String) sourceEntity.getProperty(EntityNames.CARRIER_ID).getValue();
        final Scarr scarr = mScarrService.getById(carrierCode);
        final Entity carrier = DataTransformator.transformScarrToEntity(scarr);

        navigationTargetEntityCollection.getEntities().add(carrier);

        return navigationTargetEntityCollection;
    }

    public EntityCollection getCarrierForConnection(Entity sourceEntity, EntityCollection navigationTargetEntityCollection) {
        final String carrierId = (String) sourceEntity.getProperty(EntityNames.CARRIER_ID).getValue();
        final Scarr scarr = mScarrService.getById(carrierId);
        final Entity carrier = DataTransformator.transformScarrToEntity(scarr);

        navigationTargetEntityCollection.getEntities().add(carrier);

        return navigationTargetEntityCollection;
    }

    public EntityCollection getCarrierForBooking(Entity sourceEntity, EntityCollection navigationTargetEntityCollection) {
        final String carrierId = (String) sourceEntity.getProperty(EntityNames.CARRIER_ID).getValue();
        final Scarr scarr = mScarrService.getById(carrierId);
        final Entity carrier = DataTransformator.transformScarrToEntity(scarr);

        navigationTargetEntityCollection.getEntities().add(carrier);

        return navigationTargetEntityCollection;
    }

    /**
     * Generates from the given carrier name a carrier id. If that generated id is taken a random five letter id will be generated.
     *
     * @param carrierName The carrier name of which a carrier id will be tried to generate.
     * @return A not taken carrier id.
     */
    public String generateScarrId(String carrierName) {
        final String carrierId;
        String idToCheckIfTaken = null;

        //try to build id from carrier name
        if (StringUtils.isNotEmpty(carrierName)) {
            final int posOfWhiteSpace = carrierName.indexOf(" ");
            Character secondLetter = null;
            if (posOfWhiteSpace != -1) {
                secondLetter = carrierName.charAt(posOfWhiteSpace + 1);
            }
            if (secondLetter == null) {
                secondLetter = carrierName.charAt(1);
            }
            final Character firstLetter = carrierName.charAt(0);
            idToCheckIfTaken = String.valueOf(firstLetter) + String.valueOf(secondLetter);
        }
        carrierId = Util.generateId(idToCheckIfTaken, 5, true, false, mScarrService);

        return carrierId;
    }

}
