import java.awt.Color;
import java.awt.Dimension;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import listener.InvalidInputListener;
import listener.ValidationListener;
import configs.Configs;


public class InputToolbar extends JPanel implements ActionListener
{
	private JTextField ftx;
	private Label label1,label2;
	private JLabel label3;
	private JButton buttons[];
	private Applet parent;
	public ValidationListener vl;
	
	public InputToolbar(Applet parent)
	{
		this.parent = parent;

		setBackground(Color.black);
		
		buttons = new JButton[3];
		buttons[0] = new JButton("\u2228");
		buttons[1] = new JButton("\u00AC");
		buttons[2] = new JButton("( )");
		
		buttons[0].setBackground(Color.black);
		buttons[0].setForeground(Color.red);
		buttons[0].setToolTipText("Logical OR");
		buttons[0].addActionListener(this);
		
		buttons[1].setBackground(Color.black);
		buttons[1].setForeground(Color.red);
		buttons[1].setToolTipText("Logical NOT");
		buttons[1].addActionListener(this);
		
		buttons[2].setBackground(Color.black);
		buttons[2].setForeground(Color.red);
		buttons[2].setToolTipText("Parentheses");
		buttons[2].addActionListener(this);
	

		ftx = new JTextField();
		ftx.setColumns(40);
		
		label1 = new Label("Input");
		label1.setForeground(Color.red);
		
		label2 = new Label("Operators");
		label2.setForeground(Color.red);

		label3 = new JLabel();
		label3.setPreferredSize(new Dimension(48,48));
		try 
		{
			label3.setDisabledIcon(new ImageIcon(new URL(Configs.codeBase,"icons/locked.png")));
		} 
		catch (MalformedURLException e) 
		{
			e.printStackTrace();
		}
		
		ftx.addKeyListener(new InvalidInputListener());
		vl = new ValidationListener(ftx,label3);
		ftx.getDocument().addDocumentListener(vl);
		
		add("Center",label1);
		add("Center",label3);
		add("Center",ftx);
		add("East",label2);
		add("East",buttons[0]);
		add("East",buttons[1]);
		add("East",buttons[2]);
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		Object source = e.getSource();
		if(source == buttons[0] )
		{
			ftx.replaceSelection("\u2228");
		}
		else if(source == buttons[1])
		{
			ftx.replaceSelection("\u00AC");
		}
		else
		{
			ftx.replaceSelection("()");
			ftx.setCaretPosition(ftx.getCaretPosition()-1);
		}
		ftx.requestFocus();
	}
	
	public void lockTextField(boolean b)
	{
		ftx.setEditable(!b);
		if(ftx.isEditable())
		{
			label3.setEnabled(true);
			label3.setIcon(null);
		}
		else
		{
			label3.setEnabled(false);
		}
	}
	
	public String getInput()
	{
		return ftx.getText();
	}
	
	public void replaceInput(String str)
	{
		ftx.setText(str);
	}
	
	public void lockButtons()
	{
		for(JButton b:buttons)
			b.setEnabled(false);
	}
	
	public void unlockButtons()
	{
		for(JButton b:buttons)
			b.setEnabled(true);
	}
	
}
