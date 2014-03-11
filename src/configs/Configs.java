package configs;

import java.io.IOException;
import java.net.URL;

public class Configs 
{
	public static URL codeBase;
	public static int nodeDiameter;
	public static String[] nodeColor;
	public static String[] edgeColor;
	public static String exitIcon; 
	public static String startIcon;
	public static String resetIcon;
	public static String helpIcon;
	public static String randomIcon;
	
	public static void load() throws IOException
	{
		AppletProperties appletSettings = new AppletProperties(codeBase);
		nodeDiameter = Integer.parseInt(appletSettings.getProperty("NodeDiam","50"));
		exitIcon = appletSettings.getProperty("ExitIconFilename","exit.jpg");
		startIcon = appletSettings.getProperty("StartIconFilename","start.png");
		resetIcon = appletSettings.getProperty("ResetIconFilename","reset.png");
		randomIcon = appletSettings.getProperty("RandomIcon","random.png");
		nodeColor = appletSettings.getProperty("NodeColor","0,0,0").split(",");
		edgeColor = appletSettings.getProperty("EdgeColor","0,0,0").split(",");
	}
}
