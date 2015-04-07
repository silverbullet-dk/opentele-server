package org.opentele.server.provider.model

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.opentele.server.model.MeasurementType
import org.opentele.server.model.NumericThreshold
import org.opentele.server.core.model.types.MeasurementTypeName
import org.opentele.server.provider.model.NumericThresholdController
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

@TestMixin(IntegrationTestMixin)
class NumericThresholdControllerIntegrationTests {
	def daoAuthenticationProvider
	def controller
    def grailsApplication

    NumericThreshold testThreshold

    private authenticate = {String login ->
        def authtoken, auth
        auth = new UsernamePasswordAuthenticationToken(login, login)
        authtoken = daoAuthenticationProvider.authenticate(auth)
        SecurityContextHolder.getContext().setAuthentication(authtoken)
    }

    @Before //each test
	void setUp() {
        // Avoid conflicts with objects in session created earlier. E.g. in bootstrap
        grailsApplication.mainContext.sessionFactory.currentSession.clear()
        
		controller = new NumericThresholdController()
        testThreshold = NumericThreshold.findAll().get(0)
	}

	@After
	void tearDown() {

	}

    @Test
    void testEverythingIsRight() {
        NumericThreshold threshold = new NumericThreshold(type: MeasurementType.findByName(MeasurementTypeName.WEIGHT), alertHigh: 100F, alertLow: 10F, warningHigh: 90F, warningLow: 20F)
        assert threshold.validate()
    }

    @Test
    void testAlertHighTooLowShouldFail() {
        NumericThreshold threshold = new NumericThreshold(type: MeasurementType.findByName(MeasurementTypeName.WEIGHT), alertHigh: 0F, alertLow: 10F, warningHigh: 90F, warningLow: 20F)
        assert !threshold.validate()
    }

    @Test
    void testWarningHighTooHighShouldFail() {
        NumericThreshold threshold = new NumericThreshold(type: MeasurementType.findByName(MeasurementTypeName.WEIGHT), alertHigh: 100F, alertLow: 10F, warningHigh: 190F, warningLow: 20F)
        assert !threshold.validate()
    }
    @Test
    void testWarningHighTooLowShouldFail() {
        NumericThreshold threshold = new NumericThreshold(type: MeasurementType.findByName(MeasurementTypeName.WEIGHT), alertHigh: 100F, alertLow: 10F, warningHigh: 0F, warningLow: 20F)
        assert !threshold.validate()
    }

    @Test
    void testWarningLowTooHighShouldFail() {
        NumericThreshold threshold = new NumericThreshold(type: MeasurementType.findByName(MeasurementTypeName.WEIGHT), alertHigh: 100F, alertLow: 10F, warningHigh: 90F, warningLow: 100F)
        assert !threshold.validate()
    }
    @Test
    void testWarningLowTooLowShouldFail() {
        NumericThreshold threshold = new NumericThreshold(type: MeasurementType.findByName(MeasurementTypeName.WEIGHT), alertHigh: 100F, alertLow: 10F, warningHigh: 0F, warningLow: 0F)
        assert !threshold.validate()
    }

    @Test
    void testAlertLowTooHighShouldFail() {
        NumericThreshold threshold = new NumericThreshold(type: MeasurementType.findByName(MeasurementTypeName.WEIGHT), alertHigh: 0F, alertLow: 110F, warningHigh: 90F, warningLow: 20F)
        assert !threshold.validate()
    }

    @Test
    void testWithNull() {
        NumericThreshold threshold = new NumericThreshold(type: MeasurementType.findByName(MeasurementTypeName.WEIGHT), alertHigh: null, alertLow: null, warningHigh: 90F, warningLow: 20F)
        assert threshold.validate()

        threshold = new NumericThreshold(type: MeasurementType.findByName(MeasurementTypeName.WEIGHT), alertHigh: null, alertLow: 10F, warningHigh: null, warningLow: 20F)
        assert threshold.validate()

        threshold = new NumericThreshold(type: MeasurementType.findByName(MeasurementTypeName.WEIGHT), alertHigh: 100F, alertLow: null, warningHigh: 90F, warningLow: null)
        assert threshold.validate()

    }

    @Test
    void updateSucceedsWithEverythingRight() {
        assert testThreshold != null

        controller.response.reset()
        controller.request.method = 'POST'
        controller.session.setAttribute('lastReferer', 'http://localhost:8080/opentele-server/standardThresholdSet/list')

        //Pre-check

        //Execute
        controller.params.alertHigh = "100"
        controller.params.warningHigh = "90"
        controller.params.warningLow = "40"
        controller.params.alertLow = "20"
        controller.params.'threshold.id' = testThreshold.id
        controller.update()

        //Check
        testThreshold.refresh()
        assert testThreshold.alertHigh == 100F
        assert testThreshold.warningHigh == 90F
        assert testThreshold.warningLow == 40F
        assert testThreshold.alertLow == 20F
    }

    @Test
    void updateSucceedsWithAlertHighNullValues() {
        assert testThreshold != null

        controller.response.reset()
        controller.request.method = 'POST'
        controller.session.setAttribute('lastReferer', 'http://localhost:8080/opentele-server/standardThresholdSet/list')

        //Pre-check

        //Execute
        //No alertHigh param
        controller.params.alertgHigh = ""
        controller.params.warningHigh = "90"
        controller.params.warningLow = "40"
        controller.params.alertLow = "20"
        controller.params.'threshold.id' = testThreshold.id
        controller.update()

        //Check
        testThreshold.refresh()
        assert !testThreshold.hasErrors()
        assert testThreshold.alertHigh == null
        assert testThreshold.warningHigh == 90F
        assert testThreshold.warningLow == 40F
        assert testThreshold.alertLow == 20F
    }

    @Test
    void updateSucceedsWithWarningHighNullValues() {
        assert testThreshold != null

        controller.response.reset()
        controller.request.method = 'POST'
        controller.session.setAttribute('lastReferer', 'http://localhost:8080/opentele-server/standardThresholdSet/list')

        //Pre-check

        //Execute
        //No warningHigh param
        controller.params.alertHigh = "100"
        controller.params.warningHigh = ""
        controller.params.warningLow = "40"
        controller.params.alertLow = "20"
        controller.params.'threshold.id' = testThreshold.id
        controller.update()

        //Check
        testThreshold.refresh()
        assert !testThreshold.hasErrors()
        assert testThreshold.alertHigh == 100F
        assert testThreshold.warningHigh == null
        assert testThreshold.warningLow == 40F
        assert testThreshold.alertLow == 20F
    }

    @Test
    void updateSucceedsWithWarningLowNullValues() {
        assert testThreshold != null

        controller.response.reset()
        controller.request.method = 'POST'
        controller.session.setAttribute('lastReferer', 'http://localhost:8080/opentele-server/standardThresholdSet/list')

        //Pre-check

        //Execute
        //No warningLow param
        controller.params.alertHigh = "100"
        controller.params.warningHigh = "90"
        controller.params.warningLow = ""
        controller.params.alertLow = "20"
        controller.params.'threshold.id' = testThreshold.id
        controller.update()

        //Check
        testThreshold.refresh()
        assert !testThreshold.hasErrors()
        assert testThreshold.alertHigh == 100F
        assert testThreshold.warningHigh == 90F
        assert testThreshold.warningLow == null
        assert testThreshold.alertLow == 20F
    }

    @Test
    void updateSucceedsWithAlertLowNullValues() {
        assert testThreshold != null

        controller.response.reset()
        controller.request.method = 'POST'
        controller.session.setAttribute('lastReferer', 'http://localhost:8080/opentele-server/standardThresholdSet/list')

        //Pre-check

        //Execute
        //No alertLow param
        controller.params.alertHigh = "100"
        controller.params.warningHigh = "90"
        controller.params.warningLow = "40"
        controller.params.alertLow = ""
        controller.params.'threshold.id' = testThreshold.id
        controller.update()

        //Check
        testThreshold.refresh()
        assert !testThreshold.hasErrors()
        assert testThreshold.alertHigh == 100F
        assert testThreshold.warningHigh == 90F
        assert testThreshold.warningLow == 40F
        assert testThreshold.alertLow == null
    }

}
