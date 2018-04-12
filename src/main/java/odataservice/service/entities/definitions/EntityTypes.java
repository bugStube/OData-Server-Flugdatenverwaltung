package odataservice.service.entities.definitions;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class EntityTypes {

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    public static CsdlEntityType getFlightEntityType() {
        // Key Properties
        final CsdlProperty carrierId = new CsdlProperty().setName(EntityNames.CARRIER_ID).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty connectionId = new CsdlProperty().setName(EntityNames.CONNECTION_ID).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty flightDate = new CsdlProperty().setName(EntityNames.FLIGHT_DATE).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

        // Properties
        final CsdlProperty planeType = new CsdlProperty().setName(EntityNames.PLANE_TYPE).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty speed = new CsdlProperty().setName(EntityNames.SPEED).setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName());
        final CsdlProperty price = new CsdlProperty().setName(EntityNames.PRICE).setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName());
        final CsdlProperty currency = new CsdlProperty().setName(EntityNames.CURRENCY).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty seatsMax = new CsdlProperty().setName(EntityNames.SEATS_MAX_E).setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
        final CsdlProperty seatsOcc = new CsdlProperty().setName(EntityNames.SEATS_OCC_E).setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
        final CsdlProperty seatsMaxB = new CsdlProperty().setName(EntityNames.SEATS_MAX_B).setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
        final CsdlProperty seatsOccB = new CsdlProperty().setName(EntityNames.SEATS_OCC_B).setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
        final CsdlProperty seatsMaxF = new CsdlProperty().setName(EntityNames.SEATS_MAX_F).setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
        final CsdlProperty seatsOccF = new CsdlProperty().setName(EntityNames.SEATS_OCC_F).setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());

        // create PropertyRefs for Key elements
        final CsdlPropertyRef propRefCarrierId = new CsdlPropertyRef().setName(EntityNames.CARRIER_ID);
        final CsdlPropertyRef propRefConnectionId = new CsdlPropertyRef().setName(EntityNames.CONNECTION_ID);
        final CsdlPropertyRef propRefFlightDate = new CsdlPropertyRef().setName(EntityNames.FLIGHT_DATE);

        // navigation properties
        final CsdlNavigationProperty navPropCarrier = new CsdlNavigationProperty().setName(EntityNames.ET_SCARR_NAME)
                                                                                  .setType(EntityNames.ET_SCARR_FQN)
                                                                                  .setPartner(EntityNames.ES_SFLIGHT_NAME)
                                                                                  .setCollection(false)
                                                                                  .setNullable(false);
        final CsdlNavigationProperty navPropConnection = new CsdlNavigationProperty().setName(EntityNames.ET_SPFLI_NAME)
                                                                                     .setType(EntityNames.ET_SPFLI_FQN)
                                                                                     .setPartner(EntityNames.ES_SFLIGHT_NAME)
                                                                                     .setCollection(false)
                                                                                     .setNullable(false);
        final CsdlNavigationProperty navPropPlane = new CsdlNavigationProperty().setName(EntityNames.ET_SAPLANE_NAME)
                                                                                .setType(EntityNames.ET_SAPLANE_FQN)
                                                                                .setPartner(EntityNames.ES_SFLIGHT_NAME)
                                                                                .setCollection(false)
                                                                                .setNullable(false);
        final CsdlNavigationProperty navPropBookings = new CsdlNavigationProperty().setName(EntityNames.ES_SBOOK_NAME)
                                                                                   .setType(EntityNames.ET_SBOOK_FQN)
                                                                                   .setPartner(EntityNames.ET_SFLIGHT_NAME)
                                                                                   .setCollection(true)
                                                                                   .setNullable(false);
        final List<CsdlNavigationProperty> navPropList = new ArrayList<>(Arrays.asList(navPropCarrier, navPropConnection, navPropPlane, navPropBookings));

        // configure EntityType
        final CsdlEntityType entityType = new CsdlEntityType();
        entityType.setName(EntityNames.ET_SFLIGHT_NAME);
        entityType.setProperties(Arrays.asList(carrierId,
                                               connectionId,
                                               flightDate,
                                               planeType,
                                               speed,
                                               price,
                                               currency,
                                               seatsMax,
                                               seatsOcc,
                                               seatsMaxB,
                                               seatsOccB,
                                               seatsMaxF,
                                               seatsOccF));
        entityType.setKey(Arrays.asList(propRefCarrierId, propRefConnectionId, propRefFlightDate));
        entityType.setNavigationProperties(navPropList);

        return entityType;
    }

    public static CsdlEntityType getConnectionEntityType() {
        // Key Properties
        final CsdlProperty carrierId = new CsdlProperty().setName(EntityNames.CARRIER_ID).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty connectionId = new CsdlProperty().setName(EntityNames.CONNECTION_ID).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

        // Properties
        final CsdlProperty airpFrom = new CsdlProperty().setName(EntityNames.AIRPORT_FROM).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty cityFrom = new CsdlProperty().setName(EntityNames.CITY_FROM).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty countryFr = new CsdlProperty().setName(EntityNames.COUNTRY_FROM).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty airpTo = new CsdlProperty().setName(EntityNames.AIRPORT_TO).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty cityTo = new CsdlProperty().setName(EntityNames.CITY_TO).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty countryTo = new CsdlProperty().setName(EntityNames.COUNTRY_TO).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty flTime = new CsdlProperty().setName(EntityNames.FLIGHT_TIME).setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
        final CsdlProperty depTime = new CsdlProperty().setName(EntityNames.DEPARTURE_TIME).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty arrTime = new CsdlProperty().setName(EntityNames.ARRIVAL_TIME).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty distance = new CsdlProperty().setName(EntityNames.DISTANCE).setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName());
        final CsdlProperty distanceId = new CsdlProperty().setName(EntityNames.DISTANCE_UNIT).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty flyType = new CsdlProperty().setName(EntityNames.FLIGHT_TYPE).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty period = new CsdlProperty().setName(EntityNames.PERIOD).setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());

        // create PropertyRefs for Key elements
        final CsdlPropertyRef propRefCarrierId = new CsdlPropertyRef().setName(EntityNames.CARRIER_ID);
        final CsdlPropertyRef propRefConnectionId = new CsdlPropertyRef().setName(EntityNames.CONNECTION_ID);

        // navigation properties
        final CsdlNavigationProperty navPropFlights = new CsdlNavigationProperty().setName(EntityNames.ES_SFLIGHT_NAME)
                                                                                  .setType(EntityNames.ET_SFLIGHT_FQN)
                                                                                  .setPartner(EntityNames.ET_SPFLI_NAME)
                                                                                  .setCollection(true)
                                                                                  .setNullable(false);
        final CsdlNavigationProperty navPropBookings = new CsdlNavigationProperty().setName(EntityNames.ES_SBOOK_NAME)
                                                                                   .setType(EntityNames.ET_SBOOK_FQN)
                                                                                   .setPartner(EntityNames.ET_SPFLI_NAME)
                                                                                   .setCollection(true)
                                                                                   .setNullable(false);
        final CsdlNavigationProperty navPropCarrier = new CsdlNavigationProperty().setName(EntityNames.ET_SCARR_NAME)
                                                                                  .setType(EntityNames.ET_SCARR_FQN)
                                                                                  .setPartner(EntityNames.ES_SPFLI_NAME)
                                                                                  .setCollection(false)
                                                                                  .setNullable(false);
        final List<CsdlNavigationProperty> navPropList = new ArrayList<>(Arrays.asList(navPropFlights, navPropBookings, navPropCarrier));

        // configure EntityType
        final CsdlEntityType entityType = new CsdlEntityType();
        entityType.setName(EntityNames.ET_SPFLI_NAME);
        entityType.setProperties(Arrays.asList(carrierId,
                                               connectionId,
                                               countryFr,
                                               cityFrom,
                                               airpFrom,
                                               countryTo,
                                               cityTo,
                                               airpTo,
                                               flTime,
                                               depTime,
                                               arrTime,
                                               distance,
                                               distanceId,
                                               flyType,
                                               period));
        entityType.setKey(Arrays.asList(propRefCarrierId, propRefConnectionId));
        entityType.setNavigationProperties(navPropList);

        return entityType;
    }

    public static CsdlEntityType getCarrierEntityType() {
        // Key Properties
        final CsdlProperty carrierId = new CsdlProperty().setName(EntityNames.CARRIER_ID).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

        // Properties
        final CsdlProperty carrierName = new CsdlProperty().setName(EntityNames.CARRIER_NAME).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty currency = new CsdlProperty().setName(EntityNames.CURRENCY).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty url = new CsdlProperty().setName(EntityNames.URL).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

        // create PropertyRefs for Key elements
        final CsdlPropertyRef propRefCarrierId = new CsdlPropertyRef().setName(EntityNames.CARRIER_ID);

        // navigation properties
        final CsdlNavigationProperty navPropFlights = new CsdlNavigationProperty().setName(EntityNames.ES_SFLIGHT_NAME)
                                                                                  .setType(EntityNames.ET_SFLIGHT_FQN)
                                                                                  .setPartner(EntityNames.ET_SCARR_NAME)
                                                                                  .setCollection(true)
                                                                                  .setNullable(false);
        final CsdlNavigationProperty navPropConnections = new CsdlNavigationProperty().setName(EntityNames.ES_SPFLI_NAME)
                                                                                      .setType(EntityNames.ET_SPFLI_FQN)
                                                                                      .setPartner(EntityNames.ET_SCARR_NAME)
                                                                                      .setCollection(true)
                                                                                      .setNullable(false);
        final CsdlNavigationProperty navPropBookings = new CsdlNavigationProperty().setName(EntityNames.ES_SBOOK_NAME)
                                                                                   .setType(EntityNames.ET_SBOOK_FQN)
                                                                                   .setPartner(EntityNames.ET_SCARR_NAME)
                                                                                   .setCollection(true)
                                                                                   .setNullable(false);
        final List<CsdlNavigationProperty> navPropList = new ArrayList<>(Arrays.asList(navPropConnections, navPropFlights, navPropBookings));

        // configure EntityType
        final CsdlEntityType entityType = new CsdlEntityType();
        entityType.setName(EntityNames.ET_SCARR_NAME);
        entityType.setProperties(Arrays.asList(carrierId, carrierName, currency, url));
        entityType.setKey(Collections.singletonList(propRefCarrierId));
        entityType.setNavigationProperties(navPropList);

        return entityType;
    }

    public static CsdlEntityType getBookingEntityType() {
        // Key Properties
        final CsdlProperty bookingId = new CsdlProperty().setName(EntityNames.BOOKING_ID).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty carrierId = new CsdlProperty().setName(EntityNames.CARRIER_ID).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty connectionId = new CsdlProperty().setName(EntityNames.CONNECTION_ID).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty flightDate = new CsdlProperty().setName(EntityNames.FLIGHT_DATE).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

        // Properties
        final CsdlProperty customerId = new CsdlProperty().setName(EntityNames.CUSTOMER_ID).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty sex = new CsdlProperty().setName(EntityNames.SEX).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty isSmoker = new CsdlProperty().setName(EntityNames.IS_SMOKER).setType(EdmPrimitiveTypeKind.Boolean.getFullQualifiedName());
        final CsdlProperty luggageWeight = new CsdlProperty().setName(EntityNames.LUGGAGE_WEIGHT).setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName());
        final CsdlProperty weightUnit = new CsdlProperty().setName(EntityNames.WEIGHT_UNIT).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty hasInvoice = new CsdlProperty().setName(EntityNames.HAS_INVOICE).setType(EdmPrimitiveTypeKind.Boolean.getFullQualifiedName());
        final CsdlProperty flightClass = new CsdlProperty().setName(EntityNames.FLIGHT_CLASS).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty orderDate = new CsdlProperty().setName(EntityNames.ORDER_DATE).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty isCancelled = new CsdlProperty().setName(EntityNames.IS_CANCELLED).setType(EdmPrimitiveTypeKind.Boolean.getFullQualifiedName());
        final CsdlProperty isReserved = new CsdlProperty().setName(EntityNames.IS_RESERVED).setType(EdmPrimitiveTypeKind.Boolean.getFullQualifiedName());

        // create PropertyRefs for Key elements
        final CsdlPropertyRef propRefBookingId = new CsdlPropertyRef().setName(EntityNames.BOOKING_ID);
//        final CsdlPropertyRef propRefCarrierId = new CsdlPropertyRef().setName(EntityNames.CARRIER_ID);
//        final CsdlPropertyRef propRefConnectionId = new CsdlPropertyRef().setName(EntityNames.CONNECTION_ID);
//        final CsdlPropertyRef propRefFlightDate = new CsdlPropertyRef().setName(EntityNames.FLIGHT_DATE);

        // navigation properties
        final CsdlNavigationProperty navPropFlight = new CsdlNavigationProperty().setName(EntityNames.ET_SFLIGHT_NAME)
                                                                                 .setType(EntityNames.ET_SFLIGHT_FQN)
                                                                                 .setPartner(EntityNames.ES_SBOOK_NAME)
                                                                                 .setCollection(false)
                                                                                 .setNullable(false);
        final CsdlNavigationProperty navPropConnection = new CsdlNavigationProperty().setName(EntityNames.ET_SPFLI_NAME)
                                                                                     .setType(EntityNames.ET_SPFLI_FQN)
                                                                                     .setPartner(EntityNames.ES_SBOOK_NAME)
                                                                                     .setCollection(false)
                                                                                     .setNullable(false);
        final CsdlNavigationProperty navPropCarrier = new CsdlNavigationProperty().setName(EntityNames.ET_SCARR_NAME)
                                                                                  .setType(EntityNames.ET_SCARR_FQN)
                                                                                  .setPartner(EntityNames.ES_SBOOK_NAME)
                                                                                  .setCollection(false)
                                                                                  .setNullable(false);
        final List<CsdlNavigationProperty> navPropList = new ArrayList<>(Arrays.asList(navPropFlight, navPropConnection, navPropCarrier));

        // configure EntityType
        final CsdlEntityType entityType = new CsdlEntityType();
        entityType.setName(EntityNames.ET_SBOOK_NAME);
        entityType.setProperties(Arrays.asList(bookingId,
                                               carrierId,
                                               connectionId,
                                               flightDate,
                                               customerId,
                                               sex,
                                               isSmoker,
                                               luggageWeight,
                                               weightUnit,
                                               hasInvoice,
                                               flightClass,
                                               orderDate,
                                               isCancelled,
                                               isReserved));
        entityType.setKey(Collections.singletonList(propRefBookingId));
        entityType.setNavigationProperties(navPropList);

        return entityType;
    }

    public static CsdlEntityType getPlaneEntityType() {
        // Key Properties
        final CsdlProperty planeType = new CsdlProperty().setName(EntityNames.PLANE_TYPE).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

        // Properties
        final CsdlProperty seatsMaxE = new CsdlProperty().setName(EntityNames.SEATS_MAX).setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
        final CsdlProperty seatsMaxB = new CsdlProperty().setName(EntityNames.SEATS_MAX_B).setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
        final CsdlProperty seatsMaxF = new CsdlProperty().setName(EntityNames.SEATS_MAX_F).setType(EdmPrimitiveTypeKind.Int32.getFullQualifiedName());
        final CsdlProperty consumption = new CsdlProperty().setName(EntityNames.CONSUMPTION).setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName());
        final CsdlProperty conUnit = new CsdlProperty().setName(EntityNames.CONSUM_UNIT).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty tankCapacity = new CsdlProperty().setName(EntityNames.TANK_CAPACITY).setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName());
        final CsdlProperty tankCapUnit = new CsdlProperty().setName(EntityNames.TANK_CAP_UNIT).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty weight = new CsdlProperty().setName(EntityNames.WEIGHT).setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName());
        final CsdlProperty weightUnit = new CsdlProperty().setName(EntityNames.WEIGHT_UNIT).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty span = new CsdlProperty().setName(EntityNames.SPAN).setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName());
        final CsdlProperty spanUnit = new CsdlProperty().setName(EntityNames.SPAN_UNIT).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty length = new CsdlProperty().setName(EntityNames.LENGTH).setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName());
        final CsdlProperty lengthUnit = new CsdlProperty().setName(EntityNames.LENGTH_UNIT).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty speed = new CsdlProperty().setName(EntityNames.SPEED).setType(EdmPrimitiveTypeKind.Double.getFullQualifiedName());
        final CsdlProperty speedUnit = new CsdlProperty().setName(EntityNames.SPEED_UNIT).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
        final CsdlProperty producer = new CsdlProperty().setName(EntityNames.PRODUCER).setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

        // create PropertyRefs for Key elements
        final CsdlPropertyRef propRefPlaneType = new CsdlPropertyRef().setName(EntityNames.PLANE_TYPE);

        // navigation properties
        final CsdlNavigationProperty navPropFlights = new CsdlNavigationProperty().setName(EntityNames.ES_SFLIGHT_NAME)
                                                                                  .setType(EntityNames.ET_SFLIGHT_FQN)
                                                                                  .setPartner(EntityNames.ET_SAPLANE_NAME)
                                                                                  .setCollection(true)
                                                                                  .setNullable(false);
        final List<CsdlNavigationProperty> navPropList = new ArrayList<>(Collections.singletonList(navPropFlights));

        // configure EntityType
        final CsdlEntityType entityType = new CsdlEntityType();
        entityType.setName(EntityNames.ET_SAPLANE_NAME);
        entityType.setProperties(Arrays.asList(planeType,
                                               seatsMaxE,
                                               seatsMaxB,
                                               seatsMaxF,
                                               consumption,
                                               conUnit,
                                               tankCapacity,
                                               tankCapUnit,
                                               weight,
                                               weightUnit,
                                               span,
                                               spanUnit,
                                               length,
                                               lengthUnit,
                                               speed,
                                               speedUnit,
                                               producer));
        entityType.setKey(Collections.singletonList(propRefPlaneType));
        entityType.setNavigationProperties(navPropList);

        return entityType;
    }
}
