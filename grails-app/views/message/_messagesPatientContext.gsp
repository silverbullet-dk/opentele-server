<g:each in="${messages}">
    <div class="wrapPatientMessage">
        <div class="${it.sentByPatient ? 'byPatient' : 'byHospital'} patientMessage ${it.isRead ? '' : 'unreadMessage'}">
            <i><g:message code="messages.title.label" args="${[it.title.encodeAsHTML(), formatDate(format:'dd-MM-yyyy',date:it.sendDate)]}"/></i>
            <br/>
            <p>${it.text.encodeAsHTML()}</p>
            <i><g:message code="messages.${it.isRead ? 'isRead':'isNotRead'}.label" args="${[formatDate(format:'dd-MM-yyyy',date:it.readDate)]}"/></i>
            <br/>

            <g:if test="${it.sentByPatient}">
                <g:link class="edit" controller="message" action="reply" id="${it?.id}">
                    <g:message code="messages.reply.label"/>
                    <br/>
                </g:link>
            </g:if>

            %{-- On messages from clinicians: Mark message unread  --}%
            <g:if test="${it.isRead && it.sentByPatient}">
                <g:link class="edit" controller="message" action="unread" id="${it?.id}">
                    <g:message code="messages.markAsUnread.label"/>
                </g:link>
            </g:if>
            %{-- On messages from clinicians: Mark message read  --}%
            <g:if test="${!it.isRead && it.sentByPatient}">
                <g:link class="edit" controller="message" action="read" id="${it?.id}">
                    <g:message code="messages.markAsRead.label"/>
                </g:link>
            </g:if>
        </div>
    </div>
</g:each>
