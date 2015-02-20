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
		<h1><g:message code="patient.equipment.kit.addToPatient" /></h1>
		<g:if test="${kits.size() > 0}">
			<table>
				<thead>
					<tr>
						<g:sortableColumn property="name" title="${message(code: 'patient.equipment.kit.name.label')}" />
						<g:sortableColumn property="department" title="${message(code: 'patient.equipment.kit.department.label')}" />
						<g:sortableColumn property="meters" title="${message(code: 'patient.equipment.kit.meters.label')}" />
						<g:sortableColumn property="kit" title="${message(code: 'default.action.label')}" />
					</tr>
				</thead>
				<tbody>
					<g:each in="${kits}" status="i" var="kit">
						<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
							<td>
                                <g:link controller="monitorKit" action="show" id="${kit.id}">
									${fieldValue(bean: kit, field: "name")}
								</g:link>
                            </td>
							<td>
								${fieldValue(bean: kit, field: "department")}
                            </td>
							<td>
								<ul class="table">
									<g:each in="${kit.meters}" var="meter">
										<li><g:message code="enum.meterType.${meter.meterType}" /></li>
									</g:each>
								</ul>
							</td>
							<td>
                                <g:remoteLink controller="patient"
									before="if(!confirm('${message(code: 'confirm.context.msg.addkit')}')) return false;"
									action="addKit" id="${kit.id}"
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
			<g:message code="patient.equipment.no.kits.to.add" />
		</g:else>

		<fieldset class="buttons">
            <g:link class="goback" controller="${session.lastController}" action="${session.lastAction}" params="${session.lastParams}">
				<g:message code="default.button.goback.label2" />
			</g:link>
		</fieldset>
	</div>
</body>
</html>