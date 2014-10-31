package i2p.ranet.amico;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import android.os.Message;

public class PackageButton extends LinearLayout implements OnClickListener, OnLongClickListener, OnFocusChangeListener{
	private static final String TAG="AmiCO/PackageButton";
	public static boolean bScreenLock=false;
	public static final int MENU_ADD	=	1;
	public static final int MENU_DELETE	=	2;
	public static final int MENU_REMOVE	=	4;
	public static final int MENU_NAVIGATION	=	8;
	public static final int MENU_NET	=	16;
	public static final int MENU_FUN	=	32;
	
	private final LinearLayout layout;
	private final ImageView apk_bt;
	private final TextView apk_text;	
	private ApplicationInfo mApplicationInfo;
	private DialogSelectMenu mDialogSelectMenu;
	private Handler mHandler=null;
	private int mFlag=0;
	
	@Override
	public void setFocusable(boolean b)
	{
		layout.setFocusable(b);
	}
		
	public PackageButton(Context context,ApplicationInfo info,int Width,int Height,int textSzie,int flag,Handler handler) 
	{
		super(context);
		mHandler=handler;
        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.packagebutton, this, true);		
	
        mApplicationInfo=info;
//layout        
        layout=(LinearLayout)findViewById(R.id.apk_layout);
        layout.setOnClickListener(this);
        mFlag=flag;
		if(flag!=0)
		{
			layout.setOnLongClickListener(this);
		}           
       
//button        
        apk_bt=(ImageView)findViewById(R.id.apk_bt);
        Bitmap bitmap = Bitmap.createBitmap(  
       		mApplicationInfo.icon.getIntrinsicWidth(),  
       		mApplicationInfo.icon.getIntrinsicHeight(),  
       		mApplicationInfo.icon.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);  
			Canvas canvas = new Canvas(bitmap);  
			canvas.setBitmap(bitmap);  
			mApplicationInfo.icon.setBounds(0, 0, mApplicationInfo.icon.getIntrinsicWidth(), mApplicationInfo.icon.getIntrinsicHeight());  
			mApplicationInfo.icon.draw(canvas);  

        ///bitmap.setDensity(240);
        apk_bt.setImageBitmap(bitmap);
        apk_bt.setImageDrawable(mApplicationInfo.icon);

//text        
		float TextSize=textSzie;
        apk_text=(TextView)findViewById(R.id.apk_text);
        apk_text.setText(mApplicationInfo.title);
//        Toast.makeText(context, ""+mApplicationInfo.title, Toast.LENGTH_LONG).show(); //Ignat
		apk_text.setTextSize(TextSize);
		apk_bt.setLayoutParams(new LayoutParams(Width,Height));
		
       layout.setOnFocusChangeListener(this);		
	}
/**
 * Listener
 * */	

	@Override
	public void onClick(View v) {
		if(mDialogSelectMenu==null || mDialogSelectMenu.isShowing()==false)
		{
			((Activity) getContext()).setProgressBarIndeterminateVisibility(true);
			getContext().startActivity(mApplicationInfo.intent);
			((Activity) getContext()).overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
		}		
	}
	
	@Override
	public boolean onLongClick(View v) {
		if(bScreenLock==false)
		{
			mDialogSelectMenu=new DialogSelectMenu(v.getContext(),mApplicationInfo,mFlag);
			mDialogSelectMenu.show();
		}
		return false;
	}
	
	@Override
	public void onFocusChange(View view, boolean b) {
		if(b==true && mHandler!=null)
		{
		
			Message msg=new Message();
			msg.arg1=Resource.MESSAGE_FLAG_FOCUS_CHANGED;
			mHandler.sendMessageDelayed(msg, 300);
		}
		
	}	
}