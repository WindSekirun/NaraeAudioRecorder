package com.github.windsekirun.naraeaudiorecorder.recorder

import com.github.windsekirun.naraeaudiorecorder.extensions.ignoreException
import com.github.windsekirun.naraeaudiorecorder.recorder.wav.WavHeader
import com.github.windsekirun.naraeaudiorecorder.writer.RecordWriter
import java.io.File
import java.io.IOException
import java.io.RandomAccessFile

/**
 * [AudioRecorder] for record audio and save in wav file
 */
open class WavAudioRecorder(file: File, recordWriter: RecordWriter) : DefaultAudioRecorder(file, recordWriter) {
    override fun stopRecording() {
        super.stopRecording()
        writeWavHeader()
    }

    @Throws(IOException::class)
    private fun writeWavHeader() {
        val wavFile = randomAccessFile(file)
        wavFile?.let {
            it.seek(0)
            it.write(WavHeader(recordWriter.getAudioSource(), file.length()).getWavFileHeaderByteArray())
            it.close()
        }
    }

    private fun randomAccessFile(file: File): RandomAccessFile? = ignoreException { RandomAccessFile(file, "rw") }
}