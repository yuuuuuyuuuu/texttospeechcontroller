package com.texttospeechcontroller;

import java.io.Serializable;

public class CandidateInfo implements Serializable{

	private int mDbId = 0;
	private String mSentence = "";
	private int mIsChecked = 0;  // 0: unchecked  1: checked
	
	public CandidateInfo(String sentence, int dbId)
	{
		mSentence = sentence;
		mDbId = dbId;
		mIsChecked = 0;
	}
	
	public String getSentence()
	{
		return mSentence;
	}
	
	public boolean isChecked()
	{
	    if(0 == mIsChecked)
	    {
	    	return false;
	    }
	    else
	    {
	    	return true;
	    }
	}
	
	public void setChecked(boolean value)
	{
		if(value)
		{
			mIsChecked = 1;
		}
		else
		{
			mIsChecked = 0;
		}
	}
	
	public int getDbId()
	{
		return mDbId;
	}
}
