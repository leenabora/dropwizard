package uk.co.o2.stockservice.exceptionmapper

import ch.qos.logback.classic.Level
import com.sun.jersey.api.NotFoundException
import org.junit.Test
import uk.co.o2.services.exception.SoaDataException
import uk.co.o2.services.test.base.LoggingTest

import javax.ws.rs.WebApplicationException
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider


class UnknownErrorExceptionMapperTest extends LoggingTest {
    private ExceptionMapper<Throwable> mapper = new UnknownErrorExceptionMapper()

    @Test
    void "should contain a class level Provider annotation"() {
        assert UnknownErrorExceptionMapper.getAnnotation(Provider)
    }

    @Test
    void "should return the exceptions normal response in the event of a web application exception"() {
        WebApplicationException expectedException = new NotFoundException("foo")

        Response response = mapper.toResponse(expectedException)

        assert response.is(expectedException.response)
    }

    @Test
    void "should return a response with a 500 status code and a content type of application/json"() {
        Response response = mapper.toResponse(new SoaDataException("Error"))

        assert response.status == Response.Status.INTERNAL_SERVER_ERROR.statusCode
        assert response.metadata["Content-Type"][0].toString() == MediaType.APPLICATION_JSON
    }

    @Test
    void "should return the error details in the response body"() {
        Response response = mapper.toResponse(new SoaDataException("Error"))

        assert response.entity == [unknownError: "Error"]
    }

    @Test
    void "should return the null for the error details in the response body if the exception has no message"() {
        Response response = mapper.toResponse(new IllegalArgumentException())

        assert response.entity == [unknownError: "null"]
    }

    @Test
    void "should log an error with the unknown error details when dealing with non soa faults"() {
        mapper.toResponse(new IllegalArgumentException("Error"))

        def loggingEvents = getLoggingEvents()
        assert loggingEvents.size() == 1

        def logEvent = loggingEvents.first()
        assert logEvent.level == Level.ERROR
        assert logEvent.formattedMessage == "ERROR_PC000001 Unknown Error: Error"
    }

}
