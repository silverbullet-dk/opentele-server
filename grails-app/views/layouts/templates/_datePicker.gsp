<span class="ui-datepicker-opentele" id="${name}_datepicker">
    <div class="ui-datepicker-wrapper"><g:textField name="${name}_widget" id="${id}_widget" value="${dateAsText}" class="ui-datepicker-ot-date"/></div>
    <g:if test="${showTime}">
        <div class="ui-datepicker-timewrapper">
            <g:message code="default.time.name"/>
            <g:textField name="${name}_hour" value="${formatDate(date: date, format: "HH")}" class="ui-datepicker-ot-time" maxlength="2"/>
            <g:message code="default.time.separator"/>
            <g:textField name="${name}_minute" value="${formatDate(date: date, format: "mm")}" class="ui-datepicker-ot-time" maxlength="2"/>
        </div>
    </g:if>
    <g:hiddenField name="${name}" value="date.struct"/>
    <g:hiddenField name="${name}_day" value="${formatDate(date: date, format: "d")}"/>
    <g:hiddenField name="${name}_month" value="${formatDate(date: date, format: "M")}"/>
    <g:hiddenField name="${name}_year" value="${formatDate(date: date, format: "yyyy")}"/>
</span>
<g:javascript>
    $(function() {
           $('#${name}_datepicker').openteleDatePicker({
                fieldName: '${name}',
                nullable: ${nullable},
                minDate: '${minDate}',
                maxDate: '${maxDate}',
                javascriptFormat: '${javascriptFormat}',
                date: '${dateAsText}',
                buttonImage: '${resource(dir: 'images', file: 'calendar.png')}'
           });
    });
</g:javascript>
