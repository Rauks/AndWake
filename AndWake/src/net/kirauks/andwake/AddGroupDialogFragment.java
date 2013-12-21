package net.kirauks.andwake;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class AddGroupDialogFragment extends DialogFragment{
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(this.getActivity())
            .setTitle(R.string.menu_add_group)
            .setPositiveButton(R.string.dialog_ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	AddGroupDialogFragment.this.handlePositiveClick();
                    }
                }
            )
            .setNegativeButton(R.string.dialog_cancel,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    	AddGroupDialogFragment.this.handleNegativeClick();
                    }
                }
            )
            .create();
    }
	
	private void handlePositiveClick(){
		((MainActivity) this.getActivity()).doAddGroup();
	}
	
	private void handleNegativeClick(){		
	}
}
