package com.github.windsekirun.naraeaudiorecorder.constants

/**
 * Constants for define exception log
 */
object LogConstants {
    const val TAG = "NaraeAudioRecorder"

    const val EXCEPTION_NOT_SUPPORTED_NOISE_SUPPRESSOR =
            "This device doesn't support NoiseSuppressor. Try again with DefaultAudioSource"
    const val EXCEPTION_INITIAL_FAILED_NOISE_SUPPESSOR =
            "Initialization process of NoiseSuppressor is failed."

    const val EXCEPTION_DEST_FILE_NOT_ASSIGNED = "File isn't provided."
    const val EXCEPTION_FINDER_NOT_HAVE_EMPTY_CONSTRUCTOR =
            "All RecordFinder class need empty constructor to find AudioRecorder."

    const val EXCEPTION_CONTEXT_NOT_ASSIGNED = "Context isn't provided."
    const val EXCEPTION_FFMPEG_NOT_SUPPORTED = "This device doesn't support FFmpeg."

    const val PERMISSION_DENIED = "RECORD_AUDIO, READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE will be needed for recording audio."
    const val TIMER_NOT_AVAILABLE = "Timer feature will ignored."

}