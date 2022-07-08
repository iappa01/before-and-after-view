# before-and-after-view
An android library to create BeforeAfter custom view.</br>
**BeforeAfter** view is used to see the difference of two picture.
***

# Realeases:
 Current release: 1.0.4 </br>
 You can see all the library releases [here](https://github.com/iappa01/before-and-after-view/releases).
***

# Download and installation
### Step 1: Add jitpack into repositories:
 Add "<code> maven { url 'https://jitpack.io'} </code> " into *repositories* of *dependencyResolutionManagement* in file *setting.gradle* 

 
``` Gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        ...
        maven { url 'https://jitpack.io'}
    }
}
```

---

### Step 2: Add implementation into dependences:
``` Gradle
implementation 'com.github.iappa01:before-and-after-view:1.0.4'
```

---
# How do I use **BeforeAfter** view

In XML, this view's basic fundamental:</br>

``` xml
    <com.mobiai.views.beforeafter.BeforeAfter
        android:id="@+id/before_after"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:background_slider_thumb="@drawable/ic_thumb"
        />
```

Connect in Java class:
``` java
    // create bitmap from drawable image. 
    Bitmap beforeImage = BitmapFactory.decodeResource(getResources(),R.drawable.before);
    Bitmap afterImage = BitmapFactory.decodeResource(getResources(),R.drawable.after);
    // Connect to BeforeAfter view
    BeforeAfter ba = findViewById(R.id.before_after);
    // Set before image
    ba.setBeforeImage(beforeImage);
    // Set after image
    ba.setAfterImage(afterImage);
```
