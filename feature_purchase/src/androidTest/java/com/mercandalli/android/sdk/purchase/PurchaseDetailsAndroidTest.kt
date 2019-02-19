package com.mercandalli.android.sdk.purchase

import androidx.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PurchaseDetailsAndroidTest {

    @Test
    fun testFormatEur() {
        // Given
        val priceAmountMicros = 26_500_000L
        val priceCurrencyCode = "EUR"

        // When
        val priceFormatted = PurchaseDetails.format(
            priceAmountMicros,
            priceCurrencyCode
        )

        // Then
        Assert.assertEquals("26.5€", priceFormatted)
    }

    @Test
    fun formatUsd() {
        // Given
        val priceAmountMicros = 26_500_000L
        val priceCurrencyCode = "USD"

        // When
        val priceFormatted = PurchaseDetails.format(
            priceAmountMicros,
            priceCurrencyCode
        )

        // Then
        Assert.assertEquals("26.5$", priceFormatted)
    }
}
