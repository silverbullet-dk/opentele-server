package org.opentele.taglib

import org.opentele.server.provider.ClinicianMessageService
import org.opentele.server.provider.ClinicianService
import org.opentele.server.model.Patient

class MessageTagLib {

    ClinicianMessageService clinicianMessageService
    ClinicianService clinicianService

    static namespace = "message"

    def unreadMessages = { attrs ->
        if (clinicianMessageService.getUnreadMessageCount() > 0) {
            out << """<strong class="messagecount ${attrs.class ?: ''}"> ("""
            out << clinicianMessageService.getUnreadMessageCount()
            out << ")</strong> "
        }
    }

    def canUseMessaging = { attrs, body ->
        def patient = Patient.get(session.patientId)
        if(clinicianMessageService.clinicianCanSendMessagesToPatient(clinicianService.currentClinician, patient)) {
            out << body()
        }
    }

}
