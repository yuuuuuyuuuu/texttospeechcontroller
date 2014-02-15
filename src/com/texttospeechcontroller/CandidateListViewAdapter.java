package com.texttospeechcontroller;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class CandidateListViewAdapter extends ArrayAdapter<CandidateInfo> {

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
		
		final int pos = position;
		mCandidateHolder.mImageButtonDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String selection = DB_COLUMNS[0] + "=" + String.valueOf(mItemList.get(pos).getDbId());
				int row = getContext().getContentResolver().delete(DB_URI, selection, null);
				Log.d("CandidateLisViewAdapter", String.valueOf(row));
			}
			
		});
		
		mCandidateHolder.mCheckBoxSpeak = (CheckBox)row.findViewById(R.id.checkBox_speak);
		
		mCandidateHolder.mCheckBoxSpeak.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				mItemList.get(pos).setChecked(isChecked);
			}
		});
		
		mCandidateHolder.mCheckBoxSpeak.setChecked(mItemList.get(pos).isChecked());
		
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
}
