<%@ page import="org.opentele.server.core.model.types.MeasurementTypeName; org.opentele.server.model.Patient" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'patient.label')}"/>
    <title>
        <g:message code="patient.create.flow.thresholdValues.label"/></title>
</head>

<body>
<div id="create-patient" class="content scaffold-create" role="main">
    <h1>
        <g:message code="patient.create.flow.thresholdValues.label"/>
    </h1>
    <g:if test="${error}">
        <div class="errors" role="status">
            ${error}
        </div>
    </g:if>
    <g:if test="${patientInstance.thresholdSetWasReduced}">
        <div class="errors" role="status">
            <g:message code="patient.create.flow.thresholdValues.duplicates.label"/>
        </div>
    </g:if>
    <ul class="errors" role="alert">
        <g:each in="${patientInstance.thresholds}" var="threshold">
            <g:hasErrors bean="${threshold}">
                <g:eachError bean="${threshold}" var="error">
                    <li>
                        <g:message code="threshold.${threshold.type}"/>: <g:message error="${error}"/>
                    </li>
                </g:eachError>
            </g:hasErrors>
        </g:each>
    </ul>
    <g:form>

        <table>
            <thead>
            <tr>
                <th>${message(code: 'threshold.type')}</th>
                <th>${message(code: 'threshold.alertHigh')}</th>
                <th>${message(code: 'threshold.warningHigh')}</th>
                <th>${message(code: 'threshold.warningLow')}</th>
                <th>${message(code: 'threshold.alertLow')}</th>
            </tr>
            </thead>
            <tbody>
            <g:each in="${patientInstance.thresholds.sort { g.message(code: 'threshold.' + it.type) }}" var="threshold">
                <g:if test="${threshold.type.name == MeasurementTypeName.BLOOD_PRESSURE}">
                    <tmpl:/bloodPressureThreshold/thresholds threshold="${threshold}"
                                                             text="${g.message(code: 'threshold.' + threshold.type)}"
                                                             prefix="${threshold.type.name}"/>
                </g:if>
                <g:elseif test="${threshold.type.name == MeasurementTypeName.URINE}">
                    <tmpl:/urineThreshold/threshold threshold="${threshold}" text="${g.message(code: 'threshold.' + threshold.type)}"
                                                    prefix="${threshold.type.name}"/>

                </g:elseif>
                <g:elseif test="${threshold.type.name == MeasurementTypeName.URINE_GLUCOSE}">
                    <tmpl:/urineGlucoseThreshold/threshold threshold="${threshold}" text="${g.message(code: 'threshold.' + threshold.type)}"
                                                           prefix="${threshold.type.name}"/>
                </g:elseif>
                <g:else>
                    <tmpl:/numericThreshold/threshold threshold="${threshold}" text="${g.message(code: 'threshold.' + threshold.type)}"
                                                      prefix="${threshold.type.name}"/>
                </g:else>
            </g:each>
            </tbody>
        </table>
        <script lang="javascript">
            $('.thresholds')
                    .on('focus', 'input[type="text"]', function () {
                        var input = $(this);
                        input.css('background-color', '');
                    })
                    .on('blur', 'input[type="text"]', function () {
                        var input = $(this);
                        if (!input.val()) {
                            input.css('background-color', "#C2C2C2")
                        }
                    })
                    .on('click', 'img', function () {
                        var input = $(this).prev();
                        input.css('background-color', '#C2C2C2').val('')
                    })
                    .find('input[type="text"]')
                    .each(function () {
                        var input = $(this);
                        if (!input.val() || input.val() == "") {
                            input.css('background-color', "#C2C2C2")
                        }
                    });
        </script>

        <fieldset class="buttons">
            <g:submitButton name="previous" class="goback"
                            value="${message(code: 'patient.create.flow.button.previous.label')}"/>
            <g:submitButton name="next" class="gonext"
                            value="${message(code: 'patient.create.flow.button.next.label')}"/>
            <g:submitButton name="saveAndShow" class="save"
                            value="${message(code: 'patient.create.flow.button.saveAndExit.label')}"
                            data-tooltip="${message(code: 'patient.create.flow.thresholdValues.finish.tooltip')}"/>
            <g:submitButton name="saveAndGotoMonplan" class="save"
                            value="${message(code: 'patient.create.flow.button.saveAndExitToMonplan.label')}"
                            data-tooltip="${message(code: 'patient.create.flow.thresholdValues.saveAndExitToMonitoringPlan.tooltip')}"/>
        </fieldset>
    </g:form>
</div>
</body>
</html>
