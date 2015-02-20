<%@ page import="org.opentele.server.core.model.types.PermissionName; org.opentele.server.core.model.types.PermissionName; org.opentele.server.model.Patient"%>
<%@ page import="org.opentele.server.model.MonitoringPlan"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="title" value="${message(code: 'monitoringPlan.show.title', args: [patientInstance.name.encodeAsHTML()])}" />
<title>${title}</title>
</head>
<body>
	<g:if test="${!monitoringPlanInstance}">
		<h1><g:message code="monitoringPlan.show.no-plan.label" args="[patientInstance.name.encodeAsHTML()]"/></h1>
        <sec:ifAnyGranted roles="${org.opentele.server.core.model.types.PermissionName.MONITORING_PLAN_CREATE}">
            <p>
                <i><g:message code="monitoringPlan.show.no-plan.description" /></i>
            </p>
            <g:form controller="monitoringPlan">
                <fieldset class="buttons">
                    <g:link class="create" action="create" params="['patientId':params.id]">
                        <g:message code="default.new.label" args="[message(code: 'monitoringPlan.label')]" />
                    </g:link>
                </fieldset>
            </g:form>
        </sec:ifAnyGranted>
	</g:if>
	<g:else>
		<!-- If monitoringPlanInstance is not null -->
		<div id="show-monitoringPlan" class="content scaffold-show" role="main">
			<h1>${title}</h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">
					${flash.message}
				</div>
			</g:if>

			<g:if test="${monitoringPlanInstance.startDate}">
                <ul>
                    <li class="fieldcontain">
                        <span id="startDate-label" class="property-label">
                            <g:message code="monitoringPlan.startDate.label" />
                        </span>
                        <span class="property-value" aria-labelledby="startDate-label">
                            <g:formatDate formatName="default.date.format.notime" date="${monitoringPlanInstance?.startDate}" />
                        </span>
                    </li>
                </ul>
			</g:if>

			<g:form controller="monitoringPlan">
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${monitoringPlanInstance?.id}"/>
					<g:link class="edit" action="edit" id="${monitoringPlanInstance?.id}" params="['patientId':params.id]">
						<g:message code="default.button.edit.label" />
					</g:link>
                </fieldset>
			</g:form>

			<ul class="one-to-many">
				<g:render template="questionnaireScheduleList" />
			</ul>
		</div>
	</g:else>
</body>
</html>
