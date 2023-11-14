package com.example.common.view

import CryptoViewModel
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
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
    cryptoViewModel.encryptedList()
    cryptoViewModel.unencryptedList()
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
                    Text("Encrypted Files")
                }
                itemsIndexed(cryptoViewModel.encryptedList.value) { index, item ->
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().clickable(true) {
                            cryptoViewModel.downloadFile("Encrypted Files", item!!)
                        }
                    ) {
                        item?.let {
                            Text(
                                modifier = Modifier,
                                text = it
                            )
                        }
                    }
                }

                stickyHeader {
                    Text("Unencrypted Files")
                }
                itemsIndexed(cryptoViewModel.unencryptedList.value) { index, item ->
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().clickable(true) {
                            cryptoViewModel.downloadFile("Unencrypted Files", item!!)
                        }
                    ) {
                        item?.let {
                            Text(
                                modifier = Modifier,
                                text = it
                            )
                        }
                    }
                }
            }

            Button(
                onClick = { onBackClick() }
            ) { Text("Back") }
        }
    }
}