# Youtube Floating Video - FlyingVideo
> Easy to use floating Youtube Video Library
> More features will be added in upcoming days

### Why this project exist ?
Picture in picture has become popular these days. Unfortunately Android brought this support to Android 8.0 (API level 26) and later versions. Pip and floating video supported apps offer users the ability to easily view other features while watching videos (Like reading comments etc). With this library you can play youtube videos in a floating panel. I have just started to develop this library and I am open to all your ideas and opinions.

## Features and Usage


`view` can be any Button, Imageview or ImageButton which activate the FlyingVideo. `videoId` should have an ID of your YouTube video. For simply:

```java
  FlyingVideo.get(MainActivity.this)
                .setFloatMode(TaskCoffeeVideo.FLOAT_MOVE.STICKY)
                .setVideoStartSecond((mTracker == null) ? 0 : mTracker.getCurrentSecond())
                .coffeeVideoSetup(videoId)
                .setFlyGravity(TaskCoffeeVideo.FLY_GRAVITY.TOP)
                .show(view);
```
![](previews/untitledx1.gif) ![](previews/untitledx22.gif)
