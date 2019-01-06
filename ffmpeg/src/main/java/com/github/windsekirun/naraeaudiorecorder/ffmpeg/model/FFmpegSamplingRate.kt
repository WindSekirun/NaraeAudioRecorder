package com.github.windsekirun.naraeaudiorecorder.ffmpeg.model

/**
 * FFmpegSamplingRate for convert by FFmpeg
 */
enum class FFmpegSamplingRate(val samplingRate: Int) {
    ENCODING_IN_8000(8000),
    ENCODING_IN_11025(11025),
    ENCODING_IN_16000(16000),
    ENCODING_IN_22050(22050),
    ENCODING_IN_32000(32000),
    ENCODING_IN_44100(44100),
    ENCODING_IN_48000(48000),
    ENCODING_IN_88200(88200),
    ENCODING_IN_96000(96000),
    ENCODING_IN_76400(76400),
    ENCODING_IN_192000(192000),
    ENCODING_IN_352800(352800),
    ENCODING_IN_384000(384000),
    ORIGINAL(0)
}