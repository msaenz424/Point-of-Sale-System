package globalValues;

public class Categories {
	String catID, catName, catDescription;
	Boolean catDiscontinued;
	
	public Categories(String id, String name, String descrip, boolean disc){
		this.catID = id;
		this.catName = name;
		this.catDescription = descrip;
		this.catDiscontinued = disc;
	}
	
	public void setID(String id){this.catID = id;}
	public void setName(String name){this.catName = name;}
	public void setDescription(String descrip){this.catDescription = descrip;}
	public void setDiscontinued(boolean disc){this.catDiscontinued = disc;}
	
	public String getID(){return this.catID;}
	public String getName(){return this.catName;}
	public String getDescription(){return this.catDescription;}
	public Boolean getDiscontinued(){return this.catDiscontinued;}
}
