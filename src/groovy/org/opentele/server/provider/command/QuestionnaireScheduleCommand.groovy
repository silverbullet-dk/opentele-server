package org.opentele.server.provider.command

import grails.validation.Validateable
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import org.opentele.server.core.command.ScheduleCommand
import org.opentele.server.core.model.Schedule
import org.opentele.server.model.MonitoringPlan
import org.opentele.server.model.QuestionnaireSchedule
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.opentele.server.provider.questionnaire.QuestionnaireProviderService


@Validateable(nullable = true)
@ToString(includeFields = true)
@EqualsAndHashCode
class QuestionnaireScheduleCommand extends ScheduleCommand {

    QuestionnaireProviderService questionnaireProviderService
    Long version
    Schedule questionnaireSchedule
    MonitoringPlan monitoringPlan

    @SuppressWarnings("GroovyUnusedDeclaration") // Used in views
    List<QuestionnaireHeader> getSelectableQuestionnaireHeaders() {
        def list = questionnaireProviderService.getUnusedQuestionnaireHeadersForMonitoringPlan(monitoringPlan)
        if (questionnaireSchedule && questionnaireSchedule instanceof QuestionnaireSchedule) {
            selectedQuestionnaireHeader = questionnaireSchedule.questionnaireHeader
            list << questionnaireSchedule.questionnaireHeader
            list = list.sort { it.toString().toLowerCase() }
        }
        return list
    }

    QuestionnaireHeader selectedQuestionnaireHeader

    static constraints = {
        monitoringPlan nullable: false
        questionnaireSchedule nullable: true, validator: { value, obj ->
            if (value && value.version > obj.version) {
                return "optimistic.locking.failure"
            }
        }
    }
}
