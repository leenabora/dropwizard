package uk.co.o2.stockservice.exceptionmapper

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import uk.co.o2.services.exception.ErrorCode

import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.Response.Status
import javax.ws.rs.core.UriInfo
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider

@Provider
class UnknownErrorExceptionMapper implements ExceptionMapper<Throwable> {
    private final Logger LOG = LoggerFactory.getLogger(UnknownErrorExceptionMapper)

    @Context
    UriInfo uriInfo

    @Override
    Response toResponse(Throwable exception) {
        if (exception instanceof WebApplicationException) {
            return exception.response
        }

        LOG.error(ErrorCode.UnknownError.message(exception.message), exception)
        Response.status(Status.INTERNAL_SERVER_ERROR).type(MediaType.APPLICATION_JSON).entity([unknownError: exception.message ?: "null"]).build()
    }

}
