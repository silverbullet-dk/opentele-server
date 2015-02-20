<%@ page import="org.opentele.server.model.NextOfKinPerson"%>

<div class="fieldcontain ${hasErrors(bean: nextOfKinPersonInstance, field: 'firstName', 'error')} ">
	<label for="firstName" data-tooltip="${message(code: 'nextOfKinPerson.create.tooltip')}">
        <g:message code="nextOfKinPerson.firstName.label" />
	</label>
	<g:textField name="firstName" value="${nextOfKinPersonInstance?.firstName}" />
</div>

<div class="fieldcontain ${hasErrors(bean: nextOfKinPersonInstance, field: 'lastName', 'error')} ">
	<label for="lastName"
           data-tooltip="${message(code: 'nextOfKinPerson.create.tooltip')}">
        <g:message code="nextOfKinPerson.lastName.label" />
	</label>
	<g:textField name="lastName" value="${nextOfKinPersonInstance?.lastName}" />
</div>

<div class="fieldcontain ${hasErrors(bean: nextOfKinPersonInstance, field: 'relation', 'error')} ">
	<label for="relation"
           data-tooltip="${message(code: 'nextOfKinPerson.create.tooltip')}">
        <g:message code="nextOfKinPerson.relation.label" />
	</label>
	<g:textField name="relation" value="${nextOfKinPersonInstance?.relation}" />
</div>

<div class="fieldcontain ${hasErrors(bean: nextOfKinPersonInstance, field: 'phone', 'error')} ">
	<label for="phone"
           data-tooltip="${message(code: 'nextOfKinPerson.create.tooltip')}">
        <g:message code="nextOfKinPerson.phone.label" />
	</label>
	<g:textField name="phone" value="${nextOfKinPersonInstance?.phone}" />
</div>

<div class="fieldcontain ${hasErrors(bean: nextOfKinPersonInstance, field: 'address', 'error')} ">
	<label for="address"
           data-tooltip="${message(code: 'nextOfKinPerson.create.tooltip')}">
        <g:message code="nextOfKinPerson.address.label" />
	</label>
	<g:textField name="address" value="${nextOfKinPersonInstance?.address}" />
</div>

<div class="fieldcontain ${hasErrors(bean: nextOfKinPersonInstance, field: 'city', 'error')} ">
	<label for="city"
           data-tooltip="${message(code: 'nextOfKinPerson.create.tooltip')}">
        <g:message code="nextOfKinPerson.city.label" />
	</label>
	<g:textField name="city" value="${nextOfKinPersonInstance?.city}" />
</div>

<div class="fieldcontain ${hasErrors(bean: nextOfKinPersonInstance, field: 'note', 'error')} ">
	<label for="note"
           data-tooltip="${message(code: 'nextOfKinPerson.create.tooltip')}">
        <g:message code="nextOfKinPerson.note.label" />
	</label>
	<g:textArea name="note" value="${nextOfKinPersonInstance?.note}" />
</div>


