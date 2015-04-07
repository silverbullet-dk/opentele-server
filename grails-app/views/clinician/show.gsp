
<%@ page import="org.opentele.server.core.model.types.PermissionName; org.opentele.server.core.model.types.PermissionName; org.opentele.server.core.model.types.PermissionName; org.opentele.server.core.model.types.PermissionName; org.opentele.server.core.model.types.PermissionName; org.opentele.server.model.Clinician"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName"	value="${message(code: 'default.user.label')}" />
<title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
	<div id="show-clinician" class="content scaffold-show" role="main">
		<h1><g:message code="default.show.label" args="[entityName]" /></h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
		<ol class="property-list clinician">
			<g:if test="${clinicianInstance?.firstName}">
				<li class="fieldcontain">
                    <span id="firstName-label" class="property-label">
                        <g:message code="clinician.firstName.label"/>
                    </span>
                    <span class="property-value" aria-labelledby="firstName-label">
                        <g:fieldValue bean="${clinicianInstance}" field="firstName" />
                    </span>
                </li>
			</g:if>

			<g:if test="${clinicianInstance?.lastName}">
				<li class="fieldcontain">
                    <span id="lastName-label" class="property-label">
                        <g:message code="clinician.lastName.label"/>
                    </span>
                    <span class="property-value" aria-labelledby="lastName-label">
                        <g:fieldValue bean="${clinicianInstance}" field="lastName" />
                    </span>
                </li>
			</g:if>

            <g:if test="${clinicianInstance?.user?.username}">
                <li class="fieldcontain">
                    <span id="username-label" class="property-label">
                        <g:message code="clinician.username.label"/>
                    </span>
                    <span class="property-value" aria-labelledby="username-label">
                        <g:fieldValue bean="${clinicianInstance.user}" field="username" />
                    </span>
                </li>
            </g:if>

            <g:if test="${clinicianInstance?.user?.cleartextPassword}">
                <li class="fieldcontain">
                    <span id="cleartextPassword-label" class="property-label">
                        <g:message code="clinician.cleartextPassword.label"/>
                    </span>
                    <span class="property-value" aria-labelledby="cleartextPassword-label">
                        <g:fieldValue bean="${clinicianInstance.user}" field="cleartextPassword"/>
                        <g:if test="${clinicianInstance?.user?.accountLocked}">
                            <strong><g:message code="clinician.account.locked"/></strong>
                        </g:if>
                    </span>
                </li>
            </g:if>

            <g:if test="${!clinicianInstance?.user?.cleartextPassword}">
                <li class="fieldcontain">
                    <span id="password-label" class="property-label">
                        <g:message code="clinician.password.label"/>
                    </span>
                    <span class="property-value" aria-labelledby="password-label">
                        <g:message code="clinician.password.set-by-user"/>
                        <g:if test="${clinicianInstance?.user?.accountLocked}">
                            <strong><g:message code="clinician.account.locked"/></strong>
                        </g:if>
                    </span>
                </li>
            </g:if>

            <g:if test="${clinicianInstance?.phone}">
				<li class="fieldcontain">
                    <span id="phone-label" class="property-label">
                        <g:message code="clinician.phone.label"/>
                    </span>
                    <span class="property-value" aria-labelledby="phone-label">
                        <g:fieldValue bean="${clinicianInstance}" field="phone"/>
                    </span>
                </li>
			</g:if>

			<g:if test="${clinicianInstance?.mobilePhone}">
				<li class="fieldcontain">
                    <span id="mobilePhone-label" class="property-label">
                        <g:message code="clinician.mobilePhone.label"/>
                    </span>
                    <span class="property-value" aria-labelledby="mobilePhone-label">
                        <g:fieldValue bean="${clinicianInstance}" field="mobilePhone" />
                    </span>
                </li>
			</g:if>

			<g:if test="${clinicianInstance?.email}">
				<li class="fieldcontain">
                    <span id="email-label" class="property-label">
                        <g:message code="clinician.email.label"/>
                    </span>
                    <span class="property-value" aria-labelledby="email-label">
                        <g:fieldValue bean="${clinicianInstance}" field="email" />
                    </span>
                </li>
			</g:if>

            <li class="fieldcontain">
                <span id="clinician2PatientGroups-label" class="property-label">
                    <g:message code="clinician.clinician2PatientGroups.label" />
                </span>
                <span class="property-value" aria-labelledby="clinician2PatientGroups-label">
                    <g:each in="${clinicianInstance.clinician2PatientGroups}" var="c" status="index">
                        <g:if test="${index > 0}">,</g:if>
                        ${c.patientGroup.name.encodeAsHTML()}
                    </g:each>
                </span>
            </li>

            <li class="fieldcontain">
                <span id="clinicianRoles-label" class="property-label">
                    <g:message code="clinician.roles.label"/>
                </span>

                <span class="property-value" aria-labelledby="clinicianRoles-label">
                    <g:each in="${clinicianInstance.user.authorities}" var="authority" status="index">
                        <g:if test="${index > 0}">,</g:if>
                        <g:link controller="role" action="show" id="${authority.id}">
                            ${authority.authority.encodeAsHTML()}
                        </g:link>
                    </g:each>
                </span>
            </li>

            <g:if test="${grailsApplication.config.video.enabled}">
                <g:if test="${clinicianInstance}">
                    <li class="fieldcontain">
                        <span id="video_user-label" class="property-label">
                            <g:message code="clinician.video_user.label"/>
                        </span>
                        <span class="property-value" aria-labelledby="video_user-label">
                            <g:fieldValue bean="${clinicianInstance}" field="videoUser" />
                        </span>
                    </li>
                </g:if>

                <g:if test="${clinicianInstance}">
                    <li class="fieldcontain">
                        <span id="video_password-label" class="property-label">
                            <g:message code="clinician.video_password.label"/>
                        </span>
                        <span class="property-value" aria-labelledby="video_password-label">
                            -
                        </span>
                    </li>
                </g:if>
            </g:if>
        </ol>

		<g:form>
			<fieldset class="buttons">
				<g:hiddenField name="id" value="${clinicianInstance?.id}" />
                <sec:ifAnyGranted roles="${org.opentele.server.core.model.types.PermissionName.CLINICIAN_WRITE}">
                    <g:link class="edit" action="edit" id="${clinicianInstance?.id}">
                        <g:message code="default.button.edit.label"/>
                    </g:link>
                </sec:ifAnyGranted>
                <g:if test="${!clinicianInstance?.user?.cleartextPassword && !isCurrentUser}">
                    <sec:ifAnyGranted roles="${org.opentele.server.core.model.types.PermissionName.CLINICIAN_CHANGE_PASSWORD}">
                        <g:actionSubmit class="resetPassword" action="resetPassword" value="${message(code: 'clinician.reset-password.label')}"
                                        onclick="return confirm('${message(code: 'clinician.reset-password.confirm.message')}');"/>
                    </sec:ifAnyGranted>
                </g:if>
                <g:if test="${clinicianInstance?.user?.accountLocked}">
                    <sec:ifAnyGranted roles="${org.opentele.server.core.model.types.PermissionName.CLINICIAN_CHANGE_PASSWORD}">
                        <g:actionSubmit class="resetPassword" action="unlockAccount" value="${message(code: 'clinician.unlock-account.label')}"/>
                    </sec:ifAnyGranted>
                </g:if>
			</fieldset>
		</g:form>
	</div>
</body>
</html>
