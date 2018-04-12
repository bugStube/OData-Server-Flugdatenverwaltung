////////////////////////////////////////////////////////////////////////////////
//
// Created by BBruhns on 10.03.2018.
//
// Copyright (c) 2006 - 2018 FORCAM GmbH. All rights reserved.
////////////////////////////////////////////////////////////////////////////////

package odataservice.service.request_handling.handler.services;

import odataservice.database.collections.Sbook;
import odataservice.database.collections.Scarr;
import odataservice.database.collections.Sflight;
import odataservice.database.collections.Spfli;
import odataservice.database.util.DataTransformator;
import odataservice.database.service.SbookService;
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

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static odataservice.service.entities.definitions.EntityNames.BOOKING_ID;
import static odataservice.service.entities.definitions.EntityNames.CARRIER_ID;
import static odataservice.service.entities.definitions.EntityNames.CONNECTION_ID;
import static odataservice.service.entities.definitions.EntityNames.ES_SBOOK_NAME;
import static odataservice.service.entities.definitions.EntityNames.FLIGHT_DATE;

/**
 *
 */
public class EntityBookingService {

    // ------------------------------------------------------------------------
    // members
    // ------------------------------------------------------------------------

    private SbookService mSbookService;

    // ------------------------------------------------------------------------
    // constructors
    // ------------------------------------------------------------------------

    public EntityBookingService() {
        mSbookService = new SbookService();
    }

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    public EntityCollection getBookings() {
        final List<Sbook> sbooks = mSbookService.getAllSbooks();
        final List<Entity> bookings = sbooks.stream().map(DataTransformator::transformSbookToEntity).collect(Collectors.toList());
        final EntityCollection entitySet = new EntityCollection();
        entitySet.getEntities().addAll(bookings);

        return entitySet;
    }

    public Entity getBooking(EdmEntityType edmEntityType, List<UriParameter> keyParams) throws ODataApplicationException {
        final Sbook sbook = mSbookService.getById(Util.getBookingIdKey(keyParams));
        return DataTransformator.transformSbookToEntity(sbook);
    }

    public Entity createBooking(EdmEntityType edmEntityType, Entity entity) {
        final Property idProperty = entity.getProperty(BOOKING_ID);
        final String id;

        if (idProperty != null) {
            final String givenBookId = (String) idProperty.getValue();

            if (Util.idTaken(givenBookId, mSbookService)) {
                id = Util.generateId(givenBookId, 10, true, true, mSbookService);
            } else {
                id = givenBookId;
            }
            idProperty.setValue(ValueType.PRIMITIVE, id);
        } else {
            id = Util.generateId(StringUtils.EMPTY, 10, true, true, mSbookService);
            entity.getProperties().add(new Property(null, BOOKING_ID, ValueType.PRIMITIVE, id));
        }
        entity.setId(Util.createId(ES_SBOOK_NAME, id));

        final String carrierId = (String) entity.getProperty(CARRIER_ID).getValue();
        final String connectionId = (String) entity.getProperty(CONNECTION_ID).getValue();
        final String flDate = (String) entity.getProperty(FLIGHT_DATE).getValue();

        final Scarr scarr = Util.loadAssociatedCarrier(carrierId);
        final Spfli spfli = Util.loadAssociatedConnection(connectionId);
        final Sflight sflight = Util.loadAssociatedFlight(flDate);

        mSbookService.save(DataTransformator.transformEntityToSbook(entity, scarr, spfli, sflight));

        return entity;
    }

    public void updateBooking(EdmEntityType edmEntityType, List<UriParameter> keyParams, Entity updatedEntityFromRequest, HttpMethod httpMethod)
        throws ODataApplicationException {

        final Entity entityFromDB = getBooking(edmEntityType, keyParams);
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

        final Scarr scarr = Util.loadAssociatedCarrier((String) updatedEntityFromRequest.getProperty(CARRIER_ID).getValue());
        final Spfli spfli = Util.loadAssociatedConnection((String) updatedEntityFromRequest.getProperty(CONNECTION_ID).getValue());
        final Sflight sflight = Util.loadAssociatedFlight((String) updatedEntityFromRequest.getProperty(FLIGHT_DATE).getValue());
        //this is actually an update - morphias/mongo uses upsert
        mSbookService.save(DataTransformator.transformEntityToSbook(entityFromDB, scarr, spfli, sflight));
    }

    public void deleteBooking(EdmEntityType edmEntityType, List<UriParameter> keyParams) throws ODataApplicationException {
        final Entity entity = this.getBooking(edmEntityType, keyParams);

        if (entity == null) {
            throw new ODataApplicationException("Entity not found", HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ENGLISH);
        }

        final String carrierId = (String) entity.getProperty(CARRIER_ID).getValue();
        final String connectionId = (String) entity.getProperty(CONNECTION_ID).getValue();
        final String flDate = (String) entity.getProperty(FLIGHT_DATE).getValue();

        final Scarr scarr = Util.loadAssociatedCarrier(carrierId);
        final Spfli spfli = Util.loadAssociatedConnection(connectionId);
        final Sflight sflight = Util.loadAssociatedFlight(flDate);

        mSbookService.delete(DataTransformator.transformEntityToSbook(entity, scarr, spfli, sflight));
    }

    // ========================================================================
    //                           NAVIGATION
    // ========================================================================

    public EntityCollection getBookingsForFlight(Entity sourceEntity, EntityCollection navigationTargetEntityCollection) {
        final String carrierId = (String) sourceEntity.getProperty(CARRIER_ID).getValue();
        final String connectionId = (String) sourceEntity.getProperty(CONNECTION_ID).getValue();
        final String fldate = (String) sourceEntity.getProperty(FLIGHT_DATE).getValue();
        final List<Sbook> sbooks = mSbookService.findBookingsByCarrierIdAndConnectionIdAndFlDate(carrierId, connectionId, fldate);
        final List<Entity> bookings = sbooks.stream().map(DataTransformator::transformSbookToEntity).collect(Collectors.toList());

        navigationTargetEntityCollection.getEntities().addAll(bookings);

        return navigationTargetEntityCollection;
    }

    public EntityCollection getBookingsForCarrier(Entity sourceEntity, EntityCollection navigationTargetEntityCollection) {
        final String carrierId = (String) sourceEntity.getProperty(CARRIER_ID).getValue();
        final List<Sbook> sbooks = mSbookService.findBookingsByCarrierId(carrierId);
        final List<Entity> bookings = sbooks.stream().map(DataTransformator::transformSbookToEntity).collect(Collectors.toList());

        navigationTargetEntityCollection.getEntities().addAll(bookings);

        return navigationTargetEntityCollection;

    }

    public EntityCollection getBookingsForConnection(Entity sourceEntity, EntityCollection navigationTargetEntityCollection) {
        final String connectionId = (String) sourceEntity.getProperty(CONNECTION_ID).getValue();
        final List<Sbook> sbooks = mSbookService.findBookingsByConnectionId(connectionId);
        final List<Entity> bookings = sbooks.stream().map(DataTransformator::transformSbookToEntity).collect(Collectors.toList());

        navigationTargetEntityCollection.getEntities().addAll(bookings);

        return navigationTargetEntityCollection;
    }

}
