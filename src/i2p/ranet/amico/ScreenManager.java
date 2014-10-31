package i2p.ranet.amico;

import java.util.List;

import android.content.BroadcastReceiver;       
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.provider.Settings;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.os.Handler;
import android.os.Message;

import i2p.ranet.amico.GpsStatus;
import i2p.ranet.amico.DefaultSettings;

public class ScreenManager {
    public static final int FLAG_REFLASH_ALL=0;
    public static final int FLAG_REFLASH_SCREEN_1=1;
    public static final int FLAG_REFLASH_SCREEN_2=2;
    
	
	private Context mContext;
	private PackagesManager mPackagesManager;
	private ScreenOneManager mScreenOneManager;
	private ScreenOneOneManager mScreenOneOneManager;
	private ScreenTwoManager mScreenTwoManager;
	private FrameLayout mScreenOne;
	private FrameLayout mScreenOneOne;
	private FrameLayout mScreenTwo;
	private ImageButton mBtnNavigation;
	private ImageButton mBtnNet;
	private ImageButton mBtnFun;
	
	private List<ApplicationInfo> mAppList;
	
	public static boolean isCarOverSpeed=false;
	
	public ScreenManager(Context context,FrameLayout screenOne,FrameLayout screenOneOne,FrameLayout screenTwo,ImageButton btnNavigation,ImageButton btnNet,ImageButton btnFun)
	{
		mContext=context;
		mScreenOne=screenOne;
		mScreenOneOne=screenOneOne;
		mScreenTwo=screenTwo;
		mBtnNavigation = btnNavigation;
		mBtnNet = btnNet;
		mBtnFun = btnFun;
		
		mPackagesManager=new PackagesManager(mContext); 
//		mPackagesManager.setButton(2);
		List<ApplicationInfo> appList= mPackagesManager.getActivityPackages();
		mScreenOneManager=new ScreenOneManager(mContext,mScreenOne,appList);
		//mScreenOneOneManager=new ScreenOneOneManager(mContext,mScreenOneOne,appList);
		mScreenTwoManager=new ScreenTwoManager(mContext,mScreenTwo,appList);
		
		
		
		mScreenOneManager.start(mBtnNavigation,mBtnNet,mBtnFun);
		mScreenTwoManager.start();
		//mScreenOneOneManager.start();
		
        IntentFilter iPackageChange =new IntentFilter(Intent.ACTION_PACKAGE_INSTALL);
        iPackageChange.addAction(Intent.ACTION_PACKAGE_ADDED);
        iPackageChange.addAction(Intent.ACTION_PACKAGE_REMOVED);
        iPackageChange.addDataScheme("package");
        context.registerReceiver(mBroadcastReceiver,iPackageChange);		
        
		IntentFilter i=new IntentFilter();
		i.addAction(i2p.ranet.amico.Resource.INTENT_PHONE_SIGNAL_CHANGED);
		i.addAction(i2p.ranet.amico.Resource.INTENT_APPHIDE_CHANGED);
		i.addAction(i2p.ranet.amico.Resource.INTENT_WINDOW_LOCK_CHANGED);
		i.addAction(i2p.ranet.amico.Resource.INTENT_APP_INSTALLER_STATUS_CHANGED);		
		i.addAction(Intent.ACTION_LOCALE_CHANGED);
		i.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
        i.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
        i.addAction(Resource.LAUNCHER4_FAVORITE_ADD);		
        i.addAction(Resource.LAUNCHER4_FAVORITE_REMOVE);
		i.addAction(Resource.LAUNCHER4_INTENT_WIDGET_CHANGED);
		i.addAction(Resource.LAUNCHER4_SCREENONE_PAGECHANGE);
		i.addAction(GpsStatus.INTENT_GPS_SPEED_HEIGHT);
		i.addAction(GpsStatus.INTENT_GPS_SPEED_LOW);     
		context.registerReceiver(mBroadcastReceiver, i);			
	}
	

	
    public BroadcastReceiver mBroadcastReceiver=new BroadcastReceiver()
    {

		@Override
		public void onReceive(Context c, Intent i) {      
			String action=i.getAction();

			if(i.getAction().equals(Resource.LAUNCHER4_SCREENONE_PAGECHANGE)==true)
			{
			switch(i.getExtras().getInt("PAGE_CHANGE"))
			{
				case 2:
				{
//					PageNavigation();
					mPackagesManager.setButton(1);
					reflash(FLAG_REFLASH_SCREEN_1);	
				}
				break;
				case 3:
				{
//					PageNet();
					mPackagesManager.setButton(2);
					reflash(FLAG_REFLASH_SCREEN_1);	
				}
				break;
				case 4:
				{
//					PageFun();
					mPackagesManager.setButton(3);
					reflash(FLAG_REFLASH_SCREEN_1);	
				}
				break;
			}
		}else  if(action.equals(Resource.LAUNCHER4_FAVORITE_ADD)==true)
			{
				String PackageName=i.getStringExtra("PackageName");
				String ClassName=i.getStringExtra("ClassName");
				Option.SetPackages.FavoriteAdd(mContext,PackageName,ClassName);
				reflash(FLAG_REFLASH_SCREEN_1);	
				//mScreenOneManager.reflash(mPackagesManager.getActivityPackages());  
			}else if(action.equals(Resource.LAUNCHER4_FAVORITE_REMOVE)==true)
			{
				String PackageName=i.getStringExtra("PackageName");
				String ClassName=i.getStringExtra("ClassName");
				Option.SetPackages.FavoriteDelete(mContext,PackageName,ClassName);
				reflash(FLAG_REFLASH_SCREEN_1);	
				//mScreenOneManager.reflash(mPackagesManager.getActivityPackages());
			}else if(action.equals(Resource.LAUNCHER4_INTENT_WIDGET_CHANGED)==true) 
			{
				//mScreenOneManager.reflash(mPackagesManager.getActivityPackages());
				reflash(FLAG_REFLASH_SCREEN_1);				  
			}else{
				if(action.equals(GpsStatus.INTENT_GPS_SPEED_HEIGHT))
				{
					isCarOverSpeed=true;
				}else if(action.equals(GpsStatus.INTENT_GPS_SPEED_LOW))
				{
					isCarOverSpeed=false;
				}
//				if(isCarModeOpen()==false)isCarOverSpeed=false;
				reflash(FLAG_REFLASH_ALL);	
			}
		}
    };		
    
    public void reflash(final int flag)
    {
    	
        Thread thr=new Thread()
		{
        	public void run()
        	{
				mAppList = mPackagesManager.getActivityPackages();
				Message msg=new Message();
				msg.arg1=flag;
				hReflash.sendMessage(msg);
        	}
        };
        thr.start();
   
    }
	
	public void Destroy()
	{
		mScreenOneManager.stop();
		mScreenTwoManager.stop();
	}
	
	private Handler hReflash=new Handler()
	{
		public void handleMessage(Message msg) 
		{
		  	super.handleMessage(msg);
		  	switch(msg.arg1)
		  	{
		  		case FLAG_REFLASH_ALL:
		  		{
					mScreenTwoManager.reflash(mAppList);
					mScreenOneManager.reflash(mAppList);		  		
				}
				break;
		  		case FLAG_REFLASH_SCREEN_1:
		  		{
					mScreenOneManager.reflash(mAppList);
				}
				break;
		  		case FLAG_REFLASH_SCREEN_2:
		  		{
		  			mScreenTwoManager.reflash(mAppList);
				}
				break;								
				
			}
			  
		}
	};	

//Get settings
//	private boolean isCarModeOpen()
//	{
//		int def=DefaultSettings.getInt(wp.howard.launcher4.Resource.SETTINGS_CAR_MODE_OPEN,0);//default is off
//		return Settings.System.getInt(mContext.getContentResolver(), wp.howard.launcher4.Resource.SETTINGS_CAR_MODE_OPEN,def)>0;
//	}	 
}
