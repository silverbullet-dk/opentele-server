<%@ page import="org.opentele.server.core.model.types.PermissionName; org.opentele.server.model.Patient"%>
<%@ page import="org.opentele.server.core.model.types.MeasurementTypeName"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code:"patient.label", default: 'Patient')}" />
<title><g:message code="patient.completed.questionnaire" /></title>
<g:javascript>
    function showIgnoreReasonInput() {
        $("#ignoreReasonInput").css("display", "inline-block");
        $("#ignoreReasonInput").css("display", "inline-block")
        $("#ignoreReasonButton").css("display", "none")
    }
</g:javascript>
</head>

<body>

	<div id="show-patient" class="content scaffold-show" role="main">
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>

		<h1 class="fieldcontain">
			<g:message code="patient.completed.questionnaire" />
			:
			${completedQuestionnaire.patientQuestionnaire.name}
			-
			${completedQuestionnaire.patientQuestionnaire.version}. (
			<g:formatDate formatName="default.date.format"
				date="${completedQuestionnaire?.uploadDate}" />
			)
		</h1>

		<ol class="property-list patient" id="acknowledgedQuestionnaireInfo">
			<li class="fieldcontain">
                <span id="user-label" class="property-label">
                    <g:message code="patient.acknowlegded.by" />:
                </span>
                <g:if test="${completedQuestionnaire.acknowledgedBy}">
					<span class="property-value" aria-labelledby="user-label">
                        ${completedQuestionnaire.acknowledgedBy}
					</span>
				</g:if>
				<g:else>
					<span class="property-value" aria-labelledby="user-label">
                        <g:message code="questionnaire.not.acknowledged" />
                    </span>
				</g:else>
            </li>
			<li class="fieldcontain">
                <span id="user-label" class="property-label">
                    <g:message code="patient.acknowledge.date" />:
                </span>
                <span class="property-value" aria-labelledby="user-label">
                    <g:formatDate formatName="default.date.format" date="${completedQuestionnaire.acknowledgedDate}" />
                    <g:if test="${completedQuestionnaire.showAcknowledgementToPatient}">
                        <span class="property-value" aria-labelledby="user-label">
                            (<g:message code="patient.acknowledgement.sent.to.patient" />)
                        </span>
                    </g:if>
                </span>
            </li>
			<li class="fieldcontain">
                <span id="user-label" class="property-label">
                    <g:message code="patient.acknowledge.note" />:
                </span>
                <span class="property-value fullheight" aria-labelledby="user-label">
					${completedQuestionnaire.acknowledgedNote}
			    </span>
            </li>
		</ol>

		<g:if test="${completedQuestionnaire._questionnaireIgnored}">
			<strong>
                <g:message code="questionnaire.ignored" args="${[completedQuestionnaire.questionnareIgnoredBy as String, completedQuestionnaire.questionnaireIgnoredReason as String]}" />
			</strong>
			<br>
		</g:if>
		<div>
            <g:form controller="questionnaire">
                <g:hiddenField name="ignoreNodeReason" id="ignoreNodeReason" value="" />
                <cq:showQuestionnaire questionnaireId="${completedQuestionnaire.id}" />
            </g:form>
		</div>

        <g:form action="toggleIgnoreQuestionnaire" controller="questionnaire" id="${completedQuestionnaire.id}" params="[ignoreNavigation: 'true']">
            <fieldset class="buttons">
                <g:hiddenField name="id" value="${completedQuestionnaire.id}" />
                <g:if test="${!completedQuestionnaire.acknowledgedBy}">
                    <sec:ifAnyGranted roles="${PermissionName.QUESTIONNAIRE_ACKNOWLEDGE}">
                        <div>
                            <label for="note">
                                <g:message code="patient.overview.acknowledge.note.label" />
                            </label>
                            <g:textArea class="note" name="note" />
                        </div>
                    </sec:ifAnyGranted>
                </g:if>
                <g:render template="../printable" />
                <g:if test="${!completedQuestionnaire.acknowledgedBy}">
                    <sec:ifAnyGranted roles="${PermissionName.QUESTIONNAIRE_ACKNOWLEDGE}">
                        <g:actionSubmit class="acknowledge" action="acknowledge"
                                    value="${message(code: "patient.overview.acknowledge.label")}"
                                    onclick="return true" />
                    </sec:ifAnyGranted>
                </g:if>
                <g:if test="${completedQuestionnaire._questionnaireIgnored}">
                    <sec:ifAnyGranted roles="${PermissionName.QUESTIONNAIRE_IGNORE}">
                        <g:actionSubmit value="${message(code: "patient.questionnaire.unIgnoreQuestionnaire")}"
                            class="acknowledge" action="toggleIgnoreQuestionnaire" data-tooltip="${message(code: 'tooltip.patient.questionnaire.unignoreQuestionnaire')}" />
                    </sec:ifAnyGranted>
                </g:if>
                <g:else>
                    <sec:ifAnyGranted roles="${PermissionName.QUESTIONNAIRE_IGNORE}">
                        <div id="ignoreReasonInput" style="display: none;">
                            <span><g:message code="questionnaire.ignoreReason.label" /></span>
                            <g:textArea name="ignoreReason"> </g:textArea>
                            <g:actionSubmit value="${message(code: "patient.questionnaire.ignoreWholeQuestionnaire")}" class="cancel"
                                action="toggleIgnoreQuestionnaire" />
                        </div>
                        <input id="ignoreReasonButton" type="button"
                            value="${message(code: "patient.questionnaire.ignoreWholeQuestionnaire")}" class="cancel"
                            onClick="showIgnoreReasonInput()" data-tooltip="${message(code: 'tooltip.patient.questionnaire.ignoreQuestionnaire')}" />
                    </sec:ifAnyGranted>
                </g:else>
            </fieldset>
        </g:form>
	</div>
</body>
</html>
