# Student App

This is an android app meant for students.

The app has multiple key features:
* managing your classes
* tracking your study time
* managing your timetable
* managing your assignments
* managing your semesters
* managing your grades
* creating an account to access your data from different devices
* (more to be added)

Each feature will be explained in detail below.

## Managing your classes

If you open the app, the `Classes Fragment` is launched.
Initially, you will just see one button `+ ADD CLASS`.

If you click on this button, a new View will be opened (`Class Edit`).
Here, you can name your class and assign a color to it. You can also just use the provided (random) color and name.
You can exit this View in two ways:
* If you decide against adding that class, you have to press the `DELETE`-Button.
* If you want to add the class, you can press the `SAVE`-Button.
Both will lead you back to the `Classes Fragment`-View.

If you added one or more classes, they will be listed in the `Classes Fragment`-View.
You can edit any of those classes by clicking on the corresponding pen-icon located at the right.

## Tracking your study time

You can navigate to the `Stopwatch`-View by clicking the clock icon in the bottom navigation.
At the top of this View, you can see two TextViews: `Stopwatch` and `Insights`. They are clickable.
You will also see a big time tracker, displaying the total time you studied today.
Below that you will, if you have added one or more classes, see a list of all your classes and how much you studied for this class today.
At the right of each class you can see a play-icon. 

If you click on any play-icon, the app starts tracking. This means that:

The app will now be pinned. You cannot exit the app unless you:
* Stop the tracking (by either clicking on the pause-icon or navigating to another view in the app).
* Press the `back` and `recents` buttons of your phone at the same time.
It will also block notifications.

The class you are currently tracking for increases its study time each second. The total study time is also increased each second.

While tracking, you can also seamlessly switch from one class to another.

If you click on any class in the `Stopwatch`-View, it will open a new View (`Class`-View), displaying detailed information about the time you spend studying for this class (this day, week, month and in total).

If you click on the `Insights`-TextView, it will open a new View (`Insights`-View). This will display some insights about your study time. 
For now, the insights available are:
* How much time you spent studying for which class this **day**
* How much time you spent studying for which class this **week**
* How much time you spent studying for which class this **month**
You can get back to the `Stopwatch`-View by clicking on the `Stopwatch`-TextView.

## Managing your timetable

You can navigate to the `Timetable`-View by clicking on the timetable icon in the bottom navigation.

You will see a calendar week view (from Monday to Friday, 07:00 to 18:00). It will initially be set to the calendar week you are currently in.
You can navigate to other weeks by clicking on the left and right arrow buttons at the top left/right.

You can click on any cell in the timetable. 
* If the cell is empty, it will open the `Event Edit`-View.
* If the cell is not empty (meaning an event takes place at this hour), it will open the `Day`-View.

The `Event Edit`-View works similar to the `Class Edit`-View.
You can:
* choose a name
  * if you don't change it, the name will be "New Event"
  * if you delete the name (= name is empty), the event will take the name of the class as default value. note that if you do not assign a class to your event, this will lead to the event having **no name**
* choose a date
  * by default the date of the cell clicked on
* choose a time
  * by default the time of the cell clicked on
* choose a class the event belongs to
  * by default no class
  * if you choose a class, the event will have a background color (the one of the class)
    ***if you choose no class and set the name empty, the event will not be visible in the timetable. However, it still is in the corresponding cell, just not visible***
* choose if the event should be repeated weekly
  * if you tick this, "infinite" events will be created, one each week
The `DELETE` and `SAVE` Buttons work analog to the ones of the `Event Edit`-View.

The `Day`-View will open a calendar day view. The date of the day being displayed is at the top left.
You can navigate to other days using the arrows below the date.
Next to the date and arrows, assignments for this date will be displayed.
Below the date and assignments, you can see all events for each hour of the day. If you click on an event there, it will open the `Event Edit`-View for that event.
Above the date and assignments, there is a `NEW EVENT`-Button, which will open the `Event Edit`-View for a new event.

## Managing your assignments

You can navigate to the `Assignments`-View by clicking on the assignment icon in the bottom navigation.

It will initially just display a `+ ADD ASSIGNMENT`-Button, and two TextViews (`Overview` and `Calendar View`) which are clickable.

If you click on the `+ ADD ASSIGNMENT`-Button, a new View will be opened (`Assingment Edit`-View).
If works like the other `Edit`-Views. You can:
* choose a title
  * by default it has no title
  * if you choose a class it will take the class name as title by default
* choose a due date
* choose a class
  * if you choose a class, the assignment will have a color (the class color) associated with it

After you added one or more assignments, they will be displayed (sorted by due date) in the `Assignments`-View.
If your assignment is overdue, the date and title will be red.
Other than the due date, title, (color), and the pen (by clicking on it you can edit the assignment) each assignment will also have a progress bar below it.
If you click on an assignment, a new View will open, where there is a slider with which you can indicate your progress.
If you completed the assignment (= slider is all the way to the right), the assignment will no longer be shown anywhere.

If you click on the `Calendar View`-TextView, a new View will open (`Calendar`-View). It will display the current month.
The date and arrows work like in the `Timetable`-View.
If you added one or more assignments **associated to a class** (= they have a color), the due date will have a little dot with the corresponding color.
You can click on any day, which will open the `Day`-View for the clicked day.