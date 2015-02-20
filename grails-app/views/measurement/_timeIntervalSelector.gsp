<%@ page import="org.opentele.server.core.model.types.MeasurementFilterType" %>

<g:javascript>
    function makeBold(element) {
        element.css({ 'font-weight': 'bold' });
    }

    function showIntervalChooser() {
        $('#${pageId}CustomIntervalChooser').show();
        makeBold($('#${MeasurementFilterType.CUSTOM}'));
    }

    $(function() {
        if('${params.filter}') {
            makeBold($('#${pageId}${params.filter}'));
        } else {
            makeBold($('#${pageId}${defaultInterval}'));
        }
    })
</g:javascript>

<div style="margin: 0.8em 1em 0.3em;padding: 0 0.25em;">
    <g:message code="time.filter.text"/>:
    <a id="${pageId}${MeasurementFilterType.ALL}" href="${createLink(controller:controller, action:action, id:id, params:[filter:"${MeasurementFilterType.ALL}"])}">
        <g:message code="time.filter.show.all"/>
    </a> |
    <a id="${pageId}${MeasurementFilterType.WEEK}" href="${createLink(controller:controller, action:action, id:id, params:[filter:"${MeasurementFilterType.WEEK}"])}">
        <g:message code="time.filter.show.week"/>
    </a> |
    <a id="${pageId}${MeasurementFilterType.MONTH}" href="${createLink(controller:controller, action:action, id:id, params:[filter:"${MeasurementFilterType.MONTH}"])}">
        <g:message code="time.filter.show.month"/>
    </a> |
    <a id="${pageId}${MeasurementFilterType.QUARTER}" href="${createLink(controller:controller, action:action, id:id, params:[filter:"${MeasurementFilterType.QUARTER}"])}">
        <g:message code="time.filter.show.quarter"/>
    </a> |
    <a id="${pageId}${MeasurementFilterType.YEAR}" href="${createLink(controller:controller, action:action, id:id, params:[filter:"${MeasurementFilterType.YEAR}"])}">
        <g:message code="time.filter.show.year"/>
    </a> |
    <a id="${pageId}${MeasurementFilterType.CUSTOM}" href="#" onclick="showIntervalChooser();">
        <g:message code="time.filter.show.choose"/>
    </a>
</div>

<g:if test="${MeasurementFilterType.valueOf(params.filter) == MeasurementFilterType.CUSTOM}">
    <div id="${pageId}CustomIntervalChooser">
</g:if>
<g:else>
    <div id="${pageId}CustomIntervalChooser" style="display:none">
</g:else>
<g:form method="GET" controller="${controller}" action="${action}" params='[id:id]'>
    <fieldset class="form">
        <div class="fieldcontain">
            <label for="start" style="width:auto"><g:message code="time.filter.custom.start"/>:</label>
            <jq:datePicker name="start" precision="day" value="${params.start}"/>
            <label for="end" style="width:auto"><g:message code="time.filter.custom.end"/>:</label>
            <jq:datePicker name="end" precision="day" value="${params.end}"/>
        </div>
        <fieldset class="buttons">
            <g:hiddenField name="filter" value="${MeasurementFilterType.CUSTOM}"/>
            <input type="Submit" value="${g.message(code:'time.filter.submit')}" class="acknowledge" style="float:left"/>
        </fieldset>
    </fieldset>
</g:form>
</div>