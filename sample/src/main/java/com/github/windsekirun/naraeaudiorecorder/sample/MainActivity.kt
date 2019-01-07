package com.github.windsekirun.naraeaudiorecorder.sample

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ObservableInt
import com.github.windsekirun.naraeaudiorecorder.NaraeAudioRecorder
import com.github.windsekirun.naraeaudiorecorder.chunk.AudioChunk
import com.github.windsekirun.naraeaudiorecorder.config.AudioRecordConfig
import com.github.windsekirun.naraeaudiorecorder.ffmpeg.FFmpegAudioRecorder
import com.github.windsekirun.naraeaudiorecorder.ffmpeg.FFmpegRecordFinder
import com.github.windsekirun.naraeaudiorecorder.ffmpeg.model.FFmpegConvertState
import com.github.windsekirun.naraeaudiorecorder.model.RecordMetadata
import com.github.windsekirun.naraeaudiorecorder.model.RecordState
import com.github.windsekirun.naraeaudiorecorder.sample.databinding.MainActivityBinding
import com.github.windsekirun.naraeaudiorecorder.source.DefaultAudioSource
import com.github.windsekirun.naraeaudiorecorder.source.NoiseAudioSource
import pyxis.uzuki.live.richutilskt.utils.asDateString
import pyxis.uzuki.live.richutilskt.utils.isEmpty
import pyxis.uzuki.live.richutilskt.utils.toast
import java.io.File

class MainActivity : AppCompatActivity() {
    val sourceCheckedPosition = ObservableInt(0)
    val recorderCheckedPosition = ObservableInt(0)

    private var recordInitialized: Boolean = false
    private var recordMetadata: RecordMetadata? = null

    private val mediaPlayer = MediaPlayer()
    private val audioRecorder = NaraeAudioRecorder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = MainActivityBinding.inflate(LayoutInflater.from(this)).apply {
            activity = this@MainActivity
        }
        setContentView(binding.root)

        audioRecorder.checkPermission(this)
    }

    fun clickStart(view: View) {
        val extensions = when (recorderCheckedPosition.get()) {
            0 -> ".pcm"
            1 -> ".wav"
            2 -> ".mp3"
            3 -> ".aac"
            4 -> ".m4a"
            5 -> ".wma"
            6 -> ".flac"
            else -> ""
        }
        if (extensions.isEmpty()) return

        val ffmpegMode = recorderCheckedPosition.get() >= 2
        val fileName = System.currentTimeMillis().asDateString()
        val destFile = File(getExternalFilesDir(null), "/$fileName$extensions")
        destFile.parentFile.mkdir()

        val audioSource = when (sourceCheckedPosition.get()) {
            1 -> NoiseAudioSource(AudioRecordConfig.defaultConfig())
            else -> DefaultAudioSource(AudioRecordConfig.defaultConfig())
        }

        audioRecorder.create(FFmpegRecordFinder::class.java) {
            this.destFile = destFile
            this.audioSource = audioSource
            this.chunkAvailableCallback = { chunkAvailable(it) }
            this.silentDetectedCallback = { silentDetected(it) }
            this.timerCountCallback = { current, max -> timerChanged(current, max) }
            this.debugMode = true
        }

        if (ffmpegMode) {
            val recorder: FFmpegAudioRecorder = audioRecorder.getAudioRecorder() as? FFmpegAudioRecorder ?: return
            recorder.setContext(this)
            recorder.setOnConvertStateChangeListener {
                ffmpegConvertStateChanged(it)
            }
        }

        audioRecorder.setOnRecordStateChangeListener { recordStateChanged(it) }
        audioRecorder.startRecording()
        recordInitialized = true
    }

    fun clickStop(view: View) {
        if (!recordInitialized) {
            toast("Press Start button first.")
            return
        }

        audioRecorder.stopRecording()
        recordMetadata = audioRecorder.getRecordMetadata()
    }

    fun clickResume(view: View) {
        if (!recordInitialized) {
            toast("Press Start button first.")
            return
        }

        audioRecorder.resumeRecording()
    }

    fun clickPause(view: View) {
        if (!recordInitialized) {
            toast("Press Start button first.")
            return
        }

        audioRecorder.pauseRecording()
    }

    fun clickPlay(view: View) {
        recordMetadata?.let {
            mediaPlayer.apply {
                setDataSource(this@MainActivity, Uri.fromFile(it.file))
                prepare()
                start()
            }
        }
    }

    private fun chunkAvailable(audioChunk: AudioChunk) {
        Log.d(TAG, "chunkAvailable: maxAmp: ${audioChunk.getMaxAmplitude()}, readCount: ${audioChunk.getReadCount()}")
    }

    private fun silentDetected(silentTime: Long) {
        Log.d(TAG, "silentDetected: time: $silentTime")
    }

    private fun timerChanged(currentTime: Long, maxTime: Long) {
        Log.d(TAG, "timerChanged: current: $currentTime max: $maxTime")
    }

    private fun ffmpegConvertStateChanged(convertState: FFmpegConvertState) {
        Log.d(TAG, "ffmpegConvertStateChanged: ${convertState.name}")

        runOnUiThread { toast("FFmpegConvertState: ${convertState.name}") }
    }

    private fun recordStateChanged(recordState: RecordState) {
        Log.d(TAG, "recordStateChanged: ${recordState.name}")

        runOnUiThread { toast("RecordState: ${recordState.name}") }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}
