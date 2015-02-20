<%@ page contentType="text/html;charset=UTF-8"%>
<html>
<head>
<meta name="layout" content="main">
<title><g:message code="patient.equipment.title.label" /></title>
</head>

<body>
	<div id="list-patient" class="content scaffold-list" role="main">
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
		<h1><g:message code="patient.equipment.meter.add.label" /></h1>

		<g:if test="${meters.size() > 0}">
			<table>
				<thead>
					<tr>
						<g:sortableColumn property="meterType" title="${message(code: 'patient.equipment.meters.metertype.label')}" />
						<g:sortableColumn property="name" title="${message(code: 'patient.equipment.meters.model.label')}" />
						<g:sortableColumn property="name" title="${message(code: 'patient.equipment.meters.meterId.label')}" />
						<g:sortableColumn property="kit" title="${message(code: 'patient.equipment.meters.monitorKit.label')}" />
						<g:sortableColumn property="kit" title="${message(code: 'default.action.label')}" />
					</tr>
				</thead>
				<tbody>
					<g:each in="${meters}" status="i" var="meter">
						<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
							<td>
                                <g:link controller="meter" action="show" id="${meter.id}">
									<g:message code="enum.meterType.${meter.meterType}" />
								</g:link>
                            </td>
							<td>
                                <g:link controller="meter" action="show" id="${meter.id}">
									${fieldValue(bean: meter, field: "model")}
								</g:link>
                            </td>
							<td>
                                <g:link controller="meter" action="show" id="${meter.id}">
									${fieldValue(bean: meter, field: "meterId")}
								</g:link>
                            </td>
							<td>
                                <g:link controller="meter" action="show" id="${meter.id}">
									${fieldValue(bean: meter, field: "monitorKit")}
								</g:link>
                            </td>
							<td>
                                <g:remoteLink controller="patient"
									before="if(!confirm('${message(code: 'confirm.context.msg.addmeter')}')) return false;"
									action="addMeter" id="${meter.id}"
									onSuccess="location.reload(true);">
									<img src='${resource(dir: 'images/icons', file: 'database_add.png', plugin: 'famfamfam')}' />
								</g:remoteLink>
                            </td>
						</tr>
					</g:each>
				</tbody>
			</table>
		</g:if>
		<g:else>
			<g:message code="patient.equipment.no.meters.to.add" />
		</g:else>

		<fieldset class="buttons">
			<g:link class="goback" controller="${session.lastController}" action="${session.lastAction}" params="${session.lastParams}">
				<g:message code="default.button.goback.label2" />
			</g:link>
		</fieldset>
	</div>
</body>
</html>