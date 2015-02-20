package org.opentele.server.provider.questionnaire

import org.opentele.server.model.QuestionnaireSchedule
import org.opentele.server.provider.command.QuestionnaireScheduleCommand

class QuestionnaireScheduleService {
    def update(QuestionnaireScheduleCommand command) {
        if (command.validate()) {
            def questionnaireSchedule = command.questionnaireSchedule
            questionnaireSchedule.questionnaireHeader = command.selectedQuestionnaireHeader

            command.bindScheduleData(questionnaireSchedule)
            questionnaireSchedule.save(failOnError: true)
        }

    }

    def save(QuestionnaireScheduleCommand command) {
        if (command.validate()) {
            def questionnaireSchedule = new QuestionnaireSchedule()
            command.bindScheduleData(questionnaireSchedule)
            questionnaireSchedule.monitoringPlan = command.monitoringPlan
            questionnaireSchedule.questionnaireHeader = command.selectedQuestionnaireHeader
            questionnaireSchedule.save(failOnError: true)
        }
    }
}
