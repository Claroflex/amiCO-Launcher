package i2p.ranet.amico;

import android.content.pm.PackageManager.NameNotFoundException;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import i2p.ranet.amico.SystemProperties;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.Menu;
import android.view.MenuItem;
                    
import i2p.ranet.amico.DefaultSettings;
import i2p.ranet.amico.Resource;                    

public class Activity_Help extends PreferenceActivity{
	private static final int USE_TOOLS=1;

	private static final String KEY_ICON="key_icon";
	private static final String PROP_CUSTORMER_VERSION="ro.build.version.customer";
	private static final String PROP_FW_VERSION="user.wp.fw_version";
    private static final String KEY_ANDROID_VER = "key_android_ver";
    private static final String KEY_CUSTOMER_VER = "key_customer_ver";
    private static final String KEY_FW_VER = "key_fw_ver";
    private Preference mAndroidVer;
    private Preference mCustomerVer;
    private Preference mFWVer;
    
    
    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        addPreferencesFromResource(R.xml.preferencescreen_help);

    	mAndroidVer = findPreference(KEY_ANDROID_VER);
    	mCustomerVer = findPreference(KEY_CUSTOMER_VER);
    	mFWVer = findPreference(KEY_FW_VER);

    	mAndroidVer.setSummary(Build.VERSION.RELEASE+"("+Build.VERSION.SDK+")");
		mCustomerVer.setSummary(SystemProperties.get(PROP_CUSTORMER_VERSION,android.os.Build.UNKNOWN));
    	mFWVer.setSummary(SystemProperties.get(PROP_FW_VERSION,android.os.Build.UNKNOWN));

//remove		
		PreferenceScreen root = getPreferenceScreen();
		if(DefaultSettings.getInt(Resource.LAUNCHER_HELP_LOGO,0)==0)root.removePreference(findPreference(KEY_ICON));	        
    }
     
    @Override
    public boolean onCreateOptionsMenu(Menu menu) 
    {
    	super.onCreateOptionsMenu(menu);
    	if(USE_TOOLS==1)
    	{
			menu.add( 0, 0, 0, "Use tools");
		}
    	return true;
    }   
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
    	switch(item.getItemId())
    	{
	    	case 0:
	    	{
				startActivity(new Intent(this,ToolsMenuActivity.class));	    	
	    	}
	    	break;
	   	}
    	return super.onOptionsItemSelected(item);
    }       

}
