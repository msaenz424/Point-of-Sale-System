package forms;

import globalValues.DBConnection;
import globalValues.OrderDetails;
import globalValues.TableModelProduct;
import globalValues.Variables;

import java.awt.Dialog;
import java.awt.EventQueue;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.Timer;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.util.Date;
import java.util.HashMap;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.JTable;
import javax.swing.JScrollPane;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTextField;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.SwingConstants;
import javax.swing.JSpinner;

import java.awt.Font;

import javax.swing.SpinnerNumberModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;

import java.awt.Frame;

import javax.swing.ImageIcon;
import javax.swing.UIManager;

import java.awt.Color;

import javax.swing.border.SoftBevelBorder;
import javax.swing.border.BevelBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

import javax.swing.JSeparator;

//import reports.TodaysTotalSales;


@SuppressWarnings("serial")
public class Sales extends JFrame {

	//private static Connection cx;
	private JPanel contentPane;
	private JList<String> categoryList;
	private JList<String> productList;
	private JLabel lblCategories;
	private JLabel lblProducts;
	private CallableStatement stmt = null;
	private ResultSet rs = null;
	private String catID = "";
	private String prodID = "";
	private String prodName = "";
	DefaultListModel<String> listModelCatName = new DefaultListModel<>();
	DefaultListModel<String> listModelCatID = new DefaultListModel<>();
	DefaultListModel<String> listModelProdID = new DefaultListModel<>();
	DefaultListModel<String> listModelProdName = new DefaultListModel<>();
	DefaultListModel<String> listModelOrderProduct = new DefaultListModel<>();
	JTable tableOrderDetails = new JTable();
	TableModelProduct tableModelProduct = new TableModelProduct();
	private JTextField txtSearchProduct;
	private JButton btnAdd;
	private JButton btnVoid;
	private JButton btnNewSale;
	private JButton btnPay;
	private JButton btnRemove;
	private JButton btnDiscount;
	private JButton btnEditPrice;
	private JButton btnEditQuantity;
	private JLabel lblHowManyItems;
	private JSpinner spnQuantity; 
	private SpinnerNumberModel spnQuantityModel = new SpinnerNumberModel(1, 1, 100, 1);
	private JLabel lblTotalDiscount;
	private JLabel lblSubTotal;
	private JLabel lblTax;
	private JLabel lblTotalDue;
	private JLabel displaySubTotal;
	private JLabel displayTotalDiscount;
	private JLabel displayTax;
	private JLabel displayTotalDue;
	private JLabel displayGreen;
	private JLabel displayYellow;
	private static BigDecimal totalDue, tax;
	public static BigDecimal byCash, byDebitCredit;
	public static boolean isPaid = false;
	public static String userID;
	private JPanel panel;
	private JPanel panelUser;
	private JPanel panelSearch;
	private JLabel displayUserIcon;
	private JLabel displayDate;
	boolean isCartEmpty = true;
	private JMenu menuAdmin;
	private JMenuItem mitemProducts;
	private JMenuItem mitemCategories;
	private JMenuItem mitemEmployees;
	private JMenuItem mntmDailyTopProducts;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args, Connection c) {
		//cx = c;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Sales frame = new Sales();
					frame.setVisible(true);
					System.out.println(userID);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Sales() {
		setExtendedState(Frame.MAXIMIZED_BOTH);
		
		setTitle("Sale\r\n");
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				/*
				 	when window opens, categoryList loads the categories from DB.
			 		This also stores the catID and CatName into 2 listmodels for data manipulation later on
			 	*/
				loadCategories();
				// update the timer
				Timer t = new Timer(50, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
						Date date = new Date();
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						displayDate.setText(sdf.format(date));
						
					}
				});
				t.start();
				// code for timer ends here
				enableButtons();
			}
		});
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setBounds(100, 100, 1313, 853);
		
		JMenuBar mbSales = new JMenuBar();
		setJMenuBar(mbSales);
		
		menuAdmin = new JMenu(Variables.BUNDLE.getString("Sales.Menu.Administration"));
		mbSales.add(menuAdmin);
		
		mitemCategories = new JMenuItem(Variables.BUNDLE.getString("Sales.Menu.Categories"));
		mitemCategories.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FrmCategories.main(null);
				/*
				FrmCategories frame = new FrmCategories();
				frame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
				frame.setVisible(true);
				*/
				//categoryList.clearSelection();
				//listModelCatName.clear();
				//listModelProdName.clear();
				//loadCategories();
				JOptionPane.showMessageDialog(mitemCategories, "In order to show new data you entered, re-start the system");
			}
		});
		menuAdmin.add(mitemCategories);
		
		mitemEmployees = new JMenuItem(Variables.BUNDLE.getString("Sales.Menu.Employees"));
		mitemEmployees.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*
				FrmEmployees frame = new FrmEmployees();
				frame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
				frame.setVisible(true);
				*/
				FrmEmployees.main(null);
			}
		});
		menuAdmin.add(mitemEmployees);
		
		mitemProducts = new JMenuItem(Variables.BUNDLE.getString("Sales.Menu.Products"));
		mitemProducts.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FrmProducts.main(null);
				/*
				FrmProducts frame = new FrmProducts();
				frame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
				frame.setVisible(true);
				*/
				JOptionPane.showMessageDialog(mitemProducts, "In order to show new data you entered, re-start the system");
			}
		});
		menuAdmin.add(mitemProducts);
		
		JMenu mnReports = new JMenu(Variables.BUNDLE.getString("Sales.Menu.Reports"));
		mbSales.add(mnReports);
		
		JMenuItem mntmTodaysTotalSales = new JMenuItem(Variables.BUNDLE.getString("Sales.Menu.TodayTotalSales"));
		mntmTodaysTotalSales.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//TodaysTotalSales totalSales = new TodaysTotalSales();
				//SalesDesign.main(null);
				String reportPath = "C:\\PROJECTS\\Workspace Eclipse\\POS\\src\\reports\\TodaysTopSales.jrxml";
				try {
					JasperReport jr = JasperCompileManager.compileReport(reportPath);
					JasperPrint jp = JasperFillManager.fillReport(jr, null, DBConnection.con);
					JasperViewer.viewReport(jp, false); // false will make close only the viewer when clicked close button
				} catch (JRException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		mnReports.add(mntmTodaysTotalSales);
		
		mntmDailyTopProducts = new JMenuItem(Variables.BUNDLE.getString("Sales.Menu.DailyTopProducts"));
		mntmDailyTopProducts.addActionListener(new ActionListener() {
			//user will enter a # 
			public void actionPerformed(ActionEvent arg0) {
				int unixDate1 = 0, unixDate2 = 0;
				Date date1 = new Date();
				Date date2 = new Date();
				
				/*pop up dialog window will show today's date in the following format: mm/dd/yyyy
				  however in order to manipulate sql query i need an int value with the following logic:
				  date1 will store the earliest time of the day selected, that is at 00:00:00
				  date2 will store the lastest time of the day selected, that is at 23:59:59
				*/
				
				SimpleDateFormat ft = new SimpleDateFormat("MM/dd/yyyy");
				String strDate1 = JOptionPane.showInputDialog("Enter date 1", ft.format(date1));
				
				try {
					date1 = ft.parse(strDate1);
					unixDate1 = (int) (date1.getTime() / 1000);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				String strDate2 = JOptionPane.showInputDialog("Enter date 2", ft.format(date2));
				
				try {
					date2 = ft.parse(strDate2);
					unixDate2 = (int) (date2.getTime() / 1000 + 86400);	//86400 seconds = 1 day
					System.out.println(unixDate2);
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
								
				String inputTopNumber = JOptionPane.showInputDialog("Enter the number of Products you would like to see in your report", 5);
				
				if (isNumber(inputTopNumber) == true){
					String reportPath = "C:\\PROJECTS\\Workspace Eclipse\\POS\\src\\reports\\TopProducts.jrxml";
					try {
						HashMap mapInput = new HashMap();
						//"my_date1", "my_date2", "my_top_number" are parameters created in the jasper file
						mapInput.put("my_date1", unixDate1);
						mapInput.put("my_date2", unixDate2);
						mapInput.put("my_top_number", inputTopNumber);
						JasperReport jr = JasperCompileManager.compileReport(reportPath);
						JasperPrint jp = JasperFillManager.fillReport(jr, mapInput, DBConnection.con);
						
						JasperViewer.viewReport(jp, false); // false will make close only the viewer when clicked close button
					} catch (JRException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} //end if
			}
		});
		
		JSeparator separator = new JSeparator();
		mnReports.add(separator);
		mnReports.add(mntmDailyTopProducts);
		
		JSeparator separator_1 = new JSeparator();
		mnReports.add(separator_1);
		
		JMenu mnSettings = new JMenu(Variables.BUNDLE.getString("Sales.Menu.Settings"));
		mbSales.add(mnSettings);
		
		JMenuItem mntmChangeLanguage = new JMenuItem(Variables.BUNDLE.getString("Sales.Menu.ChangeLanguage"));
		mntmChangeLanguage.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				String str = Variables.LANGUAGE;
				System.out.println("mouseClicked on menu ChangeLanguage");
				
				ChangeLanguage dialog = new ChangeLanguage();
				dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
				dialog.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
				dialog.setVisible(true);
				if (str != Variables.LANGUAGE)
					JOptionPane.showMessageDialog(mntmChangeLanguage,
							Variables.BUNDLE.getString("Sales.Menu.ChangeLanguage.Message"),
							Variables.BUNDLE.getString("Sales.Menu.ChangeLanguage.Title"), 
							JOptionPane.INFORMATION_MESSAGE);
			}
		});
		mnSettings.add(mntmChangeLanguage);
		
		JMenuItem mntmLogout = new JMenuItem(Variables.BUNDLE.getString("Sales.Menu.Logout"));
		mntmLogout.setHorizontalTextPosition(SwingConstants.RIGHT);
		mntmLogout.setHorizontalAlignment(SwingConstants.RIGHT);
		mbSales.add(mntmLogout);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(135, 206, 235));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));		
		setContentPane(contentPane);
		contentPane.setLayout(null);
				
/*********************************************** JTABLE **********************************************************/
		JScrollPane scrollPaneOrderDetails = new JScrollPane();
		scrollPaneOrderDetails.setForeground(new Color(0, 0, 128));
		scrollPaneOrderDetails.setBackground(new Color(224, 255, 255));
		scrollPaneOrderDetails.setBounds(555, 103, 621, 368);
		contentPane.add(scrollPaneOrderDetails);
		
		tableOrderDetails.setModel(tableModelProduct);
		scrollPaneOrderDetails.setViewportView(tableOrderDetails);

/************************************************* REMOVE BUTTON **************************************************/
		btnRemove = new JButton("");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(tableOrderDetails.getSelectedRow());
				tableModelProduct.removeOrderDetail(tableOrderDetails.getSelectedRow());
				tableOrderDetails.setModel(tableModelProduct);
				showTotalNumbers();
			}
		});
		btnRemove.setToolTipText(Variables.BUNDLE.getString("Sales.btnRemove.ToolTipText"));
		btnRemove.setIcon(new ImageIcon(Sales.class.getResource("/icons/1445249092_shopping-cancel.png")));
		btnRemove.setBounds(1173, 135, 76, 68);
		contentPane.add(btnRemove);
		
/****************************************************** APPLY DISCOUNT BUTTON **********************************************/
		btnDiscount = new JButton("");
		btnDiscount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// tableModelProduct.getValueAt(tableOrderDetails.getSelectedRow(), 1) --> gets the name of the product, 0 is the default initial value shown in dialog box	
				String result = JOptionPane.showInputDialog("Apply discount for " + tableModelProduct.getValueAt(tableOrderDetails.getSelectedRow(), 1) , 0);
				if (result != null){
					BigDecimal discount = new BigDecimal(result);
					tableModelProduct.applyDiscount(tableOrderDetails.getSelectedRow(), discount);
					tableOrderDetails.setModel(tableModelProduct);
					showTotalNumbers();
				}
			}
		});
		btnDiscount.setToolTipText(Variables.BUNDLE.getString("Sales.btnDiscount.ToolTipText"));
		btnDiscount.setIcon(new ImageIcon(Sales.class.getResource("/icons/1445249102_shopping-star.png")));
		btnDiscount.setBounds(1173, 269, 76, 68);
		contentPane.add(btnDiscount);
		
/******************************************************** EDIT PRICE BUTTON *************************************************/
		btnEditPrice = new JButton("");
		btnEditPrice.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// tableModelProduct.getValueAt(tableOrderDetails.getSelectedRow(), 1) --> gets the name of the product, 
				// tableModelProduct.getValueAt(tableOrderDetails.getSelectedRow(), 3) --> gets the current price value of the product
				BigDecimal price = new BigDecimal(JOptionPane.showInputDialog("Temporary change the price of " + tableModelProduct.getValueAt(tableOrderDetails.getSelectedRow(), 1) , tableModelProduct.getValueAt(tableOrderDetails.getSelectedRow(), 3)));
				tableModelProduct.editPrice(tableOrderDetails.getSelectedRow(), price);
				tableOrderDetails.setModel(tableModelProduct);
				showTotalNumbers();
			}
		});
		btnEditPrice.setToolTipText(Variables.BUNDLE.getString("Sales.btnEditPrice.ToolTipText"));
		btnEditPrice.setIcon(new ImageIcon(Sales.class.getResource("/icons/1445248456_document-dollar.png")));
		btnEditPrice.setBounds(1173, 336, 76, 68);
		contentPane.add(btnEditPrice);
		
/*********************************************************** EDIT QUANTITY ******************************************************/
		btnEditQuantity = new JButton("");
		btnEditQuantity.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// tableModelProduct.getValueAt(tableOrderDetails.getSelectedRow(), 1) --> gets the name of the product, 
				// tableModelProduct.getValueAt(tableOrderDetails.getSelectedRow(), 2) --> gets the current quantity value for that product
				int quantity = Integer.valueOf((JOptionPane.showInputDialog("How many items of " + tableModelProduct.getValueAt( tableOrderDetails.getSelectedRow(), 1) , tableModelProduct.getValueAt(tableOrderDetails.getSelectedRow(), 2))));
				tableModelProduct.editQuantity(tableOrderDetails.getSelectedRow(), quantity);
				tableOrderDetails.setModel(tableModelProduct);
				showTotalNumbers();
			}
		});
		btnEditQuantity.setIcon(new ImageIcon(Sales.class.getResource("/icons/1445248768_shopping-edit.png")));
		btnEditQuantity.setToolTipText(Variables.BUNDLE.getString("Sales.btnEditQuantity.ToolTipText"));
		btnEditQuantity.setBounds(1173, 202, 76, 68);
		contentPane.add(btnEditQuantity);
		
		btnPay = new JButton(Variables.BUNDLE.getString("Sales.btnPay.Text"));
		btnPay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MakePayment frame = new MakePayment(totalDue);
				frame.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
				frame.setVisible(true);
				System.out.println("Paid?: " + isPaid);
				if (isPaid){
					// stores order into DB
					try {
						stmt = DBConnection.con.prepareCall("{call spSaveOrder(?, ?, ?, ?, ?, ?)}");
						stmt.setString(1, userID);
						stmt.setBigDecimal(2, tax);
						stmt.setBigDecimal(3, byCash);
						stmt.setBigDecimal(4, byDebitCredit);
						stmt.setString(5, "nothing");
						stmt.registerOutParameter(6, Types.VARCHAR);	
						stmt.execute();
						//saves the ID auto-generated in SP to use in OrderDetials' table
						String orderID = stmt.getString(6);
						for (int i = 0; i < tableModelProduct.getRowCount(); i++){
							stmt = DBConnection.con.prepareCall("{call spSaveOrderDetails(?, ?, ?, ?, ?)}");
							stmt.setString(1, orderID);
							stmt.setString(2, tableModelProduct.getValueAt(i, 0).toString());
							stmt.setInt(3, Integer.valueOf(tableModelProduct.getValueAt(i, 2).toString()));
							stmt.setBigDecimal(4, new BigDecimal(tableModelProduct.getValueAt(i, 3).toString()));
							stmt.setBigDecimal(5, new BigDecimal(tableModelProduct.getValueAt(i, 4).toString()));
							stmt.execute();
						}
						System.out.println("Saved!!!!");
						isCartEmpty = true;
						enableButtons();
						btnVoid.setEnabled(false);
						displayGreen.setVisible(true);
						displayYellow.setVisible(false);
					} catch (SQLException eSQL) {
						// TODO Auto-generated catch block
						eSQL.printStackTrace();
					}
				}
			}
		});
		btnPay.setToolTipText(Variables.BUNDLE.getString("Sales.btnPay.ToolTipText"));
		btnPay.setIcon(new ImageIcon(Sales.class.getResource("/icons/1446100409_Cashier-2.png")));
		btnPay.setBounds(956, 579, 155, 94);
		contentPane.add(btnPay);
		
		btnNewSale = new JButton("");
		btnNewSale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tableModelProduct.removeAll();
				tableOrderDetails.setModel(tableModelProduct);
				isCartEmpty = true;
				resetFields();
				enableButtons();
			}
		});
		btnNewSale.setIcon(new ImageIcon(Sales.class.getResource("/icons/1445249479_shopping.png")));
		btnNewSale.setToolTipText(Variables.BUNDLE.getString("Sales.btnNewSale.ToolTipText"));
		btnNewSale.setBounds(1149, 605, 100, 68);
		contentPane.add(btnNewSale);
		
/**************************************** VOID BUTTON ***********************************************************/		
		btnVoid = new JButton("");
		btnVoid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//deletes(internally sets the order status as void) current order from db
				
				// if deletion was successful then 
				if (true){
					btnVoid.setEnabled(false);
				}
			}
		});

		btnVoid.setToolTipText(Variables.BUNDLE.getString("Sales.btnVoid.ToolTipText"));
		btnVoid.setIcon(new ImageIcon(Sales.class.getResource("/icons/1445250531_cancel.png")));
		btnVoid.setBounds(1149, 537, 100, 68);
		contentPane.add(btnVoid);
		
		displayGreen = new JLabel("");
		displayGreen.setVisible(false);
		displayGreen.setIcon(new ImageIcon(Sales.class.getResource("/icons/1445249742_Circle_Green.png")));
		displayGreen.setFont(new Font("Tahoma", Font.BOLD, 20));
		displayGreen.setBounds(1004, 500, 64, 68);
		contentPane.add(displayGreen);
		
		panel = new JPanel();
		panel.setBackground(new Color(240, 230, 140));
		panel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(555, 482, 391, 192);
		contentPane.add(panel);
		
		lblTotalDiscount = new JLabel(Variables.BUNDLE.getString("Sales.lblTotalDiscount.Text"));
		lblTotalDiscount.setForeground(new Color(0, 0, 128));
		lblTotalDiscount.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotalDiscount.setFont(new Font("Tahoma", Font.BOLD, 20));
		
		displayTotalDiscount = new JLabel("0.00");
		displayTotalDiscount.setForeground(new Color(0, 0, 128));
		displayTotalDiscount.setHorizontalAlignment(SwingConstants.TRAILING);
		displayTotalDiscount.setFont(new Font("Tahoma", Font.BOLD, 20));
		
		lblSubTotal = new JLabel(Variables.BUNDLE.getString("Sales.lblSubTotal.Text"));
		lblSubTotal.setForeground(new Color(0, 0, 128));
		lblSubTotal.setHorizontalAlignment(SwingConstants.RIGHT);
		lblSubTotal.setFont(new Font("Tahoma", Font.BOLD, 20));
		
		displaySubTotal = new JLabel("0.00");
		displaySubTotal.setForeground(new Color(0, 0, 128));
		displaySubTotal.setHorizontalAlignment(SwingConstants.TRAILING);
		displaySubTotal.setFont(new Font("Tahoma", Font.BOLD, 20));
		
		lblTax = new JLabel(Variables.BUNDLE.getString("Sales.lblTax.Text"));
		lblTax.setForeground(new Color(0, 0, 128));
		lblTax.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTax.setFont(new Font("Tahoma", Font.BOLD, 20));
		
		displayTax = new JLabel("0.00");
		displayTax.setForeground(new Color(0, 0, 128));
		displayTax.setHorizontalAlignment(SwingConstants.TRAILING);
		displayTax.setFont(new Font("Tahoma", Font.BOLD, 20));
		
		lblTotalDue = new JLabel(Variables.BUNDLE.getString("Sales.lblTotalDue.Text"));
		lblTotalDue.setForeground(new Color(0, 0, 128));
		lblTotalDue.setHorizontalAlignment(SwingConstants.RIGHT);
		lblTotalDue.setFont(new Font("Tahoma", Font.BOLD, 20));
		
		displayTotalDue = new JLabel("0.00");
		displayTotalDue.setForeground(new Color(0, 0, 128));
		displayTotalDue.setHorizontalAlignment(SwingConstants.TRAILING);
		displayTotalDue.setFont(new Font("Tahoma", Font.BOLD, 20));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(lblTax, GroupLayout.PREFERRED_SIZE, 208, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblSubTotal, GroupLayout.PREFERRED_SIZE, 208, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblTotalDue, GroupLayout.PREFERRED_SIZE, 208, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addComponent(displayTax, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
								.addComponent(displaySubTotal, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)
								.addComponent(displayTotalDue, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)))
						.addGroup(gl_panel.createSequentialGroup()
							.addComponent(lblTotalDiscount, GroupLayout.PREFERRED_SIZE, 208, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(displayTotalDiscount, GroupLayout.PREFERRED_SIZE, 129, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap(38, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGap(22)
					.addGroup(gl_panel.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addGap(74)
									.addComponent(displayTax, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_panel.createSequentialGroup()
									.addGap(37)
									.addComponent(displaySubTotal, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)))
							.addGap(38))
						.addGroup(gl_panel.createSequentialGroup()
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblTotalDiscount, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
								.addComponent(displayTotalDiscount, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
							.addGap(1)
							.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_panel.createSequentialGroup()
									.addGap(37)
									.addComponent(lblTax, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
								.addComponent(lblSubTotal, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblTotalDue, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
								.addComponent(displayTotalDue, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap(18, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		
		panelUser = new JPanel();
		panelUser.setOpaque(false);
		panelUser.setBackground(Color.LIGHT_GRAY);
		panelUser.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), Variables.BUNDLE.getString("Sales.panelUser.Title"), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panelUser.setBounds(10, 0, 215, 76);
		contentPane.add(panelUser);
		
		JLabel displayUser = new JLabel(userID);
		displayUser.setForeground(new Color(0, 0, 0));
		
		displayUserIcon = new JLabel("");
		displayUserIcon.setIcon(new ImageIcon(Sales.class.getResource("/icons/1445251485_personal.png")));
//////////////////////// GETS TODAY'S DATE
/*		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		System.out.println(sdf.format(date));*/
//////////////////////////////////////////////////////////
		displayDate = new JLabel();
		displayDate.setForeground(new Color(0, 0, 0));
		GroupLayout gl_panelUser = new GroupLayout(panelUser);
		gl_panelUser.setHorizontalGroup(
			gl_panelUser.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panelUser.createSequentialGroup()
					.addContainerGap()
					.addComponent(displayUserIcon)
					.addGap(18)
					.addGroup(gl_panelUser.createParallelGroup(Alignment.LEADING, false)
						.addComponent(displayUser, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(displayDate, GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE))
					.addContainerGap(38, Short.MAX_VALUE))
		);
		gl_panelUser.setVerticalGroup(
			gl_panelUser.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelUser.createSequentialGroup()
					.addComponent(displayUser, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(displayDate, GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE))
				.addComponent(displayUserIcon, GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
		);
		panelUser.setLayout(gl_panelUser);
		
		panelSearch = new JPanel();
		panelSearch.setOpaque(false);
		panelSearch.setBackground(Color.LIGHT_GRAY);
		panelSearch.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"),  Variables.BUNDLE.getString("Sales.panelSearch.Title"), TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 128)));
		panelSearch.setBounds(10, 99, 522, 418);
		contentPane.add(panelSearch);
		///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/************************************************** SEARCH TEXTFIELD ***********************************************/	
		JLabel lblSearchProductBy = new JLabel(Variables.BUNDLE.getString("Sales.lblSearchByName.Text"));
		lblSearchProductBy.setForeground(new Color(0, 0, 128));
		txtSearchProduct = new JTextField();
		txtSearchProduct.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				String prodPartialName = txtSearchProduct.getText();
				System.out.println(prodPartialName);
				try {
					stmt = DBConnection.con.prepareCall("{call spProductNameLike(?)}");
					stmt.setString(1, prodPartialName);
					stmt.execute();
					rs = stmt.getResultSet();
					listModelProdID.removeAllElements();
					listModelProdName.removeAllElements();
					while (rs.next()){
						String id = rs.getString("productID");
						String name = rs.getString("productName");
						listModelProdID.addElement(id);
						listModelProdName.addElement(name);	
					}
					productList.setModel(listModelProdName);
				} catch (SQLException excep) {
					// TODO Auto-generated catch block
					excep.printStackTrace();
				}
			}
		});
		txtSearchProduct.setColumns(10);
		lblCategories = new JLabel(Variables.BUNDLE.getString("Sales.lblSearchByCategories.Text"));
		lblCategories.setForeground(new Color(0, 0, 128));
		JLabel lblOr = new JLabel(Variables.BUNDLE.getString("Sales.lblOr.Text"));
/****************************************** CATEGORY LIST *************************************************************/
		categoryList = new JList<>();
		categoryList.addListSelectionListener(new ListSelectionListener() {
			
			public void valueChanged(ListSelectionEvent arg0) {
				/*
					when an item is selected from the category List
					gets the catID by finding it in listModelCatID using the index of the selected item on categoryList.
					This also stores prodID and prodName into 2 listmodels for using later on sales transaction
				*/
				productList.clearSelection();
				catID = listModelCatID.getElementAt(categoryList.getSelectedIndex());
				try {
					stmt = DBConnection.con.prepareCall("{call spProductsByCategoryID(?)}");
					stmt.setString(1, catID);
					stmt.execute();
					rs = stmt.getResultSet();
					listModelProdID.removeAllElements();
					listModelProdName.removeAllElements();
					while (rs.next()){
						String id = rs.getString("productID");
						String name = rs.getString("productName");
						listModelProdID.addElement(id);
						listModelProdName.addElement(name);	
					}
					productList.setModel(listModelProdName);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

/*************************************** PRODUCT LIST *******************************************************************/
		lblProducts = new JLabel(Variables.BUNDLE.getString("Sales.lblProducts.Text"));
		lblProducts.setForeground(new Color(0, 0, 128));
		productList = new JList<>();
		productList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				/*
					when an item is selected in the product list
					it gets the productID by finding it in listModelProdID using the index of the selected item on productList
				*/
				if (productList.getSelectedIndex() != -1){
					prodID = listModelProdID.getElementAt(productList.getSelectedIndex());
					prodName = listModelProdName.getElementAt(productList.getSelectedIndex());
					btnAdd.setEnabled(true);
				}else{
					btnAdd.setEnabled(false);
					System.out.println("index selected in productList: " + productList.getSelectedIndex());
					System.out.println("# of items in productList: " + productList.getModel().getSize());
				}
				
			}
		});
		productList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		
		lblHowManyItems = new JLabel(Variables.BUNDLE.getString("Sales.lblHowManyItems.Text"));
		lblHowManyItems.setForeground(new Color(0, 0, 128));
		lblHowManyItems.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		spnQuantity = new JSpinner();
		spnQuantity.setModel(spnQuantityModel);
		spnQuantity.setFont(new Font("Tahoma", Font.BOLD, 20));

/************************************************* ADD BUTTON ******************************************************/
		btnAdd = new JButton("");
		btnAdd.setBackground(UIManager.getColor("Button.background"));
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// when clicked productName, price, quantity and discount are added into jtable (prodID should be handled internally as well)
				// price is called from database by using a SP
				try{
					stmt = DBConnection.con.prepareCall("{call spProductsByID(?)}");
					stmt.setString(1, prodID);
					stmt.execute();
					rs = stmt.getResultSet();
					rs.next();
					BigDecimal price = rs.getBigDecimal("unitPrice");
					int quantity = spnQuantityModel.getNumber().intValue();
					OrderDetails orderDetails = new OrderDetails(prodID, prodName, quantity, price);
					tableModelProduct.addOrderDetail(orderDetails);
					tableOrderDetails.setModel(tableModelProduct);
					spnQuantityModel.setValue(new Integer(1));
				}catch (SQLException e){
					System.out.println(e);
				}
				isCartEmpty = false;
				enableButtons();
				showTotalNumbers();
			}
		});
		btnAdd.setToolTipText(Variables.BUNDLE.getString("Sales.btnAdd.Text"));
		btnAdd.setIcon(new ImageIcon(Sales.class.getResource("/icons/1445249158_shopping-add.png")));
		
		GroupLayout gl_panelSearch = new GroupLayout(panelSearch);
		gl_panelSearch.setHorizontalGroup(
			gl_panelSearch.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelSearch.createSequentialGroup()
					.addGroup(gl_panelSearch.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panelSearch.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panelSearch.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(lblSearchProductBy, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(txtSearchProduct, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)))
						.addGroup(gl_panelSearch.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_panelSearch.createParallelGroup(Alignment.TRAILING, false)
								.addComponent(lblCategories, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addComponent(categoryList, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 188, Short.MAX_VALUE)))
						.addGroup(gl_panelSearch.createSequentialGroup()
							.addGap(85)
							.addComponent(lblOr, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)))
					.addGap(18)
					.addGroup(gl_panelSearch.createParallelGroup(Alignment.LEADING, false)
						.addGroup(gl_panelSearch.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblHowManyItems)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(spnQuantity, GroupLayout.PREFERRED_SIZE, 63, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnAdd, GroupLayout.PREFERRED_SIZE, 81, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblProducts, GroupLayout.PREFERRED_SIZE, 277, GroupLayout.PREFERRED_SIZE)
						.addComponent(productList, GroupLayout.PREFERRED_SIZE, 277, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(14, Short.MAX_VALUE))
		);
		gl_panelSearch.setVerticalGroup(
			gl_panelSearch.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelSearch.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelSearch.createParallelGroup(Alignment.TRAILING)
						.addGroup(gl_panelSearch.createSequentialGroup()
							.addComponent(lblProducts)
							.addGap(2)
							.addComponent(productList, GroupLayout.PREFERRED_SIZE, 303, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_panelSearch.createSequentialGroup()
							.addComponent(lblSearchProductBy)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(txtSearchProduct, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblOr, GroupLayout.PREFERRED_SIZE, 19, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(lblCategories)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(categoryList, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE)
							.addGap(22)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_panelSearch.createParallelGroup(Alignment.LEADING)
						.addComponent(btnAdd, GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)
						.addGroup(gl_panelSearch.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblHowManyItems, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
							.addComponent(spnQuantity, GroupLayout.DEFAULT_SIZE, 48, Short.MAX_VALUE)))
					.addGap(22))
		);
		panelSearch.setLayout(gl_panelSearch);
		
		displayYellow = new JLabel("");
		displayYellow.setIcon(new ImageIcon(Sales.class.getResource("/icons/1445249822_Circle_Yellow.png")));
		displayYellow.setBounds(1004, 500, 64, 68);
		contentPane.add(displayYellow);
		
		// "hides" first column in jtable so productID is not shown
		tableOrderDetails.removeColumn(tableOrderDetails.getColumnModel().getColumn(0));
	}
	
	public BigDecimal calculateTax(BigDecimal amount){
		// tax need to be stored into a global variable
		BigDecimal tax = new BigDecimal("0.0925");
		tax = tax.multiply(amount).setScale(2, BigDecimal.ROUND_CEILING) ;
		return tax;
	}
	
	public void showTotalNumbers(){
		
		BigDecimal subTotal = tableModelProduct.calculateSubtotal();
		BigDecimal totalDiscount = tableModelProduct.calculateTotalDiscount();
		tax = calculateTax(subTotal);	//note that to subTotal the discount is already applied in JTable
		totalDue = subTotal.add(tax).setScale(2, BigDecimal.ROUND_CEILING);
		
		System.out.println(subTotal);
		System.out.println(totalDiscount);
		System.out.println(tax);
		System.out.println(totalDue);
		
		displaySubTotal.setText(String.valueOf(subTotal));
		displayTotalDiscount.setText(String.valueOf(totalDiscount));
		displayTax.setText(String.valueOf(tax));
		displayTotalDue.setText(String.valueOf(totalDue));
	}
	
	public void resetFields(){
		totalDue = BigDecimal.ZERO.setScale(2);
		displaySubTotal.setText("0.00");
		displayTotalDiscount.setText("0.00");
		displayTax.setText("0.00");
		displayTotalDue.setText("0.00");
		displayGreen.setVisible(false);
		displayYellow.setVisible(true);
	}
	
	private void enableButtons(){
		btnAdd.setEnabled(!isCartEmpty);
		btnPay.setEnabled(!isCartEmpty);
		//btnVoid.setEnabled(!isCartEmpty);
		btnRemove.setEnabled(!isCartEmpty);
		btnDiscount.setEnabled(!isCartEmpty);
		btnEditPrice.setEnabled(!isCartEmpty);
		btnEditQuantity.setEnabled(!isCartEmpty);
	}
	
	private void loadCategories(){
		try {
			stmt =  DBConnection.con.prepareCall("{call spAllCategories()}");
			stmt.execute();
			rs = stmt.getResultSet();
			while (rs.next()){
				String id = rs.getString("categoryID");
				String name = rs.getString("categoryName");
				listModelCatID.addElement(id);
				listModelCatName.addElement(name);	
			}
			categoryList.setModel(listModelCatName);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// checks if a text is a number
	private boolean isNumber(String text){
		boolean isNum = false;
		
	    if(text != null) {
	        char[] characters = text.toCharArray();
	        for (int i = 0; i < text.length(); i++) {
	            if (characters[i] < 48 || characters[i] > 57)
	                return false;
	        }
	        return true;
	    }
	    return isNum;
	}
	
}
