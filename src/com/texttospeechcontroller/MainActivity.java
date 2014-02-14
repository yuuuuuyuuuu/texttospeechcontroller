package com.texttospeechcontroller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Loader;
import android.content.DialogInterface.OnShowListener;
import android.database.Cursor;
import android.speech.tts.TextToSpeech.EngineInfo;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener, OnShowListener, android.content.DialogInterface.OnClickListener, OnItemClickListener {

	private final String TAG = "MainActivity";
	
	private Button mButtonAddCandidate = null;
	private Button mButtonDeleteCandidate = null;
	private Button mButtonSpeak = null;
	private Button mButtonSetLanguage = null;
	private Button mButtonSetSpeechEngine = null;
	
	private TextView mTextViewCandidateNumber = null;
	private TextView mTextViewCurrentEngine = null;
	private TextView mTextViewCurrentLanguage = null;
	
	private StatusController mStatusController = null;
	
	private ListView mSentenceListView = null;
	private ArrayAdapter<String> mSentenceListAdaper = null;
	private CandidateListViewAdapter mCandidateAdapter = null;
	
	private AlertDialog mAddCandidateDialog = null;
	private TextView mTextViewAddDialog = null;
	
	private AlertDialog mLanguageSelectDialog = null;
	
	private AlertDialog mSpeechEngineSelectDialog = null;
	private int mSpeechEngineIndex = -1;
	
	private TextToSpeechController mTextToSpeechController = null;
	private SpeechEngineInformation mSpeechEngineInformation = null;
	
	private Cursor mDbCursor = null;
	
	private PreferenceController mPreferenceController = null;
	
	private static final Uri DB_URI = Uri.parse("content://com.texttospeechcontroller");
	private static final String[] DB_COLUMNS = new String[]{"_id", "sentence"};
	
	private List<Locale> mAvailableLocales = null;
	private CharSequence[] mLanguageSelection = new String[]{"English", "Japanese"};
	private CharSequence[] mSpeechEngineInfoSelection = new String[]{};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Window settings
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
				
		setContentView(R.layout.activity_main);
		
		mTextToSpeechController = new TextToSpeechController(this);
		mTextToSpeechController.SetLocale(Locale.JAPANESE);
		
		mSpeechEngineInformation = new SpeechEngineInformation(this);
		
		getLoaderManager().initLoader(0, null, mCursorCallbacks);
		
		initViews();
		
		initSettings();
	}

	private void initSettings() {
		
		// Preferences
		mPreferenceController = new PreferenceController(this);
		
		
	}

	private void updateStatus()
	{
		mStatusController.SetCandidateNumInfo(mCandidateAdapter.GetCandidatesNum());
		
		Locale currentLocale = mTextToSpeechController.GetCurrentLanguage();
		if(null != currentLocale) mStatusController.SetLanguageInfo(mTextToSpeechController.GetCurrentLanguage());
		mStatusController.SetSpeechEngineInfo(mSpeechEngineInformation.GetCurrentEngine());
	}

	private void initViews()
	{
		
		mButtonAddCandidate = (Button)findViewById(R.id.buttonAddNew);
		mButtonAddCandidate.setOnClickListener(this);
		
		// mButtonDeleteCandidate = (Button)findViewById(R.id.buttonDeleteCandidate);
		// mButtonDeleteCandidate.setOnClickListener(this);
		
		mButtonSetLanguage = (Button)findViewById(R.id.buttonSetLanguage);
		mButtonSetLanguage.setOnClickListener(this);
		
		mButtonSetSpeechEngine = (Button)findViewById(R.id.buttonSetSpeechEngine);
		mButtonSetSpeechEngine.setOnClickListener(this);
		
		// TODO: implement handler
		
		mButtonSpeak = (Button)findViewById(R.id.buttonExecuteTextToSpeech);
		mButtonSpeak.setOnClickListener(this);
		
		// List
		mSentenceListView = (ListView)findViewById(R.id.listViewCandidate);
		mSentenceListAdaper = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
		// mSentenceListView.setAdapter(mSentenceListAdaper);
		
		ArrayList<CandidateInfo> candidateList = new ArrayList<CandidateInfo>();
		mCandidateAdapter = new CandidateListViewAdapter(this, R.layout.listview_item_with_delete, candidateList);
		mSentenceListView.setAdapter(mCandidateAdapter);
		
		mSentenceListView.setOnItemClickListener(this);
		
		// New candidate dialog
		LayoutInflater inflator = LayoutInflater.from(this);
		View view = inflator.inflate(R.layout.add_dialog, null);
		mTextViewAddDialog = (TextView)view.findViewById(R.id.editTextDialogNewCandidate);
		
		mAddCandidateDialog = new AlertDialog.Builder(this)
		.setTitle(R.string.dialog_label_Add_New_Candidate)
		.setView(view)
		.setPositiveButton(R.string.dialog_label_cancel, null)
		.setNegativeButton(R.string.dialog_label_add, this).create();
		
		mAddCandidateDialog.setOnShowListener(this);
		
		// Engine Selection Dialog
		mSpeechEngineSelectDialog = new AlertDialog.Builder(this)
		.setTitle(R.string.dialog_label_select_engine)
		.setSingleChoiceItems(mSpeechEngineInfoSelection, 0, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		})
		.setPositiveButton(R.string.dialog_language_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		}).create();
		
		mTextViewCandidateNumber = (TextView)findViewById(R.id.textViewCurrentCandidatesNumber);
		mTextViewCurrentEngine = (TextView)findViewById(R.id.textviewCurrentEngine);
		mTextViewCurrentLanguage = (TextView)findViewById(R.id.textViewLanguageSetting);
		mStatusController = new StatusController(this, mTextViewCandidateNumber, mTextViewCurrentEngine, mTextViewCurrentLanguage);
	}

	private ArrayList<String> getSelectedSentences()
	{
		ArrayList<String> selectedSentences = new ArrayList<String>();
		
		for(int i = 0; i < mCandidateAdapter.getCount(); i++)
		{
			CandidateInfo itemInfo = mCandidateAdapter.getItem(i);
			if(itemInfo.isChecked())
			{
				selectedSentences.add(itemInfo.getSentence());
			}
		}
		
		return selectedSentences;
	}
	@Override
	public void onClick(View v) {
		
		if(v instanceof Button)
		{
			Button clickedButton = (Button)v;
			
			if(clickedButton == mButtonDeleteCandidate)
			{
				
			}
			else if(clickedButton == mButtonSpeak)
			{
				mTextToSpeechController.Speak(getSelectedSentences(), 100);
			}
			else if(clickedButton == mButtonAddCandidate)
			{
				// show dialog for addition
				showAddCandidateDialog();
				
			}
			else if(clickedButton == mButtonSetLanguage)
			{
				// show dialog for language setting
				showLanguageSelectDialog();
			}
			else if(clickedButton == mButtonSetSpeechEngine)
			{
				showSpeechEngineSelectDialog();
			}
			else
			{
				assert(false);
			}
		}
		else if(v instanceof ImageButton)
		{
            assert(false);
		}
	}
	
	
	private void showAddCandidateDialog()
	{
		// show software keyboard
		mAddCandidateDialog.show();
	}

	private void showLanguageSelectDialog()
	{
		createLanguageSelectDialog();
		mLanguageSelectDialog.show();
	}
	
	private void showSpeechEngineSelectDialog()
	{
		createSpeechEngineDialog();
		mSpeechEngineSelectDialog.show();
	}
	
	private void createLanguageSelectDialog()
	{
		mAvailableLocales = mSpeechEngineInformation.GetAvailableLocales();
		
		List<CharSequence> localeLabels = new ArrayList<CharSequence>();
		
		for(Locale locale : mAvailableLocales)
		{
			localeLabels.add(locale.getDisplayName());
		}
		
		mLanguageSelection = localeLabels.toArray(new CharSequence[localeLabels.size()]);
		
		int selection = 0;
		mLanguageSelectDialog = new AlertDialog.Builder(this)
		.setTitle(R.string.dialog_language_title)
		.setSingleChoiceItems(mLanguageSelection, selection, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				
				if(which <= mAvailableLocales.size())
				{
					mTextToSpeechController.SetLocale(mAvailableLocales.get(which));
				}
				
				
				updateStatus();
				
			}
		})
		.setPositiveButton(R.string.dialog_language_ok, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		}).create();
		
	}
	
	private void createSpeechEngineDialog()
	{
        List<EngineInfo> engineInfoList = mSpeechEngineInformation.GetAvailableEngines();
		
		List<String> infoList = new ArrayList<String>();
		
		for(int i = 0; i < engineInfoList.size(); i++)
		{
			infoList.add(engineInfoList.get(i).label);
		}
		
		mSpeechEngineInfoSelection = infoList.toArray(new CharSequence[infoList.size()]);
		
		// Engine Selection Dialog
		mSpeechEngineSelectDialog = new AlertDialog.Builder(this)
		.setTitle(R.string.dialog_label_select_engine)
		.setSingleChoiceItems(mSpeechEngineInfoSelection, 0, new DialogInterface.OnClickListener() {		
			@Override
			public void onClick(DialogInterface arg0, int which) {
				
				mSpeechEngineIndex = which;
				
			}
		})
		.setPositiveButton(R.string.dialog_language_ok, new DialogInterface.OnClickListener() {		
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
				boolean result = false;
				if(-1 != mSpeechEngineIndex)
				{
				    List<EngineInfo> engineInfoList = mSpeechEngineInformation.GetAvailableEngines();
				    result = mTextToSpeechController.SetEngine(engineInfoList.get(mSpeechEngineIndex));
				}
				
				if(!result)
				{
					Toast.makeText(MainActivity.this, "Failed to set new TTS engine", Toast.LENGTH_SHORT).show();
				}
				
				dialog.cancel();
				
			}
		}).create();
	}
	
	private void updateSentences()
	{
		//mSentenceListAdaper.clear();
		mCandidateAdapter.clear();
		
		boolean result =  mDbCursor.moveToFirst();
		
		int columnSize = mDbCursor.getColumnCount();
		int dataSize = mDbCursor.getCount();
		
		if(0 == columnSize) return;
		if(0 == dataSize) return;
		
		do{
			
			int id = mDbCursor.getInt(mDbCursor.getColumnIndex(DB_COLUMNS[0]));
			String sentence = mDbCursor.getString(mDbCursor.getColumnIndex(DB_COLUMNS[1]));
			String displayString = sentence;
			CandidateInfo candidateInfo = new CandidateInfo(displayString, id);
			mCandidateAdapter.add(candidateInfo);
			
		}while(mDbCursor.moveToNext());
		
	}
	
	@Override
	public void onShow(DialogInterface arg0) {
		// show keyboard
		InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.showSoftInput(mTextViewAddDialog, 0);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		
		switch(which)
		{
		case DialogInterface.BUTTON_POSITIVE:
			// Cancel
			break;
			
		case DialogInterface.BUTTON_NEUTRAL:
			break;
			
		case DialogInterface.BUTTON_NEGATIVE:
			// Add
			insertSentence();
			break;
			
		default:
			assert(false);
			break;
			
		}
	}

	private void insertSentence()
	{
		// Insert to database
		String newCandidate = mTextViewAddDialog.getText().toString();
		ContentValues cv = new ContentValues();
		cv.put(DB_COLUMNS[1], newCandidate);
					
		Uri result = getContentResolver().insert(DB_URI, cv);
					
		if(null == result)
		{
			assert(false);
		}
	}
	
	private void updateView()
	{
		updateSentences();
		
		updateLocaleLabel();
		
		updateStatus();
	}
	private void updateLocaleLabel() {
		
		Locale currentLocale = mTextToSpeechController.GetCurrentLanguage();
		if(null == currentLocale) return;
		
		//mTextViewHeader.setText("Language: " + currentLocale.getLanguage());
		
	}
	private LoaderManager.LoaderCallbacks<Cursor> mCursorCallbacks = new LoaderCallbacks<Cursor>() {
		
		@Override
		public void onLoaderReset(Loader<Cursor> arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
			mDbCursor = c;
			if(null == c) return;
			
			updateView();
		}
		
		@Override
		public Loader<Cursor> onCreateLoader(int id, Bundle args) {
			// TODO Auto-generated method stub
			
			Loader<Cursor> loader = new CursorLoader(getApplicationContext(), DB_URI, null, null, null, null);
			
			return loader;
		}
	};

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		ListView listView = (ListView) parent;
        String item = (String) listView.getItemAtPosition(position);
        
	}

}
