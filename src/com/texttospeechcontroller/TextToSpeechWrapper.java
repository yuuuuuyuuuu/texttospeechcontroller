package com.texttospeechcontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.EngineInfo;
import android.speech.tts.TextToSpeech.OnInitListener;

public class TextToSpeechWrapper implements OnInitListener {

	private static TextToSpeech mTextToSpeech = null;
	private static boolean mIsReadyForSpeech = false;
	
	private Context mContext = null;
	private EngineInfo mEngineInfo = null;
	
	public TextToSpeechWrapper(Context context)
	{
		mContext = context;
		
		if(null == mTextToSpeech) mTextToSpeech = new TextToSpeech(context, this);
		
	}

	public TextToSpeech GetTextToSpeech()
	{
		return mTextToSpeech;
	}
	
	public String GetCurrentEngine()
	{
		if(null == mTextToSpeech) return "Can't get Engine";
		
		return mTextToSpeech.getDefaultEngine();
	}
	
	public boolean IsReadyForSpeech()
	{
		return mIsReadyForSpeech;
	}
	
	public List<TextToSpeech.EngineInfo> GetEngineInfo()
	{
		if(null == mTextToSpeech) return null;
		
		return mTextToSpeech.getEngines();
	}
	
	public List<Locale> GetAvailableLanguages()
	{
		List<Locale> availableLanguages = new ArrayList<Locale>();
		
		throw new UnsupportedOperationException();
		
		// return availableLanguages;
	}
	
	public Locale GetCurrentLanguage()
	{
		if(null == mTextToSpeech) return null;
		
		return mTextToSpeech.getLanguage();
	}
	
	public boolean IsLanguageAvailable(Locale locale)
	{
		if(null == mTextToSpeech) return false;
		
		int result = mTextToSpeech.isLanguageAvailable(locale);
		
		if((result == TextToSpeech.LANG_MISSING_DATA) || (result == TextToSpeech.LANG_NOT_SUPPORTED))
		{
			return false;
		}
		
		return true;
	}
	
	public boolean SetLanguage(Locale newLocale)
	{
		if(null == mTextToSpeech) return false;
		
		int result = mTextToSpeech.setLanguage(newLocale);
		
		if((result == TextToSpeech.LANG_MISSING_DATA) || (result == TextToSpeech.LANG_NOT_SUPPORTED))
		{
			return false;
		}
		
		return true;
	}
	
	public boolean SetEngine(EngineInfo newEngineInfo)
	{
		if(null == mTextToSpeech) return false;
		
		mIsReadyForSpeech = false;
		mTextToSpeech = new TextToSpeech(mContext, this);
		
		return true;
	}
	
	public boolean Speak(String message, int queueMode)
	{
		if(null == mTextToSpeech) return false;
		
		mTextToSpeech.speak(message, queueMode, null);
		
		return true;
	}
	
	@Override
	public void onInit(int status) {
	
		switch(status)
		{
		case TextToSpeech.SUCCESS:
			mIsReadyForSpeech = true;
		}
	
	}
}
