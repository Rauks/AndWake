package net.kirauks.andwake;

import net.kirauks.andwake.targets.Computer;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

public class ComputerDeleteDialogFragment extends DialogFragment{
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		AlertDialog dialog = new AlertDialog.Builder(this.getActivity())
			.setTitle(R.string.dialog_delete_computer_title)
	        .setView(inflater.inflate(R.layout.dialog_fragment_delete_computer, null))
	        .setPositiveButton(R.string.dialog_ok, new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ComputerDeleteDialogFragment.this.handlePositiveClick();
				}
			})
	        .setNegativeButton(R.string.dialog_cancel, null)
	        .create();
		
		return dialog;
	}
	
	private void handlePositiveClick(){
		if(this.delete != null){
			((MainActivity) this.getActivity()).doDeleteComputer(this.delete);
		}
	}
	
	private Computer delete;
	public void setDelete(Computer item){
		this.delete = item;
	}
}
