import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JApplet;

import configs.Configs;

public class Applet extends JApplet
{
    Options options ;
    InputToolbar itb ;
    GraphCanvas gc;
    Console c;
    public void init() 
    {
    	Configs.codeBase=getCodeBase();
    	try 
    	{
			Configs.load();
		} 
    	catch (IOException e1) 
    	{
			e1.printStackTrace();
		}
    	c = new Console();
		options = new Options(this);
		itb = new InputToolbar(this);
    	setSize(800,600);
    	setLayout(new BorderLayout(0, 0));
    	gc = new GraphCanvas(this);
    	add("East", options);
    	add("Center",gc);
    	add("North", itb);
    	add("South",c);
    }   
}
