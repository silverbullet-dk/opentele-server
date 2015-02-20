<%@ page import="org.opentele.server.model.PassiveInterval" %>
<!doctype html>
<html>
	<head>
		<meta name="layout" content="main">
		<title><g:message code="passiveInterval.label" /></title>
	</head>
	<body>
	
		<div id="show-passiveInterval" class="content scaffold-show" role="main">
			<h1><g:message code="passiveInterval.label" /></h1>
			<g:if test="${flash.message}">
			<div class="message" role="status">${flash.message}</div>
			</g:if>
			<ol class="property-list passiveInterval">


                <g:if test="${passiveIntervalInstance?.intervalStartDate}">
                    <li class="fieldcontain">
                        <span id="intervalStartDate-label" class="property-label"><g:message code="passiveInterval.intervalStartDate.label" /></span>

                        <span class="property-value" aria-labelledby="intervalStartDate-label"><g:formatDate formatName="default.date.format.notime" date="${passiveIntervalInstance?.intervalStartDate}" /></span>

                    </li>
                </g:if>


                <g:if test="${passiveIntervalInstance?.intervalEndDate}">
				<li class="fieldcontain">
					<span id="intervalEndDate-label" class="property-label"><g:message code="passiveInterval.intervalEndDate.label" /></span>
					
						<span class="property-value" aria-labelledby="intervalEndDate-label"><g:formatDate formatName="default.date.format.notime" date="${passiveIntervalInstance?.intervalEndDate}" /></span>
					
				</li>
				</g:if>

                <g:if test="${passiveIntervalInstance?.comment}">
                    <li class="fieldcontain">
                        <span id="comment-label" class="property-label">
                            <g:message code="passiveInterval.comment.label"/>
                        </span>

                        <span class="property-value" aria-labelledby="comment-label">
                            <g:fieldValue bean="${passiveIntervalInstance}" field="comment"/>
                        </span>
                    </li>
                </g:if>
			</ol>
			<g:form>
				<fieldset class="buttons">
					<g:hiddenField name="id" value="${passiveIntervalInstance?.id}" />
					<g:link class="edit" action="edit" id="${passiveIntervalInstance?.id}"><g:message code="default.button.edit.label" /></g:link>
					<g:actionSubmit class="delete" action="delete" value="${message(code: 'default.button.delete.label')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message')}');" />
				</fieldset>
			</g:form>
		</div>
	</body>
</html>
