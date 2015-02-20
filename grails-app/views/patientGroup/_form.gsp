<%@ page import="org.opentele.server.model.PatientGroup" %>

<div class="fieldcontain ${hasErrors(bean: patientGroupInstance, field: 'name', 'error')} ">
	<label for="name">
		<g:message code="patientGroup.name.label" default="Name" /> <span class="required-indicator">*</span>
	</label>
	<g:textField name="name" value="${patientGroupInstance?.name}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: patientGroupInstance, field: 'department', 'error')} required">
	<label for="department">
		<g:message code="patientGroup.department.label" default="Department" />
		<span class="required-indicator">*</span>
	</label>
	<g:select id="department" name="department.id" from="${org.opentele.server.model.Department.list()}" optionKey="id" required="" value="${patientGroupInstance?.department?.id}" class="many-to-one"/>
</div>

<div class="fieldcontain noborder ${hasErrors(bean: patientGroupInstance, field: 'disableMessaging', 'error')}">
    <label for="disableMessaging">
        <g:message code="patientGroup.disable_messaging.label"/>
    </label>
    <g:checkBox name="disableMessaging" value="${patientGroupInstance?.disableMessaging}" />
</div>

<div class="fieldcontain noborder ${hasErrors(bean: patientGroupInstance, field: 'showGestationalAge', 'error')}">
    <label for="showGestationalAge">
        <g:message code="patientGroup.showGestationalAge.label"/>
    </label>
    <g:checkBox name="showGestationalAge" value="${patientGroupInstance?.showGestationalAge}" />
</div>

<g:if test="${grailsApplication.config.running.ctg.messaging.enabled}">
    <div class="fieldcontain noborder ${hasErrors(bean: patientGroupInstance, field: 'showRunningCtgMessaging', 'error')}">
        <label for="showRunningCtgMessaging">
            <g:message code="patientGroup.showRunningCtgMessaging.label"/>
        </label>
        <g:checkBox name="showRunningCtgMessaging" value="${patientGroupInstance?.showRunningCtgMessaging}" />
    </div>
</g:if>