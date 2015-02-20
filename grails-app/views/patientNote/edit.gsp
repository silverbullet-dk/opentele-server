<%@ page import="org.opentele.server.core.model.types.PermissionName; org.opentele.server.core.model.types.PermissionName; org.opentele.server.model.PatientNote" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'patientNote.label', default: 'PatientNote')}"/>
    <title><g:message code="default.edit" args="[entityName]"/></title>
</head>

<body>
<div id="edit-patientNote" class="content scaffold-edit" role="main">
    <h1><g:message code="default.edit" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${patientNoteInstance}">
        <ul class="errors" role="alert">
            <g:eachError bean="${patientNoteInstance}" var="error">
               <li><g:message error="${error}"/></li>
            </g:eachError>
        </ul>
    </g:hasErrors>

    <g:form method="post">
        <g:hiddenField name="id" value="${patientNoteInstance?.id}"/>
        <g:hiddenField name="version" value="${patientNoteInstance?.version}"/>
        <fieldset class="form">
            <g:render template="form"/>
        </fieldset>
        <fieldset class="buttons">
            <sec:ifAnyGranted roles="${org.opentele.server.core.model.types.PermissionName.PATIENT_NOTE_WRITE}">
                <g:actionSubmit class="save" action="update" value="${message(code: 'default.update', default: 'Update')}"/>
            </sec:ifAnyGranted>
        </fieldset>
    </g:form>
</div>
</body>
</html>
