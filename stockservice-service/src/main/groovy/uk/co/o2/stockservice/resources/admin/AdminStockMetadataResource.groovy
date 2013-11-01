package uk.co.o2.stockservice.resources.admin

import uk.co.o2.services.serialization.InputSchema
import uk.co.o2.stockservice.service.StockService

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

import static javax.ws.rs.core.MediaType.APPLICATION_JSON
import static javax.ws.rs.core.Response.Status.CREATED
import static javax.ws.rs.core.Response.status

@Path("/admin/stockMetadata/")
@Produces(APPLICATION_JSON)
class AdminStockMetadataResource {

    private StockService stockService

    AdminStockMetadataResource(StockService stockService) {
        this.stockService = stockService
    }

    @POST
    @Consumes(APPLICATION_JSON)
    public Response saveMetadata(@InputSchema("admin_schemas/stock-metadata-schema.json") Map map) {
        status(CREATED).type(APPLICATION_JSON).entity("{'message': 'successfully saved delivery options and stock mesages'}").build()
    }

}
