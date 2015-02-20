<%@ page import="org.opentele.server.core.model.types.PermissionName" %>
<table>
    <thead>
    <tr>
        <th><g:message code="patient.nextOfKin.name.label"/></th>
        <th><g:message code="patient.nextOfKin.relation.label"/></th>
        <th><g:message code="patient.nextOfKin.phone.label"/></th>
        <g:unless test="${readonly}">
            <sec:ifAnyGranted
                    roles="${PermissionName.PATIENT_WRITE},${PermissionName.NEXT_OF_KIN_WRITE},${PermissionName.NEXT_OF_KIN_DELETE}">
                <th class="actions">
                    <g:message code="patient.nextOfKin.action.label"/></th>
            </sec:ifAnyGranted>
        </g:unless>
    </tr>
    </thead>
    <tbody>
    <g:if test="${patientInstance?.nextOfKinPersons}">
        <g:each in="${patientInstance?.nextOfKinPersons.sort {it.id} }" var="nextOfKin">
            <tr>
                <g:set var="nextOfKinTooltip">
                    <g:fieldValue bean="${nextOfKin}" field="name"/>
                    <g:if test="${nextOfKin.address}">
                        <p><strong><g:message code="nextOfKinPerson.address.label"/></strong>:<br/>
                            <g:fieldValue field="address" bean="${nextOfKin}"/>,
                            <g:fieldValue field="city" bean="${nextOfKin}"/>
                        </p>
                    </g:if>
                    <g:if test="${nextOfKin.note}">
                        <p><strong><g:message code="nextOfKinPerson.note.label"/></strong>:<br/>
                            ${fieldValue(bean: nextOfKin, field: 'note').replaceAll(/\n/,'<br/>')}
                            </p>
                    </g:if>
                </g:set>
                <td data-tooltip="${nextOfKinTooltip.encodeAsHTML()}">
                    <g:link controller="nextOfKinPerson" action="show" id="${nextOfKin.id}">
                        <g:fieldValue field="name" bean="${nextOfKin}"/>
                    </g:link>
                </td>
                <td><g:fieldValue field="relation" bean="${nextOfKin}"/></td>
                <td><g:fieldValue field="phone" bean="${nextOfKin}"/></td>
                <g:unless test="${readonly}">

                    <sec:ifAnyGranted
                            roles="${PermissionName.PATIENT_WRITE},${PermissionName.NEXT_OF_KIN_WRITE},${PermissionName.NEXT_OF_KIN_DELETE}">
                        <td class="table-label-td buttons actions" rowspan="${rowspan ?: 1}">
                            <sec:ifAnyGranted roles="${PermissionName.NEXT_OF_KIN_WRITE}">
                                <g:link class="edit" controller="nextOfKinPerson" action="edit"
                                        id="${nextOfKin.id}">&nbsp;</g:link>
                            </sec:ifAnyGranted>
                            <sec:ifAnyGranted roles="${PermissionName.NEXT_OF_KIN_DELETE}">
                                    <g:link action="removeNextOfKin" class="delete" id="${nextOfKin.patient.id}" params="[nextOfKin: nextOfKin.id]"
                                                    onclick="return confirm('${message(code: 'default.button.delete.confirm.message')}');">&nbsp;</g:link>
                            </sec:ifAnyGranted>

                        </td>
                    </sec:ifAnyGranted>
                </g:unless>
            </tr>
        </g:each>

    </g:if>
    <g:else>
        <tr><td colspan="10">
            <g:message code="patient.nextOfKin.none"/>
        </td></tr>
    </g:else>
    </tbody>
    <g:unless test="${readonly}">
        <tfoot>
        <tr>
            <td colspan="100">
                <sec:ifAnyGranted roles="${PermissionName.NEXT_OF_KIN_CREATE}">
                    <fieldset class="buttons">
                        <g:link controller="nextOfKinPerson" action="create"
                                params="['patientId': patientInstance.id]">
                            ${message(code: 'patient.addNextOfKin')}
                        </g:link>
                    </fieldset>
                </sec:ifAnyGranted>
            </td>
        </tr>
        </tfoot>
    </g:unless>
</table>
