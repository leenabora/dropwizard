package uk.co.o2.stockservice.configuration

import com.yammer.dropwizard.Service
import com.yammer.dropwizard.config.Bootstrap
import com.yammer.dropwizard.config.Environment
import uk.co.o2.stockservice.resources.StockSummaryResource

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
        environment.addResource(new StockSummaryResource());
    }

}