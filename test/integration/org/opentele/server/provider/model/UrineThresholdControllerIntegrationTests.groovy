package org.opentele.server.provider.model

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.opentele.server.model.MeasurementType
import org.opentele.server.model.UrineThreshold
import org.opentele.server.core.model.types.MeasurementTypeName
import org.opentele.server.core.model.types.ProteinValue
import org.opentele.server.provider.model.UrineThresholdController

@TestMixin(IntegrationTestMixin)
class UrineThresholdControllerIntegrationTests {
	def controller
    def grailsApplication

    UrineThreshold testThreshold

    @Before
	void setUp() {
        // Avoid conflicts with objects in session created earlier. E.g. in bootstrap
        grailsApplication.mainContext.sessionFactory.currentSession.clear()
        
		controller = new UrineThresholdController()
        testThreshold = UrineThreshold.findAll().get(0)
	}

    @Test
    void testEverythingIsRight() {
        UrineThreshold threshold = new UrineThreshold(type: MeasurementType.findByName(MeasurementTypeName.URINE), alertHigh: ProteinValue.PLUS_FOUR, alertLow: ProteinValue.PLUSMINUS, warningHigh: ProteinValue.PLUS_THREE, warningLow: ProteinValue.PLUS_ONE)
        assert threshold.validate()
    }

    @Test
    void testAlertHighTooLowShouldFail() {
        UrineThreshold threshold = new UrineThreshold(type: MeasurementType.findByName(MeasurementTypeName.URINE), alertHigh: ProteinValue.NEGATIVE, alertLow: ProteinValue.PLUSMINUS, warningHigh: ProteinValue.PLUS_THREE, warningLow: ProteinValue.PLUS_ONE)
        assert !threshold.validate()
    }

    @Test
    void testWarningHighTooHighShouldFail() {
        UrineThreshold threshold = new UrineThreshold(type: MeasurementType.findByName(MeasurementTypeName.URINE), alertHigh: ProteinValue.PLUS_FOUR, alertLow: ProteinValue.PLUSMINUS, warningHigh: ProteinValue.PLUS_FOUR, warningLow: ProteinValue.PLUS_ONE)
        assert !threshold.validate()
    }
    @Test
    void testWarningHighTooLowShouldFail() {
        UrineThreshold threshold = new UrineThreshold(type: MeasurementType.findByName(MeasurementTypeName.URINE), alertHigh: ProteinValue.PLUS_FOUR, alertLow: ProteinValue.PLUSMINUS, warningHigh: ProteinValue.PLUS_ONE, warningLow: ProteinValue.PLUS_ONE)
        assert !threshold.validate()
    }

    @Test
    void testWarningLowTooHighShouldFail() {
        UrineThreshold threshold = new UrineThreshold(type: MeasurementType.findByName(MeasurementTypeName.URINE), alertHigh: ProteinValue.PLUS_FOUR, alertLow: ProteinValue.PLUSMINUS, warningHigh: ProteinValue.PLUS_THREE, warningLow: ProteinValue.PLUS_FOUR)
        assert !threshold.validate()
    }
    @Test
    void testWarningLowTooLowShouldFail() {
        UrineThreshold threshold = new UrineThreshold(type: MeasurementType.findByName(MeasurementTypeName.URINE), alertHigh: ProteinValue.PLUS_FOUR, alertLow: ProteinValue.PLUSMINUS, warningHigh: ProteinValue.PLUS_THREE, warningLow: ProteinValue.NEGATIVE)
        assert !threshold.validate()
    }

    @Test
    void testAlertLowTooHighShouldFail() {
        UrineThreshold threshold = new UrineThreshold(type: MeasurementType.findByName(MeasurementTypeName.URINE), alertHigh: ProteinValue.PLUS_FOUR, alertLow: ProteinValue.PLUS_FOUR, warningHigh: ProteinValue.PLUS_THREE, warningLow: ProteinValue.PLUS_ONE)
        assert !threshold.validate()
    }

    @Test
    void testWithNull() {
        UrineThreshold threshold = new UrineThreshold(type: MeasurementType.findByName(MeasurementTypeName.URINE), alertHigh: null, alertLow: ProteinValue.PLUSMINUS, warningHigh: null, warningLow: ProteinValue.PLUS_ONE)
        assert threshold.validate()

        threshold = new UrineThreshold(type: MeasurementType.findByName(MeasurementTypeName.URINE), alertHigh: ProteinValue.PLUS_FOUR, alertLow: null, warningHigh: ProteinValue.PLUS_THREE, warningLow: null)
        assert threshold.validate()
    }


    @Test
    void updateSucceedsWithEverythingRight() {
        assert testThreshold != null

        controller.response.reset()

        //Pre-check

        //Execute
        controller.params.alertHigh = "PLUS_FOUR"
        controller.params.warningHigh = "PLUS_THREE"
        controller.params.warningLow = "PLUSMINUS"
        controller.params.alertLow = "NEGATIVE"
        controller.params.'threshold.id' = testThreshold.id
        controller.update()

        //Check
        testThreshold.refresh()
        assert !testThreshold.hasErrors()
        assert testThreshold.alertHigh == ProteinValue.PLUS_FOUR
        assert testThreshold.warningHigh == ProteinValue.PLUS_THREE
        assert testThreshold.warningLow == ProteinValue.PLUSMINUS
        assert testThreshold.alertLow == ProteinValue.NEGATIVE
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
        controller.params.warningHigh = "PLUS_THREE"
        controller.params.warningLow = "PLUSMINUS"
        controller.params.alertLow = "NEGATIVE"
        controller.params.'threshold.id' = testThreshold.id
        controller.update()

        //Check
        testThreshold.refresh()
        assert !testThreshold.hasErrors()
        assert testThreshold.alertHigh == null
        assert testThreshold.warningHigh == ProteinValue.PLUS_THREE
        assert testThreshold.warningLow == ProteinValue.PLUSMINUS
        assert testThreshold.alertLow == ProteinValue.NEGATIVE    }

    @Test
    void updateSucceedsWithWarningHighNullValues() {
        assert testThreshold != null

        controller.response.reset()
        controller.request.method = 'POST'
        controller.session.setAttribute('lastReferer', 'http://localhost:8080/opentele-server/standardThresholdSet/list')

        //Pre-check

        //Execute
        //No warningHigh param
        controller.params.alertHigh = "PLUS_FOUR"
        controller.params.warningLow = "PLUSMINUS"
        controller.params.alertLow = "NEGATIVE"
        controller.params.'threshold.id' = testThreshold.id
        controller.update()

        //Check
        testThreshold.refresh()
        assert !testThreshold.hasErrors()
        assert testThreshold.alertHigh == ProteinValue.PLUS_FOUR
        assert testThreshold.warningHigh == null
        assert testThreshold.warningLow == ProteinValue.PLUSMINUS
        assert testThreshold.alertLow == ProteinValue.NEGATIVE
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
        controller.params.alertHigh = "PLUS_FOUR"
        controller.params.warningHigh = "PLUS_THREE"
        controller.params.alertLow = "NEGATIVE"
        controller.params.'threshold.id' = testThreshold.id
        controller.update()

        //Check
        testThreshold.refresh()
        assert !testThreshold.hasErrors()
        assert testThreshold.alertHigh == ProteinValue.PLUS_FOUR
        assert testThreshold.warningHigh == ProteinValue.PLUS_THREE
        assert testThreshold.warningLow == null
        assert testThreshold.alertLow == ProteinValue.NEGATIVE
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
        controller.params.alertHigh = "PLUS_FOUR"
        controller.params.warningHigh = "PLUS_THREE"
        controller.params.warningLow = "PLUSMINUS"
        controller.params.'threshold.id' = testThreshold.id
        controller.update()

        //Check
        testThreshold.refresh()
        assert !testThreshold.hasErrors()
        assert testThreshold.alertHigh == ProteinValue.PLUS_FOUR
        assert testThreshold.warningHigh == ProteinValue.PLUS_THREE
        assert testThreshold.warningLow == ProteinValue.PLUSMINUS
        assert testThreshold.alertLow == null
    }
}
