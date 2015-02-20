package org.opentele.taglib
import org.opentele.server.model.Measurement
import org.opentele.server.core.util.NumberFormatUtil

class ConferenceTagLib {
    static namespace = "conference"

    def measurementsTable = { attrs, body ->
        def conference = attrs.conference

        out << "<div id=\"resultsContainer\">"
        //<!-- Table on left side -->
        out << "<table>"
        out << "<thead>"
        out << "<tr>"
        out << "<th> ${message(code: 'measurement.type')} </th>"
        out << "<th> ${message(code: 'measurement.result')} </th>"
        out << "</tr>"
        out << "</thead>"

        out << "<tbody>"

        conference.measurements.each { Measurement measurement ->
            measurementRow(measurement)
        }

        out << "</tbody>"

        out <<"</table></div>"

    }

    private void measurementRow(Measurement measurement) {
        out << "<tr>"
            out << "<td>"
                out << message(code: "enum.measurementType.${measurement.measurementType.name}")
            out << "</td>"
            out << "<td>"
                out << "${NumberFormatUtil.format(measurement)} "
                out << " <b>"
                    out << message(code: "enum.unit.${measurement.unit}")
                out << "</b>"
            out << "</td>"

        out << "</tr>"
    }
}
