<%@ page import="org.opentele.server.core.model.types.PermissionName; org.opentele.server.core.model.types.PermissionName; org.opentele.server.model.PatientNote" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="title" value="${message(code: 'patientNote.show.title', args: [patientNoteInstance.patient.name])}"/>
    <title>${title}</title>
</head>

<body>

<div id="show-patientNote" class="content scaffold-show" role="main">
    <h1>${title}</h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
        <ol class="property-list patientNote">

            <g:if test="${patientNoteInstance?.note}">
                <li class="fieldcontain">
                    <span id="note-label" class="property-label">
                        <g:message code="patientNote.note.label"/>
                    </span>

                    <span class="property-value" aria-labelledby="note-label">
                        <g:fieldValue bean="${patientNoteInstance}" field="note"/>
                    </span>
                </li>
            </g:if>

            <g:if test="${patientNoteInstance?.type}">
                <li class="fieldcontain">
                    <span id="type-label" class="property-label">
                        <g:message code="patientNote.type.label"/>
                    </span>

                    <span class="property-value" aria-labelledby="type-label">
                        <g:message code="enum.patientnote.${patientNoteInstance.type.name()}"/>
                    </span>
                </li>
            </g:if>
            <g:if test="${patientNoteInstance?.createdDate}">
                <li class="fieldcontain">
                    <span id="created-label" class="property-label">
                        <g:message code="patientNote.created.label"/>
                    </span>

                    <span class="property-value" aria-labelledby="type-label">
                        <g:formatDate date="${patientNoteInstance.createdDate}" />
                    </span>
                </li>
            </g:if>
            <g:if test="${patientNoteInstance.modifiedDate}">
                <li class="fieldcontain">
                    <span id="modified-label" class="property-label">
                        <g:message code="patientNote.modified.label"/>
                    </span>

                    <span class="property-value" aria-labelledby="type-label">
                        <g:formatDate date="${patientNoteInstance.modifiedDate}" />
                    </span>
                </li>
            </g:if>
            <g:if test="${patientNoteInstance?.createdBy}">
                <li class="fieldcontain">
                    <span id="createdBy-label" class="property-label">
                        <g:message code="patientNote.createdBy.label"/>
                    </span>

                    <span class="property-value" aria-labelledby="type-label">
                        <g:fieldValue bean="${patientNoteInstance}" field="createdBy"/>
                    </span>
                </li>
            </g:if>
            <g:if test="${patientNoteInstance?.reminderDate}">
                <li class="fieldcontain">
                    <span id="reminder-label" class="property-label">
                        <g:message code="patientNote.reminder.label"/>
                    </span>

                    <span class="property-value" aria-labelledby="type-label">
                        <g:formatDate format="dd-MM-yyyy HH:mm" date="${patientNoteInstance.reminderDate}"></g:formatDate>
                    </span>
                </li>
            </g:if>
        </ol>
    <g:form>
        <fieldset class="buttons">
            <g:hiddenField name="id" value="${patientNoteInstance?.id}"/>
            <g:hiddenField name="comingFrom" value="${comingFrom}"/>

            <g:if test="${canEdit == true}">
                <g:link class="edit" action="edit" id="${patientNoteInstance?.id}">
                    <g:message code="default.button.edit.label"/>
                </g:link>
                <sec:ifAnyGranted roles="${org.opentele.server.core.model.types.PermissionName.PATIENT_NOTE_DELETE}">
                    <g:actionSubmit
                            class="delete"
                            action="delete"
                            value="${message(code: 'default.button.delete.label')}"
                            onclick="return confirm('${message(code: 'default.button.delete.confirm.message')}');"
                    />
                </sec:ifAnyGranted>
           </g:if>
            <cq:patientNoteMarkSeenButton id="${patientNoteInstance?.id}" note="${patientNoteInstance}" />
        </fieldset>
    </g:form>
</div>
</body>
</html>
