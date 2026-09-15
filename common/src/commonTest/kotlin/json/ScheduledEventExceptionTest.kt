package dev.kord.common.json

import dev.kord.common.entity.DiscordGuildScheduledEvent
import dev.kord.common.entity.DiscordGuildScheduledEventException
import dev.kord.common.entity.Snowflake
import dev.kord.common.readFile
import kotlinx.coroutines.test.runTest
import kotlin.time.Instant
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ScheduledEventExceptionTest {

    private suspend fun file(name: String): String = readFile("scheduled_event", name)

    @Test
    @JsName("test1")
    fun `GuildScheduledEvent parses scheduled event exceptions`() = runTest {
        val event = Json.decodeFromString(
            DiscordGuildScheduledEvent.serializer(),
            file("guildscheduledevent_exceptions"),
        )

        assertEquals(2, event.guildScheduledEventExceptions.size)

        val moved = event.guildScheduledEventExceptions[0]
        assertEquals("906727003124498462", moved.eventId.toString())
        assertEquals("906727003124498463", moved.eventExceptionId.toString())
        assertEquals(Instant.parse("2023-11-22T18:00:00Z"), moved.scheduledStartTime)
        assertEquals(Instant.parse("2023-11-22T20:00:00Z"), moved.scheduledEndTime)
        assertFalse(moved.isCanceled)

        val canceled = event.guildScheduledEventExceptions[1]
        assertEquals("906727003124498464", canceled.eventExceptionId.toString())
        assertNull(canceled.scheduledStartTime)
        assertNull(canceled.scheduledEndTime)
        assertTrue(canceled.isCanceled)
    }

    @Test
    @JsName("test2")
    fun `GuildScheduledEvent defaults missing scheduled event exceptions`() = runTest {
        val event = Json.decodeFromString(
            DiscordGuildScheduledEvent.serializer(),
            """
            {
              "id": "906727003124498462",
              "guild_id": "197038439483310086",
              "channel_id": "906726984402739200",
              "name": "Weekly Meeting",
              "scheduled_start_time": "2023-11-15T18:00:00+00:00",
              "scheduled_end_time": "2023-11-15T19:00:00+00:00",
              "privacy_level": 2,
              "status": 1,
              "entity_type": 2,
              "entity_id": "906726984402739200",
              "entity_metadata": null
            }
            """.trimIndent(),
        )

        assertTrue(event.guildScheduledEventExceptions.isEmpty())
    }

    @Test
    @JsName("test3")
    fun `DiscordGuildScheduledEventException serializes snake case fields`() {
        val exception = DiscordGuildScheduledEventException(
            eventId = Snowflake(1),
            eventExceptionId = Snowflake(2),
            scheduledStartTime = Instant.parse("2023-11-22T18:00:00Z"),
            scheduledEndTime = null,
            isCanceled = true,
        )

        val element = Json.parseToJsonElement(
            Json.encodeToString(DiscordGuildScheduledEventException.serializer(), exception)
        ).jsonObject

        assertEquals(
            setOf(
                "event_id",
                "event_exception_id",
                "scheduled_start_time",
                "scheduled_end_time",
                "is_canceled",
            ),
            element.keys,
        )
        assertEquals("1", element.getValue("event_id").jsonPrimitive.content)
        assertEquals("2", element.getValue("event_exception_id").jsonPrimitive.content)
        assertEquals("2023-11-22T18:00:00Z", element.getValue("scheduled_start_time").jsonPrimitive.content)
        assertTrue(element.getValue("scheduled_end_time") is JsonNull)
        assertEquals(true, element.getValue("is_canceled").jsonPrimitive.boolean)
    }
}
