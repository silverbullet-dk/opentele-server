<%@ page import="org.opentele.server.core.model.types.GlucoseInUrineValue; org.opentele.server.core.model.types.ProteinValue; org.opentele.server.core.model.types.MeasurementTypeName" %>
<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="title" value="${message(code: 'default.patient.addmeasurements.title')}"/>
    <title>${title}</title>

    <g:javascript>
        $('#addbutton').click(function () {
            $("#tr" + ($('#addselect').val())).toggle(true);
            $("#show" + ($('#addselect').val())).val("true");

            $("input[type=submit]").removeAttr("disabled");
        });

        function clearSelected() {
            if ($("#showSATURATION").val() == '' && $("#showWEIGHT").val() == '' && $("#showBLOOD_PRESSURE").val() == '' && $("#showURINE_COMBI").val() == '' && $("#showLUNG_FUNCTION").val() == '')
                $("input[type=submit]").attr("disabled", "disabled");
            return this;
        };

        $('#removeSATURATION').click(function () {
            $("#trSATURATION").toggle(false);
            $("#showSATURATION").val(null);
            clearSelected()
        });
        $('#removeWEIGHT').click(function () {
            $("#trWEIGHT").toggle(false);
            $("#showWEIGHT").val(null);
            clearSelected()
        });
        $('#removeBLOOD_PRESSURE').click(function () {
            $("#trBLOOD_PRESSURE").toggle(false);
            $("#showBLOOD_PRESSURE").val(null);
            clearSelected()
        });
        $('#removeLUNG_FUNCTION').click(function () {
            $("#trLUNG_FUNCTION").toggle(false);
            $("#showLUNG_FUNCTION").val(null);
            clearSelected()
        });
        $('#removeURINE_COMBI').click(function () {
            $("#trURINE_COMBI").toggle(false);
            $("#showURINE_COMBI").val(null);
            clearSelected()
        });

        $(document).ready(function () {
            $('#confirmOK').hide();
            $('#confirmERROR').hide();
        });

        function update(data) {
            $('#confirmOK').hide();
            $('#confirmERROR').hide();

            if (data.success) {
                $('#confirmOK').show();
                $('#confirmOK').html(data.message);

                $("#trBLOOD_PRESSURE").toggle(false);
                $("#showBLOOD_PRESSURE").val(null);
                $("#systolic").val('');
                $("#diastolic").val('');
                $("#pulse").val('');

                $("#trLUNG_FUNCTION").toggle(false);
                $("#showLUNG_FUNCTION").val(null);
                $("#lungfunction").val('');

                $("#trURINE_COMBI").toggle(false);
                $("#showURINE_COMBI").val(null);
                $("#urine").val('');
                $("#urine_glucose").val('');

                $("#trSATURATION").toggle(false);
                $("#showSATURATION").val(null);
                $("#saturation").val('');
                $("#saturationPuls").val('');

                $("#trWEIGHT").toggle(false);
                $("#showWEIGHT").val(null);
                $("#weight").val('');

                clearSelected();

            } else {
                var errorList = $('<ul>');
                for (var i = 0; i < data.errors.length; i++) {
                    errorList.append('<li>' + data.errors[i] + "</li>");
                }
                errorList.append('</ul>');

                $('#confirmERROR').html(errorList);
                $('#confirmERROR').show();
            }

            $('#subscribeField').val('');
        }
    </g:javascript>
</head>

<body>
<div id="confirmOK" class="message"></div>
<div id="confirmERROR" class="errors"></div>

<div id="show-patient" class="content scaffold-show" role="main">

    <h1>${title}</h1>

    <g:formRemote onSuccess="update(data)" name="input_form" url="[controller: 'consultation', action: 'save']">
        <g:hiddenField name="showBLOOD_PRESSURE"/>
        <g:hiddenField name="showWEIGHT"/>
        <g:hiddenField name="showSATURATION" value="${showSATURATION}"/>
        <g:hiddenField name="showLUNG_FUNCTION" value="${showLUNG_FUNCTION}"/>
        <g:hiddenField name="showURINE_COMBI" value="${showURINE_COMBI}"/>

        <g:hiddenField name="id" value="${patientInstance.id}"/>

        <table><tr><td>
            <g:message code="consultation.addmeasurement"/>
            <g:select
                    id="addselect"
                    name="addselect"
                    from="${MeasurementTypeName.TABLE_CAPABLE_MEASUREMENT_TYPE_NAME_CONSULTATION*.value()}"
                    valueMessagePrefix="consultation.measurement"/>
            <input id="addbutton" type="button" value="${message(code: 'consultation.button.addmeasurement')}">
        </td></tr></table>

        <table id="tblResult">
            <tr>
                <th><g:message code="consultation.measurement.type"/></th>
                <th colspan="2"><g:message code="consultation.measurement.values"/></th>
            </tr>
            <tr id="trBLOOD_PRESSURE" style="display:none">
                <td><g:message code="consultation.measurement.blood"/></td>
                <td>
                    <table style="border: none">
                        <tr>
                            <td style="border: none"><g:message code="consultation.measurement.systolic.SYSTOLIC"/></td>
                            <td style="border: none" width="50%"><g:textField name="systolic" value="${systolic}"/></td>
                        </tr>
                        <tr>
                            <td style="border: none"><g:message
                                    code="consultation.measurement.diastolic.DIASTOLIC"/></td>
                            <td style="border: none"><g:textField name="diastolic" value="${diastolic}"/></td>
                        </tr>
                        <tr>
                            <td style="border: none"><g:message code="consultation.measurement.value.PULSE"/></td>
                            <td style="border: none"><g:textField name="pulse" value="${pulse}"/></td>
                        </tr>
                    </table>
                </td>
                <td><g:img id="removeBLOOD_PRESSURE" dir="images" file="delete.png" class="delete"/></td>
            </tr>
            <tr id="trLUNG_FUNCTION" style="display:none">
                <td><g:message code="consultation.measurement.spirometri"/></td>
                <td>
                    <table style="border: none">
                        <tr>
                            <td style="border: none"><g:message
                                    code="consultation.measurement.value.LUNGFUNCTION"/></td>
                            <td style="border: none" width="50%"><g:textField name="lungfunction"/></td>
                        </tr>
                    </table>
                </td>
                <td><g:img id="removeLUNG_FUNCTION" dir="images" file="delete.png" class="delete"/></td>
            </tr>
            <tr id="trSATURATION" style="display:none">
                <td><g:message code="consultation.measurement.saturation"/></td>
                <td>
                    <table style="border: none">
                        <tr>
                            <td style="border: none"><g:message code="consultation.measurement.value.SATURATION"/></td>
                            <td style="border: none" width="50%"><g:textField name="saturation"/></td>
                        </tr>
                        <tr>
                            <td style="border: none"><g:message code="consultation.measurement.value.PULSE"/></td>
                            <td style="border: none"><g:textField name="saturationPuls"/></td>
                        </tr>
                    </table>
                </td>
                <td><g:img id="removeSATURATION" dir="images" file="delete.png" class="delete"/></td>
            </tr>
            <tr id="trWEIGHT" style="display:none">
                <td><g:message code="consultation.measurement.weight"/></td>
                <td>
                    <table style="border: none">
                        <tr>
                            <td style="border: none"><g:message code="consultation.measurement.value.WEIGHT"/></td>
                            <td style="border: none" width="50%"><g:textField name="weight"/></td>
                        </tr>
                    </table>
                </td>
                <td><g:img id="removeWEIGHT" dir="images" file="delete.png" class="delete"/></td>
            </tr>

            <tr id="trURINE_COMBI" style="display:none">
                <td><g:message code="threshold.URINE_COMBI"/></td>
                <td>
                    <table style="border: none">
                        <tr>
                            <td style="border: none"><g:message
                                    code="consultation.measurement.value.urine.protein"/></td>
                            <td style="border: none" width="50%"><g:select name="urine"
                                                                           from="${ProteinValue.values()}"
                                                                           keys="${ProteinValue.values()}"/></td>
                        </tr>
                        <tr>
                            <td style="border: none"><g:message
                                    code="consultation.measurement.value.urine.glucose"/></td>
                            <td style="border: none" width="50%"><g:select name="urine_glucose"
                                                                           from="${GlucoseInUrineValue.values()}"
                                                                           keys="${GlucoseInUrineValue.values()}"/></td>
                        </tr>
                    </table>
                </td>
                <td><g:img id="removeURINE_COMBI" dir="images" file="delete.png" class="delete"/></td>
            </tr>
        </table>

        <fieldset class="buttons" style="text-align: right;">
            <g:submitButton name="subme" class="save" action="save" value="${message(code: 'default.button.save.label')}"
                            disabled="disabled"
            />
        </fieldset>
    </g:formRemote>

</div>
</body>
</html>