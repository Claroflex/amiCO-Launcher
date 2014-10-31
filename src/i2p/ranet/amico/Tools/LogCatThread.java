package i2p.ranet.amico.Tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.os.Handler;
import android.os.Build;
import i2p.ranet.amico.SystemProperties;
import android.util.Log;
import android.widget.Toast;

public class LogCatThread {
	private static final String TAG="LogCatThread";
	
	private static final String PROP_CUSTORMER_VERSION="ro.build.version.customer";
	private static final String PROP_FW_VERSION="user.wp.fw_version";	

	private static final String logPath="/sdcard/";
	
	private static Handler hLogCat = new Handler();
	private static Context mContext;
		
	private File logFile=null;
	private Thread mThread=null;
	
	private Runnable mLogCat = new Runnable()
	{
		public void run()
		{
			if(mThread.isAlive()==true)
			{
				hLogCat.postDelayed(mLogCat, 100);
			}else{
				Toast.makeText(mContext, "Get log was done.", Toast.LENGTH_LONG).show();
			}
		}
	};
	
	public void start() 
	{
		mThread=new Thread()
		{
    		public void run() 
			{
    			try {
	    			FileOutputStream logFileStream=logFileStream=new FileOutputStream(logFile);;
    				ArrayList<String> cmd = new ArrayList<String>();
    				cmd.add( "logcat");  
    				cmd.add( "-d");  
    				cmd.add( "-v");  
    				cmd.add( "time");
					cmd.add( "-t");
					cmd.add( "1000");    
//    				cmd.add( "-s");  
//    				cmd.add( "tag:W"); 
    			    Process process = Runtime.getRuntime().exec( cmd.toArray( new String[cmd.size()]));
    			    BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(process.getInputStream()), 1024);
    			    String strcat = new String();
    			    while ((strcat =  bufferedReader.readLine())!=null) {
    			    	logFileStream.write((strcat+"\n").getBytes());
    			    }
    				logFileStream.write(("-- Get log end --\n").getBytes());
    				String verinfo=
					"===============================================================\n"+
					"Android version : " + Build.VERSION.RELEASE+"("+Build.VERSION.SDK+")" + "\n" + 	
	    			"Customer version : " + SystemProperties.get(PROP_CUSTORMER_VERSION,android.os.Build.UNKNOWN) + "\n" +
	    			"Framework version : " + SystemProperties.get(PROP_FW_VERSION,android.os.Build.UNKNOWN) + "\n"+
	    			"===============================================================\n";
	    			logFileStream.write(verinfo.getBytes());    				
					logFileStream.flush();
					logFileStream.getFD().sync();
					logFileStream.close();    				
					bufferedReader.close();  
    			} catch ( IOException e) {  
    			}
			}
		};
		mThread.start();
		hLogCat.removeCallbacks(mLogCat);
		hLogCat.postDelayed(mLogCat, 100);
	}  
	
	public LogCatThread(Context c) 
	{
		mContext=c;
	
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy,MM,dd,HH,mm,ss");
		Calendar calendar = Calendar.getInstance();
		
	 	String split[]=sdf.format(calendar.getTime()).split(",");//get the formate gps date time.
		
	    String YEAR=split[0];
	    String MONTH=split[1];//value 0 = month 1
	    String DAY=split[2];
		
		int count=0;
		File file=new File(logPath+YEAR+MONTH+DAY+".log");
		while(file.exists()==true)
		{
			count+=1;
			file=new File(logPath+YEAR+MONTH+DAY+"-"+count+".log");//ex:/sdcard/20120709-1.log
		}
		logFile=file;
//		Log.d(TAG,"logFile = "+file);		
	}
}
