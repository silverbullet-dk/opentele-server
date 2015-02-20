
<%@ page import="org.opentele.server.model.HelpImage" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'helpImage.shortname', default: 'HelpImage')}" />
		<title><g:message code="default.show.label" args="[entityName]" /></title>
	</head>
	<body>
	
		<div id="show-helpImage" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:if test="${flash.error}">
				<div class="message_error" style="color: red;">${flash.error}</div>
			</g:if>
			<ol class="property-list helpImage">
			
				<g:if test="${helpImageInstance?.filename}">
				<li class="fieldcontain">
					<span id="filename-label" class="property-label"><g:message code="helpImage.filename.label" default="Filename" /></span>
					<span class="property-value" aria-labelledby="filename-label">
						<g:link action="downloadImage" id="${helpImageInstance.id}">${helpImageInstance.filename}</g:link>
					</span>
					
				</li>
				</g:if>
			
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${helpImageInstance?.id}" />
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
