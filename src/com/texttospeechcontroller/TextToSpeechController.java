package com.texttospeechcontroller;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

public class TextToSpeechController implements OnInitListener {
	
	private Context mContext = null;
	private TextToSpeech mTextToSpeech = null;
	private boolean mIsReadyToSpeech = false;
	private Locale mDefaultLocale = Locale.ENGLISH;
	
	public TextToSpeechController(Context context)
	{
		mContext = context;
		
		init();
	}
	
	private void init()
	{
		mTextToSpeech = new TextToSpeech(mContext, this);
	}
	
	public void SetLocale(Locale locale)
	{
		int supportStatus = mTextToSpeech.setLanguage(locale);
	}
	
	public Locale GetLocale()
	{
		return mTextToSpeech.getLanguage();
	}
	
	public boolean IsLanguageAvailable(Locale locale)
	{
		int result = mTextToSpeech.isLanguageAvailable(locale);
		boolean availability = false;
		
		switch(result)
		{
		case TextToSpeech.LANG_AVAILABLE:
		case TextToSpeech.LANG_COUNTRY_AVAILABLE:
		case TextToSpeech.LANG_COUNTRY_VAR_AVAILABLE:
			availability = true;
			break;
			
		case TextToSpeech.LANG_MISSING_DATA:
		case TextToSpeech.LANG_NOT_SUPPORTED:
			availability = false;
			break;
			
		default:
			break;
		}
		
		return availability;
	}
	
	private void Speak(String sentence, boolean queueMode)
	{
		if(!mIsReadyToSpeech) return;
		
		if(queueMode)
		{
			mTextToSpeech.speak(sentence, TextToSpeech.QUEUE_ADD, null);
		}
		else
		{
			if(mTextToSpeech.isSpeaking()) mTextToSpeech.stop();
			mTextToSpeech.speak(sentence, TextToSpeech.QUEUE_FLUSH, null);
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
	
	public boolean IsReady()
	{
		return mIsReadyToSpeech;
	}
	
	@Override
	public void onInit(int status) {
		
		switch(status)
		{
		
		case TextToSpeech.SUCCESS:
			mIsReadyToSpeech = true;
			SetLocale(mDefaultLocale);
			break;
			
		default:
			break;
			
		}
	}
}
