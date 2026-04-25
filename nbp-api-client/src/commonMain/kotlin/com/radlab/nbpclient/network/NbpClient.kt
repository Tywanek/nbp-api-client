package com.radlab.nbpclient.network

import com.radlab.nbpclient.utils.NbpEndpoints
import com.radlab.nbpclient.models.CurrencyResponse
import com.radlab.nbpclient.models.RatesByTableResponse
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class NbpClient(engine: HttpClientEngine? = null) : NbpService {
    private val jsonConfiguration =
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
        }

    private val httpClient =
        engine?.let { HttpClient(it) { configure() } }
            ?: HttpClient { configure() }

    private fun HttpClientConfig<*>.configure() {
        install(ContentNegotiation) {
            json(jsonConfiguration)
        }
    }

    fun dispose() {
        httpClient.close()
    }

    override suspend fun getEuroRate(): CurrencyResponse {
        return getRate("a", "eur")
    }

    override suspend fun getRate(
        table: String,
        code: String,
    ): CurrencyResponse {
        val url = "${NbpEndpoints.BASE_URL}/exchangerates/rates/$table/$code/?format=json"
        return httpClient.get(url).body()
    }

    override suspend fun getTableRates(table: String): List<RatesByTableResponse> {
        val url = "${NbpEndpoints.BASE_URL}/exchangerates/tables/$table/?format=json"
        return httpClient.get(url).body()
    }
}