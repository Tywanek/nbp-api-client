package com.radlab.nbpclient.models

import kotlinx.serialization.Serializable

@Serializable
data class CurrencyResponse(
    val table: String,
    val currency: String,
    val code: String,
    val rates: List<CurrencyRate>,
)

@Serializable
data class CurrencyRate(
    val no: String,
    val effectiveDate: String,
    val mid: Double,
)

@Serializable
data class RatesByTableResponse(
    val table: String,
    val no: String,
    val effectiveDate: String,
    val rates: List<TableRate>,
)

@Serializable
data class TableRate(
    val currency: String,
    val code: String,
    val mid: Double,
)
