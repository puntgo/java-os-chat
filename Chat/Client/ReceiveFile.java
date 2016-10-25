import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;

class ReceiveFile extends JFrame implements ActionListener,Runnable
{
	JPanel topPanel,centerPanel,bottomPanel;
	JLabel lblFileName,lblStatus,lblReceived;
	JTextField txtFileName;
	JButton btnCancel,btnReceive,btnBrowse;
	String fileName="/a.txt",UserEmail="";
	InputStream sin;
	OutputStream sout;
	Socket s;
	Thread t;

	ReceiveFile(Socket sckt,String ue)
	{
		s = sckt;
		UserEmail = ue;
		setResizable(false);
		setSize(225,150);
		setTitle(ue + " -Receive file");
		Container c = getContentPane();
		topPanel = new JPanel();
		centerPanel = new JPanel();
		bottomPanel = new JPanel();
		c.add(topPanel,BorderLayout.NORTH);
		c.add(centerPanel,BorderLayout.CENTER);
		c.add(bottomPanel,BorderLayout.SOUTH);
		topPanel();
		centerPanel();
		bottomPanel();
		show();
	}

	public void topPanel()
	{
		lblFileName = new JLabel("File");
		txtFileName = new JTextField(11);
		txtFileName.setText(fileName);
		txtFileName.setEnabled(false);
		btnBrowse = new JButton("...");
		topPanel.add(lblFileName);
		topPanel.add(txtFileName);
		topPanel.add(btnBrowse);
		btnBrowse.addActionListener(this);
	}

	public void centerPanel()
	{
		lblReceived = new JLabel("Clink Receive to start file transfer");
		btnCancel = new JButton("Cancel");
		btnReceive = new JButton("Receive");
		centerPanel.add(lblReceived);
		centerPanel.add(btnCancel);
		centerPanel.add(btnReceive);
		btnReceive.addActionListener(this);
		btnCancel.addActionListener(this);
	}

	public void bottomPanel()
	{
		lblStatus = new JLabel("");
		bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		bottomPanel.add(lblStatus);
	}


	public void actionPerformed(ActionEvent e)
	{
		try
		{
			sin = s.getInputStream();
			sout = s.getOutputStream();
			JButton btn = (JButton) e.getSource();
			if(btn.equals(btnBrowse))
			{
				FileDialog fd = new FileDialog(this,"Select Location to Save",FileDialog.SAVE);
				fd.setVisible(true);
				txtFileName.setText(fd.getDirectory()+fd.getFile());
			}
			if(btn.equals(btnReceive))
			{
				sout.write(("OK"+(char)27+(char)8).getBytes());
				t = new Thread(this);
				t.start();
			}
			if(btn.equals(btnCancel))
			{
				sout.write(("ERR"+(char)27+"1"+(char)8).getBytes());
				this.setVisible(false);
			}
		}
		catch(Exception exc)
		{
			System.out.println(exc);
		}
	}

	public void run()
	{
		try
		{
			String str; int ch;
			FileOutputStream fout = new FileOutputStream(txtFileName.getText());
			long pos = 0;
			while( (ch = sin.read()) != -1)
			{
				fout.write((char)ch);
				lblReceived.setText("   Bytes Received: " + pos++ +" bytes");
			}
			fout.close();
			System.out.println("Received");
			this.setVisible(false);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

}