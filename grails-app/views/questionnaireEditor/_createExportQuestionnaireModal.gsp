<%@ page import="org.opentele.server.core.model.types.MeasurementTypeName" %>
<div id="export_questionnaire_modal" class="modal hide fade" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3>
           Data for Spørgeskema
        </h3>
    </div>
    <div class="modal-body">
        <form>
            <fieldset>
                <label><g:message code="questionnaireEditor.modal.text"/> </label>
                <textarea rows="15" type="text" id="questionnaire_json" readonly="true"></textarea>
            </fieldset>
        </form>
    </div>
    <div class="modal-footer">
        <button id="close" class="btn btn-primary" data-dismiss="modal">Luk</button>
    </div>
</div>
