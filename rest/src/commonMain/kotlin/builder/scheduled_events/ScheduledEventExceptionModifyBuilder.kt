package dev.kord.rest.builder.scheduled_events

import dev.kord.common.annotation.KordDsl
import dev.kord.common.entity.optional.Optional
import dev.kord.common.entity.optional.delegate.delegate
import dev.kord.rest.builder.AuditRequestBuilder
import dev.kord.rest.json.request.ScheduledEventExceptionModifyRequest
import kotlin.time.Instant

@KordDsl
public class ScheduledEventExceptionModifyBuilder : AuditRequestBuilder<ScheduledEventExceptionModifyRequest> {
    override var reason: String? = null

    private var _scheduledStartTime: Optional<Instant?> = Optional.Missing()

    /** The overridden [Instant] this occurrence will start at. */
    public var scheduledStartTime: Instant? by ::_scheduledStartTime.delegate()

    private var _scheduledEndTime: Optional<Instant?> = Optional.Missing()

    /** The overridden [Instant] this occurrence will end at. */
    public var scheduledEndTime: Instant? by ::_scheduledEndTime.delegate()

    private var _isCanceled: Optional<Boolean?> = Optional.Missing()

    /** Whether this occurrence is canceled. */
    public var isCanceled: Boolean? by ::_isCanceled.delegate()

    override fun toRequest(): ScheduledEventExceptionModifyRequest = ScheduledEventExceptionModifyRequest(
        scheduledStartTime = _scheduledStartTime,
        scheduledEndTime = _scheduledEndTime,
        isCanceled = _isCanceled,
    )
}
