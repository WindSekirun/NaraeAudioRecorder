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
import com.github.windsekirun.naraeaudiorecorder.stream.RecordWriter
import nl.bravobit.ffmpeg.ExecuteBinaryResponseHandler
import nl.bravobit.ffmpeg.FFmpeg
import java.io.File

/**
 * [com.github.windsekirun.naraeaudiorecorder.recorder.AudioRecorder] for record audio and save in wav and convert them by FFmpeg.
 */
open class FFmpegAudioRecorder(file: File, recordWriter: RecordWriter) : WavAudioRecorder(file, recordWriter) {
    private lateinit var destFile: File
    private var context: Context? by weak(null)
    private var convertStateChangeListener: OnConvertStateChangeListener? = null
    private var convertConfig: FFmpegConvertConfig = FFmpegConvertConfig.defaultConfig()

    override fun startRecording() {
        if (context == null) {
            throw NullPointerException(LogConstants.EXCEPTION_CONTEXT_NOT_ASSIGNED)
        }

        if (!FFmpeg.getInstance(context).isSupported) {
            throw NullPointerException(LogConstants.EXCEPTION_FFMPEG_NOT_SUPPORTED)
        }

        if (!::destFile.isInitialized) {
            throw NullPointerException(LogConstants.EXCEPTION_DEST_FILE_NOT_ASSIGNED)
        }

        super.startRecording()
    }

    override fun stopRecording() {
        super.stopRecording()
        convert()
    }

    /**
     * set [destFile], [context] to [FFmpegAudioRecorder]
     */
    fun setDestFile(context: Context, destFile: File) {
        this.context = context
        this.destFile = destFile
    }

    /**
     * set [OnConvertStateChangeListener] to handle convert state of FFmpeg
     */
    fun setOnConvertStateChangeListener(listener: OnConvertStateChangeListener) {
        this.convertStateChangeListener = listener
    }

    /**
     * set [FFmpegConvertConfig] to change encoding options of FFmpeg
     */
    fun setConvertConfig(convertConfig: FFmpegConvertConfig) {
        this.convertConfig = convertConfig
    }

    private fun convert() {
        val commandBuilder = mutableListOf<String>()

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

        commandBuilder.add(destFile.path)

        val cmd = commandBuilder.toTypedArray()

        try {
            FFmpeg.getInstance(context).execute(cmd, object: ExecuteBinaryResponseHandler() {
                override fun onStart() {
                    super.onStart()
                    runOnUiThread { convertStateChangeListener?.onState(FFmpegConvertState.START) }
                }

                override fun onSuccess(message: String?) {
                    super.onSuccess(message)
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