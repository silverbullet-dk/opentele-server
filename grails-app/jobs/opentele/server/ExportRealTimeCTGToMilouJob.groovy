package opentele.server

class ExportRealTimeCTGToMilouJob {

    def milouRealtimeExportService
    def concurrent = false

    static triggers = { }

    def execute() {
        log.debug("Exporting realtime CTG measurements to Milou")
        try {
            milouRealtimeExportService.exportRealTimeCGTToMilou()
        } catch (Exception e) {
            log.error("Export realtime: uncaught:", e)
        }
    }
}
