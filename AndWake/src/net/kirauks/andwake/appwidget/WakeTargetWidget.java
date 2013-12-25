package net.kirauks.andwake.appwidget;

import net.kirauks.andwake.R;
import net.kirauks.andwake.packets.WolPacketSendHelper;
import net.kirauks.andwake.targets.Computer;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class WakeTargetWidget extends AppWidgetProvider {
	public static final String ACTION_WAKE_TARGET = "net.kirauks.andwake.appwidget.ACTION_WAKE_TARGET";
 
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int N = appWidgetIds.length;
 
        // Perform this loop procedure for each App Widget that belongs to this
        // provider
        for (int i = 0; i < N; i++) {
            int appWidgetId = appWidgetIds[i];
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
 
    /**
     * Update the widget
     *
     * @param context
     * @param appWidgetManager
     * @param appWidgetId
     */
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
 
        // Prepare widget views
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget_wake_target);
        views.setTextViewText(R.id.appwidget_wake_target_name, "TestName");
        
        // Prepare intent to launch on widget click
        Intent intent = new Intent(context, WakeTargetWidget.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setAction(ACTION_WAKE_TARGET);
        
        // Launch intent on widget click
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        views.setOnClickPendingIntent(R.id.appwidget_wake_target_wake, pendingIntent);
        
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }
    
    /**
     * Handle new messages
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
 
        if (ACTION_WAKE_TARGET.equals(intent.getAction())) {
            Computer c = new Computer();
            c.setName("Rauks");
            c.setAddress("kirauks.net");
            c.setMac("AA:AA:AA:AA:AA:AA");
            c.setPort(9);
            new WolPacketSendHelper(context).doSendWakePacket(c);
        }
    }
}
