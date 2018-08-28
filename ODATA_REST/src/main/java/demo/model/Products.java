package demo.model;

import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;

public class Products {
 
    private String id;
    private String Name;
    private String deription;

    public Products() {};
    public Products (String id,String Name,String deription)
    {
    	this.id=id;
    	this.Name=Name;
    	this.deription=deription;
    }
    
    public String getDeription() {
		return deription;
	}

	public void setDeription(String deription) {
		this.deription = deription;
	}
 

	public String getName() {
        return Name;
    }

    public void setname(String Name){
        this.Name = Name;
    }
 
 
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
 
	private static final TypeReference<Products> typeRef = new TypeReference<Products>() {};
    public static TypeReference<Products> typeRef() {
        return typeRef;
    }
    private static final TypeReference<List<Products>> listTypeRef = new TypeReference<List<Products>>() {};
    public static TypeReference<List<Products>> listTypeRef() {
        return listTypeRef;
    }
}

