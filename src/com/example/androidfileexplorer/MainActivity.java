package com.example.androidfileexplorer;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener,
		OnItemClickListener {
	private static final String tag = "MainActivity";
	private static final File ROOT_DIR = File.listRoots()[0];
	private File mCwd;
	private ArrayAdapter<File> mEntryAdapter;
	private List<File> mEntries;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mEntries = new ArrayList<File>();
		mEntryAdapter = new DirListArrayAdapter(this, R.id.entry, mEntries);

		Button upButton = (Button) findViewById(R.id.up);
		upButton.setOnClickListener(this);
		ListView listView = (ListView) findViewById(R.id.entry_list_view);
		listView.setAdapter(mEntryAdapter);
		listView.setOnItemClickListener(this);

		new DirListTask().execute(ROOT_DIR);
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
			new DirListTask().execute(new File(f.toString()));
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
		new DirListTask().execute(p != null ? new File(p) : ROOT_DIR);
	}

	private class DirListTask extends AsyncTask<File, Void, Void> {
		@Override
		protected Void doInBackground(File... params) {
			mCwd = params[0];
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
			cwdView.setText("[" + mCwd.toString() + "]");
		}

	}
}
