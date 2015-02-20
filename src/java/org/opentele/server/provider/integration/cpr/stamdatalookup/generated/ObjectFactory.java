
package org.opentele.server.provider.integration.cpr.stamdatalookup.generated;

import java.math.BigInteger;
import java.util.Calendar;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.opentele.server.provider.integration.cpr.stamdatalookup.generated package.
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _PersonPublicHealthInsurance_QNAME = new QName("http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/", "PersonPublicHealthInsurance");
    private final static QName _PersonHealthCareInformationStructure_QNAME = new QName("http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/", "PersonHealthCareInformationStructure");
    private final static QName _MunicipalityCode_QNAME = new QName("http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/03/18/", "MunicipalityCode");
    private final static QName _TimeOut_QNAME = new QName("http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", "TimeOut");
    private final static QName _PersonLookupResponse_QNAME = new QName("http://nsi.dk/2011/09/23/StamdataCpr/", "PersonLookupResponse");
    private final static QName _FlowStatus_QNAME = new QName("http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", "FlowStatus");
    private final static QName _Username_QNAME = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Username");
    private final static QName _PersonGenderCode_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", "PersonGenderCode");
    private final static QName _PersonInformationProtectionStartDate_QNAME = new QName("http://rep.oio.dk/cpr.dk/xml/schemas/core/2006/09/01/", "PersonInformationProtectionStartDate");
    private final static QName _Priority_QNAME = new QName("http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", "Priority");
    private final static QName _SecurityLevel_QNAME = new QName("http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", "SecurityLevel");
    private final static QName _Assertion_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "Assertion");
    private final static QName _FaultCode_QNAME = new QName("http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", "FaultCode");
    private final static QName _PersonCivilRegistrationIdentifier_QNAME = new QName("http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/03/18/", "PersonCivilRegistrationIdentifier");
    private final static QName _InResponseToMessageID_QNAME = new QName("http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", "InResponseToMessageID");
    private final static QName _PersonSurnameName_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", "PersonSurnameName");
    private final static QName _AssociatedGeneralPractitionerStructure_QNAME = new QName("http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/", "AssociatedGeneralPractitionerStructure");
    private final static QName _PersonAddressStructure_QNAME = new QName("http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/", "PersonAddressStructure");
    private final static QName _X509Certificate_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "X509Certificate");
    private final static QName _PersonCivilRegistrationStatusStartDate_QNAME = new QName("http://rep.oio.dk/cpr.dk/xml/schemas/core/2006/01/17/", "PersonCivilRegistrationStatusStartDate");
    private final static QName _CareOfName_QNAME = new QName("http://rep.oio.dk/itst.dk/xml/schemas/2005/06/24/", "CareOfName");
    private final static QName _StreetCode_QNAME = new QName("http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/03/18/", "StreetCode");
    private final static QName _PersonWithHealthCareInformationStructure_QNAME = new QName("http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/", "PersonWithHealthCareInformationStructure");
    private final static QName _StandardAddressIdentifier_QNAME = new QName("http://rep.oio.dk/itst.dk/xml/schemas/2005/06/24/", "StandardAddressIdentifier");
    private final static QName _Issuer_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "Issuer");
    private final static QName _PersonMiddleName_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", "PersonMiddleName");
    private final static QName _NameID_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "NameID");
    private final static QName _Password_QNAME = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Password");
    private final static QName _PersonCivilRegistrationStatusStructure_QNAME = new QName("http://rep.oio.dk/cpr.dk/xml/schemas/core/2006/01/17/", "PersonCivilRegistrationStatusStructure");
    private final static QName _SimpleCPRPerson_QNAME = new QName("http://rep.oio.dk/cpr.dk/xml/schemas/core/2006/01/17/", "SimpleCPRPerson");
    private final static QName _PostOfficeBoxIdentifier_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/05/13/", "PostOfficeBoxIdentifier");
    private final static QName _DistrictSubdivisionIdentifier_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/03/15/", "DistrictSubdivisionIdentifier");
    private final static QName _CountryIdentificationCode_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", "CountryIdentificationCode");
    private final static QName _PublicHealthInsuranceGroupIdentifier_QNAME = new QName("http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/", "PublicHealthInsuranceGroupIdentifier");
    private final static QName _MailDeliverySublocationIdentifier_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", "MailDeliverySublocationIdentifier");
    private final static QName _MessageID_QNAME = new QName("http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", "MessageID");
    private final static QName _PersonNameStructure_QNAME = new QName("http://rep.oio.dk/itst.dk/xml/schemas/2006/01/17/", "PersonNameStructure");
    private final static QName _PersonGivenName_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", "PersonGivenName");
    private final static QName _AssociatedGeneralPractitionerOrganisationName_QNAME = new QName("http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/", "AssociatedGeneralPractitionerOrganisationName");
    private final static QName _AddressComplete_QNAME = new QName("http://rep.oio.dk/xkom.dk/xml/schemas/2006/01/06/", "AddressComplete");
    private final static QName _PersonCivilRegistrationStatusCode_QNAME = new QName("http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/11/24/", "PersonCivilRegistrationStatusCode");
    private final static QName _PersonInformationProtectionIndicator_QNAME = new QName("http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/11/24/", "PersonInformationProtectionIndicator");
    private final static QName _StreetNameForAddressingName_QNAME = new QName("http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/03/18/", "StreetNameForAddressingName");
    private final static QName _SignatureValue_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "SignatureValue");
    private final static QName _RequireNonRepudiationReceipt_QNAME = new QName("http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", "RequireNonRepudiationReceipt");
    private final static QName _FloorIdentifier_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", "FloorIdentifier");
    private final static QName _AssociatedGeneralPractitionerIdentifier_QNAME = new QName("http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/", "AssociatedGeneralPractitionerIdentifier");
    private final static QName _PersonLookupRequest_QNAME = new QName("http://nsi.dk/2011/09/23/StamdataCpr/", "PersonLookupRequest");
    private final static QName _EmailAddressIdentifier_QNAME = new QName("http://rep.oio.dk/xkom.dk/xml/schemas/2005/03/15/", "EmailAddressIdentifier");
    private final static QName _StreetName_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/03/15/", "StreetName");
    private final static QName _Created_QNAME = new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "Created");
    private final static QName _BirthDateUncertaintyIndicator_QNAME = new QName("http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/11/24/", "BirthDateUncertaintyIndicator");
    private final static QName _PostCodeIdentifier_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/03/15/", "PostCodeIdentifier");
    private final static QName _AttributeValue_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "AttributeValue");
    private final static QName _StreetBuildingIdentifier_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", "StreetBuildingIdentifier");
    private final static QName _DistrictName_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/03/15/", "DistrictName");
    private final static QName _SuiteIdentifier_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", "SuiteIdentifier");
    private final static QName _AddressAccess_QNAME = new QName("http://rep.oio.dk/xkom.dk/xml/schemas/2005/03/15/", "AddressAccess");
    private final static QName _DigestValue_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "DigestValue");
    private final static QName _FlowID_QNAME = new QName("http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", "FlowID");
    private final static QName _PublicHealthInsuranceGroupStartDate_QNAME = new QName("http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/", "PublicHealthInsuranceGroupStartDate");
    private final static QName _CountyCode_QNAME = new QName("http://rep.oio.dk/ois.dk/xml/schemas/2006/04/25/", "CountyCode");
    private final static QName _KeyName_QNAME = new QName("http://www.w3.org/2000/09/xmldsig#", "KeyName");
    private final static QName _AuthorityCode_QNAME = new QName("http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/03/18/", "AuthorityCode");
    private final static QName _TelephoneSubscriberIdentifier_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/09/01/", "TelephoneSubscriberIdentifier");
    private final static QName _PersonNameForAddressingName_QNAME = new QName("http://rep.oio.dk/itst.dk/xml/schemas/2005/02/22/", "PersonNameForAddressingName");
    private final static QName _ConfirmationMethod_QNAME = new QName("urn:oasis:names:tc:SAML:2.0:assertion", "ConfirmationMethod");
    private final static QName _RegularCPRPerson_QNAME = new QName("http://rep.oio.dk/cpr.dk/xml/schemas/core/2006/01/17/", "RegularCPRPerson");
    private final static QName _AddressPostal_QNAME = new QName("http://rep.oio.dk/xkom.dk/xml/schemas/2006/01/06/", "AddressPostal");
    private final static QName _PersonInformationStructure_QNAME = new QName("http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/", "PersonInformationStructure");
    private final static QName _PersonBirthDateStructure_QNAME = new QName("http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/11/24/", "PersonBirthDateStructure");
    private final static QName _BirthDate_QNAME = new QName("http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/03/15/", "BirthDate");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.opentele.server.provider.integration.cpr.stamdatalookup.generated
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link Transforms }
     * 
     */
    public Transforms createTransforms() {
        return new Transforms();
    }

    /**
     * Create an instance of {@link PersonAddressStructureType }
     * 
     */
    public PersonAddressStructureType createPersonAddressStructureType() {
        return new PersonAddressStructureType();
    }

    /**
     * Create an instance of {@link PersonHealthCareInformationStructureType }
     * 
     */
    public PersonHealthCareInformationStructureType createPersonHealthCareInformationStructureType() {
        return new PersonHealthCareInformationStructureType();
    }

    /**
     * Create an instance of {@link PersonWithHealthCareInformationStructureType }
     * 
     */
    public PersonWithHealthCareInformationStructureType createPersonWithHealthCareInformationStructureType() {
        return new PersonWithHealthCareInformationStructureType();
    }

    /**
     * Create an instance of {@link PersonPublicHealthInsuranceType }
     * 
     */
    public PersonPublicHealthInsuranceType createPersonPublicHealthInsuranceType() {
        return new PersonPublicHealthInsuranceType();
    }

    /**
     * Create an instance of {@link AssociatedGeneralPractitionerStructureType }
     * 
     */
    public AssociatedGeneralPractitionerStructureType createAssociatedGeneralPractitionerStructureType() {
        return new AssociatedGeneralPractitionerStructureType();
    }

    /**
     * Create an instance of {@link PersonInformationStructureType }
     * 
     */
    public PersonInformationStructureType createPersonInformationStructureType() {
        return new PersonInformationStructureType();
    }

    /**
     * Create an instance of {@link PersonCivilRegistrationStatusStructureType }
     * 
     */
    public PersonCivilRegistrationStatusStructureType createPersonCivilRegistrationStatusStructureType() {
        return new PersonCivilRegistrationStatusStructureType();
    }

    /**
     * Create an instance of {@link SimpleCPRPersonType }
     * 
     */
    public SimpleCPRPersonType createSimpleCPRPersonType() {
        return new SimpleCPRPersonType();
    }

    /**
     * Create an instance of {@link RegularCPRPersonType }
     * 
     */
    public RegularCPRPersonType createRegularCPRPersonType() {
        return new RegularCPRPersonType();
    }

    /**
     * Create an instance of {@link PersonLookupRequestType }
     * 
     */
    public PersonLookupRequestType createPersonLookupRequestType() {
        return new PersonLookupRequestType();
    }

    /**
     * Create an instance of {@link PersonLookupResponseType }
     * 
     */
    public PersonLookupResponseType createPersonLookupResponseType() {
        return new PersonLookupResponseType();
    }

    /**
     * Create an instance of {@link CivilRegistrationNumberListPersonQueryType }
     * 
     */
    public CivilRegistrationNumberListPersonQueryType createCivilRegistrationNumberListPersonQueryType() {
        return new CivilRegistrationNumberListPersonQueryType();
    }

    /**
     * Create an instance of {@link NamePersonQueryType }
     * 
     */
    public NamePersonQueryType createNamePersonQueryType() {
        return new NamePersonQueryType();
    }

    /**
     * Create an instance of {@link AddressAccessType }
     * 
     */
    public AddressAccessType createAddressAccessType() {
        return new AddressAccessType();
    }

    /**
     * Create an instance of {@link Security }
     * 
     */
    public Security createSecurity() {
        return new Security();
    }

    /**
     * Create an instance of {@link Timestamp }
     * 
     */
    public Timestamp createTimestamp() {
        return new Timestamp();
    }

    /**
     * Create an instance of {@link AssertionType }
     * 
     */
    public AssertionType createAssertionType() {
        return new AssertionType();
    }

    /**
     * Create an instance of {@link Signature }
     * 
     */
    public Signature createSignature() {
        return new Signature();
    }

    /**
     * Create an instance of {@link SignedInfo }
     * 
     */
    public SignedInfo createSignedInfo() {
        return new SignedInfo();
    }

    /**
     * Create an instance of {@link CanonicalizationMethod }
     * 
     */
    public CanonicalizationMethod createCanonicalizationMethod() {
        return new CanonicalizationMethod();
    }

    /**
     * Create an instance of {@link SignatureMethod }
     * 
     */
    public SignatureMethod createSignatureMethod() {
        return new SignatureMethod();
    }

    /**
     * Create an instance of {@link Reference }
     * 
     */
    public Reference createReference() {
        return new Reference();
    }

    /**
     * Create an instance of {@link Transforms.Transform }
     * 
     */
    public Transforms.Transform createTransformsTransform() {
        return new Transforms.Transform();
    }

    /**
     * Create an instance of {@link DigestMethod }
     * 
     */
    public DigestMethod createDigestMethod() {
        return new DigestMethod();
    }

    /**
     * Create an instance of {@link KeyInfo }
     * 
     */
    public KeyInfo createKeyInfo() {
        return new KeyInfo();
    }

    /**
     * Create an instance of {@link X509Data }
     * 
     */
    public X509Data createX509Data() {
        return new X509Data();
    }

    /**
     * Create an instance of {@link UsernameToken }
     * 
     */
    public UsernameToken createUsernameToken() {
        return new UsernameToken();
    }

    /**
     * Create an instance of {@link Header }
     * 
     */
    public Header createHeader() {
        return new Header();
    }

    /**
     * Create an instance of {@link Linking }
     * 
     */
    public Linking createLinking() {
        return new Linking();
    }

    /**
     * Create an instance of {@link Conditions }
     * 
     */
    public Conditions createConditions() {
        return new Conditions();
    }

    /**
     * Create an instance of {@link SubjectConfirmationData }
     * 
     */
    public SubjectConfirmationData createSubjectConfirmationData() {
        return new SubjectConfirmationData();
    }

    /**
     * Create an instance of {@link Subject }
     * 
     */
    public Subject createSubject() {
        return new Subject();
    }

    /**
     * Create an instance of {@link NameIDType }
     * 
     */
    public NameIDType createNameIDType() {
        return new NameIDType();
    }

    /**
     * Create an instance of {@link SubjectConfirmation }
     * 
     */
    public SubjectConfirmation createSubjectConfirmation() {
        return new SubjectConfirmation();
    }

    /**
     * Create an instance of {@link Attribute }
     * 
     */
    public Attribute createAttribute() {
        return new Attribute();
    }

    /**
     * Create an instance of {@link AttributeStatement }
     * 
     */
    public AttributeStatement createAttributeStatement() {
        return new AttributeStatement();
    }

    /**
     * Create an instance of {@link PersonBirthDateStructureType }
     * 
     */
    public PersonBirthDateStructureType createPersonBirthDateStructureType() {
        return new PersonBirthDateStructureType();
    }

    /**
     * Create an instance of {@link AddressCompleteType }
     * 
     */
    public AddressCompleteType createAddressCompleteType() {
        return new AddressCompleteType();
    }

    /**
     * Create an instance of {@link AddressPostalType }
     * 
     */
    public AddressPostalType createAddressPostalType() {
        return new AddressPostalType();
    }

    /**
     * Create an instance of {@link PersonNameStructureType }
     * 
     */
    public PersonNameStructureType createPersonNameStructureType() {
        return new PersonNameStructureType();
    }

    /**
     * Create an instance of {@link CountryIdentificationCodeType }
     * 
     */
    public CountryIdentificationCodeType createCountryIdentificationCodeType() {
        return new CountryIdentificationCodeType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersonPublicHealthInsuranceType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/", name = "PersonPublicHealthInsurance")
    public JAXBElement<PersonPublicHealthInsuranceType> createPersonPublicHealthInsurance(PersonPublicHealthInsuranceType value) {
        return new JAXBElement<PersonPublicHealthInsuranceType>(_PersonPublicHealthInsurance_QNAME, PersonPublicHealthInsuranceType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersonHealthCareInformationStructureType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/", name = "PersonHealthCareInformationStructure")
    public JAXBElement<PersonHealthCareInformationStructureType> createPersonHealthCareInformationStructure(PersonHealthCareInformationStructureType value) {
        return new JAXBElement<PersonHealthCareInformationStructureType>(_PersonHealthCareInformationStructure_QNAME, PersonHealthCareInformationStructureType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/03/18/", name = "MunicipalityCode")
    public JAXBElement<String> createMunicipalityCode(String value) {
        return new JAXBElement<String>(_MunicipalityCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", name = "TimeOut")
    public JAXBElement<String> createTimeOut(String value) {
        return new JAXBElement<String>(_TimeOut_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersonLookupResponseType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://nsi.dk/2011/09/23/StamdataCpr/", name = "PersonLookupResponse")
    public JAXBElement<PersonLookupResponseType> createPersonLookupResponse(PersonLookupResponseType value) {
        return new JAXBElement<PersonLookupResponseType>(_PersonLookupResponse_QNAME, PersonLookupResponseType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", name = "FlowStatus")
    public JAXBElement<String> createFlowStatus(String value) {
        return new JAXBElement<String>(_FlowStatus_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", name = "Username")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createUsername(String value) {
        return new JAXBElement<String>(_Username_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersonGenderCodeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", name = "PersonGenderCode")
    public JAXBElement<PersonGenderCodeType> createPersonGenderCode(PersonGenderCodeType value) {
        return new JAXBElement<PersonGenderCodeType>(_PersonGenderCode_QNAME, PersonGenderCodeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/core/2006/09/01/", name = "PersonInformationProtectionStartDate")
    public JAXBElement<XMLGregorianCalendar> createPersonInformationProtectionStartDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_PersonInformationProtectionStartDate_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", name = "Priority")
    public JAXBElement<String> createPriority(String value) {
        return new JAXBElement<String>(_Priority_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", name = "SecurityLevel")
    public JAXBElement<Integer> createSecurityLevel(Integer value) {
        return new JAXBElement<Integer>(_SecurityLevel_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AssertionType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "Assertion")
    public JAXBElement<AssertionType> createAssertion(AssertionType value) {
        return new JAXBElement<AssertionType>(_Assertion_QNAME, AssertionType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", name = "FaultCode")
    public JAXBElement<String> createFaultCode(String value) {
        return new JAXBElement<String>(_FaultCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/03/18/", name = "PersonCivilRegistrationIdentifier")
    public JAXBElement<String> createPersonCivilRegistrationIdentifier(String value) {
        return new JAXBElement<String>(_PersonCivilRegistrationIdentifier_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", name = "InResponseToMessageID")
    public JAXBElement<String> createInResponseToMessageID(String value) {
        return new JAXBElement<String>(_InResponseToMessageID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", name = "PersonSurnameName")
    public JAXBElement<String> createPersonSurnameName(String value) {
        return new JAXBElement<String>(_PersonSurnameName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AssociatedGeneralPractitionerStructureType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/", name = "AssociatedGeneralPractitionerStructure")
    public JAXBElement<AssociatedGeneralPractitionerStructureType> createAssociatedGeneralPractitionerStructure(AssociatedGeneralPractitionerStructureType value) {
        return new JAXBElement<AssociatedGeneralPractitionerStructureType>(_AssociatedGeneralPractitionerStructure_QNAME, AssociatedGeneralPractitionerStructureType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersonAddressStructureType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/", name = "PersonAddressStructure")
    public JAXBElement<PersonAddressStructureType> createPersonAddressStructure(PersonAddressStructureType value) {
        return new JAXBElement<PersonAddressStructureType>(_PersonAddressStructure_QNAME, PersonAddressStructureType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "X509Certificate")
    public JAXBElement<byte[]> createX509Certificate(byte[] value) {
        return new JAXBElement<byte[]>(_X509Certificate_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/core/2006/01/17/", name = "PersonCivilRegistrationStatusStartDate")
    public JAXBElement<XMLGregorianCalendar> createPersonCivilRegistrationStatusStartDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_PersonCivilRegistrationStatusStartDate_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/itst.dk/xml/schemas/2005/06/24/", name = "CareOfName")
    public JAXBElement<String> createCareOfName(String value) {
        return new JAXBElement<String>(_CareOfName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/03/18/", name = "StreetCode")
    public JAXBElement<String> createStreetCode(String value) {
        return new JAXBElement<String>(_StreetCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersonWithHealthCareInformationStructureType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/", name = "PersonWithHealthCareInformationStructure")
    public JAXBElement<PersonWithHealthCareInformationStructureType> createPersonWithHealthCareInformationStructure(PersonWithHealthCareInformationStructureType value) {
        return new JAXBElement<PersonWithHealthCareInformationStructureType>(_PersonWithHealthCareInformationStructure_QNAME, PersonWithHealthCareInformationStructureType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/itst.dk/xml/schemas/2005/06/24/", name = "StandardAddressIdentifier")
    public JAXBElement<String> createStandardAddressIdentifier(String value) {
        return new JAXBElement<String>(_StandardAddressIdentifier_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "Issuer")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createIssuer(String value) {
        return new JAXBElement<String>(_Issuer_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", name = "PersonMiddleName")
    public JAXBElement<String> createPersonMiddleName(String value) {
        return new JAXBElement<String>(_PersonMiddleName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link NameIDType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "NameID")
    public JAXBElement<NameIDType> createNameID(NameIDType value) {
        return new JAXBElement<NameIDType>(_NameID_QNAME, NameIDType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", name = "Password")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createPassword(String value) {
        return new JAXBElement<String>(_Password_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersonCivilRegistrationStatusStructureType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/core/2006/01/17/", name = "PersonCivilRegistrationStatusStructure")
    public JAXBElement<PersonCivilRegistrationStatusStructureType> createPersonCivilRegistrationStatusStructure(PersonCivilRegistrationStatusStructureType value) {
        return new JAXBElement<PersonCivilRegistrationStatusStructureType>(_PersonCivilRegistrationStatusStructure_QNAME, PersonCivilRegistrationStatusStructureType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SimpleCPRPersonType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/core/2006/01/17/", name = "SimpleCPRPerson")
    public JAXBElement<SimpleCPRPersonType> createSimpleCPRPerson(SimpleCPRPersonType value) {
        return new JAXBElement<SimpleCPRPersonType>(_SimpleCPRPerson_QNAME, SimpleCPRPersonType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Integer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/05/13/", name = "PostOfficeBoxIdentifier")
    public JAXBElement<Integer> createPostOfficeBoxIdentifier(Integer value) {
        return new JAXBElement<Integer>(_PostOfficeBoxIdentifier_QNAME, Integer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/03/15/", name = "DistrictSubdivisionIdentifier")
    public JAXBElement<String> createDistrictSubdivisionIdentifier(String value) {
        return new JAXBElement<String>(_DistrictSubdivisionIdentifier_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link CountryIdentificationCodeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", name = "CountryIdentificationCode")
    public JAXBElement<CountryIdentificationCodeType> createCountryIdentificationCode(CountryIdentificationCodeType value) {
        return new JAXBElement<CountryIdentificationCodeType>(_CountryIdentificationCode_QNAME, CountryIdentificationCodeType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PublicHealthInsuranceGroupIdentifierType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/", name = "PublicHealthInsuranceGroupIdentifier")
    public JAXBElement<PublicHealthInsuranceGroupIdentifierType> createPublicHealthInsuranceGroupIdentifier(PublicHealthInsuranceGroupIdentifierType value) {
        return new JAXBElement<PublicHealthInsuranceGroupIdentifierType>(_PublicHealthInsuranceGroupIdentifier_QNAME, PublicHealthInsuranceGroupIdentifierType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", name = "MailDeliverySublocationIdentifier")
    public JAXBElement<String> createMailDeliverySublocationIdentifier(String value) {
        return new JAXBElement<String>(_MailDeliverySublocationIdentifier_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", name = "MessageID")
    public JAXBElement<String> createMessageID(String value) {
        return new JAXBElement<String>(_MessageID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersonNameStructureType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/itst.dk/xml/schemas/2006/01/17/", name = "PersonNameStructure")
    public JAXBElement<PersonNameStructureType> createPersonNameStructure(PersonNameStructureType value) {
        return new JAXBElement<PersonNameStructureType>(_PersonNameStructure_QNAME, PersonNameStructureType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", name = "PersonGivenName")
    public JAXBElement<String> createPersonGivenName(String value) {
        return new JAXBElement<String>(_PersonGivenName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/", name = "AssociatedGeneralPractitionerOrganisationName")
    public JAXBElement<String> createAssociatedGeneralPractitionerOrganisationName(String value) {
        return new JAXBElement<String>(_AssociatedGeneralPractitionerOrganisationName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddressCompleteType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/xkom.dk/xml/schemas/2006/01/06/", name = "AddressComplete")
    public JAXBElement<AddressCompleteType> createAddressComplete(AddressCompleteType value) {
        return new JAXBElement<AddressCompleteType>(_AddressComplete_QNAME, AddressCompleteType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/11/24/", name = "PersonCivilRegistrationStatusCode")
    public JAXBElement<BigInteger> createPersonCivilRegistrationStatusCode(BigInteger value) {
        return new JAXBElement<BigInteger>(_PersonCivilRegistrationStatusCode_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/11/24/", name = "PersonInformationProtectionIndicator")
    public JAXBElement<Boolean> createPersonInformationProtectionIndicator(Boolean value) {
        return new JAXBElement<Boolean>(_PersonInformationProtectionIndicator_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/03/18/", name = "StreetNameForAddressingName")
    public JAXBElement<String> createStreetNameForAddressingName(String value) {
        return new JAXBElement<String>(_StreetNameForAddressingName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "SignatureValue")
    public JAXBElement<byte[]> createSignatureValue(byte[] value) {
        return new JAXBElement<byte[]>(_SignatureValue_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", name = "RequireNonRepudiationReceipt")
    public JAXBElement<String> createRequireNonRepudiationReceipt(String value) {
        return new JAXBElement<String>(_RequireNonRepudiationReceipt_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", name = "FloorIdentifier")
    public JAXBElement<String> createFloorIdentifier(String value) {
        return new JAXBElement<String>(_FloorIdentifier_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link BigInteger }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/", name = "AssociatedGeneralPractitionerIdentifier")
    public JAXBElement<BigInteger> createAssociatedGeneralPractitionerIdentifier(BigInteger value) {
        return new JAXBElement<BigInteger>(_AssociatedGeneralPractitionerIdentifier_QNAME, BigInteger.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersonLookupRequestType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://nsi.dk/2011/09/23/StamdataCpr/", name = "PersonLookupRequest")
    public JAXBElement<PersonLookupRequestType> createPersonLookupRequest(PersonLookupRequestType value) {
        return new JAXBElement<PersonLookupRequestType>(_PersonLookupRequest_QNAME, PersonLookupRequestType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/xkom.dk/xml/schemas/2005/03/15/", name = "EmailAddressIdentifier")
    public JAXBElement<String> createEmailAddressIdentifier(String value) {
        return new JAXBElement<String>(_EmailAddressIdentifier_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/03/15/", name = "StreetName")
    public JAXBElement<String> createStreetName(String value) {
        return new JAXBElement<String>(_StreetName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Calendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", name = "Created")
    @XmlJavaTypeAdapter(Adapter1 .class)
    public JAXBElement<Calendar> createCreated(Calendar value) {
        return new JAXBElement<Calendar>(_Created_QNAME, Calendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Boolean }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/11/24/", name = "BirthDateUncertaintyIndicator")
    public JAXBElement<Boolean> createBirthDateUncertaintyIndicator(Boolean value) {
        return new JAXBElement<Boolean>(_BirthDateUncertaintyIndicator_QNAME, Boolean.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/03/15/", name = "PostCodeIdentifier")
    public JAXBElement<String> createPostCodeIdentifier(String value) {
        return new JAXBElement<String>(_PostCodeIdentifier_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "AttributeValue")
    public JAXBElement<String> createAttributeValue(String value) {
        return new JAXBElement<String>(_AttributeValue_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", name = "StreetBuildingIdentifier")
    public JAXBElement<String> createStreetBuildingIdentifier(String value) {
        return new JAXBElement<String>(_StreetBuildingIdentifier_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/03/15/", name = "DistrictName")
    public JAXBElement<String> createDistrictName(String value) {
        return new JAXBElement<String>(_DistrictName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2003/02/13/", name = "SuiteIdentifier")
    public JAXBElement<String> createSuiteIdentifier(String value) {
        return new JAXBElement<String>(_SuiteIdentifier_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddressAccessType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/xkom.dk/xml/schemas/2005/03/15/", name = "AddressAccess")
    public JAXBElement<AddressAccessType> createAddressAccess(AddressAccessType value) {
        return new JAXBElement<AddressAccessType>(_AddressAccess_QNAME, AddressAccessType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "DigestValue")
    public JAXBElement<byte[]> createDigestValue(byte[] value) {
        return new JAXBElement<byte[]>(_DigestValue_QNAME, byte[].class, null, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.medcom.dk/dgws/2006/04/dgws-1.0.xsd", name = "FlowID")
    public JAXBElement<String> createFlowID(String value) {
        return new JAXBElement<String>(_FlowID_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/", name = "PublicHealthInsuranceGroupStartDate")
    public JAXBElement<XMLGregorianCalendar> createPublicHealthInsuranceGroupStartDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_PublicHealthInsuranceGroupStartDate_QNAME, XMLGregorianCalendar.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ois.dk/xml/schemas/2006/04/25/", name = "CountyCode")
    public JAXBElement<String> createCountyCode(String value) {
        return new JAXBElement<String>(_CountyCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://www.w3.org/2000/09/xmldsig#", name = "KeyName")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    public JAXBElement<String> createKeyName(String value) {
        return new JAXBElement<String>(_KeyName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/03/18/", name = "AuthorityCode")
    public JAXBElement<String> createAuthorityCode(String value) {
        return new JAXBElement<String>(_AuthorityCode_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/09/01/", name = "TelephoneSubscriberIdentifier")
    public JAXBElement<String> createTelephoneSubscriberIdentifier(String value) {
        return new JAXBElement<String>(_TelephoneSubscriberIdentifier_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/itst.dk/xml/schemas/2005/02/22/", name = "PersonNameForAddressingName")
    public JAXBElement<String> createPersonNameForAddressingName(String value) {
        return new JAXBElement<String>(_PersonNameForAddressingName_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "urn:oasis:names:tc:SAML:2.0:assertion", name = "ConfirmationMethod")
    public JAXBElement<String> createConfirmationMethod(String value) {
        return new JAXBElement<String>(_ConfirmationMethod_QNAME, String.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link RegularCPRPersonType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/core/2006/01/17/", name = "RegularCPRPerson")
    public JAXBElement<RegularCPRPersonType> createRegularCPRPerson(RegularCPRPersonType value) {
        return new JAXBElement<RegularCPRPersonType>(_RegularCPRPerson_QNAME, RegularCPRPersonType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AddressPostalType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/xkom.dk/xml/schemas/2006/01/06/", name = "AddressPostal")
    public JAXBElement<AddressPostalType> createAddressPostal(AddressPostalType value) {
        return new JAXBElement<AddressPostalType>(_AddressPostal_QNAME, AddressPostalType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersonInformationStructureType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/medcom.sundcom.dk/xml/schemas/2007/02/01/", name = "PersonInformationStructure")
    public JAXBElement<PersonInformationStructureType> createPersonInformationStructure(PersonInformationStructureType value) {
        return new JAXBElement<PersonInformationStructureType>(_PersonInformationStructure_QNAME, PersonInformationStructureType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PersonBirthDateStructureType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/cpr.dk/xml/schemas/core/2005/11/24/", name = "PersonBirthDateStructure")
    public JAXBElement<PersonBirthDateStructureType> createPersonBirthDateStructure(PersonBirthDateStructureType value) {
        return new JAXBElement<PersonBirthDateStructureType>(_PersonBirthDateStructure_QNAME, PersonBirthDateStructureType.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://rep.oio.dk/ebxml/xml/schemas/dkcc/2005/03/15/", name = "BirthDate")
    public JAXBElement<XMLGregorianCalendar> createBirthDate(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_BirthDate_QNAME, XMLGregorianCalendar.class, null, value);
    }

}
