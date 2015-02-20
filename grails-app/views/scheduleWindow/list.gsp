
<%@ page import="org.opentele.server.model.ScheduleWindow" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<title><g:message code="scheduleWindow.label.list" /></title>
	</head>
	<body>
		<div id="list-scheduleWindow" class="content scaffold-list" role="main">
			<h1><g:message code="scheduleWindow.label.list" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="scheduleType" title="${message(code: 'scheduleWindow.scheduleType.label', default: 'Schedule Type')}" />
					
						<g:sortableColumn property="windowSizeMinutes" title="${message(code: 'scheduleWindow.windowSizeMinutes.label', default: 'Window Size')}" />
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${scheduleWindowInstanceList}" status="i" var="scheduleWindowInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${scheduleWindowInstance.id}"><otformat:prettyStringForScheduleType  message="${scheduleWindowInstance.scheduleType}"/></g:link></td>
					
						<td><scheduleWindow:prettyformat scheduleType="${scheduleWindowInstance.scheduleType}"/></td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
			<div class="pagination">
				<g:paginate total="${scheduleWindowInstanceTotal}" />
			</div>
		</div>
	</body>
</html>
