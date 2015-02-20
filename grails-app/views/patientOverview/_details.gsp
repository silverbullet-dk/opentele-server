<g:if test="${noUnacknowledgedQuestionnaires}">
    <g:message code="default.questionnaires.no.new" />
</g:if>
<g:else>
    <cq:renderResultTableForPatient patientID="${patient.id}"
                                    questionPreferences="${questionPreferences}" withPrefs="${true}"
                                    completedQuestionnaireResultModel="${completedQuestionnaireResultModel}"/>
    <!-- Templates for Knockout.js -->
    <script id="prefRowTemplate" type="text/html">
        <tr id="prefQuestion" class="prefQuestion" data-bind="attr: {'selectedQuestionID': $root.getQuestionID($data)}">
            <td>
                <div>
                    <select data-bind="options: $root.questions, optionsText: 'text', value: $data.questionObj, optionsCaption: '${message(code: 'patientOverview.questions.choose')}'" data-tooltip="${message(code:"patientOverview.questions.choose.tooltip")}">
                    </select>
                    <!-- ko if: $root.notLastRow($data) -->
                    <button id="removeBtn" class="remove" data-bind="click: function(){ $data.remove(); }" data-tooltip="${message(code:"patient.questionnaire.preferredValue.remove.tooltip")}"><r:img uri='/images/cancel.png'/></button>
                    <!-- /ko -->
                </div>
            </td>
        </tr>
    </script>
    <script id="prefRowResTemplate" type="text/html">
        <!-- ko if: $data.resultObj() -->
        <tr class="prefResult" data-bind="html: $data.resultObj().text"></tr>
        <!-- /ko -->
    </script>
    <cq:overviewGraphs patient="${patient}"/>
</g:else>
