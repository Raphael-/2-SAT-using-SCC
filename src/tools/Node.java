package tools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.QuadCurve2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import configs.Configs;

public class Node 
{
	private static int newNodeX=250,newNodeY=20,curveStep=20;
	private static boolean leftNode=true;
	private Point p;
	private String name;
	private boolean isLeft,visited;
	private ArrayList<Node> connectedTo;
	
	public Node(String name)
	{
		this.name=name;
		this.isLeft=leftNode;
		this.visited=false;
		leftNode=!leftNode;
		connectedTo = new ArrayList<Node>();
	}

	private void drawNode(int x, int y, String name,Graphics g) 
	{
		g.setColor(new Color(Integer.parseInt(Configs.nodeColor[0]),Integer.parseInt(Configs.nodeColor[1]),Integer.parseInt(Configs.nodeColor[2])));
		g.fillOval(x, y, Configs.nodeDiameter, Configs.nodeDiameter);
		g.setColor(Color.black);
		g.drawOval(x, y, Configs.nodeDiameter, Configs.nodeDiameter);
		
		g.setFont(new Font("Courrier New",Font.BOLD,20));
		
		if(name.length()<2)
			g.drawString(name, this.getX()-5, this.getY()+5); 
		else
			g.drawString(name, this.getX()-10, this.getY()+5); 		
		
		
		if(this.isLeft)
		{
			newNodeX+=120;
		}
		else
		{
			newNodeX-=120;
			newNodeY+=120;
		}
	}
	
	private Point getRandomCirclePoint(Node n,double startAng,double endAng)
	{
		startAng = startAng + Math.random()*endAng;
		return getCirclePoint(n,startAng);
	}
	
	private Point getCirclePoint(Node n,double angle)
	{
		int x=0,y=0;
		x=(int) (n.getX()+(Configs.nodeDiameter/2)*Math.cos(angle));
		y=(int) (n.getY()+(Configs.nodeDiameter/2)*Math.sin(angle));
		return new Point(x,y);
	}
	
	public void drawConnection(Node dest)
	{
		connectedTo.add(dest);
		Collections.sort(connectedTo,new NodeComparator());
	}
	
	public void drawConnection(Graphics2D g,Node dest) 
	{	
		g.setColor(new Color(Integer.parseInt(Configs.edgeColor[0]),Integer.parseInt(Configs.edgeColor[1]),Integer.parseInt(Configs.edgeColor[2])));
		g.setStroke(new BasicStroke(2));
		
		drawConnection(dest);
		
		if(dest.getX()==this.getX())
		{
			if(Math.abs(dest.getY()-this.getY())>120)
			{
				int ctrPointRand=4*Configs.nodeDiameter+curveStep;
				QuadCurve2D q = new QuadCurve2D.Float();
				Point p = getCirclePoint(this,this.isLeft?-0.65*Math.PI:1.85*Math.PI);
				Point r = getRandomCirclePoint(dest,(dest.isLeft?-0.65*Math.PI:-0.25*Math.PI),dest.isLeft?-0.55*Math.PI:0.45*Math.PI);
	    		if(this.isLeft)
	    			q.setCurve(p.x,p.y, (this.getX()+dest.getX()-ctrPointRand)/2, (this.getY()+dest.getY()-ctrPointRand)/2, r.x, r.y);
	    		else
	    			q.setCurve(p.x,p.y, (this.getX()+dest.getX()+ctrPointRand)/2, (this.getY()+dest.getY()+ctrPointRand)/2, r.x, r.y);
	    		((Graphics2D)g).draw(q);
	    		drawArrowHead(g,new Point((this.getX()+dest.getX()+ctrPointRand)/2, (this.getY()+dest.getY()+ctrPointRand)/2),r);
	    		curveStep+=20;
			}
			else
			{
				Point p,r;
				if(this.getY()<dest.getY())
				{
					p = getCirclePoint(this,0.57*Math.PI);
					r = getCirclePoint(dest,-0.57*Math.PI);
				}
				else
				{
					p = getCirclePoint(this,(1.6)*Math.PI);
					r = getCirclePoint(dest,-(1.6)*Math.PI);
				}
				g.drawLine(p.x, p.y, r.x,r.y);
				drawArrowHead(g,p,r);
			}
		}
		else if(dest.getY()==this.getY())
		{
			Point p,r;
			if(this.isLeft)
			{
				p = getCirclePoint(this,0.207);
				r = getCirclePoint(dest,2.933);
			}
			else
			{
				p = getCirclePoint(this,3.161);
				r = getCirclePoint(dest,-0.02);
			}
			g.drawLine(p.x,p.y,r.x,r.y);
			drawArrowHead(g,p,r);
		}
		else
		{
			Point p,r;
			if(this.isLeft)
			{
				if(this.getY()<dest.getY())
				{
					p = getCirclePoint(this,1.08);
					r = getCirclePoint(dest,3.613);
				}
				else
				{
					p = getCirclePoint(this,1.65*Math.PI);
					r = getCirclePoint(dest,2.65);
				}
			}
			else
			{
				if(this.getY()<dest.getY())
				{
					p = getCirclePoint(this,2.35);
					r = getCirclePoint(dest,1.75*Math.PI);
				}
				else
				{
					p = getCirclePoint(this,1.25*Math.PI);
					r = getCirclePoint(dest,-1.75*Math.PI);
				}
			}
			g.drawLine(p.x,p.y,r.x,r.y);
			drawArrowHead(g,p,r);
		}
    }
	
	public void drawArrowHead(Graphics2D g,Point src,Point dest)
	{
		int [] xPoints = new int[3];
		int [] yPoints = new int[3];
		xPoints[0] = dest.x;
		yPoints[0] = dest.y;
		double theta = Math.atan2(dest.y - src.y, dest.x - src.x);  
		int arrow = 9;
	    double phi = Math.PI/4; 
	    xPoints[1] = (int) (xPoints[0] - arrow * Math.cos(theta + phi));  
	    yPoints[1] = (int) (yPoints[0] - arrow * Math.sin(theta + phi));  
	    xPoints[2] = (int) (xPoints[0] - arrow * Math.cos(theta - phi));  
	    yPoints[2] = (int) (yPoints[0] - arrow * Math.sin(theta - phi));   
	    g.fillPolygon(xPoints, yPoints, 3);
	}
	
	public void updatePre(Graphics g,int pre)
	{
		g.setColor(Color.black);
		g.setFont(new Font("Courrier New",Font.BOLD,15));
		int x =(int) (this.getX()+(Configs.nodeDiameter/2)*Math.cos(1.35*Math.PI));
 		int y =(int) (this.getY()+(Configs.nodeDiameter/2)*Math.sin(-1.75*Math.PI));
 		g.drawString(String.valueOf(pre), x, y);
	}
	
	public void updatePost(Graphics g,int post)
	{
		g.setColor(Color.black);
		g.setFont(new Font("Courrier New",Font.BOLD,15));
		int x =(int) (this.getX()+(Configs.nodeDiameter/2)*Math.cos(2.45*Math.PI));
 		int y =(int) (this.getY()+(Configs.nodeDiameter/2)*Math.sin(-1.25*Math.PI));
 		g.drawString(String.valueOf(post), x, y);

	}
	
	public Node getAdjUnvisitedVertex()
    {
		Iterator<Node> itr = connectedTo.iterator();
		while(itr.hasNext())
		{
			Node n = itr.next();
			if(!n.visited())
				return n;
		}
		return null;
    }
	
	public void resetConnections()
	{
		connectedTo.clear();
	}
	
	public static void reset()
	{
		newNodeX=250;
		newNodeY=20;
		curveStep=20;
		leftNode=true;
	}
	
	public void repaint(Graphics2D g)
	{
		this.p=new Point(newNodeX+(Configs.nodeDiameter/2),newNodeY+(Configs.nodeDiameter/2));
		drawNode(newNodeX,newNodeY,name,g);
		resetConnections();
	}
	
	public int getX()
	{
		return this.p.x;
	}
	
	public int getY()
	{
		return this.p.y;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public boolean visited()
	{
		return this.visited;
	}
	
	public void setVisited(boolean v)
	{
		this.visited=v;
	}
}
