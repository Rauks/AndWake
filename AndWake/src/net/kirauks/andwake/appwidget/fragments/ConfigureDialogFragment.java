package net.kirauks.andwake.appwidget.fragments;

import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;

public abstract class ConfigureDialogFragment extends DialogFragment {
	OnConfigureCancelListener cancelListener;

	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		if (this.cancelListener != null) {
			this.cancelListener.onConfigureCancel();
		}
	}

	public void setOnConfigureCancel(OnConfigureCancelListener cancelListener) {
		this.cancelListener = cancelListener;
	}
}
