package dev.kord.rest.request

import dev.kord.common.entity.DiscordRecurrenceRule
import dev.kord.common.entity.DiscordRecurrenceRuleNWeekday
import dev.kord.common.entity.GuildScheduledEventPrivacyLevel
import dev.kord.common.entity.RecurrenceRuleFrequency
import dev.kord.common.entity.RecurrenceRuleMonth
import dev.kord.common.entity.RecurrenceRuleWeekday
import dev.kord.common.entity.ScheduledEntityType
import dev.kord.common.entity.Snowflake
import dev.kord.common.entity.optional.Optional
import dev.kord.common.entity.optional.OptionalSnowflake
import dev.kord.rest.builder.guild.ScheduledEventCreateBuilder
import dev.kord.rest.builder.scheduled_events.ScheduledEventModifyBuilder
import dev.kord.rest.json.request.GuildScheduledEventCreateRequest
import dev.kord.rest.json.request.ScheduledEventModifyRequest
import kotlin.time.Instant
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ScheduledEventRequestsTest {

    private val rule = DiscordRecurrenceRule(
        frequency = RecurrenceRuleFrequency.Weekly,
        interval = 1,
        byWeekday = listOf(RecurrenceRuleWeekday.Wednesday),
    )

    @Test
    @JsName("test1")
    fun `create request serializes recurrence rule`() {
        val request = GuildScheduledEventCreateRequest(
            channelId = OptionalSnowflake.Value(Snowflake(42)),
            name = "event",
            privacyLevel = GuildScheduledEventPrivacyLevel.GuildOnly,
            scheduledStartTime = Instant.fromEpochSeconds(1_700_000_000),
            entityType = ScheduledEntityType.Voice,
            recurrenceRule = Optional.Value(rule),
        )

        val json = Json.encodeToString(GuildScheduledEventCreateRequest.serializer(), request)
        val element = Json.parseToJsonElement(json).jsonObject

        assertTrue("recurrence_rule" in element)
        val ruleElement = element.getValue("recurrence_rule").jsonObject
        assertEquals("2", ruleElement.getValue("frequency").jsonPrimitive.content)
        assertEquals("1", ruleElement.getValue("interval").jsonPrimitive.content)
        assertEquals("2", ruleElement.getValue("by_weekday").jsonArray.single().jsonPrimitive.content)
    }

    @Test
    @JsName("test2")
    fun `create request omits missing recurrence rule`() {
        val request = GuildScheduledEventCreateRequest(
            channelId = OptionalSnowflake.Value(Snowflake(42)),
            name = "event",
            privacyLevel = GuildScheduledEventPrivacyLevel.GuildOnly,
            scheduledStartTime = Instant.fromEpochSeconds(1_700_000_000),
            entityType = ScheduledEntityType.Voice,
        )

        val element = Json.parseToJsonElement(
            Json.encodeToString(GuildScheduledEventCreateRequest.serializer(), request)
        ).jsonObject

        assertTrue("recurrence_rule" !in element)
    }

    @Test
    @JsName("test3")
    fun `modify request serializes recurrence rule`() {
        val request = ScheduledEventModifyRequest(recurrenceRule = Optional.Value(rule))

        val json = Json.encodeToString(ScheduledEventModifyRequest.serializer(), request)
        val element = Json.parseToJsonElement(json).jsonObject

        assertEquals(setOf("recurrence_rule"), element.keys)
        assertEquals("2", element.getValue("recurrence_rule").jsonObject.getValue("frequency").jsonPrimitive.content)
    }

    @Test
    @JsName("test4")
    fun `builder maps recurrence rule to request`() {
        val expected = DiscordRecurrenceRule(
            frequency = RecurrenceRuleFrequency.Monthly,
            interval = 1,
            byNWeekday = listOf(DiscordRecurrenceRuleNWeekday(n = 4, day = 2)),
        )

        val create = ScheduledEventCreateBuilder(
            name = "event",
            privacyLevel = GuildScheduledEventPrivacyLevel.GuildOnly,
            scheduledStartTime = Instant.fromEpochSeconds(1_700_000_000),
            entityType = ScheduledEntityType.Voice,
        ).apply { recurrenceRule = expected }

        assertEquals(Optional.Value(expected), create.toRequest().recurrenceRule)

        val modify = ScheduledEventModifyBuilder().apply { recurrenceRule = expected }

        assertEquals(Optional.Value(expected), modify.toRequest().recurrenceRule)
    }

    @Test
    @JsName("test5")
    fun `recurrence rule serializes by n weekday and by month`() {
        val request = ScheduledEventModifyRequest(
            recurrenceRule = Optional.Value(
                DiscordRecurrenceRule(
                    frequency = RecurrenceRuleFrequency.Yearly,
                    interval = 1,
                    byMonth = listOf(RecurrenceRuleMonth.July),
                    byMonthDay = listOf(24),
                )
            )
        )

        val json = Json.encodeToString(ScheduledEventModifyRequest.serializer(), request)
        val ruleElement = Json.parseToJsonElement(json).jsonObject.getValue("recurrence_rule").jsonObject

        assertEquals("0", ruleElement.getValue("frequency").jsonPrimitive.content)
        assertEquals("7", ruleElement.getValue("by_month").jsonArray.single().jsonPrimitive.content)
        assertEquals("24", ruleElement.getValue("by_month_day").jsonArray.single().jsonPrimitive.content)
    }
}
