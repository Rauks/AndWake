package net.kirauks.andwake;

import net.kirauks.andwake.targets.Group;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

public class GroupDeleteDialogFragment extends DialogFragment{
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		AlertDialog dialog = new AlertDialog.Builder(this.getActivity())
			.setTitle(R.string.dialog_delete_group_title)
	        .setView(inflater.inflate(R.layout.dialog_fragment_delete_group, null))
	        .setPositiveButton(R.string.dialog_ok, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					GroupDeleteDialogFragment.this.handlePositiveClick();
				}
			})
	        .setNegativeButton(R.string.dialog_cancel, null)
	        .create();
		
		return dialog;
	}
	
	private void handlePositiveClick(){
		if(this.delete != null){
			((MainActivity) this.getActivity()).doDeleteGroup(this.delete);
		}
	}
	
	private Group delete;
	public void setDelete(Group item){
		this.delete = item;
	}
}
