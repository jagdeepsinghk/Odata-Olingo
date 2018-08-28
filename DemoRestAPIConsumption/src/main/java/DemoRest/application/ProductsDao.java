package DemoRest.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ProductsDao {
	
	private List<Products> productList=new ArrayList<>(Arrays.asList(new Products("99_123","Car" ,"Tesla"),
			new Products("99_132","Bike" ,"H.Davidson")));
 
  
	public List<Products> getAllProducts()
	{
		return productList;
	}
	
	public Products getProduct(String id)
	{
		return productList.stream().filter(t->t.getId().equals(id)).findFirst().get();
	}
	
	public void addProduct(Products product)
	{
		productList.add(product);
	}
}