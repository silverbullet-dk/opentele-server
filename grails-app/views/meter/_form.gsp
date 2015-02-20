<%@ page import="org.opentele.server.model.Meter"%>

<div class="fieldcontain noborder ${hasErrors(bean: meterInstance, field: 'active', 'error')}" data-tooltip="${message(code: 'tooltip.patient.meter.active')}">
	<label for="active">
        <g:message code="meter.active.label" />
	</label>
	<g:checkBox name="active" value="${meterInstance?.active}" />
</div>

<div class="fieldcontain ${hasErrors(bean: meterInstance, field: 'meterId', 'error')} " data-tooltip="${message(code: 'tooltip.patient.meter.id')}">
	<label for="meterId">
        <g:message code="meter.meterId.label"/>
	</label>
	<g:textField name="meterId" value="${meterInstance?.meterId}" />
</div>

<div class="fieldcontain ${hasErrors(bean: meterInstance, field: 'model', 'error')} ">
	<label for="model">
        <g:message code="meter.model.label" />
	</label>
	<g:textField name="model" value="${meterInstance?.model}" />
</div>

<div class="fieldcontain ${hasErrors(bean: meterInstance, field: 'meterType', 'error')} required">
	<label for="meterType">
        <g:message code="meter.meterType.label" /> <span class="required-indicator">*</span>
	</label>
	<g:select id="meterType" name="meterType.id"
		from="${org.opentele.server.model.MeterType.list().sort(){a, b -> a.name.compareTo(b.name)}  }"
		optionKey="id" required=""
		optionValue="${{g.message(code:'enum.meterType.'+it.name)}}"
		value="${meterInstance?.meterType?.id}" class="many-to-one" />
</div>

<div class="fieldcontain ${hasErrors(bean: meterInstance, field: 'patient', 'error')} ">
	<label for="patient">
        <g:message code="meter.patient.label" />
	</label>
	<g:select id="patient" name="patient.id"
		from="${org.opentele.server.model.Patient.list()}" optionKey="id"
		value="${meterInstance?.patient?.id}" class="many-to-one"
		noSelection="['null': '']" />
</div>

<div class="fieldcontain ${hasErrors(bean: meterInstance, field: 'monitorKit', 'error')} " data-tooltip="${message(code: 'tooltip.patient.meter.kit')}">
	<label for="monitorKit">
        <g:message code="meter.monitorKit.label" />
	</label>
	<g:select id="monitorKit" name="monitorKit.id"
		from="${org.opentele.server.model.MonitorKit.list()}" optionKey="id"
		value="${meterInstance?.monitorKit?.id}" class="many-to-one"
		noSelection="['null': '']" />
</div>

