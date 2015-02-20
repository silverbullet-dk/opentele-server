<%@ page
	import="org.opentele.server.core.model.types.PermissionName; org.opentele.server.core.model.types.PermissionName; org.opentele.server.provider.constants.Constants; org.opentele.server.model.questionnaire.Questionnaire"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName" value="${message(code: 'questionnaire.label', default: 'Questionnaire')}" />
<title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
	<div id="list-questionnaire" class="content scaffold-list" role="main">
		<h1>
			<g:message code="default.list.label" args="[entityName]" />
		</h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
		<table>
			<thead>
				<tr>
					<g:sortableColumn property="name" title="${message(code: 'questionnaire.name.label', default: 'Name')}" />

					<g:sortableColumn property="revision" title="${message(code: 'questionnaire.revision.label', default: 'Revision')}" />

					<th>
                        <g:message code="questionnaire.creator.label" default="Creator" />
                    </th>

					<g:sortableColumn property="creationDate" title="${message(code: 'questionnaire.creationDate.label', default: 'Creation Date')}" />
				</tr>
			</thead>
			<tbody>
				<g:each in="${questionnaireInstanceList}" status="i" var="questionnaireInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<td>
                            <g:link action="show" id="${questionnaireInstance.id}">
								${fieldValue(bean: questionnaireInstance, field: "name")}
							</g:link>
                        </td>
						<td>
							${fieldValue(bean: questionnaireInstance, field: "revision")}
						</td>

						<td>
							${fieldValue(bean: questionnaireInstance, field: "creator")}
						</td>

						<td>
                            <g:formatDate date="${questionnaireInstance.creationDate}" />
                        </td>

					</tr>
				</g:each>
			</tbody>
		</table>
		<div class="pagination">
			<g:paginate total="${questionnaireInstanceTotal}" />
		</div>
        <sec:ifAnyGranted roles="${org.opentele.server.core.model.types.PermissionName.QUESTIONNAIRE_CREATE}">
        <fieldset class="buttons">
            <g:link class="create" controller="questionnaireEditor">
                <g:message code="default.create.label" args="${[g.message(code:'questionnaire.label')]}" />
            </g:link>
        </fieldset>
        </sec:ifAnyGranted>
	</div>
</body>
</html>
