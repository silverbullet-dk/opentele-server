<%@ page import="org.opentele.server.core.model.Schedule" %>
<div id="questionnaireScheduleDetails">
    <g:each in="${Schedule.ScheduleType.values() - Schedule.ScheduleType.UNSCHEDULED}" var="scheduleType">
        <div class="scheduleType ${scheduleType.toString()}">
            <h1 class="nav-header"><g:message code="schedule.scheduleType.${scheduleType.toString()}.label"/></h1>
        </div>
    </g:each>
    <tmpl:/schedule/timesOfDay/>
    <tmpl:/schedule/reminderStart/>
    <tmpl:/schedule/weekdaysSchedule/>
    <tmpl:/schedule/weekdaysOnceSchedule/>
    <tmpl:/schedule/monthlySchedule/>
    <tmpl:/schedule/nthDaySchedule/>
    <tmpl:/schedule/specificDate/>
</div>
<r:script>
    $('input[name="type"]').on('click', function () {
        var type = $(this).val();
        $('.scheduleType').hide();
        if (type === "") return;
        $('.scheduleType.' + type).show();
    });
    $('input[name="type"]:checked').trigger('click');
</r:script>
