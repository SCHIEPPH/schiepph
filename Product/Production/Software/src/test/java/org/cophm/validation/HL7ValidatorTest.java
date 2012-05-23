package org.cophm.validation;

import junit.framework.TestCase;
import org.cophm.util.Base64Coder;
import org.cophm.util.Constants;
import org.cophm.util.PropertyAccessException;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by
 * User: ralph
 * Date: 4/6/12
 * Time: 12:21 PM
 */
public class HL7ValidatorTest extends TestCase {

    public static String  xmitXmlPrefix = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n" +
            "<ns2:Adapter_ProvideAndRegisterDocumentSetRequest\n" +
            "    xmlns:ns2=\"urn:gov:hhs:fha:nhinc:common:nhinccommonadapter\"\n" +
            "    xmlns=\"urn:gov:hhs:fha:nhinc:common:nhinccommon\"\n" +
            "    xmlns:ns4=\"urn:oasis:names:tc:ebxml-regrep:xsd:rs:3.0\"\n" +
            "    xmlns:ns3=\"urn:oasis:names:tc:ebxml-regrep:xsd:rim:3.0\"\n" +
            "    xmlns:ns5=\"urn:oasis:names:tc:ebxml-regrep:xsd:lcm:3.0\" xmlns:ns6=\"urn:ihe:iti:xds-b:2007\"\n" +
            "    xmlns:ns7=\"urn:oasis:names:tc:ebxml-regrep:xsd:query:3.0\">\n" +
            "    <ns2:assertion>\n" +
            "        <haveSecondWitnessSignature>false </haveSecondWitnessSignature>\n" +
            "        <haveSignature>false </haveSignature>\n" +
            "        <haveWitnessSignature>false </haveWitnessSignature>\n" +
            "        <homeCommunity>\n" +
            "            <homeCommunityId>1234 </homeCommunityId>\n" +
            "        </homeCommunity>\n" +
            "        <uniquePatientId>? </uniquePatientId>\n" +
            "        <userInfo>\n" +
            "            <personName>\n" +
            "                <familyName>? </familyName>\n" +
            "                <givenName>? </givenName>\n" +
            "                <secondNameOrInitials>? </secondNameOrInitials>\n" +
            "                <fullName>? ? ? </fullName>\n" +
            "            </personName>\n" +
            "            <userName>? </userName>\n" +
            "            <org>\n" +
            "                <homeCommunityId>? </homeCommunityId>\n" +
            "                <name>? </name>\n" +
            "            </org>\n" +
            "            <roleCoded>\n" +
            "                <code>? </code>\n" +
            "                <codeSystem>? </codeSystem>\n" +
            "                <codeSystemName>? </codeSystemName>\n" +
            "                <displayName>? </displayName>\n" +
            "            </roleCoded>\n" +
            "        </userInfo>\n" +
            "        <authorized>false </authorized>\n" +
            "        <purposeOfDisclosureCoded>\n" +
            "            <code>? </code>\n" +
            "            <codeSystem>? </codeSystem>\n" +
            "            <codeSystemName>? </codeSystemName>\n" +
            "            <displayName>? </displayName>\n" +
            "        </purposeOfDisclosureCoded>\n" +
            "        <samlAuthnStatement>\n" +
            "            <authInstant>2012-04-23T19:03:33.179Z </authInstant>\n" +
            "            <sessionIndex>? </sessionIndex>\n" +
            "            <authContextClassRef>urn:oasis:names:tc:SAML:2.0:ac:classes:unspecified </authContextClassRef>\n" +
            "            <subjectLocalityAddress>? </subjectLocalityAddress>\n" +
            "            <subjectLocalityDNSName>? </subjectLocalityDNSName>\n" +
            "        </samlAuthnStatement>\n" +
            "        <samlAuthzDecisionStatement>\n" +
            "            <decision>Permit </decision>\n" +
            "            <resource>https://localhost:8181/CONNECTNhinServicesWeb/DocumentRepositoryXDR_Service </resource>\n" +
            "            <action>Execute </action>\n" +
            "            <evidence>\n" +
            "                <assertion>\n" +
            "                    <id>? </id>\n" +
            "                    <issueInstant>2012-04-23T19:03:33.182Z </issueInstant>\n" +
            "                    <version>2.0 </version>\n" +
            "                    <issuer>CN=SAML User,OU=SU,O=SAML User,L=Los Angeles,ST=CA,C=US </issuer>\n" +
            "                    <issuerFormat>urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName </issuerFormat>\n" +
            "                    <conditions>\n" +
            "                        <notBefore>2012-04-23T19:03:33.184Z </notBefore>\n" +
            "                        <notOnOrAfter>2012-04-23T19:03:33.184Z </notOnOrAfter>\n" +
            "                    </conditions>\n" +
            "                    <accessConsentPolicy>? </accessConsentPolicy>\n" +
            "                    <instanceAccessConsentPolicy>? </instanceAccessConsentPolicy>\n" +
            "                </assertion>\n" +
            "            </evidence>\n" +
            "        </samlAuthzDecisionStatement>\n" +
            "        <samlSignature>\n" +
            "            <keyInfo>\n" +
            "                <rsaKeyValueModulus> </rsaKeyValueModulus>\n" +
            "                <rsaKeyValueExponent> </rsaKeyValueExponent>\n" +
            "            </keyInfo>\n" +
            "            <signatureValue> </signatureValue>\n" +
            "        </samlSignature>\n" +
            "        <messageId>790ab04e-afc4-4982-bc7a-97618e712992 </messageId>\n" +
            "    </ns2:assertion>\n" +
            "    <ns2:ProvideAndRegisterDocumentSetRequest>\n" +
            "        <ns5:SubmitObjectsRequest comment=\"comme\" id=\"123\">\n" +
            "            <ns3:RegistryObjectList>\n" +
            "                <ns3:ExtrinsicObject mimeType=\"text/plain\"\n" +
            "                    objectType=\"urn:uuid:7edca82f-054d-47f2-a032-9b2a5b5186c1\" id=\"Document01\">\n" +
            "                    <ns3:Slot name=\"sourcePatientId\">\n" +
            "                        <ns3:ValueList>\n" +
            "                            <ns3:Value>${patientId} </ns3:Value>\n" +
            "                        </ns3:ValueList>\n" +
            "                    </ns3:Slot>\n" +
            "                    <ns3:Slot name=\"intendedRecipient\">\n" +
            "                        <ns3:ValueList>\n" +
            "                            <ns3:Value>${INTENDED_RECIPIENT} </ns3:Value>\n" +
            "                        </ns3:ValueList>\n" +
            "                    </ns3:Slot>\n" +
            "                    <ns3:ExternalIdentifier value=\"${patientId}\"\n" +
            "                        identificationScheme=\"urn:uuid:58a6f841-87b3-4a3e-92fd-a8ffeff98427\"\n" +
            "                        registryObject=\"Document01\" id=\"ei01\">\n" +
            "                        <ns3:Name>\n" +
            "                            <ns3:LocalizedString value=\"XDSDocumentEntry.patientId\"/>\n" +
            "                        </ns3:Name>\n" +
            "                    </ns3:ExternalIdentifier>\n" +
            "                    <ns3:ExternalIdentifier value=\"${XDSDocumentEntry_uniqueId}\"\n" +
            "                        identificationScheme=\"urn:uuid:2e82c1f6-a085-4c72-9da3-8640a32e42ab\"\n" +
            "                        registryObject=\"Document01\" id=\"ei02\">\n" +
            "                        <ns3:Name>\n" +
            "                            <ns3:LocalizedString value=\"XDSDocumentEntry.uniqueId\"/>\n" +
            "                        </ns3:Name>\n" +
            "                    </ns3:ExternalIdentifier>\n" +
            "                </ns3:ExtrinsicObject>\n" +
            "                <ns3:RegistryPackage id=\"SubmissionSet01\">\n" +
            "                    <ns3:ExternalIdentifier value=\"2.16.840.1.113883.3.1239\"\n" +
            "                        identificationScheme=\"urn:uuid:554ac39e-e3fe-47fe-b233-965d2a147832\"\n" +
            "                        registryObject=\"SubmissionSet01\" id=\"urn:oid: 1234\">\n" +
            "                        <ns3:Name>\n" +
            "                            <ns3:LocalizedString value=\"XDSSubmissionSet.sourceId\"/>\n" +
            "                        </ns3:Name>\n" +
            "                    </ns3:ExternalIdentifier>\n" +
            "                    <ns3:ExternalIdentifier value=\"${patientId}\"\n" +
            "                        identificationScheme=\"urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446\"\n" +
            "                        registryObject=\"SubmissionSet01\" id=\"ei05\">\n" +
            "                        <ns3:Name>\n" +
            "                            <ns3:LocalizedString value=\"XDSSubmissionSet.patientId\"/>\n" +
            "                        </ns3:Name>\n" +
            "                    </ns3:ExternalIdentifier>\n" +
            "                </ns3:RegistryPackage>\n" +
            "                <ns3:Classification\n" +
            "                    classificationNode=\"urn:uuid:a54d6aa5-d40d-43f9-88c5-b4633d873bdd\"\n" +
            "                    classifiedObject=\"SubmissionSet01\" id=\"cl10\"/>\n" +
            "                <ns3:Association targetObject=\"Document01\" sourceObject=\"SubmissionSet01\"\n" +
            "                    associationType=\"HasMember\" id=\"as01\">\n" +
            "                    <ns3:Slot name=\"SubmissionSetStatus\">\n" +
            "                        <ns3:ValueList>\n" +
            "                            <ns3:Value>Original </ns3:Value>\n" +
            "                        </ns3:ValueList>\n" +
            "                    </ns3:Slot>\n" +
            "                </ns3:Association>\n" +
            "            </ns3:RegistryObjectList>\n" +
            "        </ns5:SubmitObjectsRequest>\n" +
            "        <ns6:Document id=\"Document01\"\n" +
            "            >";

    public static String   xmitXmlPostfix = "</ns6:Document>\n" +
            "    </ns2:ProvideAndRegisterDocumentSetRequest>\n" +
            "</ns2:Adapter_ProvideAndRegisterDocumentSetRequest>";

    public static String                                                                                                                                                                                                                                                                                                hl7TestData_1 =
                    "MSH|^~\\&|||||20111213131225||ADT^A01|MSG00001|P|2.3.1||||||UTF-8\n" +
                            "EVN||8_ZZ_AA_report_date_time\n" +
                            "PID|1||9_ZZ_AA_unique_patient_id^^^^PG~9_Z1_AA_unique_patient_id^^^^PI~9_Z2_AA_unique_patient_id^^^^PQ~10_ZZ_AA_medical_record_number^^^^MR||paitent_name|||13_ZZ_AA_gender||18_ZZ_AA_paitent_race|^^14_ZZ_AA_paitent_city^16_ZZ_AA_patient_state^15_ZZ_AA_paitent_zip_code^17_ZZ_AA_paitent_country^^^20_ZZ_AA_paitent_county|||||||||||19_ZZ_AA_paitent_ethnicity\n" +
                            "PV1||24_ZZ_AA_paitent_class|||||||||||||||||21_ZZ_AA_visit_id|||||||||||||||||30_ZZ_AA_discharge_disposition||||||||22_ZZ_AA_visit_date_time|31_ZZ_AA_disposition_date_time\n" +
                            "OBX|1|HD|SS001^Treating Facility Identifier^PHINQUESTION||2_ZZ_AA_facility_name^1_ZZ_AA_facility_identifier^NP||||||C||||||\n" +
                            "OBX|2|XAD|SS002^Treating Facility Location||^3_ZZ_AA_facility_number_and_street^^^^4_ZZ_AA_facility_city^6_ZZ_AA_facility_state^30341^USA^C^^5_ZZ_AA_facility_county||||||X\n" +
                            "OBX|3|CWE|SS003^Facility/Visit Type^PHINQUESTION||7_ZZ_AA_facility_visit_type|||||||X\n" +
                            "OBX|4|NM|21612-7^AGE TIME PAITENT REPORTED^LN||11_ZZ_AA_age|a^12_ZZ_AA_age_units^UCUM|||||X\n" +
                            "OBX|5|TS|11368-8^ILLNESS OR INJURY ONSET DATE AND TIME:TMSTP:PT:PAITENT:QN^LN||23_ZZ_AA_date_of_onset||||||X\n" +
                            "OBX|6|CWE|8661-1^CHIEF COMPLAINT:FIND:PT:PAITENT:NOM:REPORTED^LN||^^^^^^^^25_ZZ_AA_chief_complaint||||||X\n" +
                            "OBX|7|TX|54094-8^TRIAGE NOTE:FIND:PT:EMERGENCY DEPARTMENT:DOC^LN||26_ZZ_AA_triage_notes||||||X\n" +
                            "OBX|8|TX|44833-2^DIAGNOSIS:PRELIMINARY:IMP:PT:PAITENT:NOM:^LN||28_ZZ_AA_clinical_impression||||||X\n" +
                            "OBX|9|NM|11289-6^BODY TEMPERATURE:TEMP:ENCTRFIRST:PAITENT:QN^LN||32_ZZ_AA_initial_temperature|^xx_ZZ_AA_temperature_units^UCUM|||||X\n" +
                            "OBX|10|NM|59408-5^OXYGEN SATURATION:MFR:PT:BLDA:QN:PULSE OXIMETRY^LN||33_ZZ_AA_pulse_oximetry|^xx_ZZ_AA_pulse_oximetry_units|||||X\n" +
                            "DG1|1||27_ZZ_AA_diagnoisis_injury_code^|||29_ZZ_AA_diagnosis_type";

    public static String  xmlData_1 = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
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
            "            <EVN.2.1>8_ZZ_AA_report_date_time </EVN.2.1>\n"+
            "        </EVN.2>\n"+
            "    </EVN>\n"+
            "    <PID>\n"+
            "        <PID.1>\n"+
            "            <PID.1.1>1 </PID.1.1>\n"+
            "        </PID.1>\n"+
            "        <PID.2/>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>9_ZZ_AA_unique_patient_id </PID.3.1>\n"+
            "            <PID.3.2/>\n"+
            "            <PID.3.3/>\n"+
            "            <PID.3.4/>\n"+
            "            <PID.3.5>PG </PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>9_Z1_AA_unique_patient_id </PID.3.1>\n"+
            "            <PID.3.2/>\n"+
            "            <PID.3.3/>\n"+
            "            <PID.3.4/>\n"+
            "            <PID.3.5>PI </PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>9_Z2_AA_unique_patient_id </PID.3.1>\n"+
            "            <PID.3.2/>\n"+
            "            <PID.3.3/>\n"+
            "            <PID.3.4/>\n"+
            "            <PID.3.5>PQ </PID.3.5>\n"+
            "        </PID.3>\n"+
            "        <PID.3>\n"+
            "            <PID.3.1>10_ZZ_AA_medical_record_number </PID.3.1>\n"+
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
            "            <PID.8.1>13_ZZ_AA_gender </PID.8.1>\n"+
            "        </PID.8>\n"+
            "        <PID.9/>\n"+
            "        <PID.10>\n"+
            "            <PID.10.1>18_ZZ_AA_paitent_race </PID.10.1>\n"+
            "        </PID.10>\n"+
            "        <PID.11>\n"+
            "            <PID.11.1/>\n"+
            "            <PID.11.2/>\n"+
            "            <PID.11.3>14_ZZ_AA_paitent_city </PID.11.3>\n"+
            "            <PID.11.4>16_ZZ_AA_patient_state </PID.11.4>\n"+
            "            <PID.11.5>15_ZZ_AA_paitent_zip_code </PID.11.5>\n"+
            "            <PID.11.6>17_ZZ_AA_paitent_country </PID.11.6>\n"+
            "            <PID.11.7/>\n"+
            "            <PID.11.8/>\n"+
            "            <PID.11.9>20_ZZ_AA_paitent_county </PID.11.9>\n"+
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
            "            <PID.22.1>19_ZZ_AA_paitent_ethnicity </PID.22.1>\n"+
            "        </PID.22>\n"+
            "    </PID>\n"+
            "    <PV1>\n"+
            "        <PV1.1/>\n"+
            "        <PV1.2>\n"+
            "            <PV1.2.1>24_ZZ_AA_paitent_class </PV1.2.1>\n"+
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
            "            <PV1.19.1>21_ZZ_AA_visit_id </PV1.19.1>\n"+
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
            "            <PV1.36.1>30_ZZ_AA_discharge_disposition </PV1.36.1>\n"+
            "        </PV1.36>\n"+
            "        <PV1.37/>\n"+
            "        <PV1.38/>\n"+
            "        <PV1.39/>\n"+
            "        <PV1.40/>\n"+
            "        <PV1.41/>\n"+
            "        <PV1.42/>\n"+
            "        <PV1.43/>\n"+
            "        <PV1.44>\n"+
            "            <PV1.44.1>22_ZZ_AA_visit_date_time </PV1.44.1>\n"+
            "        </PV1.44>\n"+
            "        <PV1.45>\n"+
            "            <PV1.45.1>31_ZZ_AA_disposition_date_time </PV1.45.1>\n"+
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
            "            <OBX.3.2>Treating Facility Identifier </OBX.3.2>\n"+
            "            <OBX.3.3>PHINQUESTION </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>2_ZZ_AA_facility_name </OBX.5.1>\n"+
            "            <OBX.5.2>1_ZZ_AA_facility_identifier </OBX.5.2>\n"+
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
            "            <OBX.3.2>Treating Facility Location </OBX.3.2>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1/>\n"+
            "            <OBX.5.2>3_ZZ_AA_facility_number_and_street </OBX.5.2>\n"+
            "            <OBX.5.3/>\n"+
            "            <OBX.5.4/>\n"+
            "            <OBX.5.5/>\n"+
            "            <OBX.5.6>4_ZZ_AA_facility_city </OBX.5.6>\n"+
            "            <OBX.5.7>6_ZZ_AA_facility_state </OBX.5.7>\n"+
            "            <OBX.5.8>30341 </OBX.5.8>\n"+
            "            <OBX.5.9>USA </OBX.5.9>\n"+
            "            <OBX.5.10>C </OBX.5.10>\n"+
            "            <OBX.5.11/>\n"+
            "            <OBX.5.12>5_ZZ_AA_facility_county </OBX.5.12>\n"+
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
            "            <OBX.3.2>Facility/Visit Type </OBX.3.2>\n"+
            "            <OBX.3.3>PHINQUESTION </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>7_ZZ_AA_facility_visit_type </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6/>\n"+
            "        <OBX.7/>\n"+
            "        <OBX.8/>\n"+
            "        <OBX.9/>\n"+
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
            "            <OBX.3.2>AGE TIME PAITENT REPORTED </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>11_ZZ_AA_age </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6>\n"+
            "            <OBX.6.1>a </OBX.6.1>\n"+
            "            <OBX.6.2>12_ZZ_AA_age_units </OBX.6.2>\n"+
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
            "            <OBX.3.2>ILLNESS OR INJURY ONSET DATE AND TIME:TMSTP:PT:PAITENT:QN </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>23_ZZ_AA_date_of_onset </OBX.5.1>\n"+
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
            "            <OBX.3.2>CHIEF COMPLAINT:FIND:PT:PAITENT:NOM:REPORTED </OBX.3.2>\n"+
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
            "            <OBX.5.9>25_ZZ_AA_chief_complaint </OBX.5.9>\n"+
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
            "            <OBX.3.2>TRIAGE NOTE:FIND:PT:EMERGENCY DEPARTMENT:DOC </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>26_ZZ_AA_triage_notes </OBX.5.1>\n"+
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
            "            <OBX.5.1>28_ZZ_AA_clinical_impression </OBX.5.1>\n"+
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
            "            <OBX.3.2>BODY TEMPERATURE:TEMP:ENCTRFIRST:PAITENT:QN </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>32_ZZ_AA_initial_temperature </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6>\n"+
            "            <OBX.6.1/>\n"+
            "            <OBX.6.2>xx_ZZ_AA_temperature_units </OBX.6.2>\n"+
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
            "            <OBX.3.2>OXYGEN SATURATION:MFR:PT:BLDA:QN:PULSE OXIMETRY </OBX.3.2>\n"+
            "            <OBX.3.3>LN </OBX.3.3>\n"+
            "        </OBX.3>\n"+
            "        <OBX.4/>\n"+
            "        <OBX.5>\n"+
            "            <OBX.5.1>33_ZZ_AA_pulse_oximetry </OBX.5.1>\n"+
            "        </OBX.5>\n"+
            "        <OBX.6>\n"+
            "            <OBX.6.1/>\n"+
            "            <OBX.6.2>xx_ZZ_AA_pulse_oximetry_units </OBX.6.2>\n"+
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
            "            <DG1.3.1>27_ZZ_AA_diagnoisis_injury_code </DG1.3.1>\n"+
            "        </DG1.3>\n"+
            "        <DG1.4/>\n"+
            "        <DG1.5/>\n"+
            "        <DG1.6>\n"+
            "            <DG1.6.1>29_ZZ_AA_diagnosis_type </DG1.6.1>\n"+
            "        </DG1.6>\n"+
            "    </DG1>\n"+
            "</HL7Message>";

    public static final String      expectedSubFieldSeparator = "^";
    public static final String      expectedFieldRepititionSeparator = "~";

    public static final String      pipe_result_1 = "MSG00001";
    public static final String      xml_result_1 = "MSG00001";
    public static final String      pipe_result_2 = "24_ZZ_AA_paitent_class";
    public static final String      xml_result_2 = "24_ZZ_AA_paitent_class";
    public static final String      pipe_result_3 = "";
    public static final String      xml_result_3 = "";
    public static final String      pipe_result_4 = "21_ZZ_AA_visit_id";
    public static final String      xml_result_4 = "21_ZZ_AA_visit_id";
    public static final String      pipe_result_5 = "7_ZZ_AA_facility_visit_type";
    public static final String      xml_result_5 = "7_ZZ_AA_facility_visit_type";
    public static final String      pipe_result_6 = "25_ZZ_AA_chief_complaint";
    public static final String      xml_result_6 = "25_ZZ_AA_chief_complaint";
    public static final String      pipe_result_7 = "9_Z1_AA_unique_patient_id";
    public static final String      xml_result_7 = "9_Z1_AA_unique_patient_id";
    public static final String      pipe_result_8 = "27_ZZ_AA_diagnoisis_injury_code";
    public static final String      xml_result_8 = "27_ZZ_AA_diagnoisis_injury_code";
    public static final String      pipe_result_9 = "";
    public static final String      xml_result_9 = "";
    public static final String      pipe_result_10 = "15_ZZ_AA_paitent_zip_code";
    public static final String      xml_result_10 = "15_ZZ_AA_paitent_zip_code";
    public static final String      pipe_result_11 = "A01";
    public static final String      xml_result_11 = "A01";

    @Before
    public void setup() {

    }

    @After
    public void teardown() {

    }

    @Test
    public void testSeparatorValues() throws IOException {
        PipeParser            pipeParser = new PipeParser();
        XmlParser             xmlParser = new XmlParser();

        pipeParser.loadData(hl7TestData_1);

        assertEquals("Did not find expected sub-field separator.", expectedSubFieldSeparator,
                     pipeParser.getSubFieldSeparator());

        assertEquals("Did not find expected field repetition separator.", expectedFieldRepititionSeparator,
                     pipeParser.getRepeatingFieldSeparator());
    }

    @Test
    public void testSimpleGetFieldValue() throws IOException {
        PipeParser            pipeParser = new PipeParser();
        XmlParser             xmlParser = new XmlParser();
        String                  value;

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
        PipeParser            pipeParser = new PipeParser();
        XmlParser             xmlParser = new XmlParser();
        String                  value;

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
        PipeParser            pipeParser = new PipeParser();
        XmlParser             xmlParser = new XmlParser();
        String                  value;
        ArrayList<Element>      identifierList = new ArrayList<Element>();
        Element                 identifier;

        pipeParser.loadData(hl7TestData_1);
        xmlParser.loadData(xmlData_1);

        //
        // Test to see that we can extract data from the correct OBX segment.
        //
        identifier = new Element(Constants.IDENTIFIER);
        identifier.setAttribute(Constants.MUST_MATCH, "true");
        identifier.setAttribute(Constants.FIELD_NUMBER, "3.1");
        identifier.setText("SS003");

        identifierList.add(identifier);

        value = pipeParser.getFieldValue("OBX", identifierList, "5");
        assertEquals("[PD] Extracting OBX(SS003)-5 failed.", pipe_result_5, value);

        value = xmlParser.getFieldValue("OBX", identifierList, "5");
        assertEquals("[XML] Extracting OBX(SS003)-5 failed.", xml_result_5, value);


        identifierList.clear();
        identifier = new Element(Constants.IDENTIFIER);
        identifier.setAttribute(Constants.MUST_MATCH, "true");
        identifier.setAttribute(Constants.FIELD_NUMBER, "3.1");
        identifier.setText("8661-1");

        identifierList.add(identifier);

        value = pipeParser.getFieldValue("OBX", identifierList, "5.9");
        assertEquals("[PD] Extracting OBX(8661-1)-5.9 failed.", pipe_result_6, value);

        value = xmlParser.getFieldValue("OBX", identifierList, "5.9");
        assertEquals("[XML] Extracting OBX(8661-1)-5.9 failed.", xml_result_6, value);
    }

    @Test
    public void testConditionalOrGetSubFieldValue() throws IOException {
        PipeParser            pipeParser = new PipeParser();
        XmlParser             xmlParser = new XmlParser();
        String                  value;
        ArrayList<Element>      identifierList = new ArrayList<Element>();
        Element                 identifier;

        pipeParser.loadData(hl7TestData_1);
        xmlParser.loadData(xmlData_1);

        //
        // Test the ability to select based on "or" logic.
        //
        identifierList.clear();
        identifier = new Element(Constants.IDENTIFIER);
        identifier.setAttribute(Constants.REPEATING_ELEMENT, "Field");
        identifier.setAttribute(Constants.MUST_MATCH, "false");
        identifier.setAttribute(Constants.FIELD_NUMBER, "3.5");
        identifier.setText("PT");

        identifierList.add(identifier);

        identifierList.clear();
        identifier = new Element(Constants.IDENTIFIER);
        identifier.setAttribute(Constants.REPEATING_ELEMENT, "Field");
        identifier.setAttribute(Constants.MUST_MATCH, "false");
        identifier.setAttribute(Constants.FIELD_NUMBER, "3.5");
        identifier.setText("PI");

        identifierList.add(identifier);

        value = pipeParser.getFieldValue("PID", identifierList, "3.1");
        assertEquals("[PD] Extracting PID(PI)-3.1 failed.", pipe_result_7, value);

        value = xmlParser.getFieldValue("PID", identifierList, "3.1");
        assertEquals("[XML] Extracting PID(PI)-3.1 failed.", xml_result_7, value);
    }

    @Test
    public void testValidateHl7Transmission()
            throws JDOMException, IOException, HL7ValidatorException, PropertyAccessException {
        HL7Validator            validator = new HL7Validator("/tmp", "/tmp");
        boolean                 result;

        validator.loadValidationRules("../XML/SyndromicDataValidations.xml");

        result = validator.validateData(xmlData_1);

        assertEquals("Validation of entire HL7 Pipe Delimited transmission Failed.", true, result);

        result = validator.validateData(xmlData_1);

        assertEquals("Validation of entire HL7 XML transmission Failed.", true, result);
    }
}