<td>
    <g:if test="${rowInstance.questionnaireScheduleOverlap}">
        <div data-tooltip="${message(code: 'questionnaireGroup2QuestionnaireHeader.overlap.useGroup')}">
            <g:radio name="questionnaireGroup2Headers[${rowInstance.questionnaireGroup2Header.id}].useStandard"
                     value="true"/><schedule:humanReadableSchedule
                    schedule="${rowInstance.questionnaireGroup2Header.schedule}"/>
        </div>

        <div data-tooltip="${message(code: 'questionnaireGroup2QuestionnaireHeader.overlap.useExisting')}">
            <g:radio name="questionnaireGroup2Headers[${rowInstance.questionnaireGroup2Header.id}].useStandard"
                     value="false" checked="checked"/><schedule:humanReadableSchedule
                    schedule="${rowInstance.questionnaireSchedule}"/>
        </div>
    </g:if>
    <g:else>
        <g:hiddenField name="questionnaireGroup2Headers[${rowInstance.questionnaireGroup2Header.id}].useStandard" value="true"/>
        <schedule:humanReadableSchedule schedule="${rowInstance.questionnaireGroup2Header.schedule}"/>
    </g:else>
</td>

<td>
    ${rowInstance.questionnaireGroup2Header.questionnaireHeader.encodeAsHTML()}
</td>
<td>
    <questionnaireHeader:status tooltipPrefix="monitoringPlan"
                                questionnaireHeader="${rowInstance.questionnaireGroup2Header.questionnaireHeader}"/>
</td>
<td data-tooltip="${message(code: "questionnaireGroup2QuestionnaireHeader.questionnaire." + (rowInstance.questionnaireScheduleOverlap ? 'existsWithOverlap' : (rowInstance.questionnaireSchedule ? 'exists' : 'new')))}">
    <g:if test="${rowInstance.questionnaireScheduleOverlap}">
        <g:checkBox
                name="questionnaireGroup2Headers[${rowInstance.questionnaireGroup2Header.id}].addQuestionnaire_dummy"
                value="${true}" disabled="true"/>
        <g:hiddenField
                name="questionnaireGroup2Headers[${rowInstance.questionnaireGroup2Header.id}].addQuestionnaire"
                value="${true}"/>
    </g:if>
    <g:elseif test="${rowInstance.questionnaireSchedule}">
        <g:checkBox name="questionnaireGroup2Headers[${rowInstance.questionnaireGroup2Header.id}].addQuestionnaire"
                    value="${false}" disabled="true"/>
    </g:elseif>
    <g:else>
        <g:checkBox name="questionnaireGroup2Headers[${rowInstance.questionnaireGroup2Header.id}].addQuestionnaire"
                    value="${true}"/>
    </g:else>
</td>

