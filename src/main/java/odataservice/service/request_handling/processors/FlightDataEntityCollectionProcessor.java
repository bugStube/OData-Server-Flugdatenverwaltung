package odataservice.service.request_handling.processors;

import odataservice.service.request_handling.handler.CRUDHandler;
import odataservice.service.request_handling.handler.NavigationHandler;
import odataservice.service.request_handling.processors.filter_expression.FilterExpressionVisitor;
import odataservice.service.util.Util;
import org.apache.olingo.commons.api.Constants;
import org.apache.olingo.commons.api.data.ContextURL;
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
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ODataResponse;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.serializer.EntityCollectionSerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriParameter;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.apache.olingo.server.api.uri.UriResourceNavigation;
import org.apache.olingo.server.api.uri.queryoption.CountOption;
import org.apache.olingo.server.api.uri.queryoption.ExpandItem;
import org.apache.olingo.server.api.uri.queryoption.ExpandOption;
import org.apache.olingo.server.api.uri.queryoption.FilterOption;
import org.apache.olingo.server.api.uri.queryoption.SelectOption;
import org.apache.olingo.server.api.uri.queryoption.SkipOption;
import org.apache.olingo.server.api.uri.queryoption.TopOption;
import org.apache.olingo.server.api.uri.queryoption.expression.Expression;
import org.apache.olingo.server.api.uri.queryoption.expression.ExpressionVisitException;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 *
 */
public class FlightDataEntityCollectionProcessor implements org.apache.olingo.server.api.processor.EntityCollectionProcessor {

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

    public FlightDataEntityCollectionProcessor(CRUDHandler CRUDHandler, NavigationHandler navigationHandler) {
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
     * This method is invoked when a collection of entities has to be read.
     * Here this can be either a "normal" read operation, or a navigation:<p>
     * Example for "normal" read entity set operation:<br>
     * {@code http://localhost:8080/DemoService/DemoService.svc/Categories}<p>
     * Example for navigation:<br>
     * {@code http://localhost:8080/DemoService/DemoService.svc/Categories(3)/Products}
     */
    public void readEntityCollection(ODataRequest request, ODataResponse response, UriInfo uriInfo, ContentType responseFormat)
        throws ODataApplicationException, SerializerException {
        // retrieve the requested EntitySet from the uriInfo (representation of the parsed URI)
        final List<UriResource> resourceParts = uriInfo.getUriResourceParts();
        final int segmentCount = resourceParts.size();
        //the first segment represents the EntitySet
        final UriResource uriResource = resourceParts.get(0);

        if (!(uriResource instanceof UriResourceEntitySet)) {
            throw new ODataApplicationException("Only EntitySet is supported", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ROOT);
        }

        final UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) uriResource;
        final EdmEntitySet startEdmEntitySet = uriResourceEntitySet.getEntitySet();

        if (segmentCount == 1) {
            this.normalRequest(startEdmEntitySet, request, response, responseFormat, uriInfo);
        } else if (segmentCount == 2) {
            // in case of navigation: DemoService.svc/Categories(3)/Products
            // more complex URIs are not supported
            this.handleNavigationRequest(resourceParts, startEdmEntitySet, uriResourceEntitySet, request, response, responseFormat, uriInfo);
        } else {
            // this would be the case for e.g. Flights(1)/Category/Flights
            throw new ODataApplicationException("Not supported", HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ROOT);
        }
    }

    /** This is the case for: {@code DemoService/DemoService.svc/Categories} */
    private void normalRequest(EdmEntitySet startEdmEntitySet, ODataRequest request, ODataResponse response, ContentType responseFormat, UriInfo uriInfo)
        throws SerializerException, ODataApplicationException {

        final EntityCollection responseEntityCollection = mCRUDHandler.readEntitySetData(startEdmEntitySet);

        this.handleResponse(startEdmEntitySet, request, responseEntityCollection, response, responseFormat, uriInfo);
    }

    /**
     * In case of navigation: {@code DemoService.svc/Categories(3)/Products}<br>
     * More complex URIs are not supported.
     */
    private void handleNavigationRequest(List<UriResource> resourceParts, EdmEntitySet startEdmEntitySet, UriResourceEntitySet uriResourceEntitySet,
                                         ODataRequest request, ODataResponse response, ContentType responseFormat, UriInfo uriInfo)
        throws ODataApplicationException, SerializerException {

        final UriResource lastSegment = resourceParts.get(1);

        if (lastSegment instanceof UriResourceNavigation) {
            final UriResourceNavigation uriResourceNavigation = (UriResourceNavigation) lastSegment;
            final EdmNavigationProperty edmNavigationProperty = uriResourceNavigation.getProperty();
            final EdmEntityType targetEntityType = edmNavigationProperty.getType();
            // from Flights(1) to Carrier
            final EdmEntitySet responseEdmEntitySet = Util.getNavigationTargetEntitySet(startEdmEntitySet, edmNavigationProperty);

            // fetch the data from the backend
            final List<UriParameter> keyPredicates = uriResourceEntitySet.getKeyPredicates();
            // e.g. for Flights(3)/Carrier - first, find the single entity: Flights with ID 3
            final Entity sourceEntity = mCRUDHandler.readEntityData(startEdmEntitySet, keyPredicates);

            if (sourceEntity == null) {
                throw new ODataApplicationException("Entity not found.", HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ROOT);
            }
            // now the entity collection where the entity navigates to will be fetched
            final EntityCollection responseEntityCollection = mNavigationHandler.getRelatedEntityCollection(sourceEntity, targetEntityType);

            this.handleResponse(responseEdmEntitySet, request, responseEntityCollection, response, responseFormat, uriInfo);
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
    private EntityCollection handleSystemQueryOptions(UriInfo uriInfo, EntityCollection entityCollection, EdmEntitySet edmEntitySet)
        throws ODataApplicationException {
        List<Entity> entityList = entityCollection.getEntities();

        // handle $count
        final EntityCollection returnEntityCollection = this.processSystemQueryOptionCount(uriInfo, entityList);
        // handle $filter
        entityList = this.processSystemQueryOptionFilter(uriInfo, entityList);
        // handle $skip
        entityList = this.processSystemQueryOptionSkip(uriInfo, entityList);
        // handle $top
        entityList = this.processSystemQueryOptionTop(uriInfo, entityList);
        // handle $expand
        entityList = this.processSystemQueryOptionExpand(uriInfo, entityList, edmEntitySet);

        // after applying the system query options, a new EntityCollection based on the reduced list will be created
        for (Entity entity : entityList) {
            returnEntityCollection.getEntities().add(entity);
        }

        return returnEntityCollection;
    }

    private List<Entity> processSystemQueryOptionFilter(UriInfo uriInfo, List<Entity> entityList) throws ODataApplicationException {
        final FilterOption filterOption = uriInfo.getFilterOption();
        // apply $filter system query option
        if (filterOption != null) {
            try {
                final Iterator<Entity> entityIterator = entityList.iterator();

                while (entityIterator.hasNext()) {
                    // To evaluate the the expression, create an instance of the Filter Expression Visitor and pass
                    // the current entity to the constructor
                    final Entity currentEntity = entityIterator.next();
                    final Expression filterExpression = filterOption.getExpression();
                    final FilterExpressionVisitor expressionVisitor = new FilterExpressionVisitor(currentEntity);

                    // start evaluating the expression
                    final Object visitorResult = filterExpression.accept(expressionVisitor);

                    // The result of the filter expression must be of type Edm.Boolean
                    if (visitorResult instanceof Boolean) {
                        if (!Boolean.TRUE.equals(visitorResult)) {
                            // The expression evaluated to false (or null), so the currentEntity has to be removed from the entityList
                            entityIterator.remove();
                        }
                    } else {
                        throw new ODataApplicationException("A filter expression must evaluated to type Edm.Boolean",
                                                            HttpStatusCode.BAD_REQUEST.getStatusCode(),
                                                            Locale.ENGLISH);
                    }
                }
            } catch (ExpressionVisitException e) {
                throw new ODataApplicationException("Exception in filter evaluation", HttpStatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), Locale.ENGLISH);
            }
        }

        return entityList;
    }

    private EntityCollection processSystemQueryOptionCount(UriInfo uriInfo, List<Entity> entityList) {
        // handle $count: always return the original number of entities, without considering $top and $skip
        final EntityCollection returnEntityCollection = new EntityCollection();
        final CountOption countOption = uriInfo.getCountOption();
        if (countOption != null) {
            final boolean isCount = countOption.getValue();
            if (isCount) {
                returnEntityCollection.setCount(entityList.size());
            }
        }

        return returnEntityCollection;
    }

    private List<Entity> processSystemQueryOptionSkip(UriInfo uriInfo, List<Entity> entityList) throws ODataApplicationException {
        final SkipOption skipOption = uriInfo.getSkipOption();
        if (skipOption != null) {
            final int skipNumber = skipOption.getValue();
            if (skipNumber >= 0) {
                if (skipNumber <= entityList.size()) {
                    return entityList.subList(skipNumber, entityList.size());
                } else {
                    // The client skipped all entities
                    return null;
                }
            } else {
                throw new ODataApplicationException("Invalid value for $skip", HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ROOT);
            }
        }

        return entityList;
    }

    private List<Entity> processSystemQueryOptionTop(UriInfo uriInfo, List<Entity> entityList) throws ODataApplicationException {
        final TopOption topOption = uriInfo.getTopOption();
        if (topOption != null) {
            final int topNumber = topOption.getValue();
            if (topNumber >= 0) {
                if (topNumber <= entityList.size()) {
                    return entityList.subList(0, topNumber);
                }  // else the client has requested more entities than available => return is given
            } else {
                throw new ODataApplicationException("Invalid value for $top", HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ROOT);
            }
        }

        return entityList;
    }

    private List<Entity> processSystemQueryOptionExpand(UriInfo uriInfo, List<Entity> entityList, EdmEntitySet edmEntitySet) throws ODataApplicationException {
        final ExpandOption expandOption = uriInfo.getExpandOption();

        if (expandOption != null) {
            // retrieve the EdmNavigationProperty from the expand expression
            EdmNavigationProperty edmNavigationProperty = null;
            for (ExpandItem expandItem : expandOption.getExpandItems()) {

                if (expandItem.isStar()) {
                    final List<EdmNavigationPropertyBinding> bindings = edmEntitySet.getNavigationPropertyBindings();
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

                    for (Entity entity : entityList) {
                        final Link link = new Link();
                        link.setTitle(navPropName);
                        link.setType(Constants.ENTITY_NAVIGATION_LINK_TYPE);
                        link.setRel(Constants.NS_ASSOCIATION_LINK_REL + navPropName);

                        if (edmNavigationProperty.isCollection()) {
                            // fetches the data for the $expand (to-many navigation) from backend
                            final EntityCollection expandEntityCollection = mNavigationHandler.getRelatedEntityCollection(entity, expandEdmEntityType);

                            link.setInlineEntitySet(expandEntityCollection);
                            //                            link.setHref(expandEntityCollection.getId().toASCIIString());
                        } else {
                            // fetches the data for the $expand (to-one navigation) from the backend
                            final Entity expandEntity = mNavigationHandler.getRelatedEntity(entity, expandEdmEntityType);
                            link.setInlineEntity(expandEntity);
                            link.setHref(expandEntity.getId().toASCIIString());
                        }

                        // set the link - containing the expand data - to the current entity
                        entity.getNavigationLinks().add(link);
                    }
                }
            }
        }

        return entityList;
    }

    /** Create and send response. */
    private void handleResponse(EdmEntitySet responseEdmEntitySet, ODataRequest request, EntityCollection responseEntityCollection, ODataResponse response,
                                ContentType responseFormat, UriInfo uriInfo) throws SerializerException, ODataApplicationException {
        // modify response when query options have been used
        final EntityCollection queryOptionsEntityCollection = this.handleSystemQueryOptions(uriInfo, responseEntityCollection, responseEdmEntitySet);
        if (!queryOptionsEntityCollection.getEntities().isEmpty()) {
            responseEntityCollection = queryOptionsEntityCollection;
        }

        // create and configure a serializer
        final CountOption countOption = uriInfo.getCountOption();
        final SelectOption selectOption = uriInfo.getSelectOption();
        final ExpandOption expandOption = uriInfo.getExpandOption();
        // the property names of the $select, in order to build the context URL
        final EdmEntityType edmEntityType = responseEdmEntitySet.getEntityType();
        String selectList = mOData.createUriHelper().buildContextURLSelectList(edmEntityType, expandOption, selectOption);
        final ContextURL contextUrl = ContextURL.with().entitySet(responseEdmEntitySet).selectList(selectList).build();
        final String id = request.getRawBaseUri() + "/" + responseEdmEntitySet.getName();
        final EntityCollectionSerializerOptions opts = EntityCollectionSerializerOptions.with().contextURL(contextUrl).id(id).count(countOption).select(
            selectOption).expand(expandOption).build();

        final ODataSerializer serializer = mOData.createSerializer(responseFormat);
        final SerializerResult serializerResult = serializer.entityCollection(this.mServiceMetadata, edmEntityType, responseEntityCollection, opts);

        // configure the response object: set the body, headers and status code
        response.setContent(serializerResult.getContent());
        response.setStatusCode(HttpStatusCode.OK.getStatusCode());
        response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
    }
}
