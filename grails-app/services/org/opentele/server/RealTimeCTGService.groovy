package org.opentele.server

import org.opentele.server.model.RealTimeCtg

class RealTimeCTGService {

    def save(def params) {
        new RealTimeCtg(params).save(failOnError: true)
    }
}
