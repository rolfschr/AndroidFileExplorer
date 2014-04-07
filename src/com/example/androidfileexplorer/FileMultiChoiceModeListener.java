package com.example.androidfileexplorer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView.MultiChoiceModeListener;

public class FileMultiChoiceModeListener implements MultiChoiceModeListener {
	MainActivity mainActivity;
	MenuItem renameItem;
	List<File> files;
	private final int renameItemId = 0;
	private final int deleteItemId = 1;

	public FileMultiChoiceModeListener(MainActivity context) {
		this.mainActivity = context;
		this.files = new ArrayList<File>();
	}

	@Override
	public void onItemCheckedStateChanged(ActionMode mode, int position,
			long id, boolean checked) {
		File f = (File) mainActivity.listView.getItemAtPosition(position);
		if (checked) {
			if (f.isDirectory()) {
				// should normally make custom drawable for dirs ...
				Util.toolTip(mainActivity, "Cannot select directories");
				mainActivity.listView.setItemChecked(position, false);
			} else {
				files.add(f);
			}
		} else {
			files.remove(f);
		}

		if (files.size() != 1) {
			renameItem.setVisible(false);
		} else {
			renameItem.setVisible(true);
		}
		mode.setTitle(Integer.toString(files.size()) + " "
				+ mainActivity.getString(R.string.num_selected));
	}

	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		// Respond to clicks on the actions in the CAB
		switch (item.getItemId()) {
		case deleteItemId:
			Util.deleteFiles(mainActivity, files);
			mode.finish(); // Action picked, so close the CAB
			return true;
		case renameItemId:
			if (files.size() == 1) {
				Util.renameFile(mainActivity, files.get(0));
			} else {
				Util.toolTip(null, R.string.internal_error);
			}
			mode.finish();
			return true;
		default:
			return false;
		}
	}

	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		renameItem = menu.add(Menu.NONE, renameItemId, Menu.NONE,
				R.string.mv_file);
		menu.add(Menu.NONE, deleteItemId, Menu.NONE, R.string.rm_file);
		return true;
	}

	@Override
	public void onDestroyActionMode(ActionMode mode) {
		this.files = new ArrayList<File>();
		mainActivity.updateDirList();
	}

	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		return false;
	}
}
