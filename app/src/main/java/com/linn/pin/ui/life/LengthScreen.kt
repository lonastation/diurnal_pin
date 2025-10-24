package com.linn.pin.ui.life

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.linn.pin.data.length.Length
import com.linn.pin.ui.AppViewModelProvider
import com.linn.pin.ui.theme.LightBlue40
import com.linn.pin.ui.theme.LightBlue60
import com.linn.pin.ui.theme.LightBlue80
import com.linn.pin.ui.theme.PinTheme
import com.linn.pin.ui.theme.Purple40
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun LifeScreen(
    viewModel: LengthViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val listUiState by viewModel.listUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    PinTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LifeBody(
                selectedTab = viewModel.tabUiState.selectedTab,
                selectedFilter = viewModel.tabUiState.selectedFilter,
                onFilterClick = { tabType: LengthTabType, filterType: LengthFilterType ->
                    coroutineScope.launch {
                        viewModel.reloadLengthList(tabType, filterType)
                    }
                },
                itemList = listUiState.itemList,
                itemUiState = viewModel.itemUiState,
                onValueChange = viewModel::updateItemUiState,
                onAddConfirm = {
                    coroutineScope.launch {
                        viewModel.insertLength()
                    }
                }
            )
        }
    }
}

@Composable
private fun LifeBody(
    selectedTab: LengthTabType,
    selectedFilter: LengthFilterType,
    onFilterClick: (tabType: LengthTabType, filterType: LengthFilterType) -> Unit,
    itemList: List<Length>,
    itemUiState: ItemUiState,
    onValueChange: (ItemDetails) -> Unit,
    onAddConfirm: () -> Unit
) {
    var addConfirmationRequired by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    addConfirmationRequired = true
                },
                modifier = Modifier.offset(x = (-20).dp, y = (-20).dp)
            ) {
                Icon(Icons.Filled.Add, "Floating action button.")
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
                            Pair(0.0f, LightBlue40),
                            Pair(0.2f, Color.White)
                        )
                    ),
                    alpha = 0.16f
                )
        ) {
            LengthTabGroup(
                selectedTab = selectedTab,
                onFilterClick = onFilterClick
            )
            LengthFilterGroup(
                selectedTab = selectedTab,
                selectedFilter = selectedFilter,
                onFilterClick = onFilterClick
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(innerPadding)
            ) {
                if (itemList.isEmpty()) {
                    Text(
                        text = "Nothing found in the local database.\r\nCreate your first record for life \uD83E\uDD75",
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        fontSize = 14.sp,
                        lineHeight = 40.sp,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 200.dp)
                    )
                } else {
                    if (selectedTab == LengthTabType.CHART) {
                        StatisticsContent(
                            dataPoints = itemList.map { it.number },
                            labels = listOf(
                                itemList.first().createTime.format(
                                    DateTimeFormatter.ofPattern(
                                        "yyyy/MM/dd"
                                    )
                                ),
                                itemList.last().createTime.format(
                                    DateTimeFormatter.ofPattern(
                                        "yyyy/MM/dd"
                                    )
                                ),
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(280.dp)
                                .padding(16.dp),
                            lineColor = LightBlue60,
                            backgroundColor = Color.Transparent
                        )
                    } else {
                        LengthList(
                            lengthList = itemList
                        )
                    }
                }
            }
        }
    }
    if (addConfirmationRequired) {
        AddItemDialog(
            itemDetails = itemUiState.itemDetails,
            onValueChange = onValueChange,
            onDismissRequest = { addConfirmationRequired = false },
            onConfirmation = {
                addConfirmationRequired = false
                onAddConfirm()
            },
        )
    }
}

@Preview
@Composable
fun LifeBodyEmptyPreview() {
    PinTheme {
        LifeBody(
            selectedTab = LengthTabType.DATA,
            selectedFilter = LengthFilterType.ALL,
            onFilterClick = { _, _ -> {} },
            itemUiState = ItemUiState(),
            itemList = listOf(),
            onValueChange = {},
            onAddConfirm = {}
        )
    }
}

@Preview
@Composable
fun LifeBodyPreview() {
    PinTheme {
        LifeBody(
            selectedTab = LengthTabType.DATA,
            selectedFilter = LengthFilterType.ALL,
            onFilterClick = { _, _ -> run {} },
            itemUiState = ItemUiState(),
            itemList = listOf(
                Length(id = 1, createTime = LocalDateTime.now().minusDays(7L), number = 87.1f),
                Length(
                    id = 2,
                    createTime = LocalDateTime.now().minusDays(6L),
                    number = 85.0f,
                ),
                Length(
                    id = 3,
                    createTime = LocalDateTime.now().minusDays(5L),
                    number = 88.4f,
                )
            ),
            onValueChange = {},
            onAddConfirm = {},
        )
    }
}

@Preview
@Composable
fun LifeBodyChartPreview() {
    PinTheme {
        LifeBody(
            selectedTab = LengthTabType.CHART,
            selectedFilter = LengthFilterType.ALL,
            onFilterClick = { _, _ -> run {} },
            itemUiState = ItemUiState(),
            itemList = listOf(
                Length(id = 1, createTime = LocalDateTime.now().minusDays(7L), number = 87.1f),
                Length(
                    id = 2,
                    createTime = LocalDateTime.now().minusDays(6L),
                    number = 85.0f,
                ),
                Length(
                    id = 3,
                    createTime = LocalDateTime.now().minusDays(5L),
                    number = 88.4f,
                ),
                Length(
                    id = 4,
                    createTime = LocalDateTime.now().minusDays(4L),
                    number = 89.4f,
                ),
                Length(
                    id = 5,
                    createTime = LocalDateTime.now().minusDays(3L),
                    number = 90.4f,
                ),
                Length(
                    id = 6,
                    createTime = LocalDateTime.now().minusDays(2L),
                    number = 91.4f,
                ),
                Length(
                    id = 7,
                    createTime = LocalDateTime.now().minusDays(1L),
                    number = 90.2f,
                )
            ),
            onValueChange = {},
            onAddConfirm = {},
        )
    }
}

@Composable
fun LengthFilterGroup(
    selectedTab: LengthTabType,
    selectedFilter: LengthFilterType = LengthFilterType.ALL,
    onFilterClick: (tabType: LengthTabType, filterType: LengthFilterType) -> Unit
) {
    if (selectedTab == LengthTabType.CHART) {
        Column(
            modifier = Modifier.padding(top = 10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Row {
                LengthFilterChip(
                    LengthTabType.CHART,
                    filterType = LengthFilterType.ALL,
                    selectedFilter == LengthFilterType.ALL,
                    onFilterClick = onFilterClick,
                )
                LengthFilterChip(
                    LengthTabType.CHART,
                    filterType = LengthFilterType.LAST_30,
                    selectedFilter == LengthFilterType.LAST_30,
                    onFilterClick = onFilterClick,
                )
                LengthFilterChip(
                    LengthTabType.CHART,
                    filterType = LengthFilterType.LAST_90,
                    selectedFilter == LengthFilterType.LAST_90,
                    onFilterClick = onFilterClick,
                )
            }
        }
    }
}

@Composable
fun LengthFilterChip(
    selectedTab: LengthTabType,
    filterType: LengthFilterType,
    selected: Boolean,
    onFilterClick: (tabType: LengthTabType, filterType: LengthFilterType) -> Unit,
) {

    Button(
        onClick = { onFilterClick(selectedTab, filterType) },
        colors = ButtonColors(
            containerColor = Color.White,
            contentColor = Color.Black,
            disabledContainerColor = Color.Red,
            disabledContentColor = Color.Blue,
        ),
        shape = RectangleShape,
        modifier = Modifier.padding(start = 16.dp)
    ) {
        Text(
            text = filterType.text,
            fontSize = 15.sp,
            color = if (selected) {
                LightBlue80
            } else {
                Color.Black
            },
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun LengthTabGroup(
    selectedTab: LengthTabType = LengthTabType.CHART,
    selectedFilter: LengthFilterType = LengthFilterType.ALL,
    onFilterClick: (tabType: LengthTabType, filterType: LengthFilterType) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            LengthTabChip(
                LengthTabType.DATA,
                selectedFilter = selectedFilter,
                selectedTab == LengthTabType.DATA,
                onFilterClick = onFilterClick,
                modifier = Modifier
            )
            LengthTabChip(
                LengthTabType.CHART,
                selectedFilter = LengthFilterType.ALL,
                selectedTab == LengthTabType.CHART,
                onFilterClick = onFilterClick,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun LengthTabChip(
    tabType: LengthTabType,
    selectedFilter: LengthFilterType = LengthFilterType.LAST_30,
    selected: Boolean,
    onFilterClick: (tabType: LengthTabType, filterType: LengthFilterType) -> Unit,
    modifier: Modifier
) {
    TextButton(
        onClick = { onFilterClick(tabType, selectedFilter) },
        modifier = modifier.padding(start = 20.dp, end = 20.dp)
    ) {
        if (selected) {
            Text(
                text = tabType.selectedText,
                fontSize = 20.sp,
                color = Color.Black,
                fontWeight = FontWeight.W400,
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline
            )
        } else {
            Text(
                text = tabType.text,
                fontSize = 18.sp,
                color = Color.Gray,
                fontWeight = FontWeight.W400
            )
        }
    }
}

@Composable
private fun LengthList(
    lengthList: List<Length>
) {
    LazyColumn(modifier = Modifier.padding(top = 10.dp)) {
        items(items = lengthList, key = { it.id }) { item ->
            LengthItem(
                item = item
            )
        }
    }
}

@Composable
private fun LengthItem(
    item: Length, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 8.dp)
        ) {
            Text(text = covert2String(item.createTime))
            Text(text = item.number.toString(), modifier = Modifier.padding(start = 16.dp))
        }
    }
}

private fun covert2String(date: LocalDateTime): String {
    return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH))
}

@Composable
private fun AddItemDialog(
    itemDetails: ItemDetails,
    onValueChange: (ItemDetails) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        AddDialogContent(itemDetails, onValueChange, onDismissRequest, onConfirmation)
    }
}

@Composable
private fun AddDialogContent(
    itemDetails: ItemDetails,
    onValueChange: (ItemDetails) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardColors(
            containerColor = Color.White,
            contentColor = Purple40,
            disabledContainerColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ItemInputForm(
                itemDetails = itemDetails,
                onValueChange = onValueChange
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                TextButton(
                    onClick = { onDismissRequest() },
                    modifier = Modifier
                        .padding(start = 20.dp, end = 8.dp, top = 6.dp, bottom = 18.dp)
                        .weight(1f)
                        .height(46.dp),
                ) {
                    Text("Dismiss")
                }
                Button(
                    onClick = { onConfirmation() },
                    modifier = Modifier
                        .padding(start = 8.dp, end = 20.dp, top = 6.dp, bottom = 18.dp)
                        .weight(1f)
                        .height(46.dp),
                    colors = ButtonColors(
                        containerColor = Purple40,
                        contentColor = Color.White,
                        disabledContainerColor = Purple40,
                        disabledContentColor = Color.White
                    )
                ) {
                    Text("Confirm")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddDialogContentPreview() {
    PinTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            AddDialogContent(
                itemDetails = ItemDetails(),
                onValueChange = {},
                onConfirmation = {},
                onDismissRequest = {}
            )
        }
    }
}

@Composable
fun ItemInputForm(
    itemDetails: ItemDetails,
    onValueChange: (ItemDetails) -> Unit,
) {
    Column(
        modifier = Modifier.padding(start = 20.dp, top = 14.dp, end = 20.dp, bottom = 10.dp)
    ) {
        OutlinedTextField(
            value = itemDetails.number,
            onValueChange = { onValueChange(itemDetails.copy(number = it)) },
            label = { Text("Number") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
    }
}

@Composable
fun StatisticsContent(
    modifier: Modifier = Modifier,
    dataPoints: List<Float>,
    labels: List<String> = emptyList(),
    lineColor: Color = Color.Blue,
    backgroundColor: Color = Color.Transparent
) {
    val chartLabels = labels.ifEmpty {
        listOf("before", "now")
    }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 12.dp, end = 12.dp, top = 16.dp, bottom = 16.dp)
                .weight(1f)
        ) {
            LineChart(
                dataPoints = dataPoints,
                modifier = Modifier.matchParentSize(),
                lineColor = lineColor,
                backgroundColor = backgroundColor
            )
        }

        HorizontalDivider(thickness = 1.dp, color = Color.Black)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
                .height(30.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            chartLabels.forEach { label ->
                Text(
                    text = label,
                    modifier = Modifier.padding(horizontal = 4.dp),
                    fontSize = 14.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    val cardData = listOf(
        Pair("Max", dataPoints.max()),
        Pair("Min", dataPoints.min()),
        Pair("Avg", dataPoints.average().toFloat()),
        Pair("Last", dataPoints.first())
    )
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(cardData) { (title, value) ->
            StatisticsCard(
                title = title,
                value = value,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun StatisticsCard(
    title: String,
    value: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .aspectRatio(1f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "%.2f".format(value),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun LineChart(
    dataPoints: List<Float>,
    modifier: Modifier = Modifier,
    lineColor: Color = Color.Blue,
    backgroundColor: Color = Color.Transparent
) {
    Canvas(modifier = modifier) {
        if (dataPoints.size < 2) return@Canvas

        val width = size.width
        val height = size.height
        val maxValue = dataPoints.maxOrNull() ?: 1f
        val minValue = dataPoints.minOrNull() ?: 0f
        val valueRange = maxValue - minValue

        drawRect(color = backgroundColor)

        val points = dataPoints.mapIndexed { index, value ->
            val x = (index.toFloat() / (dataPoints.size - 1)) * width
            val y = height - ((value - minValue) / valueRange) * height
            Offset(x, y)
        }

        drawPath(
            path = Path().apply {
                points.forEachIndexed { index, point ->
                    if (index == 0) {
                        moveTo(point.x, point.y)
                    } else {
                        lineTo(point.x, point.y)
                    }
                }
            },
            color = lineColor,
            style = Stroke(width = 3.dp.toPx())
        )

        points.forEach { point ->
            drawCircle(
                color = lineColor,
                radius = 4.dp.toPx(),
                center = point
            )
        }
    }
}