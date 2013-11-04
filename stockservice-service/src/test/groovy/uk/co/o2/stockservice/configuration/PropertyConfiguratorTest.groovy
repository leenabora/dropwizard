package uk.co.o2.stockservice.configuration

import org.junit.After
import org.junit.Assert
import org.junit.Test

class PropertyConfiguratorTest {
    private final List<File> tempFiles = []

    @After
    void ensureTempFilesAreDeleted() {
        tempFiles.each {
            it.delete()
        }
    }

    @Test
    void "loadConfiguration_should load the local properties when external configuration is not present"() {
        PropertyConfigurator.loadConfiguration(null)

        PropertyConfigurator configurator = new PropertyConfigurator()

        assert configurator.getValue("log.directory") == "${System.getProperty("user.home")}/logs"
        assert configurator.getValue("testProp") == "testVal"
    }

    @Test
    void "loadConfiguration_should load the external configuration"() {
        File externalConfigurationLocation = createTempFile()

        Properties externalConfiguration = loadLocalProperties()
        externalConfiguration.each {
            it.value = "12345"
        }
        saveExternalConfiguration(externalConfigurationLocation, externalConfiguration)

        PropertyConfigurator.loadConfiguration(externalConfigurationLocation.getAbsolutePath())

        assert PropertyConfigurator.getProperties() == externalConfiguration
    }

    @Test
    void "loadConfiguration_should expand references to systemProperties given a properties file with a value that references a systemProperty"() {
        File externalConfigurationLocation = createTempFile()

        Properties externalConfiguration = loadLocalProperties()

        String firstKey = externalConfiguration.keys()[0]
        externalConfiguration[firstKey] = '123${foo}456${foo}789'
        saveExternalConfiguration(externalConfigurationLocation, externalConfiguration)

        System.setProperty("foo", 'b\\$a\\r')

        PropertyConfigurator.loadConfiguration(externalConfigurationLocation.getAbsolutePath())

        PropertyConfigurator configurator = new PropertyConfigurator()

        assert configurator.getValue(firstKey) == '123b\\$a\\r456b\\$a\\r789'
    }

    @Test
    void "loadConfiguration_should throw an error given an external configuration With different keys than the local configuration"() {
        File externalConfigurationLocation = createTempFile()

        Properties externalConfiguration = new Properties()
        externalConfiguration.foo = 'bar'
        saveExternalConfiguration(externalConfigurationLocation, externalConfiguration)

        try {
            PropertyConfigurator.loadConfiguration(externalConfigurationLocation.getAbsolutePath())
            Assert.fail("expected an exception")
        } catch (RuntimeException e) {
            assert e.message.contains("External Properties did not contain the mandatory key")
        }
    }

    @Test
    void "loadConfiguration_should throw an exception given a properties file that references a SystemProperty that does not exist"() {
        String propertyThatDoesNotExist = "shouldNotExist"
        assert System.getProperty(propertyThatDoesNotExist) == null

        Properties externalConfiguration = loadLocalProperties()

        String firstKey = externalConfiguration.keys()[0]
        externalConfiguration[firstKey] = '123${' + propertyThatDoesNotExist + '}456'

        File externalConfigurationLocation = createTempFile()
        saveExternalConfiguration(externalConfigurationLocation, externalConfiguration)

        try {
            PropertyConfigurator.loadConfiguration(externalConfigurationLocation.getAbsolutePath())
            Assert.fail('Expected an exception')
        } catch (RuntimeException e) {
            assert e.message == "Unknown system property 'shouldNotExist' found in configuration file"
        }
    }

    private def saveExternalConfiguration(File externalConfigurationLocation, Properties externalConfiguration) {
        BufferedOutputStream output = externalConfigurationLocation.newOutputStream()
        externalConfiguration.store(output, null)
        output.close()
    }

    private Properties loadLocalProperties() {
        Properties localProperties = new Properties()
        localProperties.load(Thread.currentThread().contextClassLoader.getResourceAsStream("config/local.properties"))
        return localProperties
    }

    private File createTempFile() {
        File result = File.createTempFile("unitTests", null)
        result.deleteOnExit()
        tempFiles << result
        result
    }
}
