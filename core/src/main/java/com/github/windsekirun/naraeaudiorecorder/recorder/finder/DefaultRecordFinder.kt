package com.github.windsekirun.naraeaudiorecorder.recorder.finder

import com.github.windsekirun.naraeaudiorecorder.recorder.AudioRecorder
import com.github.windsekirun.naraeaudiorecorder.recorder.PcmAudioRecorder
import com.github.windsekirun.naraeaudiorecorder.recorder.WavAudioRecorder
import com.github.windsekirun.naraeaudiorecorder.writer.RecordWriter
import java.io.File

/**
 * Default settings of [RecordFinder]
 */
class DefaultRecordFinder : RecordFinder {

    /**
     * see [RecordFinder.find]
     */
    override fun find(extension: String, file: File, writer: RecordWriter): AudioRecorder {
        return when (extension) {
            "wav" -> WavAudioRecorder(file, writer)
            "pcm" -> PcmAudioRecorder(file, writer)
            else -> PcmAudioRecorder(file, writer)
        }
    }

}