package uk.co.o2.stockservice.resources

import com.yammer.metrics.annotation.Timed
import uk.co.o2.stockservice.model.StockSummary

import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import java.util.concurrent.atomic.AtomicLong

@Path("/stockSummary")
@Produces(MediaType.APPLICATION_JSON)
class StockSummaryResource {
    private final String template;
    private final String defaultName;
    private final AtomicLong counter;

    public StockSummaryResource() {
        this.counter = new AtomicLong();
    }

    @GET
    @Timed
    public StockSummary getStockSummary(@QueryParam("sku") String sku, @QueryParam("channel") String channel) {
        if (sku == "sku-123") {
            return new StockSummary(counter.incrementAndGet(), sku, "CFU", "PreOrder", 500);
        }
    }
}