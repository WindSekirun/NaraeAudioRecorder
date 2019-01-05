package com.github.windsekirun.naraeaudiorecorder.stream

import com.github.windsekirun.naraeaudiorecorder.source.AudioSource
import java.io.OutputStream

/**
 * Default settigns of [RecordWriter]
 */
class DefaultRecordWriter: RecordWriter {
    override fun startRecording(outputStream: OutputStream) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun stopRecording() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAudioSource(): AudioSource {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}