package listener;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import tools.Regex;
import configs.Configs;

public class ValidationListener implements DocumentListener
{
	private JLabel validation;
	private JTextField input;
    final Timer timer = new Timer(true);
    private TimerTask task;
    
    public ValidationListener(JTextField jtf,JLabel jl)
    {
    	this.validation=jl;
    	this.input=jtf;
    }
    
	@Override
	public void changedUpdate(DocumentEvent arg0) 
	{
		
	}

	@Override
	public void insertUpdate(DocumentEvent arg0)
	{
		scheduleTask();
	}

	@Override
	public void removeUpdate(DocumentEvent arg0) 
	{
		scheduleTask();
	}
	
	private void scheduleTask()
	{
		if(task != null)
			task.cancel();
        task = new TimerTask() 
        {
            @Override
            public void run() 
            {
            	try 
            	{
            		if(Regex.matches(input.getText()))
            		{
            			validation.setIcon(new ImageIcon(new URL(Configs.codeBase,"icons/valid.png")));
            		}
            		else
            		{
            			validation.setIcon(new ImageIcon(new URL(Configs.codeBase,"icons/invalid.png")));
            		}
            	} 
            	catch (MalformedURLException e) 
            	{
					e.printStackTrace();
				}
            }
        };
        timer.schedule(task, 3000);
	}
	
	public void cancelTask()
	{
		if(task != null)
			task.cancel();
	}
}
