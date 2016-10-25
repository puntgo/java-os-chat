import java.io.*;
import java.util.*;

class Contacts
{
	Person head;
	String UserEmail;

	Contacts(String UserName)
	{
		head = new Person();
		UserEmail = UserName;
		try
		{
			FileReader fin = new FileReader("Data/"+UserEmail+"/contact.txt");
			BufferedReader br = new BufferedReader(fin);
			Person p = head;
			String s;
			while((s = br.readLine()) != null)
			{
				StringTokenizer st = new StringTokenizer(s,""+(char)27);
				Person p1 = new Person();
				p1.Name = st.nextToken();
				p1.NickName = st.nextToken();
				p1.Email = st.nextToken();
				p1.Address = st.nextToken();
				p1.Phone = st.nextToken();
				Add(p1);
				p = p.next;
			}
		}
		catch(Exception exc)
		{
			System.out.println("Line 33\n"+exc);
		}
	}


	public void Add(Person p)
	{
		Add(p.Name, p.NickName, p.Email, p.Address, p.Phone);
	}

	public void Add(String Name,String NickName,String Email,String Address,String Phone)
	{
		Person p = new Person(Name,NickName,Email,Address,Phone);
		Person p1 = head;
		while(p1.next != null)
			p1 = p1.next;
		p1.next = p;
	}

	public boolean Delete(String Email)
	{
		Person p2=head,p1 = head;
		while(p1.next!=null)
		{
			p2 = p1;
			p1 = p1.next;
			if(p1.Email.equals(Email))
			{
				p2.next = p1.next;
				return true;
			}
		}
		return false;
	}

	public boolean Modify(String Name,String NickName,String Email,String Address,String Phone)
	{
		Person p1 = head;
		while(p1.next!=null)
		{
			p1 = p1.next;
			if(p1.Email.equals(Email))
			{
				p1.Name = Name;
				p1.NickName = NickName;
				p1.Address = Address;
				p1.Phone = Phone;
				return true;
			}
		}
		return false;
	}

	public boolean Exists(String Email)
	{
		Person p1 = head;
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
			FileOutputStream fout = new FileOutputStream("Data/"+UserEmail+"/contact.txt");
			Person p = head;
			p = p.next;
			while(p != null)
			{
				String s = p.Name+(char)27+p.NickName+(char)27+p.Email+(char)27+p.Address+(char)27+p.Phone+(char)27;
				fout.write((s+"\n").getBytes());
				p = p.next;
			}
		}
		catch(Exception exc)
		{
			System.out.println("Line 426\n"+exc);
		}
	}

	public String GetAllContacts()
	{
		String str = "";
		Person p1 = head;
		while(p1.next != null)
		{
			p1 = p1.next;
			str = str + (char)27 + p1.Email;
		}
		return str;
	}

	public String GetProperty(String Email)
	{
		String str = "";
		Person p1 = head;
		while(p1.next != null)
		{
			p1 = p1.next;
			if(p1.Email.equals(Email))
				str = str + (char)27 + p1.Name + (char)27 + p1.NickName + (char)27 + p1.Email + (char)27 + p1.Address + (char)27 + p1.Phone;
		}
		return str;
	}
}


class Person implements Serializable
{
	String Name, NickName, Email, Address, Phone;
	Person next = null;

	Person()
	{
		Name = "";
		NickName = "";
		Email = "";
		Address = "";
		Phone = "";
	}

	Person(String n,String nn,String e,String a,String p)
	{
		Name = n;
		NickName = nn;
		Email = e;
		Address = a;
		Phone = p;
	}

	Person(String str,String ch)
	{
		StringTokenizer st = new StringTokenizer(str,ch);
		Name = st.nextToken();
		NickName = st.nextToken();
		Email = st.nextToken();
		Address = st.nextToken();
		Phone = st.nextToken();
	}
}
