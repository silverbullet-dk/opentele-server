package org.opentele.server.provider

import org.opentele.server.model.Clinician
import org.opentele.server.model.Message
import org.opentele.server.model.Patient
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import org.opentele.server.provider.constants.Constants
import org.springframework.web.context.request.RequestContextHolder

class ClinicianMessageService {

    def springSecurityService
	def clinicianService

	//NOT transactional
	def getUnreadMessageCount() {
        def user = springSecurityService.currentUser
		def patient
		def clinician
		def departments
		def unreadCount = 0
		def session = RequestContextHolder.currentRequestAttributes().getSession()
		if (user && user.isPatient()) {
			patient = Patient.findByUser(user)
			def countQuery = "from Message as m where m.patient=(:patient) and m.isRead=(:isread) and sentByPatient='false'"
			unreadCount = Message.findAll(countQuery, [patient: patient, isread: false]).size()
		} else if (user && user.isClinician() && session.name) {
			Long patientId = session[Constants.SESSION_PATIENT_ID]
			if (patientId) {
				//Logged in as clinician and there is a specific user selected
				def countQuery = "from Message as m where m.patient.id=(:patientId) and m.isRead=(:isread)"
				unreadCount = Message.findAll(countQuery, [patientId: patientId, isread: false]).size()
			}
		} else if (user && user.isClinician()) {
			clinician = Clinician.findByUser(user)
			departments = clinician.departments()
			def countQuery = "from Message as m where m.isRead=(:isread) and m.department in (:list) and m.sentByPatient='true'"
			unreadCount = (departments.size() > 0) ? Message.findAll(countQuery, [isread: false, list: departments]).size() : 0
		}

		unreadCount
	}

    boolean autoMessageIsEnabledForCompletedQuestionnaire(Long completedQuestionnaireID) {
        def completeQuestionnaire = CompletedQuestionnaire.findById(completedQuestionnaireID)
        def patient = completeQuestionnaire?.patient
        def clinician = clinicianService.currentClinician
        if(patient && clinician) {
            return clinicianCanSendMessagesToPatient(clinician, patient)
        } else  {
            return false;
        }
    }

    boolean clinicianCanSendMessagesToPatient(Clinician clinician, Patient patient) {
        !legalMessageSendersForClinicianToPatient(clinician, patient).empty
    }

    def legalMessageSendersForClinicianToPatient(Clinician clinician, Patient patient) {
        def clinicianPatientGroups = clinician.clinician2PatientGroups.collect {it.patientGroup}
        def patientPatientGroups = patient?.patient2PatientGroups?.collect {it.patientGroup}

        def sharedPatientGroups = clinicianPatientGroups?.intersect(patientPatientGroups)

        return sharedPatientGroups?.findAll {!it.disableMessaging}.collect {it.department}.unique()
    }
}
