package forms;
import globalValues.DBConnection;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.SwingConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JScrollPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;

public class SearchDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtSearch;
	private JList listResult;
	
	private CallableStatement stmt = null;
	private ResultSet rs = null;
	private String id, name;
	static public DefaultListModel<String> listModelID = new DefaultListModel<>();			// keeps track of ID
	static public DefaultListModel<String> listModelName = new DefaultListModel<>();		// model to display in Jlist
	static public Map<String, Integer> mapIndex = new LinkedHashMap<String, Integer>();	// records index # same order as RS

	//public String selectedID;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		/*try {
			SearchDialog dialog = new SearchDialog();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}

	/**
	 * Create the dialog.
	 */
	// the argument below form is an identifier to know what form (FrmProducts, FrmCategories, FrmEmployees) the call was made from
	public SearchDialog(int form) {
		setBounds(100, 100, 314, 338);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(70, 130, 180));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblSearch = new JLabel("Type your search");
			lblSearch.setForeground(new Color(255, 255, 0));
			lblSearch.setHorizontalAlignment(SwingConstants.TRAILING);
			lblSearch.setBounds(22, 11, 111, 14);
			contentPanel.add(lblSearch);
		}
		{
			txtSearch = new JTextField();
			txtSearch.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent arg0) {
					String partialName = txtSearch.getText();
					
					switch(form){
						case 0:	//categories
							try {
								stmt = DBConnection.con.prepareCall("{call spCategoryNameLike(?)}");
								stmt.setString(1, partialName);
								stmt.execute();
								rs = stmt.getResultSet();
								listModelID.removeAllElements();
								listModelName.removeAllElements();
								while (rs.next()){
									String id = rs.getString("categoryID");
									String name = rs.getString("categoryName");
									listModelID.addElement(id);
									listModelName.addElement(name);
								}
								listResult.setModel(listModelName);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;
						case 1: //employees
							break;
						case 2: //products
							try {
								stmt = DBConnection.con.prepareCall("{call spProductNameLike(?)}");
								stmt.setString(1, partialName);
								stmt.execute();
								rs = stmt.getResultSet();
								listModelID.removeAllElements();
								listModelName.removeAllElements();
								while (rs.next()){
									String id = rs.getString("productID");
									String name = rs.getString("productName");
									listModelID.addElement(id);
									listModelName.addElement(name);
								}
								listResult.setModel(listModelName);
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							break;
					}
				}
			});
			txtSearch.setBounds(143, 8, 144, 20);
			contentPanel.add(txtSearch);
			txtSearch.setColumns(10);
		}
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DBConnection.selectedIDIndex = -1;	// -1 = no item was selected
				dispose();
			}
		});
		btnCancel.setBounds(198, 266, 89, 23);
		contentPanel.add(btnCancel);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 36, 278, 219);
		contentPanel.add(scrollPane);
		
		listResult = new JList();
		listResult.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				listResult = (JList) arg0.getSource();
				if (arg0.getClickCount() == 2){
					//when double clicked selected item is passed to previous form
					String id = listModelID.get(listResult.getSelectedIndex());	//searches similar index# from selected listModel
					DBConnection.selectedIDIndex = mapIndex.get(id);
					dispose();
				}
			}
		});
		scrollPane.setViewportView(listResult);
		listResult.setModel(listModelName);
	}
	
	private void loadData(){
		try {
			rs.first();
			while (rs.next()){
				id = rs.getString(0);
				name = rs.getString(1);
				listModelID.addElement("productID");
				listModelName.addElement("productName");
			}
			
			//listModelProdName =
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
