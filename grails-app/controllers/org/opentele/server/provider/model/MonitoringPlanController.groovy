package org.opentele.server.provider.model

import grails.plugin.springsecurity.annotation.Secured

import org.opentele.server.model.MonitoringPlan
import org.opentele.server.model.Patient
import org.opentele.server.core.model.types.PermissionName;

@Secured(PermissionName.NONE)
class MonitoringPlanController {
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def questionnaireProviderService
    def sessionService
    def patientService

    @Secured(PermissionName.MONITORING_PLAN_CREATE)
    def create() {
        Patient patientInstance = Patient.findById(params.patientId)
        if(patientInstance) {
            sessionService.setPatient(session, patientInstance)
        }

        [
            monitoringPlanInstance: new MonitoringPlan(params),
            patientInstance: patientInstance
        ]
    }

    @Secured(PermissionName.MONITORING_PLAN_CREATE)
    def save() {
        params.startDate = params.datePicker
        Patient p = Patient.get(session.patientId)
        def monitoringPlanInstance = new MonitoringPlan(params)
        monitoringPlanInstance.patient = p
        p.monitoringPlan = monitoringPlanInstance

        if (!monitoringPlanInstance.save(flush: true, failOnError: true)) {
            render(view: "create", model: [monitoringPlanInstance: monitoringPlanInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'monitoringPlan.label')])
        redirect(action: "show", id: p.id)
    }

    @Secured(PermissionName.MONITORING_PLAN_READ)
    def show() {
        Patient patient = Patient.get(params.id)

        sessionService.setPatient(session, patient)
        def monitoringPlanInstance = patient.monitoringPlan
        def canAddMoreSchedules = true
        if (monitoringPlanInstance != null) {
            canAddMoreSchedules = !questionnaireProviderService.getUnusedQuestionnaireHeadersForMonitoringPlan(monitoringPlanInstance).empty
        }
        [monitoringPlanInstance: monitoringPlanInstance, canAddMoreSchedules: canAddMoreSchedules, patientInstance: patient]
    }

    @Secured(PermissionName.MONITORING_PLAN_WRITE)
    def edit() {
        Patient patientInstance = Patient.findById(params.patientId)
        if (patientInstance) {
            sessionService.setPatient(session, patientInstance)
        }

        def monitoringPlanInstance = MonitoringPlan.get(params.id)
        if (!monitoringPlanInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'monitoringPlan.label', default: 'MonitoringPlan')])
            redirect(action: "list")
            return
        }

        [monitoringPlanInstance: monitoringPlanInstance]
    }

    @Secured(PermissionName.MONITORING_PLAN_WRITE)
    def update() {
        def monitoringPlanInstance = MonitoringPlan.get(params.id)
        if (!monitoringPlanInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'monitoringPlan.label', default: 'MonitoringPlan')])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (monitoringPlanInstance.version > version) {
                monitoringPlanInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'monitoringPlan.label', default: 'MonitoringPlan')] as Object[],
                        "Another user has updated this MonitoringPlan while you were editing")
                render(view: "edit", model: [monitoringPlanInstance: monitoringPlanInstance])
                return
            }
        }

        if (params.datePicker) {
            params.startDate = params.datePicker
        }

        monitoringPlanInstance.properties = params

        if (!monitoringPlanInstance.save(flush: true)) {
            render(view: "edit", model: [monitoringPlanInstance: monitoringPlanInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'monitoringPlan.label', default: 'MonitoringPlan')])
        redirect(action: "show", id: monitoringPlanInstance.patient.id)
    }

    @Secured(PermissionName.MONITORING_PLAN_DELETE)
    def delete() {
        //It should not be possible to actually delete the a plan from the DB (its clinical data)
        def monitoringPlanInstance = MonitoringPlan.get(params.id)
        def patient = Patient.get(monitoringPlanInstance.patient.id)
        patient.monitoringPlan = null
        patient.endedMonitoringPlans.add(monitoringPlanInstance)
        patient.save(failOnError:true)

        //"Delete" all patientQuestionnaires
        monitoringPlanInstance.questionnaireSchedules.each {
            //it.patientQuestionnaire.deleted = true
            it.patientQuestionnaire.save(failOnError: true)
        }

        redirect(action: "show", id:patient.id)
    }
}
