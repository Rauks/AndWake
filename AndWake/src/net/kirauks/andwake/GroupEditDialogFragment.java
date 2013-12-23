package net.kirauks.andwake;

import net.kirauks.andwake.targets.Group;
import net.kirauks.andwake.utils.FormValidator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class GroupEditDialogFragment extends DialogFragment{
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		final AlertDialog dialog = new AlertDialog.Builder(this.getActivity())
            .setView(inflater.inflate(R.layout.dialog_fragment_group, null))
            .setPositiveButton(R.string.dialog_ok, null)
            .setNegativeButton(R.string.dialog_cancel, null)
            .create();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface dialogInterface) {
				Button okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
				okButton.setOnClickListener(new View.OnClickListener() {
		            @Override
		            public void onClick(View view) {
		            	if(GroupEditDialogFragment.this.validateForm()){
		            		dialog.dismiss();
		            		GroupEditDialogFragment.this.handlePositiveClick();
		            	}
		            }
		        });
				
				Group content = GroupEditDialogFragment.this.edit;
				if(content != null){
					EditText nameField = (EditText)dialog.findViewById(R.id.dialog_group_name_field);
					
					nameField.setText(content.getName());
				}
			}
		});
		
		if(this.edit == null){
			dialog.setTitle(R.string.dialog_group_title_add);
		}
		else{
			dialog.setTitle(R.string.dialog_group_title_edit);
		}
		
        return dialog;
    }
	
	private boolean validateForm(){
    	boolean valid = true;
    	Dialog dialog = this.getDialog();
    	
    	EditText nameField = (EditText)dialog.findViewById(R.id.dialog_group_name_field);
    	
    	if(FormValidator.isEmpty(nameField)){
    		valid = false;
    		nameField.setError(getResources().getString(R.string.dialog_group_error_name_empty));
    	}
    	return valid;
	}
	
	private void handlePositiveClick(){
    	Dialog dialog = this.getDialog();
		
		EditText nameField = (EditText)dialog.findViewById(R.id.dialog_group_name_field);
		
		String name = nameField.getText().toString();
		
		if(this.edit != null){
			this.edit.setName(name);
			((MainActivity) this.getActivity()).doEditGroup(this.edit);
		}
		else{
			((MainActivity) this.getActivity()).doAddGroup(name);
		}
	}

	private Group edit;
	public void setEdit(Group item) {
		this.edit = item;
	}
}
