package org.opentele.server.provider.questionnaire

import org.opentele.server.core.command.AddQuestionnaireGroup2MonitoringPlanCommand
import org.opentele.server.core.command.QuestionnaireGroup2HeaderCommand
import org.opentele.server.core.model.Schedule
import org.opentele.server.core.model.types.PatientState
import org.opentele.server.core.model.types.Severity
import org.opentele.server.core.util.TimeFilter
import org.opentele.server.model.*
import org.opentele.server.model.patientquestionnaire.*
import org.opentele.server.model.questionnaire.*

import org.opentele.server.provider.Exception.DeleteQuestionnaireException
import org.springframework.transaction.annotation.Transactional

class QuestionnaireProviderService {

    static transactional = false

    def questionnaireService
    def springSecurityService
    def sessionFactory
    def questionnaireNodeService
    def grailsApplication
    def i18nService

    def extractMeasurements(Long patientID, boolean unacknowledgedOnly = false, TimeFilter timeFilter = null) {
        def completedQuestionnaireResultModel = questionnaireService.extractCompletedQuestionnaireWithAnswers(patientID, null, unacknowledgedOnly, timeFilter)

        if(!unacknowledgedOnly) {
            def conferenceResultModel = questionnaireService.extractConferenceResults(patientID, timeFilter)
            completedQuestionnaireResultModel.columnHeaders.addAll(conferenceResultModel.columnHeaders)
            completedQuestionnaireResultModel.questions.addAll(0, conferenceResultModel.questions)
            completedQuestionnaireResultModel.results.putAll(conferenceResultModel.results)

            def consultationResultModel = questionnaireService.extractConsultationResults(patientID, timeFilter)
            completedQuestionnaireResultModel.columnHeaders.addAll(consultationResultModel.columnHeaders)
            completedQuestionnaireResultModel.questions.addAll(0, consultationResultModel.questions)
            completedQuestionnaireResultModel.results.putAll(consultationResultModel.results)
        }

        completedQuestionnaireResultModel.columnHeaders.sort(true, {a, b -> b.uploadDate <=> a.uploadDate} as Comparator)
        return completedQuestionnaireResultModel
    }

    def checkForBlueAlarms(Patient patient, def checkFrom, def checkTo) {
        if (patient.stateWithPassiveIntervals != PatientState.ACTIVE) {
            return []
        }

        def idsForPatientQuestionnairesWithBlueAlarms = []

        def questionnaireSchedules = questionnaireService.getActiveQuestionnaireHeadersForPatient(patient)
        for (it in questionnaireSchedules) {
            if (hasBlueAlarm(it, checkFrom, checkTo)) {
                it.getQuestionnaireHeader().getActiveQuestionnaire().getPatientQuestionnaires().each { patientQuestionnaire ->
                    idsForPatientQuestionnairesWithBlueAlarms << patientQuestionnaire.id
                }
            }
        }

        idsForPatientQuestionnairesWithBlueAlarms
    }

    private boolean hasBlueAlarm(QuestionnaireSchedule questionnaireSchedule, Calendar checkFrom, Calendar checkTo) {
        if (questionnaireSchedule.hasTimeSchedule()) {
            // Find the latest deadline.
            Calendar latestDeadline = questionnaireSchedule.getLatestDeadlineBefore(checkTo)

            // If there hasn't been a deadline yet then there is no blue alarm.
            if (latestDeadline == null) {
                return false
            }

            // If the latest deadline before the previous check time then it has already been checked.
            if (latestDeadline.before(checkFrom)) {
                return false
            }

            return !questionnaireService.hasUploadWithinGracePeriod(questionnaireSchedule, latestDeadline)
        }
        return false
    }

    def iconAndTooltip(g, Patient patient) {
        def unacknowledgedQuestionnaires = CompletedQuestionnaire.findAllByPatientAndAcknowledgedDate(patient, null, [sort: 'uploadDate', order: 'desc'])

        iconAndTooltip(g, patient, unacknowledgedQuestionnaires)
    }

    def iconAndTooltip(g, Patient patient, questionnaires) {
        def questionnaireOfWorstSeverity = questionnaireOfWorstSeverity(questionnaires)
        def severity = questionnaireService.severity(patient, questionnaireOfWorstSeverity)

        if (severity == Severity.BLUE) {
            StringBuilder sb = new StringBuilder()
            patient.blueAlarmQuestionnaireIDs.each {
                sb.append(PatientQuestionnaire.get(it).name).append("<br/>")
            }
            [severity.icon(), g.message(code: 'patientOverview.questionnairesNotAnsweredOnTime.tooltip', args: [sb.toString()])]
        } else if (severity != Severity.NONE) {
            [severity.icon(), g.message(code: 'patientOverview.questionnaireAlarmDetails.tooltip', args: ["${questionnaireOfWorstSeverity.patientQuestionnaire.name}", "${g.formatDate(date: questionnaireOfWorstSeverity.uploadDate)}"])]
        } else {
            [Severity.NONE.icon(), g.message(code: 'patientOverview.noNewCompletedQuestionnairesFromPatient.tooltip')]
        }
    }


//    def severity(Patient patient, questionnaireOfWorstSeverity=null) {
//        if (questionnaireOfWorstSeverity == null) {
//            def unacknowledgedQuestionnaires = CompletedQuestionnaire.findAllByPatientAndAcknowledgedDate(patient, null, [sort: 'uploadDate', order: 'desc'])
//            questionnaireOfWorstSeverity = this.questionnaireOfWorstSeverity(unacknowledgedQuestionnaires)
//        }
//
//        def severity = (questionnaireOfWorstSeverity == null) ? Severity.NONE : questionnaireOfWorstSeverity.severity
//        if (severity < Severity.BLUE && !patient.blueAlarmQuestionnaireIDs.empty) {
//            return Severity.BLUE
//        }
//
//        severity
//    }

    // Ikke i core:
    def getUnusedQuestionnaireHeadersForMonitoringPlan(MonitoringPlan mp) {
        // List skemaer fra monitoreringsplanen
        def usedQuestionnaireHeaders = [:]

        def schedules = mp.questionnaireSchedules
        schedules.each() {
            it.refresh()
            QuestionnaireHeader qh = it.questionnaireHeader
            qh.refresh()
            usedQuestionnaireHeaders.put(qh.id, qh)
        }

        // hent alle skemaer
        def activeQuestionnaireHeadersList = QuestionnaireHeader.findAll()

        def unused = []

        // Fjern de anvendte
        activeQuestionnaireHeadersList.each { qh ->
            if (!usedQuestionnaireHeaders.get(qh.id)) {
                unused.add(qh)
            }
        }
        unused.sort { it.toString().toLowerCase() }
    }


    private  CompletedQuestionnaire questionnaireOfWorstSeverity(List<CompletedQuestionnaire> questionnaires) {
        questionnaires.max { it.severity }
    }

    // Ikke i core:
    @Transactional()
    def deleteQuestionnaire(Questionnaire questionnaire) {
        if (questionnaire.questionnaireHeader.draftQuestionnaire != questionnaire) {
            throw new DeleteQuestionnaireException("cannot delete questionnaire that is not marked as draft")
        }
        if (questionnaire.patientQuestionnaires) {
            throw new DeleteQuestionnaireException("Cannot delete questionnaire which has a patient questionnaire")
        }
        questionnaireNodeService.deleteQuestionnaireNodes(questionnaire, true)

        deleteQuestionnaire2MeterReferences(questionnaire)

        def questionnaireHeader = questionnaire.questionnaireHeader

        questionnaireHeader.draftQuestionnaire = null
        questionnaireHeader.removeFromQuestionnaires(questionnaire)
        questionnaireHeader.save(flush: true, failOnError: true)
        // TODO: This should be questionnaire.delete() but that continously fails
        Questionnaire.executeUpdate("delete from Questionnaire q where q=?",[questionnaire])
    }


    private deleteQuestionnaire2MeterReferences(Questionnaire questionnaire) {
        def questionnaire2MeterTypes = Questionnaire2MeterType.findAllByQuestionnaire(questionnaire)
        questionnaire2MeterTypes.each {
            questionnaire.removeFromQuestionnaire2MeterTypes(it)
            questionnaire.save(failOnError: true)
            it.delete()
        }
    }

    def findQuestionnaireGroup2HeadersAndOverlapWithExistingQuestionnaireSchedules(QuestionnaireGroup questionnaireGroup, MonitoringPlan monitoringPlan) {
        questionnaireGroup.questionnaireGroup2Header.collect { questionnaireGroup2Header ->
            def candidate = [
                    questionnaireGroup2Header: questionnaireGroup2Header
            ]
            def questionnaireSchedule = findExistingQuestionnaireSchedule(monitoringPlan.questionnaireSchedules, questionnaireGroup2Header.questionnaireHeader)
            if (questionnaireSchedule) {
                candidate.questionnaireSchedule = questionnaireSchedule
                candidate.questionnaireScheduleOverlap = !isSchedulesEqual(questionnaireGroup2Header.schedule, questionnaireSchedule)
            }
            return candidate
        }
    }

    def addOrUpdateQuestionnairesOnMonitoringPlan(AddQuestionnaireGroup2MonitoringPlanCommand command) {
        def monitoringPlan = command.monitoringPlan
        def addToMonitoringPlan = findAddedQuestionnaireGroup2HeaderCommands(command)
        addToMonitoringPlan.each { questionnaireGroup2HeaderCmd ->
            def questionnaireInMonitoringPlan = findQuestionnaireInMonitoringPlan(questionnaireGroup2HeaderCmd, monitoringPlan)
            if(questionnaireInMonitoringPlan) {
                if(updateQuestionnaireSchedule(questionnaireGroup2HeaderCmd, questionnaireInMonitoringPlan)) {
                    command.updatedQuestionnaires << questionnaireInMonitoringPlan
                }
            } else {
                command.addedQuestionnaires << addQuestionnaireToMonitoringPlan(questionnaireGroup2HeaderCmd, monitoringPlan)
            }
        }
        monitoringPlan.save(flush: true)
    }


    private addQuestionnaireToMonitoringPlan(QuestionnaireGroup2HeaderCommand questionnaireGroup2HeaderCommand, MonitoringPlan monitoringPlan) {
        def questionnaireGroup2Header = questionnaireGroup2HeaderCommand.questionnaireGroup2Header
        def questionnaireSchedule = new QuestionnaireSchedule(monitoringPlan: monitoringPlan, questionnaireHeader: questionnaireGroup2HeaderCommand.questionnaireGroup2Header.questionnaireHeader)
        questionnaireSchedule.save(failOnError: true)
        assignStandardScheduleToQuestionnaireSchedule(questionnaireGroup2Header.schedule, questionnaireSchedule)
        monitoringPlan.addToQuestionnaireSchedules(questionnaireSchedule)
        monitoringPlan.save(failOnError: true)
        return questionnaireSchedule
    }


    private updateQuestionnaireSchedule(QuestionnaireGroup2HeaderCommand questionnaireGroup2HeaderCommand, QuestionnaireSchedule questionnaireSchedule) {
        def questionnaireHeaderSchedule = questionnaireGroup2HeaderCommand.questionnaireGroup2Header.schedule
        if(questionnaireGroup2HeaderCommand.useStandard && !isSchedulesEqual(questionnaireHeaderSchedule, questionnaireSchedule)) {
            assignStandardScheduleToQuestionnaireSchedule(questionnaireHeaderSchedule, questionnaireSchedule)
            questionnaireSchedule.save(failOnError: true)
            return true
        }
    }

    private assignStandardScheduleToQuestionnaireSchedule(StandardSchedule schedule, QuestionnaireSchedule questionnaireSchedule) {
        if(schedule) {
            questionnaireSchedule.type = schedule.type
            questionnaireSchedule.weekdays = schedule.weekdays
            questionnaireSchedule.daysInMonth = schedule.daysInMonth
            questionnaireSchedule.timesOfDay = schedule.timesOfDay
            questionnaireSchedule.startingDate = schedule.startingDate
            questionnaireSchedule.dayInterval = schedule.dayInterval
            questionnaireSchedule.specificDate = schedule.specificDate
            questionnaireSchedule.reminderStartMinutes = schedule.reminderStartMinutes
            questionnaireSchedule.introPeriodWeeks = schedule.introPeriodWeeks
            questionnaireSchedule.reminderTime = schedule.reminderTime
            questionnaireSchedule.blueAlarmTime = schedule.blueAlarmTime
            questionnaireSchedule.weekdaysIntroPeriod = schedule.weekdaysIntroPeriod
            questionnaireSchedule.weekdaysSecondPeriod = schedule.weekdaysSecondPeriod
        }
    }

    List<QuestionnaireGroup2HeaderCommand> findAddedQuestionnaireGroup2HeaderCommands(AddQuestionnaireGroup2MonitoringPlanCommand addQuestionnaireGroup2MonitoringPlan) {
        addQuestionnaireGroup2MonitoringPlan.addedQuestionnaireGroup2Headers().findAll {
            it.questionnaireGroup2Header && it.addQuestionnaire
        }
    }

    private findQuestionnaireInMonitoringPlan(QuestionnaireGroup2HeaderCommand questionnaireGroup2HeaderCommand, MonitoringPlan monitoringPlan) {
        monitoringPlan.questionnaireSchedules.find {
            it.questionnaireHeader.id == questionnaireGroup2HeaderCommand.questionnaireGroup2Header.questionnaireHeader.id
        }
    }

    private isSchedulesEqual(StandardSchedule standardSchedule, QuestionnaireSchedule questionnaireSchedule) {
        if (standardSchedule) {
            if (isTypeEquals(standardSchedule, questionnaireSchedule)) {
                switch (standardSchedule.type) {
                    case Schedule.ScheduleType.UNSCHEDULED:
                        return true
                    case Schedule.ScheduleType.WEEKDAYS:
                        return isWeekdaysEquals(standardSchedule, questionnaireSchedule) && isTimesOfDayEquals(standardSchedule, questionnaireSchedule)
                    case Schedule.ScheduleType.MONTHLY:
                        return isDaysInMonthEquals(standardSchedule, questionnaireSchedule) && isTimesOfDayEquals(standardSchedule, questionnaireSchedule)
                    case Schedule.ScheduleType.EVERY_NTH_DAY:
                        return isEveryNthDayEquals(standardSchedule, questionnaireSchedule) && isTimesOfDayEquals(standardSchedule, questionnaireSchedule)
                }

            } else {
                return false
            }
        } else {
            return true
        }
    }

    private isEveryNthDayEquals(StandardSchedule standardSchedule, QuestionnaireSchedule questionnaireSchedule) {
        standardSchedule.dayInterval == questionnaireSchedule.dayInterval
    }

    private isDaysInMonthEquals(StandardSchedule standardSchedule, QuestionnaireSchedule questionnaireSchedule) {
        standardSchedule.daysInMonth.sort() == questionnaireSchedule.daysInMonth.sort()
    }

    private isTimesOfDayEquals(StandardSchedule standardSchedule, QuestionnaireSchedule questionnaireSchedule) {
        standardSchedule.timesOfDay.sort() == questionnaireSchedule.timesOfDay.sort()
    }

    private isWeekdaysEquals(StandardSchedule standardSchedule, QuestionnaireSchedule questionnaireSchedule) {
        standardSchedule.weekdays.sort() ==  questionnaireSchedule.weekdays.sort()
    }

    private isTypeEquals(StandardSchedule standardSchedule, QuestionnaireSchedule questionnaireSchedule) {
        standardSchedule.type == questionnaireSchedule.type
    }

    private findExistingQuestionnaireSchedule(Collection<QuestionnaireSchedule> questionnaireSchedules, QuestionnaireHeader questionnaireHeader) {
        questionnaireSchedules.find { it.questionnaireHeader.id == questionnaireHeader.id }
    }
}

//public class ResultTableViewModel {
//    List<OverviewColumnHeader> columnHeaders = []
//    List<MeasurementDescription> questions = []
//    Map<ResultKey, MeasurementResult> results = [:]
//}
//
//public class MeasurementDescription {
//    def type
//    def text
//    def questionnaireName
//    def templateQuestionnaireNodeId
//
//    Set<Unit> units
//    Set<MeasurementTypeName> measurementTypeNames
//
//    def orderedUnits() {
//
//        if (!units) {
//            return Collections.EMPTY_LIST
//        }
//        if (units.size() == 2 && units.containsAll([Unit.BPM, Unit.MMHG])) {
//            return [Unit.MMHG, Unit.BPM]
//        }
//        if (units.size() == 2 && units.containsAll([Unit.BPM, Unit.PERCENTAGE])) {
//            return [Unit.PERCENTAGE, Unit.BPM]
//        }
//        return units.toList()
//    }
//}
//
//public class OverviewColumnHeader {
//    def type, id, uploadDate, severity, acknowledgedBy, acknowledgedDate, acknowledgedNote, _questionnaireIgnored, questionnaireIgnoredReason, questionnareIgnoredBy
//}
//
//public enum MeasurementParentType {
//    QUESTIONNAIRE,
//    CONFERENCE,
//    CONSULTATION;
//}
//
//class GraphNode {
//    def id
//    def distance
//    def predecessor
//
//    //Needed by filter
//    def clazz
//
//    //Needed by view
//    def text
//    def questionnaireName
//    def templateQuestionnaireNodeId
//}
//
//class GraphEdge {
//    GraphNode u
//    GraphNode v
//    int w
//
//    public GraphEdge(def u, def v, def w) {
//        this.u = u
//        this.v = v
//        this.w = w
//    }
//}
//
//class ResultKey {
//    def type
//    def rowId
//    def colId
//
//    @Override
//    boolean equals(o) {
//        if (this.is(o)) return true
//        if (getClass() != o.class) return false
//
//        ResultKey resultKey = (ResultKey) o
//
//        if (colId != resultKey.colId) return false
//        if (rowId != resultKey.rowId) return false
//
//        return true
//    }
//    @Override
//    int hashCode() {
//        int result
//        result = rowId.hashCode()
//        result = 31 * result + colId.hashCode()
//        return result
//    }
//}
