package com.agustin.converter.domain

import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.math.abs

class UnitTypesTest {
    private fun close(a: Double, b: Double, eps: Double = 1e-6) = abs(a - b) < eps

    @Test fun c_to_f_and_back() {
        val f = convert(100.0, TemperatureUnit.C, TemperatureUnit.F)
        val c = convert(f, TemperatureUnit.F, TemperatureUnit.C)
        assertTrue(close(c, 100.0))
    }

    @Test fun m_to_ft_and_back() {
        val ft = convert(1.0, LengthUnit.M, LengthUnit.FT)
        val m = convert(ft, LengthUnit.FT, LengthUnit.M)
        assertTrue(close(m, 1.0))
    }

    @Test fun kg_lb_roundtrip() {
        val lb = convert(1.0, WeightUnit.KG, WeightUnit.LB)
        val kg = convert(lb, WeightUnit.LB, WeightUnit.KG)
        assertTrue(close(kg, 1.0))
    }

    @Test(expected = IllegalArgumentException::class)
    fun cannot_mix_categories() {
        convert(1.0, LengthUnit.M, TemperatureUnit.C)
    }
}
