package uk.co.o2.stockservice.configuration

import com.google.common.io.InputSupplier
import com.google.common.io.Resources
import org.springframework.core.io.FileSystemResource

import java.util.regex.Matcher
import java.util.regex.Pattern

class PropertyConfigurator {

    private static Pattern SYSTEM_PROPERTY_TOKEN = ~/\$\{(.+?)\}/

    private static Properties properties = new Properties()

    public String getValue(String configKey) {
        properties.getProperty(configKey)
    }

    public static Properties getProperties() {
        properties
    }

    public static Properties loadConfiguration(String externalPropertyFilePath) {
        Properties localProperties = expandSystemPropertiesReferencedInValues(loadProperties("config/local.properties"))
        Properties externalProperties = loadExternalProperties(externalPropertyFilePath)

        if (externalProperties) {
            localProperties.each {
                if (externalProperties.getProperty((String) it.key) == null)
                    throw new RuntimeException("External Properties did not contain the mandatory key " + it.key)
            }
            localProperties = externalProperties
        }
        properties = expandSystemPropertiesReferencedInValues(localProperties)
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

    private static Properties loadProperties(String classpathResourceName) {
        URL internalConfigurationLocation = Resources.getResource(classpathResourceName)

        InputSupplier<InputStream> inputSupplier = Resources
                .newInputStreamSupplier(internalConfigurationLocation);

        Properties props = new Properties()
        props.load(inputSupplier.getInput());
        props
    }


    private static Properties loadExternalProperties(String externalPropertyFilePath) {
        Properties externalProperties

        if (externalPropertyFilePath) {
            File externalConfiguration = new File(externalPropertyFilePath)
            if (externalConfiguration.exists()) {
                externalProperties = new Properties()
                externalProperties.load(new FileSystemResource(externalConfiguration).inputStream)
            }
        }
        externalProperties
    }

}
