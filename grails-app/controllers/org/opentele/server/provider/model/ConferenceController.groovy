package org.opentele.server.provider.model

import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

import org.opentele.server.model.Clinician
import org.opentele.server.model.Conference
import org.opentele.server.model.Patient
import org.opentele.server.model.PendingConference
import org.opentele.server.core.model.types.PermissionName
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import dk.silverbullet.kih.api.auditlog.SkipAuditLog

@Secured(PermissionName.NONE)
class ConferenceController {
    def springSecurityService
    def videoConferenceService

    static allowedMethods = [show: 'GET', initializeCall: 'POST', startVideoClient: 'GET']

    @Secured(PermissionName.VIDEO_CALL)
    def show() {
        if (videoConferenceService == null) {
            redirect(action:'noVideo')
            return
        }

        def clinician = currentClinician()
        def patient = patientInContext()

        def pendingConferences = PendingConference.findAllByClinician(clinician)
        def hasConferenceWithThisPatient = pendingConferences.any { it.patient == patient }
        def hasConferenceWithAnotherPatient = pendingConferences.any { it.patient != patient }
        def userIsPresentInOwnRoom
        try {
            userIsPresentInOwnRoom = videoConferenceService.userIsAlreadyPresentInOwnRoom(clinician.videoUser, clinician.videoPassword)
        } catch (BadCredentialsException e) {
            log.error('User not authorized')
            redirect(action:'unauthorized')
            return
        } catch (AuthenticationException e) {
            log.error('Unknown video problem', e)
            redirect(action:'unknownVideoProblem')
            return
        }

        if (hasConferenceWithAnotherPatient || (userIsPresentInOwnRoom && !hasConferenceWithThisPatient)) {
            redirect(action:'conferenceActiveWithOtherPatient')
        } else if (userIsPresentInOwnRoom && hasConferenceWithThisPatient) {
            redirect(action:'activeConference')
        } else {
            [unfinishedConferences: findUnfinishedConferences(), clinician: clinician]
        }
    }

    @Secured(PermissionName.VIDEO_CALL)
    def initializeCall() {
        def clinician = currentClinician()

        deletePendingConferences(clinician)
        completeConferences(patientInContext())

        def clientParameters = videoConferenceService.initializeCallAndCreateClientParameters(clinician.videoUser, clinician.videoPassword)

        flash.clientParameters = clientParameters
        redirect(action:'startVideoClient')
    }

    @Secured(PermissionName.VIDEO_CALL)
    def noVideo() {
    }

    @Secured(PermissionName.VIDEO_CALL)
    def unauthorized() {
    }

    @Secured(PermissionName.VIDEO_CALL)
    def unknownVideoProblem() {
    }

    @Secured(PermissionName.VIDEO_CALL)
    def activeConference() {
        [unfinishedConferences: findUnfinishedConferences(), clinician: currentClinician()]
    }

    @Secured(PermissionName.VIDEO_CALL)
    def conferenceActiveWithOtherPatient() {
    }

    @Secured(PermissionName.VIDEO_CALL)
    def startVideoClient() {
    }

    @Secured(PermissionName.VIDEO_CALL)
    def linkEndpoint(String endpointId) {
        def clinician = currentClinician()

        session.roomKey = videoConferenceService.linkEndpoint(clinician.videoUser, clinician.videoPassword, endpointId)

        render ''
    }

    @Secured(PermissionName.VIDEO_CALL)
    def joinConference() {
        def clinician = currentClinician()

        videoConferenceService.joinConference(clinician.videoUser, clinician.videoPassword)

        render ''
    }

    @Secured(PermissionName.VIDEO_CALL)
    @SkipAuditLog
    def conferenceJoined() {
        def clinician = currentClinician()

        render ([joined: videoConferenceService.userIsAlreadyPresentInOwnRoom(clinician.videoUser, clinician.videoPassword)] as JSON)
    }

    @Secured(PermissionName.VIDEO_CALL)
    def finishSettingUpConference() {
        def clinician = currentClinician()

        def videoConference = new Conference(patient: patientInContext(), clinician: clinician)
        videoConference.save(failOnError: true)
        flash.conferenceToEdit = videoConference.id

        notifyPatientOfConference(clinician, session.roomKey)

        redirect(action: 'activeConference')
    }

    @Secured(PermissionName.VIDEO_CALL)
    def endConference() {
        def clinician = currentClinician()

        // Fetch properties from clinician, otherwise we sometimes get an org.hibernate.StaleObjectStateException
        // at the videoConferenceService.endConference method call, for some reason.
        def username = clinician.videoUser.toString()
        def password = clinician.videoPassword.toString()

        deletePendingConferences(clinician)
        videoConferenceService.endConference(username, password)

        redirect(action:'conferenceEnded')
    }

    @Secured(PermissionName.VIDEO_CALL)
    def conferenceEnded() {
        def clinician = currentClinician()
        [unfinishedConferences: findUnfinishedConferences(), clinician: clinician]
    }

    private findUnfinishedConferences() {
        def result = Conference.findAllByPatientAndCompleted(patientInContext(), false)
        flash.conferenceToEdit ? result.findAll { it.id != flash.conferenceToEdit } : result
    }

    private notifyPatientOfConference(clinician, roomKey) {
        def patient = patientInContext()
        def pendingConference = new PendingConference(clinician: clinician, patient: patient, roomKey: roomKey)
        pendingConference.save(failOnError: true)
    }

    private deletePendingConferences(Clinician clinician) {
        PendingConference.findAllByClinician(clinician).each {
            it.delete()
        }
    }

    private completeConferences(Patient patient) {
        Conference.findAllByPatientAndCompleted(patient, false).each {
            it.completed = true
            it.save()
        }
    }

    private currentClinician() {
        Clinician.findByUser(springSecurityService.currentUser)
    }

    private patientInContext() {
        Patient.get(session.patientId)
    }

    private long getAsyncTimeoutMillis() {
        grailsApplication.config.video.connection.asyncTimeoutMillis
    }
}
