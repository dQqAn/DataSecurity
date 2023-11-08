package com.example.common.koin

import com.example.common.util.UserInterface
import com.example.common.util.UserRepository
import com.example.common.util.initKoin
import org.koin.dsl.module


class DesktopApp {
    val initKoin = initKoin(
        module {

            single<UserInterface> {
                UserRepository()
            }

//            singleOf(::LoginViewModel) //            factoryOf(::LoginViewModel)
        }
    )
}
