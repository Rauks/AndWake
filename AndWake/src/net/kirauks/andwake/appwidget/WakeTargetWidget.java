package net.kirauks.andwake.appwidget;

import java.util.List;

import net.kirauks.andwake.R;
import net.kirauks.andwake.packets.WolPacketSendHelper;
import net.kirauks.andwake.targets.Computer;
import net.kirauks.andwake.targets.db.DataSourceHelper;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

public class WakeTargetWidget extends AppWidgetProvider {
	public static final String ACTION_WAKE_TARGET = "net.kirauks.andwake.appwidget.ACTION_WAKE_TARGET";
	public static final String INTENT_WAKE_TARGET = "target";
	public static final String PREFERENCES_TAG = "appwidget-target-";

	private static Computer getTarget(Context context, int appWidgetId){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		long targetId = prefs.getLong(WakeTargetWidget.PREFERENCES_TAG + appWidgetId, 0);
		Computer target = new Computer();
		
		DataSourceHelper db = new DataSourceHelper(context);
		final List<Computer> computers = db.getComputerDataSource().getComputers(new long[]{targetId});
		
		if(computers.isEmpty()){
			target.setName("?");
		}
		else{
			target = computers.get(0);
		}
		return target;
	}
	
	/**
	 * Update the widget
	 * 
	 * @param context
	 * @param appWidgetManager
	 * @param appWidgetId
	 */
	static void updateAppWidget(Context context,
			AppWidgetManager appWidgetManager, int appWidgetId) {
		
		Computer target = WakeTargetWidget.getTarget(context, appWidgetId);
		
		// Prepare widget views
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.appwidget_wake_target);
		views.setTextViewText(R.id.appwidget_wake_target_name, target.getName());

		// Prepare intent to launch on widget click
		Intent intent = new Intent(context, WakeTargetWidget.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);
		intent.putExtra(WakeTargetWidget.INTENT_WAKE_TARGET, target);
		intent.setAction(WakeTargetWidget.ACTION_WAKE_TARGET);

		// Launch intent on widget click
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.appwidget_wake_target_wake,
				pendingIntent);

		appWidgetManager.updateAppWidget(appWidgetId, views);
	}

	/**
	 * Handle new messages
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

		if (WakeTargetWidget.ACTION_WAKE_TARGET.equals(intent.getAction())) {
			
			Computer target = intent.getParcelableExtra(WakeTargetWidget.INTENT_WAKE_TARGET);
			new WolPacketSendHelper(context).doSendWakePacket(target);
		}
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		final int N = appWidgetIds.length;

		// Perform this loop procedure for each App Widget that belongs to this
		// provider
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];
			WakeTargetWidget.updateAppWidget(context, appWidgetManager,
					appWidgetId);
		}
	}
}
