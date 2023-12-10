package com.example.common.view

import CryptoViewModel
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun BoxWithConstraintsScope.driveContent(
    cryptoViewModel: CryptoViewModel,
    onBackClick: () -> Unit
) {
    val maxWidth = maxWidth
    cryptoViewModel.selectedItemList.value = listOf(null)
    cryptoViewModel.selectedItemMutableList.value = mutableListOf(null)
    cryptoViewModel.driveList()
    Box(
        modifier = Modifier.fillMaxSize().padding(PaddingValues(20.dp))
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(PaddingValues(20.dp)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                stickyHeader {
                    Text("Current Folder:")
                    Text(cryptoViewModel.currentFolder.value.toString())
                    Divider(
                        modifier = Modifier.height(3.dp)
                    )
                }
                itemsIndexed(cryptoViewModel.driveList.value) { index, item ->
                    item?.let {
                        val tempList = cryptoViewModel.selectedItemList.value.find {
                            it == index
                        }
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth().clickable(true) {
                                cryptoViewModel.folderList.value[item]?.let { ref ->
                                    cryptoViewModel.selectedPath.value = ref
                                    cryptoViewModel.currentFolder.value = item
                                }
                            }
                        ) {
                            CheckBoxes(
                                selectedOption = tempList,
                                onOptionSelected = cryptoViewModel::setItemToList,
                                index = index,
                                text = it
                            )
                        }
                    }
                }
            }

            Button(
                onClick = {
                    cryptoViewModel.backFolder()
                }
            ) { Text("Back Folder") }

            Button(
                onClick = {
                    cryptoViewModel.downloadFile()
                }
            ) { Text("Download File(s)") }

            OutlinedTextField(
                modifier = Modifier
                    .width((maxWidth / 100 * 70))
                    .heightIn(max = 90.dp),
                value = cryptoViewModel.folderText.value!!,
                onValueChange = cryptoViewModel::changeFolderText,
                placeholder = { Text(text = "Folder Name") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Folder,
                        contentDescription = ""
                    )
                },
                trailingIcon = {
                    IconButton(onClick = { cryptoViewModel.changeFolderText("") }) {
                        Icon(imageVector = Icons.Default.Clear, contentDescription = "")
                    }
                }
            )

            Button(
                onClick = {
                    cryptoViewModel.createFolder()
                }
            ) { Text("Create Folder") }

            Button(
                onClick = {
                    cryptoViewModel.delete()
                }
            ) { Text("Delete") }

            Row {
                Button(
                    onClick = {
                        cryptoViewModel.selectedPathToMoveFile.value = cryptoViewModel.selectedPath.value
                    }
                ) { Text("Select Folder") }
                Button(
                    onClick = {
                        cryptoViewModel.moveFile()
                    }
                ) { Text("Move File(s)") }
            }

            Button(
                onClick = {
                    onBackClick()
                    cryptoViewModel.selectedPath.value = null
                    cryptoViewModel.currentFolder.value = "Main"
                }
            ) { Text("Back Page") }
        }
    }
}

@Composable
private fun CheckBoxes(
    selectedOption: Int?,
    onOptionSelected: (Int) -> Unit,
    index: Int,
    text: String
) {
    SelectOptionsCheckout(
        index = index,
        text = text,
        isSelectedOption = selectedOption == index,
        onSelectOption = {
            onOptionSelected(it)
        }
    )
}
