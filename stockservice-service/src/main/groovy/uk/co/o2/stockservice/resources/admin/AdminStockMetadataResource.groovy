package uk.co.o2.stockservice.resources.admin

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import uk.co.o2.services.serialization.InputSchema
import uk.co.o2.stockservice.model.StockAllocation
import uk.co.o2.stockservice.model.StockMetadata
import uk.co.o2.stockservice.service.StockService

import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.Response

import static javax.ws.rs.core.MediaType.APPLICATION_JSON
import static javax.ws.rs.core.Response.Status.CREATED
import static javax.ws.rs.core.Response.status

@Path("/admin/stockMetadata/")
@Produces(APPLICATION_JSON)
class AdminStockMetadataResource {

    private StockService stockService
    private ObjectMapper mapper

    AdminStockMetadataResource(StockService stockService, ObjectMapper mapper) {
        this.stockService = stockService
        this.mapper = mapper
    }

    @POST
    @Consumes(APPLICATION_JSON)
    public Response saveMetadata(@InputSchema("admin_schemas/stock-metadata-schema.json") Map map) {
//        JsonNode jsonNode = mapper.readTree(map)
//        StockMetadata stockMetadata = (StockAllocation) mapper.readValue(map, StockMetadata.class)

        status(CREATED).type(APPLICATION_JSON).entity("{'message': 'successfully saved delivery options and stock mesages'}").build()
    }

}
