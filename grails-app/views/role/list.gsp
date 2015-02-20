
<%@ page import="org.opentele.server.core.model.types.PermissionName; org.opentele.server.model.Role"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'role.label', default: 'Role')}" />
<title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>
	<div id="list-role" class="content scaffold-list" role="main">
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
					<g:sortableColumn property="authority"
						title="${message(code: 'role.authority.label')}" />
				</tr>
			</thead>
			<tbody>
				<g:each in="${roleInstanceList}" status="i" var="roleInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<td>
                            <g:link action="show" id="${roleInstance.id}"> ${fieldValue(bean: roleInstance, field: "authority")}</g:link>
                        </td>
					</tr>
				</g:each>
			</tbody>
		</table>
		<div class="pagination">
			<g:paginate total="${roleInstanceTotal}" />
		</div>

        <fieldset class="buttons">
            <sec:ifAnyGranted roles="${PermissionName.ROLE_CREATE}">
                <g:link class="create" action="create">
                    <g:message code="default.create.label" args='["${g.message(code:'role.label')}"]' />
                </g:link>
            </sec:ifAnyGranted>
        </fieldset>
	</div>
</body>
</html>
