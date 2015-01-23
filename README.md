# AlertMe_v01
Theft deterrent app based on device motion

Once alarm is set, the device will sound an alarm when significant movement is detected.  
Currently implemented using accelerometer only, since there are more devices with a hardware based accelerometer than with a gyroscope, but gyro support is planned!   
(Maybe acc in conjunction with gyro for more accuracy, but for now acc only is more than sufficient)  
Using low- and high-pass filters (found [here](http://developer.android.com/guide/topics/sensors/sensors_motion.html#sensors-motion-accel)) to isolate and remove random variations in accelerometer data. This ensures that sensor noise is accounted for, and also allows us to use this app on any device without concern for different variations from different hardware.  
When the app is counting down to the timer being set, the filters are being used to reach equilibrium. In some cases the device sensor will start off with really high values for the first two or three readings, and so we need the countdown to allow for the other (smaller) values to balance it out. A more efficient fix is coming soon; I still need to investigate the best way to implement the filters.
