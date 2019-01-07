package com.github.windsekirun.naraeaudiorecorder.extensions

import com.github.windsekirun.naraeaudiorecorder.model.DebugState
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
        DebugState.error("error", it)
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