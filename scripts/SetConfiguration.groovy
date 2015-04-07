includeTargets << grailsScript("_GrailsArgParsing")

/*
Run with;
grails set-configuration "--language=value" "--user=value" "--pw=value" "--db=value" ... more params
 */

target(main: "Replace configuration settings") {
    depends(parseArguments)

    def input = argsMap.findAll { it.key != 'params' }

    println("Generating war specific properties file...")
    println("Raw args: $args")
    println("Properties provided: $input")

    def properties = buildProperties(input)
    def propertiesFileName = savePropertiesFile(properties, argsMap.context)
    pointAppToPropertiesFile(argsMap.configGroovyPath, propertiesFileName)
}

def buildProperties(args) {
    def props = new Properties()

    props.setProperty("dataSource.pooled", "true")
    props.setProperty("dataSource.dialect", "org.opentele.server.core.util.MySQLInnoDBDialect")
    props.setProperty("dataSource.driverClassName", "com.mysql.jdbc.Driver")
    props.setProperty("languageTag", "${args.language}")
    props.setProperty("dataSource.username", "${args.user}")
    props.setProperty("dataSource.password", "${args.pw}")
    props.setProperty("dataSource.url", "jdbc:mysql://${args.server}/${args.db}")

    props.setProperty("logging.suffix", "${args.context}")

    return props
}

def savePropertiesFile(properties, contextRoot) {
    def file = new File('grails-app/conf', "datamon-${contextRoot}-config.properties")
    def writer = file.newWriter('UTF-8', false)
    try {
        properties.store(writer, 'Properties specific to a single war file.')
    }
    finally {
        writer.close()
    }
    println("Properties file '${file.absolutePath}' saved")

    return file.name
}

def pointAppToPropertiesFile(String pathToConfigGroovy, String propertiesFileName) {
    def config = new File(pathToConfigGroovy.toString())
    println("Setting path to properties file in: ${pathToConfigGroovy}")
    def text = ""
    config.eachLine { line ->
        if (line =~ /grails.config.locations/) {
            line = "grails.config.locations = [\"file:\${userHome}/.opentele/${propertiesFileName}\"]"
        }
        text += line + System.getProperty("line.separator")
    }
    config.write(text, 'UTF-8')
}

setDefaultTarget(main)
