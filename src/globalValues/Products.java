package globalValues;

import java.math.BigDecimal;

public class Products {
	String productID, name, description, categoryID, categoryName;
	BigDecimal price;
	int stock;
	boolean discontinued;
	
	public Products(String prodID, String name, String descrip, String catID, String catName, BigDecimal price, int stock, boolean disc){
		this.productID = prodID;
		this.name = name;
		this.description = descrip;
		this.categoryID = catID;
		this.categoryName = catName;
		this.price = price;
		this.stock = stock;
		this.discontinued = disc;
	}
	
	public void setProductID(String prodID){this.productID = prodID;}
	public void setName(String name){this.name = name;}
	public void setDescription (String description){this.description = description;}
	public void setCategoryID(String catID){this.categoryID = catID;}
	public void setCategoryName(String catName){this.categoryName = catName;}
	public void setStock(int stock){this.stock = stock;}
	public void setPrice(BigDecimal price){this.price = price;}
	public void setDiscontinued(boolean discontinued){this.discontinued = discontinued;}
	
	public String getProductID(){return this.productID;}
	public String getName(){return this.name;}
	public String getDescription(){return this.description;}
	public String getCategoryID(){return this.categoryID;}
	public String getCategoryName(){return this.categoryName;}
	public BigDecimal getPrice(){return this.price;}
	public int getStock(){return this.stock;}
	public boolean getDiscontinued(){return this.discontinued;}
}
