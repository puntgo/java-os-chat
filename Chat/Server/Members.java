import java.io.*;
import java.util.*;

class Members
{
	User head;

	Members()
	{
		head = new User();
		try
		{
			FileReader fin = new FileReader("Data/Data.txt");
			BufferedReader br = new BufferedReader(fin);
			User p = head;
			String s;
			while((s = br.readLine()) != null)
			{
				StringTokenizer st = new StringTokenizer(s,""+(char)27);
				User p1 = new User();
				p1.Name = st.nextToken();
				p1.NickName = st.nextToken();
				p1.Email = st.nextToken();
				p1.Address = st.nextToken();
				p1.Password = st.nextToken();
				Add(p1);
				p = p.next;
			}
		}
		catch(Exception exc)
		{
			System.out.println("Line 33\n"+exc);
		}
	}


	public void Add(User p)
	{
		Add(p.Name, p.NickName, p.Email, p.Address, p.Password);
	}

	public void Add(String Name,String NickName,String Email,String Address,String Password)
	{
		User p = new User(Name,NickName,Email,Address,Password);
		User p1 = head;
		while(p1.next != null)
			p1 = p1.next;
		p1.next = p;
	}

	public void Delete(int i)
	{
		User p2=head,p1 = head;
		while( (p1.next!=null) && (i >= 0))
		{
			p2 = p1;
			p1 = p1.next;
			i--;
		}
		p2.next = p1.next;
	}

	public void Delete(String Email)
	{
		User p2=head,p1 = head;
		while(p1.next!=null)
		{
			p2 = p1;
			p1 = p1.next;
			if(p1.Email.equals(Email))
				break;
		}
		p2.next = p1.next;
	}

	public void Modify(String Name,String NickName,String Email,String Address,String Password,int i)
	{
		User p1 = head;
		while( (p1.next!=null) && (i >= 0))
		{
			p1 = p1.next;
			i--;
		}
		p1.Name = Name;
		p1.NickName = NickName;
		p1.Email = Email;
		p1.Address = Address;
		p1.Password = Password;
	}

	public boolean Verify(String Email,String Password)
	{
		User p1 = head;
		while(p1.next != null)
		{
			p1 = p1.next;
			if( (p1.Email.equals(Email)) && (p1.Password.equals(Password) ) )
				return true;
		}
		return false;
	}

	public boolean Exists(String Email)
	{
		User p1 = head;
		while(p1.next != null)
		{
			p1 = p1.next;
			if(p1.Email.equals(Email))
				return true;
		}
		return false;
	}

	public void Update()
	{
		try
		{
			FileOutputStream fout = new FileOutputStream("Data/Data.txt");
			User p = head;
			p = p.next;
			while(p != null)
			{
				String s = p.Name+(char)27+p.NickName+(char)27+p.Email+(char)27+p.Address+(char)27+p.Password+(char)27;
				fout.write((s+"\n").getBytes());
				p = p.next;
				File f = new File("Data/"+p.Email);
				if(!f.exists())
				{
					f.mkdir();
					FileOutputStream fileout = new FileOutputStream("Data/"+p.Email+"/contact.txt");
				}
			}
		}
		catch(Exception exc)
		{
			System.out.println("Line 426\n"+exc);
		}
	}
}


class User implements Serializable
{
	String Name;
	String NickName;
	String Email;
	String Address;
	String Password;
	User next = null;

	User()
	{
		Name = "";
		NickName = "";
		Email = "";
		Address = "";
		Password = "";
	}

	User(String n,String nn,String e,String a,String p)
	{
		Name = n;
		NickName = nn;
		Email = e;
		Address = a;
		Password = p;
	}

	User(String str,String ch)
	{
		StringTokenizer st = new StringTokenizer(str,ch);
		Name = st.nextToken();
		NickName = st.nextToken();
		Email = st.nextToken();
		Address = st.nextToken();
		Password = st.nextToken();
	}
}
