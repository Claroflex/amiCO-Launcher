package i2p.ranet.amico;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.widget.Toast;

import i2p.ranet.amico.Tools.*;


public class ToolsMenuActivity extends PreferenceActivity implements OnPreferenceClickListener{
	private static final String KEY_GET_LOG="key_get_log";
	private static final String KEY_GET_RADIO_LOG="key_get_radio_log";	
	private static final String KEY_GET_DMESG="key_get_dmesg";
	private static final String KEY_GET_PACKAGES_INFO="key_get_packages_info";

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        
        addPreferencesFromResource(R.xml.toolsmenu);
        
        Preference mGetLog=findPreference(KEY_GET_LOG);
        mGetLog.setOnPreferenceClickListener(this);

        Preference mGetRadioLog=findPreference(KEY_GET_RADIO_LOG);
        mGetRadioLog.setOnPreferenceClickListener(this);
        
        Preference mGetDmesg=findPreference(KEY_GET_DMESG);
        mGetDmesg.setOnPreferenceClickListener(this);
        
        Preference mGetPackagesInfo=findPreference(KEY_GET_PACKAGES_INFO);
        mGetPackagesInfo.setOnPreferenceClickListener(this);
        
    }


	@Override
	public boolean onPreferenceClick(Preference Pref) {
		String KeyName=Pref.getKey();
		if(KeyName.equals(KEY_GET_LOG)==true)
		{
			LogCatThread mLogCatThread=new LogCatThread(this);
    		mLogCatThread.start();
    		
    	}else if(KeyName.equals(KEY_GET_RADIO_LOG)==true)
		{
			LogCatRadioThread mLogCatRadioThread=new LogCatRadioThread(this);
    		mLogCatRadioThread.start();
    		
		}else if(KeyName.equals(KEY_GET_DMESG)==true)
		{
			DmesgThread mDmesgThread=new DmesgThread(this);
    		mDmesgThread.start();
		}else if(KeyName.equals(KEY_GET_PACKAGES_INFO)==true)
		{
			PackagesInfoThread	 mPackagesInfoThread=new PackagesInfoThread(this);
    		mPackagesInfoThread.start();
		}
		return false; 
	}
}