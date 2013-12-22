package net.kirauks.andwake;

import net.kirauks.andwake.packets.WolPacket;
import net.kirauks.andwake.targets.Computer;
import net.kirauks.andwake.utils.FormValidator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ComputerDialogFragment extends DialogFragment{
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		final AlertDialog dialog = new AlertDialog.Builder(this.getActivity())
            .setView(inflater.inflate(R.layout.dialog_fragment_computer, null))
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
		            	if(ComputerDialogFragment.this.validateForm()){
		            		dialog.dismiss();
		            		ComputerDialogFragment.this.handlePositiveClick();
		            	}
		            }
		        });
				
				Computer content = ComputerDialogFragment.this.content;
				if(content != null){
					EditText nameField = (EditText)dialog.findViewById(R.id.dialog_computer_name_field);
					EditText macField1 = (EditText)dialog.findViewById(R.id.dialog_computer_mac_field_1);
					EditText macField2 = (EditText)dialog.findViewById(R.id.dialog_computer_mac_field_2);
					EditText macField3 = (EditText)dialog.findViewById(R.id.dialog_computer_mac_field_3);
					EditText macField4 = (EditText)dialog.findViewById(R.id.dialog_computer_mac_field_4);
					EditText macField5 = (EditText)dialog.findViewById(R.id.dialog_computer_mac_field_5);
					EditText macField6 = (EditText)dialog.findViewById(R.id.dialog_computer_mac_field_6);
					EditText addressField = (EditText)dialog.findViewById(R.id.dialog_computer_address_field);
					EditText portField = (EditText)dialog.findViewById(R.id.dialog_computer_port_field);
					
					nameField.setText(content.getName());
					String[] mac = content.getMac().split(String.valueOf(WolPacket.DEFAULT_MAC_SEPARATOR));
					macField1.setText(mac[0]);
					macField2.setText(mac[1]);
					macField3.setText(mac[2]);
					macField4.setText(mac[3]);
					macField5.setText(mac[4]);
					macField6.setText(mac[5]);
					addressField.setText(content.getAddress());
					portField.setText(String.valueOf(content.getPort()));
				}
			}
		});
		
		if(this.content == null){
			dialog.setTitle(R.string.dialog_computer_title_add);
		}
		else{
			dialog.setTitle(R.string.dialog_computer_title_edit);
		}
		
        return dialog;
    }
	
	private boolean validateForm(){
    	boolean valid = true;
    	Dialog dialog = this.getDialog();
    	
    	EditText nameField = (EditText)dialog.findViewById(R.id.dialog_computer_name_field);
    	TextView macLabel = (TextView)dialog.findViewById(R.id.dialog_computer_mac_label);
    	EditText macField1 = (EditText)dialog.findViewById(R.id.dialog_computer_mac_field_1);
    	EditText macField2 = (EditText)dialog.findViewById(R.id.dialog_computer_mac_field_2);
    	EditText macField3 = (EditText)dialog.findViewById(R.id.dialog_computer_mac_field_3);
    	EditText macField4 = (EditText)dialog.findViewById(R.id.dialog_computer_mac_field_4);
    	EditText macField5 = (EditText)dialog.findViewById(R.id.dialog_computer_mac_field_5);
    	EditText macField6 = (EditText)dialog.findViewById(R.id.dialog_computer_mac_field_6);
    	EditText addressField = (EditText)dialog.findViewById(R.id.dialog_computer_address_field);
    	EditText portField = (EditText)dialog.findViewById(R.id.dialog_computer_port_field);
    	
    	if(FormValidator.isEmpty(nameField)){
    		valid = false;
    		nameField.setError(getResources().getString(R.string.dialog_computer_error_name_empty));
    	}

    	macLabel.setError(null);
    	if(!FormValidator.isLength(macField1, 2) || !FormValidator.isHexadecimal(macField1)
    			|| !FormValidator.isLength(macField2, 2) || !FormValidator.isHexadecimal(macField2)
    			|| !FormValidator.isLength(macField3, 2) || !FormValidator.isHexadecimal(macField3)
    			|| !FormValidator.isLength(macField4, 2) || !FormValidator.isHexadecimal(macField4)
    			|| !FormValidator.isLength(macField5, 2) || !FormValidator.isHexadecimal(macField5)
    			|| !FormValidator.isLength(macField6, 2) || !FormValidator.isHexadecimal(macField6)){
    		valid = false;
    		macLabel.setError(getResources().getString(R.string.dialog_computer_error_mac_hex));
    	}
    	
    	if(FormValidator.isEmpty(addressField)){
    		valid = false;
    		addressField.setError(getResources().getString(R.string.dialog_computer_error_address_empty));
    	}
    	
    	if(FormValidator.isEmpty(portField)){
    		valid = false;
    		portField.setError(getResources().getString(R.string.dialog_computer_error_port_empty));
    	}
    	
    	return valid;
	}
	
	private void handlePositiveClick(){
    	Dialog dialog = this.getDialog();
		
		EditText nameField = (EditText)dialog.findViewById(R.id.dialog_computer_name_field);
		EditText macField1 = (EditText)dialog.findViewById(R.id.dialog_computer_mac_field_1);
		EditText macField2 = (EditText)dialog.findViewById(R.id.dialog_computer_mac_field_2);
		EditText macField3 = (EditText)dialog.findViewById(R.id.dialog_computer_mac_field_3);
		EditText macField4 = (EditText)dialog.findViewById(R.id.dialog_computer_mac_field_4);
		EditText macField5 = (EditText)dialog.findViewById(R.id.dialog_computer_mac_field_5);
		EditText macField6 = (EditText)dialog.findViewById(R.id.dialog_computer_mac_field_6);
		EditText addressField = (EditText)dialog.findViewById(R.id.dialog_computer_address_field);
		EditText portField = (EditText)dialog.findViewById(R.id.dialog_computer_port_field);
	
		String name = nameField.getText().toString();
		char macSep = WolPacket.DEFAULT_MAC_SEPARATOR;
		String mac = new StringBuilder().append(macField1.getText().toString())
			.append(macSep).append(macField2.getText().toString())
			.append(macSep).append(macField3.getText().toString())
			.append(macSep).append(macField4.getText().toString())
			.append(macSep).append(macField5.getText().toString())
			.append(macSep).append(macField6.getText().toString())
			.toString();
		String address = addressField.getText().toString();
		int port = Integer.parseInt(portField.getText().toString());
		
		if(this.content != null){
			((MainActivity) this.getActivity()).doEditComputer(this.content.getId(), name, mac , address, port);
		}
		else{
			((MainActivity) this.getActivity()).doAddComputer(name, mac , address, port);
		}
	}

	private Computer content;
	public void setEdit(Computer item) {
		this.content = item;
	}
}
