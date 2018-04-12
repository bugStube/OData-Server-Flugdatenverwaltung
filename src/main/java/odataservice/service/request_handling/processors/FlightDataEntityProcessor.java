package odataservice.service.request_handling.processors;

import odataservice.service.request_handling.handler.CRUDHandler;
import odataservice.service.request_handling.handler.NavigationHandler;
import odataservice.service.util.Util;
import org.apache.olingo.commons.api.Constants;
import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.ContextURL.Suffix;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Link;
import org.apache.olingo.commons.api.edm.EdmElement;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmNavigationProperty;
import org.apache.olingo.commons.api.edm.EdmNavigationPropertyBinding;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpMethod;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ODataResponse;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.deserializer.DeserializerException;
import org.apache.olingo.server.api.deserializer.DeserializerResult;
import org.apache.olingo.server.api.deserializer.ODataDeserializer;
import org.apache.olingo.server.api.serializer.EntitySerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.apache.olingo.server.api.uri.UriResourceNavigation;
import org.apache.olingo.server.api.uri.queryoption.ExpandItem;
import org.apache.olingo.server.api.uri.queryoption.ExpandOption;
import org.apache.olingo.server.api.uri.queryoption.SelectOption;

import java.io.InputStream;
import java.util.List;
import java.util.Locale;

/**
 *
 */
public class FlightDataEntityProcessor implements org.apache.olingo.server.api.processor.EntityProcessor {

    // ------------------------------------------------------------------------
    // members
    // ------------------------------------------------------------------------

    private OData mOData;
    private ServiceMetadata mServiceMetadata;
    private CRUDHandler mCRUDHandler;
    private NavigationHandler mNavigationHandler;

    // ------------------------------------------------------------------------
    // constructors
    // ------------------------------------------------------------------------

    public FlightDataEntityProcessor(CRUDHandler CRUDHandler, NavigationHandler navigationHandler) {
        mCRUDHandler = CRUDHandler;
        mNavigationHandler = navigationHandler;
    }

    // ------------------------------------------------------------------------
    // methods
    // ------------------------------------------------------------------------

    /** This method is invoked by the Olingo library, allowing the storing of the context objects. */
    public void init(OData odata, ServiceMetadata serviceMetadata) {
        mOData = odata;
        mServiceMetadata = serviceMetadata;
    }

    /**
     * This method is invoked when a single entity has to be read.
     * This can be either a "normal" read operation, or a navigation:
     * <p>
     * Example for "normal" read operation: {@code http://localhost:8080/DemoService/DemoService.svc/Products(1)}
     * <p>
     * Example for navigation: {@code http://localhost:8080/DemoService/DemoService.svc/Products(1)/Category}
     */
    public void readEntity(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType responseFormat)
        throws ODataApplicationException, SerializerException {
        // 1st step: retrieve the requested Entity: can be "normal" read operation, or navigation (to-one)
        final List<UriResource> resourceParts = uriInfo.getUriResourceParts();
        final int segmentCount = resourceParts.size();
        // Note: here the first segment is always the EntitySet
        final UriResource uriResource = resourceParts.get(0);

        if (!(uriResource instanceof UriResourceEntitySet)) {
            throw new ODataApplicationException("Only EntitySet is supported", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ENGLISH);
        }
        // Analyze the URI segments
        if (segmentCount == 1) {
            // no navigation
            this.normalRequest(response, responseFormat, uriResource, uriInfo);
        } else if (segmentCount == 2) {
            // navigation
            this.handleNavigationRequest(resourceParts, uriResource, responseFormat, response, uriInfo);
        } else {
            // this would be the case for e.g. Products(1)/Category/Products(1)/Category
            throw new ODataApplicationException("Not supported", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ROOT);
        }
    }

    /** This is the case for: {@code DemoService/DemoService.svc/Products(1)} */
    private void normalRequest(ODataResponse response, ContentType responseFormat, UriResource uriResource, UriInfo uriInfo)
        throws ODataApplicationException, SerializerException {

        final UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) uriResource;
        final EdmEntitySet responseEdmEntitySet = uriResourceEntitySet.getEntitySet();
        final EdmEntityType responseEdmEntityType = responseEdmEntitySet.getEntityType();
        //  retrieve the data from backend
        final List<UriParameter> keyPredicates = uriResourceEntitySet.getKeyPredicates();
        final Entity responseEntity = mCRUDHandler.readEntityData(responseEdmEntitySet, keyPredicates);

        this.handleResponse(responseEntity, responseEdmEntitySet, responseFormat, responseEdmEntityType, response, uriInfo);
    }

    /**
     * In case of navigation: {@code DemoService.svc/Products(1)/Category}<br>
     * More complex URIs are not supported.
     */
    private void handleNavigationRequest(List<UriResource> resourceParts, UriResource uriResource, ContentType responseFormat, ODataResponse response,
                                         UriInfo uriInfo) throws ODataApplicationException, SerializerException {
        // more complex URIs are not supported here
        final UriResource navSegment = resourceParts.get(1);

        if (navSegment instanceof UriResourceNavigation) {
            final UriResourceNavigation uriResourceNavigation = (UriResourceNavigation) navSegment;
            final EdmNavigationProperty edmNavigationProperty = uriResourceNavigation.getProperty();
            final UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) uriResource;
            final EdmEntitySet startEdmEntitySet = uriResourceEntitySet.getEntitySet();
            final EdmEntityType responseEdmEntityType = edmNavigationProperty.getType();
            // contextURL displays the last segment
            final EdmEntitySet responseEdmEntitySet = Util.getNavigationTargetEntitySet(startEdmEntitySet, edmNavigationProperty);
            // fetch the data from backend.
            // e.g. for the URI: Products(1)/Category the correct Category entity has to be found
            final List<UriParameter> keyPredicates = uriResourceEntitySet.getKeyPredicates();
            // e.g. for Products(1)/Category the Products(1) has to be found first
            final Entity sourceEntity = mCRUDHandler.readEntityData(startEdmEntitySet, keyPredicates);
            // now it will be checked if the navigation is
            // a) to-one: e.g. Products(1)/Category
            // b) to-many with key: e.g. Categories(3)/Products(5)
            // the key for nav is used in this case: Categories(3)/Products(5)
            final List<UriParameter> navKeyPredicates = uriResourceNavigation.getKeyPredicates();
            final Entity responseEntity;

            if (navKeyPredicates.isEmpty()) {
                // e.g. DemoService.svc/Products(1)/Category
                responseEntity = mNavigationHandler.getRelatedEntity(sourceEntity, responseEdmEntityType);
            } else {
                // e.g. DemoService.svc/Categories(3)/Products(5)
                responseEntity = mNavigationHandler.getRelatedEntity(sourceEntity, responseEdmEntityType, navKeyPredicates);
            }

            this.handleResponse(responseEntity, responseEdmEntitySet, responseFormat, responseEdmEntityType, response, uriInfo);
        }
    }

    /**
     * Handles System query options by modifying the result set according to the query options, specified by the end user.<p>
     * <b>Following system queries are currently supported:</b>
     * <ul>
     * <li>$count</li>
     * <li>$skip</li>
     * <li>$top</li>
     * </ul>
     */
    private Entity handleSystemQueryOptions(UriInfo uriInfo, Entity entity, EdmEntitySet edmEntitySet) throws ODataApplicationException {
        return this.processSystemQueryOptionExpand(uriInfo, entity, edmEntitySet);
    }

    private Entity processSystemQueryOptionExpand(UriInfo uriInfo, Entity entity, EdmEntitySet edmEntitySet) throws ODataApplicationException {
        final ExpandOption expandOption = uriInfo.getExpandOption();

        if (expandOption != null) {
            // retrieve the EdmNavigationProperty from the expand expression
            EdmNavigationProperty edmNavigationProperty = null;

            for (ExpandItem expandItem : expandOption.getExpandItems()) {

                if (expandItem.isStar()) {
                    final List<EdmNavigationPropertyBinding> bindings = edmEntitySet.getNavigationPropertyBindings();
                    // we know that there are navigation bindings
                    if (!bindings.isEmpty()) {
                        final EdmNavigationPropertyBinding binding = bindings.get(0);
                        final EdmElement property = edmEntitySet.getEntityType().getProperty(binding.getPath());
                        // errors don't have to be handled here since the olingo library already does this
                        if (property instanceof EdmNavigationProperty) {
                            edmNavigationProperty = (EdmNavigationProperty) property;
                        }
                    }
                } else {
                    final UriResource uriResource = expandItem.getResourcePath().getUriResourceParts().get(0);
                    // errors don't have to be handled here since the olingo library already does this
                    if (uriResource instanceof UriResourceNavigation) {
                        edmNavigationProperty = ((UriResourceNavigation) uriResource).getProperty();
                    }
                }

                if (edmNavigationProperty != null) {
                    final String navPropName = edmNavigationProperty.getName();
                    final EdmEntityType expandEdmEntityType = edmNavigationProperty.getType();

                    Link link = new Link();
                    link.setTitle(navPropName);
                    link.setType(Constants.ENTITY_NAVIGATION_LINK_TYPE);
                    link.setRel(Constants.NS_ASSOCIATION_LINK_REL + navPropName);

                    if (edmNavigationProperty.isCollection()) {
                        // fetches the data for the $expand (to-many navigation) from backend
                        final EntityCollection expandEntityCollection = mNavigationHandler.getRelatedEntityCollection(entity, expandEdmEntityType);

                        link.setInlineEntitySet(expandEntityCollection);
//                        link.setHref(expandEntityCollection.getId().toASCIIString());
                    } else {
                        // fetches the data for the $expand (to-one navigation) from the backend
                        final Entity expandEntity = mNavigationHandler.getRelatedEntity(entity, expandEdmEntityType);
                        link.setInlineEntity(expandEntity);
                        link.setHref(expandEntity.getId().toASCIIString());
                    }

                    // set the link - containing the expanded data - to the current entity
                    entity.getNavigationLinks().add(link);
                }
            }
        }
        return entity;
    }

    /** Create and send response. */
    private void handleResponse(Entity responseEntity, EdmEntitySet responseEdmEntitySet, ContentType responseFormat, EdmEntityType responseEdmEntityType,
                                ODataResponse response, UriInfo uriInfo) throws ODataApplicationException, SerializerException {
        // modify response when query options have been used
        if (responseEntity == null) {
            // this is the case for e.g. DemoService.svc/Categories(4) or DemoService.svc/Categories(3)/Products(999) --> product existing but not in that cat.
            throw new ODataApplicationException("Nothing found.", HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ROOT);
        }
        final Entity queryOptionsResponseEntity = this.handleSystemQueryOptions(uriInfo, responseEntity, responseEdmEntitySet);

        if (queryOptionsResponseEntity != null) {
            responseEntity = queryOptionsResponseEntity;
        }

        // 3. serialize
        final SelectOption selectOption = uriInfo.getSelectOption();
        final ExpandOption expandOption = uriInfo.getExpandOption();
        final String selectList = mOData.createUriHelper().buildContextURLSelectList(responseEdmEntityType, expandOption, selectOption);
        final ContextURL contextURL = ContextURL.with().entitySet(responseEdmEntitySet).selectList(selectList).suffix(Suffix.ENTITY).build();
        final EntitySerializerOptions opts = EntitySerializerOptions.with().contextURL(contextURL).select(selectOption).expand(expandOption).build();

        final ODataSerializer serializer = this.mOData.createSerializer(responseFormat);
        final SerializerResult serializerResult = serializer.entity(this.mServiceMetadata, responseEdmEntityType, responseEntity, opts);

        // 4. configure the response object
        response.setContent(serializerResult.getContent());
        response.setStatusCode(HttpStatusCode.OK.getStatusCode());
        response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
    }

    /**
     * Example request:
     * <p>
     * POST URL: {@code http://localhost:8080/DemoService/DemoService.svc/Products}<br>
     * {@code Header: Content-Type: application/json; mOData.metadata=minimal}<br>
     * Request body:<br>
     * {@code
     * {
     * "ID":3,
     * "Name":"Ergo Screen",
     * "Description":"17 Optimum Resolution 1024 x 768 @ 85Hz, resolution 1280 x 960"
     * }
     * }
     */
    public void createEntity(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType requestFormat, ContentType responseFormat)
        throws ODataApplicationException, DeserializerException, SerializerException {
        //1. Retrieve the entity type from the URI
        final EdmEntitySet edmEntitySet = Util.getEdmEntitySet(uriInfo);
        final EdmEntityType edmEntityType = edmEntitySet.getEntityType();

        //2. create the data in the backend
        //2.1 retrieve the payload from the POST request for the entity to create and deserialize it
        final InputStream requestInputStream = request.getBody();
        final ODataDeserializer deserializer = this.mOData.createDeserializer(requestFormat);
        final DeserializerResult result = deserializer.entity(requestInputStream, edmEntityType);
        final Entity requestEntity = result.getEntity();
        //2.2 do the creation in the backend, which returns the newly created entity
        final Entity createdEntity = mCRUDHandler.createEntityData(edmEntitySet, requestEntity);

        //3. serialize the response (we have to return the created entity)
        final ContextURL contextURL = ContextURL.with().entitySet(edmEntitySet).build();
        //expand and select currently not supported
        final EntitySerializerOptions options = EntitySerializerOptions.with().contextURL(contextURL).build();

        final ODataSerializer serializer = this.mOData.createSerializer(responseFormat);
        final SerializerResult serializedResponse = serializer.entity(mServiceMetadata, edmEntityType, createdEntity, options);

        //4. configure the response object
        response.setContent(serializedResponse.getContent());
        response.setStatusCode(HttpStatusCode.CREATED.getStatusCode());
        response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
    }

    public void updateEntity(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType requestFormat, ContentType responseFormat)
        throws ODataApplicationException, DeserializerException, SerializerException {
        // 1. Retrieve the entity set which belongs to the requested entity
        final List<UriResource> resourcePaths = uriInfo.getUriResourceParts();
        // Note: here the first segment is always the EntitySet
        final UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) resourcePaths.get(0);
        final EdmEntitySet edmEntitySet = uriResourceEntitySet.getEntitySet();
        final EdmEntityType edmEntityType = edmEntitySet.getEntityType();

        // 2. update the data in backend
        // 2.1. retrieve the payload from the PUT request for the entity to be updated
        final InputStream requestInputStream = request.getBody();
        final ODataDeserializer deserializer = this.mOData.createDeserializer(requestFormat);
        final DeserializerResult result = deserializer.entity(requestInputStream, edmEntityType);
        final Entity requestEntity = result.getEntity();
        // 2.2 do the modification in backend
        final List<UriParameter> keyPredicates = uriResourceEntitySet.getKeyPredicates();
        // Note that this updateEntity()-method is invoked for both PUT or PATCH operations
        final HttpMethod httpMethod = request.getMethod();
        mCRUDHandler.updateEntityData(edmEntitySet, keyPredicates, requestEntity, httpMethod);

        //3. configure the response object
        response.setStatusCode(HttpStatusCode.NO_CONTENT.getStatusCode());
    }

    public void deleteEntity(ODataRequest request, ODataResponse response, UriInfo uriInfo) throws ODataApplicationException {
        // 1. Retrieve the entity set which belongs to the requested entity
        final List<UriResource> resourcePaths = uriInfo.getUriResourceParts();
        // Note: here the first segment is always the EntitySet
        final UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) resourcePaths.get(0);
        final EdmEntitySet edmEntitySet = uriResourceEntitySet.getEntitySet();

        // 2. delete the data in backend
        final List<UriParameter> keyPredicates = uriResourceEntitySet.getKeyPredicates();
        mCRUDHandler.deleteEntityData(edmEntitySet, keyPredicates);

        //3. configure the response object
        response.setStatusCode(HttpStatusCode.NO_CONTENT.getStatusCode());
    }
}
