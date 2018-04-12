package odataservice.service.request_handling.processors;

import odataservice.service.entities.definitions.EntityNames;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlAbstractEdmProvider;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainer;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainerInfo;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlSchema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static odataservice.service.entities.definitions.EntitySets.getBookingEntitySet;
import static odataservice.service.entities.definitions.EntitySets.getCarrierEntitySet;
import static odataservice.service.entities.definitions.EntitySets.getConnectionsEntitySet;
import static odataservice.service.entities.definitions.EntitySets.getFlightsEntitySet;
import static odataservice.service.entities.definitions.EntitySets.getPlaneEntitySet;
import static odataservice.service.entities.definitions.EntityTypes.getBookingEntityType;
import static odataservice.service.entities.definitions.EntityTypes.getCarrierEntityType;
import static odataservice.service.entities.definitions.EntityTypes.getConnectionEntityType;
import static odataservice.service.entities.definitions.EntityTypes.getFlightEntityType;
import static odataservice.service.entities.definitions.EntityTypes.getPlaneEntityType;

/**
 *
 */
public class FlightDataEdmProvider extends CsdlAbstractEdmProvider {

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    @Override
    public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) {
        // this method is called for each EntityType that is configured in the Schema
        if (entityTypeName.equals(EntityNames.ET_SFLIGHT_FQN)) {
            return getFlightEntityType();
        } else if (entityTypeName.equals(EntityNames.ET_SPFLI_FQN)) {
            return getConnectionEntityType();
        } else if (entityTypeName.equals(EntityNames.ET_SCARR_FQN)) {
            return getCarrierEntityType();
        } else if (entityTypeName.equals(EntityNames.ET_SBOOK_FQN)) {
            return getBookingEntityType();
        } else if (entityTypeName.equals(EntityNames.ET_SAPLANE_FQN)) {
            return getPlaneEntityType();
        } else {
            return null;
        }
    }

    @Override
    public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer, String entitySetName) {

        if (entityContainer.equals(EntityNames.CONTAINER)) {
            switch (entitySetName) {
                case EntityNames.ES_SFLIGHT_NAME:
                    return getFlightsEntitySet();
                case EntityNames.ES_SPFLI_NAME:
                    return getConnectionsEntitySet();
                case EntityNames.ES_SCARR_NAME:
                    return getCarrierEntitySet();
                case EntityNames.ES_SBOOK_NAME:
                    return getBookingEntitySet();
                case EntityNames.ES_SAPLANE_NAME:
                    return getPlaneEntitySet();
            }
        }
        return null;
    }

    /** This method is invoked when displaying the service document at e.g. http://localhost:8080/DemoService/DemoService.svc. */
    @Override
    public CsdlEntityContainerInfo getEntityContainerInfo(FullQualifiedName entityContainerName) {

        if (entityContainerName == null || entityContainerName.equals(EntityNames.CONTAINER)) {
            return new CsdlEntityContainerInfo().setContainerName(EntityNames.CONTAINER);
        }

        return null;
    }

    @Override
    public List<CsdlSchema> getSchemas() {
        // create Schema
        final CsdlSchema schema = new CsdlSchema().setNamespace(EntityNames.NAMESPACE);

        // add EntityTypes
        final List<CsdlEntityType> entityTypes = new ArrayList<>();
        entityTypes.add(getEntityType(EntityNames.ET_SFLIGHT_FQN));
        entityTypes.add(getEntityType(EntityNames.ET_SPFLI_FQN));
        entityTypes.add(getEntityType(EntityNames.ET_SAPLANE_FQN));
        entityTypes.add(getEntityType(EntityNames.ET_SBOOK_FQN));
        entityTypes.add(getEntityType(EntityNames.ET_SCARR_FQN));
        schema.setEntityTypes(entityTypes);

        // add EntityContainer
        schema.setEntityContainer(getEntityContainer());

        return new ArrayList<>(Collections.singletonList(schema));
    }

    @Override
    public CsdlEntityContainer getEntityContainer() {
        // create EntitySets
        final List<CsdlEntitySet> entitySets = new ArrayList<>();
        entitySets.add(getEntitySet(EntityNames.CONTAINER, EntityNames.ES_SFLIGHT_NAME));
        entitySets.add(getEntitySet(EntityNames.CONTAINER, EntityNames.ES_SPFLI_NAME));
        entitySets.add(getEntitySet(EntityNames.CONTAINER, EntityNames.ES_SAPLANE_NAME));
        entitySets.add(getEntitySet(EntityNames.CONTAINER, EntityNames.ES_SBOOK_NAME));
        entitySets.add(getEntitySet(EntityNames.CONTAINER, EntityNames.ES_SCARR_NAME));

        // create EntityContainer
        return new CsdlEntityContainer().setName(EntityNames.CONTAINER_NAME).setEntitySets(entitySets);
    }
}