<div id="monthlyScheduleSetupContainer" class="scheduleType MONTHLY">
    <tmpl:/schedule/scheduleWindow scheduleType="MONTHLY"/>
    <div class="fieldcontain">
        <label for="daysInMonth">
            <g:message code="questionnaireSchedule.daysInMonth"/>
        </label>
        <g:select name="daysInMonth" from="${(1..28)}" value="${daysInMonth}" class="input-small" multiple="yes" data-tooltip="${message(code: 'tooltip.patient.questionnaireSchedule.create.dayInMonth')}"/>
    </div>
</div>


