package org.opentele.server.provider.model.questionnaire

import grails.plugin.springsecurity.annotation.Secured

import org.opentele.server.model.questionnaire.QuestionnaireGroup
import org.opentele.server.model.questionnaire.QuestionnaireGroup2QuestionnaireHeader
import org.opentele.server.core.model.types.PermissionName
import org.opentele.server.provider.command.QuestionnaireGroup2QuestionnaireHeaderCommand
import org.springframework.dao.DataIntegrityViolationException

@Secured(PermissionName.NONE)
class QuestionnaireGroup2QuestionnaireHeaderController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
    def questionnaireService
    def questionnaireGroupService

    def index() {
        redirect(action: "list", params: params)
    }

    @Secured(PermissionName.QUESTIONNAIRE_GROUP_CREATE)
    def create(QuestionnaireGroup2QuestionnaireHeaderCommand command) {
        if (!command.selectableQuestionnaireHeaders) {
            flash.message = message(code: 'questionnaireGroup2QuestionnaireHeader.questionnaire.none.left')
            redirect(controller: "questionnaireGroup", action: "show", id: params.questionnaireGroup.id)
        } else {
            command.properties
        }
    }

    @Secured(PermissionName.QUESTIONNAIRE_GROUP_CREATE)
    def save(QuestionnaireGroup2QuestionnaireHeaderCommand command) {
        questionnaireGroupService.save(command)
        if (command.hasErrors()) {
            render(view: 'create', model: modelWithErrors(command))
        } else {
            flash.message = message(code: 'default.created.message', args: [message(code: 'questionnaireGroup2QuestionnaireHeader.label', default: 'QuestionnaireSchedule')])
            redirect(controller: "questionnaireGroup", action: "show", id: command.questionnaireGroup.id)
        }
    }


    @Secured(PermissionName.QUESTIONNAIRE_GROUP_WRITE)
    def edit(Long id, QuestionnaireGroup2QuestionnaireHeaderCommand command) {
        withInstance(id) { QuestionnaireGroup2QuestionnaireHeader questionnaireGroup2Header ->

            command.questionnaireGroup2QuestionnaireHeader = questionnaireGroup2Header
            command.questionnaireGroup = questionnaireGroup2Header.questionnaireGroup
            command.selectedQuestionnaireHeader = questionnaireGroup2Header.questionnaireHeader
            if(questionnaireGroup2Header?.standardSchedule?.type) {
                bindData(command,questionnaireGroup2Header.standardSchedule.properties)
            }
            def model = command.properties

            return model
        }

    }

    @Secured(PermissionName.QUESTIONNAIRE_GROUP_WRITE)
    def update(QuestionnaireGroup2QuestionnaireHeaderCommand command) {
        if(!command.questionnaireGroup2QuestionnaireHeader) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'questionnaireGroup2QuestionnaireHeader.label', default: 'QuestionnaireGroup2QuestionnaireHeader'), "$id"])
            redirect(controller: "questionnaireGroup", action: "list")
            return
        }

        questionnaireGroupService.update(command)

        if (command.hasErrors()) {
            render(view: 'edit', model: modelWithErrors(command))
        } else {
            flash.message = message(code: 'default.updated.message', args: [message(code: 'questionnaireGroup2QuestionnaireHeader.label', default: 'QuestionnaireSchedule')])
            redirect(controller: "questionnaireGroup", action: "show", id: command.questionnaireGroup.id)
        }

    }

    @Secured(PermissionName.QUESTIONNAIRE_GROUP_DELETE)
    def delete(Long id) {
        withInstance(id) {
            QuestionnaireGroup questionnaireGroup = it.questionnaireGroup
            try {
                questionnaireGroup.removeFromQuestionnaireGroup2Header(it as QuestionnaireGroup2QuestionnaireHeader)
                questionnaireGroup.save()
                it.delete(flush: true)
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'questionnaireGroup2QuestionnaireHeader.label', default: 'QuestionnaireGroup2QuestionnaireHeader'), questionnaireGroup.id])
                redirect(controller: "questionnaireGroup", action: "show", id: questionnaireGroup.id)
            }
            catch (DataIntegrityViolationException ignored) {
                flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'questionnaireGroup2QuestionnaireHeader.label', default: 'QuestionnaireGroup2QuestionnaireHeader'), questionnaireGroup.id])
                redirect(controller: "questionnaireGroup", action: "show", id: questionnaireGroup.id)
            }
        }
    }
    private modelWithErrors(QuestionnaireGroup2QuestionnaireHeaderCommand command) {
        def model = command.properties ? [*:command.properties, errors: command.errors] : [errors: command.errors]
        model
    }

    private withInstance(Long id, Closure closure) {
        def questionnaireGroup2Header = QuestionnaireGroup2QuestionnaireHeader.get(id)
        if (questionnaireGroup2Header) {
            return closure.call(questionnaireGroup2Header)
        } else {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'questionnaireGroup2QuestionnaireHeader.label', default: 'QuestionnaireGroup2QuestionnaireHeader'), "$id"])
            redirect(controller: "questionnaireGroup", action: "list")
        }

    }


}
