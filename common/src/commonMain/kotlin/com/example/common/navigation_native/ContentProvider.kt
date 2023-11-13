package com.example.common.navigation_native

import CryptoViewModel
import LoginViewModel
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.common.util.Dependencies
import com.example.common.util.LocalNotification
import com.example.common.view.cryptoContent
import com.example.common.view.driveContent
import com.example.common.view.signInContent
import com.example.common.view.signUpContent

@Composable
fun BoxWithConstraintsScope.contentProvider(
    cryptoViewModel: CryptoViewModel,
    loginViewModel: LoginViewModel
) {
    val navigationStack = rememberSaveable(
        saver = listSaver<NavigationStack<Page>, Page>(
            restore = { NavigationStack(*it.toTypedArray()) },
            save = { it.stack },
        )
    ) {
        NavigationStack(loginViewModel.getPage())
    }

    val navigationStackSize: Int = navigationStack.stack.size

    AnimatedContent(
        targetState = navigationStack.lastWithIndex(),
        transitionSpec = {
            slideInHorizontally(
                animationSpec = tween(400),
                initialOffsetX = { fullWidth -> fullWidth }
            ) togetherWith
                    slideOutHorizontally(
                        animationSpec = tween(400),
                        targetOffsetX = { fullWidth -> -fullWidth }
                    )
        })
    { (_, page) ->
        when (page) {

            is SignInPage -> {
                signInContent(
                    loginViewModel = loginViewModel,
                    onSignUpPageClick = { navigationStack.push(SignUpPage()) },
                    onCryptoPageClick = { navigationStack.push(CryptoPage()) },
                )
            }

            is SignUpPage -> {
                signUpContent(
                    loginViewModel = loginViewModel,
                    onCryptoPageClick = { navigationStack.push(CryptoPage()) },
                    onBackClick = { navigationStack.back() }
                )
            }

            is CryptoPage -> {
                cryptoContent(
                    cryptoViewModel = cryptoViewModel,
                    loginViewModel = loginViewModel,
                    onFirebasePageClick = { navigationStack.push(DrivePage()) },
                    onSingInClick = { navigationStack.push(SignInPage()) }
                )
            }

            is DrivePage -> {
                driveContent(
                    cryptoViewModel = cryptoViewModel,
                    onBackClick = { navigationStack.back() }
                )
            }
        }
    }
}

@Composable
fun localContent(
    dependencies: Dependencies,
    cryptoViewModel: CryptoViewModel,
    loginViewModel: LoginViewModel
) {
    CompositionLocalProvider(
        LocalNotification provides dependencies.notification
    ) {
        BoxWithConstraints {
            contentProvider(cryptoViewModel, loginViewModel)
        }
    }

}