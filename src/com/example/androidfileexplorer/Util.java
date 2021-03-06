package com.example.androidfileexplorer;

import java.io.File;
import java.io.FilenameFilter;
import java.util.List;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.webkit.MimeTypeMap;
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
			String extension = MimeTypeMap.getFileExtensionFromUrl(Uri.encode(f
					.getName()));
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

	static public boolean deleteFiles(MainActivity context, List<File> files) {
		boolean result = true;
		for (File f : files) {
			result = result && f.delete();
		}
		if (result) {
			Util.toolTip(context, R.string.files_deleted);
		} else {
			Util.toolTip(context, R.string.could_not_del_files);
		}
		context.updateDirList();
		return false;
	}

	public static void renameFile(Activity context, File f) {
		if (!f.canWrite()) {
			toolTip(context, R.string.no_write_perm);
		} else {
			DialogFragment renameDialog = RenameFileDialog.newInstance(f);
			renameDialog.show(context.getFragmentManager(),
					RenameFileDialog.TAG);
		}
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
