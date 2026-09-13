package dev.kord.common.json

import dev.kord.common.entity.DiscordGuildScheduledEvent
import dev.kord.common.entity.DiscordGuildScheduledEventException
import dev.kord.common.entity.DiscordRecurrenceRule
import dev.kord.common.entity.DiscordRecurrenceRuleNWeekday
import dev.kord.common.entity.GuildScheduledEventStatus
import dev.kord.common.entity.RecurrenceRuleFrequency
import dev.kord.common.entity.RecurrenceRuleMonth
import dev.kord.common.entity.RecurrenceRuleWeekday
import dev.kord.common.entity.Snowflake
import dev.kord.common.readFile
import kotlinx.coroutines.test.runTest
import kotlin.time.Instant
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.boolean
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ScheduledEventTest {

    private suspend fun file(name: String): String = readFile("scheduled_event", name)

    @Test
    @JsName("test1")
    fun `DiscordRecurrenceRule serialization round-trip`() {
        val rule = DiscordRecurrenceRule(
            start = Instant.fromEpochSeconds(1_700_000_000),
            end = null,
            frequency = RecurrenceRuleFrequency.Monthly,
            interval = 1,
            byWeekday = null,
            byNWeekday = listOf(DiscordRecurrenceRuleNWeekday(n = 4, day = 2)),
            byMonth = listOf(RecurrenceRuleMonth.July),
            byMonthDay = listOf(24),
            byYearDay = null,
            count = null,
        )

        val json = Json.encodeToString(DiscordRecurrenceRule.serializer(), rule)
        val decoded = Json.decodeFromString(DiscordRecurrenceRule.serializer(), json)

        assertEquals(rule, decoded)

        val element = Json.parseToJsonElement(json).jsonObject
        assertTrue("by_n_weekday" in element)
        assertTrue("by_month" in element)
        assertTrue("by_month_day" in element)
        val byNWeekday = element.getValue("by_n_weekday").jsonArray.single().jsonObject
        assertEquals("4", byNWeekday.getValue("n").jsonPrimitive.content)
        assertEquals("2", byNWeekday.getValue("day").jsonPrimitive.content)
    }

    @Test
    @JsName("test2")
    fun `DiscordRecurrenceRule serializes snake case fields`() {
        val rule = DiscordRecurrenceRule(
            frequency = RecurrenceRuleFrequency.Weekly,
            interval = 2,
            byWeekday = listOf(RecurrenceRuleWeekday.Wednesday),
        )

        val element = Json.parseToJsonElement(Json.encodeToString(DiscordRecurrenceRule.serializer(), rule)).jsonObject

        assertEquals(setOf("frequency", "interval", "by_weekday"), element.keys)
        assertEquals("2", element.getValue("frequency").jsonPrimitive.content)
        assertEquals("2", element.getValue("interval").jsonPrimitive.content)
        assertEquals("2", element.getValue("by_weekday").jsonArray.single().jsonPrimitive.content)
    }

    @Test
    @JsName("test3")
    fun `GuildScheduledEvent parses recurrence rule`() = runTest {
        val event = Json.decodeFromString(DiscordGuildScheduledEvent.serializer(), file("guildscheduledevent"))

        assertEquals("906727003124498462", event.id.toString())
        assertEquals("Weekly Meeting", event.name)
        assertEquals(GuildScheduledEventStatus.Scheduled, event.status)

        val rule = assertNotNull(event.recurrenceRule)
        assertEquals(Instant.parse("2023-11-15T18:00:00Z"), rule.start)
        assertNull(rule.end)
        assertEquals(RecurrenceRuleFrequency.Weekly, rule.frequency)
        assertEquals(1, rule.interval)
        assertEquals(listOf(RecurrenceRuleWeekday.Wednesday), rule.byWeekday)
        assertNull(rule.byNWeekday)
    }

    @Test
    @JsName("test4")
    fun `GuildScheduledEvent parses recurrence exceptions`() = runTest {
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
    @JsName("test5")
    fun `GuildScheduledEvent defaults missing recurrence exceptions`() = runTest {
        val event = Json.decodeFromString(DiscordGuildScheduledEvent.serializer(), file("guildscheduledevent"))

        assertTrue(event.guildScheduledEventExceptions.isEmpty())
    }

    @Test
    @JsName("test6")
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
