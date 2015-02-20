<%@ page
	import="org.opentele.server.core.model.types.PermissionName; org.opentele.server.core.model.types.PermissionName; org.opentele.server.core.model.types.PermissionName; org.opentele.server.core.model.types.PermissionName; org.opentele.server.provider.constants.Constants; org.opentele.server.model.questionnaire.Questionnaire"%>
<!doctype html>
<html>
<head>
<meta name="layout" content="main">
<g:set var="entityName"
	value="${message(code: 'questionnaire.label', default: 'Questionnaire')}" />
<title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>

	<div id="show-questionnaire" class="content scaffold-show" role="main">
		<h1>
			<g:message code="default.show.label" args="[entityName]" />
		</h1>
		<g:if test="${flash.message}">
			<div class="message" role="status">
				${flash.message}
			</div>
		</g:if>
		<ol class="property-list questionnaire">

			<g:if test="${questionnaireInstance?.name}">
				<li class="fieldcontain">
                    <span id="name-label" class="property-label">
                    <g:message code="questionnaire.name.label" default="Name" />
                </span>
                    <span class="property-value" aria-labelledby="name-label">
                        <g:fieldValue bean="${questionnaireInstance}" field="name" />
                    </span>
                </li>
			</g:if>

			<g:if test="${questionnaireInstance?.revision}">
				<li class="fieldcontain">
                    <span id="revision-label" class="property-label">
                        <g:message code="questionnaire.revision.label" default="Revision" />
                    </span>
                    <span class="property-value" aria-labelledby="revision-label">
                        <g:fieldValue bean="${questionnaireInstance}" field="revision" />
                    </span>
                </li>
			</g:if>

			<g:if test="${questionnaireInstance?.creator}">
				<li class="fieldcontain">
                    <span id="creator-label" class="property-label">
                        <g:message code="questionnaire.creator.label" default="Creator" />
                    </span>
                    <span class="property-value" aria-labelledby="creator-label">
                        <g:link controller="clinician" action="show" id="${questionnaireInstance?.creator?.id}">
							${questionnaireInstance?.creator?.encodeAsHTML()}
						</g:link>
                    </span>
                </li>
			</g:if>

			<g:if test="${questionnaireInstance?.creationDate}">
				<li class="fieldcontain">
                    <span id="creationDate-label" class="property-label">
                        <g:message code="questionnaire.creationDate.label" default="Creation Date" />
                    </span>

					<span class="property-value" aria-labelledby="creationDate-label">
                        <g:formatDate date="${questionnaireInstance?.creationDate}" />
                    </span>
                </li>
			</g:if>

			<g:if test="${questionnaireInstance?.questionnaire2MeterTypes}">
				<li class="fieldcontain">
                    <span id="questionnaire2MeterTypes-label" class="property-label">
                        <g:message code="questionnaire.questionnaire2MeterTypes.label"/>
                    </span>
                    <g:each in="${questionnaireInstance.questionnaire2MeterTypes}" var="q">
						<span class="property-value" aria-labelledby="questionnaire2MeterTypes-label">
                            <g:link controller="questionnaire2MeterType" action="show" id="${q.id}">
								${q?.encodeAsHTML()}
							</g:link>
                        </span>
					</g:each>
                </li>
			</g:if>
		</ol>

			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${questionnaireInstance?.id}" />

                    <sec:ifAnyGranted roles="${org.opentele.server.core.model.types.PermissionName.QUESTIONNAIRE_WRITE}">
                        <g:link class="edit" action="edit" id="${questionnaireInstance?.id}">
                            <g:message code="default.button.rename.label"/>
                        </g:link>
                    </sec:ifAnyGranted>

                    <sec:ifAnyGranted roles="${org.opentele.server.core.model.types.PermissionName.QUESTIONNAIRE_DELETE}">
                        <g:actionSubmit class="delete" action="delete"
                            value="${message(code: 'default.button.delete.label')}"
                            onclick="return confirm('${message(code: 'default.button.delete.confirm.message')}');" />
                    </sec:ifAnyGranted>

                    <g:if test="${questionnaireInstance?.editorState}">
                        <sec:ifAnyGranted roles="${org.opentele.server.core.model.types.PermissionName.QUESTIONNAIRE_WRITE}">
                            <g:link class="edit" controller="questionnaireEditor" action="show" id="${questionnaireInstance?.id}">
                                <g:message code="questionnaire.button.show.in.editor.label"/>
                            </g:link>
                        </sec:ifAnyGranted>
                    </g:if>
				</fieldset>
			</g:form>
	</div>
</body>
</html>
