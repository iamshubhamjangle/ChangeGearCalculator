# ChangeGearCalculator

This application is designed for mechanical engineer to get the gears quickly for spiral machining. 
It takes input as (is*S), where 'is' = Dividing head constant and 'S' = pitch of table lead screw (mm).
The second input is (h), where h =  pitch of spiral flute to be milled (Spiral Pitch).

We are calculating change gears (A,B) OR (A,B,C,D), it indicates the ratio of pitch of table lead screw and the pitch of spiral to be milled.
Formula followed,
iw = (A * C) / (B * D) = (is*S) / h

Currently the app calculates ratio from the user input and compares it with the pre generated csv file to get the two gear combination A,B or four gears combination A, B, C, D.


Want to help to improve app? Read the improvement required section below.

## Improvement required
1) Existing ListView is basic list view, we need it to be updated to Custom list view. A row_item.xml file already exits in layout directory. We just need to add a custom adapter to update the current listView to CustomListView.

2) Hide the keyboard when user presses calculate button.

3) Add clear button which clears input field's.

4) Code repeatation occurs in calculateTwoGear() and calculateFourGear(). Can we improve it?


### Note:
While creating pull request describe what changes has been made. Happy Coding!
