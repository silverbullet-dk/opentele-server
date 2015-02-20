package org.opentele.server.milou

import org.opentele.server.model.RealTimectg

class MilouRealtimeExportService {
    static transactional = false

    def milouRealtimeWebClientService

    def exportRealTimeCGTToMilou() {
        def realTimectgs = RealTimectg.findAll([sort: 'createdDate', order: 'asc']) {
            true
        }
        for(RealTimectg ctg in realTimectgs) {
            def measurementExported

            RealTimectg.withNewTransaction { status ->
                measurementExported = milouRealtimeWebClientService.sendRealtimeCTGMeasurement(ctg)

                if(measurementExported) {
                   ctg.delete()
                } else {
                    status.setRollbackOnly();
                }
            }

            if(!measurementExported) {
                break
            }
        }
    }
}
