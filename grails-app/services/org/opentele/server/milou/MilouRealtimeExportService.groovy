package org.opentele.server.milou

import org.opentele.server.model.RealTimeCtg

class MilouRealtimeExportService {
    static transactional = false

    def milouRealtimeWebClientService

    def exportRealTimeCGTToMilou() {

        def ctgsToExport = getRealTimeCtgMeasurementsToExport()

        long currentPatientId = -1
        def exportFailedForPatient = false
        for (RealTimeCtg ctg in ctgsToExport) {
            def delayExportForFailedPatient = exportFailedForPatient && currentPatientId == ctg.patient.id
            if (delayExportForFailedPatient) {
                // measurements will be re-tried on next job run
                continue
            }

            currentPatientId = ctg.patient.id

            RealTimeCtg.withNewTransaction { status ->
                def exportSucceeded = milouRealtimeWebClientService.sendRealtimeCTGMeasurement(ctg)
                exportFailedForPatient = !exportSucceeded

                if(exportSucceeded) {
                   ctg.delete()
                } else {
                    status.setRollbackOnly();
                }
            }
        }
    }

    private def getRealTimeCtgMeasurementsToExport() {
        def query = RealTimeCtg.createCriteria()
        return query.list {
            patient {
                order('id', 'asc')
            }
            order('createdDate', 'asc')
        }
    }
}
