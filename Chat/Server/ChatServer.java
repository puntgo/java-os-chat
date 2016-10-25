import java.net.*;
import java.io.*;
import java.util.*;

//* Main Class Begins Here

class ChatServer
{
	public static void main(String args[]) throws MalformedURLException,IOException
	{
		Server s = new Server();
	}
}


class Server implements Runnable
{
	final int REMOTE_PORT = 1000;
	int port = 1000;
	ServerSocket ss;
	Socket s;
	Thread t;
	Random rand = new Random();
	ClientList clientList = new ClientList();

	Server()
	{
		try
		{
			ss = new ServerSocket(REMOTE_PORT);
			t = new Thread(this);
			t.start();
		}
		catch(IOException e)
		{
			System.out.println("41 IOException: " + e);
		}
	}

	public void run()
	{
		try
		{
			while(true)
			{
				s = ss.accept();
				System.out.println("Request accepted from: "+s.getInetAddress()+":"+s.getPort());
				new MySocket(s,clientList);
			}
		}
		catch(IOException e)
		{
			System.out.println("64 IOException: "+e);
		}
	}

}


class MySocket implements Runnable
{

	Thread t;
	Socket s;
	InputStream sin;
	OutputStream sout;
	String UserEmail;
	transient boolean terminate;
	ClientList clientList;
	int CHAT_PORT = 20000;

	MySocket() throws IOException
	{
		//super(1000);
		terminate = false;
		t = new Thread(this);
		t.start();
	}

	MySocket(int port,ClientList cl) throws IOException
	{
		//super(port);
		terminate = false;
		clientList = cl;
		t = new Thread(this);
		t.start();
	}

	MySocket(Socket sckt,ClientList cl) throws IOException
	{
		//super(port);
		s = sckt;
		terminate = false;
		clientList = cl;
		t = new Thread(this);
		t.start();
	}

	public void run()
	{
		try
		{
				//s = accept();
				sin = s.getInputStream();
				sout = s.getOutputStream();
				System.out.println("Accepted "+s.getInetAddress()+":"+s.getPort());
				while(true)
				{
					int ch;
					System.out.print("Message on "+s.getLocalPort()+" from: "+s.getInetAddress()+":"+s.getPort()+": ");
					String str = "";
					if(terminate)
					{
						break;
					}
					while( (ch = sin.read()) != 8)
					{
						if(terminate)
						{
							break;
						}
						System.out.print((char)ch);
						str = str + (char)ch;
					}
					if(terminate)
					{
						break;
					}
					parse(str);
					if(terminate)
					{
						break;
					}
				}
		}
		catch(IOException e)
		{
			terminate = true;
			clientList.Delete(UserEmail);
		}
	}

	public void parse(String str)
	{
		int pos = str.indexOf((char)27);
		String str1 = str.substring(0,pos);
		String str2 = str.substring(pos+1,str.length());
		str1 = str1.trim();
		str1 = str1.toUpperCase();
		if(str1.equals("LIN"))
		{
			Members m = new Members();
			StringTokenizer st = new StringTokenizer(str2,""+(char)27);
			String Email = st.nextToken();
			String Password = st.nextToken();
			if(m.Verify(Email,Password) == true)
			{
				Contacts c = new Contacts(Email);
				String feedback = "OK"+ c.GetAllContacts();
				Write(feedback);
				UserEmail = Email;
				String UserIP = s.getInetAddress().toString();
				UserIP = UserIP.substring(1,UserIP.length());
				System.out.println(UserIP);
				String UserPort = s.getPort()+"";
				String ServerPort = s.getLocalPort() + "";
				String LogonTime = new Date().toString();
				clientList.Add(UserEmail,UserIP,UserPort,ServerPort,LogonTime,this);
			}
			else
				Write("ERR");
		}
		if(str1.equals("ADD"))
		{
			Members m = new Members();
			StringTokenizer st = new StringTokenizer(str2,""+(char)27);
			String Name = st.nextToken();
			String NickName = st.nextToken();
			String Email = st.nextToken();
			String Address = st.nextToken();
			String Password = st.nextToken();
			if(m.Exists(Email))
				Write("ERR"+(char)27+"1");
			else
			{
				m.Add(Name,NickName,Email,Address,Password);
				m.Update();
				Write("OK");
			}
		}
		if(str1.equals("CHAT"))
		{
			StringTokenizer st = new StringTokenizer(str2,""+(char)27);
			String UserEmail = st.nextToken();
			if(clientList.Exists(UserEmail))
			{
				String strg = "OK"+(char)27+clientList.GetUserIP(UserEmail);
				Write(strg);
			}
			else
				Write("ERR"+(char)27+"1");
		}
		if(str1.equals("ADDC"))
		{
			Contacts c = new Contacts(UserEmail);
			StringTokenizer st = new StringTokenizer(str2,""+(char)27);
			String Name = st.nextToken();
			String NickName = st.nextToken();
			String Email = st.nextToken();
			String Address = st.nextToken();
			String Phone = st.nextToken();
			if(c.Exists(Email))
				Write("ERR"+(char)27+"1");
			else
			{
				Members m = new Members();
				if(m.Exists(Email))
				{
					c.Add(Name,NickName,Email,Address,Phone);
					c.Update();
					Write("OK");
				}
				else
					Write("ERR"+(char)27+"2");
			}
		}
		if(str1.equals("DELC"))
		{
			Contacts c = new Contacts(UserEmail);
			StringTokenizer st = new StringTokenizer(str2,(char)27+"");
			String Email = st.nextToken();
			if(c.Delete(Email))
				Write("OK");
			else
				Write("ERR"+(char)27+"1");
			c.Update();
		}
		if(str1.equals("EDTC"))
		{
			Contacts c = new Contacts(UserEmail);
			StringTokenizer st = new StringTokenizer(str2,""+(char)27);
			String Name = st.nextToken();
			String NickName = st.nextToken();
			String Email = st.nextToken();
			String Address = st.nextToken();
			String Phone = st.nextToken();
			Members m = new Members();
			if(m.Exists(Email))
			{
				if(c.Modify(Name,NickName,Email,Address,Phone))
				{
					c.Update();
					Write("OK");
				}
				else
					Write("ERR"+(char)27+"1");
			}
			else
				Write("ERR"+(char)27+"2");
		}
		if(str1.equals("PROC"))
		{
			Contacts c = new Contacts(UserEmail);
			StringTokenizer st = new StringTokenizer(str2,""+(char)27);
			String Email = st.nextToken();
			String prop = c.GetProperty(Email);
			if(! prop.equals(""))
			{
				Write("OK"+(char)27+prop);
			}
			else
				Write("ERR"+(char)27+"1");
		}
		if(str1.equals("REF"))
		{
			String Email = str2;
			Contacts c = new Contacts(Email);
			String feedback = "OK" + c.GetAllContacts();
			Write(feedback);
		}
		if(str1.equals("LOUT"))
		{
			terminate = true;
			clientList.Delete(UserEmail);
		}
	}

	public void add(String from)
	{
	}

	public void Write(String str)
	{
		try
		{
			sout.write( (str+(char)8).getBytes() );
		}
		catch(IOException e)
		{
			System.out.println(e);
		}
	}
}
