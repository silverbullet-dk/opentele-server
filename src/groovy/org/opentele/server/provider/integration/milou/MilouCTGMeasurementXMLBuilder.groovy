package org.opentele.server.provider.integration.milou

import groovy.xml.MarkupBuilder
import org.apache.commons.lang.StringUtils
import org.opentele.server.model.Measurement

import java.util.zip.GZIPOutputStream

class MilouCTGMeasurementXMLBuilder {

    static def build(Measurement measurement) {
        def writer = new StringWriter()
        def builder = new MarkupBuilder(writer)

        def ctgs = [parseList(measurement.fhr), parseList(measurement.mhr), parseList(measurement.toco), parseList(measurement.qfhr)].transpose()

        builder.'temp:reg'(xmlns:'http://schemas.datacontract.org/2004/07/Milou.Server.ImportCtg', 'xmlns:xsi':'http://www.w3.org/2001/XMLSchema-instance', 'xmlns:temp':'http://tempuri.org/', 'xmlns:arr':'http://schemas.microsoft.com/2003/10/Serialization/Arrays') {
            contactField {
                gestationField('xsi:nil':'true')
                parityField('xsi:nil':'true')
                timeField(xmlDate(measurement.startTime))
            }
            ctgField(encodeMeasurements(ctgs))
            deviceIDField(measurement?.meter?.meterId)
            endTimeField(xmlDate(measurement.endTime))
            locationField {
                clinicField('xsi:nil':'true')
                roomField {
                    nameField('Hjemme')
                }
                wardField('xsi:nil':'true')
            }
            markersField {
                parseListAndTrimQuotes(measurement.signals)?.each {
                    'arr:dateTime'(it)
                }
            }
            patientField {
                idField(measurement.patient.cpr)
                nameField {
                    firstField(measurement.patient.firstName)
                    lastField(measurement.patient.lastName)
                    middleField('xsi:nil':true)
                }
            }
            startTimeField(xmlDate(measurement.startTime))
        }
        def xmlString = writer.toString()

        xmlString
    }

    private static String encodeMeasurements(List measurements) {
        def targetStream = new ByteArrayOutputStream()

        def zipStream = new GZIPOutputStream(targetStream)
        measurements.each {
            zipStream.write("${it}\n".getBytes())
        }
        zipStream.close()

        def zipped = targetStream.toByteArray()
        targetStream.close()
        return zipped.encodeBase64()
    }

    private static def parseListAndTrimQuotes(String list) {
        parseList(list).collect { StringUtils.strip(it, "\"") }
    }

    private static def parseList(String list) {
        if (list == null || list.isEmpty() || list.equals('[]')) {
            return []
        }
        list.replace("[", "").replace("]", "").split(",").collect { it.trim() }
    }

    private static def xmlDate(Date date) {
        def calendar = Calendar.getInstance()
        calendar.setTime(date)

        javax.xml.datatype.DatatypeFactory.newInstance().newXMLGregorianCalendar(calendar)
    }
}
