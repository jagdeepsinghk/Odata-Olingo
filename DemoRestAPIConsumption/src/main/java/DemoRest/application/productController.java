package DemoRest.application;

import java.awt.PageAttributes.MediaType;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class productController {


	@Autowired	
	private ProductsDao  productsService; 
	 
	@RequestMapping(value="/ProductDetails")
	public  List<Products> getAllProducts()
	{
		 
		return productsService.getAllProducts();
	}
	 @RequestMapping("/ProductDetails/{id}")
	public Products getProduct(@PathVariable String id)
	{
		return productsService.getProduct(id);
	}
	 
	 @RequestMapping(value="/ProductDetails" , method=RequestMethod.POST)
		public void addProduct(@RequestBody Products product)
		{
			 productsService.addProduct(product);
		}
	 
}
