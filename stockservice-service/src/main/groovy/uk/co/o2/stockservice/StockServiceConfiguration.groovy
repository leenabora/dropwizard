package uk.co.o2.stockservice

import com.fasterxml.jackson.annotation.JsonProperty
import com.yammer.dropwizard.config.Configuration

class StockServiceConfiguration extends Configuration {

    private String externalConfigurationFileLocation

    @JsonProperty("externalConfigurationFileLocation")
    String getExternalConfigurationFileLocation() {
        return externalConfigurationFileLocation
    }

    @JsonProperty("externalConfigurationFileLocation")
    void setExternalConfigurationFileLocation(String externalConfigurationFileLocation) {
        this.externalConfigurationFileLocation = externalConfigurationFileLocation
    }
}