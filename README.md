#Image Classifier

Currently supports classification using either Firebase, Tensorflow Lite, or Tensorflow Lite Tasks.

```kotlin
ImageClassifier.Builder(this)
  .setClassifierType(ClassifierType.TENSORFLOW)
  .setModelUrl("https://github.com/googlecodelabs/tensorflow-for-poets-2/raw/master/android/tflite/app/src/main/assets/graph.lite")
  .setModelUrl("graph.lite") //Sourced from "assets" directory
  .setLabelUrl("labels.txt") //Sourced from "assets" directory
  .setMinimumConfidence(0.7f)
  .start()
```

```kotlin
ImageClassifier.Builder(this)
  .setClassifierType(ClassifierType.FIREBASE_CLOUD)
  .setMinimumConfidence(0.7f)
  .start()
```