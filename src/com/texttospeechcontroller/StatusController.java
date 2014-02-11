package com.texttospeechcontroller;

import java.util.Locale;

import android.content.Context;
import android.speech.tts.TextToSpeech.EngineInfo;
import android.widget.TextView;

public class StatusController {

	private Context mContext = null;
	
	private TextView mTextViewRegisteredCandidateNumber = null;
	private TextView mTextViewTTS = null;
	private TextView mTextViewLanguage = null;
	
	public StatusController(Context context, TextView tvCandidateNum, TextView tvTts, TextView tvLang)
	{
		mContext = context;
		
		mTextViewRegisteredCandidateNumber = tvCandidateNum;
		mTextViewTTS = tvTts;
		mTextViewLanguage = tvLang;
	}
	
	public void SetCandidateNumInfo(int num)
	{
		mTextViewRegisteredCandidateNumber.setText(String.valueOf(num) + " registered");
	}
	
	public void SetTtsEngineInfo(EngineInfo info)
	{
		mTextViewTTS.setText(info.name);
	}
	
	public void SetLanguageInfo(Locale locale)
	{
		mTextViewLanguage.setText(locale.getDisplayName());
	}
}
