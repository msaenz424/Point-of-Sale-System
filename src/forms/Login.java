package forms;

import globalValues.DBConnection;
import globalValues.Variables;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JPasswordField;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.SystemColor;

@SuppressWarnings("serial")
public class Login extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JPasswordField pwdPassword;
	private JComboBox<String> cbUser;
	private Connection cx = null;
	private CallableStatement stmt = null;
	private ResultSet rs = null;
	private String userID = "";
	private String userPassword = "";
	private Map<String, String> mapUser = new HashMap<String, String>();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		DBConnection.connectDB();
		Variables.initBundle();
		try {
			Login dialog = new Login();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public Login() {
		setBackground(SystemColor.menu);
		setTitle(Variables.BUNDLE.getString("Login.Title"));
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				try {
					//DBConnection dbCon = new DBConnection();
					//cx = dbCon.con;
					stmt = DBConnection.con.prepareCall("{call spViewUsers()}");
					stmt.execute();
					rs = stmt.getResultSet(); 
					while (rs.next()){
						String id = rs.getString("userID");
						cbUser.addItem(id);
						String pass = rs.getString("userPassword");
						mapUser.put(id, pass);
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		
		setBounds(100, 100, 480, 153);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(Color.DARK_GRAY);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblUser = new JLabel(Variables.BUNDLE.getString("Login.cbUser.Text") + ":");
			lblUser.setForeground(Color.RED);
			lblUser.setBounds(93, 38, 86, 14);
			contentPanel.add(lblUser);
		}
		{
			cbUser = new JComboBox<>();
			cbUser.setForeground(Color.RED);
			cbUser.setBackground(Color.LIGHT_GRAY);
			cbUser.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					userID = cbUser.getSelectedItem().toString();
				}
			});
			cbUser.setBounds(177, 35, 168, 20);
			contentPanel.add(cbUser);
		}
		{
			JLabel lblPassword = new JLabel(Variables.BUNDLE.getString("Login.pwdPassword.Text") + ":");
			lblPassword.setForeground(Color.RED);
			//JLabel lblPassword = new JLabel("Password:");
			lblPassword.setBounds(93, 69, 86, 14);
			contentPanel.add(lblPassword);
		}
		{
			pwdPassword = new JPasswordField();
			pwdPassword.setForeground(Color.RED);
			pwdPassword.setBackground(Color.LIGHT_GRAY);
			pwdPassword.setBounds(177, 66, 168, 20);
			contentPanel.add(pwdPassword);
		}
		{
			JButton btnLogin = new JButton("");
			
			btnLogin.setToolTipText(Variables.BUNDLE.getString("Login.btnLogin.ToolTipText"));
			btnLogin.setIcon(new ImageIcon("C:\\PROJECTS\\Workspace Eclipse\\POS\\icons\\1445255623_unlock.png"));
			btnLogin.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					userPassword = new String(pwdPassword.getPassword());
					try {
						if (validateLogin()){
							Sales.userID = userID;
							Sales.main(new String[0], cx);
							dispose();
						};
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			btnLogin.setBounds(355, 35, 81, 51);
			contentPanel.add(btnLogin);
		}
		{
			JLabel displayUserIcon = new JLabel("");
			displayUserIcon.setIcon(new ImageIcon("C:\\Users\\Miguel\\Documents\\Workspace Eclipse\\POS\\icons\\1445251485_personal.png"));
			displayUserIcon.setBounds(23, 30, 46, 59);
			contentPanel.add(displayUserIcon);
		}
	}
	
	private boolean validateLogin() throws SQLException{
		String mapPass = mapUser.get(userID);
		boolean result = mapPass.equals(userPassword);
			
		System.out.println(mapUser.get(cbUser.getSelectedItem().toString()) + " " + userPassword);
		return result;
	}

}
