package com.mercandalli.android.sdk.purchase

import com.android.billingclient.api.SkuDetails
import java.text.NumberFormat
import java.util.Currency

data class PurchaseDetails(
    val sku: String,
    val price: String,
    val priceAmountMicros: Long,

    /**
     * ISO 4217
     */
    val priceCurrencyCode: String
) {

    companion object {

        fun create(
            skuDetails: SkuDetails
        ): PurchaseDetails {
            val sku = skuDetails.sku
            val price = skuDetails.price
            val priceAmountMicros = skuDetails.priceAmountMicros
            val priceCurrencyCode = skuDetails.priceCurrencyCode
            return PurchaseDetails(
                sku,
                price,
                priceAmountMicros,
                priceCurrencyCode
            )
        }

        @JvmStatic
        fun format(
            priceAmountMicros: Long,
            priceCurrencyCode: String
        ): String {
            val currency = Currency.getInstance(priceCurrencyCode)
            val format = NumberFormat.getCurrencyInstance()
            format.maximumFractionDigits = 0
            format.currency = currency
            return format.format((priceAmountMicros / 1000f).toDouble())
        }
    }
}
