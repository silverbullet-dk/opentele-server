<%@ page import="org.opentele.server.core.model.types.PermissionName" %>
<h1><g:message code="navigation.main.menu.label"/></h1>

<div class="leftmenu">
<!-- Hvis der IKKE er valgt en patient -->
    <g:if test="${!session.name}">
        <ul>
            <sec:ifAnyGranted roles="${PermissionName.PATIENT_READ_ALL}">
                <li>
                    <otformat:menuLink controller="patientOverview" action="index"
                                       code="default.measurement.overview.label"/>
                </li>
                <li>
                    <otformat:menuLink controller="patient" action="search" code="default.patient.find.label"/>
                </li>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="${PermissionName.PATIENT_CREATE}">
                <li>
                    <otformat:menuLink controller="patient" action="createPatient" code="default.patient.create.label"/>
                </li>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="${PermissionName.PATIENT_NOTE_READ_ALL_TEAM}">
                <li>
                    <otformat:menuLink controller="patientNote" action="listTeam" code="default.patient.note.label"/>
                </li>
            </sec:ifAnyGranted>
        </ul>
    </g:if>
    <g:else>
        <!-- Hvis der ER valgt en patient -->
        <ul>
            <sec:ifAnyGranted roles="${PermissionName.PATIENT_READ_ALL}">
                <li>
                    <otformat:menuLink controller="patientOverview" action="index"
                                       code="default.measurement.overview.label"/>
                </li>
                <li>
                    <otformat:menuLink controller="patient" action="search" code="default.patient.find.label"/>
                </li>
            </sec:ifAnyGranted>
        </ul>
    </g:else>
</div>
