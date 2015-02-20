<tmpl:drafts/measurement measurement="${measurement}" headline="${g.message(code:'conferenceMeasurement.bloodPressure.automatic.title')}">
    <tmpl:drafts/automaticValue name="systolic" title="${g.message(code:'conferenceMeasurement.bloodPressure.systolic.title')}" value="${g.formatNumber(number: measurement.systolic, format: '0')}"/>
    <tmpl:drafts/automaticValue name="diastolic" title="${g.message(code:'conferenceMeasurement.bloodPressure.diastolic.title')}" value="${g.formatNumber(number: measurement.diastolic, format: '0')}"/>
    <tmpl:drafts/automaticValue name="pulse" title="${g.message(code:'conferenceMeasurement.bloodPressure.pulse.title')}" value="${g.formatNumber(number: measurement.pulse, format: '0')}"/>
</tmpl:drafts/measurement>
