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

At the top of each View, you can see a blue Toolbar. At the left of this the title of the current View will be displayed. At the right you can find three horizontal lines. By clicking on them you can open and close the popup-menu.

## Managing your classes

If you open the app, the `Classes Fragment` is launched.
Initially, you will just see one button `+ ADD CLASS`.

If you click on this button, a new View will be opened (`Class Edit`).
Here, you can name your class, say how many ECTS it gives, if you passed it and assign a color to it. You can also just use the provided (random) color and name.
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
* add a room to your event
  * if you leave it empty, nothing will be displayed
  * if you write something, it will be visible in the day view
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

## Managing your semesters

You can get to the `Semester`-View by opening the popup-menu and clicking on the `MANAGE SEMESTER`-TextView.

It will display:
* The current Semester
  * You can switch semesters by clicking on the text next to `Current Semester`
  * you can edit the name of the current semester
  * you can also edit the start and end date of the current semester
  * if you want your changes to be saved, you can click on save, which will exit the `Semester`-View.
  * you can also delete a semester.
* a `+ ADD NEW SEMESTER`-Button, which adds a new semester and sets it as the current semester.

**All your data is associated to a semester**. If you enter the app for the first time, a semester is automatically created, starting at the current date and ending in current date plus 6 months.
If you delete a semester, all associated data will no longer be accessible.
For now, a class can only be part of one semester.
If you create an event that is repeated weekly, an event will be created for each week in the current semester. (This is what I meant by "infinite").
The classes, events and assignments you can see in the app are only the classes, events and assignments for the currently selected semester.

## Managing your grades

You can access the `Grades`-View by opening the popup-menu and clicking on the `GRADES`-TextView.

There you can see a list of all classes with their average grade. You can add a grade for a class by clicking on the `+`-icon.

It will open the `Add Grade`-View. You can set a weight and a grade.

By clicking on a class in the list, you can open the `Grades for class`-View. There you can see all grades for this class.
You can decide to give them titles (by just editing the title at the left of each grade). You can also edit weight and grade just by editing them directly. Changes will be saved automatically. You can also delete grades by clicking the trash-icon.
You can add a grade for this class by clicking the `+ ADD GRADE`-Button, which will open the `Add Grade`-View.

## Account

You can access the `Account`-View by opening the popup-menu and clicking on the `ACCOUNT`-TextView.

There you can see one of two possible things:
* a text input for email, password, a `LOG IN`-Button and a `SIGN UP`-Button.
  * this means you are currently **logged out**
  * if you already have an account, you can enter your email and password here. Then, you can click on the `LOG IN`-Button.
  * if you don't have an account yet, you can enter your email and a password. Then, you can click on the `SIGN UP`-Button.
* a `LOG OUT`-Button
  * this means you are currently **logged in**

If you log into an account, all the data that currently is on the app will be wiped and replaced with the data for that account.

If you sign up, all the data that currently is on the app will be saved to the newly created account.

If you log out, all data that currently is on the app will be saved to the server and then wiped from your device. You will no longer see the data, but it still exists.

This means the only way you can lose data is, if you add data while being logged out and then logging in to an account.

## ECTS

ECTS stands for European Credit Transfer and Accumulation System. At UNI you have to get ECTS in different areas in order to get your degree.

You can access the `ECTS`-View by opening the popup-menu and clicking on the `ECTS`-TextView.
There you will initially be presented with with just a `+ ADD NEW TYPE`-Button. If you press it, you can add a new area, give it a name and define how many ECTS you need in that area.

If you have added some Types, they will be displayed in the `ECTS`-View. For each type it will show:
* the name you gave it
* the ECTS you are currently working on (ECTS of classes of the current Semester that are not yet passed)
* the ECTS you still need in that type
If you got all ECTS needed for a type, the type will have a green background :)

## Widgets

The app also provides some home screen widgets. Specifically:

* A timetable widget, displaying your events
  * if you click on it, it opens the `Timetable`-View
* A stopwatch widget, displaying your study time for all classes
  * if you click on it, it open the `Stopwatch`-View
* An assignment widget, displaying all your assignments (sorted by due date)
  * if you click on it, it opens the `Assignment`-View