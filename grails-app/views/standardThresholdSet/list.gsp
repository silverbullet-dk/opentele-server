<%@ page import="org.opentele.server.core.model.types.PermissionName; org.opentele.server.core.model.types.MeasurementTypeName; org.opentele.server.model.StandardThresholdSet" %>
<!doctype html>

<html>
	<head>
		<meta name="layout" content="main">
		<title><g:message code="standardThresholdSet.show"/></title>
	</head>
	<body>
		<div id="show-standardThresholdSet" class="content scaffold-show" role="main">
			<h1><g:message code="standardThresholdSet.show" /></h1>
			<g:if test="${flash.message}">
                <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${standardThresholdSetInstance}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${standardThresholdSetInstance}" var="error">
                        <li><g:message error="${error}"/></li>
                    </g:eachError>
                </ul>
			</g:hasErrors>

            <g:each in="${standardThresholdSetInstanceList}" status="i" var="standardThresholdSetInstance">
                <g:if test="${standardThresholdSetInstance.patientGroup}">
                    <div class="standardThresholdSet">
                        <h2>
                            <g:message code="standardThresholdSet.patientGroup"
                                       args="[standardThresholdSetInstance.patientGroup]"/><a
                                name="${standardThresholdSetInstance.id}">&nbsp;</a>
                        </h2>
                        <tmpl:/patient/thresholds thresholds="${standardThresholdSetInstance.thresholds}"  parent="${standardThresholdSetInstance}" writePermission="${PermissionName.STANDARD_THRESHOLD_WRITE}" deletePermission="${PermissionName.STANDARD_THRESHOLD_DELETE}" >
                            <sec:ifAnyGranted roles="${PermissionName.STANDARD_THRESHOLD_WRITE},${PermissionName.STANDARD_THRESHOLD_DELETE}">
                                <tfoot>
                                    <tr>
                                        <td colspan="100">
                                            <fieldset class="buttons">
                                                <g:hiddenField name="id" value="${standardThresholdSetInstance?.id}" />
                                                <sec:ifAnyGranted roles="${PermissionName.STANDARD_THRESHOLD_WRITE}">
                                                    <g:form method="post" action="addThreshold">
                                                        <g:hiddenField name="id" value="${standardThresholdSetInstance.id}"/>
                                                        <g:submitButton class="create"
                                                                        name="chooseThreshold"
                                                                        value="${message(code: 'standardThresholdSet.add')}"/>
                                                    </g:form>
                                                </sec:ifAnyGranted>

                                                <sec:ifAnyGranted roles="${PermissionName.STANDARD_THRESHOLD_DELETE}">
                                                    <g:form method="post">
                                                        <g:hiddenField name="id" value="${standardThresholdSetInstance.id}"/>
                                                        <g:actionSubmit class="delete"
                                                                        action="delete"
                                                                        value="${message(code: 'standardThresholdSet.deleteAll')}"
                                                                        onclick="return confirm('${message(code: 'standardThresholdSet.confirmDeleteAll')}');"/>
                                                    </g:form>
                                                </sec:ifAnyGranted>
                                            </fieldset>
                                        </td>
                                    </tr>
                                </tfoot>
                            </sec:ifAnyGranted>
                        </tmpl:/patient/thresholds>
                    </div>
                </g:if>
            </g:each>
        </div>
        <tmpl:/meta/registerCurrentPage/>
	</body>
</html>
