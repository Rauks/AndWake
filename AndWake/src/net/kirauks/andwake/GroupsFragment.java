package net.kirauks.andwake;

import java.util.List;

import net.kirauks.andwake.targets.Computer;
import net.kirauks.andwake.targets.Group;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class GroupsFragment extends ListFragment{
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		this.updateList();
	}
	
	public void updateList() {
		List<Group> values = ((MainActivity)this.getActivity()).groupDataSource.getAllGroups();
		GroupsAdapter adapter = new GroupsAdapter(this.getActivity(), values);
		this.setListAdapter(adapter);
	}
	
	public class GroupsAdapter extends ArrayAdapter<Group>{
		public GroupsAdapter(Context context, List<Group> data) {
	        super(context, R.layout.list_element_group, data);
	    }

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			final Group item = this.getItem(position);
			
			LayoutInflater inflater = ((Activity)this.getContext()).getLayoutInflater();
			View rootView = inflater.inflate(R.layout.list_element_group, parent, false);
			
			TextView name = (TextView) rootView.findViewById(R.id.list_element_group_name);
			ListView computers = (ListView) rootView.findViewById(R.id.list_element_group_computers);
			
			name.setText(item.getName());
			computers.setAdapter(new GroupComputersAdapter(this.getContext(), item.getChildren()));
			
			rootView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					((MainActivity)GroupsFragment.this.getActivity()).showEditGroup(item);		
				}
			});
			
			rootView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					((MainActivity)GroupsFragment.this.getActivity()).showDeleteGroup(item);
					return true;
				}
			});
			
			return rootView;
		}
		
		public class GroupComputersAdapter extends ArrayAdapter<Computer>{
			public GroupComputersAdapter(Context context, List<Computer> objects) {
				super(context, R.layout.list_element_group_computer, objects);
			}

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				final Computer item = this.getItem(position);

				LayoutInflater inflater = ((Activity)this.getContext()).getLayoutInflater();
				View rootView = inflater.inflate(R.layout.list_element_group, parent, false);
				
				TextView name = (TextView) rootView.findViewById(R.id.list_element_group_computer_name);
				name.setText(item.getName());
				
				return rootView;
			}
		}
	}
}