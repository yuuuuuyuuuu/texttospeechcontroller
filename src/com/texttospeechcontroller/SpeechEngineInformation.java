package com.texttospeechcontroller;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;

public class SpeechEngineInformation implements OnInitListener {

	private Context mContext = null;
	private TextToSpeech mTextToSpeech = null;
	
	public SpeechEngineInformation(Context context)
	{
		this.mContext = context;
		mTextToSpeech = new TextToSpeech(mContext, this);
	}

	@Override
	public void onInit(int arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}
