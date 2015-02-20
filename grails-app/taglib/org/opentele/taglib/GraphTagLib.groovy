package org.opentele.taglib

class GraphTagLib {

	// OpenTele Graph Tag
	static namespace = "tsgt"
		
	def initialize = { attrs, body -> 
		// Initialize it.
		out << "<gvisualization:apiImport />"
	}
	
	
	def drawChart = { attrs, body ->
		log.debug "Attrs: " + attrs
		def meter = attrs["meter"]
		def measurements = attrs["values"]
		log.debug "Meter: " + meter.name + " meter id " + meter.meterId + " type: " + meter.measurementType
		log.debug "Values: " + attrs["values"]
		log.debug "Body: " + body

		// Hard coded .... for now
		if (meter.measurementType.toString() == "Weight") {
			log.debug "Adding weight graph"
			def elementId = "linechart"
			def columns = "[['date','Dato'], ['number','V�gt']]"
			def data = []
			measurements.each { measurement -> 
				data << [""+measurement.time, ""+measurement.value]
			}
			out << "<gvisualization:lineCoreChart elementId=\"" + elementId +"\" "
			out << "title='" + message(code: "graph.weight.titel") + "' "
			out << "columns='" + columns + "' "
			out << "data='" + data + "'/>" 

			log.debug "Added weight graph"
			
		} 
		
				
		/*
		<gvisualization:lineCoreChart elementId="linechart" width="${400}"
			height="${240}" title="V�gt"
			columns="${[['date','Dato'], ['number','V�gt']]}"
			data="${entry.value}" />
	
	 */
	
		
		
	}
	

}
