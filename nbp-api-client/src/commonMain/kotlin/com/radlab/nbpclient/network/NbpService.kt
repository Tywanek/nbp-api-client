package com.radlab.nbpclient.network

import com.radlab.nbpclient.models.CurrencyResponse
import com.radlab.nbpclient.models.RatesByTableResponse

interface NbpService {
    suspend fun getEuroRate(): CurrencyResponse

    suspend fun getRate(
        table: String,
        code: String,
    ): CurrencyResponse

    suspend fun getTableRates(table: String): List<RatesByTableResponse>
}