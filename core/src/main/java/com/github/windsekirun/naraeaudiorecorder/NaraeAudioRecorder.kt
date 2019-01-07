package com.github.windsekirun.naraeaudiorecorder

import android.Manifest
import android.content.Context
import android.media.MediaMetadataRetriever
import com.github.windsekirun.naraeaudiorecorder.config.AudioRecorderConfig
import com.github.windsekirun.naraeaudiorecorder.constants.LogConstants
import com.github.windsekirun.naraeaudiorecorder.extensions.safeDispose
import com.github.windsekirun.naraeaudiorecorder.extensions.subscribe
import com.github.windsekirun.naraeaudiorecorder.listener.OnRecordStateChangeListener
import com.github.windsekirun.naraeaudiorecorder.model.DebugState
import com.github.windsekirun.naraeaudiorecorder.model.RecordMetadata
import com.github.windsekirun.naraeaudiorecorder.model.RecordState
import com.github.windsekirun.naraeaudiorecorder.recorder.AudioRecorder
import com.github.windsekirun.naraeaudiorecorder.recorder.WavAudioRecorder
import com.github.windsekirun.naraeaudiorecorder.recorder.finder.DefaultRecordFinder
import com.github.windsekirun.naraeaudiorecorder.recorder.finder.RecordFinder
import com.github.windsekirun.naraeaudiorecorder.source.DefaultAudioSource
import com.github.windsekirun.naraeaudiorecorder.source.NoiseAudioSource
import com.github.windsekirun.naraeaudiorecorder.writer.DefaultRecordWriter
import com.github.windsekirun.naraeaudiorecorder.writer.NoiseRecordWriter
import com.github.windsekirun.naraeaudiorecorder.writer.RecordWriter
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import pyxis.uzuki.live.richutilskt.utils.RPermission
import java.util.concurrent.TimeUnit


/**
 * Main-stream class for manage Record feature
 */
class NaraeAudioRecorder {
    private lateinit var recorderConfig: AudioRecorderConfig
    private lateinit var recordWriter: RecordWriter
    private lateinit var audioRecorder: AudioRecorder
    private lateinit var timerDisposable: Disposable
    private lateinit var recordMetadata: RecordMetadata

    private var currentTimer: Long = 0
    private var recordStateChangeListener: OnRecordStateChangeListener? = null
    private var permissionGranted: Boolean = false

    /**
     * create and assign [NaraeAudioRecorder]
     */
    fun create(config: AudioRecorderConfig.() -> Unit) {
        create(DefaultRecordFinder::class.java, config)
    }

    /**
     * create and assign [NaraeAudioRecorder]
     */
    fun create(recordFinder: Class<*>, config: AudioRecorderConfig.() -> Unit) {

        val audioRecorderConfig = AudioRecorderConfig()
        audioRecorderConfig.config()
        if (!audioRecorderConfig.check()) return

        this.recorderConfig = audioRecorderConfig

        findRecordWriter()
        findAudioRecorder(recordFinder)

        DebugState.state = audioRecorderConfig.debugMode
    }

    /**
     * check permission when using [NaraeAudioRecorder]
     */
    fun checkPermission(context: Context) {
        requestPermission(context) {
            permissionGranted = it

            if (permissionGranted) {
                DebugState.debug(LogConstants.PERMISSION_GRANTED)
            } else {
                DebugState.debug(LogConstants.PERMISSION_DENIED)
            }
        }
    }

    /**
     * get [AudioRecorder] object
     */
    fun getAudioRecorder() = audioRecorder

    /**
     * Start Recording
     */
    fun startRecording() {
        if (!permissionGranted) {
            DebugState.debug(LogConstants.PERMISSION_DENIED)
            return
        }

        audioRecorder.startRecording()
        recordStateChangeListener?.onState(RecordState.START)

        startTimer()
    }

    /**
     * Stop recording
     */
    fun stopRecording() {
        if (!::timerDisposable.isInitialized) {
            return
        }

        audioRecorder.stopRecording()
        recordStateChangeListener?.onState(RecordState.STOP)

        stopTimer()
        retrieveMetadata()
    }

    /**
     * Pause recording
     */
    fun pauseRecording() {
        if (!::timerDisposable.isInitialized) {
            return
        }

        audioRecorder.pauseRecording()
        recordStateChangeListener?.onState(RecordState.PAUSE)
    }

    /**
     * Resume recording
     */
    fun resumeRecording() {
        if (!::timerDisposable.isInitialized) {
            return
        }

        audioRecorder.resumeRecording()
        recordStateChangeListener?.onState(RecordState.RESUME)
    }

    /**
     * set [OnRecordStateChangeListener] to handle state changes of [NaraeAudioRecorder]
     */
    fun setOnRecordStateChangeListener(listener: OnRecordStateChangeListener) {
        this.recordStateChangeListener = listener
    }

    /**
     * Kotlin-compatible version of [setOnRecordStateChangeListener]
     */
    fun setOnRecordStateChangeListener(callback: (RecordState) -> Unit) {
        this.recordStateChangeListener = object : OnRecordStateChangeListener {
            override fun onState(state: RecordState) {
                callback.invoke(state)
            }
        }
    }

    /**
     * get [RecordMetadata] which contains file and duration
     */
    fun getRecordMetadata(): RecordMetadata? {
        if (!::recordMetadata.isInitialized) {
            return null
        }

        return recordMetadata
    }

    private fun requestPermission(context: Context, action: (Boolean) -> Unit) {
        val permission = listOf(Manifest.permission.RECORD_AUDIO,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
        RPermission.instance.checkPermission(context, permission) { code, _ ->
            action(code == RPermission.PERMISSION_GRANTED)
        }
    }

    private fun findAudioRecorder(recordFinder: Class<*>) {
        try {
            val finder = recordFinder.getConstructor().newInstance() as? RecordFinder
                    ?: throw IllegalArgumentException(LogConstants.EXCEPTION_FINDER_NOT_HAVE_EMPTY_CONSTRUCTOR)

            val file = recorderConfig.destFile ?: return // it can't be null
            audioRecorder = finder.find(file.extension, file, recordWriter)
        } catch (exception: Exception) {
            throw IllegalArgumentException(LogConstants.EXCEPTION_FINDER_NOT_HAVE_EMPTY_CONSTRUCTOR, exception)
        }
    }

    private fun findRecordWriter() {
        val recordWriter = when (recorderConfig.audioSource) {
            is DefaultAudioSource -> DefaultRecordWriter(recorderConfig.audioSource)
            is NoiseAudioSource -> NoiseRecordWriter(recorderConfig.audioSource)
            else -> null
        }

        if (recordWriter != null) this.recordWriter = recordWriter

        if (recordWriter is DefaultRecordWriter) {
            recordWriter.setOnChunkAvailableListener(recorderConfig.chunkAvailableListener)
        }

        if (recordWriter is NoiseRecordWriter) {
            recordWriter.setOnSilentDetectedListener(recorderConfig.silentDetectedListener)
        }
    }

    private fun startTimer() {
        timerDisposable = Observable.interval(recorderConfig.refreshTimerMillis, TimeUnit.MILLISECONDS)
                .subscribe { data, _, _ ->

                    if (data == null) return@subscribe
                    if (recordWriter.getAudioSource().isRecordAvailable()) {
                        currentTimer += recorderConfig.refreshTimerMillis
                        recorderConfig.timerCountListener?.onTime(currentTimer, recorderConfig.maxAvailableMillis)

                        if (recorderConfig.maxAvailableMillis != -1L && currentTimer >= recorderConfig.maxAvailableMillis) {
                            stopRecording()
                        }
                    }
                }
    }

    private fun stopTimer() {
        timerDisposable.safeDispose()
    }

    private fun retrieveMetadata() {
        val file = recorderConfig.destFile ?: return

        if (audioRecorder !is WavAudioRecorder) {
            recordMetadata = RecordMetadata(file, 0)
            return
        }

        val metaRetriever = MediaMetadataRetriever()
        metaRetriever.apply {
            setDataSource(file.absolutePath)
        }

        val duration =
                metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION).toLong()

        metaRetriever.release()

        recordMetadata = RecordMetadata(file, duration)
    }
}