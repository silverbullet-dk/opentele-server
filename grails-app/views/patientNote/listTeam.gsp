<%@ page import="org.opentele.server.model.PatientNote" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="default.patient.note.label"/></title>
</head>

<body>
<div id="list-patientNote" class="content scaffold-list" role="main">
    <h1><g:message code="default.patient.note.label"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
            <tr>
                <g:sortableColumn property="patient" title="${message(code: 'patientNote.patient.label')}"/>

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
                <td>
                    <g:link controller="patient" action="show" id="${patientNoteInstance.patient.id}">${fieldValue(bean: patientNoteInstance, field: "patient")}</g:link>
                </td>

                <td>
                    <g:link action="show" id="${patientNoteInstance.id}" params="[comingFrom:'listTeam']">
                        ${fieldValue(bean: patientNoteInstance, field: "note")}
                    </g:link>
                </td>

                <td><g:message code="enum.patientnote.${patientNoteInstance.type.name()}"/></td>

                <td><g:formatDate date="${patientNoteInstance.reminderDate}"/></td>

                <td><g:message code="default.yesno.${isSeen[patientNoteInstance]}"/></td>

                <td><g:message code="default.yesno.${isSeenByAnyUser[patientNoteInstance]}"/></td>
            </tr>
        </g:each>
        </tbody>
    </table>
    <div class="pagination">
        <g:paginate total="${patientNoteInstanceTotal}"/>
    </div>
</div>
</body>
</html>
