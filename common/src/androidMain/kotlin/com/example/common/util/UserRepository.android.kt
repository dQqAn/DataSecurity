package com.example.common.util

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

actual class UserRepository(

) : UserInterface {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun signOut() {
        auth.currentUser?.let { auth.signOut() }
    }

    override fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.v("Firebase", "signInWithEmail: success")
                    navigationClick?.invoke()//                    navigationClick.value?.invoke()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.v("Firebase", "signInWithEmail: failure", task.exception)
                    showShortToastMessage("authentication failed")
                }
            }
    }

    override fun signUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.v("Firebase", "createUserWithEmail:success")
                    navigationClick?.invoke()//                    navigationClick.value?.invoke()
//                    auth.firebaseAuthSettings.forceRecaptchaFlowForTesting(true)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.v("Firebase", "createUserWithEmail:failure", task.exception)
                    showShortToastMessage("authentication failed")
                }
            }
    }

    override fun userID(): String? {
        return auth.currentUser?.uid
    }

    override suspend fun reloadUser() {
        try {
            auth.currentUser?.reload()
                ?.await() //            (auth.currentUser?.getIdToken(true)) //it's working in the second call
        } catch (e: Exception) {
            auth.signOut()
            Log.d("Firebase", "User may have been deleted.")
        }
    }

    override var navigationClick: (() -> Unit)? = null

    override fun showShortToastMessage(message: String) {
        notificationBar?.showShortToastMessage(message)
    }

    override fun showLongToastMessage(message: String) {
        notificationBar?.showLongToastMessage(message)
    }

    override var notificationBar: Notification? = null
}
