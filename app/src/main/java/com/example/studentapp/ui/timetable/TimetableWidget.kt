package com.example.studentapp.ui.timetable

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.studentapp.R
import com.example.studentapp.ui.event.Event

/**
 * Implementation of App Widget functionality.
 */
class TimetableWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int
) {
    val intent = Intent(context, MyRemoteViewsService::class.java)
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

    val views = RemoteViews(context.packageName, R.layout.timetable_widget)
    views.setRemoteAdapter(R.id.events_recycler_view_widget, intent)
    views.setEmptyView(R.id.events_recycler_view_widget, android.R.id.empty)

    appWidgetManager.updateAppWidget(appWidgetId, views)
}

class MyRemoteViewsService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return MyRemoteViewsFactory(this.applicationContext, intent)
    }

    class MyRemoteViewsFactory(
        private val context: Context,
        intent: Intent
    ) : RemoteViewsService.RemoteViewsFactory {

        private var items = Event.getEvents()

        override fun onCreate() {
            // Load your data here
            //items.addAll(listOf("Item 1", "Item 2", "Item 3"))
        }

        override fun getCount(): Int = items.size

        override fun getViewAt(position: Int): RemoteViews {
            val rv = RemoteViews(context.packageName, android.R.layout.simple_list_item_1)
            rv.setTextViewText(android.R.id.text1, items[position].getName())
            return rv
        }

        override fun getLoadingView(): RemoteViews? = null
        override fun getViewTypeCount(): Int = 1
        override fun getItemId(position: Int): Long = position.toLong()
        override fun hasStableIds(): Boolean = true
        override fun onDataSetChanged() {
            items = Event.getEvents()
        }
        override fun onDestroy() {}
    }
}
