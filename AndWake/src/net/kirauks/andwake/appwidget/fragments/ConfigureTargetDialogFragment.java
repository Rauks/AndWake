package net.kirauks.andwake.appwidget.fragments;

import java.util.ArrayList;
import java.util.List;

import net.kirauks.andwake.targets.Computer;
import net.kirauks.andwake.targets.db.DataSourceHelper;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

public class ConfigureTargetDialogFragment extends ConfigureDialogFragment {
	OnConfigureTargetListener configureListener;
	
	public void setOnConfigureTarget(OnConfigureTargetListener configureListener){
		this.configureListener = configureListener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		DataSourceHelper db = new DataSourceHelper(this.getActivity());
		final List<Computer> computers = db.getComputerDataSource().getAllComputers();
		
		final List<String> computersNames = new ArrayList<String>();
		for (Computer c : computers) {
			computersNames.add(c.getName());
		}

		AlertDialog.Builder builder = new AlertDialog.Builder(
				this.getActivity());
		builder.setTitle("WidgetConfig")
				.setItems(
					computersNames.toArray(new String[computersNames.size()]),
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (ConfigureTargetDialogFragment.this.configureListener != null) {
								ConfigureTargetDialogFragment.this.configureListener
										.onConfigureTarget(computers.get(which));
							}
						}
					});
		return builder.create();
	}
}
