package i2p.ranet.amico;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.FrameLayout;

public class FunctionBarService {
	private static String TAG = "AmiCO/FunctionBarService";
	private static Context mContext;
	private static boolean bstart=false;
	private static ImageButton mBtnSettings;
    private static ImageButton btnUp;
    private static ImageButton btnDown;
    private static ImageButton btnMenu;
    private static MySlidingDrawer mlSlidingDrawer;	
	private static FrameLayout mScreenOneLayout=null;
	private static FrameLayout mScreenOneOneLayout=null;
	public static void setScreenOne(FrameLayout layout)
	{
		mScreenOneLayout=layout;
	}
	public static void setScreenOneOne(FrameLayout layout)
	{
		mScreenOneOneLayout=layout;
	}
    public static int getBarWidth()
    {
    	return mContext.getResources().getDrawable(R.drawable.bnt_menu_default).getIntrinsicWidth();
    }
    public static boolean isStart()
    {
    	return bstart;
    }    
    
    public static void stop()
    {
    	if(bstart==true)mContext.unregisterReceiver(mBroadcastReceiver);
    }
    
    public static boolean isOpened()
    {
    	return mlSlidingDrawer.isOpened();
    }
   

	public static void start(Context context,MySlidingDrawer slidingDrawer,  ImageButton btnSettings, ImageButton menu , ImageButton down , ImageButton up)
	{
		bstart=true;
		mContext=context;
		mBtnSettings = btnSettings;
		btnUp=up;
		btnDown=down;
		btnMenu=menu;
		mlSlidingDrawer=slidingDrawer;
		
//		mBtnSettings.setOnClickListener(BtnClickListener);
		
		mBtnSettings.setOnFocusChangeListener(BtnFocusChange);
    	btnUp.setOnFocusChangeListener(BtnFocusChange);
    	btnDown.setOnFocusChangeListener(BtnFocusChange);
    	btnMenu.setOnFocusChangeListener(BtnFocusChange);  
    	btnUp.setOnKeyListener(BtnOnKey);
    	btnDown.setOnKeyListener(BtnOnKey);
    	btnMenu.setOnKeyListener(BtnOnKey);
				
		//bar button set        
        mlSlidingDrawer.setHandleId(R.id.ctlHandle);
        mlSlidingDrawer.setTouchableIds(new int[]{R.id.btnUp, R.id.btnDown, R.id.menu});
        
        //broadcase
        IntentFilter intentFilter = new IntentFilter(Resource.LAUNCHER4_ONTOUCHEVENT_BAR);
        intentFilter.addAction(Resource.LAUNCHER4_ONCLICK_BAR);
        mContext.registerReceiver(mBroadcastReceiver, intentFilter);      
        
		btnMenu.requestFocus();
	}
	public static void DisableDownButton(boolean disable)
	{
		ButtonImageSwitchDown(btnDown.isFocused(),!disable);
	}
	
	public static void DisableUpButton(boolean disable)
	{
		ButtonImageSwitchUp(btnUp.isFocused(),!disable);
	}
	
	
    private static void ButtonImageSwitchUp(boolean press,boolean enabled)
    {
    	btnUp.setEnabled(enabled);
    	btnUp.setFocusable(enabled);    	
    	if(enabled==false)
    	{
    		int res=btnMenu.isFocused()?R.drawable.bnt_up_disable:R.drawable.bnt_up_disable;
    		btnUp.setBackgroundResource(res);
    		return;
    	}
    	
    	int resource=(press==true)?R.drawable.bnt_up_press:R.drawable.bnt_up_default;
    	int res=btnMenu.isFocused()?R.drawable.bnt_down_disable:resource;
		btnUp.setBackgroundResource(res);
    }
    
    private static void ButtonImageSwitchDown(boolean press,boolean enabled)
    {
    	btnDown.setEnabled(enabled);
    	btnDown.setFocusable(enabled);
    	if(enabled==false)
    	{
    		int res=btnMenu.isFocused()?R.drawable.bnt_down_disable:R.drawable.bnt_down_disable;
    		btnDown.setBackgroundResource(res);
    		return;
    	}
    	
    	int resource=(press==true)?R.drawable.bnt_down_press:R.drawable.bnt_down_default;
    	int res=btnMenu.isFocused()?R.drawable.bnt_menu_press_2:resource;
    	btnDown.setBackgroundResource(res);
    } 	 

    private static void ButtonImageSwitchSettings(boolean press,boolean enabled)
    {
    	mBtnSettings.setEnabled(enabled);
    	mBtnSettings.setFocusable(enabled);
    	if(enabled==false)
    	{
    		int res=btnMenu.isFocused()?R.drawable.bnt_down_disable:R.drawable.bnt_down_disable;
    		mBtnSettings.setBackgroundResource(res);
    		return;
    	}
    	
    	int resource=(press==true)?R.drawable.panel_button_selected:R.drawable.panel_button_normal;
    	int res=btnMenu.isFocused()?R.drawable.bnt_menu_press_2:resource;
    	mBtnSettings.setBackgroundResource(res);
    } 	 

    private static void ButtonImageSwitchMenu(boolean press)
    {
		if(press)
		{            
			btnMenu.setBackgroundResource(R.drawable.panel_button_selected);
                    
			int resource=(btnDown.isEnabled()==true)?R.drawable.bnt_menu_press_2:R.drawable.bnt_menu_press_3;
			btnDown.setBackgroundResource(resource);
			
			resource=(btnUp.isEnabled()==true)?R.drawable.bnt_menu_press_0:R.drawable.bnt_menu_press_4;
			btnUp.setBackgroundResource(resource);
	
			
		}else{        
		
			btnMenu.setBackgroundResource(R.drawable.panel_button_normal);

			int resource=(btnDown.isEnabled()==true)?R.drawable.bnt_down_default:R.drawable.bnt_down_disable;
			btnDown.setBackgroundResource(resource);
			
			resource=(btnUp.isEnabled()==true)?R.drawable.bnt_up_default:R.drawable.bnt_up_disable;
			btnUp.setBackgroundResource(resource);			
		}
    }    
    
    public static BroadcastReceiver mBroadcastReceiver=new BroadcastReceiver()
    {

		@Override
		public void onReceive(Context c, Intent i) 
		{
			if(i.getAction().equals(Resource.LAUNCHER4_ONTOUCHEVENT_BAR)==true)
			{
				//Toast.makeText(c, ""+btnDown.is, Toast.LENGTH_LONG).show();
				int id=i.getIntExtra("id",-1);
				int event=i.getIntExtra("event", -1);
				if(id==-1 || event==-1)
				{
					return;
				}

				switch(id)
				{
					case R.id.btnDown:
					{
						if(event==MotionEvent.ACTION_DOWN)
						{
							ButtonImageSwitchDown(true,btnDown.isEnabled());
						}else if(event==MotionEvent.ACTION_UP){
							ButtonImageSwitchDown(false,btnDown.isEnabled());
						}					
					}
					break;
					case R.id.menu:
					{
							ButtonImageSwitchSettings(true,mBtnSettings.isEnabled());
					}
					break;
					case R.id.ctlHandle:
					{
						if(event==MotionEvent.ACTION_DOWN)
						{
							ButtonImageSwitchMenu(true);
							if(mlSlidingDrawer.isOpened())
							{
								if(mScreenOneLayout!=null)mScreenOneLayout.setVisibility(View.VISIBLE);
							}
						}else if(event==MotionEvent.ACTION_UP){
							ButtonImageSwitchMenu(false);
					        final Handler handler=new Handler();
					        handler.postDelayed(new Runnable()
					        {
								@Override
								public void run() {
									if(mlSlidingDrawer.isMoving()==false)
									{
										if(mlSlidingDrawer.isOpened()==true)
										{
											if(mScreenOneLayout!=null)mScreenOneLayout.setVisibility(View.INVISIBLE);
										}
									}else{
										handler.postDelayed(this, 10);
									}
								}
					        }, 0);
						}
					}
					break;			
					case R.id.btnUp:
					{
						if(event==MotionEvent.ACTION_DOWN)
						{
							ButtonImageSwitchUp(true,btnUp.isEnabled());
						}else if(event==MotionEvent.ACTION_UP){
							ButtonImageSwitchUp(false,btnUp.isEnabled());
						}					
					}
					break;			
					
				}
			}else if(i.getAction().equals(Resource.LAUNCHER4_ONCLICK_BAR)==true)
			{
				int id=i.getIntExtra("id",-1);
				if(id==-1)
				{
					return;
				}
				if(id!=R.id.ctlHandle)
				{
					BtnClick(id);
				}
			}
		}
    };	
	
    private static OnFocusChangeListener BtnFocusChange=new OnFocusChangeListener()
    {
		@Override
		public void onFocusChange(View view, boolean b) {
			int id=view.getId();
			switch(id)
			{
				case R.id.menu:
				{
					ButtonImageSwitchSettings(b,mBtnSettings.isEnabled());
				}
				break;
				case R.id.btnDown:
				{
					ButtonImageSwitchDown(b,btnDown.isEnabled());
				}
				break;
				case R.id.btnUp:
				{
					ButtonImageSwitchUp(b,btnUp.isEnabled());
				}
				break;
				case R.id.ctlHandle:
				{
					ButtonImageSwitchMenu(b);
				}
				break;			
			
			}
		}
    };
    
    private static void BtnClick(int id)
    {
		switch(id)
		{
			case R.id.btnDown:
			{
				Intent intent=new Intent((mlSlidingDrawer.isOpened()==true)?
							Resource.LAUNCHER4_SCREENTWO_PAGECHANGE:
							Resource.LAUNCHER4_SCREENONE_PAGECHANGE
						);
				intent.putExtra("PAGE_CHANGE", 1);
				mContext.sendBroadcast(intent);
			}
			break;
			case R.id.menu:
			{
//				mBtnSettings.setBackgroundResource(R.drawable.panel_button_normal);
				Intent intent = new Intent(Intent.ACTION_MAIN);
		        intent.addCategory(Intent.CATEGORY_LAUNCHER);
				intent.setClassName("com.android.settings","com.android.settings.Settings");
				mContext.startActivity(intent);
			}
			break;
			case R.id.ctlHandle:
			{
				if(mlSlidingDrawer.isMoving()==false)
				{
					if(mlSlidingDrawer.isOpened()==true)
					{
//						mlSlidingDrawer.animateClose();
						if(mScreenOneLayout!=null)mScreenOneLayout.setVisibility(View.VISIBLE);
					}else{
//						mlSlidingDrawer.animateOpen();
						if(mScreenOneLayout!=null)mScreenOneLayout.setVisibility(View.INVISIBLE);
					}
				}
				Intent intent=new Intent(Resource.LAUNCHER4_SCREENONE_PAGECHANGE);
				intent.putExtra("PAGE_CHANGE", 0);
				mContext.sendBroadcast(intent);
			}
			break;			
			case R.id.btnUp:
			{
				Intent intent=new Intent((mlSlidingDrawer.isOpened()==true)?
						Resource.LAUNCHER4_SCREENTWO_PAGECHANGE:
						Resource.LAUNCHER4_SCREENONE_PAGECHANGE
					);
				intent.putExtra("PAGE_CHANGE", 0);
				mContext.sendBroadcast(intent);						
			}
			break;			
		
		}
   	}    	
    
    public static void CloseBar()
    {
		if(mlSlidingDrawer.isMoving()==false)
		{
			if(mlSlidingDrawer.isOpened()==true)
			{
				mlSlidingDrawer.animateClose();
				if(mScreenOneLayout!=null)mScreenOneLayout.setVisibility(View.VISIBLE);
			}
		}    
    }
    
    private static OnKeyListener BtnOnKey=new OnKeyListener()
    {

		@Override
		public boolean onKey(View view, int i, KeyEvent key) {
			int id=view.getId();
			if(i==KeyEvent.KEYCODE_ENTER && key.getAction()==KeyEvent.ACTION_UP)
			{
				BtnClick(id);
			}
			return false;
		}
    };    
}
