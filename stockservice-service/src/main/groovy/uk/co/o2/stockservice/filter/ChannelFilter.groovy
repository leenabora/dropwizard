package uk.co.o2.stockservice.filter

import com.sun.jersey.spi.container.ContainerRequest
import com.sun.jersey.spi.container.ContainerRequestFilter
import uk.co.o2.services.exception.BadTouchPointContextException
import uk.co.o2.stockservice.domain.Channel

import javax.ws.rs.core.PathSegment

class ChannelFilter implements ContainerRequestFilter {

    @Override
    ContainerRequest filter(ContainerRequest request) {
        List<PathSegment> pathSegments = request.getPathSegments()
        if (pathSegments.size() > 0) {
            if (pathSegments.get(0).path != "admin") {
                String channel = request.getHeaderValue("X-Channel")

                if (!Channel.fromString(channel)) {
                    throw new BadTouchPointContextException("missing channel or unknown channel")
                }
            }

            return request
        }
    }
}