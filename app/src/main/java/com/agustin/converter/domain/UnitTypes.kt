package com.agustin.converter.domain

enum class Category { TEMPERATURE, LENGTH, WEIGHT }

sealed interface UnitType {
    val category: Category
    val symbol: String
}

/* ---------- Temperature (use Kelvin as base) ---------- */
enum class TemperatureUnit(override val symbol: String) : UnitType {
    C("°C"), F("°F"), K("K");
    override val category = Category.TEMPERATURE

    internal fun toKelvin(v: Double): Double = when (this) {
        C -> v + 273.15
        F -> (v + 459.67) * 5.0 / 9.0
        K -> v
    }

    internal fun fromKelvin(k: Double): Double = when (this) {
        C -> k - 273.15
        F -> k * 9.0 / 5.0 - 459.67
        K -> k
    }
}

/* ---------- Length (meters as base) ---------- */
enum class LengthUnit(override val symbol: String, val metersPerUnit: Double) : UnitType {
    M("m", 1.0),
    KM("km", 1000.0),
    MI("mi", 1609.344),
    FT("ft", 0.3048);

    override val category = Category.LENGTH
}

/* ---------- Weight (grams as base) ---------- */
enum class WeightUnit(override val symbol: String, val gramsPerUnit: Double) : UnitType {
    G("g", 1.0),
    KG("kg", 1000.0),
    LB("lb", 453.59237),
    OZ("oz", 28.349523125);

    override val category = Category.WEIGHT
}

/* ---------- One entry point for all conversions ---------- */
fun convert(value: Double, from: UnitType, to: UnitType): Double {
    require(from.category == to.category) { "Can't convert ${from.category} to ${to.category}" }
    if (from == to) return value

    return when (from.category) {
        Category.TEMPERATURE -> {
            val f = from as TemperatureUnit
            val t = to as TemperatureUnit
            t.fromKelvin(f.toKelvin(value))
        }
        Category.LENGTH -> {
            val f = from as LengthUnit
            val t = to as LengthUnit
            value * f.metersPerUnit / t.metersPerUnit
        }
        Category.WEIGHT -> {
            val f = from as WeightUnit
            val t = to as WeightUnit
            value * f.gramsPerUnit / t.gramsPerUnit
        }
    }
}
