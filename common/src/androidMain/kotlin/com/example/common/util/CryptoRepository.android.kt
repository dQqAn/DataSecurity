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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
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
import javax.crypto.spec.SecretKeySpec


@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.O)
actual class CryptoRepository(
    private val context: Context
) : CryptoInterface {

    private val storage = Firebase.storage

    private val database = Firebase.database

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
    override fun uploadButton(
        algorithm: MutableState<String?>,
        key: MutableState<String?>,
        selectedPath: MutableState<String?>,
        localTextFileKey: MutableState<String?>,
    ) {
        if (checkMultiplePermissions()) {
            fileManagerListeners(algorithm, key, selectedPath, localTextFileKey)

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

    override val fileList: MutableState<Map<String?, String?>> = mutableStateOf(mapOf(null to null))
//    override val fileList: MutableState<List<String?>> = mutableStateOf(mutableListOf(null))

    override val folderList: MutableState<Map<String?, String?>> = mutableStateOf(mapOf(null to null))
//    override val folderList: MutableState<List<String?>> = mutableStateOf(mutableListOf(null))

    override var driveList: MutableState<List<String?>> = mutableStateOf(listOf(null))

    override fun driveList(
        list: MutableState<List<String?>>,
        currentFolder: MutableState<String?>,
        selectedPath: MutableState<String?>
    ) {
//        list.value = listOf(null)

        val listRef = if (selectedPath.value != null)
//            storage.reference.child("${auth.uid}/${selectedPath.value}")
            database.reference.child(selectedPath.value!!)
//        else database.reference.child("${auth.uid}")
        else database.getReference("Users").child(auth.uid!!)

        listRef.addListenerForSingleValueEvent(
            object : ValueEventListener {
                @SuppressLint("RestrictedApi")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val tempList = mutableListOf<String?>()
                        var tempFileMap: MutableMap<String?, String?> = mutableMapOf<String?, String?>()
                        var tempFolderMap: MutableMap<String?, String?> = mutableMapOf<String?, String?>()

                        val map = snapshot.value as? HashMap<*, *>
//                        val items = map.values

                        map?.forEach {
//                            println(it.key)
//                            println(it.value)

                            val itemCheck = it.value as? HashMap<*, *>
                            if (itemCheck?.get("storagePath") == null) { //foler check
                                if (itemCheck?.get("folderPath") != null && itemCheck["folderName"] != null && itemCheck["userID"] != null) { //create folder check
                                    tempList.add(it.key.toString())
                                    tempFolderMap[it.key.toString()] = listRef.path.toString() + "/" + it.key.toString()
                                    folderList.value = tempFolderMap
                                }
                            } else { // file check
                                tempList.add(itemCheck["fileName"].toString())
//                                tempFileMap[itemCheck["fileName"].toString()] = itemCheck["filePath"].toString() + "/" + itemCheck["parentKey"].toString()
                                tempFileMap[itemCheck["fileName"].toString()] =
                                    listRef.path.toString() + "/" + it.key.toString()
                                tempFileMap["StoragePath=" + itemCheck["fileName"].toString()] =
                                    itemCheck["storagePath"].toString()
//                                println(tempFileMap)
//                                println(itemCheck["fileName"].toString())
//                                println(listRef.path.toString() + "/" + it.key.toString())
//                                println(itemCheck.get("storagePath"))
                                fileList.value = tempFileMap
                            }
                        }

                        /*map.keys.forEach { key ->//folder
                            tempFolderMap = mapOf(
                                key.toString() to database.reference.path.toString()
                            )
                            tempList.add(key.toString())
                            folderList.value = tempFolderMap
                        }*/

//                        println(map)
//                        println(map.entries)
//                        println(map.values)

                        /*items.forEach {//file
                            val item = it as? HashMap<*, *>

                            item?.values?.forEach { file ->
                                val files = file as HashMap<*, *>

                                if (files["storagePath"] != null) {
                                    tempFileMap = mapOf(
                                        files["fileName"].toString() to files["filePath"].toString()
                                    )
                                    tempList.addAll(tempFileMap.keys)
                                    fileList.value = tempFileMap
                                }
                            }

                            *//*if (item["storagePath"] != null) { //file
                                tempFileMap = mapOf(
                                    item["fileName"].toString() to item["filePath"].toString()
                                )
                                tempList.addAll(tempFileMap.keys)
                                fileList.value = tempFileMap
                            }*//**//* else { //folder
                                tempFolderMap = mapOf(
                                    item["folderName"].toString() to item["folderPath"].toString()
                                )
                                tempList.addAll(tempFolderMap.keys)
                                folderList.value = tempFolderMap
                            }*//*
                        }*/

                        list.value = tempList.toList()
                        driveList.value = list.value

                        tempList.clear()
                        tempFolderMap = mutableMapOf()
                        tempFileMap = mutableMapOf()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }
            }
        )

        /*listRef.listAll()
            .addOnSuccessListener {
//                currentFolder.value = it.prefixes[0].parent?.name

                val tempList = mutableListOf<String?>()

//                var tempFolderList: List<String?> = it.prefixes.map { storageReference -> //folders
//                    storageReference.name
//                }
//                tempList.addAll(tempFolderList)
//                folderList.value = tempFolderList

                var tempFolderMap: Map<String?, String?> = it.prefixes.associate { storageReference -> //folders
                    storageReference.name to storageReference.path
                }
                tempList.addAll(tempFolderMap.keys)
//                folderList.value = tempFolderMap.keys.toList()
                folderList.value = tempFolderMap

//                var tempFileList: List<String?> = it.items.map { storageReference -> //files
//                    storageReference.name
//                }
//                tempList.addAll(tempFileList)
//                fileList.value = tempFileList

                var tempFileMap: Map<String?, String?> = it.items.associate { storageReference -> //files
                    storageReference.name to storageReference.path
                }
                tempList.addAll(tempFileMap.keys)
//                fileList.value = tempFileMap.values.toList()
                fileList.value = tempFileMap

                list.value = tempList.toList()
                driveList.value = list.value

                tempList.clear()
                tempFolderMap = mapOf()
                tempFileMap = mapOf()
//                tempFolderList = listOf(null)
//                tempFileList = listOf(null)
            }
            .addOnFailureListener {
                println(it.localizedMessage)
            }*/
    }

    override fun downloadFile(
        selectedItemList: List<Int?>,
        decryptAlgorithm: MutableState<String?>,
        localTextFileKey: MutableState<String?>,
    ) {
        //        for(index in selectedItemList[1]!!..selectedItemList.size step 1)
        selectedItemList.forEach { index ->
            if (index != null) {
                val driveRef = driveList.value[index]!!
                val fileRef = fileList.value[driveRef]!!
//
//                println(fileRef)
//                println(driveRef)
                database.getReference(fileRef).addValueEventListener(
                    object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                val map = snapshot.value as? Map<*, *>
//                                    println(map?.get("storagePath").toString())
                                if (map?.get("storagePath").toString().contains("Unencrypted Files")) {
                                    val rootPath = File(
                                        "${Environment.getExternalStorageDirectory().path}/Download",
                                        "Unencrypted Files"
                                    )
                                    if (!rootPath.exists()) {
                                        rootPath.mkdirs()
                                    }

                                    val localFile = File(rootPath, driveRef)
                                    val pathReference = storage.reference.child(map?.get("storagePath").toString())

                                    pathReference.getFile(localFile)
                                        .addOnSuccessListener {
                                            println("File downloaded.")
                                        }.addOnFailureListener {
                                            println(it.localizedMessage)
                                        }
                                } else {
                                    decryptAlgorithm.value?.let { decAlgo ->
                                        val rootPath = File(
                                            "${Environment.getExternalStorageDirectory().path}/Download",
                                            "Encrypted Files"
                                        )
                                        if (!rootPath.exists()) {
                                            rootPath.mkdirs()
                                        }

                                        val localFile = File(rootPath, driveRef)
                                        val pathReference = storage.reference.child(map?.get("storagePath").toString())

                                        pathReference.getFile(localFile)
                                            .addOnSuccessListener {
                                                val textFile = File(rootPath, "key.txt")
                                                if (fileRef.isNotEmpty() && localTextFileKey.value != null && textFile.exists()) {
                                                    val tempFile = decrypt("RC4", textFile, localTextFileKey.value!!)
                                                    val lines = tempFile?.readLines()
                                                    tempFile?.delete()
                                                    lines?.let { list ->
//                                    println(lines)
                                                        for (line in list) {
                                                            val fileName =
                                                                line.substringBefore(" & ").substringAfter("Name= ")
                                                            if (fileName == localFile.name) {
                                                                val key = line.substringAfter("Key= ")
                                                                decrypt(decAlgo, localFile, key)
                                                            }
                                                        }
                                                    }
                                                }
                                            }.addOnFailureListener {
                                                println(it.localizedMessage)
                                            }
                                    }
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            println(error.message)
                        }
                    }
                )
            }
        }
    }

    override fun delete(
        selectedItemList: MutableState<List<Int?>>,
        selectedItemMutableList: MutableState<MutableList<Int?>>,
        driveList: MutableState<List<String?>>
    ) {
        selectedItemList.value.forEach { index ->
            if (index != null) {
                val fileRef = fileList.value[driveList.value[index]]
                val folderRef = folderList.value[driveList.value[index]]

                /*val pathReference = storage.reference.child(
                    fileRef ?: (folderRef ?: "!")
                )*/

                if (fileRef != null) {
//                    println(fileRef)
//                    println(fileList.value.containsKey("StoragePath=" + driveList.value[index]))

                    database.getReference(fileRef).removeValue().addOnSuccessListener {
                        println("File reference deleted.")
                        val pathReference = fileList.value["StoragePath=" + driveList.value[index]]
                        pathReference?.let { path ->
                            storage.reference.child(path).delete().addOnSuccessListener {
                                println("File deleted.")
                                driveList.value = driveList.value.filterIndexed { i, s ->
                                    index != i
                                }
                                selectedItemMutableList.value.clear()
                                selectedItemList.value = selectedItemMutableList.value.toList()
                            }.addOnFailureListener { println(it.localizedMessage) }
                        }
                    }.addOnFailureListener { println(it.localizedMessage) }
                } else {
                    database.getReference(folderRef!!).removeValue().addOnSuccessListener {
                        println("Folder reference deleted.")
                        driveList.value = driveList.value.filterIndexed { i, s ->
                            index != i
                        }
                        selectedItemMutableList.value.clear()
                        selectedItemList.value = selectedItemMutableList.value.toList()
//                        deleteSubFiles()
                    }.addOnFailureListener { println(it.localizedMessage) }
                }
            }
        }
    }

    override fun encrypt(fileUri: String, algorithm: MutableState<String?>, key: MutableState<String?>): File {
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

//        cipher.init(Cipher.ENCRYPT_MODE, keygen, IvParameterSpec(ByteArray(cipher.blockSize)))
        cipher.init(Cipher.ENCRYPT_MODE, keygen)

        val input = context.contentResolver.openInputStream(fileUri.toUri())

        val fileExtension = fileUri.toUri().encodedPath?.substringAfterLast(".")

        val rootPath = File("${Environment.getExternalStorageDirectory().path}/Download", "Encrypted Files")
        if (!rootPath.exists()) {
            rootPath.mkdirs()
        }

        val tempFile = if (fileExtension == "txt") {
            File(rootPath, "tempKey2.txt")
        } else {
            File(
                rootPath, SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
                    .format(System.currentTimeMillis()) + "." + fileExtension
            )
        }

        /*val tempFile = File(
            context.getOutputDirectory(), SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US)
                .format(System.currentTimeMillis()) + "." + fileExtension
        )*/
        /*val tempFile = File.createTempFile(
            SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS", Locale.US).toString(), ".jpeg",
            context.getOutputDirectory()
        )*/

        val out: OutputStream = FileOutputStream(tempFile)

        CipherOutputStream(out, cipher).use {
            try {
                input?.copyTo(it)
                it.close()
            } catch (e: Exception) {
                println(e.localizedMessage)
            }
        }

        out.close()
        input?.close()

        if (fileExtension == "txt") {
            tempFile.renameTo(File(rootPath, "key.txt"))
            File(rootPath, "tempKey.txt").delete()
        }

        return tempFile
    }

    override fun getUserID(): String = auth.uid.toString()

    override fun showSelectedList(
        list: MutableState<List<String?>>,
        ref: String,
        filter: String,
        tempList: MutableList<String?>,
        tempFileMap: MutableMap<String?, String?>
    ) {
        val databaseReference = database.getReference(ref)
        databaseReference.addValueEventListener(
            object : ValueEventListener {
                @SuppressLint("RestrictedApi")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        val map = snapshot.value as? HashMap<*, *>
                        map?.forEach {//file
                            val itemCheck = it.value as? HashMap<*, *>
                            if (itemCheck?.get("storagePath") != null && itemCheck["storagePath"].toString()
                                    .contains(filter)
                            ) {
                                tempList.add(itemCheck["fileName"].toString())
                                tempFileMap[itemCheck["fileName"].toString()] =
                                    databaseReference.path.toString() + "/" + it.key.toString()
                                tempFileMap["StoragePath=" + itemCheck["fileName"].toString()] =
                                    itemCheck["storagePath"].toString()
                                fileList.value = tempFileMap
                            }
                        }

                        list.value = tempList.toList()
                        driveList.value = list.value

                        map?.forEach {//folder
                            val itemCheck = it.value as? HashMap<*, *>
                            if (itemCheck?.get("folderPath") != null) {
                                showSelectedList(
                                    list,
                                    itemCheck["folderPath"].toString(),
                                    filter,
                                    tempList,
                                    tempFileMap
                                )
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    println(error.message)
                }
            }
        )
    }

    private fun deleteSubFiles(ref: StorageReference?) {
        if (ref != null) {
            ref.listAll().addOnSuccessListener {
                it.items.forEach { ref2 ->
                    ref2.delete().addOnSuccessListener {
                        println("Sub files deleted.")
                    }.addOnFailureListener { it2 -> println(it2.localizedMessage) }
                }
                it.prefixes.forEach { ref2 ->
                    deleteSubFiles(ref2)
                }
            }.addOnFailureListener { println(it.localizedMessage) }
        }
    }

    override fun decrypt(algorithm: String, file: File, key: String): File? {
        val cipher = Cipher.getInstance(algorithm)
        val keygen = keyGen(algorithm, key)

//        cipher.init(Cipher.DECRYPT_MODE, keygen, IvParameterSpec(ByteArray(cipher.blockSize)))
        cipher.init(Cipher.DECRYPT_MODE, keygen)

        val inputStream = context.contentResolver.openInputStream(file.toUri())

        //second way, https://gist.github.com/gsandaru/b758abe3ebd6b24e599db43c2cbce1f1
//        val inputStream: FileInputStream = FileInputStream(file)
//        val inputBytes = ByteArray(file.length().toInt())
//        inputStream.read(inputBytes)
//        val outputBytes = cipher.doFinal(inputBytes)
//        output.write(outputBytes)

        val tempFolder = if (file.extension == "txt") {
            "Encrypted Files"
        } else {
            "Unencrypted Files"
        }

        val rootPath = File("${Environment.getExternalStorageDirectory().path}/Download", tempFolder)
        if (!rootPath.exists()) {
            rootPath.mkdirs()
        }
        val fileName = if (file.extension == "txt") {
            "tempKey.txt"
        } else {
            file.name
        }
        val localFile = File(rootPath, fileName)
//        localFile.createNewFile()

        val output = FileOutputStream(localFile)

        CipherInputStream(inputStream, cipher).use {
            try {
                /*if (file.extension == "txt") {
                    println(localFile.name)
                    println(file.name)
                }*/
                it.copyTo(output)
                println("File decrypted.")
            } catch (e: Exception) {
                localFile.delete()
                println(e.localizedMessage)
            }
            it.close()
        }
//        file.delete()
        output.close()
        inputStream?.close()

        /*if (file.extension == "txt") {
            localFile.renameTo(File(rootPath, "key.txt"))
        }*/

        return localFile
    }

    override fun createFolder(
        folderName: String?,
        currentFolder: MutableState<String?>,
        selectedPath: MutableState<String?>,
        selectedItemList: MutableState<List<Int?>>,
        selectedItemMutableList: MutableState<MutableList<Int?>>,
        driveList: MutableState<List<String?>>
    ) {
        if (!folderName.isNullOrEmpty()) {

            selectedPath.value = if (selectedPath.value == null) {
                "/Users/${auth.uid}"
            } else selectedPath.value

            val path = selectedPath.value!!

            val databaseRef = database.getReference("${path}/${folderName}")

            val map = hashMapOf(
                "folderName" to folderName,
                "folderPath" to "${path}/${folderName}",
                "userID" to auth.uid
            )

            databaseRef.setValue(map).addOnSuccessListener {
                println("Folder created.")
                driveList(driveList, currentFolder, selectedPath)
                selectedItemMutableList.value.clear()
                selectedItemList.value = selectedItemMutableList.value.toList()
            }.addOnFailureListener { println(it.localizedMessage) }

            /*val tempFile = File.createTempFile(
                "PlaceHolder",
                null,
//            context.getOutputDirectory() //Environment.getExternalStorageDirectory()
            )

//        val ref = storage.reference.child("${auth.uid}/${"currentPath"}/${folderName}/PlaceHolder")
            val ref = storage.reference.child("${selectedPath.value}/${folderName}/PlaceHolder")

            val uploadTask = ref.putFile(tempFile.toUri())

            uploadTask.addOnSuccessListener {
                println("Folder created.")
                driveList(driveList, currentFolder, selectedPath)
                selectedItemMutableList.value.clear()
                selectedItemList.value = selectedItemMutableList.value.toList()
            }.addOnFailureListener {
                println(it.localizedMessage)
            }*/
        } else {
            println("Entries are empty.")
        }
    }

    override fun backFolder(
        currentFolder: MutableState<String?>,
        selectedPath: MutableState<String?>,
    ) {
        if (selectedPath.value?.count { it == '/' } != 1 && currentFolder.value != "Main") {
            selectedPath.value = selectedPath.value?.substringBeforeLast("/")
            currentFolder.value =
                if (selectedPath.value?.count { it == '/' } != 1 && selectedPath.value?.substringAfterLast("/") != auth.uid) {
                    selectedPath.value?.substringAfterLast("/")
                } else {
                    "Main"
                }
        }
    }

    override fun moveItem(
        currentFolder: MutableState<String?>,
        selectedPath: MutableState<String?>,
        driveList: MutableState<List<String?>>,
        selectedItemList: MutableState<List<Int?>>,
        selectedItemMutableList: MutableState<MutableList<Int?>>,
    ) {

        /*val rootPath = File("${Environment.getExternalStorageDirectory().path}/Download", "Temp Files")
        if (!rootPath.exists()) {
            rootPath.mkdirs()
        }*/

        selectedPath.value = if (selectedPath.value == null) {
            "/Users/${auth.uid}"
        } else selectedPath.value

//        println(selectedPath.value)

        selectedPath.value?.let { path ->
            selectedItemList.value.forEach { index ->
                if (index != null) {
                    val driveRef = driveList.value[index]
                    val fileRef = fileList.value[driveRef]
                    val folderRef = folderList.value[driveRef]

                    val pathReference = database.reference.child(
                        fileRef ?: (folderRef ?: "!")
                    )

                    pathReference.addListenerForSingleValueEvent(
                        object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {

                                    val databaseRef = database.getReference(path).push()
                                    databaseRef.key?.let {

                                        val map = snapshot.value as HashMap<*, *>
                                        val newMap = hashMapOf(
                                            "fileName" to map["fileName"],
                                            "filePath" to path,
                                            "storagePath" to map["storagePath"],
                                            "userID" to auth.uid,
                                            "parentKey" to it
                                        )

                                        databaseRef.setValue(newMap).addOnSuccessListener {
                                            pathReference.removeValue().addOnSuccessListener {
                                                driveList.value = driveList.value.filterIndexed { i, s ->
                                                    index != i
                                                }
                                                selectedItemMutableList.value.clear()
                                                selectedItemList.value = selectedItemMutableList.value.toList()
                                                println("Item moved.")
                                            }.addOnFailureListener { println(it.message) }
                                        }.addOnFailureListener { println(it.message) }
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                println(error.message)
                            }
                        }
                    )

                    /*val localFile = File(rootPath, driveRef)
                    val pathReference = storage.reference.child(fileRef)
                    pathReference.getFile(localFile)
                        .addOnSuccessListener {
                            val uri =
                                FileProvider.getUriForFile(context, context.packageName + ".provider", localFile)
                            setFileLocation(path, uri)
                            localFile.delete()

                            pathReference.delete().addOnSuccessListener {
                                println("File(s) deleted.")
                                driveList.value = driveList.value.filterIndexed { i, s ->
                                    index != i
                                }
                                selectedItemMutableList.value.clear()
                                selectedItemList.value = selectedItemMutableList.value.toList()
                            }.addOnFailureListener { println(it.localizedMessage) }
                        }.addOnFailureListener {
                            println(it.localizedMessage)
                        }*/
                }
            }
            selectedPath.value = null
        }
    }

    @Composable
    private fun launchFileManager(
        algorithm: MutableState<String?>,
        key: MutableState<String?>,
        selectedPath: MutableState<String?>,
        localTextFileKey: MutableState<String?>,
    ) =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
            isOpenFileManager.value = false
            fileUri = it
            uploadFile(algorithm, key, selectedPath, localTextFileKey)
        }

    @Composable
    private fun fileManagerListeners(
        algorithm: MutableState<String?>,
        key: MutableState<String?>,
        selectedPath: MutableState<String?>,
        localTextFileKey: MutableState<String?>,
    ) {
        val getLaunchFileManager = launchFileManager(algorithm, key, selectedPath, localTextFileKey)
        LaunchedEffect(isOpenFileManager.value) {
            if (isOpenFileManager.value) {
                getLaunchFileManager.launch("*/*")
            }
        }
    }

    override fun uploadFile(
        algorithm: MutableState<String?>,
        key: MutableState<String?>,
        selectedPath: MutableState<String?>,
        localTextFileKey: MutableState<String?>,
    ) {
        fileUri?.let { it ->
            if (!algorithm.value.isNullOrEmpty() && !key.value.isNullOrEmpty() && !localTextFileKey.value.isNullOrEmpty()) {

                val tempFile = encrypt(it.toString(), algorithm, key)

                val encryptedUri =
                    FileProvider.getUriForFile(context, context.packageName + ".provider", tempFile)
                if (selectedPath.value != null) {
                    setFileLocation(selectedPath.value!!, encryptedUri)
                } else {
                    setFileLocation("Encrypted Files", encryptedUri)
                }

                val rootPath = File("${Environment.getExternalStorageDirectory().path}/Download", "Encrypted Files")
                if (!rootPath.exists()) {
                    rootPath.mkdirs()
                }

                val textFile = File(rootPath, "key.txt")
                if (textFile.exists()) {
                    val decryptedFile = decrypt("RC4", textFile, localTextFileKey.value!!) //ARC4 & RC4
                    decryptedFile?.appendText("Name= " + tempFile.name + " & " + "Key= " + key.value!! + "\n\r")
                    encrypt(decryptedFile?.toUri().toString(), mutableStateOf("RC4"), localTextFileKey)
                } else {
                    textFile.appendText("Name= " + tempFile.name + " & " + "Key= " + key.value!! + "\n\r")
                    encrypt(textFile.toUri().toString(), mutableStateOf("RC4"), localTextFileKey)
                }
            } else {
                if (selectedPath.value != null) {
                    setFileLocation(selectedPath.value!!, it)
                } else {
                    setFileLocation("Unencrypted Files", it)
                }
            }
        }
    }

    private fun keyGen(algorithm: String, key: String): SecretKeySpec {
//        val digest=MessageDigest.getInstance("SHA-256") //32 byte
//        val digest=MessageDigest.getInstance("SHA-1") //20 byte
        val digest = MessageDigest.getInstance("MD5") //16 byte

        var bytes = key.toByteArray(Charsets.UTF_8)
        val lastKey: ByteArray
        if (algorithm == "AES" || algorithm == "BLOWFISH" || algorithm == "ARC4" || algorithm == "RC4") {
            digest.update(bytes, 0, bytes.size)
            lastKey = digest.digest()
        } else {
            bytes = digest.digest(bytes)
            bytes = bytes.copyOf(16)
            val factory = SecretKeyFactory.getInstance("DES")
            lastKey = factory.generateSecret(DESKeySpec(bytes)).encoded
        }

        /*// create new key
        val secretKey = SecretKeySpec(lastKey, algorithm)
        // get base64 encoded version of the key
        val encodedKey = Base64.getEncoder().encodeToString(secretKey.encoded)
        println("$algorithm and Key: $encodedKey")*/

        return SecretKeySpec(lastKey, algorithm)
    }

    private fun setFileLocation(path: String, uri: Uri) {
        val checkedPath = if (path.count { it == '/' } > 0) {
            "${"Users"}/${path}"
        } else {
            "${"Users"}/${auth.uid}/${path}"
        }

        val tempFolderMap = hashMapOf<String, Any>(
            "folderName" to path,
            "folderPath" to checkedPath,
            "userID" to auth.uid.toString()
        )
        database.getReference(checkedPath).updateChildren(tempFolderMap)

        val databaseRef = database.getReference(checkedPath).push()

        /*val folderMap=hashMapOf(
            "folderName" to path.substringAfterLast("/"),
            "folderPath" to "/"+auth.uid+"/"+path,
            "userID" to auth.uid,
        )
        database.getReference("${"Users"}/${auth.uid}/${path}").setValue(folderMap)*/

        databaseRef.key?.let { key ->
            val storageRef = storage.reference.child("${auth.uid}/${path}/${getFileNameFromUri(context, uri)}")
            val storageUploadTask = storageRef.putFile(uri)
            storageUploadTask.addOnSuccessListener {
                println("File uploaded.")
                fileUri = null

                val map = hashMapOf(
                    "fileName" to getFileNameFromUri(context, uri),
                    "filePath" to checkedPath,
                    "storagePath" to it.storage.path,
                    "userID" to auth.uid,
                    "parentKey" to key
                )
                val uploadTask = databaseRef.setValue(map)

                uploadTask.addOnSuccessListener {
                    println("Data uploaded.")
                }.addOnFailureListener {
                    println(it.localizedMessage)
                }
            }.addOnFailureListener {
                println(it.localizedMessage)
            }
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