package net.kirauks.andwake.appwidget.fragments;

import java.util.ArrayList;
import java.util.List;

import net.kirauks.andwake.R;
import net.kirauks.andwake.targets.Group;
import net.kirauks.andwake.targets.db.DataSourceHelper;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class ConfigureGroupDialogFragment extends ConfigureDialogFragment {
	OnConfigureGroupListener configureListener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		DataSourceHelper db = new DataSourceHelper(this.getActivity());
		final List<Group> groups = db.getGroupDataSource().getAllGroups();

		final List<String> groupsNames = new ArrayList<String>();
		for (Group c : groups) {
			groupsNames.add(c.getName());
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(
				this.getActivity());
		builder.setTitle(R.string.appwidget_group_dialog_config_title)
				.setItems(groupsNames.toArray(new String[groupsNames.size()]),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (ConfigureGroupDialogFragment.this.configureListener != null) {
									ConfigureGroupDialogFragment.this.configureListener
											.onConfigureGroup(groups.get(which));
								}
							}
						});
		return builder.create();
	}

	public void setOnConfigureGroup(OnConfigureGroupListener configureListener) {
		this.configureListener = configureListener;
	}
}
