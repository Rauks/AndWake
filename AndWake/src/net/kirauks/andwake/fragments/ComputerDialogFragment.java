package net.kirauks.andwake.fragments;

import java.util.Locale;

import net.kirauks.andwake.R;
import net.kirauks.andwake.fragments.handlers.CancelHandler;
import net.kirauks.andwake.fragments.handlers.CreateComputerHandler;
import net.kirauks.andwake.fragments.handlers.UpdateComputerHandler;
import net.kirauks.andwake.packets.WolPacket;
import net.kirauks.andwake.targets.Computer;
import net.kirauks.andwake.utils.FormValidator;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ComputerDialogFragment extends DialogFragment {
    public static ComputerDialogFragment newInstance() {
        ComputerDialogFragment df = new ComputerDialogFragment();
        Bundle bundle = new Bundle();
        df.setArguments(bundle);
        return df;
    }

    public static ComputerDialogFragment newInstance(Computer toEdit) {
        ComputerDialogFragment df = new ComputerDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("edit", toEdit);
        df.setArguments(bundle);
        return df;
    }

    private void handleNegativeClick() {
        ((CancelHandler) this.getActivity()).handleCancel();
    }

    private void handlePositiveClick() {
        Dialog dialog = this.getDialog();

        EditText nameField = (EditText) dialog.findViewById(R.id.dialog_computer_name_field);
        EditText macField1 = (EditText) dialog.findViewById(R.id.dialog_computer_mac_field_1);
        EditText macField2 = (EditText) dialog.findViewById(R.id.dialog_computer_mac_field_2);
        EditText macField3 = (EditText) dialog.findViewById(R.id.dialog_computer_mac_field_3);
        EditText macField4 = (EditText) dialog.findViewById(R.id.dialog_computer_mac_field_4);
        EditText macField5 = (EditText) dialog.findViewById(R.id.dialog_computer_mac_field_5);
        EditText macField6 = (EditText) dialog.findViewById(R.id.dialog_computer_mac_field_6);
        EditText addressField = (EditText) dialog.findViewById(R.id.dialog_computer_address_field);
        EditText portField = (EditText) dialog.findViewById(R.id.dialog_computer_port_field);

        String name = nameField.getText().toString();
        char macSep = WolPacket.DEFAULT_MAC_SEPARATOR;
        String mac = new StringBuilder().append(macField1.getText().toString()).append(macSep).append(macField2.getText().toString()).append(macSep).append(macField3.getText().toString()).append(macSep).append(macField4.getText().toString()).append(macSep).append(macField5.getText().toString()).append(macSep).append(macField6.getText().toString()).toString();
        mac = mac.toUpperCase(Locale.US);
        String address = addressField.getText().toString();
        int port = Integer.parseInt(portField.getText().toString());

        Computer toEdit = (Computer) this.getArguments().getParcelable("edit");
        if (toEdit != null) {
            toEdit.setName(name);
            toEdit.setMac(mac);
            toEdit.setAddress(address);
            toEdit.setPort(port);
            ((UpdateComputerHandler) this.getActivity()).handleUpdate(toEdit);
        }
        else {
            Computer toCreate = new Computer();
            toCreate.setName(name);
            toCreate.setMac(mac);
            toCreate.setAddress(address);
            toCreate.setPort(port);
            ((CreateComputerHandler) this.getActivity()).handleCreate(toCreate);
        }
    }

    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {
        LayoutInflater inflater = this.getActivity().getLayoutInflater();
        final Computer toEdit = this.getArguments().getParcelable("edit");
        final AlertDialog dialog = new AlertDialog.Builder(this.getActivity()).setIcon(R.drawable.ic_dialog_edit).setView(inflater.inflate(R.layout.dialog_fragment_computer, null)).setPositiveButton(R.string.dialog_ok, null).setNegativeButton(R.string.dialog_cancel, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ComputerDialogFragment.this.handleNegativeClick();
            }
        }).create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Button okButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (ComputerDialogFragment.this.validateForm()) {
                            dialog.dismiss();
                            ComputerDialogFragment.this.handlePositiveClick();
                        }
                    }
                });
                
                EditText nameField = (EditText) dialog.findViewById(R.id.dialog_computer_name_field);
                EditText macField1 = (EditText) dialog.findViewById(R.id.dialog_computer_mac_field_1);
                EditText macField2 = (EditText) dialog.findViewById(R.id.dialog_computer_mac_field_2);
                EditText macField3 = (EditText) dialog.findViewById(R.id.dialog_computer_mac_field_3);
                EditText macField4 = (EditText) dialog.findViewById(R.id.dialog_computer_mac_field_4);
                EditText macField5 = (EditText) dialog.findViewById(R.id.dialog_computer_mac_field_5);
                EditText macField6 = (EditText) dialog.findViewById(R.id.dialog_computer_mac_field_6);
                EditText addressField = (EditText) dialog.findViewById(R.id.dialog_computer_address_field);
                EditText portField = (EditText) dialog.findViewById(R.id.dialog_computer_port_field);
                if (savedInstanceState != null) {
                    nameField.setText(savedInstanceState.getString("input_name"));
                    macField1.setText(savedInstanceState.getString("input_mac1"));
                    macField2.setText(savedInstanceState.getString("input_mac2"));
                    macField3.setText(savedInstanceState.getString("input_mac3"));
                    macField4.setText(savedInstanceState.getString("input_mac4"));
                    macField5.setText(savedInstanceState.getString("input_mac5"));
                    macField6.setText(savedInstanceState.getString("input_mac6"));
                    addressField.setText(savedInstanceState.getString("input_address"));
                    portField.setText(savedInstanceState.getString("input_port"));
                }
                else if (toEdit != null) {
                    nameField.setText(toEdit.getName());
                    String[] mac = toEdit.getMac().split(String.valueOf(WolPacket.DEFAULT_MAC_SEPARATOR));
                    macField1.setText(mac[0]);
                    macField2.setText(mac[1]);
                    macField3.setText(mac[2]);
                    macField4.setText(mac[3]);
                    macField5.setText(mac[4]);
                    macField6.setText(mac[5]);
                    addressField.setText(toEdit.getAddress());
                    portField.setText(String.valueOf(toEdit.getPort()));
                }
            }
        });

        if (toEdit == null) {
            dialog.setTitle(R.string.dialog_computer_title_add);
        }
        else {
            dialog.setTitle(R.string.dialog_computer_title_edit);
        }

        return dialog;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        Dialog dialog = this.getDialog();
        
        EditText nameField = (EditText) dialog.findViewById(R.id.dialog_computer_name_field);
        EditText macField1 = (EditText) dialog.findViewById(R.id.dialog_computer_mac_field_1);
        EditText macField2 = (EditText) dialog.findViewById(R.id.dialog_computer_mac_field_2);
        EditText macField3 = (EditText) dialog.findViewById(R.id.dialog_computer_mac_field_3);
        EditText macField4 = (EditText) dialog.findViewById(R.id.dialog_computer_mac_field_4);
        EditText macField5 = (EditText) dialog.findViewById(R.id.dialog_computer_mac_field_5);
        EditText macField6 = (EditText) dialog.findViewById(R.id.dialog_computer_mac_field_6);
        EditText addressField = (EditText) dialog.findViewById(R.id.dialog_computer_address_field);
        EditText portField = (EditText) dialog.findViewById(R.id.dialog_computer_port_field);

        savedInstanceState.putString("input_name", nameField.getText().toString());
        savedInstanceState.putString("input_mac1", macField1.getText().toString());
        savedInstanceState.putString("input_mac2", macField2.getText().toString());
        savedInstanceState.putString("input_mac3", macField3.getText().toString());
        savedInstanceState.putString("input_mac4", macField4.getText().toString());
        savedInstanceState.putString("input_mac5", macField5.getText().toString());
        savedInstanceState.putString("input_mac6", macField6.getText().toString());
        savedInstanceState.putString("input_address", addressField.getText().toString());
        savedInstanceState.putString("input_port", portField.getText().toString());
    }

    private boolean validateForm() {
        boolean valid = true;
        Dialog dialog = this.getDialog();

        EditText nameField = (EditText) dialog.findViewById(R.id.dialog_computer_name_field);
        TextView macLabel = (TextView) dialog.findViewById(R.id.dialog_computer_mac_label);
        EditText macField1 = (EditText) dialog.findViewById(R.id.dialog_computer_mac_field_1);
        EditText macField2 = (EditText) dialog.findViewById(R.id.dialog_computer_mac_field_2);
        EditText macField3 = (EditText) dialog.findViewById(R.id.dialog_computer_mac_field_3);
        EditText macField4 = (EditText) dialog.findViewById(R.id.dialog_computer_mac_field_4);
        EditText macField5 = (EditText) dialog.findViewById(R.id.dialog_computer_mac_field_5);
        EditText macField6 = (EditText) dialog.findViewById(R.id.dialog_computer_mac_field_6);
        EditText addressField = (EditText) dialog.findViewById(R.id.dialog_computer_address_field);
        EditText portField = (EditText) dialog.findViewById(R.id.dialog_computer_port_field);

        if (FormValidator.isEmpty(nameField)) {
            valid = false;
            nameField.setError(this.getResources().getString(R.string.dialog_computer_error_name_empty));
        }

        macLabel.setError(null);
        if (!FormValidator.isLength(macField1, 2) || !FormValidator.isHexadecimal(macField1) || !FormValidator.isLength(macField2, 2) || !FormValidator.isHexadecimal(macField2) || !FormValidator.isLength(macField3, 2) || !FormValidator.isHexadecimal(macField3) || !FormValidator.isLength(macField4, 2) || !FormValidator.isHexadecimal(macField4) || !FormValidator.isLength(macField5, 2) || !FormValidator.isHexadecimal(macField5) || !FormValidator.isLength(macField6, 2) || !FormValidator.isHexadecimal(macField6)) {
            valid = false;
            macLabel.setError(this.getResources().getString(R.string.dialog_computer_error_mac_hex));
        }

        if (FormValidator.isEmpty(addressField)) {
            valid = false;
            addressField.setError(this.getResources().getString(R.string.dialog_computer_error_address_empty));
        }

        if (FormValidator.isEmpty(portField)) {
            valid = false;
            portField.setError(this.getResources().getString(R.string.dialog_computer_error_port_empty));
        }

        return valid;
    }
}
