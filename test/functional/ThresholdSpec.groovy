import pages.LoginPage
import pages.patient.OverviewPage
import pages.threshold.AddThresholdPage
import pages.threshold.ThresholdOverviewPage

class ThresholdSpec extends OpenTeleSpec {
    def setup() {
        to LoginPage

        loginForm.j_username = "HelleAndersen"
        loginForm.j_password = "HelleAndersen1"
        loginButton.click()

        at OverviewPage
    }

    def "I can add a bloodsugar threshold to a patientgroup"() {
        given: "Im at the threshold page"
        to ThresholdOverviewPage


        withConfirm { deleteAllThresholdsFromPatientgroup(1).click() }

        when: "I add a new boodsugar threshold"
        addThressholdToPatientgroup(1).click()
        at AddThresholdPage

        form.type = "Blodsukker"

        alertHigh = 10
        warningHigh = 8

        saveButton.click()
        at ThresholdOverviewPage

        then: "the bloodsugar threshold was added"
        thresholdType(1, 'Blodsukker')
        thresholdAlertHigh(1,1) == '10,0'
        thresholdWarningHigh(1,1) == '8,0'

    }

    def "I can add a Protein i urin threshold to a patientgroup"() {
        given: "Im at the threshold page"
        to ThresholdOverviewPage


        withConfirm { deleteAllThresholdsFromPatientgroup(1).click() }

        when: "I add a new Protein i urin threshold"
        addThressholdToPatientgroup(1).click()
        at AddThresholdPage

        form.type = "Protein i urin"

        alertHigh = '+4'
        alertLow = 'Neg.'

        saveButton.click()
        at ThresholdOverviewPage

        then: "the Protein i urin threshold was added"
        thresholdType(1, 'Protein i urin')
        thresholdAlertHigh(1,1) == '+4'
        thresholdAlertLow(1,1) == 'Neg.'

    }
}
