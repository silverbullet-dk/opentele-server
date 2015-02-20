<%@ page import="org.opentele.server.core.model.types.PermissionName; org.opentele.server.provider.constants.Constants" %>
<!-- Menu logget ind som kliniker: Patient-specifik del -->
<!-- Hvis der ER valgt en patient -->
<g:if test="${session[Constants.SESSION_NAME]}">
    <h1><g:message code="navigation.patient.menu.label"/></h1>

    <!-- Patient-navneboks -->
    <div id="namebox" class="leftmenu">
        <strong>${session[Constants.SESSION_NAME].encodeAsHTML()}</strong>
        <br/>

        <g:message code="main.SSN"/>:${session[Constants.SESSION_CPR].encodeAsHTML()}
        <br/>

        <g:if test="${session[Constants.SESSION_GESTATIONAL_AGE]}">
            <g:message code="main.gestationalAge"/>:${session[Constants.SESSION_GESTATIONAL_AGE].encodeAsHTML()}
            <br/>
        </g:if>

        <g:if test="${session[Constants.SESSION_COMMENT]}">
            <div class="fieldcontain" data-tooltip="${session[Constants.SESSION_COMMENT_ALL].encodeAsHTML()}">
                <g:message alt="TEST" code="main.comment"/>:${session[Constants.SESSION_COMMENT].encodeAsHTML()}
            </div>
                <br/>
        </g:if>

        <hr/>
        <g:message code="main.phone"/>:
        <g:if test="${session[Constants.SESSION_PHONE]}">
            <otformat:formatPhoneNumber message="${session[Constants.SESSION_PHONE]}"/>
        </g:if>
        <br/>
        <g:message code="main.mobilePhone"/>:
        <g:if test="${session[Constants.SESSION_MOBILE_PHONE]}">
            <otformat:formatPhoneNumber message="${session[Constants.SESSION_MOBILE_PHONE]}"/>
        </g:if>
        <br/>
        <g:if test="${session[Constants.SESSION_FIRST_RELEATIVE]}">
            <br/>
            <g:message code="main.firstRelative"/>:<br/>
            ${session[Constants.SESSION_FIRST_RELEATIVE]?.nameAndRelation?.encodeAsHTML()} (Tlf. ${session[Constants.SESSION_FIRST_RELEATIVE]?.phone?.encodeAsHTML()})
            <br/>
        </g:if>
        <g:if test="${session[Constants.SEESION_DATARESPONSIBLE]}">
            <br/>
            <g:message code="main.dataResponsible"/>:<br/>
            ${session[Constants.SEESION_DATARESPONSIBLE]?.toString()?.encodeAsHTML()}
            <br/>
        </g:if>
    </div>

    <div class="leftmenu">
        <ul>
            <sec:ifAnyGranted roles="${PermissionName.COMPLETED_QUESTIONNAIRE_READ_ALL}">
                <li>
                    <otformat:menuLink controller="patient" action="questionnaires" paramId="${session.patientId}"
                                       code="default.patient.questionnaire.label"/>
                </li>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="${PermissionName.PATIENT_CONSULTATION}">
                <li>
                    <otformat:menuLink controller="consultation" action="addmeasurements" paramId="${session.patientId}"
                                       code="default.patient.addmeasurements.label"/>
                </li>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="${PermissionName.PATIENT_READ}">
                <li>
                    <otformat:menuLink controller="patient" action="show" paramId="${session.patientId}"
                                       code="default.patient.show.label"/>
                </li>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="${PermissionName.MESSAGE_READ}">
                <message:canUseMessaging>
                    <li>
                        <otformat:menuLink controller="patient" action="messages" paramId="${session.patientId}"
                                           code="default.patient.messsages.label" messageCount="true"/>
                    </li>
                </message:canUseMessaging>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="${PermissionName.PATIENT_NOTE_READ_ALL}">
                <li>
                    <otformat:menuLink controller="patientNote" action="list" paramId="${session.patientId}"
                                       code="default.patient.notes.label"/>
                </li>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="${PermissionName.PASSIVE_INTERVAL_READ_ALL}">
                <li>
                    <otformat:menuLink controller="passiveInterval" action="list" paramId="${session.patientId}"
                                       code="default.patient.passiveInterval.label"/>
                </li>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="${PermissionName.MEASUREMENT_READ}">
                <li>
                    <otformat:menuLink mapping="patientGraphs" params="${[patientId: session.patientId]}"
                                       code="default.patient.graphs.label"/>
                </li>
                <li>
                    <otformat:menuLink mapping="patientMeasurements" params="${[patientId: session.patientId]}"
                                       code="default.patient.measurements.label"/>
                </li>
            </sec:ifAnyGranted>
            <sec:ifAllGranted roles="${PermissionName.MONITOR_KIT_READ_ALL}, ${PermissionName.METER_READ_ALL}">
                <li>
                    <otformat:menuLink controller="patient" action="equipment" paramId="${session.patientId}"
                                       code="default.patient.equipment.label"/>
                </li>
            </sec:ifAllGranted>
            <sec:ifAnyGranted roles="${PermissionName.MONITORING_PLAN_READ}">
                <li>
                    <otformat:menuLink controller="monitoringPlan" action="show" paramId="${session.patientId}"
                                       code="default.patient.monitoringplan.label"/>
                </li>
            </sec:ifAnyGranted>
            <g:if test="${grailsApplication.config.video.enabled}">
                <sec:ifAnyGranted roles="${PermissionName.VIDEO_CALL}">
                    <li>
                        <otformat:menuLink controller="conference" action="show"
                                           code="default.patient.conference.label"/>
                    </li>
                </sec:ifAnyGranted>
            </g:if>
        </ul>
    </div> <!-- Slut patient navigation -->
</g:if>
