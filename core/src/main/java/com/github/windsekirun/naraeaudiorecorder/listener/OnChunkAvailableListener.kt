package com.github.windsekirun.naraeaudiorecorder.listener

import com.github.windsekirun.naraeaudiorecorder.chunk.AudioChunk

/**
 * Listener for handling [AudioChunk]
 */
interface OnChunkAvailableListener {

    /**
     * Called when ByteArray has success to came out.
     */
    fun onChunkAvailable(audioChunk: AudioChunk)
}