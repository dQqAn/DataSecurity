package com.example.common.view

import LoginViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun BoxWithConstraintsScope.signInContent(
    loginViewModel: LoginViewModel,
    onSignUpPageClick: () -> Unit,
    onCryptoPageClick: () -> Unit
) {
    val maxWidth = maxWidth
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        mailOutlinedTextField(
            text = loginViewModel.signInMail.value,
            onTextChanged = loginViewModel::changeSignInMail,
            placeHolderText = "Your mail",
            keyboardType = KeyboardType.Email,
            leadingIcon = Icons.Default.Email,
            leadingDescription = "",
            trailingDescription = "",
            maxWidth
        )

        Spacer(modifier = Modifier.height(14.dp))

        passwordOutlinedTextField(
            text = loginViewModel.signInPassword.value,
            onTextChanged = loginViewModel::changeSignInPassword,
            placeHolderText = "Your password",
            keyboardType = KeyboardType.Password,
            leadingIcon = Icons.Default.Lock,
            leadingDescription = "",
            trailingDescription = "",
            maxWidth
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(onClick = {
            loginViewModel.setNavigationClick(onCryptoPageClick).apply {
                loginViewModel.signIn()
            }
        }) { Text("Sign In") }

        Spacer(modifier = Modifier.height(14.dp))

        Button(onClick = {
            onSignUpPageClick()
        }) { Text("Create new account") }
    }

}

@Composable
internal fun BoxWithConstraintsScope.signUpContent(
    loginViewModel: LoginViewModel,
    onCryptoPageClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val maxWidth = maxWidth
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        mailOutlinedTextField(
            text = loginViewModel.signUpFirstMail.value,
            onTextChanged = loginViewModel::changeSignUpFirstMail,
            placeHolderText = "Your mail",
            keyboardType = KeyboardType.Email,
            leadingIcon = Icons.Default.Email,
            leadingDescription = "",
            trailingDescription = "",
            maxWidth
        )

        Spacer(modifier = Modifier.height(14.dp))

        mailOutlinedTextField(
            text = loginViewModel.signUpSecondMail.value,
            onTextChanged = loginViewModel::changeSignUpSecondMail,
            placeHolderText = "Your mail",
            keyboardType = KeyboardType.Email,
            leadingIcon = Icons.Default.Email,
            leadingDescription = "",
            trailingDescription = "",
            maxWidth
        )

        Spacer(modifier = Modifier.height(14.dp))

        passwordOutlinedTextField(
            text = loginViewModel.signUpFirstPassword.value,
            onTextChanged = loginViewModel::changeSignUpFirstPassword,
            placeHolderText = "Your password",
            keyboardType = KeyboardType.Password,
            leadingIcon = Icons.Default.Lock,
            leadingDescription = "",
            trailingDescription = "",
            maxWidth
        )

        Spacer(modifier = Modifier.height(14.dp))

        passwordOutlinedTextField(
            text = loginViewModel.signUpSecondPassword.value,
            onTextChanged = loginViewModel::changeSignUpSecondPassword,
            placeHolderText = "Your password",
            keyboardType = KeyboardType.Password,
            leadingIcon = Icons.Default.Lock,
            leadingDescription = "",
            trailingDescription = "",
            maxWidth
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(onClick = {
            loginViewModel.setNavigationClick(onCryptoPageClick).apply {
                loginViewModel.signUp()
            }
        }) { Text("Next") }

        Spacer(modifier = Modifier.height(14.dp))

        TextButton(onClick = {
            onBackClick()
        }) { Text("Back") }
    }

}

@Composable
private fun mailOutlinedTextField(
    text: String,
    onTextChanged: (String) -> Unit,
    placeHolderText: String,
    keyboardType: KeyboardType,
    leadingIcon: ImageVector,
    leadingDescription: String,
    trailingDescription: String,
    maxWidth: Dp,
) {
    OutlinedTextField(
        value = text,
        onValueChange = onTextChanged,
        placeholder = { Text(text = placeHolderText) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier
            .width((maxWidth / 100 * 70))
            .heightIn(max = 90.dp),
        maxLines = 1,
        singleLine = true,
        enabled = true,
        readOnly = false,
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = leadingDescription
            )
        },
        trailingIcon = {
            IconButton(onClick = { onTextChanged("") }) {
                Icon(imageVector = Icons.Default.Clear, contentDescription = trailingDescription)
            }
        }
    )
}

@Composable
private fun passwordOutlinedTextField(
    text: String,
    onTextChanged: (String) -> Unit,
    placeHolderText: String,
    keyboardType: KeyboardType,
    leadingIcon: ImageVector,
    leadingDescription: String,
    trailingDescription: String,
    maxWidth: Dp,
) {

    var passwordHidden by remember { mutableStateOf(true) }
//    val passwordHidden = loginViewModel.passwordHidden.value

    OutlinedTextField(
        value = text,
        onValueChange = onTextChanged,
//        label = { Text(text="Label") },
        /*onValueChange = {
            text = it
        }*/
        placeholder = { Text(text = placeHolderText) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier
            .width((maxWidth / 100 * 70))
            .heightIn(max = 90.dp),
        maxLines = 1,
        singleLine = true,
        enabled = true,
        readOnly = false,
        leadingIcon = {
            Icon(
                imageVector = leadingIcon,
                contentDescription = leadingDescription
            )
        },
        visualTransformation =
        if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,

        trailingIcon = {
            IconButton(onClick =
            {
                passwordHidden =
                    !passwordHidden // loginViewModel.changePasswordHiddenState(!passwordHidden) /*{ changePasswordHiddenState(!passwordHidden) }*/
            }) {
                val visibilityIcon =
                    if (passwordHidden) Icons.Default.VisibilityOff else Icons.Default.Visibility
//                val currentLocalizationValue = getCurrentLocalization(localizationViewModel)
//                val description = if (passwordHidden) currentLocalizationValue.showPassword else currentLocalizationValue.hidePassword

                Icon(imageVector = visibilityIcon, contentDescription = trailingDescription)
            }
        }
    )

}