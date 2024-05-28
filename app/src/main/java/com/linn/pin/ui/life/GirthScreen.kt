package com.linn.pin.ui.life

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.linn.pin.data.girth.Girth
import com.linn.pin.ui.AppViewModelProvider
import com.linn.pin.ui.theme.PinTheme
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun LifeScreen(
    viewModel: GirthViewModel = viewModel(factory = AppViewModelProvider.Factory)
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
                onFilterClick = { tabType: GirthTabType, filterType: GirthFilterType ->
                    coroutineScope.launch {
                        viewModel.reloadGirthList(tabType, filterType)
                    }
                },
                itemList = listUiState.itemList,
                itemUiState = viewModel.itemUiState,
                onValueChange = viewModel::updateItemUiState,
                onAddConfirm = {
                    coroutineScope.launch {
                        viewModel.insertGirth()
                    }
                }
            )
        }
    }
}

@Composable
private fun LifeBody(
    selectedTab: GirthTabType,
    selectedFilter: GirthFilterType,
    onFilterClick: (tabType: GirthTabType, filterType: GirthFilterType) -> Unit,
    itemList: List<Girth>,
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
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0.0f to Color(0xFF5AB2FF),
                            0.2f to Color.White
                        )
                    ),
                    alpha = 0.16f
                )
        ) {
            GirthTabGroup(
                selectedTab = selectedTab,
                onFilterClick = onFilterClick
            )
            GirthFilterGroup(
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
                    GirthList(
                        selectedTab = selectedTab,
                        selectedFilter = selectedFilter,
                        girthList = itemList
                    )
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
            selectedTab = GirthTabType.FIRST,
            selectedFilter = GirthFilterType.NONE,
            onFilterClick = { _, _ -> run {} },
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
            selectedTab = GirthTabType.FIRST,
            selectedFilter = GirthFilterType.NONE,
            onFilterClick = { _, _ -> run {} },
            itemUiState = ItemUiState(),
            itemList = listOf(
                Girth(id = 1, createTime = LocalDateTime.now(), number1 = 87.1, number2 = 99.1),
                Girth(
                    id = 2,
                    createTime = LocalDateTime.now().minusDays(1L).minusHours(6),
                    number1 = 55.0,
                    number2 = 0.0
                )
            ),
            onValueChange = {},
            onAddConfirm = {},
        )
    }
}

@Composable
fun GirthFilterGroup(
    selectedTab: GirthTabType,
    selectedFilter: GirthFilterType = GirthFilterType.NONE,
    onFilterClick: (tabType: GirthTabType, filterType: GirthFilterType) -> Unit
) {
    if (selectedTab == GirthTabType.FIRST) {
        Column(
            modifier = Modifier.padding(top = 10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            Row {
                GirthFilterChip(
                    GirthTabType.FIRST,
                    filterType = GirthFilterType.ONLY_PM,
                    selectedFilter == GirthFilterType.ONLY_PM,
                    onFilterClick = onFilterClick,
                )
                GirthFilterChip(
                    GirthTabType.FIRST,
                    filterType = GirthFilterType.ONLY_AM,
                    selectedFilter == GirthFilterType.ONLY_AM,
                    onFilterClick = onFilterClick,
                )
                GirthFilterChip(
                    GirthTabType.FIRST,
                    filterType = GirthFilterType.NONE,
                    selectedFilter == GirthFilterType.NONE,
                    onFilterClick = onFilterClick,
                )
            }
        }
    }
}

@Composable
fun GirthFilterChip(
    selectedTab: GirthTabType,
    filterType: GirthFilterType,
    selected: Boolean,
    onFilterClick: (tabType: GirthTabType, filterType: GirthFilterType) -> Unit,
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
                Color(0xFF3D7EFF)
            } else {
                Color.Black
            },
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
fun GirthTabGroup(
    selectedTab: GirthTabType = GirthTabType.FIRST,
    selectedFilter: GirthFilterType = GirthFilterType.NONE,
    onFilterClick: (tabType: GirthTabType, filterType: GirthFilterType) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            GirthTabChip(
                GirthTabType.FIRST,
                selectedFilter = selectedFilter,
                selectedTab == GirthTabType.FIRST,
                onFilterClick = onFilterClick,
                modifier = Modifier
            )
            GirthTabChip(
                GirthTabType.SECOND,
                selectedFilter = GirthFilterType.NONE,
                selectedTab == GirthTabType.SECOND,
                onFilterClick = onFilterClick,
                modifier = Modifier
            )
            GirthTabChip(
                GirthTabType.ALL,
                selectedFilter = GirthFilterType.NONE,
                selectedTab == GirthTabType.ALL,
                onFilterClick = onFilterClick,
                modifier = Modifier
            )
        }
    }
}

@Composable
fun GirthTabChip(
    tabType: GirthTabType,
    selectedFilter: GirthFilterType = GirthFilterType.ONLY_PM,
    selected: Boolean,
    onFilterClick: (tabType: GirthTabType, filterType: GirthFilterType) -> Unit,
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
private fun GirthList(
    selectedTab: GirthTabType,
    selectedFilter: GirthFilterType,
    girthList: List<Girth>
) {
    LazyColumn(modifier = Modifier.padding(top = 10.dp)) {
        items(items = girthList, key = { it.id }) { item ->
            GirthItem(
                selectedTab = selectedTab, selectedFilter = selectedFilter, item = item
            )
        }
    }
}

@Composable
private fun GirthItem(
    selectedTab: GirthTabType,
    selectedFilter: GirthFilterType,
    item: Girth, modifier: Modifier = Modifier
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
            when (selectedTab) {
                GirthTabType.FIRST -> {
                    when (selectedFilter) {
                        GirthFilterType.NONE -> {
                            if (isAm(item.createTime)) {
                                Text(
                                    text = "--/--",
                                    modifier = Modifier
                                        .width(60.dp)
                                        .padding(start = 16.dp)
                                )
                                Text(
                                    text = item.number1.toString(),
                                    modifier = Modifier
                                        .width(60.dp)
                                        .padding(start = 16.dp)
                                )
                            } else {
                                Text(
                                    text = item.number1.toString(),
                                    modifier = Modifier
                                        .width(60.dp)
                                        .padding(start = 16.dp)
                                )
                                Text(
                                    text = "--/--",
                                    modifier = Modifier
                                        .width(60.dp)
                                        .padding(start = 16.dp)
                                )
                            }
                        }

                        else -> {
                            Text(
                                text = item.number1.toString(),
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }

                GirthTabType.SECOND -> {
                    Text(
                        text = if (item.number2 > 0) {
                            item.number2.toString()
                        } else {
                            "--/--"
                        }, modifier = Modifier.padding(start = 16.dp)
                    )
                }

                GirthTabType.ALL -> {
                    Text(text = item.number1.toString(), modifier = Modifier.padding(start = 16.dp))
                    Text(
                        text = if (item.number2 > 0) {
                            item.number2.toString()
                        } else {
                            "--/--"
                        }, modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}

private fun isAm(time: LocalDateTime): Boolean {
    return time.format(DateTimeFormatter.ofPattern("a", Locale.ENGLISH)).equals("AM")
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
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
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
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Dismiss")
                    }
                    TextButton(
                        onClick = { onConfirmation() },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun AddItemDialogPreview() {
    PinTheme {
        AddItemDialog(
            itemDetails = ItemDetails(),
            onValueChange = {},
            onDismissRequest = {},
            onConfirmation = {}
        )
    }
}

@Composable
fun ItemInputForm(
    itemDetails: ItemDetails,
    onValueChange: (ItemDetails) -> Unit,
) {
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        OutlinedTextField(
            value = itemDetails.number1,
            onValueChange = { onValueChange(itemDetails.copy(number1 = it)) },
            label = { Text("number1") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
        OutlinedTextField(
            value = itemDetails.number2,
            onValueChange = { onValueChange(itemDetails.copy(number2 = it)) },
            label = { Text("number2") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
        )
    }
}