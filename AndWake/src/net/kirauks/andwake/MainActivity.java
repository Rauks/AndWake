package net.kirauks.andwake;

import java.util.List;
import java.util.Locale;

import net.kirauks.andwake.fragments.ComputerDeleteDialogFragment;
import net.kirauks.andwake.fragments.ComputerEditDialogFragment;
import net.kirauks.andwake.fragments.ComputersFragment;
import net.kirauks.andwake.fragments.FavoritesFragment;
import net.kirauks.andwake.fragments.GroupDeleteDialogFragment;
import net.kirauks.andwake.fragments.GroupEditDialogFragment;
import net.kirauks.andwake.fragments.GroupsFragment;
import net.kirauks.andwake.targets.Computer;
import net.kirauks.andwake.targets.Group;
import net.kirauks.andwake.targets.db.DataSourceHelper;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity implements
		ActionBar.TabListener {
	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		private static final int COUNT_PAGES = 3;
		private static final int PAGE_FAVORITES = 0;
		private static final int PAGE_COMPUTERS = 1;
		private static final int PAGE_GROUPS = 2;

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return SectionsPagerAdapter.COUNT_PAGES;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
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
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case PAGE_FAVORITES:
				return MainActivity.this.getString(
						R.string.fragment_favorites_title).toUpperCase(l);
			case PAGE_GROUPS:
				return MainActivity.this.getString(
						R.string.fragment_groups_title).toUpperCase(l);
			case PAGE_COMPUTERS:
				return MainActivity.this.getString(
						R.string.fragment_computers_title).toUpperCase(l);
			}
			return null;
		}
	}

	private SectionsPagerAdapter mSectionsPagerAdapter;

	private ViewPager mViewPager;

	private DataSourceHelper dataSourceHelper;

	public void doAddComputer(String name, String mac, String address, int port) {
		this.dataSourceHelper.getComputerDataSource().createComputer(name, mac,
				address, port);
		this.goAndRefreshComputersFragmentList();
	}

	public void doAddGroup(String name, List<Computer> computers) {
		this.dataSourceHelper.getGroupDataSource().createGroup(name, computers);
		this.goAndRefreshGroupsFragmentList();
	}

	public void doDeleteComputer(Computer delete) {
		this.dataSourceHelper.getComputerDataSource().deleteComputer(delete);
		this.goAndRefreshComputersFragmentList();
	}

	public void doDeleteGroup(Group delete) {
		this.dataSourceHelper.getGroupDataSource().deleteGroup(delete);
		this.goAndRefreshGroupsFragmentList();
	}

	public void doEditComputer(Computer edit) {
		this.dataSourceHelper.getComputerDataSource().updateComputer(edit);
		this.goAndRefreshComputersFragmentList();
	}

	public void doEditGroup(Group edit) {
		this.dataSourceHelper.getGroupDataSource().updateGroup(edit);
		this.goAndRefreshGroupsFragmentList();
	}

	public DataSourceHelper getDataSourceHelper() {
		return this.dataSourceHelper;
	}

	private String getFragmentTag(int pos) {
		return "android:switcher:" + R.id.pager + ":" + pos;
	}

	public void goAndRefreshComputersFragmentList() {
		this.mViewPager.setCurrentItem(SectionsPagerAdapter.PAGE_COMPUTERS);
		ComputersFragment f = (ComputersFragment) this
				.getSupportFragmentManager()
				.findFragmentByTag(
						this.getFragmentTag(SectionsPagerAdapter.PAGE_COMPUTERS));
		f.updateList();
	}

	public void goAndRefreshGroupsFragmentList() {
		this.mViewPager.setCurrentItem(SectionsPagerAdapter.PAGE_GROUPS);
		GroupsFragment f = (GroupsFragment) this.getSupportFragmentManager()
				.findFragmentByTag(
						this.getFragmentTag(SectionsPagerAdapter.PAGE_GROUPS));
		f.updateList();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.dataSourceHelper = new DataSourceHelper(this);
		this.dataSourceHelper.open();

		this.setContentView(R.layout.activity_main);

		final ActionBar actionBar = this.getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(false);

		this.mSectionsPagerAdapter = new SectionsPagerAdapter(
				this.getSupportFragmentManager());

		this.mViewPager = (ViewPager) this.findViewById(R.id.pager);
		this.mViewPager.setAdapter(this.mSectionsPagerAdapter);
		this.mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getMenuInflater().inflate(R.menu.main, menu);
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
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		this.mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	}

	public void showAddComputer() {
		ComputerEditDialogFragment.newInstance().show(
				this.getSupportFragmentManager(), "add_computer_dialog");
	}

	public void showAddGroup() {
		GroupEditDialogFragment.newInstance().show(
				this.getSupportFragmentManager(), "add_group_dialog");
	}

	public void showDeleteComputer(Computer item) {
		ComputerDeleteDialogFragment.newInstance(item).show(
				this.getSupportFragmentManager(), "delete_computer_dialog");
	}

	public void showDeleteGroup(Group item) {
		GroupDeleteDialogFragment.newInstance(item).show(
				this.getSupportFragmentManager(), "delete_group_dialog");
	}

	public void showEditComputer(Computer item) {
		ComputerEditDialogFragment.newInstance(item).show(
				this.getSupportFragmentManager(), "edit_computer_dialog");
	}

	public void showEditGroup(Group item) {
		GroupEditDialogFragment.newInstance(item).show(
				this.getSupportFragmentManager(), "edit_group_dialog");
	}
}
