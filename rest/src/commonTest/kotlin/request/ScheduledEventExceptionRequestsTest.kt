package dev.kord.rest.request

import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.Optional
import dev.kord.common.entity.optional.OptionalSnowflake
import dev.kord.rest.builder.scheduled_events.ScheduledEventExceptionCreateBuilder
import dev.kord.rest.builder.scheduled_events.ScheduledEventExceptionModifyBuilder
import dev.kord.rest.json.request.GuildScheduledEventExceptionCreateRequest
import dev.kord.rest.json.request.GuildScheduledEventUsersResponse
import dev.kord.rest.json.request.ScheduledEventExceptionModifyRequest
import kotlin.time.Instant
import kotlinx.serialization.json.Json
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class ScheduledEventExceptionRequestsTest {

    private val original = Instant.parse("2023-11-15T18:00:00Z")
    private val start = Instant.parse("2023-11-22T18:00:00Z")
    private val end = Instant.parse("2023-11-22T20:00:00Z")

    @Test
    @JsName("test1")
    fun `create request with only the required field`() {
        val request = GuildScheduledEventExceptionCreateRequest(originalScheduledStartTime = original)

        assertEquals(
            """{"original_scheduled_start_time":"2023-11-15T18:00:00Z"}""",
            Json.encodeToString(GuildScheduledEventExceptionCreateRequest.serializer(), request),
        )
    }

    @Test
    @JsName("test2")
    fun `create request with values`() {
        val request = GuildScheduledEventExceptionCreateRequest(
            originalScheduledStartTime = original,
            scheduledStartTime = Optional.Value(start),
            scheduledEndTime = Optional.Value(end),
            isCanceled = Optional.Value(false),
        )

        assertEquals(
            """{"original_scheduled_start_time":"2023-11-15T18:00:00Z",""" +
                """"scheduled_start_time":"2023-11-22T18:00:00Z",""" +
                """"scheduled_end_time":"2023-11-22T20:00:00Z","is_canceled":false}""",
            Json.encodeToString(GuildScheduledEventExceptionCreateRequest.serializer(), request),
        )
    }

    @Test
    @JsName("test3")
    fun `create request with explicit nulls`() {
        val request = GuildScheduledEventExceptionCreateRequest(
            originalScheduledStartTime = original,
            scheduledStartTime = Optional.Null(),
            scheduledEndTime = Optional.Null(),
            isCanceled = Optional.Null(),
        )

        assertEquals(
            """{"original_scheduled_start_time":"2023-11-15T18:00:00Z",""" +
                """"scheduled_start_time":null,"scheduled_end_time":null,"is_canceled":null}""",
            Json.encodeToString(GuildScheduledEventExceptionCreateRequest.serializer(), request),
        )
    }

    @Test
    @JsName("test4")
    fun `modify request omits missing fields`() {
        assertEquals(
            "{}",
            Json.encodeToString(ScheduledEventExceptionModifyRequest.serializer(), ScheduledEventExceptionModifyRequest()),
        )
    }

    @Test
    @JsName("test5")
    fun `modify request serializes each field`() {
        fun encode(request: ScheduledEventExceptionModifyRequest) =
            Json.encodeToString(ScheduledEventExceptionModifyRequest.serializer(), request)

        assertEquals("""{"scheduled_start_time":null}""", encode(
            ScheduledEventExceptionModifyRequest(scheduledStartTime = Optional.Null())
        ))
        assertEquals("""{"scheduled_end_time":"2023-11-22T20:00:00Z"}""", encode(
            ScheduledEventExceptionModifyRequest(scheduledEndTime = Optional.Value(end))
        ))
        assertEquals("""{"is_canceled":true}""", encode(
            ScheduledEventExceptionModifyRequest(isCanceled = Optional.Value(true))
        ))
        assertEquals(
            """{"scheduled_start_time":"2023-11-22T18:00:00Z","scheduled_end_time":null,"is_canceled":false}""",
            encode(
                ScheduledEventExceptionModifyRequest(
                    scheduledStartTime = Optional.Value(start),
                    scheduledEndTime = Optional.Null(),
                    isCanceled = Optional.Value(false),
                )
            ),
        )
    }

    @Test
    @JsName("test6")
    fun `create builder maps fields to request`() {
        val request = ScheduledEventExceptionCreateBuilder(original).apply {
            scheduledStartTime = start
            scheduledEndTime = end
            isCanceled = true
        }.toRequest()

        assertEquals(
            GuildScheduledEventExceptionCreateRequest(
                originalScheduledStartTime = original,
                scheduledStartTime = Optional.Value(start),
                scheduledEndTime = Optional.Value(end),
                isCanceled = Optional.Value(true),
            ),
            request,
        )
    }

    @Test
    @JsName("test7")
    fun `modify builder maps fields to request`() {
        val request = ScheduledEventExceptionModifyBuilder().apply {
            scheduledStartTime = start
            scheduledEndTime = end
            isCanceled = false
        }.toRequest()

        assertEquals(
            ScheduledEventExceptionModifyRequest(
                scheduledStartTime = Optional.Value(start),
                scheduledEndTime = Optional.Value(end),
                isCanceled = Optional.Value(false),
            ),
            request,
        )
    }

    @Test
    @JsName("test8")
    fun `builders distinguish omitted fields from explicit nulls`() {
        val omitted = ScheduledEventExceptionCreateBuilder(original).toRequest()
        assertIs<Optional.Missing<*>>(omitted.scheduledStartTime)
        assertIs<Optional.Missing<*>>(omitted.scheduledEndTime)
        assertIs<Optional.Missing<*>>(omitted.isCanceled)

        val nulled = ScheduledEventExceptionCreateBuilder(original).apply {
            scheduledStartTime = null
            scheduledEndTime = null
            isCanceled = null
        }.toRequest()
        assertIs<Optional.Null<*>>(nulled.scheduledStartTime)
        assertIs<Optional.Null<*>>(nulled.scheduledEndTime)
        assertIs<Optional.Null<*>>(nulled.isCanceled)

        val modify = ScheduledEventExceptionModifyBuilder().apply { scheduledStartTime = null }.toRequest()
        assertIs<Optional.Null<*>>(modify.scheduledStartTime)
        assertIs<Optional.Missing<*>>(modify.scheduledEndTime)
        assertIs<Optional.Missing<*>>(modify.isCanceled)
    }

    @Test
    @JsName("test9")
    fun `users response parses the different exception id states`() {
        val user = """{"id":"3","username":"Nelly","avatar":null}"""

        fun decode(exceptionId: String): GuildScheduledEventUsersResponse = Json.decodeFromString(
            GuildScheduledEventUsersResponse.serializer(),
            """{"guild_scheduled_event_id":"1","user":$user$exceptionId}""",
        )

        val value = decode(""","guild_scheduled_event_exception_id":"2"""")
        assertIs<OptionalSnowflake.Value>(value.guildScheduledEventExceptionId)
        assertEquals(Snowflake(2), value.guildScheduledEventExceptionId.value)

        val nulled = decode(""","guild_scheduled_event_exception_id":null""")
        assertNull(nulled.guildScheduledEventExceptionId)

        val missing = decode("")
        assertIs<OptionalSnowflake.Missing>(missing.guildScheduledEventExceptionId)
    }
}
