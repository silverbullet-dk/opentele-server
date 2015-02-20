<%@ page import="org.opentele.server.core.model.types.MeasurementTypeName" %>
<g:if test="${thresholdType == MeasurementTypeName.BLOOD_PRESSURE}">
    <g:render template="/bloodPressureThreshold/form"/>
</g:if>
<g:elseif test="${thresholdType == MeasurementTypeName.URINE}">
    <g:render template="/urineThreshold/form"/>
</g:elseif>
<g:elseif test="${thresholdType == MeasurementTypeName.URINE_GLUCOSE}">
    <g:render template="/urineGlucoseThreshold/form"/>
</g:elseif>
<g:else>
    <g:render template="/numericThreshold/form"/>
</g:else>
