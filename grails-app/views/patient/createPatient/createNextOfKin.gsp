<%@ page import="org.opentele.server.model.Patient"%>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName" value="${message(code: 'patient.label', default: 'Patient')}" />
    <title>
        <g:message code="patient.create.flow.createNextofKin.label"/>
    </title>
</head>
<body>

<div id="create-patient" class="content scaffold-create" role="main">
    <h1>
        <g:message code="patient.create.flow.createNextofKin.label"/>
    </h1>

    <g:if test="${error}">
        <div class="errors" role="status">
            ${error}
        </div>
    </g:if>
    <g:hasErrors bean="${nextOfKinPersonInstance}">
        <ul class="errors" role="alert">
            <g:eachError bean="${nextOfKinPersonInstance}" var="error">
                <li>
                    <g:message error="${error}" />
                </li>
            </g:eachError>
        </ul>
    </g:hasErrors>


    <g:form>
        <fieldset class="form">
            <g:render template="../nextOfKinPerson/form" />
        </fieldset>

        <fieldset class="buttons">
            <g:submitButton name="done" class="save" value="${message(code: 'patient.create.flow.button.save.label', default: 'Save')}" />
        </fieldset>
    </g:form>
</div>
</body>
</html>
