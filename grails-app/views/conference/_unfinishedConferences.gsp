<g:if test="${!conferences.empty}">
    <g:each in="${conferences.sort { it.createdDate }.reverse()}" var="conference">
        <p class="buttons">
            <g:message code="conference.unfinished" args="[conference.clinician.name.encodeAsHTML(), g.formatDate(date: conference.createdDate)]"/>
            <g:if test="${conference.clinician == clinician}">
                <g:link controller="conferenceMeasurements" action="show" id="${conference.id}" class="edit popup" style="float:none"><g:message code="conference.continueEditing"/></g:link>
            </g:if>
        </p>
    </g:each>
</g:if>
