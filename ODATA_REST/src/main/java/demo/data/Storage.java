package demo.data;


import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.json.Json;

import java.util.Iterator;
import org.apache.olingo.commons.api.data.Entity;
import org.apache.olingo.commons.api.data.EntityCollection;
import org.apache.olingo.commons.api.data.Link;
import org.apache.olingo.commons.api.data.Property;
import org.apache.olingo.commons.api.data.ValueType;
import org.apache.olingo.commons.api.edm.Edm;
import org.apache.olingo.commons.api.edm.EdmEntitySet;
import org.apache.olingo.commons.api.edm.EdmEntityType;
import org.apache.olingo.commons.api.edm.EdmNamed;
import org.apache.olingo.commons.api.edm.EdmNavigationProperty;
import org.apache.olingo.commons.api.ex.ODataRuntimeException;
import org.apache.olingo.commons.api.http.HttpStatusCode;
import org.apache.olingo.server.api.OData;
import org.apache.olingo.server.api.ODataApplicationException;
import org.apache.olingo.server.api.uri.UriParameter;
 
import  demo.model.Constants;
import demo.model.Products;
import demo.service.DemoEdmProvider;
import  demo.util.util;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Storage {
 
	  private List<Products> productList;
	 
	  // represent our database
	  public void storage()
	  {
		  productList=Arrays.asList(new Products("99","TestProduct" ,"Jeep"));
	  }
	 

	/* PUBLIC FACADE */
	public EntityCollection readEntitySetData(EdmEntitySet edmEntitySet)throws ODataApplicationException, IOException,Exception{

		EntityCollection entityColl = null; 

		// actually, this is only required if we have more than one Entity Sets
		if(edmEntitySet.getName().equals( Constants.ES_PRODUCTS_NAME)){
			entityColl = getProducts();
		}  
		  
		 
		return entityColl;
	}

	  
	
	/*  INTERNAL */
	@SuppressWarnings("unchecked") 
	private EntityCollection getProducts() throws   ODataApplicationException, IOException,Exception   {
		
		 
		 
		OkHttpClient client = new OkHttpClient();
		Request request = new Request.Builder()
                .url("http://localhost:3015/ProductDetails")
                .build();
	  Response response= client.newCall(request).execute() ;
	 
		   
	  ObjectMapper mapper=new ObjectMapper();
	  try
	  {
		 // TypeReference<HashMap<List, Products>> typeRef 
		//  = new TypeReference<HashMap<List, Products>>() {};
		  
		 // productList=  mapper.readValue(response.body().bytes(), List.class);
		 // productList=  mapper.readValue(response.body().bytes(), typeRef);
		  productList   = mapper.readValue(response.body().byteStream(), Products.listTypeRef());
		  
		  
	  }catch (Exception e)
	  {
		  throw  new Exception("Here is some problem"+e);
		  
	  }
	  EntityCollection retEntitySet = new EntityCollection();
	  
	  try
	  {
	  for (Iterator<Products> iterator = productList.iterator(); iterator.hasNext();) {
		  Products product =  (Products)iterator.next();
          Entity e9 = new Entity()
           .addProperty(new Property(null, "ID", ValueType.PRIMITIVE, product.getId()))
            .addProperty(new Property(null, "Name", ValueType.PRIMITIVE,  product.getName()))
            .addProperty(new Property(null, "deription", ValueType.PRIMITIVE,   product.getDeription())) 
            ;

          e9.setId(createId(Constants.ES_PRODUCTS_NAME, product.getId()));
		  retEntitySet.getEntities().add(e9);
        }
	   
	  }catch (Exception e)
	  {
		  throw   new Exception("Here is some problem 1"+ e);
		  
	  }
      return retEntitySet;
	} 
	
	private URI createId(String entitySetName, Object id) {
		try {
			return new URI(entitySetName + "(" + String.valueOf(id) + ")");
		} catch (URISyntaxException e) {
			throw new ODataRuntimeException("Unable to create id for entity: " + entitySetName, e);
		}		
	}

	 
}



