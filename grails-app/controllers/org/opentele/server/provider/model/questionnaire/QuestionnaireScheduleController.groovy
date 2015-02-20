package org.opentele.server.provider.model.questionnaire

import dk.silverbullet.kih.api.auditlog.SkipAuditLog
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.opentele.server.provider.questionnaire.QuestionnaireScheduleService

import org.opentele.server.core.command.AddQuestionnaireGroup2MonitoringPlanCommand
import org.opentele.server.model.QuestionnaireSchedule
import org.opentele.server.provider.command.QuestionnaireScheduleCommand
import org.opentele.server.core.model.Schedule
import org.opentele.server.core.model.types.PermissionName
import org.opentele.server.provider.questionnaire.QuestionnaireProviderService
import org.springframework.dao.DataIntegrityViolationException

import static org.opentele.server.provider.constants.Constants.SESSION_PATIENT_ID

@Secured(PermissionName.NONE)
class QuestionnaireScheduleController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    QuestionnaireProviderService questionnaireProviderService
    QuestionnaireScheduleService questionnaireScheduleService

    @Secured(PermissionName.QUESTIONNAIRE_SCHEDULE_CREATE)
    def create(QuestionnaireScheduleCommand command) {
        adjustScheduleStartDate(command)
        command.properties
    }

    @Secured(PermissionName.QUESTIONNAIRE_SCHEDULE_CREATE)
    @SkipAuditLog
    def questionnaireScheduleData(long questionnaireHeaderId, QuestionnaireScheduleCommand command) {

        def questionnaireHeader = QuestionnaireHeader.get(questionnaireHeaderId)

        def activeQuestionnaire = questionnaireHeader.activeQuestionnaire

        def questionnaireSchedule
        if (activeQuestionnaire != null) {
            questionnaireSchedule = questionnaireHeader.activeQuestionnaire.standardSchedule
        } else {
            questionnaireSchedule = new QuestionnaireSchedule()
            questionnaireSchedule.type = Schedule.ScheduleType.UNSCHEDULED
            questionnaireSchedule.monitoringPlan = command.monitoringPlan

            command.questionnaireSchedule = questionnaireSchedule
            bindData(command, questionnaireSchedule.properties)
            adjustScheduleStartDate(command)
        }

        def result = [
            scheduleType: questionnaireSchedule.type.name(),
            details: g.render(template: '/schedule/scheduleTypesAsIndividualPage', model: command.properties)
        ]

        render result as JSON
    }

    @Secured([PermissionName.QUESTIONNAIRE_SCHEDULE_CREATE, PermissionName.QUESTIONNAIRE_SCHEDULE_WRITE])
    def save(QuestionnaireScheduleCommand command) {
        questionnaireScheduleService.save(command)
        if (command.hasErrors()) {
            render(view: "create", model: modelWithErrors(command))
        } else {
            flash.message = message(code: 'default.created.message', args: [message(code: 'questionnaireSchedule.label')])
            redirect(controller: "monitoringPlan", action: "show", id: command.monitoringPlan.patient.id)
        }
    }

    @Secured(PermissionName.QUESTIONNAIRE_SCHEDULE_WRITE)
    def edit(Long id, QuestionnaireScheduleCommand command) {
        def questionnaireSchedule = QuestionnaireSchedule.get(id)
        if (!questionnaireSchedule) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'questionnaireSchedule.label')])
            redirect(controller: "monitoringPlan", action: "show", id: session[SESSION_PATIENT_ID])
            return
        }
        command.questionnaireSchedule = questionnaireSchedule
        bindData(command, questionnaireSchedule.properties)
        return command.properties
    }


    @Secured(PermissionName.QUESTIONNAIRE_SCHEDULE_WRITE)
    def update(QuestionnaireScheduleCommand command) {
        // Since questionnaireSchedule in the command has type Schedule, Grails cannot infer that it's actually a QuestionnaireSchedule
        command.questionnaireSchedule = QuestionnaireSchedule.get(params['questionnaireSchedule.id'])
        if (!command.questionnaireSchedule) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'questionnaireSchedule.label', default: 'QuestionnaireSchedule')])
            redirect(controller: "monitoringPlan", action: "show", id: session[SESSION_PATIENT_ID])
            return
        }
        questionnaireScheduleService.update(command)

        if (command.hasErrors()) {
            render(view: "edit", model: modelWithErrors(command))
        } else {
            flash.message = message(code: 'default.updated.message', args: [message(code: 'questionnaireSchedule.label', default: 'QuestionnaireSchedule')])
            redirect(controller: "monitoringPlan", action: "show", id: command.monitoringPlan.patient.id)
        }
    }

    private modelWithErrors(def command) {
        def model = command.properties.collectEntries { key, value -> [key, value] }
        model.errors = command.errors
        model
    }


    @Secured(PermissionName.QUESTIONNAIRE_SCHEDULE_DELETE)
    def del(Long id) {
        def questionnaireSchedule = QuestionnaireSchedule.get(id)
        if (!questionnaireSchedule) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'questionnaireSchedule.label', default: 'QuestionnaireSchedule')])
        } else {
            try {
                questionnaireSchedule.delete(flush: true)
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'questionnaireSchedule.label', default: 'QuestionnaireSchedule')])
            }
            catch (DataIntegrityViolationException ignored) {
                flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'questionnaireSchedule.label', default: 'QuestionnaireSchedule')])
            }
        }
        redirect(controller: "monitoringPlan", action: "show", id: session[SESSION_PATIENT_ID])
    }

    @Secured(PermissionName.QUESTIONNAIRE_SCHEDULE_CREATE)
    def showAddQuestionnaireGroup(AddQuestionnaireGroup2MonitoringPlanCommand command) {
        [command: command]
    }

    @Secured(PermissionName.QUESTIONNAIRE_SCHEDULE_CREATE)
    def pickQuestionnaireGroup(AddQuestionnaireGroup2MonitoringPlanCommand command) {
        command.newQuestionnaires = questionnaireProviderService.findQuestionnaireGroup2HeadersAndOverlapWithExistingQuestionnaireSchedules(command.questionnaireGroup, command.monitoringPlan)
        [command: command]
    }

    @Secured(PermissionName.QUESTIONNAIRE_SCHEDULE_CREATE)
    def addQuestionnaireGroup(AddQuestionnaireGroup2MonitoringPlanCommand command) {
        log.debug(command)
        questionnaireProviderService.addOrUpdateQuestionnairesOnMonitoringPlan(command)
        if (command.addedQuestionnaires || command.updatedQuestionnaires) {
            appendFlash(code: 'questionnaireGroup.questionnaireAddOrUpdate.pre')
            if (command.addedQuestionnaires) {
                appendFlash(code: "questionnaireGroup.questionnaireAddOrUpdate.add", args: [command.addedQuestionnaires.size()])
            }
            if (command.addedQuestionnaires && command.updatedQuestionnaires) {
                appendFlash(code: 'questionnaireGroup.questionnaireAddOrUpdate.and')
            }
            if (command.updatedQuestionnaires) {
                appendFlash(code: "questionnaireGroup.questionnaireAddOrUpdate.update", args: [command.updatedQuestionnaires.size()])
            }
            appendFlash(code: 'questionnaireGroup.questionnaireAddOrUpdate.post', args: [command.updatedQuestionnaires.size() + command.addedQuestionnaires.size() == 1 ? '' : message(code: 'questionnaireGroup.questionnaireAddOrUpdate.plural')])
        } else {
            flash.message = message(code: 'questionnaireGroup.questionnaireAddOrUpdate.none')
        }
        flash.updated = command.addedQuestionnaires*.id + command.updatedQuestionnaires*.id
        redirect(controller: "monitoringPlan", action: "show", id: command.monitoringPlan.patient.id)
    }

    private appendFlash(Map map) {
        flash.message = "${flash.message ?: ''} ${message(map)}".trim()
    }

    private adjustScheduleStartDate(QuestionnaireScheduleCommand command) {
        def monitoringPlanStartDate = command.monitoringPlan.startDate
        if (monitoringPlanStartDate.after(new Date())) {
            command.startingDate = monitoringPlanStartDate
        }
    }
}


