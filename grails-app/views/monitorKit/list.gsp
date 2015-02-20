
<%@ page
	import="org.opentele.server.core.model.types.PermissionName; org.opentele.server.core.model.types.PermissionName; org.opentele.server.provider.constants.Constants; org.opentele.server.model.MonitorKit"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'monitorKit.label', default: 'MonitorKit')}" />
<title><g:message code="default.list.label" args="[entityName]" /></title>
</head>

<body>
	<div id="list-monitorKit" class="content scaffold-list" role="main">
		<h1><g:message code="default.list.label" args="[entityName]" /></h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>

		<table>
			<thead>
				<tr>
					<g:sortableColumn property="name" title="${message(code: 'monitorKit.name.label')}" />
					<th><g:message code="monitorKit.patient.label" /></th>
					<th><g:message code="monitorKit.department.label" /></th>
				</tr>
			</thead>
			<tbody>
				<g:each in="${monitorKitInstanceList}" status="i" var="monitorKitInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<td>
                            <g:link action="show" id="${monitorKitInstance.id}">
								${fieldValue(bean: monitorKitInstance, field: "name")}
							</g:link>
                        </td>
						<td>${fieldValue(bean: monitorKitInstance, field: "patient")}</td>
						<td>${fieldValue(bean: monitorKitInstance, field: "department")}</td>
					</tr>
				</g:each>
			</tbody>
		</table>
		<div class="pagination">
			<g:paginate total="${monitorKitInstanceTotal}" />
		</div>
	</div>

    <sec:ifAnyGranted roles="${org.opentele.server.core.model.types.PermissionName.MONITOR_KIT_CREATE}">
        <fieldset class="buttons">
            <g:link class="create" action="create"
                data-tooltip="${message(code: 'tooltip.patient.monitorKit.add')}">
                <g:message code="default.new.label" args="[entityName]" />
            </g:link>
        </fieldset>
    </sec:ifAnyGranted>
</body>
</html>
