package com.mercandalli.android.sdk.purchase

import org.junit.Assert
import org.junit.Ignore
import org.junit.Test

@Ignore
class PurchaseDetailsTest {

    @Test
    fun formatEur() {
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
