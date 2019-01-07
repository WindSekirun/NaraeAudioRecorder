package com.github.windsekirun.naraeaudiorecorder.source

import android.media.AudioRecord
import com.github.windsekirun.naraeaudiorecorder.config.AudioRecordConfig

/**
 * Source file which contains instance of [AudioRecord] and [AudioRecordConfig]
 */
interface AudioSource {
    /**
     * Instance of [AudioRecord] for recording audio
     */
    fun getAudioRecord(): AudioRecord

    /**
     * Instance of [AudioRecordConfig]
     */
    fun getAudioConfig(): AudioRecordConfig

    /**
     * Buffer size to chunk raw data from [AudioRecord]
     */
    fun getBufferSize(): Int

    /**
     * get Flag when record is available
     */
    fun isRecordAvailable(): Boolean

    /**
     * set Flag which record is available
     */
    fun setRecordAvailable(available: Boolean): AudioSource

    /**
     * Pre-process [AudioRecord] for start to recording.
     */
    fun preProcessAudioRecord(): AudioRecord
}