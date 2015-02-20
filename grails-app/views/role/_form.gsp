<%@ page import="org.opentele.server.model.Permission; org.opentele.server.model.Role"%>
<div
	class="fieldcontain ${hasErrors(bean: roleInstance, field: 'authority', 'error')} required">
	<label for="authority">
        <g:message code="role.authority.label" />
        <span class="required-indicator">*</span>
	</label>
	<g:textField name="authority" required="" value="${roleInstance?.authority}" />
</div>
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
              class="wide_select"/>
</div>
