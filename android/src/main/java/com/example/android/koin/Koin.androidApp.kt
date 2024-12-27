package com.example.android.koin

import android.app.Application
import android.content.Context
import com.example.common.util.*
import org.koin.dsl.module

class AndroidApp : Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin(
            module {
                single<Context> { this@AndroidApp }

                single<UserInterface> {
                    UserRepository()
                }

                single<CryptoInterface> {
                    CryptoRepository(context = get())
                }
            }
        )
    }
}


