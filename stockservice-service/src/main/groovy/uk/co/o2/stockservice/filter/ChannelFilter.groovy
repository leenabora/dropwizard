package uk.co.o2.stockservice.filter

import com.sun.jersey.spi.container.ContainerRequest
import com.sun.jersey.spi.container.ContainerRequestFilter
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import uk.co.o2.services.exception.BadTouchPointContextException
import uk.co.o2.services.exception.ErrorCode
import uk.co.o2.stockservice.domain.Channel

import javax.ws.rs.core.PathSegment

class ChannelFilter implements ContainerRequestFilter {
    private static final Logger LOG = LoggerFactory.getLogger(ChannelFilter)

    private static final List<String> passThroughPaths = ["admin", "home", "ops", "healthCheck", "healthcheck", "maintenance"]
    private static final String CHANNEL = "X-Channel"

    @Override
    ContainerRequest filter(ContainerRequest request) {
        List<PathSegment> pathSegments = request.getPathSegments()

        if (pathSegments.size() > 0) {

            if (!passThroughPaths.any { pathSegments.get(0).path }) {
                String channel = request.getHeaderValue(CHANNEL)

                if (!Channel.fromString(channel)) {
                    LOG.error(ErrorCode.InvalidTouchPointContext.message("missing channel or unknown channel"))

                    throw new BadTouchPointContextException("missing channel or unknown channel")
                }
            }
        }
        return request
    }
}