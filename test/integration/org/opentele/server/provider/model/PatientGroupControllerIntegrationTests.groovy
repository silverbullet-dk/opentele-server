package org.opentele.server.provider.model

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin
import org.junit.After
import org.junit.Before
import org.opentele.server.core.test.AbstractControllerIntegrationTest
import org.opentele.server.model.Department
import org.opentele.server.model.Patient
import org.opentele.server.model.Patient2PatientGroup
import org.opentele.server.model.PatientGroup
import org.opentele.server.model.StandardThresholdSet
import org.opentele.server.core.model.types.PatientState
import org.opentele.server.core.model.types.Sex

@TestMixin(IntegrationTestMixin)
class PatientGroupControllerIntegrationTests extends AbstractControllerIntegrationTest  {
    //Injections
    def grailsApplication
    def patientGroupService

    //Globals
    def patientGroupController

    @Before
    void setUp() {
        // Avoid conflicts with objects in session created earlier. E.g. in bootstrap
        grailsApplication.mainContext.sessionFactory.currentSession.clear()

        patientGroupController = new PatientGroupController()
        patientGroupController.patientGroupService = patientGroupService
        authenticate 'HelleAndersen','HelleAndersen1'
    }

    void testDelete() {

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

        patientGroupController.response.reset()
        patientGroupController.request.method = 'POST'

        //Execute
        patientGroupController.params.id = pg.id
        patientGroupController.delete()

        //Check
        pg = PatientGroup.findById(pg.id)
        assert pg == null

        assert patientGroupController.response.redirectedUrl == '/patientGroup/list'
    }

}
