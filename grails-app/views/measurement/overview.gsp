<%@ page import="org.opentele.server.model.Measurement"%>

<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<title>OpenTele</title>
</head>

<body>
	<!--  error handling -->
	<div id="list-measurement" class="content scaffold-list" role="main">
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>

		<!--  results  -->
		<g:if test="${newMeasurements.size > 0 }">
			<h1>
				<g:message code="measurement.unread.list.label" />
			</h1>
			<table>
				<thead>
					<tr>
						<g:sortableColumn property="time" title="${message(code: 'measurement.time.label', default: 'Time')}" />
						<g:sortableColumn property="value" title="${message(code: 'measurement.value.label', default: 'Value')}" />
						<g:sortableColumn property="unit" title="${message(code: 'measurement.unit.label', default: 'Unit')}" />
						<th><g:message code="patient.label"/></th>
						<th><g:message code="measurement.meter.label" default="Meter" /></th>
						<th><g:message code="measurement.type"/></th>
					</tr>
				</thead>
				<tbody>
					<g:each in="${newMeasurements}" status="i" var="measurementInstance">
						<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
							<td>
                                <g:link action="show" id="${measurementInstance.id}">
									${fieldValue(bean: measurementInstance, field: "time")}
								</g:link>
                            </td>
							<td>
								${measurementInstance.toString()}
							</td>
							<td>
								${fieldValue(bean: measurementInstance, field: "unit")}
							</td>
							<td>
                                <a href="${createLink(controller: 'patient', action: 'patientgraph', id:measurementInstance.patient.id)}">
									${fieldValue(bean: measurementInstance, field: "patient.firstName")}
									${fieldValue(bean: measurementInstance, field: "patient.lastName")}
							    </a>
                            </td>
							<td>
								${fieldValue(bean: measurementInstance, field: "meter.id")}
							</td>
							<td>
								${fieldValue(bean: measurementInstance, field: "measurementType")}
							</td>
						</tr>
					</g:each>
				</tbody>
			</table>
		</g:if>
		<g:if test="${oldMeasurements.size > 0}">
			<h1><g:message code="measurement.read.list.label" /></h1>
			<g:if test="${flash.message}">
				<div class="message" role="status">
					${flash.message}
				</div>
			</g:if>
			<table>
				<thead>
					<tr>
						<g:sortableColumn property="time" title="${message(code: 'measurement.time.label', default: 'Time')}" />
						<g:sortableColumn property="value" title="${message(code: 'measurement.value.label', default: 'Value')}" />
						<g:sortableColumn property="unit" title="${message(code: 'measurement.unit.label', default: 'Unit')}" />
                        <th><g:message code="patient.label"/></th>
                        <th><g:message code="measurement.meter.label"/></th>
                        <th><g:message code="measurement.type"/></th>
					</tr>
				</thead>
				<tbody>
					<g:each in="${oldMeasurements}" status="i" var="measurementInstance">
						<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
							<td>
                                <g:link action="show" id="${measurementInstance.id}">
									${fieldValue(bean: measurementInstance, field: "time")}
								</g:link>
                            </td>
							<td>
								${fieldValue(bean: measurementInstance, field: "value")}
							</td>
							<td>
								${fieldValue(bean: measurementInstance, field: "unit")}
							</td>
							<td>
                                <a href="${createLink(controller: 'patient', action: 'overview', id:measurementInstance.patient.id)}">
									${fieldValue(bean: measurementInstance, field: "patient.firstName")}
									${fieldValue(bean: measurementInstance, field: "patient.lastName")}
    							</a>
                            </td>
							<td>
								${fieldValue(bean: measurementInstance, field: "meter.id")}
							</td>
							<td>
								${fieldValue(bean: measurementInstance, field: "measurementType")}
							</td>
						</tr>
					</g:each>
				</tbody>
			</table>
		</g:if>
	</div>
</body>
</html>
