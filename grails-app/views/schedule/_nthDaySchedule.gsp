<div id="nthScheduleSetupContainer" class="scheduleType EVERY_NTH_DAY">
    <tmpl:/schedule/scheduleWindow scheduleType="EVERY_NTH_DAY"/>
    <div class="fieldcontain">
        <label>
            <g:message code="questionnaireSchedule.intervalDays"/>
        </label>

        <div class="nthDayInterval">
            <g:field type="number" name="dayInterval" value="${dayInterval}" size="3" maxLength="3" min="0"
                     class="twoCharacterInput"
                     data-tooltip="${message(code: 'tooltip.patient.questionnaireSchedule.create.dayInterval')}"/>
            <div class="inlined"><g:message code="questionnaireSchedule.nthDayScheduleStartingDate"/></div>
            <jq:datePicker name="startingDate" value="${startingDate}" format="dd-MM-yyyy"
                           data-tooltip="${message(code: 'tooltip.patient.questionnaireSchedule.create.startdato')}"/>
        </div>
    </div>
</div>


