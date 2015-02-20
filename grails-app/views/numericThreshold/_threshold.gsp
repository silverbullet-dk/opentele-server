<tr class="thresholds">
    <g:if test="${text}">
        <th>${text}</th>
    </g:if>
    <td>
        <g:field type="text" name="${prefix ? "${prefix}." : ''}alertHigh"
                 value="${formatNumber(number: threshold.alertHigh, format: "0.0")}"/>
        <r:img uri="/images/remove-icon.png"/>
    </td>
    <td>
        <g:field type="text" name="${prefix ? "${prefix}." : ''}warningHigh"
                 value="${formatNumber(number: threshold.warningHigh, format: "0.0")}"/>
        <r:img uri="/images/remove-icon.png"/>
    </td>
    <td>
        <g:field type="text" name="${prefix ? "${prefix}." : ''}warningLow"
                 value="${formatNumber(number: threshold.warningLow, format: "0.0")}"/>
        <r:img uri="/images/remove-icon.png"/>
    </td>
    <td>
        <g:field type="text" name="${prefix ? "${prefix}." : ''}alertLow"
                 value="${formatNumber(number: threshold.alertLow, format: "0.0")}"/>
        <r:img uri="/images/remove-icon.png"/>
    </td>
</tr>
