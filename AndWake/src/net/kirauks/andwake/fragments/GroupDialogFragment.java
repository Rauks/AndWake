package net.kirauks.andwake.fragments;

import java.util.ArrayList;
import java.util.List;

import net.kirauks.andwake.R;
import net.kirauks.andwake.fragments.handlers.CancelHandler;
import net.kirauks.andwake.fragments.handlers.CreateGroupHandler;
import net.kirauks.andwake.fragments.handlers.UpdateGroupHandler;
import net.kirauks.andwake.targets.Computer;
import net.kirauks.andwake.targets.Group;
import net.kirauks.andwake.targets.db.DataSourceHelper;
import net.kirauks.andwake.utils.FormValidator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
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

public class GroupDialogFragment extends DialogFragment {
	public class GroupComputersAdapter extends ArrayAdapter<Computer> {
		public GroupComputersAdapter(Context context, List<Computer> objects) {
			super(context, R.layout.list_element_dialog_group_computer, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Computer item = this.getItem(position);

			LayoutInflater inflater = ((Activity) this.getContext())
					.getLayoutInflater();
			View rootView = inflater.inflate(
					R.layout.list_element_dialog_group_computer, parent, false);

			CheckBox check = (CheckBox) rootView
					.findViewById(R.id.list_element_dialog_group_computer_check);
			check.setText(item.getName());
			check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean isChecked) {
					if (isChecked) {
						if (!GroupDialogFragment.this.selectedComputers
								.contains(item)) {
							GroupDialogFragment.this.selectedComputers
									.add(item);
						}
					} else {
						GroupDialogFragment.this.selectedComputers
								.remove(item);
					}
				}
			});

			check.setChecked(GroupDialogFragment.this.selectedComputers
					.contains(item));

			return rootView;
		}
	}

	public static GroupDialogFragment newInstance() {
		GroupDialogFragment df = new GroupDialogFragment();
		Bundle bundle = new Bundle();
		df.setArguments(bundle);
		return df;
	}

	public static GroupDialogFragment newInstance(Group toEdit) {
		GroupDialogFragment df = new GroupDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable("edit", toEdit);
		df.selectedComputers.addAll(toEdit.getChildren());
		df.setArguments(bundle);
		return df;
	}

	private final List<Computer> selectedComputers = new ArrayList<Computer>();

	private void handleNegativeClick() {
		((CancelHandler)this.getActivity()).handleCancel();
	}
	
	private void handlePositiveClick() {
		Dialog dialog = this.getDialog();

		EditText nameField = (EditText) dialog
				.findViewById(R.id.dialog_group_name_field);

		String name = nameField.getText().toString();

		Group toEdit = (Group) this.getArguments().getParcelable("edit");
		if (toEdit != null) {
			toEdit.setName(name);
			toEdit.getChildren().clear();
			toEdit.getChildren().addAll(this.selectedComputers);
			((UpdateGroupHandler)this.getActivity()).handleUpdate(toEdit);
		} else {
			Group toCreate = new Group();
			toCreate.setName(name);
			toCreate.getChildren().addAll(this.selectedComputers);
			((CreateGroupHandler)this.getActivity()).handleCreate(toCreate);
		}
	}

	@Override
	public void onActivityCreated(Bundle arg0) {
		super.onActivityCreated(arg0);

		final Group toEdit = this.getArguments().getParcelable("edit");
		final AlertDialog dialog = (AlertDialog) this.getDialog();
		dialog.setOnShowListener(new DialogInterface.OnShowListener() {
			@Override
			public void onShow(DialogInterface dialogInterface) {
				Button okButton = dialog
						.getButton(DialogInterface.BUTTON_POSITIVE);
				okButton.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						if (GroupDialogFragment.this.validateForm()) {
							dialog.dismiss();
							GroupDialogFragment.this.handlePositiveClick();
						}
					}
				});

				LinearLayout computers = (LinearLayout) dialog.findViewById(R.id.dialog_group_computers_list);
				Context context = GroupDialogFragment.this.getActivity();
				GroupComputersAdapter adapter = new GroupComputersAdapter(
						context, new DataSourceHelper(context).getComputerDataSource().getAllComputers());

				for (int i = 0; i < adapter.getCount(); i++) {
					View v = adapter.getView(i, null, null);
					computers.addView(v);
				}

				if (toEdit != null) {
					EditText nameField = (EditText) dialog
							.findViewById(R.id.dialog_group_name_field);

					nameField.setText(toEdit.getName());
				}
			}
		});
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		this.setRetainInstance(true);
		super.onCreate(savedInstanceState);
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater inflater = this.getActivity().getLayoutInflater();
		final Group toEdit = (Group) this.getArguments().getParcelable("edit");
		final AlertDialog dialog = new AlertDialog.Builder(this.getActivity())
				.setIcon(R.drawable.ic_dialog_edit)
				.setView(inflater.inflate(R.layout.dialog_fragment_group, null))
				.setPositiveButton(R.string.dialog_ok, null)
				.setNegativeButton(R.string.dialog_cancel, new OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						GroupDialogFragment.this.handleNegativeClick();
					}
				}).create();

		if (toEdit == null) {
			dialog.setTitle(R.string.dialog_group_title_add);
		} else {
			dialog.setTitle(R.string.dialog_group_title_edit);
		}

		return dialog;
	}

	@Override
	public void onDestroyView() {
		if ((this.getDialog() != null) && this.getRetainInstance()) {
			this.getDialog().setDismissMessage(null);
		}
		super.onDestroyView();
	}

	private boolean validateForm() {
		boolean valid = true;
		Dialog dialog = this.getDialog();

		EditText nameField = (EditText) dialog
				.findViewById(R.id.dialog_group_name_field);

		if (FormValidator.isEmpty(nameField)) {
			valid = false;
			nameField.setError(this.getResources().getString(
					R.string.dialog_group_error_name_empty));
		}
		return valid;
	}
}
