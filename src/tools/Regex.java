package tools;

import java.util.Random;
import java.util.regex.Pattern;

public final class Regex 
{
	private static final String REGEX="(\\((\\u00AC[a-z]\\u2228|[a-z]\\u2228)(\\u00AC)?[a-z]\\))+";
	
	public static boolean matches(String str)
	{
		return Pattern.matches(REGEX, str);
	}
	
	public static String randomSat()
	{
		String sat="";
		Random r=new Random();
		String [] vars={"a","b","c","d","e","f","g"};
		int numClauses=r.nextInt(6)+3;
		int numVars=r.nextInt(4)+4;
		for(int i=0;i<numClauses;i++)
		{
			sat+="(";
			if(r.nextInt(100)<50)
				sat+="\u00AC";
			int selVar1=r.nextInt(numVars);
			sat+=vars[selVar1];
			sat+="\u2228";
			if(r.nextInt(100)<50)
			{
				sat+="\u00AC";
			}
			int selVar2=r.nextInt(numVars);
			while(selVar1==selVar2)
				selVar2=r.nextInt(numVars);
			sat+=vars[selVar2];	
			sat+=")";	
		}
		return sat;
	}
}
