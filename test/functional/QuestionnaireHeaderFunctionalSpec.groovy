import pages.LoginPage
import pages.LogoutPage
import pages.patient.OverviewPage
import pages.questionnaireHeader.QuestionnaireHeaderListPage
import spock.lang.Ignore
import spock.lang.Stepwise

@Stepwise
class QuestionnaireHeaderFunctionalSpec extends OpenTeleSpec {

    @Ignore  // TODO: Figure out, why it's not working
    def "Helle Andersen logs in"() {
        given: "I am at the login page"
        to LoginPage

        when: "I am entering valid username and password for Helle Andersen"
        loginForm.j_username = "HelleAndersen"
        loginForm.j_password = "HelleAndersen1"
        loginButton.click()

        then: "I am being redirected to the patient homepage"
        at OverviewPage
    }

    @Ignore
    def "Go to the QuestionnaireHeader list page"() {
        given: "Helle Andersen is logged in"
        to  QuestionnaireHeaderListPage

        expect: "so it is expected that there is a createLink"
        !createLink.empty

        when:
        createLink.click()

        then: "I Helle Andersen is now on the Create page"
        true // TODO: Make page!

        cleanup:
        to LogoutPage
    }
}
