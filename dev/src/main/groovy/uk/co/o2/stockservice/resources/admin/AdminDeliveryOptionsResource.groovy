package uk.co.o2.stockservice.resources.admin

import uk.co.o2.services.serialization.InputSchema

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

import static javax.ws.rs.core.MediaType.APPLICATION_JSON
import static javax.ws.rs.core.Response.Status.CREATED
import static javax.ws.rs.core.Response.status

@Path("/admin/deliveryOptions/")
@Produces(APPLICATION_JSON)
class AdminDeliveryOptionsResource {

    @POST
    @Consumes(APPLICATION_JSON)
    public Response saveDeliveryOptions(@InputSchema("admin_schemas/deliveryoptions-messages-for-stockstatus-schema.json") Map map) {
        status(CREATED).type(APPLICATION_JSON).entity("{'message': 'successfully saved delivery options'}").build()
    }

}
