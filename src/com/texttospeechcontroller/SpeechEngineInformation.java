package com.texttospeechcontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech.EngineInfo;
import android.speech.tts.TextToSpeech.OnInitListener;

public class SpeechEngineInformation implements OnInitListener {

	private Context mContext = null;
	private TextToSpeechWrapper mTextToSpeechWrapper = null;
	
	public SpeechEngineInformation(Context context)
	{
		this.mContext = context;
		mTextToSpeechWrapper = new TextToSpeechWrapper(mContext);
	}

	public List<EngineInfo> GetAvailableEngines()
	{
		if(null == mTextToSpeechWrapper) return null;
		
		return mTextToSpeechWrapper.GetEngineInfo();
	}
	
	public String GetCurrentEngine()
	{
		if(null == mTextToSpeechWrapper) return "Can't get Engine";
		
		return mTextToSpeechWrapper.GetCurrentEngine();
	}
	
	public List<Locale> GetAvailableLocales()
	{
		if(null == mTextToSpeechWrapper) return null;
		
		List<Locale> localeList = new ArrayList<Locale>();
		Locale[] locales = Locale.getAvailableLocales();
		
		for(Locale locale : locales)
		{
			if(mTextToSpeechWrapper.IsLanguageAvailable(locale))
			{
				localeList.add(locale);
			}
		}
		
		return localeList;
	}
	
	@Override
	public void onInit(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
