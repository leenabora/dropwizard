package uk.co.o2.stockservice.filter

import org.gmock.GMockController
import org.junit.Before
import org.junit.Test

import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.ws.rs.HttpMethod


class HomeFilterTest {

    private final GMockController mock = new GMockController()
    private HttpServletRequest request
    private HttpServletResponse response
    private FilterChain filterChain
    private final HomeFilter filter = new HomeFilter()

    @Before
    void setup() {
        request = (HttpServletRequest) mock.mock(HttpServletRequest)
        response = (HttpServletResponse) mock.mock(HttpServletResponse)
        filterChain = (FilterChain) mock.mock(FilterChain)
    }

    @Test
    void "doFilter_should pass long Get Urls through"() {
        def contextPath = "/context"
        request.method.returns(HttpMethod.GET)
        request.contextPath.returns(contextPath)
        request.requestURI.returns(contextPath + "/something")

        filterChain.doFilter(request, response)

        mock.play {
            filter.doFilter(request, response, filterChain)
        }
    }

    @Test
    void "doFilter_should pass any post Urls through"() {
        def contextPath = "/context"
        request.method.returns(HttpMethod.POST)
        request.contextPath.returns(contextPath)
        request.requestURI.returns(contextPath)

        filterChain.doFilter(request, response)

        mock.play {
            filter.doFilter(request, response, filterChain)
        }
    }

    @Test
    void "init_should do nothing"() {
        FilterConfig filterConfig = (FilterConfig) mock.mock(FilterConfig)
        mock.play {
            filter.init(filterConfig)
        }
    }

    @Test
    void "destroy_should do nothing"() {
        mock.play {
            filter.destroy()
        }
    }
}

