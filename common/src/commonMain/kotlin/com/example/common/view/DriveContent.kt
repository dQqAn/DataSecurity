package com.example.common.view

import CryptoViewModel
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
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
//    cryptoViewModel.encryptedList()
//    cryptoViewModel.unencryptedList()
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
//                            cryptoViewModel.downloadFile("Unencrypted Files", item!!)
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

                }
            ) { Text("Back Folder") }

            Button(
                onClick = {

                }
            ) { Text("Create Folder") }

            Button(
                onClick = {

                }
            ) { Text("Delete Folder / File") }

            Button(
                onClick = {

                }
            ) { Text("Move File") }

            Button(
                onClick = { onBackClick() }
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
