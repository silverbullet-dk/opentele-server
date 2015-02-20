package org.opentele.server.provider

import org.codehaus.groovy.grails.commons.metaclass.GroovyDynamicMethodsInterceptor
import org.codehaus.groovy.grails.web.metaclass.BindDynamicMethod
import org.opentele.server.core.command.BloodPressureThresholdCommand
import org.opentele.server.core.command.NumericThresholdCommand
import org.opentele.server.core.command.ThresholdCommand
import org.opentele.server.core.command.UrineGlucoseThresholdCommand
import org.opentele.server.core.command.UrineThresholdCommand
import org.opentele.server.model.*
import org.opentele.server.core.model.types.MeasurementTypeName

class ThresholdService {
    // From here: http://nerderg.com/Grails - makes the bindData available for services too
    ThresholdService() {
        GroovyDynamicMethodsInterceptor i = new GroovyDynamicMethodsInterceptor(this)
        i.addDynamicMethodInvocation(new BindDynamicMethod())
    }

    ThresholdCommand getThresholdCommandForEdit(Threshold threshold) {
        if (!threshold) {
            return null
        }
        ThresholdCommand thresholdCommand
        switch (threshold.type.name) {
            case MeasurementTypeName.URINE:
                thresholdCommand = new UrineThresholdCommand()
                break
            case MeasurementTypeName.URINE_GLUCOSE:
                thresholdCommand = new UrineGlucoseThresholdCommand()
                break
            case MeasurementTypeName.BLOOD_PRESSURE:
                thresholdCommand = new BloodPressureThresholdCommand()
                break
            default:
                thresholdCommand = new NumericThresholdCommand()
        }
        thresholdCommand.threshold = threshold
        bindFromThreshold(thresholdCommand)

        return thresholdCommand
    }

    void updateThreshold(ThresholdCommand thresholdCommand) {
        if (thresholdCommand.validate()) {
            bindToThreshold(thresholdCommand)
            thresholdCommand.threshold.save(failOnError: true)
        }
    }

    Threshold createThreshold(Map params) {
        MeasurementType measurementType = MeasurementType.findByName(params.type as MeasurementTypeName)
        ThresholdCommand thresholdCommand

        switch (measurementType.name) {
            case MeasurementTypeName.BLOOD_PRESSURE:
                thresholdCommand = new BloodPressureThresholdCommand(threshold: new BloodPressureThreshold())
                break
            case MeasurementTypeName.URINE:
                thresholdCommand = new UrineThresholdCommand(threshold: new UrineThreshold())
                break
            case MeasurementTypeName.URINE_GLUCOSE:
                thresholdCommand = new UrineGlucoseThresholdCommand(threshold: new UrineGlucoseThreshold())
                break
            default:
                thresholdCommand = new NumericThresholdCommand(threshold: new NumericThreshold())
        }
        thresholdCommand.threshold.type = measurementType
        bindData(thresholdCommand.threshold, params, [include: thresholdCommand.bindableProperties])

        return thresholdCommand.threshold
    }

    private bindFromThreshold(ThresholdCommand command) {
        command.bindableProperties.each {
            command[it] = command.threshold[it]
        }
    }

    private bindToThreshold(ThresholdCommand command) {
        command.bindableProperties.each {
            command.threshold[it] = command[it]
        }
    }
}
