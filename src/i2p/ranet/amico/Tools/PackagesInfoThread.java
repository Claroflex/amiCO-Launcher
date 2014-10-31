package i2p.ranet.amico.Tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.os.Build;
import i2p.ranet.amico.SystemProperties;
import android.widget.Toast;

public class PackagesInfoThread {
	private static final String TAG="PackagesInfoThread";

	private static final String PROP_CUSTORMER_VERSION="ro.build.version.customer";
	private static final String PROP_FW_VERSION="user.wp.fw_version";

	private static final String logPath="/sdcard/packages.log";

	private static Handler hLogCat = new Handler();
	private static Handler hPackageInfo = new Handler();
	private static Context mContext;

	private File logFile=new File(logPath);
	private Thread mThread=null;

	private Runnable mPackageInfo = new Runnable()
	{
		public void run()
		{
			if(mThread.isAlive()==true)
			{
				hLogCat.postDelayed(mPackageInfo, 100);
			}else{
				Toast.makeText(mContext, "Get packages information was done.", Toast.LENGTH_LONG).show();
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
	    			FileOutputStream logFileStream=null;
    	//open file
    				logFileStream=new FileOutputStream(logFile);
    				List<String> mInfo=GetPackagesInfo();
    				for(int i=0;i<mInfo.size();i++)
    				{
    					logFileStream.write((mInfo.get(i)+"\n").getBytes());
    				}
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
    			} catch ( IOException e) {  
    			}
			}
		};
		mThread.start();
		hPackageInfo.removeCallbacks(mPackageInfo);
		hPackageInfo.postDelayed(mPackageInfo, 100);
	}  
	
	public PackagesInfoThread(Context c) 
	{
		mContext=c;
	}
	
	private List<String> GetPackagesInfo()
	{
		List<String> mInfo=new ArrayList<String>();
		if(mInfo.size()!=0)
		{
			mInfo.clear();
		}
		mInfo.add("[Title Name][PackageName][ClassName]");
        PackageManager manager = mContext.getPackageManager();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        final List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));

        if (apps != null) {
            final int count = apps.size();

            for (int i = 0; i < count; i++) {
                ResolveInfo info = apps.get(i);
                String buf=new String();
                buf="[" + info.loadLabel(manager) + "]" +  //Title name
               		"[" + info.activityInfo.applicationInfo.packageName + "]" + 
               		"[" + info.activityInfo.name + "]";
                mInfo.add(buf);
            }
        }
        return mInfo;        
	}
}
