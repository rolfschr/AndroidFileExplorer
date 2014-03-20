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
	private final Context mContext;

	static class ViewHolder {
		public TextView text;
	}

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
			ViewHolder holder = new ViewHolder();
			holder.text = (TextView) convertView.findViewById(R.id.entry);
			convertView.setTag(holder);
		}
		ViewHolder holder = (ViewHolder) convertView.getTag();
		File f = getItem(pos);
		holder.text.setText(f.getName());
		if (f.isDirectory()) {
			holder.text.setTextColor(mContext.getResources().getColor(
					R.color.dir_color));
		} else {
			holder.text.setTextColor(mContext.getResources().getColor(
					R.color.file_color));
		}

		return convertView;
	}
}
