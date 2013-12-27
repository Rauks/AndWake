package net.kirauks.andwake.appwidget.fragments;

import java.util.ArrayList;
import java.util.List;

import net.kirauks.andwake.R;
import net.kirauks.andwake.appwidget.fragments.listeners.OnConfigureComputerListener;
import net.kirauks.andwake.targets.Computer;
import net.kirauks.andwake.targets.db.DataSourceHelper;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class ConfigureComputerDialogFragment extends ConfigureDialogFragment{
	private OnConfigureComputerListener configureListener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		DataSourceHelper db = new DataSourceHelper(this.getActivity());
		final List<Computer> computers = db.getComputerDataSource()
				.getAllComputers();

		final List<String> computersNames = new ArrayList<String>();
		for (Computer c : computers) {
			computersNames.add(c.getName());
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(
				this.getActivity());
		builder.setTitle(R.string.appwidget_computer_dialog_config_title)
				.setItems(
						computersNames
								.toArray(new String[computersNames.size()]),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (ConfigureComputerDialogFragment.this.configureListener != null) {
									ConfigureComputerDialogFragment.this.configureListener
											.onConfigureComputer(computers
													.get(which));
								}
							}
						});
		return builder.create();
	}

	public void setOnConfigureComputer(
			OnConfigureComputerListener configureListener) {
		this.configureListener = configureListener;
	}
}
