package i2p.ranet.amico;

public class Resource {
	
	public static final int STATUS_BAR_MODE_ALWAYS_HIDE=0;
	public static final int STATUS_BAR_MODE_ALWAYS_SHOW=1;
	public static final int STATUS_BAR_MODE_AUTO_HIDE_SHOW=2;
	public static final int LOGO_TYPE_NONE=0;
	public static final int LOGO_TYPE_FULL_WINDOW=1;
	public static final int AUTORUN_SCANING_COUNT=60;
	public static final int APPWIDGET_HOST_ID=0x100;

	public static final int MESSAGE_FLAG_FOCUS_CHANGED=1;
//launcher4 intent	
	public static final String LAUNCHER4_ONTOUCHEVENT_BAR="LAUNCHER4_ONTOUCHEVENT_BAR";
	public static final String LAUNCHER4_ONCLICK_BAR="LAUNCHER4_ONCLICK_BAR";
	public static final String LAUNCHER4_SCREENONE_PAGECHANGE="LAUNCHER4_SCREENONE_PAGECHANGE";
	public static final String LAUNCHER4_SCREENTWO_PAGECHANGE="LAUNCHER4_SCREENTWO_PAGECHANGE";
	public static final String LAUNCHER4_SCREEN_CHANDED="LAUNCHER4_SCREEN_CHANDED";
	public static final String LAUNCHER4_FAVORITE_REMOVE="LAUNCHER4_FAVORITE_REMOVE";
	public static final String LAUNCHER4_FAVORITE_ADD="LAUNCHER4_FAVORITE_ADD";
	public static final String LAUNCHER4_INTENT_SHOW_STATUS_BAR_CHANGED="LAUNCHER4_INTENT_SHOW_STATUS_BAR_CHANGED";
	public static final String LAUNCHER4_INTENT_WIDGET_CHANGED="LAUNCHER4_INTENT_WIDGET_CHANGED";	

//system settings
	public static final String LAUNCHER4_SETTINGS_STATUS_BAR_MODE="LAUNCHER4_SETTINGS_STATUS_BAR_MODE";
	public static final String LAUNCHER4_SETTINGS_AUTO_HIDE_TIME="LAUNCHER4_SETTINGS_AUTO_HIDE_TIME";
	public static final String SETTINGS_LAUNCHER_WIDGET_ENABLED="SETTINGS_LAUNCHER_WIDGET_ENABLED";
	public static final String SETTINGS_LAUNCHER_WIDGET_1="SETTINGS_LAUNCHER_WIDGET_1";
	public static final String SETTINGS_LAUNCHER_WIDGET_2="SETTINGS_LAUNCHER_WIDGET_2";
//prop
	public static final String PROP_PHONE_SIGNAL_CHANGED = "user.PhoneSignal.enable";
//other
	public static final String DELAY_TIMES[]={"3","5","10"};	
	
	
	/** Mark0
	 *	DefaultSettings
	 *	/etc/WPSetup/DefaultSettings.inf 
	 **/
	 	/** (used) **/
		/* 
			System update Ftp settings
		*/
		public static final String UPDATE_FTP_SERVER="FTP_SERVER";
		public static final String UPDATE_FTP_PORT="FTP_PORT";
		public static final String UPDATE_FTP_NAME="FTP_NAME";
		public static final String UPDATE_FTP_PASSWORD="FTP_PASSWORD";
		public static final String UPDATE_FTP_FOLDER="FTP_FOLDER";

	    /** (used) **/
		/*
			When error have , it will report the error log to us.
		*/
		public static final String ERROR_REPORT_EMAIL="REPORT_EMAIL";

	    /** (used , 2013/4/8) **/
		/*
			Settings -> Customization settings -> menu -> unlock.
			The password use MD5.
		*/
		public static final String SETTINGS_UNLOCK_PASSWORD="ENGINEER_PASSWORD";

	    /** (used , 2013/4/8) **/
		/*
			If it enabled , you can install apps. 
			If it disabled , you can'n install apps , can't use google play , can't update apps.
			0 = disabled
			1 = enabled 
		*/
		public static final String SYSTEM_PACKAGES_INSTALLER_ENABLED="SETTINGS_PACKAGES_INSTALLER_ENABLED";  

	    /** (used , 2013/4/8) **/
		/*
			launcher -> menu -> help -> logo 
			0=off 
			1=on
		*/
		public static final String LAUNCHER_HELP_LOGO="LAUNCHER_HELP_LOGO";

		/** (used , 2013/4/8) **/
		/*	When system login , the car mode show warring dialog is -> 
			0 = hide
			1 = show
		*/
		public static final String SHOW_LOGIN_CAR_MODE_WARRING_DIALOG="SHOW_LOGIN_CAR_MODE_WARRING_DIALOG";

	/** Mark1
	 *	2013/4/9 10:03:25
	 *  Intent / Action 
	 *  for send/get broadcase 
	 **/ 	
	 	/*
	 		When tool bar is show/hide , send it.
		*/ 
	 	public static final String INTENT_TOOLS_BAR_CHANGED="TOOLS_BAR_CHANGED";

	 	/*
	 		Send tool bar function is enable.
	 	*/
	 	public static final String INTENT_TOOLS_BAR_ENABLE="INTENT_TOOLS_OPEN";

	 	/*
	 		Send tool bar function is disable.
	 	*/
	 	public static final String INTENT_TOOLS_BAR_DISABLE="INTENT_TOOLS_OFF";
	 	
	 	/*
	 		Send a security changed.
	 		It will lock/unlock system.
	 	*/
		public static final String INTENT_SECURITY_CHANGED="INTENT_SECURITY_CHANGED";
		
		/*
			Tool bar is showing.
		*/
		public static final String INTENT_TOOLS_BAR_USING="TOOLS_BAR_USING";

		/*
			Send the intent to system , it will control screen lock or unlcok.
		*/
		public static final String INTENT_WINDOW_LOCK_CHANGED="WINDOW_LOCK_CHANGED";

		/*
			When mount/remount GMS , then send it.
		*/	
		public static final String INTENT_PHONE_SIGNAL_CHANGED="INTENT_PHONE_SIGNAL_CHANGED";
		
		/*
			If set , the launcher will reflash.
		*/
		public static final String INTENT_APP_INSTALLER_STATUS_CHANGED="INTENT_APP_INSTALLER_STATUS_CHANGED";
		
		/*
			When send it , launcher will reflash.
		*/
		public static final String INTENT_APPHIDE_CHANGED="INTENT_APPHIDE_CHANGED";
		 
	/** Mark2
	 *  2013/4/9 10:06:15
	 *  System settings 
	 **/ 	  
	 	/*
	 		Set screen shoot function is enable or disable.
	 		0 = disabled
			1 = enabled	
		*/ 
		public static final String SETTINGS_SCREEN_SHOOT_ENABLED="SETTINGS_SCREEN_SHOOT_ENABLED"; 
		
		/*
			Set car mode function is enable or disable
			0 = disable
			1 = enable
		*/
		public static final String SETTINGS_CAR_MODE_OPEN="SETTINGS_CAR_MODE_OPEN";
		
		/*
			Set the security is lock or unlock.
			When system will login , system will check it , if locked , system will lock it.
			0 = unlock
			1 = lock 
		*/
		public static final String SETTINGS_SECURITY_LOCK="SETTINGS_SECURITY_LOCK";
		
		/*
			Set the security function is use or not.
			0 = disable
			1 = enable
		*/
		public static final String SETTINGS_SECURITY_ENABLED="SETTINGS_SECURITY_ENABLED";
		
		/*
			Set HDMI audio output mode.
			0 = HDMI audio off
			1 = HDMI audio on
		*/
		public static final String SETTINGS_HDMI_AUDIO_OUT="HDMI_AUDIO_OUT";
		
		/*
			Set screen lock , when power up , system will check it , if locked , system will set screen is lock. 
			0 = unlock
			1 = lock
		*/
		public static final String SETTINGS_WINDOW_LOCK_STATUS="WINDOW_LOCK_STATUS";
		
		/*
			Set the second display if on or off.
		*/
		public static final String SETTINGS_SECOND_DISPLAY_CONNECTED="SECOND_DISPLAY_CONNECTED";
		
		/*
			Set tool bar auto hide
			0 = always show
			1 = auto hide
		*/
		public static final String SETTINGS_TOOLS_BAR_AUTO_HIDE="SETTINGS_TOOLS_BAR_AUTO_HIDE";
		
		/*
			Save the error report mail to system.
		*/
		public static final String SETTINGS_ERROR_REPORT_EMAIL="ERROR_REPORT_EMAIL";
		
		/*
			Set logo type
			0 = Not full window
			1 = Full window
		*/
		public static final String SETTINGS_LOGO_TYPE="SETTINGS_LOGO_TYPE";
		
		/*
			Set login logo skip.When set it,system will check sdcard/extsd/ media device
			0 = not check
			1 = check
		*/
		public static final String SETTINGS_ENABLE_SKIP_FUNCTION="SETTINGS_ENABLE_SKIP_FUNCTION";
		
		/*
			about autorun
			0 = disable
			1 = enable
		*/
		public static final String SETTINGS_AUTORUN_ENABLE="SETTINGS_AUTORUN_ENABLE";
		
		/*
			When it set and car mode is close , when system login , launcher will show car mode warning dialog
			0 = disable
			1 = enable
		*/
		public static final String SETTINGS_CAR_MODE_WARNING_DIALOG_SHOW="SETTINGS_CAR_MODE_WARNING_DIALOG_SHOW";
		
		/*
			Set how to get time
			0 = none
			1 = auto from internet
			2 = auto from gps
		*/
		public static final String SETTINGS_AUTOTIME_VALUE="AUTO_TIME_VALUE";
//		public static final String CAR_MODE_OPEN = "off";
	
}
