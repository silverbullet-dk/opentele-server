package org.opentele.server

class JSONParamsMapFilters {
    // From: http://stackoverflow.com/questions/10834422/grails-command-object-how-to-load-request-json-into-it
    def filters = {
        all(controller: 'questionnaireEditor', action: 'save') {
            before = {
                if(request.xhr) {
                    def json = request.JSON
                    json.each { key, value ->
                        params[key] = value
                    }
                }
            }

        }
    }
}
