package com.example.common.util

interface UserInterface {

    fun signOut()
    fun signIn(email: String, password: String)
    fun signUp(email: String, password: String)
    fun userID(): String?
    suspend fun reloadUser()
    var navigationClick: (() -> Unit)? //    val navigationClick: MutableState<(() -> Unit)?>
    fun showShortToastMessage(message: String)
    fun showLongToastMessage(message: String)
    var notificationBar: Notification?
}