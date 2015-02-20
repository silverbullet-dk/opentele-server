<%@ page import="org.opentele.server.model.questionnaire.QuestionnaireGroup2QuestionnaireHeader" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<title><g:message code="questionnaireGroup2QuestionnaireHeader.give.label" /></title>
	</head>
	<body>
		<div id="create-questionnaireGroup2QuestionnaireHeader" class="content scaffold-create" role="main">
			<h1><g:message code="questionnaireGroup2QuestionnaireHeader.give.label"/></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<tmpl:/questionnaireSchedule/errors/>
			<g:form action="save" >
				<fieldset class="form">
					<tmpl:form/>
				</fieldset>
				<fieldset class="buttons">
					<g:submitButton name="create" class="save" value="${message(code: 'questionnaireGroup2QuestionnaireHeader.button.create.label', default: 'Create')}" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
