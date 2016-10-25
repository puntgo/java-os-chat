import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


class NewEntry extends Dialog implements ActionListener
{
	JFrame jf;
	JPanel newPanel;
	JLabel lblName,lblNickName,lblAddress,lblEmail,lblPassword,lblConfirmPassword;
	JTextField txtName,txtNickName,txtAddress,txtEmail;
	JPasswordField txtPassword,txtConfirmPassword;
	JButton btnOK,btnCancel;
	MySocket s;


	NewEntry(JFrame f,MySocket ms)
	{
		super(f,"New User",true);
		jf=f;
		s = ms;
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
		lblPassword = new JLabel("  Password");
		lblConfirmPassword = new JLabel("Re-Password");
		txtName = new JTextField(15);
		txtNickName = new JTextField(15);
		txtAddress = new JTextField(15);
		txtEmail = new JTextField(15);
		txtPassword = new JPasswordField(15);
		txtConfirmPassword = new JPasswordField(15);
		btnOK = new JButton("  OK  ");
		btnCancel = new JButton("Cancel");
		newPanel.add(new JLabel("                                                                  "));
		newPanel.add(lblName);
		newPanel.add(txtName);
		newPanel.add(lblNickName);
		newPanel.add(txtNickName);
		newPanel.add(lblEmail);
		newPanel.add(txtEmail);
		newPanel.add(lblAddress);
		newPanel.add(txtAddress);
		newPanel.add(lblPassword);
		newPanel.add(txtPassword);
		newPanel.add(lblConfirmPassword);
		newPanel.add(txtConfirmPassword);
		newPanel.add(new JLabel("                                                                  "));
		newPanel.add(btnOK);
		newPanel.add(btnCancel);
		btnOK.addActionListener(this);
		btnCancel.addActionListener(this);
	}

	public void actionPerformed(ActionEvent e)
	{
		JButton btn = (JButton) e.getSource();
		if(btn.equals(btnOK))
		{
			if(! txtPassword.getText().equals(txtConfirmPassword.getText()) )
			{
				new MessageBox(jf,"Mismatched Passwords","Passwords entered were found mismatching.",1);
				return;
			}
			String Name = txtName.getText().trim();
			String NickName = txtNickName.getText().trim();
			String Email = txtEmail.getText().trim();
			String Address = txtAddress.getText().trim();
			String Password = txtPassword.getText().trim();
			if(Name.equals(""))
			{
				new MessageBox(jf,"Missing Entry","Name cannot be empty. Please enter a valid Name.",1);
				return;
			}
			if(NickName.equals(""))
			{
				new MessageBox(jf,"Missing Entry","Nick Name cannot be empty. Please enter a some Nick Name.",1);
				return;
			}
			if(Email.equals(""))
			{
				new MessageBox(jf,"Missing Entry","Email ID cannot be empty. Please enter a valid Email ID.",1);
				return;
			}
			if(Address.equals(""))
			{
				new MessageBox(jf,"Missing Entry","Address cannot be empty. Please enter your Address.",1);
				return;
			}
			if(Password.equals(""))
			{
				new MessageBox(jf,"Missing Entry","Password cannot be empty. Please enter a valid Password.",1);
				return;
			}
			String str = "ADD"+(char)27+Name+(char)27+NickName+(char)27+Email+(char)27+Address+(char)27+Password;
			s.Write(str);
			str = s.Read();
			StringTokenizer st = new StringTokenizer(str,""+(char)27);
			String response = st.nextToken();
			response = response.trim().toUpperCase();
			if(response.equals("OK"))
			{
				new MessageBox(jf,"New User","You are successfully added as: " + txtEmail.getText(),1);
				this.setVisible(false);
			}
			if(response.equals("ERR"))
			{
				if(st.nextToken().equals("1"))
					new MessageBox(jf,"New User Failed","A user with specified Email ID already exists. Please choose a different Email ID.",1);
				else
					new MessageBox(jf,"New User Failed","Error occured. Unable to add "+Email+" as a user.",1);
			}
		}
		if(btn.equals(btnCancel))
		{
			this.setVisible(false);
		}
	}

}
