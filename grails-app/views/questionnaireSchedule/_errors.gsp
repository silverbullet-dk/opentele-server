<g:hasErrors bean="${errors}">
    <ul class="errors" role="alert">
        <g:eachError bean="${errors}" var="error">
            <li><g:message error="${error}"/></li>
        </g:eachError>
    </ul>
</g:hasErrors>
