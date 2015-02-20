package org.opentele.server.provider.model

import grails.test.spock.IntegrationSpec
import org.opentele.server.core.model.types.NoteType
import org.opentele.server.core.model.types.PatientState
import org.opentele.server.core.model.types.Severity
import org.opentele.server.model.Clinician
import org.opentele.server.model.Message
import org.opentele.server.model.Patient
import org.opentele.server.model.PatientGroup
import org.opentele.server.model.PatientOverview


class PatientOverviewServiceIntegrationSpec extends IntegrationSpec {

    def patientOverviewService
    def patientOverviewMaintenanceService
    def grailsApplication

    def cleanup() {
        grailsApplication.config.patientoverview.use.simple.sort = false //Reset to default value to avoid test pollution
    }


    def 'PatientOverviews are primarily sorted by questionnaire severity....'() {

        setup:
        PatientGroup clinicianPatientGroup = PatientGroup.build()

        Clinician clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: clinicianPatientGroup)
        clinician.save(failOnError: true)


        def withRedAlarm = createPatientOverviewWithSeverity(Severity.RED, clinicianPatientGroup)
        def withYellowAlarm = createPatientOverviewWithSeverity(Severity.YELLOW, clinicianPatientGroup)
        def withBlueAlarm = createPatientOverviewWithSeverity(Severity.BLUE, clinicianPatientGroup)
        def withOrangeAlarm = createPatientOverviewWithSeverity(Severity.ORANGE, clinicianPatientGroup)
        def withGreenAlarm = createPatientOverviewWithSeverity(Severity.GREEN, clinicianPatientGroup)

        when:
        grailsApplication.config.patientoverview.use.simple.sort = true
        def patientOverviews = patientOverviewService.getPatientsForClinicianOverview(clinician)

        then:
        patientOverviews == [withRedAlarm, withYellowAlarm, withBlueAlarm, withOrangeAlarm,withGreenAlarm]

    }

    def '... are secondarily sorted sorted by number of unread messages from patient...'() {
        PatientGroup clinicianPatientGroup = PatientGroup.build()

        Clinician clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: clinicianPatientGroup)
        clinician.save(failOnError: true)

        def withOneUnreadMessage = createPatientOverviewWithNumberOfUnreadMessagesFromPatient(1, clinicianPatientGroup)
        def withTwoUnreadMessages = createPatientOverviewWithNumberOfUnreadMessagesFromPatient(2, clinicianPatientGroup)
        def withThreeUnreadMessages = createPatientOverviewWithNumberOfUnreadMessagesFromPatient(3, clinicianPatientGroup)
        def withFourUnreadMessages = createPatientOverviewWithNumberOfUnreadMessagesFromPatient(4, clinicianPatientGroup)
        def withFiveUnreadMessages = createPatientOverviewWithNumberOfUnreadMessagesFromPatient(5, clinicianPatientGroup)

        when:
        grailsApplication.config.patientoverview.use.simple.sort = true
        def patientOverviews = patientOverviewService.getPatientsForClinicianOverview(clinician)

        then:
        patientOverviews == [withOneUnreadMessage, withTwoUnreadMessages, withThreeUnreadMessages, withFourUnreadMessages, withFiveUnreadMessages]

    }

    def '... are then thirdly sorted by number of unread messages to patient....'() {
        PatientGroup clinicianPatientGroup = PatientGroup.build()

        Clinician clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: clinicianPatientGroup)
        clinician.save(failOnError: true)

        def withOneUnreadMessage = createPatientOverviewWithNumberOfUnreadMessagesToPatient(1, clinicianPatientGroup)
        def withTwoUnreadMessages = createPatientOverviewWithNumberOfUnreadMessagesToPatient(2, clinicianPatientGroup)
        def withThreeUnreadMessages = createPatientOverviewWithNumberOfUnreadMessagesToPatient(3, clinicianPatientGroup)
        def withFourUnreadMessages = createPatientOverviewWithNumberOfUnreadMessagesToPatient(4, clinicianPatientGroup)
        def withFiveUnreadMessages = createPatientOverviewWithNumberOfUnreadMessagesToPatient(5, clinicianPatientGroup)

        when:
        grailsApplication.config.patientoverview.use.simple.sort = true
        def patientOverviews = patientOverviewService.getPatientsForClinicianOverview(clinician)

        then:
        patientOverviews == [withOneUnreadMessage, withTwoUnreadMessages, withThreeUnreadMessages, withFourUnreadMessages, withFiveUnreadMessages]
    }

    def '... and are finally PatientOverviews are sorted by name'() {

        PatientGroup clinicianPatientGroup = PatientGroup.build()

        Clinician clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: clinicianPatientGroup)
        clinician.save(failOnError: true)

        def abe = createPatientOverviewForPatientWithName('Abe', clinicianPatientGroup)
        def brown = createPatientOverviewForPatientWithName('Brown', clinicianPatientGroup)
        def cecil = createPatientOverviewForPatientWithName('Cecil', clinicianPatientGroup)
        def dwight = createPatientOverviewForPatientWithName('Dwight', clinicianPatientGroup)
        def eugene = createPatientOverviewForPatientWithName('Eugene', clinicianPatientGroup)

        when:
        grailsApplication.config.patientoverview.use.simple.sort = true
        def patientOverviews = patientOverviewService.getPatientsForClinicianOverview(clinician)

        then:
        patientOverviews == [abe, brown, cecil, dwight, eugene]

    }

    def 'after setting the simple sort flag PatientOverviews are still returned in questionnaire severity order'() {
        setup:
        PatientGroup clinicianPatientGroup = PatientGroup.build()

        Clinician clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: clinicianPatientGroup)
        clinician.save(failOnError: true)


        def withRedAlarm = createPatientOverviewWithSeverity(Severity.RED, clinicianPatientGroup)
        def withYellowAlarm = createPatientOverviewWithSeverity(Severity.YELLOW, clinicianPatientGroup)
        def withBlueAlarm = createPatientOverviewWithSeverity(Severity.BLUE, clinicianPatientGroup)
        def withOrangeAlarm = createPatientOverviewWithSeverity(Severity.ORANGE, clinicianPatientGroup)
        def withGreenAlarm = createPatientOverviewWithSeverity(Severity.GREEN, clinicianPatientGroup)

        when:
        grailsApplication.config.patientoverview.use.simple.sort = true
        def patientOverviews = patientOverviewService.getPatientsForClinicianOverview(clinician)

        then:
        patientOverviews == [withRedAlarm, withYellowAlarm, withBlueAlarm, withOrangeAlarm,withGreenAlarm]
    }



    def 'PatientOverviews are primarily sorted by questionnaire severity by getPatientsForClinicianOverviewInPatientGroup....'() {

        setup:
        PatientGroup clinicianPatientGroup = PatientGroup.build()

        Clinician clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: clinicianPatientGroup)
        clinician.save(failOnError: true)


        def withRedAlarm = createPatientOverviewWithSeverity(Severity.RED, clinicianPatientGroup)
        def withYellowAlarm = createPatientOverviewWithSeverity(Severity.YELLOW, clinicianPatientGroup)
        def withBlueAlarm = createPatientOverviewWithSeverity(Severity.BLUE, clinicianPatientGroup)
        def withOrangeAlarm = createPatientOverviewWithSeverity(Severity.ORANGE, clinicianPatientGroup)
        def withGreenAlarm = createPatientOverviewWithSeverity(Severity.GREEN, clinicianPatientGroup)

        when:
        grailsApplication.config.patientoverview.use.simple.sort = true
        def patientOverviews = patientOverviewService.getPatientsForClinicianOverviewInPatientGroup(clinician, clinicianPatientGroup)

        then:
        patientOverviews == [withRedAlarm, withYellowAlarm, withBlueAlarm, withOrangeAlarm,withGreenAlarm]

    }

    def '... are secondarily sorted sorted by number of unread messages from patient by getPatientsForClinicianOverviewInPatientGroup...'() {
        PatientGroup clinicianPatientGroup = PatientGroup.build()

        Clinician clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: clinicianPatientGroup)
        clinician.save(failOnError: true)

        def withOneUnreadMessage = createPatientOverviewWithNumberOfUnreadMessagesFromPatient(1, clinicianPatientGroup)
        def withTwoUnreadMessages = createPatientOverviewWithNumberOfUnreadMessagesFromPatient(2, clinicianPatientGroup)
        def withThreeUnreadMessages = createPatientOverviewWithNumberOfUnreadMessagesFromPatient(3, clinicianPatientGroup)
        def withFourUnreadMessages = createPatientOverviewWithNumberOfUnreadMessagesFromPatient(4, clinicianPatientGroup)
        def withFiveUnreadMessages = createPatientOverviewWithNumberOfUnreadMessagesFromPatient(5, clinicianPatientGroup)

        when:
        grailsApplication.config.patientoverview.use.simple.sort = true
        def patientOverviews = patientOverviewService.getPatientsForClinicianOverviewInPatientGroup(clinician, clinicianPatientGroup)

        then:
        patientOverviews == [withOneUnreadMessage, withTwoUnreadMessages, withThreeUnreadMessages, withFourUnreadMessages, withFiveUnreadMessages]

    }

    def '... are then thirdly sorted by number of unread messages to patient by getPatientsForClinicianOverviewInPatientGroup....'() {
        PatientGroup clinicianPatientGroup = PatientGroup.build()

        Clinician clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: clinicianPatientGroup)
        clinician.save(failOnError: true)

        def withOneUnreadMessage = createPatientOverviewWithNumberOfUnreadMessagesToPatient(1, clinicianPatientGroup)
        def withTwoUnreadMessages = createPatientOverviewWithNumberOfUnreadMessagesToPatient(2, clinicianPatientGroup)
        def withThreeUnreadMessages = createPatientOverviewWithNumberOfUnreadMessagesToPatient(3, clinicianPatientGroup)
        def withFourUnreadMessages = createPatientOverviewWithNumberOfUnreadMessagesToPatient(4, clinicianPatientGroup)
        def withFiveUnreadMessages = createPatientOverviewWithNumberOfUnreadMessagesToPatient(5, clinicianPatientGroup)

        when:
        grailsApplication.config.patientoverview.use.simple.sort = true
        def patientOverviews = patientOverviewService.getPatientsForClinicianOverviewInPatientGroup(clinician, clinicianPatientGroup)

        then:
        patientOverviews == [withOneUnreadMessage, withTwoUnreadMessages, withThreeUnreadMessages, withFourUnreadMessages, withFiveUnreadMessages]
    }

    def '... and are finally PatientOverviews are sorted by name by getPatientsForClinicianOverviewInPatientGroup'() {

        PatientGroup clinicianPatientGroup = PatientGroup.build()

        Clinician clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: clinicianPatientGroup)
        clinician.save(failOnError: true)

        def abe = createPatientOverviewForPatientWithName('Abe', clinicianPatientGroup)
        def brown = createPatientOverviewForPatientWithName('Brown', clinicianPatientGroup)
        def cecil = createPatientOverviewForPatientWithName('Cecil', clinicianPatientGroup)
        def dwight = createPatientOverviewForPatientWithName('Dwight', clinicianPatientGroup)
        def eugene = createPatientOverviewForPatientWithName('Eugene', clinicianPatientGroup)

        when:
        grailsApplication.config.patientoverview.use.simple.sort = true
        def patientOverviews = patientOverviewService.getPatientsForClinicianOverviewInPatientGroup(clinician, clinicianPatientGroup)

        then:
        patientOverviews == [abe, brown, cecil, dwight, eugene]

    }

    def 'after setting the simple sort flag PatientOverviews are still returned in questionnaire severity order from getPatientsForClinicianOverviewInPatientGroup'() {
        setup:
        PatientGroup clinicianPatientGroup = PatientGroup.build()

        Clinician clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: clinicianPatientGroup)
        clinician.save(failOnError: true)


        def withRedAlarm = createPatientOverviewWithSeverity(Severity.RED, clinicianPatientGroup)
        def withYellowAlarm = createPatientOverviewWithSeverity(Severity.YELLOW, clinicianPatientGroup)
        def withBlueAlarm = createPatientOverviewWithSeverity(Severity.BLUE, clinicianPatientGroup)
        def withOrangeAlarm = createPatientOverviewWithSeverity(Severity.ORANGE, clinicianPatientGroup)
        def withGreenAlarm = createPatientOverviewWithSeverity(Severity.GREEN, clinicianPatientGroup)

        when:
        grailsApplication.config.patientoverview.use.simple.sort = true
        def patientOverviews = patientOverviewService.getPatientsForClinicianOverviewInPatientGroup(clinician, clinicianPatientGroup)

        then:
        patientOverviews == [withRedAlarm, withYellowAlarm, withBlueAlarm, withOrangeAlarm,withGreenAlarm]
    }


    def 'can find patients for clinician overview'() {
        setup:
        PatientGroup clinicianPatientGroup = PatientGroup.build()
        PatientGroup otherPatientGroup = PatientGroup.build()

        Clinician clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: clinicianPatientGroup)
        clinician.save(failOnError: true)

        Patient activePatientInPatientGroup = createPatientWithMessage(PatientState.ACTIVE, clinicianPatientGroup)
        createPatientWithMessage(PatientState.DECEASED, clinicianPatientGroup)
        createPatientWithMessage(PatientState.ACTIVE, otherPatientGroup)
        createPatientWithMessage(PatientState.DECEASED, otherPatientGroup)

        when:
        def patientOverviews = patientOverviewService.getPatientsForClinicianOverview(clinician)

        then:
        patientOverviews*.patient == [activePatientInPatientGroup]
    }

    def 'includes patients with reminders not seen by clinician'() {
        setup:
        PatientGroup clinicianPatientGroup = PatientGroup.build()
        PatientGroup otherPatientGroup = PatientGroup.build()

        Clinician clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: clinicianPatientGroup)
        clinician.save(failOnError: true)

        Patient activePatientInPatientGroupWithoutReminder = createPatient(PatientState.ACTIVE, clinicianPatientGroup)
        Patient activePatientInPatientGroupWithUnseenReminder = createPatient(PatientState.ACTIVE, clinicianPatientGroup)
        Patient activePatientInPatientGroupWithSeenReminder = createPatient(PatientState.ACTIVE, clinicianPatientGroup)
        Patient inactivePatientInPatientGroup = createPatient(PatientState.DECEASED, clinicianPatientGroup)
        Patient activePatientInOtherPatientGroupWithReminder = createPatient(PatientState.ACTIVE, otherPatientGroup)

        activePatientInPatientGroupWithoutReminder.addToNotes(type: NoteType.IMPORTANT, note: 'Note', reminderDate: new Date()+1, seenBy: [])
        activePatientInPatientGroupWithUnseenReminder.addToNotes(type: NoteType.NORMAL, note: 'Note', reminderDate: new Date()-1, seenBy: [])
        activePatientInPatientGroupWithSeenReminder.addToNotes(type: NoteType.NORMAL, note: 'Note', reminderDate: new Date()-1, seenBy: [clinician])
        activePatientInOtherPatientGroupWithReminder.addToNotes(type: NoteType.IMPORTANT, note: 'Note', reminderDate: new Date()-1, seenBy: [])

        when:
        def patientOverviews = patientOverviewService.getPatientsForClinicianOverview(clinician)

        then:
        patientOverviews*.patient == [activePatientInPatientGroupWithUnseenReminder]
    }

    def 'can find patients for specific patient group for clinician overview'() {
        setup:
        PatientGroup group1 = PatientGroup.build()
        PatientGroup group2 = PatientGroup.build()
        PatientGroup group3 = PatientGroup.build()

        Clinician clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: group1)
        clinician.addToClinician2PatientGroups(patientGroup: group2)
        clinician.addToClinician2PatientGroups(patientGroup: group3)
        clinician.save(failOnError: true)

        Patient activePatientInPatientGroup1 = createPatientWithMessage(PatientState.ACTIVE, group1)
        createPatientWithMessage(PatientState.DECEASED, group1)
        Patient activePatientInPatientGroup2 = createPatientWithMessage(PatientState.ACTIVE, group2)
        createPatientWithMessage(PatientState.DECEASED, group2)

        when:
        def activePatientOverviewsInGroup1 = patientOverviewService.getPatientsForClinicianOverviewInPatientGroup(clinician, group1)
        def activePatientOverviewsInGroup2 = patientOverviewService.getPatientsForClinicianOverviewInPatientGroup(clinician, group2)
        def activePatientOverviewsInGroup3 = patientOverviewService.getPatientsForClinicianOverviewInPatientGroup(clinician, group3)

        then:
        activePatientOverviewsInGroup1*.patient == [activePatientInPatientGroup1]
        activePatientOverviewsInGroup2*.patient == [activePatientInPatientGroup2]
        activePatientOverviewsInGroup3*.patient == []
    }

    def 'includes patients with reminders not seen by clinician when searching in specific patient group'() {
        setup:
        PatientGroup group1 = PatientGroup.build()
        PatientGroup group2 = PatientGroup.build()
        PatientGroup group3 = PatientGroup.build()

        Clinician clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: group1)
        clinician.addToClinician2PatientGroups(patientGroup: group2)
        clinician.addToClinician2PatientGroups(patientGroup: group3)
        clinician.save(failOnError: true)

        Patient activePatientInPatientGroup1WithoutReminder = createPatient(PatientState.ACTIVE, group1)
        Patient activePatientInPatientGroup1WithNormalPatientNote = createPatient(PatientState.ACTIVE, group1)
        Patient activePatientInPatientGroup1WithUnseenReminder = createPatient(PatientState.ACTIVE, group1)
        Patient activePatientInPatientGroup1WithSeenReminder = createPatient(PatientState.ACTIVE, group1)
        Patient inactivePatientInPatientGroup2WithReminder = createPatient(PatientState.DECEASED, group1)
        Patient activePatientInPatientGroup2WithUnseenReminder = createPatient(PatientState.ACTIVE, group2)

        activePatientInPatientGroup1WithNormalPatientNote.addToNotes(type: NoteType.IMPORTANT, note: 'Note', reminderDate: new Date()+1, seenBy: [])
        activePatientInPatientGroup1WithUnseenReminder.addToNotes(type: NoteType.IMPORTANT, note: 'Note', reminderDate: new Date()-1, seenBy: [])
        activePatientInPatientGroup1WithSeenReminder.addToNotes(type: NoteType.NORMAL, note: 'Note', reminderDate: new Date()-1, seenBy: [clinician])
        inactivePatientInPatientGroup2WithReminder.addToNotes(type: NoteType.IMPORTANT, note: 'Note', reminderDate: new Date()-1, seenBy: [])
        activePatientInPatientGroup2WithUnseenReminder.addToNotes(type: NoteType.IMPORTANT, note: 'Note', reminderDate: new Date()-1, seenBy: [])

        when:
        def activePatientOverviewsInGroup1 = patientOverviewService.getPatientsForClinicianOverviewInPatientGroup(clinician, group1)
        def activePatientOverviewsInGroup2 = patientOverviewService.getPatientsForClinicianOverviewInPatientGroup(clinician, group2)
        def activePatientOverviewsInGroup3 = patientOverviewService.getPatientsForClinicianOverviewInPatientGroup(clinician, group3)

        then:
        activePatientOverviewsInGroup1*.patient == [activePatientInPatientGroup1WithUnseenReminder]
        activePatientOverviewsInGroup2*.patient == [activePatientInPatientGroup2WithUnseenReminder]
        activePatientOverviewsInGroup3*.patient == []
    }

    def 'complains if finding patients for patient group not assigned to clinician'() {
        setup:
        PatientGroup clinicianGroup = PatientGroup.build()
        PatientGroup otherGroup = PatientGroup.build()

        Clinician clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: clinicianGroup)

        when:
        patientOverviewService.getPatientsForClinicianOverviewInPatientGroup(clinician, otherGroup)

        then:
        thrown(IllegalArgumentException)
    }

    def 'can identify patients for which messaging is enabled'() {
        setup:
        PatientGroup patientGroupWithMessaging = PatientGroup.build(disableMessaging: false)
        PatientGroup patientGroupWithoutMessaging = PatientGroup.build(disableMessaging: true)

        Clinician clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: patientGroupWithMessaging)
        clinician.addToClinician2PatientGroups(patientGroup: patientGroupWithoutMessaging)
        clinician.save(failOnError: true)

        Patient patient1WithMessagingEnabled = createPatientWithMessage(PatientState.ACTIVE, patientGroupWithMessaging)
        Patient patient2WithMessagingEnabled = createPatientWithMessage(PatientState.ACTIVE, patientGroupWithMessaging)
        Patient patient1WithMessagingDisabled = createPatientWithMessage(PatientState.ACTIVE, patientGroupWithoutMessaging)
        Patient patient2WithMessagingDisabled = createPatientWithMessage(PatientState.ACTIVE, patientGroupWithoutMessaging)

        when:
        def patientIds = patientOverviewService.getIdsOfPatientsWithMessagingEnabled(clinician, [patient1WithMessagingEnabled, patient2WithMessagingEnabled, patient1WithMessagingDisabled, patient2WithMessagingDisabled]*.patientOverview)

        then:
        patientIds.size() == 2
        patientIds.contains(patient1WithMessagingEnabled.id)
        patientIds.contains(patient2WithMessagingEnabled.id)
    }

    def 'can identify patients for which alarms triggered by unread messages *to* the patient is disabled'() {
        setup:
        PatientGroup patientGroupWithMessaging = PatientGroup.build(disableMessaging: false)
        PatientGroup patientGroupWithoutMessaging = PatientGroup.build(disableMessaging: true)

        Clinician clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: patientGroupWithMessaging)
        clinician.addToClinician2PatientGroups(patientGroup: patientGroupWithoutMessaging)
        clinician.save(failOnError: true)

        Patient patient1WithNoAlarmIfUnreadMessages = createPatientWithMessageAndNoAlarmIfUnreadMessages(PatientState.ACTIVE, patientGroupWithoutMessaging, true)
        Patient patient2WithNoAlarmIfUnreadMessages = createPatientWithMessageAndNoAlarmIfUnreadMessages(PatientState.ACTIVE, patientGroupWithMessaging, true)
        Patient patient3WithNoAlarmIfUnreadMessages = createPatientWithMessageAndNoAlarmIfUnreadMessages(PatientState.ACTIVE, patientGroupWithoutMessaging, false)
        Patient patient4WithNoAlarmIfUnreadMessages = createPatientWithMessageAndNoAlarmIfUnreadMessages(PatientState.ACTIVE, patientGroupWithMessaging, false)

        when:
        def patientIds = patientOverviewService.getIdsOfPatientsWithAlarmIfUnreadMessagesDisabled(clinician, [patient1WithNoAlarmIfUnreadMessages, patient2WithNoAlarmIfUnreadMessages, patient3WithNoAlarmIfUnreadMessages, patient4WithNoAlarmIfUnreadMessages]*.patientOverview)

        then:
        patientIds.size() == 2
        patientIds.contains(patient1WithNoAlarmIfUnreadMessages.id)
        patientIds.contains(patient2WithNoAlarmIfUnreadMessages.id)
    }

    def 'can find all patient notes not seen by clinician for a list of patients'() {
        setup:
        PatientGroup patientGroup = PatientGroup.build()

        Clinician clinician = Clinician.build()
        clinician.addToClinician2PatientGroups(patientGroup: patientGroup)
        clinician.save(failOnError: true)

        Patient patient1 = createPatient(PatientState.ACTIVE, patientGroup)
        Patient patient2 = createPatient(PatientState.ACTIVE, patientGroup)
        Patient patient3 = createPatient(PatientState.ACTIVE, patientGroup)
        Patient patient4 = createPatient(PatientState.ACTIVE, patientGroup)
        Patient patient5 = createPatient(PatientState.ACTIVE, patientGroup)

        patient1.addToNotes(type: NoteType.IMPORTANT, note: 'Note 1', reminderDate: new Date()+1, seenBy: [])
        patient1.addToNotes(type: NoteType.IMPORTANT, note: 'Note 2', reminderDate: new Date()+1, seenBy: [])
        patient1.addToNotes(type: NoteType.NORMAL, note: 'Note 3', reminderDate: new Date()-1, seenBy: [])
        patient2.addToNotes(type: NoteType.IMPORTANT, note: 'Note 4', reminderDate: new Date()-1, seenBy: [])
        patient3.addToNotes(type: NoteType.IMPORTANT, note: 'Note 5', reminderDate: new Date()+1, seenBy: [])
        patient5.addToNotes(type: NoteType.IMPORTANT, note: 'Irrelevant note', reminderDate: new Date()+1, seenBy: [])

        when:
        def patientNotes = patientOverviewService.fetchUnseenNotesForPatients(clinician, [patient1, patient2, patient3, patient4]*.patientOverview)

        then:
        patientNotes[patient1.id]*.note.toSet() == ['Note 1', 'Note 2', 'Note 3'].toSet()
        patientNotes[patient2.id]*.note.toSet() == ['Note 4'].toSet()
        patientNotes[patient3.id]*.note.toSet() == ['Note 5'].toSet()
        patientNotes[patient4.id]*.note.toSet() == [].toSet()
    }

    private Patient createPatientWithMessage(PatientState state, PatientGroup group) {
        createPatientWithMessageAndNoAlarmIfUnreadMessages(state, group, false)
    }

    private Patient createPatientWithMessageAndNoAlarmIfUnreadMessages(PatientState state, PatientGroup group, boolean noAlarmIfUnreadMessages) {
        Patient patient = Patient.build(state: state, blueAlarmQuestionnaireIDs: [], noAlarmIfUnreadMessagesToPatient: noAlarmIfUnreadMessages)
        patient.addToPatient2PatientGroups(patientGroup: group)

        // Create unread note, in order to make this patient important enough to show in patient overview
        Message.build(patient: patient)

        patientOverviewMaintenanceService.createOverviewFor(patient)

        patient
    }

    def createPatientOverviewWithSeverity(Severity severity, PatientGroup patientGroup) {
        def patient = Patient.build(state: PatientState.ACTIVE, blueAlarmQuestionnaireIDs: [])
        patient.addToPatient2PatientGroups(patientGroup: patientGroup)

        PatientOverview.build(patient: patient, questionnaireSeverity: severity, questionnaireSeverityOrdinal: severity.ordinal(), important:true)
    }

    private Patient createPatient(PatientState state, PatientGroup group) {
        Patient patient = Patient.build(state: state, blueAlarmQuestionnaireIDs: [])
        patient.addToPatient2PatientGroups(patientGroup: group)
        patientOverviewMaintenanceService.createOverviewFor(patient)

        patient
    }

    def createPatientOverviewWithNumberOfUnreadMessagesFromPatient(int numberOfUnreadMeassages, PatientGroup patientGroup) {
        def patient = Patient.build(state: PatientState.ACTIVE, blueAlarmQuestionnaireIDs: [])
        patient.addToPatient2PatientGroups(patientGroup: patientGroup)

        PatientOverview.build(patient: patient, numberOfUnreadMessagesFromPatient: numberOfUnreadMeassages, important:true)
    }

    def createPatientOverviewWithNumberOfUnreadMessagesToPatient(int numberOfUnreadMeassages, PatientGroup patientGroup) {
        def patient = Patient.build(state: PatientState.ACTIVE, blueAlarmQuestionnaireIDs: [])
        patient.addToPatient2PatientGroups(patientGroup: patientGroup)

        PatientOverview.build(patient: patient, numberOfUnreadMessagesToPatient: numberOfUnreadMeassages, important:true)
    }

    def createPatientOverviewForPatientWithName(String patientName, PatientGroup patientGroup) {
        def patient = Patient.build(state: PatientState.ACTIVE, blueAlarmQuestionnaireIDs: [])
        patient.addToPatient2PatientGroups(patientGroup: patientGroup)

        PatientOverview.build(patient: patient, name: patientName, important:true)
    }
}
