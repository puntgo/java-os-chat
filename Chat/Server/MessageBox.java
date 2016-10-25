import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


//-----------------< Classes for MessageBox >----------------//

//* Class for MessageBox which creates MessageBox


class MessageBox extends Dialog implements ActionListener
{
	String Message = new String("");
	String Title = new String("Ameya Khasgiwala");
	JButton btnOk,btnCancel,btnYes,btnNo;
	static int Answer;

	MessageBox(JFrame jf,String t,String m,int Option)
	{
		super(jf,t,true);
		Message = m;
		Title = t;
		MssgPanel();
		NewPanel(Option);
		Point p = jf.getLocation();
		setLocation((p.x+jf.getWidth()-150)/2,(p.y+jf.getHeight())/2);
		show();
	}

	public static int MessageBox(JFrame jf,String t,String m,int Option)
	{
		MessageBox mbx = new MessageBox(jf,t,m,Option);
		return mbx.Answer;
	}

	public void MssgPanel()
	{
		JPanel jp = new JPanel();
		add(jp,BorderLayout.NORTH);
		jp.add(new JLabel(Message));
	}

	public void NewPanel(int Option)
	{
		setResizable(false);
		setTitle(Title);
		if(Message.length() * 8 > 125)
			setSize(Message.length() * 8,125);
		else
				setSize(200,125);
		JPanel jp = new JPanel();
		add(jp,BorderLayout.SOUTH);
		btnOk = new JButton("Ok");
		btnCancel = new JButton("Cancel");
		btnYes = new JButton("Yes");
		btnNo = new JButton("No");
		switch(Option)
		{
			case 1:
				jp.add(btnOk);
				btnOk.addActionListener(this);
				break;
			case 2:
				jp.add(btnOk);
				jp.add(btnCancel);
				btnOk.addActionListener(this);
				btnCancel.addActionListener(this);
				break;
			case 3:
				jp.add(btnYes);
				jp.add(btnNo);
				btnYes.addActionListener(this);
				btnNo.addActionListener(this);
				break;
			case 4:
				jp.add(btnYes);
				jp.add(btnNo);
				jp.add(btnCancel);
				btnYes.addActionListener(this);
				btnNo.addActionListener(this);
				btnCancel.addActionListener(this);
				break;
		}
	}

	public void actionPerformed(ActionEvent e)
	{
		JButton btn = (JButton) e.getSource();
		if(btn.equals(btnOk))
			Answer = 0;
		if(btn.equals(btnCancel))
			Answer = 1;
		if(btn.equals(btnYes))
			Answer = 2;
		if(btn.equals(btnNo))
			Answer = 3;
		this.setVisible(false);
	}
}