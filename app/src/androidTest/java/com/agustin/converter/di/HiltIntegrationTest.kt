package com.agustin.converter.di

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.agustin.converter.data.ConversionRepository
import com.agustin.converter.domain.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@UninstallModules(ConverterModule::class)
@RunWith(AndroidJUnit4::class)
class HiltIntegrationTest {

    @get:Rule val hiltRule = HiltAndroidRule(this)

    @Inject lateinit var repo: ConversionRepository

    @Before fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun fakeRepoIsUsed() {
        val units = repo.listUnits(Category.TEMPERATURE)
        // Our fake returns only C and F for temperature
        assertEquals(listOf(TemperatureUnit.C, TemperatureUnit.F), units)
    }

    // Test replacement module
    @Module
    @InstallIn(SingletonComponent::class)
    abstract class TestModule {
        @Binds
        @Singleton
        abstract fun bindRepo(impl: FakeRepo): ConversionRepository
    }

    class FakeRepo @Inject constructor() : ConversionRepository {
        override fun listUnits(category: Category): List<UnitType> = when (category) {
            Category.TEMPERATURE -> listOf(TemperatureUnit.C, TemperatureUnit.F)
            Category.LENGTH      -> listOf(LengthUnit.M, LengthUnit.KM)
            Category.WEIGHT      -> listOf(WeightUnit.G, WeightUnit.KG)
        }

        override fun convert(
            category: Category,
            value: Double,
            from: UnitType,
            to: UnitType
        ): Double = value // no-op for the test
    }
}
