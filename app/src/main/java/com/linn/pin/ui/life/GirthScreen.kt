package com.linn.pin.ui.life

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.linn.pin.data.Girth
import com.linn.pin.ui.AppViewModelProvider
import com.linn.pin.ui.theme.PinTheme
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@Composable
fun LifeScreen(
    navController: NavController,
    viewModel: GirthViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val girthUiState by viewModel.girthUiState.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    PinTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            LifeBody(
                itemDetails = girthUiState.itemDetails,
                itemList = girthUiState.itemList,
                onValueChange = viewModel::updateUiState,
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
    itemDetails: ItemDetails,
    itemList: List<Girth>,
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
        if (itemList.isEmpty()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(innerPadding)
            ) {
                Text(
                    text = "--- nothing found ---",
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
    if (addConfirmationRequired) {
        AddItemDialog(
            itemDetails = itemDetails,
            onValueChange = onValueChange,
            onDismissRequest = { addConfirmationRequired = false },
            onConfirmation = {
                addConfirmationRequired = false
                onAddConfirm()
            },
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview
@Composable
fun GirthBodyPreview() {
    PinTheme {
        LifeBody(
            itemDetails = ItemDetails(),
            itemList = listOf(
                Girth(id = 1, createTime = LocalDateTime.now(), number1 = 80.1, number2 = 90.1),
                Girth(
                    id = 1,
                    createTime = LocalDateTime.now().minusDays(1L),
                    number1 = 80.1,
                    number2 = 90.1
                )
            ),
            onValueChange = {},
            onAddConfirm = {},
        )
    }
}

@Composable
private fun AddItemDialog(
    itemDetails: ItemDetails,
    onValueChange: (ItemDetails) -> Unit,
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ){
            ItemInputForm(
                itemDetails = itemDetails,
                onValueChange = onValueChange,
                modifier = modifier
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
    modifier: Modifier
) {
    Column(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = itemDetails.number1.toString(),
            onValueChange = { onValueChange(itemDetails.copy(number1 = it.toDouble())) },
            label = { Text("number1") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )
        OutlinedTextField(
            value = itemDetails.number2.toString(),
            onValueChange = { onValueChange(itemDetails.copy(number2 = it.toDouble())) },
            label = { Text("number2") },
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            ),
            modifier = Modifier.fillMaxWidth(),
            enabled = true,
            singleLine = true
        )
    }
}