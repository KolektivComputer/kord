@file:Generate(
    INT_KORD_ENUM, name = "RecurrenceRuleFrequency",
    docUrl = "https://discord.com/developers/docs/resources/guild-scheduled-event#guild-scheduled-event-recurrence-rule-object-guild-scheduled-event-recurrence-rule-frequency",
    entries = [
        Entry("Yearly", intValue = 0),
        Entry("Monthly", intValue = 1),
        Entry("Weekly", intValue = 2),
        Entry("Daily", intValue = 3),
    ],
)

@file:Generate(
    INT_KORD_ENUM, name = "RecurrenceRuleWeekday",
    docUrl = "https://discord.com/developers/docs/resources/guild-scheduled-event#guild-scheduled-event-recurrence-rule-object-guild-scheduled-event-recurrence-rule-weekday",
    entries = [
        Entry("Monday", intValue = 0),
        Entry("Tuesday", intValue = 1),
        Entry("Wednesday", intValue = 2),
        Entry("Thursday", intValue = 3),
        Entry("Friday", intValue = 4),
        Entry("Saturday", intValue = 5),
        Entry("Sunday", intValue = 6),
    ],
)

@file:Generate(
    INT_KORD_ENUM, name = "RecurrenceRuleMonth",
    docUrl = "https://discord.com/developers/docs/resources/guild-scheduled-event#guild-scheduled-event-recurrence-rule-object-guild-scheduled-event-recurrence-rule-month",
    entries = [
        Entry("January", intValue = 1),
        Entry("February", intValue = 2),
        Entry("March", intValue = 3),
        Entry("April", intValue = 4),
        Entry("May", intValue = 5),
        Entry("June", intValue = 6),
        Entry("July", intValue = 7),
        Entry("August", intValue = 8),
        Entry("September", intValue = 9),
        Entry("October", intValue = 10),
        Entry("November", intValue = 11),
        Entry("December", intValue = 12),
    ],
)

package dev.kord.common.entity

import dev.kord.ksp.Generate
import dev.kord.ksp.Generate.EntityType.INT_KORD_ENUM
import dev.kord.ksp.Generate.Entry
import kotlin.time.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Representation of a
 * [Guild Scheduled Event Recurrence Rule Structure](https://discord.com/developers/docs/resources/guild-scheduled-event#guild-scheduled-event-recurrence-rule-object-guild-scheduled-event-recurrence-rule-structure).
 *
 * Recurrence rules are a subset of the behaviors [defined in the iCalendar RFC](https://datatracker.ietf.org/doc/html/rfc5545).
 *
 * @property start The starting time of the recurrence interval.
 * @property end The ending time of the recurrence interval, if any.
 * @property frequency How often the event occurs.
 * @property interval The spacing between the events, defined by [frequency].
 * @property byWeekday The set of specific days within a week for the event to recur on, if any.
 * @property byNWeekday The list of specific days within a specific week (1-5) to recur on, if any.
 * @property byMonth The set of specific months to recur on, if any.
 * @property byMonthDay The set of specific dates within a month to recur on, if any.
 * @property byYearDay The set of days within a year to recur on (1-364), if any.
 * @property count The total amount of times the event is allowed to recur before stopping, if any.
 */
@Serializable
public data class DiscordRecurrenceRule(
    val start: Instant? = null,
    val end: Instant? = null,
    val frequency: RecurrenceRuleFrequency,
    val interval: Int? = null,
    @SerialName("by_weekday")
    val byWeekday: List<RecurrenceRuleWeekday>? = null,
    @SerialName("by_n_weekday")
    val byNWeekday: List<DiscordRecurrenceRuleNWeekday>? = null,
    @SerialName("by_month")
    val byMonth: List<RecurrenceRuleMonth>? = null,
    @SerialName("by_month_day")
    val byMonthDay: List<Int>? = null,
    @SerialName("by_year_day")
    val byYearDay: List<Int>? = null,
    val count: Int? = null,
)

/**
 * Representation of a
 * [Guild Scheduled Event Recurrence Rule - N_Weekday Structure](https://discord.com/developers/docs/resources/guild-scheduled-event#guild-scheduled-event-recurrence-rule-object-guild-scheduled-event-recurrence-rule-nweekday-structure).
 *
 * @property n The week to reoccur on (1-5).
 * @property day The [day][RecurrenceRuleWeekday] within the week to reoccur on.
 */
@Serializable
public data class DiscordRecurrenceRuleNWeekday(
    val n: Int,
    val day: Int,
)
