package configs;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Properties;

public final class AppletProperties extends Properties
{
	public AppletProperties(URL codeBase) throws IOException
	{
		load(new URL(Configs.codeBase,"configurations/Conf.properties").openStream());
	}
	
	public void load(InputStream inStream) throws IOException
	{
		InputStreamReader reader = null;
		try
		{
			reader = new InputStreamReader(inStream, Charset.defaultCharset());
			super.load(reader);
		}
		finally
		{
			inStream.close();
			if (reader != null)
				reader.close();
		}
	}
	
	public String getProperty(String key, String defaultValue)
	{ 
		String property = super.getProperty(key,defaultValue);
		if (property == null)
		{
			System.err.println("Missing property for key "+property);
			return null;
		}
		return property.trim();
	}
}
