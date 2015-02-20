<div id="nthScheduleSetupContainer" class="scheduleType SPECIFIC_DATE">
    <tmpl:/schedule/scheduleWindow scheduleType="SPECIFIC_DATE"/>
    <div class="fieldcontain">
        <label for="specificDate">
            <g:message code="questionnaireSchedule.specificDate"/>
        </label>
        <jq:datePicker name="specificDate" format="dd-MM-yyyy" value="${specificDate}"
                       data-tooltip="${message(code: 'tooltip.patient.questionnaireSchedule.create.specificdate')}"/>
    </div>
</div>


