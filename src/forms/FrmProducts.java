package forms;

import globalValues.DBConnection;
import globalValues.KeyValue;
import globalValues.Products;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JCheckBox;

import com.mysql.fabric.xmlrpc.base.Array;
import java.awt.Color;

public class FrmProducts extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtProductID;
	private JTextField txtProductName;
	private JTextArea txtDescription;
	private JFormattedTextField txtUnitPrice;
	private JFormattedTextField txtInStock;
	private JCheckBox chckbxDiscontinued;
	private JComboBox cbCategory;
	private JButton btnNew;
	private JButton btnSave;
	private JButton btnAccept;
	private JButton btnEdit;
	private JButton btnCancel;
	private JButton btnFirst;
	private JButton btnPrevious;
	private JButton btnNext;
	private JButton btnLast;
	
	private boolean isNew;
	private int currentRow = 0;
	private String catKey;
	
	static private Map<String, String> mapCat = new LinkedHashMap<String, String>();	// KEY->VALUE linkedhashmap allows you to add data in the order they come
	static private Vector<String> keysCat = new Vector<String>();		// to store key of categories
	static private KeyValue catKV;
	
	private CallableStatement stmt = null;
	static private ResultSet rsProd = null;
	static private ResultSet rsCat = null;
	
	Products objProd;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		/*try {
			FrmProducts dialog = new FrmProducts();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		FrmProducts frmProd = new FrmProducts();
		try {
			// getting all categories from DB to populate it in combo list
			frmProd.stmt = DBConnection.con.prepareCall("{call spAllCategories()}");
			frmProd.stmt.execute();
			rsCat = frmProd.stmt.getResultSet();
			while (rsCat.next() == true){
				catKV = new KeyValue(rsCat.getString("categoryID"), rsCat.getString("categoryName"));
				mapCat.put(catKV.getKey(), catKV.getValue());
				keysCat.add(catKV.getKey());
			}
			
			frmProd.stmt = DBConnection.con.prepareCall("{call spAllProducts()}");
			frmProd.stmt.execute();
			rsProd = frmProd.stmt.getResultSet();	//passes the data to rsProd which is a variable usable in the entire form
			rsProd.next();			// next goes to the first row
			String id 				= rsProd.getString("productID");
			String prodName 		= rsProd.getString("productName");
			String description 		= rsProd.getString("productDescription");
			String categoryID	 	= rsProd.getString("categoryID");
			String categoryName 	= mapCat.get(categoryID);
			BigDecimal price		= rsProd.getBigDecimal("unitPrice");
			int stock				= rsProd.getInt("stock");
			boolean isDiscontinued	= rsProd.getBoolean("discontinued");
			
			//the purpose of objProd is to use it as intermediary for each transaction
			frmProd.objProd = new Products(id, prodName,description, categoryID, categoryName, price, stock, isDiscontinued);
			frmProd.isEditable(false);
			frmProd.loadFields();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		frmProd.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		frmProd.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		frmProd.setVisible(true);
		
	}

	/**
	 * Create the dialog.
	 */
	public FrmProducts() {
		setBounds(100, 100, 462, 394);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(70, 130, 180));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblProductID = new JLabel("Product ID");
		lblProductID.setForeground(new Color(255, 255, 0));
		lblProductID.setHorizontalAlignment(SwingConstants.TRAILING);
		lblProductID.setBounds(14, 26, 124, 20);
		contentPanel.add(lblProductID);
		
		txtProductID = new JTextField();
		txtProductID.setColumns(10);
		txtProductID.setBounds(144, 26, 196, 20);
		contentPanel.add(txtProductID);
		
		JLabel lblProductName = new JLabel("Product Name");
		lblProductName.setForeground(new Color(255, 255, 0));
		lblProductName.setHorizontalAlignment(SwingConstants.TRAILING);
		lblProductName.setBounds(14, 51, 124, 20);
		contentPanel.add(lblProductName);
		
		txtProductName = new JTextField();
		txtProductName.setColumns(10);
		txtProductName.setBounds(144, 51, 223, 20);
		contentPanel.add(txtProductName);
		
		JLabel lblDescription = new JLabel("Description");
		lblDescription.setForeground(new Color(255, 255, 0));
		lblDescription.setHorizontalAlignment(SwingConstants.TRAILING);
		lblDescription.setBounds(14, 79, 124, 20);
		contentPanel.add(lblDescription);
		
		btnAccept = new JButton("Accept");
		btnAccept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnAccept.setIcon(new ImageIcon(FrmProducts.class.getResource("/icons/1445245812_tick_16.png")));
		btnAccept.setBounds(144, 303, 134, 44);
		contentPanel.add(btnAccept);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isEditable(false);
				setResultSet();
			}
		});
		btnCancel.setIcon(new ImageIcon(FrmProducts.class.getResource("/icons/1445257605_DeleteRed.png")));
		btnCancel.setBounds(302, 303, 134, 44);
		contentPanel.add(btnCancel);
		
		txtDescription = new JTextArea();
		txtDescription.setLineWrap(true);
		txtDescription.setBounds(144, 77, 223, 66);
		contentPanel.add(txtDescription);
		
		JButton button_2 = new JButton("...");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SearchDialog frame = new SearchDialog(2);	//2 is code for Products
				int rsIndex = 0;
				
				try {
					rsIndex = rsProd.getRow();	// remembers the last row seen in RS in case user cancels SearchDialog
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				// populates JList in SearchDialog form
				try {
					rsProd.first();
					int i = 0;
					do {
						i++;
						String id = rsProd.getString("productID");
						String name = rsProd.getString("productName");
						SearchDialog.listModelID.addElement(id);
						SearchDialog.listModelName.addElement(name);
						SearchDialog.mapIndex.put(id, i);
						
					}while(rsProd.next());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				frame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
				frame.setVisible(true);
				
				// comes back with index of Selected row in RS globalValues -> DBConnection.selectedIDIndex
				try {
					if (DBConnection.selectedIDIndex != -1)	// -1 means no item was selected from SearchDialog
						rsProd.absolute(DBConnection.selectedIDIndex);
					else
						rsProd.absolute(rsIndex);
					setResultSet();
					loadFields();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
								
				// reseting data from list models
				SearchDialog.listModelID.removeAllElements();
				SearchDialog.listModelName.removeAllElements();
				SearchDialog.mapIndex.clear();
				DBConnection.selectedIDIndex = -1;
				
			}
		});
		button_2.setToolTipText((String) null);
		button_2.setBounds(342, 25, 25, 22);
		contentPanel.add(button_2);
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(null);
		panel.setBounds(377, 26, 59, 122);
		contentPanel.add(panel);
		
		btnNew = new JButton("");
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				isNew = true;
				isEditable(true);
				
				try {
					stmt = DBConnection.con.prepareCall("{call spCreateProdID(?)}");
					stmt.registerOutParameter(1, Types.VARCHAR);
					stmt.execute();
					txtProductID.setText(stmt.getString(1));
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				txtProductName.setText("");
				txtDescription.setText("");
				cbCategory.setSelectedIndex(0);
				txtUnitPrice.setText("");
				txtInStock.setText("");
				chckbxDiscontinued.setSelected(false);
			}
		});
		btnNew.setIcon(new ImageIcon(FrmProducts.class.getResource("/icons/1446464787_add.png")));
		btnNew.setToolTipText((String) null);
		btnNew.setBounds(0, 0, 59, 40);
		panel.add(btnNew);
		
		btnSave = new JButton("");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (txtProductName.getText().trim() != ""){
					if (isNew)
						try {
							stmt = DBConnection.con.prepareCall("{call spNewProduct(?, ?, ?, ?, ?, ?, ?)}");
							stmt.setString(1, txtProductID.getText());
							stmt.setString(2, txtProductName.getText());
							stmt.setString(3, txtDescription.getText());
							//catKey = catKV.getKey();
							
							stmt.setString(4, catKey);
							stmt.setBigDecimal(5, new BigDecimal(txtUnitPrice.getText()));
							stmt.setInt(6, Integer.parseInt(txtInStock.getText()));
							stmt.setBoolean(7, chckbxDiscontinued.isSelected());
							stmt.execute();
							refreshRS();
							rsProd.last();
							
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					else
						try {
							stmt = DBConnection.con.prepareCall("{call spUpdateProduct(?, ?, ?, ?, ?, ?, ?)}");
							stmt.setString(1, txtProductID.getText());
							stmt.setString(2, txtProductName.getText());
							stmt.setString(3, txtDescription.getText());
							stmt.setString(4, catKey);
							stmt.setBigDecimal(5, new BigDecimal(txtUnitPrice.getText()));
							stmt.setInt(6, Integer.parseInt(txtInStock.getText()));
							stmt.setBoolean(7, chckbxDiscontinued.isSelected());
							stmt.execute();	
							currentRow = rsProd.getRow();	// remembers current row in rs
							refreshRS();				// recalls data from server into rs
							rsProd.absolute(currentRow);	// moves cursor to row that user had on his screen 
							
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					
					JOptionPane.showMessageDialog(btnSave, "Entry saved sucessfully!");
					
					setResultSet();
					isEditable(false);
				}else
					JOptionPane.showMessageDialog(btnSave, "An empty product name is an invalid entry");
			}
		});
		btnSave.setIcon(new ImageIcon(FrmProducts.class.getResource("/icons/1446464777_save.png")));
		btnSave.setToolTipText((String) null);
		btnSave.setBounds(0, 79, 59, 40);
		panel.add(btnSave);
		
		btnEdit = new JButton("");
		btnEdit.setBounds(0, 39, 59, 40);
		panel.add(btnEdit);
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				isNew = false;
				isEditable(true);
			}
		});
		btnEdit.setIcon(new ImageIcon(FrmProducts.class.getResource("/icons/1446464761_edit-notes.png")));
		btnEdit.setToolTipText((String) null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setOpaque(false);
		panel_1.setLayout(null);
		panel_1.setBounds(41, 259, 326, 33);
		contentPanel.add(panel_1);
		
		btnFirst = new JButton("");
		btnFirst.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (rsProd.isFirst() == false){
						rsProd.first();
						setResultSet();
						loadFields();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnFirst.setIcon(new ImageIcon(FrmProducts.class.getResource("/icons/1446463685_resultset_first.png")));
		btnFirst.setToolTipText((String) null);
		btnFirst.setBounds(0, 3, 80, 25);
		panel_1.add(btnFirst);
		
		btnPrevious = new JButton("");
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (rsProd.isFirst() == false){
						rsProd.previous();
						setResultSet();
						loadFields();
					}						
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnPrevious.setIcon(new ImageIcon(FrmProducts.class.getResource("/icons/1446463626_resultset_previous.png")));
		btnPrevious.setToolTipText((String) null);
		btnPrevious.setBounds(80, 3, 80, 25);
		panel_1.add(btnPrevious);
		
		btnNext = new JButton("");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (rsProd.next() != false){
						setResultSet();
						loadFields();
					}else
						rsProd.last();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNext.setIcon(new ImageIcon(FrmProducts.class.getResource("/icons/1446463712_resultset_next.png")));
		btnNext.setToolTipText((String) null);
		btnNext.setBounds(160, 3, 80, 25);
		panel_1.add(btnNext);
		
		btnLast = new JButton("");
		btnLast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (rsProd.isLast() == false){
						rsProd.last();
						setResultSet();
						loadFields();
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnLast.setIcon(new ImageIcon(FrmProducts.class.getResource("/icons/1446463701_resultset_last.png")));
		btnLast.setToolTipText((String) null);
		btnLast.setBounds(240, 3, 80, 25);
		panel_1.add(btnLast);
		
		JLabel lblCategory = new JLabel("Category");
		lblCategory.setForeground(new Color(255, 255, 0));
		lblCategory.setHorizontalAlignment(SwingConstants.TRAILING);
		lblCategory.setBounds(14, 154, 124, 20);
		contentPanel.add(lblCategory);
		
		JLabel lblUnitPrice = new JLabel("Unit price");
		lblUnitPrice.setForeground(new Color(255, 255, 0));
		lblUnitPrice.setHorizontalAlignment(SwingConstants.TRAILING);
		lblUnitPrice.setBounds(14, 185, 124, 20);
		contentPanel.add(lblUnitPrice);
		
		JLabel lblStock = new JLabel("In stock");
		lblStock.setForeground(new Color(255, 255, 0));
		lblStock.setHorizontalAlignment(SwingConstants.TRAILING);
		lblStock.setBounds(203, 185, 105, 20);
		contentPanel.add(lblStock);
		
		cbCategory = new JComboBox();
		cbCategory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				catKey = keysCat.elementAt(cbCategory.getSelectedIndex());
				//JOptionPane.showMessageDialog(btnSave, catKey);
			}
		});
		cbCategory.setBounds(144, 154, 223, 20);
		contentPanel.add(cbCategory);
		
		txtUnitPrice = new JFormattedTextField();
		txtUnitPrice.setBounds(144, 185, 49, 20);
		contentPanel.add(txtUnitPrice);
		
		txtInStock = new JFormattedTextField();
		txtInStock.setBounds(318, 185, 49, 20);
		contentPanel.add(txtInStock);
		
		chckbxDiscontinued = new JCheckBox("Discontinued");
		chckbxDiscontinued.setForeground(new Color(255, 255, 0));
		chckbxDiscontinued.setOpaque(false);
		chckbxDiscontinued.setBackground(new Color(255, 255, 255));
		chckbxDiscontinued.setBounds(144, 220, 124, 23);
		contentPanel.add(chckbxDiscontinued);
	}
	
	private void refreshRS(){
		try {
			stmt = DBConnection.con.prepareCall("{call spAllProducts()}");
			stmt.execute();
			rsProd = stmt.getResultSet();
			rsProd.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void setResultSet(){
		//sets the values of the current rsProd row
		try {
			objProd.setProductID(rsProd.getString("productID"));
			objProd.setName(rsProd.getString("productName"));
			objProd.setDescription(rsProd.getString("productDescription"));
			objProd.setCategoryID(rsProd.getString("categoryID"));
			objProd.setCategoryName(mapCat.get(objProd.getCategoryID()));
			objProd.setPrice(rsProd.getBigDecimal("unitPrice"));
			objProd.setStock(rsProd.getInt("stock"));
			objProd.setDiscontinued(rsProd.getBoolean("discontinued"));
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//after the values of the current row in rsProd is passed to objProd, then these values are populated into fields
	private void loadFields(){
		txtProductID.setText(objProd.getProductID());
		txtProductName.setText(objProd.getName());
		txtDescription.setText(objProd.getDescription());
		for (String s: mapCat.values())	 
			cbCategory.addItem(s);
		cbCategory.setSelectedItem(objProd.getCategoryName());
		//cbCategory.setSelectedItem(catKV.getValue());
		txtUnitPrice.setValue(objProd.getPrice());
		txtInStock.setValue(objProd.getStock());
		chckbxDiscontinued.setSelected(objProd.getDiscontinued());
	}
	

	private void isEditable(boolean b){
		txtProductID.setEditable(b);
		txtProductName.setEditable(b);
		txtDescription.setEditable(b);
		cbCategory.setEnabled(b);
		txtUnitPrice.setEditable(b);
		txtInStock.setEditable(b);
		chckbxDiscontinued.setEnabled(b);
		btnAccept.setEnabled(!b);
		btnCancel.setEnabled(b);
		btnNew.setEnabled(!b);
		btnSave.setEnabled(b);
		btnEdit.setEnabled(!b);
		btnFirst.setEnabled(!b);
		btnPrevious.setEnabled(!b);
		btnNext.setEnabled(!b);
		btnLast.setEnabled(!b);
	}
	
}
