package org.opentele.server.provider

import org.codehaus.groovy.grails.commons.DefaultGrailsControllerClass
import org.junit.Test
import org.opentele.server.provider.OpenteleAuditLogLookup

import static org.junit.Assert.fail

class OpenteleAuditLogLookupTest extends GroovyTestCase {
    def grailsApplication

    @Test
    void testAuditLookup() {
        def ignoredControllers = ["greenmail"]
        OpenteleAuditLogLookup lookupHelper = new OpenteleAuditLogLookup()
        def errors = grailsApplication.controllerClasses.inject([]) { List errors, DefaultGrailsControllerClass controller ->
            def controllerName = controller.name[0].toLowerCase() + controller.name.substring(1)

            if (!(controllerName in ignoredControllers)) {

                if (lookupHelper.lookup.containsKey(controllerName)) {
                    def actions = controller.URIs.collect { controller.getMethodActionName(it) } as SortedSet
                    actions.each { action ->
                        if (!(action in lookupHelper.lookup[controllerName].keySet())) {
                            println """"${controllerName}.${action}": "TODO::${action}","""

                            errors << """"${controllerName}.${action}" not mapped in OpenteleAuditLogLookup"""
                        }
                    }
                } else {
                    errors << "Controller: ${controllerName} not mapped in OpenteleAuditLogLookup"
                }
            }
            return errors
        }
        if (errors) {
            fail("Some actions are not mapped in OpenteleAuditLogLookup: ${errors.join(', ')}. You have to add them to org.opentele.server.provider.OpenteleAuditLogLookup")
        }
    }
}
