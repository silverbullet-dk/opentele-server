<%@ page import="org.opentele.server.model.StandardThresholdSet" %>
<table>
    <thead>
    <tr>
        <th><g:message code="threshold.alertHigh" default="Alert High"/></th>
        <th><g:message code="threshold.warningHigh" default="Warning High"/></th>
        <th><g:message code="threshold.warningLow" default="Warning Low"/></th>
        <th><g:message code="threshold.alertLow" default="Alert Low"/></th>
    </tr>
    </thead>
    <tbody>
        <tmpl:/numericThreshold/threshold threshold="${standardThresholdInstance}" text=""/>
    </tbody>
</table>
<script lang="javascript">
    $('.thresholds')
            .on('focus', 'input[type="text"]', function () {
                var input = $(this);
                input.css('background-color', '');
            })
            .on('blur', 'input[type="text"]', function () {
                var input = $(this);
                if (!input.val()) {
                    input.css('background-color', "#C2C2C2")
                }
            })
            .on('click', 'img', function () {
                var input = $(this).prev();
                input.css('background-color', '#C2C2C2').val('')
            })
            .find('input[type="text"]')
            .each(function () {
                var input = $(this);
                if (!input.val() || input.val() == "") {
                    input.css('background-color', "#C2C2C2")
                }
            });
</script>
