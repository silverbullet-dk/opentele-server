<%@ page import="org.opentele.server.core.model.Schedule; org.opentele.server.core.model.types.Weekday" %>
<div id="weekdaysScheduleSetupContainer" class="scheduleType WEEKDAYS">
    <tmpl:/schedule/scheduleWindow scheduleType="WEEKDAYS"/>

    <div class="fieldcontain noborder">
        <label><g:message code="questionnaireSchedule.weekdays"/></label>

        <div class="weekdays">
            <input type="checkbox" id="allNone" class="narrow-checkbox">
            <label for="allNone"><g:message code="schedule.allNoDays.label"/></label>
            <br/>
            <g:each in="${Weekday.values()}" var="day">
                <div>
                    <g:checkBox name="weekdays" id="weekdays_${day}" type="checkbox" value="${day}"
                                checked="${day in weekdays}" class="narrow-checkbox"/>
                    <label for="weekdays_${day}">
                        <g:message code="enum.weekday.${day}"/>
                    </label>
                </div>
            </g:each>
        </div>
    </div>
</div>
<r:require module="weekdays"/>
<r:script>
    $('#weekdaysScheduleSetupContainer').weekdays({
        name: "weekdays"
    });
</r:script>
