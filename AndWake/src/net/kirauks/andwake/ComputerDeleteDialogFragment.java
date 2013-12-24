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
	public static ComputerDeleteDialogFragment newInstance(Computer toDelete) {
		ComputerDeleteDialogFragment df = new ComputerDeleteDialogFragment();
	    Bundle bundle = new Bundle();
	    bundle.putParcelable("delete", toDelete);
	    df.setArguments(bundle);
	    return df;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.setRetainInstance(true);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onDestroyView() {
	    if (this.getDialog() != null && this.getRetainInstance())
	        this.getDialog().setDismissMessage(null);
	    super.onDestroyView();
	}

	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = this.getActivity().getLayoutInflater();
		
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
		Computer toDelete = (Computer)this.getArguments().getParcelable("delete");
		if(toDelete != null){
			((MainActivity) this.getActivity()).doDeleteComputer(toDelete);
		}
	}
}
