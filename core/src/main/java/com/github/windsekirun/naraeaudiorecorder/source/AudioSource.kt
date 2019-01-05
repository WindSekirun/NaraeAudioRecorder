package com.github.windsekirun.naraeaudiorecorder.source

import android.media.AudioRecord
import com.github.windsekirun.naraeaudiorecorder.config.AudioRecordConfig

/**
 * Source file which contains instance of [AudioRecord] and [AudioRecordConfig]
 *
 * All of recording feature need to implement this methods.
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
     * Buffer size to chunk ByteArray from [AudioRecord]
     */
    fun getBufferSize(): Int
}