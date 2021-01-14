# SimpleML
### Simplifies machine learning by combining multiple analysis methods into one easy to use interface.

Using ML can be a pain in the ass to set up, with a lot of boiler plate and additional camera code required. With SimpleML you just need to choose what you want to detect, and detect it.

## Support
Currently supported kits:
- Google ML Kit
- Firebase Vision ML

Support planned for:
- TensorflowLite Tasks
- TensorflowLite with custom model
- Firebase Vision with custom model
- Google ML kit with custom model

## Getting started
You can choose to detect:
- Objects
- Text
- Poses

Where each type has it's own module.
To use a module, add one of the following to your modules `build.gradle`:

```groovy
  dependencies {
    // ...
    implementation 'com.jakebarnby:simpleml:simpleml-objects:<version>'
    implementation 'com.jakebarnby:simpleml:simpleml-text:<version>'
    implementation 'com.jakebarnby:simpleml:simpleml-poses:<version>'
    // ...
  }
```

## How to use

There are 3 main ways to use SimpleML:

1. View
1. Add the view to your xml (all simpleml attributes are optional).
```xml
    <com.jakebarnby.simpleml.objects.view.LocalObjectAnalyzerView
        android:id="@+id/objectAnalyzerView"
        android:layout_width="200dp"
        android:layout_height="200dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:simpleml_analysisLocation="device"
        app:simpleml_classificationEnabled="true"
        app:simpleml_detectMultiple="true"
        app:simpleml_detectionDispatcher="IO"
        app:simpleml_detectorMode="frame_stream"
        app:simpleml_minimumConfidence="0.7" />
```
2. Attach a listener to the detector
```kotlin
    val objectAnalyzerView = findViewById<LocalObjectAnalyzerView>(R.id.objectAnalyzerView)

    objectAnalyzerView.setOnNextDetectionListener { results: List<DetectedObject> ->
        // Use the results
    }
```


2. Fragment
```kotlin
    val objectAnalyzerFragment = ObjectAnalyzerFragment.newInstance({ results: List<DetectedObject> ->
       // Use the results
    }, ObjectOptions(
        minimumConfidence = 0.7f,
        classificationEnabled = true,
        detectMultiple = true,
        analysisMode = AnalysisMode.FRAME_STREAM,
        analysisDispatcher = AnalysisDispatcher.IO,
        analysisLocation = AnalysisLocation.DEVICE
    ))
    
    val poseAnalyzerFragment = PoseAnalyzerFragment.newInstance { results: List<DetectedPose> ->
        // Use the results
    }
    
    val textAnalyzerFragment = TextAnalyzerFragment.newInstance { result: DetectedText ->
        Log.e(javaClass.name, result.text ?: "")
    }
    
    supportFragmentManager.beginTransaction()
        .setReorderingAllowed(true)
        .add(R.id.fragment_container_view, objectAnalyzerFragment)
        .commit()
```


3. Activity

While easy this method has limited customizability, showing only a full screen camera for detection.
```kotlin
    ObjectDetector().detectObjects(this) { results: List<DetectedObject> ->
        // Use the results
    }
    
    PoseDetector().detectPoses(this) { results: List<DetectedPose> ->
        // Use the results
    }
    
    TextDetector().detectTexts(this) { result: DetectedText ->
        // Use the results
    }
```
