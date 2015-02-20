<%@ page import="org.opentele.server.core.model.types.PermissionName" contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<link rel="stylesheet" href="${resource(dir: 'css', file: 'messages_show.css')}" type="text/css">
<meta name="layout" content="main">
<g:set var="title" value="${message(code: "message.list.title", args: [patientInstance.name.encodeAsHTML()])}"/>
<title>${title}</title>
</head>
<body>
	<div id="list-patient" class="content scaffold-list" role="main">
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
        <h1>${title}</h1>
        <sec:ifAnyGranted roles="${PermissionName.MESSAGE_CREATE}">

                <g:form>
                    <fieldset class="buttons">
                        <g:if test="${canSendMessages}">
                            <g:hiddenField name="id" value="${messageInstance?.id}" />
                            <g:link class="save" controller="message" action="create" params="[receipientId:patientInstance.id]">
                                <g:message code="message.list.new" />
                            </g:link>
                        </g:if>
                        <g:else>
                            <g:message code="message.list.cant.send.messages" />
                        </g:else>
                    </fieldset>
                </g:form>

        </sec:ifAnyGranted>
        <div class="wrapTitle">
            <div class="byHospitalHeader"><g:message code="messages.clinician"/></div>
            <div class="byPatientHeader">${patientInstance.name.encodeAsHTML()}</div>
        </div>
        <tmpl:/message/messagesPatientContext/>
	</div>
</body>
</html>
