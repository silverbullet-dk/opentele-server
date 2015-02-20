<td><g:link controller="questionnaireGroup2QuestionnaireHeader" action="edit" id="${rowInstance.id}">
    <g:if test="${rowInstance.schedule}">
    <schedule:humanReadableSchedule schedule="${rowInstance.schedule}"
                                    style="font-weight: ${rowInstance.standardSchedule ? 'bold' : 'normal'};"
                                    data-tooltip="${message(code: rowInstance.standardSchedule ? "questionnairegroup2questionnaireheader.schedule.from.group" : "questionnairegroup2questionnaireheader.schedule.from.questionnaire")}"/>
    </g:if>
    <g:else>
        <span data-tooltip="${message(code: "questionnairegroup2questionnaireheader.schedule.not.active.tooltip")}">
        <g:message code="questionnairegroup2questionnaireheader.schedule.not.active"/>
        </span>
    </g:else>
</g:link>
</td>

<td>
    ${fieldValue(bean: rowInstance, field: "questionnaireHeader.name")}
</td>
<td>
    <questionnaireHeader:status tooltipPrefix="monitoringPlan"
                                questionnaireHeader="${rowInstance.questionnaireHeader}"/>
</td>
<td class="buttons">
    <g:link controller="questionnaireGroup2QuestionnaireHeader" action="edit"
            id="${rowInstance.id}"
            data-tooltip='${message(code: 'tooltip.patient.monitorPlan.questionnaire.edit')}'>
        <img src='${resource(dir: 'images', file: 'edit.png')}'/>
    </g:link>

    <g:remoteLink controller="questionnaireGroup2QuestionnaireHeader"
                  before="if(!confirm('${message(code: 'default.confirm.msg', args: ['på du vil fjerne spørgeskemaet'])}')) return false"
                  action="delete" onComplete="location.reload(true);"
                  id="${rowInstance.id}"
                  data-tooltip="${message(code: 'tooltip.questionnaireGroup.questionnaire.remove')}">
        <img src="${resource(dir: 'images', file: 'delete.png')}"/>
    </g:remoteLink>
</td>
