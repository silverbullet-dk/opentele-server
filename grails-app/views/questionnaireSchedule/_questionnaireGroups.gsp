<%@ page import="org.opentele.server.core.model.types.PermissionName" %>
<h1>
    <g:message code="questionnaireGroup.questionnaires.label"/>
</h1>
<table class="questionnaire">
    <thead>
    <tr>
        <th>${message(code: 'questionnaireGroup.time.label', default: 'Tid')}</th>
        <th>
            <g:message code='questionnaireGroup.questionnaire.label' default='Spørgeskema'/>
        </th>
        <th>
            <g:message code='questionnaireGroup.questionnaire.status' default='Status'/>
        </th>
        <th>
            <g:message code='questionnaireGroup.questionnaire.include' default='Valgt'/>
        </th>
    </tr>
    </thead>
    <tbody>
    <g:each in="${command?.newQuestionnaires?.sort { it.questionnaireGroup2Header.questionnaireHeader.name.toLowerCase() }}"
            status="i" var="questionnaire">
        <tr class="${(i % 2) == 0 ? 'even' : 'odd'}  ${questionnaire.questionnaireScheduleOverlap ? 'highlight':''}">
            <g:render template="questionnaireGroupsRow" bean="${questionnaire}" var="rowInstance"/>
        </tr>
    </g:each>
</table>
<g:if test="${command?.newQuestionnaires?.any { it.questionnaireSchedule}}">
    <g:message code="questionnaireGroup.questionnaireSchedule.overlap"/>
</g:if>
