package org.opentele.server.provider.model

import grails.plugin.springsecurity.annotation.Secured
import org.opentele.server.provider.ThresholdService

import org.opentele.server.model.BloodPressureThreshold
import org.opentele.server.model.NumericThreshold
import org.opentele.server.model.StandardThresholdSet
import org.opentele.server.model.Threshold
import org.opentele.server.model.UrineGlucoseThreshold
import org.opentele.server.model.UrineThreshold
import org.opentele.server.core.model.types.PermissionName
import org.springframework.dao.DataIntegrityViolationException

import static org.opentele.server.core.model.types.MeasurementTypeName.*

@Secured(PermissionName.NONE)
class StandardThresholdSetController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST", addThreshold: "POST"]

    def sessionService
    ThresholdService thresholdService
    def measurementTypeService

    @Secured(PermissionName.STANDARD_THRESHOLD_READ_ALL)
    def index() {
        redirect(action: "list", params: params)
    }

    @Secured(PermissionName.STANDARD_THRESHOLD_READ_ALL)
    def list() {
        def list = StandardThresholdSet.list(fetch: [thresholds: "eager"])
        [standardThresholdSetInstanceList: list]
    }

    @Secured(PermissionName.STANDARD_THRESHOLD_CREATE)
    def save() {
        def standardThresholdSetInstance = new StandardThresholdSet(params)
        if (!standardThresholdSetInstance.save(flush: true)) {
            render(view: "create", model: [standardThresholdSetInstance: standardThresholdSetInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'standardThresholdSet.label', default: 'StandardThresholdSet'), standardThresholdSetInstance.id])
        redirect(action: "list")
    }

    @Secured(PermissionName.STANDARD_THRESHOLD_WRITE)
    def edit() {
        sessionService.setAccessTokens(session)
        def standardThresholdSetInstance = StandardThresholdSet.get(params.id)
        if (!standardThresholdSetInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'standardThresholdSet.label', default: 'StandardThresholdSet'), params.id])
            redirect(action: "list")
            return
        }

        [
            standardThresholdSetInstance: standardThresholdSetInstance,
            patientGroup: standardThresholdSetInstance.patientGroup
        ]
    }

    @Secured(PermissionName.STANDARD_THRESHOLD_WRITE)
    def update() {
        def standardThresholdSetInstance = StandardThresholdSet.get(params.id)
        if (!standardThresholdSetInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'standardThresholdSet.label', default: 'StandardThresholdSet'), params.id])
            redirect(action: "list")
            return
        }

        if (params.version) {
            def version = params.version.toLong()
            if (standardThresholdSetInstance.version > version) {
                standardThresholdSetInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                        [message(code: 'standardThresholdSet.label', default: 'StandardThresholdSet')] as Object[],
                        "Another user has updated this StandardThresholdSet while you were editing")
                render(view: "edit", model: [standardThresholdSetInstance: standardThresholdSetInstance])
                return
            }
        }

        standardThresholdSetInstance.properties = params

        if (!standardThresholdSetInstance.save(flush: true)) {
            render(view: "edit", model: [standardThresholdSetInstance: standardThresholdSetInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'standardThresholdSet.label', default: 'StandardThresholdSet'), standardThresholdSetInstance.id])
        redirect(action: "list")
    }

    @Secured(PermissionName.STANDARD_THRESHOLD_WRITE)
    def addThreshold() {
        sessionService.setAccessTokens(session)
        def standardThresholdSetInstance = StandardThresholdSet.get(params.id)
        Threshold t
        if (params.thresholdtype == BLOOD_PRESSURE.toString()) {
            t = new BloodPressureThreshold()
        } else if (params.thresholdtype == URINE.toString()) {
            t = new UrineThreshold()
        } else if (params.thresholdtype == URINE_GLUCOSE.toString()) {
            t = new UrineGlucoseThreshold()
        } else {
            t = new NumericThreshold()
        }
        render(view: "create", model: [
                standardThresholdSetInstance: standardThresholdSetInstance,
                standardThresholdInstance: t,
                thresholdType: params.thresholdtype,
                notUsedThresholds: measurementTypeService.getUnusedMeasurementTypesForThresholds(standardThresholdSetInstance.thresholds*.type*.name),
                patientGroup: standardThresholdSetInstance.patientGroup
        ])
    }

    @Secured(PermissionName.STANDARD_THRESHOLD_WRITE)
    def chooseThreshold(String type) {
        log.debug("ChooseThreshold $type")
        switch (type) {
            case BLOOD_PRESSURE.name():
                render(template: '/bloodPressureThreshold/form', model: [standardThresholdInstance: new BloodPressureThreshold()])
                break
            case URINE.name():
                render(template: '/urineThreshold/form', model: [standardThresholdInstance: new UrineThreshold()])
                break
            case URINE_GLUCOSE.name():
                render(template: '/urineGlucoseThreshold/form', model: [standardThresholdInstance: new UrineGlucoseThreshold()])
                break
            default:
                render(template: '/numericThreshold/form', model: [standardThresholdInstance: new NumericThreshold()])
        }
    }

    @Secured(PermissionName.STANDARD_THRESHOLD_DELETE)
    def removeThreshold(Long id, Long threshold) {
        def standardThresholdSetInstance = StandardThresholdSet.get(id)
        def thresholdInstance = Threshold.get(threshold)

        standardThresholdSetInstance.removeFromThresholds(thresholdInstance)
        standardThresholdSetInstance.save()
        thresholdInstance.delete()
        redirect(action: "list", fragment: "${standardThresholdSetInstance.id}")

    }

    @Secured(PermissionName.STANDARD_THRESHOLD_WRITE)
    def saveThresholdToSet(Long id) {
        def standardThresholdSetInstance = StandardThresholdSet.get(id)
        Threshold threshold = thresholdService.createThreshold(params)

        if (!threshold.validate()) {
            render(view:  "create", model: [standardThresholdSetInstance: standardThresholdSetInstance,
                                            standardThresholdInstance: threshold,
                                            thresholdType: threshold.type.name,
                                            notUsedThresholds: measurementTypeService.getUnusedMeasurementTypesForThresholds(standardThresholdSetInstance.thresholds*.type*.name),
                                            patientGroup: standardThresholdSetInstance.patientGroup])
            return
        }

        standardThresholdSetInstance.addToThresholds(threshold)
        if(!standardThresholdSetInstance.save()) {
            render(view: "list", model:  [standardThresholdSetInstance: standardThresholdSetInstance, standardThresholdSetInstanceList: StandardThresholdSet.findAll()])
        } else {
            redirect(action:  "list", fragment: "${standardThresholdSetInstance.id}")
        }
    }

    /*
    delete() will only empty the group, not remove it
     */
    @Secured(PermissionName.STANDARD_THRESHOLD_DELETE)
    def delete() {
        def standardThresholdSetInstance = StandardThresholdSet.get(params.id)
        if (!standardThresholdSetInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'standardThresholdSet.label', default: 'StandardThresholdSet'), params.id])
            redirect(action: "list")
            return
        }
        try {
            standardThresholdSetInstance.thresholds.clear()

            flash.message = message(code: 'default.deleted.message', args: [message(code: 'standardThresholdSet.label', default: 'StandardThresholdSet'), params.id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'standardThresholdSet.label', default: 'StandardThresholdSet'), params.id])
            redirect(action: "list")
        }
    }
}
