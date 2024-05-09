package com.linn.pin.ui.work

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LargeFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.linn.pin.data.work.Work
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
    val coroutineScope = rememberCoroutineScope()
    val listUiState by viewModel.listUiState.collectAsState()
    PinTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            WorkBody(
                selectedTab = viewModel.tabUiState.selectedTab,
                workList = listUiState.itemList,
                onDingClick = {
                    coroutineScope.launch {
                        viewModel.ding()
                    }
                },
                onFilterClick = {
                    coroutineScope.launch {
                        Log.i("tab index", it.toString())
                        viewModel.reloadList(it)
                    }
                }
            )
        }
    }
}

@Composable
private fun WorkBody(
    selectedTab: Int,
    workList: List<Work>,
    onDingClick: () -> Unit,
    onFilterClick: (days: Int) -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            LargeFloatingActionButton(
                onClick = { onDingClick() },
                shape = CircleShape,
                modifier = Modifier.offset(x = (-20).dp, y = (-20).dp)
            ) {
                Text(text = "Ding")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            TabGroup(
                selectedTab = selectedTab,
                onFilterClick = onFilterClick,
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(start = 16.dp)
            ) {
                if (workList.isEmpty()) {
                    Text(
                        text = "--- nothing found ---",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 6.dp)
                    )
                } else {
                    WorkList(
                        workList = workList,
                    )
                }
            }
        }
    }
}

@Composable
fun TabGroup(
    selectedTab: Int = 14,
    onFilterClick: (count: Int) -> Unit,
) {
    Column(
        modifier = Modifier.padding(top = 10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row {
            TabChip(
                14,
                selectedTab == 14,
                onFilterClick = onFilterClick,
            )
            TabChip(
                30,
                selectedTab == 30,
                onFilterClick = onFilterClick,
            )
            TabChip(
                90,
                selectedTab == 90,
                onFilterClick = onFilterClick,
            )
        }
    }
}

@Composable
fun TabChip(
    pageSize: Int,
    selected: Boolean,
    onFilterClick: (count: Int) -> Unit,
) {
    FilterChip(
        modifier = Modifier.padding(start = 16.dp),
        onClick = {
            onFilterClick(pageSize)
        },
        label = {
            Text("Latest $pageSize")
        },
        selected = selected,
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
    )
}

@Preview
@Composable
fun WorkBodyPreview() {
    PinTheme {
        WorkBody(
            14,
            workList = listOf(
                Work(id = 1, createTime = LocalDateTime.now()),
                Work(id = 2, createTime = LocalDateTime.now().minusDays(1L)),
            ), onDingClick = {}, onFilterClick = {})
    }
}

@Preview
@Composable
fun WorkBodyEmptyListPreview() {
    PinTheme {
        WorkBody(14, workList = listOf(), onDingClick = {}, onFilterClick = {})
    }
}

@Composable
private fun WorkList(workList: List<Work>) {
    LazyColumn(modifier = Modifier.padding(top = 6.dp)) {
        items(items = workList, key = { it.id }) { item ->
            WorkItem(item = item)
        }
    }
}

@Composable
private fun WorkItem(
    item: Work
) {
    Column(
        modifier = Modifier.padding(top = 4.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = covert2String(item.createTime),
            )
        }
    }
}

private fun covert2String(date: LocalDateTime): String {
    return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
}