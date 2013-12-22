package net.kirauks.andwake;

import java.io.IOException;
import java.util.Locale;

import net.kirauks.andwake.packets.Emitter;
import net.kirauks.andwake.packets.Packet;
import net.kirauks.andwake.targets.Computer;
import net.kirauks.andwake.targets.db.ComputerDataSource;
import android.app.ActionBar;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //Datasources init
        this.computerDataSource = new ComputerDataSource(this);
        this.computerDataSource.open();
        
        setContentView(R.layout.activity_main);

        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

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
        this.computerDataSource.close();
	}
	@Override
	protected void onResume() {
        this.computerDataSource.open();
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
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
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
        new ComputerDialogFragment().show(this.getFragmentManager(), "add_computer_dialog");
    }
	public void doAddComputer(String name, String mac, String address, int port) {
		this.computerDataSource.createComputer(name, mac, address, port);
		this.mViewPager.setCurrentItem(SectionsPagerAdapter.PAGE_COMPUTERS);
		ComputersFragment f = (ComputersFragment)this.getSupportFragmentManager().findFragmentByTag(this.getFragmentTag(SectionsPagerAdapter.PAGE_COMPUTERS));
		f.updateList();
	}
	public void showEditComputer(Computer item) {
		ComputerDialogFragment dialog = new ComputerDialogFragment();
		dialog.setEdit(item);
		dialog.show(this.getFragmentManager(), "edit_computer_dialog");
		
	}
	public void doEditComputer(long id, String name, String mac, String address, int port) {
		this.computerDataSource.updateComputer(id, name, mac, address, port);
		this.mViewPager.setCurrentItem(SectionsPagerAdapter.PAGE_COMPUTERS);
		ComputersFragment f = (ComputersFragment)this.getSupportFragmentManager().findFragmentByTag(this.getFragmentTag(SectionsPagerAdapter.PAGE_COMPUTERS));
		f.updateList();
	}
	public void showDeleteComputer(Computer item) {
		Toast.makeText(this, "Long click", Toast.LENGTH_SHORT).show();	
	}
    
    public void showAddGroup(){
        new AddGroupDialogFragment().show(this.getFragmentManager(), "add_group_dialog");
    }
	public void doAddGroup() {
		// TODO Auto-generated method stub
		
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
