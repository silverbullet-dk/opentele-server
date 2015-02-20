package org.opentele.server.provider.model.questionnaire

import grails.plugin.springsecurity.annotation.Secured
import org.opentele.server.core.model.types.PermissionName
import org.opentele.server.model.Clinician
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import org.opentele.server.model.patientquestionnaire.NodeResult
import org.springframework.dao.DataIntegrityViolationException
import org.opentele.server.model.questionnaire.Questionnaire

@Secured(PermissionName.NONE)
class QuestionnaireController {
	def questionnaireService
    def springSecurityService
    def patientService
    def acknowledgeQuestionnaireService
    def clinicianService

    @Secured(PermissionName.QUESTIONNAIRE_READ_ALL)
	def index() {
        redirect(controller: "questionnaireHeader", action: "list", params: params)
	}
    @Secured(PermissionName.QUESTIONNAIRE_READ_ALL)
	def list() {
        redirect(controller: "questionnaireHeader", action: "list", params: params)
	}

	/**
	 * Acknowledges a questionnaire. The function checks, if the user has the sufficient rights. The result is redirected back to the overview.
     * If params.withAutoMessage is set, the questionnaire is marked with a showAcknowledgementToPatient flag.
	 */
    @Secured(PermissionName.QUESTIONNAIRE_ACKNOWLEDGE)
	def acknowledge() {
		def qId = params.id
		def note = params.note
        def withAutoMessage = (params.withAutoMessage != null && params.withAutoMessage.equals('true'))
		def questionnaire = CompletedQuestionnaire.get(qId)
		boolean errorOccured = false

        acknowledgeQuestionnaireService.acknowledge(questionnaire, note, withAutoMessage)

		if (questionnaire.hasErrors()) {
			def msg
			questionnaire.errors.each { error ->
				msg = msg + error
				errorOccured = true
			}
			flash.error = msg
		} else {
			flash.message  = g.message(code: "completedquestionnaire.acknowledged", args: [questionnaire.patientQuestionnaire?.name, g.formatDate(date: questionnaire.acknowledgedDate)])
		}

		redirect(controller: session.lastController, action: session.lastAction, id: session.lastParams?.id, params: session.lastFilterParams, ignoreNavigation: true)
	}

    @Secured(PermissionName.QUESTIONNAIRE_CREATE)
	def create() {
		[questionnaireInstance: new Questionnaire(params)]
	}

    @Secured(PermissionName.QUESTIONNAIRE_CREATE)
	def save() {
		def questionnaireInstance = new Questionnaire(params)
		if (!questionnaireInstance.save(flush: true)) {
			render(view: "create", model: [questionnaireInstance: questionnaireInstance])
			return
		}

		flash.message = message(code: 'default.created.message', args: [message(code: 'questionnaire.label', default: 'Questionnaire')])
		redirect(action: "show", id: questionnaireInstance.id)
	}

    @Secured(PermissionName.QUESTIONNAIRE_READ)
	def show() {
		def questionnaireInstance = Questionnaire.get(params.id)
		if (!questionnaireInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'questionnaire.label', default: 'Questionnaire')])
			redirect(action: "list")
			return
		}

		[questionnaireInstance: questionnaireInstance]
	}

    @Secured(PermissionName.QUESTIONNAIRE_WRITE)
	def edit() {
		def questionnaireInstance = Questionnaire.get(params.id)
		if (!questionnaireInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'questionnaire.label', default: 'Questionnaire')])
			redirect(action: "list")
			return
		}

		[questionnaireInstance: questionnaireInstance]
	}

    @Secured(PermissionName.QUESTIONNAIRE_WRITE)
	def update() {
		def questionnaireInstance = Questionnaire.get(params.id)
		if (!questionnaireInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'questionnaire.label', default: 'Questionnaire')])
			redirect(action: "list")
			return
		}

		if (params.version) {
			def version = params.version.toLong()
			if (questionnaireInstance.version > version) {
				questionnaireInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
						[message(code: 'questionnaire.label', default: 'Questionnaire')] as Object[],
						"Another user has updated this Questionnaire while you were editing")
				render(view: "edit", model: [questionnaireInstance: questionnaireInstance])
				return
			}
		}

		questionnaireInstance.properties = params

		if (!questionnaireInstance.save(flush: true)) {
			render(view: "edit", model: [questionnaireInstance: questionnaireInstance])
			return
		}

		flash.message = message(code: 'default.updated.message', args: [message(code: 'questionnaire.label', default: 'Questionnaire')])
		redirect(action: "show", id: questionnaireInstance.id)
	}

    @Secured(PermissionName.QUESTIONNAIRE_DELETE)
	def delete() {
		def questionnaireInstance = Questionnaire.get(params.id)
		if (!questionnaireInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'questionnaire.label', default: 'Questionnaire')])
			redirect(action: "list")
			return
		}

		try {
			questionnaireInstance.delete(flush: true)
			flash.message = message(code: 'default.deleted.message', args: [message(code: 'questionnaire.label', default: 'Questionnaire')])
			redirect(action: "list")
		}
		catch (DataIntegrityViolationException e) {
			flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'questionnaire.label', default: 'Questionnaire')])
			redirect(action: "show", id: params.id)
		}
	}

    @Secured(PermissionName.QUESTIONNAIRE_IGNORE)
	def toggleIgnoreQuestionnaire() {
		def completedQuestionnaire = CompletedQuestionnaire.get(params.id)
		def user = springSecurityService.currentUser
		def clinician = Clinician.findByUser(user)
		def reason = params.ignoreReason
		
		if (!completedQuestionnaire._questionnaireIgnored) {
			completedQuestionnaire._questionnaireIgnored = true
			completedQuestionnaire.questionnaireIgnoredReason = reason
			completedQuestionnaire.questionnareIgnoredBy = clinician
			completedQuestionnaire.save()
			completedQuestionnaire.completedQuestions.each {nodeResult ->
				nodeResult.setNodeIgnored(true)
				nodeResult.setNodeIgnoredBy(clinician)
				nodeResult.setNodeIgnoredReason(reason)
				nodeResult.save()
			}
		} else {
			completedQuestionnaire._questionnaireIgnored = false
			completedQuestionnaire.questionnaireIgnoredReason = null
			completedQuestionnaire.questionnareIgnoredBy = null
			completedQuestionnaire.save()
			completedQuestionnaire.completedQuestions.each {nodeResult ->
				nodeResult.setNodeIgnored(false)
				nodeResult.setNodeIgnoredBy(null)
				nodeResult.setNodeIgnoredReason(null)
				nodeResult.save()
			}
		}
		
		completedQuestionnaire.save()
		redirect(controller: "patient", action: "questionnaire", params: [id: params.id, ignoreNavigation: 'true'])
	}

    @Secured(PermissionName.NODE_RESULT_IGNORE)
	def toggleIgnoreNode() {
		def node = NodeResult.find("from NodeResult as node where node.id=(:nID)", [nID:Long.parseLong(params.resultID)])

		if(node?.nodeIgnored) {
			//If we un-ignore atleast one node, the entire questionnaire cannot be ignored
			node?.completedQuestionnaire._questionnaireIgnored = false
			node?.completedQuestionnaire.questionnaireIgnoredReason = null
			node?.completedQuestionnaire.questionnareIgnoredBy = null

			node.setNodeIgnored(false)
			node.setNodeIgnoredBy(null)
			node.setNodeIgnoredReason(null)
		} else {
			def user = springSecurityService.currentUser
			def clinician = Clinician.findByUser(user)
			node?.setNodeIgnored(true)
			node?.setNodeIgnoredBy(clinician)
			node?.setNodeIgnoredReason(params.ignoreNodeReason)
		}
		node?.save()
	}


}