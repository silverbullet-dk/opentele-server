<td><g:link controller="questionnaireSchedule" action="edit" id="${rowInstance.id}">
    <schedule:humanReadableSchedule schedule="${rowInstance}"/>
</g:link>
</td>

<td>
    ${fieldValue(bean: rowInstance, field: "questionnaireHeader.name")}
</td>
<td>
    <questionnaireHeader:status tooltipPrefix="monitoringPlan" questionnaireHeader="${rowInstance.questionnaireHeader}"/>
</td>
<td class="buttons">
    <g:link controller="questionnaireSchedule" action="edit"
            id="${rowInstance.id}"
            data-tooltip='${message(code: 'tooltip.patient.monitorPlan.questionnaire.edit')}'>
        <img src='${resource(dir: 'images', file: 'edit.png')}'/>
    </g:link>

    <g:remoteLink controller="questionnaireSchedule"
                  before="if(!confirm('${message(code: 'questionnaireSchedule.confirmDelete')}')) return false"
                  action="del" onComplete="location.reload(true);"
                  id="${rowInstance.id}"
                  data-tooltip="${message(code: 'tooltip.patient.monitorPlan.questionnaire.remove')}">
        <img src="${resource(dir: 'images', file: 'delete.png')}"/>
    </g:remoteLink>
</td>
