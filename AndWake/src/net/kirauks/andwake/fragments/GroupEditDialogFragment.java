package net.kirauks.andwake.fragments;

import java.util.ArrayList;
import java.util.List;

import net.kirauks.andwake.MainActivity;
import net.kirauks.andwake.R;
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
	public static GroupEditDialogFragment newInstance() {
		GroupEditDialogFragment df = new GroupEditDialogFragment();
	    Bundle bundle = new Bundle();
	    df.setArguments(bundle);
	    return df;
	}
	public static GroupEditDialogFragment newInstance(Group toEdit) {
		GroupEditDialogFragment df = new GroupEditDialogFragment();
	    Bundle bundle = new Bundle();
	    bundle.putParcelable("edit", toEdit);
	    df.selectedComputers.addAll(toEdit.getChildren());
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
	
	private List<Computer> selectedComputers = new ArrayList<Computer>();
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = this.getActivity().getLayoutInflater();
		final Group toEdit = (Group)this.getArguments().getParcelable("edit");
		final AlertDialog dialog = new AlertDialog.Builder(this.getActivity())
			.setIcon(R.drawable.ic_action_edit)
            .setView(inflater.inflate(R.layout.dialog_fragment_group, null))
            .setPositiveButton(R.string.dialog_ok, null)
            .setNegativeButton(R.string.dialog_cancel, null)
            .create();
		
		if(toEdit == null){
			dialog.setTitle(R.string.dialog_group_title_add);
		}
		else{
			dialog.setTitle(R.string.dialog_group_title_edit);
		}
		
        return dialog;
    }
	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);

		final Group toEdit = (Group)this.getArguments().getParcelable("edit");
		final AlertDialog dialog = (AlertDialog)this.getDialog();
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
				GroupComputersAdapter adapter = new GroupComputersAdapter(activity, activity.getDataSourceHelper().getComputerDataSource().getAllComputers());
				
				for (int i = 0; i < adapter.getCount(); i++) {
				    View v = adapter.getView(i, null, null);
				    computers.addView(v);
				}
				
				if(toEdit != null){
					EditText nameField = (EditText)dialog.findViewById(R.id.dialog_group_name_field);
					
					nameField.setText(toEdit.getName());
				}
			}
		});
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
						if(!GroupEditDialogFragment.this.selectedComputers.contains(item)){
							GroupEditDialogFragment.this.selectedComputers.add(item);
						}
					}
					else{
						GroupEditDialogFragment.this.selectedComputers.remove(item);
					}
				}
			});
			
			check.setChecked(GroupEditDialogFragment.this.selectedComputers.contains(item));
			
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

		Group toEdit = (Group)this.getArguments().getParcelable("edit");
		if(toEdit != null){
			toEdit.setName(name);
			toEdit.getChildren().clear();
			toEdit.getChildren().addAll(this.selectedComputers);
			((MainActivity) this.getActivity()).doEditGroup(toEdit);
		}
		else{
			((MainActivity) this.getActivity()).doAddGroup(name, this.selectedComputers);
		}
	}
}
