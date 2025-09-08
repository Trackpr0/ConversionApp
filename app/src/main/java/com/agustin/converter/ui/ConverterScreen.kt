@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.agustin.converter.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.agustin.converter.domain.Category
import com.agustin.converter.domain.LengthUnit
import com.agustin.converter.domain.TemperatureUnit
import com.agustin.converter.domain.UnitType
import com.agustin.converter.domain.WeightUnit

@Composable
fun ConverterScreen(
    modifier: Modifier = Modifier,
    vm: ConversionViewModel = hiltViewModel()
) {
    val ui by vm.state.collectAsState()
    var input by remember(ui.input) { mutableStateOf(ui.input) }

    val units: List<UnitType> = when (ui.category) {
        Category.TEMPERATURE -> TemperatureUnit.values().toList()
        Category.LENGTH      -> LengthUnit.values().toList()
        Category.WEIGHT      -> WeightUnit.values().toList()
    }

    Column(
        modifier = modifier.padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        UnitDropdown(
            label = "Category",
            items = Category.values().toList(),
            selected = ui.category,
            toLabel = { categoryLabel(it) },
            onSelected = { vm.setCategory(it) }
        )

        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
            UnitDropdown(
                modifier = Modifier.weight(1f),
                label = "From",
                items = units,
                selected = ui.from,
                toLabel = { unitLabel(it as UnitType) },
                onSelected = { vm.setFromUnit(it as UnitType) }
            )
            UnitDropdown(
                modifier = Modifier.weight(1f),
                label = "To",
                items = units,
                selected = ui.to,
                toLabel = { unitLabel(it as UnitType) },
                onSelected = { vm.setToUnit(it as UnitType) }
            )
        }

        // INPUT with a tag the test can target
        OutlinedTextField(
            value = input,
            onValueChange = { input = it },
            singleLine = true,
            label = { Text("Value") },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("input"),
            colors = OutlinedTextFieldDefaults.colors()
        )

        // BUTTON with tag
        Button(
            onClick = { vm.setInput(input) },
            modifier = Modifier.testTag("convertBtn")
        ) {
            Text("Convert")
        }

        Spacer(Modifier.height(4.dp))

        // RESULT with tag
        Text(
            text = if (ui.output.isBlank()) "Result will appear here" else "Result: ${ui.output}",
            modifier = Modifier.testTag("result")
        )
    }
}

@Composable
private fun <T> UnitDropdown(
    label: String,
    items: List<T>,
    selected: T,
    toLabel: (T) -> String,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = toLabel(selected),
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors()
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.exposedDropdownSize()
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    text = { Text(toLabel(item)) },
                    onClick = {
                        onSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}

private fun categoryLabel(c: Category): String =
    c.name.lowercase().replaceFirstChar { it.uppercase() }

private fun unitLabel(u: UnitType): String =
    "${(u as Enum<*>).name} (${u.symbol})"
