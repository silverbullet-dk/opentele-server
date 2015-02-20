package org.opentele.server.provider

import grails.plugin.springsecurity.SpringSecurityUtils
import org.opentele.server.provider.constants.Constants
import org.opentele.server.model.NextOfKinPerson;
import org.opentele.server.model.Patient;

import javax.servlet.http.HttpSession;

class SessionService {
	void setPatient(HttpSession session, Patient patient) {
        session[Constants.SESSION_NAME] = patient.toString()
        session[Constants.SESSION_CPR] = patient.cpr[0..5]+"-"+patient.cpr[6..9]
        session[Constants.SESSION_PATIENT_ID] = patient.id
        session[Constants.SESSION_GESTATIONAL_AGE] = patient.shouldShowGestationalAge ? patient.getGestationalAge(new Date()) : null
        session[Constants.SESSION_RUNNING_CTG_MESSAGING] = patient.shouldShowRunningCtgMessaging
        session[Constants.SESSION_PHONE] = patient.phone
        session[Constants.SESSION_MOBILE_PHONE] = patient.mobilePhone
        session[Constants.SESSION_EMAIL] = patient.email
        session[Constants.SESSION_FIRST_RELEATIVE] = NextOfKinPerson.findByPatient(patient)
        session[Constants.SEESION_DATARESPONSIBLE] = patient.dataResponsible
        session[Constants.SESSION_ACCESS_VALIDATED] = "true"
        session[Constants.SESSION_COMMENT_ALL] = patient.comment
        session[Constants.SESSION_COMMENT] = patient.getShortComment()

	}

    void setNoPatient(HttpSession session) {
        session[Constants.SESSION_NAME] = ""
        session[Constants.SESSION_CPR] = ""
        session[Constants.SESSION_PATIENT_ID] = ""
        session[Constants.SESSION_GESTATIONAL_AGE] = null
        session[Constants.SESSION_PHONE] = ""
        session[Constants.SESSION_MOBILE_PHONE] = ""
        session[Constants.SESSION_EMAIL] = ""
        session[Constants.SESSION_FIRST_RELEATIVE] = null
        session[Constants.SEESION_DATARESPONSIBLE] = null
        session[Constants.SESSION_ACCESS_VALIDATED] = "true"
        session[Constants.SESSION_COMMENT_ALL] = ''
        session[Constants.SESSION_COMMENT] = ''
    }

    void setAccessTokens(HttpSession session) {
        session[Constants.SESSION_ACCESS_VALIDATED] = "true"
    }

    void clearAccessTokens(HttpSession session) {
        session[Constants.SESSION_ACCESS_VALIDATED] = "false"
    }

    boolean hasPermission(String permission) {
        SpringSecurityUtils.ifAllGranted(permission)
    }
}
