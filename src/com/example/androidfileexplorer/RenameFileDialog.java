package com.example.androidfileexplorer;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;

public class RenameFileDialog extends DialogFragment {
	public interface RenameFileDialogListener {
		public void onDialogPostiveClick(File f, String newName);
	}

	private static final String KEY_FILE = "file";
	public static final String TAG = "rename_dialog";

	static RenameFileDialog newInstance(File f) {
		RenameFileDialog d = new RenameFileDialog();
		Bundle args = new Bundle();
		args.putSerializable(KEY_FILE, f);
		d.setArguments(args);
		return d;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		View layout = inflater.inflate(R.layout.rename_dialog, null);
		final EditText et = (EditText) layout.findViewById(R.id.new_filename);
		final File f = (File) getArguments().getSerializable(KEY_FILE);
		et.setText(f.getName());
		TextView tv = (TextView) layout.findViewById(R.id.old_filename);
		tv.setText(f.getName());
		builder.setView(layout);
		builder.setPositiveButton(R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						File to = new File(f.getParent(), et.getText()
								.toString());
						if (f.renameTo(to)) {
							Util.toolTip(getActivity(),
									R.string.successfully_renamed);
						} else {
							Util.toolTip(getActivity(),
									R.string.renaming_failed);
						}
					}
				});
		builder.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
					}
				});

		AlertDialog dialog = builder.create();
		dialog.getWindow().setSoftInputMode(
				LayoutParams.SOFT_INPUT_STATE_VISIBLE);
		return dialog;
	}
}
