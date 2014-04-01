package com.example.androidfileexplorer;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DirListArrayAdapter extends ArrayAdapter<File> {
	private final Context mContext;
	private final LayoutInflater mLayoutInflater;
	private String dirColor;
	private String fileColor;

	static class ViewHolder {
		public TextView text;
	}

	public DirListArrayAdapter(Context context, int resource, List<File> objects) {
		super(context, resource, objects);
		this.mContext = context;
		mLayoutInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		dirColor = PreferenceManager.getDefaultSharedPreferences(mContext)
				.getString(SettingsActivity.PREF_DIR_COLOR,
						mContext.getString(R.string.pref_dirColor_default));
		fileColor = PreferenceManager.getDefaultSharedPreferences(mContext)
				.getString(SettingsActivity.PREF_FILE_COLOR,
						mContext.getString(R.string.pref_fileColor_default));
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mLayoutInflater.inflate(R.layout.entry, null);
			ViewHolder holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.entry);
			convertView.setTag(holder);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		File f = getItem(pos);
		holder.text.setText(f.getName());
		if (f.isDirectory()) {
			holder.text.setTextColor(Color.parseColor(dirColor));
		} else {
			holder.text.setTextColor(Color.parseColor(fileColor));
		}

		return convertView;
	}
}
