package odataservice.database.util;

import com.mongodb.MongoClient;
import com.mongodb.util.JSON;
import odataservice.database.collections.Saplane;
import odataservice.database.collections.Sbook;
import odataservice.database.collections.Scarr;
import odataservice.database.collections.Sflight;
import odataservice.database.collections.Spfli;
import odataservice.database.collections.enums.UnitOfCurrency;
import odataservice.database.collections.enums.UnitOfLength;
import odataservice.database.collections.enums.UnitOfMass;
import odataservice.database.collections.enums.UnitOfSpeed;
import odataservice.database.connection.MorphiaService;
import odataservice.service.entities.definitions.EntityNames;
import org.apache.commons.collections.CollectionUtils;
import org.bson.BSONObject;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 *
 */
public class DummyDataCreator {

    // ------------------------------------------------------------------------
    // constants
    // ------------------------------------------------------------------------

    private static final Logger LOGGER = LoggerFactory.getLogger(DummyDataCreator.class);

    // ------------------------------------------------------------------------
    // constructors
    // ------------------------------------------------------------------------

    private DummyDataCreator() {}

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    public static void createTestData() {
        LOGGER.info("Creating dummy data if database is empty.");

        final MorphiaService morphiaService = new MorphiaService();
        final MongoClient mongoClient = morphiaService.getMongoClient();
        final Morphia morphia = morphiaService.getMorphia();
        final Datastore datastore = morphia.createDatastore(mongoClient, EntityNames.DB_NAME);
        datastore.ensureIndexes();

        ////////////////////////SCARR////////////////////////
        BSONObject jsonArray = readJson("/dummyData/dummyDataScarr.json", DummyDataCreator.class);
        final List<Scarr> carriers = transformScarr(jsonArray);
        datastore.save(carriers);
        ////////////////////////SAPLANE////////////////////////
        jsonArray = readJson("/dummyData/dummyDataSaplane.json", DummyDataCreator.class);
        final List<Saplane> planes = transformSaplane(jsonArray);
        datastore.save(planes);
        ////////////////////////SPFLI////////////////////////
        jsonArray = readJson("/dummyData/dummyDataSpfli.json", DummyDataCreator.class);
        final List<Spfli> connections = transformSpfli(jsonArray, carriers);
        datastore.save(connections);
        ////////////////////////SFLIGHTS////////////////////////
        jsonArray = readJson("/dummyData/dummyDataSflight.json", DummyDataCreator.class);
        final List<Sflight> flights = transformSflight(jsonArray, carriers, connections, planes);
        datastore.save(flights);
        ////////////////////////SBOOKS////////////////////////
        jsonArray = readJson("/dummyData/dummyDataSbook.json", DummyDataCreator.class);
        final List<Sbook> bookings = transformSbooking(jsonArray, carriers, connections, flights);
        datastore.save(bookings);

        LOGGER.info("Dummy data created.");
    }

    private static BSONObject readJson(String path, Class clazz) {
        final InputStream inputStream = clazz.getResourceAsStream(path);
        final String json = readFromInputStream(inputStream);
        return (BSONObject) JSON.parse(json);
    }

    public static String readFromInputStream(InputStream inputStream) {
        final StringBuilder resultStringBuilder = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = br.readLine()) != null) {
                resultStringBuilder.append(line).append("\n");
            }
        } catch (Exception e) {
            LOGGER.warn("Error while reading from file:", e);
        }
        return resultStringBuilder.toString();
    }

    private static List<Scarr> transformScarr(BSONObject jsonArray) {
        final List<Scarr> carriers = new ArrayList<>(jsonArray.keySet().size());

        for (String key : jsonArray.keySet()) {
            final BSONObject jsonObject = (BSONObject) jsonArray.get(key);

            final Scarr carrier = new Scarr();
            carrier.setCarrId(convertToString(jsonObject, EntityNames.SCARR_CARRID));
            carrier.setCarrName(convertToString(jsonObject, EntityNames.SCARR_CARRNAME));
            carrier.setCurrCode(UnitOfCurrency.valueOf(convertToString(jsonObject, EntityNames.SCARR_CURRCODE)));
            carrier.setUrl(convertToString(jsonObject, EntityNames.SCARR_URL));

            carriers.add(carrier);
        }

        return carriers;
    }

    private static List<Saplane> transformSaplane(BSONObject jsonArray) {
        final List<Saplane> planes = new ArrayList<>(jsonArray.keySet().size());

        for (String key : jsonArray.keySet()) {
            final BSONObject json = (BSONObject) jsonArray.get(key);

            final Saplane plane = new Saplane();
            plane.setPlaneType(convertToString(json, EntityNames.SAPLANE_PLANETYPE));
            plane.setSeatsMax(convertToInteger(json, EntityNames.SAPLANE_SEATSMAX));
            plane.setConsum(convertStringToDouble(json, EntityNames.SAPLANE_CONSUM));
            plane.setConUnit(UnitOfMass.valueOf(convertToString(json, EntityNames.SAPLANE_CON_UNIT)));
            plane.setTankCap(convertStringToDouble(json, EntityNames.SAPLANE_TANKCAP));
            plane.setCapUnit(UnitOfMass.valueOf(convertToString(json, EntityNames.SAPLANE_CAP_UNIT)));
            plane.setWeight(convertStringToDouble(json, EntityNames.SAPLANE_WEIGHT));
            plane.setWeiUnit(UnitOfMass.valueOf(convertToString(json, EntityNames.SAPLANE_WEI_UNIT)));
            plane.setSpan(convertStringToDouble(json, EntityNames.SAPLANE_SPAN));
            plane.setSpanUnit(UnitOfLength.valueOf(convertToString(json, EntityNames.SAPLANE_SPAN_UNIT)));
            plane.setLength(convertStringToDouble(json, EntityNames.SAPLANE_LENG));
            plane.setLengUnit(UnitOfLength.valueOf(convertToString(json, EntityNames.SAPLANE_LENG_UNIT)));
            plane.setOpSpeed(convertStringToDouble(json, EntityNames.SAPLANE_OP_SPEED));
            plane.setSpeedUnit(UnitOfSpeed.valueOf(convertToString(json, EntityNames.SAPLANE_SPEED_UNIT)));
            plane.setProducer(convertToString(json, EntityNames.SAPLANE_PRODUCER));
            plane.setSeatsMaxB(convertToInteger(json, EntityNames.SAPLANE_SEATSMAX_B));
            plane.setSeatsMaxF(convertToInteger(json, EntityNames.SAPLANE_SEATSMAX_F));

            planes.add(plane);
        }

        return planes;
    }

    private static List<Spfli> transformSpfli(BSONObject jsonArray, List<Scarr> carriers) {
        final List<Spfli> connections = new ArrayList<>(jsonArray.keySet().size());

        for (String key : jsonArray.keySet()) {
            final BSONObject json = (BSONObject) jsonArray.get(key);
            final String carrId = convertToString(json, EntityNames.SPFLI_CARRID);
            final Scarr carrierFK = findForeignKeyScarr(carrId, carriers);

            if (carrierFK != null) {
                final Spfli connection = new Spfli();
                connection.setConnId(String.valueOf(convertToInteger(json, EntityNames.SPFLI_CONNID)));
                connection.setScarr(carrierFK);
                connection.setAirpFrom(convertToString(json, EntityNames.SPFLI_AIRPFROM));
                connection.setAirpTo(convertToString(json, EntityNames.SPFLI_AIRPTO));
                connection.setCityFrom(convertToString(json, EntityNames.SPFLI_CITYFROM));
                connection.setCityTo(convertToString(json, EntityNames.SPFLI_CITYTO));
                connection.setCountryFrom(convertToString(json, EntityNames.SPFLI_COUNTRYTO));
                connection.setCountryTo(convertToString(json, EntityNames.SPFLI_COUNTRYFR));
                connection.setFlTime(convertToInteger(json, EntityNames.SPFLI_FLTIME));
                connection.setDepTime(convertAndValidateStringAsTIMS(json, EntityNames.SPFLI_DEPTIME));
                connection.setArrTime(convertAndValidateStringAsTIMS(json, EntityNames.SPFLI_ARRTIME));
                connection.setDistance(convertToDouble(json, EntityNames.SPFLI_DISTANCE));
                connection.setDistId(UnitOfLength.valueOf(convertToString(json, EntityNames.SPFLI_DISTID)));
                connection.setFlType(convertToString(json, EntityNames.SPFLI_FLTYPE));
                connection.setPeriod(convertToInteger(json, EntityNames.SPFLI_PERIOD));

                connections.add(connection);
            }
        }

        return connections;
    }

    private static Scarr findForeignKeyScarr(String carrId, List<Scarr> carriers) {
        if (CollectionUtils.isNotEmpty(carriers)) {
            for (Scarr carrier : carriers) {
                if (carrId.equals(carrier.getCarrId())) {
                    return carrier;
                }
            }
        }
        return null;
    }

    private static Spfli findForeignKeySpfli(String connId, List<Spfli> connections) {
        if (CollectionUtils.isNotEmpty(connections)) {
            for (Spfli connection : connections) {
                if (connId.equals(connection.getConnId())) {
                    return connection;
                }
            }
        }
        return null;
    }

    private static Saplane findForeignKeySaplane(String planeType, List<Saplane> planes) {
        if (CollectionUtils.isNotEmpty(planes)) {
            for (Saplane plane : planes) {
                if (planeType.equals(plane.getPlaneType())) {
                    return plane;
                }
            }
        }
        return null;
    }

    private static List<Sflight> transformSflight(BSONObject jsonArray, List<Scarr> carriers, List<Spfli> connections, List<Saplane> planes) {
        final List<Sflight> flights = new ArrayList<>(jsonArray.keySet().size());

        for (String key : jsonArray.keySet()) {
            final BSONObject json = (BSONObject) jsonArray.get(key);

            final String carrId = convertToString(json, EntityNames.SFLIGHT_CARRID);
            final Scarr carrierFK = findForeignKeyScarr(carrId, carriers);

            final String connId = String.valueOf(convertToInteger(json, EntityNames.SFLIGHT_CONNID));
            final Spfli connectionFK = findForeignKeySpfli(connId, connections);

            final String planeType = convertToString(json, EntityNames.SFLIGHT_PLANETYPE);
            final Saplane planeFK = findForeignKeySaplane(planeType, planes);

            if (carrierFK != null && connectionFK != null && planeFK != null) {
                final int maxSeats = planeFK.getSeatsMax();
                final int maxSeatsB = planeFK.getSeatsMaxB();
                final int maxSeatsF = planeFK.getSeatsMaxF();

                final Sflight flight = new Sflight();
                flight.setFlDate(convertToString(json, EntityNames.SFLIGHT_FLDATE));//SAP's DATS format
                flight.setScarr(carrierFK);
                flight.setSpfli(connectionFK);
                flight.setSaplane(planeFK);

                flight.setCurrency(carrierFK.getCurrCode());
                flight.setPrice(calculateFlightPrice(connectionFK.getDistance(), connectionFK.getDistId(), flight.getCurrency()));

                flight.setSeatsMax(maxSeats);
                flight.setSeatsMaxB(maxSeatsB);
                flight.setSeatsMaxF(maxSeatsF);
                flight.setSeatsOcc(calculateOccupiedSeats(maxSeats));
                flight.setSeatsOccB(calculateOccupiedSeats(maxSeatsB));
                flight.setSeatsOccF(calculateOccupiedSeats(maxSeatsF));

                flights.add(flight);
            }
        }

        return flights;
    }

    private static List<Sbook> transformSbooking(BSONObject jsonArray, List<Scarr> carriers, List<Spfli> connections, List<Sflight> flights) {
        final List<Sbook> bookings = new ArrayList<>(jsonArray.keySet().size());
        int counter = 0;

        for (String key : jsonArray.keySet()) {
            final BSONObject json = (BSONObject) jsonArray.get(key);

            final String carrId = convertToString(json, EntityNames.SBOOK_CARRID);
            final Scarr carrierFK = findForeignKeyScarr(carrId, carriers);

            final String connId = String.valueOf(convertToInteger(json, EntityNames.SBOOK_CONNID));
            final Spfli connectionFK = findForeignKeySpfli(connId, connections);

            //randomly connect a flight to a booking
            final Sflight flightFK = flights.get(counter++);
            if (counter == flights.size()) {
                counter = 0;
            }

            if (carrierFK != null && connectionFK != null) {
                final Sbook booking = new Sbook();
                //  maybe add value price use standard price of booking and multiply by class
                booking.setBookId(convertIntegerToString(json, EntityNames.SBOOK_BOOKID));
                booking.setScarr(carrierFK);
                booking.setSpfli(connectionFK);
                booking.setSflight(flightFK);
                booking.setCustomId(convertIntegerToString(json, EntityNames.SBOOK_CUSTOMID));
                booking.setCustType(convertToCharacter(json, EntityNames.SBOOK_CUSTTYPE));
                booking.setSmoker(convertToCharacter(json, EntityNames.SBOOK_SMOKER));
                booking.setLuggWeight(convertToInteger(json, EntityNames.SBOOK_LUGGWEIGHT));
                booking.setwUnit(UnitOfMass.valueOf(convertToString(json, EntityNames.SBOOK_WUNIT)));
                booking.setInvoice(convertToCharacter(json, EntityNames.SBOOK_INVOICE));
                booking.setFlightClass(convertToCharacter(json, EntityNames.SBOOK_CLASS));
                booking.setOrderDate(convertToString(json, EntityNames.SBOOK_ORDER_DATE));//SAP's DATS format
                booking.setCancelled(convertToCharacter(json, EntityNames.SBOOK_CANCELLED));
                booking.setReserved(convertToCharacter(json, EntityNames.SBOOK_RESERVED));

                bookings.add(booking);
            }
        }

        return bookings;
    }

    // ========================================================================
    //                           HELPER
    // ========================================================================

    private static String convertToString(BSONObject bsonObject, String value) {
        return (String) bsonObject.get(value);
    }

    private static Integer convertToInteger(BSONObject bsonObject, String value) {
        return (Integer) bsonObject.get(value);
    }

    private static Double convertToDouble(BSONObject bsonObject, String value) {
        return (Double) bsonObject.get(value);
    }

    private static Character convertToCharacter(BSONObject bsonObject, String value) {
        return ((String) bsonObject.get(value)).charAt(0);
    }

    private static String convertIntegerToString(BSONObject bsonObject, String value) {
        return String.valueOf(convertToInteger(bsonObject, value));
    }

    private static Double convertStringToDouble(BSONObject bsonObject, String value) {
        return Double.parseDouble((convertToString(bsonObject, value)).replace(".", ""));
    }

    //  Abflugszeit von der verbindung hier mit rein
    //    private static Date conv(BSONObject bsonObject, String value, LocalTime departureTime) {
    //        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss.SSSSSS");
    //        LocalDateTime localDateTime = LocalDateTime.parse(date, formatter);
    //
    //    }

    //    private static Date convertStringAsDateToDate(BSONObject bsonObject, String value) {//2 methhoden
    //        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    //        final LocalDate date = LocalDate.parse(convertToString(bsonObject, value), formatter);
    //        final LocalDateConverter ldc = new LocalDateConverter();
    //
    //        return (Date) ldc.encode(date);
    //    }
    //
    //    private static Date convertToDate(BSONObject bsonObject, String value) {
    //        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    //        final LocalDate date = LocalDate.parse(convertToString(bsonObject, value), formatter);
    //        final LocalTime time = LocalTime.of(createRandomNumber(0, 24), createRandomNumber(0, 60), 0);
    //        final LocalDateTime ldt = LocalDateTime.parse(convertToString(bsonObject, value), formatter);
    //        final LocalDateTime ldt2 = LocalDateTime.of(date, time);
    //        final LocalDateTimeConverter ldtc = new LocalDateTimeConverter();
    //        final LocalDateConverter ldc = new LocalDateConverter();
    //
    //        return (Date) ldtc.encode(ldt);
    //    }

    /**
     * Validates a String that is supposed to be in SAP's {@code TIMS}-format.
     * <p>
     * Note: If the value for seconds is invalid, the actual time is still considered valid.
     * Therefore, seconds may be a 0 in that case.
     *
     * @return Returns String as valid {@code TIMS} time format or an empty String if the parsing failed.
     */
    private static String convertAndValidateStringAsTIMS(BSONObject bsonObject, String value) {
        String result = "";
        final String colon = ":";
        final String tims = convertToString(bsonObject, value);
        final String[] timeValues = tims.split(colon);

        final int invalidValue = -1;
        int hour = invalidValue;
        int min = invalidValue;
        int sec = invalidValue;

        if (timeValues.length > 0 && timeValues.length == 3) {
            hour = Integer.parseInt(timeValues[0]);
            if (hour >= 0 && hour < 24) {
                min = Integer.parseInt(timeValues[1]);
                if (min >= 0 && min < 60) {
                    String secondsValue = timeValues[2];
                    if (secondsValue.length() == 2) {
                        sec = Integer.parseInt(secondsValue);
                    } else if ((secondsValue.length() > 2)) {
                        sec = Integer.parseInt(secondsValue.substring(0, 1));
                    }
                    //fallback if seconds is invalid
                    if (sec < 0 || sec > 60) {
                        sec = 0;
                    }
                }
            }
        }

        if (hour != invalidValue && min != invalidValue) {
            final String zero = "0";
            String hourValue = String.valueOf(hour);
            String minValue = String.valueOf(min);
            String secValue = String.valueOf(sec);
            if (hour < 10) {
                hourValue = zero + hourValue;
            }
            if (min < 10) {
                minValue = zero + minValue;
            }
            if (sec < 10) {
                secValue = zero + secValue;
            }
            result = hourValue + colon + minValue + colon + secValue;
        }

        return result;
    }

    private static int createRandomNumber(int numberFrom, int NumberTo) {
        return ThreadLocalRandom.current().nextInt(numberFrom, NumberTo);
    }

    private static int calculateOccupiedSeats(int maxSeatsInPlane) {
        if (maxSeatsInPlane <= 1) {
            maxSeatsInPlane = 2;
        }
        return createRandomNumber(0, maxSeatsInPlane);
    }

    /** The flight price is derived from the flight distance. The flight class is not known here, but could also be added to the calculation. */
    private static double calculateFlightPrice(double distance, UnitOfLength distanceUnit, UnitOfCurrency currency) {
        double price;

        //calculate base price depending on flight distance
        if (distance < 1000) {
            price = createRandomNumber(25, 500);
        } else if (distance < 3000) {
            price = createRandomNumber(50, 1500);
        } else if (distance < 7500) {
            price = createRandomNumber(500, 2500);
        } else {
            price = createRandomNumber(750, 3500);
        }

        //is unit of distance in imperial miles, then adjust price
        if (distanceUnit.equals(UnitOfLength.MI)) {
            price = price * 1.62;
        }

        final DecimalFormat format = new DecimalFormat("#.00");

        return Double.parseDouble(format.format(price).replace(",", "."));
    }
}