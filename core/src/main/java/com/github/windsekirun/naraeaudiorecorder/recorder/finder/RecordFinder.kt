package com.github.windsekirun.naraeaudiorecorder.recorder.finder

import com.github.windsekirun.naraeaudiorecorder.recorder.AudioRecorder
import com.github.windsekirun.naraeaudiorecorder.writer.RecordWriter
import java.io.File

/**
 * find proper [AudioRecorder] class which condition
 */
interface RecordFinder {

    /**
     * find [AudioRecorder] with given [extension]
     */
    fun find(extension: String, file: File, writer: RecordWriter): AudioRecorder
}