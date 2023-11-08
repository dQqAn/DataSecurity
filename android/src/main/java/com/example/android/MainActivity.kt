package com.example.android

import CryptoViewModel
import LoginViewModel
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.platform.LocalContext
import com.example.common.androidContent
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val cryptoViewModel: CryptoViewModel by viewModel()
    private val loginViewModel: LoginViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            MaterialTheme {
                androidContent(context, loginViewModel, cryptoViewModel)
            }
        }
    }
}