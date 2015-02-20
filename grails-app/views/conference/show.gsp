<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="conference.show.title"/></title>

    <r:require module="popup"/>
</head>
    <body>
        <h1><g:message code="conference.show.title"/></h1>
        <div id="list-patientNote" class="content" role="main">
            <g:render template="unfinishedConferences" model="[conferences: unfinishedConferences]"/>

            <p><g:message code="conference.show.description"/></p>
            <g:form action="initializeCall">
                <fieldset class="buttons">
                    <g:submitButton name="initialize" class="gonext" value="${message(code: 'conference.show.initializeCall')}" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>