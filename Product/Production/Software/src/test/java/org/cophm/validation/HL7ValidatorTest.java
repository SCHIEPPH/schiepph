package org.cophm.validation;

//import ca.uhn.hl7v2.HL7Exception;
import junit.framework.TestCase;
import org.cophm.util.PropertyAccessException;
import org.cophm.util.XMLDefs;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by
 * User: ralph
 * Date: 4/6/12
 * Time: 12:21 PM
 */
public class HL7ValidatorTest extends TestCase {

    public static String    xmitXmlPrefix = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<ns2:Adapter_ProvideAndRegisterDocumentSetRequest\n" +
            "                                                                                                                     xmlns:ns2=\"urn:gov:hhs:fha:nhinc:common:nhinccommonadapter\"\n" +
            "                                                                                                                     xmlns=\"urn:gov:hhs:fha:nhinc:common:nhinccommon\"\n" +
            "                                                                                                                     xmlns:ns4=\"urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0\"\n" +
            "                                                                                                                     xmlns:ns3=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\"\n" +
            "                                                                                                                     xmlns:ns5=\"urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0\" xmlns:ns6=\"urn:ihe:iti:xds-b:2007\"\n" +
            "                                                                                                                     xmlns:ns7=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0\">\n" +
            "    <ns2:assertion>\n"                                                                                               +
            "        <haveSecondWitnessSignature>false </haveSecondWitnessSignature>\n"                                           +
            "        <haveSignature>false </haveSignature>\n"                                                                     +
            "        <haveWitnessSignature>false </haveWitnessSignature>\n"                                                       +
            "        <homeCommunity>\n"                                                                                           +
            "            <homeCommunityId>1234 </homeCommunityId>\n"                                                              +
            "        </homeCommunity>\n"                                                                                          +
            "        <uniquePatientId>? </uniquePatientId>\n"                                                                     +
            "        <userInfo>\n"                                                                                                +
            "            <personName>\n"                                                                                          +
            "                <familyName>? </familyName>\n"                                                                       +
            "                <givenName>? </givenName>\n"                                                                         +
            "                <secondNameOrInitials>? </secondNameOrInitials>\n"                                                   +
            "                <fullName>? ? ? </fullName>\n"                                                                       +
            "            </personName>\n"                                                                                         +
            "            <userName>? </userName>\n"                                                                               +
            "            <org>\n"                                                                                                 +
            "                <homeCommunityId>? </homeCommunityId>\n"                                                             +
            "                <name>? </name>\n"                                                                                   +
            "            </org>\n"                                                                                                +
            "            <roleCoded>\n"                                                                                           +
            "                <code>? </code>\n"                                                                                   +
            "                <codeSystem>? </codeSystem>\n"                                                                       +
            "                <codeSystemName>? </codeSystemName>\n"                                                               +
            "                <displayName>? </displayName>\n"                                                                     +
            "            </roleCoded>\n"                                                                                          +
            "        </userInfo>\n"                                                                                               +
            "        <authorized>false </authorized>\n"                                                                           +
            "        <purposeOfDisclosureCoded>\n"                                                                                +
            "            <code>? </code>\n"                                                                                       +
            "            <codeSystem>? </codeSystem>\n"                                                                           +
            "            <codeSystemName>? </codeSystemName>\n"                                                                   +
            "            <displayName>? </displayName>\n"                                                                         +
            "        </purposeOfDisclosureCoded>\n"                                                                               +
            "        <samlAuthnStatement>\n"                                                                                      +
            "            <authInstant>2012-04-23T19:03:33.179Z </authInstant>\n"                                                  +
            "            <sessionIndex>? </sessionIndex>\n"                                                                       +
            "            <authContextClassRef>urn:oasis:names:tc:SAML:2.0:ac:classes:unspecified </authContextClassRef>\n"        +
            "            <subjectLocalityAddress>? </subjectLocalityAddress>\n"                                                   +
            "            <subjectLocalityDNSName>? </subjectLocalityDNSName>\n"                                                   +
            "        </samlAuthnStatement>\n"                                                                                     +
            "        <samlAuthzDecisionStatement>\n"                                                                              +
            "            <decision>Permit </decision>\n"                                                                          +
            "            <resource>https://localhost:8181/CONNECTNhinServicesWeb/DocumentRepositoryXDR_Service </resource>\n"     +
            "            <action>Execute </action>\n"                                                                             +
            "            <evidence>\n"                                                                                            +
            "                <assertion>\n"                                                                                       +
            "                    <id>? </id>\n"                                                                                   +
            "                    <issueInstant>2012-04-23T19:03:33.182Z </issueInstant>\n"                                        +
            "                    <version>2.0 </version>\n"                                                                       +
            "                                                                                                                     <issuer>CN=SAML User,OU=SU,O=SAML User,L=Los Angeles,ST=CA,C=US </issuer>\n" +
            "                    <issuerFormat>urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName </issuerFormat>\n"       +
            "                    <conditions>\n"                                                                                  +
            "                        <notBefore>2012-04-23T19:03:33.184Z </notBefore>\n"                                          +
            "                        <notOnOrAfter>2012-04-23T19:03:33.184Z </notOnOrAfter>\n"                                    +
            "                    </conditions>\n"                                                                                 +
            "                    <accessConsentPolicy>? </accessConsentPolicy>\n"                                                 +
            "                    <instanceAccessConsentPolicy>? </instanceAccessConsentPolicy>\n"                                 +
            "                </assertion>\n"                                                                                      +
            "            </evidence>\n"                                                                                           +
            "        </samlAuthzDecisionStatement>\n"                                                                             +
            "        <samlSignature>\n"                                                                                           +
            "            <keyInfo>\n"                                                                                             +
            "                <rsaKeyValueModulus> </rsaKeyValueModulus>\n"                                                        +
            "                <rsaKeyValueExponent> </rsaKeyValueExponent>\n"                                                      +
            "            </keyInfo>\n"                                                                                            +
            "            <signatureValue> </signatureValue>\n"                                                                    +
            "        </samlSignature>\n"                                                                                          +
            "        <messageId>790ab04e-afc4-4982-bc7a-97618e712992 </messageId>\n"                                              +
            "    </ns2:assertion>\n"                                                                                              +
            "    <ns2:ProvideAndRegisterDocumentSetRequest>\n"                                                                    +
            "        <ns5:SubmitObjectsRequest                                                                                    comment=\"comme\" id=\"123\">\n" +
            "            <ns3:RegistryObjectList>\n"                                                                              +
            "                <ns3:ExtrinsicObject mimeType=\"text/plain\"\n" +
            "                                                                                 objectType=\"urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1\" id=\"Document01\">\n" +
            "                    <ns3:Slot                                                    name=\"sourcePatientId\">\n" +
            "                        <ns3:ValueList>\n"                                       +
            "                            <ns3:Value>${patientId} </ns3:Value>\n"              +
            "                        </ns3:ValueList>\n"                                      +
            "                    </ns3:Slot>\n"                                               +
            "                    <ns3:Slot                                                    name=\"intendedRecipient\">\n" +
            "                        <ns3:ValueList>\n"                                       +
            "                            <ns3:Value>${INTENDED_RECIPIENT} </ns3:Value>\n"     +
            "                        </ns3:ValueList>\n"                                      +
            "                    </ns3:Slot>\n"                                               +
            "                    <ns3:ExternalIdentifier value=\"${patientId}\"\n" +
            "                                                     identificationScheme=\"urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427\"\n" +
            "                                                     registryObject=\"Document01\" id=\"ei01\">\n" +
            "                        <ns3:Name>\n"                +
            "                            <ns3:LocalizedString     value=\"XDSDocumentEntry.patientId\"/>\n" +
            "                        </ns3:Name>\n"               +
            "                    </ns3:ExternalIdentifier>\n"     +
            "                    <ns3:ExternalIdentifier value=\"${XDSDocumentEntry_uniqueId}\"\n" +
            "                                                     identificationScheme=\"urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab\"\n" +
            "                                                     registryObject=\"Document01\" id=\"ei02\">\n" +
            "                        <ns3:Name>\n"                +
            "                            <ns3:LocalizedString     value=\"XDSDocumentEntry.uniqueId\"/>\n" +
            "                        </ns3:Name>\n"               +
            "                    </ns3:ExternalIdentifier>\n"     +
            "                </ns3:ExtrinsicObject>\n"            +
            "                <ns3:RegistryPackage                 id=\"SubmissionSet01\">\n" +
            "                    <ns3:ExternalIdentifier value=\"2.16.840.1.113883.3.1239\"\n" +
            "                                                     identificationScheme=\"urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832\"\n" +
            "                                                     registryObject=\"SubmissionSet01\" id=\"urn:oid: 1234\">\n" +
            "                        <ns3:Name>\n"                +
            "                            <ns3:LocalizedString     value=\"XDSSubmissionSet.sourceId\"/>\n" +
            "                        </ns3:Name>\n"               +
            "                    </ns3:ExternalIdentifier>\n"     +
            "                    <ns3:ExternalIdentifier value=\"${patientId}\"\n" +
            "                                                     identificationScheme=\"urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446\"\n" +
            "                                                     registryObject=\"SubmissionSet01\" id=\"ei05\">\n" +
            "                        <ns3:Name>\n"                +
            "                            <ns3:LocalizedString     value=\"XDSSubmissionSet.patientId\"/>\n" +
            "                        </ns3:Name>\n"               +
            "                    </ns3:ExternalIdentifier>\n"     +
            "                </ns3:RegistryPackage>\n"            +
            "                <ns3:Classification\n" +
            "     classificationNode=\"urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd\"\n" +
            "     classifiedObject=\"SubmissionSet01\" id=\"cl10\"/>\n" +
            "                <ns3:Association targetObject=\"Document01\" sourceObject=\"SubmissionSet01\"\n" +
            "                                                                   associationType=\"HasMember\" id=\"as01\">\n" +
            "                    <ns3:Slot                                      name=\"SubmissionSetStatus\">\n" +
            "                        <ns3:ValueList>\n"                         +
            "                            <ns3:Value>Original </ns3:Value>\n"    +
            "                        </ns3:ValueList>\n"                        +
            "                    </ns3:Slot>\n"                                 +
            "                </ns3:Association>\n"                              +
            "            </ns3:RegistryObjectList>\n"                           +
            "        </ns5:SubmitObjectsRequest>\n"                             +
            "        <ns6:Document id=\"Document01\"\n" +
            "     >";

    public static String                                              xmitXmlPostfix = "</ns6:Document>\n" +
            "    </ns2:ProvideAndRegisterDocumentSetRequest>\n"       +
            "</ns2:Adapter_ProvideAndRegisterDocumentSetRequest>";

    public static String                                                                                                                                                                                                                                                                                                                                                                                  hl7TestData_1 =
                    "MSH|^~\\&|||||20111213131225||ADT^A01|MSG00001|P|2.3.1||||||UTF-8\n"                                                                                                                                                                                                                                                                                                                 +
                            "EVN||201205311112\n"                                                                                                                                                                                                                                                                                                                                             +
                            "PID|1||8905671234^^^^SS~5671234098^^^^PI~1234765890^^^^BA~999777555^^^^MR||paitent_name|||M||1002-5|^^Fairfax City^51^22333^USA^^^Fairfax|||||||||||2186-5\n"       +
                            "PV1||E|||||||||||||||||7651234098|||||||||||||||||09||||||||201104011406-0500|20110113164512-0500\n"                                                                                                                                                                                               +
                            "OBX|1|HD|SS001^Treating Facility Identifier^PHINQUESTION||Fairfax Hospital^1098765432^NP||||||C||||||\n"                                                                                                                                                                                                                                               +
                            "OBX|2|XAD|SS002^Treating Facility Location||^123 Gallows Rd.^^^^Fairfax City^51^30341^USA^C^^Fairfax||||||X\n"                                                                                                                                                                                               +
                            "OBX|3|CWE|SS003^Facility/Visit Type^PHINQUESTION||170300000X|||||||X\n"                                                                                                                                                                                                                                                                                     +
                            "OBX|4|NM|21612-7^AGE TIME PAITENT REPORTED^LN||42|a^a^UCUM|||||X\n"                                                                                                                                                                                                                                                                               +
                            "OBX|5|TS|11368-8^ILLNESS OR INJURY ONSET DATE AND TIME:TMSTP:PT:PAITENT:QN^LN||20120401||||||X\n"                                                                                                                                                                                                                                                              +
                            "OBX|6|CWE|8661-1^CHIEF COMPLAINT:FIND:PT:PAITENT:NOM:REPORTED^LN||^^^^^^^^Nausia and Dizzyness||||||X\n"                                                                                                                                                                                                                                                                 +
                            "OBX|7|TX|54094-8^TRIAGE NOTE:FIND:PT:EMERGENCY DEPARTMENT:DOC^LN||The paitent seemed dizzy and sick to his stomach.||||||X\n"                                                                                                                                                                                                                                                                            +
                            "OBX|8|TX|44833-2^DIAGNOSIS:PRELIMINARY:IMP:PT:PAITENT:NOM:^LN||The patient is suffering from an inner ear contdtition.||||||X\n"                                                                                                                                                                                                                                                                        +
                            "OBX|9|NM|11289-6^BODY TEMPERATURE:TEMP:ENCTRFIRST:PAITENT:QN^LN||98.6|^[degF]^UCUM|||||X\n"                                                                                                                                                                                                                                      +
                            "OBX|10|NM|59408-5^OXYGEN SATURATION:MFR:PT:BLDA:QN:PULSE OXIMETRY^LN||95|%^^UCUM|||||X\n"                                                                                                                                                                                                                                        +
                            "DG1|1||78961^|||W\n" +
                            "DG1|2||78960^|||W";

    public static String    xmlData_1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
            "<HL7Message>\n"+
            "    <MSH>\n"+
            "        <MSH.1>| </MSH.1>\n"+
            "        <MSH.2>^~\\\\ </MSH.2>\n"+
            "        <MSH.3/>\n"+
            "        <MSH.4/>\n"+
            "        <MSH.5/>\n"+
            "        <MSH.6/>\n"+
            "        <MSH.7>\n"+
            "            <MSH.7.1>20111213131225 </MSH.7.1>\n"+
            "        </MSH.7>\n"+
            "        <MSH.8/>\n"+
            "        <MSH.9>\n"+
            "            <MSH.9.1>ADT </MSH.9.1>\n"+
            "            <MSH.9.2>A01 </MSH.9.2>\n"+
            "        </MSH.9>\n"+
            "        <MSH.10>\n"+
            "            <MSH.10.1>MSG00001 </MSH.10.1>\n"+
            "        </MSH.10>\n"+
            "        <MSH.11>\n"+
            "            <MSH.11.1>P </MSH.11.1>\n"+
            "        </MSH.11>\n"+
            "        <MSH.12>\n"+
            "            <MSH.12.1>2.3.1 </MSH.12.1>\n"+
            "        </MSH.12>\n"+
            "        <MSH.13/>\n"+
            "        <MSH.14/>\n"+
            "        <MSH.15/>\n"+
            "        <MSH.16/>\n"+
            "        <MSH.17/>\n"+
            "        <MSH.18>\n"+
            "            <MSH.18.1>UTF-8 </MSH.18.1>\n"+
            "        </MSH.18>\n"+
            "    </MSH>\n"+
            "    <EVN>\n"+
            "        <EVN.1/>\n"+
            "        <EVN.2>\n"+
            "            <EVN.2.1>201205311112 </EVN.2.1>\n"+
            "        </EVN.2>\n"+
            "    </EVN>\n"+
            "    <PID>\n"+
            "        <PID.1>\n"+
            "            <PID.1.1>1 </PID.1.1>\n"+
            "        </PID.1>\n"+
            "        <PID.2/>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>8905671234 </PID.3.1>\n"+
            "            <PID.3.2/>\n"+
            "            <PID.3.3/>\n"+
            "            <PID.3.4/>\n"+
            "            <PID.3.5>SS </PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>5671234098 </PID.3.1>\n"+
            "            <PID.3.2/>\n"+
            "            <PID.3.3/>\n"+
            "            <PID.3.4/>\n"+
            "            <PID.3.5>PI </PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>1234765890 </PID.3.1>\n"+
            "            <PID.3.2/>\n"+
            "            <PID.3.3/>\n"+
            "            <PID.3.4/>\n"+
            "            <PID.3.5>BA </PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>999777555 </PID.3.1>\n"+
            "            <PID.3.2></PID.3.2>\n"+
            "            <PID.3.3></PID.3.3>\n"+
            "            <PID.3.4></PID.3.4>\n"+
            "            <PID.3.5>MR</PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.4/>\n"+
            "        <PID.5>\n"+
            "            <PID.5.1>paitent_name </PID.5.1>\n"+
            "        </PID.5>\n"+
            "        <PID.6/>\n"+
            "        <PID.7/>\n"+
            "        <PID.8>\n"+
            "            <PID.8.1>M </PID.8.1>\n"+
            "        </PID.8>\n"+
            "        <PID.9/>\n"+
            "        <PID.10>\n"+
            "            <PID.10.1>1002-5 </PID.10.1>\n"+
            "        </PID.10>\n"+
            "        <PID.11>\n"+
            "            <PID.11.1/>\n"+
            "            <PID.11.2/>\n"+
            "            <PID.11.3>Fairfax City </PID.11.3>\n"+
            "            <PID.11.4>51 </PID.11.4>\n"+
            "            <PID.11.5>22333 </PID.11.5>\n"+
            "            <PID.11.6>USA </PID.11.6>\n"+
            "            <PID.11.7/>\n"+
            "            <PID.11.8/>\n"+
            "            <PID.11.9>Fairfax </PID.11.9>\n"+
            "        </PID.11>\n"+
            "        <PID.12/>\n"+
            "        <PID.13/>\n"+
            "        <PID.14/>\n"+
            "        <PID.15/>\n"+
            "        <PID.16/>\n"+
            "        <PID.17/>\n"+
            "        <PID.18/>\n"+
            "        <PID.19/>\n"+
            "        <PID.20/>\n"+
            "        <PID.21/>\n"+
            "        <PID.22>\n"+
            "            <PID.22.1>2186-5 </PID.22.1>\n"+
            "        </PID.22>\n"+
            "    </PID>\n"+
            "    <PV1>\n"+
            "        <PV1.1/>\n"+
            "        <PV1.2>\n"+
            "            <PV1.2.1>E </PV1.2.1>\n"+
            "        </PV1.2>\n"+
            "        <PV1.3/>\n"+
            "        <PV1.4/>\n"+
            "        <PV1.5/>\n"+
            "        <PV1.6/>\n"+
            "        <PV1.7/>\n"+
            "        <PV1.8/>\n"+
            "        <PV1.9/>\n"+
            "        <PV1.10/>\n"+
            "        <PV1.11/>\n"+
            "        <PV1.12/>\n"+
            "        <PV1.13/>\n"+
            "        <PV1.14/>\n"+
            "        <PV1.15/>\n"+
            "        <PV1.16/>\n"+
            "        <PV1.17/>\n"+
            "        <PV1.18/>\n"+
            "        <PV1.19>\n"+
            "            <PV1.19.1>7651234098 </PV1.19.1>\n"+
            "        </PV1.19>\n"+
            "        <PV1.20/>\n"+
            "        <PV1.21/>\n"+
            "        <PV1.22/>\n"+
            "        <PV1.23/>\n"+
            "        <PV1.24/>\n"+
            "        <PV1.25/>\n"+
            "        <PV1.26/>\n"+
            "        <PV1.27/>\n"+
            "        <PV1.28/>\n"+
            "        <PV1.29/>\n"+
            "        <PV1.30/>\n"+
            "        <PV1.31/>\n"+
            "        <PV1.32/>\n"+
            "        <PV1.33/>\n"+
            "        <PV1.34/>\n"+
            "        <PV1.35/>\n"+
            "        <PV1.36>\n"+
            "            <PV1.36.1>09 </PV1.36.1>\n"+
            "        </PV1.36>\n"+
            "        <PV1.37/>\n"+
            "        <PV1.38/>\n"+
            "        <PV1.39/>\n"+
            "        <PV1.40/>\n"+
            "        <PV1.41/>\n"+
            "        <PV1.42/>\n"+
            "        <PV1.43/>\n"+
            "        <PV1.44>\n"+
            "            <PV1.44.1>201104011406-0500 </PV1.44.1>\n"+
            "        </PV1.44>\n"+
            "        <PV1.45>\n"+
            "            <PV1.45.1>20110113164512-0500 </PV1.45.1>\n"+
            "        </PV1.45>\n"+
            "    </PV1>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>1 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>HD </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>SS001 </OBX.3.1>\n"+
            "            <OBX.3.2>Treating Facility Identifier      </OBX.3.2>\n"+
            "            <OBX.3.3>PHINQUESTION </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>Fairfax Hospital </OBX.5.1>\n"+
            "            <OBX.5.2>1098765432 </OBX.5.2>\n"+
            "            <OBX.5.3>NP </OBX.5.3>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>C </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "        <OBX.12/>\n"+
            "        <OBX.13/>\n"+
            "        <OBX.14/>\n"+
            "        <OBX.15/>\n"+
            "        <OBX.16/>\n"+
            "        <OBX.17/>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>2 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>XAD </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>SS002 </OBX.3.1>\n"+
            "            <OBX.3.2>Treating Facility Location    </OBX.3.2>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1/>\n"+
            "            <OBX.5.2>123 Gallows Rd. </OBX.5.2>\n"+
            "            <OBX.5.3/>\n"+
            "            <OBX.5.4/>\n"+
            "            <OBX.5.5/>\n"+
            "            <OBX.5.6>Fairfax City </OBX.5.6>\n"+
            "            <OBX.5.7>51 </OBX.5.7>\n"+
            "            <OBX.5.8>30341 </OBX.5.8>\n"+
            "            <OBX.5.9>USA </OBX.5.9>\n"+
            "            <OBX.5.10>C </OBX.5.10>\n"+
            "            <OBX.5.11/>\n"+
            "            <OBX.5.12>Fairfax </OBX.5.12>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>3 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>CWE </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>SS003 </OBX.3.1>\n"+
            "            <OBX.3.2>Facility/Visit Type     </OBX.3.2>\n"+
            "            <OBX.3.3>PHINQUESTION </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1></OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9>170300000X </OBX.9>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11/>\n"+
            "        <OBX.12>\n"+
            "            <OBX.12.1>X </OBX.12.1>\n"+
            "        </OBX.12>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>4 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>NM </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>21612-7 </OBX.3.1>\n"+
            "            <OBX.3.2>AGE TIME PAITENT REPORTED       </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>42 </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6>\n"+
            "            <OBX.6.1>a </OBX.6.1>\n"+
            "            <OBX.6.2>a </OBX.6.2>\n"+
            "            <OBX.6.3>UCUM </OBX.6.3>\n"+
            "        </OBX.6>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>5 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>TS </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>11368-8 </OBX.3.1>\n"+
            "            <OBX.3.2>ILLNESS OR INJURY ONSET DATE AND TIME:TMSTP:PT:PAITENT:QN       </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>20120401 </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>6 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>CWE </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>8661-1 </OBX.3.1>\n"+
            "            <OBX.3.2>CHIEF COMPLAINT:FIND:PT:PAITENT:NOM:REPORTED      </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1/>\n"+
            "            <OBX.5.2/>\n"+
            "            <OBX.5.3/>\n"+
            "            <OBX.5.4/>\n"+
            "            <OBX.5.5/>\n"+
            "            <OBX.5.6/>\n"+
            "            <OBX.5.7/>\n"+
            "            <OBX.5.8/>\n"+
            "            <OBX.5.9>Nausia and Dizzyness </OBX.5.9>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>6 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>CWE </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>8661-1 </OBX.3.1>\n"+
            "            <OBX.3.2>CHIEF COMPLAINT:FIND:PT:PAITENT:NOM:REPORTED      </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1/>\n"+
            "            <OBX.5.2/>\n"+
            "            <OBX.5.3/>\n"+
            "            <OBX.5.4/>\n"+
            "            <OBX.5.5/>\n"+
            "            <OBX.5.6/>\n"+
            "            <OBX.5.7/>\n"+
            "            <OBX.5.8/>\n"+
            "            <OBX.5.9>Nausia and Dizzyness_2 </OBX.5.9>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>7 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>TX </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>54094-8 </OBX.3.1>\n"+
            "            <OBX.3.2>TRIAGE NOTE:FIND:PT:EMERGENCY DEPARTMENT:DOC      </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>The paitent seemed dizzy and sick to his stomach. </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>8 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>TX </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>44833-2 </OBX.3.1>\n"+
            "            <OBX.3.2>DIAGNOSIS:PRELIMINARY:IMP:PT:PAITENT:NOM: </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>The patient is suffering from an inner ear contdtition. </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>9 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>NM </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>11289-6 </OBX.3.1>\n"+
            "            <OBX.3.2>BODY TEMPERATURE:TEMP:ENCTRFIRST:PAITENT:QN     </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>98.6 </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6>\n"+
            "            <OBX.6.1/>\n"+
            "            <OBX.6.2>[degF] </OBX.6.2>\n"+
            "            <OBX.6.3>UCUM </OBX.6.3>\n"+
            "        </OBX.6>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>10 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>NM </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>59408-5 </OBX.3.1>\n"+
            "            <OBX.3.2>OXYGEN SATURATION:MFR:PT:BLDA:QN:PULSE OXIMETRY     </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>95 </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6>\n"+
            "            <OBX.6.1>% </OBX.6.1>\n"+
            "            <OBX.6.2> </OBX.6.2>\n"+
            "            <OBX.6.3>UCUM </OBX.6.3>\n"+
            "        </OBX.6>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <DG1>\n"+
            "        <DG1.1>\n"+
            "            <DG1.1.1>1 </DG1.1.1>\n"+
            "        </DG1.1>\n"+
            "        <DG1.2/>\n"+
            "        <DG1.3>\n"+
            "            <DG1.3.1>78961 </DG1.3.1>\n"+
            "        </DG1.3>\n"+
            "        <DG1.4/>\n"+
            "        <DG1.5/>\n"+
            "        <DG1.6>\n"+
            "            <DG1.6.1>W </DG1.6.1>\n"+
            "        </DG1.6>\n"+
            "    </DG1>\n"+
            "    <DG1>\n"+
            "        <DG1.1>\n"+
            "            <DG1.1.1>1 </DG1.1.1>\n"+
            "        </DG1.1>\n"+
            "        <DG1.2/>\n"+
            "        <DG1.3>\n"+
            "            <DG1.3.1>78960 </DG1.3.1>\n"+
            "        </DG1.3>\n"+
            "        <DG1.4/>\n"+
            "        <DG1.5/>\n"+
            "        <DG1.6>\n"+
            "            <DG1.6.1>W </DG1.6.1>\n"+
            "        </DG1.6>\n"+
            "    </DG1>\n"+
            "</HL7Message>";

    public static String    xmlData_Conditional_data_present = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
            "<HL7Message>\n"+
            "    <MSH>\n"+
            "        <MSH.1>| </MSH.1>\n"+
            "        <MSH.2>^~\\\\ </MSH.2>\n"+
            "        <MSH.3/>\n"+
            "        <MSH.4/>\n"+
            "        <MSH.5/>\n"+
            "        <MSH.6/>\n"+
            "        <MSH.7>\n"+
            "            <MSH.7.1>20111213131225 </MSH.7.1>\n"+
            "        </MSH.7>\n"+
            "        <MSH.8/>\n"+
            "        <MSH.9>\n"+
            "            <MSH.9.1>ADT </MSH.9.1>\n"+
            "            <MSH.9.2>A01 </MSH.9.2>\n"+
            "        </MSH.9>\n"+
            "        <MSH.10>\n"+
            "            <MSH.10.1>MSG00001 </MSH.10.1>\n"+
            "        </MSH.10>\n"+
            "        <MSH.11>\n"+
            "            <MSH.11.1>P </MSH.11.1>\n"+
            "        </MSH.11>\n"+
            "        <MSH.12>\n"+
            "            <MSH.12.1>2.3.1 </MSH.12.1>\n"+
            "        </MSH.12>\n"+
            "        <MSH.13/>\n"+
            "        <MSH.14/>\n"+
            "        <MSH.15/>\n"+
            "        <MSH.16/>\n"+
            "        <MSH.17/>\n"+
            "        <MSH.18>\n"+
            "            <MSH.18.1>UTF-8 </MSH.18.1>\n"+
            "        </MSH.18>\n"+
            "    </MSH>\n"+
            "    <EVN>\n"+
            "        <EVN.1/>\n"+
            "        <EVN.2>\n"+
            "            <EVN.2.1>201205311112 </EVN.2.1>\n"+
            "        </EVN.2>\n"+
            "    </EVN>\n"+
            "    <PID>\n"+
            "        <PID.1>\n"+
            "            <PID.1.1>1 </PID.1.1>\n"+
            "        </PID.1>\n"+
            "        <PID.2/>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>8905671234 </PID.3.1>\n"+
            "            <PID.3.2/>\n"+
            "            <PID.3.3/>\n"+
            "            <PID.3.4/>\n"+
            "            <PID.3.5>PG </PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>5671234098 </PID.3.1>\n"+
            "            <PID.3.2/>\n"+
            "            <PID.3.3/>\n"+
            "            <PID.3.4/>\n"+
            "            <PID.3.5>PI </PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>1234765890 </PID.3.1>\n"+
            "            <PID.3.2/>\n"+
            "            <PID.3.3/>\n"+
            "            <PID.3.4/>\n"+
            "            <PID.3.5>PQ </PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>999777555 </PID.3.1>\n"+
            "            <PID.3.2></PID.3.2>\n"+
            "            <PID.3.3></PID.3.3>\n"+
            "            <PID.3.4></PID.3.4>\n"+
            "            <PID.3.5>MR</PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.4/>\n"+
            "        <PID.5>\n"+
            "            <PID.5.1>paitent_name </PID.5.1>\n"+
            "        </PID.5>\n"+
            "        <PID.6/>\n"+
            "        <PID.7/>\n"+
            "        <PID.8>\n"+
            "            <PID.8.1>M </PID.8.1>\n"+
            "        </PID.8>\n"+
            "        <PID.9/>\n"+
            "        <PID.10>\n"+
            "            <PID.10.1>1002-5 </PID.10.1>\n"+
            "        </PID.10>\n"+
            "        <PID.11>\n"+
            "            <PID.11.1/>\n"+
            "            <PID.11.2/>\n"+
            "            <PID.11.3>Fairfax City </PID.11.3>\n"+
            "            <PID.11.4>51 </PID.11.4>\n"+
            "            <PID.11.5>22333 </PID.11.5>\n"+
            "            <PID.11.6>USA </PID.11.6>\n"+
            "            <PID.11.7/>\n"+
            "            <PID.11.8/>\n"+
            "            <PID.11.9>Fairfax </PID.11.9>\n"+
            "        </PID.11>\n"+
            "        <PID.12/>\n"+
            "        <PID.13/>\n"+
            "        <PID.14/>\n"+
            "        <PID.15/>\n"+
            "        <PID.16/>\n"+
            "        <PID.17/>\n"+
            "        <PID.18/>\n"+
            "        <PID.19/>\n"+
            "        <PID.20/>\n"+
            "        <PID.21/>\n"+
            "        <PID.22>\n"+
            "            <PID.22.1>2186-5 </PID.22.1>\n"+
            "        </PID.22>\n"+
            "    </PID>\n"+
            "    <PV1>\n"+
            "        <PV1.1/>\n"+
            "        <PV1.2>\n"+
            "            <PV1.2.1>E </PV1.2.1>\n"+
            "        </PV1.2>\n"+
            "        <PV1.3/>\n"+
            "        <PV1.4/>\n"+
            "        <PV1.5/>\n"+
            "        <PV1.6/>\n"+
            "        <PV1.7/>\n"+
            "        <PV1.8/>\n"+
            "        <PV1.9/>\n"+
            "        <PV1.10/>\n"+
            "        <PV1.11/>\n"+
            "        <PV1.12/>\n"+
            "        <PV1.13/>\n"+
            "        <PV1.14/>\n"+
            "        <PV1.15/>\n"+
            "        <PV1.16/>\n"+
            "        <PV1.17/>\n"+
            "        <PV1.18/>\n"+
            "        <PV1.19>\n"+
            "            <PV1.19.1>7651234098 </PV1.19.1>\n"+
            "        </PV1.19>\n"+
            "        <PV1.20/>\n"+
            "        <PV1.21/>\n"+
            "        <PV1.22/>\n"+
            "        <PV1.23/>\n"+
            "        <PV1.24/>\n"+
            "        <PV1.25/>\n"+
            "        <PV1.26/>\n"+
            "        <PV1.27/>\n"+
            "        <PV1.28/>\n"+
            "        <PV1.29/>\n"+
            "        <PV1.30/>\n"+
            "        <PV1.31/>\n"+
            "        <PV1.32/>\n"+
            "        <PV1.33/>\n"+
            "        <PV1.34/>\n"+
            "        <PV1.35/>\n"+
            "        <PV1.36>\n"+
            "            <PV1.36.1>09 </PV1.36.1>\n"+
            "        </PV1.36>\n"+
            "        <PV1.37/>\n"+
            "        <PV1.38/>\n"+
            "        <PV1.39/>\n"+
            "        <PV1.40/>\n"+
            "        <PV1.41/>\n"+
            "        <PV1.42/>\n"+
            "        <PV1.43/>\n"+
            "        <PV1.44>\n"+
            "            <PV1.44.1>201104011406-0500 </PV1.44.1>\n"+
            "        </PV1.44>\n"+
            "        <PV1.45>\n"+
            "            <PV1.45.1>20110113164512-0500 </PV1.45.1>\n"+
            "        </PV1.45>\n"+
            "    </PV1>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>1 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>HD </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>SS001 </OBX.3.1>\n"+
            "            <OBX.3.2>Treating Facility Identifier      </OBX.3.2>\n"+
            "            <OBX.3.3>PHINQUESTION </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>Fairfax Hospital </OBX.5.1>\n"+
            "            <OBX.5.2>1098765432 </OBX.5.2>\n"+
            "            <OBX.5.3>NP </OBX.5.3>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>C </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "        <OBX.12/>\n"+
            "        <OBX.13/>\n"+
            "        <OBX.14/>\n"+
            "        <OBX.15/>\n"+
            "        <OBX.16/>\n"+
            "        <OBX.17/>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>2 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>XAD </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>SS002 </OBX.3.1>\n"+
            "            <OBX.3.2>Treating Facility Location    </OBX.3.2>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1/>\n"+
            "            <OBX.5.2>123 Gallows Rd. </OBX.5.2>\n"+
            "            <OBX.5.3/>\n"+
            "            <OBX.5.4/>\n"+
            "            <OBX.5.5/>\n"+
            "            <OBX.5.6>Fairfax City </OBX.5.6>\n"+
            "            <OBX.5.7>51 </OBX.5.7>\n"+
            "            <OBX.5.8>30341 </OBX.5.8>\n"+
            "            <OBX.5.9>USA </OBX.5.9>\n"+
            "            <OBX.5.10>C </OBX.5.10>\n"+
            "            <OBX.5.11/>\n"+
            "            <OBX.5.12>Fairfax </OBX.5.12>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>3 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>CWE </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>SS003 </OBX.3.1>\n"+
            "            <OBX.3.2>Facility/Visit Type     </OBX.3.2>\n"+
            "            <OBX.3.3>PHINQUESTION </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1></OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9>170300000X </OBX.9>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11/>\n"+
            "        <OBX.12>\n"+
            "            <OBX.12.1>X </OBX.12.1>\n"+
            "        </OBX.12>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>4 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>NM </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>21612-7 </OBX.3.1>\n"+
            "            <OBX.3.2>AGE TIME PAITENT REPORTED       </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>42 </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6>\n"+
            "            <OBX.6.1>a </OBX.6.1>\n"+
            "            <OBX.6.2>a </OBX.6.2>\n"+
            "            <OBX.6.3>UCUM </OBX.6.3>\n"+
            "        </OBX.6>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>5 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>TS </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>11368-8 </OBX.3.1>\n"+
            "            <OBX.3.2>ILLNESS OR INJURY ONSET DATE AND TIME:TMSTP:PT:PAITENT:QN       </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>20120401 </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>6 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>CWE </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>8661-1 </OBX.3.1>\n"+
            "            <OBX.3.2>CHIEF COMPLAINT:FIND:PT:PAITENT:NOM:REPORTED      </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1/>\n"+
            "            <OBX.5.2/>\n"+
            "            <OBX.5.3/>\n"+
            "            <OBX.5.4/>\n"+
            "            <OBX.5.5/>\n"+
            "            <OBX.5.6/>\n"+
            "            <OBX.5.7/>\n"+
            "            <OBX.5.8/>\n"+
            "            <OBX.5.9>Nausia and Dizzyness </OBX.5.9>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>7 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>TX </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>54094-8 </OBX.3.1>\n"+
            "            <OBX.3.2>TRIAGE NOTE:FIND:PT:EMERGENCY DEPARTMENT:DOC      </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>The paitent seemed dizzy and sick to his stomach. </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>8 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>TX </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>44833-2 </OBX.3.1>\n"+
            "            <OBX.3.2>DIAGNOSIS:PRELIMINARY:IMP:PT:PAITENT:NOM: </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>The patient is suffering from an inner ear contdtition. </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>9 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>NM </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>11289-6 </OBX.3.1>\n"+
            "            <OBX.3.2>BODY TEMPERATURE:TEMP:ENCTRFIRST:PAITENT:QN     </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>98.6 </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6>\n"+
            "            <OBX.6.1/>\n"+
            "            <OBX.6.2>[degF] </OBX.6.2>\n"+
            "            <OBX.6.3>UCUM </OBX.6.3>\n"+
            "        </OBX.6>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>10 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>NM </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>59408-5 </OBX.3.1>\n"+
            "            <OBX.3.2>OXYGEN SATURATION:MFR:PT:BLDA:QN:PULSE OXIMETRY     </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>95 </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6>\n"+
            "            <OBX.6.1>% </OBX.6.1>\n"+
            "            <OBX.6.2> </OBX.6.2>\n"+
            "            <OBX.6.3>UCUM </OBX.6.3>\n"+
            "        </OBX.6>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <DG1>\n"+
            "        <DG1.1>\n"+
            "            <DG1.1.1>1 </DG1.1.1>\n"+
            "        </DG1.1>\n"+
            "        <DG1.2/>\n"+
            "        <DG1.3>\n"+
            "            <DG1.3.1>4414 </DG1.3.1>\n"+
            "        </DG1.3>\n"+
            "        <DG1.4/>\n"+
            "        <DG1.5/>\n"+
            "        <DG1.6>\n"+
            "            <DG1.6.1>W </DG1.6.1>\n"+
            "        </DG1.6>\n"+
            "    </DG1>\n"+
            "</HL7Message>";

    public static String    xmlData_Conditional_data_missing = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
            "<HL7Message>\n"+
            "    <MSH>\n"+
            "        <MSH.1>| </MSH.1>\n"+
            "        <MSH.2>^~\\\\ </MSH.2>\n"+
            "        <MSH.3/>\n"+
            "        <MSH.4/>\n"+
            "        <MSH.5/>\n"+
            "        <MSH.6/>\n"+
            "        <MSH.7>\n"+
            "            <MSH.7.1>20111213131225 </MSH.7.1>\n"+
            "        </MSH.7>\n"+
            "        <MSH.8/>\n"+
            "        <MSH.9>\n"+
            "            <MSH.9.1>ADT </MSH.9.1>\n"+
            "            <MSH.9.2>A01 </MSH.9.2>\n"+
            "        </MSH.9>\n"+
            "        <MSH.10>\n"+
            "            <MSH.10.1>MSG00001 </MSH.10.1>\n"+
            "        </MSH.10>\n"+
            "        <MSH.11>\n"+
            "            <MSH.11.1>P </MSH.11.1>\n"+
            "        </MSH.11>\n"+
            "        <MSH.12>\n"+
            "            <MSH.12.1>2.3.1 </MSH.12.1>\n"+
            "        </MSH.12>\n"+
            "        <MSH.13/>\n"+
            "        <MSH.14/>\n"+
            "        <MSH.15/>\n"+
            "        <MSH.16/>\n"+
            "        <MSH.17/>\n"+
            "        <MSH.18>\n"+
            "            <MSH.18.1>UTF-8 </MSH.18.1>\n"+
            "        </MSH.18>\n"+
            "    </MSH>\n"+
            "    <EVN>\n"+
            "        <EVN.1/>\n"+
            "        <EVN.2>\n"+
            "            <EVN.2.1>201205311112 </EVN.2.1>\n"+
            "        </EVN.2>\n"+
            "    </EVN>\n"+
            "    <PID>\n"+
            "        <PID.1>\n"+
            "            <PID.1.1>1 </PID.1.1>\n"+
            "        </PID.1>\n"+
            "        <PID.2/>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>8905671234 </PID.3.1>\n"+
            "            <PID.3.2/>\n"+
            "            <PID.3.3/>\n"+
            "            <PID.3.4/>\n"+
            "            <PID.3.5>PG </PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>5671234098 </PID.3.1>\n"+
            "            <PID.3.2/>\n"+
            "            <PID.3.3/>\n"+
            "            <PID.3.4/>\n"+
            "            <PID.3.5>PI </PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>1234765890 </PID.3.1>\n"+
            "            <PID.3.2/>\n"+
            "            <PID.3.3/>\n"+
            "            <PID.3.4/>\n"+
            "            <PID.3.5>PQ </PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>999777555 </PID.3.1>\n"+
            "            <PID.3.2></PID.3.2>\n"+
            "            <PID.3.3></PID.3.3>\n"+
            "            <PID.3.4></PID.3.4>\n"+
            "            <PID.3.5>MR</PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.4/>\n"+
            "        <PID.5>\n"+
            "            <PID.5.1>paitent_name </PID.5.1>\n"+
            "        </PID.5>\n"+
            "        <PID.6/>\n"+
            "        <PID.7/>\n"+
            "        <PID.8>\n"+
            "            <PID.8.1>M </PID.8.1>\n"+
            "        </PID.8>\n"+
            "        <PID.9/>\n"+
            "        <PID.10>\n"+
            "            <PID.10.1>1002-5 </PID.10.1>\n"+
            "        </PID.10>\n"+
            "        <PID.11>\n"+
            "            <PID.11.1/>\n"+
            "            <PID.11.2/>\n"+
            "            <PID.11.3>Fairfax City </PID.11.3>\n"+
            "            <PID.11.4>51 </PID.11.4>\n"+
            "            <PID.11.5>22333 </PID.11.5>\n"+
            "            <PID.11.6>USA </PID.11.6>\n"+
            "            <PID.11.7/>\n"+
            "            <PID.11.8/>\n"+
            "            <PID.11.9>Fairfax </PID.11.9>\n"+
            "        </PID.11>\n"+
            "        <PID.12/>\n"+
            "        <PID.13/>\n"+
            "        <PID.14/>\n"+
            "        <PID.15/>\n"+
            "        <PID.16/>\n"+
            "        <PID.17/>\n"+
            "        <PID.18/>\n"+
            "        <PID.19/>\n"+
            "        <PID.20/>\n"+
            "        <PID.21/>\n"+
            "        <PID.22>\n"+
            "            <PID.22.1>2186-5 </PID.22.1>\n"+
            "        </PID.22>\n"+
            "    </PID>\n"+
            "    <PV1>\n"+
            "        <PV1.1/>\n"+
            "        <PV1.2>\n"+
            "            <PV1.2.1>E </PV1.2.1>\n"+
            "        </PV1.2>\n"+
            "        <PV1.3/>\n"+
            "        <PV1.4/>\n"+
            "        <PV1.5/>\n"+
            "        <PV1.6/>\n"+
            "        <PV1.7/>\n"+
            "        <PV1.8/>\n"+
            "        <PV1.9/>\n"+
            "        <PV1.10/>\n"+
            "        <PV1.11/>\n"+
            "        <PV1.12/>\n"+
            "        <PV1.13/>\n"+
            "        <PV1.14/>\n"+
            "        <PV1.15/>\n"+
            "        <PV1.16/>\n"+
            "        <PV1.17/>\n"+
            "        <PV1.18/>\n"+
            "        <PV1.19>\n"+
            "            <PV1.19.1>7651234098 </PV1.19.1>\n"+
            "        </PV1.19>\n"+
            "        <PV1.20/>\n"+
            "        <PV1.21/>\n"+
            "        <PV1.22/>\n"+
            "        <PV1.23/>\n"+
            "        <PV1.24/>\n"+
            "        <PV1.25/>\n"+
            "        <PV1.26/>\n"+
            "        <PV1.27/>\n"+
            "        <PV1.28/>\n"+
            "        <PV1.29/>\n"+
            "        <PV1.30/>\n"+
            "        <PV1.31/>\n"+
            "        <PV1.32/>\n"+
            "        <PV1.33/>\n"+
            "        <PV1.34/>\n"+
            "        <PV1.35/>\n"+
            "        <PV1.36>\n"+
            "            <PV1.36.1>09 </PV1.36.1>\n"+
            "        </PV1.36>\n"+
            "        <PV1.37/>\n"+
            "        <PV1.38/>\n"+
            "        <PV1.39/>\n"+
            "        <PV1.40/>\n"+
            "        <PV1.41/>\n"+
            "        <PV1.42/>\n"+
            "        <PV1.43/>\n"+
            "        <PV1.44>\n"+
            "            <PV1.44.1>201104011406-0500 </PV1.44.1>\n"+
            "        </PV1.44>\n"+
            "        <PV1.45>\n"+
            "            <PV1.45.1>20110113164512-0500 </PV1.45.1>\n"+
            "        </PV1.45>\n"+
            "    </PV1>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>1 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>HD </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>SS001 </OBX.3.1>\n"+
            "            <OBX.3.2>Treating Facility Identifier      </OBX.3.2>\n"+
            "            <OBX.3.3>PHINQUESTION </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>Fairfax Hospital </OBX.5.1>\n"+
            "            <OBX.5.2>1098765432 </OBX.5.2>\n"+
            "            <OBX.5.3>NP </OBX.5.3>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>C </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "        <OBX.12/>\n"+
            "        <OBX.13/>\n"+
            "        <OBX.14/>\n"+
            "        <OBX.15/>\n"+
            "        <OBX.16/>\n"+
            "        <OBX.17/>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>2 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>XAD </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>SS002 </OBX.3.1>\n"+
            "            <OBX.3.2>Treating Facility Location    </OBX.3.2>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1/>\n"+
            "            <OBX.5.2>123 Gallows Rd. </OBX.5.2>\n"+
            "            <OBX.5.3/>\n"+
            "            <OBX.5.4/>\n"+
            "            <OBX.5.5/>\n"+
            "            <OBX.5.6>Fairfax City </OBX.5.6>\n"+
            "            <OBX.5.7></OBX.5.7>\n"+
            "            <OBX.5.8>30341 </OBX.5.8>\n"+
            "            <OBX.5.9>USA </OBX.5.9>\n"+
            "            <OBX.5.10>C </OBX.5.10>\n"+
            "            <OBX.5.11/>\n"+
            "            <OBX.5.12>Fairfax </OBX.5.12>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>3 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>CWE </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>SS003 </OBX.3.1>\n"+
            "            <OBX.3.2>Facility/Visit Type     </OBX.3.2>\n"+
            "            <OBX.3.3>PHINQUESTION </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1></OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9>170300000X </OBX.9>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11/>\n"+
            "        <OBX.12>\n"+
            "            <OBX.12.1>X </OBX.12.1>\n"+
            "        </OBX.12>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>4 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>NM </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>21612-7 </OBX.3.1>\n"+
            "            <OBX.3.2>AGE TIME PAITENT REPORTED       </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>42 </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6>\n"+
            "            <OBX.6.1>a </OBX.6.1>\n"+
            "            <OBX.6.2>a </OBX.6.2>\n"+
            "            <OBX.6.3>UCUM </OBX.6.3>\n"+
            "        </OBX.6>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>5 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>TS </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>11368-8 </OBX.3.1>\n"+
            "            <OBX.3.2>ILLNESS OR INJURY ONSET DATE AND TIME:TMSTP:PT:PAITENT:QN       </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>20120401 </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>6 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>CWE </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>8661-1 </OBX.3.1>\n"+
            "            <OBX.3.2>CHIEF COMPLAINT:FIND:PT:PAITENT:NOM:REPORTED      </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1/>\n"+
            "            <OBX.5.2/>\n"+
            "            <OBX.5.3/>\n"+
            "            <OBX.5.4/>\n"+
            "            <OBX.5.5/>\n"+
            "            <OBX.5.6/>\n"+
            "            <OBX.5.7/>\n"+
            "            <OBX.5.8/>\n"+
            "            <OBX.5.9>Nausia and Dizzyness </OBX.5.9>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>7 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>TX </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>54094-8 </OBX.3.1>\n"+
            "            <OBX.3.2>TRIAGE NOTE:FIND:PT:EMERGENCY DEPARTMENT:DOC      </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>The paitent seemed dizzy and sick to his stomach. </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>8 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>TX </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>44833-2 </OBX.3.1>\n"+
            "            <OBX.3.2>DIAGNOSIS:PRELIMINARY:IMP:PT:PAITENT:NOM: </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>The patient is suffering from an inner ear contdtition. </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>9 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>NM </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>11289-6 </OBX.3.1>\n"+
            "            <OBX.3.2>BODY TEMPERATURE:TEMP:ENCTRFIRST:PAITENT:QN     </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>98.6 </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6>\n"+
            "            <OBX.6.1/>\n"+
            "            <OBX.6.2>[degF] </OBX.6.2>\n"+
            "            <OBX.6.3>UCUM </OBX.6.3>\n"+
            "        </OBX.6>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>10 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>NM </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>59408-5 </OBX.3.1>\n"+
            "            <OBX.3.2>OXYGEN SATURATION:MFR:PT:BLDA:QN:PULSE OXIMETRY     </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>95 </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6>\n"+
            "            <OBX.6.1>% </OBX.6.1>\n"+
            "            <OBX.6.2> </OBX.6.2>\n"+
            "            <OBX.6.3>UCUM </OBX.6.3>\n"+
            "        </OBX.6>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <DG1>\n"+
            "        <DG1.1>\n"+
            "            <DG1.1.1>1 </DG1.1.1>\n"+
            "        </DG1.1>\n"+
            "        <DG1.2/>\n"+
            "        <DG1.3>\n"+
            "            <DG1.3.1>4414 </DG1.3.1>\n"+
            "        </DG1.3>\n"+
            "        <DG1.4/>\n"+
            "        <DG1.5/>\n"+
            "        <DG1.6>\n"+
            "            <DG1.6.1>W </DG1.6.1>\n"+
            "        </DG1.6>\n"+
            "    </DG1>\n"+
            "</HL7Message>";

    public static String    xmlData_with_valid_age_value = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
            "<HL7Message>\n"+
            "    <MSH>\n"+
            "        <MSH.1>| </MSH.1>\n"+
            "        <MSH.2>^~\\\\ </MSH.2>\n"+
            "        <MSH.3/>\n"+
            "        <MSH.4/>\n"+
            "        <MSH.5/>\n"+
            "        <MSH.6/>\n"+
            "        <MSH.7>\n"+
            "            <MSH.7.1>20111213131225 </MSH.7.1>\n"+
            "        </MSH.7>\n"+
            "        <MSH.8/>\n"+
            "        <MSH.9>\n"+
            "            <MSH.9.1>ADT </MSH.9.1>\n"+
            "            <MSH.9.2>A01 </MSH.9.2>\n"+
            "        </MSH.9>\n"+
            "        <MSH.10>\n"+
            "            <MSH.10.1>MSG00001 </MSH.10.1>\n"+
            "        </MSH.10>\n"+
            "        <MSH.11>\n"+
            "            <MSH.11.1>P </MSH.11.1>\n"+
            "        </MSH.11>\n"+
            "        <MSH.12>\n"+
            "            <MSH.12.1>2.3.1 </MSH.12.1>\n"+
            "        </MSH.12>\n"+
            "        <MSH.13/>\n"+
            "        <MSH.14/>\n"+
            "        <MSH.15/>\n"+
            "        <MSH.16/>\n"+
            "        <MSH.17/>\n"+
            "        <MSH.18>\n"+
            "            <MSH.18.1>UTF-8 </MSH.18.1>\n"+
            "        </MSH.18>\n"+
            "    </MSH>\n"+
            "    <EVN>\n"+
            "        <EVN.1/>\n"+
            "        <EVN.2>\n"+
            "            <EVN.2.1>201205311112 </EVN.2.1>\n"+
            "        </EVN.2>\n"+
            "    </EVN>\n"+
            "    <PID>\n"+
            "        <PID.1>\n"+
            "            <PID.1.1>1 </PID.1.1>\n"+
            "        </PID.1>\n"+
            "        <PID.2/>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>8905671234 </PID.3.1>\n"+
            "            <PID.3.2/>\n"+
            "            <PID.3.3/>\n"+
            "            <PID.3.4/>\n"+
            "            <PID.3.5>PG </PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>5671234098 </PID.3.1>\n"+
            "            <PID.3.2/>\n"+
            "            <PID.3.3/>\n"+
            "            <PID.3.4/>\n"+
            "            <PID.3.5>PI </PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>1234765890 </PID.3.1>\n"+
            "            <PID.3.2/>\n"+
            "            <PID.3.3/>\n"+
            "            <PID.3.4/>\n"+
            "            <PID.3.5>PQ </PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>999777555 </PID.3.1>\n"+
            "            <PID.3.2></PID.3.2>\n"+
            "            <PID.3.3></PID.3.3>\n"+
            "            <PID.3.4></PID.3.4>\n"+
            "            <PID.3.5>MR</PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.4/>\n"+
            "        <PID.5>\n"+
            "            <PID.5.1>paitent_name </PID.5.1>\n"+
            "        </PID.5>\n"+
            "        <PID.6/>\n"+
            "        <PID.7/>\n"+
            "        <PID.8>\n"+
            "            <PID.8.1>M </PID.8.1>\n"+
            "        </PID.8>\n"+
            "        <PID.9/>\n"+
            "        <PID.10>\n"+
            "            <PID.10.1>1002-5 </PID.10.1>\n"+
            "        </PID.10>\n"+
            "        <PID.11>\n"+
            "            <PID.11.1/>\n"+
            "            <PID.11.2/>\n"+
            "            <PID.11.3>Fairfax City </PID.11.3>\n"+
            "            <PID.11.4>51 </PID.11.4>\n"+
            "            <PID.11.5>22333 </PID.11.5>\n"+
            "            <PID.11.6>USA </PID.11.6>\n"+
            "            <PID.11.7/>\n"+
            "            <PID.11.8/>\n"+
            "            <PID.11.9>Fairfax </PID.11.9>\n"+
            "        </PID.11>\n"+
            "        <PID.12/>\n"+
            "        <PID.13/>\n"+
            "        <PID.14/>\n"+
            "        <PID.15/>\n"+
            "        <PID.16/>\n"+
            "        <PID.17/>\n"+
            "        <PID.18/>\n"+
            "        <PID.19/>\n"+
            "        <PID.20/>\n"+
            "        <PID.21/>\n"+
            "        <PID.22>\n"+
            "            <PID.22.1>2186-5 </PID.22.1>\n"+
            "        </PID.22>\n"+
            "    </PID>\n"+
            "    <PV1>\n"+
            "        <PV1.1/>\n"+
            "        <PV1.2>\n"+
            "            <PV1.2.1>E </PV1.2.1>\n"+
            "        </PV1.2>\n"+
            "        <PV1.3/>\n"+
            "        <PV1.4/>\n"+
            "        <PV1.5/>\n"+
            "        <PV1.6/>\n"+
            "        <PV1.7/>\n"+
            "        <PV1.8/>\n"+
            "        <PV1.9/>\n"+
            "        <PV1.10/>\n"+
            "        <PV1.11/>\n"+
            "        <PV1.12/>\n"+
            "        <PV1.13/>\n"+
            "        <PV1.14/>\n"+
            "        <PV1.15/>\n"+
            "        <PV1.16/>\n"+
            "        <PV1.17/>\n"+
            "        <PV1.18/>\n"+
            "        <PV1.19>\n"+
            "            <PV1.19.1>7651234098 </PV1.19.1>\n"+
            "        </PV1.19>\n"+
            "        <PV1.20/>\n"+
            "        <PV1.21/>\n"+
            "        <PV1.22/>\n"+
            "        <PV1.23/>\n"+
            "        <PV1.24/>\n"+
            "        <PV1.25/>\n"+
            "        <PV1.26/>\n"+
            "        <PV1.27/>\n"+
            "        <PV1.28/>\n"+
            "        <PV1.29/>\n"+
            "        <PV1.30/>\n"+
            "        <PV1.31/>\n"+
            "        <PV1.32/>\n"+
            "        <PV1.33/>\n"+
            "        <PV1.34/>\n"+
            "        <PV1.35/>\n"+
            "        <PV1.36>\n"+
            "            <PV1.36.1>09 </PV1.36.1>\n"+
            "        </PV1.36>\n"+
            "        <PV1.37/>\n"+
            "        <PV1.38/>\n"+
            "        <PV1.39/>\n"+
            "        <PV1.40/>\n"+
            "        <PV1.41/>\n"+
            "        <PV1.42/>\n"+
            "        <PV1.43/>\n"+
            "        <PV1.44>\n"+
            "            <PV1.44.1>201104011406-0500 </PV1.44.1>\n"+
            "        </PV1.44>\n"+
            "        <PV1.45>\n"+
            "            <PV1.45.1>20110113164512-0500 </PV1.45.1>\n"+
            "        </PV1.45>\n"+
            "    </PV1>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>1 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>HD </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>SS001 </OBX.3.1>\n"+
            "            <OBX.3.2>Treating Facility Identifier      </OBX.3.2>\n"+
            "            <OBX.3.3>PHINQUESTION </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>Fairfax Hospital </OBX.5.1>\n"+
            "            <OBX.5.2>1098765432 </OBX.5.2>\n"+
            "            <OBX.5.3>NP </OBX.5.3>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>C </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "        <OBX.12/>\n"+
            "        <OBX.13/>\n"+
            "        <OBX.14/>\n"+
            "        <OBX.15/>\n"+
            "        <OBX.16/>\n"+
            "        <OBX.17/>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>2 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>XAD </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>SS002 </OBX.3.1>\n"+
            "            <OBX.3.2>Treating Facility Location    </OBX.3.2>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1/>\n"+
            "            <OBX.5.2>123 Gallows Rd. </OBX.5.2>\n"+
            "            <OBX.5.3/>\n"+
            "            <OBX.5.4/>\n"+
            "            <OBX.5.5/>\n"+
            "            <OBX.5.6>Fairfax City </OBX.5.6>\n"+
            "            <OBX.5.7>51 </OBX.5.7>\n"+
            "            <OBX.5.8>30341 </OBX.5.8>\n"+
            "            <OBX.5.9>USA </OBX.5.9>\n"+
            "            <OBX.5.10>C </OBX.5.10>\n"+
            "            <OBX.5.11/>\n"+
            "            <OBX.5.12>Fairfax </OBX.5.12>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>3 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>CWE </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>SS003 </OBX.3.1>\n"+
            "            <OBX.3.2>Facility/Visit Type     </OBX.3.2>\n"+
            "            <OBX.3.3>PHINQUESTION </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1></OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9>170300000X </OBX.9>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11/>\n"+
            "        <OBX.12>\n"+
            "            <OBX.12.1>X </OBX.12.1>\n"+
            "        </OBX.12>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>4 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>NM </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>21612-7 </OBX.3.1>\n"+
            "            <OBX.3.2>AGE TIME PAITENT REPORTED       </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>42</OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6>\n"+
            "            <OBX.6.1>a </OBX.6.1>\n"+
            "            <OBX.6.2>a </OBX.6.2>\n"+
            "            <OBX.6.3>UCUM </OBX.6.3>\n"+
            "        </OBX.6>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>5 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>TS </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>11368-8 </OBX.3.1>\n"+
            "            <OBX.3.2>ILLNESS OR INJURY ONSET DATE AND TIME:TMSTP:PT:PAITENT:QN       </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>20120401 </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>6 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>CWE </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>8661-1 </OBX.3.1>\n"+
            "            <OBX.3.2>CHIEF COMPLAINT:FIND:PT:PAITENT:NOM:REPORTED      </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1/>\n"+
            "            <OBX.5.2/>\n"+
            "            <OBX.5.3/>\n"+
            "            <OBX.5.4/>\n"+
            "            <OBX.5.5/>\n"+
            "            <OBX.5.6/>\n"+
            "            <OBX.5.7/>\n"+
            "            <OBX.5.8/>\n"+
            "            <OBX.5.9>Nausia and Dizzyness </OBX.5.9>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>7 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>TX </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>54094-8 </OBX.3.1>\n"+
            "            <OBX.3.2>TRIAGE NOTE:FIND:PT:EMERGENCY DEPARTMENT:DOC      </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>The paitent seemed dizzy and sick to his stomach. </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>8 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>TX </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>44833-2 </OBX.3.1>\n"+
            "            <OBX.3.2>DIAGNOSIS:PRELIMINARY:IMP:PT:PAITENT:NOM: </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>The patient is suffering from an inner ear contdtition. </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>9 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>NM </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>11289-6 </OBX.3.1>\n"+
            "            <OBX.3.2>BODY TEMPERATURE:TEMP:ENCTRFIRST:PAITENT:QN     </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>98.6 </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6>\n"+
            "            <OBX.6.1/>\n"+
            "            <OBX.6.2>[degF] </OBX.6.2>\n"+
            "            <OBX.6.3>UCUM </OBX.6.3>\n"+
            "        </OBX.6>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>10 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>NM </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>59408-5 </OBX.3.1>\n"+
            "            <OBX.3.2>OXYGEN SATURATION:MFR:PT:BLDA:QN:PULSE OXIMETRY     </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>95 </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6>\n"+
            "            <OBX.6.1>% </OBX.6.1>\n"+
            "            <OBX.6.2> </OBX.6.2>\n"+
            "            <OBX.6.3>UCUM </OBX.6.3>\n"+
            "        </OBX.6>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <DG1>\n"+
            "        <DG1.1>\n"+
            "            <DG1.1.1>1 </DG1.1.1>\n"+
            "        </DG1.1>\n"+
            "        <DG1.2/>\n"+
            "        <DG1.3>\n"+
            "            <DG1.3.1>4414 </DG1.3.1>\n"+
            "        </DG1.3>\n"+
            "        <DG1.4/>\n"+
            "        <DG1.5/>\n"+
            "        <DG1.6>\n"+
            "            <DG1.6.1>W </DG1.6.1>\n"+
            "        </DG1.6>\n"+
            "    </DG1>\n"+
            "</HL7Message>";

    public static String    xmlData_with_invalid_age_value = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
            "<HL7Message>\n"+
            "    <MSH>\n"+
            "        <MSH.1>| </MSH.1>\n"+
            "        <MSH.2>^~\\\\ </MSH.2>\n"+
            "        <MSH.3/>\n"+
            "        <MSH.4/>\n"+
            "        <MSH.5/>\n"+
            "        <MSH.6/>\n"+
            "        <MSH.7>\n"+
            "            <MSH.7.1>20111213131225 </MSH.7.1>\n"+
            "        </MSH.7>\n"+
            "        <MSH.8/>\n"+
            "        <MSH.9>\n"+
            "            <MSH.9.1>ADT </MSH.9.1>\n"+
            "            <MSH.9.2>A01 </MSH.9.2>\n"+
            "        </MSH.9>\n"+
            "        <MSH.10>\n"+
            "            <MSH.10.1>MSG00001 </MSH.10.1>\n"+
            "        </MSH.10>\n"+
            "        <MSH.11>\n"+
            "            <MSH.11.1>P </MSH.11.1>\n"+
            "        </MSH.11>\n"+
            "        <MSH.12>\n"+
            "            <MSH.12.1>2.3.1 </MSH.12.1>\n"+
            "        </MSH.12>\n"+
            "        <MSH.13/>\n"+
            "        <MSH.14/>\n"+
            "        <MSH.15/>\n"+
            "        <MSH.16/>\n"+
            "        <MSH.17/>\n"+
            "        <MSH.18>\n"+
            "            <MSH.18.1>UTF-8 </MSH.18.1>\n"+
            "        </MSH.18>\n"+
            "    </MSH>\n"+
            "    <EVN>\n"+
            "        <EVN.1/>\n"+
            "        <EVN.2>\n"+
            "            <EVN.2.1>201205311112 </EVN.2.1>\n"+
            "        </EVN.2>\n"+
            "    </EVN>\n"+
            "    <PID>\n"+
            "        <PID.1>\n"+
            "            <PID.1.1>1 </PID.1.1>\n"+
            "        </PID.1>\n"+
            "        <PID.2/>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>8905671234 </PID.3.1>\n"+
            "            <PID.3.2/>\n"+
            "            <PID.3.3/>\n"+
            "            <PID.3.4/>\n"+
            "            <PID.3.5>PG </PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>5671234098 </PID.3.1>\n"+
            "            <PID.3.2/>\n"+
            "            <PID.3.3/>\n"+
            "            <PID.3.4/>\n"+
            "            <PID.3.5>PI </PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>1234765890 </PID.3.1>\n"+
            "            <PID.3.2/>\n"+
            "            <PID.3.3/>\n"+
            "            <PID.3.4/>\n"+
            "            <PID.3.5>PQ </PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>999777555 </PID.3.1>\n"+
            "            <PID.3.2></PID.3.2>\n"+
            "            <PID.3.3></PID.3.3>\n"+
            "            <PID.3.4></PID.3.4>\n"+
            "            <PID.3.5>MR</PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.4/>\n"+
            "        <PID.5>\n"+
            "            <PID.5.1>paitent_name </PID.5.1>\n"+
            "        </PID.5>\n"+
            "        <PID.6/>\n"+
            "        <PID.7/>\n"+
            "        <PID.8>\n"+
            "            <PID.8.1>M </PID.8.1>\n"+
            "        </PID.8>\n"+
            "        <PID.9/>\n"+
            "        <PID.10>\n"+
            "            <PID.10.1>1002-5 </PID.10.1>\n"+
            "        </PID.10>\n"+
            "        <PID.11>\n"+
            "            <PID.11.1/>\n"+
            "            <PID.11.2/>\n"+
            "            <PID.11.3>Fairfax City </PID.11.3>\n"+
            "            <PID.11.4>51 </PID.11.4>\n"+
            "            <PID.11.5>22333 </PID.11.5>\n"+
            "            <PID.11.6>USA </PID.11.6>\n"+
            "            <PID.11.7/>\n"+
            "            <PID.11.8/>\n"+
            "            <PID.11.9>Fairfax </PID.11.9>\n"+
            "        </PID.11>\n"+
            "        <PID.12/>\n"+
            "        <PID.13/>\n"+
            "        <PID.14/>\n"+
            "        <PID.15/>\n"+
            "        <PID.16/>\n"+
            "        <PID.17/>\n"+
            "        <PID.18/>\n"+
            "        <PID.19/>\n"+
            "        <PID.20/>\n"+
            "        <PID.21/>\n"+
            "        <PID.22>\n"+
            "            <PID.22.1>2186-5 </PID.22.1>\n"+
            "        </PID.22>\n"+
            "    </PID>\n"+
            "    <PV1>\n"+
            "        <PV1.1/>\n"+
            "        <PV1.2>\n"+
            "            <PV1.2.1>E </PV1.2.1>\n"+
            "        </PV1.2>\n"+
            "        <PV1.3/>\n"+
            "        <PV1.4/>\n"+
            "        <PV1.5/>\n"+
            "        <PV1.6/>\n"+
            "        <PV1.7/>\n"+
            "        <PV1.8/>\n"+
            "        <PV1.9/>\n"+
            "        <PV1.10/>\n"+
            "        <PV1.11/>\n"+
            "        <PV1.12/>\n"+
            "        <PV1.13/>\n"+
            "        <PV1.14/>\n"+
            "        <PV1.15/>\n"+
            "        <PV1.16/>\n"+
            "        <PV1.17/>\n"+
            "        <PV1.18/>\n"+
            "        <PV1.19>\n"+
            "            <PV1.19.1>7651234098 </PV1.19.1>\n"+
            "        </PV1.19>\n"+
            "        <PV1.20/>\n"+
            "        <PV1.21/>\n"+
            "        <PV1.22/>\n"+
            "        <PV1.23/>\n"+
            "        <PV1.24/>\n"+
            "        <PV1.25/>\n"+
            "        <PV1.26/>\n"+
            "        <PV1.27/>\n"+
            "        <PV1.28/>\n"+
            "        <PV1.29/>\n"+
            "        <PV1.30/>\n"+
            "        <PV1.31/>\n"+
            "        <PV1.32/>\n"+
            "        <PV1.33/>\n"+
            "        <PV1.34/>\n"+
            "        <PV1.35/>\n"+
            "        <PV1.36>\n"+
            "            <PV1.36.1>09 </PV1.36.1>\n"+
            "        </PV1.36>\n"+
            "        <PV1.37/>\n"+
            "        <PV1.38/>\n"+
            "        <PV1.39/>\n"+
            "        <PV1.40/>\n"+
            "        <PV1.41/>\n"+
            "        <PV1.42/>\n"+
            "        <PV1.43/>\n"+
            "        <PV1.44>\n"+
            "            <PV1.44.1>201104011406-0500 </PV1.44.1>\n"+
            "        </PV1.44>\n"+
            "        <PV1.45>\n"+
            "            <PV1.45.1>20110113164512-0500 </PV1.45.1>\n"+
            "        </PV1.45>\n"+
            "    </PV1>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>1 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>HD </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>SS001 </OBX.3.1>\n"+
            "            <OBX.3.2>Treating Facility Identifier      </OBX.3.2>\n"+
            "            <OBX.3.3>PHINQUESTION </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>Fairfax Hospital </OBX.5.1>\n"+
            "            <OBX.5.2>1098765432 </OBX.5.2>\n"+
            "            <OBX.5.3>NP </OBX.5.3>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>C </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "        <OBX.12/>\n"+
            "        <OBX.13/>\n"+
            "        <OBX.14/>\n"+
            "        <OBX.15/>\n"+
            "        <OBX.16/>\n"+
            "        <OBX.17/>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>2 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>XAD </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>SS002 </OBX.3.1>\n"+
            "            <OBX.3.2>Treating Facility Location    </OBX.3.2>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1/>\n"+
            "            <OBX.5.2>123 Gallows Rd. </OBX.5.2>\n"+
            "            <OBX.5.3/>\n"+
            "            <OBX.5.4/>\n"+
            "            <OBX.5.5/>\n"+
            "            <OBX.5.6>Fairfax City </OBX.5.6>\n"+
            "            <OBX.5.7>51 </OBX.5.7>\n"+
            "            <OBX.5.8>30341 </OBX.5.8>\n"+
            "            <OBX.5.9>USA </OBX.5.9>\n"+
            "            <OBX.5.10>C </OBX.5.10>\n"+
            "            <OBX.5.11/>\n"+
            "            <OBX.5.12>Fairfax </OBX.5.12>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>3 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>CWE </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>SS003 </OBX.3.1>\n"+
            "            <OBX.3.2>Facility/Visit Type     </OBX.3.2>\n"+
            "            <OBX.3.3>PHINQUESTION </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1></OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9>170300000X </OBX.9>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11/>\n"+
            "        <OBX.12>\n"+
            "            <OBX.12.1>X </OBX.12.1>\n"+
            "        </OBX.12>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>4 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>NM </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>21612-7 </OBX.3.1>\n"+
            "            <OBX.3.2>AGE TIME PAITENT REPORTED       </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>30</OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6>\n"+
            "            <OBX.6.1>a </OBX.6.1>\n"+
            "            <OBX.6.2>a </OBX.6.2>\n"+
            "            <OBX.6.3>UCUM </OBX.6.3>\n"+
            "        </OBX.6>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>5 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>TS </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>11368-8 </OBX.3.1>\n"+
            "            <OBX.3.2>ILLNESS OR INJURY ONSET DATE AND TIME:TMSTP:PT:PAITENT:QN       </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>20120401 </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>6 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>CWE </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>8661-1 </OBX.3.1>\n"+
            "            <OBX.3.2>CHIEF COMPLAINT:FIND:PT:PAITENT:NOM:REPORTED      </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1/>\n"+
            "            <OBX.5.2/>\n"+
            "            <OBX.5.3/>\n"+
            "            <OBX.5.4/>\n"+
            "            <OBX.5.5/>\n"+
            "            <OBX.5.6/>\n"+
            "            <OBX.5.7/>\n"+
            "            <OBX.5.8/>\n"+
            "            <OBX.5.9>Nausia and Dizzyness </OBX.5.9>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>7 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>TX </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>54094-8 </OBX.3.1>\n"+
            "            <OBX.3.2>TRIAGE NOTE:FIND:PT:EMERGENCY DEPARTMENT:DOC      </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>The paitent seemed dizzy and sick to his stomach. </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>8 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>TX </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>44833-2 </OBX.3.1>\n"+
            "            <OBX.3.2>DIAGNOSIS:PRELIMINARY:IMP:PT:PAITENT:NOM: </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>The patient is suffering from an inner ear contdtition. </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>9 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>NM </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>11289-6 </OBX.3.1>\n"+
            "            <OBX.3.2>BODY TEMPERATURE:TEMP:ENCTRFIRST:PAITENT:QN     </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>98.6 </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6>\n"+
            "            <OBX.6.1/>\n"+
            "            <OBX.6.2>[degF] </OBX.6.2>\n"+
            "            <OBX.6.3>UCUM </OBX.6.3>\n"+
            "        </OBX.6>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>10 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>NM </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>59408-5 </OBX.3.1>\n"+
            "            <OBX.3.2>OXYGEN SATURATION:MFR:PT:BLDA:QN:PULSE OXIMETRY     </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>95 </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6>\n"+
            "            <OBX.6.1>% </OBX.6.1>\n"+
            "            <OBX.6.2> </OBX.6.2>\n"+
            "            <OBX.6.3>UCUM </OBX.6.3>\n"+
            "        </OBX.6>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <DG1>\n"+
            "        <DG1.1>\n"+
            "            <DG1.1.1>1 </DG1.1.1>\n"+
            "        </DG1.1>\n"+
            "        <DG1.2/>\n"+
            "        <DG1.3>\n"+
            "            <DG1.3.1>4414 </DG1.3.1>\n"+
            "        </DG1.3>\n"+
            "        <DG1.4/>\n"+
            "        <DG1.5/>\n"+
            "        <DG1.6>\n"+
            "            <DG1.6.1>W </DG1.6.1>\n"+
            "        </DG1.6>\n"+
            "    </DG1>\n"+
            "</HL7Message>";

    public static String  hl7TestData_2 = "MSH|^~\\&||MID-CO HLTH CTR^9876543210^N|||201110090314||ADT^A01^ADT_A01|AllData251|P|2.5.1\n" +
            "EVN||201110090314|||||TestAllData2.5.1^1234567890^NPI|\n"                                                                                         +
            "PID|1||95101100001^^^^PI~MR01234567^^^^MR||~^^^^^^U|||M||2106-3^White^CDCREC|^^Fairfax^24^21502^USA|||||||||||2135-2^Hispanic or Latino^CDCREC\n"      +
            "PV1||I||E||||||||||6|||||VN101100001^^^^VN|||||||||||||||||04||||||||20111009025915|20110113164512\n"                                        +
            "OBX|1|NM|21612-7^AGE TIME PATIENT REPORTED^^LN||30|a^YEAR^UCUM|||||F|||201102171531\n"                                                                 +
            "OBX|3|NM|11289-6^BODY TEMPERATURE:TEMP:ENCTRFIRST:PATIENT:QN^LN||100.1|[degF]^FARENHEIT^UCUM||A|||F|||20110217145139\n"                                +
            "OBX|4|NM|59408-5^OXYGEN SATURATION:MFR:PT:BLDA:QN:PULSE||95|%^PERCENT^UCUM||A|||F|||201102171658\n"                                                    +
            "OBX|5|CWE|8661-1^CHIEF COMPLAINT:FIND:PT:PATIENT:NOM:REPORTED^LN||^^^^^^^^STOMACH ACHE||||||F|||201102171531\n"                                        +
            "OBX|6|TS|11368-8^ILLNESS OR INJURY ONSET DATE AND TIME:TMSTP:PT:PATIENT:QN^LN||20110215||||||F|||201102171658\n"                                       +
            "OBX|7|CWE|SS003^FACILITY / VISIT TYPE^PHINQUESTION||1108-0^EMERGENCY DEPARTMENT^HSLOC||||||F|||201102091114\n"                                         +
            "OBX|8|XAD|SS002^TREATING FACILITY LOCATION^PHINQUESTION||^1234^^^^Chantilly^VA^30341^USA^C^^County||||||F|||201102091114\n"                            +
            "OBX|9|TX|54094-8^TRIAGE NOTE:FIND:PT:EMERGENCY DEPARTMENT:DOC^LN||Pain a recurrent cramping sensation.||||||F|||201102091114\n"                        +
            "OBX|10|TX|44833-2^DIAGNOSIS.PRELIMINARY:IMP:PT:PATIENT:NOM:^LN||Pain consist with appendicitis||||||F|||201102091114\n"                                +
            "DG1|1||E8809^FALL ON STAIR/STEP                                                                                                                        NEC^I9CDX|||A";

    public static String  hl7TestData_Conditional_data_present = "MSH|^~\\&||MID-CO HLTH CTR^9876543210^N|||201110090314||ADT^A01^ADT_A01|AllData251|P|2.5.1\n" +
            "EVN||201110090314|||||TestAllData2.5.1^1234567890^NPI|\n"                                                                                         +
            "PID|1||95101100001^^^^PI~MR01234567^^^^MR||~^^^^^^U|||M||2106-3^White^CDCREC|^^Fairfax^24^21502^USA|||||||||||2135-2^Hispanic or Latino^CDCREC\n"      +
            "PV1||I||E||||||||||6|||||VN101100001^^^^VN|||||||||||||||||04||||||||20111009025915|20110113164512\n"                                        +
            "OBX|1|NM|21612-7^AGE TIME PATIENT REPORTED^^LN||30|a^YEAR^UCUM|||||F|||201102171531\n"                                                                 +
            "OBX|3|NM|11289-6^BODY TEMPERATURE:TEMP:ENCTRFIRST:PATIENT:QN^LN||100.1|[degF]^FARENHEIT^UCUM||A|||F|||20110217145139\n"                                +
            "OBX|4|NM|59408-5^OXYGEN SATURATION:MFR:PT:BLDA:QN:PULSE||95|%^PERCENT^UCUM||A|||F|||201102171658\n"                                                    +
            "OBX|5|CWE|8661-1^CHIEF COMPLAINT:FIND:PT:PATIENT:NOM:REPORTED^LN||^^^^^^^^STOMACH ACHE||||||F|||201102171531\n"                                        +
            "OBX|6|TS|11368-8^ILLNESS OR INJURY ONSET DATE AND TIME:TMSTP:PT:PATIENT:QN^LN||20110215||||||F|||201102171658\n"                                       +
            "OBX|7|CWE|SS003^FACILITY / VISIT TYPE^PHINQUESTION||1108-0^EMERGENCY DEPARTMENT^HSLOC||||||F|||201102091114\n"                                         +
            "OBX|8|XAD|SS002^TREATING FACILITY LOCATION^PHINQUESTION||^1234^^^^Chantilly^VA^30341^USA^C^^County||||||F|||201102091114\n"                            +
            "OBX|9|TX|54094-8^TRIAGE NOTE:FIND:PT:EMERGENCY DEPARTMENT:DOC^LN||Pain a recurrent cramping sensation.||||||F|||201102091114\n"                        +
            "OBX|10|TX|44833-2^DIAGNOSIS.PRELIMINARY:IMP:PT:PATIENT:NOM:^LN||Pain consist with appendicitis||||||F|||201102091114\n"                                +
            "DG1|1||E8809^FALL ON STAIR/STEP                                                                                                                        NEC^I9CDX|||A";

    public static String  hl7TestData_Conditional_data_missing = "MSH|^~\\&||MID-CO HLTH CTR^9876543210^N|||201110090314||ADT^A01^ADT_A01|AllData251|P|2.5.1\n" +
            "EVN||201110090314|||||TestAllData2.5.1^1234567890^NPI|\n"                                                                                         +
            "PID|1||95101100001^^^^PI~MR01234567^^^^MR||~^^^^^^U|||M||2106-3^White^CDCREC|^^Fairfax^24^21502^USA|||||||||||2135-2^Hispanic or Latino^CDCREC\n"      +
            "PV1||I||E||||||||||6|||||VN101100001^^^^VN|||||||||||||||||04||||||||20111009025915|20110113164512\n"                                        +
            "OBX|1|NM|21612-7^AGE TIME PATIENT REPORTED^^LN||30|a^YEAR^UCUM|||||F|||201102171531\n"                                                                 +
            "OBX|3|NM|11289-6^BODY TEMPERATURE:TEMP:ENCTRFIRST:PATIENT:QN^LN||100.1|[degF]^FARENHEIT^UCUM||A|||F|||20110217145139\n"                                +
            "OBX|4|NM|59408-5^OXYGEN SATURATION:MFR:PT:BLDA:QN:PULSE||95|%^PERCENT^UCUM||A|||F|||201102171658\n"                                                    +
            "OBX|5|CWE|8661-1^CHIEF COMPLAINT:FIND:PT:PATIENT:NOM:REPORTED^LN||^^^^^^^^STOMACH ACHE||||||F|||201102171531\n"                                        +
            "OBX|6|TS|11368-8^ILLNESS OR INJURY ONSET DATE AND TIME:TMSTP:PT:PATIENT:QN^LN||20110215||||||F|||201102171658\n"                                       +
            "OBX|7|CWE|SS003^FACILITY / VISIT TYPE^PHINQUESTION||1108-0^EMERGENCY DEPARTMENT^HSLOC||||||F|||201102091114\n"                                         +
            "OBX|8|XAD|SS002^TREATING FACILITY LOCATION^PHINQUESTION||^1234^^^^Chantilly^^30341^USA^C^^County||||||F|||201102091114\n"                            +
            "OBX|9|TX|54094-8^TRIAGE NOTE:FIND:PT:EMERGENCY DEPARTMENT:DOC^LN||Pain a recurrent cramping sensation.||||||F|||201102091114\n"                        +
            "OBX|10|TX|44833-2^DIAGNOSIS.PRELIMINARY:IMP:PT:PATIENT:NOM:^LN||Pain consist with appendicitis||||||F|||201102091114\n"                                +
            "DG1|1||E8809^FALL ON STAIR/STEP                                                                                                                        NEC^I9CDX|||A";

    public static String  hl7TestData_with_valid_age_value = "MSH|^~\\&||MID-CO HLTH CTR^9876543210^N|||201110090314||ADT^A01^ADT_A01|AllData251|P|2.5.1\n" +
            "EVN||201110090314|||||TestAllData2.5.1^1234567890^NPI|\n"                                                                                         +
            "PID|1||95101100001^^^^PI~MR01234567^^^^MR||~^^^^^^U|||M||2106-3^White^CDCREC|^^Fairfax^24^21502^USA|||||||||||2135-2^Hispanic or Latino^CDCREC\n"      +
            "PV1||I||E||||||||||6|||||VN101100001^^^^VN|||||||||||||||||04||||||||20111009025915|20110113164512\n"                                        +
            "OBX|1|NM|21612-7^AGE TIME PATIENT REPORTED^^LN||18|a^YEAR^UCUM|||||F|||201102171531\n"                                                                 +
            "OBX|3|NM|11289-6^BODY TEMPERATURE:TEMP:ENCTRFIRST:PATIENT:QN^LN||100.1|[degF]^FARENHEIT^UCUM||A|||F|||20110217145139\n"                                +
            "OBX|4|NM|59408-5^OXYGEN SATURATION:MFR:PT:BLDA:QN:PULSE||95|%^PERCENT^UCUM||A|||F|||201102171658\n"                                                    +
            "OBX|5|CWE|8661-1^CHIEF COMPLAINT:FIND:PT:PATIENT:NOM:REPORTED^LN||^^^^^^^^STOMACH ACHE||||||F|||201102171531\n"                                        +
            "OBX|6|TS|11368-8^ILLNESS OR INJURY ONSET DATE AND TIME:TMSTP:PT:PATIENT:QN^LN||20110215||||||F|||201102171658\n"                                       +
            "OBX|7|CWE|SS003^FACILITY / VISIT TYPE^PHINQUESTION||1108-0^EMERGENCY DEPARTMENT^HSLOC||||||F|||201102091114\n"                                         +
            "OBX|8|XAD|SS002^TREATING FACILITY LOCATION^PHINQUESTION||^1234^^^^Chantilly^VA^30341^USA^C^^County||||||F|||201102091114\n"                            +
            "OBX|9|TX|54094-8^TRIAGE NOTE:FIND:PT:EMERGENCY DEPARTMENT:DOC^LN||Pain a recurrent cramping sensation.||||||F|||201102091114\n"                        +
            "OBX|10|TX|44833-2^DIAGNOSIS.PRELIMINARY:IMP:PT:PATIENT:NOM:^LN||Pain consist with appendicitis||||||F|||201102091114\n"                                +
            "DG1|1||E8809^FALL ON STAIR/STEP                                                                                                                        NEC^I9CDX|||A";

    public static String  hl7TestData_with_invalid_age_value = "MSH|^~\\&||MID-CO HLTH CTR^9876543210^N|||201110090314||ADT^A01^ADT_A01|AllData251|P|2.5.1\n" +
            "EVN||201110090314|||||TestAllData2.5.1^1234567890^NPI|\n"                                                                                         +
            "PID|1||95101100001^^^^PI~MR01234567^^^^MR||~^^^^^^U|||M||2106-3^White^CDCREC|^^Fairfax^24^21502^USA|||||||||||2135-2^Hispanic or Latino^CDCREC\n"      +
            "PV1||I||E||||||||||6|||||VN101100001^^^^VN|||||||||||||||||04||||||||20111009025915|20110113164512\n"                                        +
            "OBX|1|NM|21612-7^AGE TIME PATIENT REPORTED^^LN||30|a^YEAR^UCUM|||||F|||201102171531\n"                                                                 +
            "OBX|3|NM|11289-6^BODY TEMPERATURE:TEMP:ENCTRFIRST:PATIENT:QN^LN||100.1|[degF]^FARENHEIT^UCUM||A|||F|||20110217145139\n"                                +
            "OBX|4|NM|59408-5^OXYGEN SATURATION:MFR:PT:BLDA:QN:PULSE||95|%^PERCENT^UCUM||A|||F|||201102171658\n"                                                    +
            "OBX|5|CWE|8661-1^CHIEF COMPLAINT:FIND:PT:PATIENT:NOM:REPORTED^LN||^^^^^^^^STOMACH ACHE||||||F|||201102171531\n"                                        +
            "OBX|6|TS|11368-8^ILLNESS OR INJURY ONSET DATE AND TIME:TMSTP:PT:PATIENT:QN^LN||20110215||||||F|||201102171658\n"                                       +
            "OBX|7|CWE|SS003^FACILITY / VISIT TYPE^PHINQUESTION||1108-0^EMERGENCY DEPARTMENT^HSLOC||||||F|||201102091114\n"                                         +
            "OBX|8|XAD|SS002^TREATING FACILITY LOCATION^PHINQUESTION||^1234^^^^Chantilly^VA^30341^USA^C^^County||||||F|||201102091114\n"                            +
            "OBX|9|TX|54094-8^TRIAGE NOTE:FIND:PT:EMERGENCY DEPARTMENT:DOC^LN||Pain a recurrent cramping sensation.||||||F|||201102091114\n"                        +
            "OBX|10|TX|44833-2^DIAGNOSIS.PRELIMINARY:IMP:PT:PATIENT:NOM:^LN||Pain consist with appendicitis||||||F|||201102091114\n"                                +
            "DG1|1||E8809^FALL ON STAIR/STEP                                                                                                                        NEC^I9CDX|||A";


    public static String   hl7TestData_missing_data_in_optional_segment_segment_present =
                    "MSH|^~\\&|||||20111213131225||ADT^A01|MSG00001|P|2.3.1||||||UTF-8\n"                                                                                                                                                                                                                                                                                                                 +
                            "EVN||201205311112\n"                                                                                                                                                                                                                                                                                                                                             +
                            "PID|1||8905671234^^^^SS~5671234098^^^^PI~1234765890^^^^BA~999777555^^^^MR||paitent_name|||M||1002-5|^^Fairfax City^51^22333^USA^^^Fairfax|||||||||||2186-5\n"       +
                            "PV1||E|||||||||||||||||7651234098|||||||||||||||||09||||||||201104011406-0500|20110113164512-0500\n"                                                                                                                                                                                               +
                            "OBX|1|HD|SS001^Treating Facility Identifier^PHINQUESTION||Fairfax Hospital^1098765432^NP||||||C||||||\n"                                                                                                                                                                                                                                               +
                            "OBX|2|XAD|SS002^Treating Facility Location||^123 Gallows Rd.^^^^Fairfax City^51^30341^USA^C^^Fairfax||||||X\n"                                                                                                                                                                                               +
                            "OBX|3|CWE|SS003^Facility/Visit Type^PHINQUESTION||170300000X|||||||X\n"                                                                                                                                                                                                                                                                                     +
                            "OBX|4|NM|21612-7^AGE TIME PAITENT REPORTED^LN||42|a^a^UCUM|||||X\n"                                                                                                                                                                                                                                                                               +
                            "OBX|5|TS|11368-8^ILLNESS OR INJURY ONSET DATE AND TIME:TMSTP:PT:PAITENT:QN^LN||20120401||||||X\n"                                                                                                                                                                                                                                                              +
                            "OBX|6|CWE|8661-1^CHIEF COMPLAINT:FIND:PT:PAITENT:NOM:REPORTED^LN||^^^^^^^^Nausia and Dizzyness||||||X\n"                                                                                                                                                                                                                                                                 +
                            "OBX|7|TX|54094-8^TRIAGE NOTE:FIND:PT:EMERGENCY DEPARTMENT:DOC^LN||The paitent seemed dizzy and sick to his stomach.||||||X\n"                                                                                                                                                                                                                                                                            +
                            "OBX|8|TX|44833-2^DIAGNOSIS:PRELIMINARY:IMP:PT:PAITENT:NOM:^LN||The patient is suffering from an inner ear contdtition.||||||X\n"                                                                                                                                                                                                                                                                        +
                            "OBX|9|NM|11289-6^BODY TEMPERATURE:TEMP:ENCTRFIRST:PAITENT:QN^LN||98.6|^[degF]^UCUM|||||X\n"                                                                                                                                                                                                                                      +
                            "OBX|10|NM|59408-5^OXYGEN SATURATION:MFR:PT:BLDA:QN:PULSE OXIMETRY^LN||95|^|%^^UCUM||||X\n"                                                                                                                                                                                                                                        +
                            "DG1|2|||||";

    public static String    xmlData_missing_data_in_optional_segment_segment_present = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
            "<HL7Message>\n"+
            "    <MSH>\n"+
            "        <MSH.1>| </MSH.1>\n"+
            "        <MSH.2>^~\\\\ </MSH.2>\n"+
            "        <MSH.3/>\n"+
            "        <MSH.4/>\n"+
            "        <MSH.5/>\n"+
            "        <MSH.6/>\n"+
            "        <MSH.7>\n"+
            "            <MSH.7.1>20111213131225 </MSH.7.1>\n"+
            "        </MSH.7>\n"+
            "        <MSH.8/>\n"+
            "        <MSH.9>\n"+
            "            <MSH.9.1>ADT </MSH.9.1>\n"+
            "            <MSH.9.2>A01 </MSH.9.2>\n"+
            "        </MSH.9>\n"+
            "        <MSH.10>\n"+
            "            <MSH.10.1>MSG00001 </MSH.10.1>\n"+
            "        </MSH.10>\n"+
            "        <MSH.11>\n"+
            "            <MSH.11.1>P </MSH.11.1>\n"+
            "        </MSH.11>\n"+
            "        <MSH.12>\n"+
            "            <MSH.12.1>2.3.1 </MSH.12.1>\n"+
            "        </MSH.12>\n"+
            "        <MSH.13/>\n"+
            "        <MSH.14/>\n"+
            "        <MSH.15/>\n"+
            "        <MSH.16/>\n"+
            "        <MSH.17/>\n"+
            "        <MSH.18>\n"+
            "            <MSH.18.1>UTF-8 </MSH.18.1>\n"+
            "        </MSH.18>\n"+
            "    </MSH>\n"+
            "    <EVN>\n"+
            "        <EVN.1/>\n"+
            "        <EVN.2>\n"+
            "            <EVN.2.1>201205311112 </EVN.2.1>\n"+
            "        </EVN.2>\n"+
            "    </EVN>\n"+
            "    <PID>\n"+
            "        <PID.1>\n"+
            "            <PID.1.1>1 </PID.1.1>\n"+
            "        </PID.1>\n"+
            "        <PID.2/>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>8905671234 </PID.3.1>\n"+
            "            <PID.3.2/>\n"+
            "            <PID.3.3/>\n"+
            "            <PID.3.4/>\n"+
            "            <PID.3.5>SS </PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>5671234098 </PID.3.1>\n"+
            "            <PID.3.2/>\n"+
            "            <PID.3.3/>\n"+
            "            <PID.3.4/>\n"+
            "            <PID.3.5>PI </PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>1234765890 </PID.3.1>\n"+
            "            <PID.3.2/>\n"+
            "            <PID.3.3/>\n"+
            "            <PID.3.4/>\n"+
            "            <PID.3.5>BA </PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>999777555 </PID.3.1>\n"+
            "            <PID.3.2></PID.3.2>\n"+
            "            <PID.3.3></PID.3.3>\n"+
            "            <PID.3.4></PID.3.4>\n"+
            "            <PID.3.5>MR</PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.4/>\n"+
            "        <PID.5>\n"+
            "            <PID.5.1>paitent_name </PID.5.1>\n"+
            "        </PID.5>\n"+
            "        <PID.6/>\n"+
            "        <PID.7/>\n"+
            "        <PID.8>\n"+
            "            <PID.8.1>M </PID.8.1>\n"+
            "        </PID.8>\n"+
            "        <PID.9/>\n"+
            "        <PID.10>\n"+
            "            <PID.10.1>1002-5 </PID.10.1>\n"+
            "        </PID.10>\n"+
            "        <PID.11>\n"+
            "            <PID.11.1/>\n"+
            "            <PID.11.2/>\n"+
            "            <PID.11.3>Fairfax City </PID.11.3>\n"+
            "            <PID.11.4>51 </PID.11.4>\n"+
                "            <PID.11.5>22333 </PID.11.5>\n"+
            "            <PID.11.6>USA </PID.11.6>\n"+
            "            <PID.11.7/>\n"+
            "            <PID.11.8/>\n"+
            "            <PID.11.9>Fairfax </PID.11.9>\n"+
            "        </PID.11>\n"+
            "        <PID.12/>\n"+
            "        <PID.13/>\n"+
            "        <PID.14/>\n"+
            "        <PID.15/>\n"+
            "        <PID.16/>\n"+
            "        <PID.17/>\n"+
            "        <PID.18/>\n"+
            "        <PID.19/>\n"+
            "        <PID.20/>\n"+
            "        <PID.21/>\n"+
            "        <PID.22>\n"+
            "            <PID.22.1>2186-5 </PID.22.1>\n"+
            "        </PID.22>\n"+
            "    </PID>\n"+
            "    <PV1>\n"+
            "        <PV1.1/>\n"+
            "        <PV1.2>\n"+
            "            <PV1.2.1>E </PV1.2.1>\n"+
            "        </PV1.2>\n"+
            "        <PV1.3/>\n"+
            "        <PV1.4/>\n"+
            "        <PV1.5/>\n"+
            "        <PV1.6/>\n"+
            "        <PV1.7/>\n"+
            "        <PV1.8/>\n"+
            "        <PV1.9/>\n"+
            "        <PV1.10/>\n"+
            "        <PV1.11/>\n"+
            "        <PV1.12/>\n"+
            "        <PV1.13/>\n"+
            "        <PV1.14/>\n"+
            "        <PV1.15/>\n"+
            "        <PV1.16/>\n"+
            "        <PV1.17/>\n"+
            "        <PV1.18/>\n"+
            "        <PV1.19>\n"+
            "            <PV1.19.1>7651234098 </PV1.19.1>\n"+
            "        </PV1.19>\n"+
            "        <PV1.20/>\n"+
            "        <PV1.21/>\n"+
            "        <PV1.22/>\n"+
            "        <PV1.23/>\n"+
            "        <PV1.24/>\n"+
            "        <PV1.25/>\n"+
            "        <PV1.26/>\n"+
            "        <PV1.27/>\n"+
            "        <PV1.28/>\n"+
            "        <PV1.29/>\n"+
            "        <PV1.30/>\n"+
            "        <PV1.31/>\n"+
            "        <PV1.32/>\n"+
            "        <PV1.33/>\n"+
            "        <PV1.34/>\n"+
            "        <PV1.35/>\n"+
            "        <PV1.36>\n"+
            "            <PV1.36.1>09 </PV1.36.1>\n"+
            "        </PV1.36>\n"+
            "        <PV1.37/>\n"+
            "        <PV1.38/>\n"+
            "        <PV1.39/>\n"+
            "        <PV1.40/>\n"+
            "        <PV1.41/>\n"+
            "        <PV1.42/>\n"+
            "        <PV1.43/>\n"+
            "        <PV1.44>\n"+
            "            <PV1.44.1>201104011406-0500 </PV1.44.1>\n"+
            "        </PV1.44>\n"+
            "        <PV1.45>\n"+
            "            <PV1.45.1>20110113164512-0500 </PV1.45.1>\n"+
            "        </PV1.45>\n"+
            "    </PV1>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>1 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>HD </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>SS001 </OBX.3.1>\n"+
            "            <OBX.3.2>Treating Facility Identifier      </OBX.3.2>\n"+
            "            <OBX.3.3>PHINQUESTION </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>Fairfax Hospital </OBX.5.1>\n"+
            "            <OBX.5.2>1098765432 </OBX.5.2>\n"+
            "            <OBX.5.3>NP </OBX.5.3>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>C </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "        <OBX.12/>\n"+
            "        <OBX.13/>\n"+
            "        <OBX.14/>\n"+
            "        <OBX.15/>\n"+
            "        <OBX.16/>\n"+
            "        <OBX.17/>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>2 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>XAD </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>SS002 </OBX.3.1>\n"+
            "            <OBX.3.2>Treating Facility Location    </OBX.3.2>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1/>\n"+
            "            <OBX.5.2>123 Gallows Rd. </OBX.5.2>\n"+
            "            <OBX.5.3/>\n"+
            "            <OBX.5.4/>\n"+
            "            <OBX.5.5/>\n"+
            "            <OBX.5.6>Fairfax City </OBX.5.6>\n"+
            "            <OBX.5.7>51 </OBX.5.7>\n"+
            "            <OBX.5.8>30341 </OBX.5.8>\n"+
            "            <OBX.5.9>USA </OBX.5.9>\n"+
            "            <OBX.5.10>C </OBX.5.10>\n"+
            "            <OBX.5.11/>\n"+
            "            <OBX.5.12>Fairfax </OBX.5.12>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>3 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>CWE </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>SS003 </OBX.3.1>\n"+
            "            <OBX.3.2>Facility/Visit Type     </OBX.3.2>\n"+
            "            <OBX.3.3>PHINQUESTION </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1></OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9>170300000X </OBX.9>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11/>\n"+
            "        <OBX.12>\n"+
            "            <OBX.12.1>X </OBX.12.1>\n"+
            "        </OBX.12>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>4 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>NM </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>21612-7 </OBX.3.1>\n"+
            "            <OBX.3.2>AGE TIME PAITENT REPORTED       </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>42 </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6>\n"+
            "            <OBX.6.1>a </OBX.6.1>\n"+
            "            <OBX.6.2>a </OBX.6.2>\n"+
            "            <OBX.6.3>UCUM </OBX.6.3>\n"+
            "        </OBX.6>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>5 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>TS </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>11368-8 </OBX.3.1>\n"+
            "            <OBX.3.2>ILLNESS OR INJURY ONSET DATE AND TIME:TMSTP:PT:PAITENT:QN       </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>20120401 </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>6 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>CWE </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>8661-1 </OBX.3.1>\n"+
            "            <OBX.3.2>CHIEF COMPLAINT:FIND:PT:PAITENT:NOM:REPORTED      </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1/>\n"+
            "            <OBX.5.2/>\n"+
            "            <OBX.5.3/>\n"+
            "            <OBX.5.4/>\n"+
            "            <OBX.5.5/>\n"+
            "            <OBX.5.6/>\n"+
            "            <OBX.5.7/>\n"+
            "            <OBX.5.8/>\n"+
            "            <OBX.5.9>Nausia and Dizzyness </OBX.5.9>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>7 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>TX </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>54094-8 </OBX.3.1>\n"+
            "            <OBX.3.2>TRIAGE NOTE:FIND:PT:EMERGENCY DEPARTMENT:DOC      </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>The paitent seemed dizzy and sick to his stomach. </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>8 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>TX </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>44833-2 </OBX.3.1>\n"+
            "            <OBX.3.2>DIAGNOSIS:PRELIMINARY:IMP:PT:PAITENT:NOM: </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>The patient is suffering from an inner ear contdtition. </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>9 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>NM </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>11289-6 </OBX.3.1>\n"+
            "            <OBX.3.2>BODY TEMPERATURE:TEMP:ENCTRFIRST:PAITENT:QN     </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>98.6 </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6>\n"+
            "            <OBX.6.1/>\n"+
            "            <OBX.6.2>[degF] </OBX.6.2>\n"+
            "            <OBX.6.3>UCUM </OBX.6.3>\n"+
            "        </OBX.6>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>10 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>NM </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>59408-5 </OBX.3.1>\n"+
            "            <OBX.3.2>OXYGEN SATURATION:MFR:PT:BLDA:QN:PULSE OXIMETRY     </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>95 </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6>\n"+
            "            <OBX.6.1>% </OBX.6.1>\n"+
            "            <OBX.6.2> </OBX.6.2>\n"+
            "            <OBX.6.3>UCUM </OBX.6.3>\n"+
            "        </OBX.6>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <DG1>\n"+
            "        <DG1.1>\n"+
            "            <DG1.1.1>1 </DG1.1.1>\n"+
            "        </DG1.1>\n"+
            "        <DG1.2/>\n"+
            "        <DG1.3>\n"+
            "            <DG1.3.1></DG1.3.1>\n"+
            "        </DG1.3>\n"+
            "        <DG1.4/>\n"+
            "        <DG1.5/>\n"+
            "        <DG1.6>\n"+
            "            <DG1.6.1></DG1.6.1>\n"+
            "        </DG1.6>\n"+
            "    </DG1>\n"+
            "</HL7Message>";

    public static String   hl7TestData_missing_data_in_optional_segment_segment_missing =
                    "MSH|^~\\&|||||20111213131225||ADT^A01|MSG00001|P|2.3.1||||||UTF-8\n"                                                                                                                                                                                                                                                                                                                 +
                            "EVN||201205311112\n"                                                                                                                                                                                                                                                                                                                                             +
                            "PID|1||8905671234^^^^SS~5671234098^^^^PI~1234765890^^^^BA~999777555^^^^MR||paitent_name|||M||1002-5|^^Fairfax City^51^22333^USA^^^Fairfax|||||||||||2186-5\n"       +
                            "PV1||E|||||||||||||||||7651234098|||||||||||||||||09||||||||201104011406-0500|20110113164512-0500\n"                                                                                                                                                                                               +
                            "OBX|1|HD|SS001^Treating Facility Identifier^PHINQUESTION||Fairfax Hospital^1098765432^NP||||||C||||||\n"                                                                                                                                                                                                                                               +
                            "OBX|2|XAD|SS002^Treating Facility Location||^123 Gallows Rd.^^^^Fairfax City^51^30341^USA^C^^Fairfax||||||X\n"                                                                                                                                                                                               +
                            "OBX|3|CWE|SS003^Facility/Visit Type^PHINQUESTION||170300000X|||||||X\n"                                                                                                                                                                                                                                                                                     +
                            "OBX|4|NM|21612-7^AGE TIME PAITENT REPORTED^LN||42|a^a^UCUM|||||X\n"                                                                                                                                                                                                                                                                               +
                            "OBX|5|TS|11368-8^ILLNESS OR INJURY ONSET DATE AND TIME:TMSTP:PT:PAITENT:QN^LN||20120401||||||X\n"                                                                                                                                                                                                                                                              +
                            "OBX|6|CWE|8661-1^CHIEF COMPLAINT:FIND:PT:PAITENT:NOM:REPORTED^LN||^^^^^^^^Nausia and Dizzyness||||||X\n"                                                                                                                                                                                                                                                                 +
                            "OBX|7|TX|54094-8^TRIAGE NOTE:FIND:PT:EMERGENCY DEPARTMENT:DOC^LN||The paitent seemed dizzy and sick to his stomach.||||||X\n"                                                                                                                                                                                                                                                                            +
                            "OBX|8|TX|44833-2^DIAGNOSIS:PRELIMINARY:IMP:PT:PAITENT:NOM:^LN||The patient is suffering from an inner ear contdtition.||||||X\n"                                                                                                                                                                                                                                                                        +
                            "OBX|9|NM|11289-6^BODY TEMPERATURE:TEMP:ENCTRFIRST:PAITENT:QN^LN||98.6|^[degF]^UCUM|||||X\n"                                                                                                                                                                                                                                      +
                            "OBX|10|NM|59408-5^OXYGEN SATURATION:MFR:PT:BLDA:QN:PULSE OXIMETRY^LN||95|%^^UCUM|||||X";

    public static String    xmlData_missing_data_in_optional_segment_segment_missing = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
            "<HL7Message>\n"+
            "    <MSH>\n"+
            "        <MSH.1>| </MSH.1>\n"+
            "        <MSH.2>^~\\\\ </MSH.2>\n"+
            "        <MSH.3/>\n"+
            "        <MSH.4/>\n"+
            "        <MSH.5/>\n"+
            "        <MSH.6/>\n"+
            "        <MSH.7>\n"+
            "            <MSH.7.1>20111213131225 </MSH.7.1>\n"+
            "        </MSH.7>\n"+
            "        <MSH.8/>\n"+
            "        <MSH.9>\n"+
            "            <MSH.9.1>ADT </MSH.9.1>\n"+
            "            <MSH.9.2>A01 </MSH.9.2>\n"+
            "        </MSH.9>\n"+
            "        <MSH.10>\n"+
            "            <MSH.10.1>MSG00001 </MSH.10.1>\n"+
            "        </MSH.10>\n"+
            "        <MSH.11>\n"+
            "            <MSH.11.1>P </MSH.11.1>\n"+
            "        </MSH.11>\n"+
            "        <MSH.12>\n"+
            "            <MSH.12.1>2.3.1 </MSH.12.1>\n"+
            "        </MSH.12>\n"+
            "        <MSH.13/>\n"+
            "        <MSH.14/>\n"+
            "        <MSH.15/>\n"+
            "        <MSH.16/>\n"+
            "        <MSH.17/>\n"+
            "        <MSH.18>\n"+
            "            <MSH.18.1>UTF-8 </MSH.18.1>\n"+
            "        </MSH.18>\n"+
            "    </MSH>\n"+
            "    <EVN>\n"+
            "        <EVN.1/>\n"+
            "        <EVN.2>\n"+
            "            <EVN.2.1>201205311112 </EVN.2.1>\n"+
            "        </EVN.2>\n"+
            "    </EVN>\n"+
            "    <PID>\n"+
            "        <PID.1>\n"+
            "            <PID.1.1>1 </PID.1.1>\n"+
            "        </PID.1>\n"+
            "        <PID.2/>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>8905671234 </PID.3.1>\n"+
            "            <PID.3.2/>\n"+
            "            <PID.3.3/>\n"+
            "            <PID.3.4/>\n"+
            "            <PID.3.5>SS </PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>5671234098 </PID.3.1>\n"+
            "            <PID.3.2/>\n"+
            "            <PID.3.3/>\n"+
            "            <PID.3.4/>\n"+
            "            <PID.3.5>PI </PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>1234765890 </PID.3.1>\n"+
            "            <PID.3.2/>\n"+
            "            <PID.3.3/>\n"+
            "            <PID.3.4/>\n"+
            "            <PID.3.5>BA </PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>999777555 </PID.3.1>\n"+
            "            <PID.3.2></PID.3.2>\n"+
            "            <PID.3.3></PID.3.3>\n"+
            "            <PID.3.4></PID.3.4>\n"+
            "            <PID.3.5>MR</PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.4/>\n"+
            "        <PID.5>\n"+
            "            <PID.5.1>paitent_name </PID.5.1>\n"+
            "        </PID.5>\n"+
            "        <PID.6/>\n"+
            "        <PID.7/>\n"+
            "        <PID.8>\n"+
            "            <PID.8.1>M </PID.8.1>\n"+
            "        </PID.8>\n"+
            "        <PID.9/>\n"+
            "        <PID.10>\n"+
            "            <PID.10.1>1002-5 </PID.10.1>\n"+
            "        </PID.10>\n"+
            "        <PID.11>\n"+
            "            <PID.11.1/>\n"+
            "            <PID.11.2/>\n"+
            "            <PID.11.3>Fairfax City </PID.11.3>\n"+
            "            <PID.11.4>51 </PID.11.4>\n"+
            "            <PID.11.5>22333 </PID.11.5>\n"+
            "            <PID.11.6>USA </PID.11.6>\n"+
            "            <PID.11.7/>\n"+
            "            <PID.11.8/>\n"+
            "            <PID.11.9>Fairfax </PID.11.9>\n"+
            "        </PID.11>\n"+
            "        <PID.12/>\n"+
            "        <PID.13/>\n"+
            "        <PID.14/>\n"+
            "        <PID.15/>\n"+
            "        <PID.16/>\n"+
            "        <PID.17/>\n"+
            "        <PID.18/>\n"+
            "        <PID.19/>\n"+
            "        <PID.20/>\n"+
            "        <PID.21/>\n"+
            "        <PID.22>\n"+
            "            <PID.22.1>2186-5 </PID.22.1>\n"+
            "        </PID.22>\n"+
            "    </PID>\n"+
            "    <PV1>\n"+
            "        <PV1.1/>\n"+
            "        <PV1.2>\n"+
            "            <PV1.2.1>E </PV1.2.1>\n"+
            "        </PV1.2>\n"+
            "        <PV1.3/>\n"+
            "        <PV1.4/>\n"+
            "        <PV1.5/>\n"+
            "        <PV1.6/>\n"+
            "        <PV1.7/>\n"+
            "        <PV1.8/>\n"+
            "        <PV1.9/>\n"+
            "        <PV1.10/>\n"+
            "        <PV1.11/>\n"+
            "        <PV1.12/>\n"+
            "        <PV1.13/>\n"+
            "        <PV1.14/>\n"+
            "        <PV1.15/>\n"+
            "        <PV1.16/>\n"+
            "        <PV1.17/>\n"+
            "        <PV1.18/>\n"+
            "        <PV1.19>\n"+
            "            <PV1.19.1>7651234098 </PV1.19.1>\n"+
            "        </PV1.19>\n"+
            "        <PV1.20/>\n"+
            "        <PV1.21/>\n"+
            "        <PV1.22/>\n"+
            "        <PV1.23/>\n"+
            "        <PV1.24/>\n"+
            "        <PV1.25/>\n"+
            "        <PV1.26/>\n"+
            "        <PV1.27/>\n"+
            "        <PV1.28/>\n"+
            "        <PV1.29/>\n"+
            "        <PV1.30/>\n"+
            "        <PV1.31/>\n"+
            "        <PV1.32/>\n"+
            "        <PV1.33/>\n"+
            "        <PV1.34/>\n"+
            "        <PV1.35/>\n"+
            "        <PV1.36>\n"+
            "            <PV1.36.1>09 </PV1.36.1>\n"+
            "        </PV1.36>\n"+
            "        <PV1.37/>\n"+
            "        <PV1.38/>\n"+
            "        <PV1.39/>\n"+
            "        <PV1.40/>\n"+
            "        <PV1.41/>\n"+
            "        <PV1.42/>\n"+
            "        <PV1.43/>\n"+
            "        <PV1.44>\n"+
            "            <PV1.44.1>201104011406-0500 </PV1.44.1>\n"+
            "        </PV1.44>\n"+
            "        <PV1.45>\n"+
            "            <PV1.45.1>20110113164512-0500 </PV1.45.1>\n"+
            "        </PV1.45>\n"+
            "    </PV1>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>1 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>HD </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>SS001 </OBX.3.1>\n"+
            "            <OBX.3.2>Treating Facility Identifier      </OBX.3.2>\n"+
            "            <OBX.3.3>PHINQUESTION </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>Fairfax Hospital </OBX.5.1>\n"+
            "            <OBX.5.2>1098765432 </OBX.5.2>\n"+
            "            <OBX.5.3>NP </OBX.5.3>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>C </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "        <OBX.12/>\n"+
            "        <OBX.13/>\n"+
            "        <OBX.14/>\n"+
            "        <OBX.15/>\n"+
            "        <OBX.16/>\n"+
            "        <OBX.17/>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>2 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>XAD </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>SS002 </OBX.3.1>\n"+
            "            <OBX.3.2>Treating Facility Location    </OBX.3.2>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1/>\n"+
            "            <OBX.5.2>123 Gallows Rd. </OBX.5.2>\n"+
            "            <OBX.5.3/>\n"+
            "            <OBX.5.4/>\n"+
            "            <OBX.5.5/>\n"+
            "            <OBX.5.6>Fairfax City </OBX.5.6>\n"+
            "            <OBX.5.7>51 </OBX.5.7>\n"+
            "            <OBX.5.8>30341 </OBX.5.8>\n"+
            "            <OBX.5.9>USA </OBX.5.9>\n"+
            "            <OBX.5.10>C </OBX.5.10>\n"+
            "            <OBX.5.11/>\n"+
            "            <OBX.5.12>Fairfax </OBX.5.12>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>3 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>CWE </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>SS003 </OBX.3.1>\n"+
            "            <OBX.3.2>Facility/Visit Type     </OBX.3.2>\n"+
            "            <OBX.3.3>PHINQUESTION </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1></OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9>170300000X </OBX.9>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11/>\n"+
            "        <OBX.12>\n"+
            "            <OBX.12.1>X </OBX.12.1>\n"+
            "        </OBX.12>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>4 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>NM </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>21612-7 </OBX.3.1>\n"+
            "            <OBX.3.2>AGE TIME PAITENT REPORTED       </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>42 </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6>\n"+
            "            <OBX.6.1>a </OBX.6.1>\n"+
            "            <OBX.6.2>a </OBX.6.2>\n"+
            "            <OBX.6.3>UCUM </OBX.6.3>\n"+
            "        </OBX.6>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>5 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>TS </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>11368-8 </OBX.3.1>\n"+
            "            <OBX.3.2>ILLNESS OR INJURY ONSET DATE AND TIME:TMSTP:PT:PAITENT:QN       </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>20120401 </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>6 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>CWE </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>8661-1 </OBX.3.1>\n"+
            "            <OBX.3.2>CHIEF COMPLAINT:FIND:PT:PAITENT:NOM:REPORTED      </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1/>\n"+
            "            <OBX.5.2/>\n"+
            "            <OBX.5.3/>\n"+
            "            <OBX.5.4/>\n"+
            "            <OBX.5.5/>\n"+
            "            <OBX.5.6/>\n"+
            "            <OBX.5.7/>\n"+
            "            <OBX.5.8/>\n"+
            "            <OBX.5.9>Nausia and Dizzyness </OBX.5.9>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>7 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>TX </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>54094-8 </OBX.3.1>\n"+
            "            <OBX.3.2>TRIAGE NOTE:FIND:PT:EMERGENCY DEPARTMENT:DOC      </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>The paitent seemed dizzy and sick to his stomach. </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>8 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>TX </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>44833-2 </OBX.3.1>\n"+
            "            <OBX.3.2>DIAGNOSIS:PRELIMINARY:IMP:PT:PAITENT:NOM: </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>The patient is suffering from an inner ear contdtition. </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>9 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>NM </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>11289-6 </OBX.3.1>\n"+
            "            <OBX.3.2>BODY TEMPERATURE:TEMP:ENCTRFIRST:PAITENT:QN     </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>98.6 </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6>\n"+
            "            <OBX.6.1/>\n"+
            "            <OBX.6.2>[degF] </OBX.6.2>\n"+
            "            <OBX.6.3>UCUM </OBX.6.3>\n"+
            "        </OBX.6>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "    <OBX>\n"+
            "        <OBX.1>\n"+
            "            <OBX.1.1>10 </OBX.1.1>\n"+
            "        </OBX.1>\n"+
            "        <OBX.2>\n"+
            "            <OBX.2.1>NM </OBX.2.1>\n"+
            "        </OBX.2>\n"+
            "        <OBX.3>\n"+
            "            <OBX.3.1>59408-5 </OBX.3.1>\n"+
            "            <OBX.3.2>OXYGEN SATURATION:MFR:PT:BLDA:QN:PULSE OXIMETRY     </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>95 </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6>\n"+
            "            <OBX.6.1>%</OBX.6.1>\n"+
            "            <OBX.6.2> </OBX.6.2>\n"+
            "            <OBX.6.2>UCUM </OBX.6.2>\n"+
            "        </OBX.6>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
            "        <OBX.10/>\n"+
            "        <OBX.11>\n"+
            "            <OBX.11.1>X </OBX.11.1>\n"+
            "        </OBX.11>\n"+
            "    </OBX>\n"+
            "</HL7Message>";


    public static final String      expectedSubFieldSeparator = "^";
    public static final String      expectedFieldRepititionSeparator = "~";

    public static final String      pipe_result_1 = "MSG00001";
    public static final String      xml_result_1 = "MSG00001";
    public static final String      pipe_result_2 = "E";
    public static final String      xml_result_2 = "E";
    public static final String      pipe_result_3 = "";
    public static final String      xml_result_3 = "";
    public static final String      pipe_result_4 = "7651234098";
    public static final String      xml_result_4 = "7651234098";
    public static final String      pipe_result_5 = "95";
    public static final String      xml_result_5 = "95";
    public static final String      pipe_result_6 = "Nausia and Dizzyness";
    public static final String      xml_result_6 = "Nausia and Dizzyness";
    public static final String      pipe_result_7 = "5671234098";
    public static final String      xml_result_7 = "5671234098";
    public static final String      pipe_result_8 = "78961";
    public static final String      xml_result_8 = "78961";
    public static final String      pipe_result_9 = "";
    public static final String      xml_result_9 = "";
    public static final String      pipe_result_10 = "22333";
    public static final String      xml_result_10 = "22333";
    public static final String      pipe_result_11 = "A01";
    public static final String      xml_result_11 = "A01";

    public static final String      facility_identifier_2_3_1 = "1098765432";
    public static final String      facility_identifier_2_5_1 = "1234567890";

    public static final String      medical_record_number_hl7_1 = "MR01234567";
    public static final String      medical_record_number_xml_1 = "999777555";

    @Before
    public void setup() {

    }

    @After
    public void teardown() {

    }

    @Test
    public void testSeparatorValues() throws IOException {
        PipeParser      pipeParser = new PipeParser();
        XmlParser       xmlParser = new XmlParser();

        pipeParser.loadData(hl7TestData_1);

        assertEquals("Did not find expected sub-field separator.", expectedSubFieldSeparator,
                     pipeParser.getSubFieldSeparator());

        assertEquals("Did not find expected field repetition separator.", expectedFieldRepititionSeparator,
                     pipeParser.getRepeatingFieldSeparator());
    }

    @Test
    public void testSimpleGetFieldValue() throws IOException {
        PipeParser      pipeParser = new PipeParser();
        XmlParser       xmlParser = new XmlParser();
        String          value;

        pipeParser.loadData(hl7TestData_1);
        xmlParser.loadData(xmlData_1);

        value = pipeParser.getFieldValue("MSH", "10");
        assertEquals("[PD] Extracting MSH-10 failed.", pipe_result_1, value);

        value = xmlParser.getFieldValue("MSH", "10");
        assertEquals("[XML] Extracting MSH-10 failed.", xml_result_1, value);

        value = pipeParser.getFieldValue("PV1", "9");
        assertEquals("[PD] Extracting PV1-9 failed.", pipe_result_3, value);

        value = xmlParser.getFieldValue("PV1", "9");
        assertEquals("[XML] Extracting PV1-9 failed.", xml_result_3, value);

        value = pipeParser.getFieldValue("PV1", "19");
        assertEquals("[PD] Extracting PV1-19 failed.", pipe_result_4, value);

        value = xmlParser.getFieldValue("PV1", "19");
        assertEquals("[XML] Extracting PV1-19 failed.", xml_result_4, value);

        value = pipeParser.getFieldValue("PID", "11.5");
        assertEquals("[PD] Extracting PID-11.5 failed.", pipe_result_10, value);

        value = xmlParser.getFieldValue("PID", "11.5");
        assertEquals("[XML] Extracting PID-11.5 failed.", xml_result_10, value);

        value = pipeParser.getFieldValue("MSH", "9.2");
        assertEquals("[PD] Extracting MSH-9.2 failed.", pipe_result_11, value);

        value = xmlParser.getFieldValue("MSH", "9.2");
        assertEquals("[XML] Extracting MSH-9.2 failed.", xml_result_11, value);
    }

    @Test
    public void testGetSubFieldValue() throws IOException {
        PipeParser      pipeParser = new PipeParser();
        XmlParser       xmlParser = new XmlParser();
        String          value;

        pipeParser.loadData(hl7TestData_1);
        xmlParser.loadData(xmlData_1);

        assertEquals("Did not find expected sub-field separator.", expectedSubFieldSeparator,
                     pipeParser.getSubFieldSeparator());

        value = pipeParser.getFieldValue("DG1", "3.1");
        assertEquals("[PD] Extracting DG1-3.1 failed.", pipe_result_8, value);

        value = xmlParser.getFieldValue("DG1", "3.1");
        assertEquals("[XML] Extracting DG1-3.1 failed.", xml_result_8, value);

        value = pipeParser.getFieldValue("PV1", "2.1");
        assertEquals("[PD] Extracting PV1-2.1 failed.", pipe_result_2, value);


        value = xmlParser.getFieldValue("PV1", "2.1");
        assertEquals("[XML] Extracting PV1-2.1 failed.", xml_result_2, value);

        value = pipeParser.getFieldValue("DG1", "3.2");
        assertEquals("[PD] Extracting DG1-3.2 failed.", pipe_result_9, value);

        value = xmlParser.getFieldValue("DG1", "3.2");
        assertEquals("[XML] Extracting DG1-3.2 failed.", xml_result_9, value);
    }

    @Test
    public void testConditionalAbsoluteGetSubFieldValue() throws IOException {
        PipeParser              pipeParser = new PipeParser();
        XmlParser               xmlParser = new XmlParser();
        String                  value;
        Element                 field;
        Element                 location;
        Element                 identifier;

        pipeParser.loadData(hl7TestData_1);
        xmlParser.loadData(xmlData_1);

        //
        // Test to see that we can extract data from the correct OBX segment.
        //
        field = new Element(XMLDefs.FIELD);
        location = new Element(XMLDefs.LOCATION).addContent(new Element(XMLDefs.HL7_SEGMENT));
        field.addContent(location);

        identifier = new Element(XMLDefs.IDENTIFIER);
        identifier.setAttribute(XMLDefs.FIELD_NUMBER, "3.1");
        identifier.setText("59408-5");

        location.addContent(identifier);

        value = pipeParser.getFieldValue("OBX", location, "5");
        assertEquals("[PD] Extracting OBX(59408-5)-5 failed.", pipe_result_5, value);

        value = xmlParser.getFieldValue("OBX", location, "5");
        assertEquals("[XML] Extracting OBX(59408-5)-5 failed.", xml_result_5, value);


        location.removeChildren(XMLDefs.IDENTIFIER);
        identifier = new Element(XMLDefs.IDENTIFIER);
        identifier.setAttribute(XMLDefs.FIELD_NUMBER, "3.1");
        identifier.setText("8661-1");

        location.addContent(identifier);

        value = pipeParser.getFieldValue("OBX", location, "5.9");
        assertEquals("[PD] Extracting OBX(8661-1)-5.9 failed.", pipe_result_6, value);

        value = xmlParser.getFieldValue("OBX", location, "5.9");
        assertEquals("[XML] Extracting OBX(8661-1)-5.9 failed.", xml_result_6, value);
    }

    @Test
    public void testConditionalOrGetSubFieldValue() throws IOException {
        PipeParser              pipeParser = new PipeParser();
        XmlParser               xmlParser = new XmlParser();
        String                  value;
        Element                 field;
        Element                 location;
        Element                 identifier;

        pipeParser.loadData(hl7TestData_1);
        xmlParser.loadData(xmlData_1);

        //
        // Test the ability to select based on "or" logic.
        //
        field = new Element(XMLDefs.FIELD);
        field.setAttribute(XMLDefs.CAN_CONTAIN_MULTIPLE_VALUES, "true");
        location = new Element(XMLDefs.LOCATION).addContent(new Element(XMLDefs.HL7_SEGMENT));
        field.addContent(location);


        identifier = new Element(XMLDefs.IDENTIFIER);
        identifier.setText("PT");

        location.addContent(identifier);

        location.removeChildren(XMLDefs.IDENTIFIER);
        identifier = new Element(XMLDefs.IDENTIFIER);
        identifier.setAttribute(XMLDefs.FIELD_NUMBER, "3.5");
        identifier.setText("PI");

        location.addContent(identifier);

        value = pipeParser.getFieldValue("PID", location, "3.1");
        assertEquals("[PD] Extracting PID(PI)-3.1 failed.", pipe_result_7, value);

        value = xmlParser.getFieldValue("PID", location, "3.1");
        assertEquals("[XML] Extracting PID(PI)-3.1 failed.", xml_result_7, value);
    }

    @Test
    public void testGetFieldValueByName()
            throws IOException, HL7ValidatorException,
            JDOMException, PropertyAccessException {
        HL7Validator    validator = new HL7Validator("/tmp", "/tmp");
        String          value;

        validator.loadValidationRules("../XML/SyndromicDataValidations.xml");

        validator.loadData(hl7TestData_2);

        value = validator.getFieldValueByName("Facility Identifier");
        assertEquals("Retrieving Facility Identifier (2.5.1) failed.", facility_identifier_2_5_1, value);

        value = validator.getFieldValueByName("Medical Record Number");
        assertEquals("Retrieving Medical Record Number failed.", medical_record_number_hl7_1, value);


        validator.loadData(hl7TestData_1);

        value = validator.getFieldValueByName("Date of Onset");
        value = validator.getFieldValueByName("Facility Identifier");
        assertEquals("Retrieving Facility Identifier (2.3.1) failed.", facility_identifier_2_3_1, value);

        validator.loadData(xmlData_1);

        value = validator.getFieldValueByName("Medical Record Number");
        assertEquals("Retrieving Medical Record Number failed.", medical_record_number_xml_1, value);
    }

    @Test
    public void testGetFieldValueById()
            throws IOException, HL7ValidatorException,
            JDOMException, PropertyAccessException {
        HL7Validator    validator = new HL7Validator("/tmp", "/tmp");
        String          value;

        validator.loadValidationRules("../XML/SyndromicDataValidations.xml");

        validator.loadData(hl7TestData_2);

        value = validator.getFieldValueById(new Integer(1));
        assertEquals("Retrieving Facility Identifier (2.5.1) failed.", facility_identifier_2_5_1, value);


        validator.loadData(hl7TestData_1);

        value = validator.getFieldValueById(new Integer(1));
        assertEquals("Retrieving Facility Identifier (2.3.1) failed.", facility_identifier_2_3_1, value);
    }

    @Test
    public void  testConditionallyRequiredField()
            throws JDOMException, PropertyAccessException, IOException, HL7ValidatorException {
        HL7Validator    validator = new HL7Validator("/tmp", "/tmp");
        String          value;

        validator.loadValidationRules("src/test/resources/SyndromicDataValidationsTestConditionalField.xml");

        //
        // Test conditional data, HL7 conditional data is present.
        //
        validator.loadData(hl7TestData_Conditional_data_present);

        validator.setMaxErrorSeverity(ErrorSeverity.IGNORE);
        validator.validateData();
        if(validator.getMaxErrorSeverity() != ErrorSeverity.IGNORE) {
            assertFalse("Validation of Conditional Field failed (conditional data present - HL7).", true);
        }

        //
        // Test conditional data, XML conditional data is present.
        //
        validator.loadData(xmlData_Conditional_data_present);

        validator.setMaxErrorSeverity(ErrorSeverity.IGNORE);
        validator.validateData();
        if(validator.getMaxErrorSeverity() != ErrorSeverity.IGNORE) {
            assertFalse("Validation of Conditional Field failed (conditional data present - XML).", true);
        }

        //
        // Test conditional data, HL7 conditional data is not present.
        //
        validator.loadData(hl7TestData_Conditional_data_missing);

        validator.setMaxErrorSeverity(ErrorSeverity.IGNORE);
        validator.validateData();
        if(validator.getMaxErrorSeverity() == ErrorSeverity.IGNORE) {
            assertFalse("Validation of Conditional Field failed (conditional data missing - HL7).", true);
        }

        //
        // Test conditional data, XML conditional data is not present.
        //
        validator.loadData(xmlData_Conditional_data_missing);

        validator.setMaxErrorSeverity(ErrorSeverity.IGNORE);
        validator.validateData();
        if(validator.getMaxErrorSeverity() == ErrorSeverity.IGNORE) {
            assertFalse("Validation of Conditional Field failed (conditional data missing - XML).", true);
        }
    }

    @Test
    public void testValidateFieldDataType()
            throws JDOMException, PropertyAccessException, IOException, HL7ValidatorException {
        HL7Validator    validator = new HL7Validator("/tmp", "/tmp");
        Element         dataTypeElement;
        List<String>    valueList = new ArrayList<String>();

        validator.loadValidationRules("../XML/SyndromicDataValidations.xml");

        validator.loadData(hl7TestData_2);

        dataTypeElement = new Element(XMLDefs.DATA_TYPE);
        dataTypeElement.setAttribute(XMLDefs.SEVERITY, ErrorSeverity.HOLD.name());
        dataTypeElement.setAttribute(XMLDefs.ERROR_CODE_ID, "4");
        dataTypeElement.setAttribute(XMLDefs.ERROR_MESSAGE_ID, "5");
        dataTypeElement.setText(DataType.Alpha.name());

        //
        // Test valid alpha value.
        //
        validator.setMaxErrorSeverity(ErrorSeverity.IGNORE);
        valueList.add("abcde");
        validator.validateFieldDataType(valueList, dataTypeElement, "Test Field", Usage.Required);
        if(validator.getMaxErrorSeverity() != ErrorSeverity.IGNORE) {
            assertFalse("Validation of Alpha field failed (valid data).", true);
        }

        //
        // Test valid alpha value with special characters.
        //
        dataTypeElement.setAttribute(XMLDefs.SPECIAL_CHARACTERS, "-_");

        validator.setMaxErrorSeverity(ErrorSeverity.IGNORE);
        valueList.clear();
        valueList.add("AB-cde_f");
        validator.validateFieldDataType(valueList, dataTypeElement, "Test Field", Usage.Required);
        if(validator.getMaxErrorSeverity() != ErrorSeverity.IGNORE) {
            assertFalse("Validation of Alpha field failed (valid data with special characters).", true);
        }

        //
        // Test valid numeric value.
        //
        dataTypeElement.setText(DataType.Numeric.name());

        validator.setMaxErrorSeverity(ErrorSeverity.IGNORE);
        valueList.clear();
        valueList.add("12345");
        validator.validateFieldDataType(valueList, dataTypeElement, "Test Field", Usage.Required);
        if(validator.getMaxErrorSeverity() != ErrorSeverity.IGNORE) {
            assertFalse("Validation of Numeric field failed (valid data).", true);
        }


        //
        // Test valid numeric value with special characters.
        //
        validator.setMaxErrorSeverity(ErrorSeverity.IGNORE);
        valueList.clear();
        valueList.add("123-45678");
        validator.validateFieldDataType(valueList, dataTypeElement, "Test Field", Usage.Required);
        if(validator.getMaxErrorSeverity() != ErrorSeverity.IGNORE) {
            assertFalse("Validation of Numeric field failed (valid data with special characters).", true);
        }


        //
        // Test valid alpha numeric value.
        //
        dataTypeElement.setText(DataType.AlphaNumeric.name());

        validator.setMaxErrorSeverity(ErrorSeverity.IGNORE);
        valueList.clear();
        valueList.add("ABC12345");
        validator.validateFieldDataType(valueList, dataTypeElement, "Test Field", Usage.Required);
        if(validator.getMaxErrorSeverity() != ErrorSeverity.IGNORE) {
            assertFalse("Validation of Alpha Numeric field failed (valid data).", true);
        }


        //
        // Test valid alpha numeric value with special characters.
        //
        validator.setMaxErrorSeverity(ErrorSeverity.IGNORE);
        valueList.clear();
        valueList.add("xyz-45678");
        validator.validateFieldDataType(valueList, dataTypeElement, "Test Field", Usage.Required);
        if(validator.getMaxErrorSeverity() != ErrorSeverity.IGNORE) {
            assertFalse("Validation of Alpha Numeric field failed (valid data with special characters).", true);
        }

        //
        // Test valid alpha numeric value with invalid special characters.
        //
        validator.setMaxErrorSeverity(ErrorSeverity.IGNORE);
        valueList.clear();
        valueList.add("xyz+45678");
        validator.validateFieldDataType(valueList, dataTypeElement, "Test Field", Usage.Required);
        if(validator.getMaxErrorSeverity() == ErrorSeverity.IGNORE) {
            assertFalse("Validation of Alpha Numeric field failed (valid data with invalid special characters).", true);
        }

        //
        // Test minimum length.
        //
        dataTypeElement.setAttribute(XMLDefs.MINIMUM_LENGTH, "3");

        validator.setMaxErrorSeverity(ErrorSeverity.IGNORE);
        valueList.clear();
        valueList.add("12");
        validator.validateFieldDataType(valueList, dataTypeElement, "Test Field", Usage.Required);
        if(validator.getMaxErrorSeverity() == ErrorSeverity.IGNORE) {
            assertFalse("Validation of minimum length failed.", true);
        }

        //
        // Test maximum length.
        //
        dataTypeElement.setAttribute(XMLDefs.MAXIMUM_LENGTH, "5");

        validator.setMaxErrorSeverity(ErrorSeverity.IGNORE);
        valueList.clear();
        valueList.add("1234567");
        validator.validateFieldDataType(valueList, dataTypeElement, "Test Field", Usage.Required);
        if(validator.getMaxErrorSeverity() == ErrorSeverity.IGNORE) {
            assertFalse("Validation of maximum length failed.", true);
        }

        //
        // Test a valid length.
        //
        validator.setMaxErrorSeverity(ErrorSeverity.IGNORE);
        valueList.clear();
        valueList.add("12345");
        validator.validateFieldDataType(valueList, dataTypeElement, "Test Field", Usage.Required);
        if(validator.getMaxErrorSeverity() != ErrorSeverity.IGNORE) {
            assertFalse("Validation of a valid length failed.", true);
        }

        //
        // Test a valid date.
        //
        dataTypeElement.removeAttribute(XMLDefs.MAXIMUM_LENGTH);
        dataTypeElement.removeAttribute(XMLDefs.MINIMUM_LENGTH);

        dataTypeElement.setAttribute(XMLDefs.FORMAT, "yyyy-MM-dd");
        dataTypeElement.setText(DataType.Date.name());

        validator.setMaxErrorSeverity(ErrorSeverity.IGNORE);
        valueList.clear();
        valueList.add("2012-01-30");
        validator.validateFieldDataType(valueList, dataTypeElement, "Test Field", Usage.Required);
        if(validator.getMaxErrorSeverity() != ErrorSeverity.IGNORE) {
            assertFalse("Validation of a valid date with a format specified failed.", true);
        }

        validator.setMaxErrorSeverity(ErrorSeverity.IGNORE);
        valueList.clear();
        valueList.add("01-30-2012");
        validator.validateFieldDataType(valueList, dataTypeElement, "Test Field", Usage.Required);
        if(validator.getMaxErrorSeverity() == ErrorSeverity.IGNORE) {
            assertFalse("Validation of a date format with a different format specified failed.", true);
        }

        dataTypeElement.removeAttribute(XMLDefs.FORMAT);

        validator.setMaxErrorSeverity(ErrorSeverity.IGNORE);
        valueList.clear();
        valueList.add("201203221425");
        validator.validateFieldDataType(valueList, dataTypeElement, "Test Field", Usage.Required);
        if(validator.getMaxErrorSeverity() != ErrorSeverity.IGNORE) {
            assertFalse("Validation of a date format 1 using the default valid dates failed.", true);
        }

        validator.setMaxErrorSeverity(ErrorSeverity.IGNORE);
        valueList.clear();
        valueList.add("201203221425-0500");
        validator.validateFieldDataType(valueList, dataTypeElement, "Test Field", Usage.Required);
        if(validator.getMaxErrorSeverity() != ErrorSeverity.IGNORE) {
            assertFalse("Validation of a date format 2 using the default valid dates failed.", true);
        }

    }

    public void testValidateFieldValue()
            throws JDOMException, PropertyAccessException, IOException, HL7ValidatorException {
        HL7Validator    validator = new HL7Validator("/tmp", "/tmp");
        Element         validValuesElement;
        List<String>    valueList = new ArrayList<String>();

        validator.loadValidationRules("../XML/SyndromicDataValidations.xml");

        validator.loadData(hl7TestData_2);

        validValuesElement = new Element(XMLDefs.VALUE_LIST);
        validValuesElement.setAttribute(XMLDefs.SEVERITY, ErrorSeverity.HOLD.name());
        validValuesElement.setAttribute(XMLDefs.ERROR_CODE_ID, "4");
        validValuesElement.setAttribute(XMLDefs.ERROR_MESSAGE_ID, "5");
        validValuesElement.setAttribute(XMLDefs.CASE_SENSITIVE, "true");
        validValuesElement.addContent(new Element(XMLDefs.VALUE).setText("ABC"));
        validValuesElement.addContent(new Element(XMLDefs.VALUE).setText("DEF"));
        validValuesElement.addContent(new Element(XMLDefs.VALUE).setText("123"));

        //
        // Valid value with valid case, case sensitive = true.
        //
        validator.setMaxErrorSeverity(ErrorSeverity.IGNORE);
        valueList.add("ABC");
        validator.validateFieldValue(valueList, validValuesElement, "Test Field", Usage.Required);
        if(validator.getMaxErrorSeverity() != ErrorSeverity.IGNORE) {
            assertFalse("Validation of a valid value with valid case failed. (case sensitive = true)", true);
        }

        //
        // Valid value with invalid case, case sensitive = true.
        //
        validator.setMaxErrorSeverity(ErrorSeverity.IGNORE);
        valueList.clear();
        valueList.add("Abc");
        validator.validateFieldValue(valueList, validValuesElement, "Test Field", Usage.Required);
        if(validator.getMaxErrorSeverity() == ErrorSeverity.IGNORE) {
            assertFalse("Validation of a valid value with invalid case failed. (case sensitive = true)", true);
        }

        //
        // Valid value with valid case, case sensitive = false.
        //
        validValuesElement.removeAttribute(XMLDefs.CASE_SENSITIVE);
        validValuesElement.setAttribute(XMLDefs.CASE_SENSITIVE, "false");

        validator.setMaxErrorSeverity(ErrorSeverity.IGNORE);
        valueList.clear();
        valueList.add("Abc");
        validator.validateFieldValue(valueList, validValuesElement, "Test Field", Usage.Required);
        if(validator.getMaxErrorSeverity() != ErrorSeverity.IGNORE) {
            assertFalse("Validation of a valid value with valid case failed. (case sensitive = false)", true);
        }

        //
        // Invalid value with valid case, case sensitive = false.
        //
        validator.setMaxErrorSeverity(ErrorSeverity.IGNORE);
        valueList.clear();
        valueList.add("ABCDEF");
        validator.validateFieldValue(valueList, validValuesElement, "Test Field", Usage.Required);
        if(validator.getMaxErrorSeverity() == ErrorSeverity.IGNORE) {
            assertFalse("Validation of an invalid value with valid case failed. (case sensitive = false)", true);
        }
    }

//    @Test
//    public void  testGetAllFieldValues()
//            throws JDOMException, PropertyAccessException, IOException, HL7ValidatorException {
//        HL7Validator    validator = new HL7Validator("/tmp", "/tmp");
//        List<String>    returnedValues;
//
//        validator.loadValidationRules("../XML/SyndromicDataValidations.xml");
//
//        validator.loadData(hl7TestData_1);
//
//        returnedValues = validator.getMultipleFieldValuesByName("Unique Patient Identifier");
//        assertEquals("Get multiple field values (Pipe Delimited - field) failed.", 4, returnedValues.size());
//
//        returnedValues = validator.getMultipleFieldValuesByName("Diagnosis/Injury Code");
//        assertEquals("Get multiple field values (Pipe Delimited - segment) failed.", 2, returnedValues.size());
//
//        validator.loadData(xmlData_1);
//
//        returnedValues = validator.getMultipleFieldValuesByName("Unique Patient Identifier");
//        assertEquals("Get multiple field values (XML - field) failed.", 4, returnedValues.size());
//
//        returnedValues = validator.getMultipleFieldValuesByName("Chief Complaint / Reason for visit");
//        assertEquals("Get multiple field values (XML - segment - 1) failed.", 2, returnedValues.size());
//
//        returnedValues = validator.getMultipleFieldValuesByName("Diagnosis/Injury Code");
//        assertEquals("Get multiple field values (XML - segment - 2) failed.", 2, returnedValues.size());
//    }

    @Test
    public void  testMissingDataInOptionalSegment()
            throws JDOMException, PropertyAccessException, IOException, HL7ValidatorException {
        HL7Validator    validator = new HL7Validator("/tmp", "/tmp");
        boolean         result;

        validator.loadValidationRules("../XML/SyndromicDataValidations.xml");

        validator.loadData(hl7TestData_missing_data_in_optional_segment_segment_present);

        validator.setMaxErrorSeverity(ErrorSeverity.IGNORE);
        result = validator.validateData();
        if(validator.getMaxErrorSeverity() == ErrorSeverity.IGNORE) {
            assertFalse("Validation of Missing data in an optional segment failed. (Pipe Delimited - segment present)", true);
        }

        validator.loadData(xmlData_missing_data_in_optional_segment_segment_present);

        validator.setMaxErrorSeverity(ErrorSeverity.IGNORE);
        result = validator.validateData();
        if(validator.getMaxErrorSeverity() == ErrorSeverity.IGNORE) {
            assertFalse("Validation of Missing data in an optional segment failed. (XML - segment present)", true);
        }

        validator.loadData(hl7TestData_missing_data_in_optional_segment_segment_missing);

        validator.setMaxErrorSeverity(ErrorSeverity.IGNORE);
        result = validator.validateData();
        if(validator.getMaxErrorSeverity() != ErrorSeverity.IGNORE) {
            assertFalse("Validation of Missing data in an optional segment failed. (Pipe Delimited - segment missing)", true);
        }

        validator.loadData(xmlData_missing_data_in_optional_segment_segment_missing);

        validator.setMaxErrorSeverity(ErrorSeverity.IGNORE);
        result = validator.validateData();
        if(validator.getMaxErrorSeverity() != ErrorSeverity.IGNORE) {
            assertFalse("Validation of Missing data in an optional segment failed. (XML - segment missing)", true);
        }
    }

    @Test
    public void testValidateHl7Transmission()
            throws JDOMException, IOException, HL7ValidatorException, PropertyAccessException {
        HL7Validator    validator = new HL7Validator("/tmp", "/tmp");
        boolean         result;

        validator.loadValidationRules("../XML/SyndromicDataValidations.xml");

        validator.loadData(hl7TestData_1);
        result = validator.validateData();

        assertEquals("Validation of entire HL7 Pipe Delimited transmission Failed.", true, result);

        validator.loadData(xmlData_1);
        result = validator.validateData();

        assertEquals("Validation of entire HL7 XML transmission Failed.", true, result);
    }
}
