<%@ page import="org.opentele.server.core.model.types.PermissionName" contentType="text/html;charset=UTF-8"%>
<html>
<head>
<meta name="layout" content="main">
<g:set var="title" value="${message(code: 'patient.equipment.title', args: [patientInstance.name.encodeAsHTML()])}"/>

<title>${title}</title>
</head>

<body>
	<div id="list-patient" class="content scaffold-list" role="main">
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
		<h1>${title}</h1>
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
						<td><g:message code="enum.meterType.${meter.meterType}" /></td>
						<td>
                            <sec:ifAnyGranted roles="${PermissionName.METER_READ}">
                                <g:link controller="meter" action="show" id="${meter.id}">
                                    ${fieldValue(bean: meter, field: "model")}
                                </g:link>
                            </sec:ifAnyGranted>
                            <sec:ifNotGranted roles="${PermissionName.METER_READ}">
                                ${fieldValue(bean: meter, field: "model")}
                            </sec:ifNotGranted>
                        </td>
						<td>${fieldValue(bean: meter, field: "meterId")}</td>
						<td>${fieldValue(bean: meter, field: "monitorKit")}</td>
                        <sec:ifAnyGranted roles="${PermissionName.PATIENT_WRITE}">
                            <td class="buttons">
                                <g:if test="${!meter?.monitorKit}">
                                    <g:remoteLink controller="patient"
                                        before="if(!confirm('${message(code: 'confirm.context.msg.removemeter')}')) return false;"
                                        action="removeMeter" id="${meter.id}"
                                        onSuccess="location.reload(true);"
                                        class="delete"> <g:message code="meter.remove"/>
                                    </g:remoteLink>
                                </g:if>
                            </td>
                        </sec:ifAnyGranted>
                        <sec:ifNotGranted roles="${PermissionName.PATIENT_WRITE}">
                            <td></td>
                        </sec:ifNotGranted>
					</tr>
				</g:each>
			</tbody>
		</table>

		<h1><g:message code="patient.equipment.kit.list.label" /></h1>
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
                            <sec:ifAnyGranted roles="${PermissionName.MONITOR_KIT_READ}">
                                <g:link controller="monitorKit" action="show" id="${kit.id}">
                                    ${fieldValue(bean: kit, field: "name")}
                                </g:link>
                            </sec:ifAnyGranted>
                            <sec:ifNotGranted roles="${PermissionName.MONITOR_KIT_READ}">
                                ${fieldValue(bean: kit, field: "name")}
                            </sec:ifNotGranted>
                        </td>
						<td>${fieldValue(bean: kit, field: "department")}</td>
						<td>
							<ul class="table">
								<g:each in="${kit.meters}" var="meter">
									<li><g:message code="enum.meterType.${meter.meterType}" /></li>
								</g:each>
							</ul>
						</td>
                        <sec:ifAnyGranted roles="${PermissionName.PATIENT_WRITE}">
                            <td class="buttons">
                                <g:remoteLink controller="patient"
                                    before="if(!confirm('${message(code: 'confirm.context.msg.removekit')}')) return false;"
                                    action="removeKit" id="${kit.id}"
                                    onSuccess="location.reload(true);"
                                    class="delete"> <g:message code="monitorKit.remove"/>
                                </g:remoteLink>
                            </td>
                        </sec:ifAnyGranted>
                        <sec:ifNotGranted roles="${PermissionName.PATIENT_WRITE}">
                            <td></td>
                        </sec:ifNotGranted>
					</tr>
				</g:each>
			</tbody>
		</table>
        <sec:ifAnyGranted roles="${PermissionName.PATIENT_WRITE}">
            <fieldset class="buttons">
                <g:link class="create" action="addMeter">
                    <g:message code="patient.equipment.meter.add" />
                </g:link>
                <g:link class="create" action="addKit">
                    <g:message code="patient.equipment.kit.add" />
                </g:link>
            </fieldset>
        </sec:ifAnyGranted>
	</div>
</body>
</html>
