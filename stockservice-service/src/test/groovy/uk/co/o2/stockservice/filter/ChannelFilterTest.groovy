package uk.co.o2.stockservice.filter

import com.sun.jersey.spi.container.ContainerRequest
import org.gmock.GMockController
import org.junit.Ignore
import org.junit.Test
import uk.co.o2.services.exception.BadTouchPointContextException
import uk.co.o2.services.test.base.LoggingTest

import static org.junit.Assert.fail

@Ignore
class ChannelFilterTest extends LoggingTest {
    public static final CHANNEL_HEADER = "X-Channel"

    GMockController mock = new GMockController()

    ContainerRequest containerRequest = (ContainerRequest) mock.mock(ContainerRequest)

    ChannelFilter channelFilter = new ChannelFilter()

    @Test
    void "should pass on the request given a valid channel header"() {
        mock.ordered {
            containerRequest.getPathSegments().returns(["stocks", "sku0123"])
            containerRequest.path.returns("stocks/sku0123")
            containerRequest.getHeaderValue(CHANNEL_HEADER).returns("CFU")
        }

        mock.play {
            assert channelFilter.filter(containerRequest) == containerRequest
        }
    }

    @Test
    void "should return a 400 response given invalid channel header"() {
        mock.ordered {
            containerRequest.getPathSegments().returns(["stocks", "sku0123"])
            containerRequest.path.returns("stocks/sku0123")
            containerRequest.getHeaderValue(CHANNEL_HEADER).returns("something")
        }

        mock.play {
            try {
                assert channelFilter.filter(containerRequest) == containerRequest

                fail("BadTouchPointContextException expected")
            } catch (BadTouchPointContextException e) {
                assert e.message == "missing channel or unknown channel"
            }
        }
    }


    @Test
    void "should send 400 response given prod mode and no touchpoint context headers"() {
        mock.ordered {
            containerRequest.getPathSegments().returns(["stocks", "sku0123"])
            containerRequest.path.returns("stocks/sku0123")

            containerRequest.getHeaderValue(CHANNEL_HEADER).returns(null)
        }

        mock.play {
            try {
                assert channelFilter.filter(containerRequest) == containerRequest

                fail("BadTouchPointContextException expected")
            } catch (BadTouchPointContextException e) {
                assert e.message == "missing channel or unknown channel"
            }
        }
    }

    @Test
    void "should not be applied for the admin url"() {
        containerRequest.getPathSegments().returns(["admin", "stocks"])
        containerRequest.path.returns("admin/stocks")


        mock.play {
            assert channelFilter.filter(containerRequest) == containerRequest
        }
    }

    @Test
    void "should not be applied for the maintenance url"() {
        containerRequest.getPathSegments().returns(["maintenance"])
        containerRequest.path.returns("maintenance")


        mock.play {
            assert channelFilter.filter(containerRequest) == containerRequest
        }
    }

    @Test
    void "should not be applied for the ops url"() {
        containerRequest.getPathSegments().returns(["ops"])
        containerRequest.path.returns("ops")

        mock.play {
            assert channelFilter.filter(containerRequest) == containerRequest
        }

    }

    @Test
    void "should not be applied for the home url"() {
        containerRequest.getPathSegments().returns(["home"])
        containerRequest.path.returns("home")

        mock.play {
            assert channelFilter.filter(containerRequest) == containerRequest
        }
    }

    @Test
    void "should not be applied for the health check url"() {
        containerRequest.getPathSegments().returns(["healthcheck"])
        containerRequest.path.returns("healthcheck")

        mock.play {
            assert channelFilter.filter(containerRequest) == containerRequest
        }
    }

}