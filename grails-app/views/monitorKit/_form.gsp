<%@ page import="org.opentele.server.model.MonitorKit"%>

<div class="fieldcontain ${hasErrors(bean: monitorKitInstance, field: 'name', 'error')} ">
	<label for="name">
        <g:message code="monitorKit.name.label"	default="Name" />
	</label>
	<g:textField name="name" value="${monitorKitInstance?.name}" />
</div>

<div class="fieldcontain ${hasErrors(bean: monitorKitInstance, field: 'patient', 'error')} ">
	<label for="patient">
        <g:message code="monitorKit.patient.label" default="Patient" />
	</label>
	<g:select id="patient" name="patient.id"
		from="${org.opentele.server.model.Patient.list()?.sort(){a,b -> a.toString().compareTo(b.toString())}}"
		optionKey="id" value="${monitorKitInstance?.patient?.id}"
		class="many-to-one" noSelection="['null': '']" />
</div>

<div class="fieldcontain ${hasErrors(bean: monitorKitInstance, field: 'department', 'error')} required">
	<label for="department">
        <g:message code="monitorKit.department.label" default="Department" />
        <span class="required-indicator">*</span>
	</label>
	<g:select id="department" name="department.id"
		from="${org.opentele.server.model.Department.list()?.sort(){a,b -> a.toString().compareTo(b.toString())}}"
		optionKey="id" required=""
		value="${monitorKitInstance?.department?.id}" class="many-to-one" />
</div>
