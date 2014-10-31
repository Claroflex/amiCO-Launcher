package i2p.ranet.amico;


import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

public class DialogSelectMenu extends AlertDialog implements OnClickListener, DialogInterface.OnClickListener	{
	public static final int ID_TOOLS_ADD=1;
	public static final int ID_TOOLS_REMOVE=2;
	public static final int ID_TOOLS_DELETE=4;
	private static final int ID_TOOLS_QUICK=8;
	
	public static final int ID_TOOLS_ADD_NAVIGATION=16;
	public static final int ID_TOOLS_ADD_NET=32;
	public static final int ID_TOOLS_ADD_FUN=64;
	
	private ApplicationInfo mApplicationInfo;

	private boolean bAdd=false;
	private boolean bDelete=false;
	private boolean bRemove=false;
	
	private boolean bNavigation=false;
	private boolean bNet=false;
	private boolean bFun=false;
	
	private PackagesManager mPackagesManager;
	
	private void GetFlage(int flage)
	{
		if(flage==0)
		{
			return;
		}
		int value=flage;
		for(int i=0;i<6;i++)
		{
			int mod=value%2;
			value=value/2;
			switch(i)
			{
				case 0:
				{
					if(mod==1)
					{
						bAdd=true;
					}
				}
				break;
				case 1:
				{
					if(mod==1)
					{
						bDelete=true;
					}					
				}
				break;
				case 2:
				{
					if(mod==1)
					{
						bRemove=true;
					}					
				}
				break;
				case 3:
				{
					if(mod==1)
					{
						bNavigation=true;
					}					
				}
				break;
				case 4:
				{
					if(mod==1)
					{
						bNet=true;
					}					
				}
				break;
				case 5:
				{
					if(mod==1)
					{
						bFun=true;
					}					
				}
				break;
			}
		}

	}	
	public DialogSelectMenu(Context c,ApplicationInfo info,int flage)
	{
		super(c);
		mApplicationInfo=info;
		GetFlage(flage);
		mPackagesManager=new PackagesManager(c);
		
		ScrollView scrollView = new ScrollView(getContext());
		/** Create select menu tool button **/
		
		//Select Menu ADD button
		Button Button_Add=new Button(getContext());
		Button_Add.setId(ID_TOOLS_ADD);
		Button_Add.setText(getContext().getString(R.string.add_in_menu));
		Button_Add.setOnClickListener(this);
		
		//Select Menu ADD NAVIGATION button
		Button Button_Add_Navigation=new Button(getContext());
		Button_Add_Navigation.setId(ID_TOOLS_ADD_NAVIGATION);
		Button_Add_Navigation.setText(getContext().getString(R.string.add_in_navigation));
		Button_Add_Navigation.setOnClickListener(this);
		//Select Menu ADD NET button
		Button Button_Add_Net=new Button(getContext());
		Button_Add_Net.setId(ID_TOOLS_ADD_NET);
		Button_Add_Net.setText(getContext().getString(R.string.add_in_net));
		Button_Add_Net.setOnClickListener(this);
		//Select Menu ADD FUN button
		Button Button_Add_Fun=new Button(getContext());
		Button_Add_Fun.setId(ID_TOOLS_ADD_FUN);
		Button_Add_Fun.setText(getContext().getString(R.string.add_in_fun));
		Button_Add_Fun.setOnClickListener(this);
		
		//Select Menu REMOVE button
		Button Button_Remove=new Button(getContext());
		Button_Remove.setId(ID_TOOLS_REMOVE);
		Button_Remove.setText(getContext().getString(R.string.remove_from_menu));
		Button_Remove.setOnClickListener(this);

        //Select Menu DELETE button
		Button Button_Delete=new Button(getContext());
		Button_Delete.setId(ID_TOOLS_DELETE);		
		Button_Delete.setText(getContext().getString(R.string.delete_from_system));
		Button_Delete.setOnClickListener(this);

        //Select Menu QUICK button
		Button Button_Quick=new Button(getContext());
		Button_Quick.setId(ID_TOOLS_QUICK);
		Button_Quick.setText(getContext().getString(R.string.autorun_settings));
		Button_Quick.setOnClickListener(this);

		LinearLayout linearLayout=new LinearLayout(getContext());
		linearLayout.setOrientation(LinearLayout.VERTICAL);

		/**	Enable / Disable the select menu tool button.
		 *	Checked the tool button , if the tool button flage is true ,then create the tool button.		 
		 **/		
		//ADD		 
		if(bAdd==true)
		{
			Button_Add.setEnabled(true);
			//If the package in default save ,then set it is false
			String mApp=mApplicationInfo.intent.getComponent().getPackageName()+","+mApplicationInfo.intent.getComponent().getClassName();
			for(int k=0;k<Option.GetPackages.Default().size();k++)
			{
				ContentValues mContentValues=Option.GetPackages.Default().get(k);
				String DefaultApp=mContentValues.getAsString("PackageName")+","+mContentValues.getAsString("ClassName");
				if(DefaultApp.equals(mApp)==true)
				{
					Button_Add.setEnabled(false);
					break;
				}
			}			
			//If the package in favorite save ,then set it is false 
			for(int k=0;k<Option.GetPackages.Favorite(getContext()).size();k++)
			{
				ContentValues mContentValues=Option.GetPackages.Favorite(getContext()).get(k);
				String DefaultApp=mContentValues.getAsString("PackageName")+","+mContentValues.getAsString("ClassName");				
				if(DefaultApp.equals(mApp)==true)
				{
					Button_Add.setEnabled(false);
					break;
				}
			}
			
			linearLayout.addView(Button_Add);
		}
		//ADD Navigation	 
		if(bNavigation==true)
		{
			Button_Add_Navigation.setEnabled(true);
			linearLayout.addView(Button_Add_Navigation);
		}
		//ADD Net	 
		if(bNet==true)
		{
			Button_Add_Net.setEnabled(true);
			linearLayout.addView(Button_Add_Net);
		}
		//ADD Fun	 
		if(bFun==true)
		{
			Button_Add_Fun.setEnabled(true);
			linearLayout.addView(Button_Add_Fun);
		}
		//REMOVE
		if(bRemove==true)
		{
			Button_Remove.setEnabled(true);
			for(int i=0;i<Option.GetPackages.Default().size();i++)
			{
				ContentValues mContentValues=Option.GetPackages.Default().get(i);
				String DefaultApp=mContentValues.getAsString("PackageName")+","+mContentValues.getAsString("ClassName");	
				
				String mApp=mApplicationInfo.intent.getComponent().getPackageName()+","+mApplicationInfo.intent.getComponent().getClassName();
				if(DefaultApp.equals(mApp)==true)
				{
					Button_Remove.setEnabled(false);
					break;
				}
			}
			linearLayout.addView(Button_Remove);
		}
		
		//DELETE
		if(bDelete==true)
		{
			Button_Delete.setEnabled(true);
			
			if(mApplicationInfo.isSystemApp()==true)
			{
				Button_Delete.setEnabled(false);
			}
			linearLayout.addView(Button_Delete);
		}
		if(linearLayout.getChildCount()==0)
		{
			return;
		}
	
		//QUICK
		linearLayout.addView(Button_Quick);
		
		//Create the tools button to select menu.
		scrollView.addView(linearLayout);
		
		//Set the dialog icon.
		setIcon(android.R.drawable.ic_dialog_info);
		//Set the dialog Name.
		setTitle(mApplicationInfo.title);
		setView(scrollView);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId())
		{
			case ID_TOOLS_ADD:
			{
//				FavoriteAdd(mApplicationInfo.intent.getComponent().getPackageName(),mApplicationInfo.intent.getComponent().getClassName());
				dismiss();
			}
			break;
			case ID_TOOLS_ADD_NAVIGATION:
			{
				FavoriteAddNavigation(mApplicationInfo.intent.getComponent().getPackageName(),mApplicationInfo.intent.getComponent().getClassName());
				dismiss();
			}
			break;
			case ID_TOOLS_ADD_NET:
			{
				FavoriteAddNet(mApplicationInfo.intent.getComponent().getPackageName(),mApplicationInfo.intent.getComponent().getClassName());
				dismiss();
			}
			break;
			case ID_TOOLS_ADD_FUN:
			{
				FavoriteAddFun(mApplicationInfo.intent.getComponent().getPackageName(),mApplicationInfo.intent.getComponent().getClassName());
				dismiss();
			}
			break;
			case ID_TOOLS_REMOVE:
			{
				DialogRemove();
				dismiss();
			}
			break;
			case ID_TOOLS_DELETE:
			{
//				PackagesManager.PackageUnInstall(mApplicationInfo.intent.getComponent().getPackageName());
				dismiss();
			}
			break;
			case ID_TOOLS_QUICK:
			{
				dismiss();
				String mPackageName=mApplicationInfo.intent.getComponent().getPackageName();
				String mClassName=mApplicationInfo.intent.getComponent().getClassName();
				DialogQuickActivity dialogQuickActivity=new DialogQuickActivity(getContext(),mPackageName,mClassName);
				dialogQuickActivity.show();
				
			}
			break;	
		}
	}
	
/**
 *  Dialog Remove
 * */	
	public void DialogRemove()
	{
		AlertDialog dialog_remove= new AlertDialog.Builder(getContext())
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setTitle(getContext().getString(R.string.remove))
		.setMessage(getContext().getString(R.string.remove_msg))
		.setPositiveButton(getContext().getString(android.R.string.yes),this)
		.setNegativeButton(getContext().getString(android.R.string.no), null)
		.create();
		dialog_remove.show();
	}

	@Override
	public void onClick(DialogInterface dialog, int arg1) {
		switch(arg1)
		{	
			case DialogInterface.BUTTON_POSITIVE:
			{
				FavoriteRemove(mApplicationInfo.intent.getComponent().getPackageName(),mApplicationInfo.intent.getComponent().getClassName());
			}
			break;
		}
	}
	
/**
 *	Favorite Remove 
 **/	
	public void FavoriteRemove(String mPackageName,String mClassName)
	{
		List<String> mClassNames=new ArrayList<String>();
		List<String> mPackageNames=new ArrayList<String>();
		if(mClassNames.size()!=0 || mPackageNames.size()!=0)
		{
			mClassNames.clear();
			mPackageNames.clear();
		}
/* 		for(int i=0;i<PackagesManager.mApplications.size();i++)
		{
			if(PackagesManager.mApplications.get(i).intent.getComponent().getClassName().equals(mClassName)==true &&
			   PackagesManager.mApplications.get(i).intent.getComponent().getPackageName().equals(mPackageName)==true)
			{
				PackagesManager.mApplications.get(i).setToFavorite(false);
				break;
			}
		}*/
	
		Option.SetPackages.FavoriteDelete(getContext(),mPackageName,mClassName);
//		FavoriteUIManager.Create();
	}	
/**
 *	Favorite Add 
 **/
	public void FavoriteAdd(String mPackageName,String mClassName)
	{
		List<String> mClassNames=new ArrayList<String>();
		List<String> mPackageNames=new ArrayList<String>();
		if(mClassNames.size()!=0 || mPackageNames.size()!=0)
		{
			mClassNames.clear();
			mPackageNames.clear();
		}
 		for(int i=0;i< mPackagesManager.getActivityPackages().size();i++)
		{
			if(mPackagesManager.getActivityPackages().get(i).intent.getComponent().getClassName().equals(mClassName)==true &&
					mPackagesManager.getActivityPackages().get(i).intent.getComponent().getPackageName().equals(mPackageName)==true)
			{
				mPackagesManager.getActivityPackages().get(i).setToFavorite(true);
				break;
			}
		}
		
		Option.SetPackages.FavoriteAdd(getContext(),mPackageName,mClassName);
//		FavoriteUIManager.Create();
	}
	
	public void FavoriteAddNavigation(String mPackageName,String mClassName)
	{
		List<String> mClassNames=new ArrayList<String>();
		List<String> mPackageNames=new ArrayList<String>();
		if(mClassNames.size()!=0 || mPackageNames.size()!=0)
		{
			mClassNames.clear();
			mPackageNames.clear();
		}
 		for(int i=0;i< mPackagesManager.getActivityPackages().size();i++)
		{
			if(mPackagesManager.getActivityPackages().get(i).intent.getComponent().getClassName().equals(mClassName)==true &&
					mPackagesManager.getActivityPackages().get(i).intent.getComponent().getPackageName().equals(mPackageName)==true)
			{
				mPackagesManager.getActivityPackages().get(i).setToNavigation(true);
				break;
			}
		}
		
		Option.SetPackages.NavigationAdd(getContext(),mPackageName,mClassName);
//		FavoriteUIManager.Create();
	}
	public void FavoriteAddNet(String mPackageName,String mClassName)
	{
		List<String> mClassNames=new ArrayList<String>();
		List<String> mPackageNames=new ArrayList<String>();
		if(mClassNames.size()!=0 || mPackageNames.size()!=0)
		{
			mClassNames.clear();
			mPackageNames.clear();
		}
 		for(int i=0;i< mPackagesManager.getActivityPackages().size();i++)
		{
			if(mPackagesManager.getActivityPackages().get(i).intent.getComponent().getClassName().equals(mClassName)==true &&
					mPackagesManager.getActivityPackages().get(i).intent.getComponent().getPackageName().equals(mPackageName)==true)
			{
				mPackagesManager.getActivityPackages().get(i).setToNet(true);
				break;
			}
		}
		
//		Option.SetPackages.NetAdd(getContext(),mPackageName,mClassName);
//		FavoriteUIManager.Create();
	}

	public void FavoriteAddFun(String mPackageName,String mClassName)
	{
		List<String> mClassNames=new ArrayList<String>();
		List<String> mPackageNames=new ArrayList<String>();
		if(mClassNames.size()!=0 || mPackageNames.size()!=0)
		{
			mClassNames.clear();
			mPackageNames.clear();
		}
 		for(int i=0;i< mPackagesManager.getActivityPackages().size();i++)
		{
			if(mPackagesManager.getActivityPackages().get(i).intent.getComponent().getClassName().equals(mClassName)==true &&
					mPackagesManager.getActivityPackages().get(i).intent.getComponent().getPackageName().equals(mPackageName)==true)
			{
				mPackagesManager.getActivityPackages().get(i).setToFun(true);
				break;
			}
		}
		
//		Option.SetPackages.FunAdd(getContext(),mPackageName,mClassName);
//		FavoriteUIManager.Create();
	}
	
	
}