import java.io.*;
import java.net.*;
import java.util.*;

class ChatListener implements Runnable
{
	ServerSocket ss;
	Socket s;
	InputStream sin;
	OutputStream sout;
	Thread t;
	Random rand = new Random();
	String crlf =  (char)13 + "" +(char)10;
	final int REMOTE_PORT = 20000;

	ChatListener()
	{
		try
		{
			ss = new ServerSocket(REMOTE_PORT);
			t = new Thread(this);
			t.start();
		}
		catch(IOException e)
		{
			System.out.println("IOException: " + e);
		}
	}

	public void run()
	{
		try
		{
			while(true)
			{
				s = ss.accept();
				//int port = 20000+rand.nextInt(1000);
				System.out.println("Request accepted from: "+s.getInetAddress()+":"+s.getPort());
				int ch;
				String str = "";
				while( (ch = s.getInputStream().read()) != 8)
				{
					str = str + (char)ch;
					System.out.print((char)ch);
				}
				StringTokenizer st = new StringTokenizer(str,""+(char)27);
				if(st.nextToken().equals("FILE"))
					new ReceiveFile(s,st.nextToken());
				else
					new ChatFrame(s,st.nextToken());
				//new ChatFrame(s);
				//System.out.println("Port no alloted: "+port);
				//s.getOutputStream().write((port+"c").getBytes());
				//ss.close();
				//ss = new ServerSocket(REMOTE_PORT);
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	}
}
