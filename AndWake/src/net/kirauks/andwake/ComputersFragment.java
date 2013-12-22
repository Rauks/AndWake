package net.kirauks.andwake;

import java.util.List;

import net.kirauks.andwake.packets.Packet;
import net.kirauks.andwake.packets.WolPacket;
import net.kirauks.andwake.targets.Computer;
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

public class ComputersFragment extends ListFragment{
	public ComputersFragment(){}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.updateList();
	}
	
	public void updateList() {
		List<Computer> values = ((MainActivity)this.getActivity()).computerDataSource.getAllComputers();
		ComputersAdapter adapter = new ComputersAdapter(this.getActivity(), values);
		this.setListAdapter(adapter);
	}
	
	public class ComputersAdapter extends ArrayAdapter<Computer>{
		public ComputersAdapter(Context context, List<Computer> data) {
	        super(context, R.layout.list_element_computer, data);
	    }

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Computer item = this.getItem(position);
			
			LayoutInflater inflater = ((Activity)this.getContext()).getLayoutInflater();
			View rootView = inflater.inflate(R.layout.list_element_computer, parent, false);
			
			TextView name = (TextView) rootView.findViewById(R.id.list_element_computer_name);
			TextView mac = (TextView) rootView.findViewById(R.id.list_element_computer_mac);
			TextView address = (TextView) rootView.findViewById(R.id.list_element_computer_address);
			TextView port = (TextView) rootView.findViewById(R.id.list_element_computer_port);
			
			name.setText(item.getName());
			mac.setText(item.getMac());
			address.setText(item.getAddress());
			port.setText(String.valueOf(item.getPort()));
			
			Button wake = (Button) rootView.findViewById(R.id.list_element_computers_wake);
			final Packet wakePacket = new WolPacket(item.getAddress(), item.getMac(), item.getPort()); 
			wake.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					((MainActivity)ComputersFragment.this.getActivity()).doSendPacket(wakePacket);
				}
			});
			
			rootView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					((MainActivity)ComputersFragment.this.getActivity()).showEditComputer(item);		
				}
			});
			
			rootView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					((MainActivity)ComputersFragment.this.getActivity()).showDeleteComputer(item);
					return true;
				}
			});
			
			return rootView;
		}
	}
}
