<tmpl:drafts/measurement measurement="${measurement}" headline="${g.message(code:'conferenceMeasurement.saturation.manual.title')}">
    <tmpl:drafts/manualValue name="saturation" title="${g.message(code:'conferenceMeasurement.saturation.saturation.title')}" value="${g.formatNumber(number: measurement.saturation, format:'0')}">
        <g:message code="conferenceMeasurement.saturation.saturation.format"/>
    </tmpl:drafts/manualValue>

    <tmpl:drafts/manualValue name="pulse" title="${g.message(code:'conferenceMeasurement.saturation.pulse.title')}" value="${g.formatNumber(number: measurement.pulse, format: '0')}">
        <g:message code="conferenceMeasurement.saturation.pulse.format"/>
    </tmpl:drafts/manualValue>
</tmpl:drafts/measurement>