package org.opentele.server.provider.model

import grails.buildtestdata.mixin.Build
import org.apache.commons.logging.Log
import org.opentele.server.model.Clinician
import org.opentele.server.model.Conference
import org.opentele.server.model.ConferenceBloodPressureMeasurementDraft
import org.opentele.server.model.ConferenceLungFunctionMeasurementDraft
import org.opentele.server.model.ConferenceSaturationMeasurementDraft
import org.opentele.server.model.Patient
import org.opentele.server.model.PendingConference
import org.springframework.security.authentication.BadCredentialsException
import spock.lang.Specification

import grails.test.mixin.*

@TestFor(ConferenceController)
@Build([PendingConference, Patient, Clinician, Conference, ConferenceLungFunctionMeasurementDraft, ConferenceBloodPressureMeasurementDraft, ConferenceSaturationMeasurementDraft])
class ConferenceControllerSpec extends Specification {

    // Interface of VideoConferenceService (since we don't necessarily have access to VideoConferenceService
    // when testing
    private static interface TestVideoConferenceService {
        boolean userIsAlreadyPresentInOwnRoom(String userName, String password)
        String initializeCallAndCreateClientParameters(String userName, String password)
        String linkEndpoint(String userName, String password, String endpointId)
        void joinConference(String userName, String password)
        void endConference(String userName, String password)
    }

    def videoConferenceService = Mock(TestVideoConferenceService)

    Clinician clinician
    Patient patient
    Patient patientInContext

    void setup() {
        controller.videoConferenceService = videoConferenceService

        clinician = Clinician.build(videoUser: 'user', videoPassword: 'pass')
        controller.metaClass.currentClinician = { clinician }
        controller.metaClass.getAsyncTimeoutMillis = { 6 * 60 * 1000 }
        controller.log = Mock(Log)

        patientInContext = Patient.build()
        session.patientId = patientInContext.id
    }

    def 'complains if videoConferenceService is not present'() {
        setup:
        controller.videoConferenceService = null

        when:
        controller.show()

        then:
        response.redirectUrl.endsWith('noVideo')
    }

    def 'complains if user is not authorized for video use'() {
        setup:
        videoConferenceService.userIsAlreadyPresentInOwnRoom('user', 'pass') >> { throw new BadCredentialsException(null) }

        when:
        controller.show()

        then:
        response.redirectUrl.endsWith('unauthorized')
    }

    def 'shows unhelpful error page when unknown video communication exception occurs'() {
        setup:
        videoConferenceService.userIsAlreadyPresentInOwnRoom('user', 'pass') >> { throw new UnknownVideoProblem() }

        when:
        controller.show()

        then:
        response.redirectUrl.endsWith('unknownVideoProblem')
    }

    def 'shows normal view if user is not already present in meeting room and no PendingConference objects exist for clinician'() {
        setup:
        videoConferenceService.userIsAlreadyPresentInOwnRoom('user', 'pass') >> false

        when:
        controller.show()

        then:
        response.redirectUrl == null
    }

    def 'finds list of unfinished video conferences for normal view'() {
        setup:
        videoConferenceService.userIsAlreadyPresentInOwnRoom('user', 'pass') >> false
        def conference1 = new Conference(patient: patientInContext, clinician: clinician)
        def conference2 = new Conference(patient: patientInContext, clinician: Clinician.build())
        def conferenceOnOtherPatient = new Conference(patient: Patient.build(), clinician: clinician)
        [conference1, conference2, conferenceOnOtherPatient]*.save(failOnError: true)

        when:
        def model = controller.show()

        then:
        model.unfinishedConferences == [conference1, conference2]
        model.clinician == clinician
    }

    def 'finds list of unfinished video conferences for conferenceEnded view'() {
        setup:
        videoConferenceService.userIsAlreadyPresentInOwnRoom('user', 'pass') >> false
        def conference1 = new Conference(patient: patientInContext, clinician: clinician)
        def conference2 = new Conference(patient: patientInContext, clinician: Clinician.build())
        def conferenceOnOtherPatient = new Conference(patient: Patient.build(), clinician: clinician)
        [conference1, conference2, conferenceOnOtherPatient]*.save(failOnError: true)

        when:
        def model = controller.conferenceEnded()

        then:
        model.unfinishedConferences == [conference1, conference2]
        model.clinician == clinician
    }

    def 'shows active conference page if user is already present in meeting room and the current PendingConference object belongs to current patient'() {
        setup:
        PendingConference.build(clinician: clinician, patient: patientInContext, roomKey: 'roomKey')
        videoConferenceService.userIsAlreadyPresentInOwnRoom('user', 'pass') >> true

        when:
        controller.show()

        then:
        response.redirectUrl.endsWith('activeConference')
    }

    def 'shows informative page if current PendingConference belongs to another patient'() {
        setup:
        PendingConference.build(clinician: clinician, roomKey: 'roomKey')

        when:
        controller.show()

        then:
        response.redirectUrl.endsWith('conferenceActiveWithOtherPatient')
    }

    def 'shows informative page if user is already present in meeting room but no PendingConference belongs to current patient'() {
        setup:
        videoConferenceService.userIsAlreadyPresentInOwnRoom('user', 'pass') >> true

        when:
        controller.show()

        then:
        response.redirectUrl.endsWith('conferenceActiveWithOtherPatient')
    }

    def 'clears all previous PendingConference objects for clinician when initializing new conference call'() {
        setup:
        PendingConference.build(clinician: clinician, patient: patientInContext)
        PendingConference.build(clinician: clinician)
        def pendingConferenceForOtherClinician = PendingConference.build()
        request.method = 'POST'

        when:
        controller.initializeCall()

        then:
        PendingConference.findAll() == [pendingConferenceForOtherClinician]
    }

    def 'clears all previous ConferenceMeasurementDraft objects for patient when initializing new conference call'() {
        setup:
        Conference.build(clinician: clinician, patient: patientInContext)
        Conference.build(patient: patientInContext)
        def conferenceForOtherPatient = Conference.build()
        request.method = 'POST'

        when:
        controller.initializeCall()

        then:
        Conference.findAll().size() == 3
        Conference.findAllByCompleted(false) == [conferenceForOtherPatient]
    }

    def 'initializes and creates client URL if videoConferenceService is present'() {
        setup:
        videoConferenceService.initializeCallAndCreateClientParameters(_,_) >> 'secret parameters'
        request.method = 'POST'

        when:
        controller.initializeCall()

        then:
        flash.clientParameters == 'secret parameters'
        response.redirectUrl.endsWith('startVideoClient')
    }

    def 'can link endpoint, saving the room key in session'() {
        setup:
        videoConferenceService.linkEndpoint('user', 'pass', '123-456') >> 'roomkey'

        when:
        controller.linkEndpoint('123-456')

        then:
        session.roomKey == 'roomkey'
    }

    def 'can join conference, creating an incomplete VideoConference object'() {
        when:
        controller.joinConference()

        then:
        1 * videoConferenceService.joinConference('user', 'pass')
    }

    def 'knows when clinician has not yet joined the conference'() {
        setup:
        videoConferenceService.userIsAlreadyPresentInOwnRoom('user', 'pass') >> false

        when:
        controller.conferenceJoined()

        then:
        !response.json.joined
    }

    def 'knows when clinician has joined the conference'() {
        setup:
        videoConferenceService.userIsAlreadyPresentInOwnRoom('user', 'pass') >> true

        when:
        controller.conferenceJoined()

        then:
        response.json.joined
    }

    def 'can finish setting up the conference, thus creating a PendingConference and VideoConference object'() {
        when:
        session.roomKey = 'roomkey'
        controller.finishSettingUpConference()

        then:
        PendingConference.findByPatient(patientInContext).roomKey == 'roomkey'
        PendingConference.findByPatient(patientInContext).clinician == clinician

        def conference = Conference.findByPatient(patientInContext)
        conference.clinician == clinician
        !conference.completed

        response.redirectUrl.endsWith('activeConference')
    }

    def 'finds list of unfinished video conferences for active conference page'() {
        setup:
        def conference1 = new Conference(patient: patientInContext, clinician: clinician)
        def conference2 = new Conference(patient: patientInContext, clinician: Clinician.build())
        def conferenceOnOtherPatient = new Conference(patient: Patient.build(), clinician: clinician)
        [conference1, conference2, conferenceOnOtherPatient]*.save(failOnError: true)

        when:
        def model = controller.activeConference()

        then:
        model.unfinishedConferences == [conference1, conference2]
        model.clinician == clinician
    }

    def 'does not list recently created conference in normal view'() {
        setup:
        def conference1 = new Conference(patient: patientInContext, clinician: clinician)
        def conference2 = new Conference(patient: patientInContext, clinician: Clinician.build())
        def conferenceOnOtherPatient = new Conference(patient: Patient.build(), clinician: clinician)
        [conference1, conference2, conferenceOnOtherPatient]*.save(failOnError: true)
        flash.conferenceToEdit = conference2.id

        when:
        def model = controller.activeConference()

        then:
        model.unfinishedConferences == [conference1]
        model.clinician == clinician
    }

    def 'can close video conference'() {
        setup:
        PendingConference.build(patient: patientInContext, roomKey: 'roomKey')

        when:
        controller.endConference()

        then:
        PendingConference.findByClinician(clinician) == null
        1 * videoConferenceService.endConference('user', 'pass')
        response.redirectUrl.endsWith('conferenceEnded')
    }

    private void setPatientAsUser() {
        patient = Patient.build()
        controller.metaClass.currentPatient = { -> patient }
    }
}

class UnknownVideoProblem extends org.springframework.security.core.AuthenticationException {
    UnknownVideoProblem() {
        super("Unknown video problem")
    }
}
