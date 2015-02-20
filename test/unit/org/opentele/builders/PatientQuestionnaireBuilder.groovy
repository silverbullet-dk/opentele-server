package org.opentele.builders

import org.opentele.server.model.patientquestionnaire.PatientBooleanNode
import org.opentele.server.model.patientquestionnaire.PatientQuestionnaire

class PatientQuestionnaireBuilder {
    def String name ="TestQuestionnaire"

    PatientQuestionnaire build() {

        def startNode = PatientBooleanNode.build()
        startNode.save(failOnError: true)
        def patientQuestionnaire = PatientQuestionnaire.build(/*patient: patient,*/ startNode: startNode)

        patientQuestionnaire.save(failOnError:true)

        patientQuestionnaire
    }
}
