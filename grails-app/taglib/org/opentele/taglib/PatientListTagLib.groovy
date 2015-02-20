package org.opentele.taglib

import org.opentele.server.model.Patient
import org.opentele.server.provider.questionnaire.QuestionnaireProviderService

class PatientListTagLib {

    QuestionnaireProviderService questionnaireProviderService

    static namespace = "patientList"

    def showIcon = { attrs ->

        Patient patient = attrs.remove('patient')

        def (icon, tooltip) = questionnaireProviderService.iconAndTooltip(g, patient)
        if (tooltip) {
            out << """<div data-tooltip="${tooltip}">"""
        } else {
            out << "<div>"
        }
        out << g.img(dir: "images", file: icon)
        out << "</div>"
    }
}
