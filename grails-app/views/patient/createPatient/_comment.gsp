<%@ page import="org.opentele.server.model.Patient"%>
<%@ page import="org.opentele.server.model.PatientGroup"%>
<%@ page import="org.opentele.server.core.util.NumberFormatUtil"%>

<script>
$(document).ready(function() {
	$('select#sex').attr('value', 'UNKNOWN')
	
	$('input#cpr').change(function() {
		var input_cpr = $('input#cpr').val();
        $.ajax("${g.createLink(action: "patientSex", controller: "patient")}", {data: {identification: input_cpr}, cache: false}) // IE will cache the result unless we force cache-disabling
                .done(function(data) {
                    if (data.sex === 'UNKNOWN') {
                        return;
                    }
                    $('select#sex').attr('value', data.sex);
                });
	});
});
</script>

<div class="fieldcontain talltextarea ${hasErrors(bean: patientInstance, field: 'comment', 'error')}">
	<label for="comment">
        <g:message code="patient.comment.label" />
	</label>
    <g:textArea name="comment" value="${patientInstance?.comment}"/>
</div>
