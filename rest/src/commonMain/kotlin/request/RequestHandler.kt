package dev.kord.rest.request

import dev.kord.common.KordConstants
import io.ktor.http.HttpHeaders.Authorization
import io.ktor.http.HttpHeaders.UserAgent

/**
 * Handles Discord API requests.
 */
public interface RequestHandler {

    /**
     * The Discord authorization token used on requests.
     */
    public val token: String

    /**
     * The scheme prepended to [token] in the `Authorization` header, `Bot` by default.
     *
     * Set this to `Bearer` when [token] is an OAuth2 access token.
     */
    public val tokenPrefix: String get() = "Bot"

    /**
     * Executes the [request], abiding by the active rate limits and returning the response [R].
     *
     * @throws RestRequestException when a non-rate limit error response is returned.
     */
    public suspend fun <B : Any, R> handle(request: Request<B, R>): R

    public suspend fun <T> intercept(builder: RequestBuilder<T>) {
        builder.apply {
            unencodedHeader(UserAgent, KordConstants.USER_AGENT)
            if (route.requiresAuthorizationHeader) {
                unencodedHeader(Authorization, "$tokenPrefix $token")
            }
        }
    }
}
