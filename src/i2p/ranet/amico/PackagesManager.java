package i2p.ranet.amico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import i2p.ranet.amico.SystemProperties;
import android.provider.Settings;

import i2p.ranet.amico.DefaultSettings;
import i2p.ranet.amico.WPSQLDataBase;

public class PackagesManager {
	private Context mContext;
	private int mButton=1;
	
	private WPSQLDataBase mSQLAPPHide;
	private WPSQLDataBase mCarModeAppSet;

	public PackagesManager(Context context)
	{
		mContext=context;
		mSQLAPPHide=new WPSQLDataBase(mContext,WPSQLDataBase.TABLE_LAUNCHER_APP_HIDE);
		mCarModeAppSet=new WPSQLDataBase(mContext,WPSQLDataBase.TABLE_CARMODE_APP_SHOW);
	}
	
	public void setButton(int button)
	{
		mButton = button;
	}
	
	public List<ApplicationInfo> getActivityPackages()
	{
		List<ApplicationInfo> mApplications=new ArrayList<ApplicationInfo>();
        PackageManager manager = mContext.getPackageManager();
        
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        final List<ResolveInfo> apps = manager.queryIntentActivities(mainIntent, 0);
        Collections.sort(apps, new ResolveInfo.DisplayNameComparator(manager));

        if (apps != null) {
            final int count = apps.size();

            if(mApplications.size()!=0)
            {
            	mApplications.clear();
            }

            for (int i = 0; i < count; i++) {
                ApplicationInfo application = new ApplicationInfo();
                ResolveInfo info = apps.get(i);
				String packageName=info.activityInfo.applicationInfo.packageName;
				String className=info.activityInfo.name;
				
                application.title = info.loadLabel(manager);
                application.setActivity(new ComponentName(
                		packageName,
                		className),
                        Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                application.icon = info.activityInfo.loadIcon(manager);
                application.setSystemApp(((info.activityInfo.applicationInfo.flags & android.content.pm.ApplicationInfo.FLAG_SYSTEM) >0)? true:false);
  
                if (mButton == 1) application.setToFavorite(
                		isFavoritePackage(
                				application.intent.getComponent().getPackageName(),
                				application.intent.getComponent().getClassName()
                		)
                );
                if (mButton == 2) application.setToFavorite(
                		isNavigationPackage(
                				application.intent.getComponent().getPackageName(),
                				application.intent.getComponent().getClassName()
                		)
                );
                
//installer enable check
                if(isPackageInstallerEnabled()==false && info.activityInfo.applicationInfo.packageName.equals("com.android.vending")==true)continue;
//phone signal                
                String mPhoneSignalEnable=SystemProperties.get(Resource.PROP_PHONE_SIGNAL_CHANGED).toString();
                if(mPhoneSignalEnable.equals("0")==true)
                {
                	if(	info.activityInfo.applicationInfo.packageName.equals("com.android.contacts")==true ||
                		info.activityInfo.applicationInfo.packageName.equals("com.android.mms")==true
					)continue;
                }
//app hide
                
                if(mSQLAPPHide.isAlive(packageName,className)!=-1)continue;
                
// speed height lock app
//lock/unlock
				PackageButton.bScreenLock=false;
				if(isLockedEnabled()==true || ScreenManager.isCarOverSpeed==true)
				{
					PackageButton.bScreenLock=true;                                     
					if(mCarModeAppSet.get().size()==0)
					{
						String apks[]={"com.android.contacts,com.android.contacts.DialtactsActivity",
									   "com.android.music,com.android.music.MusicBrowserActivity"
									  };
						String getApk=packageName+","+className;
						boolean check=false;
						for(String apk : apks)
						{
							if(getApk.equals(apk)==true)
							{
								check=true;
								break;
							}
						}
						if(check==false)continue;
					}else{
		                if(mCarModeAppSet.isAlive(packageName,className)==-1)continue;
	                }
				}                
                mApplications.add(application);
            }
        }	
        return mApplications;
	}	
	
	private boolean isFavoritePackage(String mPackageName , String mClassName)
	{
		List<ContentValues> mApps=Option.GetPackages.Default();
		for(int i=0;i<mApps.size();i++)
		{
			ContentValues mApp=mApps.get(i);
			if(mApp.getAsString("PackageName").equals(mPackageName)==true &&
			   mApp.getAsString("ClassName").equals(mClassName)==true)
			{
//				return true;	
			}
		}
		
		mApps=Option.GetPackages.Favorite(mContext);
//		mApps=Option.GetPackages.Navigation(mContext);
		for(int i=0;i<mApps.size();i++)
		{
			ContentValues mApp=mApps.get(i);
			if(mApp.getAsString("PackageName").equals(mPackageName)==true &&
			   mApp.getAsString("ClassName").equals(mClassName)==true)
			{
				return true;	
			}
		}		
		return false;		
	}	

	private boolean isNavigationPackage(String mPackageName , String mClassName)
	{
		List<ContentValues> mApps=Option.GetPackages.Default();
		for(int i=0;i<mApps.size();i++)
		{
			ContentValues mApp=mApps.get(i);
			if(mApp.getAsString("PackageName").equals(mPackageName)==true &&
			   mApp.getAsString("ClassName").equals(mClassName)==true)
			{
//				return true;	
			}
		}
		
//		mApps=Option.GetPackages.Favorite(mContext);
		mApps=Option.GetPackages.Navigation(mContext);
		for(int i=0;i<mApps.size();i++)
		{
			ContentValues mApp=mApps.get(i);
			if(mApp.getAsString("PackageName").equals(mPackageName)==true &&
			   mApp.getAsString("ClassName").equals(mClassName)==true)
			{
				return true;	
			}
		}		
		return false;		
	}	
	
	
//Get settings
    private boolean isPackageInstallerEnabled() 
	{ 
    	return Settings.System.getInt(mContext.getContentResolver(), i2p.ranet.amico.Resource.SYSTEM_PACKAGES_INSTALLER_ENABLED,1)>0;
    }  
    
	private boolean isLockedEnabled()
	{
		return Settings.Secure.getInt(mContext.getContentResolver(), i2p.ranet.amico.Resource.SETTINGS_WINDOW_LOCK_STATUS,0)>0;
	}    
}
