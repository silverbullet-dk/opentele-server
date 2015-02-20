<%@ page import="org.opentele.server.core.model.types.PermissionName; org.opentele.server.model.Patient"%>
<%@ page import="org.opentele.server.core.model.types.MeasurementTypeName"%>
<%@ page import="org.opentele.server.core.model.types.MeasurementFilterType" %>

<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'patient.label', default: 'Patient')}" />
    <g:set var="title" value="${message(code: 'patient.questionnaires.title', args: [patientInstance.name.encodeAsHTML()])}"/>
    <title>${title}</title>
    <g:javascript src="knockout-2.2.0.js" />
    <g:javascript src="OpenTeleTablePreferences.js" />
    <r:require module="opentele-scroll"/>
    <link rel="stylesheet" href="${resource(dir: 'css', file: grailsApplication.config.measurement.results.tables.css)}" type="text/css">
    <script type="text/javascript">
        $(document).ready(function() {
            new QuestionnaireTableViewModel(${patientInstance.id},${questionPreferences}).init();

            $('a.acknowledge').click(function(event) {
                event.preventDefault();
                var message = '${g.message(code: 'questionnaire.confirmAcknowledgement').encodeAsJavaScript()}';
                if (confirm(message)) {
                    var automessage = $(this).attr('data-automessage');
                    var questionnaireId = $(this).attr('data-questionnaire-id');

                    window.location = '${g.createLink(controller: 'questionnaire', action: 'acknowledge', absolute:true)}/?id=' + questionnaireId + '&withAutoMessage=' + automessage;
                }
            });
        });

    </script>
</head>

<body>
		<div id="show-patient" class="content scaffold-show" role="main">
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
        <h1 class="fieldcontain">${title}</h1>
        <g:if test="${consultations > 0 || (questionnairesNumber > 0 && completedNumber > 0)}">
            <sec:ifAnyGranted roles="${PermissionName.PATIENT_PREFERENCES_WRITE}">

                <!-- Time interval chooser -->
                <div>
                    <g:render template="/measurement/timeIntervalSelector"
                        model="[controller:'patient', action:'questionnaires', pageId:'patientQuestionnaires', id:session.patientId, defaultInterval:MeasurementFilterType.MONTH]" />
                </div>

                <g:if test="${greenCompletedAndUnacknowledgedQuestionnaires}">
                    <sec:ifAnyGranted roles="${PermissionName.QUESTIONNAIRE_ACKNOWLEDGE}">
                        <div class="acknowledgeAll" style="margin: 0 1em; padding: 0 0.25em;">
                            <span>
                                <g:message code="patient.questionnaire.acknowledge.green.label"/>
                            </span><cq:renderAcknowledgeAllGreenButtons patientOverview="${patientInstance.patientOverview}"/>
                        </div>
                    </sec:ifAnyGranted>
                </g:if>

                <g:form>
                    <fieldset id="hiddenparams"></fieldset>
                    <fieldset id="patientparams">
                        <g:hiddenField name="patientID" value="${patientInstance?.id}" />
                    </fieldset>

                    <div display="hidden" id="patientPrefs" value=${questionPreferences}></div>
                    <cq:renderResultTableForPatient patientID="${patientInstance.id}" withPrefs="${true}" completedQuestionnaireResultModel="${completedQuestionnaireResultModel}" />
                    <g:if test="${completedQuestionnaireResultModel.results.size() > 0}">
                        <fieldset class="buttons">
                            <g:actionSubmit action="savePrefs" name="savePrefs" class="save" value="${g.message(code:'patient.questionnaire.savePreferredQuestions')}"  onclick="return createFormFields();" />
                            <g:if test="${patientInstance.blueAlarmQuestionnaireIDs}">
                                <g:actionSubmit action="removeBlueAlarms" name="blueAlarms" class="blue" value="${g.message(code:'patient.questionnaire.acknowledgeBlueAlarms')}" />
                            </g:if>
                            <g:else>
                                <g:actionSubmit action="removeBlueAlarms" name="blueAlarms" class="blue" value="${g.message(code:'patient.questionnaire.acknowledgeBlueAlarms')}" disabled="true" />
                            </g:else>
                        </fieldset>
                    </g:if>
                </g:form>
            </sec:ifAnyGranted>

		</g:if>
		<g:else>
			<g:if test="${questionnairesNumber > 0 && completedNumber == 0}">
				<g:message code="patient.questionnaire.nocompletequestionnaires" />
			</g:if>
			<g:else>
				<g:message code="patient.questionnaire.noquestionnaires" />
			</g:else>
		</g:else>
	</div>

	<!-- Templates for Knockout.js -->
	<script id="prefRowTemplate" type="text/html">
		<tr id="prefQuestion" class="prefQuestion" data-bind="attr: {'selectedQuestionID': $root.getQuestionID($data)}">
			<td>
                <div>
                    <select data-bind="options: $root.questions, optionsText: 'text', value: $data.questionObj, optionsCaption: '${g.message(code:'patientOverview.questions.choose')}'" data-tooltip="<g:message code="patientOverview.questions.choose.tooltip" />"></select>
                    <!-- ko if: $root.notLastRow($data) -->
                    <button id="removeBtn" class="remove" data-bind="click: function(){ $data.remove(); }" data-tooltip="<g:message code="patient.questionnaire.preferredValue.remove.tooltip" />"><r:img uri='/images/cancel.png'/></button>
                    <!-- /ko -->
                </div>
            </td>
		</tr>
	</script>
	<script id="prefRowResTemplate" type="text/html">
		<!-- ko if: $data.resultObj() -->
			<tr class="prefResult" data-bind="html: $data.resultObj().text"></tr>
		<!-- /ko -->
	</script>
	<!-- JS to copy selected preferences to Grails' hiddenfields in the (submit) form -->
	<script>
	//Could be done with afterAdd on the table binding, but 
	//no need to do it until we actually want to submit
	function createFormFields() {
		var hiddenparamsElem = $("#hiddenparams");
		$("#leftHeaderContainer tbody tr.prefQuestion").each(function(idx, elem) {
			var id = elem.getAttribute('selectedQuestionID');
			
			if((! isNaN (id-0) && id != null)) {
				hiddenparamsElem.append($('<input type="hidden" id="preferredQuestionId" name="preferredQuestionId" value="'+id+'"/>'));
			} 
		});

		return true;
	}
	</script>
</body>
</html>
