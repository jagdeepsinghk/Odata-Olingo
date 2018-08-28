package demo.model;

import org.apache.olingo.commons.api.edm.FullQualifiedName;

public final class Constants {
  // Service Namespace
  public static final String NAMESPACE = "TDA.ODATA.DEMO";

  // EDM Container
  public static final String CONTAINER_NAME = "Container";
  public static final FullQualifiedName CONTAINER_FQN = new FullQualifiedName(NAMESPACE, CONTAINER_NAME);

  // Entity Types Names
  public static final String ET_PRODUCT_NAME = "Product";
  public static final FullQualifiedName ET_PRODUCT_FQN = new FullQualifiedName(NAMESPACE, ET_PRODUCT_NAME);
 
  // Entity Set Names
  public static final String ES_PRODUCTS_NAME = "Products"; 
}







