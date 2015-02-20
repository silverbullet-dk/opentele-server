<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main"/>
    <title><g:message code='monitoringPlan.questionnaireGroup.addGroup'/>
    </title>
</head>

<body>
<div id="create-questionnaireSchedule" class="content scaffold-create"
     role="main">
    <h1>
        <g:message code='monitoringPlan.questionnaireGroup.addGroup'/>
    </h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">
            ${flash.message}
        </div>
    </g:if>
    <g:hasErrors bean="${questionnaireSchedule}">
        <ul class="errors" role="alert">
            <g:eachError bean="${questionnaireSchedule}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>>
                    <g:message error="${error}"/>
                </li>
            </g:eachError>
        </ul>
    </g:hasErrors>
    <g:form action="addQuestionnaireGroup">
        <g:hiddenField name="monitoringPlan.id" value="${command.monitoringPlan.id}"/>
        <div class="fieldcontain ${hasErrors(bean: command, field: 'questionnaireGroup', 'error')} required">
            <label for="questionnaire">
                <g:message code="monitoringPlan.questionnaireGroup.questionnaireGroup.label"/> <span
                    class="required-indicator">*</span>
            </label>
            <g:select name="questionnaireGroup.id" from="${command.questionnaireGroups}" optionKey="id"/>
        </div>

        <div id="pickQuestionnaires">

        </div>

        <div class="buttons">
            <g:submitButton name="submit" class="save"

                            value="${message(code: 'questionnaireSchedule.addQuestionnaireGroup.button')}"/>
        </div>
    </g:form>

    <g:javascript>
        $('select[name="questionnaireGroup.id"]').on('change',function () {
            $('#pickQuestionnaires').load("${createLink(action: "pickQuestionnaireGroup")}", $('form').serializeArray())
        }).trigger('change')
    </g:javascript>
</div>
</body>
</html>
