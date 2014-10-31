package i2p.ranet.amico;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WPSQLDataBase extends SQLiteOpenHelper 
{
	public static final String TAG="WPSQLDataBase";
	public static final String TABLE_BASE="BASE";
	public static final String TABLE_LAUNCHER_APP_HIDE="LAUNCHER_APP_HIDE";
	public static final String TABLE_CARMODE_APP_SHOW="CARMODE_APP_SHOW";
	public static final String TABLE_TRACKER_SECURITY_PHONE="TRACKER_SECURITY_PHONE";
	
	private static final String DATABASE_NAME="/data/WPSetup/database.db";
	private static final int DATABASE_VER=2;
	
	private String mTable=null;
	private String mParam1=null;
	private String mParam2=null;
	
	private static SQLiteDatabase mSQLiteDatabase=null; 
	public void close()
	{
		super.close();
		mSQLiteDatabase=null;
		
	}
	public WPSQLDataBase(Context context,String table) {
		super(context, DATABASE_NAME, null, DATABASE_VER);
		File file=new File(DATABASE_NAME);		
		mTable=table;                                                                                        
		if(mTable.equals(TABLE_BASE)==true)
		{
			mParam1="name";
			mParam2="value";
		}else if(mTable.equals(TABLE_LAUNCHER_APP_HIDE)==true)
		{
			mParam1="package_name";
			mParam2="class_name";
		}else if(mTable.equals(TABLE_CARMODE_APP_SHOW)==true)
		{			
			mParam1="package_name";
			mParam2="class_name";
		}else if(mTable.equals(TABLE_TRACKER_SECURITY_PHONE)==true)
		{			
			mParam1="phone_number";
			mParam2=null;
		}
		if(mSQLiteDatabase==null)mSQLiteDatabase = getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String DATABASE_CREATE_TABLE;

		DATABASE_CREATE_TABLE =
			    "create table "+TABLE_BASE+" ("
			        + "_ID INTEGER PRIMARY KEY,"
			        + "name TEXT,"
			        + "value LONG"
			    + ");";
		db.execSQL(DATABASE_CREATE_TABLE);		
		
		DATABASE_CREATE_TABLE =
			    "create table "+TABLE_LAUNCHER_APP_HIDE+" ("
			        + "_ID INTEGER PRIMARY KEY,"
			        + "package_name TEXT,"
			        + "class_name TEXT"
			    + ");";
		db.execSQL(DATABASE_CREATE_TABLE);
		
		DATABASE_CREATE_TABLE =
			    "create table "+TABLE_CARMODE_APP_SHOW+" ("
			        + "_ID INTEGER PRIMARY KEY,"
			        + "package_name TEXT,"
			        + "class_name TEXT"
			    + ");";
		db.execSQL(DATABASE_CREATE_TABLE);
		
		DATABASE_CREATE_TABLE =
			    "create table "+TABLE_TRACKER_SECURITY_PHONE+" ("
			        + "_ID INTEGER PRIMARY KEY,"
			        + "phone_number TEXT"
			    + ");";
		db.execSQL(DATABASE_CREATE_TABLE);			
	}

	private int VersionCheck(SQLiteDatabase db,int version)
	{
		 if(version==1)
		 {
		 	String DATABASE_CREATE_TABLE;
			DATABASE_CREATE_TABLE =
				    "create table "+TABLE_TRACKER_SECURITY_PHONE+" ("
				        + "_ID INTEGER PRIMARY KEY,"
				        + "phone_number TEXT"
				    + ");";
			db.execSQL(DATABASE_CREATE_TABLE);
			return version+1;
		 }
		 return version;
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {      
		db.execSQL("DROP TABLE IF EXISTS config");
		int verCheck=oldVersion;
//		onCreate(db);
		while(verCheck!=newVersion)
		{
			verCheck=VersionCheck(db,verCheck);
		}		                                
	}
	
	public long add(String param1, Object param2) {
//	Log.d(TAG,"Table = " + mTable);
//	Log.d(TAG,"param1 = " + param1);
//	Log.d(TAG,"param2 = " + param2);
	
		if(mTable.equals(TABLE_BASE) || 
		   mTable.equals(TABLE_LAUNCHER_APP_HIDE) ||
		   mTable.equals(TABLE_CARMODE_APP_SHOW))
		{
			if(param1==null || param2==null)
			{
				return -1;
			}

		}

		if(mTable.equals(TABLE_TRACKER_SECURITY_PHONE))
		{
			if(param1==null)
			{
				return -1;
			}
		}
		ContentValues args = new ContentValues();
		args.put(mParam1, param1);
		if(mTable.equals(TABLE_BASE)==true)
		{
			args.put(mParam2,Long.valueOf(param2+"") );
		}else if(mTable.equals(TABLE_LAUNCHER_APP_HIDE)==true)
		{
			args.put(mParam2, (String)param2);
		}else if(mTable.equals(TABLE_CARMODE_APP_SHOW)==true)
		{			
			args.put(mParam2, (String)param2);
		}else if(mTable.equals(TABLE_TRACKER_SECURITY_PHONE)==true)
		{
			// no param2
		}else{
			Log.w(TAG,"Can not find this table ( "+mTable+" ).");
			return -1;
		}

		int res=isAlive(param1,param2);
		if(res==-1)
		{
//			Log.d(TAG,"add new database - " + param1);
			return mSQLiteDatabase.insert(mTable, null, args);
		}

//		Log.d(TAG,"update database - " + param1);		
		return mSQLiteDatabase.update(mTable,args,"_ID=" + res,null);
    }
	
	public void delete(String param1, Object param2)
	{
		int res=isAlive(param1,param2);
		if(res==-1)
		{
			return;
		}
		mSQLiteDatabase.delete(mTable,"_ID=" + res,null);
	}

    public List<ContentValues> get()
	{              
		List<ContentValues> info=new ArrayList<ContentValues>();
		Cursor cursor=null;
		if(mTable.equals(TABLE_BASE) || 
 		   mTable.equals(TABLE_LAUNCHER_APP_HIDE) ||
		   mTable.equals(TABLE_CARMODE_APP_SHOW))
		{
		
			cursor=mSQLiteDatabase.query(mTable,new String[] {"_ID", mParam1, mParam2},null,null,null,null,null);			
		}else if(mTable.equals(TABLE_TRACKER_SECURITY_PHONE))
		{
			cursor=mSQLiteDatabase.query(mTable,new String[] {"_ID", mParam1},null,null,null,null,null);
		}
		
		int count=cursor.getCount();
		
		if(count != 0) {
			cursor.moveToFirst();			//move flag to first
			for(int i=0; i<count; i++) {
				ContentValues buf=new ContentValues();
				buf.put("id", cursor.getInt(0));
				buf.put(mParam1, cursor.getString(1));
				if(mTable.equals(TABLE_BASE)==true)
				{
					buf.put(mParam2, cursor.getLong(2));
				}else if(mTable.equals(TABLE_LAUNCHER_APP_HIDE) ||
						 mTable.equals(TABLE_CARMODE_APP_SHOW))
				{
					buf.put(mParam2, cursor.getString(2));
				}else if(mTable.equals(TABLE_TRACKER_SECURITY_PHONE))
				{
					//no param2;
				}
				info.add(buf);
				cursor.moveToNext();
			}
			
		}
		cursor.close();
		return info;
	}
	
	public int isAlive(String param1, Object param2)
	{
     	int id=-1;
     	Cursor cursor=null;
		if(mTable.equals(TABLE_BASE) ||
		   mTable.equals(TABLE_LAUNCHER_APP_HIDE) ||
		   mTable.equals(TABLE_CARMODE_APP_SHOW)) 
		{
			cursor=mSQLiteDatabase.query(mTable,new String[] {"_ID", mParam1, mParam2},null,null,null,null,null);			
		}else if(mTable.equals(TABLE_TRACKER_SECURITY_PHONE))
		{
			cursor=mSQLiteDatabase.query(mTable,new String[] {"_ID", mParam1},null,null,null,null,null);
		}	     	
		int count=cursor.getCount();
		if(count != 0) {
			cursor.moveToFirst();			//move flag to first
			for(int i=0; i<count; i++) {
				if(mTable.equals(TABLE_BASE)==true || 
				   mTable.equals(TABLE_TRACKER_SECURITY_PHONE)==true)
				{
					if(param1.equals(cursor.getString(1))==true)
					{
						id=cursor.getInt(0);
						break;
					}					
				}else if(mTable.equals(TABLE_LAUNCHER_APP_HIDE) ||
						 mTable.equals(TABLE_CARMODE_APP_SHOW))
				{
					if(param1.equals(cursor.getString(1))==true && 
					   param2.toString().equals(cursor.getString(2))==true )
					{
						id= cursor.getInt(0);
						break;
					}					
				}

				cursor.moveToNext();
			}
		}
		cursor.close();
		//return rowId	
		return id;
	}

	public Object getParam2(String param1)
	{
		Object param=null;
		Cursor cursor=mSQLiteDatabase.query(mTable,new String[] {"_ID", mParam1, mParam2},null,null,null,null,null);
		int count=cursor.getCount();
		
		if(count != 0) {
			cursor.moveToFirst();			//move flag to first
			for(int i=0; i<count; i++) {
				if(param1.equals( cursor.getString(1))==true)
				{
					if(mTable.equals(TABLE_BASE)==true)
					{
						param=(long)cursor.getLong(2);
						break;
					}else{
						param=new String(cursor.getString(2));
						break; 
					}
				}
				cursor.moveToNext();
			}
		}
		cursor.close();
		return param;
	}	
}
