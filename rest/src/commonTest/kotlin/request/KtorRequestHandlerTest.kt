package dev.kord.rest.request

import dev.kord.rest.service.RestClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class KtorRequestHandlerTest {

    private val userJson = """{"id":"80351110224678912","username":"Nelly","discriminator":"1337","avatar":null}"""

    private fun newClient(engine: MockEngine) = HttpClient(engine)

    @Test
    @JsName("test1")
    fun `sends Bot authorization header by default`() = runTest {
        var authorization: String? = null
        val engine = MockEngine { request ->
            authorization = request.headers[HttpHeaders.Authorization]
            respond(userJson, headers = headersOf(HttpHeaders.ContentType, "application/json"))
        }

        RestClient("my-token", newClient(engine)).user.getCurrentUser()

        assertEquals("Bot my-token", authorization)
    }

    @Test
    @JsName("test2")
    fun `sends configured Bearer authorization header`() = runTest {
        var authorization: String? = null
        val engine = MockEngine { request ->
            authorization = request.headers[HttpHeaders.Authorization]
            respond(userJson, headers = headersOf(HttpHeaders.ContentType, "application/json"))
        }

        RestClient("oauth-token", newClient(engine), tokenPrefix = "Bearer").user.getCurrentUser()

        assertEquals("Bearer oauth-token", authorization)
    }

    @Test
    @JsName("test3")
    fun `sends requests to configured base url`() = runTest {
        var url: String? = null
        val engine = MockEngine { request ->
            url = request.url.toString()
            respond(userJson, headers = headersOf(HttpHeaders.ContentType, "application/json"))
        }

        RestClient("my-token", newClient(engine), baseUrl = "https://example.com/api/v10").user.getCurrentUser()

        assertEquals("https://example.com/api/v10/users/@me", url)
    }
}
