import pages.LoginPage
import pages.patient.OverviewPage
import pages.questionnaire.ListPage
import spock.lang.Stepwise

@Stepwise
class LoginAuthSpec extends OpenTeleSpec {
    def "invalid login"() {
        given: "I am at the login page"
        to LoginPage

        when: "I am entering invalid password"
        loginForm.j_username = "admin"
        loginForm.j_password = "Flaf"
        loginButton.click()

        then: "I am being redirected to the login page, the password I entered is wrong"
        at LoginPage
        loginForm.j_username == ""
        loginForm.j_password == ""
    }

    def "admin login"() {
        given : "I am at the login page"
        to LoginPage

        when: "I am entering valid username and password"
        loginForm.j_username = "admin"
        loginForm.j_password = "admin_23"
        loginButton.click()

        then: "I am being redirected to the admin homepage"
        at ListPage
        $().text().contains("Logget ind som admin")

        and: "I log out"
        logoutLink.click()
    }

    def "patient login"() {
        given : "I am at the login page"
        to LoginPage

        when: "I am entering valid username and password"
        loginForm.j_username = "NancyAnn"
        loginForm.j_password = "abcd1234"
        loginButton.click()

        then: "I am not allowed to log in"
        at LoginPage
    }

    def "clinician login"() {
        given : "I am at the login page"
        to LoginPage

        when: "I am entering valid username and password"
        loginForm.j_username = "JensHansen"
        loginForm.j_password = "JensHansen1"
        loginButton.click()

        then: "I am being redirected to the patient homepage"
        at OverviewPage
    }
}
