package com.example.studentapp.ui.stopwatch

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.content.ContextCompat
import com.example.studentapp.MainActivity
import com.example.studentapp.R
import com.example.studentapp.TimeInterval
import com.example.studentapp.ui.assignments.assignment.Assignment
import com.example.studentapp.ui.calendar.CalendarUtils
import com.example.studentapp.ui.classesItem.ClassesItem
import com.example.studentapp.ui.event.Event
import com.example.studentapp.ui.timetable.TimetableRemoteViewsService

/**
 * Implementation of App Widget functionality.
 */
class StopwatchWidget : AppWidgetProvider() {
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
    val intent = Intent(context, StopwatchRemoteViewService::class.java)
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

    val views = RemoteViews(context.packageName, R.layout.stopwatch_widget)
    views.setRemoteAdapter(R.id.stopwatch_recycler_view_widget, intent)
    views.setEmptyView(R.id.stopwatch_recycler_view_widget, android.R.id.empty)

    val configIntent = Intent(context, MainActivity::class.java).apply {
        putExtra("fragmentOpen", "StopwatchFragment")
    }
    //configIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
    val configPendingIntent = PendingIntent.getActivity(context, appWidgetId, configIntent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    views.setOnClickPendingIntent(R.id.root_stopwatch_widget, configPendingIntent)
    views.setPendingIntentTemplate(R.id.stopwatch_recycler_view_widget, configPendingIntent)

    appWidgetManager.updateAppWidget(appWidgetId, views)
}

class StopwatchRemoteViewService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return RemoteViewsFactory(this.applicationContext, intent)
    }

    class RemoteViewsFactory(
        private val context: Context,
        intent: Intent
    ) : RemoteViewsService.RemoteViewsFactory {

        private var items = getAssignment()

        override fun onCreate() {}

        private fun getAssignment() : List<ClassesItem> {
            val classes = ClassesItem.getList()
            return classes
        }

        override fun getCount(): Int = items.size

        override fun getViewAt(position: Int): RemoteViews {

            val rv = RemoteViews(context.packageName, R.layout.widget_stopwatch_item)
            rv.setTextViewText(R.id.name_text_classes_item_widget_stopwatch, items[position].toString())
            // Set background color (make the whole button colored)
            /*rv.setInt(
                R.id.timer_start_button_classes_item_widget_stopwatch,
                "setColorFilter",
                items[position].getColor()
            )*/
            rv.setInt(R.id.classes_class_color_stopwatch_widget, "setBackgroundColor", items[position].getColor())
            rv.setInt(R.id.name_text_classes_item_widget_stopwatch, "setTextColor", ContextCompat.getColor(context, R.color.gray_1))
            rv.setInt(R.id.daily_time_classes_item_widget_stopwatch, "setTextColor", ContextCompat.getColor(context, R.color.gray_1))
            rv.setTextViewText(R.id.daily_time_classes_item_widget_stopwatch, ClassesItem.getTimeStringFromSeconds(items[position].getSeconds(TimeInterval.DAY)))
            rv.setOnClickFillInIntent(R.id.widget_stopwatch_item, Intent())

            return rv
        }

        override fun getLoadingView(): RemoteViews? = null
        override fun getViewTypeCount(): Int = 1
        override fun getItemId(position: Int): Long = position.toLong()
        override fun hasStableIds(): Boolean = true
        override fun onDataSetChanged() {
            items = getAssignment()
        }
        override fun onDestroy() {}
    }
}