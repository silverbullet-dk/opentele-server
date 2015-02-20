<tmpl:drafts/measurement measurement="${measurement}" headline="${g.message(code:'conferenceMeasurement.bloodPressure.manual.title')}">
    <tmpl:drafts/manualValue name="systolic" title="${g.message(code: 'conferenceMeasurement.bloodPressure.systolic.title')}" value="${g.formatNumber(number: measurement.systolic, format: '0')}">
        <g:message code="conferenceMeasurement.bloodPressure.systolic.format"/>
    </tmpl:drafts/manualValue>

    <tmpl:drafts/manualValue name="diastolic" title="${g.message(code:'conferenceMeasurement.bloodPressure.diastolic.title')}" value="${g.formatNumber(number: measurement.diastolic, format: '0')}">
        <g:message code="conferenceMeasurement.bloodPressure.diastolic.format"/>
    </tmpl:drafts/manualValue>

    <tmpl:drafts/manualValue name="pulse" title="${g.message(code:'conferenceMeasurement.bloodPressure.pulse.title')}" value="${g.formatNumber(number: measurement.pulse, format: '0')}">
        <g:message code="conferenceMeasurement.bloodPressure.pulse.format"/>
    </tmpl:drafts/manualValue>
</tmpl:drafts/measurement>