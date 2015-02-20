<td>${headline}</td>
<g:set var="waitingDisplay" value="${measurement.waiting ? 'block' : 'none'}"/>
<g:set var="loadedDisplay" value="${measurement.waiting ? 'none' : 'block'}"/>

<td class="${measurement.waiting ? 'waiting-measurement' : 'loaded-measurement'}">
    <span>
        <g:hiddenField name="id" value="${measurement.id}"/>
        <g:hiddenField name="conferenceVersion" value="${measurement.conference.version}"/>
        ${body()}
    </span>
</td>
<td><g:checkBox name="included" value="${measurement.included}"/></td>
<td><g:img dir="images" file="delete.png" class="delete"/></td>