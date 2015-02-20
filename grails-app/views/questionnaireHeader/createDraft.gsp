<%@ page import="org.opentele.server.model.questionnaire.QuestionnaireHeader" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<title><g:message code="questionnaireHeader.draftCreate.label" /></title>
	</head>
	<body>
		<div id="create-questionnaireHeader" class="content scaffold-create" role="main">
			<h1><g:message code="questionnaireHeader.draftCreate.label"/></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${questionnaireHeaderInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${questionnaireHeaderInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
				</g:eachError>
			</ul>
			</g:hasErrors>
			<g:form controller="questionnaireEditor" action="edit" >
                <g:hiddenField name="id" value="${questionnaireHeaderInstance.id}"/>
				<fieldset class="form">
                    <div class="fieldcontain ${hasErrors(bean: questionnaireHeaderInstance, field: 'name', 'error')} required">
                    	<tooltip:label for="baseId" message="questionnaireHeader.draftCreate.baseRevision.tooltip">
                    		<g:message code="questionnaireHeader.draftCreate.baseRevision.label" default="Base revision" />
                    		<span class="required-indicator">*</span>
                    	</tooltip:label>
                    	<g:select name="baseId" from="${questionnaireHeaderInstance.questionnaires.findAll {it.editorState}.sort{it.creationDate}.reverse() }" optionKey="id" noSelection="[0: g.message(code: 'questionnaireHeader.draftCreate.noRevision')]"/>
                    </div>
				</fieldset>
				<fieldset class="buttons">
					<g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
