var drawingArea;
var lastId = 0;
var nodes = {};
var schemaVersion;

<!-- Look and feel of jsPlumb-->
var setJsPlumbDefaults = function () {
    jsPlumb.importDefaults({
        PaintStyle: {lineWidth: 3, strokeStyle: '#0088cc'},
        Connector: 'Flowchart',
        ConnectionOverlays: [
            [ "Arrow", {location: -14, id: "arrow" } ]
        ],
        Endpoints: [ "Dot", [ "Dot", { radius: 6}]],
        EndpointStyle: {fillStyle: "#0088cc"},
        Anchors: ["BottomCenter", "Continuous"],
        ReattachConnections: true
    });
};

var addNodeToModel = function (nodeModel) {
    if (!nodeModel.position) {
        calculatePosition(nodeModel);
    }
    var nodeKey = nodeModel.id;
    nodes[nodeKey] = nodeModel;
    nodes[nodeKey].id = nodeKey;
};

function calculatePosition(nodeModel) {
    var newPosition = {top: 20, left: 20};
    if (!_.isEmpty(nodes)) {
        var start = _.min(nodes, function (node) {
            return node.position.top;
        });
        var startPos = start.position;
        var tops = _.filter(nodes, function (node) {
            return node.position.top <= startPos.top + 60; //
        }) || newPosition;
        var mostRight = _.max(tops, function (node) {
            return node.position.left;
        }) || newPosition;

        var mostRightPos = mostRight.position;
        newPosition = {top: startPos.top, left: mostRightPos.left + 100};
    }
    nodeModel.position = newPosition;
}


var addNode = function (nodeElement, isSource, hasBooleanChoice, isMeasurementNode) {
    drawingArea.append(nodeElement);
    makeReadyForConnection(nodeElement, isSource, hasBooleanChoice, isMeasurementNode); //Can only be called after element is added
    updatePosition(nodeElement).call();
};
var yesSourceEndPoint = {
    endpoint: "Dot",
    isSource: true,
    overlays: [
        [ "Label", {
            location: [-0.4, 1.0],
            label: i18n.yesSourceEndpoint
        } ]
    ],
    parameters: {
        choiceValue: "true",
        type: "choice",
        severity: ""
    }

};

var sourceEndPoint = {
    endpoint: "Dot",
    isSource: true,
    parameters: {
        severity: "",
        type: "normal"
    }

};

var skipMeasurementEndPoint = {
    endpoint: "Dot",
    isSource: true,
    overlays: [
        [ "Label", {
            location: [1.7, 1.0],
            label: i18n.skipMeasurementEndpoint
        } ]
    ],
    parameters: {
        measurementSkipped: "true",
        type: "measurementSkipped",
        severity: ""

    }
};

var noSourceEndPoint = {
    endpoint: "Dot",
    isSource: true,
    overlays: [
        [ "Label", {
            location: [1.7, 1.0],
            label: i18n.noSourceEndpoint
        } ]
    ],
    parameters: {
        choiceValue: "false",
        type: "choice",
        severity: ""

    }

};

function findEndpointByType(element, type, value) {
    return _.find(jsPlumb.getEndpoints(element), function (endpoint) {
        return endpoint.getParameter(type) === value;
    });
}

function getSkipMeasurementEndpoint(element) {
    return findEndpointByType(element, 'measurementSkipped', "true");
}

function getNormalEndpoint(element) {
    return findEndpointByType(element, 'type', "normal");
}

function getChoiceValueFalseEndpoint(element) {
    return findEndpointByType(element, 'choiceValue', "false");
}

function getChoiceValueTrueEndpoint(element) {
    return findEndpointByType(element, 'choiceValue', "true");
}


function makeReadyForConnection(element, makeAsSource, hasBooleanChoice, isMeasurementNode) {
    if (hasBooleanChoice) {
        if (!getChoiceValueTrueEndpoint(element)) {
            jsPlumb.addEndpoint(element, yesSourceEndPoint, {isSource: makeAsSource, anchor: "BottomLeft"});
        }
        if (!getChoiceValueFalseEndpoint(element)) {
            jsPlumb.addEndpoint(element, noSourceEndPoint, {isSource: makeAsSource, anchor: "BottomRight"});
        }
    } else if (isMeasurementNode) {
        if (!getNormalEndpoint()) {
            jsPlumb.addEndpoint(element, sourceEndPoint, {isSource: makeAsSource, anchor: "BottomLeft"});
        }
        if (!getSkipMeasurementEndpoint()) {
            jsPlumb.addEndpoint(element, skipMeasurementEndPoint, {isSource: skipMeasurementEndPoint, anchor: "BottomRight"});
        }

    } else if (makeAsSource) {
        jsPlumb.addEndpoint(element, sourceEndPoint, {isSource: makeAsSource});
    }

    jsPlumb.makeTarget(element);
}


function removeNodeAndConnections(node) {
    jsPlumb.detachAllConnections(node);
    jsPlumb.removeAllEndpoints(node);

    $(node).remove();
}

function addDeleteAction(node) {
    var deleteIcon = $("<i class='icon-trash deleteIcon'></i>");
    node.append(deleteIcon);
    $(deleteIcon).click(function () {
        var connections = jsPlumb.select({target: node});
        removeNodeAndConnections(node);
        connections.each(function (connection) {
            if (nodes[connection.source.attr('id')].dataType === 'BOOLEAN') {
                makeReadyForConnection(connection.source, true, true);
            } else {
                makeReadyForConnection(connection.source, true);
            }
        });

        var nodeModel = nodes[node.attr('id')];
        if (nodeModel.type === "start") {
            enableAddStartNode()
        }
        if (nodeModel.type === "end") {
            enableAddEndNode()
        }
        delete nodes[$(node).attr('id')];
    });
}

function addEditAction(node) {
    var editIcon = $("<i class='icon-cogs editIcon'></i>");
    node.append(editIcon);

    $(editIcon).click(function () {
        editNode(node);
    });
}

function editTextNode(nodeModel, node) {
    var modalElement = $('#createTextNodeModal');
    var headline = modalElement.find('#headline');
    var text = modalElement.find('#text');
    var helpText = modalElement.find('#helpText');
    var helpImage = modalElement.find('#helpImage');

    headline.val(nodeModel.headline);
    text.val(nodeModel.text);
    helpText.val(nodeModel.helpText);
    helpImage.val(nodeModel.helpImage);

    handleModal($(modalElement),function () {
        nodeModel.headline = headline.val();
        nodeModel.text = text.val();
        nodeModel.helpText = helpText.val();
        nodeModel.helpImage = helpImage.val();
        setNodeContents($(node), nodeModel.text);
        jsPlumb.repaint(node);
    }).call();
}

function editMeasurementNode(nodeModel, node) {
    var modalElement = $('#createMeasurementNodeModal');
    var headline = modalElement.find("#headline");
    var measurementType = modalElement.find("#measurementType");
    var shortText = modalElement.find("#shortText");
    var helpText = modalElement.find('#helpText');
    var helpImage = modalElement.find('#helpImage');

    modalElement.find("#" + nodeModel.measurementForm).prop('checked', true);
    measurementType.val(nodeModel.measurementType);
    headline.val(nodeModel.headline);
    shortText.val(nodeModel.shortText);
    helpText.val(nodeModel.helpText);
    helpImage.val(nodeModel.helpImage);

    handleModal($(modalElement),function () {
        nodeModel.measurementForm = modalElement.find('input[name=measurementForm]:checked').val();
        nodeModel.headline = headline.val();
        nodeModel.measurementType = measurementType.val();
        nodeModel.shortText = shortText.val();
        nodeModel.helpText = helpText.val();
        nodeModel.helpImage = helpImage.val();

        setNodeContents($(node), $('#createMeasurementNodeModal').find('#measurementType [value=' + nodeModel.measurementType + ']').text());
        jsPlumb.repaint(node);
    }).call();
}

function editInputNode(nodeModel, node) {

    var modalElement = $('#createInputNodeModal');
    var question = modalElement.find("#question");
    var dataType = modalElement.find("#dataType");
    var shortText = modalElement.find("#shortText");
    var helpText = modalElement.find('#helpText');
    var helpImage = modalElement.find('#helpImage');

    question.val(nodeModel.question);
    dataType.val(nodeModel.dataType);
    shortText.val(nodeModel.shortText);
    helpText.val(nodeModel.helpText);
    helpImage.val(nodeModel.helpImage);

    handleModal($(modalElement),function () {
        nodeModel.question = question.val();
        nodeModel.dataType = dataType.val();
        nodeModel.shortText = shortText.val();
        nodeModel.helpText = helpText.val();
        nodeModel.helpImage = helpImage.val();
        setNodeContents($(node), nodeModel.shortText);
        jsPlumb.repaint(node);
    }).call();
}

var editDelayNode = function (nodeModel, node) {

    var modalElement = $('#createDelayNodeModal');
    var text = modalElement.find("#text");
    var countType = modalElement.find("#countType");
    var shortText = modalElement.find("#shortText");
    var countTime = modalElement.find("#countTime");

    text.val(nodeModel.text);
    countType.val(nodeModel.countType);
    shortText.val(nodeModel.shortText);
    countTime.val(nodeModel.countTime);

    handleModal($(modalElement),function () {
        nodeModel.text = text.val();
        nodeModel.countType = countType.val();
        nodeModel.shortText = shortText.val();
        nodeModel.countTime = countTime.val();
        setNodeContents($(node), nodeModel.shortText);
        jsPlumb.repaint(node);
    }).call();
};

function editNode(node) {
    var nodeModel = nodes[$(node).attr('id')];

    switch (nodeModel.type) {
        case 'text':
            editTextNode(nodeModel, node);
            break;
        case 'input':
            editInputNode(nodeModel, node);
            break;
        case 'measurement':
            editMeasurementNode(nodeModel, node);
            break;
        case 'delay':
            editDelayNode(nodeModel, node);
            break;
    }
}
function setNodeContents(node, contents) {
    $(node).find("#content").attr('data-tooltip', contents).text(contents);
}
function baseNodeBuilder(icon, contents, idPrefix, skipEditAction, model) {
    var node = $("<div><span id='content'></span></spand></script></div>");
    node.addClass("node");
    addLargeIcon(node, icon);

    setNodeContents(node, contents);
    setId(node, idPrefix, model);
    makeDraggable(node);
    addDeleteAction(node);

    if (!skipEditAction) {
        addEditAction(node);
    }

    return node;
}
function nextId() {
    return ++lastId;
}
function addLargeIcon(element, iconName) {
    var iconAdded = addIcon(element, iconName);
    $(iconAdded).addClass("icon-2x");

    return iconAdded;
}
function addIcon(element, iconName) {
    element.append($("<i class='icon-" + iconName + "' style='float:left;'></i>"));
}
function makeDraggable(element) {
    jsPlumb.draggable(element, {containment: "parent", stop: updatePosition(element)});
}
function updatePosition(element) {
    return function () {
        var node = nodes[$(element).attr('id')];
        if (node) {
            node.position = $(element).position();
        }
    };
}
function setId(element, prefix, model) {
    if (model) {
        //Get node numeric ID from node ID string
        var numberPattern = /\d+/g;
        var idMatches = model.id.match(numberPattern);
        var nodeID;
        //If ID-strings are as expected, that is as this editor makes them,
        // the regexp result should be an array with one element.
        if (idMatches != null) {
            if (idMatches.length > 1) {
                //More than one number sequence in ID-string. ID-string not as
                // expected: Log warning and assume last number sequence is ID.
                console.log("QUESTIONNAIRE EDITOR - WARNING: Found an ID-string that did not look as expected!")
            }
            nodeID = idMatches[0];

            //Check if the current 'lastId' is numerically higher than this nodes numeric ID
            //otherwise set lastId to this nodes ID + 1 (this wont effect the rest of the import,
            //as imported nodes always get their own original ID)
            if (lastId <= nodeID) {
//                console.log("found node id larger than last ID. bumping "+lastId+" to "+nodeID);
                lastId = nodeID++;
            }
        }
    }
    element.attr({
        id: "" + prefix + nextId()
    });
}
function handleModal(modal, onSucess, beforeOpening) {
    return function () {
        if (beforeOpening) {
            beforeOpening();
        }

        modal.modal('show');

        $(modal).on('hide', function () {
            $(modal).find('#create').unbind();
            $(modal).find('#cancel').unbind();
        });

        $(modal).find('#cancel').click(function () {
            modal.modal('hide');
        });

        $(modal).find('#create').click(function () {
            onSucess(null);
            modal.modal('hide');
        });

        $(modal).on('shown.bs.modal', function () {
            $('input, textarea', modal).focus();
        });
    };
}
function serializeConnections() {
    return jsPlumb.getConnections().map(function (connection) {
        return {
            source: connection.sourceId,
            target: connection.targetId,
            choiceValue: connection.getParameter('choiceValue'),
            severity: connection.getParameter('severity'),
            measurementSkipped: connection.getParameter('measurementSkipped'),
            type: connection.getParameter('type')
        };
    });
}
function serializeSchedule(map) {
    map = map || {};

    // Find all properties with the same name
    var mapped = _.chain($('#schedule').serializeArray()).groupBy(function (e) {
        return e.name;
    }).map(function (values, key) {
            // Find the value (either single or array) for each group
            var extractedValue;
            if (_.size(values) == 1) {
                extractedValue = values[0].value;
            } else {
                extractedValue = _.map(values, function (e) {
                    return e.value
                })
            }
            return [key, extractedValue]
        }).object().value();
    _.extend(map, mapped);
    return mapped
}
function serializeQuestionnaire() {
    var map = serializeQuestionnaireForExport();
    map['questionnaireHeader.id'] = $('input[name="questionnaireHeader.id"]').val();
    return map

}
function serializeQuestionnaireForExport() {
    var map = {
        title: $('#title').val(),
        nodes: nodes,
        connections: serializeConnections()
    };
    var serializedSchedule = serializeSchedule();

    var standardSchedule = _.chain(serializedSchedule).pairs().reject(function (e) {
        return e[0].match(/Date/) ||
            e[0].match(/\.hour/) ||
            e[0].match(/\.minute/) ||
            e[0].match(/_weekdays.*/) ||
            e[0] === 'questionnaireHeader.id';
    }).object().value();

    standardSchedule['startingDate'] = serializedSchedule['startingDate_widget'];
    standardSchedule['specificDate'] = serializedSchedule['specificDate_widget'];
    standardSchedule['timesOfDay'] = _.chain(_.range(30))
        .reject(function (i) {
            return !serializedSchedule['timesOfDay[' + i + '].hour'];
        })
        .map(function (i) {
            return convertTimeOfDay(serializedSchedule, 'timesOfDay[' + i + ']')
        }).value();

    standardSchedule.daysInMonth = convertToList(standardSchedule.daysInMonth);
    standardSchedule.weekdays = convertToList(standardSchedule.weekdays);
    standardSchedule.weekdaysIntroPeriod = convertToList(standardSchedule.weekdaysIntroPeriod);
    standardSchedule.weekdaysSecondPeriod = convertToList(standardSchedule.weekdaysSecondPeriod);
    standardSchedule.reminderTime = convertTimeOfDay(serializedSchedule, 'reminderTime');
    standardSchedule.blueAlarmTime = convertTimeOfDay(serializedSchedule, 'blueAlarmTime');
    map['standardSchedule'] = standardSchedule;
    return map

}

function convertTimeOfDay(serializedSchedule, field) {
    var hour = serializedSchedule[field + '.hour'];
    var minute = serializedSchedule[field + '.minute'];
    return {hour: hour, minute: minute};
}

function convertToList(object) {
    if (typeof object === 'string') {
        return [object]
    } else {
        return object
    }
}

function getQuestionnaireHeaderId() {
    return parseInt($('input[name="questionnaireHeaderId"]').val())
}

function getQuestionnaireId() {
    var questionnaireId = $('input[name="questionnaireId"]').val();
    return questionnaireId ? parseInt(questionnaireId) : undefined
}

var exitOnSave = false;

function saveError(jqXHR) {
    return function () {
        $('body').prepend("<div class='alert alert-error span12' style='position: absolute; z-index:100;'><button type='button' class='close' data-dismiss='alert'>&times;</button> " + i18n.couldNotSave + ": " + jqXHR.responseText + "</div>");
        exitOnSave = false;
    };
}
function saveSucceded(jqXHR) {
    return function () {
        $('body').prepend("<div class='alert alert-info span12' style='position: absolute; z-index:100;'><button type='button' class='close' data-dismiss='alert'>&times;</button> " + i18n.questionnaireSaved + "</div>");
        if (exitOnSave) {
            window.location.href = $('input[name="exitUrl"]').val()
        }
    };
}
function saveQuestionnaire() {
    var spinner = $('<i class="icon-refresh icon-spin"></i>');
    var saveButton = $('#menu_save', '#menu_save_on_exit');
    saveButton.button('loading');
    saveButton.prepend(spinner);

    var serializedQuestionnaire = serializeQuestionnaire();
    var url = $('input[name="saveUrl"]').val();
    var jqXHR = $.ajax({
        type: "POST",
        url: url,
        data: JSON.stringify(serializedQuestionnaire),
        contentType: "application/json; charset=utf-8",
        dataType: 'json'
    });
    jqXHR
        .always(function () {
            $('#menu_save i').remove();
            saveButton.button('reset');
        })
        .done(saveSucceded(jqXHR))
        .fail(saveError(jqXHR));

}
function saveQuestionnaireAndExit() {
    exitOnSave = true;
    saveQuestionnaire()
}
function updateSaveButton() {
    var createButton = $('#menu_save');
    if (($("#top_menu").find("#title").val().length > 0)) {
        createButton.removeClass('disabled');
        createButton.removeAttr('disabled');
    } else {
        createButton.addClass('disabled');
        createButton.attr('disabled', true);
    }
}
function inputValidation() {
    $(new function () {
        updateSaveButton();
        $('#top_menu').find('input').keyup(updateSaveButton);
    });
}
function isShowing() {
    return getQuestionnaireId()
}
function failedToDoAjaxCall(error) {
    $('body').prepend("<div class='alert alert-error span12'><button type='button' class='close' data-dismiss='alert'>&times;</button> " + i18n.couldNotSave + ": " + error + "</div>");
}
function setPosition(element, position) {
    if (position) {
        $(element).css({
            top: position.top,
            left: position.left
        });
    }
}
function drawNodeFromModel(__, nodeModel) {
    switch (nodeModel.type) {
        case "start":
            addStartNode(nodeModel);
            break;
        case "end":
            addEndNode(nodeModel);
            break;
        case "measurement":
            addMeasurementNode(nodeModel);
            break;
        case "text":
            addTextNode(nodeModel);
            break;
        case "input":
            addInputNode(nodeModel);
            break;
        case "delay":
            addDelayNode(nodeModel);
    }
}
function drawNodesFromModel() {
    $.each(nodes, drawNodeFromModel);
}
function createConnection(__, connection) {
    var jsPlumbConnection;
    if (connection.choiceValue === "false") {
        jsPlumbConnection = jsPlumb.connect({
                source: getChoiceValueFalseEndpoint(connection.source),
                target: connection.target
            }
        );
    } else if (connection.choiceValue === "true") {
        jsPlumbConnection = jsPlumb.connect({
                source: getChoiceValueTrueEndpoint(connection.source),
                target: connection.target
            }
        );

    } else if (connection.measurementSkipped === "true") {
        jsPlumbConnection = jsPlumb.connect({
                source: getSkipMeasurementEndpoint(connection.source),
                target: connection.target
            }
        );
    } else {
        jsPlumbConnection = jsPlumb.connect({
                source: getNormalEndpoint(connection.source),
                target: connection.target
            }
        );
    }
    jsPlumbConnection.setParameter("severity", connection.severity);
    setConnectionColor(connection.severity, jsPlumbConnection);
}
function createConnections(connections) {
    $.each(connections, createConnection);
}
function loadModel() {
    $("#loadingModal").modal('show');
    var questionnaireHeaderId = getQuestionnaireHeaderId();
    var questionnaireId = getQuestionnaireId() || -1;
    var url = $('input[name="editorStateUrl"]').val();

    $.ajax({
        url: url,
        data: {id: questionnaireHeaderId, baseId: questionnaireId},
        success: function (data) {
            updateModel(data)
        },
        error: function (response, error) {
            failedToDoAjaxCall(error)
        },
        complete: function () {
            $("#loadingModal").modal('hide')
        }
    })
}
function addTextNode(model) {
    var node;
    if (!model) {
        var headline = $('#createTextNodeModal').find('#headline').val();
        var text = $('#createTextNodeModal').find('#text').val();
        var helpText = $('#createTextNodeModal').find('#helpText').val();
        var helpImage = $('#createTextNodeModal').find('#helpImage').val();
        node = baseNodeBuilder('comment', text, "text", false, null);

        model = {
            type: 'text',
            headline: headline,
            text: text,
            helpText: helpText,
            helpImage: helpImage,
            id: $(node).attr('id')
        };
        addNodeToModel(model);
    } else {
        node = baseNodeBuilder('comment', model.text, "text", false, model);
        $(node).attr('id', model.id);
    }
    setPosition(node, model.position);
    addNode(node, true);
    updatePosition(node).call();
}
function addInputNode(model) {
    var node;
    if (!model) {
        var $createInputNodeModal = $('#createInputNodeModal');
        var question = $createInputNodeModal.find('#question').val();
        var dataType = $createInputNodeModal.find('#dataType').val();
        var shortText = $createInputNodeModal.find('#shortText').val();
        var helpText = $createInputNodeModal.find('#helpText').val();
        var helpImage = $createInputNodeModal.find('#helpImage').val();

        node = baseNodeBuilder('question-sign', shortText, "input", false, null);
        model = {
            type: 'input',
            question: question,
            dataType: dataType,
            shortText: shortText,
            helpText: helpText,
            helpImage: helpImage,
            id: $(node).attr('id')
        };
        addNodeToModel(model);
    } else {
        node = baseNodeBuilder('question-sign', model.shortText, "input", false, model);
        $(node).attr('id', model.id);
    }
    setPosition(node, model.position);
    if (model.dataType === 'BOOLEAN') {
        addNode(node, true, true);
    } else {
        addNode(node, true, false);
    }
    updatePosition(node).call();

}
function addDelayNode(model) {
    var node;
    if (!model) {
        var createDelayNodeModal = $('#createDelayNodeModal');
        var text = createDelayNodeModal.find('#text').val();
        var countType = createDelayNodeModal.find('#countType').val();
        var shortText = createDelayNodeModal.find('#shortText').val();
        var countTime = createDelayNodeModal.find('#countTime').val();

        node = baseNodeBuilder('time', shortText, "delay", false, null);
        model = {
            type: 'delay',
            countType: countType,
            shortText: shortText,
            text: text,
            countTime: countTime,
            id: $(node).attr('id')
        };
        addNodeToModel(model);
    } else {
        node = baseNodeBuilder('time', model.shortText, "delay", false, model);
        $(node).attr('id', model.id);
    }
    setPosition(node, model.position);
    addNode(node, true, false);
    updatePosition(node).call();
}
function addMeasurementNode(model) {
    var node;
    var createMeasurementNodeModal = $('#createMeasurementNodeModal');
    if (!model) {
        var measurementMethod = $('input[name=measurementForm]:checked', createMeasurementNodeModal).val();
        var measurementType = createMeasurementNodeModal.find('#measurementType').val();
        var measurementTypeText = createMeasurementNodeModal.find('#measurementType :selected').text();
        var headline = createMeasurementNodeModal.find('#headline').val();
        var shotText = createMeasurementNodeModal.find('#shortText').val();
        var helpText = createMeasurementNodeModal.find('#helpText').val();
        var helpImage = createMeasurementNodeModal.find('#helpImage').val();

        node = baseNodeBuilder('beaker', measurementTypeText, "measurement", false, null);

        model = {
            type: 'measurement',
            measurementType: measurementType,
            measurementForm: measurementMethod,
            headline: headline,
            shortText: shotText,
            helpText: helpText,
            helpImage: helpImage,
            id: $(node).attr('id')
        };
        addNodeToModel(model);
    } else {
        node = baseNodeBuilder('beaker', model.shortText, "measurement", false, model);
        $(node).attr('id', model.id);
    }
    setPosition(node, model.position);
    addNode(node, true, false, true);
}
function addStartNode(model) {
    var node = baseNodeBuilder('circle-blank', i18n.startNodeName, 'start', true, null);
    if (!model) {
        model = {type: 'start', id: $(node).attr('id')};
        addNodeToModel(model);
    } else {
        $(node).attr('id', model.id);
    }
    setPosition(node, model.position);
    addNode(node, true);
    $('#menu_add_node_start').addClass('disabled-link').unbind('click');
}
function addEndNode(model) {
    var node = baseNodeBuilder('circle', i18n.endNodeName, 'end', true, model);
    if (!model) {
        model = {type: 'end', id: $(node).attr('id')};
        addNodeToModel(model);
    } else {
        $(node).attr('id', model.id);
    }
    setPosition(node, model.position);
    addNode(node, false);
    $('#menu_add_node_end').addClass('disabled-link').unbind('click');

}
function setQuestionnaireExportText() {
    var field = $('#export_questionnaire_modal').find('#questionnaire_json');
    field.val(JSON.stringify(serializeQuestionnaireForExport(), null, '\t'));
    field.click(function () {
        setTimeout(function () {
            field.select();
        }, 100);
    });
}
function removeAllNodes() {
    _.each(nodes, function (node) {
        removeNodeAndConnections($('#' + node.id));
    })
}
function updateStandardSchedule(standardSchedule) {
    // Specific on the view:
    $('input[name="type"]').removeAttr('checked');
    $('input[name="type"][value="' + standardSchedule.type + '"]').click();

    $('.timeOfDay','#timeOfDayContainer').remove();
    _.each(standardSchedule.timesOfDay, function (timeOfDay) {
        $('.addTimeOfDay').click();
        var div = $('.timeOfDay:last');
        $('input:first', div).val(timeOfDay.hour);
        $('input:last', div).val(timeOfDay.minute);
    });
    $('input[name="reminderStartMinutes"]').val(standardSchedule.reminderStartMinutes);
    updateWeekdays(standardSchedule, 'weekdays');
    updateWeekdays(standardSchedule, 'weekdaysIntroPeriod');
    updateWeekdays(standardSchedule, 'weekdaysSecondPeriod');
    updateTimeOfDay(standardSchedule, 'reminderTime');
    updateTimeOfDay(standardSchedule, 'blueAlarmTime');
    var daysInMonth = _.map(standardSchedule.daysInMonth, function (dayInMonth) {
        return dayInMonth.toString();
    });
    $('select[name="daysInMonth"]').val(daysInMonth);
    $('input[name="dayInterval"]').val(standardSchedule.dayInterval || 30);
    $('input[name="startingDate_widget"]').datepicker('setDate', standardSchedule.startingDate).trigger('change');
    $('input[name="specificDate_widget"]').datepicker('setDate', standardSchedule.specificDate).trigger('change');
    $('select[name="introPeriodWeeks"]').val(standardSchedule.introPeriodWeeks);
}
function updateWeekdays(model, field) {
    $('input[name="'+field+'"]').removeAttr('checked');
    _.each(model[field], function (weekday) {
        $('input[name="'+field+'"][value="' + weekday + '"]').click();
    });
}
function updateTimeOfDay(model, field) {
    if (model[field]) {
        $('input[name="'+field+'.hour"]').val(model[field].hour);
        $('input[name="'+field+'.minute"]').val(model[field].minute);
    }
}

function updateModel(model) {
    removeAllNodes();
    nodes = model.nodes;
    drawNodesFromModel();
    createConnections(model.connections);
    schemaVersion = model.version;
    $('#title').val(model.title);
    if (model.standardSchedule) {
        updateStandardSchedule(model.standardSchedule)
    }
    updateSaveButton();
}
function restoreStateFromJsonString(jsonString) {
    try {
        var model = $.parseJSON(jsonString);
        updateModel(model);
    } catch (e) {
        alert(i18n.importFailed);
        console.error(e)
    }
}

function updateConnectionSeverity(connection) {
    return function () {
        var modal = $("#set_connection_severity_modal");
        var severity = modal.find("input[name=severity]:checked").val();

        connection.setParameter("severity", severity);

        setConnectionColor(severity, connection);
    }
}
function setConnectionColor(severity, connection) {
    if (severity === "YELLOW") {
        connection.setPaintStyle({lineWidth: 3, strokeStyle: '#ffd91b'});
    } else if (severity === "RED") {
        connection.setPaintStyle({lineWidth: 3, strokeStyle: '#cc0022'});
    } else {
        connection.setPaintStyle({lineWidth: 3, strokeStyle: '#0088cc'});
    }
}
function enableAddStartNode() {
    $('#menu_add_node_start').removeClass('disabled-link').click(function () {
        addStartNode()
    });
}

function enableAddEndNode() {
    $('#menu_add_node_end').removeClass('disabled-link').click(function () {
        addEndNode()
    });
}

<!-- Entry point -->
jsPlumb.ready(function () {
    setJsPlumbDefaults();
    drawingArea = $('#drawingArea');
    enableAddStartNode();
    $('#menu_add_node_measurement').click(handleModal($('#createMeasurementNodeModal'), addMeasurementNode));
    $('#menu_add_node_text').click(handleModal($('#createTextNodeModal'), addTextNode));
    $('#menu_add_node_input').click(handleModal($('#createInputNodeModal'), addInputNode));
    $('#menu_add_node_delay').click(handleModal($('#createDelayNodeModal'), addDelayNode));
    $('#export_questionnaire').click(handleModal($('#export_questionnaire_modal'), $.noop, setQuestionnaireExportText));
    enableAddEndNode();
    $('#import_questionnaire').click(handleModal($('#import_questionnaire_modal'), function () {
        var field = $('#import_questionnaire_modal').find('#questionnaire_json');
        restoreStateFromJsonString(field.val())
    }));
    $('#menu_save').click(saveQuestionnaire);
    $('#menu_save_and_exit').click(saveQuestionnaireAndExit);
    $('#menu_exit').click(function () {
        window.location.href = $('input[name="exitUrl"]').val();
        return false;
    });
    inputValidation();

    if (isShowing()) {
        loadModel();
    }

    jsPlumb.bind("jsPlumbConnection", function (info) {
        info.connection.bind("click", function (connection) {
            handleModal($("#set_connection_severity_modal"), updateConnectionSeverity(connection))();
        });
    });
    // Do not allow start to be attached to end node...
    jsPlumb.bind("beforeDrop", function (info) {
        if (info.sourceId == info.targetId) return false;
        if (info.targetId.indexOf("start") == 0) return false;
        return !(info.sourceId.indexOf("start") == 0 && info.targetId.indexOf("end") == 0);
    })

});




