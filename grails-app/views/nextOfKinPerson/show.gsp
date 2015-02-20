
<%@ page import="org.opentele.server.core.model.types.PermissionName; org.opentele.server.model.NextOfKinPerson"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'patient2NextOfKinPerson.label', default: 'NextOfKinPerson')}" />
<title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
	<div id="show-nextOfKinPerson" class="content scaffold-show" role="main">
		<h1><g:message code="nextofkin.label"/></h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>

		<ol class="property-list nextOfKinPerson">
			<g:if test="${nextOfKinPersonInstance?.firstName}">
				<li class="fieldcontain">
                    <span id="firstName-label" class="property-label">
                        <g:message code="nextOfKinPerson.firstName.label" />
                    </span>
                    <span class="property-value" aria-labelledby="firstName-label">
                        <g:fieldValue bean="${nextOfKinPersonInstance}" field="firstName" />
                    </span>
                </li>
			</g:if>

			<g:if test="${nextOfKinPersonInstance?.lastName}">
				<li class="fieldcontain">
                    <span id="lastName-label" class="property-label">
                        <g:message code="nextOfKinPerson.lastName.label" />
                    </span>
                    <span class="property-value" aria-labelledby="lastName-label">
                        <g:fieldValue bean="${nextOfKinPersonInstance}" field="lastName" />
                    </span>
                </li>
			</g:if>

			<g:if test="${nextOfKinPersonInstance?.phone}">
				<li class="fieldcontain">
                    <span id="phone-label" class="property-label">
                        <g:message code="nextOfKinPerson.phone.label" />
                    </span>
                    <span class="property-value" aria-labelledby="phone-label">
                        <g:fieldValue bean="${nextOfKinPersonInstance}" field="phone" />
                    </span>
                </li>
			</g:if>

			<g:if test="${nextOfKinPersonInstance?.address}">
				<li class="fieldcontain">
                    <span id="address-label" class="property-label">
                        <g:message code="nextOfKinPerson.address.label" />
                    </span>
                    <span class="property-value" aria-labelledby="address-label">
                        <g:fieldValue bean="${nextOfKinPersonInstance}" field="address" />
                    </span>
                </li>
			</g:if>

			<g:if test="${nextOfKinPersonInstance?.city}">
				<li class="fieldcontain">
                    <span id="city-label" class="property-label">
                        <g:message code="nextOfKinPerson.city.label" />
                    </span>
                    <span class="property-value" aria-labelledby="city-label">
                        <g:fieldValue bean="${nextOfKinPersonInstance}" field="city" />
                    </span>
                </li>
			</g:if>

			<g:if test="${nextOfKinPersonInstance?.note}">
				<li class="fieldcontain">
                    <span id="note-label" class="property-label">
                        <g:message code="nextOfKinPerson.note.label" />
                    </span>
                    <span class="property-value" aria-labelledby="note-label">
                        <g:fieldValue bean="${nextOfKinPersonInstance}" field="note" />
                    </span>
                </li>
			</g:if>
		</ol>

		<g:form>
			<fieldset class="buttons">
				<g:hiddenField name="id" value="${nextOfKinPersonInstance?.id}" />
                <sec:ifAnyGranted roles="${PermissionName.NEXT_OF_KIN_WRITE}">
                    <g:link class="edit" action="edit"
                        id="${nextOfKinPersonInstance?.id}">
                        <g:message code="default.button.edit.label" default="Edit" />
                    </g:link>
                </sec:ifAnyGranted>
                <g:link class="show" controller="patient" action="show"
                                                    id="${nextOfKinPersonInstance.patient.id}"><g:message code="nextOfKinPerson.backto.patient"/></g:link>
			</fieldset>
		</g:form>
	</div>
</body>
</html>
