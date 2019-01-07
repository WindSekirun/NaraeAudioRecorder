package com.github.windsekirun.naraeaudiorecorder.recorder

import com.github.windsekirun.naraeaudiorecorder.writer.RecordWriter
import java.io.File

/**
 * [AudioRecorder] for record audio and save in pcm file
 */
class PcmAudioRecorder(file: File, recordWriter: RecordWriter) : DefaultAudioRecorder(file, recordWriter)