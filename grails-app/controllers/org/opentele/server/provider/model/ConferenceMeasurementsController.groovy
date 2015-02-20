package org.opentele.server.provider.model

import dk.silverbullet.kih.api.auditlog.SkipAuditLog
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured

import org.opentele.server.model.Clinician
import org.opentele.server.model.Conference
import org.opentele.server.model.ConferenceBloodPressureMeasurementDraft
import org.opentele.server.model.ConferenceLungFunctionMeasurementDraft
import org.opentele.server.model.ConferenceMeasurementDraft
import org.opentele.server.core.model.ConferenceMeasurementDraftType
import org.opentele.server.model.ConferenceSaturationMeasurementDraft
import org.opentele.server.model.ConferenceWeightMeasurementDraft
import org.opentele.server.model.Measurement
import org.opentele.server.model.MeasurementType
import org.opentele.server.model.Patient
import org.opentele.server.core.model.types.MeasurementTypeName
import org.opentele.server.core.model.types.PermissionName
import org.opentele.server.core.model.types.Unit

@Secured(PermissionName.NONE)
class ConferenceMeasurementsController {
    def sessionService
    def patientService
    def conferenceService
    def springSecurityService

    @Secured(PermissionName.VIDEO_CALL)
    def show(long id) {
        def conference = Conference.get(id)
        sessionService.setPatient(session, conference.patient)
        checkPermissionToConference(conference)

        if (conference.completed) {
            redirect(action:'alreadyCompleted')
        } else {
            [conference: conference, measurementDrafts: conference.measurementDrafts.toArray().sort { it.id }]
        }
    }

    @Secured(PermissionName.VIDEO_CALL)
    def alreadyCompleted() {
        // No logic here
    }

    @Secured(PermissionName.VIDEO_CALL)
    @SkipAuditLog
    def keepAlive() {
        // Don't do anything here
        render ''
    }

    @Secured(PermissionName.VIDEO_CALL)
    def loadForm(long id, String type, boolean automatic) {
        def conference = Conference.get(id)
        checkPermissionToPatient(conference.patient)
        checkPermissionToConference(conference)

        ConferenceMeasurementDraftType draftType = ConferenceMeasurementDraftType.valueOf(type)

        def measurement = createMeasurementOfType(draftType)
        measurement.conference = conference
        measurement.automatic = measurement.waiting = automatic
        measurement.included = true
        measurement.save(failOnError: true)
        render(template: templateForMeasurementType(measurement.type, automatic), model: [measurement: measurement])
    }

    @Secured(PermissionName.VIDEO_CALL)
    def updateMeasurement(long id) {
        ConferenceMeasurementDraft measurement = ConferenceMeasurementDraft.findById(id)
        if (measurement == null) {
            render ''
            return
        }

        Conference conference = measurement.conference
        checkPermissionToPatient(conference.patient)
        checkPermissionToConference(conference)
        checkConferenceVersion(conference)

        if (measurement.automatic) {
            measurement.setIncluded(params.included as boolean)
        } else {
            measurement.updateFields(params)
        }

        def errors = measurement.errorFields
        def warnings = measurement.warningFields
        if (errors.empty) {
            measurement.save(failOnError: true)
            conference.version++
        }

        def result = [conferenceVersion: conference.version, errors: errors, warnings: warnings]
        render result as JSON
    }

    @Secured(PermissionName.VIDEO_CALL)
    def deleteMeasurement(long id) {
        ConferenceMeasurementDraft measurement = ConferenceMeasurementDraft.findById(id)
        Conference conference = measurement.conference
        checkPermissionToPatient(conference.patient)
        checkPermissionToConference(conference)

        def updatedConference = conferenceService.deleteConferenceMeasurementDraft(id)

        def result = [conferenceVersion: updatedConference.version]
        render result as JSON
    }

    @Secured(PermissionName.VIDEO_CALL)
    def loadAutomaticMeasurement(long id) {
        ConferenceMeasurementDraft measurement = ConferenceMeasurementDraft.findById(id)
        if (measurement == null) {
            render ''
            return
        }

        Conference conference = measurement.conference
        checkPermissionToPatient(conference.patient)
        checkPermissionToConference(conference)

        if (measurement.waiting) {
            render ''
        } else {
            def result
            switch (measurement.type) {
                case ConferenceMeasurementDraftType.BLOOD_PRESSURE:
                    result = [
                        systolic: formatNumber(number: measurement.systolic, format:'0'),
                        diastolic: formatNumber(number: measurement.diastolic, format: '0'),
                        pulse: formatNumber(number: measurement.pulse, format: '0')
                    ]
                    break
                case ConferenceMeasurementDraftType.LUNG_FUNCTION:
                    result = [
                        fev1: formatNumber(number: measurement.fev1, format:'0.00', locale: 'DA')
                    ]
                    break
                case ConferenceMeasurementDraftType.SATURATION:
                    result = [
                        saturation: formatNumber(number: measurement.saturation, format:'0'),
                        pulse: formatNumber(number: measurement.pulse, format:'0')
                    ]
                    break
                default:
                    throw new IllegalStateException("Unknown measurement type: '${measurement.type}'")
            }
            render result as JSON
        }
    }

    @Secured(PermissionName.VIDEO_CALL)
    def confirm() {
        def conference = Conference.get(params.id)
        checkPermissionToPatient(conference.patient)
        checkPermissionToConference(conference)
        checkConferenceVersion(conference)

        def measurementProposals = []
        def includedMeasurementDrafts = conference.measurementDrafts.findAll { it.included }
        includedMeasurementDrafts.sort { it.id }.each { draft ->
            measurementProposals.addAll createMeasurementsFromDraft(draft)
        }

        [conference: conference, measurementProposals: measurementProposals]
    }

    @Secured(PermissionName.VIDEO_CALL)
    def finish() {
        def conference = Conference.get(params.id)
        checkPermissionToPatient(conference.patient)
        checkPermissionToConference(conference)
        checkConferenceVersion(conference)

        def includedMeasurementDrafts = conference.measurementDrafts.findAll { it.included }
        includedMeasurementDrafts.sort { it.id }.each { draft ->
            createMeasurementsFromDraft(draft).each { measurement ->
                conference.addToMeasurements(measurement)
                measurement.patient = conference.patient
                measurement.time = draft.modifiedDate
                measurement.save(failOnError:true)
            }
        }

        conference.completed = true

        redirect action:'close'
    }

    @Secured(PermissionName.VIDEO_CALL)
    def close() {
        // No logic here
    }

    private ConferenceMeasurementDraft createMeasurementOfType(ConferenceMeasurementDraftType type) {
        switch (type) {
            case ConferenceMeasurementDraftType.BLOOD_PRESSURE:
                return new ConferenceBloodPressureMeasurementDraft()
            case ConferenceMeasurementDraftType.LUNG_FUNCTION:
                return new ConferenceLungFunctionMeasurementDraft()
            case ConferenceMeasurementDraftType.WEIGHT:
                return new ConferenceWeightMeasurementDraft()
            case ConferenceMeasurementDraftType.SATURATION:
                return new ConferenceSaturationMeasurementDraft()
            default:
                throw new IllegalArgumentException("Unknown draft type: $type")
        }
    }

    private String templateForMeasurementType(ConferenceMeasurementDraftType type, boolean automatic) {
        switch (type) {
            case ConferenceMeasurementDraftType.BLOOD_PRESSURE:
                return automatic ? 'drafts/automaticBloodPressure' : 'drafts/manualBloodPressure'
            case ConferenceMeasurementDraftType.LUNG_FUNCTION:
                return automatic ? 'drafts/automaticLungFunction' : 'drafts/manualLungFunction'
            case ConferenceMeasurementDraftType.WEIGHT:
                return 'drafts/manualWeight'
            case ConferenceMeasurementDraftType.SATURATION:
                return automatic ? 'drafts/automaticSaturation' : 'drafts/manualSaturation'
            default:
                throw new IllegalArgumentException("Unknown draft type: $type")
        }
    }

    private createMeasurementsFromDraft(ConferenceMeasurementDraft draft) {
        def result = []
        ConferenceMeasurementDraftType type = draft.type
        switch (type) {
            case ConferenceMeasurementDraftType.BLOOD_PRESSURE:
                if (draft.systolic != null && draft.diastolic != null) {
                    result << new Measurement(measurementType: MeasurementType.findByName(MeasurementTypeName.BLOOD_PRESSURE),
                            systolic: draft.systolic, diastolic: draft.diastolic, meanArterialPressure: draft.meanArterialPressure,
                            unit: Unit.MMHG, deviceIdentification: draft.deviceId)
                }
                if (draft.pulse) {
                    result << new Measurement(measurementType: MeasurementType.findByName(MeasurementTypeName.PULSE), value: draft.pulse,
                            unit: Unit.BPM, deviceIdentification: draft.deviceId)
                }
                break
            case ConferenceMeasurementDraftType.LUNG_FUNCTION:
                if (draft.fev1 != null) {
                    result << new Measurement(measurementType: MeasurementType.findByName(MeasurementTypeName.LUNG_FUNCTION),
                       value: draft.fev1,
                       fev6: draft.fev6, fev1Fev6Ratio: draft.fev1Fev6Ratio, fef2575: draft.fef2575, isGoodTest: draft.goodTest, fevSoftwareVersion: draft.softwareVersion,
                       unit: Unit.LITER, deviceIdentification: draft.deviceId)
                }
                break
            case ConferenceMeasurementDraftType.WEIGHT:
                if (draft.weight != null) {
                    result << new Measurement(measurementType: MeasurementType.findByName(MeasurementTypeName.WEIGHT), value: draft.weight,
                        unit: Unit.KILO, deviceIdentification: draft.deviceId)
                }
                break
            case ConferenceMeasurementDraftType.SATURATION:
                if (draft.saturation != null) {
                    result << new Measurement(measurementType: MeasurementType.findByName(MeasurementTypeName.SATURATION), value: draft.saturation,
                        unit: Unit.PERCENTAGE, deviceIdentification: draft.deviceId)
                }
                if (draft.pulse != null) {
                    result << new Measurement(measurementType: MeasurementType.findByName(MeasurementTypeName.PULSE), value: draft.pulse,
                        unit: Unit.BPM, deviceIdentification: draft.deviceId)
                }
                break
            default:
                throw new IllegalArgumentException("Unknown draft type: $type")
        }

        result
    }

    private checkConferenceVersion(Conference conference) {
        if (conference.version != params.long('conferenceVersion')) {
            throw new IllegalStateException("Conference version is ${conference.version}, but client thinks it is ${params.conferenceVersion}")
        }
    }

    private checkPermissionToConference(Conference conference) {
        if (Clinician.findByUser(springSecurityService.getCurrentUser()) != conference.clinician) {
            throw new IllegalStateException('Clinician not allowed to view conference')
        }
    }

    private checkPermissionToPatient(Patient patient) {
        if (!patientService.allowedToView(patient)) {
            throw new IllegalStateException("User not allowed to view patient ${patient.id}")
        }
    }
}
