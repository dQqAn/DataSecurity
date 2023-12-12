package com.example.common.view

import CryptoViewModel
import LoginViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Key
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
internal fun BoxWithConstraintsScope.cryptoContent(
    cryptoViewModel: CryptoViewModel,
    loginViewModel: LoginViewModel,
    onFirebasePageClick: () -> Unit,
    onSingInClick: () -> Unit
) {
    val maxWidth = maxWidth
    Column(
        Modifier.selectableGroup().fillMaxSize().padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CheckBoxGroup(
            cryptoViewModel.selectedOption.value,
            cryptoViewModel::changeSelectedOption,
            cryptoViewModel::changeAlgorithm
        )

        OutlinedTextField(
            modifier = Modifier
                .width((maxWidth / 100 * 70))
                .heightIn(max = 90.dp),
            value = cryptoViewModel.keyText.value!!,
            onValueChange = cryptoViewModel::changeKeyText,
            placeholder = { Text(text = "Key") },
            enabled = cryptoViewModel.selectedOption.value > -1,
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Key,
                    contentDescription = ""
                )
            },
            trailingIcon = {
                IconButton(onClick = { cryptoViewModel.changeKeyText("") }) {
                    Icon(imageVector = Icons.Default.Clear, contentDescription = "")
                }
            }
        )

        cryptoViewModel.uploadButton()

        Button(
            onClick = {
                onFirebasePageClick()
            }
        ) { Text("Drive Page") }

        Button(
            onClick = {
                loginViewModel.signOut().apply {
                    onSingInClick()
                }
            }
        ) { Text("Sign out") }
    }
}

@Composable
fun CheckBoxGroup(
    selectedOption: Int,
    onOptionSelected: (Int) -> Unit,
    changeAlgorithm: (String?) -> Unit
) {
    val radioOptions = listOf("DES", "AES", "BLOWFISH")

    Column(Modifier.selectableGroup()) {
        radioOptions.forEachIndexed { index, text ->
            SelectOptionsCheckout(
                index = index,
                text = text,
                isSelectedOption = selectedOption == index,
                onSelectOption = {
                    if (it == selectedOption) {
                        onOptionSelected(-1)
                        changeAlgorithm(null)
                    } else {
                        onOptionSelected(it)
                        changeAlgorithm(text)
                    }
                }
            )
        }
    }
}

@Composable
fun SelectOptionsCheckout(
    index: Int,
    text: String,
    isSelectedOption: Boolean,
    onSelectOption: (Int) -> Unit
) {
    Row() {
        Text(text)
        Icon(
            imageVector = CheckboxResource(isSelected = isSelectedOption),
            contentDescription = "Checkbox",
            modifier = Modifier
                .clickable {
                    onSelectOption(index)
                }
        )
    }
}

@Composable
fun CheckboxResource(isSelected: Boolean): ImageVector {
    return if (isSelected) {
        Icons.Default.Check
    } else {
        Icons.Default.Cancel
    }
}