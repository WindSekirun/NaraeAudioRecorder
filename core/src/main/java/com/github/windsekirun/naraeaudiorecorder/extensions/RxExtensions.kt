package com.github.windsekirun.naraeaudiorecorder.extensions

import android.util.Log
import com.github.windsekirun.naraeaudiorecorder.constants.LogConstants
import io.reactivex.Observable
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.disposables.Disposable

/**
 * internal extensions to handle onNext, onError, onComplete event
 */
@CheckReturnValue
fun <T> Observable<T>.subscribe(callback: (T?, Throwable?, Boolean) -> Unit): Disposable {
    return this.subscribe({
        callback.invoke(it, null, false)
    }, {
        Log.e(LogConstants.TAG, "error", it)
        callback.invoke(null, it, false)
    }, {
        callback.invoke(null, null, true)
    })
}

/**
 * internal extensions to handle dispose Disposable
 */
fun Disposable?.safeDispose() {
    if (this != null && !this.isDisposed) this.dispose()
}