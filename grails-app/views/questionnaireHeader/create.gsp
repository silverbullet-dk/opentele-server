<%@ page import="org.opentele.server.model.questionnaire.QuestionnaireHeader" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'questionnaireHeader.label', default: 'QuestionnaireHeader')}"/>
    <title><g:message code="default.create.label" args="[entityName]"/></title>
</head>

<body>
<div id="create-questionnaireHeader" class="content scaffold-create" role="main">
    <h1><g:message code="default.create.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${questionnaireHeaderInstance}">
        <ul class="errors" role="alert">
            <g:eachError bean="${questionnaireHeaderInstance}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                        error="${error}"/></li>
            </g:eachError>
        </ul>
    </g:hasErrors>
    <g:form action="save">
        <fieldset class="form">
            <g:render template="form"/>

        </fieldset>

        <fieldset class="buttons">
            <g:submitButton name="create" class="save"
                            value="${message(code: 'default.button.create.label', default: 'Create')}"/>
            <g:actionSubmit value="${message(code: 'questionnaireHeader.button.createAndEdit.label', default: 'Create')}"
                            data-tooltip="${message(code: 'questionnaireHeader.button.createAndEdit.tooltip')}"
                            class="save" action="saveAndEdit"/>

        </fieldset>
    </g:form>
</div>
</body>
</html>
