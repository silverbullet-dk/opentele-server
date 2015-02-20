<tr class="thresholds">
    <g:if test="${text}">
        <th>${text}</th>
    </g:if>
    <g:each in="${["alertHigh", "warningHigh", "warningLow", "alertLow"]}" var="prop">
        <td>
            <g:select name="${prefix ? "${prefix}." : ''}${prop}"
                      from="${org.opentele.server.core.model.types.ProteinValue.values()}"
                      optionKey="${{ it.name() }}"
                      noSelection="${['': "..."]}"
                      value="${threshold[prop]?.name()}"/>
        </td>
    </g:each>
</tr>
