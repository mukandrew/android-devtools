package academy.mukandrew.android.devtools

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.provider.Settings.Global
import android.widget.RemoteViews

private const val INTENT_ACTION = "academy.mukandrew.android.devtools.widget.action"

class DevToolWidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?,
    ) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        val packageManager = context?.packageName ?: return
        appWidgetIds?.forEach { appWidgetId ->
            val views = RemoteViews(packageManager, R.layout.widget_devtools)

            val intent = Intent(context, DevToolWidgetProvider::class.java)
            intent.setAction(INTENT_ACTION)
            val pendingIntent =
                PendingIntent.getBroadcast(
                    context,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE,
                )
            views.setOnClickPendingIntent(R.id.button, pendingIntent)

            appWidgetManager?.updateAppWidget(appWidgetId, views)
        }
    }

    override fun onReceive(
        context: Context?,
        intent: Intent?,
    ) {
        super.onReceive(context, intent)
        if (intent?.action == INTENT_ACTION && context != null) {
            openDeveloperMode(context)
        }
    }

    private fun openDeveloperMode(context: Context) {
        try {
            val action =
                if (isDeveloperModeEnabled(context)) {
                    Settings.ACTION_APPLICATION_DEVELOPMENT_SETTINGS
                } else {
                    Settings.ACTION_DEVICE_INFO_SETTINGS
                }
            context.startActivity(
                Intent(action).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun isDeveloperModeEnabled(context: Context): Boolean {
        return Global.getInt(
            context.contentResolver,
            Global.DEVELOPMENT_SETTINGS_ENABLED,
            0,
        ) != 0
    }
}
