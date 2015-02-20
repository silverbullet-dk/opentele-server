
<%@ page import="org.opentele.server.core.model.types.PermissionName; org.opentele.server.model.Permission; org.opentele.server.model.Role"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName" value="${message(code: 'role.label')}" />
<title><g:message code="default.show.label" args="[entityName]" /></title>
<style>
    option:checked {
        color: white;
    }
</style>
</head>
<body>
	<div id="show-role" class="content scaffold-show" role="main">
		<h1>
			<g:message code="default.show.label" args="[entityName]" />
		</h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
		<ol class="property-list role">

			<g:if test="${roleInstance?.authority}">
				<li class="fieldcontain">
                    <span id="authority-label" class="property-label">
                        <g:message code="role.authority.label" default="Authority" />
                    </span>
                    <span class="property-value" aria-labelledby="authority-label">
                        <g:fieldValue bean="${roleInstance}" field="authority" />
                    </span>
                </li>
			</g:if>

            <div class="fieldcontain required">
                <label for="permissionIds" data-tooltip="${message(code: 'tooltip.create.role.permissions')}">
                    <g:message code="role.permissions.label" default="type" />
                </label>
                <g:select name="permissionIds"
                          from="${Permission.allAssignable().sort{ g.message(code: "permission.description.${it.actualName}") }}"
                          optionKey="id"
                          multiple="multiple"
                          optionValue="${{g.message(code: "permission.description.${it.actualName}")}}"
                          value="${permissions*.id}"
                          size="20"
                          class="wide_select"
                          disabled="true"/>
            </div>
        </ol>

		<g:form>
			<fieldset class="buttons">
				<g:hiddenField name="id" value="${roleInstance?.id}" />
                <sec:ifAnyGranted roles="${PermissionName.ROLE_WRITE}">
                    <g:link class="edit" action="edit" id="${roleInstance?.id}">
                        <g:message code="default.button.edit.label" default="Edit" />
                    </g:link>
                </sec:ifAnyGranted>
                <sec:ifAllGranted roles="${PermissionName.ROLE_CREATE},${PermissionName.ROLE_READ}">
                    <g:link class="create" action="create" id="${roleInstance?.id}">
                        <g:message code="role.button.copy.label" default="Copy" />
                    </g:link>
                </sec:ifAllGranted>
                <sec:ifAnyGranted roles="${PermissionName.ROLE_DELETE}">
                    <g:actionSubmit
                            class="delete"
                            action="delete"
                            value="${message(code: 'default.button.delete.label')}"
                            onclick="return confirm('${message(code: 'default.button.delete.confirm.message')}');" />
                </sec:ifAnyGranted>
			</fieldset>
		</g:form>
	</div>
</body>
</html>
