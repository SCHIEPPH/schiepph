package org.cophm.validation;

/**
 * Created by
 * User: ralph
 * Date: 4/10/12
 * Time: 5:02 PM
 */
public class ValidationResult {

    private String            errorCode;
    private String            errorMessage;
    private String            fieldName;
    private ErrorSeverity severity;

    public ValidationResult() {
    }

    public ValidationResult(String errorCode, String  errorMessage, String fieldName, ErrorSeverity severity) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
        this.fieldName = fieldName;
        this.severity = severity;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public ErrorSeverity getSeverity() {
        return severity;
    }

    public void setSeverity(ErrorSeverity severity) {
        this.severity = severity;
    }
}
