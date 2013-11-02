package uk.co.o2.stockservice.resources.admin

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import uk.co.o2.services.serialization.InputSchema
import uk.co.o2.stockservice.model.StockAllocation
import uk.co.o2.stockservice.service.StockService

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
    private static final Logger LOG = LoggerFactory.getLogger(AdminStockResource)

    private StockService stockService
    private ObjectMapper mapper

    AdminStockResource(StockService stockService, ObjectMapper mapper) {
        this.stockService = stockService
        this.mapper = mapper
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response allocateStock(@PathParam("sku") String sku, @InputSchema("admin_schemas/stock-allocation-schema.json") Map map) {
//        JsonNode jsonNode = mapper.readTree(map)
//        StockAllocation stockAllocation = (StockAllocation) mapper.readValue(map, StockAllocation.class)

        JsonNode stockAllocation = new StockAllocation(sku: sku, stockStatus: "PreOrder", stockLevel: 1000)
        stockService.allocateStock(stockAllocation)
        status(CREATED).type(APPLICATION_JSON).entity("{'message': 'successfully allocated the stock'}").build()
    }

    @PUT
    public Response updateStock(@PathParam("sku") String sku, @InputSchema("admin_schemas/stock-update-schema.json") Map map) {
        status(OK).type(APPLICATION_JSON).entity("{'message': 'successfully updated the stock'}").build()
    }
}
