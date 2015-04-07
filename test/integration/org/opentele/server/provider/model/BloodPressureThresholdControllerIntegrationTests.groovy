package org.opentele.server.provider.model

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.opentele.server.model.BloodPressureThreshold
import org.opentele.server.model.MeasurementType
import org.opentele.server.core.model.types.MeasurementTypeName
import org.opentele.server.provider.model.BloodPressureThresholdController
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder

@TestMixin(IntegrationTestMixin)
class BloodPressureThresholdControllerIntegrationTests {
	def daoAuthenticationProvider
	def controller
    def grailsApplication

    BloodPressureThreshold testThreshold
    
    private authenticate = {String login ->
        def authtoken, auth
        auth = new UsernamePasswordAuthenticationToken(login, login)
        authtoken = daoAuthenticationProvider.authenticate(auth)
        SecurityContextHolder.getContext().setAuthentication(authtoken)
    }

    @Before
	void setUp() {
        // Avoid conflicts with objects in session created earlier. E.g. in bootstrap
        grailsApplication.mainContext.sessionFactory.currentSession.clear()
        
		controller = new BloodPressureThresholdController()
        testThreshold = BloodPressureThreshold.findAll().get(0)
	}

	@After
	void tearDown() {

	}

    @Test
    void testEverythingIsRight() {
        BloodPressureThreshold threshold = new BloodPressureThreshold(type: MeasurementType.findByName(MeasurementTypeName.BLOOD_PRESSURE),
             diastolicAlertHigh: 100F,
             diastolicWarningHigh: 90F,
             diastolicWarningLow: 40F,
             diastolicAlertLow: 20F,

             systolicAlertHigh: 100F,
             systolicWarningHigh: 90F,
             systolicWarningLow: 40F,
             systolicAlertLow: 20F
        )
        assert threshold.validate()
    }

    @Test
    void testAlertHighTooLowShouldFail() {
        BloodPressureThreshold threshold = new BloodPressureThreshold(type: MeasurementType.findByName(MeasurementTypeName.BLOOD_PRESSURE),
                diastolicAlertHigh: 1F,
                diastolicWarningHigh: 90F,
                diastolicWarningLow: 40F,
                diastolicAlertLow: 20F,

                systolicAlertHigh: 1F,
                systolicWarningHigh: 90F,
                systolicWarningLow: 40F,
                systolicAlertLow: 20F
        )

        assert !threshold.validate()
    }

    @Test
    void testWarningHighTooHighShouldFail() {
        BloodPressureThreshold threshold = new BloodPressureThreshold(type: MeasurementType.findByName(MeasurementTypeName.BLOOD_PRESSURE),
                diastolicAlertHigh: 100F,
                diastolicWarningHigh: 190F,
                diastolicWarningLow: 40F,
                diastolicAlertLow: 20F,

                systolicAlertHigh: 100F,
                systolicWarningHigh: 190F,
                systolicWarningLow: 40F,
                systolicAlertLow: 20F
        )

        assert !threshold.validate()
    }
    @Test
    void testWarningHighTooLowShouldFail() {
        BloodPressureThreshold threshold = new BloodPressureThreshold(type: MeasurementType.findByName(MeasurementTypeName.BLOOD_PRESSURE),
                diastolicAlertHigh: 100F,
                diastolicWarningHigh: 10F,
                diastolicWarningLow: 40F,
                diastolicAlertLow: 20F,

                systolicAlertHigh: 100F,
                systolicWarningHigh: 10F,
                systolicWarningLow: 40F,
                systolicAlertLow: 20F
        )

        assert !threshold.validate()
    }

    @Test
    void testWarningLowTooHighShouldFail() {
        BloodPressureThreshold threshold = new BloodPressureThreshold(type: MeasurementType.findByName(MeasurementTypeName.BLOOD_PRESSURE),
                diastolicAlertHigh: 100F,
                diastolicWarningHigh: 90F,
                diastolicWarningLow: 410F,
                diastolicAlertLow: 20F,

                systolicAlertHigh: 100F,
                systolicWarningHigh: 90F,
                systolicWarningLow: 410F,
                systolicAlertLow: 20F
        )

        assert !threshold.validate()
    }
    @Test
    void testWarningLowTooLowShouldFail() {
        BloodPressureThreshold threshold = new BloodPressureThreshold(type: MeasurementType.findByName(MeasurementTypeName.BLOOD_PRESSURE),
                diastolicAlertHigh: 100F,
                diastolicWarningHigh: 90F,
                diastolicWarningLow: 4F,
                diastolicAlertLow: 20F,

                systolicAlertHigh: 100F,
                systolicWarningHigh: 90F,
                systolicWarningLow: 4F,
                systolicAlertLow: 20F
        )

        assert !threshold.validate()
    }

    @Test
    void testAlertLowTooHighShouldFail() {
        BloodPressureThreshold threshold = new BloodPressureThreshold(type: MeasurementType.findByName(MeasurementTypeName.BLOOD_PRESSURE),
                diastolicAlertHigh: 100F,
                diastolicWarningHigh: 90F,
                diastolicWarningLow: 40F,
                diastolicAlertLow: 120F,

                systolicAlertHigh: 100F,
                systolicWarningHigh: 90F,
                systolicWarningLow: 40F,
                systolicAlertLow: 120F
        )

        assert !threshold.validate()
    }

    @Test
    void testWithNull() {
        BloodPressureThreshold threshold = new BloodPressureThreshold(type: MeasurementType.findByName(MeasurementTypeName.BLOOD_PRESSURE),
                diastolicAlertHigh: null,
                diastolicWarningHigh: null,
                diastolicWarningLow: 40F,
                diastolicAlertLow: 20F,

                systolicAlertHigh: 100F,
                systolicWarningHigh: 90F,
                systolicWarningLow: 40F,
                systolicAlertLow: 20F
        )

        assert threshold.validate()

        threshold = new BloodPressureThreshold(type: MeasurementType.findByName(MeasurementTypeName.BLOOD_PRESSURE),
                diastolicAlertHigh: 100F,
                diastolicWarningHigh: 90F,
                diastolicWarningLow: null,
                diastolicAlertLow: null,

                systolicAlertHigh: 100F,
                systolicWarningHigh: 90F,
                systolicWarningLow: 40F,
                systolicAlertLow: 20F
        )
        assert threshold.validate()
        threshold = new BloodPressureThreshold(type: MeasurementType.findByName(MeasurementTypeName.BLOOD_PRESSURE),
                diastolicAlertHigh: null,
                diastolicWarningHigh: 90F,
                diastolicWarningLow: null,
                diastolicAlertLow: 20F,

                systolicAlertHigh: 100F,
                systolicWarningHigh: 90F,
                systolicWarningLow: 40F,
                systolicAlertLow: 20F
        )
        assert threshold.validate()
        threshold = new BloodPressureThreshold(type: MeasurementType.findByName(MeasurementTypeName.BLOOD_PRESSURE),
                diastolicAlertHigh: 100F,
                diastolicWarningHigh: null,
                diastolicWarningLow: 40F,
                diastolicAlertLow: null,

                systolicAlertHigh: 100F,
                systolicWarningHigh: 90F,
                systolicWarningLow: 40F,
                systolicAlertLow: 20F
        )
        assert threshold.validate()
        threshold = new BloodPressureThreshold(type: MeasurementType.findByName(MeasurementTypeName.BLOOD_PRESSURE),
                diastolicAlertHigh: 100F,
                diastolicWarningHigh: 90F,
                diastolicWarningLow: 40F,
                diastolicAlertLow: 20F,

                systolicAlertHigh: null,
                systolicWarningHigh: null,
                systolicWarningLow: 40F,
                systolicAlertLow: 20F
        )
        assert threshold.validate()
        threshold = new BloodPressureThreshold(type: MeasurementType.findByName(MeasurementTypeName.BLOOD_PRESSURE),
                diastolicAlertHigh: 100F,
                diastolicWarningHigh: 90F,
                diastolicWarningLow: 40F,
                diastolicAlertLow: 20F,

                systolicAlertHigh: 100F,
                systolicWarningHigh: 90F,
                systolicWarningLow: null,
                systolicAlertLow: null
        )
        assert threshold.validate()
        threshold = new BloodPressureThreshold(type: MeasurementType.findByName(MeasurementTypeName.BLOOD_PRESSURE),
                diastolicAlertHigh: 100F,
                diastolicWarningHigh: 90F,
                diastolicWarningLow: 40F,
                diastolicAlertLow: 20F,

                systolicAlertHigh: null,
                systolicWarningHigh: 90F,
                systolicWarningLow: null,
                systolicAlertLow: 20F
        )
        assert threshold.validate()
        threshold = new BloodPressureThreshold(type: MeasurementType.findByName(MeasurementTypeName.BLOOD_PRESSURE),
                diastolicAlertHigh: 100F,
                diastolicWarningHigh: 90F,
                diastolicWarningLow: 40F,
                diastolicAlertLow: 20F,

                systolicAlertHigh: 100F,
                systolicWarningHigh: null,
                systolicWarningLow: 40F,
                systolicAlertLow: null
        )


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
        controller.params.diastolicAlertHigh = "100"
        controller.params.diastolicWarningHigh = "90"
        controller.params.diastolicWarningLow = "40"
        controller.params.diastolicAlertLow = "20"

        controller.params.systolicAlertHigh = "100"
        controller.params.systolicWarningHigh = "90"
        controller.params.systolicWarningLow = "40"
        controller.params.systolicAlertLow = "20"

        controller.params.'threshold.id' = testThreshold.id
        controller.update()

        //Check
        testThreshold.refresh()
        assert !testThreshold.hasErrors()
        assert testThreshold.diastolicAlertHigh == 100F
        assert testThreshold.diastolicWarningHigh == 90F
        assert testThreshold.diastolicWarningLow == 40F
        assert testThreshold.diastolicAlertLow == 20F

        assert testThreshold.systolicAlertHigh == 100F
        assert testThreshold.systolicWarningHigh == 90F
        assert testThreshold.systolicWarningLow == 40F
        assert testThreshold.systolicAlertLow == 20F
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
        controller.params.diastolicAlertHigh = ""
        controller.params.diastolicWarningHigh = "90"
        controller.params.diastolicWarningLow = "40"
        controller.params.diastolicAlertLow = "20"

        controller.params.systolicAlertHigh = ""
        controller.params.systolicWarningHigh = "90"
        controller.params.systolicWarningLow = "40"
        controller.params.systolicAlertLow = "20"

        controller.params.'threshold.id' = testThreshold.id
        controller.update()

        //Check
        testThreshold.refresh()
        assert !testThreshold.hasErrors()
        assert testThreshold.diastolicAlertHigh == null
        assert testThreshold.diastolicWarningHigh == 90F
        assert testThreshold.diastolicWarningLow == 40F
        assert testThreshold.diastolicAlertLow == 20F

        assert testThreshold.systolicAlertHigh == null
        assert testThreshold.systolicWarningHigh == 90F
        assert testThreshold.systolicWarningLow == 40F
        assert testThreshold.systolicAlertLow == 20F
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
        controller.params.diastolicAlertHigh = "100"
        controller.params.diastolicWarningHigh = ""
        controller.params.diastolicWarningLow = "40"
        controller.params.diastolicAlertLow = "20"

        controller.params.systolicAlertHigh = "100"
        controller.params.systolicWarningHigh = ""
        controller.params.systolicWarningLow = "40"
        controller.params.systolicAlertLow = "20"

        controller.params.'threshold.id' = testThreshold.id
        controller.update()

        //Check
        testThreshold.refresh()
        assert !testThreshold.hasErrors()
        assert testThreshold.diastolicAlertHigh == 100F
        assert testThreshold.diastolicWarningHigh == null
        assert testThreshold.diastolicWarningLow == 40F
        assert testThreshold.diastolicAlertLow == 20F

        assert testThreshold.systolicAlertHigh == 100F
        assert testThreshold.systolicWarningHigh == null
        assert testThreshold.systolicWarningLow == 40F
        assert testThreshold.systolicAlertLow == 20F
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
        controller.params.diastolicAlertHigh = "100"
        controller.params.diastolicWarningHigh = "90"
        controller.params.diastolicWarningLow = ""
        controller.params.diastolicAlertLow = "20"

        controller.params.systolicAlertHigh = "100"
        controller.params.systolicWarningHigh = "90"
        controller.params.systolicWarningLow = ""
        controller.params.systolicAlertLow = "20"

        controller.params.'threshold.id' = testThreshold.id
        controller.update()

        //Check
        testThreshold.refresh()
        assert !testThreshold.hasErrors()
        assert testThreshold.diastolicAlertHigh == 100F
        assert testThreshold.diastolicWarningHigh == 90F
        assert testThreshold.diastolicWarningLow == null
        assert testThreshold.diastolicAlertLow == 20F

        assert testThreshold.systolicAlertHigh == 100F
        assert testThreshold.systolicWarningHigh == 90F
        assert testThreshold.systolicWarningLow == null
        assert testThreshold.systolicAlertLow == 20F
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
        controller.params.diastolicAlertHigh = "100"
        controller.params.diastolicWarningHigh = "90"
        controller.params.diastolicWarningLow = "40"
        controller.params.diastolicAlertLow = ""

        controller.params.systolicAlertHigh = "100"
        controller.params.systolicWarningHigh = "90"
        controller.params.systolicWarningLow = "40"
        controller.params.systolicAlertLow = ""

        controller.params.'threshold.id' = testThreshold.id
        controller.update()

        //Check
        testThreshold.refresh()
        assert !testThreshold.hasErrors()
        assert testThreshold.diastolicAlertHigh == 100F
        assert testThreshold.diastolicWarningHigh == 90F
        assert testThreshold.diastolicWarningLow == 40F
        assert testThreshold.diastolicAlertLow == null

        assert testThreshold.systolicAlertHigh == 100F
        assert testThreshold.systolicWarningHigh == 90F
        assert testThreshold.systolicWarningLow == 40F
        assert testThreshold.systolicAlertLow == null
    }

}
