package odataservice.service.util;

import odataservice.database.collections.Saplane;
import odataservice.database.collections.Scarr;
import odataservice.database.collections.Sflight;
import odataservice.database.collections.Spfli;
import odataservice.database.service.IDBService;
import odataservice.database.service.SaplaneService;
import odataservice.database.service.ScarrService;
import odataservice.database.service.SflightService;
import odataservice.database.service.SpfliService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.edm.EdmBindingTarget;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmKeyPropertyRef;
import org.apache.olingo.commons.api.edm.EdmNavigationProperty;
import org.apache.olingo.commons.api.edm.EdmPrimitiveType;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeException;
import org.apache.olingo.commons.api.edm.EdmProperty;
import org.apache.olingo.commons.api.edm.EdmType;
import org.apache.olingo.commons.api.ex.ODataRuntimeException;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriInfoResource;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

import static odataservice.service.entities.definitions.EntityNames.BOOKING_ID;
import static odataservice.service.entities.definitions.EntityNames.CARRIER_ID;
import static odataservice.service.entities.definitions.EntityNames.CONNECTION_ID;
import static odataservice.service.entities.definitions.EntityNames.FLIGHT_DATE;
import static odataservice.service.entities.definitions.EntityNames.PLANE_TYPE;

/**
 *
 */
public final class Util {

    public static String generateRandomId(int length, boolean useLetters, boolean useNumbers) {
        return RandomStringUtils.random(length, useLetters, useNumbers);
    }

    public static URI createId(String entitySetName, Object id) {
        try {
            return new URI(entitySetName + "(" + String.valueOf(id) + ")");
        } catch (URISyntaxException e) {
            throw new ODataRuntimeException("Unable to create id for entity: " + entitySetName, e);
        }
    }

    public static boolean idTaken(String idToCheckIfTaken, IDBService idbService) {
        return StringUtils.isEmpty(idToCheckIfTaken) || idbService.idTaken(idToCheckIfTaken);
    }

    //checks first if the given id is not taken, if so a new one will be created and returned
    public static String generateId(String idToCheckIfTaken, int length, boolean useLetters, boolean useNumbers, IDBService idbService) {
        while (Util.idTaken(idToCheckIfTaken, idbService)) {
            idToCheckIfTaken = Util.generateRandomId(length, useLetters, useNumbers);
        }

        return idToCheckIfTaken;
    }

    public static Scarr loadAssociatedCarrier(String carrierId) {
        return new ScarrService().getById(carrierId);
    }

    public static Spfli loadAssociatedConnection(String connectionId) {
        return new SpfliService().getById(connectionId);
    }

    public static Saplane loadAssociatedPlane(String planeType) {
        return new SaplaneService().getById(planeType);
    }

    public static Sflight loadAssociatedFlight(String flDate) {
        return new SflightService().getById(flDate);
    }

    public static EdmEntitySet getEdmEntitySet(UriInfoResource uriInfo) throws ODataApplicationException {
        final List<UriResource> resourcePaths = uriInfo.getUriResourceParts();
        //To get the entity set all URI segments have to bo interpreted
        if (!(resourcePaths.get(0) instanceof UriResourceEntitySet)) {
            throw new ODataApplicationException("Invalid resource type for first segment.", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
        }

        final UriResourceEntitySet uriResource = (UriResourceEntitySet) resourcePaths.get(0);
        return uriResource.getEntitySet();
    }

    public static boolean isKey(EdmEntityType edmEntityType, String propertyName) {
        final List<EdmKeyPropertyRef> keyPropertyRefs = edmEntityType.getKeyPropertyRefs();

        for (EdmKeyPropertyRef propRef : keyPropertyRefs) {
            String keyPropertyName = propRef.getName();
            if (keyPropertyName.equals(propertyName)) {
                return true;
            }
        }

        return false;
    }

    public static String getBookingIdKey(List<UriParameter> keyParams) {
        final String quote = "'";

        for (UriParameter key : keyParams) {
            String keyText = key.getText();

            if (BOOKING_ID.equals(key.getName())) {
                if (keyText.contains(quote)) {
                    keyText = keyText.replace(quote, StringUtils.EMPTY);
                }
                return keyText;
            }
        }

        return null;
    }

    public static String getCarrierIdKey(List<UriParameter> keyParams) {
        final String quote = "'";

        for (UriParameter key : keyParams) {
            String keyText = key.getText();

            if (CARRIER_ID.equals(key.getName())) {
                if (keyText.contains(quote)) {
                    keyText = keyText.replace(quote, StringUtils.EMPTY);
                }
                return keyText;
            }
        }

        return null;
    }

    public static String getConnectionIdKey(List<UriParameter> keyParams) {
        final String quote = "'";

        for (UriParameter key : keyParams) {
            String keyText = key.getText();

            if (CONNECTION_ID.equals(key.getName())) {
                if (keyText.contains(quote)) {
                    keyText = keyText.replace(quote, StringUtils.EMPTY);
                }
                return keyText;
            }
        }

        return null;
    }

    public static String getFlightDateKey(List<UriParameter> keyParams) {
        final String quote = "'";

        for (UriParameter key : keyParams) {
            String keyText = key.getText();

            if (FLIGHT_DATE.equals(key.getName())) {
                if (keyText.contains(quote)) {
                    keyText = keyText.replace(quote, StringUtils.EMPTY);
                }
                return keyText;
            }
        }

        return null;
    }

    public static String getPlaneTypeKey(List<UriParameter> keyParams) {
        final String quote = "'";

        for (UriParameter key : keyParams) {
            String keyText = key.getText();

            if (PLANE_TYPE.equals(key.getName())) {
                if (keyText.contains(quote)) {
                    keyText = keyText.replace(quote, StringUtils.EMPTY);
                }
                return keyText;
            }
        }

        return null;
    }

    /** Finds single {@link Entity} from the {@link EntityCollection} that matches all keys from the list of {@link UriParameter}s. */
    public static Entity findEntity(EdmEntityType edmEntityType, EntityCollection rt_entitySet, List<UriParameter> keyParams) throws ODataApplicationException {
        final List<Entity> entityList = rt_entitySet.getEntities();

        // looping over all entities in order to find that one that matches all keys in request e.g. contacts(ContactID=1, CompanyID=1)
        for (Entity entity : entityList) {
            final boolean foundEntity = entityMatchesAllKeys(edmEntityType, entity, keyParams);
            if (foundEntity) {
                return entity;
            }
        }

        return null;
    }

    /**
     * Compares all given keys (list of {@link UriParameter}) with the the {@link Entity}.
     *
     * @return {@code true} if all keys match.
     */
    public static boolean entityMatchesAllKeys(EdmEntityType edmEntityType, Entity rt_entity, List<UriParameter> keyParams) throws ODataApplicationException {
        for (final UriParameter key : keyParams) {
            final String keyName = key.getName();
            String keyText = key.getText();
            final String quote = "'";

            if (keyText.contains(quote)) {
                keyText = keyText.replace(quote, StringUtils.EMPTY);
            }

            // note: below line doesn't consider: keyProp which can be part of a complexType in OData V4
            // in such a case, it would be required to access it via getKeyPropertyRef() --> here it doesn't matter
            final EdmProperty edmKeyProperty = (EdmProperty) edmEntityType.getProperty(keyName);
            final Boolean isNullable = edmKeyProperty.isNullable();
            final Integer maxLength = edmKeyProperty.getMaxLength();
            final Integer precision = edmKeyProperty.getPrecision();
            final Boolean isUnicode = edmKeyProperty.isUnicode();
            final Integer scale = edmKeyProperty.getScale();
            final EdmType edmType = edmKeyProperty.getType();
            final EdmPrimitiveType edmPrimitiveType = (EdmPrimitiveType) edmType;
            // don't need to check for null, this is done in FWK
            final Object valueObject = rt_entity.getProperty(keyName).getValue();

            // valueObject should be compared with the keyText -->  this is done using the type.valueToString
            final String valueAsString;
            try {
                valueAsString = edmPrimitiveType.valueToString(valueObject, isNullable, maxLength, precision, scale, isUnicode);
            } catch (EdmPrimitiveTypeException e) {
                throw new ODataApplicationException("Failed to retrieve String value", HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH, e);
            }

            if (valueAsString == null) {
                return false;
            }

            final boolean matches = valueAsString.equals(keyText);
            if (!matches) {
                // if any of the key properties is not found in the entity, we don't need to search further
                return false;
            }
        }

        return true;
    }

    /**
     * Copied from documentation:
     * <p>
     * <b>Example:</b>
     * For the following navigation: {@code DemoService.svc/Categories(1)/Products} the EdmEntitySet for the navigation property "Products" is needed.
     * This is defined as follows in the metadata:
     * {@code EntitySet Name="Categories" EntityType="OData.Demo.Category"}
     * <br>
     * {@code NavigationPropertyBinding Path="Products" Target="Products"}
     * <p>
     * The "Target" attribute specifies the target EntitySet.
     * Therefore the startEntitySet "Categories" is needed in order to retrieve the target EntitySet "Products".
     */
    public static EdmEntitySet getNavigationTargetEntitySet(EdmEntitySet startEdmEntitySet, EdmNavigationProperty edmNavigationProperty)
        throws ODataApplicationException {

        final EdmEntitySet navigationTargetEntitySet;
        final String navPropName = edmNavigationProperty.getName();
        final EdmBindingTarget edmBindingTarget = startEdmEntitySet.getRelatedBindingTarget(navPropName);

        if (edmBindingTarget == null) {
            throw new ODataApplicationException("Not supported.", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ROOT);
        }

        if (edmBindingTarget instanceof EdmEntitySet) {
            navigationTargetEntitySet = (EdmEntitySet) edmBindingTarget;
        } else {
            throw new ODataApplicationException("Not supported.", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ROOT);
        }

        return navigationTargetEntitySet;
    }

}
