package org.opentele.server.provider

import dk.silverbullet.kih.api.auditlog.AuditLogLookup
import org.slf4j.LoggerFactory

/**
 * User: lch, pu
 * Created: 11/16/12
 * Version: 09/12/14
 */
class OpenteleAuditLogLookup implements AuditLogLookup {

    def log = LoggerFactory.getLogger(OpenteleAuditLogLookup.class)
    Map lookup = [:]

    OpenteleAuditLogLookup() {
        initializeLookup()
    }

    def initializeLookup() {

        def keywordMap = [:]

        keywordMap['login'] = [
                'auth', 'authFail', 'denied', 'ajaxDenied', 'ajaxSuccess',
                'authAjax', 'authfail', 'full', 'index'
        ]

        keywordMap['dbdoc'] = [
                'index'
        ]

        keywordMap['logout'] = [
                'logout', 'index', 'list'
        ]

        keywordMap['password'] = [
                'index', 'change', 'changed', 'update'
        ]

        keywordMap['home'] = [
                'index'
        ]

        keywordMap['user'] = getStandardTexts()

        keywordMap['role'] = getStandardTexts()

        keywordMap['rolePermission'] = getStandardTexts()

        keywordMap['urineThreshold'] = getStandardTexts()

        keywordMap['urineGlucoseThreshold'] = getStandardTexts()

        keywordMap['standardThresholdSet'] = getStandardTexts()

        keywordMap['standardThresholdSet'] += [
                'addThreshold', 'chooseThreshold', 'removeThreshold',
                'saveThresholdToSet'
        ]

        keywordMap['bloodPressureThreshold'] = getStandardTexts()

        keywordMap['numericThreshold'] = getStandardTexts()

        keywordMap['completedQuestionnaire'] = getStandardTexts()

        keywordMap['nodeResult'] = getStandardTexts()

        keywordMap['patientQuestionnaire'] = getStandardTexts()

        keywordMap['questionnaireNode'] = getStandardTexts()

        keywordMap['clinician2PatientGroup'] = getStandardTexts()

        keywordMap['conference'] = getStandardTexts()
        keywordMap['conference'] += [
                'initializeCall', 'noVideo', 'unauthorized', 'unknownVideoProblem',
                'linkEndpoint', 'joinConference', 'conferenceJoined',
                'finishSettingUpConference', 'activeConference',
                'conferenceActiveWithOtherPatient', 'startVideoClient',
                'endpointIdCallback', 'endConference', 'conferenceEnded'
        ]

        keywordMap['patientConferenceMobile'] = getStandardTexts()
        keywordMap['patientConferenceMobile'] += [
                'patientHasPendingConference', 'patientHasPendingMeasurement',
                'measurementFromPatient'
        ]

        keywordMap['conferenceMeasurements'] = getStandardTexts()
        keywordMap['conferenceMeasurements'] += [
                'keepAlive', 'alreadyCompleted', 'loadForm', 'updateMeasurement',
                'deleteMeasurement', 'loadAutomaticMeasurement', 'confirm',
                'finish', 'close'
        ]

        keywordMap['consultation'] = getStandardTexts()
        keywordMap['consultation'] += [ 'addmeasurements' ]

        keywordMap['auditLogEntry'] = getStandardTexts()
        keywordMap['auditLogEntry'] += [
                'ajaxGetActions', 'createDateFromParams', 'likeExpression',
                'search'
        ]

        keywordMap['example'] = getStandardTexts()
        keywordMap['example'] += [ 'custom' ]

        keywordMap['defaultValueThreshold'] = getStandardTexts()

        keywordMap['clinician'] = getStandardTexts()
        keywordMap['clinician'] += [
                'doRemovePatientGroup', 'removePatientGroup', 'changePassword',
                'doChangePassword', 'resetPassword', 'unlockAccount'
        ]

        keywordMap['measurement'] = getStandardTexts()
        keywordMap['measurement'] += [
                'patientGraphs', 'patientMeasurements', 'graph',
                'measurementTypes', 'overview', 'upload', 'bloodsugar'
        ]

        keywordMap['measurementType'] = getStandardTexts()

        keywordMap['message'] = getStandardTexts()
        keywordMap['message'] += [
                'listToDepartment', 'listToPatient', 'messageRecipients',
                'unread', 'reply', 'createJsonForClinicians', 'messageAsJson',
                'newMessages', 'read'
        ]

        keywordMap['patientMessage'] = getStandardTexts()
        keywordMap['patientMessage'] += [
                'listToPatient', 'messageRecipients', 'createJsonForClinicians',
                'messageAsJson', 'markAsRead'
        ]

        keywordMap['meter'] = getStandardTexts()
        keywordMap['meter'] += [ 'attachMeter' ]

        keywordMap['monitoringPlan'] = getStandardTexts()

        keywordMap['monitorKit'] = getStandardTexts()
        keywordMap['monitorKit'] += [ 'attachMeter', 'removeMeter' ]

        keywordMap['nextOfKinPerson'] = getStandardTexts()

        keywordMap['patient'] = getStandardTexts()
        keywordMap['patient'] += [
                'questionnaire', 'conference', 'questionnaires', 'search',
                'doSearch', 'resetSearch', 'equipment', 'measurements',
                'messages', 'unacknowledged', 'login', 'createPatient',
                'addKit', 'addMeter', 'addThreshold',
                'convertStringParamsToFloats', 'doRemoveNextOfKin',
                'filterPatientList2', 'filterPatientListX', 'getNextOfKin',
                'getPatientGroups', 'json', 'monitoringplan', 'removeAllBlue',
                'removeBlueAlarms', 'removeKit', 'removeMeter',
                'removeNextOfKin', 'removeThreshold', 'savePref', 'savePrefs',
                'saveThresholdToPatient', 'showJson', 'editResponsability',
                'updateDataResponsible', 'resetPassword', 'sendPassword',
                'unlockAccount', 'chooseThreshold',
                'noAlarmIfUnreadMessagesToPatient', 'consultation'
        ]

        keywordMap['patientMobile'] = getStandardTexts()
        keywordMap['patientMobile'] += [ 'login' ]

        keywordMap['patientOverview'] = [
                'index', 'details', 'acknowledgeAll', 'acknowledgeAllForAll',
                'acknowledgeQuestionnaireAndRenderDetails'
        ]

        keywordMap['patientNote'] = getStandardTexts()
        keywordMap['patientNote'] += [ 'markSeen', 'listTeam' ]

        keywordMap['questionnaire'] = getStandardTexts()
        keywordMap['questionnaire'] += [
                'toggleIgnoreQuestionnaire', 'toggleIgnoreNode', 'acknowledge',
                'sendAcknowledgeAutoMessage', 'listing'
        ]

        keywordMap['questionnaireMobile'] = getStandardTexts()
        keywordMap['questionnaireMobile'] += [
                'download', 'upload', 'acknowledgements', 'listing',
                'lastContinuousBloodSugarRecordNumber'
        ]

        keywordMap['questionnaireGroup'] = getStandardTexts()

        keywordMap['questionnaireGroup2QuestionnaireHeader'] = getStandardTexts()
        keywordMap['questionnaireGroup2QuestionnaireHeader'] += [ 'del' ]

        keywordMap['questionnaireHeader'] = getStandardTexts()
        keywordMap['questionnaireHeader'] += [
                'saveAndEdit', 'unpublish', 'publishDraft', 'deleteDraft',
                'editDraft', 'createDraft', 'doCreateDraft'
        ]

        keywordMap['questionnaireEditor'] = [
                'index', 'save', 'edit', 'editorState', 'keepAlive'
        ]

        keywordMap['questionnaireSchedule'] = getStandardTexts()
        keywordMap['questionnaireSchedule'] += [
                'questionnaireScheduleData', 'del', 'validateViewModel',
                'showAddQuestionnaireGroup', 'pickQuestionnaireGroup',
                'addQuestionnaireGroup'
        ]

        keywordMap['realTimeCTG'] = getStandardTexts()
        keywordMap['realTimeCTG'] += [ 'save' ]

        keywordMap['patientGroup'] = [
                'index', 'list', 'create', 'save', 'show', 'edit', 'update',
                'delete'
        ]

        keywordMap['meta'] = [
                'index', 'currentServerVersion', 'isAlive', 'isAliveJSON',
                'noAccess', 'registerCurrentPage'
        ]

        keywordMap['scheduleWindow'] = [ 'index', 'show', 'list', 'edit', 'update' ]

        keywordMap['patientMeasurementMobile'] = [ 'index', 'measurement' ]

        keywordMap['videoResource'] = [ 'vidyoDesktopClientStarterJar' ]

        keywordMap['passiveInterval'] = [
                'index', 'list', 'create', 'save', 'show', 'edit', 'update',
                'delete'
        ]

        keywordMap['reminder'] = [ 'next' ]

        keywordMap['helpImage'] = [ 'index', 'list', 'create', 'upload', 'show', 'delete', 'downloadImage' ]

        keywordMap['error'] = [ 'index' ]

        keywordMap.each { key, value -> lookup[key] = createMap(value, key) }

    }

    private def createMap(collection, prefix) {
        return collection.collectEntries([:]) { keyword ->
            [ keyword, createClosure(prefix + '.' + keyword) ] }
    }

    private def createClosure(code) {
        return { g -> g.message(code: code) }
    }

    private def getStandardTexts() {
        return [
                'create', 'delete', 'edit', 'index', 'list', 'save',
                'show', 'update'
        ]
    }

    @Override
    Map retrieve() {
        return lookup
    }
}
