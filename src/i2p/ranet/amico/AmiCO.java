package i2p.ranet.amico;

import i2p.ranet.amico.Activity_Help;
import i2p.ranet.amico.PackageUIActivity;
//import i2p.ranet.amico.PackagesManager;
import i2p.ranet.amico.UIListener;
import i2p.ranet.amico.FunctionBarService;
import i2p.ranet.amico.MySlidingDrawer;
import i2p.ranet.amico.ScreenManager;
//import i2p.ranet.amico.SystemProperties;
import i2p.ranet.amico.R;
import i2p.ranet.amico.util.SystemUiHider;
//import i2p.ranet.amico.DefaultSettings;
//import i2p.ranet.amico.Resource;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.view.View.OnClickListener;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class AmiCO extends Activity {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	//init
	private ScreenManager mScreenManager=null;
	private FrameLayout mScreenOne;
	private FrameLayout mScreenOneOne;
	private FrameLayout mScreenTwo;

	public static int mScreenWidth=0;
	public static int mScreenHeight=0;
	
	private TextView mInfoText;
	private Handler hSystemLoading = new Handler();
	private boolean IsInit=false;
	//private ImageButton btnNavigation;
	//private ImageButton btnSettings;
		
	//prop
	private static final String PROP_START = "user.amiCO.start";

	/**
	 *	Menu
	 *	Apk Manager , Help  
	 **/
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
	    	super.onCreateOptionsMenu(menu);
	    	// // new  Menu Item
			menu.add( 0, 0, 0, getString(R.string.open_apk_manager)).setIcon(R.drawable.all_apps_button_normal);
	    	menu.add( 0, 1, 0, getString(R.string.help)).setIcon(android.R.drawable.ic_dialog_info);
	    	return true;
			    	
	} 
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
	    	switch(item.getItemId())
	    	{
		    	case 0:
		    	{
//		        	startActivity(new Intent(this,PackageUIActivity.class));
		    	}
		    	break;
		    	case 1:
		    	{
		    		this.startActivity(new Intent(this,Activity_Help.class));//Activity the help page.
		    	}
		    	break;	  
		   	}
	    	return super.onOptionsItemSelected(item);
	}    
	    
	/**
	 *	Lock the [Back] key
	 *	In the favorite page , can't use the back key. 
	 **/    
	public boolean onKeyUp(int keyCode, KeyEvent event) 
	{
	       // if (keyCode == KeyEvent.KEYCODE_BACK && event.isTracking()
	         //       && !event.isCanceled()) {
	            // *** DO ACTION HERE ***
	           // return true;
	        //}
	        return super.onKeyUp(keyCode, event);
	}



	private void FullWindowEnabled()
	{
	        Window win = getWindow();
	        WindowManager.LayoutParams winParams = win.getAttributes();
	        
	        winParams.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
	        winParams.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
			
	        win.setAttributes(winParams);			
	}
	

/** 
 * Set the information text in full window
 * use mInfoText.setText(...) to set information text
 **/	
	private void CreateInformationText()
	{
        mInfoText  = new TextView(this);  
        mInfoText.setTextSize(24);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);  
        lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        mInfoText.setLayoutParams(lp);
	}                     
		
    private void LayoutInit()
    {
 //       SystemProperties.set(PROP_LAUNCHER_START, "1");
        
//    	RunStatusBarMode();
        setContentView(R.layout.activity_ami_co);
        
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		mScreenWidth=dm.widthPixels;
		mScreenHeight=dm.heightPixels;        
        
        MySlidingDrawer mlSlidingDrawer = (MySlidingDrawer)findViewById(R.id.slidingDrawer);
        ImageButton btnUp = (ImageButton)findViewById(R.id.btnUp);
        ImageButton btnDown = (ImageButton)findViewById(R.id.btnDown);
        ImageButton btnMenu= (ImageButton)findViewById(R.id.ctlHandle);
        ImageButton btnNavigation = (ImageButton)findViewById(R.id.navigation);
        ImageButton btnSettings = (ImageButton)findViewById(R.id.menu);
        ImageButton btnNet = (ImageButton)findViewById(R.id.net);
        ImageButton btnFun = (ImageButton)findViewById(R.id.fun);

        
        mScreenOne=(FrameLayout)this.findViewById(R.id.screen_one);
        mScreenOneOne=(FrameLayout)this.findViewById(R.id.screen_one_one);
        mScreenTwo=(FrameLayout)this.findViewById(R.id.screen_two);
        //start function bar
        FunctionBarService.start(this, mlSlidingDrawer, btnSettings, btnMenu, btnDown, btnUp);

        /* Run Screen Manager */
    	mScreenManager=new ScreenManager(AmiCO.this,mScreenOne,mScreenOneOne,mScreenTwo, btnNavigation, btnNet, btnFun);
    	Toast.makeText(this, ""+FunctionBarService.getBarWidth(), Toast.LENGTH_LONG).show();

	}	

    //main layout set
    private void Init()
	{   

					
/* Run UI Listener */		   
		UIListener.Init(this);
		UIListener.Start();
		IsInit=true;		
	}		
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// amiCO
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        //IsInit=false;		
    				
		FullWindowEnabled();
    	CreateInformationText();
    	LayoutInit();
    	Init();
	}
}

/*		final View controlsView = findViewById(R.id.fullscreen_content_controls);
//		final View controlsView = findViewById(R.id.screen_two);
		final View contentView = findViewById(R.id.fullscreen_content);
//		final View contentView = findViewById(R.id.screen_one);

				
		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView, HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
					// Cached values.
					int mControlsHeight;
					int mShortAnimTime;

					@Override
					@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
					public void onVisibilityChange(boolean visible) {
						if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
							// If the ViewPropertyAnimator API is available
							// (Honeycomb MR2 and later), use it to animate the
							// in-layout UI controls at the bottom of the
							// screen.
							if (mControlsHeight == 0) {
								mControlsHeight = controlsView.getHeight();
							}
							if (mShortAnimTime == 0) {
								mShortAnimTime = getResources().getInteger(
										android.R.integer.config_shortAnimTime);
							}
							controlsView
									.animate()
									.translationY(visible ? 0 : mControlsHeight)
									.setDuration(mShortAnimTime);
						} else {
							// If the ViewPropertyAnimator APIs aren't
							// available, simply show or hide the in-layout UI
							// controls.
							controlsView.setVisibility(visible ? View.VISIBLE : View.GONE);
						}

						if (visible && AUTO_HIDE) {
							// Schedule a hide().
							delayedHide(AUTO_HIDE_DELAY_MILLIS);
						}
					}
				});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}
*/
	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
/*
    	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	}; 

    	
	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};
*/
	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
  /*  	
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	} 
}
*/