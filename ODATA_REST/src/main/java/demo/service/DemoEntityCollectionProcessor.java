package demo.service;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import org.apache.olingo.commons.api.data.ContextURL;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpHeader;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.ODataRequest;
import org.apache.olingo.server.api.ODataResponse;
import org.apache.olingo.server.api.ServiceMetadata;
import org.apache.olingo.server.api.processor.EntityCollectionProcessor;
import org.apache.olingo.server.api.serializer.EntityCollectionSerializerOptions;
import org.apache.olingo.server.api.serializer.ODataSerializer;
import org.apache.olingo.server.api.serializer.SerializerException;
import org.apache.olingo.server.api.serializer.SerializerResult;
import org.apache.olingo.server.api.uri.UriInfo;
import org.apache.olingo.server.api.uri.UriResource;
import org.apache.olingo.server.api.uri.UriResourceEntitySet;
import org.apache.olingo.server.api.uri.UriResourceNavigation;
import org.apache.olingo.server.api.uri.queryoption.CountOption;
import org.apache.olingo.server.api.uri.queryoption.SelectOption;
import org.apache.olingo.server.api.uri.queryoption.SkipOption;
import org.apache.olingo.server.api.uri.queryoption.TopOption;

import  demo.data.Storage;

public class DemoEntityCollectionProcessor implements EntityCollectionProcessor {

	  private OData odata;
	  private ServiceMetadata srvMetadata;
	  private Storage storage;

	  public DemoEntityCollectionProcessor(Storage storage) {
	    this.storage = storage;
	  }

	  // our processor is initialized with the OData context object
	  public void init(OData odata, ServiceMetadata serviceMetadata) {
	    this.odata = odata;
	    this.srvMetadata = serviceMetadata;
	  }

	  // the only method that is declared in the EntityCollectionProcessor interface
	  // this method is called, when the user fires a request to an EntitySet
	  // in our example, the URL would be:
	  // http://localhost:8080/ExampleService1/ExampleServlet1.svc/Products
	  public void readEntityCollection(ODataRequest request, ODataResponse response,
	      UriInfo uriInfo, ContentType responseFormat)
	      throws ODataApplicationException, SerializerException {

	    EdmEntitySet responseEdmEntitySet = null; // we'll need this to build the ContextURL
	    EntityCollection responseEntityCollection = null; // we'll need this to set the response body

	    // 1st retrieve the requested EntitySet from the uriInfo (representation of the parsed URI)
	    List<UriResource> resourceParts = uriInfo.getUriResourceParts();
	    int segmentCount = resourceParts.size();

	    UriResource uriResource = resourceParts.get(0); // in our example, the first segment is the EntitySet
	    if ( !(uriResource instanceof UriResourceEntitySet)) {
	      throw new ODataApplicationException("Only EntitySet is  supported",
	          HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ROOT);
	    }

	    UriResourceEntitySet uriResourceEntitySet = (UriResourceEntitySet) uriResource;
	    EdmEntitySet startEdmEntitySet = uriResourceEntitySet.getEntitySet();
	   
	    EntityCollection returnEntityCollection = new EntityCollection();
	    CountOption countOption = uriInfo.getCountOption();
	    SelectOption selectOption = uriInfo.getSelectOption();
	    
	    
	    
	    
	    if (segmentCount == 1) { // this is the case for: DemoService/DemoService.svc/Categories
	      responseEdmEntitySet = startEdmEntitySet; // the response body is built from the first (and only) entitySet
	     
	      // 2nd: fetch the data from backend for this requested EntitySetName and deliver as EntitySet
	      try {
			responseEntityCollection = storage.readEntitySetData(startEdmEntitySet);
			 
			
			List<Entity> entityList = responseEntityCollection.getEntities();
			 
	      
	      if (countOption != null) {
	          boolean isCount = countOption.getValue();
	          if(isCount){
	              returnEntityCollection.setCount(entityList.size());
	          }
	      }
	      
	      SkipOption skipOption = uriInfo.getSkipOption();
	  
	   /*   if (  uriInfo.getSkipOption().getValue()!= null)
	      {
	    	  throw new ODataApplicationException(" Skip not found Invalid value for $skip"+uriInfo.getSkipOption().getValue(), HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ROOT);
	      }
	      */
 
	      if (skipOption != null) {
	    	    int skipNumber = skipOption.getValue();
	    	    if (skipNumber >= 0) {
	    	        if(skipNumber <= entityList.size()) {
	    	            entityList = entityList.subList(skipNumber, entityList.size());
	    	        } else {
	    	            // The client skipped all entities
	    	            entityList.clear();
	    	        }
	    	    } else {
	    	        throw new ODataApplicationException("Invalid value for $skip", HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ROOT);
	    	    }
	    	}
	      
	      TopOption topOption = uriInfo.getTopOption();
	      if (topOption != null) {
	    	    int topNumber = topOption.getValue();
	    	    if (topNumber >= 0) {
	    	        if(topNumber <= entityList.size()) {
	    	            entityList = entityList.subList(0, topNumber);
	    	        }  // else the client has requested more entities than available => return what we have
	    	    } else {
	    	        throw new ODataApplicationException("Invalid value for $top", HttpStatusCode.BAD_REQUEST.getStatusCode(), Locale.ROOT);
	    	    }
	    	}
	    	 
	    	 
	      for(Entity entity : entityList){
	    	    returnEntityCollection.getEntities().add(entity);
	    	}
	      
	      }   catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	      
	    } else if (segmentCount == 2) { // in case of navigation: DemoService.svc/Categories(3)/Products

	      UriResource lastSegment = resourceParts.get(1); // in our example we don't support more complex URIs
	      if (lastSegment instanceof UriResourceNavigation) {
	        // TODO!!!        
	        // UriResourceNavigation uriResourceNavigation = (UriResourceNavigation) lastSegment;
	        // EdmNavigationProperty edmNavigationProperty = uriResourceNavigation.getProperty();
	        // EdmEntityType targetEntityType = edmNavigationProperty.getType();
	        // // from Categories(1) to Products
	        // responseEdmEntitySet = Util.getNavigationTargetEntitySet(startEdmEntitySet, edmNavigationProperty);

	        // // 2nd: fetch the data from backend
	        // // first fetch the entity where the first segment of the URI points to
	        // List<UriParameter> keyPredicates = uriResourceEntitySet.getKeyPredicates();
	        // // e.g. for Categories(3)/Products we have to find the single entity: Category with ID 3
	        // Entity sourceEntity = storage.readEntityData(startEdmEntitySet, keyPredicates);
	        // // error handling for e.g. DemoService.svc/Categories(99)/Products
	        // if (sourceEntity == null) {
	        //   throw new ODataApplicationException("Entity not found.",
	        //       HttpStatusCode.NOT_FOUND.getStatusCode(), Locale.ROOT);
	        // }
	        // // then fetch the entity collection where the entity navigates to
	        // // note: we don't need to check uriResourceNavigation.isCollection(),
	        // // because we are the EntityCollectionProcessor
	        // responseEntityCollection = storage.getRelatedEntityCollection(sourceEntity, targetEntityType);
	      }
	    } else { // this would be the case for e.g. Products(1)/Category/Products
	      throw new ODataApplicationException("Not supported",
	          HttpStatusCode.NOT_IMPLEMENTED.getStatusCode(), Locale.ROOT);
	    }

	    // 3rd: create and configure a serializer
	    String selectList = odata.createUriHelper().buildContextURLSelectList( responseEdmEntitySet.getEntityType(),
                null, selectOption);
	    
	   // ContextURL contextUrl = ContextURL.with().entitySet(responseEdmEntitySet).build();
	    ContextURL contextUrl = ContextURL.with().entitySet(responseEdmEntitySet).selectList(selectList).build();
	    final String id = request.getRawBaseUri() + "/" + responseEdmEntitySet.getName();
	 
	//Following code comemnted and added new  code for Count>
	//      EntityCollectionSerializerOptions opts = EntityCollectionSerializerOptions.with()
	 //      .contextURL(contextUrl).id(id).build();
	        
	  EntityCollectionSerializerOptions opts = EntityCollectionSerializerOptions.with()
       .contextURL(contextUrl)
       .id(id)
       .count(countOption)
       .build();  
	        
	    EdmEntityType edmEntityType = responseEdmEntitySet.getEntityType();

	    ODataSerializer serializer = odata.createSerializer(responseFormat);
	   //SerializerResult serializerResult = serializer.entityCollection(this.srvMetadata, edmEntityType,
	   //     responseEntityCollection, opts);
	    SerializerResult serializerResult = serializer.entityCollection(this.srvMetadata,
                edmEntityType,
                returnEntityCollection,
                opts);
 
	    // 4th: configure the response object: set the body, headers and status code
	    response.setContent(serializerResult.getContent());
	    response.setStatusCode(HttpStatusCode.OK.getStatusCode());
	    response.setHeader(HttpHeader.CONTENT_TYPE, responseFormat.toContentTypeString());
	  }

}





