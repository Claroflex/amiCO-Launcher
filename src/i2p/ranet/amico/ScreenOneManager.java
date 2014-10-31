package i2p.ranet.amico;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
//import i2p.ranet.amico.Activity_Help;
import android.app.Activity;
import android.widget.Toast;


import android.app.Activity;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetHostView;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.util.Log;

public class ScreenOneManager implements OnClickListener , OnTouchListener{
	private static final String TAG="AmiCO/ScreenOneManager";
	
	private static final int ID_BTN_CLOCK=0;
	
	private static final int PAGE_CHANGE_UP=0;
	private static final int PAGE_CHANGE_DOWN=1;

	private Context mContext;
	private FrameLayout mScreenOneLayout;
	private LinearLayout mMainLayout=null;
	private LinearLayout mOthersLayout=null;
	private static ScrollView mPackagesScroll=null;
	private static LinearLayout mPackagesLayout=null;
	
    private static ImageButton mBtnNavigation;
    private static ImageButton mBtnNet;
    private static ImageButton mBtnFun;
	
    private LinearLayout mPackagesLayoutChild=null;
	private LinearLayout mClockLayout;
	private ImageView mLogoView;
	private TextView mDate;
	private TextView mTime;
	
	private Handler hClock=null;
	private int mBtnHeight;
	private int mBtnWidth;
	private int mScreenWidth;
	private int mScreenHeight;
	
	//Widget
	private AppWidgetHost mAppWidgetHost=null;
	private AppWidgetManager mAppWidgetManager=null;
	private int mWidgetId1=-1;
	private int mWidgetId2=-1;
	private PackageButton mBtn; 
	
	
	private static Handler mFocusChanged =  new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
		    switch(msg.arg1)
		    {
		    	case Resource.MESSAGE_FLAG_FOCUS_CHANGED:
		    	{
					int res=mPackagesScroll.getScrollY();
					ScrollStatusChange(res);
				}
			}

			super.handleMessage(msg);
		}
	};	
/* Views*/
	private View NotificationView()
	{
		TextView view=new TextView(mContext);
		view.setTextColor(Color.WHITE);
		view.setTextSize(Function.getIntSize(20,20,20));
		view.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
		view.setSingleLine(true);
		view.setText(R.string.dummy_content);
		return view;
	}
	
	private View LogoView()
	{
		ImageView view=new ImageView(mContext);
		view.setImageResource(R.drawable.logo);
		return view;
	}

	private View ClockView()
	{
		LinearLayout layout=new LinearLayout(mContext);
		layout.setId(ID_BTN_CLOCK);
		layout.setOnClickListener(this);
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.setPadding(0,0, Function.getIntSize(10,10,10), 0);
		layout.setBackgroundResource(R.drawable.button_style);
		//mClockLayout.setBackgroundColor(Color.GREEN);
		//layout.setBackgroundColor(Color.GREEN);
		
		mTime=new TextView(mContext);
		mDate=new TextView(mContext);
		mTime.setTextColor(Color.CYAN);
		mDate.setTextColor(Color.CYAN);
//		mTime.setTextSize(Function.getIntSize(84,84,84));
//		mDate.setTextSize(Function.getIntSize(24,24,24));
		mTime.setTextSize(Function.getIntSize(32,32,32));
		mDate.setTextSize(Function.getIntSize(15,15,15));
		mTime.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL);
		mDate.setGravity(Gravity.TOP | Gravity.LEFT);
		mTime.setText(getTime());
		mDate.setText(getDate());
		mDate.setGravity(Gravity.RIGHT);
		mDate.setPadding(0, 0, 10, 0);
		layout.addView(mTime);
		layout.addView(mDate);
		
		if(hClock==null)
		{
			hClock=new Handler();
			hClock.postDelayed(rClock, 1000);
		}
		return layout;
	}

	private View getWidgetView(int i)
	{   
		if(isWidgetEnabled()==false)
		{
			return null;
		}

		int widgetId=-1;
		if(i==0)
		{
			widgetId=getWidget1Id();	
		}else if(i==1){
			widgetId=getWidget2Id();
		}
		if(widgetId==-1)
		{
			return null;
		}

		AppWidgetProviderInfo info = mAppWidgetManager.getAppWidgetInfo(widgetId);

		if(info==null){
		    return null;
		}
		if(mWidgetId1!=-1 && i==0)
		{
			mAppWidgetHost.deleteAppWidgetId(mWidgetId1);
			mWidgetId1=-1;
		}
		if(mWidgetId2!=-1 && i==1)
		{
			mAppWidgetHost.deleteAppWidgetId(mWidgetId2);
			mWidgetId2=-1;
		}
		      
		return mAppWidgetHost.createView(mContext, widgetId,info); 
	}
	public void reflash(List<ApplicationInfo> applicationInfos)
	{

//		mScreenWidth=AmiCO.mScreenWidth-FunctionBarService.getBarWidth(); //Ignat		
		mScreenWidth=AmiCO.mScreenWidth-(int)(FunctionBarService.getBarWidth()-30); //Ignat		
		mScreenHeight=(int)(AmiCO.mScreenHeight * 5/6);
		if(getStatusBarModeValue()==Resource.STATUS_BAR_MODE_ALWAYS_SHOW)
		{
			mScreenHeight= AmiCO.mScreenHeight-((int)(AmiCO.mScreenHeight * (Integer.parseInt(mContext.getString(R.string.status_bar_height_pre))/100.0)));
//			mScreenHeight= AmiCO.mScreenHeight-((int)(AmiCO.mScreenHeight * (11/100.0)));
//			mScreenHeight= 11;
		}

		mBtnWidth=mScreenWidth/4;
		mBtnHeight=mScreenHeight/4;

		if(mMainLayout==null)
		{
			mMainLayout=new LinearLayout(mContext);
			mMainLayout.setOrientation(LinearLayout.VERTICAL);
		}else{
			mMainLayout.removeAllViews();
		}
/*Other layout*/
		//widget
		if(mAppWidgetHost==null)
		{
			mAppWidgetHost = new AppWidgetHost(mContext, Resource.APPWIDGET_HOST_ID); 
    		mAppWidgetHost.startListening();
		}

		if(mAppWidgetManager==null)mAppWidgetManager=AppWidgetManager.getInstance(mContext);
		if(mOthersLayout==null)
		{
			mOthersLayout=new LinearLayout(mContext);
		}else{
			mOthersLayout.removeAllViews();
		}

		LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT,1);
//		LayoutParams params=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,1);
		

        View widget1=getWidgetView(0);
        if(widget1==null)
        {
        	View view=LogoView();
        	mOthersLayout.addView(view,params);//logo image
		}else{
			mOthersLayout.addView(widget1,params);
		}
		View widget2=getWidgetView(1);;
        if(widget2==null)
        {
        	View view1=ClockView();
        	mOthersLayout.addView(view1,params);//clock
		}else{
			mOthersLayout.addView(widget2,params);
		}
		
/* app layout*/
		if(mPackagesScroll==null)
		{
			mPackagesScroll=new ScrollView(mContext);
			mPackagesScroll.setOnTouchListener(this);
			mPackagesScroll.setVerticalScrollBarEnabled(false);
			mPackagesScroll.setHorizontalScrollBarEnabled(false);		
			mPackagesScroll.setOverScrollMode(View.OVER_SCROLL_NEVER);
		}else{
			mPackagesScroll.removeAllViews();
		}


		if(mPackagesLayout==null)
		{
			mPackagesLayout=new LinearLayout(mContext);
			mPackagesLayout.setOrientation(LinearLayout.VERTICAL);
		}else{
			mPackagesLayout.removeAllViews();
		}
		
		if(mPackagesLayoutChild==null)
		{
			mPackagesLayoutChild=new LinearLayout(mContext);
			mPackagesLayoutChild.setOrientation(LinearLayout.HORIZONTAL);
		}else{
			mPackagesLayoutChild.removeAllViews();
		}   
		AddPackages(applicationInfos); 

		View view0=NotificationView();
		mMainLayout.addView(view0,mScreenWidth,mScreenHeight/6); // Notification bar

				
/* add layout*/		
 //		if(isWidgetEnabled()==true) //Ignat
 //		{
			mMainLayout.addView(mOthersLayout,mScreenWidth,mScreenHeight/4);
			mMainLayout.addView(mPackagesScroll,mScreenWidth,(mScreenHeight/4)*2);
//		}else{ 
//			mMainLayout.addView(mPackagesScroll,mScreenWidth,mScreenHeight);
//		}
        mScreenOneLayout.removeAllViews();
		mScreenOneLayout.addView(mMainLayout,mScreenWidth,mScreenHeight);	
	}
	public ScreenOneManager(Context context , FrameLayout screenOne,List<ApplicationInfo> applicationInfos)
	{
		mContext=context;
		mScreenOneLayout=screenOne;
		reflash(applicationInfos);
	}

	@Override
	public void onClick(View view) {
		int id=view.getId();
		switch(id)
		{
			case ID_BTN_CLOCK:
			{
				Intent intent = new Intent(Intent.ACTION_MAIN);
		        intent.addCategory(Intent.CATEGORY_LAUNCHER);
				intent.setClassName("com.android.deskclock","com.android.deskclock.DeskClock");
				mContext.startActivity(intent);
			}
			break;
			case R.id.settings:
			{
				Toast.makeText(mContext, "Settings", Toast.LENGTH_LONG).show();
			}
			break;
			case R.id.navigation:
			{
				Toast.makeText(mContext, "Navigation", Toast.LENGTH_LONG).show();
			}
			break;
		}
	}

	
	OnClickListener BtnClickListener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			int id=view.getId();
			switch(id)
			{
				case ID_BTN_CLOCK:
				{
					Intent intent = new Intent(Intent.ACTION_MAIN);
			        intent.addCategory(Intent.CATEGORY_LAUNCHER);
					intent.setClassName("com.android.deskclock","com.android.deskclock.DeskClock");
					mContext.startActivity(intent);
				}
				break;
				case R.id.net:
				{
					Intent intent= new Intent(Resource.LAUNCHER4_SCREENONE_PAGECHANGE);
					intent.putExtra("PAGE_CHANGE", 3);
					mContext.sendBroadcast(intent);

					Toast.makeText(mContext, "Net", Toast.LENGTH_LONG).show();
					mBtnNet.setBackgroundResource(R.drawable.panel_button_selected);
					mBtnNavigation.setBackgroundResource(R.drawable.panel_button_normal);
					mBtnFun.setBackgroundResource(R.drawable.panel_button_normal);
				}
				break;
				case R.id.fun:
				{
					Intent intent= new Intent(Resource.LAUNCHER4_SCREENONE_PAGECHANGE);
					intent.putExtra("PAGE_CHANGE", 4);
					mContext.sendBroadcast(intent);

					Toast.makeText(mContext, "Fun", Toast.LENGTH_LONG).show();
					mBtnNet.setBackgroundResource(R.drawable.panel_button_normal);
					mBtnNavigation.setBackgroundResource(R.drawable.panel_button_normal);
					mBtnFun.setBackgroundResource(R.drawable.panel_button_selected);
				}
				break;
				case R.id.navigation:
				{
					Intent intent= new Intent(Resource.LAUNCHER4_SCREENONE_PAGECHANGE);
					intent.putExtra("PAGE_CHANGE", 2);
					mContext.sendBroadcast(intent);
					
					Toast.makeText(mContext, "Navigation", Toast.LENGTH_LONG).show();
					mBtnNet.setBackgroundResource(R.drawable.panel_button_normal);
					mBtnNavigation.setBackgroundResource(R.drawable.panel_button_selected);
					mBtnFun.setBackgroundResource(R.drawable.panel_button_normal);
					
				}
				break;
			}
/*			Toast toast = Toast.makeText(mContext, 
					"Long Click",
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show(); */
		}
	};
/*	
    private static void ButtonImageSwitchNavigation(boolean press,boolean enabled)
    {
    	mBtnNavigation.setEnabled(enabled);
    	mBtnNavigation.setFocusable(enabled);
    	if(enabled==false)
    	{
    		int res=mBtnNavigation.isFocused()?R.drawable.panel_button_selected:R.drawable.panel_button_normal;
//    		int res=R.drawable.panel_button_selected;
    		mBtnNavigation.setBackgroundResource(res);
//    		return;
    	}
    	
    	int resource=(press==true)?R.drawable.panel_button_selected:R.drawable.panel_button_normal;
//    	int res=btnMenu.isFocused()?R.drawable.bnt_menu_press_2:resource;
    	int res=resource;
    	mBtnNavigation.setBackgroundResource(res);
    } 	 	

    private static void ButtonImageSwitchNet(boolean press,boolean enabled)
    {
    	mBtnNet.setEnabled(enabled);
    	mBtnNet.setFocusable(enabled);
    	if(enabled==false)
    	{
    		int res=mBtnNet.isFocused()?R.drawable.panel_button_selected:R.drawable.panel_button_normal;
//    		int res=R.drawable.panel_button_selected;
    		mBtnNet.setBackgroundResource(res);
//    		return;
    	}
    	
    	int resource=(press==true)?R.drawable.panel_button_selected:R.drawable.panel_button_normal;
//    	int res=btnMenu.isFocused()?R.drawable.bnt_menu_press_2:resource;
    	int res=resource;
    	mBtnNet.setBackgroundResource(res);
    } 	 	
*/
/*    
    private static OnFocusChangeListener BtnFocusChange=new OnFocusChangeListener()
    {
		@Override
		public void onFocusChange(View view, boolean b) {
			int id=view.getId();
			switch(id)
			{
				case R.id.navigation:
				{
					ButtonImageSwitchNavigation(b,mBtnNavigation.isEnabled());
				}
				break;
				case R.id.net:
				{
					ButtonImageSwitchNet(b,mBtnNet.isEnabled());
				}
				break;
				case R.id.fun:
				{
//					ButtonImageSwitchFun(b,mBtnFun.isEnabled());
				}
				break;			
			
			}
		}
    };
*/
	public void start(ImageButton btnNavigation, ImageButton btnNet, ImageButton btnFun)
	{
		mBtnNavigation = btnNavigation;
		mBtnNet = btnNet;
		mBtnFun = btnFun;
		
		
        IntentFilter intentFilter = new IntentFilter(Resource.LAUNCHER4_SCREENONE_PAGECHANGE);
        intentFilter.addAction(Resource.LAUNCHER4_SCREEN_CHANDED);
        mContext.registerReceiver(mBroadcastReceiver, intentFilter);
        FunctionBarService.setScreenOne(mScreenOneLayout);
        
		mBtnNavigation.setOnClickListener(BtnClickListener);
		mBtnNet.setOnClickListener(BtnClickListener);
		mBtnFun.setOnClickListener(BtnClickListener);
//		mBtnNavigation.setOnFocusChangeListener(BtnFocusChange);
//		mBtnNet.setOnFocusChangeListener(BtnFocusChange);
//		mBtnFun.setOnFocusChangeListener(BtnFocusChange);		
	}

	public void stop()
	{
		if(hClock!=null)
		{
			hClock.removeCallbacks(rClock);
			hClock=null;
		}
		mContext.unregisterReceiver(mBroadcastReceiver);
		try {  
            mAppWidgetHost.stopListening();  
        } catch (NullPointerException ex) {  
            Log.i(TAG, "problem while stopping AppWidgetHost during Launcher destruction", ex);  
        }  
	}

	private void AddPackages(List<ApplicationInfo> applicationInfos)
	{

		int count =0;
		for(ApplicationInfo app : applicationInfos)
		{
		
			if(app.isFavorite()==false)
			{
				continue;
			}

//			if(app.isNavigation()==false)
//			{
//				continue;
//			}
			
			
			int textSize=Function.getIntSize(16,24,24);
			if(getStatusBarModeValue()==Resource.STATUS_BAR_MODE_ALWAYS_SHOW)
			{
				textSize=Function.getIntSize(13,21,22);
			}
			int flage=PackageButton.MENU_DELETE | PackageButton.MENU_REMOVE | PackageButton.MENU_NAVIGATION | PackageButton.MENU_NET | PackageButton.MENU_FUN ;
			mBtn=new PackageButton(mContext,app,mBtnWidth-10,mBtnHeight,textSize,flage,mFocusChanged);
			mBtn.setPadding(5, Function.getIntSize(1,4,6), 5, Function.getIntSize(2,3,6));
			mPackagesLayoutChild.addView(mBtn);
			count+=1;
			
			if(mPackagesLayoutChild.getChildCount()==4)
			{
				mPackagesLayout.addView(mPackagesLayoutChild);
				mPackagesLayoutChild=new LinearLayout(mContext);
				mPackagesLayoutChild.setOrientation(LinearLayout.HORIZONTAL);	
			}
		}
		if(mPackagesLayoutChild.getChildCount()!=0)
		{
			mPackagesLayout.addView(mPackagesLayoutChild);
		}
		mPackagesScroll.addView(mPackagesLayout);	
		if(FunctionBarService.isOpened()==false)
		{
			if(count<=8)
			{
				FunctionBarService.DisableUpButton(true);
				FunctionBarService.DisableDownButton(true);
			}else{
				FunctionBarService.DisableUpButton(true);
				FunctionBarService.DisableDownButton(false);
			}
		} 
		
	}	
	
	private Runnable rClock=new Runnable()
	{
		@Override
		public void run() {
			mTime.setText(getTime());
			mDate.setText(getDate());			
			hClock.postDelayed(rClock, 1000);	
		}
	};

	private String getDate()
	{
		Calendar calendar=Calendar.getInstance(); 
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd", Locale.US); 
		return simpleDateFormat.format(calendar.getTime());
	}
	
	private String getTime()
	{
		Calendar calendar=Calendar.getInstance(); 
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("HH:mm", Locale.US); 
		return simpleDateFormat.format(calendar.getTime());
	}

	/* scroll process */	
	private boolean bPageup;
	private int mGetEventStartY=0;
	private int mGetScrollStartY=0;
	private int mSstartPoint=0;	
	@Override
	public boolean onTouch(View view, MotionEvent event) {
		switch(event.getAction())
		{

			case MotionEvent.ACTION_MOVE:
			{
				
				int endPoint=(int) event.getY();
				bPageup=(endPoint>mSstartPoint)?true:false;
				mSstartPoint=(int) endPoint;
			}
			break;
			
			case MotionEvent.ACTION_DOWN:
			{
				mGetEventStartY=(int) event.getY();
				mGetScrollStartY=mPackagesScroll.getScrollY();
			}
			break;
			case MotionEvent.ACTION_UP:
			{
				if(mGetScrollStartY==mPackagesScroll.getScrollY())
				   //mPackagesScroll.getScrollY()==0 || 
				   //mPackagesScroll.getScrollY()==mMainLayout.getHeight()-mPackagesScroll.getHeight() ||
				{
					ScrollStatusChange(mPackagesScroll.getScrollY());
					return false;
				}

				//set range
				int PageChangeRange=Function.getIntSize(5,10,15);
				
				int getEventEndY=(int) event.getY();
				int res=Function.abs(mGetEventStartY-getEventEndY);
				if(mGetEventStartY>getEventEndY)
				{
					if(res>PageChangeRange)
					{
						if(bPageup==false)
						{
							PageDown();
						}else{
							PageUp();
						}
					}else{
						PageUp();
					}
				}else{
					if(res>PageChangeRange)
					{
						if(bPageup==true)
						{
							PageUp();
						}else{
							PageDown();
						}
					}else{
						PageDown();  
					}
				}
				return true;
			}
		}		
		return false;
	}

	private void PageDown()
	{
		int move=mBtn.getHeight()*3;
		if(move==0)return;
		if(isWidgetEnabled()==true)
		{
			move=mBtn.getHeight()*2;
		}
		int ScrollStartY=mPackagesScroll.getScrollY();
		int Page=ScrollStartY/move+1;
		int ScrollMove=Page*move-ScrollStartY;
		mPackagesScroll.smoothScrollBy(0, ScrollMove);
		int res=ScrollStartY+ScrollMove;
		ScrollStatusChange(res);
	}
	
	private void PageUp()
	{
	
		int move=mBtn.getHeight()*3;
		if(move==0)return;
		if(isWidgetEnabled()==true)
		{
			move=mBtn.getHeight()*2;
		}
		int ScrollStartY=mPackagesScroll.getScrollY();
		int Page=ScrollStartY/move;
		if(ScrollStartY%move==0)Page--;
		int ScrollMove=Page*move-ScrollStartY;
		mPackagesScroll.smoothScrollBy(0, ScrollMove);
		int res=ScrollStartY+ScrollMove;
		ScrollStatusChange(res);
	}		
	
	public BroadcastReceiver mBroadcastReceiver=new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context c, Intent i) 
		{
			if(i.getAction().equals(Resource.LAUNCHER4_SCREENONE_PAGECHANGE)==true)
			{
				switch(i.getExtras().getInt("PAGE_CHANGE"))
				{
					case PAGE_CHANGE_UP:
					{
						PageUp();
					}
					break;
					case PAGE_CHANGE_DOWN:
					{
						PageDown();
					}
					break;
					case 2:
					{
//						PageNavigation();
					}
					break;
					case 3:
					{
//						PageNet();
					}
					break;
					case 4:
					{
//						PageFun();
					}
					break;
				}
			}else if(i.getAction().equals(Resource.LAUNCHER4_SCREEN_CHANDED)==true)
			{
				if(FunctionBarService.isOpened()==false)
				{
					int res=mPackagesScroll.getScrollY();
					ScrollStatusChange(res);				
					mScreenOneLayout.setVisibility(View.VISIBLE);
						
				}else{
					mScreenOneLayout.setVisibility(View.INVISIBLE);				
				}
			}
		}
	};	
	
	private static void ScrollStatusChange(int point)
	{
		int scrollheight=mPackagesLayout.getHeight()-mPackagesScroll.getHeight();
		if(FunctionBarService.isOpened()==false)
		{
			if(mPackagesLayout.getHeight()<=mPackagesScroll.getHeight())
			{
				FunctionBarService.DisableUpButton(true);
				FunctionBarService.DisableDownButton(true);
				return;
			}
			if(point>=scrollheight)
			{
				FunctionBarService.DisableDownButton(true);
				FunctionBarService.DisableUpButton(false);
			}else if(point<=0)
			{
				FunctionBarService.DisableUpButton(true);
				FunctionBarService.DisableDownButton(false);
			}else{
				FunctionBarService.DisableUpButton(false);
				FunctionBarService.DisableDownButton(false);
			}
		}
	}
/* Settings */
	private int getStatusBarModeValue()
	{
		return Settings.System.getInt(mContext.getContentResolver(), Resource.LAUNCHER4_SETTINGS_STATUS_BAR_MODE,0);
	}

	private int getWidget1Id()
	{
		return Settings.System.getInt(mContext.getContentResolver(), Resource.SETTINGS_LAUNCHER_WIDGET_1,-1);
	}
	private int getWidget2Id()
	{
		return Settings.System.getInt(mContext.getContentResolver(), Resource.SETTINGS_LAUNCHER_WIDGET_2,-1);
	}
	
	private boolean isWidgetEnabled()
    {
    	return Settings.System.getInt(mContext.getContentResolver(),Resource.SETTINGS_LAUNCHER_WIDGET_ENABLED,0)>0;
	}
}
