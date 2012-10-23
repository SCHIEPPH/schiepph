package org.cophm.util;

/**
 * Created by
 * User: ralph
 * Date: 4/3/12
 * Time: 11:07 AM
 */
public class Constants {

    public static final String      SCHIEPPH_PROPERTIES_DIR_ENVIRONMENT_VARIABLE_NAME = "SCHIEPPH_PROPERTIES_DIR";
    public static final String      SCHIEPPH_PROPERTIES_DIR_SYSTEM_VARIABLE_NAME = "schiepph.properties.dir";
    public static final String      SCHIEPPH_PROPERTIES_DIR_PROPERTIES_FILE_VARIABLE_NAME = "SCHIEPPH_PROPERTIES_DIR";

    public static final String      VERSION_231 = "2.3.1";
    public static final String      VERSION_251 = "2.5.1";

    public static final String      REPORT_PREFIX = "ValidationReport_";
    public static final String      REPORT_POSTFIX = ".rpt";
    public static final String      DATA_PREFIX = "HL7_Data_";
    public static final String      PIPE_DELIMITED_DATA_POSTFIX = ".hl7";
    public static final String      XML_DATA_POSTFIX = ".xml";
    public static final String      HL7_DATA_POSTFIX = ".hl7";

    public static final String      CONFIGURATION_ERROR_CODE = "SCHIEPPHConfigurationError";

    public static final String      OUTPUT_FILE_DATE_FORMAT = "yyyy-MM-dd_hh-mm-ss-SSS";
    public static final String      LOG_FILE_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss:SSS - ";
    public static final String      VALIDATED_DATA_FILE_PREFIX = "VALIDATED_DATA_FILE_PREFIX";
    public static final String      VALIDATED_DATA_FILE_EXTENSION = "VALIDATED_DATA_FILE_EXTENSION";
    public static final String      VALIDATED_DATA_OUTPUT_DIRECTORY = "VALIDATED_DATA_OUTPUT_DIRECTORY";

    public static final String      RESPONSE_EMAIL_SUBJECT = "SCHIEPPH NwHIN Direct Connect Reader Validation Results for :  ";

    public static final String      COULD_NOT_PARSE_DATA_TEXT = "Could not parse input data. ";

    public static final String      ADMIN_DIST_HOLD_DIR_PROPERTY_NAME = "admin_distribution_source_hold_directory";
    public static final String      ADMIN_DIST_REPORT_DIR_PROPERTY_NAME = "admin_distribution_source_report_directory";
    public static final String      ADMIN_DIST_VALIDATION_RULES_PROPERTY_NAME = "admin_distribution_source_validation_rules";

    private Constants() {
    }
}
