package org.opentele.server.provider

import org.opentele.server.core.test.AbstractIntegrationSpec
import org.opentele.server.core.command.ClinicianCommand
import org.opentele.server.model.*
import spock.lang.Ignore
import spock.lang.Unroll

class ClinicianServiceSpec extends AbstractIntegrationSpec {

    ClinicianService clinicianService

    def "test createClinician with valid command"() {
        setup:
        def command = buildClinicianCommand(roleIds: [Role.list().first().id], groupIds: PatientGroup.list()[0..1]*.id)
        when:
        def clinician = clinicianService.createClinician(command)

        then:
        !clinician.hasErrors()
        UserRole.countByUser(clinician.user) == 1
        Clinician2PatientGroup.countByClinician(clinician) == 2
    }

    @Unroll
    def "test createClinician with invalid command"() {
        setup:
        def command = buildClinicianCommand(firstName: firstname, cleartextPassword: password)

        when:
        def clinician = clinicianService.createClinician(command)
        println clinician.errors
        then:
        clinician.hasErrors()

        !clinician.id
        clinician.errors[field].code == error

        where:
        firstname | password   | field           | error
        ""        | "abcd1234" | "firstName"     | "blank"
        "First"   | ""         | "user.password" | "blank"
        "First"   | "abc"      | "user.password" | "validator.too.short"
    }

    def "test resetPassword on existing user"() {
        setup:
        def clinician = clinicianService.createClinician(buildClinicianCommand())
        def version = clinician.user.version
        def cleartextPassword = clinician.user.cleartextPassword
        def password = clinician.user.password

        when:
        clinicianService.resetPassword(clinician)

        then:
        clinician.user.version > version
        clinician.user.cleartextPassword != cleartextPassword
        clinician.user.password != password
    }

    def "test update with valid command without change of users password"() {
        setup:
        def command = buildClinicianCommand(roleIds: [Role.list().first().id], groupIds: PatientGroup.list()[0..1]*.id)
        def clinician = clinicianService.createClinician(command)
        def version = clinician.version
        command.version = version

        expect:
        UserRole.findAllByUser(clinician.user)*.roleId == command.roleIds
        Clinician2PatientGroup.findAllByClinician(clinician)*.patientGroupId == command.groupIds

        when:
        command.roleIds = [Role.list()[-1].id]
        command.groupIds = [PatientGroup.list()[1].id, PatientGroup.list()[-1].id]
        command.firstName = "New First"

        and:
        clinicianService.update(command, clinician)

        then:
        clinician.version > version
        clinician.firstName == "New First"
        UserRole.findAllByUser(clinician.user)*.roleId == command.roleIds
        Clinician2PatientGroup.findAllByClinician(clinician)*.patientGroupId == command.groupIds

    }


    def "test update with valid command with change of password"() {
        setup:
        def command = buildClinicianCommand(roleIds: [Role.list().first().id], groupIds: PatientGroup.list()[0..1]*.id, cleartextPassword: "abcd1234")
        def clinician = clinicianService.createClinician(command)
        def version = clinician.version
        command.version = version
        def userVersion = clinician.user.version

        expect:
        clinician.user.cleartextPassword == "abcd1234"
        UserRole.findAllByUser(clinician.user)*.roleId == command.roleIds
        Clinician2PatientGroup.findAllByClinician(clinician)*.patientGroupId == command.groupIds

        when:
        command.cleartextPassword = "newpassword1"

        and:
        clinician = clinicianService.update(command, clinician)

        then:
        clinician.version == version
        clinician.user.version > userVersion
        clinician.user.cleartextPassword == "newpassword1"
    }

    def "test update with version mismatch"() {
        setup:
        def command = buildClinicianCommand()
        def clinician = clinicianService.createClinician(command)
        clinician.firstName="For Update"
        clinician.save(flush: true) // Increment clinician version
        command.version = 0

        when:
        clinicianService.update(command, clinician)

        then:
        clinician.hasErrors()
        clinician.errors['version'].code == "clinician.optimistic.locking.failure"
    }

    def "test update with validation error"() {
        setup:
        def command = buildClinicianCommand(firstName: "")

        def clinician = clinicianService.createClinician(command)
        def version = clinician.version

        when:
        clinicianService.update(command, clinician)

        then:
        command.hasErrors()
        clinician.version == version
    }

    def "test get current logged in clinician"() {
        setup:
        authenticate "HelleAndersen", "HelleAndersen1"

        expect:
        clinicianService.currentClinician.firstName == 'Helle'
        clinicianService.currentClinician.lastName == 'Andersen'
    }

    def "test get current logged in clinician when not a clinician"() {
        setup:
        authenticate "NancyAnn", "abcd1234"

        expect:
        !clinicianService.currentClinician
    }

    def "save user-preference"() {
        setup:
        String preference = 'key'
        String value = 'value'
        def clinician = clinicianService.createClinician(buildClinicianCommand())

        when:
        clinicianService.saveUserPreference(clinician, preference, value)

        then:
        clinicianService.getUserPreference(clinician, preference) == value
    }

    def "can delete clinician linked to multiple patient groups"() {
        setup:
        def command = buildClinicianCommand(roleIds: [Role.list().first().id], groupIds: PatientGroup.list()[0..2]*.id)
        def clinician = clinicianService.createClinician(command)
        def id = clinician.id
        def userId = clinician.user.id
        def patientGroupIds = Clinician2PatientGroup.findAllByClinician(clinician)*.id
        
        when:
        clinicianService.delete(clinician)
        
        then:
        User.get(userId).enabled == false
        !Clinician.get(id)
        !Clinician2PatientGroup.findAllByIdInList(patientGroupIds)
    }

    private buildClinicianCommand(Map defaults = [:]) {
        def cmd = new ClinicianCommand(firstName: "First", lastName: "Last", username: "user", cleartextPassword: "abcd1234", roleIds: Role.list().find { it.id % 2 }*.id, groupIds: PatientGroup.list().find { it.id % 2 }*.id)
        defaults.each { key, value -> cmd."$key" = value }
        return cmd
    }
}
