package uk.co.o2.stockservice.filter

import javax.servlet.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.ws.rs.HttpMethod

class HomeFilter implements Filter {
    private static final String HOME = "/home"

    @Override
    void init(FilterConfig filterConfig) {
        // does nothing
    }

    @Override

    void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        HttpServletRequest httpRequest = (HttpServletRequest) request
        String uri = httpRequest.requestURI.substring(httpRequest.contextPath.length())
        String method = httpRequest.method

        if (method == HttpMethod.GET && (uri == "" || uri == "/")) {
            ((HttpServletResponse) response).sendRedirect(httpRequest.contextPath + HOME)
        } else {
            chain.doFilter(request, response)
        }

    }

    @Override
    void destroy() {
        // does nothing
    }
}
