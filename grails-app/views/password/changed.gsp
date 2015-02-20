<!doctype html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta name="layout" content="main">
    <title><g:message code="password.changed.label"/></title>
</head>
<body>
<div id="edit-clinician" class="content scaffold-edit" role="main">
    <h1>
        <g:message code="password.changed.label"/>
    </h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">
            ${flash.message}
        </div>
    </g:if>

</div>
</body>
</html>
