# Android-SwipeAndDrag

_An example illustrating swipe-to-delete and drag-and-drop functionality in a recyclerview._

Most recyclerview allow users to perform the following actions:

1. "Swipe-to-Delete" - Users can swipe on an individual item in the recyclerview, whether it be left swipe, right swipe or in both directions.

2. "Drag-and-Drop" - Reorder the items in the recyclerview be dragging a particular item.

This github repository tries to implement the above features.

<br>
<img src="https://img.shields.io/badge/minSdkVersion-26-red.svg?style=true" alt="minSdkVersion 21" data-canonical-src="https://img.shields.io/badge/minSdkVersion-24-red.svg?style=true" style="max-width:100%;">
<img src=https://img.shields.io/badge/compileSdkVersion-28-brightgreen.svg alt="compileSdkVersion 28" data-canonical-src="https://img.shields.io/badge/compileSdkVersion-27-yellow.svg?style=true" style="max-width:100%;">

## App in action

![ezgif com-crop](https://user-images.githubusercontent.com/39665412/50549419-899eee00-0c97-11e9-9333-279aa9f3ae24.gif)

## Guide
The `ItemTouchHelper` class is key to implementing the above features - it is a utility class specifically designed to support such tasks for a recyclerview.

### Swipe-to-delete

#### Layout

The easiest way to seems to be having the recyclerview viewholder's root layout as a `FrameLayout`, with the foreground layout and background layout on top of each other.

```xml
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

    <include layout="@layout/swipe_item_background" />
    <include layout="@layout/swipe_item_foreground" />

</FrameLayout>
```

__Important__ : You __must__ ensure that the background layout is above the foreground layout in xml, and that you give the foreground layout a color using `android:background` or else it will be transparent during the swipe animation.

#### RecyclerView Adapter

[_See code here_](https://github.com/wRorsjakz/Android-SwipeAndDrag/blob/master/app/src/main/java/com/example/user/swipeanddrag/Swipe/SwipeRVAdapter.java)

Though most the code in the adapter is per usual (note that in `onCreateViewHolder`, inflate the layout file with the `FrameLayout` as the parent layout, not the foreground or background layout files ), there are some extra code/methods:

1. Instantiate the foreground layout's root layout in the viewholder and create a getter method for it.

2. Create two methods - `removeItem(int position)` and `addItem(int position, Contact contact)`.

#### ItemTouchHelper

[_See code here_](https://github.com/wRorsjakz/Android-SwipeAndDrag/blob/master/app/src/main/java/com/example/user/swipeanddrag/Swipe/SwipeRVTouchHelper.java)

Create a class which extends `ItemTouchHelper.SimpleCallback` and using the observer pattern, create a interface so that the recyclerview can listen to when the item has been swiped off the screen.

You need to override the following five methods:

1. `onMove(...)` - simply return `true`

2. `onSelectedChanged(...)`

3. `onChildDraw(...)` - Customise how the background is drawn depending on which direction the item is being swiped. If you only want to allow swiping in one direction, simply hardcode the background color and icon in xml and don't include the if-block.

4. `clearView(...)`  

5. `onSwiped(...)` - Called after the item is swiped off the screen, so code what will happen after that here

#### Fragment/Activity

[_See code here_](https://github.com/wRorsjakz/Android-SwipeAndDrag/blob/master/app/src/main/java/com/example/user/swipeanddrag/Swipe/SwipeFragment.java)

Have the class implement your `ItemTouchHelper` interface and override `onSwiped`. If the user can swipe in both direction, check which direction the user has swiped using `ItemTouchHelper.LEFT` or `ItemTouchHelper.RIGHT`.

In my example, I temperory store the deleted item before calling `removeItem(int position)` and showing a `Snackbar`. Use `addItem(int position, Contact contact)` to add the recently deleted item back.

Finally, attach the listener to the recyclerview using `attachToRecyclerView()`
```java
//Swping in both direction is enabled
ItemTouchHelper.SimpleCallback simpleCallback = new SwipeRVTouchHelper(this,0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
```

Control which direction swiping is enabled by passing the appropriate params `ItemTouchHelper.DIRECTION`.

## Dependencies

```java
//Design support library
implementation 'com.android.support:design:28.0.0'
//RecyclerView
implementation 'com.android.support:recyclerview-v7:28.0.0'
//Volley
implementation 'com.android.volley:volley:1.1.1'
```

## License

```tex
MIT License

Copyright (c) 2018 Nicholas Gan

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```