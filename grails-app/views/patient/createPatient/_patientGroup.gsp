<%@ page import="org.opentele.server.model.PatientGroup" %>
<div class="fieldcontain ${hasErrors(bean: patientInstance, field: 'group', 'error')} required">

    <label for="group" data-tooltip="${message(code: 'tooltip.patient.create.group')}">
        <g:message code="patient.group.label" default="type" />
    </label>

    <g:select name="groupIds"
              from="${patientGroups.sort { it.name }}"
              optionKey="id" multiple="multiple"
              value="${patientInstance.groupIds?.collect{PatientGroup.findById(it)}}"
    />
</div>
