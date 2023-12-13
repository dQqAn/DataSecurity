package com.example.common.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import java.io.File

interface CryptoInterface {
    @Composable
    fun uploadButton(
        algorithm: MutableState<String?>,
        key: MutableState<String?>,
        selectedPath: MutableState<String?>,
        localTextFileKey: MutableState<String?>,
    )

    fun uploadFile(
        algorithm: MutableState<String?>,
        key: MutableState<String?>,
        selectedPath: MutableState<String?>,
        localTextFileKey: MutableState<String?>,
    )

    var selectedFile: File?

    val isOpenFileManager: MutableState<Boolean>

//    fun folderList(list: MutableState<List<String?>>)
//    fun fileList(list: MutableState<List<String?>>)

//    val folderList: MutableState<List<String?>>
//    val fileList : MutableState<List<String?>>

    val folderList: MutableState<Map<String?, String?>>
    val fileList: MutableState<Map<String?, String?>>

    var driveList: MutableState<List<String?>>

    fun driveList(
        list: MutableState<List<String?>>,
        currentFolder: MutableState<String?>,
        selectedPath: MutableState<String?>
    )

    //    fun downloadFile(path: String, fileName: String)
    fun downloadFile(
        selectedItemList: List<Int?>,
        decryptAlgorithm: MutableState<String?>,
        localTextFileKey: MutableState<String?>,
    )

    fun delete(
        selectedItemList: MutableState<List<Int?>>,
        selectedItemMutableList: MutableState<MutableList<Int?>>,
        driveList: MutableState<List<String?>>
    )

    fun encrypt(fileUri: String, algorithm: MutableState<String?>, key: MutableState<String?>): File

    fun decrypt(algorithm: String, file: File, key: String): File?

    fun createFolder(
        folderName: String?,
        currentFolder: MutableState<String?>,
        selectedPath: MutableState<String?>,
        selectedItemList: MutableState<List<Int?>>,
        selectedItemMutableList: MutableState<MutableList<Int?>>,
        driveList: MutableState<List<String?>>
    )

    fun backFolder(
        currentFolder: MutableState<String?>,
        selectedPath: MutableState<String?>,
    )

    fun moveFile(
        currentFolder: MutableState<String?>,
        selectedPath: MutableState<String?>,
        driveList: MutableState<List<String?>>,
        selectedItemList: MutableState<List<Int?>>,
        selectedItemMutableList: MutableState<MutableList<Int?>>,
    )
}