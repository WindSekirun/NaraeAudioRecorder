package com.github.windsekirun.naraeaudiorecorder.recorder

import com.github.windsekirun.naraeaudiorecorder.writer.DefaultRecordWriter
import com.github.windsekirun.naraeaudiorecorder.writer.RecordWriter
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.Executors

/**
 * Default settings of [AudioRecorder]
 */
open class DefaultAudioRecorder(
        protected val file: File,
        protected val recordWriter: RecordWriter = DefaultRecordWriter()
) : AudioRecorder {
    private val executorService = Executors.newSingleThreadExecutor()
    private val outputStream by lazy { FileOutputStream(file) }

    private val recordingTask = Runnable {
        try {
            recordWriter.startRecording(outputStream)
        } catch (e: IOException) {
            throw RuntimeException(e)
        } catch (e: IllegalStateException) {
            throw RuntimeException("AudioRecord state has uninitialized state", e)
        }
    }

    /**
     * see [AudioRecorder.startRecording]
     */
    override fun startRecording() {
        executorService.submit(recordingTask)
    }

    /**
     * see [AudioRecorder.resumeRecording]
     */
    override fun resumeRecording() {
        recordWriter.getAudioSource().setRecordAvailable(true)
    }

    /**
     * see [AudioRecorder.pauseRecording]
     */
    override fun pauseRecording() {
        recordWriter.getAudioSource().setRecordAvailable(false)
    }

    /**
     * see [AudioRecorder.stopRecording]
     */
    override fun stopRecording() {
        recordWriter.stopRecording()
        outputStream.flush()
        outputStream.close()
    }

}
