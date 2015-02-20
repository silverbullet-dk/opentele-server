<%@ page import="org.opentele.server.core.model.types.PermissionName; org.opentele.server.model.questionnaire.QuestionnaireGroup" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'questionnaireGroup.label', default: 'QuestionnaireGroup')}"/>
    <title><g:message code="default.list.label" args="[entityName]"/></title>
</head>

<body>
<div id="list-questionnaireGroup" class="content scaffold-list" role="main">
    <h1><g:message code="default.list.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <table>
        <thead>
        <tr>

            <g:sortableColumn property="name" title="${message(code: 'questionnaireGroup.name.label')}"/>
            <th><g:message code="questionnaireGroup.count.label"/></th>
        </tr>
        </thead>
        <tbody>
        <g:each in="${questionnaireGroupInstanceList}" status="i" var="questionnaireGroupInstance">
            <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">

                <td>
                    <sec:ifAnyGranted roles="${PermissionName.QUESTIONNAIRE_GROUP_READ}">
                        <g:link action="show"
                                id="${questionnaireGroupInstance.id}">${fieldValue(bean: questionnaireGroupInstance, field: "name")}</g:link>
                    </sec:ifAnyGranted>
                    <sec:ifNotGranted roles="${PermissionName.QUESTIONNAIRE_GROUP_READ}">
                        ${fieldValue(bean: questionnaireGroupInstance, field: "name")}
                    </sec:ifNotGranted>
                </td>
                <td>${questionnaireGroupInstance?.questionnaireGroup2Header?.size()}</td>
            </tr>
        </g:each>
        </tbody>
    </table>

    <div class="pagination">
        <g:paginate total="${questionnaireGroupInstanceTotal}"/>
    </div>
    <sec:ifAnyGranted roles="${PermissionName.QUESTIONNAIRE_GROUP_CREATE}">
        <fieldset class="buttons">
            <g:link class="create" action="create" controller="questionnaireGroup">
                <g:message code="default.create.label" args="${[g.message(code: 'questionnaireGroup.label')]}"/>
            </g:link>
        </fieldset>
    </sec:ifAnyGranted>

</div>
</body>
</html>
