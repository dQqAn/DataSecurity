import androidx.compose.runtime.mutableStateOf
import com.example.common.navigation_native.CryptoPage
import com.example.common.navigation_native.Page
import com.example.common.navigation_native.SignInPage
import com.example.common.util.UserInterface
import com.example.common.util.ViewModel

class LoginViewModel(
    private val repository: UserInterface,
) : ViewModel() {

    //region SignIn
    private var _signInMail = mutableStateOf("")
    internal val signInMail = _signInMail
    internal fun changeSignInMail(mail: String) {
        _signInMail.value = mail
    }

    private var _signInPassword = mutableStateOf("")
    internal val signInPassword = _signInPassword
    internal fun changeSignInPassword(password: String) {
        _signInPassword.value = password
    }

    internal fun signIn() {
        if (signInMail.value.isNotEmpty() && signInPassword.value.isNotEmpty()) {
            repository.signIn(signInMail.value, signInPassword.value)
        } else {
            repository.showShortToastMessage("Check your mail and password.")
        }
    }
    //endregion

    //region SignUp
    private var _signUpFirstMail = mutableStateOf("")
    internal val signUpFirstMail = _signUpFirstMail
    internal fun changeSignUpFirstMail(mail: String) {
        _signUpFirstMail.value = mail
    }

    private var _signUpSecondMail = mutableStateOf("")
    internal val signUpSecondMail = _signUpSecondMail
    internal fun changeSignUpSecondMail(mail: String) {
        _signUpSecondMail.value = mail
    }

    private var _signUpFirstPassword = mutableStateOf("")
    internal val signUpFirstPassword = _signUpFirstPassword
    internal fun changeSignUpFirstPassword(password: String) {
        _signUpFirstPassword.value = password
    }

    private var _signUpSecondPassword = mutableStateOf("")
    internal val signUpSecondPassword = _signUpSecondPassword
    internal fun changeSignUpSecondPassword(password: String) {
        _signUpSecondPassword.value = password
    }

    internal fun signUp() {
        if (signUpFirstMail.value != signUpSecondMail.value) {
            repository.showShortToastMessage("Your mails are not same.")
        } else if (signUpFirstPassword.value != signUpSecondPassword.value) {
            repository.showShortToastMessage("Your passwords are not same.")
        } else {
            if (signUpFirstMail.value.isNotEmpty() && signUpFirstPassword.value.isNotEmpty()) {
                repository.signUp(signUpFirstMail.value, signUpFirstPassword.value)
            } else {
                repository.showShortToastMessage("Check your mail and password.")
            }
        }
    }
    //endregion

    internal fun setNavigationClick(click: () -> Unit) {
        repository.navigationClick = click
    }

    internal fun reloadUser() {
        myRunBlocking { repository.reloadUser() }
    }

    internal fun getPage(): Page {
        reloadUser()

        val userId: String? = repository.userID()

        return if (userId != null) {
            CryptoPage()
        } else {
            SignInPage()
        }
    }

    internal fun signOut() {
        repository.signOut()
    }
}