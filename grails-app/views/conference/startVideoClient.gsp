<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="main">
    <title><g:message code="conference.startVideoClient.title"/></title>

    <script type="text/javascript">
        var timer;
        $(function() {
            timer = setInterval(fetchEndpointId, 3000);
        });

        function fetchEndpointId() {
            document.getElementById('vidyoApplet').getEndpointId();
        }

        function endpointIdReceived(endpointId) {
            clearInterval(timer);

            $.post('${createLink(action: 'linkEndpoint')}', { endpointId: endpointId }, function(data) {
                waitForClient();
            }, 'json')
            .fail(function() {
                alert('Noget er gået galt. Prøv venligst forfra.');
            });
        }

        function clientNotRunning() {
            showInformationText('clientNotRunning');
        }

        function waitForClient() {
            showInformationText('waitingForClient');
            var clientStatus = document.getElementById('vidyoApplet').getClientStatus();
            if (clientStatus == 'VE_STATUS_BOUND') {
                $.post('${createLink(action: 'joinConference')}', function(data) {
                    waitForJoiningConference();
                }, 'json')
                .fail(function() {
                    alert('Noget er gået galt. Prøv venligst forfra.');
                });
            } else if (clientStatus == 'VE_STATUS_UNKNOWN') {
                showInformationText('clientError');
            } else {
                setTimeout(waitForClient, 500);
            }
        }

        function waitForJoiningConference() {
            showInformationText('waitingForJoiningConference');
            $.post('${createLink(action: 'conferenceJoined')}', function(data) {
                if (data.joined) {
                    $('#finishSettingUpConferenceForm').submit();
                } else {
                    setTimeout(waitForJoiningConference, 500);
                }
            }, 'json')
            .fail(function() {
                alert('Noget er gået galt. Prøv venligst forfra.');
            });
        }

        function showInformationText(shownElementId) {
            $('.informationText').hide();
            $('#' + shownElementId).show();
        }
    </script>
</head>
<body>
    <div id="fetchingEndpointId" class="informationText">
        <h1><g:message code="conference.startVideoClient.fetchingEndpointId.header"/></h1>
        <p><g:message code="conference.startVideoClient.fetchingEndpointId.description1"/>
        <ul class="bullet-list">
            <li><g:message code="conference.startVideoClient.fetchingEndpointId.bullet1"/></li>
            <li><g:message code="conference.startVideoClient.fetchingEndpointId.bullet2"/></li>
        </ul>
        <g:message code="conference.startVideoClient.fetchingEndpointId.description2"/> </p>
    </div>

    <div id="clientNotRunning" class="informationText" style="display:none">
        <h1><g:message code="conference.startVideoClient.clientNotRunning.header"/></h1>
        <p><g:message code="conference.startVideoClient.clientNotRunning.description"/></p>
    </div>

    <div id="waitingForClient" class="informationText" style="display:none">
        <h1><g:message code="conference.startVideoClient.waitingForClient.header"/></h1>
        <p><g:message code="conference.startVideoClient.waitingForClient.description"/></p>
    </div>

    <div id="waitingForJoiningConference" class="informationText" style="display:none">
        <h1><g:message code="conference.startVideoClient.waitingForJoiningConference.header"/></h1>
        <p><g:message code="conference.startVideoClient.waitingForJoiningConference.description"/></p>
    </div>

    <div id="clientError" class="informationText" style="display:none">
        <h1><g:message code="conference.startVideoClient.clientError.header"/></h1>
        <p><g:message code="conference.startVideoClient.clientError.description"/></p>
    </div>

    <g:form action="finishSettingUpConference" name="finishSettingUpConferenceForm"/>

    <video:applet clientParameters="${flash['clientParameters']}"
                  callback="endpointIdReceived"
                  clientNotRunningCallback="clientNotRunning"/>
</body>
</html>