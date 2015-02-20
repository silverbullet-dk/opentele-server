<%@ page import="org.opentele.server.core.model.types.Severity" %>
<div id="set_connection_severity_modal" class="modal hide fade" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3>
           <g:message code="patient.overview.severity"/>
        </h3>
    </div>
    <div class="modal-body">
        <form>
            <fieldset>
                <label class="radio">
                    <input type="radio" name="severity" id="${Severity.GREEN}" value="${Severity.GREEN}">
                    <g:message code="enum.severity.GREEN"/>
                </label>
                <label class="radio">
                    <input type="radio" name="severity" id="${Severity.YELLOW}" value="${Severity.YELLOW}">
                    <g:message code="enum.severity.YELLOW"/>
                </label>
                <label class="radio">
                    <input type="radio" name="severity" id="${Severity.RED}" value="${Severity.RED}">
                    <g:message code="enum.severity.RED"/>
                </label>
            </fieldset>
        </form>
    </div>
    <div class="modal-footer">
        <button id="create" class="btn btn-primary" data-dismiss="modal"><g:message code="questionnaireEditor.modal.ok"/></button>
    </div>
</div>
