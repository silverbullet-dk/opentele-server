package org.opentele.server.provider

import org.opentele.server.model.MeasurementType
import org.opentele.server.core.model.types.MeasurementTypeName

class MeasurementTypeService {

    def i18nService

    def getUnusedMeasurementTypesForThresholds(def currentMeasurementTypeNames) {

        def list = (currentMeasurementTypeNames.size() < 1 ? MeasurementType.list() : MeasurementType.withCriteria {
            not {
                inList('name', currentMeasurementTypeNames)
            }
        })*.name.sort { i18nService.message(code: "threshold.${it.name()}") }

        list.removeAll(MeasurementTypeName.MEASUREMENT_TYPE_NAMES_WITHOUT_THRESHOLD_SUPPORT)
        list
    }
}
