package com.example.androidfileexplorer;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DirListArrayAdapter extends ArrayAdapter<File> {
	Context mContext;

	public DirListArrayAdapter(Context context, int resource, List<File> objects) {
		super(context, resource, objects);
		this.mContext = context;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		if (convertView == null) {
			LayoutInflater vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = vi.inflate(R.layout.entry, null);
		}
		File f = getItem(pos);
		TextView tv = (TextView) convertView.findViewById(R.id.entry);
		tv.setText(f.getName());
		if (f.isDirectory()) {
			tv.setTextColor(mContext.getResources().getColor(R.color.dir_color));
		} else {
			tv.setTextColor(mContext.getResources()
					.getColor(R.color.file_color));
		}

		return convertView;
	}
}
