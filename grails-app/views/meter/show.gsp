
<%@ page
	import="org.opentele.server.core.model.types.PermissionName; org.opentele.server.provider.constants.Constants; org.opentele.server.model.Meter"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'meter.label', default: 'Meter')}" />
<title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
	<div id="show-meter" class="content scaffold-show" role="main">
		<h1><g:message code="default.show.label" args="[entityName]" /></h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>

		<ol class="property-list meter">
			<li class="fieldcontain">
                <span id="active-label" class="property-label">
                    <g:message code="meter.active.label" default="Active" />
                </span>
                <span class="property-value" aria-labelledby="active-label">
                    <g:formatBoolean
						boolean="${meterInstance?.active}"
						true="${message(code: 'default.yesno.true')}"
						false="${message(code: 'default.yesno.false')}" />
                </span>
            </li>
			<g:if test="${meterInstance?.meterId}">
				<li class="fieldcontain">
                    <span id="meterId-label" class="property-label">
                        <g:message code="meter.meterId.label" default="Meter Id" />
                    </span>
                    <span class="property-value" aria-labelledby="meterId-label">
                        <g:fieldValue bean="${meterInstance}" field="meterId" />
                    </span>
                </li>
			</g:if>

			<g:if test="${meterInstance?.model}">
				<li class="fieldcontain">
                    <span id="model-label" class="property-label">
                        <g:message code="meter.model.label" default="Model" />
                    </span>
                    <span class="property-value" aria-labelledby="model-label">
                        <g:fieldValue bean="${meterInstance}" field="model" />
                    </span>
                </li>
			</g:if>

            <g:if test="${meterInstance?.meterType}">
                <li class="fieldcontain">
                    <span id="meterType-label" class="property-label">
                        <g:message code="meter.meterType.label" default="Meter Type"/>
                    </span>
                    <span class="property-value" aria-labelledby="meterType-label">
                        ${message(code: 'enum.meterType.' + meterInstance?.meterType)}
                    </span>
                </li>
            </g:if>

            <g:if test="${meterInstance?.patient}">
				<li class="fieldcontain">
                    <span id="patient-label" class="property-label">
                        <g:message code="meter.patient.label" default="Patient" />
                    </span>
                    <span class="property-value" aria-labelledby="patient-label">
                        <g:link controller="patient" action="show" id="${meterInstance?.patient?.id}">
							${meterInstance?.patient?.encodeAsHTML()}
						</g:link>
                    </span>
                </li>
			</g:if>

			<g:if test="${meterInstance?.monitorKit}">
				<li class="fieldcontain">
                    <span id="monitorKit-label" class="property-label">
                        <g:message code="meter.monitorKit.label" default="Monitor Kit" />
                    </span>
                    <span class="property-value" aria-labelledby="monitorKit-label">
                        <g:link controller="monitorKit" action="show" id="${meterInstance?.monitorKit?.id}">
							${meterInstance?.monitorKit?.encodeAsHTML()}
						</g:link>
                    </span>
                </li>
			</g:if>
		</ol>

        <g:form>
            <fieldset class="buttons">
                <g:hiddenField name="id" value="${meterInstance?.id}" />
                <sec:ifAnyGranted roles="${PermissionName.METER_WRITE}">
                    <g:link class="edit" action="edit" id="${meterInstance?.id}">
                        <g:message code="default.button.edit.label" />
                    </g:link>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="${PermissionName.METER_DELETE}">
                    <g:actionSubmit class="delete" action="delete"
                        value="${message(code: 'default.button.delete.label')}"
                        onclick="return confirm('${message(code: 'default.button.delete.confirm.message')}');" />
                </sec:ifAnyGranted>
            </fieldset>
        </g:form>
	</div>
</body>
</html>
