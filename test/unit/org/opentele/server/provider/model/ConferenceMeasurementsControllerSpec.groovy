package org.opentele.server.provider.model

import grails.buildtestdata.mixin.Build
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityService
import grails.test.mixin.TestFor
import org.opentele.server.core.model.types.MeasurementTypeName
import org.opentele.server.core.model.types.Unit
import org.opentele.server.model.*
import org.opentele.server.provider.ConferenceService
import org.opentele.server.provider.PatientService
import org.opentele.server.provider.SessionService
import spock.lang.Specification

@TestFor(ConferenceMeasurementsController)
@Build([Conference, ConferenceMeasurementDraft, ConferenceLungFunctionMeasurementDraft, ConferenceWeightMeasurementDraft,
        ConferenceBloodPressureMeasurementDraft, ConferenceSaturationMeasurementDraft, Measurement, MeasurementType])
class ConferenceMeasurementsControllerSpec extends Specification {
    Conference conference
    ConferenceService mockConferenceService

    def setup() {
        conference = Conference.build(version: 1, measurements: [])
        def lungMeasurementType = MeasurementType.build(name: MeasurementTypeName.LUNG_FUNCTION)
        def weightMeasurementType = MeasurementType.build(name: MeasurementTypeName.WEIGHT)
        def bloodPressureMeasurementType = MeasurementType.build(name: MeasurementTypeName.BLOOD_PRESSURE)
        def saturationMeasurementType = MeasurementType.build(name: MeasurementTypeName.SATURATION)
        def pulseMeasurementType = MeasurementType.build(name: MeasurementTypeName.PULSE)
        [conference, lungMeasurementType, weightMeasurementType, bloodPressureMeasurementType, saturationMeasurementType, pulseMeasurementType]*.save(failOnError: true)

        controller.sessionService = Mock(SessionService)
        controller.patientService = Mock(PatientService)
        controller.patientService.allowedToView(_) >> true
        controller.springSecurityService = Mock(SpringSecurityService)
        controller.springSecurityService.getCurrentUser() >> conference.clinician.user
        mockConferenceService = Mock(ConferenceService)
        controller.conferenceService = mockConferenceService
    }

    def 'blows up if other clinician is opening conference'() {
        setup:
        conference.clinician = Clinician.build()

        when:
        controller.show(conference.id)

        then:
        thrown(IllegalStateException)
    }

    def 'informs when conference is already completed'() {
        setup:
        def lungFunctionMeasurementDraft = ConferenceLungFunctionMeasurementDraft.build(conference: conference)
        def weightMeasurementDraft = ConferenceWeightMeasurementDraft.build(conference: conference)
        [lungFunctionMeasurementDraft, weightMeasurementDraft]*.save(failOnError: true)
        conference.completed = true
        conference.save(failOnError: true)

        when:
        controller.show(conference.id)

        then:
        response.redirectedUrl.endsWith('/alreadyCompleted')
    }

    def 'gives existing measurements on conference'() {
        setup:
        def lungFunctionMeasurementDraft = ConferenceLungFunctionMeasurementDraft.build(conference: conference)
        def weightMeasurementDraft = ConferenceWeightMeasurementDraft.build(conference: conference)
        [lungFunctionMeasurementDraft, weightMeasurementDraft]*.save(failOnError: true)

        when:
        def model = controller.show(conference.id)

        then:
        model.measurementDrafts.sort { it.id } == [lungFunctionMeasurementDraft, weightMeasurementDraft]
    }

    def 'can create new, manual blood pressure measurement draft'() {
        when:
        controller.loadForm(conference.id, 'BLOOD_PRESSURE', false)
        def measurement = ConferenceMeasurementDraft.findAll()[0] // I'd much rather like to get to the rendered model, but that's not possible

        then:
        measurement instanceof ConferenceBloodPressureMeasurementDraft
        measurement.conference == conference
        !measurement.automatic
        measurement.systolic == null
        measurement.diastolic == null
        measurement.pulse == null
        measurement.included
    }

    def 'can create new, automatic blood pressure measurement draft'() {
        when:
        controller.loadForm(conference.id, 'BLOOD_PRESSURE', true)
        def measurement = ConferenceMeasurementDraft.findAll()[0] // I'd much rather like to get to the rendered model, but that's not possible

        then:
        measurement instanceof ConferenceBloodPressureMeasurementDraft
        measurement.conference == conference
        measurement.automatic
        measurement.waiting
        measurement.included
    }

    def 'can create new, manual saturation measurement draft'() {
        when:
        controller.loadForm(conference.id, 'SATURATION', false)
        def measurement = ConferenceMeasurementDraft.findAll()[0] // I'd much rather like to get to the rendered model, but that's not possible

        then:
        measurement instanceof ConferenceSaturationMeasurementDraft
        measurement.conference == conference
        !measurement.automatic
        measurement.saturation == null
        measurement.pulse == null
        measurement.included
    }

    def 'can create new, automatic saturation measurement draft'() {
        when:
        controller.loadForm(conference.id, 'SATURATION', true)
        def measurement = ConferenceMeasurementDraft.findAll()[0] // I'd much rather like to get to the rendered model, but that's not possible

        then:
        measurement instanceof ConferenceSaturationMeasurementDraft
        measurement.conference == conference
        measurement.automatic
        measurement.waiting
        measurement.included
    }

    def 'can create new, manual weight measurement draft'() {
        when:
        controller.loadForm(conference.id, 'WEIGHT', false)
        def measurement = ConferenceMeasurementDraft.findAll()[0] // I'd much rather like to get to the rendered model, but that's not possible

        then:
        measurement instanceof ConferenceWeightMeasurementDraft
        measurement.conference == conference
        !measurement.automatic
        measurement.weight == null
        measurement.included
    }

    def 'can create new, manual lung function measurement draft'() {
        when:
        controller.loadForm(conference.id, 'LUNG_FUNCTION', false)
        def measurement = ConferenceMeasurementDraft.findAll()[0] // I'd much rather like to get to the rendered model, but that's not possible

        then:
        measurement instanceof ConferenceLungFunctionMeasurementDraft
        measurement.conference == conference
        !measurement.automatic
        measurement.fev1 == null
        measurement.included
    }

    def 'can create new, automatic lung function measurement draft'() {
        when:
        controller.loadForm(conference.id, 'LUNG_FUNCTION', true)
        def measurement = ConferenceMeasurementDraft.findAll()[0]

        then:
        measurement instanceof ConferenceLungFunctionMeasurementDraft
        measurement.conference == conference
        measurement.automatic
        measurement.waiting
        measurement.included
    }

    def 'can update manual measurement draft'() {
        setup:
        def lungFunctionMeasurementDraft = ConferenceLungFunctionMeasurementDraft.build(conference: conference)
        def oldConferenceVersion = conference.version

        when:
        params.conferenceVersion = oldConferenceVersion
        params.fev1 = '3,56'
        params.included = 'on'
        controller.updateMeasurement(lungFunctionMeasurementDraft.id)
        def model = JSON.parse(response.contentAsString)
        lungFunctionMeasurementDraft = ConferenceLungFunctionMeasurementDraft.get(lungFunctionMeasurementDraft.id)

        then:
        lungFunctionMeasurementDraft.fev1 == 3.56
        lungFunctionMeasurementDraft.included
        conference.version == oldConferenceVersion + 1
        model.conferenceVersion == conference.version
        model.errors == []
        model.warnings == []
    }

    def 'can only update included property on automatic measurement draft'() {
        setup:
        def lungFunctionMeasurementDraft = ConferenceLungFunctionMeasurementDraft.build(conference: conference, fev1: 3.0, included: false, automatic: true)
        def oldConferenceVersion = conference.version

        when:
        params.conferenceVersion = conference.version
        params.fev1 = '3,56'
        params.included = 'on'
        controller.updateMeasurement(lungFunctionMeasurementDraft.id)
        def model = JSON.parse(response.contentAsString)
        lungFunctionMeasurementDraft = ConferenceLungFunctionMeasurementDraft.get(lungFunctionMeasurementDraft.id)

        then:
        lungFunctionMeasurementDraft.fev1 == 3.0
        lungFunctionMeasurementDraft.included
        conference.version == oldConferenceVersion + 1
        model.conferenceVersion == conference.version
        model.errors == []
        model.warnings == []
    }

    def 'gives validation errors when setting fields to invalid values'() {
        setup:
        def lungFunctionMeasurementDraft = ConferenceLungFunctionMeasurementDraft.build(conference: conference)
        def oldConferenceVersion = conference.version

        when:
        params.conferenceVersion = oldConferenceVersion
        params.fev1 = 'invalid'
        controller.updateMeasurement(lungFunctionMeasurementDraft.id)
        def model = JSON.parse(response.contentAsString)

        then:
        conference.version == oldConferenceVersion
        model.errors == ['fev1']
        model.warnings == []
    }

    def 'gives validation warnings when missing certain fields'() {
        setup:
        def bloodPressureMeasurementDraft = ConferenceBloodPressureMeasurementDraft.build(conference: conference)
        def oldConferenceVersion = conference.version

        when:
        params.conferenceVersion = oldConferenceVersion
        params.systolic = '125'
        controller.updateMeasurement(bloodPressureMeasurementDraft.id)
        def model = JSON.parse(response.contentAsString)

        then:
        conference.version == oldConferenceVersion + 1
        model.errors == []
        model.warnings == ['systolic', 'diastolic']
    }

    def 'gives blank response when updating non-existing measurement'() {
        when:
        controller.updateMeasurement(125)

        then:
        response.contentAsString == ''
    }

    def 'can delete measurement draft'() {
        setup:
        def lungFunctionMeasurementDraft = ConferenceLungFunctionMeasurementDraft.build(conference: conference, automatic: true, waiting: true)
        lungFunctionMeasurementDraft.save(failOnError: true)
        request.method = 'POST'

        when:
        controller.deleteMeasurement(lungFunctionMeasurementDraft.id)

        then:
        1 * mockConferenceService.deleteConferenceMeasurementDraft(lungFunctionMeasurementDraft.id) >> conference
        def model = JSON.parse(response.contentAsString)
        model.conferenceVersion > 0
    }

    def 'knows when automatic measurement has not been submitted by patient'() {
        setup:
        def lungFunctionMeasurementDraft = ConferenceLungFunctionMeasurementDraft.build(conference: conference, automatic: true, waiting: true)

        when:
        controller.loadAutomaticMeasurement(lungFunctionMeasurementDraft.id)

        then:
        response.status == 200
        response.text == ''
    }

    def 'gives blank response when loading non-existing measurement'() {
        when:
        controller.loadAutomaticMeasurement(243)

        then:
        response.status == 200
        response.text == ''
    }


    def 'can load automatically submitted blood pressure measurement'() {
        setup:
        def bloodPressureMeasurementDraft = ConferenceBloodPressureMeasurementDraft.build(conference: conference, automatic: true, waiting: false,
            systolic: 124, diastolic: 54, pulse: 47, meanArterialPressure: 102)

        when:
        controller.loadAutomaticMeasurement(bloodPressureMeasurementDraft.id)
        def json = response.json

        then:
        response.status == 200
        json == [
            systolic: '124',
            diastolic: '54',
            pulse: '47'
        ]
    }

    def 'can load automatically submitted lung function measurement'() {
        setup:
        def lungFunctionMeasurementDraft = ConferenceLungFunctionMeasurementDraft.build(conference: conference, automatic: true, waiting: false, fev1: 3.4)

        when:
        controller.loadAutomaticMeasurement(lungFunctionMeasurementDraft.id)
        def json = response.json

        then:
        response.status == 200
        json == [fev1: '3,40']
    }

    def 'can load automatically submitted saturation measurement'() {
        setup:
        def saturationMeasurementDraft = ConferenceSaturationMeasurementDraft.build(conference: conference, automatic: true, waiting: false, saturation: 96, pulse: 45)

        when:
        controller.loadAutomaticMeasurement(saturationMeasurementDraft.id)
        def json = response.json

        then:
        response.status == 200
        json == [saturation: '96', pulse: '45']
    }

    def 'complains if confirming drafts given the wrong conference version'() {
        when:
        params.id = conference.id
        params.conferenceVersion = conference.version - 1
        controller.confirm()

        then:
        thrown(IllegalStateException)
    }

    def 'creates measurement objects for confirmation, but does NOT save or attach them to patients'() {
        setup:
        ConferenceLungFunctionMeasurementDraft.build(conference: conference, fev1: 3.67, modifiedDate: new Date(), included: true)
        ConferenceWeightMeasurementDraft.build(conference: conference, weight: 76.5, modifiedDate: new Date(), included: true)
        conference.measurements = []

        when:
        params.id = conference.id
        params.conferenceVersion = conference.version
        def model = controller.confirm()

        then:
        conference.measurements.empty
        model.measurementProposals.size() == 2

        model.measurementProposals[0].measurementType.name == MeasurementTypeName.LUNG_FUNCTION
        model.measurementProposals[0].value == 3.67
        !model.measurementProposals[0].patient

        model.measurementProposals[1].measurementType.name == MeasurementTypeName.WEIGHT
        model.measurementProposals[1].value == 76.5
        !model.measurementProposals[1].patient

        !conference.completed
    }

    def 'fills out all properties on automatic blood pressure measurements'() {
        setup:
        ConferenceBloodPressureMeasurementDraft.build(conference: conference, modifiedDate: new Date(), included: true,
                systolic: 123, diastolic: 67, pulse: 45, meanArterialPressure: 100)
        conference.measurements = []

        when:
        params.id = conference.id
        params.conferenceVersion = conference.version
        def model = controller.confirm()
        Measurement bloodPressureMeasurement = model.measurementProposals[0]
        Measurement pulseMeasurement = model.measurementProposals[1]

        then:
        bloodPressureMeasurement.measurementType.name == MeasurementTypeName.BLOOD_PRESSURE
        bloodPressureMeasurement.systolic == 123
        bloodPressureMeasurement.diastolic == 67
        bloodPressureMeasurement.meanArterialPressure == 100
        pulseMeasurement.measurementType.name == MeasurementTypeName.PULSE
        pulseMeasurement.value == 45
    }

    def 'fills out all properties on automatic lung function measurements'() {
        setup:
        ConferenceLungFunctionMeasurementDraft.build(conference: conference, modifiedDate: new Date(), included: true,
                fev1: 3.67, fev6: 5.7, fev1Fev6Ratio: 0.543, fef2575: 2.34, goodTest: true, softwareVersion: 945)
        conference.measurements = []

        when:
        params.id = conference.id
        params.conferenceVersion = conference.version
        Measurement lungFunctionMeasurement = controller.confirm().measurementProposals[0]

        then:
        lungFunctionMeasurement.measurementType.name == MeasurementTypeName.LUNG_FUNCTION
        lungFunctionMeasurement.value == 3.67
        lungFunctionMeasurement.fev6 == 5.7
        lungFunctionMeasurement.fev1Fev6Ratio == 0.543
        lungFunctionMeasurement.fef2575 == 2.34
        lungFunctionMeasurement.isGoodTest
        lungFunctionMeasurement.fevSoftwareVersion == 945
    }

    def 'ignores non-included measurement drafts for confirmation'() {
        setup:
        ConferenceLungFunctionMeasurementDraft.build(conference: conference, fev1: 3.67, modifiedDate: new Date(), included: false)

        when:
        params.id = conference.id
        params.conferenceVersion = conference.version
        def model = controller.confirm()

        then:
        model.measurementProposals.empty
    }

    def 'complains if finishing drafts given the wrong conference version'() {
        when:
        params.id = conference.id
        params.conferenceVersion = conference.version - 1
        controller.finish()

        then:
        thrown(IllegalStateException)
    }

    def 'can complete conference by creating real measurements from drafts'() {
        setup:
        ConferenceLungFunctionMeasurementDraft.build(conference: conference, fev1: 3.67, modifiedDate: new Date(), included: true, deviceId: '678')
        ConferenceWeightMeasurementDraft.build(conference: conference, weight: 76.5, modifiedDate: new Date(), included: true)

        when:
        params.id = conference.id
        params.conferenceVersion = conference.version
        controller.finish()

        then:
        def measurements = conference.measurements.sort { it.id }
        measurements.size() == 2

        measurements[0].measurementType.name == MeasurementTypeName.LUNG_FUNCTION
        measurements[0].value == 3.67
        measurements[0].unit == Unit.LITER
        measurements[0].deviceIdentification == '678'

        measurements[1].measurementType.name == MeasurementTypeName.WEIGHT
        measurements[1].value == 76.5
        measurements[1].unit == Unit.KILO

        conference.completed
    }

    def 'ignores non-included measurement drafts for completion'() {
        setup:
        ConferenceLungFunctionMeasurementDraft.build(conference: conference, fev1: 3.67, modifiedDate: new Date(), included: false)

        when:
        params.id = conference.id
        params.conferenceVersion = conference.version
        controller.finish()

        then:
        conference.measurements.empty
        conference.completed
    }

    def 'can complete conference with blood pressure drafts'() {
        setup:
        ConferenceBloodPressureMeasurementDraft.build(conference: conference, systolic: 120, diastolic: 65, pulse: 76, modifiedDate: new Date(), included: true, deviceId: '321')

        when:
        params.id = conference.id
        params.conferenceVersion = conference.version
        controller.finish()

        then:
        def measurements = conference.measurements.sort { it.id }
        measurements.size() == 2

        measurements[0].measurementType.name == MeasurementTypeName.BLOOD_PRESSURE
        measurements[0].systolic == 120
        measurements[0].diastolic == 65
        measurements[0].unit == Unit.MMHG
        measurements[0].deviceIdentification == '321'

        measurements[1].measurementType.name == MeasurementTypeName.PULSE
        measurements[1].value == 76
        measurements[1].unit == Unit.BPM
        measurements[1].deviceIdentification == '321'

        conference.completed
    }

    def 'can complete conference with saturation drafts'() {
        setup:
        ConferenceSaturationMeasurementDraft.build(conference: conference, saturation: 97, pulse: 76, modifiedDate: new Date(), included: true, deviceId: '456')

        when:
        params.id = conference.id
        params.conferenceVersion = conference.version
        controller.finish()

        then:
        def measurements = conference.measurements.sort { it.id }
        measurements.size() == 2

        measurements[0].measurementType.name == MeasurementTypeName.SATURATION
        measurements[0].value == 97
        measurements[0].unit == Unit.PERCENTAGE
        measurements[0].deviceIdentification == '456'

        measurements[1].measurementType.name == MeasurementTypeName.PULSE
        measurements[1].value == 76
        measurements[1].unit == Unit.BPM
        measurements[1].deviceIdentification == '456'

        conference.completed
    }

    def 'only creates a blood pressure measurement when pulse is not specified'() {
        setup:
        ConferenceBloodPressureMeasurementDraft.build(conference: conference, systolic: 120, diastolic: 65, modifiedDate: new Date(), included: true, deviceId: '786')

        when:
        params.id = conference.id
        params.conferenceVersion = conference.version
        controller.finish()

        then:
        def measurements = conference.measurements.sort { it.id }
        measurements.size() == 1

        measurements[0].measurementType.name == MeasurementTypeName.BLOOD_PRESSURE
        measurements[0].systolic == 120
        measurements[0].diastolic == 65
        measurements[0].unit == Unit.MMHG
        measurements[0].deviceIdentification == '786'
    }

    def 'only creates a pulse measurement when blood pressure is not specified'() {
        setup:
        ConferenceBloodPressureMeasurementDraft.build(conference: conference, pulse: 76, modifiedDate: new Date(), included: true)

        when:
        params.id = conference.id
        params.conferenceVersion = conference.version
        controller.finish()

        then:
        def measurements = conference.measurements.sort { it.id }
        measurements.size() == 1

        measurements[0].measurementType.name == MeasurementTypeName.PULSE
        measurements[0].value == 76
        measurements[0].unit == Unit.BPM
    }

    def 'only creates a saturation measurement when pulse is not specified'() {
        setup:
        ConferenceSaturationMeasurementDraft.build(conference: conference, saturation: 97, modifiedDate: new Date(), included: true)

        when:
        params.id = conference.id
        params.conferenceVersion = conference.version
        controller.finish()

        then:
        def measurements = conference.measurements.sort { it.id }
        measurements.size() == 1

        measurements[0].measurementType.name == MeasurementTypeName.SATURATION
        measurements[0].value == 97
        measurements[0].unit == Unit.PERCENTAGE
    }

    def 'only creates a pulse measurement when saturation is not specified'() {
        setup:
        ConferenceSaturationMeasurementDraft.build(conference: conference, pulse: 76, modifiedDate: new Date(), included: true)

        when:
        params.id = conference.id
        params.conferenceVersion = conference.version
        controller.finish()

        then:
        def measurements = conference.measurements.sort { it.id }
        measurements.size() == 1

        measurements[0].measurementType.name == MeasurementTypeName.PULSE
        measurements[0].value == 76
        measurements[0].unit == Unit.BPM
    }
}
