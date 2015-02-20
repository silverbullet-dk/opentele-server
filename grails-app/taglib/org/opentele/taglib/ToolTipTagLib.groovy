package org.opentele.taglib

import org.codehaus.groovy.grails.plugins.web.taglib.FormTagLib

class ToolTipTagLib {

    FormTagLib formTagLib = new FormTagLib()
    static namespace = "tooltip"

    def label = {attrs, body ->
        def tooltip = attrs.remove('tooltip')
        def message = attrs.remove('message')

        if(!tooltip && !message) throwTagError("Missing [tooltip] or [message] attribute on [tt:label]")
        if(tooltip && message) throwTagError("Can only specify [tooltip] or [message] attribute on [tt:label]")
        if(message) {
            message = g.message(code: message, default: message)
        } else {
            message = tooltip
        }
        attrs.'data-tooltip' = message
        out << "<label "
        formTagLib.outputAttributes(attrs, out)
        out << ">"
        if(body) out << body()
        out <<"</label>"
    }
}
