import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JWindow;

import tools.Regex;
import configs.Configs;


class Options extends JPanel implements ActionListener
{
	private JButton[] buttons;
	private Applet parent;
	public Options(Applet parent)
	{
		this.parent = parent;
		
		buttons = new JButton[3];
		setLayout(new GridLayout(6, 1, 0, 10));
		setBackground(new Color(0,0,0));
		try 
		{
			buttons[0]=new JButton(new ImageIcon(new URL(Configs.codeBase,"icons/"+Configs.startIcon)));
			buttons[1]=new JButton(new ImageIcon(new URL(Configs.codeBase,"icons/"+Configs.resetIcon)));
			buttons[2]=new JButton(new ImageIcon(new URL(Configs.codeBase,"icons/"+Configs.randomIcon)));
		} 
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		buttons[0].setBackground(new Color(0,0,0));
		buttons[0].setToolTipText("Starts algorithm with user's input");
		buttons[0].addActionListener(this);
		buttons[1].setBackground(Color.black);
		buttons[1].setToolTipText("Reset algorithm");
		buttons[1].addActionListener(this);
		buttons[2].setBackground(Color.black);
		buttons[2].setToolTipText("Starts algorithm with random input");
		buttons[2].addActionListener(this);
		add(buttons[0]);
		add(buttons[1]);
		add(buttons[2]);
	}
	@Override
	public void actionPerformed(ActionEvent arg0)
	{
		Object ob=arg0.getSource();
		
		if(ob==buttons[0])
		{
			if(Regex.matches(parent.itb.getInput()))
			{
				lockButtons(4);
				parent.itb.lockTextField(true);
				parent.itb.lockButtons();
				new Thread(new Runnable() 
				{
			       public void run() 
			       {
			    	   parent.gc.start(parent.itb.getInput());
			        }
				}).start();
			}
			else
			{
				JOptionPane.showMessageDialog(null,
					    "You cannot start the algorithm with an invalid input!",
					    "ERROR",
					    JOptionPane.ERROR_MESSAGE);
			}
		}
		else if(ob==buttons[1])
		{
			this.parent.itb.lockTextField(false);
			this.parent.itb.unlockButtons();
			this.parent.itb.replaceInput("");
			this.parent.itb.vl.cancelTask();
			this.parent.gc.clear();
			unlockButtons(4);
		}
		else if(ob==buttons[2])
		{
			this.parent.itb.replaceInput(Regex.randomSat());
			if(Regex.matches(parent.itb.getInput()))
			{
				lockButtons(4);
				parent.itb.lockTextField(true);
				parent.itb.lockButtons();
				new Thread(new Runnable() 
				{
			       public void run() 
			       {
			    	   parent.gc.start(parent.itb.getInput());
			        }
				}).start();
			}
			else
			{
				JOptionPane.showMessageDialog(null,
					    "You cannot start the algorithm with an invalid input!",
					    "ERROR",
					    JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public void lockButtons(int button)
	{
		switch(button)
		{
			case 1:
				buttons[0].setEnabled(false);
				break;
			case 2:
				buttons[1].setEnabled(false);
				break;
			case 3:
				buttons[2].setEnabled(false);
				break;
			case 4:
				for(JButton b:buttons)
					b.setEnabled(false);
				break;
		}
	}
	
	public void unlockButtons(int button)
	{
		switch(button)
		{
			case 1:
				buttons[0].setEnabled(true);
				break;
			case 2:
				buttons[1].setEnabled(true);
				break;
			case 3:
				buttons[2].setEnabled(true);
				break;
			case 4:
				for(JButton b:buttons)
					b.setEnabled(true);
				break;
		}
	}
}

