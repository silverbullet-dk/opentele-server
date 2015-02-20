package org.opentele.taglib

import org.opentele.server.model.questionnaire.QuestionnaireHeader

class QuestionnaireHeaderTagLib {
    static namespace = "questionnaireHeader"

    def status = { attrs ->
        def tooltipPrefix = attrs.remove('tooltipPrefix')
        QuestionnaireHeader questionnaireHeader = attrs.remove('questionnaireHeader')

        if (!questionnaireHeader) throwTagError("Missing [questionnaireHeader] attribute on [status] tag")

        if (questionnaireHeader.activeQuestionnaire && !questionnaireHeader.draftQuestionnaire) {
            outputImage("questionnairePublished", tooltipPrefix, "published")
        } else if (!questionnaireHeader.activeQuestionnaire && questionnaireHeader.draftQuestionnaire) {
            outputImage("questionnaireDraft", tooltipPrefix, "draft")
        } else if (questionnaireHeader.activeQuestionnaire && questionnaireHeader.draftQuestionnaire) {
            outputImage("questionnairePublishedDraft", tooltipPrefix, "publishedDraft")
        } else {
            outputImage("questionnaireHistoric", tooltipPrefix, "historic")
        }

    }

    private outputImage(image, tooltipPrefix, tooltip) {
        out << r.img(uri: "/images/${image}.png", 'data-tooltip': message(code: "${tooltipPrefix ? "${tooltipPrefix}.":""}questionnaireHeader.${tooltip}.tooltip"))
    }


}
