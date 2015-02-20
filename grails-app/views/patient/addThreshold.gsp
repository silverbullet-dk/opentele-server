<%@ page import="org.opentele.server.core.model.types.MeasurementTypeName; org.opentele.server.model.Patient" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="patient.threshold.title.label"/></title>
</head>

<body>
<div id="create-addThreshold" class="content scaffold-create" role="main">
    <h1><g:message code="patient.threshold.title.label"/></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
    <g:hasErrors bean="${standardThresholdInstance}">
        <ul class="errors" role="alert">
            <g:eachError bean="${standardThresholdInstance}" var="error">
                <li><g:message error="${error}"/></li>
            </g:eachError>
        </ul>
    </g:hasErrors>
    <g:form action="saveThresholdToPatient">
        <fieldset class="form">
            <g:hiddenField name="id" value="${patientInstance?.id}"/>
            <div class="fieldcontain">
                <label for="type">
                    <g:message code="patient.threshold.choseType.label"/> <span class="required-indicator">*</span>
                </label>
                <g:select name="type" from="${notUsedThresholds}"
                          noSelection="[null: message(code: 'patient.threshold.chooseType')]" value="${thresholdType}"/>
            </div>

            <div id="thresholdFields">
                <g:if test="${thresholdType}">
                    <g:render template="chooseThreshold"/>
                </g:if>
            </div>

        </fieldset>
        <fieldset class="buttons">
            <g:submitButton name="saveThresholdToPatient" class="save"
                            value="${message(code: 'default.button.create.label', default: 'Create')}"/>
        </fieldset>
    </g:form>
    <g:javascript>
        function checkEnabled() {
            var type =$('select[name="type"]').val()
            if(type === 'null') {
                $('input[name="saveThresholdToPatient"]').attr('disabled','disabled');

            } else {
                 $('input[name="saveThresholdToPatient"]').removeAttr('disabled');
            }
        }
        $('select[name="type"]').on('change', function () {
            var type =$(this).val()
            if(type === 'null') {
                $('#thresholdFields').empty();
                checkEnabled();
            } else {
                $('#thresholdFields').load('${createLink(action: 'chooseThreshold')}', {type: $(this).val()}, checkEnabled )
            }
        });
        checkEnabled();
    </g:javascript>

</div>
</body>
</html>
