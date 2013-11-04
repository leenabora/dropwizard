package uk.co.o2.stockservice.configuration

import org.junit.Test


class DependencyInjectorTest {

    @Test
    public void "inject_should wire all dependencies"() {
        PropertyConfigurator.loadConfiguration(null)

        DependencyInjector dependencyInjector = new DependencyInjector()

        Map<String, Object> dependencies = dependencyInjector.inject()

        assert dependencies.size() == 13

        assert dependencies["badTouchPointContextExceptionMapper"]
        assert dependencies["itemNotFoundExceptionMapper"]
        assert dependencies["illegalStateExceptionMapper"]
        assert dependencies["validationExceptionMapper"]
        assert dependencies["unknownErrorExceptionMapper"]
        assert dependencies["jacksonJsonSchemaValidatingProvider"]

        assert dependencies["homeFilter"]
        assert dependencies["channelFilter"]

        assert dependencies["homeResource"]
        assert dependencies["stockResource"]
        assert dependencies["adminStockResource"]
        assert dependencies["adminStockMetadataResource"]

        assert dependencies["stockServiceHealthCheck"]
    }
}
