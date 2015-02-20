package org.opentele.server.provider.model

import grails.test.mixin.TestMixin
import grails.test.mixin.integration.IntegrationTestMixin
import org.junit.Ignore
import org.opentele.server.core.RoleNameService
import org.opentele.server.core.test.AbstractControllerIntegrationTest
import org.opentele.server.model.Clinician
import org.opentele.server.model.Role
import org.opentele.server.model.UserRole

@TestMixin(IntegrationTestMixin)
class ClinicianControllerIntegrationTests extends AbstractControllerIntegrationTest {
    //Injections
    def grailsApplication

    //Globals
    def clinicianController

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
        Object result = object.save(flush: true)
        assertNotNull("Object not created: " + object, result)
        return result
    }

    private void validateAndPrintErrors(Object object) {
        if (!object.validate()) {
            object.errors.allErrors.each { error ->
                println error
            }
            fail("failed to save object ${object}")
        }
    }

    void setUp() {
        grailsApplication.mainContext.sessionFactory.currentSession.clear()

        clinicianController = new ClinicianController()
        authenticate 'HelleAndersen', 'HelleAndersen1'
    }

    void testClinicianUpdateRoleNullRoleListIsIgnored() {
        def clinician = Clinician.findAll().get(0)
        def user = clinician.user //get id of the first clinician/admin.. just a non-patient
        def clinicianRolesPre = UserRole.findAllByUser(user)

        assert clinicianRolesPre != null
        assert clinicianRolesPre instanceof List
        assert clinicianRolesPre.size() > 0

        clinicianController.params.id = clinician.id
        clinicianController.params.roleIds = null
        clinicianController.params.version = clinician.version
        clinicianController.params.firstName = clinician.firstName
        clinicianController.params.lastName = clinician.lastName

        clinicianController.update()

        def clinicianRolesPost = UserRole.findAllByUser(user)

        assert clinicianRolesPre.equals(clinicianRolesPost)
    }

    @Ignore
    void testClinicianUpdateRoleEmptyListEqualsNoRoles() {
        def clinician = Clinician.findAll().get(0)
        def user = clinician.user //get id of the first clinician/admin.. just a non-patient
        def clinicianRolesPre = UserRole.findAllByUser(user)

        assert clinicianRolesPre != null
        assert clinicianRolesPre instanceof List
        assert clinicianRolesPre.size() > 0

        clinicianController.params.id = clinician.id
        clinicianController.params.version = clinician.version
        clinicianController.params.roleIds = []
        clinicianController.update()

        def clinicianRolesPost = UserRole.findAllByUser(user)

        assert clinicianRolesPost != null
        assert clinicianRolesPost instanceof List
        assert clinicianRolesPost.size() == 0
    }

    @Ignore
    void testClinicianUpdateRoleUpdateRoles() {
        def clinician = Clinician.findByFirstNameAndLastName("Helle", "Andersen")
        def user = clinician.user //get id of the first clinician/admin.. just a non-patient

        grailsApplication.mainContext.sessionFactory.currentSession.clear()

        def roleAdmin = Role.findByAuthority(RoleNameService.roleAdministrator)
        def roleClinician =  Role.findByAuthority(RoleNameService.roleClinician)

        clinicianController.params.id = clinician.id
        clinicianController.params.version = clinician.version
        clinicianController.params.roleIds = [roleAdmin.id.toString(), roleClinician.id.toString()]

        clinicianController.update()

        def clinicianRolesPost = UserRole.findAllByUser(user)

        assert clinicianRolesPost != null
        assert clinicianRolesPost instanceof List
        assert clinicianRolesPost.size() == 1

        def roleIds = clinicianRolesPost.collect { it.role.id }
        assert roleIds.contains(roleAdmin.id)
        assert roleIds.contains(roleClinician.id)
    }
}
