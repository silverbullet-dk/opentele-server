<tr class="thresholds">
    <th>${text ?: ''} <g:message code="bloodPressureThreshold.systolic"/></th>
    <td>
        <g:field type="text" name="${prefix ? "${prefix}." : ''}systolicAlertHigh"
                 value="${formatNumber(number: threshold.systolicAlertHigh, format: "0.0")}"/>
         <r:img uri="/images/remove-icon.png"/>
    </td>
    <td>
        <g:field type="text" name="${prefix ? "${prefix}." : ''}systolicWarningHigh"
                 value="${formatNumber(number: threshold.systolicWarningHigh, format: "0.0")}"/>
         <r:img uri="/images/remove-icon.png"/>
    </td>
    <td>
        <g:field type="text" name="${prefix ? "${prefix}." : ''}systolicWarningLow"
                 value="${formatNumber(number: threshold.systolicWarningLow, format: "0.0")}"/>
         <r:img uri="/images/remove-icon.png"/>
    </td>
    <td>
        <g:field type="text" name="${prefix ? "${prefix}." : ''}systolicAlertLow"
                 value="${formatNumber(number: threshold.systolicAlertLow, format: "0.0")}"/>
         <r:img uri="/images/remove-icon.png"/>
    </td>
</tr>
<tr class="thresholds">
    <th>${text ?: ''} <g:message code="bloodPressureThreshold.diastolic"/></th>
    <td>
        <g:field type="text" name="${prefix ? "${prefix}." : ''}diastolicAlertHigh"
                 value="${formatNumber(number: threshold.diastolicAlertHigh, format: "0.0")}"/>
         <r:img uri="/images/remove-icon.png"/>
    </td>
    <td>
        <g:field type="text" name="${prefix ? "${prefix}." : ''}diastolicWarningHigh"
                 value="${formatNumber(number: threshold.diastolicWarningHigh, format: "0.0")}"/>
         <r:img uri="/images/remove-icon.png"/>
    </td>
    <td>
        <g:field type="text" name="${prefix ? "${prefix}." : ''}diastolicWarningLow"
                 value="${formatNumber(number: threshold.diastolicWarningLow, format: "0.0")}"/>
         <r:img uri="/images/remove-icon.png"/>
    </td>
    <td>
        <g:field type="text" name="${prefix ? "${prefix}." : ''}diastolicAlertLow"
                 value="${formatNumber(number: threshold.diastolicAlertLow, format: "0.0")}"/>
         <r:img uri="/images/remove-icon.png"/>
    </td>
</tr>
