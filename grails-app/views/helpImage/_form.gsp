<%@ page import="org.opentele.server.model.HelpImage" %>



<div class="fieldcontain ${hasErrors(bean: helpImageInstance, field: 'filename', 'error')} required">
	<label for="filename">
		<g:message code="helpImage.filename.label" default="Filename" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="filename" required="" value="${helpImageInstance?.filename}"/>
</div>

