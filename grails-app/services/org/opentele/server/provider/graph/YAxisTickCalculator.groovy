package org.opentele.server.provider.graph

import org.opentele.server.provider.TickY
import org.opentele.server.core.model.types.MeasurementTypeName
import org.opentele.server.core.model.types.MeasurementTypeNameVisitor

import java.text.NumberFormat

class YAxisTickCalculator implements MeasurementTypeNameVisitor {
    private static ThreadLocal<NumberFormat> INTEGER_FORMATTER = new ThreadLocal<NumberFormat>() {
        @Override
        protected NumberFormat initialValue() {
            NumberFormat result = NumberFormat.getInstance()
            result.maximumFractionDigits = 0
            result
        }
    }
    private static ThreadLocal<NumberFormat> SINGLE_DIGIT_FORMATTER = new ThreadLocal<NumberFormat>() {
        @Override
        protected NumberFormat initialValue() {
            NumberFormat result = NumberFormat.getInstance(new Locale('da', 'DK'))
            result.minimumFractionDigits = result.maximumFractionDigits = 1
            result
        }
    }

    List<TickY> result
    double minimum, maximum

    static List<TickY> calculate(MeasurementTypeName measurementTypeName, double minimum, double maximum) {
        def YAxisTickCalculator calculator = new YAxisTickCalculator(minimum, maximum)
        measurementTypeName.visit(calculator)
        calculator.result
    }

    static List<TickY> calculateSystolic(double minimum, double maximum) {
        def YAxisTickCalculator calculator = new YAxisTickCalculator(minimum, maximum)
        calculator.createTicksFor(100, 200, 20, INTEGER_FORMATTER)
        calculator.result
    }

    static List<TickY> calculateDiastolic(double minimum, double maximum) {
        def YAxisTickCalculator calculator = new YAxisTickCalculator(minimum, maximum)
        calculator.createTicksFor(60, 140, 20, INTEGER_FORMATTER)
        calculator.result
    }

    private YAxisTickCalculator(double minimum, double maximum) {
        this.minimum = minimum
        this.maximum = maximum
        this.result = []
    }

    @Override
    void visitBloodPressure() {
        createTicksFor(40, 200, 20, INTEGER_FORMATTER)
    }

    @Override
    void visitCtg() {
        throw new IllegalArgumentException('Cannot generate Y axis ticks for CTG')
    }

    @Override
    void visitTemperature() {
        createTicksFor(35, 40, 0.5, SINGLE_DIGIT_FORMATTER)
    }

    @Override
    void visitUrine() {
        createTicksFor(0, 5, 1, INTEGER_FORMATTER)
    }

    @Override
    void visitUrineCombi() {
        // TODO KM: Har ingen ide om hvad der skal stå her....
    }

    @Override
    void visitUrineGlucose() {
        createTicksFor(0, 5, 1, INTEGER_FORMATTER)
    }

    @Override
    void visitPulse() {
        createTicksFor(50, 130, 10, INTEGER_FORMATTER)
    }

    @Override
    void visitWeight() {
        createTicksFor(40, 200, 20, INTEGER_FORMATTER)
    }

    @Override
    void visitHemoglobin() {
        createTicksFor(5, 11, 0.5, SINGLE_DIGIT_FORMATTER)
    }

    @Override
    void visitSaturation() {
        createTicksFor(80, 100, 5, INTEGER_FORMATTER)
    }

    @Override
    void visitCrp() {
        // CRP values can get insanely high, so we have to adjust to potentially very high maximum values here
        for (int tickSize = 10; ; tickSize *= 10) {
            if (maximum <= tickSize * 20) {
                createTicksFor(0, tickSize*10, tickSize, INTEGER_FORMATTER)
                return
            }
        }
    }

    @Override
    void visitBloodSugar() {
        def tickSize = 2
        minimum -= 1 //Add one unit of space to bottom of graph
        minimum = Math.floor(minimum)

        if(minimum < 0) {
            minimum = 0
        }

        maximum += tickSize
        maximum = Math.ceil(maximum)
        createTicksFor(minimum, maximum, tickSize, INTEGER_FORMATTER)
    }

    @Override
    void visitContinuousBloodSugarMeasurement() {
        def tickSize = 2
        minimum -= 1 //Add one unit of space to bottom of graph
        minimum = Math.floor(minimum)

        if(minimum < 0) {
            minimum = 0
        }

        maximum += tickSize
        maximum = Math.ceil(maximum)
        createTicksFor(minimum, maximum, tickSize, INTEGER_FORMATTER)
    }

    @Override
    void visitLungFunction() {
        createTicksFor(2, 5, 0.5, SINGLE_DIGIT_FORMATTER)
    }

    private void createTicksFor(double rangeStart, double rangeEnd, double stepSize, ThreadLocal<NumberFormat> formatter) {
        def startAt = findStart(rangeStart, stepSize, minimum)
        def endAt = findEnd(rangeEnd, stepSize, maximum)
        for (double currentTick = startAt; currentTick <= endAt; currentTick += stepSize) {
            result << new TickY(value: currentTick, text: formatter.get().format(currentTick))
        }
    }

    private double findStart(double rangeStart, double stepSize, double actualRangeStart) {
        double result = rangeStart
        while (result > actualRangeStart) {
            result -= stepSize
        }

        result
    }

    private double findEnd(double rangeEnd, double stepSize, double actualRangeEnd) {
        double result = rangeEnd
        while (result < actualRangeEnd) {
            result += stepSize
        }
        result
    }
}
