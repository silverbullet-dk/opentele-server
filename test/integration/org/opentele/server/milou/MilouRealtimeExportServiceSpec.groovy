package org.opentele.server.milou

import grails.test.spock.IntegrationSpec
import org.opentele.server.model.RealTimectg

class MilouRealtimeExportServiceSpec extends IntegrationSpec {
    def milouRealtimeExportService
    static transactional = false

    def setup() {  // We need to handle the cleanup ourselves since we are running without transactions
        RealTimectg.findAll()*.delete()
    }

    def 'will delete CTG after export'() {
        when:
        def realtimeCtgMeasurement = RealTimectg.build()
        realtimeCtgMeasurement.save(failOnError: true)

        milouRealtimeExportService.milouRealtimeWebClientService = Mock(MilouRealtimeWebClientService)
        milouRealtimeExportService.milouRealtimeWebClientService.sendRealtimeCTGMeasurement(_) >> true
        milouRealtimeExportService.exportRealTimeCGTToMilou()

        then:
        RealTimectg.findAll().empty

    }

    def 'will export CTGs in correct order'() {
        when:
        def realtimeCtgMeasurement1 = RealTimectg.build()
        realtimeCtgMeasurement1.save(failOnError: true)

        def realtimeCtgMeasurement2 = RealTimectg.build()
        realtimeCtgMeasurement2.save(failOnError: true)

        def realtimeCtgMeasurement3 = RealTimectg.build()
        realtimeCtgMeasurement3.save(failOnError: true)

        milouRealtimeExportService.milouRealtimeWebClientService = Mock(MilouRealtimeWebClientService)

        //Not pretty. But we need to ensure that the ctgs are sent in a first-in first-out manner.
        def expectedId = 2
        milouRealtimeExportService.milouRealtimeWebClientService.sendRealtimeCTGMeasurement({ RealTimectg ctg ->
            assert ctg.id == expectedId

            expectedId += 1
        }) >> true

        milouRealtimeExportService.exportRealTimeCGTToMilou()

        then:

        RealTimectg.findAll().empty

    }

    //TODO: EMH: Fix test
//    def 'will stop exporting on first error'() {
//
//        when:
//        def realtimeCtgMeasurement1 = RealTimectg.build()
//        realtimeCtgMeasurement1.save(failOnError: true)
//
//        def realtimeCtgMeasurement2 = RealTimectg.build()
//        realtimeCtgMeasurement2.save(failOnError: true)
//
//        def realtimeCtgMeasurement3 = RealTimectg.build()
//        realtimeCtgMeasurement3.save(failOnError: true)
//
//        def realtimeCtgMeasurement4 = RealTimectg.build()
//        realtimeCtgMeasurement4.save(failOnError: true)
//
//        milouRealtimeExportService.milouRealtimeWebClientService = Mock(MilouRealtimeWebClientService)
//        3 * milouRealtimeExportService.milouRealtimeWebClientService.sendRealtimeCTGMeasurement(_) >>> [true, true, false] //Let the first two exports succeed, but fail the third one
//
//        milouRealtimeExportService.exportRealTimeCGTToMilou()
//
//        then:
//        RealTimectg.findAll()*.id == [realtimeCtgMeasurement3.id, realtimeCtgMeasurement4.id]
//
//    }
}
