<%@ page import="org.opentele.server.model.PatientNote" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="patientNote.create.title" /></title>
</head>

<body>
<div id="create-patientNote" class="content scaffold-create" role="main">
    <h1><g:message code="patientNote.create.title"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${patientNoteInstance}">
        <ul class="errors" role="alert">
            <g:eachError bean="${patientNoteInstance}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if> >
                    <g:message error="${error}"/>
                </li>
            </g:eachError>
        </ul>
    </g:hasErrors>
    <g:form action="save">
        <fieldset class="form">
            <g:hiddenField name="patientId" value="${patient.id}"/>
            <g:render template="form"/>
        </fieldset>
        <fieldset class="buttons">
            <g:submitButton name="create" class="save" value="${message(code: 'default.button.create.label')}"/>
        </fieldset>
    </g:form>
</div>
</body>
</html>
