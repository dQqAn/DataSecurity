import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.common.util.CryptoInterface
import com.example.common.util.ViewModel

class CryptoViewModel(
    private val repository: CryptoInterface,
) : ViewModel() {
    private var _keyText: MutableState<String?> = mutableStateOf("")
    internal val keyText = _keyText
    internal fun changeKeyText(key: String) {
        _keyText.value = key
    }

    internal val selectedOption = mutableStateOf(-1)
    internal fun changeSelectedOption(option: Int) {
        selectedOption.value = option
    }

    private val algorithm: MutableState<String?> = mutableStateOf(null)
    internal fun changeAlgorithm(method: String?) {
        algorithm.value = method
    }

    @Composable
    internal fun uploadButton() = repository.uploadButton(algorithm, keyText, selectedPath)

    val folderList: MutableState<Map<String?, String?>> = repository.folderList
//    val folderList: MutableState<List<String?>> = repository.folderList

    val fileList: MutableState<Map<String?, String?>> = repository.fileList
//    val fileList: MutableState<List<String?>> = repository.fileList

    val selectedItem = mutableStateOf(-1)
    internal fun changeSelectedItem(option: Int) {
        selectedItem.value = option
    }

    internal val decryptSelectedOption = mutableStateOf(-1)
    internal fun changeDecryptSelectedOption(option: Int) {
        decryptSelectedOption.value = option
    }

    private val decryptAlgorithm: MutableState<String?> = mutableStateOf(null)
    internal fun changeDecryptAlgorithm(method: String?) {
        decryptAlgorithm.value = method
    }

    val selectedItemMutableList: MutableState<MutableList<Int?>> = mutableStateOf(mutableListOf(null))
    val selectedItemList: MutableState<List<Int?>> = mutableStateOf(selectedItemMutableList.value.toList())
    internal fun addItemToList(option: Int) {
        selectedItemMutableList.value.add(option)
    }

    internal fun removeItemToList(option: Int) {
        selectedItemMutableList.value.remove(option)
    }

    internal fun setItemToList(option: Int) {
        val tempInt = selectedItemMutableList.value.find {
            it == option
        }
        if (tempInt != null) {
//            println("A: " + tempInt)
            selectedItemMutableList.value.remove(option)
        } else {
//            println("B: " + option)
            selectedItemMutableList.value.add(option)
        }
        selectedItemList.value = selectedItemMutableList.value.toList()
//        println(selectedItemMutableList.value)
    }

    val currentFolder: MutableState<String?> = mutableStateOf("Main")
    val selectedPath: MutableState<String?> = mutableStateOf(null)
    val selectedPathToMoveFile: MutableState<String?> = mutableStateOf(null)
    val driveList: MutableState<List<String?>> = mutableStateOf(mutableListOf(null))
    fun driveList() = repository.driveList(driveList, currentFolder, selectedPath)

    //    fun downloadFile(path: String, fileName: String) = repository.downloadFile(path, fileName)
    //    fun downloadFile(path: String, fileName: String) = repository.downloadFile(path, fileName)
    fun downloadFile() = repository.downloadFile(selectedItemList.value.toList(), decryptAlgorithm)

    fun delete() = repository.delete(selectedItemList, selectedItemMutableList, driveList)

    private var _folderText: MutableState<String?> = mutableStateOf("")
    internal val folderText = _folderText
    internal fun changeFolderText(name: String) {
        _folderText.value = name
    }

    fun createFolder() = repository.createFolder(
        _folderText.value,
        currentFolder,
        selectedPath,
        selectedItemList,
        selectedItemMutableList,
        driveList
    )

    fun backFolder() = repository.backFolder(currentFolder, selectedPath)

    fun moveFile() = repository.moveFile(
        currentFolder,
        selectedPathToMoveFile,
        driveList,
        selectedItemList,
        selectedItemMutableList,
    )
}