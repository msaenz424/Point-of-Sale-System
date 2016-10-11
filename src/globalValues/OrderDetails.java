package globalValues;

import java.math.BigDecimal;

public class OrderDetails {
	String productID, productName;
	BigDecimal unitPrice, discount, extPrice;
	int quantity;
	double subTotal, totalDiscount, tax, totalDue;
	
	public OrderDetails(String productID, String productName, int quantity, BigDecimal unitPrice){
		this.productID = productID;
		this.productName = productName;
		this.quantity = quantity;
		this.unitPrice = unitPrice.setScale(2, BigDecimal.ROUND_CEILING);
		this.discount = new BigDecimal("0.00");
		this.extPrice =  unitPrice.multiply(new BigDecimal(quantity)).subtract(discount);
		
	}
	
	private void reCalculateExtPrice(){
		this.extPrice = unitPrice.multiply(new BigDecimal(quantity)).subtract(discount);
	}
	
	public void setProductID(String productID){this.productID = productID;}
	public void setProductName(String productName){this.productName = productName;}
	public void setQuantity(int quantity){this.quantity = quantity; reCalculateExtPrice(); }
	public void setUnitPrice(BigDecimal unitPrice){
		this.unitPrice = unitPrice.setScale(2, BigDecimal.ROUND_CEILING);
		reCalculateExtPrice();
	}
	public void setDiscount(BigDecimal discount){
		this.discount = discount.setScale(2, BigDecimal.ROUND_CEILING);
		reCalculateExtPrice();
	}
	public void setExtPrice(BigDecimal actual){
		this.discount = actual.setScale(2, BigDecimal.ROUND_CEILING);
	}
	public String getProductID(){return this.productID;}
	public String getProductName(){return this.productName;}
	public int getQuantity(){return this.quantity;}
	public BigDecimal getUnitPrice(){return this.unitPrice;}
	public BigDecimal getDiscount(){return this.discount;}
	public BigDecimal getExtPrice(){return this.extPrice;}
	
}
