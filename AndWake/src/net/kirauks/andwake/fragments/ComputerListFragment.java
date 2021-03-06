package net.kirauks.andwake.fragments;

import java.util.ArrayList;
import java.util.List;

import net.kirauks.andwake.R;
import net.kirauks.andwake.fragments.handlers.FavoriteComputerHandler;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class ComputerListFragment extends ListFragment {
    public class ComputersAdapter extends ArrayAdapter<Computer> {
        private List<Computer> favorites;
        public ComputersAdapter(Context context, List<Computer> data, List<Computer> favorites) {
            super(context, R.layout.list_element_computer, data);
            this.favorites = favorites;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Computer item = this.getItem(position);

            if(convertView == null){
                LayoutInflater inflater = ((Activity) this.getContext()).getLayoutInflater();
                convertView = inflater.inflate(R.layout.list_element_computer, parent, false);
            }

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
                    new WolPacketSendHelper(ComputerListFragment.this.getActivity()).doSendWakePacket(item);
                }
            });
            
            CheckBox favorite = (CheckBox) convertView.findViewById(R.id.list_element_computers_favorite);
            favorite.setOnCheckedChangeListener(null);
            favorite.setChecked(this.favorites.contains(item));
            favorite.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ((FavoriteComputerHandler) ComputerListFragment.this.getActivity()).handleFavoriteComputer(item, isChecked);
                }
            });

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((RequestUpdateComputerHandler) ComputerListFragment.this.getActivity()).handleRequestUpdate(item);
                }
            });

            convertView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ((RequestDeleteComputerHandler) ComputerListFragment.this.getActivity()).handleRequestDelete(item);
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

    private List<Computer> values;
    private List<Computer> favorites;
    private ComputersAdapter adapter;
    
    private void createList(){
        this.values = new ArrayList<Computer>();
        this.favorites = new ArrayList<Computer>();
        this.adapter = new ComputersAdapter(this.getActivity(), this.values, this.favorites);
        this.setListAdapter(this.adapter);
        this.updateList();
    }
    
    public void updateList() {
        DataSourceHelper helper = new DataSourceHelper(this.getActivity());
        if(this.values == null || this.values == null || this.adapter == null){
        	this.createList();
        }
        this.values.clear();
        this.values.addAll(helper.getComputerDataSource().getAllComputers());
        this.favorites.clear();
        this.favorites.addAll(helper.getComputerDataSource().getAllFavoritesComputers());
        this.adapter.notifyDataSetChanged();
    }
}
