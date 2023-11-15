package com.example.common.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.OpenableColumns
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


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

    override fun encryptedList(list: MutableState<List<String?>>) {
        val listRef = storage.reference.child("Encrypted Files/${auth.uid}")
        listRef.listAll()
            .addOnSuccessListener {
                list.value = it.items.map { storageReference ->
                    storageReference.name
                }
            }
            .addOnFailureListener {
                println(it.localizedMessage)
            }
    }

    override fun unencryptedList(list: MutableState<List<String?>>) {
        val listRef = storage.reference.child("Unencrypted Files/${auth.uid}")
        listRef.listAll()
            .addOnSuccessListener {
                list.value = it.items.map { storageReference ->
                    storageReference.name
                }
            }
            .addOnFailureListener {
                println(it.localizedMessage)
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun downloadFile(category: String, fileName: String) {
        val pathReference = storage.reference.child("${category}/${auth.uid}/${fileName}")
        val rootPath = File("${Environment.getExternalStorageDirectory().path}/Download", category)
        if (!rootPath.exists()) {
            rootPath.mkdirs()
        }
        val localFile = File(rootPath, fileName)
        pathReference.getFile(localFile)
            .addOnSuccessListener {
                decrypt("AES", localFile, "asd")
            }.addOnFailureListener {
                println(it.localizedMessage)
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun decrypt(algorithm: String, file: File, key: String) {
        val cipher = Cipher.getInstance(algorithm)
        val keygen = keyGen(algorithm, key)

        cipher.init(Cipher.DECRYPT_MODE, keygen, IvParameterSpec(ByteArray(cipher.blockSize)))

        val inputStream = context.contentResolver.openInputStream(file.toUri())

        //second way, https://gist.github.com/gsandaru/b758abe3ebd6b24e599db43c2cbce1f1
//        val inputStream: FileInputStream = FileInputStream(file)
//        val inputBytes = ByteArray(file.length().toInt())
//        inputStream.read(inputBytes)
//        val outputBytes = cipher.doFinal(inputBytes)

        val rootPath = File("${Environment.getExternalStorageDirectory().path}/Download", "Unencrypted Files")
        if (!rootPath.exists()) {
            rootPath.mkdirs()
        }
        val localFile = File(rootPath, file.name)
        val output = FileOutputStream(localFile)

//        output.write(outputBytes)

        CipherInputStream(inputStream, cipher).use {
            try {
                it.copyTo(output)
                println("File decrypted.")
            } catch (e: Exception) {
                println(e.localizedMessage)
            }
            it.close()
        }
        output.close()
        inputStream?.close()
    }

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
        fileUri?.let { it ->
            if (!algorithm.value.isNullOrEmpty() && !key.value.isNullOrEmpty()) {

                //DES
                /*var charac = key.value!!.toByteArray(Charsets.UTF_8)
                        val md = MessageDigest.getInstance("SHA-1")
                        charac = md.digest(charac)
                        charac = charac.copyOf(16)
                        val factory = SecretKeyFactory.getInstance("DES")
                        val keygen = factory.generateSecret(DESKeySpec(charac))*/
                //aes
//                val skeySpec = SecretKeySpec(key.value!!.substring(0, 32).toByteArray(), "AES")
//                val ivSpec = IvParameterSpec(IV.substring(0, 16).getBytes())

                //aes
                /*val salt: ByteArray = random.generateSeed(128 / 8)
                val keySpec = PBEKeySpec(key.value!!.toCharArray(), salt, 872791, 128)
                val secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512", "BC")
                val passwordKey = secretKeyFactory.generateSecret(keySpec)
                cipher.init(Cipher.ENCRYPT_MODE, passwordKey, PBEParameterSpec(salt, 872791)) */

                //blowfish
                //min 4 bytes (32 bits), max56 bytes (448 bits)
//                val keygen = SecretKeySpec(key.value!!.toByteArray(), algorithm.value)

                val cipher = Cipher.getInstance(algorithm.value)
                val keygen = keyGen(algorithm.value!!, key.value!!)

                cipher.init(Cipher.ENCRYPT_MODE, keygen, IvParameterSpec(ByteArray(cipher.blockSize)))

                val input = context.contentResolver.openInputStream(it)

                val tempFile = File(
                    context.getOutputDirectory(), SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
                        .format(System.currentTimeMillis()) + ".jpeg"
                )
                /*val tempFile = File.createTempFile(
                    SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US).toString(), ".jpeg",
                    context.getOutputDirectory()
                )*/

                val out: OutputStream = FileOutputStream(tempFile)

                CipherOutputStream(out, cipher).use {
                    input?.copyTo(it)
                    it.close()
                }
                out.close()
                input?.close()

                val encryptedUri =
                    FileProvider.getUriForFile(context, context.packageName + ".provider", tempFile)
                setFileLocation("Encrypted Files", encryptedUri)
            } else {
                setFileLocation("Unencrypted Files", it)
            }
        }
    }

    private fun keyGen(algorithm: String, key: String): SecretKeySpec {
//        val digest=MessageDigest.getInstance("SHA-256") //32 byte
//        val digest=MessageDigest.getInstance("SHA-1") //20 byte
        val digest = MessageDigest.getInstance("MD5") //16 byte

        var bytes = key.toByteArray(Charsets.UTF_8)
        val lastKey: ByteArray
        if (algorithm == "AES" || algorithm == "BLOWFISH") {
            digest.update(bytes, 0, bytes.size)
            lastKey = digest.digest()
        } else {
            bytes = digest.digest(bytes)
            bytes = bytes.copyOf(16)
            val factory = SecretKeyFactory.getInstance("DES")
            lastKey = factory.generateSecret(DESKeySpec(bytes)).encoded
        }

        return SecretKeySpec(lastKey, algorithm)
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

    private fun Context.getOutputDirectory(): File {
        val mediaDir = this.externalMediaDirs.firstOrNull()?.let {
            File(it, "demo").apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else this.filesDir
    }
}