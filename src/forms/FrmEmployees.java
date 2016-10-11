package forms;

import globalValues.DBConnection;
import globalValues.Employees;
import globalValues.Variables;

import java.awt.Dialog;
import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JPanel;
import javax.swing.ImageIcon;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFormattedTextField;
import javax.swing.text.MaskFormatter;
import javax.swing.border.LineBorder;

import java.awt.Color;

import javax.swing.JRadioButton;
import javax.swing.border.TitledBorder;

public class FrmEmployees extends JDialog {
	static private JTextField txtEmployeeID, txtFirstName, txtLastName, txtAddress, txtCity, txtZIP, txtMiddleName, txtState;
	static private JFormattedTextField txtDate, txtSSN, txtPhone, txtWage;
	static private JRadioButton rbWage, rbSalary;
	
	private JButton btnAccept;
	private JButton btnCancel;
	private JButton btnSearch;
	private JButton btnNew;
	private JButton btnSave;
	private JButton btnEdit;
	private JButton btnFirst;
	private JButton btnPrevious;
	private JButton btnNext;
	private JButton btnLast;
	
	private CallableStatement stmt = null;
	static private ResultSet rs = null;
	
	static private int employeeID, hiredDate;
	static private String firstName, middleName, lastName, address, city, state, zipCode, ssn, phone;
	//tatic private Date hiredDate;
	static private BigDecimal wage;
	static private boolean isSalary;
	
	Employees objEmp;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		/*
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrmEmployees dialog = new FrmEmployees();
					dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
					dialog.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});*/

		FrmEmployees frmEmp = new FrmEmployees();
		try {
			frmEmp.stmt = DBConnection.con.prepareCall("{call spAllEmployees()}");
			frmEmp.stmt.execute();
			rs = frmEmp.stmt.getResultSet();
			rs.next();
			int id 				= rs.getInt("employeeID");
			String firstName 	= rs.getString("employeeFirstName");
			String middleName	= rs.getString("employeeMiddleName");
			String lastName	 	= rs.getString("employeeLastName");
			String phone 		= rs.getString("phoneNumber");
			String address		= rs.getString("address");
			String city			= rs.getString("city");
			String state		= rs.getString("state");
			String zipCode		= rs.getString("zipCode");
			int hiredDate		= rs.getInt("dateHired");
			String ssn			= rs.getString("ssn");
			BigDecimal wage		= rs.getBigDecimal("wage");
			boolean isSalary	= rs.getBoolean("isSalary");
			frmEmp.objEmp = new Employees(id, firstName, middleName, lastName,
								phone, address, city, state,
								zipCode, hiredDate, ssn, wage, isSalary);
			frmEmp.isEditable(false);
			frmEmp.getResultSet();
			frmEmp.loadFields();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		frmEmp.setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		frmEmp.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		frmEmp.setVisible(true);
		
	}

	/**
	 * Create the dialog.
	 */
	public FrmEmployees() {
		getContentPane().setBackground(new Color(70, 130, 180));
		setBounds(100, 100, 504, 423);
		getContentPane().setLayout(null);
		
		JLabel lblEmployeeID = new JLabel("Employee ID");
		lblEmployeeID.setForeground(new Color(255, 255, 0));
		lblEmployeeID.setHorizontalAlignment(SwingConstants.TRAILING);
		lblEmployeeID.setBounds(10, 11, 96, 20);
		getContentPane().add(lblEmployeeID);
		
		JLabel lblEmployeeName = new JLabel("First Name");
		lblEmployeeName.setForeground(new Color(255, 255, 0));
		lblEmployeeName.setHorizontalAlignment(SwingConstants.TRAILING);
		lblEmployeeName.setBounds(10, 41, 96, 20);
		getContentPane().add(lblEmployeeName);
		
		txtFirstName = new JTextField();
		txtFirstName.setColumns(10);
		txtFirstName.setBounds(116, 41, 96, 20);
		getContentPane().add(txtFirstName);
		
		btnAccept = new JButton(Variables.BUNDLE.getString("General.btnAccept"));
		btnAccept.setIcon(new ImageIcon(FrmEmployees.class.getResource("/icons/1445245812_tick_16.png")));
		btnAccept.setBounds(173, 327, 134, 44);
		getContentPane().add(btnAccept);
		
		btnCancel = new JButton(Variables.BUNDLE.getString("General.btnCancel"));
		btnCancel.setIcon(new ImageIcon(FrmEmployees.class.getResource("/icons/1445257605_DeleteRed.png")));
		btnCancel.setBounds(344, 327, 134, 44);
		getContentPane().add(btnCancel);
		
		btnSearch = new JButton("...");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SearchDialog frame = new SearchDialog(1);	//1 is code for FrmEmployees
				
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
						String id = rs.getString("employeeID");
						
						// builds up complete name, including middle name, or skipping a blank space if there is not middlename
						String name = rs.getString("employeeFirstName") + " ";
						String midName = rs.getString("employeeMiddleName");
						if (midName != null)
							name = name + midName + " ";
						name = name + rs.getString("employeeLastName") +  " (" + id + ")";
						
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
			}
		});
		btnSearch.setToolTipText((String) null);
		btnSearch.setBounds(218, 10, 25, 22);
		getContentPane().add(btnSearch);
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setLayout(null);
		panel.setBounds(419, 11, 59, 125);
		getContentPane().add(panel);
		
		btnNew = new JButton("");
		btnNew.setIcon(new ImageIcon(FrmEmployees.class.getResource("/icons/1446464787_add.png")));
		btnNew.setToolTipText((String) null);
		btnNew.setBounds(0, 0, 59, 40);
		panel.add(btnNew);
		
		btnSave = new JButton("");
		btnSave.setIcon(new ImageIcon(FrmEmployees.class.getResource("/icons/1446464777_save.png")));
		btnSave.setToolTipText((String) null);
		btnSave.setBounds(0, 40, 59, 40);
		panel.add(btnSave);
		
		btnEdit = new JButton("");
		btnEdit.setIcon(new ImageIcon(FrmEmployees.class.getResource("/icons/1446464761_edit-notes.png")));
		btnEdit.setToolTipText((String) null);
		btnEdit.setBounds(0, 81, 59, 40);
		panel.add(btnEdit);
		
		JPanel panel_1 = new JPanel();
		panel_1.setOpaque(false);
		panel_1.setLayout(null);
		panel_1.setBounds(78, 283, 326, 33);
		getContentPane().add(panel_1);
		
		btnFirst = new JButton("");
		btnFirst.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (rs.first() != false){
						getResultSet();
						loadFields();
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnFirst.setIcon(new ImageIcon(FrmEmployees.class.getResource("/icons/1446463685_resultset_first.png")));
		btnFirst.setToolTipText((String) null);
		btnFirst.setBounds(0, 3, 80, 25);
		panel_1.add(btnFirst);
		
		btnPrevious = new JButton("");
		btnPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					if (rs.isFirst() == false){
						rs.previous();
						getResultSet();
						loadFields();
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnPrevious.setIcon(new ImageIcon(FrmEmployees.class.getResource("/icons/1446463626_resultset_previous.png")));
		btnPrevious.setToolTipText((String) null);
		btnPrevious.setBounds(80, 3, 80, 25);
		panel_1.add(btnPrevious);
		
		btnNext = new JButton("");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (rs.isLast() == false){
						rs.next();
						getResultSet();
						loadFields();
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNext.setIcon(new ImageIcon(FrmEmployees.class.getResource("/icons/1446463712_resultset_next.png")));
		btnNext.setToolTipText((String) null);
		btnNext.setBounds(160, 3, 80, 25);
		panel_1.add(btnNext);
		
		btnLast = new JButton("");
		btnLast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (rs.last() != false){
						getResultSet();
						loadFields();
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnLast.setIcon(new ImageIcon(FrmEmployees.class.getResource("/icons/1446463701_resultset_last.png")));
		btnLast.setToolTipText((String) null);
		btnLast.setBounds(240, 3, 80, 25);
		panel_1.add(btnLast);
		
		JLabel lblLastName = new JLabel("Last Name");
		lblLastName.setForeground(new Color(255, 255, 0));
		lblLastName.setHorizontalAlignment(SwingConstants.TRAILING);
		lblLastName.setBounds(37, 71, 69, 17);
		getContentPane().add(lblLastName);
		
		txtLastName = new JTextField();
		txtLastName.setBounds(116, 69, 96, 20);
		getContentPane().add(txtLastName);
		txtLastName.setColumns(10);
		
		JLabel lblPhoneNumber = new JLabel("Phone");
		lblPhoneNumber.setForeground(new Color(255, 255, 0));
		lblPhoneNumber.setHorizontalAlignment(SwingConstants.TRAILING);
		lblPhoneNumber.setBounds(222, 72, 76, 14);
		getContentPane().add(lblPhoneNumber);
		
		JLabel lblAddress = new JLabel("Address");
		lblAddress.setForeground(new Color(255, 255, 0));
		lblAddress.setHorizontalAlignment(SwingConstants.TRAILING);
		lblAddress.setBounds(10, 103, 96, 14);
		getContentPane().add(lblAddress);
		
		JLabel lblCity = new JLabel("City");
		lblCity.setForeground(new Color(255, 255, 0));
		lblCity.setHorizontalAlignment(SwingConstants.TRAILING);
		lblCity.setBounds(10, 131, 96, 17);
		getContentPane().add(lblCity);
		
		txtAddress = new JTextField();
		txtAddress.setBounds(116, 100, 288, 20);
		getContentPane().add(txtAddress);
		txtAddress.setColumns(10);
		
		txtCity = new JTextField();
		txtCity.setBounds(116, 131, 191, 20);
		getContentPane().add(txtCity);
		txtCity.setColumns(10);
		
		JLabel lblZipCode = new JLabel("Zip Code");
		lblZipCode.setForeground(new Color(255, 255, 0));
		lblZipCode.setHorizontalAlignment(SwingConstants.TRAILING);
		lblZipCode.setBounds(161, 164, 69, 16);
		getContentPane().add(lblZipCode);
		
		txtZIP = new JTextField();
		txtZIP.setBounds(239, 162, 68, 20);
		getContentPane().add(txtZIP);
		txtZIP.setColumns(10);
		
		JLabel lblMiddleName = new JLabel("Middle Name");
		lblMiddleName.setForeground(new Color(255, 255, 0));
		lblMiddleName.setHorizontalAlignment(SwingConstants.TRAILING);
		lblMiddleName.setBounds(222, 41, 76, 20);
		getContentPane().add(lblMiddleName);
		
		txtMiddleName = new JTextField();
		txtMiddleName.setBounds(308, 41, 96, 20);
		getContentPane().add(txtMiddleName);
		txtMiddleName.setColumns(10);
		
		JLabel lblHiringDate = new JLabel("Hiring date");
		lblHiringDate.setForeground(new Color(255, 255, 0));
		lblHiringDate.setHorizontalAlignment(SwingConstants.TRAILING);
		lblHiringDate.setBounds(10, 199, 96, 20);
		getContentPane().add(lblHiringDate);
		
		// setting field to insert only date formats and also masking textfield
		DateFormat format = new SimpleDateFormat("MM/dd/yyyy");
		txtDate = new JFormattedTextField(format);
		txtDate.setToolTipText("MM/dd/yyyy");
		txtDate.setBounds(116, 199, 76, 20);
		getContentPane().add(txtDate);
		MaskFormatter dateMask;
		try {
			dateMask = new MaskFormatter("##/##/####");
			dateMask.install(txtDate);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
		JLabel lblSSN = new JLabel("SSN");
		lblSSN.setForeground(new Color(255, 255, 0));
		lblSSN.setHorizontalAlignment(SwingConstants.TRAILING);
		lblSSN.setBounds(65, 230, 39, 20);
		getContentPane().add(lblSSN);
		// setting max of 9 digits for SSN
		NumberFormat fSSN = NumberFormat.getNumberInstance(); 
		fSSN.setMaximumIntegerDigits(9);
		txtSSN = new JFormattedTextField(fSSN);
		txtSSN.setBounds(116, 230, 76, 20);
		getContentPane().add(txtSSN);
		MaskFormatter ssnMask;
		try {
			ssnMask = new MaskFormatter("###-##-####");
			ssnMask.install(txtSSN);
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}	
		
		// setting max of 10 digits for Phone
		NumberFormat fPhone = NumberFormat.getNumberInstance(); 
		fPhone.setMaximumIntegerDigits(10);
		txtPhone = new JFormattedTextField(fPhone);
		txtPhone.setBounds(308, 69, 96, 20);
		getContentPane().add(txtPhone);
		MaskFormatter phoneMask;
		try {
			phoneMask = new MaskFormatter("(###)###-####");
			phoneMask.install(txtPhone);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		JPanel panel_2 = new JPanel();
		panel_2.setForeground(new Color(255, 255, 0));
		panel_2.setOpaque(false);
		panel_2.setBorder(new TitledBorder(null, "Type of Wage", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(227, 191, 177, 81);
		getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		rbWage = new JRadioButton("Hourly");
		rbWage.setOpaque(false);
		rbWage.setForeground(new Color(255, 255, 0));
		rbWage.setBounds(6, 21, 77, 23);
		panel_2.add(rbWage);
		
		
		rbSalary = new JRadioButton("Monthly");
		rbSalary.setOpaque(false);
		rbSalary.setForeground(new Color(255, 255, 0));
		rbSalary.setBounds(88, 21, 83, 23);
		rbSalary.setHorizontalAlignment(SwingConstants.CENTER);
		rbSalary.setHorizontalTextPosition(SwingConstants.RIGHT);
		panel_2.add(rbSalary);
		
		JLabel lblWage = new JLabel("Wage");
		lblWage.setForeground(new Color(255, 255, 0));
		lblWage.setHorizontalAlignment(SwingConstants.TRAILING);
		lblWage.setBounds(6, 52, 65, 19);
		panel_2.add(lblWage);
		
		txtWage = new JFormattedTextField();
		txtWage.setBounds(81, 51, 70, 20);
		panel_2.add(txtWage);
		
		JLabel lblState = new JLabel("State");
		lblState.setForeground(new Color(255, 255, 0));
		lblState.setHorizontalAlignment(SwingConstants.TRAILING);
		lblState.setBounds(60, 165, 46, 14);
		getContentPane().add(lblState);
		
		txtState = new JTextField();
		txtState.setBounds(116, 162, 39, 20);
		getContentPane().add(txtState);
		txtState.setColumns(10);
		
		txtEmployeeID = new JTextField();
		txtEmployeeID.setBounds(116, 11, 96, 20);
		getContentPane().add(txtEmployeeID);
		txtEmployeeID.setColumns(10);
	}
	
	private void isEditable(boolean b){
		txtEmployeeID.setEditable(b);
		txtFirstName.setEditable(b);
		txtMiddleName.setEditable(b);
		txtLastName.setEditable(b);
		txtPhone.setEditable(b);
		txtAddress.setEditable(b);
		txtCity.setEditable(b);
		txtState.setEditable(b);
		txtZIP.setEditable(b);
		txtDate.setEditable(b);
		txtSSN.setEditable(b);
		rbSalary.setEnabled(b);
		rbWage.setEnabled(b); 
		txtWage.setEditable(b);
		
		btnNew.setEnabled(!b);
		btnEdit.setEnabled(!b);
		btnSave.setEnabled(b);
		btnFirst.setEnabled(!b);
		btnPrevious.setEnabled(!b);
		btnNext.setEnabled(!b);
		btnLast.setEnabled(!b);
		btnAccept.setEnabled(!b);
		btnCancel.setEnabled(b);
	}
	
	private void getResultSet(){
		try {
			objEmp.setID(rs.getInt("employeeID"));
			objEmp.setFirstName(rs.getString("employeeFirstName"));
			objEmp.setMiddleName(rs.getString("employeeMiddleName"));
			objEmp.setLastName(rs.getString("employeeLastName"));
			objEmp.setPhoneNumber(rs.getString("phoneNumber"));
			objEmp.setAddress(rs.getString("address"));
			objEmp.setCity(rs.getString("city"));
			objEmp.setState(rs.getString("state"));
			objEmp.setZipCode(rs.getString("zipCode"));
			objEmp.setHiredDate(rs.getInt("dateHired"));
			objEmp.setSSN(rs.getString("ssn"));
			objEmp.setWage(rs.getBigDecimal("wage"));
			objEmp.setIsSalary(rs.getBoolean("isSalary"));	
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadFields(){
		txtEmployeeID.setText(new Integer(objEmp.getID()).toString());
		txtFirstName.setText(objEmp.getFirstName());
		txtMiddleName.setText(objEmp.getMiddleName());
		txtLastName.setText(objEmp.getLastName());
		txtPhone.setText(objEmp.getPhoneNumber());
		txtAddress.setText(objEmp.getAddress());
		txtCity.setText(objEmp.getCity());
		txtState.setText(objEmp.getState());
		txtZIP.setText(objEmp.getZipCode());
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		txtDate.setText(sdf.format(objEmp.getHiredDate()*1000L));	//multiplied by 1000 to get miliseconds
		txtSSN.setValue(Integer.valueOf(objEmp.getSSN()));
		if (objEmp.getIsSalary()){
			rbSalary.setSelected(true);
			rbWage.setSelected(false);
		}else{
			rbSalary.setSelected(false);
			rbWage.setSelected(true);
		}
			
			 
		txtWage.setValue(objEmp.getWage());
	}
}
