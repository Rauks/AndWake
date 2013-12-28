package net.kirauks.andwake.fragments;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.kirauks.andwake.R;
import net.kirauks.andwake.fragments.handlers.FavoriteComputerHandler;
import net.kirauks.andwake.fragments.handlers.FavoriteGroupHandler;
import net.kirauks.andwake.fragments.handlers.RequestDeleteComputerHandler;
import net.kirauks.andwake.fragments.handlers.RequestDeleteGroupHandler;
import net.kirauks.andwake.fragments.handlers.RequestUpdateComputerHandler;
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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class FavoriteListFragment extends ListFragment {
    public class FavoritesAdapter extends BaseAdapter {
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
        
        private final int HEAD_COUNT = 3;
        private final int HEAD_TYPE_GROUP = 0;
        private final int HEAD_TYPE_COMPUTER = 1;
        private final int HEAD_TYPE_HEADER = 2;
        
        private Context context;

        private List<Object> datas;
        private List<Integer> datasType;
        private List<Group> groups;
        private List<Computer> computers;
        
        public FavoritesAdapter(Context context, List<Group> groups, List<Computer> computers) {
            super();
            this.context = context;
            this.groups = groups;
            this.computers = computers;
            this.datas = new ArrayList<Object>();
            this.datasType = new ArrayList<Integer>();
            this.updateDatas();
        }
        
        private void updateDatas(){
        	this.datas.clear();
        	this.datasType.clear();
            if(!this.groups.isEmpty()){
                this.datas.add(this.context.getResources().getString(R.string.list_header_group_name));
                this.datasType.add(this.HEAD_TYPE_HEADER);
                this.datas.addAll(this.groups);
                this.datasType.addAll(Collections.nCopies(this.groups.size(), this.HEAD_TYPE_GROUP));
            }
            if(!this.computers.isEmpty()){
                this.datas.add(this.context.getResources().getString(R.string.list_header_computer_name));
                this.datasType.add(this.HEAD_TYPE_HEADER);
                this.datas.addAll(this.computers);
                this.datasType.addAll(Collections.nCopies(this.computers.size(), this.HEAD_TYPE_COMPUTER));
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Object rawItem = this.getItem(position);
            int itemType = this.getItemViewType(position);

            if(convertView == null){
            	LayoutInflater inflater = ((Activity) this.context).getLayoutInflater();

                if (itemType == this.HEAD_TYPE_GROUP) {
                	convertView = inflater.inflate(R.layout.list_element_group, parent, false);
                }
                else if (itemType == this.HEAD_TYPE_COMPUTER) {
                	convertView = inflater.inflate(R.layout.list_element_computer, parent, false);
                }
                else if (itemType == this.HEAD_TYPE_HEADER){
                	convertView = inflater.inflate(R.layout.list_header_favorites, parent, false);
                }
            }
            
            if (itemType == this.HEAD_TYPE_GROUP) {
                final Group item = (Group)rawItem;
            
                TextView name = (TextView) convertView.findViewById(R.id.list_element_group_name);
                LinearLayout computers = (LinearLayout) convertView.findViewById(R.id.list_element_group_computers);

                name.setText(item.getName());
                GroupComputersAdapter adapter = new GroupComputersAdapter(this.context, item.getChildren());

                computers.removeAllViews();
                for (int i = 0; i < adapter.getCount(); i++) {
                    View v = adapter.getView(i, null, null);
                    computers.addView(v);
                }

                Button wake = (Button) convertView.findViewById(R.id.list_element_group_wake);
                wake.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new WolPacketSendHelper(FavoriteListFragment.this.getActivity()).doSendWakePacket(item.getChildren());
                    }
                });
                
                CheckBox favorite = (CheckBox) convertView.findViewById(R.id.list_element_group_favorite);
                favorite.setOnCheckedChangeListener(null);
                favorite.setChecked(true);
                favorite.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        ((FavoriteGroupHandler) FavoriteListFragment.this.getActivity()).handleFavoriteGroup(item, isChecked);
                    }
                });

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((RequestUpdateGroupHandler) FavoriteListFragment.this.getActivity()).handleRequestUpdate(item);
                    }
                });

                convertView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ((RequestDeleteGroupHandler) FavoriteListFragment.this.getActivity()).handleRequestDelete(item);
                        return true;
                    }
                });
            }
            else if (itemType == this.HEAD_TYPE_COMPUTER) {
                final Computer item = (Computer)rawItem;
                
                TextView name = (TextView) convertView.findViewById(R.id.list_element_computer_name);
                TextView mac = (TextView) convertView.findViewById(R.id.list_element_computer_mac);
                TextView address = (TextView) convertView.findViewById(R.id.list_element_computer_address);
                TextView port = (TextView) convertView.findViewById(R.id.list_element_computer_port);

                name.setText(item.getName());
                mac.setText(item.getMac());
                address.setText(item.getAddress());
                port.setText(String.valueOf(item.getPort()));
                
                Button wake = (Button) convertView.findViewById(R.id.list_element_computers_wake);
                wake.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new WolPacketSendHelper(FavoriteListFragment.this.getActivity()).doSendWakePacket(item);
                    }
                });
                
                CheckBox favorite = (CheckBox) convertView.findViewById(R.id.list_element_computers_favorite);
                favorite.setOnCheckedChangeListener(null);
                favorite.setChecked(true);
                favorite.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        ((FavoriteComputerHandler) FavoriteListFragment.this.getActivity()).handleFavoriteComputer(item, isChecked);
                    }
                });

                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((RequestUpdateComputerHandler) FavoriteListFragment.this.getActivity()).handleRequestUpdate(item);
                    }
                });

                convertView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ((RequestDeleteComputerHandler) FavoriteListFragment.this.getActivity()).handleRequestDelete(item);
                        return true;
                    }
                });
            }
            else if (itemType == this.HEAD_TYPE_HEADER){
                TextView header = (TextView) convertView.findViewById(R.id.list_header_favorites_name);
                header.setText((String)rawItem);
            }
            
            return convertView;
        }

		@Override
        public int getCount() {
            return this.datas.size();
        }

        @Override
        public Object getItem(int position) {
            return this.datas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemViewType(int position) {
            return this.datasType.get(position);
        }

        @Override
        public int getViewTypeCount() {
            return this.HEAD_COUNT;
        }

        @Override
		public void notifyDataSetChanged() {
        	this.updateDatas();
			super.notifyDataSetChanged();
		}
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.createList();
    }

    private List<Computer> favoritesComputers;
    private List<Group> favoritesGroups;
    private FavoritesAdapter adapter;
    
    private void createList(){
        this.favoritesComputers = new ArrayList<Computer>();
        this.favoritesGroups = new ArrayList<Group>();
        this.adapter = new FavoritesAdapter(this.getActivity(), this.favoritesGroups, this.favoritesComputers);
        this.setListAdapter(this.adapter);
        this.updateList();
    }
    
    public void updateList() {
        DataSourceHelper helper = new DataSourceHelper(this.getActivity());
        if(this.favoritesComputers == null || this.favoritesGroups == null || this.adapter == null){
        	this.createList();
        }
        this.favoritesComputers.clear();
        this.favoritesComputers.addAll(helper.getComputerDataSource().getAllFavoritesComputers());
        this.favoritesGroups.clear();
        this.favoritesGroups.addAll(helper.getGroupDataSource().getAllFavoritesGroups());
        this.adapter.notifyDataSetChanged();
    }
}
