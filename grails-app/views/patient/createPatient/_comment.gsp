<%@ page import="org.opentele.server.model.Patient"%>
<%@ page import="org.opentele.server.model.PatientGroup"%>
<%@ page import="org.opentele.server.core.util.NumberFormatUtil"%>

<script>
$(document).ready(function() {
	$('select#sex').attr('value', 'UNKNOWN')
	
	$('input#cpr').change(function() {
		var input_cpr = $('input#cpr').val();

		if (input_cpr.indexOf("-") > 0 && input_cpr.length == 11) {
			try {
				var temp
				temp = input_cpr.substring(0, input_cpr.indexOf('-'))
				input_cpr = temp + input_cpr.substring(input_cpr.indexOf('-')+1, input_cpr.length)
			} catch (err) {
			}
		}
		if (input_cpr.length == 10 && Number(input_cpr) != 'NaN') {
			if (input_cpr%2==0) {
				$('select#sex').attr('value', 'FEMALE')
			} else {
				$('select#sex').attr('value', 'MALE')
			}			
		}
	});
});
</script>

<div class="fieldcontain talltextarea ${hasErrors(bean: patientInstance, field: 'comment', 'error')}">
	<label for="comment">
        <g:message code="patient.comment.label" />
	</label>
    <g:textArea name="comment" value="${patientInstance?.comment}"/>
</div>
