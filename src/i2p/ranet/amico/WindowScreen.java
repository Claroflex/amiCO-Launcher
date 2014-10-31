package i2p.ranet.amico;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;

public class WindowScreen{
	private Context context;
	private DisplayMetrics dm = new DisplayMetrics();
	public WindowScreen(Context c)
	{
		context=c;
		((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
	}
	public int GetScreenWidth()
	{
		return dm.widthPixels;
	}
	
	public int GetScreenHeight()
	{
		return dm.heightPixels;
	}
	
	public float GetScreenDensity()
	{
		return dm.density;
	}	
	
	public float GetStatusBarHeight()
	{
//    	return (float)((Integer.parseInt(context.getString(android.R.string.status_bar_height_pre))/100.0)*dm.heightPixels);
	return (float) 40;
	}	
	public float GetTitleBarHeight()
	{
//		String str_bar_height[]=context.getString(android.R.dimen.title_bar_height).split("dip");
//		return Float.parseFloat(str_bar_height[0])*dm.density;
		return (float) 10;
	}
}
