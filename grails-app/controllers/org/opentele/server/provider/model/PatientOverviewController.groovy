package org.opentele.server.provider.model

import grails.plugin.springsecurity.annotation.Secured
import org.opentele.server.model.Clinician
import org.opentele.server.model.ClinicianQuestionPreference
import org.opentele.server.model.Patient
import org.opentele.server.model.PatientGroup
import org.opentele.server.model.PatientNote
import org.opentele.server.model.PatientOverview
import org.opentele.server.core.util.TimeFilter

import org.opentele.server.provider.constants.Constants
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import org.opentele.server.core.model.types.PermissionName

@Secured(PermissionName.NONE)
class PatientOverviewController {
    def sessionService
    def patientService
    def patientNoteService
    def acknowledgeQuestionnaireService
    def questionnaireService
    def questionnaireProviderService
    def clinicianMessageService
    def clinicianService
    def patientOverviewService

    static allowedMethods = [index: 'GET', details: 'GET', acknowledgeQuestionnaireAndRenderDetails: 'POST', acknowledgeAll: 'GET', acknowledgeAllForAll: 'GET']

    @Secured(PermissionName.PATIENT_READ_ALL)
    def index() {
        def requestURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getForwardURI()
        session.setAttribute('lastReferer', requestURL)

        Clinician clinician = clinicianService.currentClinician
        sessionService.setNoPatient(session)

        if (params['patientgroup.filter.id'] != null) {
            clinicianService.saveUserPreference(clinician, Constants.SESSION_PATIENT_GROUP_ID, params.long('patientgroup.filter.id'))
        } else {
            params.setProperty('patientgroup.filter.id', clinicianService.getUserPreference(clinician, Constants.SESSION_PATIENT_GROUP_ID))
        }
        session[Constants.SESSION_PATIENT_GROUP_ID] = params.long('patientgroup.filter.id')
        List<PatientOverview> patientOverviews = fetchPatientOverviews(clinician, params)
        int patientOverviewTotal = countPatientOverviews(clinician)

        Set<Long> idsOfPatientsWithMessaging = patientOverviewService.getIdsOfPatientsWithMessagingEnabled(clinician, patientOverviews)
        Set<Long> idsOfPatientsWithAlarmIfUnreadMessagesDisabled = patientOverviewService.getIdsOfPatientsWithAlarmIfUnreadMessagesDisabled(clinician, patientOverviews)
        Map<Long, List<PatientNote>> patientNotes = patientOverviewService.fetchUnseenNotesForPatients(clinician, patientOverviews)

        [
                patients                                      : patientOverviews,
                patientNotes                                  : patientNotes,
                idsOfPatientsWithMessaging                    : idsOfPatientsWithMessaging,
                idsOfPatientsWithAlarmIfUnreadMessagesDisabled: idsOfPatientsWithAlarmIfUnreadMessagesDisabled,
                questionPreferences                           : questionPreferencesForClinician(clinician),
                clinicianPatientGroups                        : clinicianService.patientGroupsForCurrentClinician,
                patientOverviewTotal                          : patientOverviewTotal
        ]
    }

    @Secured(PermissionName.PATIENT_READ_ALL)
    def details() {
        def clinician = clinicianService.currentClinician
        def patient = Patient.get(params.id)
        checkAccessToPatient(patient)

        def completedQuestionnaireResultModel = questionnaireProviderService.extractMeasurements(patient.id, true, TimeFilter.all())

        def noUnacknowledgedQuestionnaires = completedQuestionnaireResultModel.columnHeaders.empty

        [
                patient                          : patient,
                noUnacknowledgedQuestionnaires   : noUnacknowledgedQuestionnaires,
                questionPreferences              : questionPreferencesForClinician(clinician),
                completedQuestionnaireResultModel: completedQuestionnaireResultModel
        ]
    }

    @Secured(PermissionName.QUESTIONNAIRE_ACKNOWLEDGE)
    def acknowledgeQuestionnaireAndRenderDetails() {
        def withAutoMessage = params.boolean('withAutoMessage')
        def questionnaire = CompletedQuestionnaire.get(params.id)
        def patient = questionnaire.patient
        def clinician = clinicianService.currentClinician
        checkAccessToPatient(patient)

        acknowledgeQuestionnaireService.acknowledge(questionnaire, null, withAutoMessage)

        def unacknowledgedQuestionnaires = CompletedQuestionnaire.findAllByPatientAndAcknowledgedDateIsNull(patient, [sort: 'uploadDate', order: 'desc'])
        def completedQuestionnaireResultModel = questionnaireProviderService.extractMeasurements(patient.id, true, TimeFilter.all())
        def patientNotes = patientOverviewService.fetchUnseenNotesForPatients(clinician, [patient.patientOverview])[patient.id]

        render(view: '/patientOverview/detailsWithHeader', model: [
                patient                          : patient,
                patientOverview                  : patient.patientOverview,
                messagingEnabled                 : clinicianMessageService.clinicianCanSendMessagesToPatient(clinician, patient),
                completedQuestionnaireResultModel: completedQuestionnaireResultModel,
                unacknowledgedQuestionnaires     : unacknowledgedQuestionnaires,
                patientNotes                     : patientNotes,
                questionPreferences              : questionPreferencesForClinician(clinician)
        ])
    }

    @Secured(PermissionName.QUESTIONNAIRE_ACKNOWLEDGE)
    def acknowledgeAll(Long id, boolean withAutoMessage) {
        def patient = Patient.get(id)
        if (!patient) {
            // Setting up session values
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patient.label', default: 'Patient')])
        } else {

            def completedQuestionnaireIds = params.list('ids').collect { it as Long }

            def questionnaires = CompletedQuestionnaire.findAllByIdInList(completedQuestionnaireIds)
            questionnaires.each { questionnaire ->
                checkAccessToPatient(questionnaire.patient)
            }

            acknowledgeQuestionnaireService.acknowledge(questionnaires, withAutoMessage)

            def msg = questionnaires.collect { questionnaire ->

                g.message(code: "completedquestionnaire.acknowledged",
                        args: [questionnaire.patientQuestionnaire?.name, g.formatDate(date: questionnaire.acknowledgedDate)])
            }

            if (msg) {
                flash.message = msg.join('<br/>')
            }
        }

        def lastReferer = session.getAttribute('lastReferer')
        if (lastReferer) {
            session.removeAttribute('lastReferer')
            redirect(url: lastReferer)
        }
    }

    @Secured(PermissionName.QUESTIONNAIRE_ACKNOWLEDGE)
    def acknowledgeAllForAll(boolean withAutoMessage) {
        Clinician clinician = clinicianService.currentClinician
        List<PatientOverview> patients = fetchPatientOverviews(clinician, [:])

        List<Long> unacknowledgedGreenQuestionnaireIds = []
        patients.findAll {
            it.greenQuestionnaireIds != null
        }.each {
            it.greenQuestionnaireIds.split(',').each {
                unacknowledgedGreenQuestionnaireIds << it.toLong()
            }
        }
        List<CompletedQuestionnaire> unacknowledgedGreenQuestionnaires = CompletedQuestionnaire.findAllByIdInList(unacknowledgedGreenQuestionnaireIds)
        acknowledgeQuestionnaireService.acknowledge(unacknowledgedGreenQuestionnaires, withAutoMessage)

        redirect(action: 'index')
    }

    private void checkAccessToPatient(Patient patient) {
        if (!patientService.allowedToView(patient)) {
            throw new IllegalStateException("User not allowed to view patient ${patient.id}")
        }
    }

    private def questionPreferencesForClinician(Clinician clinician) {
        if (clinician != null) {
            ClinicianQuestionPreference.findAllByClinicianAndQuestionIsNotNull(clinician, [sort: 'id', order: 'asc'])?.collect {
                it.questionId
            }
        } else {
            []
        }
    }

    private int countPatientOverviews(Clinician clinician) {
        patientOverviewService.count(clinician, getPatientGroupFilter(clinician))
    }

    private List<PatientOverview> fetchPatientOverviews(Clinician clinician, Map params) {
        PatientGroup patientGroupFilter = getPatientGroupFilter(clinician)

        (patientGroupFilter == null) ?
                patientOverviewService.getPatientsForClinicianOverview(clinician, params) :
                patientOverviewService.getPatientsForClinicianOverviewInPatientGroup(clinician, params, patientGroupFilter)
    }

    private PatientGroup getPatientGroupFilter(Clinician clinician) {
        PatientGroup patientGroupFilter = session[Constants.SESSION_PATIENT_GROUP_ID] ? PatientGroup.get(session[Constants.SESSION_PATIENT_GROUP_ID]) : null
        if (!patientOverviewService.isClinicianPartOfPatientGroup(clinician, patientGroupFilter)) {
            clinicianService.saveUserPreference(clinician, Constants.SESSION_PATIENT_GROUP_ID, null)
            patientGroupFilter = null
        }
        patientGroupFilter
    }
}



