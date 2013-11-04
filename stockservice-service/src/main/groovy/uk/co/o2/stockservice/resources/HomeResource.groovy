package uk.co.o2.stockservice.resources

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType

@Path("/home")
class HomeResource {

    @GET
    @Produces(MediaType.TEXT_HTML)
    String get() {
        "<h1>Hello, this is the Stock Service.</h1>"
    }
}
