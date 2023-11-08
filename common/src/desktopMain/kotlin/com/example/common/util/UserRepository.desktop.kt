package com.example.common.util

actual class UserRepository(

) : UserInterface {
    override fun signOut() {
        TODO("Not yet implemented")
    }

    override fun signIn(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override fun signUp(email: String, password: String) {
        TODO("Not yet implemented")
    }

    override fun userID(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun reloadUser() {
        TODO("Not yet implemented")
    }

    override var navigationClick: (() -> Unit)?
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun showShortToastMessage(message: String) {
        TODO("Not yet implemented")
    }

    override fun showLongToastMessage(message: String) {
        TODO("Not yet implemented")
    }

    override var notificationBar: Notification?
        get() = TODO("Not yet implemented")
        set(value) {}

}
