package net.kirauks.andwake.appwidget;

import java.util.List;

import net.kirauks.andwake.R;
import net.kirauks.andwake.packets.WolPacketSendHelper;
import net.kirauks.andwake.targets.Group;
import net.kirauks.andwake.targets.db.DataSourceHelper;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

public class WakeGroupWidget extends AppWidgetProvider {
	public static final String ACTION_WAKE_GROUP = "net.kirauks.andwake.appwidget.ACTION_WAKE_GROUP";
	public static final String INTENT_WAKE_GROUP = "group";
	public static final String PREFERENCES_TAG = "appwidget-group-";

	private static Group getTarget(Context context, int appWidgetId){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		long targetId = prefs.getLong(WakeGroupWidget.PREFERENCES_TAG + appWidgetId, 0);
		Group target = new Group();
		
		DataSourceHelper db = new DataSourceHelper(context);
		final List<Group> groups = db.getGroupDataSource().getGroups(new long[]{targetId});
		
		if(groups.isEmpty()){
			target.setName("?");
		}
		else{
			target = groups.get(0);
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
		
		Group target = WakeGroupWidget.getTarget(context, appWidgetId);
		
		// Prepare widget views
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.appwidget_wake_group);
		views.setTextViewText(R.id.appwidget_group_name, target.getName());

		// Prepare intent to launch on widget click
		Intent intent = new Intent(context, WakeGroupWidget.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetId);
		intent.putExtra(WakeGroupWidget.INTENT_WAKE_GROUP, target);
		intent.setAction(WakeGroupWidget.ACTION_WAKE_GROUP);

		// Launch intent on widget click
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetId,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.appwidget_group_wake,
				pendingIntent);

		appWidgetManager.updateAppWidget(appWidgetId, views);
	}

	/**
	 * Handle new messages
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);

		if (WakeGroupWidget.ACTION_WAKE_GROUP.equals(intent.getAction())) {
			
			Group target = intent.getParcelableExtra(WakeGroupWidget.INTENT_WAKE_GROUP);
			new WolPacketSendHelper(context).doSendWakePacket(target.getChildren());
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
			WakeGroupWidget.updateAppWidget(context, appWidgetManager,
					appWidgetId);
		}
	}
}
