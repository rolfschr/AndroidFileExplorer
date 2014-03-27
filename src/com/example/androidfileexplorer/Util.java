package com.example.androidfileexplorer;

import java.io.File;
import java.io.FilenameFilter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class Util {
	public static FilenameFilter onlyDirFilter() {
		return new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		};
	}

	public static FilenameFilter onlyFileFilter() {
		return new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isFile();
			}
		};
	}

	public static void openFileWithApp(Context context, File f) {
		if (!f.canRead()) {
			toolTip(context, R.string.no_read_perm);
		} else {
			String type = null;
			String extension = MimeTypeMap.getFileExtensionFromUrl(f.getName()
					.replace(" ", "%20"));
			MimeTypeMap map = MimeTypeMap.getSingleton();
			type = map.getMimeTypeFromExtension(extension);
			if (type == null) {
				type = PreferenceManager
						.getDefaultSharedPreferences(context)
						.getString(
								SettingsActivity.PREF_UNKNOWN_MIME_TYPE_HANLDER,
								"*/*");
			}
			Intent intent = new Intent();
			intent.setAction(android.content.Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(f), type);
			context.startActivity(intent);
		}
	}

	static public void deleteFile(Context context, ArrayAdapter<File> adapter,
			File f) {
		if (f.delete()) {
			Util.toolTip(context, R.string.file_deleted);
			adapter.remove(f);
			adapter.notifyDataSetChanged();
		} else {
			Util.toolTip(context, R.string.could_not_del_file);
		}
	}

	public static void renameFile(Context context, ArrayAdapter<File> adapter,
			File f) {
		toolTip(context, "not yet implemented :(");

	}

	static public void toolTip(Context context, String msg) {
		int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, msg, duration);
		toast.show();
	}

	static public void toolTip(Context context, int id) {
		toolTip(context, context.getResources().getString(id));
	}

}
