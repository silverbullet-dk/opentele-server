package org.opentele.server.model

import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.support.GrailsUnitTestMixin
import org.aspectj.lang.annotation.Before
import org.codehaus.groovy.grails.web.context.ServletContextHolder
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.junit.Test
import org.opentele.server.model.patientquestionnaire.PatientHelpInfo
import org.opentele.server.model.questionnaire.HelpInfo
import org.opentele.server.model.HelpImage
import org.opentele.server.util.HelpImageUtil
import org.spockframework.compiler.model.Spec
import org.springframework.mock.web.MockServletContext
import spock.lang.Specification

@TestFor(HelpImageController)
@Build([HelpInfo, HelpImage, PatientHelpInfo])
@TestMixin(GrailsUnitTestMixin)
class HelpImageControllerSpec extends Specification {

    def setup() {
        grailsApplication.config.help.image.providedImagesPath = "images/helpimages/"
        def servletContext = new MockServletContext()
        ServletContextHolder.setServletContext(servletContext)
        HelpImageUtil.servletContext = new MockServletContext()
    }

    def "test that we do not delete when there's nothing to delete"() {
        when:
        params.id = 0

        and:
        controller.delete()

        and:
        String err = flash.error

        then:
        err.contains('default.not.found.message')
        response.redirectedUrl == "/helpImage/list"
    }

    def "test that we do not delete HelpImage's that are referenced from HelpInfo's"() {
        setup:
        def helpImage = HelpImage.build()
        def helpInfo = HelpInfo.build(helpImage: helpImage)
        def patientHelpInfo = PatientHelpInfo.build(helpImage: helpImage)

        when:
        params.id = helpImage.id

        and:
        controller.delete()

        and:
        String err = flash.error

        then:
        err.contains('helpImage.delete.reference.error')
        response.redirectedUrl == "/helpImage/list"
    }

    void tearDown() {
        // Tear down logic here
    }

//    void testSomething() {
//        fail "Implement me"
//    }
}
