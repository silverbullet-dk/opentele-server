package org.opentele.server.provider.integration.kihdb

import dk.sosi.seal.model.Request
import dk.sosi.seal.xml.XmlUtil
import org.opentele.server.dgks.monitoringdataset.version1_0_1.CreateMonitoringDataset
import org.w3c.dom.Document

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory

class RequestUtil {

    public String generateRequest(CreateMonitoringDataset cxfRequest, Request sosiRequest) {
        //Create Marshaller
        JAXBContext context = JAXBContext.newInstance(CreateMonitoringDataset.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        //Create document
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();

        // Stringify document
        marshaller.marshal(cxfRequest, doc)

        sosiRequest.body = doc.getDocumentElement()
        return XmlUtil.node2String(sosiRequest.serialize2DOMDocument(), false, true)
    }
}
