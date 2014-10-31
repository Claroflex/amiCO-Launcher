package i2p.ranet.amico;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;;

public class MySlidingDrawer extends SlidingDrawer implements OnDrawerCloseListener,  OnDrawerOpenListener{
    private int mHandleId = 0;        
    private int[] mTouchableIds = null;
     
    public int[] getTouchableIds() {
        return mTouchableIds;
    }
 
    public void setTouchableIds(int[] mTouchableIds) {
        this.mTouchableIds = mTouchableIds;
    }
 
    public int getHandleId() {
        return mHandleId;
    }
 
    public void setHandleId(int mHandleId) {
        this.mHandleId = mHandleId;
    }
 
    public MySlidingDrawer(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnDrawerOpenListener(this);
        setOnDrawerCloseListener(this);
    }
     
    public MySlidingDrawer(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);
    }
     
    public Rect getRectOnScreen(View view){
        Rect rect = new Rect();
        int[] location = new int[2];
        View parent = view;
        if(view.getParent() instanceof View){
            parent = (View)view.getParent();
        }
        parent.getLocationOnScreen(location);
        view.getHitRect(rect);
        rect.offset(location[0], location[1]);
         
        return rect;
    }
    
    private static final int CLICK_TIME_OUT=300;
    private int mFocusId=0;
    private boolean mIdClick=false;
    private Handler hClcik=new Handler();
    

    private Runnable rClick=new Runnable()
    {
		@Override
		public void run() {
			mIdClick=false;
		}
    };
    
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int[] location = new int[2];
        int x = (int)event.getX();
        int y = (int)event.getY();
        this.getLocationOnScreen(location);
        x += location[0];
        y += location[1];
         
        if(mTouchableIds != null){
            for(int id : mTouchableIds){
                View view = findViewById(id);
                Rect rect = getRectOnScreen(view);
                if(rect.contains(x,y)){
                    boolean result = view.dispatchTouchEvent(event);
                    mFocusId=id;
                    if(event.getAction() == MotionEvent.ACTION_DOWN)
                    {
	                    mIdClick=true;
	                    hClcik.removeCallbacks(rClick);
	                    hClcik.postDelayed(rClick, CLICK_TIME_OUT);
                    }
                    SendEventFormBroadcase(this.getContext(),event);
                    return false;
                }
            }
        }
         
        if(event.getAction() == MotionEvent.ACTION_DOWN && mHandleId != 0){
            View view = findViewById(mHandleId);
            Rect rect = getRectOnScreen(view);
            if(rect.contains(x, y))
            {
                Log.i("MySlidingDrawer", "Hit handle");
                mFocusId=mHandleId;
                if(event.getAction() == MotionEvent.ACTION_DOWN)
                {
                    mIdClick=true;
                    hClcik.removeCallbacks(rClick);
                    hClcik.postDelayed(rClick, CLICK_TIME_OUT);
                }
                //SendEventFormBroadcase(this.getContext(),event.getAction());                
            }else{
                return false;
            }
        }

        if(event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_DOWN)
        {
        	SendEventFormBroadcase(this.getContext(),event);
        }        
        return super.onInterceptTouchEvent(event);
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {

    	if(event.getAction()==MotionEvent.ACTION_UP)
    	{
    		SendEventFormBroadcase(this.getContext(),event);
    	}
        return super.onTouchEvent(event);
    }
    
    //send broadcase to launcher4
    private void SendEventFormBroadcase(Context context,MotionEvent event)
    {
    	View view = findViewById(mFocusId);
		if(view==null)
		{
			return;
		}
    	int x=(int) event.getX();
    	int[] res = new int[2];
    	view.getLocationInWindow(res);
    	if(x<res[0] || x>(res[0]+view.getRight()))
    	{
    		return;
    	}
    	
    	//change button image
    	Intent intent=new Intent(Resource.LAUNCHER4_ONTOUCHEVENT_BAR);
        intent.putExtra("id", mFocusId);
        intent.putExtra("event", event.getAction());
        getContext().sendBroadcast(intent);

        //button click check
        if(event.getAction()==MotionEvent.ACTION_UP)
        {
	        hClcik.removeCallbacks(rClick);        
			if(mIdClick==true)
			{
		        intent=new Intent(Resource.LAUNCHER4_ONCLICK_BAR);
		        intent.putExtra("id", mFocusId);
		        getContext().sendBroadcast(intent);    			
			}        
        }
    }
    
	@Override
	public void onDrawerOpened() {
		Intent intent=new Intent(Resource.LAUNCHER4_SCREEN_CHANDED);
		this.getContext().sendBroadcast(intent);
	}

	@Override
	public void onDrawerClosed() {
		Intent intent=new Intent(Resource.LAUNCHER4_SCREEN_CHANDED);
		this.getContext().sendBroadcast(intent);
	}

}
