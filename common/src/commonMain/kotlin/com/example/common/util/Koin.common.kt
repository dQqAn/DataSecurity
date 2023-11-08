package com.example.common.util

import CryptoViewModel
import LoginViewModel
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

fun initKoin(appModule: Module): KoinApplication = startKoin { modules(coreModule, appModule) }

private val coreModule = module {
    single { LoginViewModel(repository = get()) } //    singleOf(::LoginViewModel)
    single { CryptoViewModel(repository = get()) }
}

//expect val platformModule: Module