# OrientationTest
Demonstrating YouTube-like functionality of an expand/collapse button

The MainActivity shows determining orientation based on raw pitch/roll/yaw values.

The TestActivity demonstrates that orientation can be sensor based even AFTER set programmatically. Normally, on Android (in my experience) the orientation sticks forever when it is set programmatically. This switches it back to sensor once the user physically matches the orientation with the screen
