package tools;

import java.util.Comparator;

public class NodeComparator implements Comparator<Node>
{
	@Override
	public int compare(Node arg0, Node arg1) 
	{
		return arg0.getName().compareTo(arg1.getName());
	}
}
