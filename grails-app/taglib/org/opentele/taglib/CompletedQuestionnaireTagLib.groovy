package org.opentele.taglib

import grails.plugin.springsecurity.SpringSecurityService
import grails.plugin.springsecurity.SpringSecurityUtils
import groovy.xml.MarkupBuilder
import org.opentele.server.core.QuestionnaireService
import org.opentele.server.core.util.MeasurementResult
import org.opentele.server.provider.ClinicianMessageService
import org.opentele.server.provider.ClinicianService
import org.opentele.server.provider.MeasurementService
import org.opentele.server.provider.PatientService
import org.opentele.server.core.util.TimeFilter
import org.opentele.server.model.*
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import org.opentele.server.core.model.types.MeasurementTypeName
import org.opentele.server.core.model.types.NoteType
import org.opentele.server.core.model.types.PermissionName
import org.opentele.server.core.model.types.Severity
import org.opentele.server.core.model.types.Unit
import org.opentele.server.core.MeasurementDescription
import org.opentele.server.core.MeasurementParentType

import org.opentele.server.core.OverviewColumnHeader
import org.opentele.server.core.ResultKey
import org.opentele.server.core.util.NumberFormatUtil

class CompletedQuestionnaireTagLib {
	static namespace = "cq"

	QuestionnaireService questionnaireService
    PatientService patientService
    MeasurementService measurementService
    SpringSecurityService springSecurityService
    ClinicianMessageService clinicianMessageService
    ClinicianService clinicianService

	def renderResultTableForPatient = { attrs, body ->
        def withPrefs = attrs.withPrefs
		def patientID = attrs.patientID
        def resultModel = attrs.completedQuestionnaireResultModel
        def builder = new MarkupBuilder(out)

        if (resultModel.results.size() == 0) {
            // No measurements fetched
            builder.div(style: "margin: 10px 0 20px;", g.message(code: "patient.questionnaire.noMeasurementsForPeriod"))

        } else {
            // Build html
            builder.div(id: "resultsContainer", class: "scrollable") {
                table(cellspacing: 0) {
                    tbody {
                        tr {
                            // Upper-left
                            buildOuterCellHeader(builder, "staticHeader", {
                                builder.th(g.message(code: "default.questions"))
                            })
                            // Upper-right
                            buildOuterCellHeader(builder, "headerContainer", {
                                resultModel.columnHeaders.each { questionnaireMetaData ->
                                    builder.th {
                                        renderHeaderForThisType(builder, questionnaireMetaData)
                                    }
                                }
                            })
                        }
                        tr {
                            // Lower-left
                            def args =  [class: "questionTable" + patientID, 'data-bind': "template:{name:\"prefRowTemplate\", foreach: prefRows}"]
                            buildOuterCellBody(builder, "leftHeaderContainer", withPrefs, args, {
                                resultModel.questions.each { question ->
                                    renderQuestionForType(builder, question, patientID)
                                }
                            })
                            // Lower-right
                            args =  [class: "resultsTable" + patientID, 'data-bind': "template:{name:\"prefRowResTemplate\", foreach: prefResRows}"]
                            buildOuterCellBody(builder, "resultTableContainer", withPrefs, args, {
                                for (int i = 0; i < resultModel.questions.size(); i++) {
                                    tr(name: resultModel.questions[i].templateQuestionnaireNodeId, class: "result") {
                                        for(int j = 0; j < resultModel.columnHeaders.size(); j++) {
                                            def key = getKeyForThisCell(resultModel.questions[i], resultModel.columnHeaders[j])

                                            if(resultModel.results[key]) {
                                                renderMeasurementCell(builder, resultModel.results[key], key.type)
                                            } else {
                                                builder.td {
                                                    builder.div()
                                                }
                                            }
                                        }
                                    }
                                }
                            })
                        }
                    }
                }
            }
        }
	}

    private void buildOuterCellHeader(MarkupBuilder builder, containerID, innerContent) {
        builder.td(class: "outerCell") {
            builder.div(class:"tableContainer", id: containerID) {
                builder.table {
                    builder.thead {
                        builder.tr {
                            innerContent()
                        }
                    }
                }
            }
        }
    }

    private void buildOuterCellBody(MarkupBuilder builder, containerID, withPrefs, args, innerContent) {
        builder.td(class: "outerCell") {
            builder.div(class:"tableContainer", id: containerID) {
                builder.table(cellspacing: 0) {
                    builder.tbody(withPrefs ? [class: args['class'], "data-bind": args['data-bind']] : [class: args['class']]){
                        innerContent()
                    }
                }
            }
        }
    }

    private void renderQuestionForType(MarkupBuilder builder, MeasurementDescription measurementDescription, patientID) {
        switch (measurementDescription.type) {
            case MeasurementParentType.QUESTIONNAIRE:
                renderQuestionForQuestionnaire(builder, measurementDescription, patientID)
                break;
            case MeasurementParentType.CONFERENCE:
                renderQuestionForConference(builder, measurementDescription, patientID)
                break;
            case MeasurementParentType.CONSULTATION:
                renderQuestionForConsultation(builder, measurementDescription, patientID)
                break;
            default:
                throw new IllegalArgumentException("Unknown measurementParentType: ${measurementDescription.type}")
        }
    }

    private void renderQuestionForConsultation(MarkupBuilder builder, MeasurementDescription measurementDescription, patientID) {
        def thresholdTooltipMessage = getThresholdValuesForQuestion(patientID as Long, measurementDescription.measurementTypeNames)
        def tooltip = message(code: "result.table.question.tooltip.consultation", args: [measurementDescription.questionnaireName])
        tooltip = thresholdTooltipMessage ? tooltip + "<br/>" + thresholdTooltipMessage : tooltip
        def units = measurementDescription.units ? measurementDescription.orderedUnits() : null
        def cellText = message(code: "enum.measurementType." + measurementDescription.measurementTypeNames?.find{true})

        renderQuestionCell(builder, tooltip, cellText, null, units)
    }

    private void renderQuestionForConference(MarkupBuilder builder, MeasurementDescription measurementDescription, patientID) {
        def thresholdTooltipMessage = getThresholdValuesForQuestion(patientID as Long, measurementDescription.measurementTypeNames)
        def tooltip = message(code: "result.table.question.tooltip.conference", args: [measurementDescription.questionnaireName])
        tooltip = thresholdTooltipMessage ? tooltip + "<br/>" + thresholdTooltipMessage : tooltip
        def units = measurementDescription.units ? measurementDescription.orderedUnits() : null
        def cellText = message(code: "enum.measurementType." + measurementDescription.measurementTypeNames?.find{true})

        renderQuestionCell(builder, tooltip, cellText, null, units)
    }

    private void renderQuestionForQuestionnaire(MarkupBuilder builder, MeasurementDescription measurementDescription, patientID) {
        def thresholdTooltipMessage = getThresholdValuesForQuestion(patientID as Long, measurementDescription.measurementTypeNames)
        def tooltip = message(code: "result.table.question.tooltip.questionnaire", args: [measurementDescription.questionnaireName])
        tooltip = thresholdTooltipMessage ? tooltip + "<br/>" + thresholdTooltipMessage : tooltip
        def units = measurementDescription.units ? measurementDescription.orderedUnits() : null
        def cellText = measurementDescription.text
        def args = [questionnaireName: measurementDescription.questionnaireName, templateQuestionnaireNodeId: measurementDescription.templateQuestionnaireNodeId]

        renderQuestionCell(builder, tooltip, cellText, args, units)
    }

    private void renderQuestionCell(MarkupBuilder builder, tooltip, cellText, args, units) {
        builder.tr {
            builder.td('data-tooltip': tooltip) {
                builder.div(args ? [class: "question", questionnaireName: args['questionnaireName'], name: "question", id: args['templateQuestionnaireNodeId']] : [class: "question"]) {
                    builder.b(cellText)
                    if (units != null) {
                       builder.getMkp().yieldUnescaped(buildUnitString(units))
                    }
                }
            }
        }
    }

    private String buildUnitString(units) {
        def unitString = " ("

        def separator = "/"

        if (units != null && units.containsAll([Unit.BPM, Unit.MMHG])) {
            separator = ", "
        }

        units.eachWithIndex { unit, idx ->
            if (idx > 0) {
                unitString += separator
            }
            unitString += message(code: "enum.unit." + unit)
        }
        unitString += ")"

        return unitString
    }

    ResultKey getKeyForThisCell(MeasurementDescription question, OverviewColumnHeader columnHeader) {
        switch (columnHeader.type) {
            case MeasurementParentType.CONSULTATION:
                if(question.type == MeasurementParentType.CONSULTATION) {
                    // TODO: Den holder nu, men er på ingen måde pæn!
                    return new ResultKey(rowId: "cons-${question.measurementTypeNames.find{true}}", colId: "cons-${columnHeader.id}", type: MeasurementParentType.CONSULTATION)
                } else {
                    return new ResultKey(rowId: "", colId: "")
                }
                break;
            case MeasurementParentType.QUESTIONNAIRE:
                return new ResultKey(rowId: "cq-${question.templateQuestionnaireNodeId}", colId: "cq-${columnHeader.id}", type: MeasurementParentType.QUESTIONNAIRE)
            case MeasurementParentType.CONFERENCE:
                if(question.type == MeasurementParentType.CONFERENCE) {
                    // TODO: Den holder nu, men er på ingen måde pæn!
                    return new ResultKey(rowId: "conf-${question.measurementTypeNames.find{true}}", colId: "conf-${columnHeader.id}", type: MeasurementParentType.CONFERENCE)
                } else {
                    return new ResultKey(rowId: "", colId: "")
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown measurementParentType: ${columnHeader.type}")
        }
    }

    private void renderMeasurementCell(MarkupBuilder builder, List<MeasurementResult> measurementResultList, MeasurementParentType parentType) {
        switch(parentType) {
            case MeasurementParentType.QUESTIONNAIRE:
                renderMeasurementCellForQuestionnaire(builder, measurementResultList)
                break;
            case MeasurementParentType.CONFERENCE:
                renderMeasurementCellForConference(builder, measurementResultList)
                break;
            case MeasurementParentType.CONSULTATION:
                renderMeasurementCellForConsultation(builder, measurementResultList)
                break;
            default:
                throw new IllegalArgumentException("Unknown measurementParentType: ${parentType}")
        }
    }

    private void renderMeasurementCellForQuestionnaire(MarkupBuilder builder, List<MeasurementResult> measurementResultList) {
        def measurementResult = measurementResultList.get(0)
        if (measurementResultList.size() > 1) {
            measurementResult.value = measurementResultList*.value.join(",")
        }
        def prettyResultString = getPrettyResultString(measurementResult)

        renderCellContents(builder, prettyResultString as String, {
            def measurementContent = {
                renderSeverityIcon(builder, measurementResult)
                builder.getMkp().yieldUnescaped(prettyResultString)
            }

            //If any of the measurements are ignored they are all ignored
            if (measurementResult.ignored) {
                builder.del {
                    measurementContent()
                }
            } else {
                measurementContent()
            }
        })
    }

    private void renderMeasurementCellForConference(MarkupBuilder builder, List<MeasurementResult> measurementResultList) {
        String results = measurementResultList.collect {getPrettyResultString(it)}.join(" <br/> ")
        renderCellContents(builder, results, {
            builder.getMkp().yieldUnescaped(results)
        })
    }

    private void renderMeasurementCellForConsultation(MarkupBuilder builder, List<MeasurementResult> measurementResultList) {
        String results = measurementResultList.collect {getPrettyResultString(it)}.join(" <br/> ")
        renderCellContents(builder, results, {
            builder.getMkp().yieldUnescaped(results)
        })
    }

    private void renderCellContents(MarkupBuilder builder, tooltipText, cellContent) {
        def toolTip = tooltipForMeasurementResult(tooltipText)
        builder.td {
            builder.div(toolTip ? ['data-tooltip': toolTip] : [:]) {
                cellContent()
            }
        }
    }

    private String tooltipForMeasurementResult(String resultString) {
      if(resultString.length() > 15) {
        return resultString
      }
    }

    private void renderHeaderForThisType(MarkupBuilder builder, OverviewColumnHeader columnHeader) {
        MeasurementParentType columnType = columnHeader.type
        switch (columnType) {
            case MeasurementParentType.CONFERENCE:
                renderHeaderForConference(builder, columnHeader)
                break;
            case MeasurementParentType.CONSULTATION:
                renderHeaderForConsultation(builder, columnHeader)
                break;
            case MeasurementParentType.QUESTIONNAIRE:
                renderHeaderForQuestionnaire(builder, columnHeader)
                break;
            default:
                throw new IllegalArgumentException("Unknown measurementParentType: ${columnType}")
        }
    }

    private void renderHeaderForConsultation(MarkupBuilder builder, OverviewColumnHeader columnHeader) {
        builder.div(columnHeader.uploadDate.toCalendar().format(g.message(code:"default.date.format.short"))) {
            builder.br()
            def conference_image = """<img src=${g.resource(dir: 'images', file: 'consultationaddmeasurements.png')} data-tooltip="${g.message(code: 'patientOverview.measurementsFromConsultation')}"/>"""
            builder.getMkp().yieldUnescaped(g.link(controller:"patient", action:"consultation", id:columnHeader.id, conference_image))
        }
    }

    private void renderHeaderForConference(MarkupBuilder builder, OverviewColumnHeader columnHeader) {
        builder.div(columnHeader.uploadDate.toCalendar().format(g.message(code:"default.date.format.short"))) {
            builder.br()
            def conference_image = """<img src=${g.resource(dir: 'images', file: 'conferenceshow.png')} data-tooltip="${g.message(code: 'patientOverview.measurementsFromConference')}"/>"""
            builder.getMkp().yieldUnescaped(g.link(controller:"patient", action:"conference", id:columnHeader.id, conference_image))
        }
    }

    private void renderHeaderForQuestionnaire(MarkupBuilder builder, columnHeader) {
        builder.div(columnHeader.uploadDate.toCalendar().format(g.message(code:"default.date.format.short"))) {
            builder.br()
            builder.img(src: g.resource(dir: "images", file: columnHeader.severity.icon()))
            def img_edit = """<img src=${g.resource(dir: "images", file: "edit.png")} data-tooltip="${g.message(code:'patientOverview.edit')}"/>"""
            builder.getMkp().yieldUnescaped(g.link(controller:"patient", action:"questionnaire", id:columnHeader.id, img_edit))

            if (columnHeader.acknowledgedBy) {
                def tooltip = g.message(code:"patientOverview.acknowledgedBy", args: [columnHeader.acknowledgedBy, columnHeader.acknowledgedDate.format(message(code: "default.date.format")).toString()]) + (columnHeader.acknowledgedNote? "\\n${g.message(code: 'patientOverview.note', args: [columnHeader.acknowledgedNote])}" : "")
                builder.img(src: g.resource(dir: "images", file: "acknowledged.png"), 'data-tooltip': tooltip)

            } else {
                builder.a(href: "#", class: "acknowledge", 'data-automessage': "false", 'data-questionnaire-id': columnHeader.id) {
                    builder.img(src: g.resource(dir: "images", file: "unacknowledged.png"), 'data-tooltip': g.message(code: 'patientOverview.acknowledge'))
                }

                def autoMessageEnabledForPatient = clinicianMessageService.autoMessageIsEnabledForCompletedQuestionnaire(columnHeader.id)
                if (autoMessageEnabledForPatient) {
                    builder.a(href: "#", class: "acknowledge", 'data-automessage': "true", 'data-questionnaire-id': columnHeader.id) {
                        renderAutoMessageSingleIcon(builder, autoMessageEnabledForPatient)
                    }
                } else {
                    //Just the image
                    renderAutoMessageSingleIcon(builder, autoMessageEnabledForPatient)
                }
            }
        }
    }

    private String getAutoMessageAllIcon(boolean autoMessageEnabledForPatient) {
        if (autoMessageEnabledForPatient) {
            "<img src=${g.resource(dir: 'images', file: 'unacknowledgedWithAutoMessage.png')} data-tooltip='${message(code: 'patientOverview.acknowledgeAllWithMessage')}'>"
        } else {
            "<img src=${g.resource(dir: 'images', file: 'unacknowledgedWithAutoMessageDisabled.png')} data-tooltip='${message(code: 'patientOverview.acknowledgeAllWithMessage.disabled')}'>"
        }
    }

    private void renderAutoMessageSingleIcon(MarkupBuilder builder, boolean autoMessageEnabledForPatient) {
        if (autoMessageEnabledForPatient) {
            builder.img(src: g.resource(dir: 'images', file: 'unacknowledgedWithAutoMessage.png'), 'data-tooltip': message(code: 'patientOverview.acknowledgeWithMessage'))
        } else {
            builder.img(src: g.resource(dir: 'images', file: 'unacknowledgedWithAutoMessageDisabled.png'), 'data-tooltip': message(code: 'patientOverview.acknowledgeAllWithMessage.disabled'))
        }
    }

    private String getThresholdValuesForQuestion(Long patientId, Set<MeasurementTypeName> types) {
        Patient p = Patient.get(patientId)
        def tooltipText = ""

        types.each { type ->
            switch (type) {
                case MeasurementTypeName.BLOOD_PRESSURE:
                    BloodPressureThreshold bpThresh = p.getThreshold(type)
                    if (bpThresh) {
                        def argsSystolic = [getSafeThresholdString(bpThresh.systolicAlertHigh), getSafeThresholdString(bpThresh.systolicWarningHigh),
                                            getSafeThresholdString(bpThresh.systolicWarningLow), getSafeThresholdString(bpThresh.systolicAlertLow)]
                        tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.${type as String}.systolic")
                        tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.values", args: argsSystolic)

                        def argsDiastolic = [getSafeThresholdString(bpThresh.diastolicAlertHigh), getSafeThresholdString(bpThresh.diastolicWarningHigh),
                                             getSafeThresholdString(bpThresh.diastolicWarningLow), getSafeThresholdString(bpThresh.diastolicAlertLow)]
                        tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.${type as String}.diastolic")
                        tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.values", args: argsDiastolic)
                    }
                    break;
                case MeasurementTypeName.URINE:
                    UrineThreshold t = p.getThreshold(type)
                    if (t) {
                        tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.${type as String}")
                        tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.values", args: getSafeThresholdMap(t))
                    }
                    break;
                case MeasurementTypeName.URINE_GLUCOSE:
                    UrineGlucoseThreshold t = p.getThreshold(type)
                    if (t) {
                        tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.${type as String}")
                        tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.values", args: getSafeThresholdMap(t))
                    }
                    break;
                default:
                    if (type) {
                        NumericThreshold t = p.getThreshold(type)
                        if (t) {
                            tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.${type as String}")
                            tooltipText = tooltipText + message(code: "result.table.question.tooltip.threshold.values", args: getSafeThresholdMap(t))
                        }
                    }
            }
        }
        return tooltipText
	}

    private List getSafeThresholdMap(th) {
        def resList = []
        if(th) {
            resList = [getSafeThresholdString(th.alertHigh), getSafeThresholdString(th.warningHigh), getSafeThresholdString(th.warningLow), getSafeThresholdString(th.alertLow)]
        }
        return resList
    }

    private String getSafeThresholdString(def thresholdValue) {
        if (thresholdValue != null) {
            return thresholdValue
        } else {
            return message(code: "result.table.question.tooltip.threshold.null")
        }
    }

    private void renderSeverityIcon(MarkupBuilder builder, MeasurementResult result) {
        if (result) {
            switch (result.severity) {
                case Severity.RED:
                case Severity.YELLOW:
                    builder.img(class: "measurementResultSeverityIcon", src: g.resource(dir: "images", file: result.severity.icon()))
                    break;
            }
        }
    }

    private String getPrettyResultString(MeasurementResult result) {
        def prettyString = ""
        if (result) {
            if (!result.type) {
                //InputNodeResult
                if (result.value.equals("false") || result.value == false) {
                    prettyString = message(code: "default.yesno.false")
                } else if (result.value.equals("true") || result.value == true) {
                    prettyString = message(code: "default.yesno.true")
                } else {
                    prettyString = result.value.toString().encodeAsHTML()
                }
            } else {
                //MeasurementNodeResult
                MeasurementTypeName type = MeasurementTypeName.valueOf(result.type as String)
                prettyString = NumberFormatUtil.formatMeasurementResult(result, type)
            }
        }
        return prettyString
    }

    def renderAcknowledgeAllGreenButtons = { attributes ->
        PatientOverview patientOverview = (PatientOverview) attributes['patientOverview']
        boolean messagingEnabled = clinicianMessageService.clinicianCanSendMessagesToPatient(Clinician.findByUser(springSecurityService.currentUser), patientOverview.patient)
        writeAcknowledgeAllGreenButtons(patientOverview, messagingEnabled, false)
    }

	def renderOverviewForPatient = { attributes, body ->
		PatientOverview patientOverview = (PatientOverview) attributes['patientOverview']
        List<PatientNote> patientNotes = (List<PatientNote>) attributes['patientNotes']
        boolean messagingEnabled = (boolean) attributes['messagingEnabled']
        boolean alarmIfUnreadMessagesToPatientDisabled = (boolean) attributes['alarmIfUnreadMessagesToPatientDisabled']

        def (icon, severityTooltip) = iconAndTooltip(g, patientOverview)

        MarkupBuilder builder = new MarkupBuilder(out)
        def mkp = builder.getMkp()
        builder.div(id: "questionnaireListHeader", class: "overviewStyleShadow", "") {

            // Put the (right) floating element first to prevent the IE7 float-newline bug
            writeAcknowledgeAllGreenButtons(patientOverview, messagingEnabled)
            if (patientOverview.blueAlarmText != null) {
                writeRemoveBlueAlarmsButton(builder, patientOverview.patientId)
            }

            // IF has grey alarm caused by unread messages from clinician
            if (messagingEnabled && !alarmIfUnreadMessagesToPatientDisabled && patientOverview.numberOfUnreadMessagesToPatient > 0) {
                writeNoAlarmIfUnreadMessagesToPatientButton(builder, patientOverview.patientId)
            }
            // First entry item: Patient status
            def imageArguments = [src: g.resource(dir: "images", file: icon), id: "statusIcon"]
            if (severityTooltip) {
                imageArguments['data-tooltip'] = severityTooltip
            }
            builder.img(imageArguments)

            // Second entry item: Messages to patient icon
            if (messagingEnabled) {
                def imageOUT
                def imageIN

                if (patientOverview.numberOfUnreadMessagesToPatient > 0) {
                    def tooltip = message(code: "patientOverview.unreadMessagesFromDepartment",args: [patientOverview.numberOfUnreadMessagesToPatient, formatDate(date: patientOverview.dateOfOldestUnreadMessageToPatient)])
                    imageOUT = """<img src=${g.resource(dir: "/images", file: "outboxNew.png")} id="outboxIcon" data-tooltip="$tooltip"/>"""
                } else {
                    def tooltip =message(code: "patientOverview.noUnreadMessagesFromDepartment")
                    imageOUT = """<img src=${g.resource(dir: "/images", file: "outbox.png")} id="outboxIcon" data-tooltip="$tooltip"/>"""
                }
                if (patientOverview.numberOfUnreadMessagesFromPatient > 0) {
                    def tooltip=message(code: "patientOverview.unreadMessagesFromPatient", args: [patientOverview.numberOfUnreadMessagesFromPatient, formatDate(date: patientOverview.dateOfOldestUnreadMessageFromPatient)])
                    imageIN = """<img src=${g.resource(dir: "/images", file: "inboxNew.png")} id="inboxIcon" data-tooltip="$tooltip"/>"""
                } else {
                    def tooltip=message(code: "patientOverview.noUnreadMessagesFromPatient")
                    imageIN = """<img src=${g.resource(dir: "/images", file: "inbox.png")} id="inboxIcon" data-tooltip="$tooltip"/>"""
                }
                mkp.yieldUnescaped(g.link(controller:"patient", action:"messages", id:patientOverview.patientId, imageOUT))
                mkp.yieldUnescaped(g.link(controller:"patient", action:"messages", id:patientOverview.patientId, imageIN))

            } else {
                def tooltip = message(code: "patientOverview.messagingDisabled")
                builder.img(src: g.resource(dir: "/images", file: "outbox-dimmed.png"), id: "outboxIcon", 'data-tooltip': tooltip)
                builder.img(src: g.resource(dir: "/images", file: "inbox-dimmed.png"), id: "inboxIcon", 'data-tooltip': tooltip)
            }

            // Third entry item: Patient name and CPR
            builder.div('data-tooltip': message(code: 'patientOverview.goToPatient.tooltip')) {
                builder.h2(class: "questionnaireListHeader", id: "patientName", "") {
                    mkp.yieldUnescaped(g.link(action: 'questionnaires', controller: 'patient', id: patientOverview.patientId, patientOverview.name.encodeAsHTML()))
                }
            }
            builder.div('data-tooltip': message(code: 'patientOverview.goToPatient.tooltip')) {
                builder.h2(class: "questionnaireListHeader", id: "patientCPR", "") {
                    mkp.yieldUnescaped(g.link(action: 'questionnaires', controller: 'patient', id: patientOverview.patientId, otformat.formatCPR(cpr: patientOverview.cpr)))
                }
            }

            // Fourth entry item: Expland/Collapse measurement table
            def tooltip = g.message(code: 'patientOverview.numberOfUnacknowledgedQuestionnaires', args: [patientOverview.numberOfUnacknowledgedQuestionnaires])
            builder.div('data-tooltip': tooltip) {
                builder.img(src: g.resource(dir: "images", file: 'measurements_expand.png'), class: "measurementsIcon", style: "position:center")
            }

            // Patient notes
            builder.div(class: "patientNotes", "") {
                renderPatientNoteIcon(builder, patientOverview, patientNotes)
            }
        }
    }

    def iconAndTooltip(g, PatientOverview patientOverview) {
        def severity = patientOverview.questionnaireSeverity

        if (severity == Severity.BLUE) {
            String tooltip = g.message(code: 'patientOverview.questionnairesNotAnsweredOnTime.tooltip', args: [patientOverview.blueAlarmText.replace('\n', '<br/>')])
            [severity.icon(), tooltip]
        } else if (severity != Severity.NONE) {
            [severity.icon(), g.message(code: 'patientOverview.questionnaireAlarmDetails.tooltip', args: ["${patientOverview.mostSevereQuestionnaireName}", "${g.formatDate(date: patientOverview.mostSevereQuestionnaireDate)}"])]
        } else {
            [Severity.NONE.icon(), g.message(code: 'patientOverview.noNewCompletedQuestionnairesFromPatient.tooltip')]
        }
    }

    private void renderPatientNoteIcon(MarkupBuilder builder, PatientOverview patientOverview, List<PatientNote> patientNotes) {
        def patientNoteImage = g.resource(dir:'/images', file: patientNoteImage(patientNotes))
        def patientNoteTooltip = patientNoteToolTip(patientNotes)
        def patientNoteIcon = "<img src='${patientNoteImage}' data-tooltip='${patientNoteTooltip}' id='noteIcon'/>"

        builder.getMkp().yieldUnescaped(g.link(controller:"patientNote", action:"list", id:patientOverview.patientId, patientNoteIcon))
    }

    private String patientNoteImage(List<PatientNote> patientNotes) {
        def hasUnreadImportantWithReminder = patientNotes.any { it.type == NoteType.IMPORTANT && PatientNote.isRemindToday(it) }
        def hasUnreadNormalWithReminder = patientNotes.any { it.type == NoteType.NORMAL && PatientNote.isRemindToday(it) }
        def hasUnreadImportantWithoutDeadline = patientNotes.any { it.type == NoteType.IMPORTANT && !it.reminderDate }

        if (hasUnreadImportantWithReminder) {
            'note_reminder_red.png'
        } else if (hasUnreadNormalWithReminder) {
            'note_reminder_green.png'
        } else if (hasUnreadImportantWithoutDeadline) {
            'note_important.png'
        } else {
            'note.png'
        }
    }

    private String patientNoteToolTip(List<PatientNote> patientNotes) {
        String tooltip = g.message(code: 'patientOverview.noUnreadNotes').encodeAsHTML()

        def reminders = patientNotes.findAll { PatientNote.isRemindToday(it) }
        def important = patientNotes.findAll { it.type == NoteType.IMPORTANT }
        def numReminders = reminders.size()
        def numImportant = important.size()
        def numUnread = patientNotes.size()

        if (numUnread + numReminders + numImportant > 0) {
            tooltip = g.message(code: 'patientOverview.numberOfUnreadNotesAndReminders', args: [numUnread, numReminders, numImportant])
            if (numReminders > 0) {
                tooltip = "${tooltip}<br/><strong>${g.message(code: 'patientOverview.reminders')}</strong>"
                reminders.each {
                    tooltip = "${tooltip} <br/>${it.note}"
                }
            }
            if (numImportant > 0) {
                tooltip = "${tooltip}<br/><strong>${g.message(code: 'patientOverview.importantReminders')}</strong>"
                important.each {
                    tooltip = "${tooltip} <br/>${it.note}"
                }
            }
        }

        tooltip
    }

    def patientNoteMarkSeenButton = { attrs, body ->
        if (SpringSecurityUtils.ifAnyGranted(PermissionName.PATIENT_NOTE_MARK_SEEN)) {
            if(!patientService.isNoteSeenByUser(attrs['note'])) {
                out << g.link(controller: "patientNote", action: "markSeen", id: attrs['id'], class: "acknowledge", g.message(code: "patientNote.markAsRead"))
            }
        }
    }

    def overviewGraphs = { attributes, body ->
        def patient = attributes['patient']
        def (measurements) = measurementService.dataForGraphsAndTables(patient, TimeFilter.lastMonth())

        MarkupBuilder builder = new MarkupBuilder(out)
        def mkp = builder.getMkp()
        builder.div(class: "measurementsPlots") {
            mkp.yieldUnescaped("<![if gt IE 7]>")
            builder.h2(message(code: "patientOverview.graphs", args:["30"]))

            measurements.each { measurement ->
                builder.div(id: "${measurement.type}-${patient.id}", class: "overviewGraph", style: "width:750px")
            }

            mkp.yieldUnescaped(g.render(template: "/measurement/measurementGraph", collection: measurements, var: 'measurement', model:[patient: patient]))
            mkp.yieldUnescaped("<![endif]>")
        }
	}
	
	private void writeRemoveBlueAlarmsButton(MarkupBuilder builder, patientId) {
        builder.div(id: "removeBlueButton") {
            def tooltip = message(code:"patientOverview.removeBlueAlarms")
            builder.getMkp().yieldUnescaped(
                g.form(controller: "patient", action: "removeAllBlue",
                    """<fieldset class="buttons">
                        <input type="hidden" name="patientID" value="${patientId}" />
                        <input 	type="submit" name="_action_removeAllBlue"
                        data-tooltip="${tooltip}"
                        value="" class="removeBlueAlarms"
                        onclick="return confirm('${message(code: 'patientOverview.removeBlueAlarms.confirm')}');" />
				    </fieldset>"""
                )
            )
        }
	}

    private void writeNoAlarmIfUnreadMessagesToPatientButton(MarkupBuilder builder, patientId) {
        builder.div(id: "noAlarmIfUnreadMessagesToPatient") {
            def tooltip = message(code:"patientOverview.noAlarmIfUnreadMessagesToPatient")
            builder.getMkp().yieldUnescaped(
                    g.form(controller: "patient", action: "noAlarmIfUnreadMessagesToPatient",
                            """<fieldset class="buttons">
                        <input type="hidden" name="patientID" value="${patientId}" />
                        <input 	type="submit" name="_action_noAlarmIfUnreadMessagesToPatient"
                        data-tooltip="${tooltip}"
                        value="" class="noAlarmIfUnreadMessagesToPatient"
                        onclick="return confirm('${message(code: 'patientOverview.noAlarmIfUnreadMessagesToPatient.confirm')}');" />
				    </fieldset>"""
                    )
            )
        }
    }

	def writeAcknowledgeAllGreenButtons(PatientOverview patientOverview, boolean messagingEnabled, createDivWrapper = true) {
        def idsOfGreenQuestionnaires = patientOverview.greenQuestionnaireIds?.split(',') ?: []

        MarkupBuilder builder = new MarkupBuilder(out)
        if (idsOfGreenQuestionnaires) {
            def innerContent = {
                def acknowledgeAllIcon = """<img src="${g.resource(dir: 'images', file: 'acknowledged.png')}" data-tooltip="${message(code: 'patientOverview.acknowledgeAllForAll')}"/>"""
                out << g.remoteLink(controller: 'patientOverview',
                        action: 'acknowledgeAll',
                        onComplete: 'location.reload(true);',
                        id: patientOverview.patientId,
                        params:[ids:idsOfGreenQuestionnaires, withAutoMessage: 'false'],
                        before: "return confirm('${message(code: 'patientOverview.acknowledgeAllForAll.confirm')}')", acknowledgeAllIcon)

               def withAutoMessageIcon = getAutoMessageAllIcon(messagingEnabled)
               if (messagingEnabled) {
                    out << g.remoteLink(controller: 'patientOverview',
                            action: 'acknowledgeAll',
                            onComplete: 'location.reload(true);',
                            id: patientOverview.patientId,
                            params:[ids:idsOfGreenQuestionnaires, withAutoMessage: 'true'],
                            before: "return confirm('${message(code: 'patientOverview.acknowledgeAllForAllWithMessage.confirm')}')", withAutoMessageIcon)
               } else {
                   out << "<a>${withAutoMessageIcon}</a>"
               }
            }

            if (createDivWrapper) {
                builder.div(id: "acknowledgeButton") {
                    fieldset(class: "buttons", "") {
                        innerContent()
                    }
                }
            } else {
                innerContent()
            }
        }
	}

    def showQuestionnaire =  { attrs, body ->
        def completedQuestionnaireId = attrs.questionnaireId
        def cq = CompletedQuestionnaire.get(completedQuestionnaireId)
        def resultModel = questionnaireService.extractCompletedQuestionnaireWithAnswers(cq.patient.id, [cq.id])

        MarkupBuilder builder = new MarkupBuilder(out)
        def mkp = builder.getMkp()
        builder.table {
            thead {
                tr {
                    th(message(code:"default.questions"))
                    th(message(code:"default.answer"))
                    th(message(code:"default.severity"))
                    th(message(code:"default.ignored"))
                }
            }
            tbody {
                HashMap<ResultKey, List<MeasurementResult>> results = resultModel.results
                List<MeasurementDescription> questions = resultModel.questions
                OverviewColumnHeader header = resultModel.columnHeaders[0]

                questions.each {question ->
                    ResultKey key = getKeyForThisCell(question, header)
                    def answerList = results.get(key)
                    def answer
                    if(answerList) {
                        answer = answerList[0]
                        if (answerList.size() > 1) {
                            answer.value = answerList*.value.join(",")
                        }
                    }

                    tr {
                        td {
                            if (answer?.ignored) {
                                del(question.text)
                            } else {
                                mkp.yieldUnescaped(question.text)
                            }
                        }
                        td {
                            if (answer?.ignored) {
                                del(getPrettyResultString(answer))
                            } else {
                                mkp.yieldUnescaped(getPrettyResultString(answer))
                            }
                        }
                        td {
                            if (answer?.severity != null) {
                                if (answer?.ignored) {
                                    del(g.message(code: "enum.severity." + answer.severity))
                                } else {
                                    builder.img(src: g.resource(dir: 'images', file: answer.severity.icon))
                                }
                            }
                        }
                        td(class: "buttons") {
                            def btnLabel = g.message(code: 'patient.questionnaire.ignore')
                            def btnIcon = "cancel"
                            if (answer?.ignored) {
                                //I'll buy an ice-cream for whomever can tell me what the action of "un-ignoring" is called in danish.
                                //Has to be in imperative form.
                                btnLabel = g.message(code: 'patient.questionnaire.undoIgnore')
                                btnIcon = "acknowledge"
                            }

                            if (SpringSecurityUtils.ifAnyGranted(PermissionName.NODE_RESULT_IGNORE)) {
                                if (answer != null) {
                                    builder.div('data-tooltip': g.message(code: 'patient.questionnaire.ignore.tooltip'), "") {
                                        mkp.yieldUnescaped(g.remoteLink(controller:"questionnaire", action:"toggleIgnoreNode", onComplete: "location.reload(true);", class: btnIcon, before:"", params:[resultID:answer.id, ignoreNavigation:'true'], btnLabel))
                                    }
                                } else {
                                    div('data-tooltip': g.message(code: 'patient.questionnaire.ignoreMeasurement.unavailable.tooltip'), g.message(code: "patient.questionnaire.ignoreMeasurement.unavailable"))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
