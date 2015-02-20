package org.opentele.server.provider.sosi

import dk.sosi.seal.SOSIFactory
import dk.sosi.seal.model.IDCard
import dk.sosi.seal.model.Request
import org.codehaus.groovy.grails.commons.GrailsApplication
import org.opentele.server.provider.util.SosiUtil

class SosiService {
    SosiUtil sosiUtil
    String cvr
    String systemName

    //TODO mss check that this method is called
    void setGrailsApplication(GrailsApplication grailsApplication) {
        cvr = grailsApplication.config.seal.cvr
        systemName = grailsApplication.config.seal.systemName
        sosiUtil = new SosiUtil(grailsApplication.config)
    }

    def createRequest() {
        Request sosiRequest = createSosiRequest()
        setIDCard(sosiRequest)
        sosiRequest
    }

    private void setIDCard(Request sosiRequest) {
        IDCard signedIdCard = sosiUtil.getSignedIDCard()
        sosiRequest.setIDCard(signedIdCard)
    }

    private Request createSosiRequest() {
        SOSIFactory sosiFactory = sosiUtil.getSOSIFactory()
        sosiFactory.createNewRequest(false, "flow")
    }
}
