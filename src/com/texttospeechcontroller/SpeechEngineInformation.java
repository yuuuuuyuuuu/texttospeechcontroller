package com.texttospeechcontroller;

import java.util.List;

import android.content.Context;
import android.speech.tts.TextToSpeech;
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
	
	@Override
	public void onInit(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
