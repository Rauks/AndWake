package net.kirauks.andwake.fragments;

import java.util.ArrayList;
import java.util.List;

import net.kirauks.andwake.R;
import net.kirauks.andwake.fragments.handlers.FavoriteGroupHandler;
import net.kirauks.andwake.fragments.handlers.RequestDeleteGroupHandler;
import net.kirauks.andwake.fragments.handlers.RequestUpdateGroupHandler;
import net.kirauks.andwake.packets.WolPacketSendHelper;
import net.kirauks.andwake.targets.Computer;
import net.kirauks.andwake.targets.Group;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class GroupListFragment extends ListFragment {
    public class GroupsAdapter extends ArrayAdapter<Group> {
        public class GroupComputersAdapter extends ArrayAdapter<Computer> {
            public GroupComputersAdapter(Context context, List<Computer> objects) {
                super(context, R.layout.list_element_group_computer, objects);
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final Computer item = this.getItem(position);

                if(convertView == null){
	                LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
	                convertView = inflater.inflate(R.layout.list_element_group_computer, parent, false);
                }
                
                TextView name = (TextView) convertView.findViewById(R.id.list_element_group_computer_name);
                name.setText(item.getName());

                return convertView;
            }
        }
        
        private List<Group> favorites;

        public GroupsAdapter(Context context, List<Group> data, List<Group> favorites) {
            super(context, R.layout.list_element_group, data);
            this.favorites = favorites;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Group item = this.getItem(position);

            if(convertView == null){
            	LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
            	convertView = inflater.inflate(R.layout.list_element_group, parent, false);
            }
            
            TextView name = (TextView) convertView.findViewById(R.id.list_element_group_name);
            LinearLayout computers = (LinearLayout) convertView.findViewById(R.id.list_element_group_computers);

            name.setText(item.getName());
            GroupComputersAdapter adapter = new GroupComputersAdapter(this.getContext(), item.getChildren());

            computers.removeAllViews();
            for (int i = 0; i < adapter.getCount(); i++) {
                View v = adapter.getView(i, null, null);
                computers.addView(v);
            }

            Button wake = (Button) convertView.findViewById(R.id.list_element_group_wake);
            wake.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new WolPacketSendHelper(GroupListFragment.this.getActivity()).doSendWakePacket(item.getChildren());
                }
            });
            
            CheckBox favorite = (CheckBox) convertView.findViewById(R.id.list_element_group_favorite);
            favorite.setOnCheckedChangeListener(null);
            favorite.setChecked(this.favorites.contains(item));
            favorite.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ((FavoriteGroupHandler) GroupListFragment.this.getActivity()).handleFavoriteGroup(item, isChecked);
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((RequestUpdateGroupHandler) GroupListFragment.this.getActivity()).handleRequestUpdate(item);
                }
            });

            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ((RequestDeleteGroupHandler) GroupListFragment.this.getActivity()).handleRequestDelete(item);
                    return true;
                }
            });

            return convertView;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.createList();
    }

    private List<Group> values;
    private List<Group> favorites;
    private GroupsAdapter adapter;
    
    private void createList(){
        this.values = new ArrayList<Group>();
        this.favorites = new ArrayList<Group>();
        this.adapter = new GroupsAdapter(this.getActivity(), this.values, this.favorites);
        this.setListAdapter(this.adapter);
        this.updateList();
    }
    
    public void updateList() {
        DataSourceHelper helper = new DataSourceHelper(this.getActivity());
        if(this.values == null || this.values == null || this.adapter == null){
        	this.createList();
        }
        this.values.clear();
        this.values.addAll(helper.getGroupDataSource().getAllGroups());
        this.favorites.clear();
        this.favorites.addAll(helper.getGroupDataSource().getAllFavoritesGroups());
        this.adapter.notifyDataSetChanged();
    }
}