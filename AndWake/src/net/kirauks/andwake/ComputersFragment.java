package net.kirauks.andwake;

import java.io.IOException;
import java.util.List;

import net.kirauks.andwake.packets.Emitter;
import net.kirauks.andwake.packets.Packet;
import net.kirauks.andwake.packets.WolPacket;
import net.kirauks.andwake.targets.Computer;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
			Computer item = this.getItem(position);
			
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
					new AsyncTask<Void, Void, Void>(){
						boolean sendError = false;
			        	
			        	@Override
						protected Void doInBackground(Void... params) {
							try {
								new Emitter(wakePacket).send();
							} catch (IOException e) {
								this.sendError = true;
							}
							return null;
						}

						@Override
						protected void onPostExecute(Void result) {
							super.onPostExecute(result);
							if(sendError){
								Toast.makeText(ComputersFragment.this.getActivity(), R.string.toast_wake_error, Toast.LENGTH_SHORT).show();
							}
							else{
								Toast.makeText(ComputersFragment.this.getActivity(), R.string.toast_wake_done, Toast.LENGTH_SHORT).show();
							}
						}
			        }.execute(null, null, null);

					Toast.makeText(ComputersFragment.this.getActivity(), R.string.toast_wake_init, Toast.LENGTH_SHORT).show();
				}
			});
			return rootView;
		}
	}
}
