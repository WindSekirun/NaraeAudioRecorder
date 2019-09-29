# NaraeAudioRecorder

[ ![Download](https://api.bintray.com/packages/windsekirun/maven/NaraeAudioRecorder-Core/images/download.svg) ](https://bintray.com/windsekirun/maven/NaraeAudioRecorder-Core/_latestVersion) [![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-NaraeAudioRecorder-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/7436)

AudioRecorder for Android powered by Kotlin.

[<img src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" width="200">](https://play.google.com/store/apps/details?id=com.github.windsekirun.naraeaudiorecorder.sample)

## Key Features

* Easy to use API 
* Record on various format
  * core: pcm, wav
  * ffmpeg-recorder: mp3, m4a, wma, flac, aac
* Pause & Resume recording
* Integrated timer is provide `maxAvailableTime` feature
* Remove background noise using [NoiseSuppressor](https://developer.android.com/reference/android/media/audiofx/NoiseSuppressor) API
* 100% write in Kotlin, but has Java Compatible
* Detect amount of time of Silent

## Import

It distributed on JCenter, but sometime it's not available.

```
repositories {
    maven { url "https://dl.bintray.com/windsekirun/maven/" }
}
```

### Core

Core module will contain most feature and two format. 'pcm' and 'wav'

```
implementation 'com.github.WindSekirun.NaraeAudioRecorder:core:1.1.0'
```

### FFmpeg-Recorder

FFmpeg-Recorder will contain five format, 'mp3', 'm4a', 'wma', 'flac', 'aac'

This module will increase Final APK Size about 20MB. You can reduce APK Size by [this guide](https://github.com/bravobit/FFmpeg-Android/wiki/Reduce-APK-File-Size)

```
implementation 'nl.bravobit:android-ffmpeg:1.1.5'
implementation 'com.github.WindSekirun.NaraeAudioRecorder:ffmpeg-recorder:1.1.0'
```

## Usages

### Core

#### Initialization

```kotlin
 val audioRecorder = NaraeAudioRecorder()
 val destFile = File(Environment.getExternalStorageDirectory(), "/NaraeAudioRecorder/$fileName$extensions")
 audioRecorder.create() {
            this.destFile = destFile
 }
```

Define instance of 'NaraeAudioRecorder' and create file and provide with `NaraeAudioRecorder.create` is fine.

If you want use NoiseSuppressor, define instance of `NoiseAudioSource` and provide with `NaraeAudioRecorder.create`.

```kotlin
val recordConfig = AudioRecordConfig.defaultConfig()
val audioSource = NoiseAudioSource(recordConfig)
audioRecorder.create() {
          this.destFile = this.destFile
          this.recordConfig = recordConfig
          this.audioSource = audioSource
}
```

#### Initialization in Java
This is full sample of 'initialization in Java'. other feature is same.

```java
File destFile = new File(getContext().getExternalFilesDir(null) + String.format("/recorder/%s.mp3", fileName));
destFile.getParentFile().mkdirs();

mAudioRecorder.checkPermission(getContext());
mAudioRecorder.create(FFmpegRecordFinder.class, config -> {
    config.setDestFile(destFile);
    config.setDebugMode(true);
    config.setTimerCountListener((currentTime, maxTime) -> { 
    });
            
    return null;
});

FFmpegAudioRecorder ffmpegAudioRecorder = (FFmpegAudioRecorder) mAudioRecorder.getAudioRecorder();
ffmpegAudioRecorder.setContext(getContext());
ffmpegAudioRecorder.setOnConvertStateChangeListener(state -> {
    if (state == FFmpegConvertState.SUCCESS) {
        mRecordedPath = destFile.getPath();
    }
});
```

#### Permission

Starting from 1.2.0, if permission isn't granted, NaraeAudioRecorder will throw RuntimeException.

* RECORD_AUDIO
* WRITE_EXTERNAL_STORAGE
* READ_EXTERNAL_STORAGE

So make sure grant proper permissions **before using `startRecording` function.**

#### Start / Stop / Pause / Resume

````kotlin
audioRecorder.startRecording(Context)
audioRecorder.stopRecording()
audioRecorder.pauseRecording()
audioRecorder.resumeRecording()
````

#### Custom config of AudioRecord

You can custom config of `AudioRecord` with `AudioRecordConfig`

```kotlin
fun defaultConfig() = AudioRecordConfig(MediaRecorder.AudioSource.MIC,
                AudioFormat.ENCODING_PCM_16BIT,
                AudioFormat.CHANNEL_IN_MONO,
                AudioConstants.FREQUENCY_44100)
```

#### Listen about state changes of record

```kotlin
audioRecorder.setOnRecordStateChangeListener(OnRecordStateChangeListener)
```

#### Set maxAvailableTime & Listen about changes of timer

```kotlin
audioRecorder.create(FFmpegRecordFinder::class.java) {
          ...
          this.maxAvailableMillis = TimeUnit.SECONDS.toMillis(20)
          this.timerCountListener = { currentTime, maxTime -> }
}
```

If you want use accuracy, define `refreshTimerMillis` in `create` section will be help for you. Default is `50`.

### FFmpeg-recorder

Using FFmpeg-recorder will need some additional info.

```kotlin
audioRecorder.create(FFmpegRecordFinder::class.java) {
            this.destFile = this.destFile
            this.recordConfig = recordConfig
            this.audioSource = audioSource
}

val ffmpegAudioRecorder: FFmpegAudioRecorder = audioRecorder.getAudioRecorder() as? FFmpegAudioRecorder ?: return
ffmpegAudioRecorder.setContext(this)
```

In parameters of `create`, use `FFmpegRecordFinder::class.java` to determine proper recorder  with `destFile` .

FFmpeg-recorder uses `WavAudioRecorder` internally, recording in wav and convert them to desire format using FFmpeg command.

#### Change config of FFmpeg

You can custom config of FFmpeg using this options.

```kotlin
ffmpegAudioRecorder.setConvertConfig(FFmpegConvertConfig)
```

Default value is ```fun defaultConfig() = FFmpegConvertConfig(bitRate = FFmpegBitRate.def, samplingRate = FFmpegSamplingRate.ORIGINAL, mono = true)```. 

#### Listen about state changes of convert

```kotlin
ffmpegAudioRecorder.setOnConvertStateChangeListener(OnConvertStateChangeListener)
```

### Sample Application

[Code](https://github.com/WindSekirun/NaraeAudioRecorder/blob/master/sample/src/main/java/com/github/windsekirun/naraeaudiorecorder/sample/MainActivity.kt)

## License

### Core

```
Copyright 2019, WindSekirun (DongGil, Seo)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

### FFmpeg-recorder

* [FFmpeg-Android License](https://github.com/bravobit/FFmpeg-Android/blob/master/LICENSE)
* [FFmpeg License](https://www.ffmpeg.org/legal.html)
