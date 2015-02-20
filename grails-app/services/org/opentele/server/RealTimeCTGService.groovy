package org.opentele.server

import org.opentele.server.model.RealTimectg

class RealTimeCTGService {


    def save(def params) {
        new RealTimectg(params).save(failOnError: true)
    }
}
