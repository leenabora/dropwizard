package uk.co.o2.stockservice

import com.sun.jersey.api.core.ResourceConfig
import com.yammer.dropwizard.config.Environment
import org.gmock.GMockController
import org.junit.Before
import org.junit.Test
import uk.co.o2.services.mapper.BadTouchPointContextExceptionMapper
import uk.co.o2.services.mapper.IllegalStateExceptionMapper
import uk.co.o2.services.mapper.ItemNotFoundExceptionMapper
import uk.co.o2.services.mapper.ValidationExceptionMapper
import uk.co.o2.services.serialization.JacksonJsonSchemaValidatingProvider
import uk.co.o2.stockservice.configuration.DependencyInjector
import uk.co.o2.stockservice.exceptionmapper.UnknownErrorExceptionMapper
import uk.co.o2.stockservice.filter.ChannelFilter
import uk.co.o2.stockservice.filter.HomeFilter
import uk.co.o2.stockservice.healthcheck.StockServiceHealthCheck
import uk.co.o2.stockservice.resources.HomeResource
import uk.co.o2.stockservice.resources.StockResource
import uk.co.o2.stockservice.resources.admin.AdminStockMetadataResource
import uk.co.o2.stockservice.resources.admin.AdminStockResource

class StockServiceBootStrapTest {
    GMockController mock = new GMockController()
    private DependencyInjector dependencyInjector
    private StockServiceBootStrap stockServiceBootStrap
    private Environment environment

    @Before
    public void beforeEachTest() {
        dependencyInjector = (DependencyInjector) mock.mock(DependencyInjector)

        environment = (Environment) mock.mock(Environment)

        stockServiceBootStrap = new StockServiceBootStrap(dependencyInjector)
    }


    @Test
    public void "run_should initialize stock service"() {
        StockServiceConfiguration configuration = new StockServiceConfiguration()
        configuration.setExternalConfigurationFileLocation("externalPropFile")

        Map dependencies = mockDependencies()

        dependencyInjector.inject().returns(dependencies)

        mock.play {
            stockServiceBootStrap.run(configuration, environment)
        }
    }

    private Map mockDependencies() {

        BadTouchPointContextExceptionMapper badTouchPointContextExceptionMapper = (BadTouchPointContextExceptionMapper) mock.mock(BadTouchPointContextExceptionMapper)
        ItemNotFoundExceptionMapper itemNotFoundExceptionMapper = (ItemNotFoundExceptionMapper) mock.mock(ItemNotFoundExceptionMapper)
        IllegalStateExceptionMapper illegalStateExceptionMapper = (IllegalStateExceptionMapper) mock.mock(IllegalStateExceptionMapper)
        ValidationExceptionMapper validationExceptionMapper = (ValidationExceptionMapper) mock.mock(ValidationExceptionMapper)
        UnknownErrorExceptionMapper unknownErrorExceptionMapper = (UnknownErrorExceptionMapper) mock.mock(UnknownErrorExceptionMapper)
        JacksonJsonSchemaValidatingProvider jacksonJsonSchemaValidatingProvider = (JacksonJsonSchemaValidatingProvider) mock.mock(JacksonJsonSchemaValidatingProvider)

        HomeFilter homeFilter = (HomeFilter) mock.mock(HomeFilter)
        ChannelFilter channelFilter = (ChannelFilter) mock.mock(ChannelFilter)

        HomeResource homeResource = (HomeResource) mock.mock(HomeResource)
        StockResource stockResource = (StockResource) mock.mock(StockResource)
        AdminStockResource adminStockResource = (AdminStockResource) mock.mock(AdminStockResource)
        AdminStockMetadataResource adminStockMetadataResource = (AdminStockMetadataResource) mock.mock(AdminStockMetadataResource)

        StockServiceHealthCheck stockServiceHealthCheck = (StockServiceHealthCheck) mock.mock(StockServiceHealthCheck)


        environment.addFilter(homeFilter, "/*")

        ResourceConfig resourceConfig = (ResourceConfig) mock.mock(ResourceConfig)
        ArrayList filters = (ArrayList) mock.mock(ArrayList)
        filters.add(channelFilter)
        resourceConfig.getContainerRequestFilters().returns(filters)
        environment.getJerseyResourceConfig().returns(resourceConfig)

        environment.addResource(homeResource)
        environment.addResource(stockResource)
        environment.addResource(adminStockResource)
        environment.addResource(adminStockMetadataResource)

        environment.addProvider(badTouchPointContextExceptionMapper)
        environment.addProvider(itemNotFoundExceptionMapper)
        environment.addProvider(illegalStateExceptionMapper)
        environment.addProvider(validationExceptionMapper)
        environment.addProvider(unknownErrorExceptionMapper)
        environment.addProvider(jacksonJsonSchemaValidatingProvider)

        environment.addHealthCheck(stockServiceHealthCheck)

        return [
                "badTouchPointContextExceptionMapper": badTouchPointContextExceptionMapper,
                "itemNotFoundExceptionMapper": itemNotFoundExceptionMapper,
                "illegalStateExceptionMapper": illegalStateExceptionMapper,
                "validationExceptionMapper": validationExceptionMapper,
                "unknownErrorExceptionMapper": unknownErrorExceptionMapper,
                "jacksonJsonSchemaValidatingProvider": jacksonJsonSchemaValidatingProvider,

                "homeFilter": homeFilter,
                "channelFilter": channelFilter,

                "homeResource": homeResource,
                "stockResource": stockResource,
                "adminStockResource": adminStockResource,
                "adminStockMetadataResource": adminStockMetadataResource,

                "stockServiceHealthCheck": stockServiceHealthCheck
        ]
    }


}
