package odataservice.database.util;

import odataservice.database.collections.Saplane;
import odataservice.database.collections.Sbook;
import odataservice.database.collections.Scarr;
import odataservice.database.collections.Sflight;
import odataservice.database.collections.Spfli;
import odataservice.database.collections.enums.UnitOfCurrency;
import odataservice.database.collections.enums.UnitOfLength;
import odataservice.database.collections.enums.UnitOfMass;
import odataservice.database.collections.enums.UnitOfSpeed;
import odataservice.service.entities.definitions.EntityNames;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.ex.ODataRuntimeException;

import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 */
public final class DataTransformator {

    // ------------------------------------------------------------------------
    // constructors
    // ------------------------------------------------------------------------

    private DataTransformator() {}

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    // ========================================================================
    //                    TRANSFORM COLLECTION OBJECT TO ENTITY
    // ========================================================================

    public static Entity transformSflightToEntity(Sflight sflight) {
        final Entity flight = new Entity();
        //  Key Properties and References
        flight.addProperty(new Property(null, EntityNames.FLIGHT_DATE, ValueType.PRIMITIVE, sflight.getFlDate()));
        flight.addProperty(new Property(null, EntityNames.CARRIER_ID, ValueType.PRIMITIVE, sflight.getScarr().getCarrId()));
        flight.addProperty(new Property(null, EntityNames.CONNECTION_ID, ValueType.PRIMITIVE, sflight.getSpfli().getConnId()));
        //  Properties
        flight.addProperty(new Property(null, EntityNames.PLANE_TYPE, ValueType.PRIMITIVE, sflight.getSaplane().getPlaneType()));
        flight.addProperty(new Property(null, EntityNames.PRICE, ValueType.PRIMITIVE, sflight.getPrice()));
        flight.addProperty(new Property(null, EntityNames.CURRENCY, ValueType.PRIMITIVE, sflight.getCurrency()));
        flight.addProperty(new Property(null, EntityNames.SEATS_MAX_E, ValueType.PRIMITIVE, sflight.getSeatsMax()));
        flight.addProperty(new Property(null, EntityNames.SEATS_OCC_E, ValueType.PRIMITIVE, sflight.getSeatsOcc()));
        flight.addProperty(new Property(null, EntityNames.SEATS_MAX_B, ValueType.PRIMITIVE, sflight.getSeatsMaxB()));
        flight.addProperty(new Property(null, EntityNames.SEATS_OCC_B, ValueType.PRIMITIVE, sflight.getSeatsOccB()));
        flight.addProperty(new Property(null, EntityNames.SEATS_MAX_F, ValueType.PRIMITIVE, sflight.getSeatsMaxF()));
        flight.addProperty(new Property(null, EntityNames.SEATS_OCC_F, ValueType.PRIMITIVE, sflight.getSeatsOccF()));

        flight.setType(EntityNames.ET_SFLIGHT_FQN.getFullQualifiedNameAsString());
        flight.setId(createId(flight, null, EntityNames.FLIGHT_DATE, EntityNames.CARRIER_ID, EntityNames.CONNECTION_ID));

        return flight;
    }

    public static Entity transformSpfliToEntity(Spfli spfli) {
        final Entity connection = new Entity();
        //  Key Properties and References
        connection.addProperty(new Property(null, EntityNames.CONNECTION_ID, ValueType.PRIMITIVE, spfli.getConnId()));
        connection.addProperty(new Property(null, EntityNames.CARRIER_ID, ValueType.PRIMITIVE, spfli.getScarr().getCarrId()));
        //  Properties
        connection.addProperty(new Property(null, EntityNames.AIRPORT_FROM, ValueType.PRIMITIVE, spfli.getAirpFrom()));
        connection.addProperty(new Property(null, EntityNames.CITY_FROM, ValueType.PRIMITIVE, spfli.getCityFrom()));
        connection.addProperty(new Property(null, EntityNames.COUNTRY_FROM, ValueType.PRIMITIVE, spfli.getCountryFrom()));
        connection.addProperty(new Property(null, EntityNames.AIRPORT_TO, ValueType.PRIMITIVE, spfli.getAirpTo()));
        connection.addProperty(new Property(null, EntityNames.CITY_TO, ValueType.PRIMITIVE, spfli.getCityTo()));
        connection.addProperty(new Property(null, EntityNames.COUNTRY_TO, ValueType.PRIMITIVE, spfli.getCountryTo()));
        connection.addProperty(new Property(null, EntityNames.FLIGHT_TIME, ValueType.PRIMITIVE, spfli.getFlTime()));
        connection.addProperty(new Property(null, EntityNames.DEPARTURE_TIME, ValueType.PRIMITIVE, spfli.getDepTime()));
        connection.addProperty(new Property(null, EntityNames.ARRIVAL_TIME, ValueType.PRIMITIVE, spfli.getArrTime()));
        connection.addProperty(new Property(null, EntityNames.DISTANCE, ValueType.PRIMITIVE, spfli.getDistance()));
        connection.addProperty(new Property(null, EntityNames.DISTANCE_UNIT, ValueType.PRIMITIVE, spfli.getDistId()));
        connection.addProperty(new Property(null, EntityNames.FLIGHT_TYPE, ValueType.PRIMITIVE, spfli.getFlType()));
        connection.addProperty(new Property(null, EntityNames.PERIOD, ValueType.PRIMITIVE, spfli.getPeriod()));

        connection.setType(EntityNames.ET_SPFLI_FQN.getFullQualifiedNameAsString());
        connection.setId(createId(connection, null, EntityNames.CONNECTION_ID, EntityNames.CARRIER_ID));

        return connection;
    }

    public static Entity transformScarrToEntity(Scarr scarr) {
        final Entity carrier = new Entity();
        //  Key Properties and References
        carrier.addProperty(new Property(null, EntityNames.CARRIER_ID, ValueType.PRIMITIVE, scarr.getCarrId()));
        //  Properties
        carrier.addProperty(new Property(null, EntityNames.CARRIER_NAME, ValueType.PRIMITIVE, scarr.getCarrName()));
        carrier.addProperty(new Property(null, EntityNames.CURRENCY, ValueType.PRIMITIVE, scarr.getCurrCode()));
        carrier.addProperty(new Property(null, EntityNames.SCARR_URL, ValueType.PRIMITIVE, scarr.getUrl()));

        carrier.setType(EntityNames.ET_SCARR_FQN.getFullQualifiedNameAsString());
        carrier.setId(createId(carrier, null, EntityNames.CARRIER_ID));

        return carrier;
    }

    public static Entity transformSbookToEntity(Sbook sbook) {
        final Entity booking = new Entity();
        //  Key Properties and References
        booking.addProperty(new Property(null, EntityNames.BOOKING_ID, ValueType.PRIMITIVE, sbook.getBookId()));
        booking.addProperty(new Property(null, EntityNames.CARRIER_ID, ValueType.PRIMITIVE, sbook.getScarr().getCarrId()));
        booking.addProperty(new Property(null, EntityNames.CONNECTION_ID, ValueType.PRIMITIVE, sbook.getSpfli().getConnId()));
        booking.addProperty(new Property(null, EntityNames.FLIGHT_DATE, ValueType.PRIMITIVE, sbook.getSflight().getFlDate()));
        //  Properties
        booking.addProperty(new Property(null, EntityNames.CUSTOMER_ID, ValueType.PRIMITIVE, sbook.getCustomId()));
        booking.addProperty(new Property(null, EntityNames.SEX, ValueType.PRIMITIVE, sbook.getCustType()));
        booking.addProperty(new Property(null, EntityNames.IS_SMOKER, ValueType.PRIMITIVE, sbook.isSmoker()));
        booking.addProperty(new Property(null, EntityNames.LUGGAGE_WEIGHT, ValueType.PRIMITIVE, sbook.getLuggWeight()));
        booking.addProperty(new Property(null, EntityNames.WEIGHT_UNIT, ValueType.ENUM, sbook.getwUnit()));
        booking.addProperty(new Property(null, EntityNames.HAS_INVOICE, ValueType.PRIMITIVE, sbook.hasInvoice()));
        booking.addProperty(new Property(null, EntityNames.FLIGHT_CLASS, ValueType.PRIMITIVE, sbook.getFlightClass()));
        booking.addProperty(new Property(null, EntityNames.ORDER_DATE, ValueType.PRIMITIVE, sbook.getOrderDate()));
        booking.addProperty(new Property(null, EntityNames.IS_CANCELLED, ValueType.PRIMITIVE, sbook.isCancelled()));
        booking.addProperty(new Property(null, EntityNames.IS_RESERVED, ValueType.PRIMITIVE, sbook.isReserved()));

        booking.setType(EntityNames.ET_SBOOK_FQN.getFullQualifiedNameAsString());
        booking.setId(createId(booking, EntityNames.BOOKING_ID, EntityNames.CARRIER_ID, EntityNames.CONNECTION_ID, EntityNames.FLIGHT_DATE));

        return booking;
    }

    public static Entity transformSaplaneToEntity(Saplane saplane) {
        final Entity plane = new Entity();
        //  Key Properties and References
        plane.addProperty(new Property(null, EntityNames.PLANE_TYPE, ValueType.PRIMITIVE, saplane.getPlaneType()));
        //  Properties
        plane.addProperty(new Property(null, EntityNames.SEATS_MAX, ValueType.PRIMITIVE, saplane.getSeatsMax()));
        plane.addProperty(new Property(null, EntityNames.SEATS_MAX_B, ValueType.PRIMITIVE, saplane.getSeatsMaxB()));
        plane.addProperty(new Property(null, EntityNames.SEATS_MAX_F, ValueType.PRIMITIVE, saplane.getSeatsMaxF()));
        plane.addProperty(new Property(null, EntityNames.CONSUMPTION, ValueType.PRIMITIVE, saplane.getConsum()));
        plane.addProperty(new Property(null, EntityNames.CONSUM_UNIT, ValueType.PRIMITIVE, saplane.getConUnit()));
        plane.addProperty(new Property(null, EntityNames.TANK_CAPACITY, ValueType.PRIMITIVE, saplane.getTankCap()));
        plane.addProperty(new Property(null, EntityNames.TANK_CAP_UNIT, ValueType.PRIMITIVE, saplane.getCapUnit()));
        plane.addProperty(new Property(null, EntityNames.WEIGHT, ValueType.PRIMITIVE, saplane.getWeight()));
        plane.addProperty(new Property(null, EntityNames.WEIGHT_UNIT, ValueType.PRIMITIVE, saplane.getWeiUnit()));
        plane.addProperty(new Property(null, EntityNames.SPAN, ValueType.PRIMITIVE, saplane.getSpan()));
        plane.addProperty(new Property(null, EntityNames.SPAN_UNIT, ValueType.PRIMITIVE, saplane.getSpanUnit()));
        plane.addProperty(new Property(null, EntityNames.LENGTH, ValueType.PRIMITIVE, saplane.getLength()));
        plane.addProperty(new Property(null, EntityNames.LENGTH_UNIT, ValueType.PRIMITIVE, saplane.getLengUnit()));
        plane.addProperty(new Property(null, EntityNames.SPEED, ValueType.PRIMITIVE, saplane.getOpSpeed()));
        plane.addProperty(new Property(null, EntityNames.SPEED_UNIT, ValueType.PRIMITIVE, saplane.getSpeedUnit()));
        plane.addProperty(new Property(null, EntityNames.PRODUCER, ValueType.PRIMITIVE, saplane.getProducer()));

        plane.setType(EntityNames.ET_SAPLANE_FQN.getFullQualifiedNameAsString());
        plane.setId(createId(plane, EntityNames.PLANE_TYPE));

        return plane;
    }

    // ========================================================================
    //                     TRANSFORM ENTITY TO COLLECTION OBJECT
    // ========================================================================

    public static Scarr transformEntityToScarr(Entity carrier) {
        final String carrierId = getStringValue(carrier, EntityNames.CARRIER_ID);
        final String carrierName = getStringValue(carrier, EntityNames.CARRIER_NAME);
        final UnitOfCurrency currency = getUnitOfCurrencyValue(carrier, EntityNames.CURRENCY);
        final String url = getStringValue(carrier, EntityNames.SCARR_URL);

        return new Scarr(carrierId, carrierName, currency, url);
    }

    public static Saplane transformEntityToSaplane(Entity plane) {
        final String planeType = getStringValue(plane, EntityNames.PLANE_TYPE);
        final int seatsMax = getIntegerValue(plane, EntityNames.SEATS_MAX);
        final double consum = getDoubleValue(plane, EntityNames.CONSUMPTION);
        final UnitOfMass conUnit = getUnitOfMassValue(plane, EntityNames.CONSUM_UNIT);
        final double tankCap = getDoubleValue(plane, EntityNames.TANK_CAPACITY);
        final UnitOfMass capUnit = getUnitOfMassValue(plane, EntityNames.TANK_CAP_UNIT);
        final double weight = getDoubleValue(plane, EntityNames.WEIGHT);
        final UnitOfMass weiUnit = getUnitOfMassValue(plane, EntityNames.WEIGHT_UNIT);
        final double span = getDoubleValue(plane, EntityNames.SPAN);
        final UnitOfLength spanUnit = getUnitOfLengthValue(plane, EntityNames.SPAN_UNIT);
        final double length = getDoubleValue(plane, EntityNames.LENGTH);
        final UnitOfLength lengUnit = getUnitOfLengthValue(plane, EntityNames.LENGTH_UNIT);
        final double opSpeed = getDoubleValue(plane, EntityNames.SPEED);
        final UnitOfSpeed speedUnit = getUnitOfSpeedValue(plane, EntityNames.SPEED_UNIT);
        final String producer = getStringValue(plane, EntityNames.PRODUCER);
        final int seatsMaxB = getIntegerValue(plane, EntityNames.SEATS_MAX_B);
        final int seatsMaxF = getIntegerValue(plane, EntityNames.SEATS_MAX_F);

        return new Saplane(planeType,
                           seatsMax,
                           consum,
                           conUnit,
                           tankCap,
                           capUnit,
                           weight,
                           weiUnit,
                           span,
                           spanUnit,
                           length,
                           lengUnit,
                           opSpeed,
                           speedUnit,
                           producer,
                           seatsMaxB,
                           seatsMaxF);
    }

    public static Sflight transformEntityToSflight(Entity flight, Scarr scarr, Spfli spfli, Saplane saplane) {
        final String flDate = getStringValue(flight, EntityNames.FLIGHT_DATE);
        final double price = getDoubleValue(flight, EntityNames.PRICE);
        final UnitOfCurrency currency = getUnitOfCurrencyValue(flight, EntityNames.CURRENCY);
        final int seatsMax = getIntegerValue(flight, EntityNames.SEATS_MAX_E);
        final int seatsOcc = getIntegerValue(flight, EntityNames.SEATS_OCC_E);
        final int seatsMaxB = getIntegerValue(flight, EntityNames.SEATS_MAX_B);
        final int seatsOccB = getIntegerValue(flight, EntityNames.SEATS_OCC_B);
        final int seatsMaxF = getIntegerValue(flight, EntityNames.SEATS_MAX_F);
        final int seatsOccF = getIntegerValue(flight, EntityNames.SEATS_OCC_F);

        return new Sflight(flDate, scarr, spfli, saplane, price, currency, seatsMax, seatsOcc, seatsMaxB, seatsOccB, seatsMaxF, seatsOccF);
    }

    public static Sbook transformEntityToSbook(Entity booking, Scarr scarr, Spfli spfli, Sflight sflight) {
        final Sbook sbook = new Sbook();

        sbook.setBookId(getStringValue(booking, EntityNames.BOOKING_ID));
        sbook.setScarr(scarr);
        sbook.setSpfli(spfli);
        sbook.setSflight(sflight);
        sbook.setCustomId(getStringValue(booking, EntityNames.CUSTOMER_ID));
        sbook.setCustType(getStringValue(booking, EntityNames.SEX));
        sbook.setSmoker(getBooleanValue(booking, EntityNames.IS_SMOKER));
        sbook.setLuggWeight(getDoubleValue(booking, EntityNames.LUGGAGE_WEIGHT));
        sbook.setwUnit(getUnitOfMassValue(booking, EntityNames.WEIGHT_UNIT));
        sbook.setInvoice(getBooleanValue(booking, EntityNames.HAS_INVOICE));
        sbook.setFlightClass(getStringValue(booking, EntityNames.FLIGHT_CLASS));
        sbook.setOrderDate(getStringValue(booking, EntityNames.ORDER_DATE));
        sbook.setCancelled(getBooleanValue(booking, EntityNames.IS_CANCELLED));
        sbook.setReserved(getBooleanValue(booking, EntityNames.IS_RESERVED));

        return sbook;
    }

    public static Spfli transformEntityToSpfli(Entity connection, Scarr carrier) {
        final String connId = getStringValue(connection, EntityNames.CONNECTION_ID);
        final String airpFrom = getStringValue(connection, EntityNames.AIRPORT_FROM);
        final String airpTo = getStringValue(connection, EntityNames.AIRPORT_TO);
        final String cityFrom = getStringValue(connection, EntityNames.CITY_FROM);
        final String cityTo = getStringValue(connection, EntityNames.CITY_TO);
        final String countryFrom = getStringValue(connection, EntityNames.COUNTRY_FROM);
        final String countryTo = getStringValue(connection, EntityNames.COUNTRY_TO);
        final int flTime = getIntegerValue(connection, EntityNames.FLIGHT_TIME);
        final String depTime = getStringValue(connection, EntityNames.DEPARTURE_TIME);
        final String arrTime = getStringValue(connection, EntityNames.ARRIVAL_TIME);
        final double distance = getDoubleValue(connection, EntityNames.DISTANCE);
        final UnitOfLength distId = getUnitOfLengthValue(connection, EntityNames.DISTANCE_UNIT);
        final String flType = getStringValue(connection, EntityNames.FLIGHT_TYPE);
        final int period = getIntegerValue(connection, EntityNames.PERIOD);

        return new Spfli(connId,
                         carrier,
                         airpFrom,
                         airpTo,
                         cityFrom,
                         cityTo,
                         countryFrom,
                         countryTo,
                         flTime,
                         depTime,
                         arrTime,
                         distance,
                         distId,
                         flType,
                         period);
    }

    // ========================================================================
    //                           HELPER
    // ========================================================================

    private static URI createId(Entity entity, String navigationName, String... idPropertyNames) {
        try {
            final StringBuilder sb = new StringBuilder(getEntitySetName(entity)).append("(");

            for (String idPropertyName : idPropertyNames) {
                final Property property = entity.getProperty(idPropertyName);
                sb.append(property.asPrimitive()).append(",");
            }
            sb.deleteCharAt(sb.lastIndexOf(","));
            sb.append(")");

            if (navigationName != null) {
                sb.append("/").append(navigationName);
            }
            return new URI(sb.toString());
        } catch (URISyntaxException e) {
            throw new ODataRuntimeException("Unable to create (Atom) id for entity: " + entity, e);
        }
    }

    private static String getEntitySetName(Entity entity) {
        if (EntityNames.ET_SPFLI_FQN.getFullQualifiedNameAsString().equals(entity.getType())) {
            return EntityNames.ES_SPFLI_NAME;
        } else if (EntityNames.ET_SFLIGHT_FQN.getFullQualifiedNameAsString().equals(entity.getType())) {
            return EntityNames.ES_SFLIGHT_NAME;
        }
        return entity.getType();
    }

    private static String getStringValue(Entity entity, String propertyName) {
        final Property property = getProperty(entity, propertyName);
        return property == null ? null : String.valueOf(property.getValue());
    }

    private static Double getDoubleValue(Entity entity, String propertyName) {
        final Property property = getProperty(entity, propertyName);
        return property == null ? 0 : (Double) property.getValue();
    }

    private static Integer getIntegerValue(Entity entity, String propertyName) {
        final Property property = getProperty(entity, propertyName);
        return property == null ? 0 : (Integer) property.getValue();
    }

    private static Boolean getBooleanValue(Entity entity, String propertyName) {
        final Property property = getProperty(entity, propertyName);
        return property == null ? false : (Boolean) property.getValue();
    }

    private static UnitOfCurrency getUnitOfCurrencyValue(Entity entity, String propertyName) {
        final Property property = getProperty(entity, propertyName);
        return property == null ? null : UnitOfCurrency.valueOf(String.valueOf(property.getValue()));
    }

    private static UnitOfMass getUnitOfMassValue(Entity entity, String propertyName) {
        final Property property = getProperty(entity, propertyName);
        return property == null ? null : UnitOfMass.valueOf(String.valueOf(property.getValue()));
    }

    private static UnitOfSpeed getUnitOfSpeedValue(Entity entity, String propertyName) {
        final Property property = getProperty(entity, propertyName);
        return property == null ? null : UnitOfSpeed.valueOf(String.valueOf(property.getValue()));
    }

    private static UnitOfLength getUnitOfLengthValue(Entity entity, String propertyName) {
        final Property property = getProperty(entity, propertyName);
        return property == null ? null : UnitOfLength.valueOf(String.valueOf(property.getValue()));
    }

    private static Property getProperty(Entity entity, String propertyName) {
        final Property property = entity.getProperty(propertyName);

        if (property != null) {
            if (property.getValue() != null) {
                return property;
            }
        }
        return null;
    }
}
