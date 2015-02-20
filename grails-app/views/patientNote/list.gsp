<%@ page import="org.opentele.server.core.model.types.PermissionName; org.opentele.server.core.model.types.PermissionName; org.opentele.server.model.PatientNote" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="title" value="${message(code: 'patientNote.list.title', args: [patient.name.encodeAsHTML()])}"/>
    <title>${title}</title>
</head>

<body>
<div id="list-patientNote" class="content scaffold-list" role="main">
    <h1>${title}</h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
            <tr>
                <g:sortableColumn property="note" title="${message(code: 'patientNote.note.label')}"/>

                <g:sortableColumn property="type" title="${message(code: 'patientNote.type.label')}"/>

                <g:sortableColumn property="reminderDate" title="${message(code: 'patientNote.reminder.label')}"/>

                <g:sortableColumn property="isSeen" title="${message(code: 'patientNote.isSeen.label')}"/>

                <g:sortableColumn property="isSeenByAnyUser" title="${message(code: 'patientNote.isSeenByAnyUser.label')}"/>
            </tr>
        </thead>
        <tbody>
        <g:each in="${patientNoteInstanceList}" status="i" var="patientNoteInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <g:if test="${PatientNote.isRemindToday(patientNoteInstance)}">
                    <td id="patientNoteReminder" onclick="window.location='${createLink(action: 'show', id: patientNoteInstance.id)}'" onmouseover="this.style.textDecoration='underline'" onmouseout="this.style.textDecoration='none'" style="cursor:pointer;">
                </g:if>
                <g:else>
                    <td onclick="window.location='${createLink(action: 'show', id: patientNoteInstance.id)}'" onmouseover="this.style.textDecoration='underline'" onmouseout="this.style.textDecoration='none'" style="cursor:pointer;">
                </g:else>
                ${fieldValue(bean: patientNoteInstance, field: "note")}
                </td>
                <td><g:message code="enum.patientnote.${patientNoteInstance.type.name()}"/></td>

                <td><g:formatDate date="${patientNoteInstance.reminderDate}"/></td>

                <td><g:message code="default.yesno.${isSeen[patientNoteInstance]}"/></td>

                <td><g:message code="default.yesno.${isSeenByAnyUser[patientNoteInstance]}"/></td>
            </tr>
        </g:each>
        </tbody>
    </table>
    <g:form>
        <fieldset class="buttons">
            <g:hiddenField name="id" value="${patientNoteInstance?.id}" />
            <sec:ifAnyGranted roles="${org.opentele.server.core.model.types.PermissionName.PATIENT_NOTE_CREATE}">
                <g:link class="create" action="create" id="${patientNoteInstance?.id}" params="[patientId: patient.id]">
                    <g:message code="default.button.create.label" />
                </g:link>
            </sec:ifAnyGranted>
        </fieldset>
    </g:form>
    <div class="pagination">
        <g:paginate total="${patientNoteInstanceTotal}" id="${patient.id}"/>
    </div>
</div>
</body>
</html>
