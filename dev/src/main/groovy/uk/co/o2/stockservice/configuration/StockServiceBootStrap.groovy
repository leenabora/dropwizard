package uk.co.o2.stockservice.configuration

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.yammer.dropwizard.Service
import com.yammer.dropwizard.config.Bootstrap
import com.yammer.dropwizard.config.Environment
import uk.co.o2.json.schema.SchemaPassThroughCache
import uk.co.o2.services.serialization.JacksonJsonSchemaValidatingProvider
import uk.co.o2.stockservice.resources.StockResource
import uk.co.o2.stockservice.resources.admin.AdminDeliveryOptionsResource
import uk.co.o2.stockservice.resources.admin.AdminStockResource

class StockServiceBootStrap extends Service<StockServiceConfiguration> {
    public static void main(String[] args) throws Exception {
        new StockServiceBootStrap().run(args);
    }

    @Override
    public void initialize(Bootstrap<StockServiceConfiguration> bootstrap) {
        bootstrap.setName("stock-service");
    }

    @Override
    public void run(StockServiceConfiguration configuration,
                    Environment environment) {
        registerProvider(environment)

        registerResources(environment)
    }

    private void registerProvider(Environment environment) {
        ObjectMapper mapper = new ObjectMapper()
        JsonFactory jsonFactory = new JsonFactory(mapper)
        def passThroughCache = new SchemaPassThroughCache(jsonFactory)

        JacksonJsonSchemaValidatingProvider validationProvider = new JacksonJsonSchemaValidatingProvider(passThroughCache, mapper)

        environment.addProvider(validationProvider)
    }

    private void registerResources(Environment environment) {
        environment.addResource(new StockResource());
        environment.addResource(new AdminStockResource());
        environment.addResource(new AdminDeliveryOptionsResource());
    }

}