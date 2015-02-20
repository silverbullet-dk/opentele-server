<%@ page import="org.opentele.server.model.questionnaire.QuestionnaireGroup2QuestionnaireHeader" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'questionnaireGroup2QuestionnaireHeader.label', default: 'QuestionnaireGroup2QuestionnaireHeader')}" />
		<title><g:message code="default.edit" args="[entityName]" /></title>
	</head>
	<body>
		<div id="edit-questionnaireGroup2QuestionnaireHeader" class="content scaffold-edit" role="main">
			<h1><g:message code="default.edit" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
            <tmpl:/questionnaireSchedule/errors/>

			<g:form method="post" >
				<g:hiddenField name="questionnaireGroup2QuestionnaireHeader.id" value="${questionnaireGroup2QuestionnaireHeader.id}" />
				<g:hiddenField name="version" value="${questionnaireGroup2QuestionnaireHeader.version}" />
				<fieldset class="form">
					<g:render template="form"/>
				</fieldset>
				<fieldset class="buttons">
                    <g:link class="goback" controller="questionnaireGroup" action="show" id="${questionnaireGroupId}">
                        <g:message code="default.button.goback.label2" />
                    </g:link>
					<g:actionSubmit class="save" action="update" value="${message(code: 'default.update', default: 'Update')}" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
