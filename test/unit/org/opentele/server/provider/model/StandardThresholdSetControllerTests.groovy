package org.opentele.server.provider.model

import grails.buildtestdata.mixin.Build
import grails.test.mixin.*
import grails.test.mixin.support.GrailsUnitTestMixin
import org.opentele.server.provider.SessionService
import org.opentele.server.provider.ThresholdService
import org.opentele.server.model.BloodPressureThreshold
import org.opentele.server.model.MeasurementType
import org.opentele.server.model.NumericThreshold
import org.opentele.server.model.StandardThresholdSet
import org.opentele.server.model.UrineGlucoseThreshold
import org.opentele.server.model.UrineThreshold
import org.opentele.server.core.model.types.MeasurementTypeName

@SuppressWarnings("GroovyAssignabilityCheck")
@TestFor(StandardThresholdSetController)
@Build([StandardThresholdSet, MeasurementType, BloodPressureThreshold, NumericThreshold, UrineThreshold, UrineGlucoseThreshold])
class StandardThresholdSetControllerTests {
    void setUp() {
        controller.thresholdService = new ThresholdService()
        controller.sessionService = new SessionService()
    }

    def populateValidParams(params) {
        assert params != null
    }

    void testIndex() {
        controller.index()
        assert response.redirectedUrl == "/standardThresholdSet/list"
    }

    void testList() {
        def model = controller.list()

        assert model.standardThresholdSetInstanceList.size() == 0
    }

    void testStandardThresholdSetsSaveWorksWithEmptyStringValuesBLOODPRESSURE() {
        MeasurementType.build(name: MeasurementTypeName.BLOOD_PRESSURE)
        StandardThresholdSet sts = StandardThresholdSet.build()
        sts.save()
        sts = StandardThresholdSet.findAll().get(0)
        assert sts != null

        params.id = sts.id
        params.type = MeasurementTypeName.BLOOD_PRESSURE.toString()
        params.diastolicAlertHigh = ""
        params.diastolicAlertLow = ""
        params.diastolicWarningHigh = ""
        params.diastolicWarningLow = ""
        params.systolicAlertHigh = ""
        params.systolicAlertLow = ""
        params.systolicWarningHigh = ""
        params.systolicWarningLow = ""

        controller.saveThresholdToSet()
        assert response.redirectedUrl == "/standardThresholdSet/list#1"
    }

    void testStandardThresholdSetsSaveWorksWithEmptyStringValuesNUMERICTYPES() {
        MeasurementType.build(name: MeasurementTypeName.PULSE)
        StandardThresholdSet sts = StandardThresholdSet.build()
        sts.save()
        sts = StandardThresholdSet.findAll().get(0)
        assert sts != null

        params.id = sts.id
        params.type = MeasurementTypeName.PULSE.toString()
        params.alertHigh = ""
        params.alertLow = ""
        params.warningHigh = ""
        params.warningLow = ""

        controller.saveThresholdToSet()
        assert response.redirectedUrl == "/standardThresholdSet/list#1"
    }

    void testStandardThresholdSetsSaveWorksWithEmptyStringValuesURINE() {
        MeasurementType.build(name: MeasurementTypeName.URINE)
        StandardThresholdSet sts = StandardThresholdSet.build()
        sts.save()
        sts = StandardThresholdSet.findAll().get(0)
        assert sts != null

        params.id = sts.id
        params.type = MeasurementTypeName.URINE.toString()
        params.alertHigh = ""
        params.alertLow = ""
        params.warningHigh = ""
        params.warningLow = ""

        controller.saveThresholdToSet()
        assert response.redirectedUrl == "/standardThresholdSet/list#1"
    }

    void testStandardThresholdSetsSaveWorksWithEmptyStringValuesGLUCOSE() {
        MeasurementType.build(name: MeasurementTypeName.URINE_GLUCOSE)
        StandardThresholdSet sts = StandardThresholdSet.build()
        sts.save()
        sts = StandardThresholdSet.findAll().get(0)
        assert sts != null

        params.id = sts.id
        params.type = MeasurementTypeName.URINE_GLUCOSE.toString()
        params.alertHigh = ""
        params.alertLow = ""
        params.warningHigh = ""
        params.warningLow = ""

        controller.saveThresholdToSet()
        assert response.redirectedUrl == "/standardThresholdSet/list#1"
    }

}
