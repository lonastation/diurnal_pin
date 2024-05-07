package com.linn.pin.ui.work

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.linn.pin.data.Work
import com.linn.pin.ui.AppViewModelProvider
import com.linn.pin.ui.theme.PinTheme
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun WorkScreen(
    navController: NavController,
    viewModel: WorkViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val workUiState by viewModel.workUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    PinTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            WorkBody(
                workList = workUiState.itemList,
                onDingClick = {
                    coroutineScope.launch {
                        viewModel.ding()
                    }
                },
                modifier = Modifier
                    .padding(15.dp)
                    .fillMaxSize()
            )
        }
    }
}

@Composable
private fun WorkBody(
    workList: List<Work>,
    onDingClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        if (workList.isEmpty()) {
            Text(
                text = "--",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge
            )
            DingButton(onClick = onDingClick)
        } else {
            WorkList(
                workList = workList,
                modifier = modifier
            )
            DingButton(onClick = onDingClick)
        }
    }
}

@Composable
fun DingButton(onClick: () -> Unit) {
    Button(onClick = { onClick() }) {
        Text("Ding")
    }
}

@Composable
private fun WorkList(workList: List<Work>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(items = workList, key = { it.id }) { item ->
            WorkItem(item = item, modifier)
        }
    }
}

@Composable
private fun WorkItem(
    item: Work, modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Top
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = covert2String(item.createTime),
                    style = MaterialTheme.typography.titleLarge,
                )
            }
        }
    }
}

private fun covert2String(date: LocalDateTime): String {
    return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
}