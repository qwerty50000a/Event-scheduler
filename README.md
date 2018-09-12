# Event-scheduler
A GUI-based event scheduler that lets the user create, edit, and search for events effeciently.

# Features
- GUI interface which allows easy user interaction
- Built-in calendar which allows viewing the events of a certain day
- Each event has a starting time and ending time which allows flexibility in event assigning
- Allows searching events between two dates in an efficient time 





![alt text](https://github.com/qwerty50000a/event-scheduler/blob/master/sc1.png)


# Implementation

Each event has a starting and ending time. Therefore, each event can be represented by a two dimensional point which has (x,y) = (event staring time, event ending time).
All the points are inserted into a k-d tree (k=2). The k-d tree allows searching for events between two dates in efficiently. Searching for events between two dates can be achieved by searching for events inside a rectangle in the k-d tree. The dimensions of the searching rectangle in the k-d tree is determined by searching interval.

![alt text](http://url/to/img.png)

