package com.example.common

import CryptoViewModel
import LoginViewModel
import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.example.common.navigation_native.localContent
import com.example.common.util.Dependencies
import com.example.common.util.Notification
import com.example.common.util.PopupNotification

@SuppressLint("ComposableNaming")
@Composable
fun androidContent(
    context: Context,
    loginViewModel: LoginViewModel,
    cryptoViewModel: CryptoViewModel,
) {
    val dependencies = remember(context) {
        getDependencies(context)
    }
    localContent(dependencies, cryptoViewModel, loginViewModel)
}

private fun getDependencies(
    context: Context,
) = object : Dependencies() {
    override val notification: Notification = object : PopupNotification() {
        override fun showLongPopUpMessage(text: String) {
            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        }

        override fun showShortPopUpMessage(text: String) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
        }
    }
}