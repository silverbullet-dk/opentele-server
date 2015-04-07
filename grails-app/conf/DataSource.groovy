import groovy.sql.Sql
import org.opentele.server.core.model.BootStrapUtil

dataSource {
    pooled = true
//    driverClassName = "org.h2.Driver"
//    dialect = "org.opentele.server.core.util.H2Dialect"
//    username = "sa"
//    password = ""
    logSql = false
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = true
    cache.region.factory_class = 'org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory' // hibernate 4
    //cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // hibernate 4
    //cache.region.factory_class = 'net.sf.ehcache.hibernate.SingletonEhCacheRegionFactory' // hibernate 3
}
// environment specific settings
environments {
    
    development {
        dataSource {
            username = "sa"
            password = ""
            driverClassName = "org.h2.Driver"
            dialect = "org.opentele.server.core.util.H2Dialect"
            // dbCreate = "create-drop" // one of 'create', 'create-drop', 'update', 'validate', ''

            if(BootStrapUtil.isH2DatabaseServerRunning("jdbc:h2:tcp://localhost:8043/citizenDb", "sa", "")) {
                url = "jdbc:h2:tcp://localhost:8043/citizenDb"
            } else {
                url = "jdbc:h2:clinicianDb;MVCC=TRUE;IGNORECASE=TRUE"
            }
            println "Using database url: ${url}"
        }
    }
    test {
        dataSource {
//            dbCreate = "create-drop"
            username = "sa"
            password = ""
            driverClassName = "org.h2.Driver"
            dialect = "org.opentele.server.core.util.H2Dialect"
            url = "jdbc:h2:mem:testDb;MVCC=TRUE;IGNORECASE=TRUE"
        }
    }
    performance {
        dataSource {
            pooled = true
            dialect = "org.opentele.server.core.util.MySQLInnoDBDialect"
            driverClassName = "com.mysql.jdbc.Driver"
            username = "opentele"
            password = "opentele"
            dbCreate = 'create'
            url = "jdbc:mysql://localhost:3306/opentele"
        }
    }
}
