package com.example.androidfileexplorer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
	private File cwd;
	private ArrayAdapter<File> adapter;
	private List<File> entries;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		entries = new ArrayList<File>();
		adapter = getDirListArrayAdapter(entries);

		Button upButton = (Button) findViewById(R.id.up);
		upButton.setOnClickListener(this);
		ListView listView = (ListView) findViewById(R.id.entry_list_view);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(this);

		cwd = ROOT_DIR;
		updateEntriesAndViews();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private FilenameFilter onlyDirFilter() {
		return new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		};
	}

	private FilenameFilter onlyFileFilter() {
		return new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isFile();
			}
		};
	}

	private ArrayAdapter<File> getDirListArrayAdapter(List<File> ref) {
		return new ArrayAdapter<File>(this, R.layout.entry, ref) {

			@Override
			public View getView(int pos, View convertView, ViewGroup parent) {
				if (convertView == null) {
					LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = vi.inflate(R.layout.entry, null);
				}
				File f = getItem(pos);
				TextView tv = (TextView) convertView.findViewById(R.id.entry);
				tv.setText(f.getName());
				if (f.isDirectory()) {
					tv.setTextColor(getResources().getColor(R.color.dir_color));
				} else {
					tv.setTextColor(getResources().getColor(R.color.file_color));
				}

				return convertView;
			}

		};
	}

	private void updateEntriesAndViews() {
		entries.clear();
		File[] tmp = cwd.listFiles(onlyDirFilter());
		if (tmp != null) {
			Arrays.sort(tmp);
			entries.addAll(Arrays.asList(tmp));
		}
		tmp = cwd.listFiles(onlyFileFilter());
		if (tmp != null) {
			Arrays.sort(tmp);
			entries.addAll(Arrays.asList(tmp));
		}
		adapter.notifyDataSetChanged();
		TextView cwdView = (TextView) findViewById(R.id.cwd);
		cwdView.setText("[" + cwd.toString() + "]");
	}

	@Override
	public void onItemClick(AdapterView<?> listView, View view, int pos, long id) {
		File f = (File) listView.getItemAtPosition(pos);
		if (f.isDirectory()) {
			cwd = new File(f.toString());
			updateEntriesAndViews();
			// finish();
			// startActivity(intent);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.up:
			String p = cwd.getParent();
			cwd = (p != null ? new File(p) : ROOT_DIR);
			updateEntriesAndViews();
			break;
		}
	}
}
