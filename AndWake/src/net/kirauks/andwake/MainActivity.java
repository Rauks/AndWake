package net.kirauks.andwake;

import java.util.Locale;

import net.kirauks.andwake.targets.db.ComputerDataSource;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

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
        
        /*
        new AsyncTask<Void, Void, Void>(){
        	private Packet wol = new WolPacket("kirauks.net", "6C:62:6D:43:D8:BB"); 
        	
        	@Override
			protected Void doInBackground(Void... params) {
				
				try {
					//new Emitter(this.wol).send();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				super.onPostExecute(result);
				Toast.makeText(MainActivity.this.getApplicationContext(), this.wol.toString(), Toast.LENGTH_LONG).show();
			}
        	
        }.execute(null, null, null);
        */
    }

    @Override
	protected void onPause() {
        this.computerDataSource.close();
		super.onPause();
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
                new AddComputerDialogFragment().show(this.getFragmentManager(), "add_computer_dialog");
                return true;
            case R.id.menu_add_group:
                new AddGroupDialogFragment().show(this.getFragmentManager(), "add_group_dialog");
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
                    return getString(R.string.favorites_title).toUpperCase(l);
                case PAGE_GROUPS:
                    return getString(R.string.groups_title).toUpperCase(l);
                case PAGE_COMPUTERS:
                    return getString(R.string.computers_title).toUpperCase(l);
            }
            return null;
        }
    }

    private String getFragmentTag(int pos){
        return "android:switcher:" + R.id.pager + ":" + pos;
    }
    
	public void doAddComputer(String name, String mac, String address, int port) {
		this.computerDataSource.createComputer(name, mac, address, port);
		this.mViewPager.setCurrentItem(SectionsPagerAdapter.PAGE_COMPUTERS);
		ComputersFragment f = (ComputersFragment)this.getSupportFragmentManager().findFragmentByTag(this.getFragmentTag(SectionsPagerAdapter.PAGE_COMPUTERS));
		f.updateList();
	}

	public void doAddGroup() {
		// TODO Auto-generated method stub
		
	}
}
