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
                val items = listOf<String>("A", "B", "C")
                itemsIndexed(items) { index, item ->
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().clickable(true) {
                            println(index)
                        }
                    ) {
                        Text(
                            modifier = Modifier,
                            text = item
                        )
                    }
                }

                stickyHeader {
                    Text("Unencrypted Files")
                }
                val items2 = listOf<String>("1", "2", "3")
                itemsIndexed(items2) { index, item ->
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth().clickable(true) {
                            println(index)
                        }
                    ) {
                        Text(
                            modifier = Modifier,
                            text = item
                        )
                    }
                }
            }

            Button(
                onClick = {

                }
            ) { Text("Download") }

            Button(
                onClick = { onBackClick() }
            ) { Text("Back") }
        }
    }
}