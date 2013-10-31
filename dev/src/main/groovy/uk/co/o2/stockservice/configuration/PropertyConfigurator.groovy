package uk.co.o2.stockservice.configuration

import com.google.common.io.InputSupplier
import com.google.common.io.Resources
import org.springframework.core.io.FileSystemResource

import java.util.regex.Matcher
import java.util.regex.Pattern

class PropertyConfigurator {

    private static Pattern SYSTEM_PROPERTY_TOKEN = ~/\$\{(.+?)\}/

    private static Properties properties = new Properties()

    static {
        properties = loadConfiguration()
    }

    public String getValue(String configKey) {
        properties.getProperty(configKey)
    }


    private static Properties loadConfiguration() {
        Properties localProperties = new Properties()

        URL internalConfigurationLocation = Resources.getResource("config/local.properties")
        def externalPropertyFilePath = "${System.getProperty('catalina.base')}/appdata/stockService.properties"
        File externalConfiguration = new File(externalPropertyFilePath)

        InputSupplier<InputStream> inputSupplier = Resources
                .newInputStreamSupplier(internalConfigurationLocation);

        localProperties.load(inputSupplier.getInput());

        Properties externalProperties = new Properties()

        if (externalConfiguration.exists()) {
            externalProperties.load(new FileSystemResource(externalConfiguration).inputStream)
            localProperties.each {
                if (externalProperties.getProperty((String) it.key) == null)
                    throw new RuntimeException("External Properties did not contain the mandatory key " + it.key)
            }
            localProperties = externalProperties
        }
        expandSystemPropertiesReferencedInValues(localProperties)
    }

    private static Properties expandSystemPropertiesReferencedInValues(Properties properties) {
        (Properties) properties.each {
            Matcher matcher = SYSTEM_PROPERTY_TOKEN.matcher((String) it.value)
            StringBuffer expandedResult = new StringBuffer()
            while (matcher.find()) {
                String property = System.getProperty(matcher.group(1))
                if (property == null) {
                    throw new RuntimeException("Unknown system property '${matcher.group(1)}' found in configuration file")
                }
                matcher.appendReplacement(expandedResult, property.replace('\\', '\\\\').replace('$', '\\$'))
            }
            matcher.appendTail(expandedResult)
            it.value = expandedResult.toString()
        }
    }

}
