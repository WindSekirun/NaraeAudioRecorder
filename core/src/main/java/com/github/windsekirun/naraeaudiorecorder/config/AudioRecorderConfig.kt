package com.github.windsekirun.naraeaudiorecorder.config

import com.github.windsekirun.naraeaudiorecorder.constants.LogConstants
import com.github.windsekirun.naraeaudiorecorder.listener.OnChunkAvailableListener
import com.github.windsekirun.naraeaudiorecorder.listener.OnSilentDetectedListener
import com.github.windsekirun.naraeaudiorecorder.listener.OnTimerCountListener
import com.github.windsekirun.naraeaudiorecorder.source.AudioSource
import com.github.windsekirun.naraeaudiorecorder.source.DefaultAudioSource
import java.io.File

/**
 * Config file of [com.github.windsekirun.naraeaudiorecorder.NaraeAudioRecorder]
 */
class AudioRecorderConfig {
    var destFile: File? = null
    var recordConfig: AudioRecordConfig = AudioRecordConfig.defaultConfig()
    var audioSource : AudioSource = DefaultAudioSource(recordConfig)
    var chunkAvailableListener: OnChunkAvailableListener? = null
    var silentDetectedListener: OnSilentDetectedListener? = null
    var timerCountListener: OnTimerCountListener? = null
    var maxAvailableMillis: Long = -1L
    var refreshTimerMillis: Long = 50

    fun check(): Boolean {
        if (destFile == null) {
            throw NullPointerException(LogConstants.EXCEPTION_DEST_FILE_NOT_ASSIGNED)
        }

        return true
    }
}
