<%@ page import="org.opentele.server.core.model.types.PermissionName" %>
<h1>
	<g:message code="monitoringPlan.questionnaires.label" />
</h1>
<table>
	<thead>
		<tr>
			<th>${message(code: 'monitoringPlan.time.label')}</th>
			<th>
				<g:message code='monitoringPlan.questionnaire.label'/>
            </th>
            <th>
                <g:message code='monitoringPlan.questionnaire.status'/>
            </th>
            <th>
				<g:message code='monitoringPlan.questionnaire.action'/>
			</th>
        </tr>
	</thead>
	<tbody>
		<g:each
			in="${monitoringPlanInstance?.questionnaireSchedules?.sort { it.questionnaireHeader.name.toLowerCase() } }"
			status="i" var="questionnaireScheduleInstance">
			<tr class="${(i % 2) == 0 ? 'even' : 'odd'} ${(questionnaireScheduleInstance.id in flash.updated ?: [])  ? 'highlight':''}">
                <g:render template="questionnaireScheduleListRow" bean="${questionnaireScheduleInstance}" var="rowInstance" model="[schedule: questionnaireScheduleInstance, instanceController: 'questionnaireSchedule']"/>
			</tr>
		</g:each>
	</tbody>
</table>
<g:if test="${ actionName != "create" }">
    <g:if test="${canAddMoreSchedules}">
        <sec:ifAnyGranted roles="${PermissionName.MONITORING_PLAN_CREATE}">
            <div class="buttons">
                <li class="create">
                    <g:link class="create" controller="questionnaireSchedule"
                        action="create"
                        params="['monitoringPlan.id': monitoringPlanInstance?.id, 'addtoplan': 'true']">
                        ${message(code: 'monitoringPlan.assignQuestionnaire')}
                    </g:link>

                    <g:link class="create" controller="questionnaireSchedule"
                        action="showAddQuestionnaireGroup"
                        params="['monitoringPlan.id': monitoringPlanInstance?.id]">
                        ${message(code: 'monitoringPlan.questionnaireGroup.addGroup')}
                    </g:link>
                </li>
            </div>
        </sec:ifAnyGranted>
    </g:if>
    <g:else>
        <g:message code='monitoringPlan.questionnaire.hasAll'/>
    </g:else>
</g:if>
