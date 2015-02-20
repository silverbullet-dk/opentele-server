package org.opentele.server.provider.model

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.opentele.server.core.test.AbstractControllerIntegrationTest
import org.opentele.server.model.Department
import org.opentele.server.model.MeasurementType
import org.opentele.server.model.Message
import org.opentele.server.model.NextOfKinPerson
import org.opentele.server.model.NumericThreshold
import org.opentele.server.model.Patient
import org.opentele.server.model.Patient2PatientGroup
import org.opentele.server.model.PatientGroup
import org.opentele.server.model.StandardThresholdSet
import org.opentele.server.model.Threshold
import org.opentele.server.core.model.types.MeasurementTypeName
import org.opentele.server.core.model.types.PatientState
import org.opentele.server.core.model.types.Sex

@TestMixin(IntegrationTestMixin)
class PatientControllerIntegrationTests extends AbstractControllerIntegrationTest  {
    //Injections
    def grailsApplication
    def passwordService
    //Globals
    def patientController


    /**
     * Evidently in integration tests, save(flush: true) can fail
     * without error. Source:
     * http://jan-so.blogspot.dk/2008/12/grails-integration-testing-some-tips.html
     *
     * Thus here are two helper methods for saving objects that
     * should make sure that no .save(..) fails silently.
     *
     * @param object that needs to be saved
     * @return instance of the saved object
     */
    private Object save(Object object) {
        validateAndPrintErrors(object)
        Object result = object.save(flush:true)
        assert result != null
        return result
    }

    private void validateAndPrintErrors(Object object) {
        if (!object.validate()) {
            object.errors.allErrors.each {error ->
                println error
            }
            fail("failed to save object ${object}")
        }
    }

    private void populatePatientParams(map) {
        //Build a test patient dataset
        def cpr = '1234567890'
        def firstName = 'Mette'
        def lastName ='Andersen'
        def sex =  Sex.FEMALE
        def address = 'Gade 15'
        def postalCode = '8000'
        def city = 'Aarhus C'
        def mobilePhone =  null
        def phone =  null
        def email =  null
        def thresholds = new ArrayList<Threshold>()
        def state = PatientState.ACTIVE

        def password = "MetteAndersen1"
        def reenteredpassword = "MetteAndersen1"
        def username = "MetteAndersen"

        map.cpr = cpr
        map.firstName = firstName
        map.lastName = lastName
        map.sex = sex
        map.address = address
        map.postalCode = postalCode
        map.city = city
        map.mobilePhone = mobilePhone
        map.phone = phone
        map.email = email
        map.thresholds = thresholds
        map.state = state
        map.username = username
        map.password = password
        map.reenteredpassword = reenteredpassword
    }

    @Before
    void setUp() {
        // Avoid conflicts with objects in session created earlier. E.g. in bootstrap
        grailsApplication.mainContext.sessionFactory.currentSession.clear()

        patientController = new PatientController()
        patientController.passwordService = passwordService
        authenticate 'HelleAndersen','HelleAndersen1'
    }

    @Test
    void testUpdateWorksWithGroups() {
        Patient p = Patient.findByFirstName("Nancy Ann") //Assume nancy
        assert p != null
        patientController.response.reset()
        Department deptB = Department.findByName("Afdeling-B Test")
        PatientGroup pg = PatientGroup.findByName("Hjertepatient", deptB)


        //Pre-check
        assert p.groups.size() == 1 //Nancy has 2 pg from bootstrap

        //Execute
        patientController.params.groupid = pg.id
        patientController.params.id = p.id
        patientController.update()

        //Check
        p = Patient.findById(p.id)
        assert p.groups.size() == 1
    }

    @Test
    void testCanRemovePatientGroups() {
        //Patient p = Patient.findByFirstName("Nancy Ann") //Assume nancy

        Patient p = new Patient(firstName:'Test',
                lastName:'P.G. Remove',
                cpr:'1231234234',
                sex: Sex.FEMALE,
                address:'Vej1',
                postalCode:'8000',
                city:'Aarhus C',
                state: PatientState.ACTIVE)

        p.save(failOnError: true)
        assert p != null

        // Add a single patientgroup
        Department deptB = Department.findByName("Afdeling-B Test")

        StandardThresholdSet st = new StandardThresholdSet()
        st.save(failOnError:true)

        PatientGroup pg = new PatientGroup(name: "RemoveGroup", department: deptB, standardThresholdSet: st)
        pg.save(failOnError:true)
        Patient2PatientGroup.link(p, pg)

        p.refresh()
        assert p.groups.size() == 1

        patientController.response.reset()
        patientController.request.method = 'POST'

        //Execute
        patientController.params.groupid = []
        patientController.params.id = p.id
        patientController.update()

        //Check
        p = Patient.findById(p.id)
        assert p.groups.size() == 0
    }

    @Test
    void testUpdateWorksWithNextOfKin() {
        Patient p = Patient.findByCpr("2512484916") //Assume nancy
        assert p != null
        patientController.response.reset()
        patientController.request.method = 'POST'
        NextOfKinPerson nok = new NextOfKinPerson(firstName: "Foobar")
        save(nok)

        //Pre-check
        p.refresh()
        assert p.getNextOfKinPersons().size() == 0

        //Execute
        patientController.params.nextOfKinPersonId = nok.id
        patientController.params.id = p.id

        //This test mess with the obj graph on a way that makes hibernate fail
        //on some of its lazy loading; questionnaireSchedules are not flushed
        //on save(..). However if we force it to lazy load them, they are.....
        p.monitoringPlan.questionnaireSchedules

        patientController.update()

        //Check
        p = Patient.findById(p.id)
        assert p.getNextOfKinPersons().size() == 1
    }

    @Test
    void testPatientGetNumberOfUnreadMessagesWorks() {
        //Setup
        Patient p = Patient.findById(1)
        Department d = Department.findAll()[0]

        //Check one
        assert p.numberOfUnreadMessages == [0, null, 0, null]

        //Update
        Message m = new Message(department: d, patient: p, title: "foobar", text: "barbaz")
        save(m)

        //Check two
        assert p.numberOfUnreadMessages[0] == 1

        //Update two
        (1..10).each {
            Message m2 = new Message(department: d, patient: p, title: "foobar", text: "barbaz")
            save(m2)
        }

        //Check three
        assert p.numberOfUnreadMessages[0] == 11

        //Update
        Message.findAll().each {
            it.delete()
        }

        //Check four
        assert p.numberOfUnreadMessages == [0, null, 0, null]
    }

    @Test
    void testPatientCanUpdateThreshold() {
        //Setup
        Patient p = Patient.findById(1)

        //Pre-check
        assert p.thresholds != null
        assert p.thresholds.size() > 1
        assert p.thresholds.count { it.type.name == MeasurementTypeName.TEMPERATURE} == 1
        def thresholdAlertHigh = p.getThreshold(MeasurementTypeName.TEMPERATURE).alertHigh

        //Execute
        NumericThreshold newt = new NumericThreshold(type: MeasurementType.findByName(MeasurementTypeName.TEMPERATURE), alertHigh: 9000.0f, alertLow: 0.0f, warningLow: 10.0f, warningHigh: 20.0f)
        save(newt)
        p.setThreshold(newt)
        save(p)
        //Check
        p = Patient.findById(1)
        assert p.getThreshold(MeasurementTypeName.TEMPERATURE).alertHigh != thresholdAlertHigh
        assert p.getThreshold(MeasurementTypeName.TEMPERATURE).alertHigh == 9000
    }

    @Test
    void testPatientCannotHaveTwoThresholdsOfTheSameType() {
        //Setup
        Patient p = Patient.findById(1)

        //Pre-check
        assert p.thresholds != null
        assert p.thresholds.size() > 1
        assert p.thresholds.count { it.type.name == MeasurementTypeName.TEMPERATURE} == 1

        //Execute
        NumericThreshold newt = new NumericThreshold(type: MeasurementType.findByName(MeasurementTypeName.TEMPERATURE), alertHigh: 9000.0f, alertLow: 0.0f, warningLow: 10.0f, warningHigh: 20.0f)
        save(newt)
        p.thresholds.add(newt)

        //Check
        assert !p.validate()
        assert p.errors.getFieldError("thresholds") != null && p.errors.getFieldError("thresholds") != ''
    }

    @Test
    void testCanSetResponsibleGroup() {
        Patient p = Patient.findById(10) //Assume nancy
        assert p != null
        patientController.response.reset()
        Department deptB = Department.findByName("Afdeling-B Test")
        PatientGroup pg = PatientGroup.findByName("Hjertepatient", deptB)

        //Pre-check
        assert p.dataResponsible == null

        //Execute
        patientController.params.dataResponsible = pg.id
        patientController.params.id = p.id
        patientController.updateDataResponsible()

        //Check
        p = Patient.findById(p.id)
        assert p.dataResponsible != null
        assert p.dataResponsible.equals(pg)
    }

}
