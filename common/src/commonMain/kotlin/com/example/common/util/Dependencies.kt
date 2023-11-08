package com.example.common.util

import androidx.compose.runtime.staticCompositionLocalOf

abstract class Dependencies {
    abstract val notification: Notification
}

internal val LocalNotification = staticCompositionLocalOf<Notification> {
    noLocalProvidedFor("LocalNotification")
}

private fun noLocalProvidedFor(name: String): Nothing {
    error("CompositionLocal $name not present")
}

interface Notification {
    fun showLongToastMessage(message: String)
    fun showShortToastMessage(message: String)
}

abstract class PopupNotification : Notification {
    abstract fun showLongPopUpMessage(text: String)
    abstract fun showShortPopUpMessage(text: String)
    override fun showLongToastMessage(message: String) = showLongPopUpMessage(message)
    override fun showShortToastMessage(message: String) = showShortPopUpMessage(message)
}
