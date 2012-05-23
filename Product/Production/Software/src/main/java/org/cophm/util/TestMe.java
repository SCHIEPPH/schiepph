package org.cophm.util;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.*;
import ca.uhn.hl7v2.validation.impl.NoValidation;
import ca.uhn.hl7v2.validation.impl.RuleBinding;

import java.io.IOException;
import java.util.Iterator;


/**
 * Created by
 * User: ralph
 * Date: 3/28/12
 * Time: 9:44 AM
 */
public class TestMe {

    public static String    testData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><HL7Message><MSH><MSH.1></MSH.1><MSH.2>^~\\&amp;</MSH.2><MSH.3/><MSH.4/><MSH.5/><MSH.6/><MSH.7><MSH.7.1>20111213131225</MSH.7.1></MSH.7><MSH.8/><MSH.9><MSH.9.1>ADT</MSH.9.1><MSH.9.2>A01</MSH.9.2></MSH.9><MSH.10><MSH.10.1>MSG00001</MSH.10.1></MSH.10><MSH.11><MSH.11.1>P</MSH.11.1></MSH.11><MSH.12><MSH.12.1>2.3.1</MSH.12.1></MSH.12><MSH.13/><MSH.14/><MSH.15/><MSH.16/><MSH.17/><MSH.18><MSH.18.1>UTF-8</MSH.18.1></MSH.18></MSH><EVN><EVN.1/><EVN.2><EVN.2.1>8_ZZ_AA_report_date_time</EVN.2.1></EVN.2></EVN><PID><PID.1><PID.1.1>1</PID.1.1></PID.1><PID.2><PID.2.1>9_ZZ_AA_unique_patient_id</PID.2.1><PID.2.2/><PID.2.3/><PID.2.4/><PID.2.5>PI</PID.2.5></PID.2><PID.3><PID.3.1>10_ZZ_AA_medical_record_number</PID.3.1></PID.3><PID.4/><PID.5><PID.5.1>paitent_name</PID.5.1></PID.5><PID.6/><PID.7/><PID.8><PID.8.1>13_ZZ_AA_gender</PID.8.1></PID.8><PID.9/><PID.10><PID.10.1>18_ZZ_AA_paitent_race</PID.10.1></PID.10><PID.11><PID.11.1/><PID.11.2/><PID.11.3>14_ZZ_AA_paitent_city</PID.11.3><PID.11.4>16_ZZ_AA_patient_state</PID.11.4><PID.11.5>15_ZZ_AA_paitent_zip_code</PID.11.5><PID.11.6>17_ZZ_AA_paitent_country</PID.11.6><PID.11.7/><PID.11.8/><PID.11.9>20_ZZ_AA_paitent_county</PID.11.9></PID.11><PID.12/><PID.13/><PID.14/><PID.15/><PID.16/><PID.17/><PID.18/><PID.19/><PID.20/><PID.21/><PID.22><PID.22.1>19_ZZ_AA_paitent_ethnicity</PID.22.1></PID.22></PID><PV1><PV1.1/><PV1.2><PV1.2.1>24_ZZ_AA_paitent_class</PV1.2.1></PV1.2><PV1.3/><PV1.4/><PV1.5/><PV1.6/><PV1.7/><PV1.8/><PV1.9/><PV1.10/><PV1.11/><PV1.12/><PV1.13/><PV1.14/><PV1.15/><PV1.16/><PV1.17/><PV1.18/><PV1.19><PV1.19.1>21_ZZ_AA_visit_id</PV1.19.1></PV1.19><PV1.20/><PV1.21/><PV1.22/><PV1.23/><PV1.24/><PV1.25/><PV1.26/><PV1.27/><PV1.28/><PV1.29/><PV1.30/><PV1.31/><PV1.32/><PV1.33/><PV1.34/><PV1.35/><PV1.36><PV1.36.1>30_ZZ_AA_discharge_disposition</PV1.36.1></PV1.36><PV1.37/><PV1.38/><PV1.39/><PV1.40/><PV1.41/><PV1.42/><PV1.43/><PV1.44><PV1.44.1>22_ZZ_AA_visit_date_time</PV1.44.1></PV1.44><PV1.45><PV1.45.1>31_ZZ_AA_disposition_date_time</PV1.45.1></PV1.45></PV1><OBX><OBX.1><OBX.1.1>1</OBX.1.1></OBX.1><OBX.2><OBX.2.1>AD</OBX.2.1></OBX.2><OBX.3><OBX.3.1>SS001</OBX.3.1><OBX.3.2>Treating Facility Identifier</OBX.3.2><OBX.3.3>PHINQUESTION</OBX.3.3></OBX.3><OBX.4/><OBX.5/><OBX.6/><OBX.7/><OBX.8/><OBX.9/><OBX.10/><OBX.11><OBX.11.1>C</OBX.11.1></OBX.11><OBX.12/><OBX.13/><OBX.14/><OBX.15><OBX.15.1>1_ZZ_AA_facility_identifier</OBX.15.1></OBX.15><OBX.16/><OBX.17/></OBX><OBX><OBX.1><OBX.1.1>2</OBX.1.1></OBX.1><OBX.2><OBX.2.1>HD</OBX.2.1></OBX.2><OBX.3><OBX.3.1>SS001</OBX.3.1><OBX.3.2>Treating Facility Identifier</OBX.3.2><OBX.3.3>PHINQUESTION</OBX.3.3></OBX.3><OBX.4/><OBX.5><OBX.5.1>2_ZZ_AA_facility_name</OBX.5.1></OBX.5><OBX.6/><OBX.7/><OBX.8/><OBX.9/><OBX.10/><OBX.11><OBX.11.1>X</OBX.11.1></OBX.11></OBX><OBX><OBX.1><OBX.1.1>3</OBX.1.1></OBX.1><OBX.2><OBX.2.1>XAD</OBX.2.1></OBX.2><OBX.3><OBX.3.1>SS002</OBX.3.1><OBX.3.2>Treating Facility Location</OBX.3.2></OBX.3><OBX.4/><OBX.5><OBX.5.1/><OBX.5.2>3_ZZ_AA_facility_number_and_street</OBX.5.2><OBX.5.3/><OBX.5.4/><OBX.5.5/><OBX.5.6>4_ZZ_AA_facility_city</OBX.5.6><OBX.5.7>6_ZZ_AA_facility_state</OBX.5.7><OBX.5.8>30341</OBX.5.8><OBX.5.9>USA</OBX.5.9><OBX.5.10>C</OBX.5.10><OBX.5.11/><OBX.5.12>5_ZZ_AA_facility_county</OBX.5.12></OBX.5><OBX.6/><OBX.7/><OBX.8/><OBX.9/><OBX.10/><OBX.11><OBX.11.1>X</OBX.11.1></OBX.11></OBX><OBX><OBX.1><OBX.1.1>4</OBX.1.1></OBX.1><OBX.2><OBX.2.1>CWE</OBX.2.1></OBX.2><OBX.3><OBX.3.1>SS003</OBX.3.1><OBX.3.2>Facility/Visit Type</OBX.3.2><OBX.3.3>PHINQUESTION</OBX.3.3></OBX.3><OBX.4/><OBX.5><OBX.5.1>7_ZZ_AA_facility_visit_type</OBX.5.1></OBX.5><OBX.6/><OBX.7/><OBX.8/><OBX.9/><OBX.10/><OBX.11/><OBX.12><OBX.12.1>X</OBX.12.1></OBX.12></OBX><OBX><OBX.1><OBX.1.1>5</OBX.1.1></OBX.1><OBX.2><OBX.2.1>NM</OBX.2.1></OBX.2><OBX.3><OBX.3.1>21612-7</OBX.3.1><OBX.3.2>AGE TIME PAITENT REPORTED</OBX.3.2><OBX.3.3>LN</OBX.3.3></OBX.3><OBX.4/><OBX.5><OBX.5.1>11_ZZ_AA_age</OBX.5.1></OBX.5><OBX.6><OBX.6.1>a</OBX.6.1><OBX.6.2>12_ZZ_AA_age_units</OBX.6.2><OBX.6.3>UCUM</OBX.6.3></OBX.6><OBX.7/><OBX.8/><OBX.9/><OBX.10/><OBX.11><OBX.11.1>X</OBX.11.1></OBX.11></OBX><OBX><OBX.1><OBX.1.1>6</OBX.1.1></OBX.1><OBX.2><OBX.2.1>TS</OBX.2.1></OBX.2><OBX.3><OBX.3.1>11368-8</OBX.3.1><OBX.3.2>ILLNESS OR INJURY ONSET DATE AND TIME:TMSTP:PT:PAITENT:QN</OBX.3.2><OBX.3.3>LN</OBX.3.3></OBX.3><OBX.4/><OBX.5><OBX.5.1>23_ZZ_AA_date_of_onset</OBX.5.1></OBX.5><OBX.6/><OBX.7/><OBX.8/><OBX.9/><OBX.10/><OBX.11><OBX.11.1>X</OBX.11.1></OBX.11></OBX><OBX><OBX.1><OBX.1.1>7</OBX.1.1></OBX.1><OBX.2><OBX.2.1>CWE</OBX.2.1></OBX.2><OBX.3><OBX.3.1>8661-1</OBX.3.1><OBX.3.2>CHIEF COMPLAINT:FIND:PT:PAITENT:NOM:REPORTED</OBX.3.2><OBX.3.3>LN</OBX.3.3></OBX.3><OBX.4/><OBX.5><OBX.5.1/><OBX.5.2/><OBX.5.3/><OBX.5.4/><OBX.5.5/><OBX.5.6/><OBX.5.7/><OBX.5.8/><OBX.5.9>25_ZZ_AA_chief_complaint</OBX.5.9></OBX.5><OBX.6/><OBX.7/><OBX.8/><OBX.9/><OBX.10/><OBX.11><OBX.11.1>X</OBX.11.1></OBX.11></OBX><OBX><OBX.1><OBX.1.1>8</OBX.1.1></OBX.1><OBX.2><OBX.2.1>TX</OBX.2.1></OBX.2><OBX.3><OBX.3.1>54094-8</OBX.3.1><OBX.3.2>TRIAGE NOTE:FIND:PT:EMERGENCY DEPARTMENT:DOC</OBX.3.2><OBX.3.3>LN</OBX.3.3></OBX.3><OBX.4/><OBX.5><OBX.5.1>26_ZZ_AA_triage_notes</OBX.5.1></OBX.5><OBX.6/><OBX.7/><OBX.8/><OBX.9/><OBX.10/><OBX.11><OBX.11.1>X</OBX.11.1></OBX.11></OBX><OBX><OBX.1><OBX.1.1>9</OBX.1.1></OBX.1><OBX.2><OBX.2.1>TX</OBX.2.1></OBX.2><OBX.3><OBX.3.1>44833-2</OBX.3.1><OBX.3.2>DIAGNOSIS:PRELIMINARY:IMP:PT:PAITENT:NOM:</OBX.3.2><OBX.3.3>LN</OBX.3.3></OBX.3><OBX.4/><OBX.5><OBX.5.1>28_ZZ_AA_clinical_impression</OBX.5.1></OBX.5><OBX.6/><OBX.7/><OBX.8/><OBX.9/><OBX.10/><OBX.11><OBX.11.1>X</OBX.11.1></OBX.11></OBX><OBX><OBX.1><OBX.1.1>10</OBX.1.1></OBX.1><OBX.2><OBX.2.1>NM</OBX.2.1></OBX.2><OBX.3><OBX.3.1>11289-6</OBX.3.1><OBX.3.2>BODY TEMPERATURE:TEMP:ENCTRFIRST:PAITENT:QN</OBX.3.2><OBX.3.3>LN</OBX.3.3></OBX.3><OBX.4/><OBX.5><OBX.5.1>32_ZZ_AA_initial_temperature</OBX.5.1></OBX.5><OBX.6><OBX.6.1/><OBX.6.2>xx_ZZ_AA_temperature_units</OBX.6.2><OBX.6.3>UCUM</OBX.6.3></OBX.6><OBX.7/><OBX.8/><OBX.9/><OBX.10/><OBX.11><OBX.11.1>X</OBX.11.1></OBX.11></OBX><OBX><OBX.1><OBX.1.1>11</OBX.1.1></OBX.1><OBX.2><OBX.2.1>NM</OBX.2.1></OBX.2><OBX.3><OBX.3.1>59408-5</OBX.3.1><OBX.3.2>OXYGEN SATURATION:MFR:PT:BLDA:QN:PULSE OXIMETRY</OBX.3.2><OBX.3.3>LN</OBX.3.3></OBX.3><OBX.4/><OBX.5><OBX.5.1>33_ZZ_AA_pulse_oximetry</OBX.5.1></OBX.5><OBX.6><OBX.6.1/><OBX.6.2>xx_ZZ_AA_pulse_oximetry_units</OBX.6.2></OBX.6><OBX.7/><OBX.8/><OBX.9/><OBX.10/><OBX.11><OBX.11.1>X</OBX.11.1></OBX.11></OBX><DG1><DG1.1><DG1.1.1>1</DG1.1.1></DG1.1><DG1.2/><DG1.3><DG1.3.1/><DG1.3.2>27_ZZ_AA_diagnoisis_injury_code</DG1.3.2></DG1.3><DG1.4/><DG1.5/><DG1.6><DG1.6.1>29_ZZ_AA_diagnosis_type</DG1.6.1></DG1.6></DG1></HL7Message>";

    public NoValidation getMyValidation(){

            NoValidation noValidation = new NoValidation();

            Iterator iter =
    noValidation.getPrimitiveRuleBindings().listIterator();

            while(iter.hasNext()){

                RuleBinding rule = (RuleBinding)iter.next();

                iter.remove();

            }

            return noValidation;

        }

    public String  convertXmlToPipeDelimited(String  xml) throws HL7Exception {
XMLParser parseXML = new DefaultXMLParser();
       parseXML.setValidationContext(getMyValidation());
       PipeParser pipeParser = new PipeParser();
       pipeParser.setValidationContext(getMyValidation());
       Message ER7Msg = parseXML.parse(xml);
       ER7Msg.setValidationContext(getMyValidation());
       String encodedMessage = pipeParser.encode(ER7Msg );

        return encodedMessage;




//        Parser        gp = new DefaultXMLParser();
//        Message       parsedMessage;
//
//        parsedMessage = gp.parse(xml);
//
//        return gp.encode(parsedMessage, "|");
//
    }

    public static void main(String[] args) throws IOException, HL7Exception {
        TestMe      tm = new TestMe();

        System.out.println(tm.convertXmlToPipeDelimited(testData));
    }
}
