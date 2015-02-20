package org.opentele.taglib

import grails.buildtestdata.mixin.Build
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import org.opentele.server.core.QuestionnaireService
import org.opentele.server.provider.ClinicianMessageService
import org.opentele.server.provider.ClinicianService
import org.opentele.server.model.*
import org.opentele.server.core.model.types.NoteType
import org.opentele.server.provider.questionnaire.QuestionnaireProviderService
import spock.lang.Specification
import spock.lang.Unroll

@Build([PatientOverview, Message, NextOfKinPerson, Clinician, User])
@TestFor(CompletedQuestionnaireTagLib)
class CompletedQuestionnaireTagLibSpec extends Specification {
    def patientOverview, clinician

    def setup() {
        patientOverview = PatientOverview.build(cpr:"0000000000")
        patientOverview.metaClass.patientId = 10L
        clinician = Clinician.build()

        tagLib.clinicianService = Mock(ClinicianService)
        tagLib.clinicianService.currentClinician >> clinician

        QuestionnaireService questionnaireService = Mock(QuestionnaireService)
        tagLib.questionnaireService = questionnaireService

        SpringSecurityService springSecurityService = Mock(SpringSecurityService)
        springSecurityService.currentUser >> clinician.user

        tagLib.springSecurityService = springSecurityService
        tagLib.clinicianMessageService = Mock(ClinicianMessageService)
        tagLib.metaClass.otformat = Mock(FormattingTagLib)
        tagLib.otformat.formatCPR(_, _) >> '1234567890'
    }

    @Unroll
    def "Patient notes are shown with the correct icon"() {

        when:
        def patientNote = new PatientNote(type: type, reminderDate: reminderDate, seenBy: [])
        def output = tagLib.renderOverviewForPatient(patientOverview: patientOverview, patientNotes: [patientNote], null)

        then:
        output.contains icon

        where:
        type                | reminderDate    | icon
        NoteType.NORMAL     | new Date()-1    | "note_reminder_green.png"
        NoteType.NORMAL     | new Date()+1    | "note.png"
        NoteType.NORMAL     | null            | "note.png"
        NoteType.IMPORTANT  | new Date()+1    | "note.png"
        NoteType.IMPORTANT  | new Date()-1    | "note_reminder_red.png"
        NoteType.IMPORTANT  | null            | "note_important.png"
    }

    @Unroll
    def "Patient notes are only visible until seen"() {
        when:

        def patientNotes = seenByClinician ? [] : [new PatientNote(type: type, reminderDate: reminderDate)]
        def output = tagLib.renderOverviewForPatient(patientOverview: patientOverview, patientNotes: patientNotes, messagingEnabled: false, null)

        then:
        output.contains icon

        where:
        type                | reminderDate | seenByClinician    | icon
        NoteType.NORMAL     | new Date()   | false              | "note_reminder_green.png"
        NoteType.IMPORTANT  | new Date()   | false              | "note_reminder_red.png"
        NoteType.IMPORTANT  | null         | false              | "note_important.png"
        NoteType.NORMAL     | new Date()   | true               | "note.png"
        NoteType.IMPORTANT  | new Date()   | true               | "note.png"
        NoteType.IMPORTANT  | null         | true               | "note.png"

    }

    @Unroll
    def "Patient can be messaged if enabled on the patientgroup otherwise not"() {
        when:
        def output = tagLib.renderOverviewForPatient(patientOverview: patientOverview, patientNotes: [], messagingEnabled: canMessage, null)

        then:
        output.contains iconInbox
        output.contains iconOutbox

        where:
        canMessage | iconInbox          | iconOutbox
        false      | 'inbox-dimmed.png' | 'outbox-dimmed.png'
        true       | 'inbox.png'        | 'outbox.png'
    }


}
