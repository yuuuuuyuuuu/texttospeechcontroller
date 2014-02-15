package com.texttospeechcontroller;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.EngineInfo;
import android.speech.tts.TextToSpeech.OnInitListener;

public class TextToSpeechController {
	
	private Context mContext = null;
	
	
	private TextToSpeechWrapper mTextToSpeechWrapper = null;
	
	public TextToSpeechController(Context context)
	{
		mContext = context;
		
		init();
	}
	
	private void init()
	{
		mTextToSpeechWrapper = TextToSpeechWrapper.GetInstance(mContext);
	}
	
	public void SetLocale(Locale locale)
	{
		boolean supportStatus = mTextToSpeechWrapper.SetLanguage(locale);
	}
	
	public Locale GetCurrentLanguage()
	{
		return mTextToSpeechWrapper.GetCurrentLanguage();
	}
	
	public boolean IsLanguageAvailable(Locale locale)
	{
		
		boolean availability = mTextToSpeechWrapper.IsLanguageAvailable(locale);
		
		return availability;
	}
	
	private void Speak(String sentence, boolean queueMode)
	{
		if(!mTextToSpeechWrapper.IsReadyForSpeech()) return;
		
		if(queueMode)
		{
			mTextToSpeechWrapper.Speak(sentence, TextToSpeech.QUEUE_ADD);
		}
		else
		{
			mTextToSpeechWrapper.Speak(sentence, TextToSpeech.QUEUE_FLUSH);
		}
		
	}

	public void Speak(ArrayList<String> sentences, long interval)
	{
		if(interval < 0) interval = 0;
		
		for(int i = 0; i < sentences.size(); i++)
		{
			Speak(sentences.get(i), true);
		}
	}
	
	public boolean SetEngine(EngineInfo newEngineInfo)
	{
		boolean result = mTextToSpeechWrapper.SetEngine(newEngineInfo);
		
		return result;
	}
}
