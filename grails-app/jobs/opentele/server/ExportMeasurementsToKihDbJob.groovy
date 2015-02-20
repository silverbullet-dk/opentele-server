package opentele.server

class ExportMeasurementsToKihDbJob {

    def kihDbExportService

    def concurrent = false

    static triggers = { }

    def execute() {
        log.debug("Exporting measurements to KIH Database")

        kihDbExportService.exportToKihDatabase()
    }
}

