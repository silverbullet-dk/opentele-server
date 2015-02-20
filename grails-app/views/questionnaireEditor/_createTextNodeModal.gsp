<%@ page import="org.opentele.server.core.model.types.MeasurementTypeName" %>
<div id="createTextNodeModal" class="modal hide fade" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="myModalLabel">
            <g:message code="questionnaireEditor.modal.title.create.textNode"/>
        </h3>
    </div>
    <div class="modal-body">
        <form>
            <fieldset>
                <label><g:message code="questionnaireEditor.modal.textHeadline"/> </label>
                <textarea rows="2" type="text" id="headline" placeholder="${g.message(code: 'questionnaireEditor.modal.textHeadline')}"></textarea>
            </fieldset>
            <fieldset>
                <label><g:message code="questionnaireEditor.modal.text"/> </label>
                <textarea rows="5" type="text" id="text" placeholder="${g.message(code: 'questionnaireEditor.modal.text')}"></textarea>
            </fieldset>
            <fieldset>
                <label><g:message code="questionnaireEditor.modal.helpText"/> </label>
                <textarea rows="4" type="text" id="helpText" placeholder="${g.message(code: 'questionnaireEditor.modal.helpText')}"></textarea>
            </fieldset>
            <fieldset>
                <label><g:message code="questionnaireEditor.modal.helpImage"/> </label>
                <g:select id="helpImage" name='helpImage'
                          noSelection="${['null':'...']}"
                          from='${org.opentele.server.model.HelpImage.list()}'
                          optionKey="filename" optionValue="filename"></g:select>
            </fieldset>
        </form>
    </div>
    <div class="modal-footer">
        <button id="cancel" class="btn" ><g:message code="questionnaireEditor.modal.cancel"/></button>
        <button id="create" class="btn btn-primary disabled" disabled="true"><g:message code="questionnaireEditor.modal.create"/></button>
    </div>
</div>
<script type="text/javascript">
    //Page validation functionality: Must enter text
    $(new function() {
        var createButton = $('#createTextNodeModal #create');
        var textField = $('#createTextNodeModal #text');
        function setCorrectStateOnCreateButton() {
            if(textField.val().length > 0) {
                createButton.removeClass('disabled');
                createButton.attr('disabled', false);
            } else {
                createButton.addClass('disabled');
                createButton.attr('disabled', true);
            }
        }
        $('#createTextNodeModal').find('textarea').keyup(function() {
            setCorrectStateOnCreateButton();
        });
    });
</script>
