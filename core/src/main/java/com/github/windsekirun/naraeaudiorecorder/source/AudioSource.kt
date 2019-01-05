package com.github.windsekirun.naraeaudiorecorder.source

import android.media.AudioRecord
import com.github.windsekirun.naraeaudiorecorder.config.AudioRecordConfig

/**
 * Source file which contains instance of [AudioRecord] and [AudioRecordConfig]
 *
 * All of recording feature need to implement this methods.
 */
interface AudioSource {
    fun getAudioRecord() : AudioRecord

    fun getAudioConfig() : AudioRecordConfig

    fun getMinimumBufferSize(): Int
}