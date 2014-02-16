package com.texttospeechcontroller;

import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

public class CandidateListViewAdapter extends ArrayAdapter<CandidateInfo> implements OnClickListener{

	private static final Uri DB_URI = Uri.parse("content://com.texttospeechcontroller");
	private static final String[] DB_COLUMNS = new String[]{"_id", "sentence", "selected"};
	
	private Context mContext = null;
	private int mResourceId = -1;
	private List<CandidateInfo> mItemList = null;
	private SentenceDbHelper mDbHelper = null;
	
	public CandidateListViewAdapter(Context context, int textViewResourceId,
			List<CandidateInfo> objects) {
		super(context, textViewResourceId, objects);
		
		mContext = context;
		mResourceId = textViewResourceId;
		mItemList = objects;
		
	}

	public void setDbHelper(SentenceDbHelper dbHelper)
	{
		mDbHelper = dbHelper;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View row = convertView;
		
		if(null == row)
		{
			LayoutInflater inflator = ((MainActivity)mContext).getLayoutInflater();
			row = inflator.inflate(mResourceId, null);
		}
		
		CandidateInfoHolder mCandidateHolder = new CandidateInfoHolder();
		mCandidateHolder.mCandidateInfo = mItemList.get(position);
		mCandidateHolder.mTextViewId = (TextView) row.findViewById(R.id.textview_listview_id);
		mCandidateHolder.mTextViewSentence = (TextView) row.findViewById(R.id.textview_listview_candidate);
		mCandidateHolder.mImageButtonDelete = (ImageButton)row.findViewById(R.id.imagebutton_delete);

		mCandidateHolder.mImageButtonDelete.setOnClickListener(this);
		
		mCandidateHolder.mImageButtonDelete.setTag(position);
		
		mCandidateHolder.mCheckBoxSpeak = (CheckBox)row.findViewById(R.id.checkBox_speak);
		mCandidateHolder.mCheckBoxSpeak.setTag(position);
		mCandidateHolder.mCheckBoxSpeak.setChecked(getItem(position).isChecked());
		mCandidateHolder.mCheckBoxSpeak.setOnClickListener(this);
		
		row.setTag(mCandidateHolder);
		setupItem(mCandidateHolder, position);
		
		return row;
		
	}
	
	public int GetCandidatesNum()
	{
		return mItemList.size();
	}
	
	private void setupItem(CandidateInfoHolder mCandidateHolder, int position) {
		
		mCandidateHolder.mTextViewId.setText(String.valueOf(position + 1));
		mCandidateHolder.mTextViewSentence.setText(mCandidateHolder.mCandidateInfo.getSentence());
		
	}

	public static class CandidateInfoHolder {
		CandidateInfo mCandidateInfo;
		TextView mTextViewId;
		TextView mTextViewSentence;
		CheckBox mCheckBoxSpeak;
		ImageButton mImageButtonDelete;
	}

	@Override
	public void onClick(View v) {
		
		if(v instanceof ImageButton)
		{
			ImageButton ib = (ImageButton)v;
			
			int pos = (Integer) ib.getTag();
			CandidateInfo info = getItem(pos);
			
			deleteRow(info.getDbId());
		}
		else if (v instanceof CheckBox)
		{
			CheckBox cb = (CheckBox) v;
			boolean checkState = cb.isChecked();
			int checkStateInt = 0;
			if(checkState) checkStateInt = 1;
			
			int  pos = (Integer) cb.getTag();
			CandidateInfo info = getItem(pos);
			
			ContentValues cv = new ContentValues();
			cv.put(DB_COLUMNS[1], info.getSentence());
			cv.put(DB_COLUMNS[2], checkStateInt);
			
			updateRow(info.getDbId(), cv);
		}
		
	}
	
	private void updateRow(int dbRowNum, ContentValues cv)
	{
		String selection = DB_COLUMNS[0] + "=" + String.valueOf(dbRowNum);
		int row = getContext().getContentResolver().update(DB_URI, cv, selection, null);
	}
	
	private void deleteRow(int dbRowNum)
	{
		String selection = DB_COLUMNS[0] + "=" + String.valueOf(dbRowNum);
		int row = getContext().getContentResolver().delete(DB_URI, selection, null);
		
	}
}
