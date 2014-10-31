package i2p.ranet.amico;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class DefaultSettings {

	private static final String SETTING_FILE="/data/WPSetup/DefaultSettings.inf";
	private static String getKeyValue(String key)
	{
		try{

			FileInputStream file = new FileInputStream(SETTING_FILE); 
			BufferedReader buf = new BufferedReader(new InputStreamReader(file)); 
			String read = new String(); 
			while((read = buf.readLine())!= null)
			{ 
				String str_split[]=read.split("=");
				if(key.equals(str_split[0])==true)
				{
					buf.close();
					return str_split[1];
				}
			} 
			file.close();
		} catch (IOException e){ 
			return null;
		}
		return null;
	}
	public static int getInt(String key)
	{
		String keyValue=getKeyValue(key);
		return (keyValue==null)?0:Integer.parseInt(keyValue);
	}

	public static int getInt(String key,int value)
	{
		String keyValue=getKeyValue(key);
		return (keyValue==null)?value:Integer.parseInt(keyValue);
	}

	public static String getString(String key)
	{
		return getKeyValue(key);		
	}
	
	public static String getString(String key,String value)
	{
		String str=getKeyValue(key);
		return (str==null)?value:str;		
	}

	
}
