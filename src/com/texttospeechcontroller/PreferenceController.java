package com.texttospeechcontroller;

import java.util.Currency;
import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;

public class PreferenceController {

	private final String TAG = "PreferenceController";
	
	private Context mContext = null;
	private SharedPreferences mSharedPreferences = null; 
	
	public PreferenceController(Context context)
	{
		mContext = context;
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
	}
	
	public void setLangSetting(Locale newLocale)
	{
		if(null == mSharedPreferences) return;
		
		// Log.d(TAG, newLocale.toString());
		
		Editor editor = mSharedPreferences.edit();
		editor.putString(PreferenceKeys.PREFERENCE_KEY_CURRENT_LOCALE, newLocale.toString());
		editor.commit();
		
	}
	
	public Locale readLangSetting()
	{
		if(null == mSharedPreferences) return null;
		
		Locale currentLocale = new Locale(mSharedPreferences.getString(PreferenceKeys.PREFERENCE_KEY_CURRENT_LOCALE, Locale.ENGLISH.toString()));
		
		// Log.d(TAG, currentLocale.toString());
		
		return currentLocale;
	}
	
	public void setInitialLaunchSetting(boolean isInitial)
	{

		Editor editor = mSharedPreferences.edit();
		editor.putBoolean(PreferenceKeys.PREFERENCE_KEY_INITIAL_LAUNCH, isInitial);
		editor.commit();
	}
	
	public boolean getInitialLaunchSetting()
	{
		return mSharedPreferences.getBoolean(PreferenceKeys.PREFERENCE_KEY_INITIAL_LAUNCH, true);
	}
}
