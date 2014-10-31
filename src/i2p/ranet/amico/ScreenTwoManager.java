package i2p.ranet.amico;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.View.OnKeyListener;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;



public class ScreenTwoManager implements OnTouchListener {

	private static final String TAG="AmiCO/ScreenTwoManager";
	
	private static final int PAGE_CHANGE_UP=0;
	private static final int PAGE_CHANGE_DOWN=1;
	
	private Context mContext;
	private ScrollView mMainScroll=null;
	private LinearLayout mMainLayout=null;
	private LinearLayout mChiledLayout=null;
	private FrameLayout mScreenTwoLayout=null;
	private int mBtnHeight;
	private int mBtnWidth;
	private int mScreenWidth;
	private int mScreenHeight;
	private PackageButton mBtn;

	private Handler mFocusChanged =  new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
		    switch(msg.arg1)
		    {
		    	case Resource.MESSAGE_FLAG_FOCUS_CHANGED:
		    	{
 					int res=mMainScroll.getScrollY();
					ScrollStatusChange(res);	
				}
			}

			super.handleMessage(msg);
		}
	};

	public BroadcastReceiver mBroadcastReceiver=new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context c, Intent i) 
		{
			if(i.getAction().equals(Resource.LAUNCHER4_SCREENTWO_PAGECHANGE)==true)
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
				}
			}else if(i.getAction().equals(Resource.LAUNCHER4_SCREEN_CHANDED)==true)
			{
			
				if(FunctionBarService.isOpened()==true)
				{
					int res=mMainScroll.getScrollY();
					ScrollStatusChange(res);					
				}
			}
		}
	};		
	
    
	public void start()
	{
        IntentFilter intentFilter = new IntentFilter(Resource.LAUNCHER4_SCREENTWO_PAGECHANGE);
        intentFilter.addAction(Resource.LAUNCHER4_SCREEN_CHANDED);
        mContext.registerReceiver(mBroadcastReceiver, intentFilter);
        
	}
	
	public void stop()
	{
		mContext.unregisterReceiver(mBroadcastReceiver);
	}
	
	public void reflash(List<ApplicationInfo> applicationInfos)
	{
//		mScreenWidth=AmiCO.mScreenWidth-FunctionBarService.getBarWidth();	//Ignat
		mScreenWidth=AmiCO.mScreenWidth-(int)(FunctionBarService.getBarWidth()/2);	//Ignat
		mScreenHeight=AmiCO.mScreenHeight;
		if(getStatusBarModeValue()==Resource.STATUS_BAR_MODE_ALWAYS_SHOW)
		{
//			mScreenHeight= AmiCO.mScreenHeight-((int)(AmiCO.mScreenHeight * (Integer.parseInt(mContext.getString(android.R.string.status_bar_height_pre))/100.0)));
			mScreenHeight= AmiCO.mScreenHeight-((int)(AmiCO.mScreenHeight * (11/100.0)));
//			mScreenHeight= 11;
		}

		mBtnWidth=mScreenWidth/5;
		mBtnHeight=mScreenHeight/6;
	
		if(mMainScroll==null)
		{
			mMainScroll=new ScrollView(mContext);
			mMainScroll.setVerticalScrollBarEnabled(false);
			mMainScroll.setHorizontalScrollBarEnabled(false);	
			mMainScroll.setOverScrollMode(View.OVER_SCROLL_NEVER);
			mMainScroll.setOnTouchListener(this);
		}else{
			mMainScroll.removeAllViews();
		}

		if(mMainLayout==null)
		{
			mMainLayout=new LinearLayout(mContext);
			mMainLayout.setOrientation(LinearLayout.VERTICAL);
		}else{
			mMainLayout.removeAllViews();
		}
		
		if(mChiledLayout==null)
		{
			mChiledLayout=new LinearLayout(mContext);
			mChiledLayout.setOrientation(LinearLayout.HORIZONTAL);
		}else{
			mChiledLayout.removeAllViews();
		}
		mScreenTwoLayout.removeAllViews();
		mScreenTwoLayout.addView(mMainScroll);
		AddPackages(applicationInfos);	
	}

	public ScreenTwoManager(Context context,FrameLayout layout ,List<ApplicationInfo> applicationInfos)
	{
		mContext=context;
		mScreenTwoLayout=layout;	
		reflash(applicationInfos);
	}

	private boolean isShowStatusBarChecked()
	{
		return false;//Settings.System.getInt(mContext.getContentResolver(), SettingsActivity.LAUNCHER4_SETTINGS_SHOW_STATUS_BAR,0)>0;
	}	
	private void AddPackages(List<ApplicationInfo> applicationInfos)
	{
		int textSize=Function.getIntSize(16,24,24);
		if(getStatusBarModeValue()==Resource.STATUS_BAR_MODE_ALWAYS_SHOW)
		{
			textSize=Function.getIntSize(14,21,22);
		}	
		for(ApplicationInfo app : applicationInfos)
		{
			int flage=PackageButton.MENU_ADD | PackageButton.MENU_REMOVE | PackageButton.MENU_NAVIGATION | PackageButton.MENU_NET | PackageButton.MENU_FUN;
			mBtn=new PackageButton(mContext,app,mBtnWidth-10,mBtnHeight,textSize,flage,mFocusChanged);
			mBtn.setPadding(5, Function.getIntSize(1,4,6), 5, Function.getIntSize(2,3,6));
			mChiledLayout.addView(mBtn);
			
			if(mChiledLayout.getChildCount()==5) 
			{
				mMainLayout.addView(mChiledLayout);
				mChiledLayout=new LinearLayout(mContext);
				mChiledLayout.setOrientation(LinearLayout.HORIZONTAL);	
			}
		}
		if(mChiledLayout.getChildCount()!=0)
		{
			mMainLayout.addView(mChiledLayout);
		}
		mMainScroll.addView(mMainLayout);
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
				mGetScrollStartY=mMainScroll.getScrollY();
			}
			break;
			case MotionEvent.ACTION_UP:
			{
				
				if(mGetScrollStartY==mMainScroll.getScrollY())
					//mMainScroll.getScrollY()==0 || 
				   //mMainScroll.getScrollY()==mMainLayout.getHeight()-mMainScroll.getHeight() ||
				{
					ScrollStatusChange(mMainScroll.getScrollY());
					return false;
				}				

				//set range
				int PageChangeRange=Function.getIntSize(5,10,15);
				
				int getEventEndY=(int) event.getY();
				if(mGetEventStartY>getEventEndY)
				{
					if(mGetEventStartY-getEventEndY>PageChangeRange)
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
					if(getEventEndY-mGetEventStartY>PageChangeRange)
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
	
		int move=mBtn.getHeight()*4;
		if(move==0)return;
		int ScrollStartY=mMainScroll.getScrollY();
		int Page=ScrollStartY/mScreenHeight+1;
		int ScrollMove=Page*move-ScrollStartY;
		mMainScroll.smoothScrollBy(0, ScrollMove);
		int res=ScrollStartY+ScrollMove;
		ScrollStatusChange(res);		
	}
	
	private void PageUp()
	{
		int move=mBtn.getHeight()*4;
		if(move==0)return;
		int ScrollStartY=mMainScroll.getScrollY();
		int Page=ScrollStartY/mScreenHeight;
		if(ScrollStartY%mScreenHeight==0)Page--;
		int ScrollMove=Page*move-ScrollStartY;
		mMainScroll.smoothScrollBy(0, ScrollMove);
		int res=ScrollStartY+ScrollMove;
		ScrollStatusChange(res);

	}
	
	private void ScrollStatusChange(int point)
	{
		int scrollheight=mMainLayout.getHeight()-mMainScroll.getHeight();
		if(FunctionBarService.isOpened()==true)
		{
			if(mMainLayout.getHeight()<=mMainScroll.getHeight())
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
	
	private int getStatusBarModeValue()
	{
		return Settings.System.getInt(mContext.getContentResolver(), Resource.LAUNCHER4_SETTINGS_STATUS_BAR_MODE,0);
	}	
}
