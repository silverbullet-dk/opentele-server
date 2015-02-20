package org.opentele.server.provider.model
import grails.buildtestdata.mixin.Build
import grails.test.mixin.TestMixin
import grails.test.mixin.webflow.WebFlowUnitTestMixin
import org.junit.Before
import org.junit.Test
import org.opentele.server.core.PasswordService
import org.opentele.server.core.command.CreatePatientCommand
import org.opentele.server.model.User
import org.opentele.server.provider.model.PatientController

@TestMixin([WebFlowUnitTestMixin])
@Build(User)
class CreatePatientFlowUnitTests {

    def controller
    @Before
    void mockController() {
        controller = mockController(PatientController)
    }

    @Test
    void willInitializePatientCommandOnFlowStart() {
        def passwordServiceMock = new PasswordService()
        passwordServiceMock.words = ["word"]
        controller.passwordService = passwordServiceMock

        createPatientFlow.startMagic.action()
        assert flow.patientInstance
    }

    @Test
    void previousGoesBackOneState() {
        assert !createPatientFlow.basicPatientInformation.on.previous

        assert createPatientFlow.setAuthentication.on.previous.to == 'basicPatientInformation'
        assert createPatientFlow.choosePatientGroup.on.previous.to == 'setAuthentication'
        assert createPatientFlow.thresholdValues.on.previous.to == 'choosePatientGroup'
        assert createPatientFlow.addComment.on.previous.to == 'thresholdValues'
        assert createPatientFlow.addNextOfKin.on.previous.to == 'addComment'
    }

    @Test
    void endIsTheEnd() {
        assert !createPatientFlow.on
    }

    @Test
    void clearsValidationErrorsWhenGoingToPrevious() {
        setPatientInstanceWithError()
        createPatientFlow.setAuthentication.on.previous.action()
        assert !flow.patientInstance.hasErrors()

        setPatientInstanceWithError()
        createPatientFlow.choosePatientGroup.on.previous.action()
        assert !flow.patientInstance.hasErrors()

        setPatientInstanceWithError()
        createPatientFlow.thresholdValues.on.previous.action()
        assert !flow.patientInstance.hasErrors()

        setPatientInstanceWithError()
        createPatientFlow.addComment.on.previous.action()
        assert !flow.patientInstance.hasErrors()

        setPatientInstanceWithError()
        createPatientFlow.addNextOfKin.on.previous.action()
        assert !flow.patientInstance.hasErrors()
    }

    @Test
    void canSkipToEnd() {
        assert createPatientFlow.choosePatientGroup.on.saveAndShow
        assert createPatientFlow.thresholdValues.on.saveAndShow
        assert createPatientFlow.addComment.on.saveAndShow
        assert createPatientFlow.addNextOfKin.on.saveAndShow
    }

    @Test
    void canSkipToEndAndShowMonitoringPlan() {
        assert createPatientFlow.choosePatientGroup.on.saveAndGotoMonplan
        assert createPatientFlow.thresholdValues.on.saveAndGotoMonplan
        assert createPatientFlow.addComment.on.saveAndGotoMonplan
        assert createPatientFlow.addNextOfKin.on.saveAndGotoMonplan
    }

    private CreatePatientCommand setPatientInstanceWithError() {
        def createPatientCommand = new CreatePatientCommand()
        createPatientCommand.errors.reject('firstName')
        flow.patientInstance = createPatientCommand

        assert flow.patientInstance.hasErrors()
    }
}
