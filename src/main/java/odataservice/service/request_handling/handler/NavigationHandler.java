////////////////////////////////////////////////////////////////////////////////
//
// Created by BBruhns on 10.03.2018.
//
// Copyright (c) 2006 - 2018 FORCAM GmbH. All rights reserved.
////////////////////////////////////////////////////////////////////////////////

package odataservice.service.request_handling.handler;

import odataservice.service.entities.definitions.EntityNames;
import odataservice.service.request_handling.handler.services.EntityBookingService;
import odataservice.service.request_handling.handler.services.EntityCarrierService;
import odataservice.service.request_handling.handler.services.EntityConnectionService;
import odataservice.service.request_handling.handler.services.EntityFlightService;
import odataservice.service.request_handling.handler.services.EntityPlaneService;
import odataservice.service.util.Util;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 */
public class NavigationHandler {

    // ------------------------------------------------------------------------
    // constants
    // ------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(NavigationHandler.class);

    // ------------------------------------------------------------------------
    // members
    // ------------------------------------------------------------------------

    private EntityFlightService mEntityFlightService;
    private EntityBookingService mEntityBookingService;
    private EntityConnectionService mEntityConnectionService;
    private EntityPlaneService mEntityPlaneService;
    private EntityCarrierService mEntityCarrierService;

    // ------------------------------------------------------------------------
    // constructors
    // ------------------------------------------------------------------------

    public NavigationHandler() {
        mEntityFlightService = new EntityFlightService();
        mEntityBookingService = new EntityBookingService();
        mEntityConnectionService = new EntityConnectionService();
        mEntityPlaneService = new EntityPlaneService();
        mEntityCarrierService = new EntityCarrierService();
    }

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    // ========================================================================
    //                           NAVIGATION
    // ========================================================================

    public Entity getRelatedEntity(Entity entity, EdmEntityType relatedEntityType) {
        final EntityCollection collection = this.getRelatedEntityCollection(entity, relatedEntityType);

        if (collection.getEntities().isEmpty()) {
            LOGGER.info("No related entity found.");
            return null;
        }

        return collection.getEntities().get(0);
    }

    public Entity getRelatedEntity(Entity entity, EdmEntityType relatedEntityType, List<UriParameter> keyPredicates) throws ODataApplicationException {
        final EntityCollection relatedEntities = this.getRelatedEntityCollection(entity, relatedEntityType);

        return Util.findEntity(relatedEntityType, relatedEntities, keyPredicates);
    }

    public EntityCollection getRelatedEntityCollection(Entity sourceEntity, EdmEntityType targetEntityType) {
        final String sourceEntityFqn = sourceEntity.getType();
        final boolean navFromFlights = sourceEntityFqn.equals(EntityNames.ET_SFLIGHT_FQN.getFullQualifiedNameAsString());
        final boolean navFromCarriers = sourceEntityFqn.equals(EntityNames.ET_SCARR_FQN.getFullQualifiedNameAsString());
        final boolean navFromConnections = sourceEntityFqn.equals(EntityNames.ET_SPFLI_FQN.getFullQualifiedNameAsString());
        final boolean navFromBookings = sourceEntityFqn.equals(EntityNames.ET_SBOOK_FQN.getFullQualifiedNameAsString());
        EntityCollection navigationTargetEntityCollection = new EntityCollection();

        if (navFromFlights) {
            navigationTargetEntityCollection = this.getRelatedFlights(sourceEntity, targetEntityType, navigationTargetEntityCollection);
        } else if (navFromCarriers) {
            navigationTargetEntityCollection = this.getRelatedCarreirs(sourceEntity, targetEntityType, navigationTargetEntityCollection);
        } else if (navFromConnections) {
            navigationTargetEntityCollection = this.getRelatedConnections(sourceEntity, targetEntityType, navigationTargetEntityCollection);
        } else if (navFromBookings) {
            navigationTargetEntityCollection = this.getRelatedBookings(sourceEntity, targetEntityType, navigationTargetEntityCollection);
        }

        if (navigationTargetEntityCollection == null) {
            LOGGER.warn("Invalid navigation path!");
            return null;
        }

        return navigationTargetEntityCollection;
    }

    private EntityCollection getRelatedFlights(Entity sourceEntity, EdmEntityType targetEntityType, EntityCollection navigationTargetEntityCollection) {
        final FullQualifiedName relatedEntityFqn = targetEntityType.getFullQualifiedName();
        final boolean navToCarrier = relatedEntityFqn.equals(EntityNames.ET_SCARR_FQN);
        final boolean navToConnection = relatedEntityFqn.equals(EntityNames.ET_SPFLI_FQN);
        final boolean navToPlane = relatedEntityFqn.equals(EntityNames.ET_SAPLANE_FQN);
        final boolean navToBookings = relatedEntityFqn.equals(EntityNames.ET_SBOOK_FQN);

        if (navToCarrier) {
            return mEntityCarrierService.getCarrierforFlight(sourceEntity, navigationTargetEntityCollection);
        } else if (navToConnection) {
            return mEntityConnectionService.getConnectionForFlight(sourceEntity, navigationTargetEntityCollection);
        } else if (navToPlane) {
            return mEntityPlaneService.getPlaneForFlight(sourceEntity, navigationTargetEntityCollection);
        } else if (navToBookings) {
            return mEntityBookingService.getBookingsForFlight(sourceEntity, navigationTargetEntityCollection);
        } else {
            return null;
        }
    }

    private EntityCollection getRelatedCarreirs(Entity sourceEntity, EdmEntityType targetEntityType, EntityCollection navigationTargetEntityCollection) {
        final FullQualifiedName relatedEntityFqn = targetEntityType.getFullQualifiedName();
        final boolean navToFlights = relatedEntityFqn.equals(EntityNames.ET_SFLIGHT_FQN);
        final boolean navToConnections = relatedEntityFqn.equals(EntityNames.ET_SPFLI_FQN);
        final boolean navToBookings = relatedEntityFqn.equals(EntityNames.ET_SBOOK_FQN);

        if (navToFlights) {
            return mEntityFlightService.getFlightsForCarrier(sourceEntity, navigationTargetEntityCollection);
        } else if (navToConnections) {
            return mEntityConnectionService.getConnectionsForCarrier(sourceEntity, navigationTargetEntityCollection);
        } else if (navToBookings) {
            return mEntityBookingService.getBookingsForCarrier(sourceEntity, navigationTargetEntityCollection);
        } else {
            return null;
        }
    }

    private EntityCollection getRelatedConnections(Entity sourceEntity, EdmEntityType targetEntityType, EntityCollection navigationTargetEntityCollection) {
        final FullQualifiedName relatedEntityFqn = targetEntityType.getFullQualifiedName();
        final boolean navToCarrier = relatedEntityFqn.equals(EntityNames.ET_SCARR_FQN);
        final boolean navToFlights = relatedEntityFqn.equals(EntityNames.ET_SFLIGHT_FQN);
        final boolean navToBookings = relatedEntityFqn.equals(EntityNames.ET_SBOOK_FQN);

        if (navToFlights) {
            return mEntityFlightService.getFlightsForConnection(sourceEntity, navigationTargetEntityCollection);
        } else if (navToCarrier) {
            return mEntityCarrierService.getCarrierForConnection(sourceEntity, navigationTargetEntityCollection);
        } else if (navToBookings) {
            return mEntityBookingService.getBookingsForConnection(sourceEntity, navigationTargetEntityCollection);
        } else {
            return null;
        }
    }

    private EntityCollection getRelatedBookings(Entity sourceEntity, EdmEntityType targetEntityType, EntityCollection navigationTargetEntityCollection) {
        final FullQualifiedName relatedEntityFqn = targetEntityType.getFullQualifiedName();
        final boolean navToCarrier = relatedEntityFqn.equals(EntityNames.ET_SCARR_FQN);
        final boolean navToFlight = relatedEntityFqn.equals(EntityNames.ET_SFLIGHT_FQN);
        final boolean navToConnection = relatedEntityFqn.equals(EntityNames.ET_SPFLI_FQN);

        if (navToFlight) {
            return mEntityFlightService.getFlightForBooking(sourceEntity, navigationTargetEntityCollection);
        } else if (navToConnection) {
            return mEntityConnectionService.getConnectionForBooking(sourceEntity, navigationTargetEntityCollection);
        } else if (navToCarrier) {
            return mEntityCarrierService.getCarrierForBooking(sourceEntity, navigationTargetEntityCollection);
        } else {
            return null;
        }
    }
}
