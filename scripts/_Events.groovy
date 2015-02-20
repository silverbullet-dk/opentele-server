eventWebXmlStart = { webXmlFile ->

    // Handle custom session timeout
    def tmpWebXmlFile = new File(projectWorkDir, webXmlFile)

    ant.replace(file: tmpWebXmlFile, token: "@grails.session.timeout.default@",
            value: "${grailsApp.config.grails.session.timeout.default}")
}
