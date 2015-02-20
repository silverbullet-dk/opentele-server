package opentele.server

class ExportCTGToMilouJob {
    def milouExportService
    def grailsApplication
    def concurrent = false

    static triggers = { }

    def execute() {
        log.debug("Exporting CTG measurements to Milou")
        milouExportService.exportToMilou()
    }
}

