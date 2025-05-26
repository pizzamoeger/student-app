package com.example.studentapp.ui.timetable

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.content.ContextCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.studentapp.MainActivity
import com.example.studentapp.R
import com.example.studentapp.ui.calendar.CalendarUtils
import com.example.studentapp.ui.classesItem.ClassesItem
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
    val configIntent = Intent(context, MainActivity::class.java).apply {
        putExtra("fragmentOpen", "TimetableFragment")
    }
    //configIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
    val configPendingIntent = PendingIntent.getActivity(context, appWidgetId, configIntent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)

    // TODO on click open correct day
    /*val clickPendingIntentTemplate = NavDeepLinkBuilder(context)
        .setGraph(R.navigation.mobile_navigation)
        .setDestination(R.id.fragment_calendar_day)
        .setComponentName(MainActivity::class.java)
        .createPendingIntent()*/


    val intent = Intent(context, TimetableRemoteViewsService::class.java)
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

    val views = RemoteViews(context.packageName, R.layout.timetable_widget)
    views.setRemoteAdapter(R.id.events_recycler_view_widget, intent)
    views.setEmptyView(R.id.events_recycler_view_widget, android.R.id.empty)
    //views.setPendingIntentTemplate(R.id.events_recycler_view_widget, clickPendingIntentTemplate)
    views.setOnClickPendingIntent(R.id.root_timetable_widget, configPendingIntent)
    views.setPendingIntentTemplate(R.id.events_recycler_view_widget, configPendingIntent)

    appWidgetManager.updateAppWidget(appWidgetId, views)
}

class TimetableRemoteViewsService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return RemoteViewsFactory(this.applicationContext, intent)
    }

    class RemoteViewsFactory(
        private val context: Context,
        intent: Intent
    ) : RemoteViewsService.RemoteViewsFactory {

        val formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

        private var items: List<Event> = emptyList()

        override fun onCreate() {}

        private fun getEvents() : List<Event> {
            val events = Event.getEvents().toMutableList()
            return events.sortedBy {java.time.LocalDateTime.parse("${it.getDate()} ${it.getTime()}", formatter)}
        }

        private fun isFirstOfDay(position: Int) : Boolean {
            if (position == 0) return true
            if (items[position-1].getDate() != items[position].getDate()) return true
            return false
        }

        override fun getCount(): Int = items.size

        override fun getViewAt(position: Int): RemoteViews {

            val rv = RemoteViews(context.packageName, R.layout.timetable_widget_item)
            rv.setTextViewText(R.id.name_widget_timetable_item, items[position].getName())
            rv.setInt(R.id.widget_timetable_item, "setBackgroundColor", ClassesItem.get(items[position].getClassId()).getColor())
            rv.setInt(R.id.name_widget_timetable_item, "setTextColor", ContextCompat.getColor(context, R.color.gray_1))
            rv.setTextViewText(R.id.time_widget_timetable_item, items[position].getTime().toString())

            if (isFirstOfDay(position)) {
                rv.setViewVisibility(R.id.date_widget_timetable, View.VISIBLE)
                rv.setTextViewText(R.id.date_widget_timetable, CalendarUtils.dayMonth(items[position].getDate()))
            } else {
                rv.setViewVisibility(R.id.date_widget_timetable, View.INVISIBLE)
            }

            rv.setOnClickFillInIntent(R.id.widget_timetable_item_all, Intent())

            /*val fillInIntent = Intent().apply {
                data = Uri.parse("studentapp://dayview/${items[position].getDate()}")
            }
            rv.setOnClickFillInIntent(R.id.widget_timetable_item, fillInIntent)*/

            return rv
        }

        override fun getLoadingView(): RemoteViews? = null
        override fun getViewTypeCount(): Int = 1
        override fun getItemId(position: Int): Long = position.toLong()
        override fun hasStableIds(): Boolean = true
        override fun onDataSetChanged() {
            items = getEvents()
        }
        override fun onDestroy() {}
    }
}
