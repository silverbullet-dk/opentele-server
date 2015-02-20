package org.opentele.server.provider.model.questionnaire
import dk.silverbullet.kih.api.auditlog.SkipAuditLog
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import org.codehaus.groovy.grails.web.json.JSONObject

import org.opentele.server.model.questionnaire.Questionnaire
import org.opentele.server.core.command.QuestionnaireEditorCommand
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.opentele.server.core.model.types.PermissionName

@Secured(PermissionName.NONE)
class QuestionnaireEditorController {
    def questionnaireEditorService
    def clinicianService

    static allowedMethods = [save:'POST',  editorState:'GET']

    // Databinding with commandobject and JSON works because of org.opentele.server.JSONParamsMapFilters
    @Secured(PermissionName.QUESTIONNAIRE_CREATE)
    def save() {
        def editCommand = new QuestionnaireEditorCommand()
        bindData(editCommand, params)
        editCommand.cloneScheduleData(params.standardSchedule)
        if(editCommand.validate()) {
            try {
                questionnaireEditorService.createOrUpdateQuestionnaire(editCommand, clinicianService.currentClinician)
                render([editCommand.questionnaire.id] as JSON)
                return
            } catch (e) {
                log.warn("Could not save questionnaire due to error: ${e.message}", e)
                editCommand.errors.reject("questionnaireeditor.unknown.error")
            }
        } else {
            log.debug("Save validation failed")
        }
        response.status = 422;
        render(template: '/questionnaireSchedule/errors', model: [errors: editCommand.errors])
    }

    @Secured(PermissionName.QUESTIONNAIRE_CREATE)
    def edit(Long id, QuestionnaireEditorCommand command) {
        def questionnaireHeader = QuestionnaireHeader.get(id)
        command.questionnaireHeader = questionnaireHeader
        command.title = questionnaireHeader.name
        bindData(command, command.questionnaire.standardSchedule.properties)

        if(command.validate(['questionnaireHeader.id'])) {
            command.properties
        } else {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'questionnaireHeader.label', default: 'QuestionnaireHeader'), id])
            redirect(controller: "questionnaireHeader", action: "list")
        }
    }

    @Secured(PermissionName.QUESTIONNAIRE_WRITE)
    def editorState(Long baseId) {
        def questionnaire = Questionnaire.findById(baseId)
        render new JSONObject(questionnaire?.editorState) as JSON
    }

    @Secured(PermissionName.QUESTIONNAIRE_WRITE)
    @SkipAuditLog
    def keepAlive() {
        render "pong"
    }
}
