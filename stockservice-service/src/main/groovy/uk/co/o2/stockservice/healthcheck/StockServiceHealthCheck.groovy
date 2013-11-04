package uk.co.o2.stockservice.healthcheck

import com.yammer.metrics.core.HealthCheck
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import uk.co.o2.services.monitoring.ExternalDependencyStatusValue
import uk.co.o2.services.monitoring.MongoMonitor
import uk.co.o2.services.monitoring.MongoStatus

class StockServiceHealthCheck extends HealthCheck {
    private static final Logger LOG = LoggerFactory.getLogger(StockServiceHealthCheck)

    private MongoMonitor mongoMonitor
    private Properties configuration

    StockServiceHealthCheck(MongoMonitor mongoMonitor, Properties configuration) {
        super("stock-service");

        this.mongoMonitor = mongoMonitor
        this.configuration = configuration
    }


    @Override
    protected HealthCheck.Result check() throws Exception {
        MongoStatus mongoStatus = mongoMonitor.monitor()
        Map result = [
                hostName: InetAddress.localHost.hostName,
                externalDependencyStatuses: mongoStatus.toRestfulRepresentation(),
                configurationProperties: configuration.findAll { !it.key.toString().toLowerCase().contains("password") }
        ]


        if (mongoStatus.status != ExternalDependencyStatusValue.Alive) {
            LOG.error(mongoStatus.errorCode.message())
            result["database"] = mongoStatus.errorCode.message()
            return HealthCheck.Result.unhealthy(result.toMapString())
        }

        return HealthCheck.Result.healthy(result.toMapString());
    }

}