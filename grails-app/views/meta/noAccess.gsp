<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="entityName"
           value="${message(code: 'patient.label', default: 'Patient')}" />
    <title><g:message code="patient.no.access" /></title>
</head>
<body>
    <div id="show-patient" class="content scaffold-show" role="main">
        <g:message code="patient.no.access"/>
    </div>
</body>
</html>
