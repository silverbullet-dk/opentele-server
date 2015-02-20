<tmpl:drafts/measurement measurement="${measurement}" headline="${g.message(code: 'conferenceMeasurement.saturation.automatic.title')}">
    <tmpl:drafts/automaticValue name="saturation" title="${g.message(code:'conferenceMeasurement.saturation.saturation.title')}" value="${g.formatNumber(number: measurement.saturation, format:'0')}"/>
    <tmpl:drafts/automaticValue name="pulse" title="${g.message(code:'conferenceMeasurement.saturation.pulse.title')}" value="${g.formatNumber(number: measurement.pulse, format: '0')}"/>
</tmpl:drafts/measurement>