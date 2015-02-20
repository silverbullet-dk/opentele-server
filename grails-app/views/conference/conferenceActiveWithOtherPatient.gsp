<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="conference.conferenceActiveWithOtherPatient.title"/></title>
</head>
<body>
    <h1><g:message code="conference.conferenceActiveWithOtherPatient.header"/></h1>
    <p><g:message code="conference.conferenceActiveWithOtherPatient.description"/>
    <g:form action="endConference">
        <fieldset class="buttons">
            <g:submitButton name="close" class="delete" value="${message(code: 'conference.conferenceActiveWithOtherPatient.close')}" />
        </fieldset>
    </g:form>
    </p>
</body>
</html>