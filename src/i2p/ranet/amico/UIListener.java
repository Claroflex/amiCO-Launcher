package i2p.ranet.amico;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;                
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.app.ActivityManager;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

import i2p.ranet.amico.GpsStatus;
import i2p.ranet.amico.Resource;
import i2p.ranet.amico.DefaultSettings;


public class UIListener{
	private static Context context;
	private static String mIntentActivity="";
	public static boolean isCarOverSpeed=false;
	public static String getIntentActivity()
	{
		return mIntentActivity;
	}

	private static boolean isCarModeOpen()
	{
		int def=DefaultSettings.getInt(Resource.SETTINGS_CAR_MODE_OPEN,0);//default is off
		return Settings.System.getInt(context.getContentResolver(), Resource.SETTINGS_CAR_MODE_OPEN,def)>0;
//		return true;
	}	
	public static void Init(Context c)
	{
		context=c;           
	}
	public static void Start()
	{
		/*Install\UnInstall*/
        IntentFilter iPackageChange =new IntentFilter(Intent.ACTION_PACKAGE_INSTALL);
        iPackageChange.addAction(Intent.ACTION_PACKAGE_ADDED);
        iPackageChange.addAction(Intent.ACTION_PACKAGE_REMOVED);
        iPackageChange.addDataScheme("package");
        context.registerReceiver(mBroadcastReceiver,iPackageChange);		
        
		IntentFilter i=new IntentFilter();
		i.addAction(Intent.ACTION_LOCALE_CHANGED);
		i.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
        i.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);		
		i.addAction(Resource.INTENT_PHONE_SIGNAL_CHANGED);
		i.addAction(Resource.INTENT_APPHIDE_CHANGED);
		i.addAction(Resource.INTENT_WINDOW_LOCK_CHANGED);
		i.addAction(Resource.INTENT_APP_INSTALLER_STATUS_CHANGED	);
		i.addAction(GpsStatus.INTENT_GPS_SPEED_HEIGHT);
		i.addAction(GpsStatus.INTENT_GPS_SPEED_LOW);											

		context.registerReceiver(mBroadcastReceiver, i);					          		
	}


	
    public static BroadcastReceiver mBroadcastReceiver=new BroadcastReceiver()
    {

		@Override
		public void onReceive(Context c, Intent i) {      
			mIntentActivity=i.getAction().toString();   
//			if(mIntentActivity.equals(GpsStatus.INTENT_GPS_SPEED_HEIGHT))
//			{
//				isCarOverSpeed=true;
//			}else if(mIntentActivity.equals(GpsStatus.INTENT_GPS_SPEED_LOW))
//			{
				isCarOverSpeed=false;
//			}

			if(isCarModeOpen()==false)isCarOverSpeed=false;

			reflash();
		}
    };		

	private static void reflash()
	{
/*		PackagesManager.getActivityPackages();
		if(FavoriteUIManager.isCreateed==true)
		{
			FavoriteUIManager.Create();
		}
		if(PackageUIManager.isCreateed==true)
		{
			PackageUIManager.Create();
		}*/
	}		 	
}
