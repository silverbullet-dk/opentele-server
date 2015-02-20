<tmpl:drafts/measurement measurement="${measurement}" headline="${g.message(code:'conferenceMeasurement.lungFunction.manual.title')}">
    <tmpl:drafts/manualValue name="fev1" title="${g.message(code:'conferenceMeasurement.lungFunction.fev1.title')}" value="${g.formatNumber(number: measurement.fev1, format:'0.00')}">
        <g:message code="conferenceMeasurement.lungFunction.fev1.format"/>
    </tmpl:drafts/manualValue>
</tmpl:drafts/measurement>
