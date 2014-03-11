import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import javax.swing.SwingUtilities;
import tools.Node;
import tools.NodeComparator;

public class GraphCanvas extends Canvas
{
	private ArrayList<Node> nodes;
	private Applet parent;
	private String input;
	private int step;
	private Stack<Node> descPostOrder;
	private ArrayList<ArrayList<Node>> scc;
	private boolean instantResult;
	
	public GraphCanvas(Applet parent)
	{
		this.setIgnoreRepaint(true);
		this.parent=parent;
		this.step=0;
		setBackground(Color.white);
		nodes=new ArrayList<Node>();
		descPostOrder=new Stack<Node>();
		scc=new ArrayList<ArrayList<Node>>();
	}
	
	private void doRepaint()
	{
		Runnable task = new Runnable() 
		{
			public void run() 
			{
				paint(getGraphics());
			}
		};
			try 
			{
				SwingUtilities.invokeAndWait(task);
			}
			catch (InterruptedException e) 
			{
				e.printStackTrace();
			}
			catch (InvocationTargetException e)
			{
				e.printStackTrace();
			}
	}
	
	private boolean isSat()
	{
		parent.c.write("There "+(scc.size()<2?"is ":"are ")+"a total of "+scc.size()+" Strongly Connected Component"+(scc.size()<2?"":"s"),true);
		Iterator<ArrayList<Node>> itr1 = scc.iterator();
		int counter=0;
		while(itr1.hasNext())
		{
			switch(++counter)
			{
				case 1:
					parent.c.write("1st SCC : ",false);
					break;
				case 2:
					parent.c.write("2nd SCC : ",false);
					break;
				case 3:
					parent.c.write("3rd SCC : ",false);
					break;
				default:
					parent.c.write(counter+"th SCC : ",false);
					break;
			}
			ArrayList<Node> a = itr1.next();
			Iterator<Node> itr2 = a.iterator();
			while(itr2.hasNext())
			{
				parent.c.write(itr2.next().getName()+(itr2.hasNext()?", ":""),false);
			}
			parent.c.write("\n",false);
		}
		itr1 = scc.iterator();
		while(itr1.hasNext())
		{
			ArrayList<Node> a = itr1.next();
			Iterator<Node> itr2 = a.iterator();
			while(itr2.hasNext())
			{
				Node n = itr2.next();
				if(n.getName().length()==2)
				{
					if(getIndexOf(n.getName().substring(1),a)!=a.size())
					{
						parent.c.write("Formula is not satisfied because variable "+n.getName()+" and "+n.getName().substring(1)+" belong to the same SCC",true);
						return false;
					}
				}
				else
				{
					
					if(getIndexOf("\u00AC"+n.getName(),a)!=a.size())
					{
						parent.c.write("Formula is not satisfied because variable \u00AC"+n.getName()+" and "+n.getName()+" belong to the same SCC",true);
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public void start(String input)
	{
		this.input=input;
		init(input);
		if(nodes.size()>8)
			instantResult=true;
		if(!instantResult)
			graphSol();
		else
			instantSol();
		if(isSat())
		{
			Collections.sort(nodes,new NodeComparator());
			parent.c.write("Formula is satisfied.Values that satisfy the formula are : (",false);
			for(int i=0;i<nodes.size()/2;i++)
				parent.c.write(nodes.get(i).getName()+(i!=1-(nodes.size()/2)?",":""), false);
			parent.c.write(") = ("+this.satValue(input)+")",true);
		}
		parent.options.unlockButtons(2);
			
	}
	
	private void graphSol()
	{
		parent.c.write("Draw a node for every unique literal",true);
		++step;
		doRepaint();
		sleep(2500);
		parent.c.write("For every edge (x,y) two edges are being drawn.First edge starts from \u00ACx to y and second edge starts from \u00ACy to x.",true);
		++step;
		doRepaint();
		parent.c.write("Reverse original graph.",true);
		sleep(2500);
		++step;
		doRepaint();
		sleep(2500);
		parent.c.write("Perform Depth First Search for the reversed graph.",true);
		++step;
		doRepaint();
		sleep(2500);
		++step;
		parent.c.write("Perform Depth First Search for the original graph , using the descending post order from the previous execution of DFS.",true);
		doRepaint();
		sleep(2500);
	}
	
	private void instantSol()
	{
		addConections(input,true,true);
		descPostOrder.clear();
		DFS(nodes.get(0),false,descPostOrder,-1);
		Iterator<Node> itr = nodes.iterator();
		while(itr.hasNext())
			itr.next().resetConnections();
		addConections(input,false,true);
		SCC(descPostOrder,scc);
	}
	
	private void init(String input)
	{
		Set<String> literals=new HashSet<String>();
		List<String> temp=new ArrayList<String>();
		
		for(String str : input.replaceAll("[)|\u2228]", " ").replace("(", "").split(" "))
		{
			literals.add(str);
			if(str.length()==2)
				literals.add(str.substring(1));
			else
				literals.add("\u00AC"+str);
		}
		
		temp.addAll(literals);
		Collections.sort(temp);
		while(!temp.isEmpty())
		{
			String lit=temp.get(0);
			nodes.add(new Node(lit));
			temp.remove(0);
			if(lit.length()==2)
			{
				nodes.add(new Node(lit.substring(1)));
				temp.remove(lit.substring(1));
			}
			else
			{
				nodes.add(new Node("\u00AC"+lit));
				temp.remove("\u00AC"+lit);
			}
		}
	}
	
	private void addConections(String input,boolean reversed,boolean ignoreSleep)
	{	  
		for(String str:input.replace("(", "").split("\\)"))
		{
			String [] s = str.split("\u2228");
			if(s.length == 2)
			{
				if(!reversed)
				{
					if(instantResult)
					{
						nodes.get(this.getIndexOf(s[0].length()==1?("\u00AC"+s[0]):s[0].substring(1),nodes)).drawConnection(nodes.get(this.getIndexOf(s[1],nodes)));
						nodes.get(this.getIndexOf(s[1].length()==1?("\u00AC"+s[1]):s[1].substring(1),nodes)).drawConnection(nodes.get(this.getIndexOf(s[0],nodes)));
					}
					else
					{
						nodes.get(this.getIndexOf(s[0].length()==1?("\u00AC"+s[0]):s[0].substring(1),nodes)).drawConnection((Graphics2D)this.getGraphics(), nodes.get(this.getIndexOf(s[1],nodes)));
						nodes.get(this.getIndexOf(s[1].length()==1?("\u00AC"+s[1]):s[1].substring(1),nodes)).drawConnection((Graphics2D)this.getGraphics(), nodes.get(this.getIndexOf(s[0],nodes)));
					}
				}
				else
				{
					if(instantResult)
					{
						nodes.get(this.getIndexOf(s[0],nodes)).drawConnection(nodes.get(this.getIndexOf(s[1].length()==1?("\u00AC"+s[1]):s[1].substring(1),nodes)));
						nodes.get(this.getIndexOf(s[1],nodes)).drawConnection(nodes.get(this.getIndexOf(s[0].length()==1?("\u00AC"+s[0]):s[0].substring(1),nodes)));
					}
					else
					{
						nodes.get(this.getIndexOf(s[0],nodes)).drawConnection((Graphics2D)this.getGraphics(), nodes.get(this.getIndexOf(s[1].length()==1?("\u00AC"+s[1]):s[1].substring(1),nodes)));
						nodes.get(this.getIndexOf(s[1],nodes)).drawConnection((Graphics2D)this.getGraphics(), nodes.get(this.getIndexOf(s[0].length()==1?("\u00AC"+s[0]):s[0].substring(1),nodes)));
					}
				}
			}
			if(!ignoreSleep)
				sleep(500);
		}
	}
	
	private void repaintGraph()
	{
		erase();
		Iterator<Node> itr = nodes.iterator();
		while(itr.hasNext())
		{
			Node n = itr.next();
			n.repaint((Graphics2D)getGraphics());
		}
	}
	
	private void erase()
	{
		Node.reset();
		Graphics g = this.getGraphics();
		g.setColor(Color.white);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
	
	public void clear()
	{
		erase();
		this.scc.clear();
		this.descPostOrder.clear();
		this.step=0;
		this.nodes.clear();
		this.parent.c.clear();
	}
	
	private int getIndexOf(String str,ArrayList<Node> al)
	{
		int i=0;
		Iterator<Node> itr = al.iterator();
		while(itr.hasNext())
		{
			
			if(itr.next().getName().equals(str))
				break;
			else
				i++;
		}
		return i;
	}
	
	private int DFS(Node root,boolean ignoreOrder,Stack<Node> postOrder,int prevClock)
	{
		Stack<Node> stack = new Stack<Node>();
		int clock=ignoreOrder?prevClock:0;
		while(root!=null)
		{
			root.setVisited(true);
			++clock;
			if(!instantResult)
				root.updatePre(getGraphics(),clock);
			stack.push(root);
			while(!stack.isEmpty())
			{
				if(!instantResult)
					sleep(1000);
				Node n = stack.peek().getAdjUnvisitedVertex();
				if(n==null)
				{
					Node r=stack.pop();
					if(!ignoreOrder)
						postOrder.push(r);
					++clock;
					if(!instantResult)
						r.updatePost(getGraphics(), clock);
				}
				else
				{
					n.setVisited(true);
					++clock;
					if(!instantResult)
						n.updatePre(getGraphics(),clock);					
					stack.push(n);
				}
			}
			if(ignoreOrder)
				break;
			root=getUnvisitedVertex(nodes);
		}
		if(!ignoreOrder)
		{
			Iterator<Node> it = nodes.iterator();
			while(it.hasNext())
				it.next().setVisited(false);
		}
		return clock;
	}
	
	private Node getUnvisitedVertex(ArrayList<Node> list) 
	{
		Iterator<Node> itr = list.iterator();
		while(itr.hasNext())
		{
			Node n = itr.next();
			if(!n.visited())
				return n;
		}
		return null;
	}

	private void SCC(Stack<Node> stack,ArrayList<ArrayList<Node>> SCC)
	{
		 int clock=-1;
	     while(!stack.isEmpty())
	     {
	    	 ArrayList<Node> component = new ArrayList<Node>();
	    	 Node r = stack.pop();
	    	 component.add(r);
	         clock = DFS(r,true,null,clock==-1?0:clock);
	         for(Iterator<Node> it = stack.iterator(); it.hasNext(); )
	         {
	        	 Node n = it.next();
	        	 if(n.visited())
	             {
	        		 component.add(n);
	            	 it.remove();
	             }
	         }
	         SCC.add(component);
	       }
	}
	
	private String satValue(String input)
	{
		String bytes;
		StringBuffer buffer = new StringBuffer(input);		
		for(int j=0;j<Math.pow(2, nodes.size()/2);j++)
		{
			bytes=increase(j,nodes.size()/2);
			for(int i=0;i<input.length();i++)
			{
				char c=input.charAt(i);
				if(Character.isLetter(c))
				{
					buffer.setCharAt(i,bytes.charAt(getIndexOf(""+input.charAt(i),nodes)));
				}
				else if(c=='\u00AC')
				{
					buffer.setCharAt(i++, ' ');
					buffer.setCharAt(i, bytes.charAt(getIndexOf(""+input.charAt(i),nodes))=='0'?'1':'0');
				}
				else if(c=='\u2228')
				{
					buffer.setCharAt(i, ' ');
				}
				else if(c=='(' || c==')')
				{
					buffer.setCharAt(i, ' ');
				}
			}
			if(buffer.toString().replace(" ", "").matches("(01|10|11)+"))
				return bytes.replaceAll(".(?=.)", "$0,");
		}
		return "ERROR";
	}
	
	private String increase(int n,int size)
	{
		String str=Integer.toBinaryString(n);
		while(str.length()<size)
			str="0"+str;
		return str;
	}
	
	private void sleep(int ms)
	{
		try 
		{
			Thread.sleep(ms);
		} 
		catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	public void paint(Graphics g)
	{
		switch(step)
		{
			case 0:
				break;
			case 1:
				repaintGraph();
				break;
			case 2:
				repaintGraph();
				addConections(input,false,false);
				break;
			case 3:
				repaintGraph();
				addConections(input,true,false);
				break;
			case 4:
				descPostOrder.clear();
				DFS(nodes.get(0),false,descPostOrder,-1);
				break;
			case 5:
				repaintGraph();
				addConections(input,false,true);
				SCC(descPostOrder,scc);
				break;
		}
	}
}