<div id="timeOfDayContainer" class="scheduleType WEEKDAYS MONTHLY EVERY_NTH_DAY SPECIFIC_DATE">
    <div class="fieldcontain">
        <label data-tooltip="${message(code: "schedule.timesOfDay.tooltip")}"><g:message
                code="schedule.timesOfDay.label"/></label>

        <div class="timeOfDayInlined">
            <div class="timesOfDay">
                <g:each in="${timesOfDay}" var="timeOfDay" status="i">
                    <tmpl:/schedule/timeOfDay name="timesOfDay[${i}]" value="${timeOfDay}">
                        <g:img file="delete.png"
                               data-tooltip="${message(code: 'tooltip.questionnaireSchedule.removeTimeOfDay')}"
                               class="removeTimeOfDay"/>
                    </tmpl:/schedule/timeOfDay>
                </g:each>
            </div>
        </div>
        <div class="timesOfDayAdd">
            <g:img file="add.png" data-tooltip="${message(code: 'tooltip.questionnaireSchedule.addTimeOfDay')}"
                   class="addTimeOfDay"/>
        </div>
    </div>
</div>
<r:script>
    var timesOfDay = $('.timesOfDay', '#timeOfDayContainer');
    timesOfDay
            .on('click', '.removeTimeOfDay', function () {
                $(this).parent().remove();
                timesOfDay.trigger('reindex');
            })
            .on('reindex', function () {
                var spans = $('.timeOfDay', timesOfDay).each(function (index) {
                    $('input', this).each(function () {
                        var element = $(this);
                        var name = element.attr('name').replace(/\d+/, "" + index);
                        element.attr('name', name).attr('id', name)
                    });
                    $('.removeTimeOfDay', this).show();
                });
                if (spans.size() == 1) {
                    spans.find('.removeTimeOfDay').hide();
                }
            })
            .trigger('reindex')
            .data('timeOfDay', timesOfDay.find('.timeOfDay')[0].outerHTML);

    $('#timeOfDayContainer').on('click', '.addTimeOfDay', function () {
        var newTimeOfDay = $(timesOfDay.data('timeOfDay')).appendTo(timesOfDay);

        $('input', newTimeOfDay).each(function () {
            $(this).val('00');
        });
        $('input:first', newTimeOfDay).focus().select();
        setTimeout(function () {
            newTimeOfDay.trigger('reindex')
        }, 100);
    })
</r:script>
