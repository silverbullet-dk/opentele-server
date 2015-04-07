eventWebXmlStart = { webXmlFile ->

    // Handle custom session timeout
    def tmpWebXmlFile = new File(projectWorkDir, webXmlFile)

    ant.replace(file: tmpWebXmlFile, token: "@grails.session.timeout.default@",
            value: "${grailsApp.config.grails.session.timeout.default}")
}

eventCompileEnd = {
    ant.copy(file: "${basedir}/etc/testdb/devDb.h2.db",
            tofile: "${basedir}/clinicianDb.h2.db",
            overwrite: "true",
            preservelastmodified: "true")
}
