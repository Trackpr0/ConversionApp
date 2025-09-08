package com.agustin.converter.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agustin.converter.data.ConversionRepository
import com.agustin.converter.domain.*
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ConversionUiState(
    val category: Category = Category.TEMPERATURE,
    val from: UnitType = TemperatureUnit.C,
    val to: UnitType = TemperatureUnit.F,
    val input: String = "",
    val output: String = ""
)

@HiltViewModel
class ConversionViewModel @Inject constructor(
    private val repo: ConversionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ConversionUiState())
    val state: StateFlow<ConversionUiState> = _state

    fun setCategory(category: Category) = viewModelScope.launch {
        // Default sensible pair per category
        val (defFrom, defTo) = when (category) {
            Category.TEMPERATURE -> TemperatureUnit.C to TemperatureUnit.F
            Category.LENGTH      -> LengthUnit.M to LengthUnit.KM
            Category.WEIGHT      -> WeightUnit.G to WeightUnit.KG
        }
        _state.update { it.copy(category = category, from = defFrom, to = defTo, output = "") }
        recalc()
    }

    fun setFromUnit(unit: UnitType) {
        _state.update { s ->
            require(unit.category == s.category)
            if (unit == s.to) {
                // User picked the same as 'to' → swap to avoid crash
                s.copy(from = unit, to = s.from)
            } else s.copy(from = unit)
        }
        recalc()
    }

    fun setToUnit(unit: UnitType) {
        _state.update { s ->
            require(unit.category == s.category)
            if (unit == s.from) {
                // Same as 'from' → swap
                s.copy(to = unit, from = s.to)
            } else s.copy(to = unit)
        }
        recalc()
    }

    fun setInput(text: String) {
        _state.update { it.copy(input = text) }
        recalc()
    }

    fun swapUnits() {
        _state.update { it.copy(from = it.to, to = it.from) }
        recalc()
    }

    private fun recalc() {
        val s = _state.value
        val v = s.input.toDoubleOrNull()
        val out = if (v != null) {
            repo.convert(s.category, v, s.from, s.to).toString()
        } else ""
        _state.update { it.copy(output = out) }
    }
}
