import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.table.*;

class ClientList extends JFrame implements ActionListener
{
	JPanel tablePanel,buttonPanel;
	MyTable clientTable;
	JButton btnDisconnect,btnProperty,btnRefresh;

	Client head;

	ClientList()
	{
		this.setTitle("Server");
		this.setSize(400,300);
		head = new Client("","","","",null);
		Container serverPane = getContentPane();
		tablePanel = new JPanel();
		buttonPanel = new JPanel();
		serverPane.setLayout(new BorderLayout());
		serverPane.add(tablePanel,BorderLayout.CENTER);
		serverPane.add(buttonPanel,BorderLayout.NORTH);
		tablePanel();
		buttonPanel();
		setSize(650,350);
		show();
	}

	public void tablePanel()
	{
		clientTable = new MyTable();
		clientTable.table.setPreferredScrollableViewportSize(new Dimension(600,250));
		tablePanel.add(clientTable.sp);
	}

	public void buttonPanel()
	{
		btnDisconnect = new JButton("Disconnect");
		btnProperty = new JButton("Property");
		btnRefresh = new JButton("Refresh");
		buttonPanel.add(btnDisconnect);
		buttonPanel.add(btnProperty);
		buttonPanel.add(btnRefresh);
		btnDisconnect.addActionListener(this);
		btnProperty.addActionListener(this);
		btnRefresh.addActionListener(this);
	}

	public void Add(Client c)
	{
		Add(c.UserEmail, c.UserIP, c.UserPort, c.ChatPort,new Date().toString(),c.SocketThread);
	}

	public void Add(String UserEmail,String UserIP,String UserPort,String ChatPort,String LogonTime,MySocket m)
	{
		Client c = new Client(UserEmail,UserIP,UserPort,ChatPort,m);
		Client c1 = head;
		while(c1.next != null)
			c1 = c1.next;
		c1.next = c;
		Vector v = new Vector();
		v.add(UserEmail);
		v.add(UserIP+":"+UserPort);
		v.add(ChatPort);
		v.add(" ");
		clientTable.add(UserEmail,UserIP+" : "+UserPort,ChatPort,LogonTime);
	}

	public boolean Delete(String UserEmail)
	{
		Client c2=head,c1 = head;
		int i=0;
		while(c1.next!=null)
		{
			c2 = c1;
			c1 = c1.next;
			if(c1.UserEmail.equals(UserEmail))
			{
				c2.next = c1.next;
				clientTable.remove(i);
				c1.SocketThread.terminate=true;
				return true;
			}
			i++;
		}
		return false;
	}

	public boolean Exists(String UserEmail)
	{
		Client c1 = head;
		while(c1.next != null)
		{
			c1 = c1.next;
			if(c1.UserEmail.equals(UserEmail))
				return true;
		}
		return false;
	}

	public String GetAllClients()
	{
		String str = "";
		Client c1 = head;
		while(c1.next != null)
		{
			c1 = c1.next;
			str = str + (char)27 + c1.UserEmail;
		}
		return str;
	}

	public String GetUserIP(String UserEmail)
	{
		String str = "";
		Client c1 = head;
		while(c1.next != null)
		{
			c1 = c1.next;
			if(c1.UserEmail.equals(UserEmail))
				return c1.UserIP;
		}
		return str;
	}

	public String GetUserPort(String UserEmail)
	{
		String str = "";
		Client c1 = head;
		while(c1.next != null)
		{
			c1 = c1.next;
			if(c1.UserEmail.equals(UserEmail))
				return c1.UserPort;
		}
		return str;
	}

	public void actionPerformed(ActionEvent e)
	{
		JButton btn = (JButton)e.getSource();
		if(btn.equals(btnDisconnect))
		{
			try
			{
				int row = clientTable.table.getSelectedRow(),i=0;
				Client c1 = head.next;
				while(i != row)
				{
					if(c1.next == null)
						break;
					i++;
					c1 = c1.next;
				}
				if( (new MessageBox(this,"Disconnect","Disconnect '"+c1.UserEmail+"' ?",3)).Answer == 2)
				{
					Delete(c1.UserEmail);
					System.out.println("\nDisconnecting "+c1);
				}
			}
			catch(Exception exc)
			{
				new MessageBox(this,"Error","Either no selection or some error occured.",1);
			}
		}
		if(btn.equals(btnProperty))
		{
			try
			{
				int row = clientTable.table.getSelectedRow(),i=0;
				Client c1 = head.next;
				while(i != row)
				{
					if(c1.next == null)
						break;
					i++;
					c1 = c1.next;
				}
				new MessageBox(this,"Property",c1.UserEmail+" @ "+c1.UserIP+":"+c1.UserPort+" / "+c1.ChatPort,1);
			}
			catch(Exception exc)
			{
				new MessageBox(this,"Error","Either no selection or some error occured.",1);
			}
		}
	}
}



class Client
{
	String UserEmail;
	String UserIP;
	String UserPort;
	String ChatPort;
	MySocket SocketThread;
	Client next;

	Client(String ue,String uip,String up,String cp,MySocket m)
	{
		UserEmail = ue;
		UserIP = uip;
		UserPort = up;
		ChatPort = cp;
		SocketThread = m;
		next = null;
	}

	private Client()
	{
	}
}


class MyTable extends DefaultTableCellRenderer
{
	public Object[][] data;
	private String[] columnNames;
	public JTable table;
	public JScrollPane sp;
	public DefaultTableModel model;

	MyTable()
	{
		columnNames = new String[]{"Email ID","Client IP","Server Port","Logon Time"};
		data = null;
		model = new DefaultTableModel(data, columnNames)
		{
			public boolean isCellEditable(int rowIndex, int columnIndex)
			{
				return false;
			}
		};

	   	table = new JTable(model)
	   	{
	        public Component prepareRenderer(TableCellRenderer renderer,int rowIndex, int vColIndex)
	        {
				Component c = super.prepareRenderer(renderer, rowIndex, vColIndex);
				return c;
			}
		};
		sp = new JScrollPane(table);
		sp.setSize(200,200);
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean isFocused, int row, int column)
	{
		Component component = super.getTableCellRendererComponent(table, value, isSelected, isFocused, row, column);
		return component;
	}

	public int getRowCount()
	{
		return data.length;
	}

	public int getColumnCount()
	{
		return columnNames.length;
	}

	public Object getValueAt(int row, int col)
	{
		return data[row][col];
	}

	public void setValueAt(Object value, int row, int col)
	{
		data[row][col] = value;
	}

	public String getColumnName(int columnIndex)
	{
		return columnNames[columnIndex];
	}

	public Class getColumnClass(int columnIndex)
	{
		return data[0][columnIndex].getClass();
	}

	public void add(String s1,String s2,String s3,String s4)
	{
		Object o[] = {s1,s2,s3,s4};
		model.addRow(o);
	}

	public void remove(int i)
	{
		model.removeRow(i);
	}

	public void remove()
	{
		model.removeRow(table.getSelectedRowCount());
	}

	public void removeAll()
	{
		int rows = table.getRowCount();
		for(int i=0;i<rows;i++)
			model.removeRow(i);
	}

}