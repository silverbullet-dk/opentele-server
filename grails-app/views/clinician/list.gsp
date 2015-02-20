
<%@ page import="org.opentele.server.model.Clinician"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'default.user.label', default: 'User')}" />
<title><g:message code="default.list.label" args="[entityName]" /></title>
</head>
<body>

	<div id="list-clinician" class="content scaffold-list" role="main">
		<h1>
			<g:message code="default.list.label" args="[entityName]" />
		</h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
		<table>
			<thead>
				<tr>
					<g:sortableColumn property="firstName"
						title="${message(code: 'clinician.firstName.label')}" />
					<g:sortableColumn property="lastName"
						title="${message(code: 'clinician.lastName.label')}" />
					<g:sortableColumn property="phone"
						title="${message(code: 'clinician.phone.label')}" />
					<g:sortableColumn property="mobilePhone"
						title="${message(code: 'clinician.mobilePhone.label')}" />
					<g:sortableColumn property="email"
						title="${message(code: 'clinician.email.label')}" />
                    <g:sortableColumn property="username"
                                      title="${message(code: 'clinician.username.label')}" />
                    <g:if test="${grailsApplication.config.video.enabled}">
                        <g:sortableColumn property="videoUser"
                                          title="${message(code: 'clinician.video_user.label')}" />
                    </g:if>
				</tr>
			</thead>
			<tbody>
				<g:each in="${clinicianInstanceList}" status="i"
					var="clinicianInstance">
					<tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
						<td>
                            <g:link action="show" id="${clinicianInstance.id}">
								${fieldValue(bean: clinicianInstance, field: "firstName")}
							</g:link>
                        </td>
						<td>
							${fieldValue(bean: clinicianInstance, field: "lastName")}
						</td>
						<td>
							${fieldValue(bean: clinicianInstance, field: "phone")}
						</td>
						<td>
							${fieldValue(bean: clinicianInstance, field: "mobilePhone")}
						</td>
						<td>
							${fieldValue(bean: clinicianInstance, field: "email")}
						</td>
                        <td>
                            <g:if test="${clinicianInstance?.user?.username}">
                                ${fieldValue(bean: clinicianInstance, field: "user.username")}
                            </g:if>
                        </td>
                        <g:if test="${grailsApplication.config.video.enabled}">
                            <td>
                                ${fieldValue(bean: clinicianInstance, field: "videoUser")}
                            </td>
                        </g:if>
					</tr>
				</g:each>
			</tbody>
		</table>
		<div class="pagination">
			<g:paginate total="${clinicianInstanceTotal}" />
		</div>
	</div>
	<fieldset class="buttons">
		<g:link class="create" action="create">
			<g:message code="default.new.person.label" args="[entityName]" />
		</g:link>
	</fieldset>
</body>
</html>
