package org.opentele.server.provider

import org.opentele.server.model.StandardThresholdSet
import org.opentele.server.model.Threshold


class StandardThresholdSetService {
    StandardThresholdSet findStandardThresholdSetForThreshold(Threshold threshold) {
        StandardThresholdSet.forThreshold(threshold).get()
    }
}
