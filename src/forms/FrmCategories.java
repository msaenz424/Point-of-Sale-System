package forms;

import globalValues.Categories;
import globalValues.DBConnection;
import globalValues.Products;
import globalValues.Variables;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

import java.awt.Dialog;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import java.awt.Color;

@SuppressWarnings("serial")
public class FrmCategories extends JDialog {
	
	private JButton btnSearch;
	private JButton btnNew;
	private JButton btnEdit;
	private JButton btnSave;
	private JButton btnAccept;
	private JButton btnCancel;
	private JButton btnFirst;
	private JButton btnPrevious;
	private JButton btnNext;
	private JButton btnLast;
	private JTextField txtCategoryID;
	private JTextField txtCategoryName;
	private JTextArea txtDescription;
	private JCheckBox chckbxDiscontinued;
	private boolean isNew;
	private int currentRow = 0;	
	private CallableStatement stmt = null;
	static private ResultSet rs = null;
	Categories objCat;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		FrmCategories frmCat = new FrmCategories();
		
		try {
			frmCat.stmt = DBConnection.con.prepareCall("{call spAllCategories()}");
			frmCat.stmt.execute();
			rs = frmCat.stmt.getResultSet();
			rs.next();
			String id = rs.getString("categoryID");
			String name = rs.getString("categoryName");
			String description = rs.getString("categoryDescription");
			Boolean discontinued = rs.getBoolean("discontinued");
			
			// objCat is just an intermediary for each transaction
			frmCat.objCat = new Categories(id, name, description, discontinued);
			
			frmCat.isEditable(false);
			frmCat.loadFields();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frmCat.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		frmCat.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		frmCat.setVisible(true);
		
		/*
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrmCategories dialog = new FrmCategories();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		*/
	}

	/**
	 * Create the dialog.
	 */
	public FrmCategories() {
		getContentPane().setBackground(new Color(70, 130, 180));
		setTitle(Variables.BUNDLE.getString("Sales.Menu.Categories"));
		setBounds(100, 100, 467, 314);
		getContentPane().setLayout(null);
		
		JLabel lblCatID = new JLabel(Variables.BUNDLE.getString("FrmCategories.lblCatID"));
		lblCatID.setForeground(new Color(255, 255, 0));
		lblCatID.setHorizontalAlignment(SwingConstants.TRAILING);
		lblCatID.setBounds(10, 18, 124, 20);
		getContentPane().add(lblCatID);
		
		txtCategoryID = new JTextField();
		txtCategoryID.setBounds(140, 18, 196, 20);
		getContentPane().add(txtCategoryID);
		txtCategoryID.setColumns(10);
		//txtCategoryID.setText(id);
		
		JLabel lblCategoryName = new JLabel(Variables.BUNDLE.getString("FrmCategories.lblCatName"));
		lblCategoryName.setForeground(new Color(255, 255, 0));
		lblCategoryName.setHorizontalAlignment(SwingConstants.TRAILING);
		lblCategoryName.setBounds(10, 43, 124, 20);
		getContentPane().add(lblCategoryName);
		
		txtCategoryName = new JTextField();
		txtCategoryName.setBounds(140, 43, 223, 20);
		getContentPane().add(txtCategoryName);
		txtCategoryName.setColumns(10);
		//txtCategoryName.setText(name);
		
		JLabel lblDescription = new JLabel(Variables.BUNDLE.getString("FrmCategories.lblDescription"));
		lblDescription.setForeground(new Color(255, 255, 0));
		lblDescription.setHorizontalAlignment(SwingConstants.TRAILING);
		lblDescription.setBounds(10, 71, 124, 20);
		getContentPane().add(lblDescription);
		
		btnAccept = new JButton(Variables.BUNDLE.getString("General.btnAccept"));
		btnAccept.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnAccept.setIcon(new ImageIcon(FrmCategories.class.getResource("/icons/1445245812_tick_16.png")));
		btnAccept.setBounds(141, 220, 134, 44);
		getContentPane().add(btnAccept);
		
		btnCancel = new JButton(Variables.BUNDLE.getString("General.btnCancel"));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isEditable(false);
				try {
					rs.absolute(currentRow);
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				setResultSet();
			}
		});
		btnCancel.setIcon(new ImageIcon(FrmCategories.class.getResource("/icons/1445257605_DeleteRed.png")));
		btnCancel.setBounds(298, 220, 134, 44);
		getContentPane().add(btnCancel);
		
		txtDescription = new JTextArea();
		txtDescription.setBounds(140, 69, 223, 66);
		txtDescription.setLineWrap(true);
		getContentPane().add(txtDescription);
		//txtDescription.setText(description);
		
		btnSearch = new JButton("...");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SearchDialog frame = new SearchDialog(0);	//0 is code for Categories
				int rsIndex = 0;
				
				try {
					rsIndex = rs.getRow();	// remembers the last row seen in RS in case user cancels SearchDialog
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				
				// populates JList in SearchDialog form
				try {
					rs.first();
					int i = 0;
					do {
						i++;
						String id = rs.getString("categoryID");
						String name = rs.getString("categoryName");
						SearchDialog.listModelID.addElement(id);
						SearchDialog.listModelName.addElement(name);
						SearchDialog.mapIndex.put(id, i);
						
					}while(rs.next());
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				frame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
				frame.setVisible(true);
				
				// comes back with index of Selected row in RS globalValues -> DBConnection.selectedIDIndex
				try {
					if (DBConnection.selectedIDIndex != -1)	// -1 means no item was selected from SearchDialog
						rs.absolute(DBConnection.selectedIDIndex);
					else
						rs.absolute(rsIndex);
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
		btnSearch.setToolTipText(Variables.BUNDLE.getString("General.btnSearch.ToolTipText"));
		btnSearch.setBounds(338, 17, 25, 22);
		getContentPane().add(btnSearch);
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(70, 130, 180));
		panel.setBounds(373, 13, 59, 122);
		getContentPane().add(panel);
		
		
		btnNew = new JButton("");
		btnNew.setBounds(0, 0, 59, 40);
		btnNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isNew = true;			// isNew distinguishes when either New or Edit button is pressed
				isEditable(true);		// should textfield be editable when New button is pressed?
				
				try {
					currentRow = rs.getRow();	// remembers current row in RS for further use when process is complete (Save & Cancel button)
					stmt = DBConnection.con.prepareCall("{call spCreateCatID(?)}");
					stmt.registerOutParameter(1, Types.VARCHAR);
					stmt.execute();
					objCat.setID(stmt.getString(1));
					//catID = stmt.getString(1);
					txtCategoryID.setText(objCat.getID());
					txtCategoryName.setText("");
					txtDescription.setText("");
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNew.setIcon(new ImageIcon(FrmCategories.class.getResource("/icons/1446464787_add.png")));
		btnNew.setToolTipText("New Category");
		
		btnSave = new JButton("");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (txtCategoryName.getText().trim() != ""){
					// if it is a new entry
					if (isNew){
						try {
							stmt = DBConnection.con.prepareCall("{call spNewCategory(?, ?, ?, ?)}");
							stmt.setString(1, txtCategoryID.getText());
							stmt.setString(2, txtCategoryName.getText());
							stmt.setString(3, txtDescription.getText());
							stmt.setBoolean(4, chckbxDiscontinued.isSelected());
							stmt.execute();
							refreshRS();
							rs.last();
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					// if current rs is being modified
					}else{
						try {
							stmt = DBConnection.con.prepareCall("{call spUpdateCategory(?, ?, ?, ?)}");
							stmt.setString(1, txtCategoryID.getText());
							stmt.setString(2, txtCategoryName.getText());
							stmt.setString(3, txtDescription.getText());
							stmt.setBoolean(4, chckbxDiscontinued.isSelected());
							stmt.execute();
							currentRow = rs.getRow();	// remembers current row in rs
							refreshRS();				// recalls data from server into rs
							rs.absolute(currentRow);	// moves cursor to row that user had on his screen 
							
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					JOptionPane.showMessageDialog(btnSave, "Entry saved sucessfully!");
					isEditable(false);
				}else
					JOptionPane.showMessageDialog(btnSave, "Cannot save an empty entry");
				
				//AFTER SAVING A NEW CATEGORY, PRODUCTS ARE SHOWING INCORRECTLY IN PRODUCT LIST FOR LAST ENTRY
				//WHEN RETURNING TO SALES WINDOW
				//getResultSet();
			}
		});
		btnSave.setBounds(0, 80, 59, 40);
		btnSave.setIcon(new ImageIcon(FrmCategories.class.getResource("/icons/1446464777_save.png")));
		btnSave.setToolTipText("Save");
		
		btnEdit = new JButton("");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				isNew = false;
				isEditable(true);
			}
		});
		btnEdit.setBounds(0, 40, 59, 40);
		btnEdit.setIcon(new ImageIcon(FrmCategories.class.getResource("/icons/1446464761_edit-notes.png")));
		btnEdit.setToolTipText("Edit");
		panel.setLayout(null);
		panel.add(btnNew);
		panel.add(btnSave);
		panel.add(btnEdit);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(70, 130, 180));
		panel_1.setBounds(37, 176, 326, 33);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		btnFirst = new JButton("");
		btnFirst.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					rs.first();
					setResultSet();
					loadFields();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnFirst.setBounds(0, 3, 80, 25);
		panel_1.add(btnFirst);
		btnFirst.setIcon(new ImageIcon(FrmCategories.class.getResource("/icons/1446463685_resultset_first.png")));
		btnFirst.setToolTipText("First");
		
		btnPrevious = new JButton("");
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (rs.previous() != false){
						setResultSet();
						loadFields();
					}else
						rs.first();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnPrevious.setBounds(80, 3, 80, 25);
		panel_1.add(btnPrevious);
		btnPrevious.setIcon(new ImageIcon(FrmCategories.class.getResource("/icons/1446463626_resultset_previous.png")));
		btnPrevious.setToolTipText("Previous");
		
		btnNext = new JButton("");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (rs.next() != false){
						setResultSet();
						loadFields();
					}else
						rs.last();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnNext.setBounds(160, 3, 80, 25);
		panel_1.add(btnNext);
		btnNext.setIcon(new ImageIcon(FrmCategories.class.getResource("/icons/1446463712_resultset_next.png")));
		btnNext.setToolTipText("Next");
		
		btnLast = new JButton("");
		btnLast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					rs.last();
					setResultSet();
					loadFields();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnLast.setBounds(240, 3, 80, 25);
		panel_1.add(btnLast);
		btnLast.setIcon(new ImageIcon(FrmCategories.class.getResource("/icons/1446463701_resultset_last.png")));
		btnLast.setToolTipText("Last");
		
		chckbxDiscontinued = new JCheckBox("Discontinued");
		chckbxDiscontinued.setForeground(new Color(255, 255, 0));
		chckbxDiscontinued.setBackground(new Color(70, 130, 180));
		chckbxDiscontinued.setBounds(140, 142, 97, 23);
		getContentPane().add(chckbxDiscontinued);
		//chckbxDiscontinued.setSelected(discontinued);

		// enables only some fields when form loads
		//isEditable(false);
	}
	
	private void refreshRS(){
		try {
			stmt = DBConnection.con.prepareCall("{call spAllCategories()}");
			stmt.execute();
			rs = stmt.getResultSet();
			rs.next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private void setResultSet(){
		try {
			objCat.setID(rs.getString("categoryID"));
			objCat.setName(rs.getString("categoryName"));
			objCat.setDescription(rs.getString("categoryDescription"));
			objCat.setDiscontinued(rs.getBoolean("discontinued"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadFields(){
		txtCategoryID.setText(objCat.getID());
		txtCategoryName.setText(objCat.getName());
		txtDescription.setText(objCat.getDescription());
		chckbxDiscontinued.setSelected(objCat.getDiscontinued());
	}

	
	private void isEditable(boolean b){
		btnSearch.setEnabled(!b);
		btnNew.setEnabled(!b);
		btnEdit.setEnabled(!b);
		btnSave.setEnabled(b);
		btnAccept.setEnabled(!b);
		btnCancel.setEnabled(b);
		btnFirst.setEnabled(!b);
		btnPrevious.setEnabled(!b);
		btnNext.setEnabled(!b);
		btnLast.setEnabled(!b);
		txtCategoryID.setEditable(b);
		txtCategoryName.setEditable(b);
		txtDescription.setEditable(b);
		chckbxDiscontinued.setEnabled(b);
	}
}
