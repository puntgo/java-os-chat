import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;


class NewContact extends Dialog implements ActionListener
{
	JFrame jf;
	JPanel newPanel;
	JLabel lblName,lblNickName,lblAddress,lblEmail,lblPhone;
	JTextField txtName,txtNickName,txtAddress,txtEmail,txtPhone;
	JButton btnOK,btnCancel;
	MySocket s;


	NewContact(JFrame j,MySocket ms)
	{
		super(j,"Add New Contact",true);
		jf=j;
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
		lblPhone = new JLabel("      Phone");
		txtName = new JTextField(15);
		txtNickName = new JTextField(15);
		txtAddress = new JTextField(15);
		txtEmail = new JTextField(15);
		txtPhone = new JTextField(15);
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
		newPanel.add(lblPhone);
		newPanel.add(txtPhone);
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
			String Name = txtName.getText().trim();
			String NickName = txtNickName.getText().trim();
			String Email = txtEmail.getText().trim();
			String Address = txtAddress.getText().trim();
			String Phone = txtPhone.getText().trim();
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
			if(Phone.equals(""))
			{
				new MessageBox(jf,"Missing Entry","Phone no cannot be empty. Please enter a valid Phone no.",1);
				return;
			}
			String str = "ADDC"+(char)27+Name+(char)27+NickName+(char)27+Email+(char)27+Address+(char)27+Phone;
			s.Write(str);
			str = s.Read();
			StringTokenizer st = new StringTokenizer(str,""+(char)27);
			String response = st.nextToken();
			response = response.trim().toUpperCase();
			if(response.equals("OK"))
			{
				new MessageBox(jf,"Contact added","Contact successfully added as: " + Email,1);
				this.setVisible(false);
			}
			if(response.equals("ERR"))
			{
				String errMessage = st.nextToken();
				if(errMessage.equals("1"))
				{
					new MessageBox(jf,"New Contact Failed","A contact with specified Email ID already exists in your contact list.",1);
				}
				else
				{
					if(errMessage.equals("2"))
						new MessageBox(jf,"New Contact Failed","A user with Email ID \""+Email+"\" does not exist.",1);
				}
			}
		}
		if(btn.equals(btnCancel))
		{
			this.setVisible(false);
		}
	}
}
