<%@ page import="org.opentele.server.core.model.types.PermissionName; org.opentele.server.model.questionnaire.QuestionnaireHeader" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'questionnaireHeader.label', default: 'QuestionnaireHeader')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<div id="list-questionnaireHeader" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>
            <g:sortableColumn property="name"
                              title="${message(code: 'questionnaireHeader.name.label', default: 'Name')}" style="width: 300px"/>
            <th><g:message code="questionnaireHeader.revision.label" default="Revision"/></th>
            <th style="width: 120px"><g:message code="questionnaireHeader.createdDate.label" default="Date created"/></th>
            <th><g:message code="questionnaireHeader.status.label" default="Status"/></th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${questionnaireHeaderInstanceList}" status="i" var="questionnaireHeaderInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td style="text-align: left; padding-left: 5px;"><g:link action="show"
                            id="${questionnaireHeaderInstance.id}">${fieldValue(bean: questionnaireHeaderInstance, field: "name")}</g:link></td>

                <td>
                    <g:if test="${questionnaireHeaderInstance.activeQuestionnaire}">
                        ${fieldValue(bean: questionnaireHeaderInstance.activeQuestionnaire, field: "revision")}
                    </g:if>
                </td>

                <td>
                    <g:if test="${questionnaireHeaderInstance.activeQuestionnaire}">
                        <g:formatDate date="${questionnaireHeaderInstance.activeQuestionnaire.creationDate}"/>
                    </g:if>
                </td>
                <td>
                    <questionnaireHeader:status questionnaireHeader="${questionnaireHeaderInstance}"/>
                </td>
            </tr>
        </g:each>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate total="${questionnaireHeaderInstanceTotal}"/>
    </div>

    <sec:ifAnyGranted roles="${PermissionName.QUESTIONNAIRE_CREATE}">
    <fieldset class="buttons">
        <g:link class="create" action="create" controller="questionnaireHeader">
            <g:message code="default.create.label" args="${[g.message(code: 'questionnaireHeader.label')]}"/>
        </g:link>
    </fieldset>
    </sec:ifAnyGranted>
</div>
</body>
</html>
