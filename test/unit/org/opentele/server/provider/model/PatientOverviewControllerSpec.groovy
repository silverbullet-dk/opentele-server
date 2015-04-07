package org.opentele.server.provider.model

import grails.buildtestdata.mixin.Build
import grails.test.mixin.*
import org.opentele.server.provider.ClinicianService
import org.opentele.server.provider.PatientNoteService
import org.opentele.server.provider.PatientOverviewService
import org.opentele.server.model.Clinician
import org.opentele.server.model.Clinician2PatientGroup
import org.opentele.server.model.Department
import org.opentele.server.model.Patient
import org.opentele.server.model.Patient2PatientGroup
import org.opentele.server.model.PatientGroup
import org.opentele.server.model.PatientOverview
import org.opentele.server.model.User

import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.opentele.server.core.model.types.Severity
import org.opentele.server.provider.AcknowledgeQuestionnaireService
import spock.lang.Specification

@SuppressWarnings("GroovyAssignabilityCheck")
@TestFor(PatientOverviewController)
@Build([CompletedQuestionnaire, Clinician, User, Patient, Department, PatientGroup, Patient2PatientGroup, Clinician2PatientGroup, QuestionnaireHeader, PatientOverview])
class PatientOverviewControllerSpec extends Specification {
    Patient patientA
    Patient patientB
    Clinician clinicianA
    Clinician clinicianB
    PatientGroup patientGroupA
    PatientGroup patientGroupB
    ArrayList<CompletedQuestionnaire> questionnaireTestList

    def setup() {
        controller.acknowledgeQuestionnaireService = Mock(AcknowledgeQuestionnaireService)
        controller.clinicianService = Mock(ClinicianService)
        controller.patientOverviewService = Mock(PatientOverviewService)
        controller.metaClass.checkAccessToPatient = { Patient patient -> /* Do nothing */ }
        controller.patientNoteService = Mock(PatientNoteService)

        Department depA = Department.build(name: "A")
        Department depB = Department.build(name: "B")
        patientGroupA = PatientGroup.build(department: depA)
        patientGroupB = PatientGroup.build(department: depB)

        patientA = Patient.build(firstName: "PatientA")
        Patient2PatientGroup.build(patient: patientA, patientGroup: patientGroupA)

        patientB = Patient.build(firstName: "PatientB")
        Patient2PatientGroup.build(patient: patientB, patientGroup: patientGroupB)

        clinicianA = Clinician.build(firstName: "ClinicianA")
        Clinician2PatientGroup.build(clinician: clinicianA, patientGroup: patientGroupA)

        clinicianB = Clinician.build(firstName: "ClinicianB")
        Clinician2PatientGroup.build(clinician: clinicianB, patientGroup: patientGroupB)

        controller.clinicianService.currentClinician >> clinicianA

        controller.patientNoteService.countImportantWithoutDeadline(_) >> 0
        controller.patientNoteService.countImportantWithReminder(_) >> 0
        controller.patientNoteService.countNormalWithReminder(_) >> 0


        questionnaireTestList = [
            CompletedQuestionnaire.build(patient: patientA, acknowledgedDate: new Date(), severity: Severity.GREEN),
            CompletedQuestionnaire.build(patient: patientA, acknowledgedDate: new Date(), severity: Severity.GREEN),
            CompletedQuestionnaire.build(patient: patientA, acknowledgedDate: new Date(), severity: Severity.BLUE),
            CompletedQuestionnaire.build(patient: patientA, acknowledgedDate: new Date(), severity: Severity.YELLOW),
            CompletedQuestionnaire.build(patient: patientA, acknowledgedDate: new Date(), severity: Severity.RED),
            CompletedQuestionnaire.build(patient: patientA, acknowledgedDate: new Date(), severity: Severity.ABOVE_THRESHOLD),
            CompletedQuestionnaire.build(patient: patientA, acknowledgedDate: new Date(), severity: Severity.BELOW_THRESHOLD),
            CompletedQuestionnaire.build(patient: patientB, acknowledgedDate: new Date(), severity: Severity.GREEN),
            CompletedQuestionnaire.build(patient: patientB, acknowledgedDate: new Date(), severity: Severity.GREEN),
            CompletedQuestionnaire.build(patient: patientB, acknowledgedDate: new Date(), severity: Severity.BLUE),
            CompletedQuestionnaire.build(patient: patientB, acknowledgedDate: new Date(), severity: Severity.YELLOW),
            CompletedQuestionnaire.build(patient: patientB, acknowledgedDate: new Date(), severity: Severity.RED),
            CompletedQuestionnaire.build(patient: patientB, acknowledgedDate: new Date(), severity: Severity.ABOVE_THRESHOLD),
            CompletedQuestionnaire.build(patient: patientB, acknowledgedDate: new Date(), severity: Severity.BELOW_THRESHOLD)
        ]
    }

    def 'sends auto-messages as part of acknowledgeAll when told to'() {
        //The tested method is passed a list of Questionnaire.ids. It has to call be the messageService this amount of times.
        setup:
        def greenQuestionnairesForPatientA = questionnaireTestList.findAll { it.patient == patientA && it.severity == Severity.GREEN }
        params.ids = greenQuestionnairesForPatientA*.id
        params.id = patientA.id

        when:
        controller.params.withAutoMessage = 'true'
        controller.acknowledgeAll()

        then:
        1 * controller.acknowledgeQuestionnaireService.acknowledge({it.size() == greenQuestionnairesForPatientA.size()}, true)
    }

    def 'does not send auto-messages as part of acknowledgeAll when told not to'() {
        //The tested method is passed a list of Questionnaire.ids. It has to call be the messageService this amount of times.
        setup:
        def greenQuestionnairesForPatientA = questionnaireTestList.findAll { it.patient == patientA && it.severity == Severity.GREEN }
        params.ids = greenQuestionnairesForPatientA*.id
        params.id = patientA.id

        when:
        controller.params.withAutoMessage = 'false'
        controller.acknowledgeAll()

        then:
        1 * controller.acknowledgeQuestionnaireService.acknowledge({it.size() == greenQuestionnairesForPatientA.size()}, false)
    }

    def 'sends auto-messages in acknowledgeAllForAll the correct number of times when told so'() {
        setup:
        def correctQuestionnaires = questionnaireTestList.findAll { it.severity == Severity.GREEN }
        def greenQuestionnaireIdsForPatientA = correctQuestionnaires.findAll { it.patient == patientA }*.id
        def greenQuestionnaireIdsForPatientB = correctQuestionnaires.findAll { it.patient == patientB }*.id
        controller.patientOverviewService.getPatientsForClinicianOverview(clinicianA, [:]) >> [
                PatientOverview.build(patient: patientA, greenQuestionnaireIds: greenQuestionnaireIdsForPatientA.join(',')),
                PatientOverview.build(patient: patientB, greenQuestionnaireIds: greenQuestionnaireIdsForPatientB.join(','))]

        when:
        controller.params.withAutoMessage = 'true'
        controller.acknowledgeAllForAll()

        then:
        1 * controller.acknowledgeQuestionnaireService.acknowledge(correctQuestionnaires, true)
    }

    def 'does not send any auto-messages in acknowledgeAllForAll when told not to'() {
        setup:
        def correctQuestionnaires = questionnaireTestList.findAll { it.severity == Severity.GREEN }
        def greenQuestionnaireIdsForPatientA = correctQuestionnaires.findAll { it.patient == patientA }*.id
        def greenQuestionnaireIdsForPatientB = correctQuestionnaires.findAll { it.patient == patientB }*.id
        controller.patientOverviewService.getPatientsForClinicianOverview(clinicianA, [:]) >> [
                PatientOverview.build(patient: patientA, greenQuestionnaireIds: greenQuestionnaireIdsForPatientA.join(',')),
                PatientOverview.build(patient: patientB, greenQuestionnaireIds: greenQuestionnaireIdsForPatientB.join(','))]

        when:
        controller.params.withAutoMessage = 'false'
        controller.acknowledgeAllForAll()

        then:
        1 * controller.acknowledgeQuestionnaireService.acknowledge(correctQuestionnaires, false)
    }
}