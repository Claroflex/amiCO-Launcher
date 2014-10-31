package i2p.ranet.amico;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class DialogQuickActivity extends AlertDialog implements OnClickListener	{
									
	private static final int ID_QUICK_ACTIVITY_L=0;
	private static final int ID_QUICK_ACTIVITY_R=1;
	private static final int ID_QUICK_ACTIVITY_A=2;
	private String mPackageName;
	private String mClassName;
	
	public DialogQuickActivity(Context c,String PackageName,String ClassName)
	{
		super(c);

		mPackageName=PackageName;
		mClassName=ClassName;
		ApplicationInfo mApplicationInfo=null;
//Quick Activity L
		mApplicationInfo=Option.QuickActivity.GetLiftActtivity(getContext());
		Button button_quick_L=new Button(getContext());
		if(mApplicationInfo==null)
		{
			button_quick_L.setText(getContext().getString(R.string.sliding_left)+" : null");
		}else{
			button_quick_L.setText(getContext().getString(R.string.sliding_left)+" : " + mApplicationInfo.title);			
		}

		button_quick_L.setId(ID_QUICK_ACTIVITY_L);
		button_quick_L.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		button_quick_L.setOnClickListener(this);
		
//Quick Activity R
		mApplicationInfo=Option.QuickActivity.GetRightActtivity(getContext());
		Button button_quick_R=new Button(getContext());
		if(mApplicationInfo==null)
		{
			button_quick_R.setText(getContext().getString(R.string.sliding_right)+" : null");
		}else{
			button_quick_R.setText(getContext().getString(R.string.sliding_right)+" : " + mApplicationInfo.title);			
		}
		button_quick_R.setId(ID_QUICK_ACTIVITY_R);
		button_quick_R.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		button_quick_R.setOnClickListener(this);
//Quick Activity A
		mApplicationInfo=Option.QuickActivity.GetAutoActtivity(getContext());
		Button button_quick_A=new Button(getContext());
		if(mApplicationInfo==null)
		{
			button_quick_A.setText(getContext().getString(R.string.autorun)+" : null");
		}else{
			button_quick_A.setText(getContext().getString(R.string.autorun)+" : " + mApplicationInfo.title);			
		}		

		button_quick_A.setId(ID_QUICK_ACTIVITY_A);
		button_quick_A.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
		button_quick_A.setOnClickListener(this);
				
		LinearLayout layout=new LinearLayout(getContext());
		layout.setOrientation(LinearLayout.VERTICAL);
		layout.addView(button_quick_L);
		layout.addView(button_quick_R);
		if(Option.isAutoRunEnable(getContext())==true)
		{
			layout.addView(button_quick_A);
		}
		
		ScrollView mScrollView=new ScrollView(getContext());
		mScrollView.addView(layout);

		setIcon(android.R.drawable.ic_dialog_info);
		setTitle(getContext().getString(R.string.autorun_settings));
		setView(mScrollView);
	}
	
/*Listener*/	
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{

			case ID_QUICK_ACTIVITY_L:
			{
				dismiss();
				Option.QuickActivity.SetLiftActtivity(getContext(), mPackageName,mClassName);
				
			}
			break;
			case ID_QUICK_ACTIVITY_R:
			{
				dismiss();
				Option.QuickActivity.SetRightActtivity(getContext(),  mPackageName,mClassName);		
			}
			break;
			case ID_QUICK_ACTIVITY_A:
			{
				dismiss();
				Option.QuickActivity.SetAutoActtivity(getContext(),  mPackageName,mClassName);		
			}
			break;								
		}

	}

}