import grails.plugin.springsecurity.SecurityConfigType
import org.apache.log4j.DailyRollingFileAppender

grails.config.locations = [ "file:${userHome}/.opentele/datamon-demo-config.properties", "file:c:/kihdatamon/settings/datamon-config.properties", "file:${userHome}/.kih/datamon-config.properties"]

grails.databinding.convertEmptyStringsToNull = false

grails.session.timeout.default = 30

grails.project.groupId = appName
grails.mime.file.extensions = true
grails.mime.use.accept.header = true
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
	xml: ['text/xml', 'application/xml'],
	text: 'text/plain',
	js: 'text/javascript',
	rss: 'application/rss+xml',
	atom: 'application/atom+xml',
	css: 'text/css',
	csv: 'text/csv',
	all: '*/*',
	json: ['application/json','text/json'],
	form: 'application/x-www-form-urlencoded',
	multipartForm: 'multipart/form-data'
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// What URL patterns should be processed by the resources plugin
grails.resources.adhoc.patterns = ['/images/*', '/css/*', '/js/*', '/plugins/*']



// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"

// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []
// whether to disable processing of multi part requests
grails.web.disable.multipart=false

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// enable query caching by default
grails.hibernate.cache.queries = true

// Using database-migrations from core plugin
def pluginDir = org.codehaus.groovy.grails.plugins.GrailsPluginUtils.getPluginDirForName('opentele-server-core-plugin')?.path
grails.plugin.databasemigration.changelogLocation = "$pluginDir/grails-app/migrations"


// Som standard er email disabled og lige så greenmail. I de miljøer hvor det skal enables, skal der stå
// grails {
//   mail {
//     disabled = false
//   }
//}
//greenmail.disabled=false
// samt den øvrige mail konfiguration
grails {
    mail {
        disabled = true
        'default' {
            from="svar-ikke@opentele.dk"
        }
    }
}
greenmail.disabled = true
cpr.lookup.enabled = false

milou {
    run = false
    repeatIntervalMillis = 180000

    realtimeRun = false
    realtimeRepeatIntervalMillis = 60000
}

kihdb {
    run = false
    repeatIntervalMillis = 180000
}

video {
    enabled = false
    connection {
        timeoutMillis = 5 * 60 * 1000 // 5 minutes
        asyncTimeoutMillis = 6 * 60 * 1000 // 6 minutes
    }
}

help {
    image {
        contentTypes = ["image/jpeg","image/pjpeg","image/png","image/x-png"]
        uploadPath = "helpimg-upload" //In production this path should be configured to a full local path. NOT a path relatvie to web-app!
        providedImagesPath = "helpimg-upload"
        overwriteExistingFiles = true
    }
}

measurement.results.tables.css = 'measurement_results_tables.css'

// set per-environment serverURL stem for creating absolute links
environments {
	development {
		grails.logging.jul.usebridge = true

        // Database plugin settings: Run autoupdate in all devel contexts.. But nowhere else..
        grails.plugin.databasemigration.dropOnStart = true
        grails.plugin.databasemigration.updateOnStart = true

        // If developing using MSSQL server:
        //grails.plugin.databasemigration.updateOnStartDefaultSchema = 'opentele].[dbo'

        grails.plugin.databasemigration.updateOnStartFileNames = ['changelog.groovy']
        grails.plugin.databasemigration.autoMigrateScripts = ['RunApp', 'TestApp']


        // SOSI Settings
        seal.sts.url = "http://test1.ekstern-test.nspop.dk:8080/sts/services/SecurityTokenService"
        //cpr.service.url = "http://test1.ekstern-test.nspop.dk:8080/stamdata-cpr-ws/service/StamdataPersonLookup"
        cpr.service.url = "http://nsptest.silverbullet.dk/stamdata-cpr-ws/service/StamdataPersonLookup"
        cpr.lookup.enabled = true

        grails {
            mail {
                disabled = false
                host = "localhost"
                port = com.icegreen.greenmail.util.ServerSetupTest.SMTP.port
            }
        }

        greenmail.disabled = false
        // For at tillade Greenmail i UDV
        grails.plugin.springsecurity.controllerAnnotations.staticRules = [
                '/greenmail/**': ['IS_AUTHENTICATED_ANONYMOUSLY']
        ]

    }
    devPerformance {

        grails.plugin.databasemigration.dropOnStart = true
        grails.plugin.databasemigration.updateOnStart = true
        grails.plugin.databasemigration.updateOnStartFileNames = ['changelog.groovy']
        grails.plugin.databasemigration.autoMigrateScripts = ['RunApp', 'TestApp']

        running.ctg.messaging.enabled = true
    }
    performance {

        grails.plugin.databasemigration.dropOnStart = true
        grails.plugin.databasemigration.updateOnStart = true
        grails.plugin.databasemigration.updateOnStartFileNames = ['changelog.groovy']
        grails.plugin.databasemigration.autoMigrateScripts = ['RunApp', 'TestApp']

        running.ctg.messaging.enabled = true
    }
    test {
        // Database plugin settings: Run autoupdate in all devel contexts..
        // Assumes H2 database
        grails.plugin.databasemigration.dropOnStart = true
        grails.plugin.databasemigration.updateOnStart = true
        grails.plugin.databasemigration.updateOnStartFileNames = ['changelog.groovy']
        grails.plugin.databasemigration.autoMigrateScripts = ['RunApp', 'TestApp']
        // Must be set due to bug in migration plugin: https://jira.grails.org/browse/GPDATABASEMIGRATION-160
        grails.plugin.databasemigration.forceAutoMigrate = true

        grails {
            mail {
                disabled = false
                host = "localhost"
                port = com.icegreen.greenmail.util.ServerSetupTest.SMTP.port
            }
        }

        greenmail.disabled = false

    }
}

String logDirectory = "${System.getProperty('catalina.base') ?: '.'}/logs"

// Logging
String commonPattern = "%d [%t] %-5p %c{2} %x - %m%n"

log4j = {
    appenders {
        console name: "stdout",
                layout: pattern(conversionPattern: commonPattern)
        appender new DailyRollingFileAppender(
                name:"opentele", datePattern: "'.'yyyy-MM-dd",
                file:"${logDirectory}/opentele.log",
                layout: pattern(conversionPattern: commonPattern))
    }

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
            'org.codehaus.groovy.grails.web.pages', //  GSP
            'org.codehaus.groovy.grails.web.sitemesh', //  layouts
            'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
            'org.codehaus.groovy.grails.web.mapping', // URL mapping
            'org.codehaus.groovy.grails.commons', // core / classloading
            'org.codehaus.groovy.grails.plugins', // plugins
            'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration,
            'org.codehaus.groovy.grails.commons.cfg',
            'org.springframework',
            'org.hibernate',
            'org.apache',
            'net.sf.ehcache.hibernate',
            'grails.app.services.org.grails.plugin.resource',
            'grails.app.taglib.org.grails.plugin.resource',
            'grails.app.resourceMappers.org.grails.plugin.resource',
            'grails.app.service.grails.buildtestdata.BuildTestDataService',
            'grails.app.buildtestdata',
            'grails.app.services.grails.buildtestdata',
            'grails.buildtestdata.DomainInstanceBuilder'

    root {
        error 'opentele', 'stdout'
    }

    environments {
        development {
            debug 'grails.app',
                    'org.opentele',
                    'grails.app.jobs'
//            debug 'org.hibernate.SQL'
//           trace 'org.hibernate.type'
        }
        test {
            debug 'grails.app',
                    'org.opentele'
        }
        performance {
            debug 'grails.app',
                    'org.opentele',
                    'grails.app.jobs'
        }
    }
}

// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.userLookup.userDomainClassName = 'org.opentele.server.model.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'org.opentele.server.model.UserRole'
grails.plugin.springsecurity.authority.className = 'org.opentele.server.model.Role'
grails.plugin.springsecurity.useBasicAuth = true
grails.plugin.springsecurity.basic.realmName = "OpenTele Server"
grails.plugin.springsecurity.useSecurityEventListener = true
grails.plugin.springsecurity.securityConfigType = SecurityConfigType.Annotation
grails.plugin.springsecurity.password.algorithm = 'SHA-256'
grails.plugin.springsecurity.password.hash.iterations = 1

grails.plugin.springsecurity.controllerAnnotations.staticRules = [
        '/patient/createPatient': [org.opentele.server.core.model.types.PermissionName.PATIENT_CREATE],
        '/dbconsole/**': [org.opentele.server.core.model.types.PermissionName.WEB_LOGIN]
]

grails.plugin.springsecurity.filterChain.chainMap = [
    '/**': 'JOINED_FILTERS,-basicAuthenticationFilter,-basicExceptionTranslationFilter'
]

grails.plugin.springsecurity.providerNames = [
        'caseInsensitivePasswordAuthenticationProvider',
        'anonymousAuthenticationProvider',
        'rememberMeAuthenticationProvider'
]


passwordRetryGracePeriod=120
passwordMaxAttempts=3
reminderEveryMinutes=15
