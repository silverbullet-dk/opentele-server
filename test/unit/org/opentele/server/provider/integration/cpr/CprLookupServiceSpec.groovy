package org.opentele.server.provider.integration.cpr

import grails.test.mixin.*
import org.apache.commons.logging.Log
import org.opentele.server.core.model.types.Sex
import spock.lang.Specification

@TestFor(CprLookupService)
class CprLookupServiceSpec extends Specification{
    private static final String TEST_CPR = "2512484916"
    private static final String SOAP_RESPONSE = """
<medcom:PersonLookupResponse xmlns:cprnr="http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/03/18/"
 xmlns:dkcc2005-2="http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/05/13/"
 xmlns:medcom="urn:oio:medcom:cprservice:1.0.2"
 xmlns:cpr="http://rep.oio.dk/cpr.dk/xml/schemas/core/2006/01/17/"
 xmlns:ns0="http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/"
 xmlns:cb-ois="http://rep.oio.dk/ois.dk/xml/schemas/2006/04/25/"
 xmlns:cpr-2="http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/11/24/"
 xmlns:dkcc2005="http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/03/15/"
 xmlns:ns2="http://rep.oio.dk/itst.dk/xml/schemas/2005/02/22/"
 xmlns:itst="http://rep.oio.dk/itst.dk/xml/schemas/2005/06/24/"
 xmlns:ns1="http://rep.oio.dk/itst.dk/xml/schemas/2006/01/17/"
 xmlns:mc100="http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/"
 xmlns:cpr2="http://rep.oio.dk/cpr.dk/xml/schemas/core/2006/09/01/"
 xmlns:xkom="http://rep.oio.dk/xkom.dk/xml/schemas/2006/01/06/"
 xmlns:xkom-2="http://rep.oio.dk/xkom.dk/xml/schemas/2005/03/15/"
 xmlns:dkcc="http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/09/01/">
 <medcom:PersonInformationStructure>

<medcom:CurrentPersonCivilRegistrationIdentifier>2512484916</medcom:CurrentPersonCivilRegistrationIdentifier>
 <cpr:RegularCPRPerson>
 <cpr:SimpleCPRPerson>
 <ns1:PersonNameStructure>
 <ns0:PersonGivenName>Nancy</ns0:PersonGivenName>
 <ns0:PersonMiddleName>Ann</ns0:PersonMiddleName>
 <ns0:PersonSurnameName>Berggren</ns0:PersonSurnameName>
 </ns1:PersonNameStructure>
 <cprnr:PersonCivilRegistrationIdentifier>2512484916</cprnr:PersonCivilRegistrationIdentifier>
 </cpr:SimpleCPRPerson>
 <ns2:PersonNameForAddressingName>Nance Ann Berggren</ns2:PersonNameForAddressingName>
 <ns0:PersonGenderCode>female</ns0:PersonGenderCode>
 <cpr-2:PersonInformationProtectionIndicator>true</cpr-2:PersonInformationProtectionIndicator>
 <cpr-2:PersonBirthDateStructure>
 <dkcc2005:BirthDate>1948-12-25</dkcc2005:BirthDate>
 <cpr-2:BirthDateUncertaintyIndicator>0</cpr-2:BirthDateUncertaintyIndicator>
 </cpr-2:PersonBirthDateStructure>
 <cpr:PersonCivilRegistrationStatusStructure>
 <cpr-2:PersonCivilRegistrationStatusCode>01</cpr-2:PersonCivilRegistrationStatusCode>
 <cpr:PersonCivilRegistrationStatusStartDate>2013-08-12</cpr:PersonCivilRegistrationStatusStartDate>
 </cpr:PersonCivilRegistrationStatusStructure>
 </cpr:RegularCPRPerson>
 <medcom:PersonAddressStructure>
 <itst:CareOfName>c/o Kaj Nielsen</itst:CareOfName>
  <medcom:AddressComplete>
 <medcom:AddressAccess>
 <cprnr:MunicipalityCode>0234</cprnr:MunicipalityCode>
 <cprnr:StreetCode>8697</cprnr:StreetCode>
 <ns0:StreetBuildingIdentifier>3Q</ns0:StreetBuildingIdentifier>
 </medcom:AddressAccess>
 <medcom:AddressPostal>
 <ns0:MailDeliverySublocationIdentifier>Solholm</ns0:MailDeliverySublocationIdentifier>
 <dkcc2005:StreetName>Gammel Kongevej</dkcc2005:StreetName>
 <cprnr:StreetNameForAddressingName>Gl. Kongevej</cprnr:StreetNameForAddressingName>
 <ns0:StreetBuildingIdentifier>6</ns0:StreetBuildingIdentifier>
 <ns0:FloorIdentifier>2</ns0:FloorIdentifier>
 <ns0:SuiteIdentifier>tv.</ns0:SuiteIdentifier>
 <dkcc2005:DistrictSubdivisionIdentifier>Frederikstaden</dkcc2005:DistrictSubdivisionIdentifier>
 <dkcc2005-2:PostOfficeBoxIdentifier>1200</dkcc2005-2:PostOfficeBoxIdentifier>
 <dkcc2005:PostCodeIdentifier>1231</dkcc2005:PostCodeIdentifier>
 <dkcc2005:DistrictName>København</dkcc2005:DistrictName>
 <ns0:CountryIdentificationCode scheme="iso3166-alpha2">DK</ns0:CountryIdentificationCode>
 </medcom:AddressPostal>
 </medcom:AddressComplete>
 <cpr2:PersonInformationProtectionStartDate>2012-07-28</cpr2:PersonInformationProtectionStartDate>
 <medcom:CountyCode>0820</medcom:CountyCode>
 </medcom:PersonAddressStructure>
 </medcom:PersonInformationStructure>
</medcom:PersonLookupResponse>
"""

    def setup() {
        service.log = Mock(Log) // Just to avoid excessive output during test
    }

    def 'creates request body containing the CPR'() {
        when:
        def body = service.generateRequestBody(TEST_CPR)

        then:
        body.firstChild.firstChild.textContent == TEST_CPR
    }

    def 'correctly parses result from CPR'() {
        setup:
        def person = new CPRPerson()
        def soapResponse = new XmlSlurper().parseText("<body>$SOAP_RESPONSE</body>")

        when:
        service.parseResponse(soapResponse, person)

        then:
        person.civilRegistrationNumber == TEST_CPR

        person.firstName == 'Nancy'
        person.middleName == 'Ann'
        person.lastName == 'Berggren'

        person.sex == Sex.FEMALE

        person.address == 'Gammel Kongevej 6'
        person.postalCode == '1231'
        person.city == 'København'

        !person.hasErrors
    }

}
