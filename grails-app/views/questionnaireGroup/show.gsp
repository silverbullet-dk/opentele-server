<%@ page import="org.opentele.server.model.questionnaire.QuestionnaireGroup" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'questionnaireGroup.label', default: 'QuestionnaireGroup')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>

<div id="show-questionnaireGroup" class="content scaffold-show" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <ol class="property-list questionnaireGroup">

        <g:if test="${questionnaireGroupInstance?.name}">
            <li class="fieldcontain">
                <span id="name-label" class="property-label"><g:message code="questionnaireGroup.name.label"
                                                                        default="Name"/></span>

                <span class="property-value" aria-labelledby="name-label"><g:fieldValue
                        bean="${questionnaireGroupInstance}" field="name"/></span>

            </li>
        </g:if>

    </ol>
    <g:form>
        <fieldset class="buttons">
            <g:hiddenField name="id" value="${questionnaireGroupInstance?.id}"/>
            <g:link class="edit" action="edit" id="${questionnaireGroupInstance?.id}"><g:message
                    code="default.button.edit.label" default="Edit"/></g:link>
            <g:actionSubmit class="delete" action="delete"
                            value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                            onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');"/>
        </fieldset>
    </g:form>
    <ul class="one-to-many">
        <g:render template="questionnaireGroup2HeaderList"/>
    </ul>

</div>

</body>
</html>
