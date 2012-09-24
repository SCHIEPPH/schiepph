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

    public boolean  isSegmentPresent(String  segmentName);

    public String  getFieldValue(String  segmentName, String fieldNumber);

    public String  getFieldValue(List<Element> locationList) throws HL7ValidatorException;

    public List<String>  getAllFieldValues(List<Element> locationList) throws HL7ValidatorException;
}
