package net.kirauks.andwake.fragments;

import java.util.List;

import net.kirauks.andwake.R;
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
import android.widget.LinearLayout;
import android.widget.TextView;

public class FavoriteListFragment extends ListFragment {
    public class FavoritesAdapter extends BaseAdapter {
        public class GroupComputersAdapter extends ArrayAdapter<Computer> {
            public GroupComputersAdapter(Context context, List<Computer> objects) {
                super(context, R.layout.list_element_group_computer, objects);
            }
    
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                final Computer item = this.getItem(position);
    
                LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
                View rootView = inflater.inflate(R.layout.list_element_group_computer, parent, false);
    
                TextView name = (TextView) rootView.findViewById(R.id.list_element_group_computer_name);
                name.setText(item.getName());
    
                return rootView;
            }
        }
        
        private Context context;
        
        private List<Group> groups;
        private List<Computer> computers;
        
        public FavoritesAdapter(Context context, List<Group> groups, List<Computer> computers) {
            super();
            this.context = context;
            this.groups = groups;
            this.computers = computers;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Object rawItem = this.getItem(position);

            LayoutInflater inflater = ((Activity) this.context).getLayoutInflater();

            View rootView = convertView;

            if (rawItem instanceof Group) {
                final Group item = (Group)rawItem;
                rootView = inflater.inflate(R.layout.list_element_group, parent, false);
            
                TextView name = (TextView) rootView.findViewById(R.id.list_element_group_name);
                LinearLayout computers = (LinearLayout) rootView.findViewById(R.id.list_element_group_computers);

                name.setText(item.getName());
                GroupComputersAdapter adapter = new GroupComputersAdapter(this.context, item.getChildren());

                for (int i = 0; i < adapter.getCount(); i++) {
                    View v = adapter.getView(i, null, null);
                    computers.addView(v);
                }

                Button wake = (Button) rootView.findViewById(R.id.list_element_group_wake);
                wake.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new WolPacketSendHelper(FavoriteListFragment.this.getActivity()).doSendWakePacket(item.getChildren());
                    }
                });

                rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((RequestUpdateGroupHandler) FavoriteListFragment.this.getActivity()).handleRequestUpdate(item);
                    }
                });

                rootView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ((RequestDeleteGroupHandler) FavoriteListFragment.this.getActivity()).handleRequestDelete(item);
                        return true;
                    }
                });
            }
            else if (rawItem instanceof Computer) {
                final Computer item = (Computer)rawItem;
                rootView = inflater.inflate(R.layout.list_element_computer, parent, false);
                
                TextView name = (TextView) rootView.findViewById(R.id.list_element_computer_name);
                TextView mac = (TextView) rootView.findViewById(R.id.list_element_computer_mac);
                TextView address = (TextView) rootView.findViewById(R.id.list_element_computer_address);
                TextView port = (TextView) rootView.findViewById(R.id.list_element_computer_port);

                name.setText(item.getName());
                mac.setText(item.getMac());
                address.setText(item.getAddress());
                port.setText(String.valueOf(item.getPort()));
                
                Button wake = (Button) rootView.findViewById(R.id.list_element_computers_wake);
                wake.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new WolPacketSendHelper(FavoriteListFragment.this.getActivity()).doSendWakePacket(item);
                    }
                });

                rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ((RequestUpdateComputerHandler) FavoriteListFragment.this.getActivity()).handleRequestUpdate(item);
                    }
                });

                rootView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        ((RequestDeleteComputerHandler) FavoriteListFragment.this.getActivity()).handleRequestDelete(item);
                        return true;
                    }
                });
            }
            
            return rootView;
        }

        @Override
        public int getCount() {
            return this.groups.size() + this.computers.size();
        }

        @Override
        public Object getItem(int position) {
            if(position >= this.groups.size()){
                return this.computers.get(position - this.groups.size());
            }
            return this.groups.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.updateList();
    }

    public void updateList() {
        List<Computer> favoritesComputers = new DataSourceHelper(this.getActivity()).getComputerDataSource().getAllFavoritesComputers();
        List<Group> favoritesGroups = new DataSourceHelper(this.getActivity()).getGroupDataSource().getAllFavoritesGroups();

        FavoritesAdapter adapter = new FavoritesAdapter(this.getActivity(), favoritesGroups, favoritesComputers);
        this.setListAdapter(adapter);
    }
}
