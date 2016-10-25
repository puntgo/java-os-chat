import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.net.*;
import java.io.*;
import javax.swing.tree.*;
import java.util.*;

//* Main Class Begins Here

class ChatClient
{
	public static void main(String args[])
	{
		PasswordFrame s;
		if(args.length > 0)
		{
			s = new PasswordFrame(args[0]);
		}
		else
		{
			s = new PasswordFrame("127.0.0.1");
		}
		s.show();
	}
}


class PasswordFrame extends JFrame implements ActionListener
{
	JPanel passwordPanel,statusPanel;
	Container passwordContainer;
	JLabel lblStatus;
	JLabel lblEmail,lblPassword,lblServer;
	JTextField txtEmail,txtServer;
	JPasswordField txtPassword;
	JButton btnNew,btnLogin;
	MySocket s;

	Toolkit kit = Toolkit.getDefaultToolkit();
	Dimension screenSize = kit.getScreenSize();
	int screenHeight = screenSize.height;
	int screenWidth = screenSize.width;
	Image icon = kit.getImage("../Images/Icon.gif");

	PasswordFrame(String IP)
	{
		this.setTitle("Client");
		this.setSize(230,250);
		this.setIconImage(icon);
		this.setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		passwordContainer = getContentPane();
		this.getContentPane().setLayout(new BorderLayout());
		passwordPanel = new JPanel();
		passwordContainer.add(passwordPanel,BorderLayout.CENTER);
		statusPanel = new JPanel();
		passwordContainer.add(statusPanel,BorderLayout.SOUTH);
		passwordPanel();
		statusPanel();
		show();
		setStatusText("Enter your id, password & click Login");
		s = new MySocket(this,IP);
	}

	public void passwordPanel()
	{
		lblEmail = new JLabel(" E-mail ID  ");
		lblPassword = new JLabel("Password");
		txtEmail = new JTextField(13);
		txtPassword = new JPasswordField(13);
		btnLogin = new JButton(" Login ");
		btnNew = new JButton("  New  ");
		passwordPanel.add(new JLabel("                                                         "));
		passwordPanel.add(new JLabel("                                                         "));
		passwordPanel.add(lblEmail);
		passwordPanel.add(txtEmail);
		passwordPanel.add(new JLabel("                                                         "));
		passwordPanel.add(lblPassword);
		passwordPanel.add(txtPassword);
		passwordPanel.add(new JLabel("                                                         "));
		passwordPanel.add(new JLabel("                                                         "));
		passwordPanel.add(btnNew);
		passwordPanel.add(btnLogin);
		btnNew.addActionListener(this);
		btnLogin.addActionListener(this);
	}

	public void statusPanel()
	{
		lblStatus = new JLabel("Welcome to Ameya's chat client.",JLabel.LEFT);
		statusPanel.add(lblStatus);
	}

	public void setStatusText(String str)
	{
		for(int i=str.length();i<40;i++)
			str += " ";
		lblStatus.setText(str);
	}

	public void actionPerformed(ActionEvent e)
	{
		JButton btn = (JButton) e.getSource();
		if(btn.equals(btnLogin))
		{
			txtEmail.setEnabled(false);
			txtPassword.setEnabled(false);
			btnNew.setEnabled(false);
			btnLogin.setEnabled(false);
			setStatusText("Verifying Password...");
			String str = "LIN"+(char)27+txtEmail.getText().trim()+(char)27+txtPassword.getText().trim();
			s.Write(str);
			str = s.Read();
			StringTokenizer st = new StringTokenizer(str,""+(char)27);
			String response = st.nextToken();
			response = response.trim().toUpperCase();
			if(response.equals("OK"))
			{
				this.setVisible(false);
				String contacts[] = new String[st.countTokens()];
				int i=0;
				while(st.hasMoreTokens())
					contacts[i++] = st.nextToken();
				new ClientFrame(s,contacts,txtEmail.getText());
			}
			else
			{
				new MessageBox(this,"Login Failed","Wrong Email-ID or password. Please relogin.",1);
				txtEmail.setEnabled(true);
				txtPassword.setEnabled(true);
				btnNew.setEnabled(true);
				btnLogin.setEnabled(true);
			}
		}
		if(btn.equals(btnNew))
		{
			txtEmail.setEnabled(false);
			txtPassword.setEnabled(false);
			btnNew.setEnabled(false);
			btnLogin.setEnabled(false);
			setStatusText("Fill the entries and click OK...");
			new NewEntry(this,s);
			txtEmail.setEnabled(true);
			txtPassword.setEnabled(true);
			btnNew.setEnabled(true);
			btnLogin.setEnabled(true);
			setStatusText("Added new user...");
		}
	}
}

class ClientFrame extends JFrame implements ActionListener
{
	JPanel toolPanel,statusPanel,clientPanel;
	JScrollPane scpContact;
	JTree treContact;
	JButton btnAdd,btnDelete,btnEdit,btnProperty,btnChat,btnRefresh,btnSendFile,btnLogout;
	JLabel lblStatus;
	MySocket s;
	String contacts[],UserEmail;
	MyTree tree;


	Toolkit kit = Toolkit.getDefaultToolkit();
	Dimension screenSize = kit.getScreenSize();
	int screenHeight = screenSize.height;
	int screenWidth = screenSize.width;
	Image icon = kit.getImage("../Images/Icon.gif");

	ClientFrame(MySocket ms,String contacts[],String Email)
	{
		s = ms;
		s.jf=this;
		this.contacts = contacts;
		UserEmail = Email.trim();
		this.setTitle(UserEmail+" -Client");
		this.setSize(230,500);
		this.setIconImage(icon);
		this.setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Container clientContainer = getContentPane();
		this.getContentPane().setLayout(new BorderLayout());
		statusPanel = new JPanel();
		toolPanel = new JPanel();
		clientPanel = new JPanel();
		clientContainer.add(toolPanel,BorderLayout.NORTH);
		toolPanel();
		clientContainer.add(clientPanel,BorderLayout.CENTER);
		clientPanel();
		clientContainer.add(statusPanel,BorderLayout.SOUTH);
		statusPanel();
		show();
		new ChatListener();
	}


	public void toolPanel()
	{
		toolPanel.setLayout(new BorderLayout());
		btnAdd = new JButton(new ImageIcon("../images/Add.gif"));
		btnDelete = new JButton(new ImageIcon("../images/Delete.gif"));
		btnEdit = new JButton(new ImageIcon("../images/Edit.gif"));
		btnProperty = new JButton(new ImageIcon("../images/Property.gif"));
		btnChat = new JButton(new ImageIcon("../images/Chat.gif"));
		btnRefresh = new JButton(new ImageIcon("../images/Refresh.gif"));
		btnSendFile = new JButton(new ImageIcon("../images/SendFile.gif"));
		btnLogout = new JButton(new ImageIcon("../images/Logout.gif"));
		JPanel toolPanel1 = new JPanel();
		JPanel toolPanel2 = new JPanel();
		toolPanel1.add(btnAdd);
		toolPanel1.add(btnDelete);
		toolPanel1.add(btnEdit);
		toolPanel1.add(btnProperty);
		toolPanel2.add(btnChat);
		toolPanel2.add(btnRefresh);
		toolPanel2.add(btnSendFile);
		toolPanel2.add(btnLogout);
		toolPanel.add(toolPanel1,BorderLayout.NORTH);
		toolPanel.add(toolPanel2,BorderLayout.CENTER);
		btnAdd.addActionListener(this);
		btnDelete.addActionListener(this);
		btnEdit.addActionListener(this);
		btnProperty.addActionListener(this);
		btnChat.addActionListener(this);
		btnRefresh.addActionListener(this);
		btnSendFile.addActionListener(this);
		btnLogout.addActionListener(this);
	}


	public void clientPanel()
	{
		tree = new MyTree();
		for(int i=0;i<contacts.length;i++)
			tree.addObject(null,contacts[i]);
		clientPanel.add(tree);
	}


	public void statusPanel()
	{
		statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		lblStatus = new JLabel("Connected",JLabel.LEFT);
		setStatusText("Connected...");
		statusPanel.add(lblStatus);
	}


	public void setStatusText(String str)
	{
		for(int i=str.length();i<40;i++)
			str += " ";
		lblStatus.setText(str);
	}

	public void actionPerformed(ActionEvent e)
	{
		JButton btn = (JButton) e.getSource();
		if(btn.equals(btnAdd))
		{
			new NewContact(this,s);
			RefreshTree();
		}
		if(btn.equals(btnDelete))
		{
			MessageBox mb = new MessageBox(this,"Delete Contact","Are you sure you wanna delete \""+tree.selectedTreeItem+"\" from your contacts?",3);
			if(mb.Answer != 2)
				return;
			String str = "DELC"+(char)27+tree.selectedTreeItem;
			s.Write(str);
			String feedback = s.Read();
			StringTokenizer st = new StringTokenizer(feedback,""+(char)27);
			String str1 = st.nextToken();
			if(str1.equals("OK"))
			{
				new MessageBox(this,"Contact Deleted","\""+tree.selectedTreeItem+"\" deleted from your contacts list.",1);
			}
			if(str1.equals("ERR"))
			{
				String str2 = st.nextToken();
				if(str2.equals("1"))
				{
					new MessageBox(this,"Delete Contact","\""+tree.selectedTreeItem+"\" was not found in your contacts list.",1);
				}
			}
			RefreshTree();
		}
		if(btn.equals(btnEdit))
		{
			if(! tree.selectedTreeItem.equals(""))
				new EditContact(this,s,tree.selectedTreeItem);
		}
		if(btn.equals(btnProperty))
		{
			if(tree.selectedTreeItem.equals(""))
				return;
			s.Write("PROC"+(char)27+tree.selectedTreeItem);
			String feedback = s.Read();
			StringTokenizer st = new StringTokenizer(feedback,""+(char)27);
			if(st.nextToken().equals("OK"))
			{
				String Name = st.nextToken();
				String NickName = st.nextToken();
				String Email = st.nextToken();
				String Address = st.nextToken();
				String Phone = st.nextToken();
				new Property(this,Name,NickName,Email,Address,Phone);
			}
			else
				new MessageBox(this,"Property","\""+tree.selectedTreeItem+"\""+" not found in contact list.",1);
		}
		if(btn.equals(btnChat))
		{
			if(tree.selectedTreeItem.equals(""))
				return;
			s.Write("CHAT"+(char)27+tree.selectedTreeItem);
			String feedback = s.Read();
			StringTokenizer st = new StringTokenizer(feedback,""+(char)27);
			if(st.nextToken().equals("OK"))
			{
				String UserIP = st.nextToken();
				new ChatFrame(UserIP,tree.selectedTreeItem,UserEmail);
			}
			else
			{
				if(st.nextToken().equals("1"))
					new MessageBox(this,"Chat Error","The specified user seems to be offline. Unable to connect.",1);
				else
					new MessageBox(this,"Chat Error","An unspecified error has occured. Unable to connect.",1);
			}
		}
		if(btn.equals(btnRefresh))
		{
			String data = "REF"+(char)27+UserEmail;
			s.Write(data);
			String feedback = s.Read();
			StringTokenizer st = new StringTokenizer(feedback,(char)27+"");
			st.nextToken();
			tree.clear();
			while(st.hasMoreTokens())
			{
				tree.addObject(null,st.nextToken());
			}
		}
		if(btn.equals(btnSendFile))
		{
			if(tree.selectedTreeItem.equals(""))
				return;
			s.Write("CHAT"+(char)27+tree.selectedTreeItem);
			String feedback = s.Read();
			StringTokenizer st = new StringTokenizer(feedback,""+(char)27);
			if(st.nextToken().equals("OK"))
			{
				String UserIP = st.nextToken();
				FileDialog fd = new FileDialog(this,"Select file to send",FileDialog.LOAD);
				fd.setVisible(true);
				if(fd.getFile().equals(""))
					return;
				else
					new SendFile(fd.getDirectory()+fd.getFile(),UserEmail,UserIP);
			}
			else
			{
				if(st.nextToken().equals("1"))
					new MessageBox(this,"Send Error","The specified user seems to be offline. Unable to connect.",1);
				else
					new MessageBox(this,"Send Error","An unspecified error has occured. Unable to connect.",1);
			}
		}
		if(btn.equals(btnLogout))
		{
			MessageBox mb = new MessageBox(this,"Logout","Are you sure you wanna logout?",3);
			if(mb.Answer != 2)
				return;
			s.Write("LOUT"+(char)27);
			System.exit(0);
		}
	}

	public void RefreshTree()
	{
		String data = "REF"+(char)27+UserEmail;
		s.Write(data);
		String feedback = s.Read();
		StringTokenizer st = new StringTokenizer(feedback,(char)27+"");
		st.nextToken();
		tree.clear();
		while(st.hasMoreTokens())
		{
			tree.addObject(null,st.nextToken());
		}
	}
}

class MySocket implements Runnable
{
	Socket s;
	InputStream sin;
	OutputStream sout;
	Thread t;
	String crlf =  (char)13 + "" +(char)10;
	String REMOTE_HOST = "127.0.0.1";
	String buff = new String("");
	JFrame jf;

	MySocket(JFrame j,String IP)
	{
		try
		{
			jf = j;
			REMOTE_HOST = IP;
			s = new Socket(REMOTE_HOST,1000);
			sin = s.getInputStream();
			sout = s.getOutputStream();
		}
		catch(IOException e)
		{
			new MessageBox(jf,"Error connecting","Unable to connect to server. Please restart the application and try again.",1);
		}
		catch(Exception e)
		{
			new MessageBox(jf,"Error","Connection to server lost. Please restart the application and try again.",1);
		}
		t = new Thread(this);
		t.start();
	}

	public void Write(String str)
	{
		try
		{
			sout.write( (str+(char)8).getBytes() );
		}
		catch(IOException e)
		{
			new MessageBox(jf,"Error","Connection to server lost. Please restart the application and try again.",1);
		}
		catch(Exception e)
		{
			new MessageBox(jf,"Error","Connection to server lost. Please restart the application and try again.",1);
		}
	}

	public String Read()
	{
		try
		{
			while(buff.equals(""))
				Thread.sleep(1000);
		}
		catch(InterruptedException e)
		{
			new MessageBox(jf,"Error","Connection to server lost. Please restart the application and try again.",1);
		}
		catch(Exception e)
		{
			new MessageBox(jf,"Error","Connection to server lost. Please restart the application and try again.",1);
		}
		String str = buff;
		buff = "";
		return str;
	}

	public void run()
	{
		try
		{
			while(true)
			{
				int ch;
				while( (ch = sin.read()) != 8)
				{
					buff = buff + (char)ch;
					System.out.print((char)ch);
				}
			}
		}
		catch(IOException e)
		{
			new MessageBox(jf,"Error","Connection to server lost. Please restart the application and try again.",1);
		}
		catch(Exception e)
		{
			new MessageBox(jf,"Error","Connection to server lost. Please restart the application and try again.",1);
		}
	}
}



class MyTree extends JPanel implements TreeSelectionListener
{
	protected DefaultMutableTreeNode rootNode;
	protected DefaultTreeModel treeModel;
	protected JTree tree;
	private Toolkit toolkit = Toolkit.getDefaultToolkit();
	public String selectedTreeItem = "";

	public MyTree()
	{
		super(new GridLayout(1,0));

		rootNode = new DefaultMutableTreeNode("    < Contacts >                              ");
		treeModel = new DefaultTreeModel(rootNode);

		tree = new JTree(treeModel);
		tree.getSelectionModel().setSelectionMode
			(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.setShowsRootHandles(true);

		JScrollPane scrollPane = new JScrollPane(tree);
		add(scrollPane);
		tree.addTreeSelectionListener(this);
	}

	/** Remove all nodes except the root node. */
	public void clear()
	{
		rootNode.removeAllChildren();
		treeModel.reload();
	}

    /** Remove the currently selected node. */
	public void removeCurrentNode()
	{
		TreePath currentSelection = tree.getSelectionPath();
		if (currentSelection != null)
		{
			DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) (currentSelection.getLastPathComponent());
			MutableTreeNode parent = (MutableTreeNode)(currentNode.getParent());
			if (parent != null)
			{
				treeModel.removeNodeFromParent(currentNode);
				return;
			}
		}
		toolkit.beep();
	}

    /** Add child to the currently selected node. */
	public DefaultMutableTreeNode addObject(Object child)
	{
		DefaultMutableTreeNode parentNode = null;
		TreePath parentPath = tree.getSelectionPath();

		if (parentPath == null)
		{
			parentNode = rootNode;
		}
		else
		{
			parentNode = (DefaultMutableTreeNode) parentPath.getLastPathComponent();
		}

		return addObject(parentNode, child, true);
	}

	public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,Object child)
	{
		return addObject(parent, child, false);
	}

	public DefaultMutableTreeNode addObject(DefaultMutableTreeNode parent,Object child,boolean shouldBeVisible)
	{
		DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
		if (parent == null)
		{
			parent = rootNode;
		}
		treeModel.insertNodeInto(childNode, parent,parent.getChildCount());
		if (shouldBeVisible)
		{
			tree.scrollPathToVisible(new TreePath(childNode.getPath()));
		}
		return childNode;
	}

	public void valueChanged(TreeSelectionEvent e)
	{
		TreePath tp = e.getPath();
		String addr = tp.toString();
		int pos = addr.lastIndexOf(",")+1;
		if(pos>1)
			addr = addr.substring(pos,addr.length()-1).trim();
		else
			addr = "";
		selectedTreeItem = addr;
	}
}
