<%@ page import="org.opentele.server.model.HelpImage" %>
<!doctype html>
<html>
<head>
	<meta name="layout" content="main">
	<g:set var="entityName" value="${message(code: 'helpImage.shortname', default: 'HelpImage')}" />
	<title><g:message code="default.create.label" args="[entityName]" /></title>
</head>
<body>
<div class="content scaffold-create" role="main">
	<h1><g:message code="helpImage.create.label" args="${[g.message(code: 'helpImage.shortname', default: 'image')]}"/></h1>
	<g:if test="${flash.message}"><div class="message" role="status">${flash.message}</div></g:if>
	<g:if test="${flash.error}">
		<div class="message_error" style="color: red;">${flash.error}</div>
	</g:if>
	<g:uploadForm action="upload">
		<fieldset class="form">
			<input type="file" name="file" />
		</fieldset>
		<fieldset class="buttons">
			<g:submitButton name="upload" class="save" value="Upload" />
		</fieldset>
	</g:uploadForm>
</div>
</body>
</html>
