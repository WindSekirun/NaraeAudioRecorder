package com.github.windsekirun.naraeaudiorecorder.ffmpeg.model

/**
 * FFmpegBitRate for convert by FFmpeg
 */
enum class FFmpegBitRate {
    def,
    u8,
    s16,
    s32,
    flt,
    dbl,
    u8p,
    s16p,
    s32p,
    fltp,
    dblp;

    val bitRate: String
        get() = name.toLowerCase()
}