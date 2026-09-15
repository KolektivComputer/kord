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
 * See [RecurrenceRuleMonth]s in the [Discord Developer Documentation](https://discord.com/developers/docs/resources/guild-scheduled-event#guild-scheduled-event-recurrence-rule-object-guild-scheduled-event-recurrence-rule-month).
 */
@Serializable(with = RecurrenceRuleMonth.Serializer::class)
public sealed class RecurrenceRuleMonth(
    /**
     * The raw value used by Discord.
     */
    public val `value`: Int,
) {
    final override fun equals(other: Any?): Boolean = this === other || (other is RecurrenceRuleMonth && this.value == other.value)

    final override fun hashCode(): Int = value.hashCode()

    final override fun toString(): String = if (this is Unknown) "RecurrenceRuleMonth.Unknown(value=$value)" else "RecurrenceRuleMonth.${this::class.simpleName}"

    /**
     * An unknown [RecurrenceRuleMonth].
     *
     * This is used as a fallback for [RecurrenceRuleMonth]s that haven't been added to Kord yet.
     */
    public class Unknown internal constructor(
        `value`: Int,
    ) : RecurrenceRuleMonth(value)

    public object January : RecurrenceRuleMonth(1)

    public object February : RecurrenceRuleMonth(2)

    public object March : RecurrenceRuleMonth(3)

    public object April : RecurrenceRuleMonth(4)

    public object May : RecurrenceRuleMonth(5)

    public object June : RecurrenceRuleMonth(6)

    public object July : RecurrenceRuleMonth(7)

    public object August : RecurrenceRuleMonth(8)

    public object September : RecurrenceRuleMonth(9)

    public object October : RecurrenceRuleMonth(10)

    public object November : RecurrenceRuleMonth(11)

    public object December : RecurrenceRuleMonth(12)

    internal object Serializer : KSerializer<RecurrenceRuleMonth> {
        override val descriptor: SerialDescriptor =
                PrimitiveSerialDescriptor("dev.kord.common.entity.RecurrenceRuleMonth", PrimitiveKind.INT)

        override fun serialize(encoder: Encoder, `value`: RecurrenceRuleMonth) {
            encoder.encodeInt(value.value)
        }

        override fun deserialize(decoder: Decoder): RecurrenceRuleMonth = from(decoder.decodeInt())
    }

    public companion object {
        /**
         * A [List] of all known [RecurrenceRuleMonth]s.
         */
        public val entries: List<RecurrenceRuleMonth> by lazy(mode = PUBLICATION) {
            listOf(
                January,
                February,
                March,
                April,
                May,
                June,
                July,
                August,
                September,
                October,
                November,
                December,
            )
        }

        /**
         * Returns an instance of [RecurrenceRuleMonth] with [RecurrenceRuleMonth.value] equal to the specified [value].
         */
        public fun from(`value`: Int): RecurrenceRuleMonth = when (value) {
            1 -> January
            2 -> February
            3 -> March
            4 -> April
            5 -> May
            6 -> June
            7 -> July
            8 -> August
            9 -> September
            10 -> October
            11 -> November
            12 -> December
            else -> Unknown(value)
        }
    }
}
