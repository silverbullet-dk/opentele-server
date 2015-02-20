package org.opentele.server.provider.integration.milou

import grails.buildtestdata.mixin.Build
import org.opentele.builders.CompletedQuestionnaireBuilder
import org.opentele.builders.MeasurementBuilder
import org.opentele.builders.MeasurementTypeBuilder
import org.opentele.builders.PatientBuilder
import org.opentele.server.model.Measurement
import org.opentele.server.model.Patient
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import org.opentele.server.model.patientquestionnaire.MeasurementNodeResult
import org.opentele.server.model.patientquestionnaire.PatientQuestionnaireNode
import org.opentele.server.model.questionnaire.BooleanNode
import org.opentele.server.core.model.types.MeasurementTypeName
import grails.test.mixin.*
import grails.test.mixin.support.*

@TestMixin(GrailsUnitTestMixin)
@TestFor(CtgMeasurementService)
@Build([Patient, CompletedQuestionnaire, Measurement, PatientQuestionnaireNode, BooleanNode, MeasurementNodeResult])
class CtgMeasurementServiceTests {
    CompletedQuestionnaire questionnaire

    void setUp() {
        new MeasurementTypeBuilder().ofType(MeasurementTypeName.CTG).build()
        new MeasurementTypeBuilder().ofType(MeasurementTypeName.PULSE).build()
        def patient = new PatientBuilder().build()
        questionnaire = new CompletedQuestionnaireBuilder().forPatient(patient).build()
    }

    void testFindsNoUnexportedMeasurementsIfAllMeasurementsHaveBeenExported() {
        new MeasurementBuilder().alreadyExported().inQuestionnaire(questionnaire).ofType(MeasurementTypeName.CTG).build()
        new MeasurementBuilder().alreadyExported().inQuestionnaire(questionnaire).ofType(MeasurementTypeName.PULSE).build()

        assert service.ctgMeasurementsToExport() == []
    }

    void testFindsUnexportedCtgMeasurements() {
        def measurement1 = new MeasurementBuilder().inQuestionnaire(questionnaire).ofType(MeasurementTypeName.CTG).build()
        def measurement2 = new MeasurementBuilder().inQuestionnaire(questionnaire).ofType(MeasurementTypeName.CTG).build()
        new MeasurementBuilder().inQuestionnaire(questionnaire).ofType(MeasurementTypeName.PULSE).build()

        def unexportedCtgMeasurements = service.ctgMeasurementsToExport()
        assert unexportedCtgMeasurements.size() == 2
        assert unexportedCtgMeasurements.contains(measurement1)
        assert unexportedCtgMeasurements.contains(measurement2)
    }

    void testCanMarkCtgMeasurementExported() {
        def measurement = new MeasurementBuilder().inQuestionnaire(questionnaire).ofType(MeasurementTypeName.CTG).build()

        assert !measurement.exported
        service.markAsExported(measurement)

        assert measurement.exported
        assert service.ctgMeasurementsToExport().isEmpty()
    }

    void testFailsIfUnableToMarkCtgMeasurementExported() {
        def measurement = new MeasurementBuilder().inQuestionnaire(questionnaire).ofType(MeasurementTypeName.CTG).build()
        measurement.patient = null

        shouldFail {
            service.markAsExported(measurement)
        }
    }
}
