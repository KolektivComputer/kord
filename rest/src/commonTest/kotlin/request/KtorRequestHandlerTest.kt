package dev.kord.rest.request

import dev.kord.common.entity.Snowflake
import dev.kord.rest.json.request.GuildScheduledEventExceptionCreateRequest
import dev.kord.rest.route.Position
import dev.kord.rest.service.RestClient
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.headersOf
import kotlin.time.Instant
import kotlinx.coroutines.test.runTest
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals

class KtorRequestHandlerTest {

    private val userJson = """{"id":"80351110224678912","username":"Nelly","discriminator":"1337","avatar":null}"""
    private val exceptionJson =
        """{"event_id":"1","event_exception_id":"2","scheduled_start_time":null,"scheduled_end_time":null,"is_canceled":true}"""

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

    @Test
    @JsName("test4")
    fun `scheduled event exceptions are created on the exceptions route`() = runTest {
        var url: String? = null
        var method: HttpMethod? = null
        val engine = MockEngine { request ->
            url = request.url.toString()
            method = request.method
            respond(exceptionJson, headers = headersOf(HttpHeaders.ContentType, "application/json"))
        }

        RestClient("my-token", newClient(engine)).guild.createScheduledEventException(
            Snowflake(1),
            Snowflake(2),
            GuildScheduledEventExceptionCreateRequest(
                originalScheduledStartTime = Instant.parse("2023-11-15T18:00:00Z"),
            ),
        )

        assertEquals(HttpMethod.Post, method)
        assertEquals("https://discord.com/api/v10/guilds/1/scheduled-events/2/exceptions", url)
    }

    @Test
    @JsName("test5")
    fun `scheduled event exception users use the exception id path without an exceptions segment`() = runTest {
        var url: String? = null
        val engine = MockEngine { request ->
            url = request.url.toString()
            respond("[]", headers = headersOf(HttpHeaders.ContentType, "application/json"))
        }

        RestClient("my-token", newClient(engine)).guild.getScheduledEventExceptionUsers(
            Snowflake(1),
            Snowflake(2),
            Snowflake(3),
            withMember = true,
            limit = 5,
            position = Position.After(Snowflake(4)),
        )

        assertEquals(
            "https://discord.com/api/v10/guilds/1/scheduled-events/2/3/users?limit=5&with_member=true&after=4",
            url,
        )
    }
}
