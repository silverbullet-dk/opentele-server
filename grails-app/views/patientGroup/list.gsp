
<%@ page import="org.opentele.server.model.PatientGroup" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<title><g:message code="default.patientGroup.list.label" /></title>
	</head>
	<body>
		<div id="list-patientGroup" class="content scaffold-list" role="main">
			<h1><g:message code="default.patientGroup.list.label"/></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<table>
				<thead>
					<tr>
					
						<g:sortableColumn property="name" title="${message(code: 'patientGroup.name.label')}" />
					
						<th><g:message code="patientGroup.department.label"/></th>
                        <th><g:message code="patientGroup.messages.allowed.label"/></th>
					
					</tr>
				</thead>
				<tbody>
				<g:each in="${patientGroupInstanceList}" status="i" var="patientGroupInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
					
						<td><g:link action="show" id="${patientGroupInstance.id}">${fieldValue(bean: patientGroupInstance, field: "name")}</g:link></td>
					
						<td>${fieldValue(bean: patientGroupInstance, field: "department")}</td>
                        <td>
                            <g:if test="${patientGroupInstance.disableMessaging}">
                                <g:message code="default.yesno.no"/>
                            </g:if>
                            <g:else>
                                <g:message code="default.yesno.yes"/>
                            </g:else>
                        </td>
					
					</tr>
				</g:each>
				</tbody>
			</table>
            <div class="pagination">
                <g:paginate total="${patientGroupInstanceTotal}" />
            </div>

            <fieldset class="buttons">
                <g:link class="create" action="create">
                    <g:message code="default.create.label" args="[g.message(code:'patientGroup.label')]" />
                </g:link>
            </fieldset>
		</div>
	</body>
</html>
