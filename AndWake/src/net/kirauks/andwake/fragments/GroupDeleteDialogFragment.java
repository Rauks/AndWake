package net.kirauks.andwake.fragments;

import net.kirauks.andwake.R;
import net.kirauks.andwake.fragments.handlers.CancelHandler;
import net.kirauks.andwake.fragments.handlers.DeleteGroupHandler;
import net.kirauks.andwake.targets.Group;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

public class GroupDeleteDialogFragment extends DialogFragment {
    public static GroupDeleteDialogFragment newInstance(Group toDelete) {
        GroupDeleteDialogFragment df = new GroupDeleteDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("delete", toDelete);
        df.setArguments(bundle);
        return df;
    }

    private void handleNegativeClick() {
        ((CancelHandler) this.getActivity()).handleCancel();
    }

    private void handlePositiveClick() {
        Group toDelete = (Group) this.getArguments().getParcelable("delete");
        ((DeleteGroupHandler) this.getActivity()).handleDelete(toDelete);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        LayoutInflater inflater = this.getActivity().getLayoutInflater();

        AlertDialog dialog = new AlertDialog.Builder(this.getActivity()).setTitle(R.string.dialog_delete_group_title).setIcon(R.drawable.ic_dialog_discard).setView(inflater.inflate(R.layout.dialog_fragment_delete_group, null)).setPositiveButton(R.string.dialog_ok, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GroupDeleteDialogFragment.this.handlePositiveClick();
            }
        }).setNegativeButton(R.string.dialog_cancel, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                GroupDeleteDialogFragment.this.handleNegativeClick();
            }
        }).create();
        return dialog;
    }
}
