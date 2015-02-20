<%@ page
	import="org.opentele.server.core.model.types.PermissionName; org.opentele.server.core.model.types.PermissionName; org.opentele.server.core.model.types.MeasurementTypeName; org.opentele.server.provider.constants.Constants; org.opentele.server.model.Patient; org.opentele.server.core.util.NumberFormatUtil"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="title" value="${message(code: 'patient.show.title', args: [patientInstance.name.encodeAsHTML()])}"/>
<title>${title}</title>
</head>

<body>
	<div id="show-patient" class="content scaffold-show" role="main">
		<h1>${title}</h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
		<ol class="property-list patient">
			<g:if test="${patientInstance.user}">
				<li class="fieldcontain">
                    <span id="patientinstance-user-username-label" class="property-label">
                        <g:message code="patient.user.label" />
				    </span>
                    <span class="property-value" aria-labelledby="user-label">
                        ${patientInstance.user.username.encodeAsHTML()}
				    </span>
                </li>
			</g:if>
			<g:if test="${patientInstance.user}">
                <g:if test="${patientInstance.user.cleartextPassword}">
                    <li class="fieldcontain">
                        <span id="cleartextPassword-label" class="property-label">
                            <g:message code="patient.cleartextPassword.label"/>
                        </span>
                        <span class="property-value" aria-labelledby="user-label">
                            ${patientInstance.user.cleartextPassword.encodeAsHTML()}
                            <g:if test="${patientInstance.user.accountLocked}">
                                <strong><g:message code="patient.account.locked"/></strong>
                            </g:if>
                        </span>
                    </li>
                </g:if>
                <g:else>
                    <li class="fieldcontain">
                        <span id="password-label" class="property-label">
                            <g:message code="patient.password.label"/>
                        </span>
                        <span class="property-value" aria-labelledby="user-label">
                           <g:message code="patient.password.set-by-user"/>
                            <g:if test="${patientInstance.user.accountLocked}">
                                <strong><g:message code="patient.account.locked"/></strong>
                            </g:if>
                        </span>
                    </li>
                </g:else>
            </g:if>

			<g:if test="${patientInstance.cpr}">
				<li class="fieldcontain">
                    <span id="cpr-label" class="property-label">
                        <g:message code="patient.cpr.label" />
                    </span>
                    <span class="property-value" aria-labelledby="cpr-label">
                        <otformat:formatCPR cpr="${patientInstance.cpr}"/>
				    </span>
                </li>
			</g:if>

			<g:if test="${patientInstance.firstName}">
				<li class="fieldcontain">
                    <span id="firstName-label" class="property-label">
                        <g:message code="patient.firstName.label" />
                    </span>
                    <span class="property-value" aria-labelledby="firstName-label">
                        <g:fieldValue bean="${patientInstance}" field="firstName" />
                    </span>
                </li>
			</g:if>

			<g:if test="${patientInstance.lastName}">
				<li class="fieldcontain">
                    <span id="lastName-label" class="property-label">
                        <g:message code="patient.lastName.label" />
                    </span>
                    <span class="property-value" aria-labelledby="lastName-label">
                        <g:fieldValue bean="${patientInstance}" field="lastName" />
                    </span>
                </li>
			</g:if>

			<g:if test="${patientInstance.sex}">
				<li class="fieldcontain">
                    <span id="sex-label" class="property-label">
                        <g:message code="patient.sex.label" />
                    </span>
                    <span class="property-value" aria-labelledby="sex-label">
                        ${message(code:'enum.sex.'+fieldValue(bean: patientInstance, field: "sex"))}
				    </span>
                </li>
			</g:if>

			<li class="fieldcontain">
                <span id="state-label" class="property-label">
                    <g:message code="patient.status" />
                </span>
                <span class="property-value" aria-labelledby="state-label">
                    ${message(code:'enum.patientstate.'+patientInstance.getState())}
    			</span>
            </li>
            <g:if test="${patientInstance?.dueDate}">
                <li class="fieldcontain">
                    <span id="dueDate-label" class="property-label">
                        <g:message code="patient.dueDate.label" />
                    </span>
                    <span class="property-value" aria-labelledby="dueDate-label">
                        <g:formatDate formatName="default.date.format.notime" date="${patientInstance?.dueDate}" />
                    </span>
                </li>
            </g:if>
			<g:if test="${patientInstance.address}">
				<li class="fieldcontain">
                    <span id="address-label" class="property-label">
                        <g:message code="patient.address.label" />
                    </span>
                    <span class="property-value" aria-labelledby="address-label">
                        <g:fieldValue bean="${patientInstance}" field="address" />
                    </span>
                </li>
			</g:if>

			<g:if test="${patientInstance.postalCode}">
				<li class="fieldcontain">
                    <span id="postalCode-label" class="property-label">
                        <g:message code="patient.postalCode.label" />
                    </span>
                    <span class="property-value" aria-labelledby="postalCode-label">
                        <g:fieldValue bean="${patientInstance}" field="postalCode" />
                    </span>
                </li>
			</g:if>

			<g:if test="${patientInstance.city}">
				<li class="fieldcontain">
                    <span id="city-label" class="property-label">
                        <g:message code="patient.city.label" />
                    </span>
                    <span class="property-value" aria-labelledby="city-label">
                        <g:fieldValue bean="${patientInstance}" field="city" />
                    </span>
                </li>
			</g:if>

			<g:if test="${patientInstance.phone}">
				<li class="fieldcontain">
                    <span id="phone-label" class="property-label">
                        <g:message code="patient.phone.label" />
                    </span>
                    <span class="property-value" aria-labelledby="phone-label">
                        <otformat:formatPhoneNumber message="${patientInstance.phone}" />
                    </span>
                </li>
			</g:if>

			<g:if test="${patientInstance.mobilePhone}">
				<li class="fieldcontain">
                    <span id="mobilePhone-label" class="property-label">
                        <g:message code="patient.mobilePhone.label" />
                    </span>
                    <span class="property-value" aria-labelledby="mobilePhone-label">
                        <otformat:formatPhoneNumber message="${patientInstance.mobilePhone}" />
                    </span>
                </li>
			</g:if>

			<g:if test="${patientInstance.email}">
				<li class="fieldcontain">
                    <span id="email-label" class="property-label">
                        <g:message code="patient.email.label" />
                    </span>
                    <span class="property-value" aria-labelledby="email-label">
                        <g:fieldValue bean="${patientInstance}" field="email" />
                    </span>
                </li>
			</g:if>

            <g:if test="${messagingEnabled}">
                <li class="fieldcontain">
                    <span id="noAlarmIfUnreadMessagesToPatient.label" class="property-label">
                        <g:message code="patient.noAlarmIfUnreadMessagesToPatient.label"/>
                    </span>

                    <span class="property-value" aria-labelledby="patientGroup.noAlarmIfUnreadMessagesToPatient.label">
                        <g:checkBox name="noAlarmIfUnreadMessagesToPatient" value="${patientInstance?.noAlarmIfUnreadMessagesToPatient}" disabled="true" />
                    </span>
                </li>
            </g:if>

			<g:if test="${nextOfKin.size() > 0}">
				<li class="fieldcontain">
                    <span id="nextOfKin-label" class="property-label">
                        <g:message code="patient.nextOfKin"/>:
                    </span>
                    <tmpl:nextOfKin readonly="true"/>
                </li>
			</g:if>

			<g:if test="${groups.size() > 0}">
				<li class="fieldcontain">
                    <span id="group-label" class="property-label"><g:message code="patient.patientGroups"/>:</span>
                    <span class="property-value fullheight" aria-labelledby="comment-label">
                        <g:each in="${groups}" var="group">
							${group}<br />
						</g:each>
				    </span>
                </li>
			</g:if>

            <li class="fieldcontain">
                <span id="comment-label" class="property-label">
                    <g:message code="patient.comment.label" />
                </span>
                <span class="property-value fullheight" aria-labelledby="comment-label">
                    <g:fieldValue bean="${patientInstance}" field="comment" />
                </span>
            </li>

            <li class="fieldcontain" data-tooltip="${message(code: 'tooltip.patient.responsible.group.label')}">
                <span id="dataResponsible-label" class="property-label">
                    <g:message code="responsible.patient.group.label" />
                </span>
                <span class="property-value fullheight" aria-labelledby="dataResponsible-label">
                    <g:fieldValue bean="${patientInstance}" field="dataResponsible" />
                </span>
            </li>
        </ol>

        <g:render template="thresholds" model="[thresholds: patientInstance.thresholds, readonly: true]"/>

        <fieldset class="buttons">
        <sec:ifAnyGranted roles="${org.opentele.server.core.model.types.PermissionName.PATIENT_WRITE}">
            <g:form>
                <g:hiddenField name="id" value="${patientInstance.id}"/>
                <g:link class="edit" action="edit" id="${patientInstance.id}">
                    <g:message code="default.button.edit.label"/>
                </g:link>
                <g:if test="${!patientInstance.user.cleartextPassword}">
                    <g:actionSubmit class="resetPassword" action="resetPassword"
                                    value="${message(code: 'patient.reset-password.label')}"
                                    onclick="return confirm('${message(code: 'patient.reset-password.confirm.message')}');"/>
                </g:if>
                <g:else>
                    <mailsender:isEnabled>
                        <g:if test="${patientInstance.email}">
                            <g:actionSubmit class="mail" action="sendPassword"
                                            value="${message(code: 'patient.send-password.label')}"/>
                        </g:if>
                    </mailsender:isEnabled>
                </g:else>

                <g:if test="${patientInstance.user.accountLocked}">
                    <g:actionSubmit class="resetPassword" action="unlockAccount"
                                    value="${message(code: 'patient.unlock-account.label')}"/>
                </g:if>

            </g:form>
        </sec:ifAnyGranted>
        <g:render template="../printable"/>
    </fieldset>
    </div>
</body>
</html>
