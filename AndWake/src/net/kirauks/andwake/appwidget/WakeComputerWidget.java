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

public class WakeComputerWidget extends AppWidgetProvider {
	public static final String ACTION_WAKE_COMPUTER = "net.kirauks.andwake.appwidget.ACTION_WAKE_COMPUTER";
	public static final String INTENT_WAKE_COMPUTER = "computer";
	public static final String PREFERENCES_TAG = "appwidget-computer-";

	private static Computer getTarget(Context context, int appWidgetId){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		long targetId = prefs.getLong(WakeComputerWidget.PREFERENCES_TAG + appWidgetId, 0);
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
		
		Computer target = WakeComputerWidget.getTarget(context, appWidgetId);
		
		// Prepare widget views
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.appwidget_wake_computer);
		views.setTextViewText(R.id.appwidget_computer_name, target.getName());
		views.setTextViewText(R.id.appwidget_computer_mac, target.getMac());
		views.setTextViewText(R.id.appwidget_computer_address, target.getAddress());
		views.setTextViewText(R.id.appwidget_computer_port, String.valueOf(target.getPort()));

		// Prepare intent to launch on widget click
		Intent intent = new Intent(context, WakeComputerWidget.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);
		intent.putExtra(WakeComputerWidget.INTENT_WAKE_COMPUTER, target);
		intent.setAction(WakeComputerWidget.ACTION_WAKE_COMPUTER);

		// Launch intent on widget click
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.appwidget_computer_wake,
				pendingIntent);

		appWidgetManager.updateAppWidget(appWidgetId, views);
	}

	/**
	 * Handle new messages
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

		if (WakeComputerWidget.ACTION_WAKE_COMPUTER.equals(intent.getAction())) {
			
			Computer target = intent.getParcelableExtra(WakeComputerWidget.INTENT_WAKE_COMPUTER);
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
			WakeComputerWidget.updateAppWidget(context, appWidgetManager,
					appWidgetId);
		}
	}
}
