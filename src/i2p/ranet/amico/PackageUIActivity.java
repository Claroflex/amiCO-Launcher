package i2p.ranet.amico;

import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.MotionEvent;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;     
import android.widget.Toast;



public class PackageUIActivity extends Activity {
    /** Called when the activity is first created. */
	public static boolean isActivity;

	private List<ApplicationInfo> mAppList;
	private PackagesManager mPackagesManager;
	private Context mContext;
	private PackageButton mBtn;
	private int mBtnHeight;
	private int mBtnWidth;
	private int mScreenWidth;
	private int mScreenHeight;
	private ScrollView mPackagesScroll=null;
	private LinearLayout mPackagesLayout=null;

	
	private Handler mFocusChanged =  new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
		    switch(msg.arg1)
		    {
		    	case Resource.MESSAGE_FLAG_FOCUS_CHANGED:
		    	{
					int res=mPackagesScroll.getScrollY();
//					ScrollStatusChange(res);
				}
			}

			super.handleMessage(msg);
		}
	};	
	
	public void PackageUIActivity(Context context){
		mContext=context;
		mPackagesManager=new PackagesManager(mContext); 
		List<ApplicationInfo> appList= mPackagesManager.getActivityPackages();
		mAppList = appList;
		mScreenWidth=AmiCO.mScreenWidth-FunctionBarService.getBarWidth();		
		mScreenHeight=AmiCO.mScreenHeight;
		
		mBtnWidth=mScreenWidth/4;
		mBtnHeight=mScreenHeight/4;
		
		
		/* app layout*/
		if(mPackagesScroll==null)
		{
			mPackagesScroll=new ScrollView(mContext);
//			mPackagesScroll.setOnTouchListener(this);
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
		

		for(int i=0;i<mAppList.size();i++)
		{
			ApplicationInfo mApp=mAppList.get(i);
//			Toast.makeText(context, ""+mApp.title, Toast.LENGTH_LONG).show(); //Ignat
			int textSize=Function.getIntSize(16,24,24);
			int flage=PackageButton.MENU_DELETE | PackageButton.MENU_REMOVE;
			mBtn=new PackageButton(mContext,mApp,mBtnWidth-10,mBtnHeight,textSize,flage,mFocusChanged);
			mBtn.setPadding(5, Function.getIntSize(1,4,6), 5, Function.getIntSize(2,3,6));
			mPackagesLayout.addView(mBtn);
			
		}
		mPackagesScroll.addView(mPackagesLayout);
//		mMainLayout=(LinearLayout)this.findViewById(R.layout.main2);

	}
	

    @Override
    public void onResume() {
    	super.onResume();
		setProgressBarIndeterminateVisibility(false); 
    }	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.main2);
	    
//        mMainLayout=(LinearLayout)this.findViewById(R.layout.main2);
        
        setProgressBarIndeterminateVisibility(false);

  //      PackageUIActivity(this);
        isActivity=true;
    }
}