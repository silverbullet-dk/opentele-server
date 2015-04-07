package org.opentele.server.provider.model
import grails.converters.JSON
import grails.plugin.springsecurity.annotation.Secured
import org.opentele.server.core.model.types.Sex
import org.opentele.server.model.BloodPressureThreshold
import org.opentele.server.model.Clinician
import org.opentele.server.model.Clinician2PatientGroup
import org.opentele.server.model.ClinicianQuestionPreference
import org.opentele.server.model.Conference
import org.opentele.server.model.Consultation
import org.opentele.server.core.command.CreatePatientCommand
import org.opentele.server.model.Message
import org.opentele.server.model.Meter
import org.opentele.server.model.MonitorKit
import org.opentele.server.model.NextOfKinPerson
import org.opentele.server.model.NumericThreshold
import org.opentele.server.model.Patient
import org.opentele.server.model.Patient2PatientGroup
import org.opentele.server.model.PatientGroup
import org.opentele.server.core.command.PatientSearchCommand
import org.opentele.server.model.QuestionnaireSchedule
import org.opentele.server.model.Threshold
import org.opentele.server.model.UrineGlucoseThreshold
import org.opentele.server.model.UrineThreshold
import org.opentele.server.provider.PatientIdentificationService
import org.opentele.server.provider.ThresholdService
import org.opentele.server.core.util.TimeFilter

import org.opentele.server.provider.constants.Constants
import org.opentele.server.provider.integration.cpr.CPRPerson
import org.opentele.server.core.exception.OptimisticLockingException
import org.opentele.server.core.exception.PatientException
import org.opentele.server.core.exception.PatientNotFoundException
import org.opentele.server.model.patientquestionnaire.CompletedQuestionnaire
import org.opentele.server.model.questionnaire.QuestionnaireNode
import org.opentele.server.core.model.types.PatientState
import org.opentele.server.core.model.types.PermissionName
import org.springframework.validation.Errors

import static org.opentele.server.core.model.types.MeasurementTypeName.*

@Secured(PermissionName.NONE)
class PatientController {

    def springSecurityService
    def patientService
    def questionnaireService
    def questionnaireProviderService
    def sessionService
    def passwordService
    def clinicianMessageService
    def cprLookupService
    def clinicianService
    def measurementTypeService
    def patientIdentificationService

	static allowedMethods = [index: "GET", save: "POST", update: "POST", delete: "POST", overview:["GET","POST"]]
    ThresholdService thresholdService

    @Secured(PermissionName.PATIENT_READ_ALL)
	def index() {
		redirect(action: "overview")
	}

    // Secured in Config.groovy since secured annotation cannot be used on closures.
    def createPatientFlow = {
        session[Constants.SESSION_ACCESS_VALIDATED] = "false"

        startMagic {
            action {

                def cleartextPassword = passwordService.generateTempPassword()
                flow.patientInstance = new CreatePatientCommand(cleartextPassword: cleartextPassword)
            }
            on("success").to "basicPatientInformation"
        }
        basicPatientInformation {
            on("next") {
                params.cpr = patientIdentificationService.formatForStorage(params.cpr)
                flow.patientInstance.setBasicInformation(params)
                !flow.patientInstance.validate(['firstName', 'lastName', 'cpr', 'sex', 'address', 'postalCode', 'city']) ? error() : success()
            }.to "setAuthentication"
            on("lookupCPR") {
                def cpr = params.cpr
                cpr = cpr?.replaceAll("-", "")
                cpr = cpr?.replaceAll(" ", "")
                flow.patientInstance.cpr = cpr
                if (flow.patientInstance.validate(['cpr'])) {
                    CPRPerson cprLookupResult = cprLookupService.getPersonDetails(flow.patientInstance.cpr)

                    if (!cprLookupResult.hasErrors) {

                        flow.patientInstance.cpr = cprLookupResult.civilRegistrationNumber
                        flow.patientInstance.firstName = cprLookupResult.getFirstNames()
                        flow.patientInstance.lastName = cprLookupResult.lastName
                        flow.patientInstance.sex = cprLookupResult.sex
                        flow.patientInstance.address = cprLookupResult.address
                        flow.patientInstance.postalCode = cprLookupResult.postalCode
                        flow.patientInstance.city = cprLookupResult.city

                        if (!cprLookupResult.hasErrors && cprLookupResult.civilRegistrationNumber == null) {
                            //Call to DetGodeCPROpslag was good, but empty result
                            flash.error = message(code: 'patient.create.flow.CPRLookup.emptyResponse')
                        }

                        success()
                    } else {
                        //Call to DetGodeCPROpslag was erroneous
                        flash.error = message(code: 'patient.create.flow.CPRLookup.SOAPError')
                        error()
                    }

                } else {
                    error()
                }
            }.to "basicPatientInformation"
        }
        setAuthentication {
            on("next") {
                flow.patientInstance.setAuthentication(params.username, params.cleartextPassword)
                flow.patientInstance.validate(['username', 'cleartextPassword']) ? success() : error()
            }.to "findPatientGroups"
            on("previous") { flow.patientInstance.clearErrors() }.to "basicPatientInformation"
        }
        findPatientGroups {
            action {
                def user = springSecurityService?.currentUser
                def clinician = Clinician.findByUser(user)
                def cpgs = Clinician2PatientGroup.findAllByClinician(clinician)

                flow.patientGroups = cpgs.collect { it.patientGroup }
            }
            on("success").to "choosePatientGroup"
        }
        choosePatientGroup {
            on("next") {
                flow.patientInstance.clearErrors(); flow.nextStateName = "thresholdValues"
            }.to "writePatientGroup"
            on("previous") { flow.patientInstance.clearErrors() }.to "setAuthentication"
            on("saveAndShow") { flow.nextStateName = "summary" }.to "writePatientGroup"
            on("saveAndGotoMonplan") {
                flow.nextStateName = "writePatientToDB"; flow.endRedirectToMonplan = true
            }.to "writePatientGroup"
        }
        writePatientGroup {
            action {
                if (params.groupIds) {
                    flow.patientInstance.setPatientGroups(params.groupIds)
                }
                !flow.patientInstance.validate(['groupIds']) ? error() : action.result(flow.nextStateName)
            }
            on("writePatientToDB").to "writePatientToDB"
            on("thresholdValues").to "thresholdValues"
            on("error").to "choosePatientGroup"
            on("summary").to "summary"
        }
        thresholdValues {
            on("next") { flow.nextStateName = "addComment" }.to "updateThresholdValues"
            on("previous") { flow.patientInstance.clearErrors() }.to "choosePatientGroup"
            on("saveAndShow") { flow.nextStateName = "summary" }.to "updateThresholdValues"
            on("saveAndGotoMonplan") {
                flow.nextStateName = "writePatientToDB"; flow.endRedirectToMonplan = true
            }.to "updateThresholdValues"
        }
        updateThresholdValues {
            action {
                flow.patientInstance.updateThresholds(params)
                !flow.patientInstance.validate(['thresholds']) ? error() : action.result(flow.nextStateName)
                flow.patientInstance.thresholds.any { !it.validate() } ? error() : action.result(flow.nextStateName)
            }
            on("addComment").to "addComment"
            on("summary").to "summary"
            on("writePatientToDB").to "writePatientToDB"
            on("error").to "thresholdValues"
        }
        addComment {
            on("next") { flow.nextStateName = "addNextOfKin" }.to "writeComment"
            on("previous") { flow.patientInstance.clearErrors() }.to "thresholdValues"
            on("saveAndShow") { flow.nextStateName = "summary" }.to "writeComment"
            on("saveAndGotoMonplan") {
                flow.nextStateName = "writePatientToDB"; flow.endRedirectToMonplan = true
            }.to "writeComment"
        }
        writeComment {
            action {
                flow.patientInstance.setComment(params.comment)
                !flow.patientInstance.validate(['comment']) ? error() : action.result(flow.nextStateName)
            }
            on("addNextOfKin").to "addNextOfKin"
            on("summary").to "summary"
            on("writePatientToDB").to "writePatientToDB"
            on("error").to "addComment"
        }
        addNextOfKin {
            on("next").to "summary"
            on("create").to "createNextOfKin"
            on("previous") { flow.patientInstance.clearErrors() }.to "addComment"
            on("saveAndShow").to "summary"
            on("saveAndGotoMonplan") { flow.endRedirectToMonplan = true }.to "writePatientToDB"
        }
        createNextOfKin {
            on("done") {
                flow.nextOfKinPersonInstance = null ///Might have been set during validation error
                def nextOfKinPersonInstance = new NextOfKinPerson(params)
                if (!nextOfKinPersonInstance.save()) {
                    flow.nextOfKinPersonInstance = nextOfKinPersonInstance
                    //Make sure view has access to instance with validation errors
                    error()
                } else {
                    flow.patientInstance.addNextOfKinPerson(nextOfKinPersonInstance)
                    success()
                }
            }.to "addNextOfKin"
        }
        summary {
            on("save").to "writePatientToDB"
            on("saveAndGotoMonplan") { flow.endRedirectToMonplan = true }.to "writePatientToDB"
            on("editBasic").to "basicPatientInformation"
            on("editAuth").to "setAuthentication"
            on("editPG").to "choosePatientGroup"
            on("editThresholds").to "thresholdValues"
            on("editComment").to "addComment"
            on("editNok").to "addNextOfKin"
            on("quitNoSaving").to "quitNoSave"
        }
        writePatientToDB {
            action {
                Patient patientInstance
                try {
                    patientInstance = patientService.buildAndSavePatient(flow.patientInstance)
                    if (patientInstance.hasErrors()) {
                        return error()
                    }
                } catch (PatientException e) {
                    flow.hasErrors = true
                    flow.errorMessage = g.message(code: e.getMessage())
                    return error()
                }

                flow.savedPatient = patientInstance
            }
            on("success").to "finish"
            on("error").to "summary"
        }
        finish {
            action {
                if (flow.endRedirectToMonplan) {
                    redirect(controller: "monitoringPlan", action: "show", id: flow.savedPatient.id)
                } else {
                    redirect(controller: "patient", action: "show", id: flow.savedPatient.id)
                }
            }
            on("success").to "end"
        }
        quitNoSave {
            action {
                redirect(controller: "patientOverview", action: "index")
            }
            on("success").to "end"
        }
        end()
    }

    @Secured(PermissionName.PATIENT_READ)
	def show() {
		def patientInstance = Patient.get(params.id)
        if (!patientInstance) {
            // Setting up session values
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patient.label', default: 'Patient')])
            sessionService.setNoPatient(session)
            redirect(action: "overview")
            return
        }
        sessionService.setPatient(session, patientInstance)
        createShowModel(patientInstance)
	}

    @Secured(PermissionName.PATIENT_WRITE)
    def edit() {
        def patientInstance = Patient.get(params.id)

        if (!patientInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patient.label', default: 'Patient')])
            sessionService.setNoPatient(session)
            redirect(action: "overview")
            return
        }

        sessionService.setPatient(session, patientInstance)

        createEditModel(patientInstance)
    }

	private def getPatientGroups(def patientInstance) {
		def p2pg = Patient2PatientGroup.findAllByPatient(patientInstance)
		def groups = []
		p2pg.each {map ->
			groups << map.patientGroup
		}
		groups
	}
	
	private def getNextOfKin(patientInstance) {
		NextOfKinPerson.findAllByPatient(patientInstance)
	}

    @Secured(PermissionName.PATIENT_WRITE)
    def update() {
		def patientInstance = Patient.findById(params.id)
        flash.message = null
		try {
			patientService.updatePatient(params, patientInstance)

            if(!patientInstance.hasErrors()) {
                // Setting up session values
                sessionService.setPatient(session, patientInstance)

                //Clear kits from patient if patient treatment is stopped, or if status is discharged and
                //equipment is handed in
                if (patientInstance.state == PatientState.DISCHARGED_EQUIPMENT_DELIVERED) {
                    MonitorKit.findAllByPatient(patientInstance).each { kit ->
                        def meters = kit.meters
                        meters.each {meter ->
                            meter.patient = null
                            meter.save()
                        }

                        patientInstance.removeFromMonitorKits(kit)
                        patientInstance.save()
                    }
                }

                if (patientInstance.state == PatientState.DISCHARGED_EQUIPMENT_DELIVERED || patientInstance.state == PatientState.DISCHARGED_EQUIPMENT_NOT_DELIVERED || patientInstance.state == PatientState.DECEASED) {
                    clearDataFromInactivePatient(patientInstance)
                }

                flash.message = message(code: 'default.updated.message', args: [message(code: 'patient.label', default: 'Patient')])
                sessionService.setPatient(session, patientInstance)
                render(view: "show", model: createShowModel(patientInstance))
            } else {

                render(view: "edit", model: createEditModel(patientInstance))
            }

		} catch (PatientNotFoundException e) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patient.label', default: 'Patient')])
			redirect(action: "overview")
		} catch (OptimisticLockingException e) {
            patientInstance = Patient.findById(params.id)
            patientInstance.errors.reject("default.optimistic.locking.failure", [message(code:"patientNote.label")] as Object[] , "i18n error")
            render(view: "edit", model: createEditModel(patientInstance))
        } catch (Exception e) {
            //Transaction has rolledback
            log.warn("patientUpdate failed with exception: "+e)
            Errors errors = patientInstance.getErrors()
            patientInstance = Patient.findById(params.id)
            patientInstance.setErrors(errors)

            render(view: "edit", model: createEditModel(patientInstance))
        }
	}

    private def createShowModel(Patient patientInstance) {

        boolean messagingEnabled = clinicianMessageService.clinicianCanSendMessagesToPatient(Clinician.findByUser(springSecurityService.currentUser), patientInstance)
        [patientInstance: patientInstance, groups:getPatientGroups(patientInstance), nextOfKin: getNextOfKin(patientInstance), messagingEnabled: messagingEnabled]
    }

    private def createEditModel(Patient patientInstance) {

        [patientInstance: patientInstance,
                groups: PatientGroup.list(sort:"name"),
                messagingEnabled: clinicianMessageService.clinicianCanSendMessagesToPatient(Clinician.findByUser(springSecurityService.currentUser), patientInstance),
                showDueDate: patientInstance.shouldShowGestationalAge, showRunningCtgMessaging: patientInstance.shouldShowRunningCtgMessaging]
    }

    private void clearDataFromInactivePatient(Patient patient) {
        // If patient is 'udmeldt' (discharged), or if patient is deceased (afdød)
        //  * Clear monitoring plan from patient.
        //  * Remove blue alarms from the patient.
        //  * Acknowledge all the patient's completed questionnaires.
        //  * Remove reminders from notes.
        patient.endedMonitoringPlans.add(patient.monitoringPlan)
        patient.monitoringPlan = null
        patient.blueAlarmQuestionnaireIDs = []
        patient.save(failOnError: true, flush: true)

        acknowledgeAllQuestionnaires(patient)
        removeRemindersFromNotes(patient)
    }

    private void acknowledgeAllQuestionnaires(Patient patient) {
        def user = springSecurityService.currentUser
        def clinician = Clinician.findByUser(user)
        for (questionnaire in patient.completedQuestionnaires) {
            questionnaire.acknowledgedBy = clinician
            questionnaire.acknowledgedDate = new Date()
        }
    }

    private void removeRemindersFromNotes(Patient patient) {
        for (note in patient.notes) {
            note.reminderDate = null
        }
    }

    @Secured(PermissionName.PATIENT_READ_ALL)
    def search() {

        [searchCommand: new PatientSearchCommand(), patients:[], clinicianPatientGroups: clinicianService.patientGroupsForCurrentClinician]
    }

    @Secured(PermissionName.PATIENT_READ_ALL)
    def doSearch(PatientSearchCommand searchCommand) {

        def patientList = patientService.searchPatient(searchCommand)

		//For g:sortableColumns
        params.sort = params.sort ?: 'firstName'
        params.order = params.order ?: 'asc'
		if (params.sort) {
			if (params.sort == 'severity') {
                patientList.sort { questionnaireService.severity(it) }
            } else if (params.sort == 'state') {
                patientList.sort { g.message(code: "enum.patientstate.${it.stateWithPassiveIntervals}") }
			} else {
				patientList.sort { normalizeSortValue(it."${params.sort}") }
			}

            if (params.order.equals("desc")) {
                patientList.reverse(true)
            }
		}
        render(view:"search",  model: [searchCommand: searchCommand, patients:patientList, clinicianPatientGroups: clinicianService.patientGroupsForCurrentClinician])
	}

    @Secured(PermissionName.PATIENT_READ_ALL)
    def resetSearch() {
        redirect(action: 'search')
    }

    /**
	 * Provides an overview of a single questionnaire.
	 */
    @Secured(PermissionName.COMPLETED_QUESTIONNAIRE_READ)
	def questionnaire() {
        def requestURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getForwardURI()
        session.setAttribute('lastReferer', requestURL)
		def user = springSecurityService.currentUser

		def completedQuestionnaire = CompletedQuestionnaire.get(params.id)

		if (user.isPatient()) {
			def myPatient = Patient.findByUser(user)
			if (completedQuestionnaire.patient != myPatient) {
				redirect(controller: "login", action: "denied")
			}
		}


		sessionService.setPatient(session, completedQuestionnaire.patient)

		[completedQuestionnaire:completedQuestionnaire]
	}

    @Secured(PermissionName.CONFERENCE_READ)
    def conference() {
        def conference = Conference.get(params.id)
        sessionService.setPatient(session, conference.patient)
        [conference:conference]
    }

    @Secured(PermissionName.CONSULTATION_READ)
    def consultation() {
        def consultation = Consultation.get(params.id)
        sessionService.setPatient(session, consultation.patient)
        [consultation:consultation]
    }

    /**
	 * Closure for presenting the results of a patients response to questionnaires
	 */
    @Secured(PermissionName.COMPLETED_QUESTIONNAIRE_READ_ALL)
    def questionnaires(Long id) {
        def requestURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getForwardURI()
        session.setAttribute('lastReferer', requestURL)
        def user = springSecurityService.currentUser

		def patient
		if (user.isPatient()) {
			patient = Patient.findByUser(user)
		} else  if (user.isClinician() &&id) {
            patient = Patient.get(id)
		}

		def questionPreferences = questionPreferencesForClinician(Clinician.findByUser(user))
		def questionnairesNumber = []
		def completed
        def greenCompletedQuestionnaires = []
        def resultModel
        def consultations

        if (patient) {
			// Setting up session values
			sessionService.setPatient(session, patient)
            if(patient.getMonitoringPlan()) {
                questionnairesNumber = QuestionnaireSchedule.countByMonitoringPlan(patient.getMonitoringPlan())
            } else {
                questionnairesNumber = 0
            }

            // Default time frame is a month
            TimeFilter timeFilter
            if (params.filter) {
                convertDateTypes(params)
                timeFilter = TimeFilter.fromParams(params)
                session.lastFilterParams = params
            } else {
                timeFilter = TimeFilter.lastMonth()
            }

            resultModel = questionnaireProviderService.extractMeasurements(patient.id, false, timeFilter)

			completed = CompletedQuestionnaire.countByPatient(patient)

            consultations = Consultation.countByPatient(patient)

            greenCompletedQuestionnaires = CompletedQuestionnaire.unacknowledgedGreenQuestionnairesByPatient(patient, timeFilter).list()

        }

        [patientInstance: patient, questionnairesNumber: questionnairesNumber, completedQuestionnaireResultModel: resultModel, questionPreferences: questionPreferences, completedNumber: completed, greenCompletedAndUnacknowledgedQuestionnaires: greenCompletedQuestionnaires, consultations: consultations]
	}

    private def convertDateTypes(params) {
        if (params.start && params.start instanceof String) {
            def startDate = Calendar.getInstance()
            startDate.set(params.int('start_year'), params.int('start_month') - 1, params.int('start_day'), 0, 0, 0)
            def endDate = Calendar.getInstance()
            endDate.set(params.int('end_year'), params.int('end_month') - 1, params.int('end_day'), 0, 0, 0)
            params.start = startDate.getTime()
            params.end = endDate.getTime()
        }
    }

    @Secured([PermissionName.METER_READ_ALL, PermissionName.MONITOR_KIT_READ_ALL])
	def equipment () {

		def p = Patient.get(params.id)
        sessionService.setPatient(session,p)

		def kits = MonitorKit.findAllByPatient(p)
		def meters = Meter.findAllByPatient(p)
		[patientInstance: p, kits:kits,meters:meters]
	}

    @Secured(PermissionName.PATIENT_WRITE)
	def removeNextOfKin(Long id, Long nextOfKin) {
		def patientInstance = Patient.get(id)

		if (!patientInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'patient.label', default: 'Patient')])
			redirect(action: "edit", params: [id: id])
			return
		}
		
        def nextOfKinPersonInstance = NextOfKinPerson.get(nextOfKin)
		if (!nextOfKinPersonInstance) {
			flash.message = message(code: 'default.not.found.message', args: [message(code: 'patient2NextOfKinPerson.label', default: 'Next of kin')])
			redirect(action: "edit", params: [id: id])
			return
		}
		def name = nextOfKinPersonInstance.nameAndRelation
        patientInstance.nextOfKinPersons.remove(nextOfKinPersonInstance)
		nextOfKinPersonInstance.delete()

        flash.message = message(code: "patient.nextOfKin.removed", args:[name])

		redirect(action: "edit", id: id)
	}

    @Secured(PermissionName.PATIENT_WRITE)
	def removeKit() {
		// TODO: Add to service
		def kit = MonitorKit.get(params.id)
		String name = kit.name
		def p = Patient.get(session.patientId)
		def meters = kit.meters
		meters.each {meter ->
			meter.patient = null
			meter.save()
		}

		p.removeFromMonitorKits(kit)
		p.save()

		flash.message = message(code: "patient.equipment.kit.removed", args:[name])
		redirect(action: "equipment", id: session.patientId)
	}

    @Secured(PermissionName.PATIENT_WRITE)
	def removeMeter() {
		// TODO: Add to service
		def meter = Meter.get(params.id)
		def msg
		if (!meter?.monitorKit) {
			meter.patient = null
			meter.save()
			msg = message(code: "patient.equipment.meter.removed", args:[meter?.model])
		} else {
			def name = (meter?.monitorKit?.name?: "No meter")
			msg = message(code: "patient.equipment.meter.error.in.kit", args:[name])
		}

		flash.message = msg
		redirect(action: "equipment", id: session.patientId)
	}

    @Secured(PermissionName.PATIENT_WRITE)
	def addKit() {
		// TODO: Add to service
		def kits = MonitorKit.findAllWhere(patient:null)

		if (params.id) {
			def p = Patient.get(session.patientId)
			def kit = MonitorKit.get(params.id)
			p.addToMonitorKits(kit)

			kit.meters.each { meter ->
				meter.patient = p
				meter.save()
			}

			flash.message = message(code: "patient.equipment.kit.added", args:[kit.name])
			redirect(action: "equipment", id: session.patientId)
		}

		[kits:kits]
	}

    @Secured(PermissionName.PATIENT_WRITE)
	def addMeter() {
		// TODO: Add to service
		def meters = Meter.findAllWhere(patient:null, monitorKit:null)

		if (params.id) {
			def p = Patient.get(session.patientId)
			def meter = Meter.get(params.id)
			meter.patient = p
			meter.save()

			flash.message = message(code: "patient.equipment.meter.added", args:[meter.model])
			redirect(action: "equipment", id: session.patientId)
		}

		[meters:meters]
	}

    @Secured(PermissionName.MESSAGE_READ)
	def messages() {
		def patient = Patient.get(params.id)
        // Setting up session values
        sessionService.setPatient(session, patient)
        def clinician = Clinician.findByUser(springSecurityService.currentUser)

        if(clinicianMessageService.clinicianCanSendMessagesToPatient(clinician, patient)) {
            def messageList = Message.findAll("from Message as m where m.patient=(:patient) order by m.sendDate desc", [patient: patient])
            [canSendMessages: true, patientInstance: patient, messages: messageList]
        } else {
            [canSendMessages: false, patientInstance: patient]
        }
	}

    @Secured(PermissionName.PATIENT_PREFERENCES_WRITE)
    def savePrefs() {
        def clinician = Clinician.findByUser(springSecurityService.currentUser)
        def patient = Patient.get(params.patientID)

        def preferredQuestionIds = params.preferredQuestionId

        savePref(clinician, preferredQuestionIds)

        redirect(action: "questionnaires", params: [id: patient.id])
    }

    @Secured(PermissionName.PATIENT_REMOVE_BLUE_ALARMS)
    def removeBlueAlarms() {
        def clinician = Clinician.findByUser(springSecurityService.currentUser)
        def patient = Patient.get(params.patientID)

        patientService.removeAllBlueAlarms(patient)

        redirect(action: "questionnaires", id: patient.id, params: session.lastFilterParams)
    }

	private def savePref(Clinician c, def preferredQuestionIds) {
		//Clear old prefs, ignore 'empty' submit
		if (preferredQuestionIds) {
			def query = "from ClinicianQuestionPreference as cqp where cqp.clinician.id=(:c_id)"
			def questionPrefs = ClinicianQuestionPreference.findAll(query, [c_id: c.id])
			questionPrefs.each {
				it.delete()
			}
		}
		
		preferredQuestionIds.each {
			//Question id - ignore id=-1 as that would be the 'V��lg..' select
			try {
				def id = Integer.parseInt(it);
				if (id > 0) {
					def pref = new ClinicianQuestionPreference()
					pref.question = QuestionnaireNode.get(it)
					pref.clinician = c
					pref.save(failOnError:true)
				}
			} catch (Exception e) {
				//Ignore
			}
		}
	}

    @Secured(PermissionName.PATIENT_REMOVE_BLUE_ALARMS)
	def removeAllBlue() {
		Patient patient = Patient.get(params.patientID)
        patientService.removeAllBlueAlarms(patient)

		redirect(controller: 'patientOverview', action: 'index')
	}

    @Secured(PermissionName.PATIENT_DISABLE_ALARM_IF_UNREAD_MESSAGES_TO_PATIENT)
    def noAlarmIfUnreadMessagesToPatient() {
        Patient patient = Patient.get(params.patientID)
        patientService.noAlarmIfUnreadMessagesToPatient(patient)

        redirect(controller: 'patientOverview', action: 'index')
    }

    @Secured(PermissionName.PATIENT_WRITE)
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

    @Secured(PermissionName.PATIENT_WRITE)
    def addThreshold(Long id) {
        def patientInstance = Patient.get(id)
        [patientInstance: patientInstance,
         notUsedThresholds: measurementTypeService.getUnusedMeasurementTypesForThresholds(patientInstance.thresholds*.type*.name)]
    }

    @Secured(PermissionName.PATIENT_WRITE)
    def removeThreshold(Long id, Long threshold) {g
        def patientInstance = Patient.get(id)
        def thresholdInstance = Threshold.get(threshold)

        patientInstance.removeFromThresholds(thresholdInstance)
        patientInstance.save()
        thresholdInstance.delete()
        redirect(action: "show", id: patientInstance.id)
    }

    @Secured(PermissionName.PATIENT_WRITE)
    def saveThresholdToPatient(Long id) {
        def patientInstance = Patient.get(id)

        Threshold threshold = thresholdService.createThreshold(params)
        //If the threshold is OK, then update the patient
        if (threshold.validate()) {
            patientInstance.addToThresholds(threshold)

            if(!patientInstance.validate()) {
                render(view: "edit", model:  createEditModel(patientInstance))
            } else {
                patientInstance.save(flush: true)
                render(view:  "edit", model: createEditModel(patientInstance))
            }
        } else {
            render(view: "addThreshold",
                    model: [patientInstance: patientInstance,
                            standardThresholdInstance: threshold,
                            thresholdType: threshold.type.name,
                            notUsedThresholds: measurementTypeService.getUnusedMeasurementTypesForThresholds(patientInstance.thresholds*.type*.name)])
        }
    }

    @Secured(PermissionName.SET_PATIENT_RESPONSIBILITY)
    def updateDataResponsible() {

        Patient patientInstance = null
        if (params.id) {
            patientInstance = Patient.findById(params.id)
        }

        PatientGroup patientGroupInstance = null
        if (params.dataResponsible) {
            patientGroupInstance = PatientGroup.findById(params.dataResponsible)
        }

        if (!patientInstance) {
            render(view: "editResponsability", model: [patientInstance: patientInstance])
            return
        }

        patientInstance.dataResponsible = patientGroupInstance

        if(!patientInstance.save(flush: true)) {
            render(view: "editResponsability", model: [patientInstance: patientInstance])
            return
        }

        redirect(action: "edit", id: patientInstance.id)
    }

    @Secured(PermissionName.SET_PATIENT_RESPONSIBILITY)
    def editResponsability() {
        Patient patientInstance = Patient.findById(params.id)
        sessionService.setPatient(session, patientInstance)
        render(view: "editResponsability", model: [patientInstance: patientInstance])
    }

    @Secured(PermissionName.PATIENT_WRITE)
    def resetPassword(Long id) {
        def patientInstance = Patient.get(id)
        if (!patientInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patient.label', default: 'Patient')])
            redirect(action: "list")
            return
        }

        patientService.resetPassword(patientInstance)
        flash.message = message(code: 'patient.reset-password.done', args: [patientInstance.name])
        redirect(action: "show", id: id)
    }

    @Secured(PermissionName.PATIENT_WRITE)
    def sendPassword(Long id) {
        def patientInstance = Patient.get(id)
        if (!patientInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patient.label', default: 'Patient')])
            redirect(action: "list")
            return
        }

        patientService.sendPassword(patientInstance)
        flash.message = message(code: 'patient.send-password.done', args: [patientInstance.email])
        redirect(action: "show", id: id)
    }

    @Secured(PermissionName.PATIENT_WRITE)
    def unlockAccount(Long id) {
        def patientInstance = Patient.get(id)
        if (!patientInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'patient.label', default: 'Patient')])
            redirect(action: "list")
            return
        }

        passwordService.unlockAccount(patientInstance.user)
        flash.message = message(code: 'patient.unlock-account.done', args: [patientInstance.name])
        redirect(action: "show", id: id)

    }

    @Secured(PermissionName.PATIENT_CREATE)
    def patientSex() {
        def identification = params.identification

        def sex
        if (!identification) {
            sex = Sex.UNKNOWN
        } else {
            sex = patientIdentificationService.calculateSex(identification)
        }

        render(contentType: 'text/json') {[
            'sex': sex.toString()
        ]}
    }

    private def questionPreferencesForClinician(Clinician clinician) {
        if (clinician != null) {
            ClinicianQuestionPreference.findAllByClinicianAndQuestionIsNotNull(clinician, [sort:'id', order:'asc'])?.collect { it.questionId }

        } else {
            []
        }
    }

    private normalizeSortValue(def value) {
        value instanceof String ? value.toLowerCase() : value
    }
}
