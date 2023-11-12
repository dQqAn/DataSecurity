package com.example.common.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File


@OptIn(ExperimentalPermissionsApi::class)
actual class CryptoRepository(
    private val context: Context
) : CryptoInterface {

    private val storage = Firebase.storage

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    @Composable
    private fun getMultiplePermissions() = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
        )
    )

    @Composable
    private fun checkMultiplePermissions(): Boolean = getMultiplePermissions().allPermissionsGranted

    @Composable
    override fun uploadButton(algorithm: MutableState<String?>, key: MutableState<String?>) {
        if (checkMultiplePermissions()) {
            fileManagerListeners(algorithm, key)

            Button(
                onClick = {
                    isOpenFileManager.value = true
                }
            ) { Text("Upload a file") }
        } else {
            val permissionState = getMultiplePermissions()
            Button(
                onClick = {
                    permissionState.launchMultiplePermissionRequest()
                }
            ) {
                Text("Take permissions")
            }
        }
    }

    override var selectedFile: File? = null
    private var fileUri: Uri? = null

    override val isOpenFileManager: MutableState<Boolean> = mutableStateOf(false)

    @Composable
    private fun launchFileManager(algorithm: MutableState<String?>, key: MutableState<String?>) =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
            isOpenFileManager.value = false
            fileUri = it
            uploadFile(algorithm, key)
        }

    @Composable
    private fun fileManagerListeners(algorithm: MutableState<String?>, key: MutableState<String?>) {
        val getLaunchFileManager = launchFileManager(algorithm, key)
        LaunchedEffect(isOpenFileManager.value) {
            if (isOpenFileManager.value) {
                getLaunchFileManager.launch("*/*")
            }
        }
    }

    override fun uploadFile(algorithm: MutableState<String?>, key: MutableState<String?>) {
        fileUri?.let {
            if (!algorithm.value.isNullOrEmpty() && !key.value.isNullOrEmpty()) {
                setFileLocation("Encrypted Files", it)
            } else {
                setFileLocation("Unencrypted Files", it)
            }
        }
    }

    private fun setFileLocation(path: String, uri: Uri) {
        val ref = storage.reference.child("${path}/${auth.uid}/${getFileNameFromUri(context, uri)}")
        val uploadTask = ref.putFile(uri)

        uploadTask.addOnSuccessListener {
            println("File uploaded.")
            fileUri = null
        }.addOnFailureListener {
            println(it.localizedMessage)
        }
    }

    @SuppressLint("Range")
    private fun getFileNameFromUri(context: Context, uri: Uri): String? {
        val fileName: String?
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        fileName = cursor?.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        cursor?.close()
        return fileName
    }
}