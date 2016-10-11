package globalValues;

import java.math.BigDecimal;
import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;


@SuppressWarnings("serial")
public class TableModelProduct extends AbstractTableModel {
	private ArrayList<OrderDetails> listOrderDetails;
	private String[] columnNames = {"Prod ID", 
									Variables.BUNDLE.getString("ProductName"),
									Variables.BUNDLE.getString("Quantity"),
									Variables.BUNDLE.getString("UnitPrice"),
									Variables.BUNDLE.getString("Discount"),
									Variables.BUNDLE.getString("ExtPrice")};
	
	public TableModelProduct(){
		this.listOrderDetails = new ArrayList<>();
	}
	
	// this method is to initialize the column names in header 
	@Override
	public String getColumnName(int column) {
		// TODO Auto-generated method stub
		return columnNames[column];
	}
	
	public void addOrderDetail(OrderDetails orderDetail){
		listOrderDetails.add(orderDetail);
		fireTableDataChanged();
	}
	
	public void removeOrderDetail(int index){
		listOrderDetails.remove(index);
		fireTableDataChanged();
	}
	
	public void removeAll(){
		listOrderDetails.clear();
		fireTableDataChanged();
	}
	
	public void editQuantity(int index, int quantity){
		listOrderDetails.get(index).setQuantity(quantity);
		fireTableDataChanged();
	}
	
	public void editPrice(int index, BigDecimal price){
		listOrderDetails.get(index).setUnitPrice(price);
		fireTableDataChanged();
	}
	
	public void applyDiscount(int index, BigDecimal discount){
		listOrderDetails.get(index).setDiscount(discount);
		fireTableDataChanged();
	}
	
	public BigDecimal calculateSubtotal(){
		BigDecimal subTotal = new BigDecimal("0.00");
		for (int i=0; i <= listOrderDetails.size() - 1; i++){
			subTotal = subTotal.add(listOrderDetails.get(i).getExtPrice());
		}
		return subTotal;
	}
	
	public BigDecimal calculateTotalDiscount(){
		BigDecimal totalDiscount = new BigDecimal("0.00");
		for (int i=0; i <= listOrderDetails.size() - 1; i++){
			totalDiscount = totalDiscount.add(listOrderDetails.get(i).getDiscount());
		}
		return totalDiscount;
	}
	
	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public int getRowCount() {
		return listOrderDetails.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex){
			case 0:
				return listOrderDetails.get(rowIndex).getProductID();
			case 1:
				return listOrderDetails.get(rowIndex).getProductName();
			case 2:
				return listOrderDetails.get(rowIndex).getQuantity();
			case 3:
				return listOrderDetails.get(rowIndex).getUnitPrice();
			case 4:
				return listOrderDetails.get(rowIndex).getDiscount();
			case 5:
				return listOrderDetails.get(rowIndex).getExtPrice();
			default:
				return listOrderDetails.get(rowIndex);
		}
	}
}
