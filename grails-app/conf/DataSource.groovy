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
    //cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // hibernate 4
    cache.region.factory_class = 'net.sf.ehcache.hibernate.SingletonEhCacheRegionFactory' // hibernate 3
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
            url = "jdbc:h2:devDb;MVCC=TRUE;IGNORECASE=TRUE"
            // url = "jdbc:h2:mem:devDb;MVCC=TRUE;IGNORECASE=TRUE"
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
