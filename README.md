# Android-SwipeAndDrag

_An example illustrating swipe-to-delete and drag-and-drop functionality in a recyclerview._

Most recyclerview allow users to perform the following actions:

1. "Swipe-to-Delete" - Users can swipe on an individual item in the recyclerview, whether it be left swipe, right swipe or in both directions.

2. "Drag-and-Drop" - Reorder the items in the recyclerview be dragging a particular item.

This github repository tries to implement the above features.

<br>
<img src="https://img.shields.io/badge/minSdkVersion-21-red.svg?style=true" alt="minSdkVersion 21" data-canonical-src="https://img.shields.io/badge/minSdkVersion-24-red.svg?style=true" style="max-width:100%;">
<img src=https://img.shields.io/badge/compileSdkVersion-28-brightgreen.svg alt="compileSdkVersion 28" data-canonical-src="https://img.shields.io/badge/compileSdkVersion-27-yellow.svg?style=true" style="max-width:100%;">

## App in action

Swipe                    | Drag
:-------------------------:|:-------------------------:
![ezgif com-crop](https://user-images.githubusercontent.com/39665412/50549419-899eee00-0c97-11e9-9333-279aa9f3ae24.gif) |![drag-example-gif](https://user-images.githubusercontent.com/39665412/50563495-3e004900-0d58-11e9-9ef4-e4b6f54e7043.gif)
Swipe left to delete, swipe right to archive | Drag recyclerview item up or down to rearrange

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

Create a class which extends `ItemTouchHelper.SimpleCallback` and using the observer pattern, create a interface so that the recyclerview can listen to when `on the item has been swiped off the screen.

You need to override the following methods:

1. `onMove(...)` - simply return `false` since we disable dragging

2. `onSelectedChanged(...)`

3. `onChildDraw(...)` - Customise how the background is drawn depending on which direction the item is being swiped. If you only want to allow swiping in one direction, simply hardcode the background color and icon in xml and don't include the if-block.

4. `clearView(...)`  

5. `onSwiped(...)` - Called after the item is swiped off the screen, so code what will happen after that here

6. `isLongPressDragEnabled()` - Return `false` to disable dragging

7. `isItemViewSwipeEnabled()` - Return `true` to allow item to be swiped

#### Fragment/Activity

[_See code here_](https://github.com/wRorsjakz/Android-SwipeAndDrag/blob/master/app/src/main/java/com/example/user/swipeanddrag/Swipe/SwipeFragment.java)

Have the class implement your `ItemTouchHelper` interface and override `onSwiped(...)`. If the user can swipe in both direction, check which direction the user has swiped using `ItemTouchHelper.LEFT` or `ItemTouchHelper.RIGHT`.

In my example, I temperory store the deleted item before calling `removeItem(int position)` and showing a `Snackbar`. Use `addItem(int position, Contact contact)` to add the recently deleted item back.

Finally, attach the listener to the recyclerview using `attachToRecyclerView()`. Note that `0` since we have disabled dragging. Enable swiping direction by passing the appropriate params `ItemTouchHelper.DIRECTION`.

```java
//Swping in both direction is enabled
ItemTouchHelper.SimpleCallback simpleCallback = new SwipeRVTouchHelper(this,0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);
```

### Drag-and-drop

#### Layout 

[_See code here_](https://github.com/wRorsjakz/Android-SwipeAndDrag/blob/master/app/src/main/res/layout/drag_item_container.xml)

Unlike the swipe-to-delete item above which has two seperate layouts for the foreground and background, you only need one this time.

In order to give the individual recyclerview item a ripple effect after long-pressed, ensure to add the following attributes to the root layout

```xml
android:clickable="true"
android:focusable="true"
android:foreground="?android:attr/selectableItemBackground"
```

#### ItemTouchHelper

[_See code here_](https://github.com/wRorsjakz/Android-SwipeAndDrag/blob/master/app/src/main/java/com/example/user/swipeanddrag/Drag/DragRVTouchHelper.java)

Create a class which extends `ItemTouchHelper.SimpleCallback` and using the observer pattern, create a interface so that the fragment/activity can listen to when `onMove(...)` is called, which is when the item is being dragged.

Override the following methods:

1. `onSelectedChanged(...)`
2. `onSwiped(...)` - Do nothing since swiping is disabled
3. `onMove(...)` - Called when an item is being moved. Our fragment/activity will implement this method
4. `isLongPressDragEnabled()` - Return `true` to enable an item to drag and drop after long pressed
5. `isItemViewSwipeEnabled()` - Return `false` to prevent item from being able to swipe

#### Fragment/Activity

[_See code here_](https://github.com/wRorsjakz/Android-SwipeAndDrag/blob/master/app/src/main/java/com/example/user/swipeanddrag/Drag/DragFragment.java)

Have the class implement your `ItemTouchHelper` interface and override `onMove(int originalPos, int newPos)`. We have to check if the user is dragging the recyclerview item up or down, and then swap the object in the `ArrayList` before calling `notifyItemMoved(...)`.

```java
@Override
    public void onMove(int originalPos, int newPos) {

        if (originalPos < newPos) {
            //Drag item downwards
            for (int i = originalPos; i < newPos; i++) {
                Collections.swap(contactArrayList, i, i + 1);
                adapter.notifyItemMoved(i, i + 1);
            }
        } else if (originalPos > newPos) {
            //User drag up
            for (int i = originalPos; i > newPos; i--) {
                Collections.swap(contactArrayList, i, i - 1);
                adapter.notifyItemMoved(i, i - 1);
            }
        }
    }
```

Finally, attach the listener to the recyclerview using `attachToRecyclerView()`. Note that we pass `0` since that swipping is disabled. Also pass the appropriate params to enable dragging in both directions.

```java
ItemTouchHelper.SimpleCallback simpleCallback = new DragRVTouchHelper(this, ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0);
new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

```

## Dependencies/Library

Project has been migrated to AndroidX

1. [JSONPlaceholder](https://jsonplaceholder.typicode.com/) - Fake Online REST API for Testing and Prototyping, retrieved [`/users`](https://jsonplaceholder.typicode.com/users) resource

2. Design Support Library

3. RecyclerView

4. Volley

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