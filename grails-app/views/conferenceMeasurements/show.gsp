<%@ page import="org.opentele.server.provider.constants.Constants; org.opentele.server.core.model.ConferenceMeasurementDraftType" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="conferenceMeasurements">

    <r:script>
    $(function() {
        $('#measurementDraftType').change(function() {
            var type = $(this).val();
            var automatic = false;
            if (stringStartsWith(type, 'AUTOMATIC_')) {
                type = type.substring(10);
                automatic = true;
            } else if (stringStartsWith(type, 'MANUAL_')) {
                type = type.substring(7);
            }
            var tr = $('<tr/>').appendTo('#measurements');
            tr.load('${createLink(action: 'loadForm')}', {id: ${conference.id}, type: type, automatic: automatic}, function() {
                $('#measurementDraftType').val('null');
            });
        });

        $('body').on('click', '.delete', function(element) {
            var tableRow = $(this).closest('tr');
            var measurementId = tableRow.find("input[name='id']").val();

            $(this).remove();
            addDeletionToUpdateQueue(measurementId);
        });

        $('body').on('change', 'input', function() {
            var tr = $(this).parents('tr');
            var inputFields = $('input', tr);

            addFieldUpdateToUpdateQueue(inputFields);
        });

        setTimeout(checkAutomaticMeasurements, 2000);

        function checkAutomaticMeasurements() {
            var promises = $.map($.find('.waiting-measurement'), function(element) {
                return startUpdatingWaitingElement(element);
            });
            $.when(promises).always(function() {
                setTimeout(checkAutomaticMeasurements, 2000);
            });
        }

        function startUpdatingWaitingElement(element) {
            var measurementId = $(element).find("input[name='id']").val();
            return $.ajax('${createLink(action: 'loadAutomaticMeasurement')}?id=' + measurementId)
            .success(function(data) {
                if (data !== '') {
                    updateWaitingElement(element, data);
                }
            });
        }

        function updateWaitingElement(element, data) {
            $.map(data, function(value, key) {
                $(element).find("span[name='" + key + "']").html(data[key]);
            });
            $(element).find('.waiting').hide();
            $(element).find('.loaded').show();
            $(element).removeClass('waiting-measurement');
        }

        var updateQueue = [];
        var updateInProgress = false;

        function addFieldUpdateToUpdateQueue(inputFields) {
            updateQueue.push({ type: 'fieldUpdate', inputFields: inputFields });
            processUpdateQueue();
        }

        function addDeletionToUpdateQueue(measurementId) {
            updateQueue.push({ type: 'deletion', measurementId: measurementId });
            processUpdateQueue();
        }

        function processUpdateQueue() {
            if (!updateInProgress && updateQueue.length > 0) {
                setUpdateInProgress(true);

                var job = updateQueue.shift();
                var jobType = job.type;

                var postAction;
                if (jobType === 'fieldUpdate') {
                    var inputFields = job.inputFields;
                    postAction = $.post('${createLink(action: 'updateMeasurement')}', inputFields.serializeArray(), function(data) {
                        updateConferenceVersionEverywhere(data['conferenceVersion']);
                        setFieldStyles(inputFields, data['warnings'], data['errors']);
                    }, 'json');
                } else if (jobType === 'deletion') {
                    var measurementId = job.measurementId;
                    postAction = $.post('${createLink(action: 'deleteMeasurement')}', { id: measurementId }, function(data) {
                        updateConferenceVersionEverywhere(data['conferenceVersion']);
                        visualizeMeasurementAsDeleted(measurementId);
                    }, 'json');
                }

                postAction.fail(function() {
                    alert('${g.message(code: 'conferenceMeasurement.error')}');
                })
                .always(function() {
                    setUpdateInProgress(false);
                    processUpdateQueue();
                });
            }
        }

        function setUpdateInProgress(inProgress) {
            updateInProgress = inProgress;
            setSubmitDisabled(inProgress);
        }

        function setFieldStyles(inputFields, warningFields, errorFields) {
            for (var i=0; i < inputFields.length; i++) {
                var inputField = inputFields[i];
                var name = inputField.name;
                var isError = arrayContains(errorFields, name);
                var isWarning = !isError && arrayContains(warningFields, name);
                var errorMessageField = $(inputField).closest('td').find('div[name=' + name + '-error-message]');

                if (isError) {
                    $(inputField).addClass('error');
                    errorMessageField.slideDown();
                } else {
                    $(inputField).removeClass('error');
                    errorMessageField.slideUp();
                }

                if (isWarning) {
                    $(inputField).addClass('warning');
                } else {
                    $(inputField).removeClass('warning');
                }
            }
        }

        function visualizeMeasurementAsDeleted(measurementId) {
            var idFieldForMeasurement = $("input[name='id'][value='" + measurementId + "']");
            var tableRow = idFieldForMeasurement.closest('tr');

            tableRow.find('input').prop('disabled', true);
            tableRow.find('td,div,span').css('text-decoration', 'line-through');
        }

        function updateConferenceVersionEverywhere(newVersion) {
            $("input[name='conferenceVersion']").val(newVersion);
        }

        function setSubmitDisabled(disabled) {
            var hasWarnings = $('.warning').length > 0;
            var hasErrors = $('.error').length > 0;
            if (hasWarnings || hasErrors) {
                disabled = true
            }
            $("input[name='confirm']").attr('disabled', disabled);
        }

        function arrayContains(array, value) {
            return $.inArray(value, array ) != -1;
        }

        function stringStartsWith(string, prefix) {
            return string.indexOf(prefix) == 0;
        }
    });
    </r:script>

    <title><g:message code="conferenceMeasurement.title" /></title>
</head>

<body>
<h1><g:message code="conferenceMeasurement.header" /></h1>
<p>
    <span id="patent_name">${session[Constants.SESSION_NAME]} </span>
    <span id="patient_cpr"><g:message code="main.SSN"/>: ${session[Constants.SESSION_CPR]} </span>
</p>
<table>
    <thead>
    <tr>
        <th><g:message code="conferenceMeasurement.type"/></th>
        <th><g:message code="conferenceMeasurement.value"/></th>
        <th><g:message code="conferenceMeasurement.include"/></th>
        <th></th>
    </tr>
    </thead>
    <tbody id="measurements">
        <g:each in="${measurementDrafts.sort { it.id }}" var="measurementDraft">
            <tr>
                <g:if test="${measurementDraft.type == ConferenceMeasurementDraftType.BLOOD_PRESSURE && !measurementDraft.automatic}">
                    <g:render template="drafts/manualBloodPressure" model="[measurement: measurementDraft]"/>
                </g:if>
                <g:elseif test="${measurementDraft.type == ConferenceMeasurementDraftType.BLOOD_PRESSURE && measurementDraft.automatic}">
                    <g:render template="drafts/automaticBloodPressure" model="[measurement: measurementDraft]"/>
                </g:elseif>
                <g:elseif test="${measurementDraft.type == ConferenceMeasurementDraftType.LUNG_FUNCTION && !measurementDraft.automatic}">
                    <g:render template="drafts/manualLungFunction" model="[measurement: measurementDraft]"/>
                </g:elseif>
                <g:elseif test="${measurementDraft.type == ConferenceMeasurementDraftType.LUNG_FUNCTION && measurementDraft.automatic}">
                    <g:render template="drafts/automaticLungFunction" model="[measurement: measurementDraft]"/>
                </g:elseif>
                <g:elseif test="${measurementDraft.type == ConferenceMeasurementDraftType.SATURATION && !measurementDraft.automatic}">
                    <g:render template="drafts/manualSaturation" model="[measurement: measurementDraft]"/>
                </g:elseif>
                <g:elseif test="${measurementDraft.type == ConferenceMeasurementDraftType.WEIGHT && !measurementDraft.automatic}">
                    <g:render template="drafts/manualWeight" model="[measurement: measurementDraft]"/>
                </g:elseif>
                <g:else>
                    <td colspan="3"><g:message code="conferenceMeasurement.unknownType" args="[measurementDraft.type]"/></td>
                </g:else>
            </tr>
        </g:each>
    </tbody>
    <tfoot>
        <tr>
            <td colspan="4">
                <g:message code="conferenceMeasurement.addMeasurement"/>
                <g:select name="measurementDraftType" valueMessagePrefix="conferenceMeasurement.measurementType" from="['MANUAL_BLOOD_PRESSURE', 'AUTOMATIC_BLOOD_PRESSURE', 'MANUAL_LUNG_FUNCTION', 'AUTOMATIC_LUNG_FUNCTION', 'MANUAL_SATURATION', 'AUTOMATIC_SATURATION', 'MANUAL_WEIGHT']" noSelection="[null: '']"/>

                <g:form action="confirm" id="${conference.id}">
                    <g:hiddenField name="conferenceVersion" value="${conference.version}"/>
                    <g:submitButton name="confirm" value="${g.message(code: 'conferenceMeasurement.saveAndClose')}"/>
                </g:form>
            </td>
        </tr>
    </tfoot>
</table>
</body>
</html>