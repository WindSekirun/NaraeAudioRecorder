package com.github.windsekirun.naraeaudiorecorder.stream

import android.media.AudioRecord
import android.os.Handler
import android.os.Looper
import com.github.windsekirun.naraeaudiorecorder.chunk.AudioChunk
import com.github.windsekirun.naraeaudiorecorder.chunk.ByteArrayAudioChunk
import com.github.windsekirun.naraeaudiorecorder.extensions.runOnUiThread
import com.github.windsekirun.naraeaudiorecorder.listener.OnChunkAvailableListener
import com.github.windsekirun.naraeaudiorecorder.source.AudioSource
import java.io.OutputStream

/**
 * Default settigns of [RecordWriter]
 *
 * @param audioSource
 */
open class DefaultRecordWriter(private val audioSource: AudioSource) : RecordWriter {
    private var chunkAvailableListener: OnChunkAvailableListener? = null

    /**
     * see [RecordWriter.startRecording]
     */
    override fun startRecording(outputStream: OutputStream) {
        write(getAudioSource().getAudioRecord(), getAudioSource().getBufferSize(), outputStream)
    }

    /**
     * see [RecordWriter.stopRecording]
     */
    override fun stopRecording() {
        getAudioSource().getAudioRecord().stop()
        getAudioSource().getAudioRecord().release()
    }

    /**
     * see [RecordWriter.getAudioSource]
     */
    override fun getAudioSource(): AudioSource = audioSource

    open fun write(audioRecord: AudioRecord, bufferSize: Int, outputStream: OutputStream) {
        val audioChunk = ByteArrayAudioChunk(ByteArray(bufferSize))
        audioChunk.setReadCount(audioRecord.read(audioChunk.bytes, 0, bufferSize))

        if (audioChunk.getReadCount() != AudioRecord.ERROR_INVALID_OPERATION &&
            audioChunk.getReadCount() != AudioRecord.ERROR_BAD_VALUE) {

            runOnUiThread {
                chunkAvailableListener?.onChunkAvailable(audioChunk)
            }

            // write out on outputStream
            outputStream.write(audioChunk.bytes)
        }
    }

    /**
     * set [OnChunkAvailableListener] to get [AudioChunk]
     */
    fun setOnChunkAvailableListener(listener: OnChunkAvailableListener) =
        this.apply { this.chunkAvailableListener = listener }
}