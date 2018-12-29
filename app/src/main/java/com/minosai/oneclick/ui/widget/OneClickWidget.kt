package com.minosai.oneclick.ui.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.*
import android.widget.RemoteViews
import com.minosai.oneclick.R
import com.minosai.oneclick.util.Constants
import com.minosai.oneclick.util.helper.LoginLogoutBroadcastHelper
import com.minosai.oneclick.network.WebService

/**
 * Implementation of App Widget functionality.
 */
class OneClickWidget :
        AppWidgetProvider() {

    val TAG = javaClass.simpleName ?: Constants.PACKAGE_NAME

    private lateinit var preferences: SharedPreferences
    private lateinit var webService: WebService

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them

        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
        preferences = context.getSharedPreferences(Constants.PREF_KEY, Context.MODE_PRIVATE)
//        webService = WebService(context, preferences)
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                                     appWidgetId: Int) {

            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.one_click_widget)
            views.setOnClickPendingIntent(R.id.widget_button, LoginLogoutBroadcastHelper.getPendingIntent(context))

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

}

