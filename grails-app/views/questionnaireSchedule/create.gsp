<%@ page import="org.opentele.server.model.QuestionnaireSchedule"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName" value="${message(code: 'questionnaireSchedule.label', default: 'QuestionnaireSchedule')}" />
<title><g:message code="default.give.label"
		args="[entityName]" /></title>
</head>

<r:script>
    $(function() {
        var questionnaireSelector = $('#selectedQuestionnaireHeaderId');

        var updateScheduleForQuestionnaire = function() {
            questionnaireSelector.prop('disabled', 'disabled');

            $.getJSON('questionnaireScheduleData', { questionnaireHeaderId: questionnaireSelector.val(), 'monitoringPlan.id': ${monitoringPlan.id} }, function(data) {
                $('input[name="type"][value="' + data.scheduleType + '"]').prop('checked', true);
                $('#questionnaireScheduleDetails').replaceWith(data.details);

                questionnaireSelector.removeAttr('disabled');
            });
        };

        questionnaireSelector.change(updateScheduleForQuestionnaire);

        // Make sure that the initially selected questionnaire gets its schedule set as well
        updateScheduleForQuestionnaire();
    });
</r:script>

<body>
	<div id="create-questionnaireSchedule" class="content scaffold-create"
		role="main">
		<h1>
			<g:message code="default.give.label" args="[entityName]" />
		</h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
        <tmpl:errors/>


		<g:form action="save">
			<fieldset class="form">
				<g:render template="form" />
			</fieldset>
			<fieldset class="buttons">
				<g:submitButton name="create" class="save" value="${message(code: 'default.button.give.label')}" />
                <g:link class="cancel"  action="show"
					                    controller="monitoringPlan"
					                    id="${session.patientId}">
					<g:message code="default.button.goback.label" />
				</g:link>
			</fieldset>
		</g:form>
	</div>
</body>
</html>
