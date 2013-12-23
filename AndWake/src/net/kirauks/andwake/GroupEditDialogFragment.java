package net.kirauks.andwake;

import java.util.ArrayList;
import java.util.List;

import net.kirauks.andwake.targets.Computer;
import net.kirauks.andwake.targets.Group;
import net.kirauks.andwake.utils.FormValidator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;

public class GroupEditDialogFragment extends DialogFragment{
	List<Computer> selectedComputers = new ArrayList<Computer>();
	
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
				
				LinearLayout computers = (LinearLayout)dialog.findViewById(R.id.dialog_group_computers_list);
				MainActivity activity = (MainActivity)GroupEditDialogFragment.this.getActivity();
				GroupComputersAdapter adapter = new GroupComputersAdapter(activity, activity.computerDataSource.getAllComputers());
				
				for (int i = 0; i < adapter.getCount(); i++) {
				    View v = adapter.getView(i, null, null);
				    computers.addView(v);
				}
				
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
	
	public class GroupComputersAdapter extends ArrayAdapter<Computer>{
		public GroupComputersAdapter(Context context, List<Computer> objects) {
			super(context, R.layout.list_element_dialog_group_computer, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Computer item = this.getItem(position);

			LayoutInflater inflater = ((Activity)this.getContext()).getLayoutInflater();
			View rootView = inflater.inflate(R.layout.list_element_dialog_group_computer, parent, false);
			
			CheckBox check = (CheckBox) rootView.findViewById(R.id.list_element_dialog_group_computer_check);
			check.setText(item.getName());
			check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					if(isChecked){
						GroupEditDialogFragment.this.selectedComputers.add(item);
					}
					else{
						GroupEditDialogFragment.this.selectedComputers.remove(item);
					}
				}
			});
			
			Group edit = GroupEditDialogFragment.this.edit;
			if(edit != null && edit.getChildren().contains(item)){
				check.setChecked(true);
			}
			
			return rootView;
		}
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
			this.edit.getChildren().clear();
			this.edit.getChildren().addAll(this.selectedComputers);
			((MainActivity) this.getActivity()).doEditGroup(this.edit);
		}
		else{
			((MainActivity) this.getActivity()).doAddGroup(name, this.selectedComputers);
		}
	}

	private Group edit;
	public void setEdit(Group item) {
		this.edit = item;
	}
}
