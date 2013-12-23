package net.kirauks.andwake;

import java.io.IOException;
import java.util.Locale;

import net.kirauks.andwake.packets.Emitter;
import net.kirauks.andwake.packets.Packet;
import net.kirauks.andwake.targets.Computer;
import net.kirauks.andwake.targets.Group;
import net.kirauks.andwake.targets.db.ComputerDataSource;
import net.kirauks.andwake.targets.db.GroupDataSource;
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
    
    ComputerDataSource computerDataSource;
    GroupDataSource groupDataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Datasources init
        this.computerDataSource = new ComputerDataSource(this);
        this.computerDataSource.open();
        this.groupDataSource = new GroupDataSource(this, this.computerDataSource);
        this.groupDataSource.open();
        
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

    @Override
	protected void onPause() {
		super.onPause();
		this.groupDataSource.close();
        this.computerDataSource.close();
	}
	@Override
	protected void onResume() {
        this.computerDataSource.open();
        this.groupDataSource.open();
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
    
    public void showAddComputer(){
        new ComputerEditDialogFragment().show(this.getSupportFragmentManager(), "add_computer_dialog");
    }
    public void goAndRefreshComputersFragmentList(){
		this.mViewPager.setCurrentItem(SectionsPagerAdapter.PAGE_COMPUTERS);
		ComputersFragment f = (ComputersFragment)this.getSupportFragmentManager().findFragmentByTag(this.getFragmentTag(SectionsPagerAdapter.PAGE_COMPUTERS));
		f.updateList();
    }
	public void doAddComputer(String name, String mac, String address, int port) {
		this.computerDataSource.createComputer(name, mac, address, port);
		this.goAndRefreshComputersFragmentList();
	}
	public void showEditComputer(Computer item) {
		ComputerEditDialogFragment dialog = new ComputerEditDialogFragment();
		dialog.setEdit(item);
		dialog.show(this.getSupportFragmentManager(), "edit_computer_dialog");
		
	}
	public void doEditComputer(Computer edit){
		this.computerDataSource.updateComputer(edit);
		this.goAndRefreshComputersFragmentList();
	}
	public void showDeleteComputer(Computer item) {
		ComputerDeleteDialogFragment dialog = new ComputerDeleteDialogFragment();
		dialog.setDelete(item);
		dialog.show(this.getSupportFragmentManager(), "delete_computer_dialog");
	}
	public void doDeleteComputer(Computer delete){
		this.computerDataSource.deleteComputer(delete);
		this.goAndRefreshComputersFragmentList();
	}
    
    public void showAddGroup(){
        new GroupEditDialogFragment().show(this.getSupportFragmentManager(), "add_group_dialog");
    }
    public void goAndRefreshGroupsFragmentList(){
		this.mViewPager.setCurrentItem(SectionsPagerAdapter.PAGE_GROUPS);
		GroupsFragment f = (GroupsFragment)this.getSupportFragmentManager().findFragmentByTag(this.getFragmentTag(SectionsPagerAdapter.PAGE_GROUPS));
		f.updateList();
    }
	public void doAddGroup(String name) {
		this.groupDataSource.createGroup(name, new long[0]);
		this.goAndRefreshGroupsFragmentList();
	}
	public void showEditGroup(Group item){
		GroupEditDialogFragment dialog = new GroupEditDialogFragment();
		dialog.setEdit(item);
		dialog.show(this.getSupportFragmentManager(), "edit_group_dialog");
	}
	public void doEditGroup(Group edit){
		this.groupDataSource.updateGroup(edit);
		this.goAndRefreshGroupsFragmentList();
	}
	public void showDeleteGroup(Group item) {
		GroupDeleteDialogFragment dialog = new GroupDeleteDialogFragment();
		dialog.setDelete(item);
		dialog.show(this.getSupportFragmentManager(), "delete_group_dialog");
	}
	public void doDeleteGroup(Group delete){
		this.groupDataSource.deleteGroup(delete);
		this.goAndRefreshGroupsFragmentList();
	}
	
	public void doSendPacket(final Packet packet){
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
