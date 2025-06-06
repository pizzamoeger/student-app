package com.hannah.studentapp.ui.assignments

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.hannah.studentapp.MainActivity
import com.hannah.studentapp.R
import com.hannah.studentapp.ui.assignments.assignment.Assignment

/**
 * Implementation of App Widget functionality.
 */
class AssignmentWidget : AppWidgetProvider() {
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
    val intent = Intent(context, AssignmentRemoteViewService::class.java)
    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

    val views = RemoteViews(context.packageName, R.layout.assignment_widget)
    views.setRemoteAdapter(R.id.assignments_recycler_view_widget, intent)
    views.setEmptyView(R.id.assignments_recycler_view_widget, android.R.id.empty)

    val configIntent = Intent(context, MainActivity::class.java).apply {
        putExtra("fragmentOpen", "AssignmentFragment")
    }
    //configIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
    val configPendingIntent = PendingIntent.getActivity(context, appWidgetId, configIntent,
        PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT)
    views.setOnClickPendingIntent(R.id.root_assignment_widget, configPendingIntent)
    views.setPendingIntentTemplate(R.id.assignments_recycler_view_widget, configPendingIntent)

    appWidgetManager.updateAppWidget(appWidgetId, views)
}

class AssignmentRemoteViewService : RemoteViewsService() {
    override fun onGetViewFactory(intent: Intent): RemoteViewsFactory {
        return RemoteViewsFactory(this.applicationContext, intent)
    }

    class RemoteViewsFactory(
        private val context: Context,
        intent: Intent
    ) : RemoteViewsService.RemoteViewsFactory {

        private var items = getAssignment()

        override fun onCreate() {}

        private fun getAssignment() : List<Assignment> {
            val assignments = Assignment.getUncompletedList()
            return assignments.sortedBy {it.getDueDate()}
        }

        private fun isFirstOfDay(position: Int) : Boolean {
            if (position == 0) return true
            if (items[position-1].getDueDate() != items[position].getDueDate()) return true
            return false
        }

        override fun getCount(): Int = items.size

        override fun getViewAt(position: Int): RemoteViews {

            val rv = RemoteViews(context.packageName, R.layout.assignment_widget_item)
            rv.setTextViewText(R.id.name_text_assignment_widget, items[position].getTitle())
            rv.setInt(R.id.classes_class_color_assignment_widget, "setBackgroundColor", items[position].getClass().getColor())

            rv.setTextViewText(R.id.date_text_assignment_widget, items[position].getDueDate().toString())

            rv.setProgressBar(R.id.progress_bar_widget, 100, items[position].getProgress(), false)
            /*rv.setInt(R.id.name_widget_timetable_item, "setTextColor", ContextCompat.getColor(context, R.color.gray_1))
            rv.setTextViewText(R.id.time_widget_timetable_item, items[position].getTime().toString())

            if (isFirstOfDay(position)) {
                rv.setViewVisibility(R.id.date_widget_timetable, View.VISIBLE)
                rv.setTextViewText(R.id.date_widget_timetable, CalendarUtils.dayMonth(items[position].getDate()))
            } else {
                rv.setViewVisibility(R.id.date_widget_timetable, View.INVISIBLE)
            }*/
            rv.setOnClickFillInIntent(R.id.assignment_widget_item, Intent())


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