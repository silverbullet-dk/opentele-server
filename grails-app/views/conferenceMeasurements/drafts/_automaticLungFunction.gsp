<tmpl:drafts/measurement measurement="${measurement}" headline="${g.message(code:'conferenceMeasurement.lungFunction.automatic.title')}">
    <tmpl:drafts/automaticValue name="fev1" title="${g.message(code:'conferenceMeasurement.lungFunction.fev1.title')}" value="${g.formatNumber(number: measurement.fev1, format:'0.00')}"/>
</tmpl:drafts/measurement>
