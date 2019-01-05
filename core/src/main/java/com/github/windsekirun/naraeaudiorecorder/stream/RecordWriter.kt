package com.github.windsekirun.naraeaudiorecorder.stream

import com.github.windsekirun.naraeaudiorecorder.source.AudioSource
import java.io.OutputStream

/**
 * Record control for pulling from [android.media.AudioRecord] and write to [OutputStream]
 */
interface RecordWriter {

    /**
     * Start recording feature using [getAudioSource]
     *
     * Basically, it pull from [android.media.AudioRecord] and write to [OutputStream]
     */
    fun startRecording(outputStream: OutputStream)

    /**
     * Stop recording feature
     */
    fun stopRecording()

    /**
     * get [AudioSource] which is used for pulling from [android.media.AudioRecord]
     */
    fun getAudioSource(): AudioSource
}