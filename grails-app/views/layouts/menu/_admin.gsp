<%@ page import="org.opentele.server.core.model.types.PermissionName" %>
<!-- Menu logget ind som administrator -->
<sec:ifAnyGranted
        roles="${PermissionName.QUESTIONNAIRE_READ_ALL},${PermissionName.MONITOR_KIT_READ_ALL},${PermissionName.METER_READ_ALL},${PermissionName.CLINICIAN_READ_ALL},${PermissionName.PATIENT_GROUP_READ_ALL},${PermissionName.AUDITLOG_READ},${PermissionName.STANDARD_THRESHOLD_READ_ALL}">
    <g:if test="${!session.name}">
        <h1><g:message code="navigation.admin.menu.label"/></h1>

        <div class="leftmenu">
            <ul>
                <sec:ifAnyGranted roles="${PermissionName.QUESTIONNAIRE_READ_ALL}">
                    <li>
                        <a href="${createLink(controller: "questionnaireHeader", action: "list")}">
                            <g:message code="default.questionnaire.list.label"/>
                        </a>
                    </li>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="${PermissionName.QUESTIONNAIRE_GROUP_READ_ALL}">
                    <li>
                        <a href="${createLink(controller: "questionnaireGroup", action: "list")}">
                            <g:message code="default.questionnaireGroup.list.label"/>
                        </a>
                    </li>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="${PermissionName.MONITOR_KIT_READ_ALL}">
                    <li>
                        <a href="${createLink(controller: "monitorKit", action: "list")}">
                            <g:message code="default.monitorKit.list.label"/>
                        </a>
                    </li>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="${PermissionName.METER_READ_ALL}">
                    <li>
                        <a href="${createLink(controller: "meter", action: "list")}">
                            <g:message code="default.meter.list.label"/>
                        </a>
                    </li>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="${PermissionName.CLINICIAN_READ_ALL}">
                    <li>
                        <a href="${createLink(controller: "clinician", action: "list")}">
                            <g:message code="default.users.label"/>
                        </a>
                    </li>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="${PermissionName.PATIENT_GROUP_READ_ALL}">
                    <li>
                        <a href="${createLink(controller: "patientGroup", action: "list")}">
                            <g:message code="default.patientGroup.list.label" default="Patientgrupper"/>
                        </a>
                    </li>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="${PermissionName.ROLE_READ_ALL}">
                    <li>
                        <a href="${createLink(controller: "role", action: "list")}">
                            <g:message code="default.roles.list.label"/>
                        </a>
                    </li>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="${PermissionName.AUDITLOG_READ}">
                    <li>
                        <a href="${createLink(controller: "auditLogEntry", action: "list")}">
                            <g:message code="auditLogEntry.label" default="Hændelser"/>
                        </a>
                    </li>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="${PermissionName.STANDARD_THRESHOLD_READ_ALL}">
                    <li>
                        <a href="${createLink(controller: "standardThresholdSet", action: "list")}">
                            <g:message code="standardThreshold.label"/>
                        </a>
                    </li>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="${PermissionName.SCHEDULEWINDOW_READ_ALL}">
                    <li>
                        <a href="${createLink(controller: "scheduleWindow", action: "list")}">
                            <g:message code="scheduleWindow.label"/>
                        </a>
                    </li>
                </sec:ifAnyGranted>
                <sec:ifAnyGranted roles="${PermissionName.QUESTIONNAIRE_CREATE}">
                    <li>
                        <a href="${createLink(controller: "helpImage", action: "list")}">
                            <g:message code="helpImage.menu.label"/>
                        </a>
                    </li>
                </sec:ifAnyGranted>
            </ul>
        </div>
    </g:if>
</sec:ifAnyGranted>
