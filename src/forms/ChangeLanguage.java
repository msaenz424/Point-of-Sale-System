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
import javax.swing.SwingConstants;
import javax.swing.DefaultComboBoxModel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.CallableStatement;
import java.sql.SQLException;
import java.awt.Color;

@SuppressWarnings("serial")
public class ChangeLanguage extends JDialog {
	private CallableStatement stmt;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		/*
		try {
			ChangeLanguage dialog = new ChangeLanguage();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		*/
	}

	/**
	 * Create the dialog.
	 */
	public ChangeLanguage() {
		setBounds(100, 100, 384, 124);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(70, 130, 180));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblChooseLanguage = new JLabel(Variables.BUNDLE.getString("ChangeLanguage.lblChooseLanguage"));
		lblChooseLanguage.setForeground(new Color(255, 255, 0));
		lblChooseLanguage.setBackground(new Color(255, 255, 255));
		lblChooseLanguage.setHorizontalAlignment(SwingConstants.TRAILING);
		lblChooseLanguage.setBounds(10, 24, 138, 14);
		contentPanel.add(lblChooseLanguage);
		
		JComboBox<String> cbLanguage = new JComboBox<String>();
		cbLanguage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Variables.LANGUAGE = cbLanguage.getSelectedItem().toString();
			}
		});
		cbLanguage.setModel(new DefaultComboBoxModel<>(new String[]
															{Variables.BUNDLE.getString("ChangeLanguage.cbLanguage.English"), 
															Variables.BUNDLE.getString("ChangeLanguage.cbLanguage.Spanish")}));
		cbLanguage.setBounds(159, 21, 199, 20);
		contentPanel.add(cbLanguage);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(new Color(70, 130, 180));
			buttonPane.setBounds(0, 52, 368, 30);
			contentPanel.add(buttonPane);
			{
				JButton okButton = new JButton(Variables.BUNDLE.getString("General.btnAccept"));
				okButton.setBounds(184, 5, 87, 23);
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String str;
						switch (Variables.LANGUAGE){
							case "English":
								str = "en";
								break;
							case "Spanish":
								str = "es";
								break;
							default:
								str = "en";
								break;
						}
						try {
							stmt =  DBConnection.con.prepareCall("{call spChangeLanguage(?, ?)}");
							stmt.setString(1, str);
							stmt.setString(2, "US");
							stmt.execute();
							
						} catch (SQLException eSQL) {
							// TODO Auto-generated catch block
							eSQL.printStackTrace();
						}
						dispose();
					}
				});
				buttonPane.setLayout(null);
				//okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton(Variables.BUNDLE.getString("General.btnCancel"));
				cancelButton.setBounds(276, 5, 87, 23);
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
