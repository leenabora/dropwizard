package uk.co.o2.stockservice.resources

import uk.co.o2.stockservice.configuration.PropertyConfigurator
import uk.co.o2.stockservice.model.StockSummary

import javax.ws.rs.*
import javax.ws.rs.core.Response

import static javax.ws.rs.core.MediaType.APPLICATION_JSON
import static javax.ws.rs.core.Response.Status.NOT_FOUND
import static javax.ws.rs.core.Response.Status.OK
import static javax.ws.rs.core.Response.status

@Path("/stocks/{sku}")
@Produces(APPLICATION_JSON)
class StockResource {

    @GET
    public Response getStockSummary(@PathParam("sku") String sku, @QueryParam("channel") String channel) {
        def prop = new PropertyConfigurator()
        println(prop.getValue("name"))

        def prop1 = new PropertyConfigurator()
        println(prop1.getValue("name"))

        def prop2 = new PropertyConfigurator()
        println(prop2.getValue("name"))

        def prop3 = new PropertyConfigurator()
        println(prop3.getValue("name"))


        if (sku == "sku-123") {
            StockSummary stockSummary = new StockSummary(1, sku, channel, "PreOrder", 500);
            status(OK).type(APPLICATION_JSON).entity(stockSummary).build()

        } else {
            status(NOT_FOUND).type(APPLICATION_JSON).entity("{'message': 'sku ${sku} not found '}").build()
        }
    }

    @PUT
    @Path("/reserve")
    public Response reserveStock(@PathParam("sku") String sku, @QueryParam("channel") String channel) {
        status(OK).type(APPLICATION_JSON).entity("{'stockReservationToken': '100329482385483'}").build()
    }

    @PUT
    @Path("/release")
    public Response releaseStock(@PathParam("sku") String sku, @QueryParam("stockReservationToken") String stockReservationToken) {
        status(OK).type(APPLICATION_JSON).entity("{'message': 'successfully released the stock'}").build()
    }

}