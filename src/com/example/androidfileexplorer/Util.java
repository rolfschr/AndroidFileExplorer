package com.example.androidfileexplorer;

import java.io.File;
import java.io.FilenameFilter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;

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
		String type = null;
		String extension = MimeTypeMap.getFileExtensionFromUrl(f.getName()
				.replace(" ", "%20"));
		MimeTypeMap map = MimeTypeMap.getSingleton();
		type = map.getMimeTypeFromExtension(extension);
		if (type == null) {
			type = "*/*";
		}
		Intent intent = new Intent();
		intent.setAction(android.content.Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(f), type);
		context.startActivity(intent);
	}

}
