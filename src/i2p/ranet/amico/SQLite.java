package i2p.ranet.amico;

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

public class SQLite extends SQLiteOpenHelper 
{
	public static final String TABLE_FAVORITE="favorite";
	public static final String TABLE_NAVIGATION="navigation";
	public static final String TABLE_NET="net";
	public static final String TABLE_FUN="fun";
	public static final String TABLE_QUICKACTIVITY_RIGHT="quick_activity_right";
	public static final String TABLE_QUICKACTIVITY_LEFT="quick_activity_left";
	public static final String TABLE_QUICKACTIVITY_AUTORUN="quick_activity_autorun";
	
	private static final String DATABASE_NAME="/data/WPSetup/launcher1.db";
	private static final int DATABASE_VER=1;
	

	public SQLite(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VER);
	}	

	@Override
	public void onCreate(SQLiteDatabase db) {
		String DATABASE_CREATE_TABLE =
			    "create table "+TABLE_FAVORITE+" ("
			        + "_ID INTEGER PRIMARY KEY,"
			        + "package_name TEXT,"
			        + "class_name TEXT"
			    + ");";
		db.execSQL(DATABASE_CREATE_TABLE);
		
		DATABASE_CREATE_TABLE =
			    "create table "+TABLE_NAVIGATION+" ("
			        + "_ID INTEGER PRIMARY KEY,"
			        + "package_name TEXT,"
			        + "class_name TEXT"
			    + ");";
		db.execSQL(DATABASE_CREATE_TABLE);
		
		DATABASE_CREATE_TABLE =
			    "create table "+TABLE_NET+" ("
			        + "_ID INTEGER PRIMARY KEY,"
			        + "package_name TEXT,"
			        + "class_name TEXT"
			    + ");";
		db.execSQL(DATABASE_CREATE_TABLE);
		
		DATABASE_CREATE_TABLE =
			    "create table "+TABLE_FUN+" ("
			        + "_ID INTEGER PRIMARY KEY,"
			        + "package_name TEXT,"
			        + "class_name TEXT"
			    + ");";
		db.execSQL(DATABASE_CREATE_TABLE);
		
		DATABASE_CREATE_TABLE =
			    "create table " + TABLE_QUICKACTIVITY_RIGHT + " ("
			        + "_ID INTEGER PRIMARY KEY,"
			        + "package_name TEXT,"
			        + "class_name TEXT"
			    + ");";
		db.execSQL(DATABASE_CREATE_TABLE);
		
		DATABASE_CREATE_TABLE =
			    "create table " + TABLE_QUICKACTIVITY_LEFT + " ("
			        + "_ID INTEGER PRIMARY KEY,"
			        + "package_name TEXT,"
			        + "class_name TEXT"
			    + ");";
		db.execSQL(DATABASE_CREATE_TABLE);
		
		DATABASE_CREATE_TABLE =
			    "create table " + TABLE_QUICKACTIVITY_AUTORUN + " ("
			        + "_ID INTEGER PRIMARY KEY,"
			        + "package_name TEXT,"
			        + "class_name TEXT"
			    + ");";
		db.execSQL(DATABASE_CREATE_TABLE);		
	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS config");
		onCreate(db);
	}
	
	public long add(SQLiteDatabase db,String Table,String packageName, String className) {
		ContentValues args = new ContentValues();
		args.put("package_name", packageName);
		args.put("class_name", className);
		if(Table.equals(TABLE_FAVORITE)==true )
		{
			int res=isAlive(db,Table,packageName,className);
			if(res==-1)
			{
				return db.insert(Table, null, args);
			}
			return db.update(Table,args,"_ID=" + res,null);
			
		}
		if (Table.equals(TABLE_NAVIGATION)==true  )
		{
			int res=isAlive(db,Table,packageName,className);
			if(res==-1)
			{
				return db.insert(Table, null, args);
			}
			return db.update(Table,args,"_ID=" + res,null);
		
		}
		if ( Table.equals(TABLE_NET)==true ) {
			int res=isAlive(db,Table,packageName,className);
			if(res==-1)
			{
				return db.insert(Table, null, args);
			}
			return db.update(Table,args,"_ID=" + res,null);
			
		} 
		if (Table.equals(TABLE_FUN)==true  )
			{
			int res=isAlive(db,Table,packageName,className);
			if(res==-1)
			{
				return db.insert(Table, null, args);
			}
			return db.update(Table,args,"_ID=" + res,null);
				
		}else{
			int res=db.update(Table,args,"_ID=" + 1,null);
			if(res==0)
			{
				res=(int)db.insert(Table, null, args);
			}
			return res;
		}
    }
	
	public void delete(SQLiteDatabase db,String Table,String packageName, String className)
	{
		int res=isAlive(db,Table,packageName,className);
		if(res==-1)
		{
			return;
		}
		db.delete(Table,"_ID=" + res,null);
	}
	public List<ContentValues> get(SQLiteDatabase db,String Table)
	{
		List<ContentValues> info=new ArrayList<ContentValues>();
		Cursor cursor=db.query(Table,new String[] {"_ID", "package_name", "class_name"},null,null,null,null,null);
		int count=cursor.getCount();
		
		if(count != 0) {
			cursor.moveToFirst();			//move flag to first
			for(int i=0; i<count; i++) {
				ContentValues buf=new ContentValues();
				buf.put("id", cursor.getInt(0));
				buf.put("PackageName", cursor.getString(1));
				buf.put("ClassName", cursor.getString(2));
				info.add(buf);
				cursor.moveToNext();
			}
			
		}
		cursor.close();
		return info;
	}
	
	private int isAlive(SQLiteDatabase db,String Table,String packageName, String className)
	{
		Cursor cursor=db.query(Table,new String[] {"_ID", "package_name", "class_name"},null,null,null,null,null);
		int count=cursor.getCount();
		int id=-1;
		
		if(count != 0) {
			cursor.moveToFirst();			//move flag to first
			for(int i=0; i<count; i++) {
				if(packageName.equals( cursor.getString(1))==true && className.equals( cursor.getString(2))==true )
				{
					id= cursor.getInt(0);
					break;
				}
				cursor.moveToNext();
			}
		}
		cursor.close();
		return id;
	}
}
