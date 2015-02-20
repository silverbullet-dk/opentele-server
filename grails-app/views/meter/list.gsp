
<%@ page
	import="org.opentele.server.core.model.types.PermissionName; org.opentele.server.provider.constants.Constants; org.opentele.server.model.Meter"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'meter.label', default: 'Meter')}" />
<title><g:message code="default.list.label" args="[entityName]" /></title>
</head>

<body>
	<div id="list-meter" class="content scaffold-list" role="main">
		<h1><g:message code="default.list.label" args="[entityName]" /></h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>

		<table>
			<thead>
				<tr>
					<g:sortableColumn property="active" title="${message(code: 'meter.active.label')}" />
					<g:sortableColumn property="meterId" title="${message(code: 'meter.meterId.label')}" />
					<g:sortableColumn property="model" title="${message(code: 'meter.model.label')}" />
					<th><g:message code="meter.meterType.label" /></th>
					<th><g:message code="meter.patient.label" /></th>
					<th><g:message code="meter.monitorKit.label" /></th>
				</tr>
			</thead>
			<tbody>
				<g:each in="${meterInstanceList}" status="i" var="meterInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<td>
                            <g:link action="show" id="${meterInstance.id}">
								<g:message code="default.yesno.${fieldValue(bean: meterInstance, field: "active")}" />
							</g:link>
                        </td>
						<td>${fieldValue(bean: meterInstance, field: "meterId")}</td>
						<td>${fieldValue(bean: meterInstance, field: "model")}</td>
						<td>${message(code:'enum.meterType.'+fieldValue(bean: meterInstance, field: "meterType"))}</td>
						<td>${fieldValue(bean: meterInstance, field: "patient")}</td>
						<td>${fieldValue(bean: meterInstance, field: "monitorKit")}</td>
					</tr>
				</g:each>
			</tbody>
		</table>
		<div class="pagination">
			<g:paginate total="${meterInstanceTotal}" />
		</div>
		<sec:ifAnyGranted roles="${PermissionName.METER_CREATE}">
			<fieldset class="buttons">
				<g:link class="create" action="create"
					data-tooltip="${message(code: 'tooltip.patient.meter.add')}">
					<g:message code="default.new.label" args="[entityName]" />
				</g:link>
			</fieldset>
		</sec:ifAnyGranted>

	</div>
</body>
</html>
