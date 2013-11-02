package uk.co.o2.stockservice.resources

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import uk.co.o2.stockservice.model.StockSummary
import uk.co.o2.stockservice.service.StockService

import javax.ws.rs.*
import javax.ws.rs.core.Response

import static javax.ws.rs.core.MediaType.APPLICATION_JSON
import static javax.ws.rs.core.Response.Status.NOT_FOUND
import static javax.ws.rs.core.Response.Status.OK
import static javax.ws.rs.core.Response.status

@Path("/stocks/{sku}")
@Produces(APPLICATION_JSON)
class StockResource {

    private static final Logger LOG = LoggerFactory.getLogger(StockResource)

    private StockService stockService

    StockResource(StockService stockService) {
        this.stockService = stockService
    }

    @GET
    public Response getStockSummary(@PathParam("sku") String sku, @HeaderParam("X-Channel") String channel) {
        LOG.info("sku " + sku)

        StockSummary stockSummary = stockService.getStockSummary(sku, channel)
        if (stockSummary) {
            status(OK).type(APPLICATION_JSON).entity(stockSummary).build()

        } else {
            status(NOT_FOUND).type(APPLICATION_JSON).entity("{'message': 'sku ${sku} not found '}").build()
        }
    }

    @PUT
    @Path("/reserve")
    public Response reserveStock(@PathParam("sku") String sku, @HeaderParam("X-Channel") String channel) {
        status(OK).type(APPLICATION_JSON).entity("{'stockReservationToken': '100329482385483'}").build()
    }

    @PUT
    @Path("/release")
    public Response releaseStock(@PathParam("sku") String sku, @QueryParam("stockReservationToken") String stockReservationToken, @HeaderParam("X-Channel") String channel) {
        status(OK).type(APPLICATION_JSON).entity("{'message': 'successfully released the stock'}").build()
    }

}