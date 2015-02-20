<%@ page import="org.opentele.server.model.PatientNote" %>
<%@ page import="org.opentele.server.core.model.types.NoteType" %>

<div class="fieldcontain ${hasErrors(bean: patientNoteInstance, field: 'note', 'error')} ">
    <label for="note">
        <g:message code="patientNote.note.label" default="Note"/>
        <span class="required-indicator">*</span>
    </label>
    <g:textArea name="note" value="${patientNoteInstance?.note}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: patientNoteInstance, field: 'type', 'error')} required">
    <label for="type">
        <g:message code="patientNote.type.label" default="Type"/>
        <span class="required-indicator">*</span>
    </label>
    <g:select name="type" from="${NoteType.values().collect { g.message(code: 'enum.patientnote.' + it.name()) }}"
              keys="${NoteType.values()*.name()}" required=""
              value="${patientNoteInstance?.type?.name()}"/>
</div>

<div class="fieldcontain ${hasErrors(bean: patientNoteInstance, field: 'reminderDate', 'error')}">
    <label for="reminderDate" data-tooltip="${message(code: 'tooltip.patientNote.reminder.date')}">
        <g:message code="patientNoteInstance.reminderDate.label" />
    </label>
<div class="ui-datepicker-opentele">
        <jq:datePicker default="none" noSelection="['':'']" name="reminderDate" precision="minute" years="${2013..2050}" value="${patientNoteInstance?.reminderDate}"/>
</div>
</div>

