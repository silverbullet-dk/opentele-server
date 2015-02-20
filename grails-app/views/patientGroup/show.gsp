
<%@ page import="org.opentele.server.model.PatientGroup" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<title><g:message code="default.show.label" args="[g.message(code:'patientGroup.label')]" /></title>
	</head>
	<body>
	
		<div id="show-patientGroup" class="content scaffold-show" role="main">
			<h1><g:message code="default.show.label" args="[g.message(code:'patientGroup.label')]" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list patientGroup">
			
				<g:if test="${patientGroupInstance?.name}">
				<li class="fieldcontain">
					<span id="name-label" class="property-label"><g:message code="patientGroup.name.label" /></span>
					
						<span class="property-value" aria-labelledby="name-label"><g:fieldValue bean="${patientGroupInstance}" field="name"/></span>
					
				</li>
				</g:if>
			
				<g:if test="${patientGroupInstance?.department}">
				<li class="fieldcontain">
					<span id="department-label" class="property-label"><g:message code="patientGroup.department.label" /></span>
					
					<span class="property-value" aria-labelledby="department-label">${patientGroupInstance?.department?.encodeAsHTML()}</span>
				</li>
				</g:if>


                <li class="fieldcontain">
                    <span id="patientGroup.disable_messaging.label" class="property-label">
                        <g:message code="patientGroup.disable_messaging.label"/>
                    </span>

                    <span class="property-value" aria-labelledby="patientGroup.disable_messaging.label">
                        <g:checkBox name="disableMessaging" value="${patientGroupInstance?.disableMessaging}" disabled="true" />
                    </span>
                </li>
                <li class="fieldcontain">
                    <span id="patientGroup.show_gestational_age.label" class="property-label">
                        <g:message code="patientGroup.showGestationalAge.label"/>
                    </span>

                    <span class="property-value" aria-labelledby="patientGroup.show_gestational_age.label">
                        <g:checkBox name="showGestationalAge" value="${patientGroupInstance?.showGestationalAge}" disabled="true" />
                    </span>
                </li>
                <g:if test="${grailsApplication.config.running.ctg.messaging.enabled}">
                    <li class="fieldcontain">
                        <span id="patientGroup.show_running_ctg_messaging.label" class="property-label">
                            <g:message code="patientGroup.showRunningCtgMessaging.label"/>
                        </span>

                        <span class="property-value" aria-labelledby="patientGroup.show_running_c_t_gmessaging.label">
                            <g:checkBox name="showRunningCtgMessaging" value="${patientGroupInstance?.showRunningCtgMessaging}" disabled="true" />
                        </span>
                    </li>
                </g:if>
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${patientGroupInstance?.id}" />
					<g:link class="edit" action="edit" id="${patientGroupInstance?.id}"><g:message code="default.button.edit.label" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label')}" onclick="return confirm('${message(code: 'patientGroup.delete.message.confirm', args: [patientGroupInstance?.name, patientGroupInstance?.department])}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
