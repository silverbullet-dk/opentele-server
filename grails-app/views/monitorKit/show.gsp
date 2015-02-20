<%@ page import="org.opentele.server.core.model.types.PermissionName; org.opentele.server.core.model.types.PermissionName; org.opentele.server.core.model.types.PermissionName; org.opentele.server.core.model.types.PermissionName; org.opentele.server.core.model.types.PermissionName; org.opentele.server.core.model.types.PermissionName; org.opentele.server.provider.constants.Constants; org.opentele.server.model.MonitorKit"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'monitorKit.label', default: 'MonitorKit')}" />
<title><g:message code="default.show.label" args="[entityName]" /></title>
</head>

<body>
	<div id="show-monitorKit" class="content scaffold-show" role="main">
		<h1><g:message code="default.show.label" args="[entityName]" /></h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>

		<ol class="property-list monitorKit">
			<g:if test="${monitorKitInstance?.name}">
				<li class="fieldcontain">
                    <span id="name-label" class="property-label">
                        <g:message code="monitorKit.name.label" default="Name" />
                    </span>
                    <span class="property-value" aria-labelledby="name-label">
                        <g:fieldValue bean="${monitorKitInstance}" field="name" />
                    </span>
                </li>
			</g:if>

			<g:if test="${monitorKitInstance?.patient}">
				<li class="fieldcontain">
                    <span id="patient-label" class="property-label">
                        <g:message code="monitorKit.patient.label" default="Patient" />
                    </span>
                    <span class="property-value" aria-labelledby="patient-label">
                        <g:link controller="patient" action="show" id="${monitorKitInstance?.patient?.id}">
							${monitorKitInstance?.patient?.encodeAsHTML()}
						</g:link>
                    </span>
                </li>
			</g:if>

			<g:if test="${monitorKitInstance?.department}">
				<li class="fieldcontain">
                    <span id="department-label" class="property-label">
                        <g:message code="monitorKit.department.label" default="Department" />
                    </span>
                    <span class="property-value" aria-labelledby="department-label">
						${monitorKitInstance?.department?.encodeAsHTML()}
				    </span>
                </li>
			</g:if>

			<g:if test="${monitorKitInstance?.meters}">
				<li class="fieldcontain">
                    <span id="meters-label" class="property-label">
                        <g:message code="monitorKit.meters.label" default="Meters" />
                    </span>
                    <g:each in="${monitorKitInstance.meters}" var="m">
						<span class="property-value" aria-labelledby="meters-label">
                            <g:link controller="meter" action="show" id="${m.id}">
								${m?.encodeAsHTML()}
							</g:link>
                        </span>
					</g:each>
                </li>
			</g:if>
		</ol>

        <g:form>
            <fieldset class="buttons">
                <g:hiddenField name="id" value="${monitorKitInstance?.id}" />
                <sec:ifAnyGranted roles="${org.opentele.server.core.model.types.PermissionName.MONITOR_KIT_WRITE}">
                    <g:link class="edit" action="edit" id="${monitorKitInstance?.id}">
                        <g:message code="default.button.edit.label" default="Edit" />
                    </g:link>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="${org.opentele.server.core.model.types.PermissionName.MONITOR_KIT_DELETE}">
                    <g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label')}"
                        onclick="return confirm('${message(code: 'confirm.context.msg.removekit')}');"
                        data-tooltip="${message(code: 'tooltip.patient.meter.remove')}" />
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="${org.opentele.server.core.model.types.PermissionName.MONITOR_KIT_WRITE}">
                    <g:link class="edit" action="attachMeter"
                        id="${monitorKitInstance?.id}"
                        data-tooltip="${message(code: 'tooltip.patient.monitorKit.addExistingMeter')}">
                        ${message(code:'default.button.attach.label', args: [message(code: 'meter.label', default: 'Meter')])}
                    </g:link>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="${org.opentele.server.core.model.types.PermissionName.MONITOR_KIT_CREATE}">
                    <g:link class="edit" controller="meter" action="create"
                        params="['monitorKit.id': monitorKitInstance?.id]"
                        data-tooltip="${message(code: 'tooltip.patient.monitorKit.addNewMeter')}">
                        ${message(code: 'default.add.new.label', args: [message(code: 'meter.label', default: 'Meter')])}
                    </g:link>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="${org.opentele.server.core.model.types.PermissionName.MONITOR_KIT_WRITE}">
                    <g:link class="edit" action="removeMeter"
                        id="${monitorKitInstance?.id}"
                        data-tooltip="${message(code: 'tooltip.patient.monitorKit.removeMeter')}">
                        ${message(code:'default.button.remove.label', args: [message(code: 'meter.label', default: 'Meter')])}
                    </g:link>
                </sec:ifAnyGranted>
            </fieldset>
        </g:form>
	</div>
</body>
</html>
