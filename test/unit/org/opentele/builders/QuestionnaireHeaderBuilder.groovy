package org.opentele.builders

import org.opentele.server.model.patientquestionnaire.PatientBooleanNode
import org.opentele.server.model.patientquestionnaire.PatientQuestionnaire
import org.opentele.server.model.questionnaire.BooleanNode
import org.opentele.server.model.questionnaire.Questionnaire
import org.opentele.server.model.questionnaire.QuestionnaireHeader

class QuestionnaireHeaderBuilder {
    def String name

    QuestionnaireHeaderBuilder forName(String name) {
        this.name = name

        this
    }

    QuestionnaireHeader build() {

        QuestionnaireHeader questionnaireHeader = QuestionnaireHeader.build(name: "TestQuestionnaireHeader"+System.nanoTime())
        if(name) {
            questionnaireHeader.name = this.name
        }

        def startNode = BooleanNode.build()
        startNode.save(failOnError: true)

        Questionnaire questionnaire = Questionnaire.build(questionnaireHeader: questionnaireHeader, startNode: startNode)
        questionnaire.addToPatientQuestionnaires(PatientQuestionnaire.build(templateQuestionnaire: questionnaire))

        questionnaireHeader.addToQuestionnaires(questionnaire)
        questionnaireHeader.setActiveQuestionnaire(questionnaire)

        questionnaireHeader.save(failOnError:true)

        questionnaireHeader
    }
}
