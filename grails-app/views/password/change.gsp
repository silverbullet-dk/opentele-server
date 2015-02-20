<!doctype html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta name="layout" content="main">
    <title><g:message code="password.change.label"/></title>
</head>
<body>
<div id="edit-clinician" class="content scaffold-edit" role="main">
    <h1>
        <g:message code="password.change.label"/>
    </h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">
            ${flash.message}
        </div>
    </g:if>
    <g:if test="${flash.error}">
        <g:message code="${flash.error}" args="${flash.args}" default="${flash.default}"/>
    </g:if>
    <g:hasErrors bean="${command}">
        <ul class="errors" role="alert">
            <g:eachError bean="${command}" var="error">
                <li
                    <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                        error="${error}" /></li>
            </g:eachError>
        </ul>
    </g:hasErrors>
        <g:form method="post">
            <fieldset class="form">
                <div class="fieldcontain required">
                    <label for="currentPassword">
                        <g:message code="passwordCommand.currentPassword.label" /><span class="required-indicator">*</span>
                    </label>
                    <g:passwordField name="currentPassword" />
                    <br/>

                    <label for="newpassword">
                        <g:message code="passwordCommand.password.label" /><span class="required-indicator">*</span>
                    </label>
                    <g:passwordField name="password" />
                    <br/>

                    <label for="reenteredpassword">
                        <g:message code="passwordCommand.passwordRepeat.label" /> <span class="required-indicator">*</span>
                    </label>
                    <g:passwordField name="passwordRepeat" />
                </div>
            </fieldset>
            <fieldset class="buttons">
                <g:actionSubmit class="save" action="update" value="${message(code: 'default.update', default: 'Update')}" />
            </fieldset>
        </g:form>
</div>
</body>
</html>
