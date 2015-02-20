package org.opentele.server.provider.model

import grails.test.mixin.*
import grails.buildtestdata.mixin.Build
import org.opentele.server.provider.PatientService
import org.opentele.server.model.Patient
import org.opentele.server.model.PatientNote
import org.opentele.server.model.Threshold
import org.opentele.server.provider.PatientNoteService
import org.opentele.server.provider.SessionService
import org.opentele.server.core.model.types.NoteType

@TestFor(PatientNoteController)
@Build([Patient, PatientNote])
class PatientNoteControllerTests {
    def populateValidParams(params) {
        assert params != null
        params['note'] = "foobar"
        params['type'] = NoteType.NORMAL
        return params
    }

    void testIndex() {
        controller.index()

        assert response.redirectedUrl == "/patientNote/list"
    }

    void testCanGiveEmptyListOfPatientNotes() {
        def (patient, patientId) = createPatient()

        def sessionServiceControl = mockFor(SessionService)
        sessionServiceControl.demand.setPatient {session, patientForSession ->
            assert patientForSession == patient
        }
        controller.sessionService = sessionServiceControl.createMock()

        controller.params.id = patientId

        def model = controller.list()

        assert model.patient == patient
        assert model.patientNoteInstanceList == []
        assert model.patientNoteInstanceTotal == 0
    }

    void testGives10PatientNotesOutOf20ForPagination() {
        def (Patient patient, patientId) = createPatient()
        20.times { patient.addToNotes(PatientNote.build(seenBy: [])) }
        patient.save(failOnError: true)

        def sessionServiceControl = mockFor(SessionService)
        sessionServiceControl.demand.setPatient {session, patientForSession ->
            assert patientForSession == patient
        }
        controller.sessionService = sessionServiceControl.createMock()

        def patientServiceControl = mockFor(PatientService, true)
        patientServiceControl.demand.isNoteSeenByUser(10..10) { false }
        controller.patientService = patientServiceControl.createMock()

        def patientNoteServiceControl = mockFor(PatientNoteService, true)
        patientNoteServiceControl.demand.isNoteSeenByAnyUser(10..10) { false }
        controller.patientNoteService = patientNoteServiceControl.createMock()

        controller.params.id = patientId

        def model = controller.list()

        assert model.patient == patient
        assert model.patientNoteInstanceList.size() == 10
        assert model.patientNoteInstanceTotal == 20
    }

    void testWillEnsureHTMLSafeStrings() {

        params.patientId = createPatient()[1]
        params.note = "<script>alert('hej!')</script>"
        params.type = NoteType.NORMAL

        request.method = 'POST'
        controller.save()

        assert !PatientNote.findById(1).note.contains("<")

    }

    void testCreate() {
        def model = controller.create()

        assert model.patientNoteInstance != null
    }

    void testSave() {
        def ret = createPatient()
        controller.params.patientId = ret[1]
        request.method = 'POST'
        controller.save()

        assert model.patientNoteInstance != null
        assert view == '/patientNote/create'

        response.reset()

        populateValidParams(params)
        request.method = 'POST'
        controller.save()

        assert response.redirectedUrl == '/patientNote/show/1'
        assert controller.flash.message != null
        assert PatientNote.count() == 1
    }

    void testUpdate() {
        populateValidParams(params)
        def patientNote = new PatientNote(params)
        def ret = createPatient()
        patientNote.patient = ret[0]
        assert patientNote.save() != null

        // test invalid parameters in update
        params.id = patientNote.id

        params.type = null

        request.method = 'POST'
        controller.update()

        assert view == "/patientNote/edit"
        assert model.patientNoteInstance != null

        patientNote.clearErrors()

        populateValidParams(params)

        request.method = 'POST'
        controller.update()

        assert response.redirectedUrl == "/patientNote/show/$patientNote.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        patientNote.clearErrors()

        populateValidParams(params)
        params.id = patientNote.id
        params.version = -1
        controller.update()

        assert view == "/patientNote/edit"
        assert model.patientNoteInstance != null
        assert model.patientNoteInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        def patientNoteServiceControl = mockFor(PatientNoteService, true)
        patientNoteServiceControl.demand.isNoteSeenByAnyUser {note -> false }


        def (Patient patient, patientId) = createPatient()
        def note1 = PatientNote.build()
        patient.addToNotes(note1)
        def note2 = PatientNote.build()
        patient.addToNotes(note2)
        def note3 = PatientNote.build()
        patient.addToNotes(note3)
        patient.save(failOnError: true)

        controller.patientNoteService = patientNoteServiceControl.createMock()
        controller.params.id = note2.id
        request.method = 'POST'
        controller.delete()

        def notes = PatientNote.findAll()

        assert response.redirectedUrl == "/patientNote/list/${patientId}"
        assert controller.flash.message != null
        assert notes.contains(note1)
        assert !notes.contains(note2)
        assert notes.contains(note3)
    }

    private createPatient() {
        def patient = Patient.build(cpr: '1234567890', thresholds: new ArrayList<Threshold>())
        patient.notes = []
        patient.save(failOnError: true)

        def patientId = patient.id
        [patient, patientId]
    }
}
