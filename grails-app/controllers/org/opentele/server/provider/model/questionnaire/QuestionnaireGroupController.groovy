package org.opentele.server.provider.model.questionnaire

import grails.plugin.springsecurity.annotation.Secured

import org.opentele.server.model.questionnaire.QuestionnaireGroup
import org.opentele.server.core.model.types.PermissionName
import org.springframework.dao.DataIntegrityViolationException

@Secured(PermissionName.NONE)
class QuestionnaireGroupController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    @Secured(PermissionName.QUESTIONNAIRE_GROUP_READ_ALL)
    def index() {
        redirect(action: "list", params: params)
    }

    @Secured(PermissionName.QUESTIONNAIRE_GROUP_READ_ALL)
    def list() {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [questionnaireGroupInstanceList: QuestionnaireGroup.list(params), questionnaireGroupInstanceTotal: QuestionnaireGroup.count()]
    }

    @Secured(PermissionName.QUESTIONNAIRE_GROUP_CREATE)
    def create() {
        [questionnaireGroupInstance: new QuestionnaireGroup(params)]
    }

    @Secured(PermissionName.QUESTIONNAIRE_GROUP_CREATE)
    def save(String name) {
        def questionnaireGroupInstance = new QuestionnaireGroup(name: name)
        if (!questionnaireGroupInstance.save(flush: true)) {
            render(view: "create", model: [questionnaireGroupInstance: questionnaireGroupInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'questionnaireGroup.label', default: 'QuestionnaireGroup'), questionnaireGroupInstance.id])
        redirect(action: "show", id: questionnaireGroupInstance.id)
    }

    @Secured(PermissionName.QUESTIONNAIRE_GROUP_READ)
    def show() {
        def questionnaireGroupInstance = QuestionnaireGroup.get(params.id)
        if (!questionnaireGroupInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'questionnaireGroup.label', default: 'QuestionnaireGroup'), params.id])
            redirect(action: "list")
            return
        }

        [questionnaireGroupInstance: questionnaireGroupInstance]
    }

    @Secured(PermissionName.QUESTIONNAIRE_GROUP_WRITE)
    def edit() {
        def questionnaireGroupInstance = QuestionnaireGroup.get(params.id)
        if (!questionnaireGroupInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'questionnaireGroup.label', default: 'QuestionnaireGroup'), params.id])
            redirect(action: "list")
            return
        }

        [questionnaireGroupInstance: questionnaireGroupInstance]
    }

    @Secured(PermissionName.QUESTIONNAIRE_GROUP_WRITE)
    def update() {
        def questionnaireGroupInstance = QuestionnaireGroup.get(params.id)
        if (!questionnaireGroupInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'questionnaireGroup.label', default: 'QuestionnaireGroup'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (questionnaireGroupInstance.version > version) {
                questionnaireGroupInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'questionnaireGroup.label', default: 'QuestionnaireGroup')] as Object[],
                        "Another user has updated this QuestionnaireGroup while you were editing")
                render(view: "edit", model: [questionnaireGroupInstance: questionnaireGroupInstance])
                return
            }
        }

        questionnaireGroupInstance.properties = params

        if (!questionnaireGroupInstance.save(flush: true)) {
            render(view: "edit", model: [questionnaireGroupInstance: questionnaireGroupInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'questionnaireGroup.label', default: 'QuestionnaireGroup'), questionnaireGroupInstance.id])
        redirect(action: "show", id: questionnaireGroupInstance.id)
    }

    @Secured(PermissionName.QUESTIONNAIRE_GROUP_DELETE)
    def delete() {
        def questionnaireGroupInstance = QuestionnaireGroup.get(params.id)
        if (!questionnaireGroupInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'questionnaireGroup.label', default: 'QuestionnaireGroup'), params.id])
            redirect(action: "list")
            return
        }

        try {
            questionnaireGroupInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'questionnaireGroup.label', default: 'QuestionnaireGroup'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'questionnaireGroup.label', default: 'QuestionnaireGroup'), params.id])
            redirect(action: "show", id: params.id)
        }
    }
}
