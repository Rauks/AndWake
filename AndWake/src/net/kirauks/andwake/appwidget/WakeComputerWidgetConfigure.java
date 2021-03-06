package net.kirauks.andwake.appwidget;

import net.kirauks.andwake.appwidget.fragments.ConfigureComputerDialogFragment;
import net.kirauks.andwake.appwidget.fragments.listeners.OnConfigureCancelListener;
import net.kirauks.andwake.appwidget.fragments.listeners.OnConfigureComputerListener;
import net.kirauks.andwake.targets.Computer;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;

public class WakeComputerWidgetConfigure extends FragmentActivity {
    private int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    /**
     * Configures the created widget
     * 
     * @param context
     */
    public void configureWidget(Context context) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        WakeComputerWidget.updateAppWidget(context, appWidgetManager, this.mAppWidgetId);
    }

    /** Called when the activity is created */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // If the user closes window, don't create the widget
        this.setResult(Activity.RESULT_CANCELED);

        // Find widget id from launching intent
        Intent intent = this.getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            this.mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If they gave us an intent without the widget id, just bail.
        if (this.mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            this.finish();
        }

        ConfigureComputerDialogFragment config = new ConfigureComputerDialogFragment();
        config.setOnConfigureComputer(new OnConfigureComputerListener() {
            @Override
            public void onConfigureComputer(Computer choice) {
                WakeComputerWidgetConfigure.this.onDialogResponse(choice);
            }
        });
        config.setOnConfigureCancel(new OnConfigureCancelListener() {
            @Override
            public void onConfigureCancel() {
                WakeComputerWidgetConfigure.this.finish();
            }
        });
        config.show(this.getSupportFragmentManager(), "configure_widget");
    }

    private void onDialogResponse(Computer choice) {
        Context context = this.getApplicationContext();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putLong(WakeComputerWidget.PREFERENCES_TAG + this.mAppWidgetId, choice.getId()).commit();

        this.configureWidget(context);

        // Make sure we pass back the original appWidgetId before closing the
        // activity
        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, this.mAppWidgetId);
        this.setResult(Activity.RESULT_OK, resultValue);
        this.finish();
    }
}