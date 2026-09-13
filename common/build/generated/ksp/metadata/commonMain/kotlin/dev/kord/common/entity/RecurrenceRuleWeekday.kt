// THIS FILE IS AUTO-GENERATED, DO NOT EDIT!
@file:Suppress(names = arrayOf("IncorrectFormatting", "ReplaceArrayOfWithLiteral", "SpellCheckingInspection", "GrazieInspection"))

package dev.kord.common.entity

import kotlin.LazyThreadSafetyMode.PUBLICATION
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 *
 *
 * See [RecurrenceRuleWeekday]s in the [Discord Developer Documentation](https://discord.com/developers/docs/resources/guild-scheduled-event#guild-scheduled-event-recurrence-rule-object-guild-scheduled-event-recurrence-rule-weekday).
 */
@Serializable(with = RecurrenceRuleWeekday.Serializer::class)
public sealed class RecurrenceRuleWeekday(
    /**
     * The raw value used by Discord.
     */
    public val `value`: Int,
) {
    final override fun equals(other: Any?): Boolean = this === other || (other is RecurrenceRuleWeekday && this.value == other.value)

    final override fun hashCode(): Int = value.hashCode()

    final override fun toString(): String = if (this is Unknown) "RecurrenceRuleWeekday.Unknown(value=$value)" else "RecurrenceRuleWeekday.${this::class.simpleName}"

    /**
     * An unknown [RecurrenceRuleWeekday].
     *
     * This is used as a fallback for [RecurrenceRuleWeekday]s that haven't been added to Kord yet.
     */
    public class Unknown internal constructor(
        `value`: Int,
    ) : RecurrenceRuleWeekday(value)

    public object Monday : RecurrenceRuleWeekday(0)

    public object Tuesday : RecurrenceRuleWeekday(1)

    public object Wednesday : RecurrenceRuleWeekday(2)

    public object Thursday : RecurrenceRuleWeekday(3)

    public object Friday : RecurrenceRuleWeekday(4)

    public object Saturday : RecurrenceRuleWeekday(5)

    public object Sunday : RecurrenceRuleWeekday(6)

    internal object Serializer : KSerializer<RecurrenceRuleWeekday> {
        override val descriptor: SerialDescriptor =
                PrimitiveSerialDescriptor("dev.kord.common.entity.RecurrenceRuleWeekday", PrimitiveKind.INT)

        override fun serialize(encoder: Encoder, `value`: RecurrenceRuleWeekday) {
            encoder.encodeInt(value.value)
        }

        override fun deserialize(decoder: Decoder): RecurrenceRuleWeekday = from(decoder.decodeInt())
    }

    public companion object {
        /**
         * A [List] of all known [RecurrenceRuleWeekday]s.
         */
        public val entries: List<RecurrenceRuleWeekday> by lazy(mode = PUBLICATION) {
            listOf(
                Monday,
                Tuesday,
                Wednesday,
                Thursday,
                Friday,
                Saturday,
                Sunday,
            )
        }

        /**
         * Returns an instance of [RecurrenceRuleWeekday] with [RecurrenceRuleWeekday.value] equal to the specified [value].
         */
        public fun from(`value`: Int): RecurrenceRuleWeekday = when (value) {
            0 -> Monday
            1 -> Tuesday
            2 -> Wednesday
            3 -> Thursday
            4 -> Friday
            5 -> Saturday
            6 -> Sunday
            else -> Unknown(value)
        }
    }
}
