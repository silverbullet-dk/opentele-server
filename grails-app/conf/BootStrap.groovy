import groovy.time.TimeCategory
import opentele.server.ExportCTGToMilouJob
import opentele.server.ExportMeasurementsToKihDbJob
import opentele.server.ExportRealTimeCTGToMilouJob
import opentele.server.QuestionnaireScheduleCheckJob
import org.opentele.server.core.RoleNameService
import org.opentele.server.core.model.BootStrapUtil
import org.opentele.server.core.model.Schedule
import org.opentele.server.core.model.types.MeasurementTypeName
import org.opentele.server.core.model.types.MeterTypeName
import org.opentele.server.core.model.types.NoteType
import org.opentele.server.core.model.types.PatientState
import org.opentele.server.core.model.types.PermissionName
import org.opentele.server.core.model.types.ProteinValue
import org.opentele.server.core.model.types.Severity
import org.opentele.server.core.model.types.Sex
import org.opentele.server.core.model.types.Unit
import org.opentele.server.core.model.types.Weekday
import org.opentele.server.core.questionnaire.RMQuestionnaireBuilder
import org.opentele.server.core.questionnaire.RNQuestionnaireBuilder
import org.opentele.server.core.questionnaire.TestQuestionnaireBuilder
import org.opentele.server.core.util.ISO8601DateParser
import org.opentele.server.core.util.JSONMarshallerUtil
import org.opentele.server.model.*
import org.opentele.server.model.cgm.ContinuousBloodSugarMeasurement
import org.opentele.server.model.cgm.CoulometerReadingEvent
import org.opentele.server.model.cgm.ExerciseEvent
import org.opentele.server.model.cgm.GenericEvent
import org.opentele.server.model.cgm.HyperAlarmEvent
import org.opentele.server.model.cgm.HypoAlarmEvent
import org.opentele.server.model.cgm.ImpendingHyperAlarmEvent
import org.opentele.server.model.cgm.ImpendingHypoAlarmEvent
import org.opentele.server.model.cgm.InsulinEvent
import org.opentele.server.model.cgm.MealEvent
import org.opentele.server.model.cgm.StateOfHealthEvent
import org.opentele.server.model.patientquestionnaire.*
import org.opentele.server.model.questionnaire.Questionnaire
import org.opentele.server.model.questionnaire.QuestionnaireGroup
import org.opentele.server.model.questionnaire.QuestionnaireHeader
import org.opentele.server.model.questionnaire.StandardSchedule
import org.opentele.server.provider.constants.Constants
import org.opentele.server.util.HelpImageUtil
import org.springframework.web.context.support.WebApplicationContextUtils

import java.text.SimpleDateFormat

@SuppressWarnings("GroovyDocCheck")
class BootStrap {
    private static final String LINDA_CPR = '0101860124'
    private static final String ERNA_CPR = '0101800124'
    private static final String KIRAN_CPR = '1103811376'
    private static final String NANCY_CPR = '2512484916'
    private static final String LENE_CPR = '2512688916'
    private static final String SVEND_CPR = '1212852635'

    private static final String CARL_CPR = '1105491135'
    private static final String JOHN_CPR = '1206322639'

    private static final CREATED_BY_NAME = "BootStrap"

    // Test patient groups
    static String praeklampsi = "Præeklampsi"
    static String hjertepatient = "Hjertepatient"
    static final String PPROM = "PPROM"
    static String lungs = "Lungemedicinsk"

    // Test department names
    static String afdelingBTest = "Afdeling-B Test"
    static String afdelingYTest = "Afdeling-Y Test"
    static String afdelingDemo = "Lungemedicinsk Afdeling"

    // Test clinician data
    static def clinicianHelleAndersen = [
            firstName: 'Helle', lastName: 'Andersen',
            email    : 'helle@andersen'
    ]
    static def clinicianJensHansen = [
            firstName: 'Jens', lastName: 'Hansen',
            email    : 'jens@hansen'
    ]
    static def clinicianDoktorHansen = [
            firstName: 'Doktor', lastName: 'Hansen',
            email    : 'drhansen@sygehuset'
    ]
    static def clinicianAllAccess = [
            firstName: 'Super', lastName: 'Adgang',
            email    : 'super@adgang'
    ]

    // Test patient data
    static def patientNancyAnn = [
            firstName: 'Nancy Ann', lastName: 'Berggren', cpr: NANCY_CPR,
            address  : 'Åbogade 15', postalCode: '8200', city: 'Aarhus N'
    ]

    static def patientLeneJensen = [
            firstName: 'Lene', lastName: 'Jensen', cpr: LENE_CPR,
            address  : 'Åbogade 25', postalCode: '8200', city: 'Aarhus N'
    ]

    static def patientKiran = [
            firstName: 'Kiran', lastName: 'Liaqat', cpr: KIRAN_CPR,
            address  : 'Gade 15', postalCode: '8000', city: 'Aarhus C'
    ]

    static def patientSvend = [
            firstName: 'Svend', lastName: 'Andersson', cpr: SVEND_CPR,
            address  : 'Gade 15', postalCode: '8000', city: 'Aarhus C'
    ]

    static def patientCarl = [
            firstName: 'Carl', lastName: 'Petersen', cpr: CARL_CPR,
            address  : 'Gadekæret 2', postalCode: '8270', city: 'Højbjerg'
    ]

    static def patientJohn = [
            firstName: 'John', lastName: 'Hansen', cpr: JOHN_CPR,
            address  : 'Vejgade 25', postalCode: '8230', city: 'Åbyhøj'
    ]

    static String testMessageHeader = "Ondt i halsen"
    static String testMessageText = """Kære Helle.
                Jeg har haft ondt i halsen siden søndag, hvor jeg efter anbefaling fra min gode ven kemikaliegrossist Svend Indler begyndte at bruge "Svends Vidunderskyllemiddel", et saltsyrebaseret præparat.

                Bør jeg standse med at bruge midlet eller er det noget helt andet der er fat med mig?

                Venligst Erna Hansen"""

    static String testMessageReplyHeader = "Sv. vedr. ondt i halsen"

    static String testMessageReplyText = """Kære Erna Hansen

                Saltsyre er ikke det bedste mundskyllemiddel du kan anvende og hvis koncentrationen er høj kan du faktisk komme ud i en situation, hvor der til sidst ikke er nogen mund at skylle.

                Jeg vil anbefale dig at standse med midlet og i stedet skylle munden i en let saltvandsopløsning.

                Mvh.

                Helle Andersen"""

    static String testMessagePatientReplyHeader = "Mange tak"

    static String testMessagePatientReplyText = """Kære Helle

                Mange tak for hjælpen! Jeg har det allerede meget bedre!

                Hilsen Erna"""


    def springSecurityService
    def questionnaireService
    def grailsApplication
    def passwordService
    def bootstrapQuestionnaireService
    def patientOverviewMaintenanceService
    def patientIdentificationService

    BootStrapUtil bootStrapUtil = null

    def init = { servletContext ->
        def applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext)
        configureDatasource(applicationContext)

        patientIdentificationService.setupValidation()
        bootStrapUtil = new BootStrapUtil()

        // Setup marshaller
        JSONMarshallerUtil.registerCustomJSONMarshallers(grailsApplication)

        passwordService.initialize(applicationContext)
        environments {
            development {
                println "Initializing for DEVEL"
                doBootstrapForTest()
                createPatientOverviewDataForPatients()

            }
            performance {
                println "Initializing for PERFORMANCE"
                doBootstrapForTest()

                createNancyAnnsClones(10)
                createHelleClones(10)

                createNancyAnnsClones(500)
                createHelleClones(100)
                createPatientOverviewDataForPatients()
            }
            test {
                println "Initializing for TEST"
                doBootstrapForTest()
                createPatientOverviewDataForPatients()
            }
        }

        cleanUpOldPermissions()

        if (Boolean.valueOf(grailsApplication.config.milou.run)) {
            ExportCTGToMilouJob.schedule(grailsApplication.config.milou.repeatIntervalMillis as Long, -1, [name: 'ExportCTGToMilou', startDelay: 60000])
        }

        if (Boolean.valueOf(grailsApplication.config.kihdb.run)) {
            ExportMeasurementsToKihDbJob.schedule(grailsApplication.config.kihdb.repeatIntervalMillis as Long, -1, [name: 'ExportMeasurementsToKihDb', startDelay: 30000])
        }

        if (Boolean.valueOf(grailsApplication.config.milou.realtimeRun)) {
            ExportRealTimeCTGToMilouJob.schedule(grailsApplication.config.milou.realtimeRepeatIntervalMillis as Long, -1, [name: 'ExportRealtimeCTGToMilou', startDelay: 60000])
        }


        QuestionnaireScheduleCheckJob.schedule(Constants.BLUE_ALARM_CHECK_INTERVAL * 60000, -1, [name: 'CheckForBlueAlarms', startDelay: 50000])
//        simple(name: 'CheckForBlueAlarms', startDelay: 20000, repeatInterval: Constants.BLUE_ALARM_CHECK_INTERVAL*60000, repeatCount: -1)

    }

    private void createPatientOverviewDataForPatients() {
        println 'Creating patient overview for patients'
        List<Patient> patientsWithNoPatientOverviewDetails = Patient.findAll('from Patient p where p.id not in (select po.patient.id from PatientOverview po)')
        patientsWithNoPatientOverviewDetails.each {
            println "Creating overview for patient with id: ${it.id}"
            patientOverviewMaintenanceService.createOverviewFor(it)
        }
    }

    private static void cleanUpOldPermissions() {
        // General clean-up of unused permissions
        BootStrapUtil.removePermission('ROLE_ADMINISTRATOR')
        BootStrapUtil.removePermission('ROLE_QUESTIONNAIRE_SUBMIT')
        BootStrapUtil.removePermission('ROLE_NODE_RESULT_DELETE')
        BootStrapUtil.removePermission('ROLE_NODE_RESULT_WRITE')
        BootStrapUtil.removePermission('ROLE_NODE_RESULT_READ')
        BootStrapUtil.removePermission('ROLE_NODE_RESULT_CREATE')
        BootStrapUtil.removePermission('ROLE_NODE_RESULT_READ_ALL')
        BootStrapUtil.removePermission('ROLE_COMPLETED_QUESTIONNAIRE_DELETE')
        BootStrapUtil.removePermission('ROLE_COMPLETED_QUESTIONNAIRE_WRITE')
        BootStrapUtil.removePermission('ROLE_COMPLETED_QUESTIONNAIRE_CREATE')
        BootStrapUtil.removePermission('ROLE_QUESTIONNAIRE_SCHEDULE_READ')
        BootStrapUtil.removePermission('ROLE_MESSAGE_DELETE')
        BootStrapUtil.removePermission('ROLE_MESSAGE_DELETE_JSON')
        BootStrapUtil.removePermission('ROLE_MEASUREMENT_CREATE')
        BootStrapUtil.removePermission('ROLE_CLAIM_PATIENT_RESPONSIBILITY')
        BootStrapUtil.removePermission('ROLE_MEASUREMENT_WRITE')
        BootStrapUtil.removePermission('ROLE_MEASUREMENT_DELETE')
        BootStrapUtil.removePermission('ROLE_MEASUREMENT_READ_ALL')

        // Removal of Clinician2PatientGroupController
        BootStrapUtil.removePermission('ROLE_CLINICIAN_PATIENT_GROUP_READ')
        BootStrapUtil.removePermission('ROLE_CLINICIAN_PATIENT_GROUP_READ_ALL')
        BootStrapUtil.removePermission('ROLE_CLINICIAN_PATIENT_GROUP_CREATE')
        BootStrapUtil.removePermission('ROLE_CLINICIAN_PATIENT_GROUP_WRITE')
    }

    private void patchQuestionnairesWithoutPatientQuestionnaires(Clinician creator) {
        QuestionnaireHeader.findAllByActiveQuestionnaireIsNotNull().each { questionnaireHeader ->
            Questionnaire questionnaire = questionnaireHeader.activeQuestionnaire
            if (questionnaire.patientQuestionnaires == null || questionnaire.patientQuestionnaires.empty) {
                questionnaireService.createPatientQuestionnaire(creator, questionnaire)
            }
        }
    }


    def doBootstrapForTest() {
        setupRolesAndTypes()

        Role role = bootStrapUtil.setupRoleIfNotExists(RoleNameService.roleReadAllPatientsInSystem, new Date())
        bootStrapUtil.setupPermissionsForRole(role)

        createAdminUser("admin_23")
        createOrganizationObjectsForTest()

        createCliniciansForTest()
        createTestPatients()

        def schemaCreator = Clinician.findByFirstNameAndLastName(clinicianHelleAndersen.firstName, clinicianHelleAndersen.lastName)

        def nancyAnn = Patient.findByCpr(NANCY_CPR)
        def kiran = Patient.findByCpr(KIRAN_CPR)
        def pErna = Patient.findByCpr(ERNA_CPR)
        def pLinda = Patient.findByCpr(LINDA_CPR)
        def pLene = Patient.findByCpr(LENE_CPR)

        Department deptY = Department.findByName(afdelingYTest)
        Department deptDemo = Department.findByName(afdelingDemo)

        createMessagesForTest(pErna, deptY)
        createMonitorKitsAndMetersForTest(nancyAnn, kiran, deptY, pLene, deptDemo)
        createQuestionnairesForTest(schemaCreator)

        // ..assign the questionnaires to patients...
        createPatientQuestionnairesForTest(schemaCreator, nancyAnn, pLinda, pLene)
        createConferencesForTest(schemaCreator, nancyAnn, pLene)

    }

    def createNancyAnnsClones(int count) {

        def schemaCreator = Clinician.findByFirstNameAndLastName(clinicianHelleAndersen.firstName, clinicianHelleAndersen.lastName)
        PatientGroup heart = PatientGroup.findByName(hjertepatient)

        Date now = new Date()
        Date today = now.clone().clearTime()

        // Create lots of small Nancies
        for (int i = 0; i < count; i++) {

            if (i % 5 == 0) {
                println "created ${i} Nancy clones so far}"
            }

            def cpr = "251248${String.format('%04d', i)}"

            Patient pt = createPatientIfNotExists(firstName: "${patientNancyAnn.firstName}perf${i}",
                    lastName: patientNancyAnn.lastName,
                    cpr: cpr,
                    sex: Sex.FEMALE,
                    address: patientNancyAnn.address,
                    postalCode: patientNancyAnn.postalCode,
                    city: patientNancyAnn.city,
                    mobilePhone: null,
                    phone: null,
                    email: null)
            addNancyAnnLikeRelationsToPatient(pt, heart, today, now)
            assignNancyLikeQuestionnairesToPatient(schemaCreator, pt)
            createConferencesForTest(schemaCreator, pt)
        }
    }

    def doBootstrapForEnglishTest() {
        RoleNameService.roleAdministrator = "Administrator"
        RoleNameService.rolePatient = "Patient"
        RoleNameService.roleClinician = "Clinican"
        RoleNameService.roleVideoConsultant = "Video consultant"
        RoleNameService.roleReadAllPatientsInSystem = "Access all patients"

        praeklampsi = 'Preeclampsia'
        hjertepatient = 'Heart Patient'
        lungs = 'Obstructive Lung Disease Clinic'

        afdelingBTest = 'Department B'
        afdelingYTest = 'Department Y'
        afdelingDemo = 'Department of Pulmonary Medicine'

        clinicianHelleAndersen = [
                firstName: 'Helen', lastName: 'Anderson',
                email    : 'helen@anderson'
        ]
        clinicianJensHansen = [
                firstName: 'John', lastName: 'Hansson',
                email    : 'john@hansson'
        ]
        clinicianDoktorHansen = [
                firstName: 'Doctor', lastName: 'Hansson',
                email    : 'drhansson@hospital'
        ]
        clinicianAllAccess = [
                firstName: 'All', lastName: 'Access',
                email    : 'all@access'
        ]

        patientNancyAnn = [
                firstName: 'Nancy Ann', lastName: 'Doe', cpr: NANCY_CPR,
                address  : '150 Tremont St', postalCode: 'MA 02111', city: 'Boston, Downtown'
        ]

        patientLeneJensen = [
                firstName: 'Lisa', lastName: 'Jensen', cpr: LENE_CPR,
                address  : '21 Carter Building Washington', postalCode: 'DC 20510', city: 'Washington DC'
        ]

        patientKiran = [
                firstName: 'Kimberly', lastName: 'Smith', cpr: KIRAN_CPR,
                address  : '24 Square Building Washington', postalCode: 'DC 20510', city: 'Washington DC'
        ]

        patientSvend = [
                firstName: 'Sven', lastName: 'Andersson', cpr: SVEND_CPR,
                address  : '1 Broadway', postalCode: '10007', city: 'New York NY'
        ]


        patientCarl = [
                firstName: 'Carl', lastName: 'Peterson', cpr: CARL_CPR,
                address  : '2 Avenue', postalCode: '10022', city: 'New York NY'
        ]

        patientJohn = [
                firstName: 'John', lastName: 'Doe', cpr: JOHN_CPR,
                address  : '25 Lower Street', postalCode: '10022', city: 'New York NY'
        ]


        testMessageHeader = "Sore throat"
        testMessageText = """Dear Sir or Madam
                I am having problems with a sore throat.

                Please contact me whenever possible.

                Sincerely Lisa Jensen"""

        testMessageReplyHeader = "Re: Sore throat"

        testMessageReplyText = """Dear Lisa

                You will be contacted by our staff as soon as possible!

                Sincerely,

                Helen Anderson"""

        testMessagePatientReplyHeader = "Thank you!"

        testMessagePatientReplyText = """Dear Helen

                Thank you for helping me!

                Sincerely,

                Helen Lisa Jensen"""

        setupRolesAndTypes()

        createAdminUser("admin_23")
        createOrganizationObjectsForTest()

        createCliniciansForTest()
        createTestPatients()

        def schemaCreator = Clinician.findByFirstNameAndLastName(clinicianHelleAndersen.firstName, clinicianHelleAndersen.lastName)

        def nancyAnn = Patient.findByCpr(NANCY_CPR)
        def kiran = Patient.findByCpr(KIRAN_CPR)
        def pLinda = Patient.findByCpr(LINDA_CPR)
        def pLene = Patient.findByCpr(LENE_CPR)
        def pErna = Patient.findByCpr(ERNA_CPR)
        def pSvend = Patient.findByCpr(SVEND_CPR)
        def pCarl = Patient.findByCpr(CARL_CPR)
        def pJohn = Patient.findByCpr(JOHN_CPR)

        Department deptY = Department.findByName(afdelingYTest)
        Department deptDemo = Department.findByName(afdelingDemo)

        createQuestionnairesForEnglishTest(schemaCreator)
        createMonitorKitsAndMetersForTest(nancyAnn, kiran, deptY, pLene, deptDemo)
        // ..assign the questionnaires to patients...
        createPatientQuestionnairesForEnglishTest(schemaCreator, nancyAnn, pLinda, pLene, pErna, pSvend, pCarl, pJohn)

        createConferencesForTest(schemaCreator, nancyAnn, pLene)
        createMessagesForTest(pLene, deptDemo)

        createPatientNoteForEnglishTest(nancyAnn, "Please check if BP values are withing range", NoteType.IMPORTANT,)

        createPatientNoteForEnglishTest(pSvend, "Please remember to add extra BP measurement to plan.", NoteType.IMPORTANT, null)

        createPatientNoteForEnglishTest(kiran, "Please add extra BP measurement to schedule.")
        createPatientNoteForEnglishTest(kiran, "Patient has difficulties hearing. Remember to ask if hearing aid is turned on.")

        new BlueAlarmCheck(checkDate: new Date() - 3).save(failOnError: true)
    }

    /**
     * Configure connectionpool to check connections
     *
     * @see http://java.dzone.com/news/database-connection-pooling
     * @see http://greybeardedgeek.net/2010/09/12/database-connection-pooling-in-grails-solving-the-idle-timeout-issue/
     */
    def configureDatasource(def applicationContext) {

        // Avoid dead connections in connectionpool

        //def ctx = servletContext.getAttribute(ApplicationAttributes.APPLICATION_CONTEXT)
        def dataSource = applicationContext.dataSourceUnproxied

        dataSource.setMinEvictableIdleTimeMillis(1000 * 60 * 30)
        dataSource.setTimeBetweenEvictionRunsMillis(1000 * 60 * 30)
        dataSource.setNumTestsPerEvictionRun(3)
        dataSource.setTestOnBorrow(true)
        dataSource.setTestWhileIdle(false)
        dataSource.setTestOnReturn(false)
        dataSource.setValidationQuery("SELECT 1")
    }

    def setupRolesAndTypes() {
        createPermissions()
        createRoles()
        createMeasurementTypes()
        createMeterTypes()
        createScheduleWindows()
    }

      def createPermissions() {
        PermissionName.allValues().collect {
            bootStrapUtil.createPermissionIfNotExists(it)
        }
    }

    def createRoles() {
        Date now = new Date()
        def role

        role = bootStrapUtil.setupRoleIfNotExists(RoleNameService.roleAdministrator)
        bootStrapUtil.setupPermissionsForRole(role)
        role = bootStrapUtil.setupRoleIfNotExists(RoleNameService.rolePatient, now)
        bootStrapUtil.setupPermissionsForRole(role)
        role = bootStrapUtil.setupRoleIfNotExists(RoleNameService.roleClinician, now)
        bootStrapUtil.setupPermissionsForRole(role)
        role = bootStrapUtil.setupRoleIfNotExists(RoleNameService.roleVideoConsultant, now)
        bootStrapUtil.setupPermissionsForRole(role)
    }

    def createAdminUser(String code) {
        def adminUser = setupUserIfNotExists('admin', code, 'admin', 'admin')
        Role adminRole = Role.findByAuthority(RoleNameService.roleAdministrator)
        bootStrapUtil.addUserToRoleIfNotExists(adminUser, adminRole)

        adminUser
    }

    User setupUserIfNotExists(String username, String password, String firstName, String lastName) {
        def user = User.findByUsername(username) ?: new User(
                username: username,
                password: password,
                enabled: true,
                createdBy: CREATED_BY_NAME,
                modifiedBy: CREATED_BY_NAME,
                createdDate: new Date(),
                modifiedDate: new Date()
        ).save(failOnError: true)

        if (!user.isClinician()) {
            new Clinician(
                    firstName: firstName,
                    lastName: lastName,
                    user: user
            ).save(failOnError: true)
        }

        return user
    }

    def createMeasurementTypes() {
        Date now = new Date()

        MeasurementTypeName.values().each { name ->
            bootStrapUtil.createMeasurementTypeIfNotExists(name, now)
        }
    }

    def createMeterTypes() {
        MeterTypeName.values().each { name ->
            bootStrapUtil.createMeterTypeIfNotExists(name)
        }
    }

    def createScheduleWindows() {
        def scheduleWindows = [new ScheduleWindow(scheduleType: Schedule.ScheduleType.WEEKDAYS, windowSizeHours: 10),
                               new ScheduleWindow(scheduleType: Schedule.ScheduleType.WEEKDAYS_ONCE, windowSizeHours: 23),
                               new ScheduleWindow(scheduleType: Schedule.ScheduleType.EVERY_NTH_DAY, windowSizeDays: 30),
                               new ScheduleWindow(scheduleType: Schedule.ScheduleType.MONTHLY, windowSizeDays: 1),
                               new ScheduleWindow(scheduleType: Schedule.ScheduleType.SPECIFIC_DATE, windowSizeDays: 1)]

        scheduleWindows.each { scheduleWindow ->
            bootStrapUtil.createScheduleWindowIfNotExists(scheduleWindow)
        }
    }

    def createOrganizationObjectsForTest() {

        Date now = new Date()

        Department deptB = bootStrapUtil.createDepartmentIfNotExists(afdelingBTest)
        Department deptY = bootStrapUtil.createDepartmentIfNotExists(afdelingYTest)
        Department deptDemo = bootStrapUtil.createDepartmentIfNotExists(afdelingDemo)

        //Set ThresholdSet for each created patientGroup
        def preEmpStandardThresholdSet = bootStrapUtil.createStandardThresholdSetForPatientGroup(['temperature', 'hemoglobin', 'crp'])
        def ppromStandardThresholdSet = bootStrapUtil.createStandardThresholdSetForPatientGroup(['saturation', 'weight', 'urine'])
        def heartStandardThresholdSet = bootStrapUtil.createStandardThresholdSetForPatientGroup(['bloodPressure', 'pulse'])

        def lungStandardThresholdSet = bootStrapUtil.createStandardThresholdSetForPatientGroup(['bloodPressure', 'pulse', 'saturation'])

        bootStrapUtil.createPatientGroupIfNotExists(praeklampsi, deptY, now, preEmpStandardThresholdSet, true)
        bootStrapUtil.createPatientGroupIfNotExists(PPROM, deptY, now, ppromStandardThresholdSet, true)
        bootStrapUtil.createPatientGroupIfNotExists(hjertepatient, deptB, now, heartStandardThresholdSet)
        bootStrapUtil.createPatientGroupIfNotExists(lungs, deptDemo, now, lungStandardThresholdSet)
    }

    def createEmptyStandardThresholdSet() {
        StandardThresholdSet thresholdSet = new StandardThresholdSet()
        thresholdSet.save(failOnError: true)
        thresholdSet
    }

    def createTestPatients() {

        Date now = new Date()
        Date today = now.clone().clearTime()

        Department deptY = Department.findByName(afdelingYTest)
        Department deptB = Department.findByName(afdelingBTest)
        Department deptDemo = Department.findByName(afdelingDemo)

        PatientGroup preEmp = PatientGroup.findByNameAndDepartment(praeklampsi, deptY)
        PatientGroup pprom = PatientGroup.findByNameAndDepartment(PPROM, deptY)
        PatientGroup heart = PatientGroup.findByNameAndDepartment(hjertepatient, deptB)
        PatientGroup lungs = PatientGroup.findByNameAndDepartment(lungs, deptDemo)

        Patient pLinda = createPatientIfNotExists(firstName: 'Linda',
                lastName: 'Hansen',
                cpr: LINDA_CPR,
                sex: Sex.FEMALE,
                address: 'Gravidgade 12',
                postalCode: '7988',
                city: 'Store Obstetringe',
                mobilePhone: '20827266',
                dueDate: (new Date() + 180),
                phone: null,
                email: 'lise.hansen@gmail.com')

        addPatient2PatientGroupIfNotExists(pLinda, preEmp)
        addPatient2PatientGroupIfNotExists(pLinda, pprom)
        addStandardThresholds2Patient(pLinda)

        def lindasPlan = MonitoringPlan.findByPatient(pLinda)
        if (!lindasPlan) {

            // Create monitoringplan for Linda
            lindasPlan = new MonitoringPlan(createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: now, modifiedDate: now)
            lindasPlan.patient = pLinda
            lindasPlan.startDate = today
            lindasPlan.save(failOnError: true)
        }
        pLinda.monitoringPlan = lindasPlan

        Patient pErna = createPatientIfNotExists(firstName: 'Erna',
                lastName: 'Hansen',
                cpr: ERNA_CPR,
                sex: Sex.FEMALE,
                address: 'Gravidgade 14',
                postalCode: '7988',
                city: 'Store Obstetringe',
                mobilePhone: '20827268',
                phone: null,
                email: 'erna.hansen@gmail.com',
                cleartextPassword: "abcd1234")

        addPatient2PatientGroupIfNotExists(pErna, preEmp)
        addPatient2PatientGroupIfNotExists(pErna, lungs)
        addStandardThresholds2Patient(pErna)

        Patient p = createPatientIfNotExists(firstName: 'Else',
                lastName: 'Nielsen',
                cpr: '0102820988',
                sex: Sex.FEMALE,
                address: 'Gade 15',
                postalCode: '8000',
                city: 'Aarhus C',
                mobilePhone: null,
                phone: null,
                email: null)

        addPatient2PatientGroupIfNotExists(p, preEmp)
        addStandardThresholds2Patient(p)

        p = createPatientIfNotExists(firstName: 'Anne',
                lastName: 'Kruuger',
                cpr: '0912789866',
                sex: Sex.FEMALE,
                address: 'Gade 15',
                postalCode: '8000',
                city: 'Aarhus C',
                mobilePhone: null,
                phone: null,
                email: null)

        addPatient2PatientGroupIfNotExists(p, pprom)
        addStandardThresholds2Patient(p)

        p = createPatientIfNotExists(firstName: 'Malene',
                lastName: 'Jensen',
                cpr: '1103825432',
                sex: Sex.FEMALE,
                address: 'Gade 15',
                postalCode: '8000',
                city: 'Aarhus C',
                mobilePhone: null,
                phone: null,
                email: null)
        addPatient2PatientGroupIfNotExists(p, pprom)
        addStandardThresholds2Patient(p)
        p = createPatientIfNotExists(firstName: 'Kirstine',
                lastName: 'Andersen',
                cpr: '0809772412',
                sex: Sex.FEMALE,
                address: 'Gade 15',
                postalCode: '8000',
                city: 'Aarhus C',
                mobilePhone: null,
                phone: null,
                email: null)
        addPatient2PatientGroupIfNotExists(p, preEmp)
        addStandardThresholds2Patient(p)
        p = createPatientIfNotExists(firstName: 'Line',
                lastName: 'Roslyng',
                cpr: '0708703242',
                sex: Sex.FEMALE,
                address: 'Gade 15',
                postalCode: '8000',
                city: 'Aarhus C',
                mobilePhone: null,
                phone: null,
                email: null)
        addPatient2PatientGroupIfNotExists(p, preEmp)
        addStandardThresholds2Patient(p)
        p = createPatientIfNotExists(firstName: 'Mette',
                lastName: 'Andersen',
                cpr: '1012852634',
                sex: Sex.FEMALE,
                address: 'Gade 15',
                postalCode: '8000',
                city: 'Aarhus C',
                mobilePhone: null,
                phone: null,
                email: null)
        addPatient2PatientGroupIfNotExists(p, pprom)
        addStandardThresholds2Patient(p)

        Patient kiran = createPatientIfNotExists(firstName: patientKiran.firstName,
                lastName: patientKiran.lastName,
                cpr: KIRAN_CPR,
                sex: Sex.FEMALE,
                address: patientKiran.address,
                postalCode: patientKiran.postalCode,
                city: patientKiran.city,
                mobilePhone: null,
                phone: null,
                email: null)
        addPatient2PatientGroupIfNotExists(kiran, pprom)
        addPatient2PatientGroupIfNotExists(kiran, lungs)
        addStandardThresholds2Patient(kiran)


        def kiransPlan = MonitoringPlan.findByPatient(kiran)
        if (!kiransPlan) {

            // Create monitoringplan for Kiran
            kiransPlan = new MonitoringPlan(createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: now, modifiedDate: now)
            kiransPlan.patient = kiran
            kiransPlan.startDate = today
            kiransPlan.save(failOnError: true)
        }
        kiran.monitoringPlan = kiransPlan

        [patientSvend, patientCarl, patientJohn].each { patientData ->
            Patient patient = createPatientIfNotExists(firstName: patientData.firstName,
                    lastName: patientData.lastName,
                    cpr: patientData.cpr,
                    sex: Sex.MALE,
                    address: patientData.address,
                    postalCode: patientData.postalCode,
                    city: patientData.city,
                    mobilePhone: null,
                    phone: null,
                    email: null,
                    cleartextPassword: "abcd1234")
            addPatient2PatientGroupIfNotExists(patient, pprom)
            addPatient2PatientGroupIfNotExists(patient, lungs)
            addStandardThresholds2Patient(patient)

            def plan = MonitoringPlan.findByPatient(patient)
            if (!plan) {

                // Create monitoringplan
                plan = new MonitoringPlan(createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: now, modifiedDate: now)
                plan.patient = patient
                plan.startDate = today
                plan.save(failOnError: true)
            }
            patient.monitoringPlan = plan
        }

        Patient nancyAnn = createPatientIfNotExists(firstName: patientNancyAnn.firstName,
                lastName: patientNancyAnn.lastName,
                cpr: NANCY_CPR,
                sex: Sex.FEMALE,
                address: patientNancyAnn.address,
                postalCode: patientNancyAnn.postalCode,
                city: patientNancyAnn.city,
                mobilePhone: null,
                phone: null,
                email: null)
        addNancyAnnLikeRelationsToPatient(nancyAnn, heart, today, now)

        Patient leneJensen = createPatientIfNotExists(firstName: patientLeneJensen.firstName,
                lastName: patientLeneJensen.lastName,
                cpr: LENE_CPR,
                sex: Sex.FEMALE,
                address: patientLeneJensen.address,
                postalCode: patientLeneJensen.postalCode,
                city: patientLeneJensen.city,
                mobilePhone: null,
                phone: null,
                email: null)
        addPatient2PatientGroupIfNotExists(leneJensen, lungs)
        addStandardThresholds2Patient(leneJensen)

        def leneJensensPlan = MonitoringPlan.findByPatient(leneJensen)
        if (!leneJensensPlan) {

            // Create monitoringplan for Mette
            leneJensensPlan = new MonitoringPlan(createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: now, modifiedDate: now)
            leneJensensPlan.patient = leneJensen
            leneJensensPlan.startDate = today
            leneJensensPlan.save(failOnError: true)
        }
        leneJensen.monitoringPlan = leneJensensPlan

    }

    def addNancyAnnLikeRelationsToPatient(def patient, def heart, def today, def now) {

        addPatient2PatientGroupIfNotExists(patient, heart)
        addStandardThresholds2Patient(patient)

        // addBloodsugarThreshold

        Threshold bloodsugarThreshold = new NumericThreshold(type: MeasurementType.findByName(MeasurementTypeName.BLOODSUGAR),
                alertLow: 1, warningLow: 3, warningHigh: 15, alertHigh: 20)
        bloodsugarThreshold.save(failOnError: true)
        patient.setThreshold(bloodsugarThreshold)
        patient.save(failOnError: true)

        def nancysPlan = MonitoringPlan.findByPatient(patient)
        if (!nancysPlan) {

            // Create monitoringplan for Nancy
            nancysPlan = new MonitoringPlan(createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: now, modifiedDate: now)
            nancysPlan.patient = patient
            nancysPlan.startDate = today
            nancysPlan.save(failOnError: true)
        }
        patient.monitoringPlan = nancysPlan
    }

    def createMonitorKitsAndMetersForTest(Patient nancyAnn, Patient kiran, Department deptY, Patient lene, Department deptDemo) {

        Date now = new Date()

        // MonitorKit
        MonitorKit kit1 = bootStrapUtil.createMonitorKit("SKJGYNA0001", deptY, nancyAnn, now)
        MonitorKit kit2 = bootStrapUtil.createMonitorKit("SKJGYNA0002", deptY, nancyAnn, now)
        MonitorKit kit3 = bootStrapUtil.createMonitorKit("SKJGYNA0002", deptY, kiran, now)

        MonitorKit kit4 = bootStrapUtil.createMonitorKit("KIT-004", deptDemo, lene, now)

        MeterType weightMeterType = MeterType.findByName(MeterTypeName.WEIGHT)
        MeterType bloodPressureMeterType = MeterType.findByName(MeterTypeName.BLOOD_PRESSURE_PULSE)
        MeterType urineMeterType = MeterType.findByName(MeterTypeName.URINE)
        MeterType ctgMeterType = MeterType.findByName(MeterTypeName.CTG)
        MeterType lungFunctionMeterType = MeterType.findByName(MeterTypeName.LUNG_FUNCTION)
        MeterType saturationMeterType = MeterType.findByName(MeterTypeName.SATURATION)
        MeterType bloodSugarMeterType = MeterType.findByName(MeterTypeName.BLOODSUGAR)
        MeterType cgmMeterType = MeterType.findByName(MeterTypeName.CONTINUOUS_BLOOD_SUGAR_MEASUREMENT)

        // Meters
        bootStrapUtil.createMeter("A&D Medical UC-321PBT-C", "054534", kit1, nancyAnn, weightMeterType)
        bootStrapUtil.createMeter("Hansen Healthcare XYZ", "58354", kit1, nancyAnn, bloodPressureMeterType)
        bootStrapUtil.createMeter("Hansen Healthcare XYZ", "58355", kit3, kiran, bloodPressureMeterType)
        bootStrapUtil.createMeter("Inkontinentia Healthcare", "6521", kit2, nancyAnn, urineMeterType)
        bootStrapUtil.createMeter("Monica Healthcare CTG", "123", kit2, nancyAnn, ctgMeterType)

        // Supported meters
        bootStrapUtil.createMeter("A&D Medical UA-767PBT", "012345", kit4, lene, bloodPressureMeterType)
        bootStrapUtil.createMeter("A&D Medical UC-321PBT-C", "012346", kit4, lene, weightMeterType)
        bootStrapUtil.createMeter("Vitalograph Model 4000", "012347", kit4, lene, lungFunctionMeterType)
        bootStrapUtil.createMeter("Nonin Model 9560BT Oximeter", "012348", kit4, lene, saturationMeterType)
        bootStrapUtil.createMeter("Monica AN24 Fetal Maternal Holter Recorder", "012349", kit4, lene, ctgMeterType)
        bootStrapUtil.createMeter("Accu-Chek mobile", "012350", kit4, lene, bloodSugarMeterType)
        bootStrapUtil.createMeter("Abbott FreeStyle Navigator", "012351", kit4, lene, cgmMeterType)
        bootStrapUtil.createMeter("Nonin 3230 Bluetooth Smart", "012352", kit4, lene, saturationMeterType)
    }

    def createCgmMeasurementsForTest(Patient patient, PatientQuestionnaire patientQuestionnaire) {
        def cgmMeasurement = new Measurement()
        cgmMeasurement.time = new Date() - 3
        cgmMeasurement.measurementType = MeasurementType.findByName(MeasurementTypeName.CONTINUOUS_BLOOD_SUGAR_MEASUREMENT)
        cgmMeasurement.patient = patient
        cgmMeasurement.unit = Unit.MMOL_L

        def eventTime = cgmMeasurement.time
        def glucoseValue = 3.0d
        def valueStep = 0.1d
        (0..999).each { time ->
            use( TimeCategory ) {
                eventTime = eventTime - 5.minutes
            }
            valueStep = glucoseValue >= 10 ? -0.1d : valueStep
            valueStep = glucoseValue <= 4 ? 0.1d : valueStep
            glucoseValue += valueStep
            cgmMeasurement.addToContinuousBloodSugarEvents(new ContinuousBloodSugarMeasurement(recordNumber: time, time: eventTime, glucoseValueInmmolPerl: glucoseValue))
        }

        cgmMeasurement.addToContinuousBloodSugarEvents(new HyperAlarmEvent(recordNumber: 2000, time: cgmMeasurement.time - 1, glucoseValueInmmolPerl: 1))
        cgmMeasurement.addToContinuousBloodSugarEvents(new HypoAlarmEvent(recordNumber: 2001, time: cgmMeasurement.time + 1 , glucoseValueInmmolPerl: 1))
        cgmMeasurement.addToContinuousBloodSugarEvents(new ImpendingHyperAlarmEvent(impendingNess: 10, recordNumber: 2002, time: cgmMeasurement.time - 1, glucoseValueInmmolPerl: 1))
        cgmMeasurement.addToContinuousBloodSugarEvents(new ImpendingHypoAlarmEvent(impendingNess: 10, recordNumber: 2003, time: cgmMeasurement.time - 1, glucoseValueInmmolPerl: 1))
        cgmMeasurement.addToContinuousBloodSugarEvents(new CoulometerReadingEvent(recordNumber: 2004, time: cgmMeasurement.time + 1, glucoseValueInmmolPerl: 5.6))
        cgmMeasurement.addToContinuousBloodSugarEvents(new InsulinEvent(recordNumber: 2005, time: cgmMeasurement.time + 1, insulinType: org.opentele.server.model.types.cgm.InsulinType.INTERMEDIATE, units: 10))
        cgmMeasurement.addToContinuousBloodSugarEvents(new ExerciseEvent(recordNumber: 2006, time: cgmMeasurement.time + 1, durationInMinutes: 13, exerciseIntensity: org.opentele.server.model.types.cgm.ExerciseIntensity.HIGH, exerciseType: org.opentele.server.model.types.cgm.ExerciseType.AEROBICS))
        cgmMeasurement.addToContinuousBloodSugarEvents(new MealEvent(recordNumber: 2007, time: cgmMeasurement.time - 1, foodType: org.opentele.server.model.types.cgm.FoodType.BREAKFAST, carboGrams: 1))
        cgmMeasurement.addToContinuousBloodSugarEvents(new StateOfHealthEvent(recordNumber: 2008, time: cgmMeasurement.time - 1, stateOfHealth: org.opentele.server.model.types.cgm.HealthState.DIZZY))
        cgmMeasurement.addToContinuousBloodSugarEvents(new GenericEvent(recordNumber: 2009, time: cgmMeasurement.time - 1, indicatedEvent: "1"))

        cgmMeasurement.save(failOnError: true, flush: true)

        Date now = new Date()
        CompletedQuestionnaire completedQuestionnaire = new CompletedQuestionnaire(createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: now, modifiedDate: now, uploadDate: now, receivedDate: now)
        completedQuestionnaire.setPatientQuestionnaire(patientQuestionnaire)
        completedQuestionnaire.setQuestionnaireHeader(patientQuestionnaire.getTemplateQuestionnaire().getQuestionnaireHeader())
        completedQuestionnaire.setPatient(patient)
        completedQuestionnaire.setSeverity(Severity.ORANGE)

        completedQuestionnaire.save(failOnError: true)

        PatientQuestionnaire cgmPatientQuestionnaire = completedQuestionnaire.patientQuestionnaire
        cgmPatientQuestionnaire.refresh()

        // Find the node producing output...
        def cgmNode = cgmPatientQuestionnaire.nodes.find { it.instanceOf(PatientMeasurementNode) }
        MeasurementType cgmMeasurementType = MeasurementType.findByName(MeasurementTypeName.CONTINUOUS_BLOOD_SUGAR_MEASUREMENT)

        // CGM result
        MeasurementNodeResult cgmResult = new MeasurementNodeResult(measurementType: cgmMeasurementType, createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: now, modifiedDate: now)
        cgmResult.setCompletedQuestionnaire(completedQuestionnaire)
        cgmResult.setCompletionTime(now)
        cgmResult.setSeverity(Severity.GREEN)

        cgmResult.addToMeasurements(cgmMeasurement)
        cgmResult.setPatientQuestionnaireNode(cgmNode)
        cgmResult.save(failOnError: true)
   }

    def createCtgMeasurementForTest(Patient patient, PatientQuestionnaire patientQuestionnaire) {
        Measurement ctgMeasurement = new Measurement()

        def fetalHeartRate = "[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.25, 147.25, 147.25, 147.25, 147.25, 147.25, 147.25, 147.25, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 151.5, 151.5, 151.5, 151.5, 151.5, 151.5, 151.5, 151.5, 152.5, 152.5, 152.5, 152.5, 152.5, 152.5, 152.5, 152.5, 151.75, 151.75, 151.75, 151.75, 151.75, 151.75, 151.75, 151.75, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 154.25, 154.25, 154.25, 154.25, 154.25, 154.25, 154.25, 154.25, 158.25, 158.25, 158.25, 158.25, 158.25, 158.25, 158.25, 158.25, 161.0, 161.0, 161.0, 161.0, 161.0, 161.0, 161.0, 161.0, 160.25, 160.25, 160.25, 160.25, 160.25, 160.25, 160.25, 160.25, 158.25, 158.25, 158.25, 158.25, 158.25, 158.25, 158.25, 158.25, 156.25, 156.25, 156.25, 156.25, 156.25, 156.25, 156.25, 156.25, 155.75, 155.75, 155.75, 155.75, 155.75, 155.75, 155.75, 155.75, 157.5, 157.5, 157.5, 157.5, 157.5, 157.5, 157.5, 157.5, 158.0, 158.0, 158.0, 158.0, 158.0, 158.0, 158.0, 158.0, 155.75, 155.75, 155.75, 155.75, 155.75, 155.75, 155.75, 155.75, 154.25, 154.25, 154.25, 154.25, 154.25, 154.25, 154.25, 154.25, 148.75, 148.75, 148.75, 148.75, 148.75, 148.75, 148.75, 148.75, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.25, 148.25, 148.25, 148.25, 148.25, 148.25, 148.25, 148.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.75, 147.75, 147.75, 147.75, 147.75, 147.75, 147.75, 147.75, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 138.5, 138.5, 138.5, 138.5, 138.5, 138.5, 138.5, 138.5, 134.25, 134.25, 134.25, 134.25, 134.25, 134.25, 134.25, 134.25, 129.0, 129.0, 129.0, 129.0, 129.0, 129.0, 129.0, 129.0, 128.5, 128.5, 128.5, 128.5, 128.5, 128.5, 128.5, 128.5, 128.5, 128.5, 128.5, 128.5, 128.5, 128.5, 128.5, 128.5, 130.5, 130.5, 130.5, 130.5, 130.5, 130.5, 130.5, 130.5, 129.25, 129.25, 129.25, 129.25, 129.25, 129.25, 129.25, 129.25, 132.25, 132.25, 132.25, 132.25, 132.25, 132.25, 132.25, 132.25, 131.5, 131.5, 131.5, 131.5, 131.5, 131.5, 131.5, 131.5, 136.5, 136.5, 136.5, 136.5, 136.5, 136.5, 136.5, 136.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 143.25, 143.25, 143.25, 143.25, 143.25, 143.25, 143.25, 143.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 150.25, 150.25, 150.25, 150.25, 150.25, 150.25, 150.25, 150.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 150.25, 150.25, 150.25, 150.25, 150.25, 150.25, 150.25, 150.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 152.25, 152.25, 152.25, 152.25, 152.25, 152.25, 152.25, 152.25, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 155.75, 155.75, 155.75, 155.75, 155.75, 155.75, 155.75, 155.75, 159.0, 159.0, 159.0, 159.0, 159.0, 159.0, 159.0, 159.0, 159.25, 159.25, 159.25, 159.25, 159.25, 159.25, 159.25, 159.25, 153.5, 153.5, 153.5, 153.5, 153.5, 153.5, 153.5, 153.5, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 147.75, 147.75, 147.75, 147.75, 147.75, 147.75, 147.75, 147.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 143.5, 143.5, 143.5, 143.5, 143.5, 143.5, 143.5, 143.5, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 148.75, 148.75, 148.75, 148.75, 148.75, 148.75, 148.75, 148.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 148.25, 148.25, 148.25, 148.25, 148.25, 148.25, 148.25, 148.25, 153.75, 153.75, 153.75, 153.75, 153.75, 153.75, 153.75, 153.75, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 138.5, 138.5, 138.5, 138.5, 138.5, 138.5, 138.5, 138.5, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 143.25, 143.25, 143.25, 143.25, 143.25, 143.25, 143.25, 143.25, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 139.25, 139.25, 139.25, 139.25, 139.25, 139.25, 139.25, 139.25, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 143.25, 143.25, 143.25, 143.25, 143.25, 143.25, 143.25, 143.25, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 143.5, 143.5, 143.5, 143.5, 143.5, 143.5, 143.5, 143.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 138.75, 138.75, 138.75, 138.75, 138.75, 138.75, 138.75, 138.75, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 138.75, 138.75, 138.75, 138.75, 138.75, 138.75, 138.75, 138.75, 137.0, 137.0, 137.0, 137.0, 137.0, 137.0, 137.0, 137.0, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 144.0, 144.0, 144.0, 144.0, 144.0, 144.0, 144.0, 144.0, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 151.0, 151.0, 151.0, 151.0, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 138.75, 138.75, 138.75, 138.75, 138.75, 138.75, 138.75, 138.75, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 150.25, 150.25, 150.25, 150.25, 150.25, 150.25, 150.25, 150.25, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 153.25, 153.25, 153.25, 153.25, 153.25, 153.25, 153.25, 153.25, 153.5, 153.5, 153.5, 153.5, 153.5, 153.5, 153.5, 153.5, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 147.75, 147.75, 147.75, 147.75, 147.75, 147.75, 147.75, 147.75, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 147.75, 147.75, 147.75, 147.75, 147.75, 147.75, 147.75, 147.75, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 149.0, 149.0, 149.0, 149.0, 149.0, 149.0, 149.0, 149.0, 151.5, 151.5, 151.5, 151.5, 151.5, 151.5, 151.5, 151.5, 153.75, 153.75, 153.75, 153.75, 153.75, 153.75, 153.75, 153.75, 153.25, 153.25, 153.25, 153.25, 153.25, 153.25, 153.25, 153.25, 151.75, 151.75, 151.75, 151.75, 151.75, 151.75, 151.75, 151.75, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 163.5, 163.5, 163.5, 163.5, 163.5, 163.5, 163.5, 163.5, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 163.75, 163.75, 163.75, 163.75, 163.75, 163.75, 163.75, 163.75, 165.25, 165.25, 165.25, 165.25, 165.25, 165.25, 165.25, 165.25, 167.0, 167.0, 167.0, 167.0, 167.0, 167.0, 167.0, 167.0, 157.5, 157.5, 157.5, 157.5, 157.5, 157.5, 157.5, 157.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 132.25, 132.25, 132.25, 132.25, 132.25, 132.25, 132.25, 132.25, 134.5, 134.5, 134.5, 134.5, 134.5, 134.5, 134.5, 134.5, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 156.25, 156.25, 156.25, 156.25, 156.25, 156.25, 156.25, 156.25, 161.5, 161.5, 161.5, 161.5, 161.5, 161.5, 161.5, 161.5, 163.0, 163.0, 163.0, 163.0, 163.0, 163.0, 163.0, 163.0, 164.5, 164.5, 164.5, 164.5, 164.5, 164.5, 164.5, 164.5, 165.5, 165.5, 165.5, 165.5, 165.5, 165.5, 165.5, 165.5, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 151.75, 151.75, 151.75, 151.75, 151.75, 151.75, 151.75, 151.75, 155.0, 155.0, 155.0, 155.0, 155.0, 155.0, 155.0, 155.0, 154.75, 154.75, 154.75, 154.75, 154.75, 154.75, 154.75, 154.75, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 153.0, 153.0, 153.0, 153.0, 153.0, 153.0, 153.0, 153.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 147.25, 147.25, 147.25, 147.25, 147.25, 147.25, 147.25, 147.25, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 149.0, 149.0, 149.0, 149.0, 149.0, 149.0, 149.0, 149.0, 153.25, 153.25, 153.25, 153.25, 153.25, 153.25, 153.25, 153.25, 153.0, 153.0, 153.0, 153.0, 153.0, 153.0, 153.0, 153.0, 151.5, 151.5, 151.5, 151.5, 151.5, 151.5, 151.5, 151.5, 152.5, 152.5, 152.5, 152.5, 152.5, 152.5, 152.5, 152.5, 153.5, 153.5, 153.5, 153.5, 153.5, 153.5, 153.5, 153.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 148.25, 148.25, 148.25, 148.25, 148.25, 148.25, 148.25, 148.25, 147.75, 147.75, 147.75, 147.75, 147.75, 147.75, 147.75, 147.75, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 150.25, 150.25, 150.25, 150.25, 150.25, 150.25, 150.25, 150.25, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 150.25, 150.25, 150.25, 150.25, 150.25, 150.25, 150.25, 150.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 153.75, 153.75, 153.75, 153.75, 153.75, 153.75, 153.75, 153.75, 154.75, 154.75, 154.75, 154.75, 154.75, 154.75, 154.75, 154.75, 157.75, 157.75, 157.75, 157.75, 157.75, 157.75, 157.75, 157.75, 161.5, 161.5, 161.5, 161.5, 161.5, 161.5, 161.5, 161.5, 164.75, 164.75, 164.75, 164.75, 164.75, 164.75, 164.75, 164.75, 168.25, 168.25, 168.25, 168.25, 168.25, 168.25, 168.25, 168.25, 169.0, 169.0, 169.0, 169.0, 169.0, 169.0, 169.0, 169.0, 162.25, 162.25, 162.25, 162.25, 162.25, 162.25, 162.25, 162.25, 159.25, 159.25, 159.25, 159.25, 159.25, 159.25, 159.25, 159.25, 161.5, 161.5, 161.5, 161.5, 161.5, 161.5, 161.5, 161.5, 164.75, 164.75, 164.75, 164.75, 164.75, 164.75, 164.75, 164.75, 163.5, 163.5, 163.5, 163.5, 163.5, 163.5, 163.5, 163.5, 157.5, 157.5, 157.5, 157.5, 157.5, 157.5, 157.5, 157.5, 152.5, 152.5, 152.5, 152.5, 152.5, 152.5, 152.5, 152.5, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 133.75, 133.75, 133.75, 133.75, 133.75, 133.75, 133.75, 133.75, 142.0, 142.0, 142.0, 142.0, 142.0, 142.0, 142.0, 142.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 137.25, 137.25, 137.25, 137.25, 137.25, 137.25, 137.25, 137.25, 128.75, 128.75, 128.75, 128.75, 128.75, 128.75, 128.75, 128.75, 136.75, 136.75, 136.75, 136.75, 136.75, 136.75, 136.75, 136.75, 138.0, 138.0, 138.0, 138.0, 138.0, 138.0, 138.0, 138.0, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 143.5, 143.5, 143.5, 143.5, 143.5, 143.5, 143.5, 143.5, 139.5, 139.5, 139.5, 139.5, 139.5, 139.5, 139.5, 139.5, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 139.5, 139.5, 139.5, 139.5, 139.5, 139.5, 139.5, 139.5, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 139.75, 139.75, 139.75, 139.75, 139.75, 139.75, 139.75, 139.75, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 135.75, 135.75, 135.75, 135.75, 135.75, 135.75, 135.75, 135.75, 130.75, 130.75, 130.75, 130.75, 130.75, 130.75, 130.75, 130.75, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 137.25, 137.25, 137.25, 137.25, 137.25, 137.25, 137.25, 137.25, 149.0, 149.0, 149.0, 149.0, 149.0, 149.0, 149.0, 149.0, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 144.0, 144.0, 144.0, 144.0, 144.0, 144.0, 144.0, 144.0, 138.25, 138.25, 138.25, 138.25, 138.25, 138.25, 138.25, 138.25, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 152.75, 152.75, 152.75, 152.75, 152.75, 152.75, 152.75, 152.75, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 151.0, 149.0, 149.0, 149.0, 149.0, 149.0, 149.0, 149.0, 149.0, 143.5, 143.5, 143.5, 143.5, 143.5, 143.5, 143.5, 143.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 160.0, 164.25, 164.25, 164.25, 164.25, 164.25, 164.25, 164.25, 164.25, 165.75, 165.75, 165.75, 165.75, 165.75, 165.75, 165.75, 165.75, 164.75, 164.75, 164.75, 164.75, 164.75, 164.75, 164.75, 164.75, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 134.25, 134.25, 134.25, 134.25, 134.25, 134.25, 134.25, 134.25, 129.75, 129.75, 129.75, 129.75, 129.75, 129.75, 129.75, 129.75, 135.75, 135.75, 135.75, 135.75, 135.75, 135.75, 135.75, 135.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 148.25, 148.25, 148.25, 148.25, 148.25, 148.25, 148.25, 148.25, 149.0, 149.0, 149.0, 149.0, 149.0, 149.0, 149.0, 149.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 151.5, 151.5, 151.5, 151.5, 151.5, 151.5, 151.5, 151.5, 156.5, 156.5, 156.5, 156.5, 156.5, 156.5, 156.5, 156.5, 157.5, 157.5, 157.5, 157.5, 157.5, 157.5, 157.5, 157.5, 153.5, 153.5, 153.5, 153.5, 153.5, 153.5, 153.5, 153.5, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 147.75, 147.75, 147.75, 147.75, 147.75, 147.75, 147.75, 147.75, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 139.5, 139.5, 139.5, 139.5, 139.5, 139.5, 139.5, 139.5, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 139.5, 139.5, 139.5, 139.5, 139.5, 139.5, 139.5, 139.5, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 144.0, 144.0, 144.0, 144.0, 144.0, 144.0, 144.0, 144.0, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 149.5, 149.5, 149.5, 149.5, 149.5, 149.5, 149.5, 149.5, 149.0, 149.0, 149.0, 149.0, 149.0, 149.0, 149.0, 149.0, 151.5, 151.5, 151.5, 151.5, 151.5, 151.5, 151.5, 151.5, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 142.0, 142.0, 142.0, 142.0, 142.0, 142.0, 142.0, 142.0, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 155.75, 155.75, 155.75, 155.75, 155.75, 155.75, 155.75, 155.75, 156.25, 156.25, 156.25, 156.25, 156.25, 156.25, 156.25, 156.25, 155.5, 155.5, 155.5, 155.5, 155.5, 155.5, 155.5, 155.5, 157.5, 157.5, 157.5, 157.5, 157.5, 157.5, 157.5, 157.5, 160.5, 160.5, 160.5, 160.5, 160.5, 160.5, 160.5, 160.5, 163.25, 163.25, 163.25, 163.25, 163.25, 163.25, 163.25, 163.25, 165.25, 165.25, 165.25, 165.25, 165.25, 165.25, 165.25, 165.25, 165.75, 165.75, 165.75, 165.75, 159.25, 159.25, 159.25, 159.25, 159.25, 159.25, 159.25, 159.25, 152.5, 152.5, 152.5, 152.5, 152.5, 152.5, 152.5, 152.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 154.25, 154.25, 154.25, 154.25, 154.25, 154.25, 154.25, 154.25, 153.0, 153.0, 153.0, 153.0, 153.0, 153.0, 153.0, 153.0, 161.0, 161.0, 161.0, 161.0, 161.0, 161.0, 161.0, 161.0, 161.5, 161.5, 161.5, 161.5, 161.5, 161.5, 161.5, 161.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 143.25, 143.25, 143.25, 143.25, 143.25, 143.25, 143.25, 143.25, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 136.25, 136.25, 136.25, 136.25, 136.25, 136.25, 136.25, 136.25, 147.75, 147.75, 147.75, 147.75, 147.75, 147.75, 147.75, 147.75, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 147.25, 147.25, 147.25, 147.25, 147.25, 147.25, 147.25, 147.25, 148.25, 148.25, 148.25, 148.25, 148.25, 148.25, 148.25, 148.25, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 147.75, 147.75, 147.75, 147.75, 147.75, 147.75, 147.75, 147.75, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 148.75, 148.75, 148.75, 148.75, 148.75, 148.75, 148.75, 148.75, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.25, 147.25, 147.25, 147.25, 147.25, 147.25, 147.25, 147.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 143.25, 143.25, 143.25, 143.25, 143.25, 143.25, 143.25, 143.25, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 147.25, 147.25, 147.25, 147.25, 147.25, 147.25, 147.25, 147.25, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 149.0, 149.0, 149.0, 149.0, 149.0, 149.0, 149.0, 149.0, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 149.0, 149.0, 149.0, 149.0, 149.0, 149.0, 149.0, 149.0, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.25, 148.25, 148.25, 148.25, 148.25, 148.25, 148.25, 148.25, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 147.25, 147.25, 147.25, 147.25, 147.25, 147.25, 147.25, 147.25, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 151.75, 151.75, 151.75, 151.75, 151.75, 151.75, 151.75, 151.75, 153.5, 153.5, 153.5, 153.5, 153.5, 153.5, 153.5, 153.5, 154.75, 154.75, 154.75, 154.75, 154.75, 154.75, 154.75, 154.75, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 139.75, 139.75, 139.75, 139.75, 139.75, 139.75, 139.75, 139.75, 136.0, 136.0, 136.0, 136.0, 136.0, 136.0, 136.0, 136.0, 138.5, 138.5, 138.5, 138.5, 138.5, 138.5, 138.5, 138.5, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 143.5, 143.5, 143.5, 143.5, 143.5, 143.5, 143.5, 143.5, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 150.0, 150.0, 150.0, 150.0, 150.0, 150.0, 150.0, 150.0, 154.75, 154.75, 154.75, 154.75, 154.75, 154.75, 154.75, 154.75, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 160.5, 160.5, 160.5, 160.5, 160.5, 160.5, 160.5, 160.5, 162.5, 162.5, 162.5, 162.5, 162.5, 162.5, 162.5, 162.5, 165.0, 165.0, 165.0, 165.0, 165.0, 165.0, 165.0, 165.0, 166.5, 166.5, 166.5, 166.5, 166.5, 166.5, 166.5, 166.5, 167.25, 167.25, 167.25, 167.25, 167.25, 167.25, 167.25, 167.25, 161.75, 161.75, 161.75, 161.75, 161.75, 161.75, 161.75, 161.75, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 134.25, 134.25, 134.25, 134.25, 134.25, 134.25, 134.25, 134.25, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 154.75, 154.75, 154.75, 154.75, 154.75, 154.75, 154.75, 154.75, 158.5, 158.5, 158.5, 158.5, 158.5, 158.5, 158.5, 158.5, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 165.75, 165.75, 165.75, 165.75, 165.75, 165.75, 165.75, 165.75, 169.0, 169.0, 169.0, 169.0, 169.0, 169.0, 169.0, 169.0, 163.0, 163.0, 163.0, 163.0, 163.0, 163.0, 163.0, 163.0, 151.25, 151.25, 151.25, 151.25, 151.25, 151.25, 151.25, 151.25, 155.0, 155.0, 155.0, 155.0, 155.0, 155.0, 155.0, 155.0, 157.5, 157.5, 157.5, 157.5, 157.5, 157.5, 157.5, 157.5, 159.0, 159.0, 159.0, 159.0, 159.0, 159.0, 159.0, 159.0, 160.25, 160.25, 160.25, 160.25, 160.25, 160.25, 160.25, 160.25, 159.5, 159.5, 159.5, 159.5, 159.5, 159.5, 159.5, 159.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.25, 150.25, 150.25, 150.25, 150.25, 150.25, 150.25, 150.25, 155.25, 155.25, 155.25, 155.25, 155.25, 155.25, 155.25, 155.25, 158.5, 158.5, 158.5, 158.5, 158.5, 158.5, 158.5, 158.5, 154.5, 154.5, 154.5, 154.5, 154.5, 154.5, 154.5, 154.5, 156.75, 156.75, 156.75, 156.75, 156.75, 156.75, 156.75, 156.75, 158.5, 158.5, 158.5, 158.5, 158.5, 158.5, 158.5, 158.5, 159.25, 159.25, 159.25, 159.25, 159.25, 159.25, 159.25, 159.25, 157.5, 157.5, 157.5, 157.5, 157.5, 157.5, 157.5, 157.5, 163.5, 163.5, 163.5, 163.5, 163.5, 163.5, 163.5, 163.5, 165.75, 165.75, 165.75, 165.75, 165.75, 165.75, 165.75, 165.75, 167.25, 167.25, 167.25, 167.25, 167.25, 167.25, 167.25, 167.25, 161.25, 161.25, 161.25, 161.25, 161.25, 161.25, 161.25, 161.25, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 130.0, 130.0, 130.0, 130.0, 130.0, 130.0, 130.0, 130.0, 128.75, 128.75, 128.75, 128.75, 128.75, 128.75, 128.75, 128.75, 129.5, 129.5, 129.5, 129.5, 129.5, 129.5, 129.5, 129.5, 128.5, 128.5, 128.5, 128.5, 128.5, 128.5, 128.5, 128.5, 134.25, 134.25, 134.25, 134.25, 134.25, 134.25, 134.25, 134.25, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 139.75, 139.75, 139.75, 139.75, 139.75, 139.75, 139.75, 139.75, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 139.0, 139.0, 139.0, 139.0, 139.0, 139.0, 139.0, 139.0, 139.5, 139.5, 139.5, 139.5, 139.5, 139.5, 139.5, 139.5, 138.75, 138.75, 138.75, 138.75, 138.75, 138.75, 138.75, 138.75, 139.5, 139.5, 139.5, 139.5, 139.5, 139.5, 139.5, 139.5, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 144.0, 144.0, 144.0, 144.0, 144.0, 144.0, 144.0, 144.0, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 139.75, 139.75, 139.75, 139.75, 139.75, 139.75, 139.75, 139.75, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 135.25, 135.25, 135.25, 135.25, 135.25, 135.25, 135.25, 135.25, 136.75, 136.75, 136.75, 136.75, 136.75, 136.75, 136.75, 136.75, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 143.5, 143.5, 143.5, 143.5, 143.5, 143.5, 143.5, 143.5, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 144.25, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 140.75, 140.75, 140.75, 140.75, 140.75, 140.75, 140.75, 140.75, 143.25, 143.25, 143.25, 143.25, 143.25, 143.25, 143.25, 143.25, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.25, 147.25, 147.25, 147.25, 147.25, 147.25, 147.25, 147.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 152.75, 152.75, 152.75, 152.75, 152.75, 152.75, 152.75, 152.75, 156.5, 156.5, 156.5, 156.5, 156.5, 156.5, 156.5, 156.5, 159.75, 159.75, 159.75, 159.75, 159.75, 159.75, 159.75, 159.75, 162.5, 162.5, 162.5, 162.5, 162.5, 162.5, 162.5, 162.5, 157.75, 157.75, 157.75, 157.75, 157.75, 157.75, 157.75, 157.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 151.5, 151.5, 151.5, 151.5, 151.5, 151.5, 151.5, 151.5, 152.75, 152.75, 152.75, 152.75, 152.75, 152.75, 152.75, 152.75, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.0, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 147.25, 147.25, 147.25, 147.25, 147.25, 147.25, 147.25, 147.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 144.0, 144.0, 144.0, 144.0, 144.0, 144.0, 144.0, 144.0, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 148.25, 148.25, 148.25, 148.25, 148.25, 148.25, 148.25, 148.25, 150.25, 150.25, 150.25, 150.25, 150.25, 150.25, 150.25, 150.25, 148.25, 148.25, 148.25, 148.25, 148.25, 148.25, 148.25, 148.25, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 136.0, 136.0, 136.0, 136.0, 136.0, 136.0, 136.0, 136.0, 136.75, 136.75, 136.75, 136.75, 136.75, 136.75, 136.75, 136.75, 138.75, 138.75, 138.75, 138.75, 138.75, 138.75, 138.75, 138.75, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 142.0, 142.0, 142.0, 142.0, 142.0, 142.0, 142.0, 142.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 138.75, 138.75, 138.75, 138.75, 138.75, 138.75, 138.75, 138.75, 139.0, 139.0, 139.0, 139.0, 139.0, 139.0, 139.0, 139.0, 136.5, 136.5, 136.5, 136.5, 136.5, 136.5, 136.5, 136.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 139.0, 139.0, 139.0, 139.0, 139.0, 139.0, 139.0, 139.0, 135.0, 135.0, 135.0, 135.0, 135.0, 135.0, 135.0, 135.0, 137.25, 137.25, 137.25, 137.25, 137.25, 137.25, 137.25, 137.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 139.25, 139.25, 139.25, 139.25, 139.25, 139.25, 139.25, 139.25, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 139.25, 139.25, 139.25, 139.25, 139.25, 139.25, 139.25, 139.25, 137.0, 137.0, 137.0, 137.0, 137.0, 137.0, 137.0, 137.0, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 138.25, 138.25, 138.25, 138.25, 138.25, 138.25, 138.25, 138.25, 139.75, 139.75, 139.75, 139.75, 139.75, 139.75, 139.75, 139.75, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 143.5, 143.5, 143.5, 143.5, 143.5, 143.5, 143.5, 143.5, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 137.25, 137.25, 137.25, 137.25, 137.25, 137.25, 137.25, 137.25, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 139.75, 139.75, 139.75, 139.75, 139.75, 139.75, 139.75, 139.75, 140.75, 140.75, 140.75, 140.75, 140.75, 140.75, 140.75, 140.75, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 141.0, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 140.75, 140.75, 140.75, 140.75, 140.75, 140.75, 140.75, 140.75, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 120.75, 120.75, 120.75, 120.75, 120.75, 120.75, 120.75, 120.75, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 142.0, 142.0, 142.0, 142.0, 142.0, 142.0, 142.0, 142.0, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 139.75, 139.75, 139.75, 139.75, 139.75, 139.75, 139.75, 139.75, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 143.5, 143.5, 143.5, 143.5, 143.5, 143.5, 143.5, 143.5, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 140.75, 140.75, 140.75, 140.75, 140.75, 140.75, 140.75, 140.75, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.75, 140.75, 140.75, 140.75, 140.75, 140.75, 140.75, 140.75, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 142.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 145.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 144.5, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 138.25, 138.25, 138.25, 138.25, 138.25, 138.25, 138.25, 138.25, 139.75, 139.75, 139.75, 139.75, 139.75, 139.75, 139.75, 139.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 145.0, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 139.5, 139.5, 139.5, 139.5, 139.5, 139.5, 139.5, 139.5, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 140.5, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 142.25, 137.25, 137.25, 137.25, 137.25, 137.25, 137.25, 137.25, 137.25, 138.75, 138.75, 138.75, 138.75, 138.75, 138.75, 138.75, 138.75, 139.0, 139.0, 139.0, 139.0, 139.0, 139.0, 139.0, 139.0, 143.5, 143.5, 143.5, 143.5, 143.5, 143.5, 143.5, 143.5, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 143.75, 138.75, 138.75, 138.75, 138.75, 138.75, 138.75, 138.75, 138.75, 136.75, 136.75, 136.75, 136.75, 136.75, 136.75, 136.75, 136.75, 136.25, 136.25, 136.25, 136.25, 136.25, 136.25, 136.25, 136.25, 137.0, 137.0, 137.0, 137.0, 137.0, 137.0, 137.0, 137.0, 132.75, 132.75, 132.75, 132.75, 132.75, 132.75, 132.75, 132.75, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 136.5, 136.5, 136.5, 136.5, 136.5, 136.5, 136.5, 136.5, 135.0, 135.0, 135.0, 135.0, 135.0, 135.0, 135.0, 135.0, 134.25, 134.25, 134.25, 134.25, 134.25, 134.25, 134.25, 134.25, 134.5, 134.5, 134.5, 134.5, 134.5, 134.5, 134.5, 134.5, 133.5, 133.5, 133.5, 133.5, 133.5, 133.5, 133.5, 133.5, 136.75, 136.75, 136.75, 136.75, 136.75, 136.75, 136.75, 136.75, 136.75, 136.75, 136.75, 136.75, 136.75, 136.75, 136.75, 136.75, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 137.0, 137.0, 137.0, 137.0, 137.0, 137.0, 137.0, 137.0, 139.75, 139.75, 139.75, 139.75, 139.75, 139.75, 139.75, 139.75, 137.25, 137.25, 137.25, 137.25, 137.25, 137.25, 137.25, 137.25, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 135.75, 135.75, 135.75, 135.75, 135.75, 135.75, 135.75, 135.75, 134.75, 134.75, 134.75, 134.75, 134.75, 134.75, 134.75, 134.75, 135.5, 135.5, 135.5, 135.5, 135.5, 135.5, 135.5, 135.5, 136.75, 136.75, 136.75, 136.75, 136.75, 136.75, 136.75, 136.75, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 138.0, 138.0, 138.0, 138.0, 138.0, 138.0, 138.0, 138.0, 139.5, 139.5, 139.5, 139.5, 139.5, 139.5, 139.5, 139.5, 138.0, 138.0, 138.0, 138.0, 138.0, 138.0, 138.0, 138.0, 135.5, 135.5, 135.5, 135.5, 135.5, 135.5, 135.5, 135.5, 136.75, 136.75, 136.75, 136.75, 136.75, 136.75, 136.75, 136.75, 137.25, 137.25, 137.25, 137.25, 137.25, 137.25, 137.25, 137.25, 136.5, 136.5, 136.5, 136.5, 136.5, 136.5, 136.5, 136.5, 135.5, 135.5, 135.5, 135.5, 135.5, 135.5, 135.5, 135.5, 129.5, 129.5, 129.5, 129.5, 129.5, 129.5, 129.5, 129.5, 126.5, 126.5, 126.5, 126.5, 126.5, 126.5, 126.5, 126.5, 130.75, 130.75, 130.75, 130.75, 130.75, 130.75, 130.75, 130.75, 129.5, 129.5, 129.5, 129.5, 129.5, 129.5, 129.5, 129.5, 128.0, 128.0, 128.0, 128.0, 128.0, 128.0, 128.0, 128.0, 130.75, 130.75, 130.75, 130.75, 130.75, 130.75, 130.75, 130.75, 130.0, 130.0, 130.0, 130.0, 130.0, 130.0, 130.0, 130.0, 132.0, 132.0, 132.0, 132.0, 132.0, 132.0, 132.0, 132.0, 135.25, 135.25, 135.25, 135.25, 135.25, 135.25, 135.25, 135.25, 133.5, 133.5, 133.5, 133.5, 133.5, 133.5, 133.5, 133.5, 132.0, 132.0, 132.0, 132.0, 132.0, 132.0, 132.0, 132.0, 129.75, 129.75, 129.75, 129.75, 129.75, 129.75, 129.75, 129.75, 134.5, 134.5, 134.5, 134.5, 134.5, 134.5, 134.5, 134.5, 136.25, 136.25, 136.25, 136.25, 136.25, 136.25, 136.25, 136.25, 132.0, 132.0, 132.0, 132.0, 132.0, 132.0, 132.0, 132.0, 130.5, 130.5, 130.5, 130.5, 130.5, 130.5, 130.5, 130.5, 132.5, 132.5, 132.5, 132.5, 132.5, 132.5, 132.5, 132.5, 132.25, 132.25, 132.25, 132.25, 132.25, 132.25, 132.25, 132.25, 132.25, 132.25, 132.25, 132.25, 132.25, 132.25, 132.25, 132.25, 133.75, 133.75, 133.75, 133.75, 133.75, 133.75, 133.75, 133.75, 135.25, 135.25, 135.25, 135.25, 135.25, 135.25, 135.25, 135.25, 135.25, 135.25, 135.25, 135.25, 135.25, 135.25, 135.25, 135.25, 135.5, 135.5, 135.5, 135.5, 135.5, 135.5, 135.5, 135.5, 136.5, 136.5, 136.5, 136.5, 136.5, 136.5, 136.5, 136.5, 138.75, 138.75, 138.75, 138.75, 138.75, 138.75, 138.75, 138.75, 139.5, 139.5, 139.5, 139.5, 139.5, 139.5, 139.5, 139.5, 138.25, 138.25, 138.25, 138.25, 138.25, 138.25, 138.25, 138.25, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 134.75, 134.75, 134.75, 134.75, 134.75, 134.75, 134.75, 134.75, 135.0, 135.0, 135.0, 135.0, 135.0, 135.0, 135.0, 135.0, 136.25, 136.25, 136.25, 136.25, 136.25, 136.25, 136.25, 136.25, 135.25, 135.25, 135.25, 135.25, 135.25, 135.25, 135.25, 135.25, 136.0, 136.0, 136.0, 136.0, 136.0, 136.0, 136.0, 136.0, 137.0, 137.0, 137.0, 137.0, 137.0, 137.0, 137.0, 137.0, 136.0, 136.0, 136.0, 136.0, 136.0, 136.0, 136.0, 136.0, 136.25, 136.25, 136.25, 136.25, 136.25, 136.25, 136.25, 136.25, 133.0, 133.0, 133.0, 133.0, 133.0, 133.0, 133.0, 133.0, 130.0, 130.0, 130.0, 130.0, 130.0, 130.0, 130.0, 130.0, 129.75, 129.75, 129.75, 129.75, 129.75, 129.75, 129.75, 129.75, 135.25, 135.25, 135.25, 135.25, 135.25, 135.25, 135.25, 135.25, 134.75, 134.75, 134.75, 134.75, 134.75, 134.75, 134.75, 134.75, 132.75, 132.75, 132.75, 132.75, 132.75, 132.75, 132.75, 132.75, 131.25, 131.25, 131.25, 131.25, 131.25, 131.25, 131.25, 131.25, 131.75, 131.75, 131.75, 131.75, 131.75, 131.75, 131.75, 131.75, 135.0, 135.0, 135.0, 135.0, 135.0, 135.0, 135.0, 135.0, 134.0, 134.0, 134.0, 134.0, 134.0, 134.0, 134.0, 134.0, 131.5, 131.5, 131.5, 131.5, 131.5, 131.5, 131.5, 131.5, 132.25, 132.25, 132.25, 132.25, 132.25, 132.25, 132.25, 132.25, 139.0, 139.0, 139.0, 139.0, 139.0, 139.0, 139.0, 139.0, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 142.75, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 146.0, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 144.75, 153.25, 153.25, 153.25, 153.25, 153.25, 153.25, 153.25, 153.25, 151.5, 151.5, 151.5, 151.5, 151.5, 151.5, 151.5, 151.5, 160.25, 160.25, 160.25, 160.25, 160.25, 160.25, 160.25, 160.25, 162.25, 162.25, 162.25, 162.25, 162.25, 162.25, 162.25, 162.25, 163.25, 163.25, 163.25, 163.25, 163.25, 163.25, 163.25, 163.25, 163.0, 163.0, 163.0, 163.0, 163.0, 163.0, 163.0, 163.0, 156.5, 156.5, 156.5, 156.5, 156.5, 156.5, 156.5, 156.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 150.5, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 141.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 140.25, 153.0, 153.0, 153.0, 153.0, 153.0, 153.0, 153.0, 153.0, 159.75, 159.75, 159.75, 159.75, 159.75, 159.75, 159.75, 159.75, 160.25, 160.25, 160.25, 160.25, 160.25, 160.25, 160.25, 160.25, 157.5, 157.5, 157.5, 157.5, 157.5, 157.5, 157.5, 157.5, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 147.75, 147.75, 147.75, 147.75, 147.75, 147.75, 147.75, 147.75, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 140.0, 135.0, 135.0, 135.0, 135.0, 135.0, 135.0, 135.0, 135.0, 130.5, 130.5, 130.5, 130.5, 130.5, 130.5, 130.5, 130.5, 124.0, 124.0, 124.0, 124.0, 124.0, 124.0, 124.0, 124.0, 118.75, 118.75, 118.75, 118.75, 118.75, 118.75, 118.75, 118.75, 115.5, 115.5, 115.5, 115.5, 115.5, 115.5, 115.5, 115.5, 117.0, 117.0, 117.0, 117.0, 117.0, 117.0, 117.0, 117.0, 120.75, 120.75, 120.75, 120.75, 120.75, 120.75, 120.75, 120.75, 125.25, 125.25, 125.25, 125.25, 125.25, 125.25, 125.25, 125.25, 126.75, 126.75, 126.75, 126.75, 126.75, 126.75, 126.75, 126.75, 126.0, 126.0, 126.0, 126.0, 126.0, 126.0, 126.0, 126.0, 128.25, 128.25, 128.25, 128.25, 128.25, 128.25, 128.25, 128.25, 130.25, 130.25, 130.25, 130.25, 130.25, 130.25, 130.25, 130.25, 134.25, 134.25, 134.25, 134.25, 134.25, 134.25, 134.25, 134.25, 135.0, 135.0, 135.0, 135.0, 135.0, 135.0, 135.0, 135.0, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 137.5, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 143.0, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 143.25, 143.25, 143.25, 143.25, 143.25, 143.25, 143.25, 143.25, 138.25, 138.25, 138.25, 138.25, 138.25, 138.25, 138.25, 138.25, 134.75, 134.75, 134.75, 134.75, 134.75, 134.75, 134.75, 134.75, 133.25, 133.25, 133.25, 133.25, 133.25, 133.25, 133.25, 133.25, 135.75, 135.75, 135.75, 135.75, 135.75, 135.75, 135.75, 135.75, 135.5, 135.5, 135.5, 135.5, 135.5, 135.5, 135.5, 135.5, 135.75, 135.75, 135.75, 135.75, 135.75, 135.75, 135.75, 135.75, 136.5, 136.5, 136.5, 136.5, 136.5, 136.5, 136.5, 136.5, 135.5, 135.5, 135.5, 135.5, 135.5, 135.5, 135.5, 135.5, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 137.75, 134.75, 134.75, 134.75, 134.75, 134.75, 134.75, 134.75, 134.75, 135.5, 135.5, 135.5, 135.5, 135.5, 135.5, 135.5, 135.5, 140.75, 140.75, 140.75, 140.75, 140.75, 140.75, 140.75, 140.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 141.75, 135.25, 135.25, 135.25, 135.25, 135.25, 135.25, 135.25, 135.25, 122.75, 122.75, 122.75, 122.75, 122.75, 122.75, 122.75, 122.75, 128.5, 128.5, 128.5, 128.5, 128.5, 128.5, 128.5, 128.5, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 145.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 146.25, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.0, 147.25, 147.25, 147.25, 147.25, 147.25, 147.25, 147.25, 147.25, 150.0, 150.0, 150.0, 150.0, 150.0, 150.0, 150.0, 150.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 152.0, 151.5, 151.5, 151.5, 151.5, 151.5, 151.5, 151.5, 151.5, 153.0, 153.0, 153.0, 153.0, 153.0, 153.0, 153.0, 153.0, 156.25, 156.25, 156.25, 156.25, 156.25, 156.25, 156.25, 156.25, 160.25, 160.25, 160.25, 160.25, 160.25, 160.25, 160.25, 160.25, 161.75, 161.75, 161.75, 161.75, 161.75, 161.75, 161.75, 161.75, 159.75, 159.75, 159.75, 159.75, 159.75, 159.75, 159.75, 159.75, 157.25, 157.25, 157.25, 157.25, 157.25, 157.25, 157.25, 157.25, 156.25, 156.25, 156.25, 156.25, 156.25, 156.25, 156.25, 156.25, 156.5, 156.5, 156.5, 156.5, 156.5, 156.5, 156.5, 156.5, 158.0, 158.0, 158.0, 158.0, 158.0, 158.0, 158.0, 158.0, 157.25, 157.25, 157.25, 157.25, 157.25, 157.25, 157.25, 157.25, 154.75, 154.75, 154.75, 154.75, 154.75, 154.75, 154.75, 154.75, 151.5, 151.5, 151.5, 151.5, 151.5, 151.5, 151.5, 151.5, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 149.25, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 149.0, 149.0, 149.0, 149.0, 149.0, 149.0, 149.0, 149.0, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 145.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 146.75, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 147.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 149.75, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 148.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 146.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5, 141.5]"
        def maternalHeartRate = "[74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 71.0, 71.0, 71.0, 71.0, 71.0, 71.0, 71.0, 71.0, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 70.25, 70.25, 70.25, 70.25, 70.25, 70.25, 70.25, 70.25, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 71.0, 71.0, 71.0, 71.0, 71.0, 71.0, 71.0, 71.0, 69.25, 69.25, 69.25, 69.25, 69.25, 69.25, 69.25, 69.25, 68.5, 68.5, 68.5, 68.5, 68.5, 68.5, 68.5, 68.5, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 81.5, 81.5, 81.5, 81.5, 81.5, 81.5, 81.5, 81.5, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 80.75, 80.75, 80.75, 80.75, 80.75, 80.75, 80.75, 80.75, 80.5, 80.5, 80.5, 80.5, 80.5, 80.5, 80.5, 80.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 81.25, 81.25, 81.25, 81.25, 81.25, 81.25, 81.25, 81.25, 82.5, 82.5, 82.5, 82.5, 82.5, 82.5, 82.5, 82.5, 84.75, 84.75, 84.75, 84.75, 84.75, 84.75, 84.75, 84.75, 82.75, 82.75, 82.75, 82.75, 82.75, 82.75, 82.75, 82.75, 80.5, 80.5, 80.5, 80.5, 80.5, 80.5, 80.5, 80.5, 83.0, 83.0, 83.0, 83.0, 83.0, 83.0, 83.0, 83.0, 79.0, 79.0, 79.0, 79.0, 79.0, 79.0, 79.0, 79.0, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 80.25, 80.25, 80.25, 80.25, 80.25, 80.25, 80.25, 80.25, 80.5, 80.5, 80.5, 80.5, 80.5, 80.5, 80.5, 80.5, 82.25, 82.25, 82.25, 82.25, 82.25, 82.25, 82.25, 82.25, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 80.75, 80.75, 80.75, 80.75, 80.75, 80.75, 80.75, 80.75, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 79.75, 79.75, 79.75, 79.75, 79.75, 79.75, 79.75, 79.75, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 71.0, 71.0, 71.0, 71.0, 71.0, 71.0, 71.0, 71.0, 70.25, 70.25, 70.25, 70.25, 70.25, 70.25, 70.25, 70.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 82.25, 82.25, 82.25, 82.25, 82.25, 82.25, 82.25, 82.25, 85.0, 85.0, 85.0, 85.0, 85.0, 85.0, 85.0, 85.0, 88.25, 88.25, 88.25, 88.25, 88.25, 88.25, 88.25, 88.25, 88.75, 88.75, 88.75, 88.75, 88.75, 88.75, 88.75, 88.75, 82.0, 82.0, 82.0, 82.0, 82.0, 82.0, 82.0, 82.0, 82.0, 82.0, 82.0, 82.0, 82.0, 82.0, 82.0, 82.0, 86.5, 86.5, 86.5, 86.5, 86.5, 86.5, 86.5, 86.5, 88.75, 88.75, 88.75, 88.75, 88.75, 88.75, 88.75, 88.75, 82.25, 82.25, 82.25, 82.25, 82.25, 82.25, 82.25, 82.25, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 83.75, 83.75, 83.75, 83.75, 83.75, 83.75, 83.75, 83.75, 85.25, 85.25, 85.25, 85.25, 85.25, 85.25, 85.25, 85.25, 89.0, 89.0, 89.0, 89.0, 89.0, 89.0, 89.0, 89.0, 81.75, 81.75, 81.75, 81.75, 81.75, 81.75, 81.75, 81.75, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 79.75, 79.75, 79.75, 79.75, 79.75, 79.75, 79.75, 79.75, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 80.75, 80.75, 80.75, 80.75, 80.75, 80.75, 80.75, 80.75, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 70.5, 70.5, 70.5, 70.5, 70.5, 70.5, 70.5, 70.5, 70.5, 70.5, 70.5, 70.5, 70.5, 70.5, 70.5, 70.5, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 79.0, 79.0, 79.0, 79.0, 79.0, 79.0, 79.0, 79.0, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 80.25, 80.25, 80.25, 80.25, 80.25, 80.25, 80.25, 80.25, 82.75, 82.75, 82.75, 82.75, 82.75, 82.75, 82.75, 82.75, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 71.0, 71.0, 71.0, 71.0, 71.0, 71.0, 71.0, 71.0, 70.25, 70.25, 70.25, 70.25, 70.25, 70.25, 70.25, 70.25, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 79.0, 79.0, 79.0, 79.0, 79.0, 79.0, 79.0, 79.0, 79.25, 79.25, 79.25, 79.25, 79.25, 79.25, 79.25, 79.25, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 79.0, 79.0, 79.0, 79.0, 79.0, 79.0, 79.0, 79.0, 80.75, 80.75, 80.75, 80.75, 80.75, 80.75, 80.75, 80.75, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 79.25, 79.25, 79.25, 79.25, 79.25, 79.25, 79.25, 79.25, 84.75, 84.75, 84.75, 84.75, 84.75, 84.75, 84.75, 84.75, 90.5, 90.5, 90.5, 90.5, 90.5, 90.5, 90.5, 90.5, 90.5, 90.5, 90.5, 90.5, 90.5, 90.5, 90.5, 90.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 84.25, 84.25, 84.25, 84.25, 84.25, 84.25, 84.25, 84.25, 89.0, 89.0, 89.0, 89.0, 89.0, 89.0, 89.0, 89.0, 83.25, 83.25, 83.25, 83.25, 83.25, 83.25, 83.25, 83.25, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 79.0, 79.0, 79.0, 79.0, 79.0, 79.0, 79.0, 79.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 88.25, 88.25, 88.25, 88.25, 88.25, 88.25, 88.25, 88.25, 90.25, 90.25, 90.25, 90.25, 90.25, 90.25, 90.25, 90.25, 86.5, 86.5, 86.5, 86.5, 86.5, 86.5, 86.5, 86.5, 85.75, 85.75, 85.75, 85.75, 85.75, 85.75, 85.75, 85.75, 91.25, 91.25, 91.25, 91.25, 91.25, 91.25, 91.25, 91.25, 84.25, 84.25, 84.25, 84.25, 84.25, 84.25, 84.25, 84.25, 83.75, 83.75, 83.75, 83.75, 83.75, 83.75, 83.75, 83.75, 80.75, 80.75, 80.75, 80.75, 80.75, 80.75, 80.75, 80.75, 80.5, 80.5, 80.5, 80.5, 80.5, 80.5, 80.5, 80.5, 82.25, 82.25, 82.25, 82.25, 82.25, 82.25, 82.25, 82.25, 86.25, 86.25, 86.25, 86.25, 86.25, 86.25, 86.25, 86.25, 83.75, 83.75, 83.75, 83.75, 83.75, 83.75, 83.75, 83.75, 82.25, 82.25, 82.25, 82.25, 82.25, 82.25, 82.25, 82.25, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 81.75, 81.75, 81.75, 81.75, 81.75, 81.75, 81.75, 81.75, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 81.5, 81.5, 81.5, 81.5, 81.5, 81.5, 81.5, 81.5, 80.5, 80.5, 80.5, 80.5, 80.5, 80.5, 80.5, 80.5, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 86.25, 86.25, 86.25, 86.25, 86.25, 86.25, 86.25, 86.25, 85.0, 85.0, 85.0, 85.0, 85.0, 85.0, 85.0, 85.0, 88.75, 88.75, 88.75, 88.75, 88.75, 88.75, 88.75, 88.75, 87.75, 87.75, 87.75, 87.75, 87.75, 87.75, 87.75, 87.75, 86.0, 86.0, 86.0, 86.0, 86.0, 86.0, 86.0, 86.0, 86.75, 86.75, 86.75, 86.75, 86.75, 86.75, 86.75, 86.75, 83.5, 83.5, 83.5, 83.5, 83.5, 83.5, 83.5, 83.5, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 81.5, 81.5, 81.5, 81.5, 81.5, 81.5, 81.5, 81.5, 81.25, 81.25, 81.25, 81.25, 81.25, 81.25, 81.25, 81.25, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 74.25, 74.25, 74.25, 74.25, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 80.5, 80.5, 80.5, 80.5, 80.5, 80.5, 80.5, 80.5, 83.5, 83.5, 83.5, 83.5, 83.5, 83.5, 83.5, 83.5, 84.5, 84.5, 84.5, 84.5, 84.5, 84.5, 84.5, 84.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 82.5, 82.5, 82.5, 82.5, 82.5, 82.5, 82.5, 82.5, 79.25, 79.25, 79.25, 79.25, 79.25, 79.25, 79.25, 79.25, 79.25, 79.25, 79.25, 79.25, 79.25, 79.25, 79.25, 79.25, 79.0, 79.0, 79.0, 79.0, 79.0, 79.0, 79.0, 79.0, 82.75, 82.75, 82.75, 82.75, 82.75, 82.75, 82.75, 82.75, 86.75, 86.75, 86.75, 86.75, 86.75, 86.75, 86.75, 86.75, 82.25, 82.25, 82.25, 82.25, 82.25, 82.25, 82.25, 82.25, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 70.25, 70.25, 70.25, 70.25, 70.25, 70.25, 70.25, 70.25, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 70.25, 70.25, 70.25, 70.25, 70.25, 70.25, 70.25, 70.25, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 83.75, 83.75, 83.75, 83.75, 83.75, 83.75, 83.75, 83.75, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 82.0, 82.0, 82.0, 82.0, 82.0, 82.0, 82.0, 82.0, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 83.0, 83.0, 83.0, 83.0, 83.0, 83.0, 83.0, 83.0, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 79.25, 79.25, 79.25, 79.25, 79.25, 79.25, 79.25, 79.25, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 71.0, 71.0, 71.0, 71.0, 71.0, 71.0, 71.0, 71.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 83.75, 83.75, 83.75, 83.75, 83.75, 83.75, 83.75, 83.75, 83.75, 83.75, 83.75, 83.75, 83.75, 83.75, 83.75, 83.75, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 82.25, 82.25, 82.25, 82.25, 82.25, 82.25, 82.25, 82.25, 81.25, 81.25, 81.25, 81.25, 81.25, 81.25, 81.25, 81.25, 81.5, 81.5, 81.5, 81.5, 81.5, 81.5, 81.5, 81.5, 85.25, 85.25, 85.25, 85.25, 85.25, 85.25, 85.25, 85.25, 84.25, 84.25, 84.25, 84.25, 84.25, 84.25, 84.25, 84.25, 83.5, 83.5, 83.5, 83.5, 83.5, 83.5, 83.5, 83.5, 81.5, 81.5, 81.5, 81.5, 81.5, 81.5, 81.5, 81.5, 79.25, 79.25, 79.25, 79.25, 79.25, 79.25, 79.25, 79.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 79.0, 79.0, 79.0, 79.0, 79.0, 79.0, 79.0, 79.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 87.0, 87.0, 87.0, 87.0, 87.0, 87.0, 87.0, 87.0, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 81.5, 81.5, 81.5, 81.5, 81.5, 81.5, 81.5, 81.5, 85.25, 85.25, 85.25, 85.25, 85.25, 85.25, 85.25, 85.25, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 79.75, 79.75, 79.75, 79.75, 79.75, 79.75, 79.75, 79.75, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 82.0, 82.0, 82.0, 82.0, 82.0, 82.0, 82.0, 82.0, 81.75, 81.75, 81.75, 81.75, 81.75, 81.75, 81.75, 81.75, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 81.75, 81.75, 81.75, 81.75, 81.75, 81.75, 81.75, 81.75, 82.25, 82.25, 82.25, 82.25, 82.25, 82.25, 82.25, 82.25, 81.25, 81.25, 81.25, 81.25, 81.25, 81.25, 81.25, 81.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 81.75, 81.75, 81.75, 81.75, 81.75, 81.75, 81.75, 81.75, 84.5, 84.5, 84.5, 84.5, 84.5, 84.5, 84.5, 84.5, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 79.75, 79.75, 79.75, 79.75, 79.75, 79.75, 79.75, 79.75, 86.75, 86.75, 86.75, 86.75, 86.75, 86.75, 86.75, 86.75, 82.75, 82.75, 82.75, 82.75, 82.75, 82.75, 82.75, 82.75, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 82.5, 82.5, 82.5, 82.5, 82.5, 82.5, 82.5, 82.5, 80.75, 80.75, 80.75, 80.75, 80.75, 80.75, 80.75, 80.75, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 84.25, 84.25, 84.25, 84.25, 84.25, 84.25, 84.25, 84.25, 84.25, 84.25, 84.25, 84.25, 84.25, 84.25, 84.25, 84.25, 79.0, 79.0, 79.0, 79.0, 79.0, 79.0, 79.0, 79.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 83.0, 83.0, 83.0, 83.0, 83.0, 83.0, 83.0, 83.0, 81.75, 81.75, 81.75, 81.75, 81.75, 81.75, 81.75, 81.75, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 82.25, 82.25, 82.25, 82.25, 82.25, 82.25, 82.25, 82.25, 82.75, 82.75, 82.75, 82.75, 82.75, 82.75, 82.75, 82.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 81.5, 81.5, 81.5, 81.5, 81.5, 81.5, 81.5, 81.5, 80.25, 80.25, 80.25, 80.25, 80.25, 80.25, 80.25, 80.25, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 83.25, 83.25, 83.25, 83.25, 83.25, 83.25, 83.25, 83.25, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 79.75, 79.75, 79.75, 79.75, 79.75, 79.75, 79.75, 79.75, 83.25, 83.25, 83.25, 83.25, 83.25, 83.25, 83.25, 83.25, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 68.75, 68.75, 68.75, 68.75, 68.75, 68.75, 68.75, 68.75, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 70.5, 70.5, 70.5, 70.5, 70.5, 70.5, 70.5, 70.5, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 81.5, 81.5, 81.5, 81.5, 81.5, 81.5, 81.5, 81.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 84.0, 84.0, 84.0, 84.0, 84.0, 84.0, 84.0, 84.0, 83.25, 83.25, 83.25, 83.25, 83.25, 83.25, 83.25, 83.25, 79.75, 79.75, 79.75, 79.75, 79.75, 79.75, 79.75, 79.75, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 80.5, 80.5, 80.5, 80.5, 80.5, 80.5, 80.5, 80.5, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 79.0, 79.0, 79.0, 79.0, 79.0, 79.0, 79.0, 79.0, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 71.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 79.75, 79.75, 79.75, 79.75, 79.75, 79.75, 79.75, 79.75, 80.25, 80.25, 80.25, 80.25, 80.25, 80.25, 80.25, 80.25, 81.25, 81.25, 81.25, 81.25, 81.25, 81.25, 81.25, 81.25, 81.75, 81.75, 81.75, 81.75, 81.75, 81.75, 81.75, 81.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 78.25, 79.75, 79.75, 79.75, 79.75, 79.75, 79.75, 79.75, 79.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 81.5, 81.5, 81.5, 81.5, 81.5, 81.5, 81.5, 81.5, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 80.0, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 78.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.5, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 85.5, 85.5, 85.5, 85.5, 85.5, 85.5, 85.5, 85.5, 85.25, 85.25, 85.25, 85.25, 85.25, 85.25, 85.25, 85.25, 87.75, 87.75, 87.75, 87.75, 87.75, 87.75, 87.75, 87.75, 84.75, 84.75, 84.75, 84.75, 84.75, 84.75, 84.75, 84.75, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.25, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 77.75, 79.25, 79.25, 79.25, 79.25, 79.25, 79.25, 79.25, 79.25, 79.75, 79.75, 79.75, 79.75, 79.75, 79.75, 79.75, 79.75, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 82.0, 82.0, 82.0, 82.0, 82.0, 82.0, 82.0, 82.0, 80.75, 80.75, 80.75, 80.75, 80.75, 80.75, 80.75, 80.75, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 77.5, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 74.0, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 79.5, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 71.0, 71.0, 71.0, 71.0, 71.0, 71.0, 71.0, 71.0, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 71.0, 71.0, 71.0, 71.0, 71.0, 71.0, 71.0, 71.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 70.0, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 76.25, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 70.5, 70.5, 70.5, 70.5, 70.5, 70.5, 70.5, 70.5, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 75.0, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 70.25, 70.25, 70.25, 70.25, 70.25, 70.25, 70.25, 70.25, 70.5, 70.5, 70.5, 70.5, 70.5, 70.5, 70.5, 70.5, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 80.5, 80.5, 80.5, 80.5, 80.5, 80.5, 80.5, 80.5, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 81.0, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 70.25, 70.25, 70.25, 70.25, 70.25, 70.25, 70.25, 70.25, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 77.0, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 76.75, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 74.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 73.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.75, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 75.5, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.25, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 72.5, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.0, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 75.25, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 74.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.5, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 74.25, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 78.5, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 72.75, 70.25, 70.25, 70.25, 70.25, 70.25, 70.25, 70.25, 70.25, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 73.0, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 70.75, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 72.0, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 71.25, 70.25, 70.25, 70.25, 70.25, 70.25, 70.25, 70.25, 70.25, 69.0, 69.0, 69.0, 69.0, 69.0, 69.0, 69.0, 69.0, 69.25, 69.25, 69.25, 69.25, 69.25, 69.25, 69.25, 69.25, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 71.5, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 73.25, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0, 76.0]"
        def qfhr = "[2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2]"
        def toco = "[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 27.0, 27.0, 27.0, 27.0, 27.0, 27.0, 27.0, 27.0, 26.0, 26.0, 26.0, 26.0, 26.0, 26.0, 26.0, 26.0, 24.5, 24.5, 24.5, 24.5, 24.5, 24.5, 24.5, 24.5, 24.0, 24.0, 24.0, 24.0, 24.0, 24.0, 24.0, 24.0, 22.5, 22.5, 22.5, 22.5, 22.5, 22.5, 22.5, 22.5, 21.5, 21.5, 21.5, 21.5, 21.5, 21.5, 21.5, 21.5, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 20.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 18.0, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.5, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 17.0, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.0, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5, 16.5]"

        def signals = "[2012-05-16T13:24:44Z, 2012-05-16T13:30:44Z, 2012-05-16T13:31:49Z, 2012-05-16T13:33:49Z]"

        ctgMeasurement.fhr = fetalHeartRate
        ctgMeasurement.mhr = maternalHeartRate
        ctgMeasurement.qfhr = qfhr
        ctgMeasurement.toco = toco
        ctgMeasurement.signals = signals
        ctgMeasurement.endTime = ISO8601DateParser.parse("2012-05-16T13:40:08Z")
        ctgMeasurement.startTime = ISO8601DateParser.parse("2012-05-16T13:10:23Z")
        ctgMeasurement.measurementType = MeasurementType.findByName(MeasurementTypeName.CTG)
        ctgMeasurement.meter = Meter.findByMeterId("123")
        ctgMeasurement.time = ISO8601DateParser.parse("2012-05-16T13:10:23Z")
        ctgMeasurement.patient = patient
        ctgMeasurement.unit = Unit.CTG

        ctgMeasurement.save(failOnError: true, flush: true)

        Date now = new Date()
        CompletedQuestionnaire completedQuestionnaire = new CompletedQuestionnaire(createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: now, modifiedDate: now, uploadDate: now, receivedDate: now)
        completedQuestionnaire.setPatientQuestionnaire(patientQuestionnaire)
        completedQuestionnaire.setQuestionnaireHeader(patientQuestionnaire.getTemplateQuestionnaire().getQuestionnaireHeader())
        completedQuestionnaire.setPatient(patient)
        completedQuestionnaire.setSeverity(Severity.ORANGE)

        completedQuestionnaire.save(failOnError: true)

        PatientQuestionnaire ctgPatientQuestionnaire = completedQuestionnaire.patientQuestionnaire
        ctgPatientQuestionnaire.refresh()

        // Find the node producing output...
        def ctgNode = ctgPatientQuestionnaire.nodes.find { it.instanceOf(PatientMeasurementNode) }
        def inputNode = ctgPatientQuestionnaire.nodes.find { it.instanceOf(PatientInputNode) }

        MeasurementType ctgMeasurementType = MeasurementType.findByName(MeasurementTypeName.CTG)

        // CTG result
        MeasurementNodeResult ctgResult = new MeasurementNodeResult(measurementType: ctgMeasurementType, createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: now, modifiedDate: now)
        ctgResult.setCompletedQuestionnaire(completedQuestionnaire)
        ctgResult.setCompletionTime(now)
        ctgResult.setSeverity(Severity.GREEN)

        ctgResult.addToMeasurements(ctgMeasurement)
        ctgResult.setPatientQuestionnaireNode(ctgNode)
        ctgResult.save(failOnError: true)

        if (inputNode != null) {

            // Duration of CTG measurement
            InputNodeResult durationResult = new InputNodeResult(result: '5', createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: now, modifiedDate: now)
            durationResult.setCompletedQuestionnaire(completedQuestionnaire)
            durationResult.setCompletionTime(now)
            durationResult.setSeverity(Severity.GREEN)

            durationResult.setPatientQuestionnaireNode(inputNode)
            durationResult.save(failOnError: true)
        }

    }

    def createQuestionnairesForTest(Clinician schemaCreator) {
        // Should ensure help image files exists - even when questionnaires already exists - in case e.g. help.image.uploadPath conf. changed/was deleted etc.
        HelpImageUtil.ensureHelpImageExists("janejtestbillede.jpg")
        HelpImageUtil.ensureHelpImageExists("BT4.jpg")
        HelpImageUtil.ensureHelpImageExists("Urin4 m rettelser.jpg")
        HelpImageUtil.ensureHelpImageExists("CTG2a med rettelser.jpg")

        TestQuestionnaireBuilder testQuestionnaireBuilder = new TestQuestionnaireBuilder(createdByName: CREATED_BY_NAME)

        testQuestionnaireBuilder.createRNKOLQuestionnaire("KOL Spørgeskema (skal ikke bruges) (manuel)", "1.0")
        testQuestionnaireBuilder.createJsonTestQuestionnaire(schemaCreator, "JSON test", "0.1")
        testQuestionnaireBuilder.createYesNoQuestionnaire(schemaCreator, "JaNej", "0.1")

        def q6 = testQuestionnaireBuilder.createUrineQuestionnaire(schemaCreator, 'Proteinindhold i urin', '0.1')
        def q7 = testQuestionnaireBuilder.createBPQuestionnaire(schemaCreator, false, 'Blodtryk', '0.1')
        createQuestionnaireGroup("Gruppe 1", [q6, q7])

        def q8 = testQuestionnaireBuilder.createBPQuestionnaire(schemaCreator, true, "Blodtryk (manuel)", "0.1")
        def q9 = testQuestionnaireBuilder.createLungFunctionQuestionnaire(schemaCreator, "Lungefunktion", "0.1")
        def q10 = testQuestionnaireBuilder.createCTGQuestionnaire(schemaCreator, "CTG", "0.1")
        createQuestionnaireGroup("Gruppe 2", [q8, q9, q10, q6], true)

        testQuestionnaireBuilder.createTimedCTGQuestionnaire(schemaCreator, "CTG m/tid", "0.1")
        testQuestionnaireBuilder.createTemperatureQuestionnaire(schemaCreator, "Temperatur test", "0.1")
        testQuestionnaireBuilder.createHemoQuestionnaire(schemaCreator, "Hæmoglobin indhold i blod", "0.1")
        testQuestionnaireBuilder.createCRPQuestionnaire(schemaCreator, "C-reaktivt Protein (CRP)", "0.1")
        testQuestionnaireBuilder.createWeightQuestionnaire(schemaCreator, false, "Vejning", "0.1")
        testQuestionnaireBuilder.createWeightQuestionnaire(schemaCreator, true, "Vejning (manuel)", "0.1")
        testQuestionnaireBuilder.createRadioButtonQuestionnaire(schemaCreator, "Radioknap test", "0.1")
        testQuestionnaireBuilder.createSaturationQuestionnaire(schemaCreator, false, "Saturation", "0.1", MeterTypeName.SATURATION)
        testQuestionnaireBuilder.createSaturationQuestionnaire(schemaCreator, true, "Saturation (manuel)", "0.1", MeterTypeName.SATURATION)
        testQuestionnaireBuilder.createSaturationQuestionnaire(schemaCreator, false, "Saturation u/puls", "0.1", MeterTypeName.SATURATION_W_OUT_PULSE)
        testQuestionnaireBuilder.createSaturationQuestionnaire(schemaCreator, true, "Saturation u/puls (manuel)", "0.1", MeterTypeName.SATURATION_W_OUT_PULSE)
        testQuestionnaireBuilder.createBloodSugarQuestionnaire(schemaCreator, true, "Blodsukker (manuel)", "0.1")
        testQuestionnaireBuilder.createBloodSugarQuestionnaire(schemaCreator, false, "Blodsukker", "0.1")

        bootstrapQuestionnaireService.ensureQuestionnaireExists(schemaCreator, 'Blodtryk m/alarm', 'Test_bt_m_alarm.json')
        bootstrapQuestionnaireService.ensureQuestionnaireExists(schemaCreator, 'Kontinuert glukosemåling (simuleret)', 'Test_kontinuert_glukosemaaling_simuleret.json')
        bootstrapQuestionnaireService.ensureQuestionnaireExists(schemaCreator, 'Kontinuert glukosemåling', 'Test_kontinuert_glukosemaaling.json')

        bootstrapQuestionnaireService.ensureQuestionnaireExists(schemaCreator, 'Testknuder med Hjælp', 'Test_knuder_m_hjaelp.json')

        // Production questionnaires
        createQuestionnairesForRMProd(schemaCreator)
        createQuestionnairesForRNProd(schemaCreator)
        createQuestionnairesForRHProd(schemaCreator)
    }

    def createQuestionnairesForEnglishTest(Clinician schemaCreator) {
        bootstrapQuestionnaireService.ensureQuestionnaireExists schemaCreator, 'Blood pressure and pulse', 'US_blood_pressure.json'
        bootstrapQuestionnaireService.ensureQuestionnaireExists schemaCreator, 'Blood sugar levels', 'US_blood_sugar_levels.json'
        bootstrapQuestionnaireService.ensureQuestionnaireExists schemaCreator, 'Lung function', 'US_Lung_function.json'
        bootstrapQuestionnaireService.ensureQuestionnaireExists schemaCreator, 'Oxygen saturation', 'US_saturation.json'
        bootstrapQuestionnaireService.ensureQuestionnaireExists schemaCreator, 'Weight', 'US_weight.json'
        bootstrapQuestionnaireService.ensureQuestionnaireExists schemaCreator, 'How are you feeling', 'US_general_welfare.json'
        bootstrapQuestionnaireService.ensureQuestionnaireExists schemaCreator, 'COPD questionnaire', 'US_copd_standard.json'
        bootstrapQuestionnaireService.ensureQuestionnaireExists schemaCreator, 'Daily CTG', 'US_daily_CTG.json'
    }

    def createQuestionnaireGroup(String name, ArrayList<Questionnaire> questionnaires, boolean overrideSchedule = false) {
        if (QuestionnaireGroup.findByName(name) == null) {
            def questionnaireGroup = new QuestionnaireGroup(name: name)

            questionnaires.each {
                questionnaireGroup.addToQuestionnaireGroup2Header(questionnaireHeader: it.questionnaireHeader)
            }
            if (overrideSchedule) {
                questionnaireGroup.questionnaireGroup2Header.find().standardSchedule = new StandardSchedule(type: Schedule.ScheduleType.WEEKDAYS, internalWeekdays: "MONDAY,TUESDAY,SUNDAY", internalTimesOfDay: "09:30,12:00,17:30")
            }
            questionnaireGroup.save(failOnError: true, flush: true)
        }
    }

    def createQuestionnairesForRMProd(Clinician creator) {
        // Should ensure help image files exists - even when questionnaires already exists - in case e.g. help.image.uploadPath conf. changed/was deleted etc.
        HelpImageUtil.ensureHelpImageExists("BT4.jpg")
        HelpImageUtil.ensureHelpImageExists("Urin4 m rettelser.jpg")
        HelpImageUtil.ensureHelpImageExists("CTG2a med rettelser.jpg")

        bootstrapQuestionnaireService.ensureQuestionnaireExists creator, 'Blodtryk og puls (manuel)', 'RM_blodtryk_puls_manuel.json'
        bootstrapQuestionnaireService.ensureQuestionnaireExists creator, 'Overførsel af blodsukkermålinger', 'RM_overfoersel_af_blodsukkermaalinger.json'
        bootstrapQuestionnaireService.ensureQuestionnaireExists creator, 'CTG måling', 'RM_ctg-maaling.json'
        bootstrapQuestionnaireService.ensureQuestionnaireExists creator, 'Vægt (manuel)', 'RM_vaegt_manuel.json'

        bootstrapQuestionnaireService.ensureQuestionnaireVersionExists(creator, 'Overførsel af blodsukkermålinger', '2.0', 'RM_overfoersel_af_blodsukkermaalinger_v2.json')
        bootstrapQuestionnaireService.ensureQuestionnaireVersionExists(creator, 'Vægt (manuel)', '2.0', 'RM_vaegt_manuel_v2.json')
        bootstrapQuestionnaireService.ensureQuestionnaireVersionExists(creator, 'CTG måling', '2.0', 'RM_ctg-maaling_m_hjaelp.json')


        RMQuestionnaireBuilder rmQuestionnaireBuilder = new RMQuestionnaireBuilder(createdByName: CREATED_BY_NAME)

        // Cannot be created in questionnaire editor due to ChoiceNodes
        rmQuestionnaireBuilder.createRMProdTemperatureQuestionnaire('Temperatur', '1.0', Severity.GREEN)
        rmQuestionnaireBuilder.createRMProdBloodpressureAndPulseQuestionnaire('Blodtryk og puls (PPROM, manuel)', '1.0', Severity.GREEN)
        rmQuestionnaireBuilder.createRMProdUrineQuestionnaire('Urinundersøgelse (protein)', '1.0', Severity.GREEN)
        rmQuestionnaireBuilder.createRMProdCRPQuestionnaire('CRP', '1.0', Severity.GREEN)
        rmQuestionnaireBuilder.createPPROMQuestionnaire('PPROM (primær spørgeskema, manuel)', '1.0', Severity.GREEN)
        rmQuestionnaireBuilder.createPraeeklampsiOrDiabetesQuestionnaire('Præeklampsi (primær spørgeskema, manuel)', '1.2', Severity.GREEN)
        rmQuestionnaireBuilder.createPraeeklampsiOrDiabetesQuestionnaire('Diabetes (primær spørgeskema, manuel)', '1.2', Severity.GREEN)

        rmQuestionnaireBuilder.createPraeeklampsiOrDiabetesQuestionnaire('Præeklampsi (primær spørgeskema, manuel)', '1.3', Severity.GREEN)
        rmQuestionnaireBuilder.createPraeeklampsiOrDiabetesQuestionnaire('Diabetes (primær spørgeskema, manuel)', '1.3', Severity.GREEN)

        // Cannot be created in questionnaire editor due to variables between nodes
        rmQuestionnaireBuilder.createTimedCTGQuestionnaire('CTG måling m/tid', false, '1.0', Severity.GREEN)
        rmQuestionnaireBuilder.createTimedCTGQuestionnaire('CTG måling m/tid (simuleret)', true, '1.0', Severity.GREEN)

        // Create new versions of the above, with yellow severity on omitted measurementnodes

        rmQuestionnaireBuilder.createRMProdBloodpressureAndPulseQuestionnaire('Blodtryk og puls (PPROM, manuel)', '2.0', Severity.YELLOW)
        rmQuestionnaireBuilder.createRMProdUrineQuestionnaire('Urinundersøgelse (protein)', '2.0', Severity.YELLOW)
        rmQuestionnaireBuilder.createRMProdCRPQuestionnaire('CRP', '2.0', Severity.YELLOW)
        rmQuestionnaireBuilder.createPPROMQuestionnaire('PPROM (primær spørgeskema, manuel)', '2.0', Severity.YELLOW)
        rmQuestionnaireBuilder.createPraeeklampsiOrDiabetesQuestionnaire('Præeklampsi (primær spørgeskema, manuel)', '2.0', Severity.YELLOW)
        rmQuestionnaireBuilder.createPraeeklampsiOrDiabetesQuestionnaire('Diabetes (primær spørgeskema, manuel)', '2.0', Severity.YELLOW)

        rmQuestionnaireBuilder.createPraeeklampsiOrDiabetesQuestionnaire('Præeklampsi (primær spørgeskema, manuel)', '2.0', Severity.YELLOW)
        rmQuestionnaireBuilder.createPraeeklampsiOrDiabetesQuestionnaire('Diabetes (primær spørgeskema, manuel)', '2.0', Severity.YELLOW)

        // Cannot be created in questionnaire editor due to variables between nodes
        rmQuestionnaireBuilder.createTimedCTGQuestionnaire('CTG måling m/tid', false, '2.0', Severity.YELLOW)
        rmQuestionnaireBuilder.createTimedCTGQuestionnaire('CTG måling m/tid (simuleret)', true, '2.0', Severity.YELLOW)
        rmQuestionnaireBuilder.createRMProdTemperatureQuestionnaire('Temperatur', '2.0', Severity.YELLOW)

        // Does not exist in a v.1.0 version
        rmQuestionnaireBuilder.createRMProdBloodpressureQuestionnaire('Blodtryk (manuel)', true, '2.0', Severity.YELLOW)

        // Create new versions with help added
        rmQuestionnaireBuilder.createRMProdBloodpressureQuestionnaire('Blodtryk (manuel)', true, '3.0', Severity.YELLOW)
        rmQuestionnaireBuilder.createRMProdBloodpressureAndPulseQuestionnaire('Blodtryk og puls (PPROM, manuel)', '3.0', Severity.YELLOW)
        rmQuestionnaireBuilder.createRMProdCRPQuestionnaire('CRP', '3.0', Severity.YELLOW)
        rmQuestionnaireBuilder.createRMProdUrineQuestionnaire('Urinundersøgelse (protein)', '3.0', Severity.YELLOW)
        rmQuestionnaireBuilder.createRMProdTemperatureQuestionnaire('Temperatur', '3.0', Severity.YELLOW)
        rmQuestionnaireBuilder.createPraeeklampsiOrDiabetesQuestionnaire('Præeklampsi (primær spørgeskema, manuel)', '3.0', Severity.YELLOW)
        rmQuestionnaireBuilder.createPPROMQuestionnaire('PPROM (primær spørgeskema, manuel)', '3.0', Severity.YELLOW)
        rmQuestionnaireBuilder.createPraeeklampsiOrDiabetesQuestionnaire('Diabetes (primær spørgeskema, manuel)', '3.0', Severity.YELLOW)
    }

    def createQuestionnairesForRNProd(Clinician creator) {
        bootstrapQuestionnaireService.ensureQuestionnaireExists creator, 'KOL målinger og spørgsmål', 'RN_kol_maalinger_og_spoergsmaal.json'
        bootstrapQuestionnaireService.ensureQuestionnaireExists creator, 'Rejse-sætte sig træningsøvelse', 'RN_rejse-saette_sig_traeningsoevelse.json'
        bootstrapQuestionnaireService.ensureQuestionnaireExists creator, 'Vægtmåling', 'RN_vaegtmaaling.json'
        bootstrapQuestionnaireService.ensureQuestionnaireExists creator, 'Vægtmåling (manuel)', 'RN_vaegtmaaling_manuel.json'

        RNQuestionnaireBuilder questionnaireBuilder = new RNQuestionnaireBuilder(createdByName: CREATED_BY_NAME)

        // Cannot be created in questionnaire editor because of the ChoiceValues
        questionnaireBuilder.createKOLStandUpQuestionnaire('Rejse-sætte-sig test', '8.0')
    }

    def createQuestionnairesForRHProd(Clinician creator) {
        bootstrapQuestionnaireService.ensureQuestionnaireExists creator, 'Blodtryk og puls', 'RH_blodtryk_puls.json'
        bootstrapQuestionnaireService.ensureQuestionnaireExists creator, 'Blodtryk og puls (manuel)', 'RH_blodtryk_puls_manuel.json'
        bootstrapQuestionnaireService.ensureQuestionnaireExists creator, 'KOL-målinger', 'RH_kol-maalinger.json'
        bootstrapQuestionnaireService.ensureQuestionnaireExists creator, 'KOL-spørgetræ (Manuel)', 'RH_kol-spoergetrae_manuel.json'
        bootstrapQuestionnaireService.ensureQuestionnaireExists creator, 'Urinundersøgelse (protein)', 'RH_urinundersoegelse_protein.json'
        bootstrapQuestionnaireService.ensureQuestionnaireExists creator, 'Urinundersøgelse (protein og glukose)', 'RH_urinundersoegelse_protein_og_glukose.json'
        bootstrapQuestionnaireService.ensureQuestionnaireExists creator, 'Vægt', 'RH_vaegt.json'
        bootstrapQuestionnaireService.ensureQuestionnaireExists creator, 'Konsultation hos jordemoder', 'RH_konsultation-hos-jordemoder.json'
    }


    def assignNancyLikeQuestionnairesToPatient(Clinician schemaCreator, Patient thePatient) {

        def weightMeter = Meter.findByMeterId("054534")

        def nancysPlan = MonitoringPlan.findByPatient(thePatient)

        createPqAndSchedule(QuestionnaireHeader.findByName("Blodtryk og puls"), thePatient, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))
        createPqAndSchedule(QuestionnaireHeader.findByName('Kontinuert glukosemåling'), thePatient, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))

        PatientQuestionnaire pq

        // JSON Test patientQuestionnaire + results
        pq = createPqAndSchedule(QuestionnaireHeader.findByName("JSON test"), thePatient, schemaCreator, nancysPlan, atWeekdays(new Schedule.TimeOfDay(hour: 10), [Weekday.SATURDAY, Weekday.SUNDAY]))
        createResultsForJsonTest(pq, weightMeter, thePatient)

        // BT skema med Severity.RED på undlad
        createPqAndSchedule(QuestionnaireHeader.findByName("Blodtryk m/alarm"), thePatient, schemaCreator, nancysPlan, unscheduled())

        //////////////
        // Simpelt Ja/Nej skema
        pq = createPqAndSchedule(QuestionnaireHeader.findByName("JaNej"), thePatient, schemaCreator, nancysPlan, unscheduled())

        if (pq && !CompletedQuestionnaire.findByPatientQuestionnaireAndSeverityAndPatient(pq, Severity.RED, thePatient)) {
            createResultsForOverYesNoQuestionnaire(thePatient, pq, Severity.RED, new Date() - 1, false)
        }
        if (pq && !CompletedQuestionnaire.findByPatientQuestionnaireAndSeverityAndPatient(pq, Severity.GREEN, thePatient)) {
            createResultsForOverYesNoQuestionnaire(thePatient, pq, Severity.GREEN, new Date(), true)
        }

        createPqAndSchedule(QuestionnaireHeader.findByName("Proteinindhold i urin"), thePatient, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))

        //////////////
        // Skema til måling af blodtryk
        pq = createPqAndSchedule(QuestionnaireHeader.findByName("Blodtryk"), thePatient, schemaCreator, nancysPlan, atWeekdays(new Schedule.TimeOfDay(hour: 13), [Weekday.MONDAY, Weekday.WEDNESDAY, Weekday.FRIDAY]))
        if (pq && !CompletedQuestionnaire.findByPatientQuestionnaireAndPatient(pq, thePatient)) {

            def bpMeter = Meter.findByMeterId("58354")

            def date = new Date().clearTime()
            date[Calendar.DATE] = date[Calendar.DATE] - 8

            (130..120).each {
                createResultsForBlodtryk(pq, thePatient, bpMeter, new Date(date.time), it, 80, 50 + new Random().nextInt(10), Severity.GREEN)
                date[Calendar.MONTH] = date[Calendar.MONTH] - 1
            }
        }

        // Skema til måling af CTG
        createPqAndSchedule(QuestionnaireHeader.findByName("CTG"), thePatient, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))

        pq = createPqAndSchedule(QuestionnaireHeader.findByName("CTG m/tid"), thePatient, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))
        if (pq && !CompletedQuestionnaire.findByPatientQuestionnaireAndPatient(pq, thePatient)) {
            createCtgMeasurementForTest(thePatient, pq)
        }

        // Skema til måling af temperatur, og intet andet (simpelt input-felt..)
        createPqAndSchedule(QuestionnaireHeader.findByName("Temperatur test"), thePatient, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))

        // Skema til måling af hæmoglobin, og intet andet
        createPqAndSchedule(QuestionnaireHeader.findByName("Hæmoglobin indhold i blod"), thePatient, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))

        // Skema til måling af CRP, og intet andet
        createPqAndSchedule(QuestionnaireHeader.findByName("C-reaktivt Protein (CRP)"), thePatient, schemaCreator, nancysPlan, unscheduled())

        // Skema til måling af Vaegt
        createPqAndSchedule(QuestionnaireHeader.findByName("Vejning"), thePatient, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 7)))

        // Simpelt skema til test af radioknapper
        createPqAndSchedule(QuestionnaireHeader.findByName("Radioknap test"), thePatient, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))

        // Skema til måling af blodsukker
        pq = createPqAndSchedule(QuestionnaireHeader.findByName("Blodsukker"), thePatient, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))
        createResultsForBloodSugarTest(thePatient, pq, null)

        // Manuel måling af blodsukker
        createPqAndSchedule(QuestionnaireHeader.findByName("Blodsukker (manuel)"), thePatient, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))

        createPqAndSchedule(QuestionnaireHeader.findByName("Rejse-sætte-sig test"), thePatient, schemaCreator, nancysPlan, unscheduled())

        // Kontinuert blodsukkermåling
        createPqAndSchedule(QuestionnaireHeader.findByName("Kontinuert glukosemåling (simuleret)"), thePatient, schemaCreator, nancysPlan, unscheduled())

        // Lungefunktion-skemaer
        createPqAndSchedule(QuestionnaireHeader.findByName("Lungefunktion"), thePatient, schemaCreator, nancysPlan, unscheduled())

        //
        // Not strictly required for test, but very nice to have in development
        //
        createPqAndSchedule(QuestionnaireHeader.findByName("Blodtryk (manuel)"), thePatient, schemaCreator, nancysPlan, unscheduled())
        createPqAndSchedule(QuestionnaireHeader.findByName("Vejning (manuel)"), thePatient, schemaCreator, nancysPlan, unscheduled())
        createPqAndSchedule(QuestionnaireHeader.findByName("Saturation"), thePatient, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))
        createPqAndSchedule(QuestionnaireHeader.findByName("Saturation (manuel)"), thePatient, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))

        createPqAndSchedule(QuestionnaireHeader.findByName("Saturation u/puls"), thePatient, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))
        createPqAndSchedule(QuestionnaireHeader.findByName("Saturation u/puls (manuel)"), thePatient, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))
    }


    def createPatientQuestionnairesForTest(Clinician schemaCreator, Patient nancyAnn, Patient linda, Patient lene) {
        def weightMeter = Meter.findByMeterId("054534")

        def nancysPlan = MonitoringPlan.findByPatient(nancyAnn)
        def lindasPlan = MonitoringPlan.findByPatient(linda)

        //Test questionnaires in connection with RM HelpInfo
        createPqAndSchedule(QuestionnaireHeader.findByName("Vægt"), nancyAnn, schemaCreator, nancysPlan, unscheduled())
        createPqAndSchedule(QuestionnaireHeader.findByName("CTG måling"), nancyAnn, schemaCreator, nancysPlan, unscheduled())
        createPqAndSchedule(QuestionnaireHeader.findByName("CRP"), nancyAnn, schemaCreator, nancysPlan, unscheduled())
        createPqAndSchedule(QuestionnaireHeader.findByName("PPROM (primær spørgeskema, manuel)"), nancyAnn, schemaCreator, nancysPlan, unscheduled())
        createPqAndSchedule(QuestionnaireHeader.findByName("Præeklampsi (primær spørgeskema, manuel)"), nancyAnn, schemaCreator, nancysPlan, unscheduled())
        createPqAndSchedule(QuestionnaireHeader.findByName("Blodtryk og puls (PPROM, manuel)"), nancyAnn, schemaCreator, nancysPlan, unscheduled())
        createPqAndSchedule(QuestionnaireHeader.findByName("Urinundersøgelse (protein)"), nancyAnn, schemaCreator, nancysPlan, unscheduled())
        createPqAndSchedule(QuestionnaireHeader.findByName("Temperatur"), nancyAnn, schemaCreator, nancysPlan, unscheduled())
        createPqAndSchedule(QuestionnaireHeader.findByName("Diabetes (primær spørgeskema, manuel)"), nancyAnn, schemaCreator, nancysPlan, unscheduled())
        createPqAndSchedule(QuestionnaireHeader.findByName("Testknuder med Hjælp"), nancyAnn, schemaCreator, nancysPlan, unscheduled())
        //END Test questionnaires in connection with RM HelpInfo

        createPqAndSchedule(QuestionnaireHeader.findByName("Blodtryk og puls"), nancyAnn, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))

        // Questionnaire with Blue alarm
        createPqAndSchedule(QuestionnaireHeader.findByName("Blodtryk og puls"), nancyAnn, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 13)), true)

        createPqAndSchedule(QuestionnaireHeader.findByName("Blodtryk og puls"), lene, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))

        // Questionnaire with Blue alarm
        createPqAndSchedule(QuestionnaireHeader.findByName("Blodtryk og puls"), lene, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 13)), true)

        PatientQuestionnaire pq

        // JSON Test patientQuestionnaire + results
        pq = createPqAndSchedule(QuestionnaireHeader.findByName("JSON test"), nancyAnn, schemaCreator, nancysPlan, atWeekdays(new Schedule.TimeOfDay(hour: 10), [Weekday.SATURDAY, Weekday.SUNDAY]))
        createResultsForJsonTest(pq, weightMeter, nancyAnn)

        // BT skema med Severity.RED på undlad
        createPqAndSchedule(QuestionnaireHeader.findByName("Blodtryk m/alarm"), nancyAnn, schemaCreator, nancysPlan, unscheduled())

        //////////////
        // Simpelt Ja/Nej skema
        pq = createPqAndSchedule(QuestionnaireHeader.findByName("JaNej"), nancyAnn, schemaCreator, nancysPlan, unscheduled())

        if (pq && !CompletedQuestionnaire.findByPatientQuestionnaireAndSeverityAndPatient(pq, Severity.RED, nancyAnn)) {
            createResultsForOverYesNoQuestionnaire(nancyAnn, pq, Severity.RED, new Date() - 1, false)
        }
        if (pq && !CompletedQuestionnaire.findByPatientQuestionnaireAndSeverityAndPatient(pq, Severity.GREEN, nancyAnn)) {
            createResultsForOverYesNoQuestionnaire(nancyAnn, pq, Severity.GREEN, new Date(), true)
        }

        createPqAndSchedule(QuestionnaireHeader.findByName("Proteinindhold i urin"), nancyAnn, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))

        //////////////
        // Skema til måling af blodtryk
        pq = createPqAndSchedule(QuestionnaireHeader.findByName("Blodtryk"), nancyAnn, schemaCreator, nancysPlan, atWeekdays(new Schedule.TimeOfDay(hour: 13), [Weekday.MONDAY, Weekday.WEDNESDAY, Weekday.FRIDAY]))
        if (pq && !CompletedQuestionnaire.findByPatientQuestionnaireAndPatient(pq, nancyAnn)) {

            def bpMeter = Meter.findByMeterId("58354")

            def date = new Date().clearTime()
            date[Calendar.DATE] = date[Calendar.DATE] - 8

            (130..120).each {
                createResultsForBlodtryk(pq, nancyAnn, bpMeter, new Date(date.time), it, 80, 50 + new Random().nextInt(10), Severity.GREEN)
                date[Calendar.MONTH] = date[Calendar.MONTH] - 1
            }
        }

        // Skema til måling af CTG
        createPqAndSchedule(QuestionnaireHeader.findByName("CTG"), nancyAnn, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))

        pq = createPqAndSchedule(QuestionnaireHeader.findByName("CTG m/tid"), nancyAnn, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))
        if (pq && !CompletedQuestionnaire.findByPatientQuestionnaireAndPatient(pq, nancyAnn)) {
            createCtgMeasurementForTest(nancyAnn, pq)
        }

        pq = createPqAndSchedule(QuestionnaireHeader.findByName("CTG m/tid"), linda, schemaCreator, lindasPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))
        if (pq && !CompletedQuestionnaire.findByPatientQuestionnaireAndPatient(pq, linda)) {
            createCtgMeasurementForTest(linda, pq)
        }

        // Skema til måling af temperatur, og intet andet (simpelt input-felt..)
        createPqAndSchedule(QuestionnaireHeader.findByName("Temperatur test"), nancyAnn, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))

        // Skema til måling af hæmoglobin, og intet andet
        createPqAndSchedule(QuestionnaireHeader.findByName("Hæmoglobin indhold i blod"), nancyAnn, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))
        createPqAndSchedule(QuestionnaireHeader.findByName("Hæmoglobin indhold i blod"), linda, schemaCreator, lindasPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))

        // Skema til måling af CRP, og intet andet
        createPqAndSchedule(QuestionnaireHeader.findByName("C-reaktivt Protein (CRP)"), nancyAnn, schemaCreator, nancysPlan, unscheduled())
        createPqAndSchedule(QuestionnaireHeader.findByName("C-reaktivt Protein (CRP)"), linda, schemaCreator, lindasPlan, unscheduled())

        // Skema til måling af Vaegt
        createPqAndSchedule(QuestionnaireHeader.findByName("Vejning"), nancyAnn, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 7)))

        // Simpelt skema til test af radioknapper
        createPqAndSchedule(QuestionnaireHeader.findByName("Radioknap test"), nancyAnn, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))

        // Skema til måling af blodsukker
        pq = createPqAndSchedule(QuestionnaireHeader.findByName("Blodsukker"), nancyAnn, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))
        createResultsForBloodSugarTest(nancyAnn, pq, null)

        // Manuel måling af blodsukker
        createPqAndSchedule(QuestionnaireHeader.findByName("Blodsukker (manuel)"), nancyAnn, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))
        createPqAndSchedule(QuestionnaireHeader.findByName("Blodsukker (manuel)"), linda, schemaCreator, lindasPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))

        createPqAndSchedule(QuestionnaireHeader.findByName("Rejse-sætte-sig test"), nancyAnn, schemaCreator, nancysPlan, unscheduled())
        createPqAndSchedule(QuestionnaireHeader.findByName("Rejse-sætte-sig test"), linda, schemaCreator, lindasPlan, unscheduled())

        // Kontinuert blodsukkermåling
        pq = createPqAndSchedule(QuestionnaireHeader.findByName("Kontinuert glukosemåling (simuleret)"), nancyAnn, schemaCreator, nancysPlan, unscheduled())
        if (pq && !CompletedQuestionnaire.findByPatientQuestionnaireAndPatient(pq, nancyAnn)) {
            createCgmMeasurementsForTest(nancyAnn, pq)
        }

        // Lungefunktion-skemaer
        createPqAndSchedule(QuestionnaireHeader.findByName("Lungefunktion"), nancyAnn, schemaCreator, nancysPlan, unscheduled())

        //
        // Not strictly required for test, but very nice to have in development
        //
        createPqAndSchedule(QuestionnaireHeader.findByName("Blodtryk (manuel)"), nancyAnn, schemaCreator, nancysPlan, unscheduled())
        createPqAndSchedule(QuestionnaireHeader.findByName("Vejning (manuel)"), nancyAnn, schemaCreator, nancysPlan, unscheduled())
        createPqAndSchedule(QuestionnaireHeader.findByName("Saturation"), nancyAnn, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))
        createPqAndSchedule(QuestionnaireHeader.findByName("Saturation (manuel)"), nancyAnn, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))

        createPqAndSchedule(QuestionnaireHeader.findByName("Saturation u/puls"), nancyAnn, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))
        createPqAndSchedule(QuestionnaireHeader.findByName("Saturation u/puls (manuel)"), nancyAnn, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))
    }

    def createPatientQuestionnairesForEnglishTest(Clinician schemaCreator, Patient nancyAnn, Patient linda, Patient lisa, Patient pErna, Patient pSvend, Patient pCarl, Patient pJohn) {
        def nancysPlan = MonitoringPlan.findByPatient(nancyAnn)

        PatientQuestionnaire pq

        //////////////
        // Nancy: Questionnaire for measuring bloodpressure
        pq = createPqAndSchedule(QuestionnaireHeader.findByName("Blood pressure and pulse"), nancyAnn, schemaCreator, nancysPlan, atWeekdays(new Schedule.TimeOfDay(hour: 13), [Weekday.MONDAY, Weekday.WEDNESDAY, Weekday.FRIDAY]))
        if (pq && !CompletedQuestionnaire.findByPatientQuestionnaireAndPatient(pq, nancyAnn)) {

            def bpMeter = Meter.findByMeterId("58354")

            def date = new Date().clearTime()
            date[Calendar.DATE] = date[Calendar.DATE] - 8

            (130..120).each {
                createResultsForBlodtryk(pq, nancyAnn, bpMeter, new Date(date.time), it + new Random().nextInt(10), 80 + new Random().nextInt(10), 50 + new Random().nextInt(10), Severity.GREEN)
                date[Calendar.MONTH] = date[Calendar.MONTH] - 1
            }
        }

        createPqAndSchedule(QuestionnaireHeader.findByName("Weight"), nancyAnn, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 7)))

        pq = createPqAndSchedule(QuestionnaireHeader.findByName("Blood sugar levels"), nancyAnn, schemaCreator, nancysPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))
        createResultsForBloodSugarTest(nancyAnn, pq, null, 1)

        createPqAndSchedule(QuestionnaireHeader.findByName("Lung function"), nancyAnn, schemaCreator, nancysPlan, unscheduled())

        def lisasPlan = MonitoringPlan.findByPatient(lisa)
        //////////////
        // Lisa: Questionnaire for measuring bloodpressure
        pq = createPqAndSchedule(QuestionnaireHeader.findByName("Blood pressure and pulse"), lisa, schemaCreator, lisasPlan, atWeekdays(new Schedule.TimeOfDay(hour: 13), [Weekday.MONDAY, Weekday.WEDNESDAY, Weekday.FRIDAY]))
        if (pq && !CompletedQuestionnaire.findByPatientQuestionnaireAndPatient(pq, lisa)) {

            def bpMeter = Meter.findByMeterId("012345")

            def date = new Date().clearTime()
            date[Calendar.DATE] = date[Calendar.DATE] - 9

            (130..120).each {
                createResultsForBlodtryk(pq, lisa, bpMeter, new Date(date.time), it + new Random().nextInt(10), 80 + new Random().nextInt(10), 50 + new Random().nextInt(10), Severity.GREEN)
                date[Calendar.MONTH] = date[Calendar.MONTH] - 1
            }

            createResultsForBlodtryk(pq, lisa, bpMeter, new Date(), 141, 89, 56, Severity.RED)
        }

        createPqAndSchedule(QuestionnaireHeader.findByName("Weight"), lisa, schemaCreator, lisasPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 7)))

        pq = createPqAndSchedule(QuestionnaireHeader.findByName("Blood sugar levels"), lisa, schemaCreator, lisasPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))
        createResultsForBloodSugarTest(lisa, pq, Meter.findByMeterId("012350"), 1)

        createPqAndSchedule(QuestionnaireHeader.findByName("Lung function"), lisa, schemaCreator, lisasPlan, unscheduled())
        createPqAndSchedule(QuestionnaireHeader.findByName("COPD questionnaire"), lisa, schemaCreator, lisasPlan, unscheduled())

        // Svend
        def svendPlan = MonitoringPlan.findByPatient(pSvend)
        pq = createPqAndSchedule(QuestionnaireHeader.findByName("Blood pressure and pulse"), pSvend, schemaCreator, svendPlan, atWeekdays(new Schedule.TimeOfDay(hour: 13), [Weekday.MONDAY, Weekday.WEDNESDAY, Weekday.FRIDAY]))
        if (pq && !CompletedQuestionnaire.findByPatientQuestionnaireAndPatient(pq, pSvend)) {

            createResultsForBlodtryk(pq, pSvend, null, new Date() - 1, 139, 88, 77, Severity.YELLOW)
        }
        createPqAndSchedule(QuestionnaireHeader.findByName("Weight"), pSvend, schemaCreator, svendPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 7)))

        // Linda
        def lindasPlan = MonitoringPlan.findByPatient(linda)

        pq = createPqAndSchedule(QuestionnaireHeader.findByName("Daily CTG"), linda, schemaCreator, lindasPlan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 12)))
        if (pq && !CompletedQuestionnaire.findByPatientQuestionnaireAndPatient(pq, linda)) {
            createCtgMeasurementForTest(linda, pq)
        }

        // Erna, Carl and John

        [pErna, pCarl, pJohn].each { patient ->
            def plan = MonitoringPlan.findByPatient(patient)
            if (!plan) {

                // Create monitoringplan for Erna
                plan = new MonitoringPlan(createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: new Date(), modifiedDate: new Date())
                plan.patient = patient
                plan.startDate = new Date() - 3
                plan.save(failOnError: true)

                patient.monitoringPlan = plan
            }

            def pq1 = createPqAndSchedule(QuestionnaireHeader.findByName("Oxygen saturation"), patient, schemaCreator, plan, specificDate(new Date(), new Schedule.TimeOfDay(hour: 1)))
            def pq2 = createPqAndSchedule(QuestionnaireHeader.findByName("Lung Function"), patient, schemaCreator, plan, everyWeekdayAt(new Schedule.TimeOfDay(hour: 8)))

            patient.blueAlarmQuestionnaireIDs.addAll([pq1, pq2]*.id)
        }
    }

    def createPatientNoteForEnglishTest(Patient patient, String text,
                                        def noteType = NoteType.NORMAL, def reminderDate = new Date() - 1) {

        if (PatientNote.findAllByPatient(patient).empty) {

            def patientNote = new PatientNote(patient: patient)

            patientNote.note = text
            patientNote.type = noteType
            patientNote.reminderDate = reminderDate
            patientNote.save(failOnError: true)
        }
    }

    def createConferencesForTest(Clinician schemaCreator, Patient... patients) {
        patients.each { patient ->

            if (Conference.findAllByClinicianAndPatient(schemaCreator, patient).empty) {
                def conference = new Conference(clinician: schemaCreator, patient: patient)
                def lungFunctionMeasurementDraft = new ConferenceLungFunctionMeasurementDraft(fev1: 3.67, included: true)
                def saturationMeasurementDraft = new ConferenceSaturationMeasurementDraft(saturation: 97, pulse: 67, included: false)
                def weightMeasurementDraft = new ConferenceWeightMeasurementDraft(weight: 87.3, included: true)
                def bloodPressureMeasurementDraft = new ConferenceBloodPressureMeasurementDraft(systolic: 130, diastolic: 65, pulse: 65, included: true)

                conference.addToMeasurementDrafts(lungFunctionMeasurementDraft)
                conference.addToMeasurementDrafts(saturationMeasurementDraft)
                conference.addToMeasurementDrafts(weightMeasurementDraft)
                conference.addToMeasurementDrafts(bloodPressureMeasurementDraft)

                [conference, lungFunctionMeasurementDraft, saturationMeasurementDraft, weightMeasurementDraft, bloodPressureMeasurementDraft]*.save(failOnError: true)

                //Create finished conference
                def finishedConference = new Conference(clinician: schemaCreator, patient: patient, completed: true)

                Meter meter = Meter.findByMeterId("58354")
                MeasurementType bloodPressureMeterType = MeasurementType.findByName(MeasurementTypeName.BLOOD_PRESSURE)
                Measurement bloodPressureMeasurement = new Measurement(meter: meter, measurementType: bloodPressureMeterType, patient: patient, systolic: 130, diastolic: 65, unit: Unit.MMHG, time: new Date(), unread: true, createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: new Date(), modifiedDate: new Date())
                finishedConference.addToMeasurements(bloodPressureMeasurement)

                [finishedConference, bloodPressureMeasurement]*.save(failOnError: true)
            }
        }
    }

    def createPqAndSchedule(QuestionnaireHeader questionnaireHeader, Patient patient, Clinician creator, MonitoringPlan mp, QuestionnaireSchedule schedule, boolean blueAlarm = false) {
        if (questionnaireHeader && questionnaireHeader.activeQuestionnaire) {

            PatientQuestionnaire pq = PatientQuestionnaire.find("from PatientQuestionnaire as pq where pq.templateQuestionnaire=:template",
                    [template: questionnaireHeader.activeQuestionnaire])

            if (!pq) {
                pq = questionnaireService.createPatientQuestionnaire(creator, questionnaireHeader.activeQuestionnaire);
            }

            // Er skemaet allerede i planen?
            QuestionnaireSchedule qsch = QuestionnaireSchedule.findByQuestionnaireHeaderAndMonitoringPlan(questionnaireHeader, mp)
            if (!qsch) {
                schedule.createdBy = CREATED_BY_NAME
                schedule.modifiedBy = CREATED_BY_NAME
                schedule.monitoringPlan = mp
                schedule.questionnaireHeader = questionnaireHeader

                schedule.save(failOnError: true)
            }

            if (blueAlarm) {
                patient.blueAlarmQuestionnaireIDs.add(pq.id)
                patient.save(failOnError: true)
            }
            pq
        } else {
            null
        }
    }

    def createHelleClones(int count) {
        for (int i = 0; i < count; i++) {
            def helleClone = [
                    firstName: "perf_${i}",
                    lastName : 'test',
                    email    : 'perf@test'
            ]
            createHelleLikeClinician(helleClone)
        }
    }

    def createCliniciansForTest() {

        Department deptY = Department.findByName(afdelingYTest)
        Department deptB = Department.findByName(afdelingBTest)
        Department deptDemo = Department.findByName(afdelingDemo)

        PatientGroup preEmp = PatientGroup.findByNameAndDepartment(praeklampsi, deptY)
        PatientGroup pprom = PatientGroup.findByNameAndDepartment(PPROM, deptY)
        PatientGroup heart = PatientGroup.findByNameAndDepartment(hjertepatient, deptB)
        PatientGroup lung = PatientGroup.findByNameAndDepartment(lungs, deptDemo)

        createHelleLikeClinician(clinicianHelleAndersen)

        // Jens Hansen
        Clinician clJens = createClinicianIfNotExists(firstName: clinicianJensHansen.firstName, lastName: clinicianJensHansen.lastName, email: clinicianJensHansen.email, mobile: '12345678')
        addClinician2PatientGroupIfNotExists(clJens, pprom)

        // Doktor Hansen
        createClinicianIfNotExists(firstName: clinicianDoktorHansen.firstName, lastName: clinicianDoktorHansen.lastName, email: clinicianDoktorHansen.email, mobile: '12345678')

        //
        Clinician c = createClinicianIfNotExists(firstName: clinicianAllAccess.firstName, lastName: clinicianAllAccess.lastName, email: clinicianAllAccess.email, mobile: '12345678')
        Role allAccessRole = Role.findByAuthority(RoleNameService.roleReadAllPatientsInSystem)
        bootStrapUtil.addUserToRoleIfNotExists(c.user, allAccessRole)
        Role adminRole = Role.findByAuthority(RoleNameService.roleAdministrator)
        bootStrapUtil.addUserToRoleIfNotExists(c.user, adminRole)
    }

    def createHelleLikeClinician(def cliniciandDetails) {
        PatientGroup preEmp = PatientGroup.findByName(praeklampsi)
        PatientGroup heart = PatientGroup.findByName(hjertepatient)
        PatientGroup lung = PatientGroup.findByName(lungs)

        Clinician clHelle = createClinicianIfNotExists(firstName: cliniciandDetails.firstName, lastName: cliniciandDetails.lastName, email: cliniciandDetails.email, mobile: '12345678', videoUser: 'HelleAndersen', videoPassword: 'HelleAndersen1')
        Role adminRole = Role.findByAuthority(RoleNameService.roleAdministrator)
        bootStrapUtil.addUserToRoleIfNotExists(clHelle.user, adminRole)
        Role videoRole = Role.findByAuthority(RoleNameService.roleVideoConsultant)
        bootStrapUtil.addUserToRoleIfNotExists(clHelle.user, videoRole)

        addClinician2PatientGroupIfNotExists(clHelle, preEmp)
        addClinician2PatientGroupIfNotExists(clHelle, heart)
        addClinician2PatientGroupIfNotExists(clHelle, lung)
    }

    def createClinicianIfNotExists(Map params) {

        Date now = new Date()

        // Assumes unique firstname/lastname for test-users
        Clinician c = Clinician.findByFirstNameAndLastName(params.firstName, params.lastName)
        if (!c) {
            c = new Clinician(params)

            def username
            if (params.username) {
                username = params.username
            } else {
                username = (params.firstName + params.lastName).replaceAll(" ", "")
            }

            def password
            if (params.password) {
                password = params.password
            } else {
                password = username + '1'
            }

            User user = new User(username: username, password: password,
                    accountExpired: false, accountLocked: false, enabled: true, passwordExpired: false,
                    createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: now, modifiedDate: now);
            user.save(failOnError: true);

            Role clinicianRole = Role.findByAuthority(RoleNameService.roleClinician)

            def userRole = new UserRole(user: user, role: clinicianRole, createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: now, modifiedDate: now)
            userRole.save(failOnError: true, flush: true)

            c.user = user
            c.createdBy = CREATED_BY_NAME
            c.modifiedBy = CREATED_BY_NAME
            c.createdDate = now
            c.modifiedDate = now

            c.save(failOnError: true)
        }
        c
    }

    Patient createPatientIfNotExists(Map params) {

        Date now = new Date()

        // Find by CPR
        Patient p = Patient.findByCpr(params.cpr)

        if (!p) {
            p = new Patient(params)

            def username
            if (params.username) {
                username = params.username
            } else {
                username = params.firstName.replaceAll(" ", "")
            }
            def password = params.cleartextPassword ?: "abcd1234"
            User user = new User(username: username, password: password, cleartextPassword: params.cleartextPassword,
                    accountExpired: false, accountLocked: false, enabled: true, passwordExpired: false,
                    createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: now, modifiedDate: now);

            user.save(failOnError: true);

            Role patientRole = Role.findByAuthority(RoleNameService.rolePatient)

            def userRole = new UserRole(user: user, role: patientRole, createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: now, modifiedDate: now)
            userRole.save(failOnError: true, flush: true)


            p.state = PatientState.ACTIVE
            p.user = user
            p.createdBy = CREATED_BY_NAME
            p.modifiedBy = CREATED_BY_NAME
            p.createdDate = now
            p.modifiedDate = now

            p.save(failOnError: true)
        }
        p
    }

    def addPatient2PatientGroupIfNotExists(Patient p, PatientGroup pg) {
        Patient2PatientGroup ref = Patient2PatientGroup.findByPatientAndPatientGroup(p, pg)
        if (!ref) {

            ref = new Patient2PatientGroup(createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: new Date(), modifiedDate: new Date())
            p.addToPatient2PatientGroups(ref)
            pg.addToPatient2PatientGroups(ref)
            ref.save(failOnError: true)
        }
        ref
    }

    def addStandardThresholds2Patient(Patient p) {
        p.refresh()
        //Add standard thresholds
        p.patient2PatientGroups.each { p2pg ->
            //If more than one PG for the patient, the thresholds will be overwritten, since we
            //currently do not support a set of thresholds per patientGroup per patient
            p2pg.patientGroup.standardThresholdSet.thresholds.each {

                Threshold t = it.duplicate()
                t.save(failOnError: true, flush: true)
                p.setThreshold(t)
            }
        }
        p.save(failOnError: true, flush: true)
    }

    def addClinician2PatientGroupIfNotExists(Clinician cl, PatientGroup pg) {
        Clinician2PatientGroup ref = Clinician2PatientGroup.findByClinicianAndPatientGroup(cl, pg)
        if (!ref) {
            ref = new Clinician2PatientGroup(createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: new Date(), modifiedDate: new Date())
            cl.addToClinician2PatientGroups(ref)
            pg.addToClinician2PatientGroups(ref)
            ref.save(failOnError: true)
        }
        ref
    }

    def createResultsForBloodSugarTest(Patient patient, PatientQuestionnaire pq, Meter meter, def yearsBack = 2) {

        if (pq && !CompletedQuestionnaire.findByPatientAndPatientQuestionnaire(patient, pq)) {
            def startDate = Calendar.getInstance()
            startDate.set(Calendar.HOUR_OF_DAY, 0)
            startDate.set(Calendar.MINUTE, 0)
            startDate.set(Calendar.SECOND, 0)
            startDate.set(Calendar.MILLISECOND, 0)
            startDate.add(Calendar.DATE, -8)

            def measurements = generateTestBloodSugarData(yearsBack)
            for (def measurement : measurements) {
                def day = measurement[0]
                def hour = measurement[1]
                def minute = measurement[2]

                Calendar calendar = (Calendar) startDate.clone()
                calendar.add(Calendar.DAY_OF_MONTH, -day)
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)

                def value = measurement[3]
                def isBeforeMeal = measurement[4]
                def isAfterMeal = measurement[5]
                def isControlMeasurement = measurement[6]
                def otherInformation = measurement[7]
                createBloodSugarMeasurement(patient, pq, calendar.getTime(), value, isBeforeMeal, isAfterMeal, isControlMeasurement, otherInformation, meter)
            }
        }
    }

    private def generateTestBloodSugarData(def yearsBack) {
        // Field order: day, hour, minute, value, isBeforeMeal, isAfterField

        // Add a few measurements at various times over the last few days.
        def bloodSugarTestData = [
                [1, 2, 0, 7.2, false, false, false, false],
                [2, 2, 0, 7.4, false, false, false, false],
                [2, 14, 12, 7.7, true, false, false, false],
                [2, 14, 18, 5.2, false, true, false, false],
                [2, 14, 19, 3.4, false, false, false, false],
                [2, 14, 21, 6.1, true, false, false, false],
                [2, 15, 43, 4.3, true, false, false, false],
                [2, 17, 0, 7.9, false, true, false, false]
        ]

        // Add 5 measurements per day for about one month.
        for (day in 5..35) {
            for (hour in [9, 11, 13, 17, 19]) {
                def value = 6.0 + (hour / 10.0)
                def isBeforeMeal = (hour == 11 || hour == 17)
                def isAfterMeal = (hour == 13 || hour == 19)
                def isControlMeasurement = (hour == 11)
                def otherInformation = (hour == 13)

                bloodSugarTestData << [day, hour, 0, value, isBeforeMeal, isAfterMeal, isControlMeasurement, otherInformation]
            }
        }

        // Add one measurement per day for about 2 years.
        for (month in 1..(yearsBack * 12 - 3)) {
            bloodSugarTestData << [month * 30, 15, 10, 8.0, false, false, false, false]
        }

        return bloodSugarTestData
    }

    private void createBloodSugarMeasurement(Patient patient, PatientQuestionnaire pq, Date date, float value, boolean isBeforeMeal, boolean isAfterMeal, boolean isControlMeasurement, boolean otherInformation, Meter meter) {

        CompletedQuestionnaire completedQuestionnaire = new CompletedQuestionnaire(
                questionnaireHeader: pq.getTemplateQuestionnaire().getQuestionnaireHeader(),
                patientQuestionnaire: pq,
                patient: patient,
                uploadDate: date,
                receivedDate: date,
                severity: Severity.GREEN,
                createdBy: CREATED_BY_NAME,
                modifiedBy: CREATED_BY_NAME,
                createdDate: date,
                modifiedDate: date)

        completedQuestionnaire.save(failOnError: true)

        PatientQuestionnaire myPq = completedQuestionnaire.patientQuestionnaire
        myPq.refresh()
        completedQuestionnaire.patientQuestionnaire.nodes*.refresh()

        def patientMeasurementNode = completedQuestionnaire.patientQuestionnaire.nodes.find {
            it.instanceOf(PatientMeasurementNode) && it.text in ["Blodsukker", "Blood sugar levels"]
        }

        MeasurementType measurementType = MeasurementType.findByName(MeasurementTypeName.BLOODSUGAR)

        MeasurementNodeResult measurementNodeResult = new MeasurementNodeResult(
                measurementType: measurementType,
                completedQuestionnaire: completedQuestionnaire,
                completionTime: date,
                wasOmitted: false,
                acknowledgedDate: null,
                acknowledgedBy: null,
                severity: null,
                createdBy: CREATED_BY_NAME,
                modifiedBy: CREATED_BY_NAME,
                createdDate: date,
                modifiedDate: date)

        Measurement measurement = new Measurement(
                meter: meter,
                measurementType: measurementType,
                patient: patient,
                value: value,
                unit: Unit.MMOL_L,
                time: date,
                isBeforeMeal: isBeforeMeal,
                isAfterMeal: isAfterMeal,
                isControlMeasurement: isControlMeasurement,
                otherInformation: otherInformation,
                unread: true,
                createdBy: CREATED_BY_NAME,
                modifiedBy: CREATED_BY_NAME,
                createdDate: date,
                modifiedDate: date)

        measurement.save(failOnError: true)

        measurementNodeResult.addToMeasurements(measurement)
        measurementNodeResult.setPatientQuestionnaireNode(patientMeasurementNode)
        measurementNodeResult.save(failOnError: true)
    }

    def createResultsForJsonTest(PatientQuestionnaire pq, Meter weightMeter, Patient patient) {

        if (pq && !CompletedQuestionnaire.findByPatientQuestionnaireAndPatient(pq, patient)) {

            def now = new Date()

            // Store results/answers to patientQuestionnaire
            CompletedQuestionnaire cq = new CompletedQuestionnaire(createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: now, modifiedDate: now)
            cq.setPatientQuestionnaire(pq)
            cq.setQuestionnaireHeader(pq.getTemplateQuestionnaire().getQuestionnaireHeader())
            cq.setPatient(patient)
            cq.setReceivedDate(new Date())
            cq.setUploadDate(new Date())
            cq.setSeverity(Severity.RED)

            cq.save(failOnError: true)

            PatientQuestionnaire myPq = cq.patientQuestionnaire
            myPq.refresh()
            cq.patientQuestionnaire.nodes*.refresh()

            // Find the nodes that can produce output...
            def wellNode = cq.patientQuestionnaire.nodes.find {
                it.instanceOf(PatientInputNode) && it.text == "Har du det godt?"
            }
            def reasonNode = cq.patientQuestionnaire.nodes.find {
                it.instanceOf(PatientInputNode) && it.text == "Indtast årsag"
            }
            def weightNode = cq.patientQuestionnaire.nodes.find { it.instanceOf(PatientMeasurementNode) }

            // Har du det godt?
            InputNodeResult iorWell = new InputNodeResult(createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: now, modifiedDate: now)
            iorWell.setCompletedQuestionnaire(cq)
            iorWell.setCompletionTime(new Date())
            iorWell.setWasOmitted(false)
            iorWell.setAcknowledgedDate(null)
            iorWell.setAcknowledgedBy(null)
            iorWell.setSeverity(null)
            iorWell.setResult("Nej")
            iorWell.setPatientQuestionnaireNode(wellNode)
            iorWell.save(failOnError: true)

            // Vaegt result
            MeasurementType weight = MeasurementType.findByName(MeasurementTypeName.WEIGHT)

            MeasurementNodeResult weightResult = new MeasurementNodeResult(measurementType: weight, createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: now, modifiedDate: now)
            weightResult.setCompletedQuestionnaire(cq)
            weightResult.setCompletionTime(new Date())
            weightResult.setWasOmitted(false)
            weightResult.setAcknowledgedDate(null)
            weightResult.setAcknowledgedBy(null)
            weightResult.setSeverity(null)

            Measurement wm = new Measurement(meter: weightMeter, measurementType: weight, patient: patient, value: 80, unit: Unit.KILO, time: new Date(), unread: true, createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: now, modifiedDate: now)
            wm.save(failOnError: true)

            weightResult.addToMeasurements(wm)
            weightResult.setPatientQuestionnaireNode(weightNode)
            weightResult.save(failOnError: true)

            // Vaegt aarsag?
            InputNodeResult iorCause = new InputNodeResult(createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: now, modifiedDate: now)
            iorCause.setCompletedQuestionnaire(cq)
            iorCause.setCompletionTime(new Date())
            iorCause.setWasOmitted(false)
            iorCause.setAcknowledgedDate(null)
            iorCause.setAcknowledgedBy(null)
            iorCause.setSeverity(Severity.RED)
            iorCause.setResult("Spiste for meget chokolade i går")
            iorCause.setPatientQuestionnaireNode(reasonNode)
            iorCause.save(failOnError: true)

            // Store another set of results/answers to the patientQuestionnaire
            CompletedQuestionnaire cq2 = new CompletedQuestionnaire(createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: now, modifiedDate: now)
            cq2.setPatientQuestionnaire(pq)
            cq2.setQuestionnaireHeader(pq.getTemplateQuestionnaire().getQuestionnaireHeader())
            cq2.setPatient(patient)
            cq2.setUploadDate(new Date() - 6)
            cq2.setReceivedDate(new Date() - 6)
            cq2.setSeverity(Severity.GREEN)

            cq2.save(failOnError: true)

            myPq.refresh()
            // Find the nodes that can produce output...
            wellNode = cq.patientQuestionnaire.nodes.find {
                it.instanceOf(PatientInputNode) && it.text == "Har du det godt?"
            }
            reasonNode = cq.patientQuestionnaire.nodes.find {
                it.instanceOf(PatientInputNode) && it.text == "Indtast årsag"
            }
            weightNode = cq.patientQuestionnaire.nodes.find { it.instanceOf(PatientMeasurementNode) }

            // Har du det godt?
            iorWell = new InputNodeResult(createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: now, modifiedDate: now)
            iorWell.setCompletedQuestionnaire(cq2)
            iorWell.setCompletionTime(new Date() - 6)
            iorWell.setWasOmitted(false)
            iorWell.setAcknowledgedDate(null)
            iorWell.setAcknowledgedBy(null)
            iorWell.setSeverity(null)
            iorWell.setResult("Nej")
            iorWell.setPatientQuestionnaireNode(wellNode)
            iorWell.save(failOnError: true)

            // Vaegt result
            weightResult = new MeasurementNodeResult(measurementType: weight, createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: now, modifiedDate: now)
            weightResult.setCompletedQuestionnaire(cq2)
            weightResult.setCompletionTime(new Date() - 6)
            weightResult.setWasOmitted(false)
            weightResult.setAcknowledgedDate(null)
            weightResult.setAcknowledgedBy(null)
            weightResult.setSeverity(null)

            wm = new Measurement(meter: weightMeter, measurementType: weight, patient: patient, value: 71, unit: Unit.KILO, time: new Date() - 6, unread: true, createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: now, modifiedDate: now)
            wm.save(failOnError: true)

            weightResult.addToMeasurements(wm)
            weightResult.setPatientQuestionnaireNode(weightNode)
            weightResult.save(failOnError: true)

            // Vaegt aarsag?
            iorCause = new InputNodeResult(createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: now, modifiedDate: now)
            iorCause.setCompletedQuestionnaire(cq2)
            iorCause.setCompletionTime(new Date() - 6)
            iorCause.setWasOmitted(false)
            iorCause.setAcknowledgedDate(null)
            iorCause.setAcknowledgedBy(null)
            iorCause.setSeverity(null)
            iorCause.setResult("Spiste for lidt chokolade i går")
            iorCause.setPatientQuestionnaireNode(reasonNode)
            iorCause.save(failOnError: true)
        }
    }

    def createResultsForOver18Questionnaire(Patient patient, PatientQuestionnaire pq18, int bp) {

        if (pq18 && !CompletedQuestionnaire.findByPatientQuestionnaireAndPatient(pq18, patient)) {

            def now = new Date()

            // Store results/answers to patientQuestionnaire
            CompletedQuestionnaire cq18 = new CompletedQuestionnaire(createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: now, modifiedDate: now)
            cq18.setPatientQuestionnaire(pq18)
            cq18.setQuestionnaireHeader(pq18.getTemplateQuestionnaire().getQuestionnaireHeader())
            cq18.setPatient(patient)
            cq18.setUploadDate(new Date())
            cq18.setReceivedDate(new Date())
            cq18.setSeverity(Severity.ABOVE_THRESHOLD)

            cq18.save(failOnError: true)

            PatientQuestionnaire myPq18 = cq18.patientQuestionnaire

            myPq18.refresh()
            myPq18.nodes*.refresh()

            // Find the nodes that can produce output...
            def overNode = myPq18.nodes.find {
                it.instanceOf(PatientInputNode) && it.text == "Er du over 18 år gammel?"
            }
            def bloodNode = myPq18.nodes.find { it.instanceOf(PatientInputNode) && it.text == "Indtast dit blodtryk" }

            // Har du det godt?
            InputNodeResult over18Rez = new InputNodeResult(createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: now, modifiedDate: now)
            over18Rez.setCompletedQuestionnaire(cq18)
            over18Rez.setCompletionTime(new Date())
            over18Rez.setWasOmitted(false)
            over18Rez.setAcknowledgedDate(null)
            over18Rez.setAcknowledgedBy(null)
            over18Rez.setSeverity(null)
            over18Rez.setResult(false)
            over18Rez.setPatientQuestionnaireNode(overNode)
            over18Rez.save(failOnError: true)

            InputNodeResult blodtrykRez = new InputNodeResult(createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: now, modifiedDate: now)
            blodtrykRez.setCompletedQuestionnaire(cq18)
            blodtrykRez.setCompletionTime(new Date())
            blodtrykRez.setWasOmitted(false)
            blodtrykRez.setAcknowledgedDate(null)
            blodtrykRez.setAcknowledgedBy(null)
            blodtrykRez.setSeverity(Severity.ABOVE_THRESHOLD)
            blodtrykRez.setResult(bp)
            blodtrykRez.setPatientQuestionnaireNode(bloodNode)
            blodtrykRez.save(failOnError: true)
        }
    }

    def createResultsForOverYesNoQuestionnaire(Patient patient, PatientQuestionnaire pq18, Severity severity, Date date, boolean result) {

        if (pq18 && !CompletedQuestionnaire.findWhere(patient: patient, patientQuestionnaire: pq18, severity: severity)) {

            def now = new Date()

            // Store results/answers to patientQuestionnaire
            CompletedQuestionnaire cq18 = new CompletedQuestionnaire(createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: now, modifiedDate: now)
            cq18.setPatientQuestionnaire(pq18)
            cq18.setQuestionnaireHeader(pq18.getTemplateQuestionnaire().getQuestionnaireHeader())
            cq18.setPatient(patient)
            cq18.setUploadDate(date)
            cq18.setReceivedDate(date)
            cq18.setSeverity(severity)

            cq18.save(failOnError: true)

            PatientQuestionnaire myPq18 = cq18.patientQuestionnaire
            myPq18.refresh()

            def inputNode = null
            myPq18.nodes.each() { q ->
                if (q.instanceOf(PatientInputNode)) {
                    inputNode = q
                }
            }

            InputNodeResult over18Rez = new InputNodeResult(createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: now, modifiedDate: now)
            over18Rez.setCompletedQuestionnaire(cq18)
            over18Rez.setCompletionTime(date)
            over18Rez.setWasOmitted(false)
            over18Rez.setAcknowledgedDate(null)
            over18Rez.setAcknowledgedBy(null)
            over18Rez.setSeverity(severity)
            over18Rez.setResult(result)
            over18Rez.setPatientQuestionnaireNode(inputNode)
            over18Rez.save(failOnError: true)
        }
    }

    def createResultsForBlodtryk(PatientQuestionnaire pqBlodtryk, Patient patient, Meter m2, Date when, int bp_sys, int bp_dia, int bp_pulse, Severity worstSeverity) {

        if (pqBlodtryk) {

            // Store results/answers to patientQuestionnaire
            CompletedQuestionnaire cqBlodtryk = new CompletedQuestionnaire(createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: when, modifiedDate: when)
            cqBlodtryk.setPatientQuestionnaire(pqBlodtryk)
            cqBlodtryk.setQuestionnaireHeader(pqBlodtryk.getTemplateQuestionnaire().getQuestionnaireHeader())
            cqBlodtryk.setPatient(patient)
            cqBlodtryk.setUploadDate(when)
            cqBlodtryk.setReceivedDate(when)
            cqBlodtryk.setSeverity(worstSeverity)

            cqBlodtryk.save(failOnError: true)


            PatientQuestionnaire myBlodtrykPq = cqBlodtryk.patientQuestionnaire

            myBlodtrykPq.refresh()
            myBlodtrykPq.nodes*.refresh()

            // Find the node that can produce output...
            def bpNode = myBlodtrykPq.nodes.find { it.instanceOf(PatientMeasurementNode) }

            MeasurementType bloodPressure = MeasurementType.findByName(MeasurementTypeName.BLOOD_PRESSURE)
            MeasurementType pulse = MeasurementType.findByName(MeasurementTypeName.PULSE)

            // BP result
            MeasurementNodeResult bpResult = new MeasurementNodeResult(measurementType: bloodPressure, createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: when, modifiedDate: when)
            bpResult.setCompletedQuestionnaire(cqBlodtryk)
            bpResult.setCompletionTime(when)
            bpResult.setWasOmitted(false)
            bpResult.setAcknowledgedDate(null)
            bpResult.setAcknowledgedBy(null)
            bpResult.setSeverity(worstSeverity)

            Measurement bpm = new Measurement(meter: m2, measurementType: bloodPressure, patient: patient, systolic: bp_sys, diastolic: bp_dia, unit: Unit.MMHG, time: when, unread: true, createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: when, modifiedDate: when)
            bpm.save(failOnError: true)

            bpResult.addToMeasurements(bpm)
            bpResult.setPatientQuestionnaireNode(bpNode)
            bpResult.save(failOnError: true)

            Measurement pm = new Measurement(meter: m2, measurementType: pulse, patient: patient, value: bp_pulse, unit: Unit.BPM, time: when, unread: true, createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: when, modifiedDate: when)
            pm.save(failOnError: true)

            bpResult.addToMeasurements(pm)
            bpResult.save(failOnError: true)
        }
    }

    def createMessagesForTest(Patient patient, Department department) {

        if (!Message.findByTitleAndPatient(testMessageHeader, patient)) {

            // Message
            Message message = new Message(title: testMessageHeader, createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: new Date(), modifiedDate: new Date())
            message.setPatient(patient)
            message.setDepartment(department)
            message.setSendDate(new SimpleDateFormat("yyyy-MM-dd hh:mm").parse("2012-01-15 17:04"))
            message.setReadDate(new SimpleDateFormat("yyyy-MM-dd hh:mm").parse("2012-01-16 10:54"))
            message.setIsRead(true)
            message.setSentByPatient(true)
            message.setText(testMessageText)

            message.save(failOnError: true)
        }
        if (!Message.findByTitleAndPatient(testMessageReplyHeader, patient)) {

            // Message
            Message message = new Message(title: testMessageReplyHeader, createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: new Date(), modifiedDate: new Date())

            message.setPatient(patient)
            message.setDepartment(department)
            message.setIsRead(true)
            message.setSendDate(new SimpleDateFormat("yyyy-MM-dd hh:mm").parse("2012-01-16 10:59"))
            message.setText(testMessageReplyText)

            message.save(failOnError: true)
        }
        if (!Message.findByTitleAndPatient(testMessagePatientReplyHeader, patient)) {

            // Message
            Message message = new Message(title: testMessagePatientReplyHeader, createdBy: CREATED_BY_NAME, modifiedBy: CREATED_BY_NAME, createdDate: new Date(), modifiedDate: new Date())

            message.setPatient(patient)
            message.setDepartment(department)
            message.setSentByPatient(true)
            message.setSendDate(new SimpleDateFormat("yyyy-MM-dd hh:mm").parse("2012-01-17 12:04"))
            message.setText(testMessagePatientReplyText)

            message.save(failOnError: true)
        }
    }

    @SuppressWarnings("GroovyUnusedDeclaration")
    def destroy = {
    }

    static QuestionnaireSchedule everyWeekdayAt(Schedule.TimeOfDay timeOfDay) {
        atWeekdays(timeOfDay, new ArrayList((Collection<Weekday>) Weekday.values()))
    }

    static QuestionnaireSchedule atWeekdays(Schedule.TimeOfDay timeOfDay, List<Weekday> weekdays) {
        def schedule = new QuestionnaireSchedule()
        schedule.type = Schedule.ScheduleType.WEEKDAYS
        schedule.weekdays = weekdays
        schedule.timesOfDay = [timeOfDay]

        schedule
    }

    static QuestionnaireSchedule specificDate(Date specificDate, Schedule.TimeOfDay timeOfDay) {
        def schedule = new QuestionnaireSchedule()
        schedule.type = Schedule.ScheduleType.SPECIFIC_DATE
        schedule.specificDate = specificDate
        schedule.timesOfDay = [timeOfDay]

        schedule
    }

    static QuestionnaireSchedule unscheduled() {
        def schedule = new QuestionnaireSchedule()
        schedule.type = Schedule.ScheduleType.UNSCHEDULED

        schedule
    }
}
