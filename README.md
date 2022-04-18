# SimpleML

## Simplifies machine learning integration by wrapping multiple analysis methods into one easy to use interface

Machine Learning integrations can be difficult and time consuming to set up, with a lot of boiler plate code and domain-specific knowledge required. With SimpleML you just need to choose what you want to detect, and detect it.

## Installation

Add to your `build.gradle`:

```groovy
    implementation 'com.jakebarnby:simpleml:simpleml-objects:1.1.0-beta01'
    implementation 'com.jakebarnby:simpleml:simpleml-text:1.1.0-beta01'
    implementation 'com.jakebarnby:simpleml:simpleml-poses:1.1.0-beta01'
```

## Getting started

You can choose to detect:

- Objects
- Text
- Poses

There is a separate dependency for each so you can include one or all depending on your needs.

## Roadmap

- Tensorflow Lite Integration
- TensorflowLite Tasks Integration
- Custom model support

## Usage

There are 3 easy ways to use SimpleML.

1. As a `View`

Add the view to your xml (all simpleml attributes are optional).

```xml
    <com.jakebarnby.simpleml.objects.view.LocalObjectAnalyzerView
        android:id="@+id/view"
        android:layout_width="200dp"
        android:layout_height="200dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:simpleml_analysisLocation="device"
        app:simpleml_classificationEnabled="true"
        app:simpleml_detectMultiple="true"
        app:simpleml_detectionDispatcher="io"
        app:simpleml_detectorMode="frame_stream"
        app:simpleml_minimumConfidence="0.7" />
```

Attach a listener to the detector

```kotlin
    val objectAnalyzerView = findViewById<LocalObjectAnalyzerView>(R.id.view)

    objectAnalyzerView.setOnNextDetectionListener { results: List<DetectedObject> ->
    }
```

2. As a `Fragment`

```kotlin
    val objectAnalyzerFragment = ObjectAnalyzerFragment.newInstance { results: List<DetectedObject> ->
    }
```

3. As an `Activity`

```kotlin
    ObjectDetector().stream(this) { results: List<DetectedObject> -> }

    PoseDetector().stream(this) { results: List<DetectedPose> -> }

    TextDetector().stream(this) { result: DetectedText -> }
```

## Models

Each detectable type has a corresponding result model type that is returned from detection

### Objects

```kotlin
class DetectedObject(
    // Pairs of label to confidence
    var labels: List<Pair<String, Float>> = listOf(),
    
    // Detected objects position
    var boundingBox: Rect?  = null,
)
```

### Text

```kotlin
class DetectedText(
    // The raw detected text
    var text: String? = null,

    // The languages of detected texts
    var detectedLanguages: List<String>? = null,

    // The detected text as boxes(lines(words))
    var textBoxes: List<TextBox>? = null,
)
```

### Poses

```kotlin
class DetectedPose(
    // The detected landmark
    var landmark: PoseLandmark? = null,

    // The position of the detected landmark
    var position: PointF? = null,
    
    // Confidence that the landmark is in frame
    var inFrameLikelihood: Float? = null,
)
```

## Configuration

Each detectable type has a corresponding `Options` type extending an `OptionsBase` that can be used to configure detection parameters. All option sets are pre-configured with sane defaults so you can safely ignore them if you want to detect and forget.

### Shared Options

```kotlin
open class OptionsBase(
    val analysisType: AnalysisType,
    val analysisMode: AnalysisMode             = AnalysisMode.FRAME_STREAM,
    val analysisDispatcher: AnalysisDispatcher = AnalysisDispatcher.IO,
    val analysisLocation: AnalysisLocation     = AnalysisLocation.DEVICE,
) : Serializable
```

### Object Options

```kotlin
class ObjectOptions(
    val minimumConfidence: Float            = 0.5f,
    val classificationEnabled: Boolean      = true,
    val detectMultiple: Boolean             = true,
    analysisMode: AnalysisMode              = AnalysisMode.FRAME_STREAM,
    analysisDispatcher: AnalysisDispatcher  = AnalysisDispatcher.IO,
    analysisLocation: AnalysisLocation      = AnalysisLocation.DEVICE
) : OptionsBase(
    AnalysisType.OBJECT,
    analysisMode,
    analysisDispatcher,
    analysisLocation
)
```

### Text Options

```kotlin
class TextOptions(
    val minimumConfidence: Float            = 0.5f,
    analysisMode: AnalysisMode              = AnalysisMode.FRAME_STREAM,
    analysisDispatcher: AnalysisDispatcher  = AnalysisDispatcher.IO,
    analysisLocation: AnalysisLocation      = AnalysisLocation.DEVICE
) : OptionsBase(
    AnalysisType.TEXT,
    analysisMode,
    analysisDispatcher,
    analysisLocation
)
```

### Pose Options

```kotlin
class PoseOptions(
    analysisMode: AnalysisMode              = AnalysisMode.FRAME_STREAM,
    analysisDispatcher: AnalysisDispatcher  = AnalysisDispatcher.IO,
    analysisLocation: AnalysisLocation      = AnalysisLocation.DEVICE
) : OptionsBase(
    AnalysisType.POSE,
    analysisMode,
    analysisDispatcher,
    analysisLocation
)
```

## Development

SimpleML is built with extension in mind, and as such makes heavy use of generics. All functionality is extensible via the included base classes.

```kotlin
class YourAnalyzer: Analyzer<TDetector, TOptions, TInput, TResult>
```

```kotlin
class YourView: Camera2View<
    TDetector, 
    TOptions, 
    TInput, 
    TResult, 
    TOutResult>
```

```kotlin
class YourFragment: Camera2Fragment<CustomAnalyzer,
     TDetector,
     TOptions,
     TInput,
     TResult,
     TOutResult>
```
