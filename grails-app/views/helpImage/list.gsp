
<%@ page
		import="org.opentele.server.core.model.types.PermissionName; org.opentele.server.model.HelpImage" %>
<!doctype html>
<html>
<head>
	<meta name="layout" content="main">
	<title><g:message code="helpImage.list.label"/></title>
</head>
<body>
<div class="content scaffold-list" role="main">
	<h1><g:message code="helpImage.list.label"/></h1>
	<g:if test="${flash.message}"><div class="message" role="status">${flash.message}</div></g:if>
	<g:if test="${flash.error}">
		<div class="message_error" style="color: red;">${flash.error}</div>
	</g:if>
	<table>
		<thead>
		<tr>
			<g:sortableColumn property="filename" title="${message(code: 'helpImage.filename.label', default: 'Filename')}" />
			<g:sortableColumn property="dummy" title="${message(code: 'helpImage.downloadHeading.label', default: 'Download image')}" />
		</tr>
		</thead>
		<tbody>
		<g:each in="${helpImageInstanceList}" status="i" var="helpImageInstance">
			<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
				<td><g:link action="show" id="${helpImageInstance.id}">${fieldValue(bean: helpImageInstance, field: "filename")}</g:link></td>
				<td><g:link action="downloadImage" id="${helpImageInstance.id}">${message(code: 'helpImage.download.label', default: 'download')}</g:link></td>
			</tr>
		</g:each>
		</tbody>
	</table>
	<div class="pagination">
		<g:paginate total="${helpImageInstanceTotal}" />
	</div>

	<sec:ifAnyGranted roles="${PermissionName.QUESTIONNAIRE_CREATE}">
		<fieldset class="buttons">
			<g:link class="create" action="create" controller="helpImage">
				<g:message code="helpImage.create.label" args="${[g.message(code: 'helpImage.shortname', default: 'image')]}"/>
			</g:link>
		</fieldset>
	</sec:ifAnyGranted>
</div>
</body>
</html>
