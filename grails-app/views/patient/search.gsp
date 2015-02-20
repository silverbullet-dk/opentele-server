<%@ page import="org.opentele.server.model.PatientGroup" %>
<%@ page import="org.opentele.server.model.Patient" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="title"><g:message code="default.patient.search.label"/></g:set>
    <title>${title}</title>
</head>

<body>
<div class="content">
    <h1>${title}</h1>
<!--  Filter box  -->
    <g:form method="post" action="doSearch">
        <fieldset class="form">
            <tmpl:patientform/>
        </fieldset>
    </g:form>

    <g:if test="${patients?.size() > 0}">
        <tmpl:patientlist/>
    </g:if>
</div>
</body>
</html>
