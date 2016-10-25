import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


class Property extends Dialog implements ActionListener
{
	JFrame jf;
	JPanel newPanel;
	JLabel lblName,lblNickName,lblAddress,lblEmail,lblPhone;
	JTextField txtName,txtNickName,txtAddress,txtEmail,txtPhone;
	JButton btnOK,btnCancel;
	String Name,NickName,Email,Address,Phone;


	Property(JFrame j,String n,String nn,String e,String add, String ph)
	{
		super(j,e+" -Property",true);
		jf = j;
		Name = n;
		NickName = nn;
		Email = e;
		Address = add;
		Phone = ph;
		this.setSize(275,275);
		newPanel = new JPanel();
		add(newPanel);
		newPanel();
		show();
	}

	public void newPanel()
	{
		lblName = new JLabel("    Full Name");
		lblNickName = new JLabel("  Nick Name");
		lblAddress = new JLabel("      Address");
		lblEmail = new JLabel("       Email ID");
		lblPhone = new JLabel("      Phone");
		txtName = new JTextField(15);
		txtNickName = new JTextField(15);
		txtAddress = new JTextField(15);
		txtEmail = new JTextField(15);
		txtPhone = new JTextField(15);
		btnOK = new JButton(" Save ");
		btnCancel = new JButton("Close");
		newPanel.add(new JLabel("                                                                  "));
		newPanel.add(lblName);
		newPanel.add(txtName);
		newPanel.add(lblNickName);
		newPanel.add(txtNickName);
		newPanel.add(lblEmail);
		newPanel.add(txtEmail);
		newPanel.add(lblAddress);
		newPanel.add(txtAddress);
		newPanel.add(lblPhone);
		newPanel.add(txtPhone);
		newPanel.add(new JLabel("                                                                  "));
		newPanel.add(btnOK);
		newPanel.add(btnCancel);
		btnOK.addActionListener(this);
		btnCancel.addActionListener(this);
		txtName.setText(Name);
		txtName.setEnabled(false);
		txtNickName.setText(NickName);
		txtNickName.setEnabled(false);
		txtEmail.setText(Email);
		txtEmail.setEnabled(false);
		txtAddress.setText(Address);
		txtAddress.setEnabled(false);
		txtPhone.setText(Phone);
		txtPhone.setEnabled(false);
	}

	public void actionPerformed(ActionEvent e)
	{
		JButton btn = (JButton) e.getSource();
		if(btn.equals(btnOK))
			this.setVisible(false);
		if(btn.equals(btnCancel))
			this.setVisible(false);
	}
}
