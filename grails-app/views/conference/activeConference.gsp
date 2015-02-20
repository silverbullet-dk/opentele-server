<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="conference.activeConference.title"/></title>

    <r:require module="popup"/>
</head>
<body>
    <h1><g:message code="conference.activeConference.header"/></h1>
    <p><g:message code="conference.activeConference.description"/></p>

    <g:render template="unfinishedConferences" model="[conferences: unfinishedConferences, clinician: clinician]"/>

    <p>
        <g:form action="endConference">
            <fieldset class="buttons">
                <g:if test="${flash.conferenceToEdit}">
                    <g:link controller="conferenceMeasurements" action="show" id="${flash.conferenceToEdit}" class="button edit popup"><g:message code="conference.activeConference.specifyMeasurements"/></g:link>
                </g:if>

                <g:submitButton name="close" class="delete" value="${message(code: 'conference.activeConference.close')}" />
            </fieldset>
        </g:form>
    </p>
</body>
</html>