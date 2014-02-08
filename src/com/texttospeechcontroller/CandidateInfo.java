package com.texttospeechcontroller;

import java.io.Serializable;

public class CandidateInfo implements Serializable{

	private int mDbId = 0;
	private String mSentence = "";
	private boolean mIsChecked = false;
	
	public CandidateInfo(String sentence, int dbId)
	{
		mSentence = sentence;
		mDbId = dbId;
	}
	
	public String getSentence()
	{
		return mSentence;
	}
	
	public boolean isChecked()
	{
		return mIsChecked;
	}
	
	public void setChecked(boolean value)
	{
		mIsChecked = value;
	}
	
	public int getDbId()
	{
		return mDbId;
	}
}
