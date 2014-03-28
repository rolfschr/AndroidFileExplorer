package com.example.androidfileexplorer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener,
		OnItemClickListener {
	public static final boolean DEVELOPER_MODE = true;
	private static final String tag = "MainActivity";
	private static final String BUNDLE_CWD = "cwd";
	private static final File ROOT_DIR = File.listRoots()[0];
	private File mCwd;
	private ArrayAdapter<File> mEntryAdapter;
	private List<File> mEntries;
	ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setPreferences();

		mEntries = new ArrayList<File>();
		mEntryAdapter = new DirListArrayAdapter(this, R.id.entry, mEntries);

		Button upButton = (Button) findViewById(R.id.up);
		upButton.setOnClickListener(this);
		listView = (ListView) findViewById(R.id.entry_list_view);
		listView.setAdapter(mEntryAdapter);
		listView.setOnItemClickListener(this);
		registerForContextMenu(listView);

		mCwd = ROOT_DIR;
		if (savedInstanceState != null) {
			String d = savedInstanceState.getString(BUNDLE_CWD);
			if (d != null) {
				mCwd = new File(d);
			}
		}
		new DirListTask().execute();
	}

	private void setPreferences() {
		boolean readAgain = false;
		if (DEVELOPER_MODE) {
			SharedPreferences sharedPref = PreferenceManager
					.getDefaultSharedPreferences(this);
			SharedPreferences.Editor editor = sharedPref.edit();
			editor.clear();
			editor.commit();
			readAgain = true;
		}
		PreferenceManager.setDefaultValues(this, R.xml.preferences, readAgain);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> listView, View view, int pos, long id) {
		File f = (File) listView.getItemAtPosition(pos);
		if (f.isDirectory()) {
			mCwd = new File(f.toString());
			new DirListTask().execute();
		} else {
			Util.openFileWithApp(this, f);
		}
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		File f = (File) listView.getItemAtPosition(info.position);
		if (f.isFile()) {
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.file_context_menu, menu);
			listView.setItemChecked(info.position, true);
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		File f = (File) listView.getItemAtPosition(listView
				.getCheckedItemPosition());
		switch (item.getItemId()) {
		case R.id.rm_file:
			Util.deleteFile(this, mEntryAdapter, f);
			return true;
		case R.id.mv_file:
			Util.renameFile(this, mEntryAdapter, f);
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	public void onBackPressed() {
		moveUp();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.up:
			moveUp();
			break;
		}
	}

	private void moveUp() {
		String p = mCwd.getParent();
		mCwd = (p != null ? new File(p) : ROOT_DIR);
		new DirListTask().execute();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(BUNDLE_CWD, mCwd.toString());
	}

	public void openSettingsActivity(MenuItem item) {
		Intent intent = new Intent(this, SettingsActivity.class);
		startActivity(intent);
	}

	private class DirListTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			mEntries.clear();
			File[] tmp = mCwd.listFiles(Util.onlyDirFilter());
			if (tmp != null) {
				Arrays.sort(tmp);
				mEntries.addAll(Arrays.asList(tmp));
			}
			tmp = mCwd.listFiles(Util.onlyFileFilter());
			if (tmp != null) {
				Arrays.sort(tmp);
				mEntries.addAll(Arrays.asList(tmp));
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mEntryAdapter.notifyDataSetChanged();
			TextView cwdView = (TextView) findViewById(R.id.cwd);
			listView.setSelection(0);
			cwdView.setText("[" + mCwd.toString() + "]");
		}
	}
}
