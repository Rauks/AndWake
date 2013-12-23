package net.kirauks.andwake;

import java.util.List;

import net.kirauks.andwake.targets.Group;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GroupsFragment extends ListFragment{
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.updateList();
	}
	
	public void updateList() {
		List<Group> values = ((MainActivity)this.getActivity()).groupDataSource.getAllGroups();
		GroupAdapter adapter = new GroupAdapter(this.getActivity(), values);
		this.setListAdapter(adapter);
	}
	
	public class GroupAdapter extends ArrayAdapter<Group>{
		public GroupAdapter(Context context, List<Group> data) {
	        super(context, R.layout.list_element_computer, data);
	    }

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Group item = this.getItem(position);
			
			LayoutInflater inflater = ((Activity)this.getContext()).getLayoutInflater();
			View rootView = inflater.inflate(R.layout.list_element_computer, parent, false);
			
			TextView name = (TextView) rootView.findViewById(R.id.list_element_computer_name);
			
			name.setText(item.getName());
			
			return rootView;
		}
	}
}