package uk.co.o2.stockservice

import com.fasterxml.jackson.core.JsonFactory
import com.fasterxml.jackson.databind.ObjectMapper
import com.yammer.dropwizard.Service
import com.yammer.dropwizard.config.Bootstrap
import com.yammer.dropwizard.config.Environment
import uk.co.o2.json.schema.SchemaPassThroughCache
import uk.co.o2.services.logger.LogbackFactory
import uk.co.o2.services.mapper.BadTouchPointContextExceptionMapper
import uk.co.o2.services.mapper.IllegalStateExceptionMapper
import uk.co.o2.services.mapper.ItemNotFoundExceptionMapper
import uk.co.o2.services.mapper.ValidationExceptionMapper
import uk.co.o2.services.serialization.JacksonJsonSchemaValidatingProvider
import uk.co.o2.stockservice.configuration.DependencyInjector
import uk.co.o2.stockservice.configuration.PropertyConfigurator
import uk.co.o2.stockservice.exceptionmapper.UnknownErrorExceptionMapper
import uk.co.o2.stockservice.filter.ChannelFilter
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

        ObjectMapper mapper = new ObjectMapper()

        registerProvider(environment, mapper)

        registerResourcesAndFilter(environment, mapper)

    }

    private void loadConfigurations(StockServiceConfiguration configuration) {
        PropertyConfigurator.loadConfiguration(configuration.externalConfigurationFileLocation)

        LogbackFactory.configureLogging(PropertyConfigurator.getProperties(), "/logback_stockservice.xml")
    }

    private void registerProvider(Environment environment, ObjectMapper mapper) {
        environment.addProvider(schemaValidationProvider(mapper))

        environment.addProvider(new BadTouchPointContextExceptionMapper())
        environment.addProvider(new ItemNotFoundExceptionMapper())
        environment.addProvider(new IllegalStateExceptionMapper())
        environment.addProvider(new ValidationExceptionMapper())
        environment.addProvider(new UnknownErrorExceptionMapper())
    }

    private JacksonJsonSchemaValidatingProvider schemaValidationProvider(ObjectMapper mapper) {
        JsonFactory jsonFactory = new JsonFactory(mapper)
        def passThroughCache = new SchemaPassThroughCache(jsonFactory)

        JacksonJsonSchemaValidatingProvider validationProvider = new JacksonJsonSchemaValidatingProvider(passThroughCache, mapper)
        validationProvider
    }

    private void registerResourcesAndFilter(Environment environment, ObjectMapper mapper) {
        DependencyInjector dependencyInjector = new DependencyInjector()
        StockService stockService = dependencyInjector.inject()

        environment.getJerseyResourceConfig().getContainerRequestFilters().add(new ChannelFilter());

        environment.addResource(new StockResource(stockService))
        environment.addResource(new AdminStockResource(stockService, mapper))
        environment.addResource(new AdminStockMetadataResource(stockService, mapper))
    }

}