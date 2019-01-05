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
    fun getAudioRecord() : AudioRecord

    /**
     * Instance of [AudioRecordConfig]
     */
    fun getAudioConfig() : AudioRecordConfig

    /**
     * Buffer size to chunk raw data from [AudioRecord]
     */
    fun getBufferSize(): Int
}