package uk.co.o2.stockservice

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.yammer.dropwizard.Service
import com.yammer.dropwizard.config.Bootstrap
import com.yammer.dropwizard.config.Environment
import uk.co.o2.json.schema.SchemaPassThroughCache
import uk.co.o2.services.serialization.JacksonJsonSchemaValidatingProvider
import uk.co.o2.stockservice.configuration.DependencyInjector
import uk.co.o2.stockservice.configuration.PropertyConfigurator
import uk.co.o2.stockservice.resources.StockResource
import uk.co.o2.stockservice.resources.admin.AdminStockMetadataResource
import uk.co.o2.stockservice.resources.admin.AdminStockResource
import uk.co.o2.stockservice.service.StockService

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

        loadConfigurations(configuration)

        registerProvider(environment)

        registerResources(environment)

    }

    private void loadConfigurations(StockServiceConfiguration configuration) {
        PropertyConfigurator.loadConfiguration(configuration.externalConfigurationFileLocation)
    }

    private void registerProvider(Environment environment) {
        ObjectMapper mapper = new ObjectMapper()
        JsonFactory jsonFactory = new JsonFactory(mapper)
        def passThroughCache = new SchemaPassThroughCache(jsonFactory)

        JacksonJsonSchemaValidatingProvider validationProvider = new JacksonJsonSchemaValidatingProvider(passThroughCache, mapper)

        environment.addProvider(validationProvider)
    }

    private void registerResources(Environment environment) {
        DependencyInjector dependencyInjector = new DependencyInjector()
        StockService stockService = dependencyInjector.inject()

        environment.addResource(new StockResource(stockService))
        environment.addResource(new AdminStockResource(stockService))
        environment.addResource(new AdminStockMetadataResource(stockService))
    }

}