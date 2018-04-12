package odataservice.service.request_handling.handler;

import odataservice.service.entities.definitions.EntityNames;
import odataservice.service.request_handling.handler.services.EntityBookingService;
import odataservice.service.request_handling.handler.services.EntityCarrierService;
import odataservice.service.request_handling.handler.services.EntityConnectionService;
import odataservice.service.request_handling.handler.services.EntityFlightService;
import odataservice.service.request_handling.handler.services.EntityPlaneService;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.http.HttpMethod;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriParameter;

import java.util.List;

/**
 *
 */
public class CRUDHandler {

    private EntityFlightService mEntityFlightService;
    private EntityBookingService mEntityBookingService;
    private EntityConnectionService mEntityConnectionService;
    private EntityPlaneService mEntityPlaneService;
    private EntityCarrierService mEntityCarrierService;

    public CRUDHandler() {
        mEntityFlightService = new EntityFlightService();
        mEntityBookingService = new EntityBookingService();
        mEntityConnectionService = new EntityConnectionService();
        mEntityPlaneService = new EntityPlaneService();
        mEntityCarrierService = new EntityCarrierService();
    }

    public EntityCollection readEntitySetData(EdmEntitySet edmEntitySet) {

        switch (edmEntitySet.getName()) {
            case EntityNames.ES_SFLIGHT_NAME:
                return mEntityFlightService.getFlights();
            case EntityNames.ES_SPFLI_NAME:
                return mEntityConnectionService.getConnections();
            case EntityNames.ES_SCARR_NAME:
                return mEntityCarrierService.getCarriers();
            case EntityNames.ES_SBOOK_NAME:
                return mEntityBookingService.getBookings();
            case EntityNames.ES_SAPLANE_NAME:
                return mEntityPlaneService.getPlanes();
            default:
                return null;
        }
    }

    public Entity readEntityData(EdmEntitySet edmEntitySet, List<UriParameter> keyParams) throws ODataApplicationException {
        final EdmEntityType edmEntityType = edmEntitySet.getEntityType();

        switch (edmEntitySet.getName()) {
            case EntityNames.ES_SFLIGHT_NAME:
                return mEntityFlightService.getFlight(edmEntityType, keyParams);
            case EntityNames.ES_SPFLI_NAME:
                return mEntityConnectionService.getConnection(edmEntityType, keyParams);
            case EntityNames.ES_SCARR_NAME:
                return mEntityCarrierService.getCarrier(edmEntityType, keyParams);
            case EntityNames.ES_SBOOK_NAME:
                return mEntityBookingService.getBooking(edmEntityType, keyParams);
            case EntityNames.ES_SAPLANE_NAME:
                return mEntityPlaneService.getPlane(edmEntityType, keyParams);
            default:
                return null;
        }
    }

    public Entity createEntityData(EdmEntitySet edmEntitySet, Entity entityToCreate) {
        final EdmEntityType edmEntityType = edmEntitySet.getEntityType();

        switch (edmEntitySet.getName()) {
            case EntityNames.ES_SFLIGHT_NAME:
                return mEntityFlightService.createFlight(edmEntityType, entityToCreate);
            case EntityNames.ES_SPFLI_NAME:
                return mEntityConnectionService.createConnection(edmEntityType, entityToCreate);
            case EntityNames.ES_SCARR_NAME:
                return mEntityCarrierService.createCarrier(edmEntityType, entityToCreate);
            case EntityNames.ES_SBOOK_NAME:
                return mEntityBookingService.createBooking(edmEntityType, entityToCreate);
            case EntityNames.ES_SAPLANE_NAME:
                return mEntityPlaneService.createPlane(edmEntityType, entityToCreate);
            default:
                return null;
        }
    }

    public void updateEntityData(EdmEntitySet edmEntitySet, List<UriParameter> keyParams, Entity updateEntity, HttpMethod httpMethod)
        throws ODataApplicationException {
        final EdmEntityType edmEntityType = edmEntitySet.getEntityType();

        switch (edmEntitySet.getName()) {
            case EntityNames.ES_SFLIGHT_NAME:
                mEntityFlightService.updateFlight(edmEntityType, keyParams, updateEntity, httpMethod);
                return;
            case EntityNames.ES_SPFLI_NAME:
                mEntityConnectionService.updateConnection(edmEntityType, keyParams, updateEntity, httpMethod);
                return;
            case EntityNames.ES_SCARR_NAME:
                mEntityCarrierService.updateCarrier(edmEntityType, keyParams, updateEntity, httpMethod);
                return;
            case EntityNames.ES_SBOOK_NAME:
                mEntityBookingService.updateBooking(edmEntityType, keyParams, updateEntity, httpMethod);
                return;
            case EntityNames.ES_SAPLANE_NAME:
                mEntityPlaneService.updatePlane(edmEntityType, keyParams, updateEntity, httpMethod);
        }
    }

    public void deleteEntityData(EdmEntitySet edmEntitySet, List<UriParameter> keyParams) throws ODataApplicationException {
        final EdmEntityType edmEntityType = edmEntitySet.getEntityType();

        switch (edmEntitySet.getName()) {
            case EntityNames.ES_SFLIGHT_NAME:
                mEntityFlightService.deleteFlight(edmEntityType, keyParams);
                return;
            case EntityNames.ES_SPFLI_NAME:
                mEntityConnectionService.deleteConnection(edmEntityType, keyParams);
                return;
            case EntityNames.ES_SCARR_NAME:
                mEntityCarrierService.deleteCarrier(edmEntityType, keyParams);
                return;
            case EntityNames.ES_SBOOK_NAME:
                mEntityBookingService.deleteBooking(edmEntityType, keyParams);
                return;
            case EntityNames.ES_SAPLANE_NAME:
                mEntityPlaneService.deletePlane(edmEntityType, keyParams);
        }
    }
}
