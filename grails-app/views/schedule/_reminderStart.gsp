<div id="reminderStartContainer" class="scheduleType WEEKDAYS MONTHLY EVERY_NTH_DAY SPECIFIC_DATE">
    <div class="fieldcontain">
        <label for="reminderStartMinutes" data-tooltip="${message(code: "schedule.reminderStart.tooltip")}">
            <g:message code="schedule.reminderStart.label"/>
        </label>

            <g:field type="number" name="reminderStartMinutes" value="${reminderStartMinutes}"
               class='input-mini'
               data-tooltip="${message(code: 'tooltip.patient.questionnaireSchedule.create.reminderStart')}" />
        <span><g:message code="questionnaireSchedule.minutesBefore"/></span>
    </div>
</div>


