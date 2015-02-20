<tmpl:drafts/measurement measurement="${measurement}" headline="${g.message(code:'conferenceMeasurement.weight.manual.title')}">
    <tmpl:drafts/manualValue name="weight" title="${g.message(code:'conferenceMeasurement.weight.weight.title')}" value="${g.formatNumber(number: measurement.weight, format:'0.0')}">
        <g:message code="conferenceMeasurement.weight.weight.format"/>
    </tmpl:drafts/manualValue>
</tmpl:drafts/measurement>
