<%@ page import="org.opentele.server.core.model.types.DataType; org.opentele.server.core.model.types.MeterTypeName" %>
<div id="createDelayNodeModal" class="modal hide fade" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="myModalLabel">
            <g:message code="questionnaireEditor.modal.title.create.delay"/>
        </h3>
    </div>
    <div class="modal-body">
        <form>
            <fieldset>
                <label><g:message code="questionnaireEditor.modal.shortText"/> </label>
                <input type="text" id="shortText" placeholder="${g.message(code: 'questionnaireEditor.modal.shortText')}" class="span5">

                <label><g:message code="questionnaireEditor.modal.text"/> </label>
                <input type="text" id="text" placeholder="${g.message(code: 'questionnaireEditor.modal.text')} " class="span5">

                <label><g:message code="questionnaireEditor.modal.countTime"/> </label>
                <input type="number" min="0" id="countTime" placeholder="${g.message(code: 'questionnaireEditor.modal.countTime')}" class="span5">

                <label><g:message code="questionnaireEditor.modal.counttype"/> </label>
                <g:select name="countType" from="${[g.message(code: 'questionnaireEditor.modal.counttype.up'), g.message(code: 'questionnaireEditor.modal.counttype.down')]}"  valueMessagePrefix="questionnaireEditor.countType" class="span5"/>
            </fieldset>
        </form>
    </div>
    <div class="modal-footer">
        <button id="cancel" class="btn" ><g:message code="questionnaireEditor.modal.cancel"/></button>
        <button id="create" class="btn btn-primary disabled" disabled="true"><g:message code="questionnaireEditor.modal.create"/></button>
    </div>
</div>

<script type="text/javascript">
    //Page validation functionality: Must enter headline



    $(new function() {
        var createDelayNodeModal = $('#createDelayNodeModal');
        var createButton = createDelayNodeModal.find('#create');
        var shortText = $('#shortText', createDelayNodeModal);
        var text = $('#text', createDelayNodeModal);
        var countTime = $('#countTime', createDelayNodeModal);
        countTime.on('keydown', function(event) {
            return event.which < 48 || (event.which >= 48 && event.which <= 57) || (event.which >= 96 && event.which <= 105)
        });
        function validate() {
            var shortTextValid = shortText.val().length > 0;
            var textValid = text.val().length > 0;
            var countTimeValid = countTime.val().length >0 && parseInt(countTime.val())>0;

            if(shortTextValid && textValid && countTimeValid) {
                createButton.removeClass('disabled');
                createButton.attr('disabled', false);
            } else {
                createButton.addClass('disabled');
                createButton.attr('disabled', true);
            }
        }
        createDelayNodeModal.find('#text, #countTime').keyup(validate).change(validate);
    });
</script>
