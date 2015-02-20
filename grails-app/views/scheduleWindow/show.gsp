
<%@ page import="org.opentele.server.model.ScheduleWindow" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<title><g:message code="scheduleWindow.label.show" /></title>
	</head>
	<body>
	
		<div id="show-scheduleWindow" class="content scaffold-show" role="main">
			<h1><g:message code="scheduleWindow.label.show" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list scheduleWindow">
			
				<g:if test="${scheduleWindowInstance?.scheduleType}">
				<li class="fieldcontain">
					<span id="scheduleType-label" class="property-label"><g:message code="scheduleWindow.scheduleType.label" default="Schedule Type" /></span>
					
					<span class="property-value" aria-labelledby="scheduleType-label"><otformat:prettyStringForScheduleType  message="${scheduleWindowInstance.scheduleType}"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${scheduleWindowInstance?.windowSizeMinutes}">
				<li class="fieldcontain">
					<span id="windowSizeMinutes-label" class="property-label"><g:message code="scheduleWindow.windowSizeMinutes.label" default="Window Size Minutes" /></span>
					
						<span class="property-value" aria-labelledby="windowSizeMinutes-label"><scheduleWindow:prettyformat scheduleType="${scheduleWindowInstance.scheduleType}"/></span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${scheduleWindowInstance?.id}" />
					<g:link class="edit" action="edit" id="${scheduleWindowInstance?.id}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
