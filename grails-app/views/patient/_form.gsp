<%@ page import="org.opentele.server.core.model.types.PermissionName; org.opentele.server.core.model.types.Sex; org.opentele.server.model.Patient"%>
<%@ page import="org.opentele.server.model.PatientGroup"%>
<%@ page import="org.opentele.server.core.model.types.PatientState"%>
<%@ page import="org.opentele.server.core.util.NumberFormatUtil"%>

<script>
$(document).ready(function() {
	$('input#cpr').change(function() {
		var input_cpr = $('input#cpr').val();

		if (input_cpr.indexOf("-") > 0 && input_cpr.length == 11) {
			try {
				var temp;
				temp = input_cpr.substring(0, input_cpr.indexOf('-'));
				input_cpr = temp + input_cpr.substring(input_cpr.indexOf('-')+1, input_cpr.length)
			} catch (err) {
			}
		}
		if (input_cpr.length == 10 && Number(input_cpr) != 'NaN') {
			if (input_cpr%2==0) {
				$('select#sex').attr('value', 'FEMALE')
			} else {
				$('select#sex').attr('value', 'MALE')
			}			
		}
	});
});
</script>


<div class="fieldcontain ${hasErrors(bean: patientInstance, field: 'user.username', 'error')} required"
	data-tooltip="${message(code: 'tooltip.patient.edit.username')}">
	<label for="username">
        <g:message code="patient.username.label" />
        <span class="required-indicator">*</span>
	</label>
	<g:if test="${patientInstance?.user}">
		<g:textField name="username" value="${patientInstance?.user?.username}" readonly="readonly"/>
	</g:if>
</div>
<g:if test="${patientInstance?.user?.cleartextPassword}">
    <div class="fieldcontain ${hasErrors(bean: patientInstance?.user, field: 'cleartextPassword', 'error')} required"
    	data-tooltip="${message(code: 'tooltip.patient.create.cleartextPassword')}">

    	<label for="cleartextPassword">
            <g:message code="patient.cleartextPassword.label" /> <span class="required-indicator">*</span>
    	</label>
    	<g:textField name="cleartextPassword" value="${patientInstance?.user?.cleartextPassword}"/>
     </div>
</g:if>
<g:else>
    <div class="fieldcontain $required" data-tooltip="${message(code: 'tooltip.patient.edit.password')}">

    	<label for="password">
            <g:message code="patient.password.label" /> <span class="required-indicator">*</span>
    	</label>
    	<g:textField name="password"  value="${message(code: 'patient.password.set-by-user')}" readonly="readonly"/>
    </div>
</g:else>

<div class="fieldcontain ${hasErrors(bean: patientInstance, field: 'cpr', 'error')} required" data-tooltip="${message(code: 'tooltip.patient.create.SSN')}">
	<label for="cpr">
        <g:message code="patient.cpr.label" />
        <span class="required-indicator">*</span>
	</label>
	<g:textField name="cpr" value="${patientInstance?.cpr}" />
</div>


<div class="fieldcontain ${hasErrors(bean: patientInstance, field: 'firstName', 'error')} required">
	<label for="firstName">
        <g:message code="patient.firstName.label" />
        <span class="required-indicator">*</span>
	</label>
	<g:textField name="firstName" value="${patientInstance?.firstName}" />
</div>

<div class="fieldcontain ${hasErrors(bean: patientInstance, field: 'lastName', 'error')} required">
	<label for="lastName">
        <g:message code="patient.lastName.label" />
        <span class="required-indicator">*</span>
	</label>
	<g:textField name="lastName" value="${patientInstance?.lastName}" />
</div>



<div class="fieldcontain ${hasErrors(bean: patientInstance, field: 'sex', 'error')} required" data-tooltip="${message(code: 'tooltip.patient.create.state')}">
    <label for="sex">
        <g:message code="patient.sex.label" />
        <span class="required-indicator">*</span>
    </label>
    <g:select name="sex"
              from="${[Sex.UNKNOWN, Sex.FEMALE, Sex.MALE]}"
              valueMessagePrefix="enum.sex" required=""
              value="${patientInstance?.sex}" />
</div>

<div class="fieldcontain ${hasErrors(bean: patientInstance, field: 'state', 'error')} required" data-tooltip="${message(code: 'tooltip.patient.create.state')}">
	<label for="state">
        <g:message code="patient.status" />
        <span class="required-indicator">*</span>
	</label>
	<g:select name="state"
		from="${org.opentele.server.core.model.types.PatientState.valuesForPersisting}"
		valueMessagePrefix="enum.patientstate" required=""
		value="${patientInstance?.state}" />
</div>

<g:if test="${showDueDate}">
    <div class="fieldcontain ${hasErrors(bean: patientInstance, field: 'dueDate', 'error')} " data-tooltip="${message(code: 'tooltip.patient.create.dueDate')}">
        <label for="dueDate">
            <g:message code="patient.dueDate.label" />
        </label>
        <jq:datePicker default="none" name="dueDate" precision="day" years="${2014..2050}" value="${patientInstance?.dueDate}" noSelection="['':'']"/>
    </div>
</g:if>

<div class="fieldcontain ${hasErrors(bean: patientInstance, field: 'address', 'error')} required">
	<label for="address">
        <g:message code="patient.address.label" />
        <span class="required-indicator">*</span>
	</label>
	<g:textField name="address" value="${patientInstance?.address}" />
</div>

<div class="fieldcontain ${hasErrors(bean: patientInstance, field: 'postalCode', 'error')} required">
	<label for="postalCode">
        <g:message code="patient.postalCode.label" />
        <span class="required-indicator">*</span>
	</label>
	<g:textField name="postalCode" value="${patientInstance?.postalCode}" />
</div>

<div class="fieldcontain ${hasErrors(bean: patientInstance, field: 'city', 'error')} required">
	<label for="city">
        <g:message code="patient.city.label" />
        <span class="required-indicator">*</span>
	</label>
	<g:textField name="city" value="${patientInstance?.city}" />
</div>

<div class="fieldcontain ${hasErrors(bean: patientInstance, field: 'phone', 'error')} required">
	<label for="phone">
        <g:message code="patient.phone.label" />
	</label>
	<g:textField name="phone" value="${patientInstance?.phone}" />
</div>

<div class="fieldcontain ${hasErrors(bean: patientInstance, field: 'mobilePhone', 'error')} required">
	<label for="mobilePhone">
        <g:message code="patient.mobilePhone.label" />
	</label>
	<g:textField name="mobilePhone" value="${patientInstance?.mobilePhone}" />
</div>

<div class="fieldcontain ${hasErrors(bean: patientInstance, field: 'email', 'error')} required">
	<label for="email">
        <g:message code="patient.email.label" />
	</label>
	<g:textField name="email" value="${patientInstance?.email}" />
</div>

<g:if test="${messagingEnabled}">
    <div class="fieldcontain noborder ${hasErrors(bean: patientInstance, field: 'noAlarmIfUnreadMessagesToPatient', 'error')}">
        <label for="noAlarmIfUnreadMessagesToPatient">
            <g:message code="patient.noAlarmIfUnreadMessagesToPatient.label"/>
        </label>
        <g:checkBox name="noAlarmIfUnreadMessagesToPatient" value="${patientInstance?.noAlarmIfUnreadMessagesToPatient}" />
    </div>
</g:if>


<div class="fieldcontain ${hasErrors(bean: patientInstance, field: 'group', 'error')} required" data-tooltip="${message(code: 'tooltip.patient.create.group')}">
	<label for="groupid">
        <g:message code="patient.group.label" />
	</label>

	<g:select name="groupid" from="${groups}" noSelection="${['':"..."]}"
		optionKey="id" multiple="multiple"
		value="${patientInstance*.patient2PatientGroups?.patientGroup?.id?.flatten()}" />
</div>

<div class="fieldcontain ${hasErrors(bean: patientInstance, field: 'comment', 'error')}">
	<label for="comment">
        <g:message code="patient.comment.label" />
	</label>
	<g:textArea name="comment" value="${patientInstance?.comment}" />
</div>

<div class="fieldcontain ${hasErrors(bean: patientInstance, field: 'dataResponsible', 'error')}">
    <label>
        <g:message code="responsible.patient.group.label" />
    </label>
    <g:fieldValue bean="${patientInstance}" field="dataResponsible" />
</div>
<sec:ifAnyGranted roles="${PermissionName.SET_PATIENT_RESPONSIBILITY}">
<div class="fieldcontain">
    <ul>
        <li class="buttons add">
            <g:link controller="patient" action="editResponsability" params="['id': patientInstance?.id]" data-tooltip="${message(code: 'tooltip.patient.responsible.group')}">
                ${message(code: 'responsible.patient.group.edit.label')}
            </g:link>
        </li>
    </ul>
</div>
</sec:ifAnyGranted>
<g:if test="${patientInstance.id}">
	<div class="fieldcontain ${hasErrors(bean: patientInstance, field: 'nextOfKin', 'error')} ">
		<label>
            <g:message code="patient.nextOfKin" />
		</label>
        <tmpl:nextOfKin/>
	</div>
</g:if>

