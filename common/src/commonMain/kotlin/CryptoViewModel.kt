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
    internal fun uploadButton() = repository.uploadButton(algorithm, keyText)

}