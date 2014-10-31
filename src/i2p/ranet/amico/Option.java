package i2p.ranet.amico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileOutputStream;

import i2p.ranet.amico.ApplicationInfo;
import i2p.ranet.amico.PackagesManager;

import android.util.Log;

import android.os.Environment;
import android.content.pm.PackageInfo;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;

//import android.wp.Resource;

public class Option{
	private static final String mApp_Default[]={"com.android.settings,com.android.settings.Settings"
												,"com.android.gallery,com.android.camera.GalleryPicker"
												,"com.android.music,com.android.music.MusicBrowserActivity"
												,"com.android.quicksearchbox,com.android.quicksearchbox.SearchActivity"
												};
	public static boolean isAutoRunEnable(Context context) {
        return Settings.Secure.getInt(context.getContentResolver(),Resource.SETTINGS_AUTORUN_ENABLE, 0) > 0;
    }
/**
 *	QuickActivity 
 **/	
	public static class QuickActivity
	{
		public static void SetLiftActtivity(Context context,String packageName,String className)
		{
			SQLite  mSQLite = new SQLite(context);
			SQLiteDatabase db=mSQLite.getWritableDatabase();
			mSQLite.add(db, SQLite.TABLE_QUICKACTIVITY_LEFT, packageName, className);
			mSQLite.close();
		}
		public static void SetRightActtivity(Context context,String packageName,String className)
		{
			SQLite  mSQLite = new SQLite(context);
			SQLiteDatabase db=mSQLite.getWritableDatabase();
			mSQLite.add(db, SQLite.TABLE_QUICKACTIVITY_RIGHT, packageName, className);
			mSQLite.close();
		}
		public static void SetAutoActtivity(Context context,String packageName,String className)
		{
			SQLite  mSQLite = new SQLite(context);
			SQLiteDatabase db=mSQLite.getWritableDatabase();
			mSQLite.add(db, SQLite.TABLE_QUICKACTIVITY_AUTORUN, packageName, className);
			mSQLite.close();
		}				
		public static ApplicationInfo GetLiftActtivity(Context context)
		{
			SQLite  mSQLite = new SQLite(context);
			SQLiteDatabase db=mSQLite.getWritableDatabase();
			List<ContentValues> mApps=mSQLite.get(db, SQLite.TABLE_QUICKACTIVITY_LEFT);
			mSQLite.close();	
			if(mApps.size()==0)
			{
				return null;
			}
			
			PackagesManager packagesManager=new PackagesManager(context);
			List<ApplicationInfo> list=packagesManager.getActivityPackages();
			ApplicationInfo mApplicationInfo=null;
//			for(int i=0;i<PackagesManager.mApplications.size();i++)
			for(ApplicationInfo app : list)
			{
//				String mPackageName=PackagesManager.mApplications.get(i).intent.getComponent().getPackageName();
//				String mClassName=PackagesManager.mApplications.get(i).intent.getComponent().getClassName();
				String mPackageName=app.intent.getComponent().getPackageName();
				String mClassName=app.intent.getComponent().getClassName();
				if(mApps.get(0).getAsString("PackageName").equals(mPackageName)==true && 
						   mApps.get(0).getAsString("ClassName").equals(mClassName)==true)
						{
							mApplicationInfo=app;
							break;
						}
//				if(mApps.get(0).getAsString("PackageName").equals(mPackageName)==true && 
//				   mApps.get(0).getAsString("ClassName").equals(mClassName)==true)
//				{
//					mApplicationInfo=PackagesManager.mApplications.get(i);
//					break;
//				}
			}			
			return mApplicationInfo;
		}
		public static ApplicationInfo GetRightActtivity(Context context)
		{
			SQLite  mSQLite = new SQLite(context);
			SQLiteDatabase db=mSQLite.getWritableDatabase();
			List<ContentValues> mApps=mSQLite.get(db, SQLite.TABLE_QUICKACTIVITY_RIGHT);
			mSQLite.close();	
			if(mApps.size()==0)
			{
				return null;
			}
			PackagesManager packagesManager=new PackagesManager(context);
			List<ApplicationInfo> list=packagesManager.getActivityPackages();
			ApplicationInfo mApplicationInfo=null;
/*			for(int i=0;i<PackagesManager.mApplications.size();i++)
			{
				String mPackageName=PackagesManager.mApplications.get(i).intent.getComponent().getPackageName();
				String mClassName=PackagesManager.mApplications.get(i).intent.getComponent().getClassName();
				if(mApps.get(0).getAsString("PackageName").equals(mPackageName)==true && 
				   mApps.get(0).getAsString("ClassName").equals(mClassName)==true)
				{
					mApplicationInfo=PackagesManager.mApplications.get(i);
					break;
				}
			}*/
			for(ApplicationInfo app : list)
			{
				String mPackageName=app.intent.getComponent().getPackageName();
				String mClassName=app.intent.getComponent().getClassName();
				if(mApps.get(0).getAsString("PackageName").equals(mPackageName)==true && 
				   mApps.get(0).getAsString("ClassName").equals(mClassName)==true)
				{
					mApplicationInfo=app;
					break;
				}
			}	
			return mApplicationInfo;
		}
		public static ApplicationInfo GetAutoActtivity(Context context)
		{
			SQLite  mSQLite = new SQLite(context);
			SQLiteDatabase db=mSQLite.getWritableDatabase();
			List<ContentValues> mApps=mSQLite.get(db, SQLite.TABLE_QUICKACTIVITY_AUTORUN);
			mSQLite.close();	
			if(mApps.size()==0)
			{
				return null;
			}
			
			PackagesManager packagesManager=new PackagesManager(context);
			List<ApplicationInfo> list=packagesManager.getActivityPackages();
			ApplicationInfo mApplicationInfo=null;
/*			for(int i=0;i<PackagesManager.mApplications.size();i++)
			{
				String mPackageName=PackagesManager.mApplications.get(i).intent.getComponent().getPackageName();
				String mClassName=PackagesManager.mApplications.get(i).intent.getComponent().getClassName();
				if(mApps.get(0).getAsString("PackageName").equals(mPackageName)==true && 
				   mApps.get(0).getAsString("ClassName").equals(mClassName)==true)
				{
					mApplicationInfo=PackagesManager.mApplications.get(i);
					break;
				}
			}*/
			for(ApplicationInfo app : list)
			{
				String mPackageName=app.intent.getComponent().getPackageName();
				String mClassName=app.intent.getComponent().getClassName();
				if(mApps.get(0).getAsString("PackageName").equals(mPackageName)==true && 
				   mApps.get(0).getAsString("ClassName").equals(mClassName)==true)
				{
					mApplicationInfo=app;
					break;
				}
			}	
			return mApplicationInfo;
		}						
	}

/**
 *	SetPackages : Set Packages in settings
 *		--> Favorite : Set favorite packages , and save in favorite option file 
**/
 	public static class SetPackages
	{
		public static void FavoriteAdd(Context context,String mPackageNames,String mClassNames)
		{
			
			SQLite  mSQLite = new SQLite(context);
			SQLiteDatabase db=mSQLite.getWritableDatabase();
			mSQLite.add(db, SQLite.TABLE_FAVORITE, mPackageNames, mClassNames);
			mSQLite.close();			
		}
		public static void NavigationAdd(Context context,String mPackageNames,String mClassNames)
		{
			
			SQLite  mSQLite = new SQLite(context);
			SQLiteDatabase db=mSQLite.getWritableDatabase();
			mSQLite.add(db, SQLite.TABLE_NAVIGATION, mPackageNames, mClassNames);
			mSQLite.close();			
		}
		public static void FavoriteDelete(Context context,String mPackageNames,String mClassNames)
		{
			
			SQLite  mSQLite = new SQLite(context);
			SQLiteDatabase db=mSQLite.getWritableDatabase();
			mSQLite.delete(db, SQLite.TABLE_FAVORITE, mPackageNames, mClassNames);
			mSQLite.close();			
		}		
	}	
/**
 *	GetPackages : Get packages from settings
 *		--> Default : Get default packages
 *		--> Favorite : Get favorite packages  
 **/	
	public static class GetPackages
	{
		public static final String DEFAULT_FILE_PATH="/data/WPSetup/Launcher/Settings.ini";

		/**
		 * return [PackageName,ClassName]
		 * */
		private static List<ContentValues> GetSettingsApps()
		{
			List<ContentValues> mApps=new ArrayList<ContentValues>();
			//Load optione
			try{

				FileInputStream file = new FileInputStream(DEFAULT_FILE_PATH); 
				BufferedReader buf = new BufferedReader(new InputStreamReader(file)); 
				String str_read = new String(); 
				while((str_read = buf.readLine())!= null)
				{ 
					String str_split[]=str_read.split("=");
					if(str_split[0].equals("APP")==true)
					{
						ContentValues mApp=new ContentValues();
						String split[]=str_split[1].split(",");
						mApp.put("PackageName", split[0]);
						mApp.put("ClassName", split[1]);
						mApps.add(mApp);
					}
				} 
				file.close();
			} catch (IOException e){ 
			}
			return mApps;
		}

		public static List<ContentValues> Default()
		{
		    List<ContentValues> mApps=GetSettingsApps();  
		    if(mApps.size()!=0)
		    {
		    	return mApps;
			}
		    mApps=new ArrayList<ContentValues>();
	        for(int i=0;i<mApp_Default.length;i++)
	        {
	        	ContentValues mApp=new ContentValues();
	        	String split[]=mApp_Default[i].split(",");
	        	mApp.put("PackageName", split[0]);
	        	mApp.put("ClassName", split[1]);
	        	mApps.add(mApp);
	        }
			return mApps;			  
		}
		
		public static List<ContentValues> Favorite(Context context)
		{
			SQLite  mSQLite = new SQLite(context);
			SQLiteDatabase db=mSQLite.getWritableDatabase();
			List<ContentValues> mApps=mSQLite.get(db, SQLite.TABLE_FAVORITE);
			mSQLite.close();				
			return mApps;			
		}		

		public static List<ContentValues> Navigation(Context context)
		{
			SQLite  mSQLite = new SQLite(context);
			SQLiteDatabase db=mSQLite.getWritableDatabase();
			List<ContentValues> mApps=mSQLite.get(db, SQLite.TABLE_NAVIGATION);
			mSQLite.close();				
			return mApps;			
		}		

	}	
	
//Load logo
	public static class Logo
	{
		public static final int TYPE_FULL_NONE=0;
		public static final int TYPE_FULL_WINDOW=1;
		private static final String DefaultPath="/data/WPSetup/Launcher/logo.png";
		private static final String Path="/data/WPSetup/Launcher/logo.png";
		public static Drawable Load(Context c)
		{
			String logoPath=Path;
			File mFile=new File(logoPath);
			if(mFile.exists()==false)
			{
				logoPath=DefaultPath;
			}
			Bitmap bm = BitmapFactory.decodeFile(logoPath);  
			return new BitmapDrawable(bm);
		
		}
		
		public static boolean GetSettingsEnableSkipFunction(Context c)
		{
			int i=0;
//			try {
//				i=Settings.System.getInt(c.getContentResolver(), Resource.SETTINGS_ENABLE_SKIP_FUNCTION);
//			} catch (SettingNotFoundException e) {
				i=0;
//			}
			return (i==1)?true:false;
		}
		
		public static int getType(Context c)
		{
			int i=TYPE_FULL_WINDOW;
//			try {
//				i=Settings.System.getInt(c.getContentResolver(), Resource.SETTINGS_LOGO_TYPE);
//			} catch (SettingNotFoundException e) {
				i=TYPE_FULL_WINDOW;
//			}
			return i;
		}
	}		
}
