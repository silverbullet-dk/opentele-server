package org.opentele.server.milou

import grails.test.spock.IntegrationSpec
import org.opentele.server.model.Patient
import org.opentele.server.model.RealTimeCtg

class MilouRealtimeExportServiceSpec extends IntegrationSpec {
    def milouRealtimeExportService
    static transactional = false // don't delete, tests will start to fail...

    def setup() {  // We need to handle the cleanup ourselves since we are running without transactions
        RealTimeCtg.findAll()*.delete()
        milouRealtimeExportService.milouRealtimeWebClientService = Mock(MilouRealtimeWebClientService)
    }

    def 'will delete CTG after export'() {
        when:
        def realtimeCtgMeasurement = RealTimeCtg.build()
        realtimeCtgMeasurement.save(failOnError: true)

        milouRealtimeExportService.milouRealtimeWebClientService.sendRealtimeCTGMeasurement(_) >> true
        milouRealtimeExportService.exportRealTimeCGTToMilou()

        then:
        RealTimeCtg.findAll().empty
    }

    def 'will export CTGs in correct order'() {
        when:
        def now = new Date()
        def patient = Patient.build()
        def realtimeCtgMeasurement1 = RealTimeCtg.build(patient: patient)
        realtimeCtgMeasurement1.save(failOnError: true)

        def realtimeCtgMeasurement2 = RealTimeCtg.build(patient: patient)
        realtimeCtgMeasurement2.save(failOnError: true)

        def realtimeCtgMeasurement3 = RealTimeCtg.build(patient: patient)
        realtimeCtgMeasurement3.save(failOnError: true)

        //Not pretty. But we need to ensure that the ctgs are sent in a first-in first-out manner.
        def orderedSendIds = []
        milouRealtimeExportService.milouRealtimeWebClientService.sendRealtimeCTGMeasurement({ RealTimeCtg ctg ->
            orderedSendIds << ctg.id
        }) >> true

        milouRealtimeExportService.exportRealTimeCGTToMilou()

        then:
        orderedSendIds == [realtimeCtgMeasurement1.id, realtimeCtgMeasurement2.id, realtimeCtgMeasurement3.id]
        RealTimeCtg.findAll().empty
    }

    def 'if CTG measurements for one patient fails, measurements for other patient will still be exported'() {
        given:
        def patient1 = Patient.build(id: 1)
        def patient2 = Patient.build(id: 2)
        def patient3 = Patient.build(id: 3)
        def m1 = RealTimeCtg.build(patient: patient1)
        def fails1 = RealTimeCtg.build(patient: patient2)
        def fails2 = RealTimeCtg.build(patient: patient2)
        def m2 = RealTimeCtg.build(patient: patient3)
        def m3 = RealTimeCtg.build(patient: patient3)
        m1.save(failOnError: true)
        fails1.save(failOnError: true)
        m2.save(failOnError: true)
        fails2.save(failOnError: true)
        m3.save(failOnError: true)

        milouRealtimeExportService.milouRealtimeWebClientService.sendRealtimeCTGMeasurement(fails1) >> false
        milouRealtimeExportService.milouRealtimeWebClientService.sendRealtimeCTGMeasurement(!fails1) >> true

        when:
        milouRealtimeExportService.exportRealTimeCGTToMilou()

        then:
        def all = RealTimeCtg.findAll()
        all.size() == 2
        all.find {ctg -> ctg.id == fails1.id} != null
        all.find {ctg -> ctg.id == fails2.id} != null
    }
}
