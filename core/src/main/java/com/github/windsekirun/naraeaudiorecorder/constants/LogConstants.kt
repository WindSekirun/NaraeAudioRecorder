package com.github.windsekirun.naraeaudiorecorder.constants

/**
 * NaraeAudioRecorder
 * Class: LogConstants
 * Created by Pyxis on 1/5/19.
 *
 * Description:
 */
object LogConstants {
    const val TAG = "NaraeAudioRecorder"

    const val EXCEPTION_NOT_SUPPORTED_NOISE_SUPPRESSOR =
        "This device doesn't support NoiseSuppressor. Try again with DefaultAudioSource"
    const val EXCEPTION_INITIAL_FAILED_NOISE_SUPPESSOR =
            "Initialization process of NoiseSuppressor is failed."

    const val EXCEPTION_NOT_SUPPORTED_AUTOMATIC_GAIN_CONTROL =
        "This device doesn't support AutomaticGainControl. Try again with DefaultAudioSource"
    const val EXCEPTION_INITIAL_FAILED_AUTOMATIC_GAIN_CONTROL =
        "Initialization process of AutomaticGainControl is failed."
}