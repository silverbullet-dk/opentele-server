package org.opentele.server.provider.questionnaire

import org.opentele.server.model.Clinician
import org.opentele.server.model.questionnaire.Questionnaire
import org.opentele.server.model.questionnaire.QuestionnaireGroup2QuestionnaireHeader
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.springframework.transaction.annotation.Transactional

class QuestionnaireHeaderService {

    def questionnaireService

    def questionnaireProviderService

    List<Questionnaire> findHistoricQuestionnaires(QuestionnaireHeader questionnaireHeaderInstance) {
        def excludedQuestionnaires = [questionnaireHeaderInstance.activeQuestionnaire?.id, questionnaireHeaderInstance.draftQuestionnaire?.id]

        return questionnaireHeaderInstance.questionnaires.findAll {
            !(it.id in excludedQuestionnaires)
        }.sort {it.revision}
    }

    void unpublishActive(QuestionnaireHeader questionnaireHeaderInstance) {
        questionnaireHeaderInstance.activeQuestionnaire = null
    }

    void publishDraft(QuestionnaireHeader questionnaireHeader, Clinician creator) {
        if (questionnaireHeader.draftQuestionnaire) {
            questionnaireService.createPatientQuestionnaire(creator, questionnaireHeader.draftQuestionnaire)
            questionnaireHeader.activeQuestionnaire = questionnaireHeader.draftQuestionnaire
            questionnaireHeader.draftQuestionnaire = null
            questionnaireHeader.save()
        }
    }

    @Transactional
    void deleteDraft(QuestionnaireHeader questionnaireHeader) {
        if (questionnaireHeader.draftQuestionnaire) {
            questionnaireProviderService.deleteQuestionnaire(questionnaireHeader.draftQuestionnaire)
        }
    }

    @Transactional
    void delete(QuestionnaireHeader questionnaireHeader) {
        if (!(questionnaireHeader.activeQuestionnaire || findHistoricQuestionnaires(questionnaireHeader).any())) {
            deleteDraft(questionnaireHeader)

            // TODO: This fails.
            // questionnaireHeader.delete(flush: true)

            // So do this instead:
            // Delete this questionnaire from all questionnaire groups.
            QuestionnaireGroup2QuestionnaireHeader.executeUpdate("delete from QuestionnaireGroup2QuestionnaireHeader qg2qh where questionnaireHeader=?", [questionnaireHeader])

            // Then delete the questionnaire header itself.
            QuestionnaireHeader.executeUpdate("delete from QuestionnaireHeader qh where qh=?",[questionnaireHeader])
        }
    }
}
