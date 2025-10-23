package com.linn.pin.ui.work

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.linn.pin.data.work.Work
import com.linn.pin.ui.AppViewModelProvider
import com.linn.pin.ui.theme.PinTheme
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun WorkScreen(
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
                        viewModel.reloadList(it)
                    }
                }
            )
        }
    }
}

@Composable
private fun WorkBody(
    selectedTab: WorkTabType,
    workList: List<Work>,
    onDingClick: () -> Unit,
    onFilterClick: (type: WorkTabType) -> Unit,
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onDingClick() },
                shape = CircleShape,
                modifier = Modifier.offset(x = (-20).dp, y = (-20).dp)
            ) {
                Text(text = "Ding")
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color(0xFF5AB2FF),
                            0.16f to Color.White
                        )
                    ),
                    alpha = 0.16f
                )
        ) {
            WorkTabGroup(
                selectedTab = selectedTab,
                onFilterClick = onFilterClick,
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(start = 16.dp)
            ) {
                if (workList.isEmpty()) {
                    Text(
                        text = "Hooray! No works here!\r\nHappy vacation\uD83E\uDD70",
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontSize = 14.sp,
                        lineHeight = 40.sp,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 200.dp)
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
fun WorkTabGroup(
    selectedTab: WorkTabType,
    onFilterClick: (type: WorkTabType) -> Unit
) {
    Column(
        modifier = Modifier.padding(top = 10.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row {
            WorkTabChip(
                WorkTabType.FOURTEEN,
                selectedTab == WorkTabType.FOURTEEN,
                onFilterClick = onFilterClick,
            )
            WorkTabChip(
                WorkTabType.THIRTY,
                selectedTab == WorkTabType.THIRTY,
                onFilterClick = onFilterClick,
            )
            WorkTabChip(
                WorkTabType.NINETY,
                selectedTab == WorkTabType.NINETY,
                onFilterClick = onFilterClick,
            )
        }
    }
}

@Composable
fun WorkTabChip(
    selectedTab: WorkTabType,
    selected: Boolean,
    onFilterClick: (type: WorkTabType) -> Unit
) {
    Button(
        onClick = { onFilterClick(selectedTab) },
        colors = ButtonColors(
            containerColor = Color.Transparent,
            contentColor = Color.Black,
            disabledContainerColor = Color.Red,
            disabledContentColor = Color.Blue,
        ),
        shape = RectangleShape,
        contentPadding = PaddingValues(start = 18.dp, end = 30.dp, top = 8.dp, bottom = 8.dp),
        modifier = Modifier
    ) {
        if (selected) {
            Icon(
                imageVector = Icons.Filled.Done,
                contentDescription = "Done icon",
                modifier = Modifier
                    .size(22.dp)
                    .padding(end = 6.dp),
                tint = Color(0xFF3D7EFF)
            )
        }
        Text(
            text = selectedTab.text,
            color = if (selected) {
                Color(0xFF3D7EFF)
            } else {
                Color.Black
            }
        )
    }
}

@Preview
@Composable
fun WorkBodyPreview() {
    PinTheme {
        WorkBody(
            WorkTabType.FOURTEEN,
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
        WorkBody(WorkTabType.FOURTEEN, workList = listOf(), onDingClick = {}, onFilterClick = {})
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
    return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH))
}