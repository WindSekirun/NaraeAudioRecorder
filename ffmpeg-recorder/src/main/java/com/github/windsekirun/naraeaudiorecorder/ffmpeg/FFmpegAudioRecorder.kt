package com.github.windsekirun.naraeaudiorecorder.ffmpeg

import android.content.Context
import android.util.Log
import com.github.windsekirun.naraeaudiorecorder.constants.LogConstants
import com.github.windsekirun.naraeaudiorecorder.extensions.runOnUiThread
import com.github.windsekirun.naraeaudiorecorder.ffmpeg.config.FFmpegConvertConfig
import com.github.windsekirun.naraeaudiorecorder.ffmpeg.extensions.weak
import com.github.windsekirun.naraeaudiorecorder.ffmpeg.listener.OnConvertStateChangeListener
import com.github.windsekirun.naraeaudiorecorder.ffmpeg.model.FFmpegBitRate
import com.github.windsekirun.naraeaudiorecorder.ffmpeg.model.FFmpegConvertState
import com.github.windsekirun.naraeaudiorecorder.ffmpeg.model.FFmpegSamplingRate
import com.github.windsekirun.naraeaudiorecorder.recorder.WavAudioRecorder
import com.github.windsekirun.naraeaudiorecorder.writer.RecordWriter
import nl.bravobit.ffmpeg.ExecuteBinaryResponseHandler
import nl.bravobit.ffmpeg.FFmpeg
import java.io.File

/**
 * [com.github.windsekirun.naraeaudiorecorder.recorder.AudioRecorder] for record audio and save in wav and convert them by FFmpeg.
 */
open class FFmpegAudioRecorder(file: File, recordWriter: RecordWriter) : WavAudioRecorder(file, recordWriter) {
    private lateinit var destFile: File
    private var _context: Context? by weak(null)
    private var convertStateChangeListener: OnConvertStateChangeListener? = null
    private var convertConfig: FFmpegConvertConfig = FFmpegConvertConfig.defaultConfig()

    override fun startRecording() {
        if (_context == null) {
            throw NullPointerException(LogConstants.EXCEPTION_CONTEXT_NOT_ASSIGNED)
        }

        if (!FFmpeg.getInstance(_context).isSupported) {
            throw NullPointerException(LogConstants.EXCEPTION_FFMPEG_NOT_SUPPORTED)
        }

        super.startRecording()
    }

    override fun stopRecording() {
        super.stopRecording()
        convert()
    }

    /**
     * set [Context] to [FFmpegAudioRecorder]
     */
    fun setContext(context: Context) {
        this._context = context
    }

    /**
     * set [OnConvertStateChangeListener] to handle convert state of FFmpeg
     */
    fun setOnConvertStateChangeListener(listener: OnConvertStateChangeListener) {
        this.convertStateChangeListener = listener
    }

    /**
     * Kotlin-compatible version of [setOnConvertStateChangeListener]
     */
    fun setOnConvertStateChangeListener(callback: (FFmpegConvertState) -> Unit) {
        this.convertStateChangeListener = object : OnConvertStateChangeListener {
            override fun onState(state: FFmpegConvertState) {
                callback.invoke(state)
            }
        }
    }

    /**
     * set [FFmpegConvertConfig] to change encoding options of FFmpeg
     */
    fun setConvertConfig(convertConfig: FFmpegConvertConfig) {
        this.convertConfig = convertConfig
    }

    private fun convert() {
        val commandBuilder = mutableListOf<String>()
        val destFile = File(file.absolutePath)
        val tempFile = File(file.parent, "tmp-${file.name}")

        commandBuilder.addAll(listOf("-y", "-i", file.path))

        if (convertConfig.samplingRate != FFmpegSamplingRate.ORIGINAL) {
            commandBuilder.addAll(listOf("-ar", convertConfig.samplingRate.samplingRate.toString()))
        }

        if (convertConfig.bitRate !== FFmpegBitRate.def) {
            commandBuilder.addAll(listOf("-sample_fmt", convertConfig.bitRate.bitRate))
        }

        if (convertConfig.mono) {
            commandBuilder.addAll(listOf("-ac", "1"))
        }

        commandBuilder.add(tempFile.path)

        val cmd = commandBuilder.toTypedArray()

        try {
            FFmpeg.getInstance(_context).execute(cmd, object : ExecuteBinaryResponseHandler() {
                override fun onStart() {
                    super.onStart()
                    runOnUiThread { convertStateChangeListener?.onState(FFmpegConvertState.START) }
                }

                override fun onSuccess(message: String?) {
                    super.onSuccess(message)

                    file.delete()
                    tempFile.renameTo(destFile)

                    runOnUiThread { convertStateChangeListener?.onState(FFmpegConvertState.SUCCESS) }
                }

                override fun onFailure(message: String?) {
                    super.onFailure(message)
                    Log.e(LogConstants.TAG, message ?: "Failed")
                    runOnUiThread { convertStateChangeListener?.onState(FFmpegConvertState.ERROR) }
                }
            })
        } catch (ex: Exception) {
            Log.e(LogConstants.TAG, ex.message ?: "", ex)
            runOnUiThread { convertStateChangeListener?.onState(FFmpegConvertState.ERROR) }
        }
    }
}