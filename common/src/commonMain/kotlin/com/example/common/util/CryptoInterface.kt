package com.example.common.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import java.io.File

interface CryptoInterface {
    @Composable
    fun uploadButton(algorithm: MutableState<String?>, key: MutableState<String?>)

    fun uploadFile(algorithm: MutableState<String?>, key: MutableState<String?>)

    var selectedFile: File?

    val isOpenFileManager: MutableState<Boolean>

    fun encryptedList(list: MutableState<List<String?>>)
    fun unencryptedList(list: MutableState<List<String?>>)

    fun driveList(
        list: MutableState<List<String?>>,
        currentFolder: MutableState<String?>,
        selectedPath: MutableState<String?>
    )

    fun downloadFile(category: String, fileName: String)

    fun decrypt(algorithm: String, file: File, key: String)

    fun createFolder(folderName: String)
}