package net.kirauks.andwake;

import java.util.Locale;

import net.kirauks.andwake.appwidget.WakeComputerWidget;
import net.kirauks.andwake.appwidget.WakeGroupWidget;
import net.kirauks.andwake.fragments.ComputerDeleteDialogFragment;
import net.kirauks.andwake.fragments.ComputerDialogFragment;
import net.kirauks.andwake.fragments.ComputerListFragment;
import net.kirauks.andwake.fragments.FavoriteListFragment;
import net.kirauks.andwake.fragments.GroupDeleteDialogFragment;
import net.kirauks.andwake.fragments.GroupDialogFragment;
import net.kirauks.andwake.fragments.GroupListFragment;
import net.kirauks.andwake.fragments.handlers.CancelHandler;
import net.kirauks.andwake.fragments.handlers.CreateComputerHandler;
import net.kirauks.andwake.fragments.handlers.CreateGroupHandler;
import net.kirauks.andwake.fragments.handlers.DeleteComputerHandler;
import net.kirauks.andwake.fragments.handlers.DeleteGroupHandler;
import net.kirauks.andwake.fragments.handlers.RequestDeleteComputerHandler;
import net.kirauks.andwake.fragments.handlers.RequestDeleteGroupHandler;
import net.kirauks.andwake.fragments.handlers.RequestUpdateComputerHandler;
import net.kirauks.andwake.fragments.handlers.RequestUpdateGroupHandler;
import net.kirauks.andwake.fragments.handlers.UpdateComputerHandler;
import net.kirauks.andwake.fragments.handlers.UpdateGroupHandler;
import net.kirauks.andwake.targets.Computer;
import net.kirauks.andwake.targets.Group;
import net.kirauks.andwake.targets.db.DataSourceHelper;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity implements CancelHandler,
        CreateGroupHandler, UpdateGroupHandler, DeleteGroupHandler,
        CreateComputerHandler, UpdateComputerHandler, DeleteComputerHandler,
        RequestUpdateGroupHandler, RequestDeleteGroupHandler,
        RequestUpdateComputerHandler, RequestDeleteComputerHandler,
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
                    return new FavoriteListFragment();
                case PAGE_GROUPS:
                    return new GroupListFragment();
                case PAGE_COMPUTERS:
                    return new ComputerListFragment();
                default:
                    return new FavoriteListFragment();
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case PAGE_FAVORITES:
                    return MainActivity.this.getString(R.string.fragment_favorites_title).toUpperCase(l);
                case PAGE_GROUPS:
                    return MainActivity.this.getString(R.string.fragment_groups_title).toUpperCase(l);
                case PAGE_COMPUTERS:
                    return MainActivity.this.getString(R.string.fragment_computers_title).toUpperCase(l);
            }
            return null;
        }
    }

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    private String getFragmentTag(int pos) {
        return "android:switcher:" + R.id.pager + ":" + pos;
    }

    @Override
    public void handleCancel() {
    }

    @Override
    public void handleCreate(Computer computer) {
        new DataSourceHelper(this).getComputerDataSource().createComputer(computer);
        this.showAndRefreshComputerListFragment();
    }

    @Override
    public void handleCreate(Group group) {
        new DataSourceHelper(this).getGroupDataSource().createGroup(group);
        this.showAndRefreshGroupListFragment();
    }

    @Override
    public void handleDelete(Computer computer) {
        new DataSourceHelper(this).getComputerDataSource().deleteComputer(computer);
        this.showAndRefreshComputerListFragment();
    }

    @Override
    public void handleDelete(Group group) {
        new DataSourceHelper(this).getGroupDataSource().deleteGroup(group);
        this.showAndRefreshGroupListFragment();
    }

    @Override
    public void handleRequestDelete(Computer computer) {
        ComputerDeleteDialogFragment.newInstance(computer).show(this.getSupportFragmentManager(), "delete_computer_dialog");
    }

    @Override
    public void handleRequestDelete(Group group) {
        GroupDeleteDialogFragment.newInstance(group).show(this.getSupportFragmentManager(), "delete_group_dialog");
    }

    @Override
    public void handleRequestUpdate(Computer computer) {
        ComputerDialogFragment.newInstance(computer).show(this.getSupportFragmentManager(), "edit_computer_dialog");
    }

    @Override
    public void handleRequestUpdate(Group group) {
        GroupDialogFragment.newInstance(group).show(this.getSupportFragmentManager(), "edit_group_dialog");
    }

    @Override
    public void handleUpdate(Computer computer) {
        new DataSourceHelper(this).getComputerDataSource().updateComputer(computer);
        this.showAndRefreshComputerListFragment();
    }

    @Override
    public void handleUpdate(Group group) {
        new DataSourceHelper(this).getGroupDataSource().updateGroup(group);
        this.showAndRefreshGroupListFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setContentView(R.layout.activity_main);

        final ActionBar actionBar = this.getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(false);

        this.mSectionsPagerAdapter = new SectionsPagerAdapter(this.getSupportFragmentManager());

        this.mViewPager = (ViewPager) this.findViewById(R.id.pager);
        this.mViewPager.setAdapter(this.mSectionsPagerAdapter);
        this.mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        for (int i = 0; i < this.mSectionsPagerAdapter.getCount(); i++) {
            actionBar.addTab(actionBar.newTab().setText(this.mSectionsPagerAdapter.getPageTitle(i)).setTabListener(this));
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
                this.onRequestCreateComputer();
                return true;
            case R.id.menu_add_group:
                this.onRequestCreateGroup();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onRequestCreateComputer() {
        ComputerDialogFragment.newInstance().show(this.getSupportFragmentManager(), "add_computer_dialog");
    }

    public void onRequestCreateGroup() {
        GroupDialogFragment.newInstance().show(this.getSupportFragmentManager(), "add_group_dialog");
    }

    @Override
    public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        this.mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    public void showAndRefreshComputerListFragment() {
        this.mViewPager.setCurrentItem(SectionsPagerAdapter.PAGE_COMPUTERS);
        ComputerListFragment f = (ComputerListFragment) this.getSupportFragmentManager().findFragmentByTag(this.getFragmentTag(SectionsPagerAdapter.PAGE_COMPUTERS));
        f.updateList();
        this.updateAllAppwidgets();
    }

    public void showAndRefreshGroupListFragment() {
        this.mViewPager.setCurrentItem(SectionsPagerAdapter.PAGE_GROUPS);
        GroupListFragment f = (GroupListFragment) this.getSupportFragmentManager().findFragmentByTag(this.getFragmentTag(SectionsPagerAdapter.PAGE_GROUPS));
        f.updateList();
        this.updateAllAppwidgets();
    }

    public void updateAllAppwidgets() {
        final Class<?>[] providerClasses = new Class<?>[]
            { WakeComputerWidget.class, WakeGroupWidget.class };
        for (Class<?> providerClass : providerClasses) {
            Intent intent = new Intent(this, providerClass);
            intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
            int[] ids = AppWidgetManager.getInstance(this.getApplication()).getAppWidgetIds(new ComponentName(this.getApplication(), providerClass));
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
            this.sendBroadcast(intent);
        }

    }
}
