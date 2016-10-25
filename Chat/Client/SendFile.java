import java.awt.*;
import java.io.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import java.net.*;

class SendFile extends JFrame implements ActionListener,Runnable
{
	JPanel topPanel,centerPanel,bottomPanel;
	JLabel lblFileName,lblStatus;
	JTextField txtFileName;
	JButton btnCancel;
	JProgressBar pgbProgress;
	String fileName = "",UserEmail="";
	InputStream sin;
	OutputStream sout;
	Socket s;
	Thread t;

	SendFile(String file,String ue,String UserIP)
	{
		fileName = file;
		UserEmail = ue;
		setResizable(false);
		setSize(225,150);
		setTitle(UserEmail + " -Send");
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
		try
		{
			s = new Socket(UserIP,20000);
			sin = s.getInputStream();
			sout = s.getOutputStream();
			t = new Thread(this);
			t.start();
		}
		catch(IOException e)
		{
			System.out.println(e);
			lblStatus.setText(e.toString());
		}
		catch(Exception e)
		{
			System.out.println(e);
			lblStatus.setText(e.toString());
		}

	}

	public void topPanel()
	{
		lblFileName = new JLabel("Filename");
		txtFileName = new JTextField(13);
		txtFileName.setText(fileName);
		txtFileName.setEnabled(false);
		topPanel.add(lblFileName);
		topPanel.add(txtFileName);
	}

	public void centerPanel()
	{
		pgbProgress = new JProgressBar(0,100);
		btnCancel = new JButton("Cancel");
		centerPanel.add(pgbProgress);
		centerPanel.add(btnCancel);
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
	}

	public void run()
	{
		try
		{
			sout.write(("FILE"+(char)27+UserEmail+(char)8).getBytes());
			lblStatus.setText("Waiting for reply...");
			int ch;
			String str = "";
			while( (ch=sin.read()) != 8 )
			{
				str = str + (char)ch;
			}
			System.out.println("Received: "+str);
			StringTokenizer st = new StringTokenizer(str,""+(char)27);
			if(st.nextToken().equals("ERR"))
			{
				if(st.nextToken().equals("1"))
					new MessageBox(this,"Error Sending file","The client refused to accept the selected file",1);
				else
					new MessageBox(this,"Error Sending file","An unknown error occured. Unable to send file.",1);
				this.setVisible(false);
				return;
			}
			lblStatus.setText("Sending file...");
			FileInputStream fin = new FileInputStream(fileName);
			System.out.println("Sending...");
			long length = (new File(fileName)).length();
			long pos = 0;
			while( (ch = fin.read()) != -1 )
			{
				sout.write( ((char)ch+"").getBytes());
				pgbProgress.setValue((int)(100*pos/length));
				setTitle( (100*pos/length)+"% sent");
				pos++;
			}
			fin.close();
			s.close();
			System.out.println("Sent");
			this.setVisible(false);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}

}