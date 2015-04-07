package org.opentele.taglib

class FormattingTagLib {

    def transient clinicianMessageService
    def transient patientIdentificationService

	// OpenTele Graph Tag
	static namespace = "otformat"

    def prettyStringForScheduleType = {attrs, body ->
        if(!attrs.message) {
            throwTagError("Missing [message] for tag [prettyStringForScheduleType")
        }

        out << g.message(code: "schedule.scheduleType.${attrs.message}.label")
    }

    def camelCase(String message) {
        return message?.toLowerCase()?.replaceAll(/_[^_]/) {
            it[1].toUpperCase()
        }
    }

    def shortString = { attrs, body ->
		if (attrs.message.length()>100) {
			out << attrs.message.substring(0, 97)+"...";
		} else {
			out << attrs.message
		}
		
	} 

	def booleanToJaNej = { attrs, body ->
		if (attrs.message) {
			out << "Ja"
		} else {
			out << "Nej"
		} 
	}
	
	def formatPhoneNumber = {attrs, body ->
		def digits = attrs.message
		def hasPlus = false
		if (digits.size() > 0 && digits[0] == "+") {
			digits = attrs.message[1..attrs.message.length()-1]
			hasPlus = true
		}
		
		StringBuilder b = new StringBuilder();
		if (hasPlus) {
			b.append("+")
		}
		for (int i = 1; i < digits.size(); i+=2) {
			b.append(digits[i-1])
			b.append(digits[i])
			b.append(" ")
		}
	
		out << b.toString().encodeAsHTML()
	}
	
	def formatCPR = {attrs, body ->
		if (attrs.cpr) {
            out << patientIdentificationService.formatForDisplay(attrs.cpr).encodeAsHTML()
		}
	}
	
	/**
	 * Give 5 params - first 3 mandatory
	 * controller: The controller to link to
	 * action: The action to link to
	 * code: the i18n code for the button label
	 * (optional) paramId: the id param to set, if any
	 * (optional) messageCount: the number of messages to display after the code
	 */
	
	def menuLink = {attrs, body ->
		//Create the link
        def icon
        def linkAttrs
		if (attrs.mapping) {
            icon = "${attrs.mapping}.png"
            linkAttrs = [mapping: attrs.mapping, params: attrs.params]
		} else {
            icon = "${attrs.controller}${attrs.action}.png"
            linkAttrs = [controller: attrs.controller, action: attrs.action]
        }

        if(attrs.paramId) {
            linkAttrs.id = attrs.paramId
		}
        if (attrs.messageCount == "true" && clinicianMessageService.getUnreadMessageCount() > 0) {
            icon = "patientmessages-unread.png"
        }
        out << link(linkAttrs) {
            out << img(dir: 'images', file: icon, class: 'leftmenu icon')
            out << message(code: attrs.code)
            if(attrs.messageCount == "true") {
                out << message.unreadMessages(class: 'red')
            }
        }
	}
}
