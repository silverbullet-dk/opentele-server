<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title><g:message code="questionnaireEditor"/></title>
        <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}">
    <link href="${resource(dir: 'css', file: 'bootstrap-combined.no-icons.2.3.1.min.css')}" rel="stylesheet">
    <link href="${resource(dir: 'css', file: 'font-awesome.min.css')}" rel="stylesheet">
    <link href="${resource(dir: 'css', file: 'jquery-ui.custom.css')}" rel="stylesheet">
    <link href="${resource(dir: 'css', file: 'questionnaireEditor.css')}" rel="stylesheet">

    <!-- Internationalization -->
    <r:script disposition="head">
    i18n = {
        startNodeName: "${g.message(code:'questionnaireEditor.nodetype.start')}",
        endNodeName: "${g.message(code:'questionnaireEditor.nodetype.end')}",
        skipMeasurementEndpoint: "${g.message(code:'questionnaireEditor.skipMeasurementEndpoint')}",
        yesSourceEndpoint: "${g.message(code:'questionnaireEditor.yesSourceEndpoint')}",
        noSourceEndpoint: "${g.message(code:'questionnaireEditor.noSourceEndpoint')}",
        importFailed: "${g.message(code:'questionnaireEditor.importFailed')}",
        couldNotSave: "${g.message(code:'questionnaireEditor.couldNotSave')}",
        questionnaireSaved: "${g.message(code:'questionnaireEditor.questionnaireSaved')}"
    }
    </r:script>

    <r:layoutResources/>

    <!--[if lt IE 9]>
    <script type="text/javascript">
        alert("${g.message(code:'questionnaireEditor.unsupported')}");
    </script>
   <![endif]-->
</head>
<body>

<g:javascript src="jquery-1.9.1.min.js"/>
<g:javascript src="jquery-ui/jquery-ui-1.10.1.min.js"/>
<g:javascript src="jquery-ui/jquery.ui.datepicker-da.js"/>
<g:javascript src="jquery.openteleDatePicker.js"/>
<link rel="stylesheet" href="${resource(dir: 'css', file: 'jquery.optenteleDatePicker.css')}" type="text/css">
<g:javascript src="underscore-min.js"/>

<g:hiddenField name="editUrl" value="${g.createLink(action: "edit")}"/>
<g:hiddenField name="editorStateUrl" value="${g.createLink(action: "editorState")}"/>
<g:hiddenField name="saveUrl" value="${g.createLink(action: "save")}"/>
<g:hiddenField name="questionnaireHeaderId" value="${questionnaireHeader.id}"/>
<g:hiddenField name="questionnaireId" value="${questionnaire.id}"/>
<g:hiddenField name="exitUrl" value="${g.createLink(controller: "questionnaireHeader", action: "show", id: questionnaireHeader.id)}"/>

<div class="container">
    <div class="navbar">
        <div class="navbar-inner">
            <a class="brand" href="#">
                <g:message code="questionnaireEditor"/>
            </a>
            <ul class="nav">
                <li>
                    <form class="navbar-form" id="top_menu">
                        <g:textField class="span5" name="title" value="${questionnaireHeader.name}" readonly="true"/>
                        <button class="btn btn-primary" data-loading-text="${g.message(code: 'default.button.saving.label')}" type="button" id="menu_save"><g:message code="questionnaireEditor.button.save.draft"/> </button>
                        <button class="btn btn-primary" data-loading-text="${g.message(code: 'default.button.saving.label')}" type="button" id="menu_save_and_exit"><g:message code="questionnaireEditor.button.save.exit"/> </button>
                        %{--//TODO: NOT included, as there's no check for modifications in model yet. <button class="btn btn-primary" dtype="button" id="menu_exit"><g:message code="questionnaireEditor.button.exit"/> </button>--}%
                    </form>
                </li>
            </ul>
            <ul class="nav pull-right">
                <li id="fat-menu" class="dropdown">
                    <a href="#" id="drop-import-export" class="dropdown-toggle" data-toggle="dropdown"><g:message code="questionnaireEditor.menu.importExport"/> <b class="icon-angle-down"></b></a>
                    <ul class="dropdown-menu" aria-labelledby="drop-import-export">
                        <li id="import_questionnaire"><a tabindex="-1" href="#"><g:message code="questionnaireEditor.menu.importExport.import"/></a></li>
                        <li id="export_questionnaire"><a tabindex="-1" href="#"><g:message code="questionnaireEditor.menu.importExport.export"/></a></li>
                    </ul>
                </li>
            </ul>
        </div>
    </div>
    <div class="row">
        <div class="span3">
            <div class="well">
                <ul class="nav nav-list">
                    <li class="nav-header"><g:message code="questionnaireEditor.nodetypes"/> </li>
                    <li><a id="menu_add_node_start" href="#"><i
                            class="icon-beaker icon-circle-blank icon-large"></i> <g:message
                            code="questionnaireEditor.nodetype.start"/></a></li>
                    <li><a id="menu_add_node_measurement" href="#"><i class="icon-beaker icon-large"></i> <g:message
                            code="questionnaireEditor.nodetype.measurement"/></a></li>
                    <li><a id="menu_add_node_text" href="#"><i class="icon-comment icon-large"></i> <g:message
                            code="questionnaireEditor.nodetype.text"/></a></li>
                    <li><a id="menu_add_node_input" href="#"><i class="icon-question-sign icon-large"></i> <g:message
                            code="questionnaireEditor.nodetype.input"/></a></li>
                    <li><a id="menu_add_node_delay" href="#"><i class="icon-time icon-large"></i> <g:message
                            code="questionnaireEditor.nodetype.delay"/></a></li>
                    <li><a id="menu_add_node_end" href="#"><i class="icon-beaker icon-circle icon-large"></i> <g:message
                            code="questionnaireEditor.nodetype.end"/></a></li>
                </ul>
            </div>
            <div class="well scheduling">
                <form id="schedule">
                    <g:hiddenField name="questionnaireHeader.id" value="${questionnaireHeader.id}"/>

                <g:render template="schedule" />
                </form>
            </div>
        </div>
        <div class="span9">
            <div class="well" id="drawingArea">
            </div>
        </div>
    </div>
</div>

<!-- Modals -->
<g:render template="createMeasurementNodeModal"/>
<g:render template="createTextNodeModal"/>
<g:render template="createInputNodeModal"/>
<g:render template="createDelayNodeModal"/>
<g:render template="loadingModal"/>
<g:render template="createExportQuestionnaireModal"/>
<g:render template="createImportQuestionnaireModal"/>
<g:render template="setConnectionSeverityModal"/>
<r:script>
        // Make sure that user is never logged out when using the questionnaire editor
        setInterval(function() { $.ajax({ url: "${g.createLink(action: 'keepAlive')}" }); }, 30000);
</r:script>

<g:javascript src="jquery.jsPlumb-1.3.16-all-min.js"/>
<g:javascript src="bootstrap-2.3.1.min.js"/>
<g:javascript src="json2.js"/>
<g:javascript src="questionnaireEditor.js"/>
<r:script>
    $('body').on('mouseover','*[data-tooltip]', function() {
        tooltip.show($(this).attr('data-tooltip'))
    }).on('mouseout', '*[data-tooltip]', function() {
        tooltip.hide()
    })
</r:script>
<r:layoutResources/>
</body>
</html>
