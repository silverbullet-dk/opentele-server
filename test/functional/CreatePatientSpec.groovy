import pages.LoginPage
import pages.patient.FindPage
import pages.patient.OverviewPage
import pages.patient.create.*

class CreatePatientSpec extends OpenTeleSpec {
    def setupSpec() {
        to LoginPage

        loginForm.j_username = "HelleAndersen"
        loginForm.j_password = "HelleAndersen1"
        loginButton.click()

        at OverviewPage
    }

    def "Clinician can create a patient"() {
        given: "That I'm logged in and at the overview page"
        at OverviewPage

        when: "I fill out basic patient information"
        createPatientLink.click()
        at BasicInformationPage

        enterBasicInformation()

        and: "fill out user information"
        at UserInformationPage

        createPatientForm.username = "FunctionalTestUser8"
        nextButton.click()

        //Then patient exists...
        and: "select patient group"
        at PatientGroupPage
        createPatientForm.groupIds = "Hjertepatient (Afdeling-B Test)"
        nextButton.click()

        and: "fill out thresholds"
        //TODO: Fill out thresholds
        at ThresholdsPage
        nextButton.click()

        and: "add a few comments"
        at CommentsPage
        createPatientForm.comment = "Vi skal kontakte ...."
        nextButton.click()

        and: "add patient relation"
        at AddPatientRelationsPage
        nextButton.click()

        and: "confirm information"
        at ConfirmationPage
        saveButton.click()


        and: "I search for the patient"
        to FindPage
        searchForm.ssn = "0987654319"
        findPatientButton.click()

        then: "Then the patient is found"
        firstResultCPR == "098765-4319"



    }

    private void enterBasicInformation() {
        createPatientForm.cpr = "0987654319"
        createPatientForm.firstName = "Test"
        createPatientForm.lastName = "Testsen"
        createPatientForm.sex = "MALE"
        createPatientForm.address = "Test gade 12"
        createPatientForm.postalCode = "8200"
        createPatientForm.city = "Aarhus N"

        nextButton.click()
    }
}
