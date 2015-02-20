import geb.spock.GebReportingSpec
import pages.LogoutPage

abstract class OpenTeleSpec extends GebReportingSpec {
    @Override
    def cleanupSpec() {
        to LogoutPage
    }
}
