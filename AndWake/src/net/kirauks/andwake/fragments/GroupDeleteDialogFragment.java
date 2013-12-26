package net.kirauks.andwake.fragments;

import net.kirauks.andwake.MainActivity;
import net.kirauks.andwake.R;
import net.kirauks.andwake.targets.Group;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

public class GroupDeleteDialogFragment extends DialogFragment {
	public static GroupDeleteDialogFragment newInstance(Group toDelete) {
		GroupDeleteDialogFragment df = new GroupDeleteDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable("delete", toDelete);
		df.setArguments(bundle);
		return df;
	}

	private void handlePositiveClick() {
		Group toDelete = (Group) this.getArguments().getParcelable("delete");
		if (toDelete != null) {
			((MainActivity) this.getActivity()).doDeleteGroup(toDelete);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.setRetainInstance(true);
		super.onCreate(savedInstanceState);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = this.getActivity().getLayoutInflater();

		AlertDialog dialog = new AlertDialog.Builder(this.getActivity())
				.setTitle(R.string.dialog_delete_group_title)
				.setIcon(R.drawable.ic_dialog_discard)
				.setView(
						inflater.inflate(R.layout.dialog_fragment_delete_group,
								null))
				.setPositiveButton(R.string.dialog_ok, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						GroupDeleteDialogFragment.this.handlePositiveClick();
					}
				}).setNegativeButton(R.string.dialog_cancel, null).create();
		return dialog;
	}

	@Override
	public void onDestroyView() {
		if ((this.getDialog() != null) && this.getRetainInstance()) {
			this.getDialog().setDismissMessage(null);
		}
		super.onDestroyView();
	}
}
