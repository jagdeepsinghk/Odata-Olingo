package demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;
import org.apache.olingo.commons.api.edm.provider.CsdlAbstractEdmProvider;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainer;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityContainerInfo;
import org.apache.olingo.commons.api.edm.provider.CsdlEntitySet;
import org.apache.olingo.commons.api.edm.provider.CsdlEntityType;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlNavigationPropertyBinding;
import org.apache.olingo.commons.api.edm.provider.CsdlProperty;
import org.apache.olingo.commons.api.edm.provider.CsdlPropertyRef;
import org.apache.olingo.commons.api.edm.provider.CsdlSchema;

import  demo.model.Constants;

public class DemoEdmProvider extends CsdlAbstractEdmProvider {

	  @Override
	  public List<CsdlSchema> getSchemas() {

	    // create Schema
	    CsdlSchema schema = new CsdlSchema();
	    schema.setNamespace(Constants.NAMESPACE);

	    // add EntityTypes
	    List<CsdlEntityType> entityTypes = new ArrayList<CsdlEntityType>();
	    entityTypes.add(getEntityType(Constants.ET_PRODUCT_FQN)); 
	    schema.setEntityTypes(entityTypes);

	    // add EntityContainer
	    schema.setEntityContainer(getEntityContainer());

	    // finally
	    List<CsdlSchema> schemas = new ArrayList<CsdlSchema>();
	    schemas.add(schema);

	    return schemas;
	  }

	  @Override
	  public CsdlEntityType getEntityType(FullQualifiedName entityTypeName) {

	    CsdlEntityType entityType = null;

	    // this method is called for one of the EntityTypes that are configured in the Schema
	    if(entityTypeName.equals(Constants.ET_PRODUCT_FQN)){ // Product

	      //create EntityType properties
	      CsdlProperty id = new CsdlProperty().setName("ID").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	      CsdlProperty name = new CsdlProperty().setName("Name").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());
	      CsdlProperty description = new CsdlProperty().setName("deription").setType(EdmPrimitiveTypeKind.String.getFullQualifiedName());

	      // create CsdlPropertyRef for Key element
	      CsdlPropertyRef propertyRef = new CsdlPropertyRef();
	      propertyRef.setName("ID");

	      // configure EntityType
	      entityType = new CsdlEntityType();
	      entityType.setName(Constants.ET_PRODUCT_NAME);
	      entityType.setProperties(Arrays.asList(id, name, description));
	      entityType.setKey(Arrays.asList(propertyRef));
	    }
	    return entityType;
	  }
	  
	  @Override
	  public CsdlEntitySet getEntitySet(FullQualifiedName entityContainer, String entitySetName) {

	    CsdlEntitySet entitySet = null;

	    if(entityContainer.equals(Constants.CONTAINER_FQN)){
	      if(entitySetName.equals(Constants.ES_PRODUCTS_NAME)){ // Sample product
	        entitySet = new CsdlEntitySet();
	        entitySet.setName(Constants.ES_PRODUCTS_NAME);
	        entitySet.setType(Constants.ET_PRODUCT_FQN);
	      } 	      }
	  

	    return entitySet;
	  }

	  @Override
	  public CsdlEntityContainer getEntityContainer() {

	    // create EntitySets
	    List<CsdlEntitySet> entitySets = new ArrayList<CsdlEntitySet>();
	    entitySets.add(getEntitySet(Constants.CONTAINER_FQN, Constants.ES_PRODUCTS_NAME)); 

	    // create EntityContainer
	    CsdlEntityContainer entityContainer = new CsdlEntityContainer();
	    entityContainer.setName(Constants.CONTAINER_NAME);
	    entityContainer.setEntitySets(entitySets);

	    return entityContainer;
	  }

	  @Override
	  public CsdlEntityContainerInfo getEntityContainerInfo(FullQualifiedName entityContainerName) {

	    // This method is invoked when displaying the service document at e.g. http://localhost:8080/DemoService/DemoService.svc
	    if(entityContainerName == null || entityContainerName.equals(Constants.CONTAINER_FQN)){
	      CsdlEntityContainerInfo entityContainerInfo = new CsdlEntityContainerInfo();
	      entityContainerInfo.setContainerName(Constants.CONTAINER_FQN);
	      
	      return entityContainerInfo;
	    }

	    return null;
	  }
	}

	
	
	
	