@file:Suppress("PARCELABLE_PRIMARY_CONSTRUCTOR_IS_EMPTY")

package com.example.common.navigation_native

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
internal actual class SignInPage actual constructor() : Page, Parcelable

@Parcelize
internal actual class SignUpPage actual constructor() : Page, Parcelable

@Parcelize
internal actual class CryptoPage actual constructor() : Page, Parcelable

@Parcelize
internal actual class DrivePage actual constructor() : Page, Parcelable
