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
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		File dir = File.listRoots()[0];
		TextView cwdView = (TextView) findViewById(R.id.cwd);
		cwdView.setText("[" + dir.toString() + "]");

		List<File> entries = new ArrayList<File>();
		File[] tmp = dir.listFiles(onlyDirFilter());
		Arrays.sort(tmp);
		entries.addAll(Arrays.asList(tmp));
		tmp = dir.listFiles(onlyFileFilter());
		Arrays.sort(tmp);
		entries.addAll(Arrays.asList(tmp));

		ArrayAdapter<File> adapter = DirListArrayAdapter(entries);
		ListView listView = (ListView) findViewById(R.id.entry_list_view);
		listView.setAdapter(adapter);
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

	private ArrayAdapter<File> DirListArrayAdapter(List<File> entries) {
		return new ArrayAdapter<File>(this, R.layout.entry, entries) {

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				if (convertView == null) {
					LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					convertView = vi.inflate(R.layout.entry, null);
				}
				File f = getItem(position);
				TextView tv = (TextView) convertView.findViewById(R.id.entry);
				tv.setText(f.toString());
				if (f.isDirectory()) {
					tv.setTextColor(getResources().getColor(R.color.dir_color));
				} else {
					tv.setTextColor(getResources().getColor(R.color.file_color));
				}

				return convertView;
			}

		};
	}
}
