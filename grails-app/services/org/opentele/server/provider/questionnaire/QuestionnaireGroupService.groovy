package org.opentele.server.provider.questionnaire
import org.opentele.server.model.questionnaire.QuestionnaireGroup
import org.opentele.server.model.questionnaire.QuestionnaireGroup2QuestionnaireHeader
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.opentele.server.model.questionnaire.StandardSchedule
import org.opentele.server.provider.command.QuestionnaireGroup2QuestionnaireHeaderCommand

class QuestionnaireGroupService {

    void save(QuestionnaireGroup2QuestionnaireHeaderCommand command) {
        if (command.validate()) {
            def questionnaireGroup2QuestionnaireHeader = new QuestionnaireGroup2QuestionnaireHeader()
            if (command.type) {
                questionnaireGroup2QuestionnaireHeader.standardSchedule = new StandardSchedule()
                command.bindScheduleData(questionnaireGroup2QuestionnaireHeader.standardSchedule)
            }
            questionnaireGroup2QuestionnaireHeader.questionnaireGroup = command.questionnaireGroup
            command.questionnaireGroup.addToQuestionnaireGroup2Header(questionnaireGroup2QuestionnaireHeader)
            questionnaireGroup2QuestionnaireHeader.questionnaireHeader = command.selectedQuestionnaireHeader
            command.questionnaireGroup.save(failOnError: true)
        }

    }

    void update(QuestionnaireGroup2QuestionnaireHeaderCommand command) {
        if (command.validate()) {
            def questionnaireGroup2Header = command.questionnaireGroup2QuestionnaireHeader
            questionnaireGroup2Header.questionnaireHeader = command.selectedQuestionnaireHeader
            if (command.type) {
                if (!questionnaireGroup2Header?.standardSchedule?.type) {
                    questionnaireGroup2Header.standardSchedule = new StandardSchedule()
                }
                command.bindScheduleData(questionnaireGroup2Header.standardSchedule)

            } else {
                questionnaireGroup2Header.standardSchedule = null
            }

            questionnaireGroup2Header.save(failOnError: true)
        }
    }

    public List<QuestionnaireHeader> getUnusedQuestionnaireHeadersForQuestionnaireGroup(QuestionnaireGroup questionnaireGroup) {
        def usedQuestionnaireHeaders = (questionnaireGroup?.questionnaireGroup2Header*.questionnaireHeader*.id ?: []) as List

        def list = QuestionnaireHeader.list([sort: 'name']).findAll { !(it.id in usedQuestionnaireHeaders) }

        return list
    }
}
