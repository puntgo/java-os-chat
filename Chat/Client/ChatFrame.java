import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

class ChatFrame extends JFrame implements ActionListener,Runnable
{
	JPanel clientPanel;
	JScrollPane scpSent,scpSend;
	JTextArea txaSent,txaSend;
	JButton btnSend;
	//ServerSocket ss;
	Socket s;
	InputStream sin;
	OutputStream sout;
	Thread t;
	String crlf =  (char)13 + "" +(char)10;
	boolean isListener;
	final int REMOTE_PORT = 20000;
	String ChatTo;

	Toolkit kit = Toolkit.getDefaultToolkit();
	Dimension screenSize = kit.getScreenSize();
	int screenHeight = screenSize.height;
	int screenWidth = screenSize.width;
	Image icon = kit.getImage("../Images/Icon.gif");

	ChatFrame(Socket sckt,String UserEmail)
	{
		s = sckt;
		this.setTitle("Chat- " + UserEmail);
		this.setSize(375,300);
		this.setIconImage(icon);
		this.setResizable(false);
		Container clientContainer = getContentPane();
		clientPanel = new JPanel();
		clientContainer.add(clientPanel);
		clientPanel();
		show();
		isListener = true;
		/*try
		{
			ss = new ServerSocket(UserPort);*/
			t = new Thread(this);
			t.start();
		/*}
		catch(IOException e)
		{
			System.out.println("IOException: " + e);
		}*/
	}

	ChatFrame(String UserIP,String chatter,String UserEmail)
	{
		ChatTo = chatter;
		this.setTitle("Chat <"+ChatTo+">");
		this.setSize(375,300);
		this.setIconImage(icon);
		Container clientContainer = getContentPane();
		clientPanel = new JPanel();
		clientContainer.add(clientPanel);
		clientPanel();
		isListener = false;
		show();
		try
		{
			s = new Socket(UserIP,REMOTE_PORT);
			sin = s.getInputStream();
			sout = s.getOutputStream();
			sout.write(("CHAT"+(char)27+UserEmail+(char)8).getBytes());
			System.out.println("Connected to: "+s.getInetAddress()+":"+s.getPort());
			int ch=0;
			String str = "";
			/*while( (ch = sin.read()) != 'c')
			{
				str = str + (char)ch;
				System.out.print((char)ch);
			}
			int port = Integer.parseInt(str);
			System.out.println("Connecting to "+port);
			s.close();
			s = null;
			s = new Socket(UserIP,port);*/
			//s.getOutputStream().write(("Ameya Khasgiwala"+(char)8).getBytes());
			sin = s.getInputStream();
			sout = s.getOutputStream();
			sout.write(UserEmail.getBytes());
		}
		catch(IOException e)
		{
			System.out.println("IOException: " + e);
		}
		t = new Thread(this);
		t.start();
	}

	public void clientPanel()
	{
		txaSent = new JTextArea(11,32);
		txaSend = new JTextArea(4,25);
		btnSend = new JButton("  Send  ");
		scpSent = new JScrollPane(txaSent);
		scpSend = new JScrollPane(txaSend);
		clientPanel.add(scpSent);
		clientPanel.add(scpSend);
		clientPanel.add(btnSend);
		btnSend.addActionListener(this);
		txaSent.setEnabled(false);
	}

	public void actionPerformed(ActionEvent e)
	{
		String str = e.getActionCommand();
		str = str.trim();
		if(str.equals("Send"))
		{
			txaSent.setText(txaSent.getText() + txaSend.getText() + crlf + crlf);
			try
			{
				sout.write((txaSend.getText() + (char)8).getBytes());
			}
			catch(IOException exc)
			{
				System.out.println(exc);
			}
			txaSend.setText("");
		}
	}

	public void run()
	{
		try
		{
			/*if(isListener)
			{
				s = ss.accept();
				show();
			}*/
			sin = s.getInputStream();
			sout = s.getOutputStream();
			while(true)
			{
				Thread.sleep(1000);
				int ch;
				while( (ch = sin.read()) != 8)
				{
					txaSent.setText(txaSent.getText() + (char)ch);
					System.out.print((char)ch);
				}
				txaSent.setText(txaSent.getText() + crlf + crlf);
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
			new MessageBox(this,"Chat",e.toString(),1);
			this.setVisible(false);
		}
	}
}
