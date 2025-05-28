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