package org.opentele.server.provider.model

import grails.buildtestdata.mixin.Build
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import grails.test.mixin.TestMixin
import grails.test.mixin.domain.DomainClassUnitTestMixin
import org.junit.Before
import org.junit.Test
import org.opentele.builders.PatientBuilder
import org.opentele.server.core.PatientOverviewMaintenanceService
import org.opentele.server.core.model.types.MeasurementTypeName
import org.opentele.server.model.*
import org.opentele.server.provider.ClinicianMessageService
import org.opentele.server.provider.PatientService
import org.opentele.server.provider.SessionService
import org.opentele.server.provider.ThresholdService

@SuppressWarnings("GroovyAccessibility")
@TestFor(PatientController)
@Build([Patient, Clinician, PatientGroup, Patient2PatientGroup, User, Role, UserRole, NextOfKinPerson, MonitoringPlan, Message, User])
@Mock([Patient, Threshold])
class PatientControllerUnitTests {

    // NB: Patients are created using a webflow, with its own tests

    // create, save, delete, filterLists, search, overview, questionnaires, doRemoveNextOfKin, messages, savePrefs
    // are tested in integration tests.
    // savePatient tested in PatientController + PatientService integration tests

    @Before
    void setUp() {
        controller.sessionService = new SessionService()
        controller.patientService = new PatientService()
        controller.thresholdService = new ThresholdService()
    }

    def mockServices = { patient ->
        def mockSpringSecurityService = mockFor(SpringSecurityService)
        mockSpringSecurityService.metaClass.getCurrentUser = { ->
            patient.getUser()
        }
        controller.springSecurityService = mockSpringSecurityService

        def mockMessageService = mockFor(ClinicianMessageService)
        mockMessageService.metaClass.clinicianCanSendMessagesToPatient = { x, y ->
            null
        }
        controller.clinicianMessageService = mockMessageService
    }

    @Test
    void patientShowReturnsPatient() {

        //Setup
        Patient patient = new PatientBuilder().build()
        patient.save()

        mockServices(patient)

        //Execute
        params.id = patient.id
        def model = controller.show()

        //Check
        assert response.redirectedUrl == null
        assert model.patientInstance != null
        assert model.patientInstance.id == patient.id

        //.. check patient groups are returned
        assert model.groups != null
        //.. check patient next of kin are returned
        assert model.nextOfKin != null
    }

    @Test
    void patientShowRedirectsIfNoId() {
        //Setup
        Patient patient = new PatientBuilder().build()
        patient.save()

        //Execute
        //Not setting params.id..
        def model = controller.show()

        //Check
        assert '/patient/overview' == response.redirectedUrl
        assert model == null
    }

    @Test
    void patientShowRedirectsIfWrongId() {
        //Setup
        Patient patient = new PatientBuilder().build()
        patient.save()

        //Execute
        params.id = 4242
        def model = controller.show()

        //Check
        assert '/patient/overview' == response.redirectedUrl
        assert model == null
    }

    @Test
    void patientEditReturnsPatient() {
        //Setup
        Patient patient = new PatientBuilder().build()
        patient.save()

        mockServices(patient)

        //Execute
        params.id = patient.id
        def model = controller.edit()

        //Check
        assert response.redirectedUrl == null
        assert model.patientInstance != null
        assert model.patientInstance.id == patient.id

        //.. check patient groups are returned
        assert model.groups != null
    }

    @Test
    void patientEditRedirectsIfNoId() {
        //Setup
        Patient patient = new PatientBuilder().build()
        patient.save()

        //Execute
        //Not setting params.id..
        def model = controller.show()

        //Check
        assert '/patient/overview' == response.redirectedUrl
        assert model == null
    }

    @Test
    void patientEditRedirectsIfWrongId() {
        //Setup
        Patient patient = new PatientBuilder().build()
        patient.save()

        //Execute
        params.id = 4242
        def model = controller.show()

        //Check
        assert '/patient/overview' == response.redirectedUrl
        assert model == null
    }

    @SuppressWarnings("GroovyAccessibility")
    @Test
    void getPatientGroupsReturnsCorrectGroup() {
        //Setup
        Patient patient = new PatientBuilder().build()
        PatientGroup.build(name: 'badGroup1')
        PatientGroup.build(name: 'badGroup2')
        PatientGroup.build(name: 'badGroup3')
        PatientGroup pg = PatientGroup.build(name: 'goodGroup')
        Patient2PatientGroup.link(patient, pg)
        patient.save(flush: true)

        //Execute
        def groups = controller.getPatientGroups(patient)

        //Check
        assert groups != null
        assert groups.size() == 1
        assert groups[0] != null
        assert groups[0].name == 'goodGroup'
    }

    @Test
    void getPatientGroupsReturnsCorrectGroups() {
        //Setup
        Patient patient = new PatientBuilder().build()
        PatientGroup.build(name: 'badGroup1')
        PatientGroup.build(name: 'badGroup2')
        PatientGroup.build(name: 'badGroup3')
        PatientGroup pg = PatientGroup.build(name: 'goodGroup')
        PatientGroup pg2 = PatientGroup.build(name: 'goodGroup2')
        Patient2PatientGroup.link(patient, pg)
        Patient2PatientGroup.link(patient, pg2)
        patient.save(flush: true)

        //Execute
        def groups = controller.getPatientGroups(patient)

        //Check
        assert groups != null
        assert groups.size() == 2
        assert groups[0] != null
        assert groups[1] != null
        assert (groups[0].name == 'goodGroup' || groups[0].name == 'goodGroup2')
        assert (groups[1].name == 'goodGroup' || groups[1].name == 'goodGroup2')
        assert !(groups[0].name == 'goodGroup' && groups[1].name == 'goodGroup')
        assert !(groups[0].name == 'goodGroup2' && groups[1].name == 'goodGroup2')
    }

    @Test
    void getPatientNextOfKinReturnsCorrectNok() {
        //Setup
        Patient patient = new PatientBuilder().build()
        NextOfKinPerson.build(firstName: 'badNok1')
        NextOfKinPerson.build(firstName: 'badNok2')
        NextOfKinPerson.build(firstName: 'badNok3')
        NextOfKinPerson nextOfKinPerson = NextOfKinPerson.build(firstName: 'goodNok1')
        patient.addToNextOfKinPersons(nextOfKinPerson)
        patient.save(flush: true)

        //Execute
        def nextOfKinPersons = controller.getNextOfKin(patient)

        //Check
        assert nextOfKinPersons != null
        assert nextOfKinPersons.size() == 1
        assert nextOfKinPersons[0].firstName == 'goodNok1'
    }

    @Test
    void getPatientNextOfKinReturnsCorrectNoks() {
        //Setup
        Patient patient = new PatientBuilder().build()
        NextOfKinPerson nextOfKinPerson1 = NextOfKinPerson.build(firstName: 'goodNok1')
        NextOfKinPerson nextOfKinPerson2 = NextOfKinPerson.build(firstName: 'goodNok2')
        //noinspection GroovyUnusedAssignment
        NextOfKinPerson nextOfKinPerson3 = NextOfKinPerson.build(firstName: 'badNok1')
        patient.addToNextOfKinPersons(nextOfKinPerson1)
        patient.addToNextOfKinPersons(nextOfKinPerson2)
        patient.save(flush: true)

        //Execute
        def nextOfKinPersons = controller.getNextOfKin(patient)

        //Check
        assert nextOfKinPersons != null
        assert nextOfKinPersons.size() == 2
        assert (nextOfKinPersons[0].firstName == 'goodNok1' || nextOfKinPersons[0].firstName == 'goodNok2')
        assert (nextOfKinPersons[1].firstName == 'goodNok2' || nextOfKinPersons[1].firstName == 'goodNok1')
        assert !(nextOfKinPersons[0].firstName == 'goodNok1' && nextOfKinPersons[1].firstName == 'goodNok1')
        assert !(nextOfKinPersons[0].firstName == 'goodNok2' && nextOfKinPersons[1].firstName == 'goodNok2')
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    @Test
    void removeNextOfKinReturnsExistingPatient() {
        //Setup
        Patient patient = new PatientBuilder().build()
        patient.addToNextOfKinPersons(firstName: 'firstName', lastName: 'lastName')
        patient.save()
        def nextOfKin = NextOfKinPerson.findByPatient(patient)

        assert nextOfKin

        //Execute
        params.id = patient.id
        params.nextOfKin = nextOfKin.id

        controller.removeNextOfKin()

        //Check
        assert response.redirectedUrl == "/patient/edit/" + patient.id
    }

    @Test
    void removeAllBluesRemovesBlueAlarms() {
        //Setup
        Patient patient = new PatientBuilder().build()
        patient.blueAlarmQuestionnaireIDs = ['0', '1', '2'] as Set<Long>
        patient.save(flush: true)
        def patientOverviewMaintenanceServiceControl = mockFor(PatientOverviewMaintenanceService)
        patientOverviewMaintenanceServiceControl.demand.updateOverviewFor { it == patient }
        controller.patientService.patientOverviewMaintenanceService = patientOverviewMaintenanceServiceControl.createMock()

        //Execute
        params.patientID = patient.id
        controller.removeAllBlue()
        patient.refresh()

        //Check
        assert response.redirectedUrl != null
        assert patient.blueAlarmQuestionnaireIDs.size() == 0
    }

    @Test
    void setNoAlarmIfUnreadMessagesToPatientWorks() {
        //Setup
        Patient patient = new PatientBuilder().build()
        patient.save(flush: true)

        assert !patient.noAlarmIfUnreadMessagesToPatient

        def patientOverviewMaintenanceServiceControl = mockFor(PatientOverviewMaintenanceService)
        patientOverviewMaintenanceServiceControl.demand.updateOverviewFor { it == patient }
        controller.patientService.patientOverviewMaintenanceService = patientOverviewMaintenanceServiceControl.createMock()

        //Execute
        params.patientID = patient.id
        controller.noAlarmIfUnreadMessagesToPatient()
        patient.refresh()

        //Check
        assert response.redirectedUrl != null
        assert patient.noAlarmIfUnreadMessagesToPatient
    }

    @Test
    void testChangeDataResponsibleToNoneWorks() {
        //Setup
        Patient patient = new PatientBuilder().build()
        patient.dataResponsible = PatientGroup.build(name: 'A-Team')
        patient.save(flush: true, validate: null)

        //Execute
        params.id = patient.id
        params.dataResponsible = ''
        controller.updateDataResponsible()

        //Check
        assert response.redirectedUrl == "/patient/edit/" + patient.id
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    @Test
    void testCanUpdatePatientThresholdValueToEmptyBP() {
        //Setup
        Patient patient = new PatientBuilder().build()
        assert patient != null
        mockDomain(MeasurementType, [[name: MeasurementTypeName.BLOOD_PRESSURE]])
        mockDomain(BloodPressureThreshold)
        mockServices(patient)
        //Execute

        controller.params.id = patient.id
        controller.params.type = "BLOOD_PRESSURE"
        controller.saveThresholdToPatient()

        //Check
        assert controller.response.status == 200
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    @Test
    void testCanUpdatePatientThresholdValueToEmptyURINE() {
        //Setup
        Patient patient = new PatientBuilder().build()
        assert patient != null
        mockDomain(MeasurementType, [[name: MeasurementTypeName.URINE]])
        mockDomain(UrineThreshold)
        mockServices(patient)

        //Execute

        controller.params.id = patient.id
        controller.params.type = "URINE"
        controller.saveThresholdToPatient()

        //Check
        assert controller.response.status == 200
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    @Test
    void testCanUpdatePatientThresholdValueToEmptyNUMERIC() {
        //Setup
        Patient patient = new PatientBuilder().build()
        assert patient != null
        mockDomain(MeasurementType, [[name: MeasurementTypeName.WEIGHT]])
        mockDomain(NumericThreshold)
        mockServices(patient)

        //Execute

        controller.params.id = patient.id
        controller.params.type = "WEIGHT"
        controller.saveThresholdToPatient()

        //Check
        assert controller.response.status == 200
    }
}
