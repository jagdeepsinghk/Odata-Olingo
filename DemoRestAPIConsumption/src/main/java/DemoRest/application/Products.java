package DemoRest.application;
 
public class Products {

	 private String id;
	 private String name;
	 private String deription;
	
	 	public Products( String id, String name, String deription ) {
		// TODO Auto-generated constructor stub
	 	this.id=id;
		this.name=name;
	    this.deription=deription;
	}
	 public String getDeription() {
		return deription;
	}
	public void setDeription(String deription) {
		this.deription = deription;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	} 
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	 
	
}
