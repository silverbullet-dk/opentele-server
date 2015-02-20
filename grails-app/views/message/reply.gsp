<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<meta name="layout" content="main">
<title><g:message code="messages.reply.title" /></title>
</head>
<body>
	<%@ page import="org.opentele.server.model.Message"%>

	<div id="create-message" class="content scaffold-create" role="main">
		<h1><g:message code="messages.reply.title" /></h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
		<g:hasErrors bean="${messageInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${messageInstance}" var="error">
					<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>>
                        <g:message error="${error}" />
                    </li>
				</g:eachError>
			</ul>
		</g:hasErrors>
		<g:form action="save">
			<fieldset class="form">
				<div class="fieldcontain ${hasErrors(bean: messageInstance, field: 'patient', 'error')} required">
					<label>
                        <g:message code="message.to.label" />
					</label>
					<g:if test="${messageInstance.inReplyTo.sentByPatient}">
						${messageInstance.patient.name()}
					</g:if>
					<g:else>
						${messageInstance.department}
					</g:else>
				</div>

				<div class="fieldcontain ${hasErrors(bean: messageInstance, field: 'title', 'error')} ">
					<label for="title"> <g:message code="message.title.label" />
					</label>
					<g:textField name="title" value="${messageInstance?.title}" />
				</div>

				<div class="fieldcontain ${hasErrors(bean: messageInstance, field: 'text', 'error')} ">
					<label for="text"> <g:message code="message.text.label" />
					</label>
					<g:textArea name="text" cols="40" rows="5" maxlength="2000" value="${messageInstance?.text}" />
				</div>
			</fieldset>
			<fieldset class="buttons">
				<g:submitButton name="create" class="save" value="${message(code: 'messages.reply.label')}" />
			</fieldset>
			<g:hiddenField name="patient" value="${messageInstance?.patient.id}" />
			<g:hiddenField name="department" value="${messageInstance?.department.id}" />
			<g:hiddenField name="inReplyTo" value="${messageInstance?.inReplyTo.id}" />
		</g:form>
	</div>

	<g:if test="${messageInstance.inReplyTo.sentByPatient}">
		<h1><g:message code="message.reply.label" args="[messageInstance.patient]" /></h1>
	</g:if>
	<g:else>
		<h1><g:message code="message.reply.label" args="[messageInstance.department]" /></h1>
	</g:else>

	<div class="content scaffold-show">
		<span class="property-value">
			${messageInstance.inReplyTo.text.encodeAsHTML()}
		</span>
	</div>
</body>
</html>