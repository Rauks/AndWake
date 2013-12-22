package net.kirauks.andwake;

import java.util.List;

import net.kirauks.andwake.targets.Computer;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ArrayAdapter;

public class ComputersFragment extends ListFragment{
	public ComputersFragment(){}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.updateList();
	}
	
	public void updateList() {
		List<Computer> values = ((MainActivity)this.getActivity()).computerDataSource.getAllComputers();
		ArrayAdapter<Computer> adapter = new ArrayAdapter<Computer>(this.getActivity(),
		    android.R.layout.simple_list_item_1, values);
		this.setListAdapter(adapter);
	}
}
