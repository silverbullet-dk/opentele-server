package org.opentele.server.provider.model

import grails.plugin.springsecurity.annotation.Secured

import org.opentele.server.model.Clinician
import org.opentele.server.model.Department
import org.opentele.server.model.Message
import org.opentele.server.model.Patient
import org.opentele.server.core.model.types.PermissionName

@Secured(PermissionName.NONE)
class MessageController {

    def springSecurityService
    def messageService
    def clinicianMessageService
    def sessionService
    def patientService

    static allowedMethods = [save: "POST", update: "POST", delete: ["POST": "DELETE"]]

    @Secured([PermissionName.MESSAGE_CREATE])
    def create() {
        def user = springSecurityService.currentUser
        def patient
        def clinician
        def msg = new Message()
        def departments = []

        if (params.receipientId) {
            patient = Patient.get(params.receipientId)
        }

        if (user) {
            clinician = Clinician.findByUser(user)
        }

        if (patient != null && clinician != null) {
            departments = clinicianMessageService.legalMessageSendersForClinicianToPatient(clinician, patient)
        }

        if (patient) {
            sessionService.setPatient(session, patient)
        }
        [messageInstance: msg, patient: patient, departments: departments]
    }

    @Secured([PermissionName.MESSAGE_WRITE])
    def reply() {
        def msg = Message.get(params.id)

        def newMessage = new Message(patient:msg.patient, department: msg.department, title: replyTitle(msg.title), inReplyTo: msg)
        messageService.setRead(msg)
        sessionService.setPatient(session, msg.patient)
        [messageInstance:newMessage, oldMessage:msg]
    }

    @Secured([PermissionName.MESSAGE_WRITE])
    def save() {

        params.patient = Patient.get(params.patient)
        params.department = Department.get(params.department)
        params.inReplyTo = Message.get(params.inReplyTo)

        def messageInstance = messageService.saveMessage(params)

        if (!messageInstance.hasErrors()) {
            withFormat {
                html {
                    flash.message = message(code: 'default.created.message', args: [message(code: 'message.label', default: 'Message'), messageInstance.id])
                    redirect(controller: "patient", action: "messages", id: messageInstance.patient.id)
                }
                form {
                    flash.message = message(code: 'default.created.message', args: [message(code: 'message.label', default: 'Message'), messageInstance.id])
                    redirect(action: "show", id: messageInstance.id)
                }
            }
        } else {
            withFormat {
                form {
                    render(view: "create", model: [messageInstance: messageInstance])
                }
            }
        }
    }

    @Secured([PermissionName.MESSAGE_WRITE,PermissionName.MESSAGE_WRITE_JSON ]) // TODO: Remove JSON permission?
    def read() {
        def messageInstance = Message.get(params.id)
        messageService.setRead(messageInstance)
        redirect(controller: "patient", action: "messages", id: messageInstance.patient.id)
    }

    @Secured([PermissionName.MESSAGE_WRITE,PermissionName.MESSAGE_WRITE_JSON ]) // TODO: Remove JSON permission?
    def unread() {

        def messageInstance = Message.get(params.id)
        def res

        if (messageInstance.sentByPatient) {
            res = messageService.setUnRead(messageInstance)
        }

        withFormat {
            html {
//					flash.message = message(code: 'default.created.message', args: [message(code: 'message.label', default: 'Message'), messageInstance.id])
                redirect(controller: "patient", action: "messages", id: messageInstance.patient.id)
            }
            form {
//					flash.message = message(code: 'default.created.message', args: [message(code: 'message.label', default: 'Message'), messageInstance.id])
//					redirect(action: "show", id: messageInstance.id)
                redirect(controller: "patient", action: "messages", id: messageInstance.patient.id)
            }
        }

        if (!messageInstance) {
            withFormat renderNotFound
        } else {
            withFormat {
                html { [messageInstance: messageInstance] }
            }
        }
    }

    private renderNotFound = {
        html {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'message.label', default: 'Message')])}"
            list()
        }
        form {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'message.label', default: 'Message')])}"
            list()
        }
    }

    private String replyTitle(String title) {

        if (title) {
            title.startsWith("Re:") ? title  : "Re: " + title
        } else {
            "Re: "
        }
    }
}