<%@ page import="org.opentele.server.model.Clinician"%>
<%@ page import="org.opentele.server.model.Role"%>

<div
	class="fieldcontain ${hasErrors(bean: cmd, field: 'firstName', 'error')} required" >
	<label for="firstName">
        <g:message code="clinician.firstName.label" default="First Name" /> <span class="required-indicator">*</span>
	</label>
	<g:textField name="firstName" value="${cmd?.firstName}" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: cmd, field: 'lastName', 'error')} ">
	<label for="lastName">
        <g:message code="clinician.lastName.label" default="Last Name" /> <span class="required-indicator">*</span>
	</label>
	<g:textField name="lastName" value="${cmd?.lastName}" />
</div>

<div class="fieldcontain ${hasErrors(bean: cmd, field: 'username', 'error')} required">
    <label for="username">
        <g:message code="clinician.username.label" default="Username" /> <span class="required-indicator">*</span>
    </label>
    <g:if test="${cmd?.id}">
        <g:field type="text" name="username" readonly="readonly" value="${cmd?.username}" data-tooltip="${message(code: 'tooltip.clinician.edit.username')}"/>
    </g:if>
    <g:else>
        <g:textField name="username" value="${cmd?.username}" data-tooltip="${message(code: 'tooltip.clinician.create.username')}"/>
    </g:else>
</div>
    <g:if test="${cmd?.cleartextPassword}">
    <div class="fieldcontain ${hasErrors(bean: cmd, field: 'cleartextPassword', 'error')} required" data-tooltip="${message(code: 'tooltip.clinician.create.cleartextPassword')}">

        <label for="cleartextPassword">
            <g:message code="clinician.cleartextPassword.label" default="Midlertidig Adgangskode" /> <span class="required-indicator">*</span>
        </label>
        <g:textField name="cleartextPassword" autocomplete="off" value="${cmd?.cleartextPassword}"/>
        <br />
    </div>
    </g:if>
    <g:else>
        <div class="fieldcontain ${hasErrors(bean: cmd, field: 'password', 'error')} required" data-tooltip="${message(code: 'tooltip.clinician.edit.password')}">

            <label for="password">
                <g:message code="clinician.password.label" default="Adgangskode" /> <span class="required-indicator">*</span>
            </label>
            <g:textField name="password" autocomplete="off" value="${message(code:"clinician.password.set-by-user")}" readonly="readonly"/>
            <br />
        </div>
    </g:else>
<div
	class="fieldcontain ${hasErrors(bean: cmd, field: 'phone', 'error')} ">
	<label for="phone">
        <g:message code="clinician.phone.label" default="Phone" />
	</label>
	<g:textField name="phone" value="${cmd?.phone}" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: cmd, field: 'mobilePhone', 'error')} ">
	<label for="mobilePhone">
        <g:message code="clinician.mobilePhone.label" default="Mobile Phone" />
	</label>
	<g:textField name="mobilePhone" value="${cmd?.mobilePhone}" />
</div>

<div
	class="fieldcontain ${hasErrors(bean: cmd, field: 'email', 'error')} ">
	<label for="email">
        <g:message code="clinician.email.label" default="Email" />
	</label>
	<g:textField name="email" value="${cmd?.email}" />
</div>

<g:if test="${grailsApplication.config.video.enabled}">
    <div class="fieldcontain ${hasErrors(bean: cmd, field: 'videoUser', 'error')} ">
        <label for="videoUser">
            <g:message code="clinician.video_user.label"/>
        </label>
        <g:textField name="videoUser" value="${cmd?.videoUser}" />
    </div>

    <div  class="fieldcontain ${hasErrors(bean: cmd, field: 'videoPassword', 'error')} ">
        <label for="videoPassword">
            <g:message code="clinician.video_password.label"/>
        </label>
        <g:textField name="videoPassword" value="${cmd?.videoPassword}" />
    </div>
</g:if>


<div
    class="fieldcontain ${hasErrors(bean: cmd, field: 'group', 'error')} required"
    data-tooltip="${message(code: 'tooltip.clinician.create.group')}">
    <label for="groupIds">
        <g:message code="clinician.group.label" default="type" />
    </label>

    <g:select name="groupIds" from="${cmd.possiblePatientGroups}"
              optionKey="id" multiple="multiple"
              value="${cmd?.groupIds}" />
</div>

<div class="fieldcontain required">
    <label for="roleIds" data-tooltip="${message(code: 'clinician.roles.label')}">
        <g:message code="clinician.roles.label" default="type" />
    </label>
    <g:select name="roleIds" from="${cmd.possibleRoles}"
              optionKey="id" multiple="multiple" optionValue="authority"
              value="${cmd?.roleIds}"/>
</div>

