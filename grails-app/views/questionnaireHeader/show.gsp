<%@ page import="org.opentele.server.core.model.types.PermissionName; org.opentele.server.model.questionnaire.QuestionnaireHeader" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'questionnaireHeader.label', default: 'QuestionnaireHeader')}"/>
    <title><g:message code="default.show.label" args="[entityName]"/></title>
</head>

<body>

<div id="show-questionnaireHeader" class="content scaffold-show" role="main">
    <h1><g:message code="default.show.label" args="[entityName]"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:if test="${flash.error}">
        <div class="errors" role="status">${flash.error}</div>
    </g:if>
    <ol class="property-list questionnaireHeader">

        <g:if test="${questionnaireHeaderInstance?.name}">
            <li class="fieldcontain">
                <span id="name-label" class="property-label"><g:message code="questionnaireHeader.name.label"
                                                                        default="Name"/></span>

                <span class="property-value" aria-labelledby="name-label"><g:fieldValue
                        bean="${questionnaireHeaderInstance}" field="name"/></span>

            </li>
        </g:if>


         <li class="fieldcontain"
             data-tooltip="${message(code: 'tooltip.questionnaireHeader.requiresManualInspection')}">
            <span id="requiresManualInspection-label" class="property-label">
                <g:message code="questionnaireHeader.requiresManualInspection.label"/>
            </span>

             <span class="property-value" aria-labelledby="requiresManualInspection-label">
                 <g:checkBox
                     name="requiresManualInspection"
                     value="${questionnaireHeaderInstance.requiresManualInspection}"
                     disabled="true"
                 />
             </span>
         </li>

        <g:if test="${questionnaireHeaderInstance?.activeQuestionnaire}">
            <li class="fieldcontain">
                <span id="activeQuestionnaire-label" class="property-label"><g:message
                        code="questionnaireHeader.activeQuestionnaire.label" default="Active Questionnaire"/></span>

                <span class="property-value" aria-labelledby="activeQuestionnaire-label">
                    <g:message code="questionnaireHeader.questionnaire.information"
                               args="[questionnaireHeaderInstance.activeQuestionnaire.creator?.name ?: questionnaireHeaderInstance.activeQuestionnaire.createdBy, questionnaireHeaderInstance.activeQuestionnaire.revision, formatDate(date: questionnaireHeaderInstance.activeQuestionnaire.creationDate)]"/>
                </span>

            </li>
        </g:if>

        <g:if test="${questionnaireHeaderInstance?.draftQuestionnaire}">
            <li class="fieldcontain">
                <span id="draftQuestionnaire-label" class="property-label"><g:message
                        code="questionnaireHeader.draftQuestionnaire.label" default="Draft Questionnaire"/></span>

                <span class="property-value" aria-labelledby="draftQuestionnaire-label">
                    <g:message code="questionnaireHeader.questionnaire.information"
                               args="[questionnaireHeaderInstance.draftQuestionnaire.creator?.name ?: questionnaireHeaderInstance.draftQuestionnaire.createdBy, questionnaireHeaderInstance.draftQuestionnaire.revision, formatDate(date: questionnaireHeaderInstance.draftQuestionnaire.creationDate)]"/>
                </span>

            </li>
        </g:if>

        <g:if test="${historicQuestionnaires}">
            <li class="fieldcontain">
                <span id="questionnaires-label" class="property-label"><g:message
                        code="questionnaireHeader.questionnaires.label" default="Questionnaires"/></span>

                <table>
                    <thead>
                    <tr>

                        <th><g:message code="questionnaire.revision.label" default="Revision"/></th>

                        <th>
                            <g:message code="questionnaire.creator.label" default="Creator"/>
                        </th>

                        <th><g:message code='questionnaire.creationDate.label' default='Creation Date'/></th>
                    </tr>
                    </thead>
                    <tbody>
                    <g:each in="${historicQuestionnaires}" status="i" var="questionnaireInstance">
                        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                            <td>
                                ${fieldValue(bean: questionnaireInstance, field: "revision")}
                            </td>

                            <td>
                                ${fieldValue(bean: questionnaireInstance, field: "creator")}
                            </td>

                            <td>
                                <g:formatDate date="${questionnaireInstance.creationDate}"/>
                            </td>

                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </li>
        </g:if>

    </ol>
    <sec:ifAnyGranted
            roles="${PermissionName.QUESTIONNAIRE_WRITE},${PermissionName.QUESTIONNAIRE_DELETE},${PermissionName.QUESTIONNAIRE_CREATE}">
        <g:form>
            <fieldset class="buttons">
                <g:hiddenField name="id" value="${questionnaireHeaderInstance?.id}"/>
                <sec:ifAnyGranted roles="${PermissionName.QUESTIONNAIRE_WRITE}">
                    <g:link class="edit" data-tooltip="${message(code: 'questionnaireHeader.edit.tooltip')}"
                            action="edit" id="${questionnaireHeaderInstance?.id}">
                        <g:message code="default.button.edit.label"/>
                    </g:link>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="${PermissionName.QUESTIONNAIRE_DELETE}">
                    <g:if test="${questionnaireHeaderInstance.activeQuestionnaire}">
                        <g:actionSubmit class="delete" data-tooltip="${message(code: 'questionnaireHeader.activeQuestionnaire.makeHistoric.tooltip')}"
                                        action="unpublish"
                                        value="${message(code: 'questionnaireHeader.activeQuestionnaire.makeHistoric.label', default: 'Unpublish')}"
                                        onclick="return confirm('${message(code: 'questionnaireHeader.activeQuestionnaire.makeHistoric.message', default: 'Are you sure?')}');"/>
                    </g:if>
                    <g:if test="${!(questionnaireHeaderInstance.activeQuestionnaire || historicQuestionnaires)}">
                        <g:actionSubmit class="delete" data-tooltip="${message(code: 'questionnaireHeader.delete.tooltip')}"
                                        action="delete"
                                        value="${message(code: 'questionnaireHeader.delete.label', default: 'Delete')}"
                                        onclick="return confirm('${message(code: 'questionnaireHeader.delete.message', default: 'Are you sure?')}');"/>
                    </g:if>
                </sec:ifAnyGranted>
            </fieldset>
            <fieldset class="buttons">
                <sec:ifAnyGranted roles="${PermissionName.QUESTIONNAIRE_WRITE}">
                    <g:if test="${questionnaireHeaderInstance.draftQuestionnaire}">
                        <g:actionSubmit class="publish" data-tooltip="${message(code: 'questionnaireHeader.draftQuestionnaire.publish.tooltip')}"
                                        action="publishDraft"
                                        value="${message(code: 'questionnaireHeader.draftQuestionnaire.publish.label', default: 'Publish')}"
                                        onclick="return confirm('${message(code: 'questionnaireHeader.draftQuestionnaire.publish.message', default: 'Are you sure?')}');"/>
                    </g:if>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="${PermissionName.QUESTIONNAIRE_CREATE}">
                    <g:if test="${questionnaireHeaderInstance.draftQuestionnaire?.editorState}">
                        <g:actionSubmit class="edit" action="editDraft"
                                        value="${message(code: 'questionnaireHeader.draftQuestionnaire.edit.label', default: 'Edit Draft')}"/>
                    </g:if>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="${PermissionName.QUESTIONNAIRE_WRITE}">

                    <g:if test="${questionnaireHeaderInstance.draftQuestionnaire}">
                        <g:actionSubmit class="delete" data-tooltip="${message(code: 'questionnaireHeader.draftQuestionnaire.delete.tooltip')}"
                                        action="deleteDraft"
                                        value="${message(code: 'questionnaireHeader.draftQuestionnaire.delete.label', default: 'Delete Draft')}"
                                        onclick="return confirm('${message(code: 'questionnaireHeader.draftQuestionnaire.delete.message', default: 'Are you sure?')}');"/>
                    </g:if>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="${PermissionName.QUESTIONNAIRE_CREATE}">
                    <g:if test="${!questionnaireHeaderInstance.draftQuestionnaire}">
                        <g:actionSubmit class="create" data-tooltip="${message(code: 'questionnaireHeader.draftQuestionnaire.create.tooltip')}"
                                        action="createDraft"
                                        value="${message(code: 'questionnaireHeader.draftQuestionnaire.create.label', default: 'Create Draft')}"/>
                    </g:if>
                </sec:ifAnyGranted>
            </fieldset>
        </g:form>
    </sec:ifAnyGranted>
</div>
</body>
</html>
