<%@ page import="org.opentele.server.core.model.types.PermissionName; org.opentele.server.core.model.types.MeasurementTypeName" %>
<tr>
    <td>
        <g:message code="threshold.${threshold.type.name}"/> <g:message code="threshold.${threshold.type.name}.${field}" default=""/>
    </td>
    <td><g:formatThreshold threshold="${threshold}" field="${field}" level="AlertHigh" /></td>
    <td><g:formatThreshold threshold="${threshold}" field="${field}" level="WarningHigh" /></td>
    <td><g:formatThreshold threshold="${threshold}" field="${field}" level="WarningLow" /></td>
    <td><g:formatThreshold threshold="${threshold}" field="${field}" level="AlertLow" /></td>

    <g:unless test="${readonly}">
        <sec:ifAnyGranted roles="${writePermission},${deletePermission}">
            <td class="table-label-td buttons actions" rowspan="${rowspan ?: 1}">
                <sec:ifAnyGranted roles="${writePermission}">
                    <g:link class="edit" controller="${controllerName}" action="edit"
                            id="${threshold.id}">&nbsp;</g:link>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="${deletePermission}">
                    <g:link class="delete" action="removeThreshold" id="${parent.id}"
                            params="[threshold: threshold.id]">&nbsp;</g:link>
                </sec:ifAnyGranted>

            </td>
        </sec:ifAnyGranted>
    </g:unless>
</tr>
