<%@ page import="org.opentele.server.core.model.types.MeasurementTypeName" %>
<table class="thresholds">
    <thead>
        <tr>
            <th><g:message code="threshold.type"/></th>
            <th><g:message code="threshold.alertHigh"/></th>
            <th><g:message code="threshold.warningHigh"/></th>
            <th><g:message code="threshold.warningLow"/></th>
            <th><g:message code="threshold.alertLow"/></th>
            <g:unless test="${readonly}">
                <sec:ifAnyGranted roles="${writePermission},${deletePermission}">
                    <th class="actions"><g:message code="threshold.action"/></th>
                </sec:ifAnyGranted>
            </g:unless>
        </tr>
    </thead>
    <tbody>
        <g:each in="${thresholds.sort { it.refresh(); message(code: 'threshold.' + it.type.name + '.label') }}" status="i" var="threshold">
            <g:if test="${threshold.type.name == MeasurementTypeName.BLOOD_PRESSURE}">
                <tmpl:/patient/thresholdRow threshold="${threshold}" field="systolic" controllerName="bloodPressureThreshold" rowspan="2"/>
                <tmpl:/patient/thresholdRow threshold="${threshold}" field="diastolic" controllerName="bloodPressureThreshold" readonly="true"/>
            </g:if>
            <g:elseif test="${threshold.type.name == MeasurementTypeName.URINE}">
                <tmpl:/patient/thresholdRow threshold="${threshold}" controllerName="urineThreshold"/>
            </g:elseif>
            <g:elseif test="${threshold.type.name == MeasurementTypeName.URINE_GLUCOSE}">
                <tmpl:/patient/thresholdRow threshold="${threshold}" controllerName="urineGlucoseThreshold"/>
            </g:elseif>
            <g:else>
                <tmpl:/patient/thresholdRow threshold="${threshold}" controllerName="numericThreshold"/>
            </g:else>
        </g:each>
    </tbody>
    <% if(body) out << body() %>
</table>
