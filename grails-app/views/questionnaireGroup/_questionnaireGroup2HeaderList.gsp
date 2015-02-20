<%@ page import="org.opentele.server.core.model.types.PermissionName" %>
<h1>
	<g:message code="questionnaireGroup.questionnaires.label" />
</h1>
<table>
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
   				<g:message code='questionnaireGroup.questionnaire.action' default='Handling'/>
   			</th>
           </tr>
   	</thead>
	<tbody>
		<g:each
			in="${questionnaireGroupInstance?.questionnaireGroup2Header?.sort {it.questionnaireHeader.name.toLowerCase() } }"
			status="i" var="questionnaireGroup2Header">
            <!-- // TODO: Only edit, if schedule is "local" -->
			<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                <g:render template="questionnaireGroup2HeaderListRow" bean="${questionnaireGroup2Header}" var="rowInstance"/>
			</tr>
		</g:each>
	</tbody>
</table>
<g:if test="${ actionName != "create" }">
        <sec:ifAnyGranted roles="${PermissionName.MONITORING_PLAN_CREATE}">
            <div class="buttons">
                <li class="create">
                    <g:link class="create" controller="questionnaireGroup2QuestionnaireHeader"
                        action="create"
                        params="['questionnaireGroup.id': questionnaireGroupInstance?.id]">
                        ${message(code: 'questionnaireGroup2QuestionnaireHeader.give.label')}
                    </g:link>
                </li>
            </div>
        </sec:ifAnyGranted>
</g:if>
