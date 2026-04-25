package com.radlab.nbpclient

import com.radlab.nbpclient.network.NbpClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class NbpClientTest {
    @Test
    fun getRate_for_EUR_should_correctly_parse_NBP_official_response() =
        runTest {
            val mockEngine =
                MockEngine { request ->
                    respond(
                        content =
                            """
                            {
                                "table":"A",
                                "currency":"euro",
                                "code":"EUR",
                                "rates":[
                                    {
                                        "no":"079/A/NBP/2026",
                                        "effectiveDate":"2026-04-24",
                                        "mid":4.2428
                                    }
                                ]
                            }
                            """.trimIndent(),
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json"),
                    )
                }

            val mockClient =
                HttpClient(mockEngine) {
                    install(ContentNegotiation) {
                        json(
                            Json {
                                ignoreUnknownKeys = true
                            },
                        )
                    }
                }

            val nbpClient = NbpClient(mockEngine)
            val response = nbpClient.getEuroRate()

            assertEquals("EUR", response.code)
            assertEquals("euro", response.currency)
            assertEquals(1, response.rates.size)
            assertEquals(4.2428, response.rates.first().mid)
            assertEquals("2026-04-24", response.rates.first().effectiveDate)
        }

    @Test
    fun getTableRates_should_parse_official_nbp_table_correctly() =
        runTest {
            val jsonResponse =
                """
                [{
                    "table":"A",
                    "no":"079/A/NBP/2026",
                    "effectiveDate":"2026-04-24",
                    "rates":[
                        {"currency":"bat (Tajlandia)","code":"THB","mid":0.1119},
                        {"currency":"dolar amerykański","code":"USD","mid":3.6294},
                        {"currency":"euro","code":"EUR","mid":4.2428}
                    ]
                }]
                """.trimIndent()

            val mockEngine =
                MockEngine { _ ->
                    respond(
                        content = jsonResponse,
                        status = HttpStatusCode.OK,
                        headers = headersOf(HttpHeaders.ContentType, "application/json"),
                    )
                }

            val nbpClient = NbpClient(mockEngine)
            val response = nbpClient.getTableRates("a")

            assertEquals(1, response.size)
            val tableA = response.first()
            assertEquals("A", tableA.table)
            assertEquals("079/A/NBP/2026", tableA.no)

            assertEquals(3, tableA.rates.size)

            val eurRate = tableA.rates.find { it.code == "EUR" }
            assertEquals(4.2428, eurRate?.mid)
            assertEquals("euro", eurRate?.currency)
        }

    @Test
    fun integration_test_real_api_call() =
        runTest {
            val client = NbpClient()

            try {
                val response = client.getEuroRate()

                println("Fetched rate: ${response.currency} (${response.code})")
                println("Date: ${response.rates.first().effectiveDate}, Mid: ${response.rates.first().mid}")

                assertEquals("EUR", response.code)
                assertTrue(response.rates.first().mid > 0.0, "Rate should be greater than zero")
            } catch (e: Exception) {
                println("API Call failed: ${e.message}")
                throw e
            } finally {
                client.dispose()
            }
        }
}
