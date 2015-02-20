package org.opentele.server.provider.util

import dk.sosi.seal.SOSIFactory
import dk.sosi.seal.model.AuthenticationLevel
import dk.sosi.seal.model.CareProvider
import dk.sosi.seal.model.IDCard
import dk.sosi.seal.model.SecurityTokenRequest
import dk.sosi.seal.model.SecurityTokenResponse
import dk.sosi.seal.model.SignatureUtil
import dk.sosi.seal.model.constants.NameSpaces
import dk.sosi.seal.model.constants.SubjectIdentifierTypeValues
import dk.sosi.seal.pki.Federation
import dk.sosi.seal.pki.SOSIFederation
import dk.sosi.seal.pki.SOSITestFederation
import dk.sosi.seal.vault.ClasspathCredentialVault
import dk.sosi.seal.vault.CredentialVault
import dk.sosi.seal.vault.FileBasedCredentialVault
import dk.sosi.seal.vault.GenericCredentialVault
import dk.sosi.seal.xml.XmlUtil
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.w3c.dom.Document

class SosiUtil {


    private static final Logger log = LoggerFactory.getLogger(SosiUtil.class)

    CareProvider careProvider
    AuthenticationLevel authLevel
    SOSIFactory factory

    private String credentialVaultPassword
    private String credentialVaultPath
    private String credentialVaultType
    private String sealFederationType
    private String cvr
    private String systemName
    private String stsUrl

    private boolean overrideSosi = false  // "true".equals(System.getProperty(Constants.SOSI_OVERRIDE))

    public SosiUtil(ConfigObject config) {
        this.cvr = config.seal.cvr
        this.systemName = config.seal.systemName
        this.credentialVaultType = (config.seal.vault.type? config.seal.vault.type : "classpath")
        this.sealFederationType = (config.seal.federation.type? config.seal.federation.type : "test")
        this.credentialVaultPath = config.seal.vault.path
        this.credentialVaultPassword = config.seal.vault.password
        this.stsUrl = config.seal.sts.url

        if (!sealFederationType) {
            log.info "No seal.federation.type configured - setting to test"
            this.sealFederationType = "test"
        }

        careProvider = new CareProvider(SubjectIdentifierTypeValues.CVR_NUMBER, cvr, systemName)
        authLevel = AuthenticationLevel.VOCES_TRUSTED_SYSTEM
        factory = createSOSIFactory()

        log.debug "Override SOSI checks: " + overrideSosi
    }


    public SOSIFactory getSOSIFactory() {
        if (!factory) {
            factory = createSOSIFactory()
        }

        return factory
    }


    public SosiUtil(String cvr, String systemName, String credentialVaultType, String credentialVaultPath, String credentialVaultPassword, String stsUrl) {

        this.credentialVaultPassword = credentialVaultPassword
        this.credentialVaultPath = credentialVaultPath
        this.credentialVaultType = (credentialVaultType ? credentialVaultType : "classpath")  // Set to classpath if nothing is set.
        this.sealFederationType = (config.seal.federation.type? config.seal.federation.type : "test")

        this.systemName = systemName
        this.cvr = cvr
        this.stsUrl = stsUrl

        careProvider = new CareProvider(SubjectIdentifierTypeValues.CVR_NUMBER, cvr, systemName)
        authLevel = AuthenticationLevel.VOCES_TRUSTED_SYSTEM
        factory = createSOSIFactory()

        log.debug "Override SOSI checks: " + overrideSosi
    }

    public IDCard getSignedIDCard() {
        IDCard card = createNewSystemIDCard()

        def signedIdCard

        if (!overrideSosi) {

            SecurityTokenRequest securityTokenRequest = factory.createNewSecurityTokenRequest();
            securityTokenRequest.setIDCard(card);

            log.debug "Calling IDP service"

            SecurityTokenResponse response = callIdpService(factory, securityTokenRequest.serialize2DOMDocument())

            if (response.fault) {
                log.error("Got error when retrieving ID card from IDP:")
                log.error(" - Error code: ${response.faultCode}")
                log.error(" - Error message: ${response.faultString}")
                log.error(" - Error actor: ${response.faultActor}")
            }

            signedIdCard = response?.IDCard

        } else {
            log.debug "Overriding SOSI"
            signedIdCard = card
        }

        return signedIdCard
    }

    public IDCard createNewSystemIDCard() {

        CredentialVault vault = factory?.getCredentialVault()
        def systemPair = vault?.systemCredentialPair
        def certificate = systemPair?.getCertificate()

        IDCard card = factory.createNewSystemIDCard(
                systemName, careProvider, authLevel,certificate, systemName);

        return card
    }

    public  GenericCredentialVault getCredentialVault(Properties properties) {
        // Init credentialvault with system certificate
        // Use serialized JKS rather than serialized PKCS12, thus not needing
        // bouncycastle


        CredentialVault vault = null
        if ("classpath".equals(credentialVaultType)) {
            log.info "Using classpath based vault based on setting seal.vault.type: " + credentialVaultType
            vault = new ClasspathCredentialVault(properties, credentialVaultPath, credentialVaultPassword)
        }

        if ("file".equals(credentialVaultType)) {
            log.info "Using file based vault based on setting seal.vault.type: " + credentialVaultType
            vault = new FileBasedCredentialVault(properties,new File(credentialVaultPath),credentialVaultPassword)
        }

        return vault
    }

    private SOSIFactory createSOSIFactory() {

        Properties properties = new Properties();
        properties.setProperty("medcom:ITSystemName", systemName)
        properties.setProperty("medcom:CareProviderID", cvr) //(og NameFormat skal være "medcom:cvrnumber")

        Federation federation = getFederation(properties)

        CredentialVault credentialVault = getCredentialVault(properties)
        SOSIFactory factory = new SOSIFactory(federation, credentialVault, properties);

        return factory

    }

    private Federation getFederation(Properties properties) {
        Federation federation

        if ("production".equals(sealFederationType)) {
            log.info "Setting SOSI federation to production"
            federation = new SOSIFederation(properties);

        } else {
            log.info "Setting SOSI federation to test"
            federation = new SOSITestFederation(properties);
        }

        return federation
    }


    public SecurityTokenResponse callIdpService(SOSIFactory factory, Document doc) throws Exception {

        // Setup WebService
        final String SOAP_ACTION = "";

        try {
            URL u = new URL(stsUrl);
            URLConnection uc = u.openConnection();
            HttpURLConnection connection = (HttpURLConnection) uc;
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("SOAPAction", SOAP_ACTION);

            OutputStream out = connection.getOutputStream();
            Writer wout = new OutputStreamWriter(out);

            String xml = XmlUtil.node2String(doc, false, true);
            wout.write(xml);
            wout.flush();
            wout.close();

            InputStream inputStream = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            def total = "";
            while ((line = reader.readLine()) != null) {
                total += line;
            }
            inputStream.close();

            def response = total;

            Document returnDoc = XmlUtil.readXml(factory.getProperties(), response, true);

            boolean isFault = returnDoc.getElementsByTagNameNS(NameSpaces.SOAP_SCHEMA, "Fault").getLength() > 0;
            if (!isFault) {
                int authlvl = -1;
                org.w3c.dom.NodeList attributes=  returnDoc.getElementsByTagNameNS(NameSpaces.SAML2ASSERTION_SCHEMA, "Attribute");
                for (int i = 0; i < attributes.getLength(); i++) {
                    org.w3c.dom.Node elmAttr = attributes.item(i);

                    if ("sosi:AuthenticationLevel".equals(elmAttr.getAttributes().item(0).getNodeValue())) {
                        authlvl = Integer.parseInt(elmAttr.getFirstChild().getFirstChild().getNodeValue());
                        break;
                    }
                }

                if (AuthenticationLevel.MOCES_TRUSTED_USER.getLevel() == authlvl ||
                        AuthenticationLevel.VOCES_TRUSTED_SYSTEM.getLevel() == authlvl) {

                    org.w3c.dom.Node elmSignature = returnDoc.getElementsByTagNameNS(NameSpaces.DSIG_SCHEMA, "Signature").item(0);
                    if (!SignatureUtil.validate(elmSignature, factory.getFederation(), factory.getCredentialVault(),true))  {
                        log.info "Validation failed"
                    }
                } else {
                    log.info "No validation"
                }
            }

            // Build reply
            XmlUtil.readXml(factory.getProperties(),total, true);
            return factory.deserializeSecurityTokenResponse(total);

        } catch (IOException e) {
            log.error "Error : " + e.printStackTrace();
            return null
        }
    }
}
