package forms;

import globalValues.Variables;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;
import javax.swing.JLabel;

import java.awt.Font;

import javax.swing.SwingConstants;
import javax.swing.JFormattedTextField;
import javax.swing.UIManager;

import java.awt.Color;

import javax.swing.LayoutStyle.ComponentPlacement;

import java.awt.Dimension;
import java.math.BigDecimal;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;

@SuppressWarnings("serial")
public class MakePayment extends JDialog {

	private final JPanel contentPanel = new JPanel();
	JLabel displayAmountDue;
	JFormattedTextField txtAmountReceived = new JFormattedTextField();
	JLabel displayByDebitCredit = new JLabel("0.00");
	JLabel displayByCash = new JLabel("0.00");
	JLabel displayChange = new JLabel("0.00");
	
	boolean isCash = true;
	BigDecimal amountDue, totalDue;
	BigDecimal byCash = new BigDecimal(0).setScale(2);
	BigDecimal byDebitCredit = new BigDecimal(0).setScale(2);
	BigDecimal totalAmountPaid = new BigDecimal(0).setScale(2);
	BigDecimal amountReceived = new BigDecimal(0).setScale(2);;
	BigDecimal oneD = new BigDecimal(1).setScale(2);
	BigDecimal twoD = new BigDecimal(2).setScale(2);
	BigDecimal fiveD = new BigDecimal(5).setScale(2);
	BigDecimal tenD = new BigDecimal(10).setScale(2);
	BigDecimal twentyD = new BigDecimal(20).setScale(2);
	BigDecimal fiftyD = new BigDecimal(50).setScale(2);
	BigDecimal hundredD = new BigDecimal(100).setScale(2);
	
	/**
	 * Launch the application.
	 */
/*	public static void main(String[] args) {
		try {
			MakePayment dialog = new MakePayment();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
*/
	/**
	 * Create the dialog.
	 */
	public MakePayment(BigDecimal amountDue) {
		setTitle(Variables.BUNDLE.getString("MakePayment.Title"));
		this.amountDue = amountDue;
		this.totalDue = amountDue;
		
		setBounds(100, 100, 590, 467);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(70, 130, 180));
		contentPanel.setForeground(new Color(0, 0, 0));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JPanel panelReceived = new JPanel();
			panelReceived.setOpaque(false);
			panelReceived.setBackground(new Color(135, 206, 235));
			panelReceived.setBounds(30, 11, 343, 271);
			panelReceived.setBorder(new TitledBorder(null, Variables.BUNDLE.getString("MakePayment.panelReceived.Title"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
			contentPanel.add(panelReceived);
			JLabel lblAmountDue = new JLabel(Variables.BUNDLE.getString("MakePayment.lblAmountDue"));
			lblAmountDue.setForeground(new Color(255, 255, 0));
			lblAmountDue.setHorizontalAlignment(SwingConstants.RIGHT);
			lblAmountDue.setFont(new Font("Tahoma", Font.PLAIN, 18));
			displayAmountDue = new JLabel(amountDue.toString());
			displayAmountDue.setForeground(new Color(255, 255, 0));
			displayAmountDue.setHorizontalAlignment(SwingConstants.RIGHT);
			displayAmountDue.setFont(new Font("Tahoma", Font.BOLD, 20));
			JLabel lblAmountReceived = new JLabel(Variables.BUNDLE.getString("MakePayment.lblAmountReceived"));
			lblAmountReceived.setForeground(new Color(255, 255, 0));
			lblAmountReceived.setHorizontalAlignment(SwingConstants.RIGHT);
			lblAmountReceived.setFont(new Font("Tahoma", Font.PLAIN, 18));
			
			txtAmountReceived.setText("0.00");
			txtAmountReceived.setHorizontalAlignment(SwingConstants.RIGHT);
			txtAmountReceived.setFont(new Font("Tahoma", Font.BOLD, 20));
			JButton btnOne = new JButton("+$1");
			btnOne.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					amountReceived = amountReceived.add(oneD);
					System.out.println(amountReceived.toString());
					txtAmountReceived.setText(amountReceived.toString());
				}
			});
			JButton btnTwo = new JButton("+$2");
			btnTwo.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					amountReceived = amountReceived.add(twoD);
					txtAmountReceived.setText(amountReceived.toString());
				}
			});
			JButton btnFive = new JButton("+$5");
			btnFive.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					amountReceived = amountReceived.add(fiveD);
					txtAmountReceived.setText(amountReceived.toString());
				}
			});
			JButton btnTen = new JButton("+$10");
			btnTen.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					amountReceived = amountReceived.add(tenD);
					txtAmountReceived.setText(amountReceived.toString());
				}
			});
			JButton btnTwenty = new JButton("+$20");
			btnTwenty.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					amountReceived = amountReceived.add(twentyD);
					txtAmountReceived.setText(amountReceived.toString());
				}
			});
			JButton btnFifty = new JButton("+$50");
			btnFifty.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					amountReceived = amountReceived.add(fiftyD);
					txtAmountReceived.setText(amountReceived.toString());
				}
			});
			JButton btnHundred = new JButton("+$100");
			btnHundred.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					amountReceived = amountReceived.add(hundredD);
					txtAmountReceived.setText(amountReceived.toString());
				}
			});
			JButton btnClear = new JButton(Variables.BUNDLE.getString("MakePayment.btnClear.Text"));
			btnClear.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					amountReceived = BigDecimal.ZERO.setScale(2);
					txtAmountReceived.setText(amountReceived.toString());
				}
			});
			GroupLayout gl_panelReceived = new GroupLayout(panelReceived);
			gl_panelReceived.setHorizontalGroup(
				gl_panelReceived.createParallelGroup(Alignment.LEADING)
					.addGap(0, 331, Short.MAX_VALUE)
					.addGap(0, 331, Short.MAX_VALUE)
					.addGroup(gl_panelReceived.createSequentialGroup()
						.addGap(25)
						.addGroup(gl_panelReceived.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_panelReceived.createSequentialGroup()
								.addComponent(lblAmountDue, GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE)
								.addGap(18)
								.addComponent(displayAmountDue, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_panelReceived.createSequentialGroup()
								.addComponent(lblAmountReceived, GroupLayout.PREFERRED_SIZE, 141, GroupLayout.PREFERRED_SIZE)
								.addGap(18)
								.addComponent(txtAmountReceived, GroupLayout.PREFERRED_SIZE, 110, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_panelReceived.createSequentialGroup()
								.addGap(3)
								.addComponent(btnOne, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
								.addGap(10)
								.addComponent(btnTwo, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
								.addGap(10)
								.addComponent(btnFive, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_panelReceived.createSequentialGroup()
								.addGap(3)
								.addComponent(btnTen, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
								.addGap(10)
								.addComponent(btnTwenty, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
								.addGap(10)
								.addComponent(btnFifty, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_panelReceived.createSequentialGroup()
								.addGap(3)
								.addComponent(btnHundred, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
								.addGap(10)
								.addComponent(btnClear, GroupLayout.PREFERRED_SIZE, 174, GroupLayout.PREFERRED_SIZE)))
						.addContainerGap(25, Short.MAX_VALUE))
			);
			gl_panelReceived.setVerticalGroup(
				gl_panelReceived.createParallelGroup(Alignment.LEADING)
					.addGap(0, 298, Short.MAX_VALUE)
					.addGap(0, 275, Short.MAX_VALUE)
					.addGroup(gl_panelReceived.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panelReceived.createParallelGroup(Alignment.LEADING)
							.addComponent(lblAmountDue, GroupLayout.PREFERRED_SIZE, 29, GroupLayout.PREFERRED_SIZE)
							.addComponent(displayAmountDue, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE))
						.addGap(5)
						.addGroup(gl_panelReceived.createParallelGroup(Alignment.LEADING)
							.addGroup(gl_panelReceived.createSequentialGroup()
								.addGap(2)
								.addComponent(lblAmountReceived, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE))
							.addComponent(txtAmountReceived, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE))
						.addGap(18)
						.addGroup(gl_panelReceived.createParallelGroup(Alignment.LEADING)
							.addComponent(btnOne, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnTwo, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnFive, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
						.addGap(11)
						.addGroup(gl_panelReceived.createParallelGroup(Alignment.LEADING)
							.addComponent(btnTen, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnTwenty, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnFifty, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
						.addGap(11)
						.addGroup(gl_panelReceived.createParallelGroup(Alignment.LEADING)
							.addComponent(btnHundred, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE)
							.addComponent(btnClear, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
						.addContainerGap(19, Short.MAX_VALUE))
			);
			panelReceived.setLayout(gl_panelReceived);
		}
		{
			JPanel panelPaid = new JPanel();
			panelPaid.setOpaque(false);
			panelPaid.setForeground(new Color(0, 0, 0));
			panelPaid.setBackground(new Color(135, 206, 235));
			panelPaid.setBounds(30, 299, 188, 107);
			panelPaid.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), Variables.BUNDLE.getString("MakePayment.panelPaid.Title"), TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
			contentPanel.add(panelPaid);
			JLabel lblCash = new JLabel(Variables.BUNDLE.getString("MakePayment.lblCash"));
			JLabel lblDebitCredit = new JLabel(Variables.BUNDLE.getString("MakePayment.lblDebitCredit"));
			
			displayByDebitCredit.setHorizontalAlignment(SwingConstants.RIGHT);
			displayByDebitCredit.setFont(new Font("Tahoma", Font.PLAIN, 14));
			
			displayByCash.setHorizontalAlignment(SwingConstants.RIGHT);
			displayByCash.setFont(new Font("Tahoma", Font.PLAIN, 14));
			GroupLayout gl_panelPaid = new GroupLayout(panelPaid);
			gl_panelPaid.setHorizontalGroup(
				gl_panelPaid.createParallelGroup(Alignment.LEADING)
					.addGroup(Alignment.TRAILING, gl_panelPaid.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panelPaid.createParallelGroup(Alignment.LEADING)
							.addComponent(lblCash, GroupLayout.DEFAULT_SIZE, 82, Short.MAX_VALUE)
							.addComponent(lblDebitCredit, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(ComponentPlacement.RELATED)
						.addGroup(gl_panelPaid.createParallelGroup(Alignment.LEADING)
							.addComponent(displayByCash, GroupLayout.DEFAULT_SIZE, 66, Short.MAX_VALUE)
							.addComponent(displayByDebitCredit, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE))
						.addContainerGap())
			);
			gl_panelPaid.setVerticalGroup(
				gl_panelPaid.createParallelGroup(Alignment.LEADING)
					.addGroup(Alignment.TRAILING, gl_panelPaid.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panelPaid.createParallelGroup(Alignment.TRAILING)
							.addGroup(gl_panelPaid.createSequentialGroup()
								.addComponent(displayByCash, GroupLayout.PREFERRED_SIZE, 27, GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(displayByDebitCredit, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
							.addGroup(gl_panelPaid.createSequentialGroup()
								.addComponent(lblCash, GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
								.addPreferredGap(ComponentPlacement.RELATED)
								.addComponent(lblDebitCredit, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)))
						.addGap(36))
			);
			panelPaid.setLayout(gl_panelPaid);
		}
		{
			JPanel panelChange = new JPanel();
			panelChange.setOpaque(false);
			panelChange.setForeground(new Color(0, 0, 0));
			panelChange.setBackground(new Color(135, 206, 235));
			panelChange.setBounds(228, 299, 145, 107);
			panelChange.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), Variables.BUNDLE.getString("MakePayment.panelChange.Title"), TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
			contentPanel.add(panelChange);
			displayChange.setForeground(new Color(255, 0, 0));
			
			displayChange.setHorizontalAlignment(SwingConstants.CENTER);
			displayChange.setFont(new Font("Tahoma", Font.BOLD, 20));
			GroupLayout gl_panelChange = new GroupLayout(panelChange);
			gl_panelChange.setHorizontalGroup(
				gl_panelChange.createParallelGroup(Alignment.LEADING)
					.addGroup(Alignment.TRAILING, gl_panelChange.createSequentialGroup()
						.addContainerGap()
						.addComponent(displayChange, GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE)
						.addContainerGap())
			);
			gl_panelChange.setVerticalGroup(
				gl_panelChange.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panelChange.createSequentialGroup()
						.addContainerGap()
						.addComponent(displayChange, GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
						.addContainerGap())
			);
			panelChange.setLayout(gl_panelChange);
		}
		{
			JButton btnComplete = new JButton(Variables.BUNDLE.getString("General.btnAccept"));
			btnComplete.setIcon(new ImageIcon("C:\\Users\\Miguel\\Documents\\Workspace Eclipse\\POS\\icons\\1445245812_tick_16.png"));
			btnComplete.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					//if amountDue = 0 then return to finish transaction in Sales screen
					
					if (MakePayment.this.amountDue.equals(BigDecimal.ZERO.setScale(2)) == true){
						Sales.isPaid = true;
						Sales.byCash = byCash;
						Sales.byDebitCredit = byDebitCredit;
						dispose();
					}
				}
			});
			btnComplete.setBounds(394, 355, 145, 51);
			btnComplete.setPreferredSize(new Dimension(130, 23));
			contentPanel.add(btnComplete);
		}
		{
			JButton btnCancel = new JButton(Variables.BUNDLE.getString("General.btnCancel"));
			btnCancel.setIcon(new ImageIcon("C:\\Users\\Miguel\\Documents\\Workspace Eclipse\\POS\\icons\\1445257605_DeleteRed.png"));
			btnCancel.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					dispose();
				}
			});
			btnCancel.setBounds(394, 293, 145, 51);
			btnCancel.setPreferredSize(new Dimension(130, 23));
			contentPanel.add(btnCancel);
		}
		{
			JPanel panelPayBy = new JPanel();
			panelPayBy.setOpaque(false);
			panelPayBy.setBackground(new Color(135, 206, 235));
			panelPayBy.setBounds(394, 11, 153, 224);
			panelPayBy.setBorder(new TitledBorder(null,Variables.BUNDLE.getString("MakePayment.panelPayBy.Title"), TitledBorder.LEADING, TitledBorder.TOP, null, null));
			contentPanel.add(panelPayBy);
			JButton btnDebitCredit = new JButton("");
			btnDebitCredit.setToolTipText("Debit or Credit Card");
			btnDebitCredit.setIcon(new ImageIcon("C:\\Users\\Miguel\\Documents\\Workspace Eclipse\\POS\\icons\\1445256304_credit-cards.png"));
			btnDebitCredit.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					isCash = false;
					pay();
					displayByDebitCredit.setText(byDebitCredit.toString()); //shows accumulated DebitCredit
				}
			});
			JButton btnCash = new JButton("");
			btnCash.setToolTipText("Cash");
			btnCash.setIcon(new ImageIcon("C:\\Users\\Miguel\\Documents\\Workspace Eclipse\\POS\\icons\\1445256288_checkout.png"));
			btnCash.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					isCash = true;
					pay();
					displayByCash.setText(byCash.toString());
				}
			});
			GroupLayout gl_panelPayBy = new GroupLayout(panelPayBy);
			gl_panelPayBy.setHorizontalGroup(
				gl_panelPayBy.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panelPayBy.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_panelPayBy.createParallelGroup(Alignment.TRAILING, false)
							.addComponent(btnDebitCredit, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(btnCash, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 123, Short.MAX_VALUE))
						.addContainerGap())
			);
			gl_panelPayBy.setVerticalGroup(
				gl_panelPayBy.createParallelGroup(Alignment.LEADING)
					.addGroup(gl_panelPayBy.createSequentialGroup()
						.addContainerGap()
						.addComponent(btnCash, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
						.addPreferredGap(ComponentPlacement.RELATED)
						.addComponent(btnDebitCredit, GroupLayout.PREFERRED_SIZE, 83, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(15, Short.MAX_VALUE))
			);
			panelPayBy.setLayout(gl_panelPayBy);
		}
	}
	
	private void pay(){
		
		BigDecimal change = BigDecimal.ZERO;
		amountReceived = new BigDecimal(txtAmountReceived.getText());
		amountDue = amountDue.subtract(amountReceived);
		
		// displays the change if amountDue becomes negative, this happens when payment is being received
		if (amountDue.compareTo(BigDecimal.ZERO) < 0){
			change = amountDue.abs();
			amountDue = BigDecimal.ZERO.setScale(2);
			//displayAmountDue.setText(amountDue.toString());	
			displayChange.setText(change.toString());
		}
		displayAmountDue.setText(amountDue.toString());
		
		if (isCash == true){
			byCash = byCash.add(amountReceived).subtract(change);
			displayByCash.setText(byCash.toString());
		}else{
			//byDebitCredit assumes the amountReceived as the payment made by C.C (no change considered)
			byDebitCredit = byDebitCredit.add(amountReceived);
			displayByDebitCredit.setText(byDebitCredit.toString());
		}
		amountReceived = BigDecimal.ZERO.setScale(2);
		txtAmountReceived.setText(amountReceived.toString());
	}
}
