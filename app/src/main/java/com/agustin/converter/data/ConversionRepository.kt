package com.agustin.converter.data

import com.agustin.converter.domain.*
import javax.inject.Inject
import javax.inject.Singleton

interface ConversionRepository {
    fun listUnits(category: Category): List<UnitType>
    fun convert(category: Category, value: Double, from: UnitType, to: UnitType): Double
}

@Singleton
class DefaultConversionRepository @Inject constructor() : ConversionRepository {

    override fun listUnits(category: Category): List<UnitType> = when (category) {
        Category.TEMPERATURE -> TemperatureUnit.values().toList()
        Category.LENGTH      -> LengthUnit.values().toList()
        Category.WEIGHT      -> WeightUnit.values().toList()
    }

    override fun convert(
        category: Category,
        value: Double,
        from: UnitType,
        to: UnitType
    ): Double {
        // Safety: repo API includes category; enforce it, then delegate to domain convert
        require(from.category == category && to.category == category)
        return com.agustin.converter.domain.convert(value, from, to)
    }
}
