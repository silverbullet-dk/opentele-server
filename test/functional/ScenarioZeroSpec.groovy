import pages.LoginPage
import pages.meter.CreatePage
import pages.meter.MetersListPage
import pages.meter.ShowPage
import pages.patient.OverviewPage
import spock.lang.Stepwise

@Stepwise
class ScenarioZeroSpec extends OpenTeleSpec {

    private String weightString = "Weight"
    private String bloodPressureString = "BloodPressure"
    private String weightMeterId="AC-Test-"+ weightString
    private String bloodPressureMeterId="AC-Test-" + bloodPressureString
    private String weightModel="AC-Test-Model-" + weightString
    private String bloodPressureModel="AC-Test-Model-" + bloodPressureString
    private int weightTypeId=2
    private int bloodPressureTypeId=1


    def "Helle Andersen logs in" () {
        given : "I am at the login page"
        to LoginPage

        when: "I am entering valid username and password"
        loginForm.j_username = "HelleAndersen"
        loginForm.j_password = "HelleAndersen1"
        loginButton.click()

        then: "I am being redirected to the patient homepage"
        at OverviewPage
//        $().text().contains("Logget ind som HelleAndersen")
    }

    def "Goes to meters list and click create" () {
        given: "I am logged in"
        to MetersListPage

        when: "I click create meter"
        createLink.click()

        then: "I am being directed to create page"
        at CreatePage
    }

    def "Create new weight meter"() {
        when:
        active = true
        meterId=weightMeterId
        model=weightModel
        meterType.value(""+weightTypeId)
        createLink.click()

        then:
        at ShowPage
    }

    def "validate weight meter created"() {
        expect:
        active == false
        id == weightMeterId
        model == weightModel
    }

    def "Create new blood pressure meter"() {

    }

}
