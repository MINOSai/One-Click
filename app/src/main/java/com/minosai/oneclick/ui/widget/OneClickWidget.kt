package com.minosai.oneclick.ui.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.SharedPreferences
import android.widget.RemoteViews
import com.minosai.oneclick.R
import com.minosai.oneclick.util.helper.Constants
import com.minosai.oneclick.util.service.WebService

/**
 * Implementation of App Widget functionality.
 */
class OneClickWidget : AppWidgetProvider() {

    private var isLogged = false
    private var isWifiConnected = false
    private var isOnline = false

    private lateinit var preferences: SharedPreferences
    private lateinit var webService: WebService

    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them

        with(preferences) {
            isLogged = getBoolean(Constants.PREF_ISLOGGED, false)
            isWifiConnected = getBoolean(Constants.PREF_ISWIFICONNECTED, false)
            isOnline = getBoolean(Constants.PREF_ISONLINE, false)
        }

        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
        preferences = context.getSharedPreferences(Constants.PREF_KEY, Context.MODE_PRIVATE)
        webService = WebService(context, preferences)
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    companion object {

        internal fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager,
                                     appWidgetId: Int) {

//            val widgetText = context.getString(R.string.appwidget_text)
            // Construct the RemoteViews object
            val views = RemoteViews(context.packageName, R.layout.one_click_widget)
//            views.setTextViewText(R.id.fab_widget, widgetText)

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }
}

