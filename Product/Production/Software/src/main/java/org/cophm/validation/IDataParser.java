package org.cophm.validation;

//import ca.uhn.hl7v2.HL7Exception;

import org.jdom.Element;

import java.io.IOException;
import java.util.List;


/**
 * Created by
 * User: ralph
 * Date: 4/18/12
 * Time: 8:46 PM
 */
public interface IDataParser {

    public void  loadData(String  data) throws IOException;

    public String  getFieldValue(String  segmentName, String fieldNumber);

    public String  getFieldValue(List locationList) throws HL7ValidatorException;
}
