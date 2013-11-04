package uk.co.o2.stockservice

import com.yammer.dropwizard.Service
import com.yammer.dropwizard.config.Bootstrap
import com.yammer.dropwizard.config.Environment
import uk.co.o2.services.logger.LogbackFactory
import uk.co.o2.stockservice.configuration.DependencyInjector
import uk.co.o2.stockservice.configuration.PropertyConfigurator
import uk.co.o2.stockservice.healthcheck.StockServiceHealthCheck

class StockServiceBootStrap extends Service<StockServiceConfiguration> {
    private DependencyInjector dependencyInjector

    public static void main(String[] args) throws Exception {
        new StockServiceBootStrap().run(args);
    }

    StockServiceBootStrap() {
        dependencyInjector = new DependencyInjector()

    }

    StockServiceBootStrap(DependencyInjector dependencyInjector) {
        this.dependencyInjector = dependencyInjector
    }

    @Override
    public void initialize(Bootstrap<StockServiceConfiguration> bootstrap) {
        bootstrap.setName("stock-service");
    }

    @Override
    public void run(StockServiceConfiguration configuration,
                    Environment environment) {

        loadConfigurations(configuration)

        register(environment)
    }

    private void loadConfigurations(StockServiceConfiguration configuration) {
        PropertyConfigurator.loadConfiguration(configuration.externalConfigurationFileLocation)

        LogbackFactory.configureLogging(PropertyConfigurator.getProperties(), "/logback_stockservice.xml")
    }

    private void register(Environment environment) {
        Map<String, Object> dependencies = dependencyInjector.inject()

        registerProvider(environment, dependencies)

        registerFilters(environment, dependencies)

        registerResources(environment, dependencies)

        registerHealthChecks(environment, dependencies)

    }

    private void registerProvider(Environment environment, Map<String, Object> dependencies) {
        environment.addProvider(dependencies["badTouchPointContextExceptionMapper"])
        environment.addProvider(dependencies["jacksonJsonSchemaValidatingProvider"])
        environment.addProvider(dependencies["itemNotFoundExceptionMapper"])
        environment.addProvider(dependencies["illegalStateExceptionMapper"])
        environment.addProvider(dependencies["validationExceptionMapper"])
        environment.addProvider(dependencies["unknownErrorExceptionMapper"])
    }

    private void registerFilters(Environment environment, Map<String, Object> dependencies) {
        environment.addFilter(dependencies["homeFilter"], "/*")
        environment.getJerseyResourceConfig().getContainerRequestFilters().add(dependencies["channelFilter"]);

    }

    private void registerResources(Environment environment, Map<String, Object> dependencies) {
        environment.addResource(dependencies["homeResource"])
        environment.addResource(dependencies["stockResource"])
        environment.addResource(dependencies["adminStockResource"])
        environment.addResource(dependencies["adminStockMetadataResource"])
    }

    private void registerHealthChecks(Environment environment, Map<String, Object> dependencies) {
        environment.addHealthCheck(dependencies["stockServiceHealthCheck"])
    }

}