package com.github.windsekirun.naraeaudiorecorder.ffmpeg.extensions

import java.lang.ref.WeakReference
import kotlin.reflect.KProperty

internal class WeakRefHolder<T>(private var value: WeakReference<T?>) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        return value.get()
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        this.value = WeakReference(value)
    }
}

internal fun <T> weak(value: T) = WeakRefHolder(WeakReference(value))