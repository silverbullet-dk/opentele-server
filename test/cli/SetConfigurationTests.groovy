import grails.test.AbstractCliTestCase

class SetConfigurationTests extends AbstractCliTestCase {
    File propertiesFile
    File configGroovyFile

    protected void setUp() {
        super.setUp()

        propertiesFile = new File("grails-app/conf/datamon-test-app-context-config.properties")
        propertiesFile.delete()

        configGroovyFile = new File('test/cli/SetConfiguration/Config.groovy')
        configGroovyFile.delete()

        def templateText = new File('test/cli/SetConfiguration/TestTemplate_Config.groovy').text
        configGroovyFile.write(templateText)

        assertFalse("Properties file not properly deleted", propertiesFile.exists())
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testSetConfiguration() {

        execute(["set-configuration", "--configGroovyPath=test/cli/SetConfiguration/Config.groovy", "--language=da-DK", "--user=user_name", "--pw=passw", "--server=localhost:3307", "--db=opentele", "--context=test-app-context"])

        assertEquals 0, waitForProcess()
        verifyHeader()
        assertTrue("Properties file should exist", propertiesFile.exists())
        def content = new Properties()
        content.load(propertiesFile.newReader('UTF-8'))
        assertTrue("Database details missing in file", content["dataSource.url"].contains("localhost:3307/opentele"))
        assertTrue("Username missing in file", content["dataSource.username"] == "user_name")
        assertTrue("Password missing in file", content["dataSource.password"] == "passw")
        assertTrue("Locale config missing in file", content["languageTag"] == "da-DK")
        assertTrue("logging suffix missing in file", content["logging.suffix"] == "test-app-context")
        assertTrue("Config.groovy not updated", configGroovyFile.text.contains("grails.config.locations = [\"file:\${userHome}/.opentele/datamon-test-app-context-config.properties\"]"))
    }
}
