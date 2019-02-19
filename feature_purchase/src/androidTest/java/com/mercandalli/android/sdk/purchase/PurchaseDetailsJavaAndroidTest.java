package com.mercandalli.android.sdk.purchase;


import org.junit.Assert;
import org.junit.Test;


public class PurchaseDetailsJavaAndroidTest {

    @Test
    public void testFormatEur() {

        // Given
        long priceAmountMicros = 26_500_000L;
        String priceCurrencyCode = "EUR";

        // When
        String priceFormatted = PurchaseDetails.format(
                priceAmountMicros,
                priceCurrencyCode
        );

        // Then
        Assert.assertEquals("26.5€", priceFormatted);
    }
}
