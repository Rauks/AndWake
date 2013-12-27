package net.kirauks.andwake.fragments;

import java.util.List;

import net.kirauks.andwake.R;
import net.kirauks.andwake.fragments.handlers.RequestDeleteComputerHandler;
import net.kirauks.andwake.fragments.handlers.RequestUpdateComputerHandler;
import net.kirauks.andwake.packets.WolPacketSendHelper;
import net.kirauks.andwake.targets.Computer;
import net.kirauks.andwake.targets.db.DataSourceHelper;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

public class ComputerListFragment extends ListFragment{
	public class ComputersAdapter extends ArrayAdapter<Computer> {
		public ComputersAdapter(Context context, List<Computer> data) {
			super(context, R.layout.list_element_computer, data);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Computer item = this.getItem(position);

			LayoutInflater inflater = ((Activity) this.getContext())
					.getLayoutInflater();
			View rootView = inflater.inflate(R.layout.list_element_computer,
					parent, false);

			TextView name = (TextView) rootView
					.findViewById(R.id.list_element_computer_name);
			TextView mac = (TextView) rootView
					.findViewById(R.id.list_element_computer_mac);
			TextView address = (TextView) rootView
					.findViewById(R.id.list_element_computer_address);
			TextView port = (TextView) rootView
					.findViewById(R.id.list_element_computer_port);

			name.setText(item.getName());
			mac.setText(item.getMac());
			address.setText(item.getAddress());
			port.setText(String.valueOf(item.getPort()));

			Button wake = (Button) rootView
					.findViewById(R.id.list_element_computers_wake);
			wake.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					new WolPacketSendHelper(ComputerListFragment.this
							.getActivity()).doSendWakePacket(item);
				}
			});

			rootView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					((RequestUpdateComputerHandler)ComputerListFragment.this.getActivity())
							.handleRequestUpdate(item);
				}
			});

			rootView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					((RequestDeleteComputerHandler)ComputerListFragment.this.getActivity())
							.handleRequestDelete(item);
					return true;
				}
			});

			return rootView;
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.updateList();
	}

	public void updateList() {
		List<Computer> values = new DataSourceHelper(this.getActivity()).getComputerDataSource().getAllComputers();
		ComputersAdapter adapter = new ComputersAdapter(this.getActivity(), values);
		this.setListAdapter(adapter);
	}
}
