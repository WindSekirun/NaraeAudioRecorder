package com.github.windsekirun.naraeaudiorecorder.source

import android.media.AudioRecord
import com.github.windsekirun.naraeaudiorecorder.config.AudioRecordConfig

/**
 * Default settings of [AudioSource]
 *
 * @param audioRecordConfig optional, [AudioRecordConfig] instance for configure [AudioRecord]
 */
open class DefaultAudioSource(var audioRecordConfig: AudioRecordConfig = AudioRecordConfig.defaultConfig())
    : AudioSource {

    /**
     * backing property name for [getBufferSize]
     */
    private val _bufferSize: Int by lazy {
        AudioRecord.getMinBufferSize(
                audioRecordConfig.frequency,
                audioRecordConfig.channel, audioRecordConfig.audioEncoding
        )
    }

    /**
     * backing property name for [getAudioRecord]
     */
    private val _audioRecord: AudioRecord by lazy {
        AudioRecord(
                audioRecordConfig.audioSource,
                audioRecordConfig.frequency,
                audioRecordConfig.channel,
                audioRecordConfig.audioEncoding,
                getBufferSize()
        )
    }

    private var _recordAvailable: Boolean = false

    /**
     * see [AudioSource.getAudioRecord]
     */
    override fun getAudioRecord(): AudioRecord = _audioRecord

    /**
     * see [AudioSource.getAudioConfig]
     */
    override fun getAudioConfig(): AudioRecordConfig = audioRecordConfig

    /**
     * see [AudioSource.getBufferSize]
     */
    override fun getBufferSize(): Int = _bufferSize

    /**
     * see [AudioSource.isRecordAvailable]
     */
    override fun isRecordAvailable(): Boolean = _recordAvailable

    /**
     * see [AudioSource.setRecordAvailable]
     */
    override fun setRecordAvailable(available: Boolean): AudioSource = this.apply { _recordAvailable = available }

    /**
     * Pre-process [AudioRecord] for start to recording.
     */
    override fun preProcessAudioRecord(): AudioRecord {
        return getAudioRecord().apply {
            startRecording()
            this@DefaultAudioSource.setRecordAvailable(true)
        }
    }
}