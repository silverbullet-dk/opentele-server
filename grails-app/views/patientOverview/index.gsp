<%@ page import="org.opentele.server.provider.constants.Constants; org.opentele.server.core.model.types.PermissionName; org.opentele.server.model.PatientGroup"%>

<!doctype html>
<html>
<head>
    <meta name="layout" content="main">
    <g:set var="title" value="${message(code: "patientOverview.title")}"/>
    <title>${title}</title>
    <g:render template="/measurement/graphFunctions"/>
    <g:javascript src="knockout-2.2.0.js" />
    <g:javascript src="OpenTeleTablePreferences.js" />
    <r:require module="opentele-scroll"/>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'patient_overview.css')}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: grailsApplication.config.measurement.results.tables.css)}" type="text/css">
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'jquery.jqplot.css')}" type="text/css">
    <script>
		$(function() {
            $(".questionnaireListInner").slideUp('fast');

            enableMeasurementsIcons();

            function fetchAndShowDetails(patientId, detailsElement, collapseIcon) {
                setCollapseIconToLoading(collapseIcon);
                $.ajax('details/' + patientId, {cache: false}) // IE will cache the result unless we force cache-disabling
                    .done(function(data) {
                        loadPatientDetailsIntoElement(data, detailsElement, patientId, collapseIcon);
                    })
                    .fail(function() {
                        loadingFailed(detailsElement, collapseIcon);
                    });
            }

            function loadPatientDetailsIntoElement(data, element, patientId, collapseIcon) {
                element.html(data);
                initializeDetailsView(element, patientId, collapseIcon);
                showDetails(element, collapseIcon);
            }

            function updatePatientRow(data, element, patientId) {
                element.html(data);
                var collapseIcon = element.find("img.measurementsIcon");
                setCollapseIconToOpened(collapseIcon);
                collapseIcon.data('loaded', true);
                var detailsElement = element.find(".questionnaireListInner");
                initializeDetailsView(detailsElement, patientId, collapseIcon);
            }

            function initializeDetailsView(element, patientId, collapseIcon) {
                new QuestionnaireTableViewModel(patientId, ${questionPreferences}).init();
                enableMeasurementsIcons();

                element.find('.acknowledge').click(function() {
                    var message = '${g.message(code: 'questionnaire.confirmAcknowledgement').encodeAsJavaScript()}';
                    if (confirm(message)) {
                        var automessage = $(this).attr('data-automessage');
                        var questionnaireId = $(this).attr('data-questionnaire-id');
                        var patientEntryElement = $(this).closest('.patientEntry');

                        // Show loader, disable all other links
                        $(this).find('img').attr({src: '../images/loader.gif'}).css({width: '20px', height: '20px'});
                        disableOtherAcknowlagdeIcons(patientEntryElement);


                        var url = '${g.createLink(controller: 'patientOverview', action: 'acknowledgeQuestionnaireAndRenderDetails', absolute:true)}/?id=' + questionnaireId + '&withAutoMessage=' + automessage;
                        $.post(url)
                        .done(function(data) {
                            updatePatientRow(data, patientEntryElement, patientId);
                        })
                        .fail(function() {
                            loadingFailed(element, collapseIcon);
                        });
                    }
                });

                collapseIcon.data('loaded', true);
            }

            function disableOtherAcknowlagdeIcons(patientEntryElement) {
                patientEntryElement.find('a').unbind('click');

                patientEntryElement.find('a>img[src$="unacknowledged.png"]').attr({src: '../images/unacknowledgedDisabled.png'});
                patientEntryElement.find('a>img[src$="unacknowledgedWithAutoMessage.png"]').attr({src: '../images/unacknowledgedWithAutoMessageDisabled.png'});
            }

            function loadingFailed(detailsElement, collapseIcon) {
                detailsElement.html('<h1><g:message code="patientOverview.errorFetchingDetails"/></h1>');
                setCollapseIconToOpened(collapseIcon);
                collapseIcon.data('loaded', false);
            }

            function showDetails(detailsElement, collapseIcon) {
                detailsElement.slideDown();
                var rightTable = collapseIcon.closest("div.questionnaireList").find("div#rightTable").focus();
                detailsElement.find('div').trigger('visibilityChanged');
                setCollapseIconToOpened(collapseIcon);
            }

            function hideDetails(detailsElement, collapseIcon) {
                detailsElement.slideUp();
                detailsElement.find('div').trigger('visibilityChanged');
                setCollapseIconToClosed(collapseIcon);
            }

            function enableMeasurementsIcons() {
                var measurementsIcons = $("img.measurementsIcon");
                measurementsIcons.unbind('click');
                measurementsIcons.click(function() {
                    var collapseIcon = $(this);
                    var container = collapseIcon.closest("div.questionnaireList");
                    var patientId = container.attr("id");
                    var detailsElement = container.find("div.questionnaireListInner");
                    if (detailsElement.is(":hidden")) {
                        if (collapseIcon.data('loaded')) {
                            showDetails(detailsElement, collapseIcon);
                        } else {
                            fetchAndShowDetails(patientId, detailsElement, collapseIcon);
                        }
                    } else {
                        hideDetails(detailsElement, collapseIcon);
                    }
                });
            }

            function setCollapseIconToOpened(collapseIcon) {
                collapseIcon.attr({src: '../images/measurements_collapse.png', style: 'margin:0px'});
            }

            function setCollapseIconToClosed(collapseIcon) {
                collapseIcon.attr({src: '../images/measurements_expand.png', style: 'margin:0px'});
            }

            function setCollapseIconToLoading(collapseIcon) {
                collapseIcon.attr({src: '../images/loader.gif', style: 'margin:7px'});
            }
		});
	</script>
</head>

<body>
	<g:if test="${flash?.error?.size() > 0}">
		<ul class="errors" role="alert">
			<li class="error" role="status">
				${flash.error}
			</li>
		</ul>
	</g:if>
	<g:if test="${flash.message}">
		<div class="message" role="status">
			${flash.message}
		</div>
	</g:if>

	<h1 class="fieldcontain">${title}</h1>

    <sec:ifAnyGranted roles="${PermissionName.QUESTIONNAIRE_ACKNOWLEDGE}">
        <div id="overviewButtons" class="buttons clearfix">
            <g:form action="index" style="display: inline-block; float: right;  width: 450px" method="GET">
                <g:submitButton name="submitFilter" class="search" data-tooltip="${message(code: "patientOverview.patientGroupFilter.tooltip")}" value="${message(code: 'patientOverview.filter')}"/>
                <g:select id="filterDropdown"
                          name="patientgroup.filter.id"
                          class="many-to-one"
                          from="${clinicianPatientGroups}"
                          value="${session[Constants.SESSION_PATIENT_GROUP_ID]}"
                          noSelection="['':message(code: 'patientOverview.option.allPatientGroups')]"
                          optionKey="id"
                    />
            </g:form>
            <g:if test="${!patients.empty}">
                <tmpl:acknowledgeGreen />
            </g:if>

        </div>
    </sec:ifAnyGranted>
    <r:script>
        $('.ie #filterDropdown')
                .on('mousedown',function () {
                    $(this).css('width', 'auto');
                }).on('blur change', function () {
                    $(this).css('width', '300px');
                });
    </r:script>

<div id="list-patient" class="content scaffold-list" role="main">
		<g:each in="${patients}" var="patientOverview">
			<div class="patientEntry" id="${patientOverview.patientId}">
				<div class="questionnaireList" id="${patientOverview.patientId}">
					<cq:renderOverviewForPatient patientOverview="${patientOverview}"
                                                 patientNotes="${patientNotes[patientOverview.patientId]}"
                                                 messagingEnabled="${idsOfPatientsWithMessaging.contains(patientOverview.patientId)}"
                                                 alarmIfUnreadMessagesToPatientDisabled="${idsOfPatientsWithAlarmIfUnreadMessagesDisabled.contains(patientOverview.patientId)}"
                    />
					<div class="questionnaireListInner" style="display: none;">
					</div>
				</div>
			</div>
		</g:each>
	</div>
</body>
</html>
