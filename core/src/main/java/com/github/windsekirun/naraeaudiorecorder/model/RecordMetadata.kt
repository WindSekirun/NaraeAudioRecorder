package com.github.windsekirun.naraeaudiorecorder.model

import java.io.File

/**
 * NaraeAudioRecorder
 * Class: RecordMetadata
 * Created by Pyxis on 1/6/19.
 *
 * Description:
 */

data class RecordMetadata(val file: File, val duration: Long = 0)