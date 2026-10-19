package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.ScaleUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Timbangan Digital", appName)
    }

    @Test
    fun `test bmi and digital formatting calculations`() {
        val bmi = ScaleUtils.calculateBmi(72.0, 172.0)
        assertEquals(24.33, bmi, 0.1)
        assertEquals("Normal / Ideal", ScaleUtils.getBmiCategory(bmi))

        val weightFormatted = ScaleUtils.formatWeightDigital(72.8)
        assertEquals("000072.80", weightFormatted)

        val bmiFormatted = ScaleUtils.formatBmiDigital(24.61)
        assertEquals("000024.61", bmiFormatted)

        val heightFormatted = ScaleUtils.formatHeight(172.0)
        assertEquals("172.0 cm", heightFormatted)
    }

    @Test
    fun `test age calculation from indonesian dob format`() {
        val age = ScaleUtils.calculateAgeFromDob("15-Jan-1985")
        assertTrue(age >= 39)
    }
}
