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
 * See [RecurrenceRuleFrequency]s in the [Discord Developer Documentation](https://discord.com/developers/docs/resources/guild-scheduled-event#guild-scheduled-event-recurrence-rule-object-guild-scheduled-event-recurrence-rule-frequency).
 */
@Serializable(with = RecurrenceRuleFrequency.Serializer::class)
public sealed class RecurrenceRuleFrequency(
    /**
     * The raw value used by Discord.
     */
    public val `value`: Int,
) {
    final override fun equals(other: Any?): Boolean = this === other || (other is RecurrenceRuleFrequency && this.value == other.value)

    final override fun hashCode(): Int = value.hashCode()

    final override fun toString(): String = if (this is Unknown) "RecurrenceRuleFrequency.Unknown(value=$value)" else "RecurrenceRuleFrequency.${this::class.simpleName}"

    /**
     * An unknown [RecurrenceRuleFrequency].
     *
     * This is used as a fallback for [RecurrenceRuleFrequency]s that haven't been added to Kord yet.
     */
    public class Unknown internal constructor(
        `value`: Int,
    ) : RecurrenceRuleFrequency(value)

    public object Yearly : RecurrenceRuleFrequency(0)

    public object Monthly : RecurrenceRuleFrequency(1)

    public object Weekly : RecurrenceRuleFrequency(2)

    public object Daily : RecurrenceRuleFrequency(3)

    internal object Serializer : KSerializer<RecurrenceRuleFrequency> {
        override val descriptor: SerialDescriptor =
                PrimitiveSerialDescriptor("dev.kord.common.entity.RecurrenceRuleFrequency", PrimitiveKind.INT)

        override fun serialize(encoder: Encoder, `value`: RecurrenceRuleFrequency) {
            encoder.encodeInt(value.value)
        }

        override fun deserialize(decoder: Decoder): RecurrenceRuleFrequency = from(decoder.decodeInt())
    }

    public companion object {
        /**
         * A [List] of all known [RecurrenceRuleFrequency]s.
         */
        public val entries: List<RecurrenceRuleFrequency> by lazy(mode = PUBLICATION) {
            listOf(
                Yearly,
                Monthly,
                Weekly,
                Daily,
            )
        }

        /**
         * Returns an instance of [RecurrenceRuleFrequency] with [RecurrenceRuleFrequency.value] equal to the specified [value].
         */
        public fun from(`value`: Int): RecurrenceRuleFrequency = when (value) {
            0 -> Yearly
            1 -> Monthly
            2 -> Weekly
            3 -> Daily
            else -> Unknown(value)
        }
    }
}
