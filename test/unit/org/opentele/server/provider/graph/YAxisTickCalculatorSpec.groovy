package org.opentele.server.provider.graph

import org.opentele.server.core.model.types.MeasurementTypeName
import spock.lang.Specification

class YAxisTickCalculatorSpec extends Specification {
    def 'gives correct ticks for weight'() {
        expect:
        ticks(MeasurementTypeName.WEIGHT, minimum, maximum) == expectedTicks

        where:
        minimum | maximum | expectedTicks
             60 |     120 | [40, 60, 80, 100, 120, 140, 160, 180, 200]
             40 |     200 | [40, 60, 80, 100, 120, 140, 160, 180, 200]
             35 |     135 | [20, 40, 60, 80, 100, 120, 140, 160, 180, 200]
            205 |     230 | [40, 60, 80, 100, 120, 140, 160, 180, 200, 220, 240]
    }

    def 'formats tick labels correctly for weight'() {
        expect:
        tickLabels(MeasurementTypeName.WEIGHT, minimum, maximum) == expectedTickLabels

        where:
        minimum | maximum | expectedTickLabels
             60 |     120 | ['40', '60', '80', '100', '120', '140', '160', '180', '200']
    }

    def 'gives correct ticks for temperature'() {
        expect:
        ticks(MeasurementTypeName.TEMPERATURE, minimum, maximum) == expectedTicks

        where:
        minimum | maximum | expectedTicks
           37.2 |    38.0 | [35.0, 35.5, 36.0, 36.5, 37.0, 37.5, 38.0, 38.5, 39.0, 39.5, 40.0]
           33.0 |    38.0 | [33.0, 33.5, 34.0, 34.5, 35.0, 35.5, 36.0, 36.5, 37.0, 37.5, 38.0, 38.5, 39.0, 39.5, 40.0]
           37.2 |    40.5 | [35.0, 35.5, 36.0, 36.5, 37.0, 37.5, 38.0, 38.5, 39.0, 39.5, 40.0, 40.5]
    }

    def 'formats tick labels correctly for temperature'() {
        expect:
        tickLabels(MeasurementTypeName.TEMPERATURE, minimum, maximum) == expectedTickLabels

        where:
        minimum | maximum | expectedTickLabels
           37.2 |    38.0 | ['35,0', '35,5', '36,0', '36,5', '37,0', '37,5', '38,0', '38,5', '39,0', '39,5', '40,0']
    }

    def 'gives correct ticks for urine'() {
        expect:
        ticks(MeasurementTypeName.URINE, minimum, maximum) == expectedTicks

        where:
        minimum | maximum | expectedTicks
              3 |       4 | [0, 1, 2, 3, 4, 5]
             -1 |       4 | [-1, 0, 1, 2, 3, 4, 5]
              3 |       7 | [0, 1, 2, 3, 4, 5, 6, 7]
    }

    def 'gives correct ticks for glucose in urine'() {
        expect:
        ticks(MeasurementTypeName.URINE_GLUCOSE, minimum, maximum) == expectedTicks

        where:
        minimum | maximum | expectedTicks
              3 |       4 | [0, 1, 2, 3, 4, 5]
             -1 |       4 | [-1, 0, 1, 2, 3, 4, 5]
              3 |       7 | [0, 1, 2, 3, 4, 5, 6, 7]
    }

    def 'gives correct ticks for pulse'() {
        expect:
        ticks(MeasurementTypeName.PULSE, minimum, maximum) == expectedTicks

        where:
        minimum | maximum | expectedTicks
             65 |      70 | [50, 60, 70, 80, 90, 100, 110, 120, 130]
             35 |      70 | [30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130]
             65 |     150 | [50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150]
    }

    def 'gives correct ticks for hemoglobin'() {
        expect:
        ticks(MeasurementTypeName.HEMOGLOBIN, minimum, maximum) == expectedTicks

        where:
        minimum | maximum | expectedTicks
            7.0 |     8.0 | [5.0, 5.5, 6.0, 6.5, 7.0, 7.5, 8.0, 8.5, 9.0, 9.5, 10.0, 10.5, 11.0]
            3.4 |     8.0 | [3.0, 3.5, 4.0, 4.5, 5.0, 5.5, 6.0, 6.5, 7.0, 7.5, 8.0, 8.5, 9.0, 9.5, 10.0, 10.5, 11.0]
            7.0 |    12.0 | [5.0, 5.5, 6.0, 6.5, 7.0, 7.5, 8.0, 8.5, 9.0, 9.5, 10.0, 10.5, 11.0, 11.5, 12.0]
    }

    def 'gives correct ticks for saturation'() {
        expect:
        ticks(MeasurementTypeName.SATURATION, minimum, maximum) == expectedTicks

        where:
        minimum | maximum | expectedTicks
             95 |      96 | [80, 85, 90, 95, 100]
             65 |      96 | [65, 70, 75, 80, 85, 90, 95, 100]
             95 |     105 | [80, 85, 90, 95, 100, 105]
    }

    def 'gives correct ticks for CRP, and expands scale sensibly'() {
        expect:
        ticks(MeasurementTypeName.CRP, minimum, maximum) == expectedTicks

        where:
        minimum | maximum | expectedTicks
             50 |       60 | [0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100]
             50 |      130 | [0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130]
            -10 |       60 | [-10, 0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100]
              0 |      200 | [0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200]
              0 |      201 | [0, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000]
              0 |     1000 | [0, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000]
              0 |     1001 | [0, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 1100]
              0 |     2000 | [0, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 1100, 1200, 1300, 1400, 1500, 1600, 1700, 1800, 1900, 2000]
              0 |     2001 | [0, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000]
              0 |    10000 | [0, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000]
              0 |    20000 | [0, 1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000, 10000, 11000, 12000, 13000, 14000, 15000, 16000, 17000, 18000, 19000, 20000]
              0 |    20001 | [0, 10000, 20000, 30000, 40000, 50000, 60000, 70000, 80000, 90000, 100000]
              0 |   100000 | [0, 10000, 20000, 30000, 40000, 50000, 60000, 70000, 80000, 90000, 100000]
              0 |   100001 | [0, 10000, 20000, 30000, 40000, 50000, 60000, 70000, 80000, 90000, 100000, 110000]
              0 |   200000 | [0, 10000, 20000, 30000, 40000, 50000, 60000, 70000, 80000, 90000, 100000, 110000, 120000, 130000, 140000, 150000, 160000, 170000, 180000, 190000, 200000]
              0 |   200001 | [0, 100000, 200000, 300000, 400000, 500000, 600000, 700000, 800000, 900000, 1000000]
              0 |  1000000 | [0, 100000, 200000, 300000, 400000, 500000, 600000, 700000, 800000, 900000, 1000000]
              0 |  2000000 | [0, 100000, 200000, 300000, 400000, 500000, 600000, 700000, 800000, 900000, 1000000, 1100000, 1200000, 1300000, 1400000, 1500000, 1600000, 1700000, 1800000, 1900000, 2000000]
              0 |  2000001 | [0, 1000000, 2000000, 3000000, 4000000, 5000000, 6000000, 7000000, 8000000, 9000000, 10000000]
              0 | 10000000 | [0, 1000000, 2000000, 3000000, 4000000, 5000000, 6000000, 7000000, 8000000, 9000000, 10000000]
              0 | 10000001 | [0, 1000000, 2000000, 3000000, 4000000, 5000000, 6000000, 7000000, 8000000, 9000000, 10000000, 11000000]
              0 | 11000000 | [0, 1000000, 2000000, 3000000, 4000000, 5000000, 6000000, 7000000, 8000000, 9000000, 10000000, 11000000]
              0 | 12000000 | [0, 1000000, 2000000, 3000000, 4000000, 5000000, 6000000, 7000000, 8000000, 9000000, 10000000, 11000000, 12000000]
              0 | 13000000 | [0, 1000000, 2000000, 3000000, 4000000, 5000000, 6000000, 7000000, 8000000, 9000000, 10000000, 11000000, 12000000, 13000000]
    }

    def 'formats tick labels correctly for CRP'() {
        where:
        minimum |  maximum | expectedTickLabels
             50 |       60 | ['0', '10', '20', '30', '40', '50', '60', '70', '80', '90', '100']
              0 |  1000000 | ['0', '100.000', '200.000', '300.000', '400.000', '500.000', '600.000', '700.000', '800.000', '900.000', '1.000.000']
              0 | 10000000 | ['0', '1.000.000', '2.000.000', '3.000.000', '4.000.000', '5.000.000', '6.000.000', '7.000.000', '8.000.000', '9.000.000', '10.000.000']
    }

    def 'gives ticks for lung function'() {
        expect:
        ticks(MeasurementTypeName.LUNG_FUNCTION, minimum, maximum) == expectedTicks

        where:
        minimum | maximum | expectedTicks
            3.0 |     3.2 | [2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0]
            1.3 |     3.2 | [1.0, 1.5, 2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0]
            3.0 |     5.4 | [2.0, 2.5, 3.0, 3.5, 4.0, 4.5, 5.0, 5.5]
    }

    def 'gives correct ticks for blood pressure'() {
        expect:
        ticks(MeasurementTypeName.BLOOD_PRESSURE, minimum, maximum) == expectedTicks

        where:
        minimum | maximum | expectedTicks
             67 |     120 | [40, 60, 80, 100, 120, 140, 160, 180, 200]
             24 |     120 | [20, 40, 60, 80, 100, 120, 140, 160, 180, 200]
             67 |     256 | [40, 60, 80, 100, 120, 140, 160, 180, 200, 220, 240, 260]
    }

    def 'gives correct ticks for systolic blood pressure'() {
        expect:
        systolicTicks(minimum, maximum) == expectedTicks

        where:
        minimum | maximum | expectedTicks
            145 |     150 | [100, 120, 140, 160, 180, 200]
             75 |     150 | [60, 80, 100, 120, 140, 160, 180, 200]
            145 |     250 | [100, 120, 140, 160, 180, 200, 220, 240, 260]
    }

    def 'gives correct ticks for diastolic blood pressure'() {
        expect:
        diastolicTicks(minimum, maximum) == expectedTicks

        where:
        minimum | maximum | expectedTicks
            105 |     110 | [60, 80, 100, 120, 140]
             45 |     110 | [40, 60, 80, 100, 120, 140]
            105 |     165 | [60, 80, 100, 120, 140, 160, 180]
    }

    def 'gives correct ticks for blood sugar'() {
        expect:
        ticks(MeasurementTypeName.BLOODSUGAR, minimum, maximum) == expectedTicks

        where:
        minimum | maximum | expectedTicks
              0 |      20   | [0.0, 2.0, 4.0, 6.0, 8.0, 10.0, 12.0, 14.0, 16.0, 18.0, 20.0, 22.0]
              5 |      10   | [4.0, 6.0, 8.0, 10.0, 12.0]
            3.4 |      12.3 | [2.0, 4.0, 6.0, 8.0, 10.0, 12.0, 14.0]
            2.3 |      14.3 | [1.0, 3, 5, 7, 9, 11, 13, 15, 17]
              2 |      30   | [1.0, 3.0, 5.0, 7.0, 9.0, 11.0, 13.0, 15.0, 17.0, 19.0, 21.0, 23.0, 25.0, 27.0, 29.0, 31.0]
            1.0 |      33.3 | [0.0, 2.0, 4.0, 6.0, 8.0, 10.0, 12.0, 14.0, 16.0, 18.0, 20.0, 22.0, 24.0, 26.0, 28.0, 30.0, 32.0, 34.0, 36.0]
            1.0 |      11.9 | [0.0, 2.0, 4.0, 6.0, 8.0, 10.0, 12.0, 14.0]
    }

    private def systolicTicks(double minimum, double maximum) {
        YAxisTickCalculator.calculateSystolic(minimum, maximum)*.value
    }

    private def diastolicTicks(double minimum, double maximum) {
        YAxisTickCalculator.calculateDiastolic(minimum, maximum)*.value
    }

    private def ticks(MeasurementTypeName measurementType, double minimum, double maximum) {
        YAxisTickCalculator.calculate(measurementType, minimum, maximum)*.value
    }

    private def tickLabels(MeasurementTypeName measurementType, double minimum, double maximum) {
        YAxisTickCalculator.calculate(measurementType, minimum, maximum)*.text
    }
}
