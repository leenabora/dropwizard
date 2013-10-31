package uk.co.o2.stockservice.resources.admin

import uk.co.o2.services.serialization.InputSchema

import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

import static javax.ws.rs.core.MediaType.APPLICATION_JSON
import static javax.ws.rs.core.Response.Status.CREATED
import static javax.ws.rs.core.Response.Status.OK
import static javax.ws.rs.core.Response.status

@Path("/admin/stocks/{sku}")
@Produces(APPLICATION_JSON)
class AdminStockResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response allocateStock(@PathParam("sku") String sku, @InputSchema("admin_schemas/stock-allocation-schema.json") Map map) {
        status(CREATED).type(APPLICATION_JSON).entity("{'message': 'successfully allocated the stock'}").build()
    }

    @PUT
    public Response updateStock(@PathParam("sku") String sku, @InputSchema("admin_schemas/stock-update-schema.json") Map map) {
        status(OK).type(APPLICATION_JSON).entity("{'message': 'successfully updated the stock'}").build()
    }
}
