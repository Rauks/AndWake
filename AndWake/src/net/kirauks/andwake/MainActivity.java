package net.kirauks.andwake;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import net.kirauks.andwake.fragments.ComputerDeleteDialogFragment;
import net.kirauks.andwake.fragments.ComputerEditDialogFragment;
import net.kirauks.andwake.fragments.ComputersFragment;
import net.kirauks.andwake.fragments.FavoritesFragment;
import net.kirauks.andwake.fragments.GroupDeleteDialogFragment;
import net.kirauks.andwake.fragments.GroupEditDialogFragment;
import net.kirauks.andwake.fragments.GroupsFragment;
import net.kirauks.andwake.packets.Emitter;
import net.kirauks.andwake.packets.Packet;
import net.kirauks.andwake.packets.WolPacket;
import net.kirauks.andwake.targets.Computer;
import net.kirauks.andwake.targets.Group;
import net.kirauks.andwake.targets.db.DataSourceHelper;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    
    private DataSourceHelper dataSourceHelper;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        this.dataSourceHelper = new DataSourceHelper(this);
        this.dataSourceHelper.open();
        
        setContentView(R.layout.activity_main);

        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);

        this.mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        this.mViewPager = (ViewPager) findViewById(R.id.pager);
        this.mViewPager.setAdapter(this.mSectionsPagerAdapter);
        this.mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < this.mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab()
                .setText(this.mSectionsPagerAdapter.getPageTitle(i))
                .setTabListener(this));
        }
    }
    
    public DataSourceHelper getDataSourceHelper() {
		return dataSourceHelper;
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.dataSourceHelper.close();
	}
	@Override
	protected void onResume() {
        this.dataSourceHelper.open();
		super.onResume();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_add_computer:
            	this.showAddComputer();
            	return true;
            case R.id.menu_add_group:
            	this.showAddGroup();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    	this.mViewPager.setCurrentItem(tab.getPosition());
    }
	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {}
	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {}
	
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
    	private static final int COUNT_PAGES = 3;
    	private static final int PAGE_FAVORITES = 0;
    	private static final int PAGE_GROUPS = 1;
    	private static final int PAGE_COMPUTERS = 2;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
        	switch(position){
        		case PAGE_FAVORITES:
        			return new FavoritesFragment();
        		case PAGE_GROUPS:
        			return new GroupsFragment();
        		case PAGE_COMPUTERS:
        			return new ComputersFragment();
    			default:
        			return new FavoritesFragment();
        	}
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return COUNT_PAGES;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case PAGE_FAVORITES:
                    return getString(R.string.fragment_favorites_title).toUpperCase(l);
                case PAGE_GROUPS:
                    return getString(R.string.fragment_groups_title).toUpperCase(l);
                case PAGE_COMPUTERS:
                    return getString(R.string.fragment_computers_title).toUpperCase(l);
            }
            return null;
        }
    }

    private String getFragmentTag(int pos){
        return "android:switcher:" + R.id.pager + ":" + pos;
    }

    public void goAndRefreshComputersFragmentList(){
		this.mViewPager.setCurrentItem(SectionsPagerAdapter.PAGE_COMPUTERS);
		ComputersFragment f = (ComputersFragment)this.getSupportFragmentManager().findFragmentByTag(this.getFragmentTag(SectionsPagerAdapter.PAGE_COMPUTERS));
		f.updateList();
    }
    public void showAddComputer(){
        ComputerEditDialogFragment.newInstance().show(this.getSupportFragmentManager(), "add_computer_dialog");
    }
	public void doAddComputer(String name, String mac, String address, int port) {
		this.dataSourceHelper.getComputerDataSource().createComputer(name, mac, address, port);
		this.goAndRefreshComputersFragmentList();
	}
	public void showEditComputer(Computer item) {
		ComputerEditDialogFragment.newInstance(item).show(this.getSupportFragmentManager(), "edit_computer_dialog");
	}
	public void doEditComputer(Computer edit){
		this.dataSourceHelper.getComputerDataSource().updateComputer(edit);
		this.goAndRefreshComputersFragmentList();
	}
	public void showDeleteComputer(Computer item) {
		ComputerDeleteDialogFragment.newInstance(item).show(this.getSupportFragmentManager(), "delete_computer_dialog");
	}
	public void doDeleteComputer(Computer delete){
		this.dataSourceHelper.getComputerDataSource().deleteComputer(delete);
		this.goAndRefreshComputersFragmentList();
	}
    
    public void goAndRefreshGroupsFragmentList(){
		this.mViewPager.setCurrentItem(SectionsPagerAdapter.PAGE_GROUPS);
		GroupsFragment f = (GroupsFragment)this.getSupportFragmentManager().findFragmentByTag(this.getFragmentTag(SectionsPagerAdapter.PAGE_GROUPS));
		f.updateList();
    }
    public void showAddGroup(){
        GroupEditDialogFragment.newInstance().show(this.getSupportFragmentManager(), "add_group_dialog");
    }
	public void doAddGroup(String name, List<Computer> computers) {
		this.dataSourceHelper.getGroupDataSource().createGroup(name, computers);
		this.goAndRefreshGroupsFragmentList();
	}
	public void showEditGroup(Group item){
		GroupEditDialogFragment.newInstance(item).show(this.getSupportFragmentManager(), "edit_group_dialog");
	}
	public void doEditGroup(Group edit){
		this.dataSourceHelper.getGroupDataSource().updateGroup(edit);
		this.goAndRefreshGroupsFragmentList();
	}
	public void showDeleteGroup(Group item) {
		GroupDeleteDialogFragment.newInstance(item).show(this.getSupportFragmentManager(), "delete_group_dialog");
	}
	public void doDeleteGroup(Group delete){
		this.dataSourceHelper.getGroupDataSource().deleteGroup(delete);
		this.goAndRefreshGroupsFragmentList();
	}
	
	public void doSendWakePacket(Computer computer){
		Packet wakePacket = new WolPacket(computer.getAddress(), computer.getMac(), computer.getPort());
		this.doSendPacket(wakePacket);
	}
	public void doSendWakePacket(List<Computer> computers){
		for(Computer computer : computers){
			this.doSendWakePacket(computer);
		}
	}
	private void doSendPacket(final Packet packet){
		new AsyncTask<Void, Void, Void>(){
			boolean sendError = false;
        	
        	@Override
			protected Void doInBackground(Void... params) {
				try {
					new Emitter(packet).send();
				} catch (IOException e) {
					this.sendError = true;
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				if(sendError){
					Toast.makeText(MainActivity.this, R.string.toast_wake_error, Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(MainActivity.this, R.string.toast_wake_done, Toast.LENGTH_SHORT).show();
				}
			}
        }.execute(null, null, null);

		Toast.makeText(MainActivity.this, R.string.toast_wake_init, Toast.LENGTH_SHORT).show();
	}
}
