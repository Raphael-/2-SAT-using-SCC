import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

public class Console extends JPanel
{
	private JTextArea textArea;
	
	public Console()
	{
		   textArea = new JTextArea(5,5);
		   textArea.setBackground(Color.black);
		   textArea.setLineWrap (true);
		   textArea.setFont(new Font("System", Font.BOLD, 12));
		   textArea.setForeground(Color.GREEN);
		   textArea.setEditable(false);
		   setLayout(new BorderLayout());
		   this.add(textArea);
	}
	
	public void write(String text,boolean addNewLine)
	{
		validateLines();
        for(char c:text.toCharArray())
        {
            textArea.append(Character.toString(c));
            try 
            {
            	Thread.sleep(70);
            } 
            catch (InterruptedException e)
            {
				e.printStackTrace();
			}
        }
        if(addNewLine)
        	textArea.append("\n");       
	}
	
	private void validateLines()
	{
		if(textArea.getLineCount()==5)
		{
			Element root = textArea.getDocument().getDefaultRootElement();
			Element first = root.getElement(0);
			try 
			{
				textArea.getDocument().remove(first.getStartOffset(), first.getEndOffset());
			}
			catch (BadLocationException e1)
			{
				e1.printStackTrace();
			}
		}
	}
	public void clear()
	{
		textArea.setText("");
	}
}
