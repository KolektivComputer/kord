package dev.kord.core.cache.data

import dev.kord.common.entity.DiscordGuildScheduledEvent
import dev.kord.common.entity.DiscordGuildScheduledEventException
import dev.kord.common.entity.GuildScheduledEventPrivacyLevel
import dev.kord.common.entity.GuildScheduledEventStatus
import dev.kord.common.entity.ScheduledEntityType
import dev.kord.common.entity.Snowflake
import kotlin.time.Instant
import kotlin.js.JsName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GuildScheduledEventDataTest {

    private fun event(exceptions: List<DiscordGuildScheduledEventException>) = DiscordGuildScheduledEvent(
        id = Snowflake(1),
        guildId = Snowflake(2),
        channelId = null,
        name = "event",
        scheduledStartTime = Instant.parse("2023-11-15T18:00:00Z"),
        scheduledEndTime = null,
        privacyLevel = GuildScheduledEventPrivacyLevel.GuildOnly,
        status = GuildScheduledEventStatus.Scheduled,
        entityType = ScheduledEntityType.External,
        entityId = null,
        entityMetadata = null,
        guildScheduledEventExceptions = exceptions,
    )

    @Test
    @JsName("test1")
    fun `exceptions are mapped from the event`() {
        val exception = DiscordGuildScheduledEventException(
            eventId = Snowflake(1),
            eventExceptionId = Snowflake(3),
            scheduledStartTime = Instant.parse("2023-11-22T18:00:00Z"),
            scheduledEndTime = null,
            isCanceled = false,
        )

        val data = GuildScheduledEventData.from(event(listOf(exception)))

        assertEquals(listOf(exception), data.guildScheduledEventExceptions)
    }

    @Test
    @JsName("test2")
    fun `events without exceptions default to an empty list`() {
        val data = GuildScheduledEventData.from(event(emptyList()))

        assertTrue(data.guildScheduledEventExceptions.isEmpty())
    }
}
