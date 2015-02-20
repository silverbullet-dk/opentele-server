<%@ page import="org.opentele.server.model.StandardThresholdSet" %>

<table>
	<thead>
		<tr>
			<th><g:message code="threshold.alertHigh" default="Alert High" /></th>
			<th><g:message code="threshold.warningHigh" default="Warning High" /></th>
			<th><g:message code="threshold.warningLow" default="Warning Low" /></th>
			<th><g:message code="threshold.alertLow" default="Alert Low" /></th>
		</tr>
	</thead>
	<tbody>
        <tmpl:/urineThreshold/threshold threshold="${standardThresholdInstance}" text=""/>
	</tbody>
</table>
