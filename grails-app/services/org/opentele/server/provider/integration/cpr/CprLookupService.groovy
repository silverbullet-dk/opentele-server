package org.opentele.server.provider.integration.cpr

import dk.sosi.seal.SOSIFactory
import dk.sosi.seal.model.IDCard
import dk.sosi.seal.model.Request
import dk.sosi.seal.xml.XmlUtil
import org.opentele.server.provider.integration.cpr.stamdatalookup.generated.ObjectFactory
import org.opentele.server.provider.integration.cpr.stamdatalookup.generated.PersonLookupRequestType
import org.opentele.server.core.model.types.Sex
import org.opentele.server.provider.util.SosiUtil
import org.w3c.dom.Document
import org.w3c.dom.Element
import wslite.soap.SOAPClient

import javax.xml.bind.JAXBContext
import javax.xml.bind.Marshaller
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import wslite.soap.SOAPFaultException

class CprLookupService {
    def grailsApplication

    /**
     * Returns a CPR Person containing the persons name (first,middle,lastname) and the persons address
     * @param cpr The PersonCivilRegistrationIdentifier of the person to fetch.
     * @return a CPR Person containing the information
     */
    def getPersonDetails(String cpr) {
        String cprUrl = grailsApplication.config.cpr.service.url

        SosiUtil sosiUtil = new SosiUtil(grailsApplication.config)
        SOSIFactory sosiFactory = sosiUtil.getFactory()

        def soapClient = new SOAPClient(cprUrl)
        Request sosiRequest = sosiFactory.createNewRequest(false, "flow")

        String soapRequest = generateRequest(cpr, sosiRequest, sosiUtil)
        CPRPerson person = new CPRPerson()
        if (soapRequest) {
            try {
                log.debug "Calling CPR service"
                def response = soapClient.send(soapRequest)

                parseResponse(response, person)
            } catch (SOAPFaultException e) {
                person.hasErrors = true
                person.errorMessage = e.message
                log.error "Caught SOAP exception calling CPR service", e
            } catch (Exception e) {
                person.hasErrors = true
                person.errorMessage = e.message
                log.error "Caught exception calling CPR service", e
            }
        } else {
            person.hasErrors = true
            person.errorMessage = "Not able to get valid DGWS request"
        }

        return person
    }

    /**
     * Handle the pain of adding the CXF request into the Sosi Request.
     * @param cpr The CPR number.
     * @param sosiRequest The SOSI request
     * @param sosiUtil SosiUtil for handling interaction with SEAL
     * @return The string representing the request
     */
    private String generateRequest(String cpr, Request sosiRequest, SosiUtil sosiUtil) {
        IDCard signedIdCard = sosiUtil.getSignedIDCard()

        if (signedIdCard) {
            sosiRequest.setIDCard(signedIdCard)
            sosiRequest.body = generateRequestBody(cpr)
            return XmlUtil.node2String(sosiRequest.serialize2DOMDocument(), false, true)
        }

        return null
    }

    Element generateRequestBody(String cpr) {
        ObjectFactory objectFactory = new ObjectFactory()
        PersonLookupRequestType msg = objectFactory.createPersonLookupRequestType()
        msg.civilRegistrationNumberPersonQuery = cpr

        //Create Marshaller
        JAXBContext context = JAXBContext.newInstance(PersonLookupRequestType.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        //Create document
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();

        // Stringify document
        marshaller.marshal(msg, doc)

        doc.getDocumentElement()
    }

    void parseResponse(def response, CPRPerson person) {
        handleSimpleCprPerson(response?.PersonLookupResponse?.PersonInformationStructure?.RegularCPRPerson?.SimpleCPRPerson, person)
        handleAddressStructure(response?.PersonLookupResponse?.PersonInformationStructure?.PersonAddressStructure, person)

        String sexCode = response.PersonLookupResponse?.PersonInformationStructure?.RegularCPRPerson?.PersonGenderCode?.text()
        if (sexCode.equalsIgnoreCase("female")) {
            person.sex = Sex.FEMALE
        } else if (sexCode.equalsIgnoreCase("male")) {
            person.sex = Sex.MALE
        }
    }

    private CPRPerson handleAddressStructure(def xml, CPRPerson person) {
        String streetName = xml?.AddressComplete?.AddressPostal?.StreetName?.text()
        String streetBuildingIdentifier = xml?.AddressComplete?.AddressPostal?.StreetBuildingIdentifier?.text()
        String postCode = xml?.AddressComplete?.AddressPostal?.PostCodeIdentifier?.text()
        String districtName = xml?.AddressComplete?.AddressPostal?.DistrictName?.text()

        person.address = (streetName.size() > 0 ? streetName + " " + streetBuildingIdentifier:null)
        person.postalCode = (postCode.size() > 0 ? postCode : null)
        person.city = (districtName.size() >0?districtName:null)

        return person
    }

    private CPRPerson handleSimpleCprPerson(def xml, CPRPerson person) {
        String givenName = xml?.PersonNameStructure?.PersonGivenName?.text()
        String middleName = xml?.PersonNameStructure?.PersonMiddleName?.text()
        String lastName = xml?.PersonNameStructure?.PersonSurnameName?.text()
        String cpr = xml?.PersonCivilRegistrationIdentifier?.text()

        person.firstName = (givenName.size() > 0 ? givenName : null)
        person.middleName = (middleName.size() > 0 ? middleName : null)
        person.lastName = (lastName.size() > 0 ? lastName : null)
        person.civilRegistrationNumber = (cpr.size() > 0? cpr : null)

        return person
    }
}
