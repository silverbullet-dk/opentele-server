<%@ page import="org.opentele.server.core.model.types.MeasurementTypeName; org.opentele.server.model.Patient" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
  		<title><g:message code="standardThresholdSet.create" /></title>
  	</head>
  	<body>
  		<div id="create-standardThresholdSet" class="content scaffold-create" role="main">
  			<h1><g:message code="standardThresholdSet.createForPatientGroup" args="[patientGroup.name]" /></h1>
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
    <g:form action="saveThresholdToSet" id="${standardThresholdSetInstance?.id}">
        <fieldset class="form">
            <div class="fieldcontain">
                <label for="type">
                    <g:message code="standardThresholdSet.chooseType"/> <span class="required-indicator">*</span>
                </label>
                <g:select name="type" from="${notUsedThresholds}"
                          noSelection="[null: message(code: 'standardThresholdSet.chooseType.option')]" value="${thresholdType}"/>
            </div>

            <div id="thresholdFields">
                <g:if test="${thresholdType}">
                    <g:render template="/patient/chooseThreshold"/>
                </g:if>
            </div>

        </fieldset>
        <fieldset class="buttons">
            <g:submitButton name="saveThreshold" class="save"
                            value="${message(code: 'default.button.create.label')}"/>
        </fieldset>
    </g:form>
    <g:javascript>
        function checkEnabled() {
            var type =$('select[name="type"]').val()
            if(type === 'null') {
                $('input[name="saveThreshold"]').attr('disabled','disabled');

            } else {
                 $('input[name="saveThreshold"]').removeAttr('disabled');
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


%{--
<%@ page import="org.opentele.server.core.model.types.MeasurementTypeName; org.opentele.server.model.StandardThresholdSet" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<g:set var="entityName" value="${message(code: 'standardThresholdSet.label', default: 'StandardThresholdSet')}" />
		<title><g:message code="default.create.label" args="[entityName]" /></title>
	</head>
	<body>
		<div id="create-standardThresholdSet" class="content scaffold-create" role="main">
			<h1><g:message code="default.create.label" args="[entityName]" /></h1>
			<g:if test="${flash.message}">
			    <div class="message" role="status">${flash.message}</div>
			</g:if>

            <g:hasErrors bean="${bloodpressureThresholdInstance}">
			<ul class="errors" role="alert">
				<g:eachError bean="${bloodpressureThresholdInstance}" var="error">
				<li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if> >
                    <g:message error="${error}"/>
                </li>
				</g:eachError>
			</ul>
			</g:hasErrors>
            <g:hasErrors bean="${urineThresholdInstance}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${urineThresholdInstance}" var="error">
                        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if> >
                            <g:message error="${error}"/>
                        </li>
                    </g:eachError>
                </ul>
            </g:hasErrors>
            <g:hasErrors bean="${standardThresholdInstance}">
                <ul class="errors" role="alert">
                    <g:eachError bean="${standardThresholdInstance}" var="error">
                        <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if> >
                            <g:message error="${error}"/>
                        </li>
                    </g:eachError>
                </ul>
            </g:hasErrors>
			<g:form action="saveThresholdToSet" >
				<fieldset class="form">
                    <g:hiddenField name="id" value="${standardThresholdSetInstance?.id}" />
                    <g:hiddenField name="version" value="${standardThresholdSetInstance?.version}" />
                    <g:if test="${thresholdType == MeasurementTypeName.BLOOD_PRESSURE.toString()}">
                        <g:hiddenField name="type" value="${thresholdType}"/>
                        <g:render template="../bloodPressureThreshold/form"/>
                    </g:if>
                    <g:elseif test="${thresholdType == MeasurementTypeName.URINE.toString()}">
                        <g:hiddenField name="type" value="${thresholdType}"/>
                        <g:render template="../urineThreshold/form"/>
                    </g:elseif>
                    <g:elseif test="${thresholdType == MeasurementTypeName.URINE_GLUCOSE.toString()}">
                        <g:hiddenField name="type" value="${thresholdType}"/>
                        <g:render template="../urineGlucoseThreshold/form"/>
                    </g:elseif>
                    <g:else>
                        <g:hiddenField name="type" value="${thresholdType}"/>
                        <g:render template="../numericThreshold/form"/>
                    </g:else>
				</fieldset>
				<fieldset class="buttons">
					<g:submitButton name="saveThresholdToSet" class="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
--}%
