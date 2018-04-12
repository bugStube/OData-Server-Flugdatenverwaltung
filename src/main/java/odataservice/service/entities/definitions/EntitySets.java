package odataservice.service.entities.definitions;

import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationPropertyBinding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class EntitySets {

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    public static CsdlEntitySet getFlightsEntitySet() {
        // navigation property binding
        final CsdlNavigationPropertyBinding navBindingToCarrier = buildNavigationPropertyBinding(EntityNames.ES_SCARR_NAME, EntityNames.ET_SCARR_NAME);
        final CsdlNavigationPropertyBinding navBindingToConnection = buildNavigationPropertyBinding(EntityNames.ES_SPFLI_NAME, EntityNames.ET_SPFLI_NAME);
        final CsdlNavigationPropertyBinding navBindingToPlane = buildNavigationPropertyBinding(EntityNames.ES_SAPLANE_NAME, EntityNames.ET_SAPLANE_NAME);
        final CsdlNavigationPropertyBinding navBindingToBookings = buildNavigationPropertyBinding(EntityNames.ES_SBOOK_NAME, EntityNames.ES_SBOOK_NAME);

        final List<CsdlNavigationPropertyBinding> navPropBindingList = new ArrayList<>(Arrays.asList(navBindingToCarrier,
                                                                                                     navBindingToConnection,
                                                                                                     navBindingToPlane,
                                                                                                     navBindingToBookings));
        return new CsdlEntitySet().setName(EntityNames.ES_SFLIGHT_NAME).setType(EntityNames.ET_SFLIGHT_FQN).setNavigationPropertyBindings(navPropBindingList);
    }

    public static CsdlEntitySet getConnectionsEntitySet() {
        // navigation property binding
        final CsdlNavigationPropertyBinding navBindingToFlights = buildNavigationPropertyBinding(EntityNames.ES_SFLIGHT_NAME, EntityNames.ES_SFLIGHT_NAME);
        final CsdlNavigationPropertyBinding navBindingToBookings = buildNavigationPropertyBinding(EntityNames.ES_SBOOK_NAME, EntityNames.ES_SBOOK_NAME);
        final CsdlNavigationPropertyBinding navBindingToCarrier = buildNavigationPropertyBinding(EntityNames.ES_SCARR_NAME, EntityNames.ET_SCARR_NAME);

        final List<CsdlNavigationPropertyBinding> navPropBindingList = new ArrayList<>(Arrays.asList(navBindingToFlights,
                                                                                                     navBindingToCarrier,
                                                                                                     navBindingToBookings));
        return new CsdlEntitySet().setName(EntityNames.ES_SPFLI_NAME).setType(EntityNames.ET_SPFLI_FQN).setNavigationPropertyBindings(navPropBindingList);
    }

    public static CsdlEntitySet getCarrierEntitySet() {
        // navigation property binding
        final CsdlNavigationPropertyBinding navPropBindingToFlights = buildNavigationPropertyBinding(EntityNames.ES_SFLIGHT_NAME, EntityNames.ES_SFLIGHT_NAME);
        final CsdlNavigationPropertyBinding navPropBindingToConnections = buildNavigationPropertyBinding(EntityNames.ES_SPFLI_NAME, EntityNames.ES_SPFLI_NAME);
        final CsdlNavigationPropertyBinding navPropBindingToBookings = buildNavigationPropertyBinding(EntityNames.ES_SBOOK_NAME, EntityNames.ES_SBOOK_NAME);

        final List<CsdlNavigationPropertyBinding> navPropBindingList = new ArrayList<>(Arrays.asList(navPropBindingToFlights,
                                                                                                     navPropBindingToConnections,
                                                                                                     navPropBindingToBookings));
        return new CsdlEntitySet().setName(EntityNames.ES_SCARR_NAME).setType(EntityNames.ET_SCARR_FQN).setNavigationPropertyBindings(navPropBindingList);
    }

    public static CsdlEntitySet getBookingEntitySet() {
        // navigation property binding
        final CsdlNavigationPropertyBinding navBindingToFlight = buildNavigationPropertyBinding(EntityNames.ES_SFLIGHT_NAME, EntityNames.ET_SFLIGHT_NAME);
        final CsdlNavigationPropertyBinding navBindingToConnection = buildNavigationPropertyBinding(EntityNames.ES_SPFLI_NAME, EntityNames.ET_SPFLI_NAME);
        final CsdlNavigationPropertyBinding navBindingToCarrier = buildNavigationPropertyBinding(EntityNames.ES_SCARR_NAME, EntityNames.ET_SCARR_NAME);

        final List<CsdlNavigationPropertyBinding> navPropBindingList = new ArrayList<>(Arrays.asList(navBindingToFlight,
                                                                                                     navBindingToConnection,
                                                                                                     navBindingToCarrier));
        return new CsdlEntitySet().setName(EntityNames.ES_SBOOK_NAME).setType(EntityNames.ET_SBOOK_FQN).setNavigationPropertyBindings(navPropBindingList);
    }

    public static CsdlEntitySet getPlaneEntitySet() {
        // navigation property binding
        final CsdlNavigationPropertyBinding navBindingToFlights = buildNavigationPropertyBinding(EntityNames.ES_SFLIGHT_NAME, EntityNames.ES_SFLIGHT_NAME);
        final List<CsdlNavigationPropertyBinding> navPropBindingList = new ArrayList<>(Collections.singletonList(navBindingToFlights));

        return new CsdlEntitySet().setName(EntityNames.ES_SAPLANE_NAME).setType(EntityNames.ET_SAPLANE_FQN).setNavigationPropertyBindings(navPropBindingList);
    }

    /**
     * Helper to build navigation property binding.
     *
     * @param target The target entity set, where the navigation property points to.
     * @param path   The path from entity type to navigation property.
     */
    private static CsdlNavigationPropertyBinding buildNavigationPropertyBinding(String target, String path) {
        return new CsdlNavigationPropertyBinding().setTarget(target).setPath(path);
    }
}
